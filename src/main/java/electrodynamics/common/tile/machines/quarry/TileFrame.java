package electrodynamics.common.tile.machines.quarry;

import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileFrame extends BlockEntity {

	public BlockPos ownerQuarryPos = null;

	private static final String KEY = "quarrypos";

	public TileFrame(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_QUARRY_FRAME.get(), pos, state);
	}

	public void purposefullyDestroyed() {
		if (ownerQuarryPos == null) {
			return;
		}
		BlockEntity entity = level.getBlockEntity(ownerQuarryPos);
		if (entity != null && entity instanceof TileQuarry quarry) {
			quarry.addBrokenFrame(getBlockPos(), getBlockState());
		}
	}

	public void setQuarryPos(BlockPos pos) {
		ownerQuarryPos = pos;
		setChanged();
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		if (ownerQuarryPos != null) {
			tag.put(KEY, NbtUtils.writeBlockPos(ownerQuarryPos));
		}
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider pRegistries) {
		super.loadAdditional(tag, pRegistries);
		if (tag.contains(KEY)) {
			ownerQuarryPos = NbtUtils.readBlockPos(tag, KEY).get();
		}
	}

}
