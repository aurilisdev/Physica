package electrodynamics.common.tile.machines.charger;

import electrodynamics.api.item.IItemElectric;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerChargerGeneric;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public abstract class GenericTileCharger extends GenericTile {

    private static final int BATTERY_COUNT = 3;

    protected GenericTileCharger(BlockEntityType<?> typeIn, int voltageMultiplier, SubtypeMachine machine, BlockPos worldPosition, BlockState blockState) {
        super(typeIn, worldPosition, blockState);
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BACK).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * voltageMultiplier).maxJoules(2000.0 * voltageMultiplier));
        addComponent(new ComponentInventory(this, InventoryBuilder.newInv().inputs(BATTERY_COUNT + 1).outputs(1)).valid(machineValidator()).setDirectionsBySlot(0, BlockEntityUtils.MachineDirection.RIGHT, BlockEntityUtils.MachineDirection.TOP).setDirectionsBySlot(4, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.BOTTOM));
        addComponent(new ComponentContainerProvider(machine, this).createMenu((id, player) -> new ContainerChargerGeneric(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));

    }

    public void tickServer(ComponentTickable tickable) {

        ComponentInventory inventory = getComponent(IComponentType.Inventory);
        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        ItemStack itemInput = inventory.getItem(0);

		if(itemInput.isEmpty()) {
			return;
		}

        if (itemInput.getItem() instanceof IItemElectric electricItem) {

            if (inventory.inputs() > 1 && drainBatterySlots(inventory, electro)) {
                return;
            }

            double room = electricItem.getMaximumCapacity(itemInput) - electricItem.getJoulesStored(itemInput);

			if(electro.getJoulesStored() <= 0 || room <= 0) {
				return;
			}

			double recieveVoltage = electricItem.getElectricProperties().receive.getVoltage();
			double machineVoltage = electro.getVoltage();

			if (machineVoltage > recieveVoltage) {

				electricItem.overVoltage(TransferPack.joulesVoltage(electricItem.getElectricProperties().receive.getJoules(), machineVoltage));
				level.setBlockAndUpdate(worldPosition, Blocks.AIR.defaultBlockState());
				level.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 2f, ExplosionInteraction.BLOCK);

			} else if (machineVoltage == recieveVoltage) {

				electro.joules(electro.getJoulesStored() - electricItem.receivePower(itemInput, TransferPack.joulesVoltage(electro.getJoulesStored(), machineVoltage), false).getJoules());

			} else {

				float underVoltRatio = (float) ((float) machineVoltage / recieveVoltage);
				float itemStoredRatio = (float) ((float) electricItem.getJoulesStored(itemInput) / electricItem.getMaximumCapacity(itemInput));

				float x = Math.abs(itemStoredRatio / (itemStoredRatio - underVoltRatio + 0.00000001F/* ensures it's never zero */));
				float reductionCoef = getRationalFunctionValue(x);

				if (itemStoredRatio >= underVoltRatio) {

					electricItem.extractPower(itemInput, electro.getJoulesStored() * reductionCoef, false);

				} else {

					electro.joules(electro.getJoulesStored() - electricItem.receivePower(itemInput, TransferPack.joulesVoltage(electro.getJoulesStored() * reductionCoef, recieveVoltage), false).getJoules());

				}
			}

			if (electricItem.getJoulesStored(itemInput) == electricItem.getMaximumCapacity(itemInput) && inventory.getItem(4).isEmpty()) {

				inventory.setItem(4, inventory.getItem(0).copy());
				inventory.getItem(0).shrink(1);

			}

			inventory.setChanged();

        } else if (itemInput.getCapability(Capabilities.EnergyStorage.ITEM) instanceof IEnergyStorage storage) {

			if (inventory.inputs() > 1 && drainBatterySlots(inventory, electro) || !storage.canReceive()) {
				return;
			}

			int room = storage.receiveEnergy(Integer.MAX_VALUE, true);

			if(electro.getJoulesStored() <= 0 || room <= 0) {
				return;
			}

			double machineVoltage = electro.getVoltage();

			if (machineVoltage > ElectrodynamicsCapabilities.DEFAULT_VOLTAGE) {

				level.setBlockAndUpdate(worldPosition, Blocks.AIR.defaultBlockState());
				level.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 2f, ExplosionInteraction.BLOCK);
				return;

			}

			int accepted = (int) Math.min(electro.getJoulesStored(), room);

			storage.receiveEnergy(accepted, false);

			electro.joules(electro.getJoulesStored() - accepted);

			inventory.setChanged();

		}

    }

    // to simulate undervolting a chargeable object
    private static float getRationalFunctionValue(float x) {
        if (x >= 100.0F) {
            return 0.0F;
        }
        if (x <= 1.0F) {
            return 1.0F;
        }
        return 1 / x;
    }

    private boolean drainBatterySlots(ComponentInventory inv, ComponentElectrodynamic electro) {
        double machineVoltage = electro.getVoltage();
        double battVoltage = 0;
        for (int i = 0; i < BATTERY_COUNT; i++) {
            ItemStack battery = inv.getItem(i + 1);
            if (!battery.isEmpty() && battery.getItem() instanceof IItemElectric electricItem) {
                battVoltage = electricItem.getElectricProperties().receive.getVoltage();
                if (battVoltage < machineVoltage) {
                    inv.setItem(i + 1, new ItemStack(ElectrodynamicsItems.ITEM_SLAG.get()).copy());
                    getLevel().playSound(null, getBlockPos(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 1F, 1F);
                } else if (battVoltage > machineVoltage) {
                    electro.overVoltage(TransferPack.joulesVoltage(electro.getJoulesStored(), battVoltage));
                    return true;
                } else if (electro.getMaxJoulesStored() - electro.getJoulesStored() > 0) {
                    electro.joules(electro.getJoulesStored() + electricItem.extractPower(battery, Constants.CHARGER_USAGE_PER_TICK, false).getJoules());
                }
            }
        }
        return false;
    }

}
