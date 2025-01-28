package electrodynamics.common.tile.electricitygrid;

import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic.LoadProfile;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
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

public class TileRelay extends GenericTile {

    private boolean recievedRedstoneSignal = false;

    private boolean isLocked = false;

    public static final BlockEntityUtils.MachineDirection OUTPUT = BlockEntityUtils.MachineDirection.FRONT;
    public static final BlockEntityUtils.MachineDirection INPUT = BlockEntityUtils.MachineDirection.BACK;

    public TileRelay(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_RELAY.get(), worldPos, blockState);
        addComponent(new ComponentElectrodynamic(this, true, true).receivePower(this::receivePower).getConnectedLoad(this::getConnectedLoad).setOutputDirections(OUTPUT).setInputDirections(INPUT)
                //
                .voltage(-1).getAmpacity(this::getAmpacity).getMinimumVoltage(this::getMinimumVoltage));
    }

    public TransferPack receivePower(TransferPack transfer, boolean debug) {
        if (recievedRedstoneSignal || isLocked) {
            return TransferPack.EMPTY;
        }
        Direction output = BlockEntityUtils.getRelativeSide(getFacing(), OUTPUT.mappedDir);

        BlockEntity tile = level.getBlockEntity(worldPosition.relative(output));

        if (tile == null) {
            return TransferPack.EMPTY;
        }

        isLocked = true;

        ICapabilityElectrodynamic electro = tile.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, tile.getBlockPos(), tile.getBlockState(), tile, output.getOpposite());

        if (electro == null) {
            isLocked = false;
            return TransferPack.EMPTY;
        }

        TransferPack accepted = electro.receivePower(TransferPack.joulesVoltage(transfer.getJoules() * Constants.RELAY_EFFICIENCY, transfer.getVoltage()), debug);

        isLocked = false;

        return TransferPack.joulesVoltage(accepted.getJoules() / Constants.RELAY_EFFICIENCY, accepted.getVoltage());
    }

    public TransferPack getConnectedLoad(LoadProfile lastEnergy, Direction dir) {

        if (recievedRedstoneSignal || isLocked) {
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

        LoadProfile transformed = new LoadProfile(TransferPack.joulesVoltage(lastEnergy.lastUsage().getJoules() * Constants.RELAY_EFFICIENCY, lastEnergy.lastUsage().getVoltage()), TransferPack.joulesVoltage(lastEnergy.maximumAvailable().getJoules() * Constants.RELAY_EFFICIENCY, lastEnergy.maximumAvailable().getVoltage()));

        isLocked = true;

        ICapabilityElectrodynamic electro = tile.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, tile.getBlockPos(), tile.getBlockState(), tile, dir);

        if (electro == null) {
            isLocked = false;
            return TransferPack.EMPTY;
        }

        TransferPack returner = electro.getConnectedLoad(transformed, dir);

        isLocked = false;
        return TransferPack.joulesVoltage(returner.getJoules() / Constants.RELAY_EFFICIENCY, returner.getVoltage());

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
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.putBoolean("hasredstonesignal", recievedRedstoneSignal);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        recievedRedstoneSignal = compound.getBoolean("hasredstonesignal");
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
