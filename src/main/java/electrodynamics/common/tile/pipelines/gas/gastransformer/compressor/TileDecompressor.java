package electrodynamics.common.tile.pipelines.gas.gastransformer.compressor;

import electrodynamics.common.inventory.container.tile.ContainerDecompressor;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.registers.ElectrodynamicsTileTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileDecompressor extends GenericTileCompressor {

	public TileDecompressor(BlockPos worldPos, BlockState blockState) {
		super(ElectrodynamicsTileTypes.TILE_DECOMPRESSOR.get(), worldPos, blockState, true);
	}

	@Override
	public ComponentContainerProvider getContainerProvider() {
		return new ComponentContainerProvider("container.decompressor", this).createMenu((id, inv) -> new ContainerDecompressor(id, inv, getComponent(IComponentType.Inventory), getCoordsArray()));
	}

}
