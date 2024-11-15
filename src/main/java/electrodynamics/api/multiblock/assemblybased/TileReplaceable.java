package electrodynamics.api.multiblock.assemblybased;

import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileReplaceable extends GenericTile {

	private final Property<BlockState> disguisedBlock = property(new Property<>(PropertyTypes.BLOCK_STATE, "disguisedblock", Blocks.AIR.defaultBlockState()));
	
	public TileReplaceable(BlockEntityType<?> tileEntityTypeIn, BlockPos worldPos, BlockState blockState) {
		super(tileEntityTypeIn, worldPos, blockState);
	}
	
	public void setDisguise(BlockState state) {
		disguisedBlock.set(state);
	}
	
	public BlockState getDisguise() {
		return disguisedBlock.get();
	}
	

}
