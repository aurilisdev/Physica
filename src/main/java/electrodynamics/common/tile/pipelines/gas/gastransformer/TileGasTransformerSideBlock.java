package electrodynamics.common.tile.pipelines.gas.gastransformer;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.ItemStack;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.common.tile.machines.quarry.TileQuarry;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.CapabilityInputType;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.registers.ElectrodynamicsTileTypes;
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
        super(ElectrodynamicsTileTypes.TILE_COMPRESSOR_SIDE.get(), worldPos, blockState);
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
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.put("owner", NbtUtils.writeBlockPos(ownerPos));
        compound.putBoolean("isleft", isLeft);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        ownerPos = NbtUtils.readBlockPos(compound, "owner").get();
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
    public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {
        if (getLevel().getBlockEntity(ownerPos) instanceof GenericTileGasTransformer compressor) {
            return compressor.useWithItem(used, player, hand, hit);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public InteractionResult useWithoutItem(Player player, BlockHitResult hit) {
        if (getLevel().getBlockEntity(ownerPos) instanceof GenericTileGasTransformer compressor) {
            return compressor.useWithoutItem(player, hit);
        }
        return InteractionResult.FAIL;
    }

}
