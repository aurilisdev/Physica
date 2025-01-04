package electrodynamics.api.multiblock.subnodebased;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public record Subnode(BlockPos pos, VoxelShape[] shapes) {

	public static final Subnode EMPTY = new Subnode(BlockPos.ZERO, Shapes.empty());

	public Subnode(BlockPos pos, VoxelShape allDirsShape) {
		this(pos, new VoxelShape[] { allDirsShape, allDirsShape, allDirsShape, allDirsShape, allDirsShape, allDirsShape });
	}

	public VoxelShape getShape(Direction dir) {
		return shapes[dir.ordinal()];
	}

}
