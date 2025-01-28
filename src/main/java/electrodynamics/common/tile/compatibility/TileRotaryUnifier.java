package electrodynamics.common.tile.compatibility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import electrodynamics.Electrodynamics;
import electrodynamics.api.References;
import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.PropertyGasTank;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.inventory.container.tile.ContainerRotaryUnifier;
import electrodynamics.common.settings.Constants;
import electrodynamics.compatibility.mekanism.MekanismHandler;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.sound.utils.ITickableSound;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.types.GenericGasTile;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsSounds;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.fml.ModList;

public class TileRotaryUnifier extends GenericGasTile implements ITickableSound {

    public static final int MAX_GAS_AMOUNT = 1000;
    public static final int MAX_CHEM_AMOUNT = 1000;

    private final boolean mekIsLoaded;
    public final int chemStackIndex;
    public final Property<Boolean> conversionIsFlipped = property(new Property<>(PropertyTypes.BOOLEAN, "conversionisflipped", false));
    public final PropertyGasTank gasTank = new PropertyGasTank(this, "gastank", MAX_GAS_AMOUNT, 1000, 1000);

    public Direction gasIO;
    public Direction chemicalIO;

    private boolean playing = false;

    public TileRotaryUnifier(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_ROTARYUNIFIER.get(), worldPos, blockState);
        addComponent(new ComponentTickable(this).tickClient(this::tickClient));
        addComponent(new ComponentElectrodynamic(this, false, true).maxJoules(Constants.ROTARY_UNIFIER_USAGE * 20).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 4).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM));
        addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().upgrades(3)).validUpgrades(ContainerRotaryUnifier.VALID_UPGRADES).valid(machineValidator()));
        addComponent(new ComponentProcessor(this).usage(Constants.ROTARY_UNIFIER_USAGE).canProcess(this::canProcess).process(this::process));
        addComponent(new ComponentContainerProvider("container.rotaryunifier", this).createMenu((id, player) -> new ContainerRotaryUnifier(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
        gasIO = BlockEntityUtils.getRelativeSide(getFacing(), BlockEntityUtils.MachineDirection.RIGHT.mappedDir);
        chemicalIO = BlockEntityUtils.getRelativeSide(getFacing(), BlockEntityUtils.MachineDirection.LEFT.mappedDir);
        mekIsLoaded = ModList.get().isLoaded(References.MEKANISM_ID);
        if(mekIsLoaded) {
            chemStackIndex = MekanismHandler.addProperty(this);
            gasTank.setValidator(MekanismHandler.getTankPredicate());
        } else {
            chemStackIndex = 0;
            gasTank.setValidator(stack -> false);
        }
    }

    private void tickClient(ComponentTickable tickable) {

        ComponentProcessor proc = getComponent(IComponentType.Processor);

        if(!proc.isActive()) {
            return;
        }

        if (!playing) {
            playing = true;
            SoundBarrierMethods.playTileSound(ElectrodynamicsSounds.SOUND_COMPRESSORRUNNING.get(), this, true);
        }

        Direction direction = getFacing();
        double axisShift = 0;
        double yShift = 0;

        axisShift = Electrodynamics.RANDOM.nextDouble(0.64) + 0.18;
        yShift = Electrodynamics.RANDOM.nextDouble(0.57) + 0.25;

        double xShift = direction.getAxis() == Direction.Axis.X ? direction.getStepX() * (direction.getStepX() == -1 ? -1 : 0) : axisShift;
        double zShift = direction.getAxis() == Direction.Axis.Z ? direction.getStepZ() * (direction.getStepZ() == -1 ? -1 : 0) : axisShift;

        level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + xShift, worldPosition.getY() + yShift, worldPosition.getZ() + zShift, 0.0D, 0.0D, 0.0D);

        xShift = direction.getAxis() == Direction.Axis.X ? direction.getStepX() * (direction.getStepX() == -1 ? 0 : 1) : axisShift;
        zShift = direction.getAxis() == Direction.Axis.Z ? direction.getStepZ() * (direction.getStepZ() == -1 ? 0 : 1) : axisShift;

        level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + xShift, worldPosition.getY() + yShift, worldPosition.getZ() + zShift, 0.0D, 0.0D, 0.0D);
    }

    private void process(ComponentProcessor componentProcessor) {
        if(mekIsLoaded) {
            MekanismHandler.process(this, componentProcessor);
        }

    }

    private boolean canProcess(ComponentProcessor componentProcessor) {
        boolean update = false;
        if(mekIsLoaded) {
            update = MekanismHandler.canProcess(this, componentProcessor);
        }
        if (BlockEntityUtils.isLit(this) ^ update) {
            BlockEntityUtils.updateLit(this, update);
        }
        return update;
    }

    @Override
    public @Nullable IGasHandler getGasHandlerCapability(@Nullable Direction side) {
        if(side == null || side != gasIO) {
            return null;
        }
        return new IGasHandler() {
            @Override
            public int getTanks() {
                return 1;
            }

            @Override
            public GasStack getGasInTank(int tank) {
                return gasTank.getGasInTank(0);
            }

            @Override
            public int getTankCapacity(int tank) {
                return gasTank.getTankCapacity(0);
            }

            @Override
            public int getTankMaxTemperature(int tank) {
                return gasTank.getTankMaxTemperature(0);
            }

            @Override
            public int getTankMaxPressure(int tank) {
                return gasTank.getTankMaxPressure(0);
            }

            @Override
            public boolean isGasValid(int tank, @NotNull GasStack gas) {
                return gasTank.isGasValid(gas);
            }

            @Override
            public int fill(GasStack gas, GasAction action) {
                return conversionIsFlipped.get() ? 0 : gasTank.fill(gas, action);
            }

            @Override
            public GasStack drain(GasStack gas, GasAction action) {
                return conversionIsFlipped.get() ? gasTank.drain(gas, action) : GasStack.EMPTY;
            }

            @Override
            public GasStack drain(int maxFill, GasAction action) {
                return conversionIsFlipped.get() ? gasTank.drain(maxFill, action) : GasStack.EMPTY;
            }

            @Override
            public int heat(int tank, int deltaTemperature, GasAction action) {
                return gasTank.heat(tank, deltaTemperature, action);
            }

            @Override
            public int bringPressureTo(int tank, int atm, GasAction action) {
                return gasTank.bringPressureTo(tank, atm, action);
            }
        };
    }

    @Override
    public void onBlockStateUpdate(BlockState oldState, BlockState newState) {
        super.onBlockStateUpdate(oldState, newState);
        if (!level.isClientSide() && oldState.hasProperty(ElectrodynamicsBlockStates.FACING) && newState.hasProperty(ElectrodynamicsBlockStates.FACING) && oldState.getValue(ElectrodynamicsBlockStates.FACING) != newState.getValue(ElectrodynamicsBlockStates.FACING)) {
            gasIO = BlockEntityUtils.getRelativeSide(getFacing(), BlockEntityUtils.MachineDirection.RIGHT.mappedDir);
            chemicalIO = BlockEntityUtils.getRelativeSide(getFacing(), BlockEntityUtils.MachineDirection.RIGHT.mappedDir);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putInt("gasio", gasIO.ordinal());
        compound.putInt("chemicalio", chemicalIO.ordinal());
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        gasIO = Direction.values()[compound.getInt("gasio")];
        chemicalIO = Direction.values()[compound.getInt("chemicalio")];
    }

    @Override
    public InteractionResult useWithoutItem(Player player, BlockHitResult hit) {
        if(mekIsLoaded) {
            return super.useWithoutItem(player, hit);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void setNotPlaying() {
        playing = false;
    }

    @Override
    public boolean shouldPlaySound() {
        return this.<ComponentProcessor>getComponent(IComponentType.Processor).isActive();
    }
}
