package electrodynamics.common.block;

import electrodynamics.api.multiblock.assemblybased.TileMultiblockSlave;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockMultiblockSlave extends GenericMachineBlock {

    public BlockMultiblockSlave() {
        super(TileMultiblockSlave::new);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if(worldIn.getBlockEntity(pos) instanceof TileMultiblockSlave slave) {
            return slave.getShape();
        }
        return Shapes.block();
    }
}
