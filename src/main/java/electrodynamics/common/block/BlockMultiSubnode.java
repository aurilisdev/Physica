package electrodynamics.common.block;

import com.mojang.serialization.MapCodec;

import electrodynamics.api.multiblock.subnodebased.child.IMultiblockChildBlock;
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockMultiSubnode extends GenericEntityBlock implements IMultiblockChildBlock {

    public BlockMultiSubnode() {
        super(Blocks.GLASS.properties().strength(3.5F).sound(SoundType.METAL).isRedstoneConductor((a, b, c) -> false).noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return worldIn.getBlockEntity(pos) instanceof TileMultiSubnode subnode ? subnode.getShape() : Shapes.block();
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1.0f;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof TileMultiSubnode subnode) {
            return new ItemStack(level.getBlockState(subnode.parentPos.get()).getBlock());
        }
        return super.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileMultiSubnode(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.isAir() && level.getBlockEntity(pos) instanceof GenericTile generic) {
            generic.onBlockDestroyed();
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void onPlace(BlockState newState, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(newState, level, pos, oldState, isMoving);
        if (level.getBlockEntity(pos) instanceof GenericTile generic) {
            generic.onPlace(oldState, isMoving);
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }

}
