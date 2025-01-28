package electrodynamics.common.tile.pipelines.gas.gastransformer;

import java.util.Optional;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.CapabilityInputType;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class TileGasTransformerSideBlock extends GenericTile implements IAddonTankManager {

    private BlockPos ownerPos = BlockEntityUtils.OUT_OF_REACH;
    private boolean isLeft = false;

    public TileGasTransformerSideBlock(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_COMPRESSOR_SIDE.get(), worldPos, blockState);
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

    @Override
    public void updateTankCount() {
        BlockPos abovePos = getBlockPos().above();
        BlockState aboveState = getLevel().getBlockState(abovePos);
        BlockEntity aboveTile;
        int tankCount = 0;
        for (int i = 0; i < Constants.GAS_TRANSFORMER_ADDON_TANK_LIMIT; i++) {
            if (!aboveState.is(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK)) {
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
        if (owner != null && owner instanceof IMultiblockGasTransformer compressor) {
            compressor.updateAddonTanks(tankCount, isLeft);
        }
    }

    @Override
    public void onBlockDestroyed() {
        if (level.isClientSide) {
            return;
        }
        if (getLevel().getBlockEntity(ownerPos) instanceof IMultiblockGasTransformer compressor) {
            getLevel().destroyBlock(ownerPos, !compressor.hasBeenDestroyed());
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
        Optional<BlockPos> optional = NbtUtils.readBlockPos(compound, "owner");
        ownerPos = optional.isPresent() ? optional.get() : BlockEntityUtils.OUT_OF_REACH;
        isLeft = compound.getBoolean("isleft");
    }

    @Override
    public @org.jetbrains.annotations.Nullable IFluidHandler getFluidHandlerCapability(@org.jetbrains.annotations.Nullable Direction side) {
        if (ownerPos == null || ownerPos.equals(BlockEntityUtils.OUT_OF_REACH)) {
            return null;
        }

        if (getLevel().getBlockEntity(ownerPos) instanceof GenericTileGasTransformer compressor && compressor.hasComponent(IComponentType.FluidHandler)) {

            if (isLeft) {
                return compressor.<IComponentFluidHandler>getComponent(IComponentType.FluidHandler).getCapability(side, CapabilityInputType.INPUT);
            }
            return compressor.<IComponentFluidHandler>getComponent(IComponentType.FluidHandler).getCapability(side, CapabilityInputType.OUTPUT);

        }
        return null;
    }

    @Override
    public @org.jetbrains.annotations.Nullable IGasHandler getGasHandlerCapability(@org.jetbrains.annotations.Nullable Direction side) {
        if (ownerPos == null || ownerPos.equals(BlockEntityUtils.OUT_OF_REACH)) {
            return null;
        }

        if (getLevel().getBlockEntity(ownerPos) instanceof GenericTileGasTransformer compressor) {
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
