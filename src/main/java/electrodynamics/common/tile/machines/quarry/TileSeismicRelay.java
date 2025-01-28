package electrodynamics.common.tile.machines.quarry;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerSeismicRelay;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileSeismicRelay extends GenericTile {

	public Property<List<BlockPos>> markerLocs = property(new Property<>(PropertyTypes.BLOCK_POS_LIST, "markerlocs", new ArrayList<>()));

	public boolean cornerOnRight = false;

	public TileSeismicRelay(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_SEISMICRELAY.get(), worldPosition, blockState);
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().outputs(1)).valid((slot, stack, i) -> ItemUtils.testItems(stack.getItem(), ElectrodynamicsItems.ITEM_SEISMICMARKER.get())));
		addComponent(new ComponentContainerProvider(SubtypeMachine.seismicrelay, this).createMenu((id, player) -> new ContainerSeismicRelay(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	private void tickServer(ComponentTickable tickable) {
		if (markerLocs.get().size() < 4) {
			Direction facing = getFacing().getOpposite();
			Level world = getLevel();
			BlockEntity tile = world.getBlockEntity(getBlockPos().relative(facing));
			if (tile != null && tile instanceof TileSeismicMarker marker) {
				getMarkers(marker, facing);
			}
		}

	}

	private void getMarkers(TileSeismicMarker marker, Direction facing) {
		List<BlockPos> positions = new ArrayList<>();
		BlockPos frontMarker = getMarker(facing, marker.getBlockPos(), marker.getLevel());
		BlockPos sideMarker = null;
		BlockPos cornerMarker = null;
		if (frontMarker != null) {
			sideMarker = getMarker(facing.getClockWise(), marker.getBlockPos(), marker.getLevel());
			if (sideMarker != null) {
				cornerOnRight = true;
				cornerMarker = getMarker(facing, sideMarker, marker.getLevel());
			} else {
				sideMarker = getMarker(facing.getCounterClockWise(), marker.getBlockPos(), marker.getLevel());
				if (sideMarker != null) {
					cornerMarker = getMarker(facing, sideMarker, marker.getLevel());
				}
			}
		}
		positions.add(marker.getBlockPos());
		if (frontMarker != null) {
			positions.add(frontMarker);
		}
		if (sideMarker != null) {
			positions.add(sideMarker);
		}
		if (cornerMarker != null) {
			positions.add(cornerMarker);
		}

		markerLocs.set(positions);
		markerLocs.forceDirty();

		if (markerLocs.get().size() > 3) {
			collectMarkers();
			getLevel().playSound(null, getBlockPos(), SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	private static BlockPos getMarker(Direction facing, BlockPos blockPos, Level level) {
		for (int i = 0; i <= TileSeismicMarker.MAX_RADIUS; i++) {
			blockPos = blockPos.relative(facing);
			BlockEntity marker = level.getBlockEntity(blockPos);
			if (marker instanceof TileSeismicMarker && i > 0) {
				return marker.getBlockPos();
			}
		}
		return null;
	}

	private void collectMarkers() {
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		ItemStack input = inv.getOutputContents().get(0);
		if (input.isEmpty()) {
			inv.setItem(0, new ItemStack(ElectrodynamicsItems.ITEM_SEISMICMARKER.get(), markerLocs.get().size()).copy());
			for (BlockPos pos : markerLocs.get()) {
				getLevel().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}
		} else if (ItemUtils.testItems(input.getItem(), ElectrodynamicsItems.ITEM_SEISMICMARKER.get())) {
			int room = input.getMaxStackSize() - input.getCount();
			int accepted = room >= markerLocs.get().size() ? markerLocs.get().size() : room;
			input.grow(accepted);
			for (BlockPos pos : markerLocs.get()) {
				getLevel().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}
		}
	}

	public boolean hasMarkers() {
		return markerLocs.get().size() > 3;
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.putBoolean("onRight", cornerOnRight);
	}

	@Override
	protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		cornerOnRight = compound.getBoolean("onRight");
	}

}
