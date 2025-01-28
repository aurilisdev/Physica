package electrodynamics.common.block.chemicalreactor;

import org.jetbrains.annotations.Nullable;

import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import electrodynamics.common.tile.machines.chemicalreactor.TileChemicalReactor;
import electrodynamics.prefab.block.GenericMachineBlock;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class BlockChemicalReactor extends GenericMachineBlock {
    public BlockChemicalReactor() {
        super(TileChemicalReactor::new, VoxelShapeProvider.DEFAULT);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return super.canSurvive(state, level, pos) && level.getBlockState(pos.offset(0, 1, 0)).canBeReplaced() && level.getBlockState(pos.offset(0, 2, 0)).canBeReplaced();
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile != null) {
            tile.saveToItem(stack, level.registryAccess());
        }
        return stack;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(level, pos, pState, pPlacer, pStack);
        level.setBlockAndUpdate(pos.offset(BlockChemicalReactorExtra.Location.MIDDLE.offsetUpFromParent), ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOREXTRA_MIDDLE.get().defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false).setValue(ElectrodynamicsBlockStates.FACING, pState.getValue(ElectrodynamicsBlockStates.FACING)));
        level.setBlockAndUpdate(pos.offset(BlockChemicalReactorExtra.Location.TOP.offsetUpFromParent), ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOREXTRA_TOP.get().defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false).setValue(ElectrodynamicsBlockStates.FACING, pState.getValue(ElectrodynamicsBlockStates.FACING)));
    }
}
