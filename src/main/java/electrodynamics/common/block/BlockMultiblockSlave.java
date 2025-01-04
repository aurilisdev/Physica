package electrodynamics.common.block;

import electrodynamics.api.multiblock.assemblybased.TileMultiblockSlave;
import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockMultiblockSlave extends GenericMachineBlock {

    public BlockMultiblockSlave() {
        super(TileMultiblockSlave::new, VoxelShapeProvider.DEFAULT);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if(worldIn.getBlockEntity(pos) instanceof TileMultiblockSlave slave) {
            return slave.getShape();
        }
        return Shapes.block();
    }

    @Override
    public void onRotate(ItemStack stack, BlockPos pos, Player player) {

    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state;
    }

}
