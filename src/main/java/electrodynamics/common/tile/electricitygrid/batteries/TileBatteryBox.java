package electrodynamics.common.tile.electricitygrid.batteries;

import org.jetbrains.annotations.Nullable;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerBatteryBox;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.prefab.item.ItemElectric;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.CapabilityUtils.FEInputDispatcher;
import electrodynamics.prefab.utilities.CapabilityUtils.FEOutputDispatcher;
import electrodynamics.prefab.utilities.ElectricityUtils;
import electrodynamics.prefab.utilities.object.CachedTileOutput;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class TileBatteryBox extends GenericTile implements IEnergyStorage {

    private final FEInputDispatcher inputDispatcher = new FEInputDispatcher(this);
    private final FEOutputDispatcher outputDispatcher = new FEOutputDispatcher(this);

    public final Property<Double> powerOutput;
    public final Property<Double> maxJoules;
    public Property<Double> currentCapacityMultiplier = property(new Property<>(PropertyTypes.DOUBLE, "currentCapacityMultiplier", 1.0));
    public Property<Double> currentVoltageMultiplier = property(new Property<>(PropertyTypes.DOUBLE, "currentVoltageMultiplier", 1.0));
    protected Property<Double> receiveLimitLeft;
    protected CachedTileOutput output;

    public final int baseVoltage;

    public static final BlockEntityUtils.MachineDirection OUTPUT = BlockEntityUtils.MachineDirection.BACK;
    public static final BlockEntityUtils.MachineDirection INPUT = BlockEntityUtils.MachineDirection.FRONT;

    public TileBatteryBox(BlockPos worldPosition, BlockState blockState) {
        this(ElectrodynamicsTiles.TILE_BATTERYBOX.get(), SubtypeMachine.batterybox, 120, 359.0 * ElectrodynamicsCapabilities.DEFAULT_VOLTAGE / 20.0, 10000000, worldPosition, blockState);
    }

    public TileBatteryBox(BlockEntityType<?> type, SubtypeMachine machine, int baseVoltage, double output, double max, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
        this.baseVoltage = baseVoltage;
        powerOutput = property(new Property<>(PropertyTypes.DOUBLE, "powerOutput", output));
        maxJoules = property(new Property<>(PropertyTypes.DOUBLE, "maxJoulesStored", max));
        receiveLimitLeft = property(new Property<>(PropertyTypes.DOUBLE, "receiveLimitLeft", output * currentCapacityMultiplier.get()));
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentInventory(this, InventoryBuilder.newInv().inputs(1).upgrades(3)).validUpgrades(ContainerBatteryBox.VALID_UPGRADES).valid((i, s, c) -> i == 0 ? s.getItem() instanceof ItemElectric : machineValidator().test(i, s, c)));
        addComponent(new ComponentContainerProvider(machine, this).createMenu((id, player) -> new ContainerBatteryBox(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
        addComponent(new ComponentElectrodynamic(this, true, true).voltage(baseVoltage).maxJoules(max).setInputDirections(INPUT).setOutputDirections(OUTPUT));

    }

    protected void tickServer(ComponentTickable tickable) {
        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
        Direction facing = getFacing();
        if (output == null) {
            output = new CachedTileOutput(level, worldPosition.relative(facing.getOpposite()));
        }
        if (tickable.getTicks() % 40 == 0) {
            output.update(worldPosition.relative(facing.getOpposite()));
        }
        if (electro.getJoulesStored() > 0 && output.valid()) {

            electro.joules(electro.getJoulesStored() - ElectricityUtils.receivePower(output.getSafe(), facing, TransferPack.joulesVoltage(Math.min(electro.getJoulesStored(), powerOutput.get() * currentCapacityMultiplier.get()), electro.getVoltage()), false).getJoules());
        }
        if (electro.getJoulesStored() > electro.getMaxJoulesStored()) {
            electro.joules(electro.getMaxJoulesStored());
        }
        electro.drainElectricItem(0);
    }

    @Nullable
    public IEnergyStorage getFECapability(@Nullable Direction side) {
        if (side == null) {
            return null;
        }

        Direction facing = getFacing();

        if(side == facing){
            return inputDispatcher;
        } else if (side == facing.getOpposite()){
            return outputDispatcher;
        } else {
            return null;
        }

    }

    // this is changed so all the battery boxes can convert FE to joules, regardless of voltage
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        int receive = (int) Math.min(maxReceive, powerOutput.get() * currentCapacityMultiplier.get());

        int accepted = Math.min(receive, (int) (electro.getMaxJoulesStored() - electro.getJoulesStored()));

        if (!simulate) {
            electro.joules(electro.getJoulesStored() + accepted);
        }

        return accepted;
    }

    // we still mandate 120V for all FE cables here though
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        int extract = (int) Math.min(maxExtract, powerOutput.get() * currentCapacityMultiplier.get());

        int taken = Math.min(extract, (int) electro.getJoulesStored());

        if (!simulate) {

            electro.joules(electro.getJoulesStored() - taken);

            if (electro.getVoltage() > ElectrodynamicsCapabilities.DEFAULT_VOLTAGE) {
                electro.overVoltage(TransferPack.joulesVoltage(taken, electro.getVoltage()));
            }

        }

        return taken;
    }

    @Override
    public int getEnergyStored() {
        return (int) Math.min(Integer.MAX_VALUE, this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getJoulesStored());
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) Math.min(Integer.MAX_VALUE, this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getMaxJoulesStored());
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public void onInventoryChange(ComponentInventory inv, int slot) {
        super.onInventoryChange(inv, slot);
        if (inv.getUpgradeContents().size() > 0 && (slot >= inv.getUpgradeSlotStartIndex() || slot == -1)) {
            ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

            double capacityMultiplier = 1.0;
            double voltageMultiplier = 1.0;

            for (ItemStack stack : inv.getUpgradeContents()) {
                if (!stack.isEmpty() && stack.getItem() instanceof ItemUpgrade upgrade && upgrade.subtype.isEmpty) {
                    for (int i = 0; i < stack.getCount(); i++) {
                        if (upgrade.subtype == SubtypeItemUpgrade.basiccapacity) {
                            capacityMultiplier = Math.min(capacityMultiplier * 1.5, Math.pow(1.5, 3));
                            voltageMultiplier = Math.min(voltageMultiplier * 2, 2);
                        } else if (upgrade.subtype == SubtypeItemUpgrade.advancedcapacity) {
                            capacityMultiplier = Math.min(capacityMultiplier * 2.25, Math.pow(2.25, 3));
                            voltageMultiplier = Math.min(voltageMultiplier * 4, 4);
                        }
                    }
                }
            }

            currentCapacityMultiplier.set(capacityMultiplier);
            currentVoltageMultiplier.set(voltageMultiplier);

            receiveLimitLeft.set(powerOutput.get() * currentCapacityMultiplier.get());

            electro.maxJoules(maxJoules.get() * currentCapacityMultiplier.get());
            electro.voltage(baseVoltage * currentVoltageMultiplier.get());
        }
    }

    @Override
    public int getComparatorSignal() {
        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
        return (int) ((electro.getJoulesStored() / Math.max(1, electro.getMaxJoulesStored())) * 15.0);
    }

}