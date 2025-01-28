package electrodynamics.common.tile.electricitygrid;

import electrodynamics.Electrodynamics;
import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic.LoadProfile;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileCircuitBreaker extends GenericTile {

    public static final int TRIP_CURVE = 20;

    private boolean recievedRedstoneSignal = false;
    private boolean tripped = false;

    private int tripCurveTimer = 0;

    private boolean isLocked = false;

    public static final BlockEntityUtils.MachineDirection OUTPUT = BlockEntityUtils.MachineDirection.FRONT;
    public static final BlockEntityUtils.MachineDirection INPUT = BlockEntityUtils.MachineDirection.BACK;

    public TileCircuitBreaker(BlockPos worldPosition, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_CIRCUITBREAKER.get(), worldPosition, blockState);
        addComponent(new ComponentElectrodynamic(this, true, true).receivePower(this::receivePower).getConnectedLoad(this::getConnectedLoad).setOutputDirections(OUTPUT).setInputDirections(INPUT)
                //
                .voltage(-1).getAmpacity(this::getAmpacity).getMinimumVoltage(this::getMinimumVoltage));
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
    }

    public void tickServer(ComponentTickable tickable) {
        if (tripCurveTimer > 0) {
            tripCurveTimer--;
            return;
        }
        if (tripped) {
            level.playSound(null, getBlockPos(), SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS);
        }
        tripped = false;
    }

    // will not transfer power if is recieving redstone signal, voltage exceeds recieving end voltage, or if current exceeds
    // recieving
    // end current if recieving end is wire
    public TransferPack receivePower(TransferPack transfer, boolean debug) {

        if (recievedRedstoneSignal || isLocked || tripped) {
            return TransferPack.EMPTY;
        }

        Direction output = BlockEntityUtils.getRelativeSide(getFacing(), OUTPUT.mappedDir);

        BlockEntity tile = level.getBlockEntity(worldPosition.relative(output));

        if (tile == null) {
            return TransferPack.EMPTY;
        }

        isLocked = true;

        ICapabilityElectrodynamic electro = level.getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, tile.getBlockPos(), tile.getBlockState(), tile, output.getOpposite());

        if (electro == null) {
            isLocked = false;

            return TransferPack.EMPTY;
        }

        if (electro.getMinimumVoltage() > -1 && electro.getMinimumVoltage() < transfer.getVoltage()) {
            tripped = true;
            tripCurveTimer = TRIP_CURVE;
            level.playSound(null, getBlockPos(), SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS);

            isLocked = false;

            return transfer;
        }

        if (electro.getAmpacity() > 0 && electro.getAmpacity() < transfer.getAmpsInTicks()) {
            Electrodynamics.LOGGER.info("tripped");
            tripped = true;
            tripCurveTimer = TRIP_CURVE;
            level.playSound(null, getBlockPos(), SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS);

            isLocked = false;

            return transfer;

        }

        if (debug) {

            TransferPack accepted = electro.receivePower(TransferPack.joulesVoltage(transfer.getJoules() * Constants.CIRCUITBREAKER_EFFICIENCY, transfer.getVoltage()), debug);

            isLocked = false;

            if (accepted.getJoules() > 0) {

                return TransferPack.joulesVoltage(accepted.getJoules() / Constants.CIRCUITBREAKER_EFFICIENCY, accepted.getVoltage());

            }
            return TransferPack.EMPTY;

        }

        TransferPack accepted = electro.receivePower(TransferPack.joulesVoltage(transfer.getJoules() * Constants.CIRCUITBREAKER_EFFICIENCY, transfer.getVoltage()), debug);

        isLocked = false;

        return TransferPack.joulesVoltage(accepted.getJoules() / Constants.CIRCUITBREAKER_EFFICIENCY, accepted.getVoltage());
    }

    public TransferPack getConnectedLoad(LoadProfile lastEnergy, Direction dir) {

        if (recievedRedstoneSignal || isLocked || tripped) {
            return TransferPack.EMPTY;
        }

        Direction output = BlockEntityUtils.getRelativeSide(getFacing(), OUTPUT.mappedDir);

        if (dir != output.getOpposite()) {
            return TransferPack.EMPTY;
        }

        BlockEntity tile = level.getBlockEntity(worldPosition.relative(output));

        if (tile == null) {
            return TransferPack.EMPTY;
        }

        isLocked = true;

        ICapabilityElectrodynamic electro = level.getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, tile.getBlockPos(), tile.getBlockState(), tile, output.getOpposite());

        if (electro == null) {
            isLocked = false;
            return TransferPack.EMPTY;
        }

        if (electro.getMinimumVoltage() > -1 && (electro.getMinimumVoltage() < lastEnergy.lastUsage().getVoltage() || electro.getMinimumVoltage() < lastEnergy.maximumAvailable().getVoltage())) {
            tripped = true;
            tripCurveTimer = TRIP_CURVE;
            level.playSound(null, getBlockPos(), SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS);

            isLocked = false;

            return TransferPack.EMPTY;
        }

        if (electro.getAmpacity() > 0 && electro.getAmpacity() <= lastEnergy.lastUsage().getAmpsInTicks()) {

            tripped = true;
            tripCurveTimer = TRIP_CURVE;
            level.playSound(null, getBlockPos(), SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS);

            isLocked = false;

            return TransferPack.EMPTY;

        }

        LoadProfile transformed = new LoadProfile(TransferPack.joulesVoltage(lastEnergy.lastUsage().getJoules() * Constants.CIRCUITBREAKER_EFFICIENCY, lastEnergy.lastUsage().getVoltage()), TransferPack.joulesVoltage(lastEnergy.maximumAvailable().getJoules() * Constants.CIRCUITBREAKER_EFFICIENCY, lastEnergy.maximumAvailable().getVoltage()));

        isLocked = true;

        TransferPack returner = electro.getConnectedLoad(transformed, dir);

        isLocked = false;

        return TransferPack.joulesVoltage(returner.getJoules() / Constants.CIRCUITBREAKER_EFFICIENCY, returner.getVoltage());
    }

    public double getMinimumVoltage() {
        Direction facing = getFacing();
        if (isLocked) {
            return 0;
        }
        BlockEntity output = level.getBlockEntity(worldPosition.relative(facing));
        if (output == null) {
            return -1;
        }
        isLocked = true;

        ICapabilityElectrodynamic electro = output.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, output.getBlockPos(), output.getBlockState(), output, facing.getOpposite());

        if (electro == null) {
            isLocked = false;
            return -1;
        }

        double minimumVoltage = electro.getMinimumVoltage();

        isLocked = false;
        return minimumVoltage;
    }

    public double getAmpacity() {
        Direction facing = getFacing();
        if (isLocked) {
            return 0;
        }
        BlockEntity output = level.getBlockEntity(worldPosition.relative(facing));
        if (output == null) {
            return -1;
        }
        isLocked = true;

        ICapabilityElectrodynamic electro = output.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, output.getBlockPos(), output.getBlockState(), output, facing.getOpposite());

        if (electro == null) {
            isLocked = false;
            return -1;
        }
        double ampacity = electro.getAmpacity();
        isLocked = false;
        return ampacity;
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putBoolean("hasredstonesignal", recievedRedstoneSignal);
        compound.putBoolean("tripped", tripped);
        compound.putInt("timer", tripCurveTimer);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        recievedRedstoneSignal = compound.getBoolean("hasredstonesignal");
        tripped = compound.getBoolean("tripped");
        tripCurveTimer = compound.getInt("timer");
    }

    @Override
    public void onNeightborChanged(BlockPos neighbor, boolean blockStateTrigger) {
        if (level.isClientSide) {
            return;
        }
        recievedRedstoneSignal = level.hasNeighborSignal(getBlockPos());
        if (BlockEntityUtils.isLit(this) ^ recievedRedstoneSignal) {
            BlockEntityUtils.updateLit(this, recievedRedstoneSignal);
            if (recievedRedstoneSignal) {
                level.playSound(null, getBlockPos(), SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS);
            } else {
                level.playSound(null, getBlockPos(), SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS);
            }
        }
    }

    @Override
    public void onPlace(BlockState oldState, boolean isMoving) {
        super.onPlace(oldState, isMoving);
        if (level.isClientSide) {
            return;
        }
        recievedRedstoneSignal = level.hasNeighborSignal(getBlockPos());
        if (BlockEntityUtils.isLit(this) ^ recievedRedstoneSignal) {
            BlockEntityUtils.updateLit(this, recievedRedstoneSignal);
            if (recievedRedstoneSignal) {
                level.playSound(null, getBlockPos(), SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS);
            }
        }
    }

}
