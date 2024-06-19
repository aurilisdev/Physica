package electrodynamics.common.tile.pipelines.gas.gastransformer;

import org.jetbrains.annotations.NotNull;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.common.tile.machines.quarry.TileQuarry;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.CapabilityInputType;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.registers.ElectrodynamicsBlockTypes;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class TileGasTransformerSideBlock extends GenericTile {

    private BlockPos ownerPos = TileQuarry.OUT_OF_REACH;
    private boolean isLeft = false;

    public TileGasTransformerSideBlock(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsBlockTypes.TILE_COMPRESSOR_SIDE.get(), worldPos, blockState);
    }

    public void setOwnerPos(BlockPos ownerPos) {
        this.ownerPos = ownerPos;
    }

    public void setIsLeft() {
        isLeft = true;
    }

    public boolean isLeft() {
        return isLeft;
    }

    @Override
    public void onPlace(BlockState oldState, boolean isMoving) {
        super.onPlace(oldState, isMoving);
        if (level.isClientSide) {
            return;
        }
        updateTankCount();
    }

    public void updateTankCount() {
        BlockPos abovePos = getBlockPos().above();
        BlockState aboveState = getLevel().getBlockState(abovePos);
        BlockEntity aboveTile;
        int tankCount = 0;
        for (int i = 0; i < TileGasTransformerAddonTank.MAX_ADDON_TANKS; i++) {
            if (!aboveState.is(ElectrodynamicsBlocks.blockGasTransformerAddonTank)) {
                break;
            }
            aboveTile = getLevel().getBlockEntity(abovePos);
            if ((aboveTile == null) || !(aboveTile instanceof TileGasTransformerAddonTank tank)) {
                break;
            }
            abovePos = abovePos.above();
            aboveState = getLevel().getBlockState(abovePos);
            tank.setOwnerPos(getBlockPos());
            tankCount++;
        }
        BlockEntity owner = getLevel().getBlockEntity(ownerPos);
        if (owner != null && owner instanceof GenericTileGasTransformer compressor) {
            compressor.updateAddonTanks(tankCount, isLeft);
        }
    }

    @Override
    public void onBlockDestroyed() {
        if (level.isClientSide) {
            return;
        }
        if (getLevel().getBlockEntity(ownerPos) instanceof GenericTileGasTransformer compressor) {
            getLevel().destroyBlock(ownerPos, !compressor.hasBeenDestroyed);
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("owner", NbtUtils.writeBlockPos(ownerPos));
        compound.putBoolean("isleft", isLeft);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        ownerPos = NbtUtils.readBlockPos(compound.getCompound("owner"));
        isLeft = compound.getBoolean("isleft");
    }

    @Override
    public @org.jetbrains.annotations.Nullable IFluidHandler getFluidHandlerCapability(@org.jetbrains.annotations.Nullable Direction side) {
        if (ownerPos == null || ownerPos.equals(TileQuarry.OUT_OF_REACH)) {
            return null;
        }

        BlockEntity owner = getLevel().getBlockEntity(ownerPos);

        if (owner instanceof GenericTileGasTransformer compressor && compressor.hasComponent(IComponentType.FluidHandler)) {

            if (isLeft) {
                return compressor.<IComponentFluidHandler>getComponent(IComponentType.FluidHandler).getCapability(side, CapabilityInputType.INPUT);
            }
            return compressor.<IComponentFluidHandler>getComponent(IComponentType.FluidHandler).getCapability(side, CapabilityInputType.OUTPUT);

        }
        return null;
    }

    @Override
    public @org.jetbrains.annotations.Nullable IGasHandler getGasHandlerCapability(@org.jetbrains.annotations.Nullable Direction side) {
        if (ownerPos == null || ownerPos.equals(TileQuarry.OUT_OF_REACH)) {
            return null;
        }

        BlockEntity owner = getLevel().getBlockEntity(ownerPos);

        if (owner instanceof GenericTileGasTransformer compressor) {
            return compressor.getGasHandlerCapability(side);
        }

        return null;
    }

    @Override
    public InteractionResult use(Player player, InteractionHand handIn, BlockHitResult hit) {
        if (getLevel().getBlockEntity(ownerPos) instanceof GenericTileGasTransformer compressor) {
            return compressor.use(player, handIn, hit);
        }
        return InteractionResult.FAIL;
    }

}
