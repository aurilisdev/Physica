package electrodynamics.common.block;

import electrodynamics.api.multiblock.assemblybased.TileMultiblockSlave;
import electrodynamics.prefab.block.GenericMachineBlock;

public class BlockMultiblockSlave extends GenericMachineBlock {

	public BlockMultiblockSlave() {
		super(TileMultiblockSlave::new);
	}

}
