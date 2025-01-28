package electrodynamics.api.multiblock.assemblybased;

import electrodynamics.Electrodynamics;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileReplaceable extends GenericTile {

	protected final Property<Integer> disguisedBlock = property(new Property<>(PropertyTypes.INTEGER, "disguisedblock", 0));
	
	public TileReplaceable(BlockEntityType<?> tileEntityTypeIn, BlockPos worldPos, BlockState blockState) {
		super(tileEntityTypeIn, worldPos, blockState);
	}
	
	public void setDisguise(BlockState state) {
		if(state.isAir()){
			Electrodynamics.LOGGER.info("air");
		}
		disguisedBlock.set(Block.BLOCK_STATE_REGISTRY.getId(state));
	}
	
	public BlockState getDisguise() {
		return Block.BLOCK_STATE_REGISTRY.byId(disguisedBlock.get());
	}
	

}
