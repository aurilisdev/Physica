package electrodynamics.common.tile.pipelines.gas.gastransformer.compressor;

import electrodynamics.common.inventory.container.tile.ContainerCompressor;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileCompressor extends GenericTileCompressor {

	public TileCompressor(BlockPos worldPos, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_COMPRESSOR.get(), worldPos, blockState, false);
	}

	@Override
	public ComponentContainerProvider getContainerProvider() {
		return new ComponentContainerProvider("container.compressor", this).createMenu((id, inv) -> new ContainerCompressor(id, inv, getComponent(IComponentType.Inventory), getCoordsArray()));
	}

}
