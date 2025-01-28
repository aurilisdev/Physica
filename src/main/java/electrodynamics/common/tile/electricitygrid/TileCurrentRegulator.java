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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileCurrentRegulator extends GenericTile {

    private boolean isLocked = false;

    public static final BlockEntityUtils.MachineDirection OUTPUT = BlockEntityUtils.MachineDirection.FRONT;
    public static final BlockEntityUtils.MachineDirection INPUT = BlockEntityUtils.MachineDirection.BACK;

    public TileCurrentRegulator(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_CURRENTREGULATOR.get(), worldPos, blockState);
        addComponent(new ComponentElectrodynamic(this, true, true).receivePower(this::receivePower).getConnectedLoad(this::getConnectedLoad).setOutputDirections(OUTPUT).setInputDirections(INPUT).voltage(-1)
                //
                .getAmpacity(this::getAmpacity).getMinimumVoltage(this::getMinimumVoltage));
    }

    public TransferPack receivePower(TransferPack transfer, boolean debug) {

        if (isLocked) {
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

        TransferPack accepted = electro.receivePower(TransferPack.joulesVoltage(transfer.getJoules() * Constants.CURRENTREGULATOR_EFFICIENCY, transfer.getVoltage()), debug);

        isLocked = false;

        TransferPack adjusted = TransferPack.joulesVoltage(accepted.getJoules() / Constants.CURRENTREGULATOR_EFFICIENCY, accepted.getVoltage());

        double ampacityInTicks = electro.getAmpacity();

        if (ampacityInTicks < 0) {
            return adjusted;
        }

        double currentInTicks = adjusted.getAmpsInTicks();

        if (currentInTicks > ampacityInTicks) {

            adjusted = TransferPack.ampsVoltage(ampacityInTicks, adjusted.getVoltage());

        }

        return adjusted;
    }

    public TransferPack getConnectedLoad(LoadProfile lastEnergy, Direction dir) {

        if (isLocked) {
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

        ICapabilityElectrodynamic electro = tile.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, tile.getBlockPos(), tile.getBlockState(), tile, output.getOpposite());

        if (electro == null) {
            isLocked = false;
            return TransferPack.EMPTY;
        }

        LoadProfile transformed = new LoadProfile(TransferPack.joulesVoltage(lastEnergy.lastUsage().getJoules() * Constants.CIRCUITBREAKER_EFFICIENCY, lastEnergy.lastUsage().getVoltage()), TransferPack.joulesVoltage(lastEnergy.maximumAvailable().getJoules() * Constants.CIRCUITBREAKER_EFFICIENCY, lastEnergy.maximumAvailable().getVoltage()));

        TransferPack returner = electro.getConnectedLoad(transformed, dir);

        isLocked = false;

        TransferPack adjusted = TransferPack.joulesVoltage(returner.getJoules() / Constants.CIRCUITBREAKER_EFFICIENCY, returner.getVoltage());

        double ampacityInTicks = electro.getAmpacity();

        if (ampacityInTicks < 0) {
            return adjusted;
        }

        double currentInTicks = adjusted.getAmpsInTicks();

        if (currentInTicks > ampacityInTicks) {

            adjusted = TransferPack.ampsVoltage(ampacityInTicks, adjusted.getVoltage());

        }

        return adjusted;
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

}
