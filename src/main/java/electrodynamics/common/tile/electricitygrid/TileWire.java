package electrodynamics.common.tile.electricitygrid;

import electrodynamics.prefab.properties.PropertyTypes;
import net.minecraft.core.HolderLookup;

import electrodynamics.common.block.connect.BlockWire;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.block.subtype.SubtypeWire.WireColor;
import electrodynamics.prefab.properties.Property;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileWire extends GenericTileWire {

	public Property<Double> transmit = property(new Property<>(PropertyTypes.DOUBLE, "transmit", 0.0));

	public SubtypeWire wire = null;
	public WireColor color = null;

	public TileWire(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_WIRE.get(), pos, state);
	}

	public TileWire(BlockEntityType<TileLogisticalWire> tileEntityType, BlockPos pos, BlockState state) {
		super(tileEntityType, pos, state);
	}

	@Override
	public SubtypeWire getWireType() {
		if (wire == null) {
			wire = ((BlockWire) getBlockState().getBlock()).wire;
		}
		return wire;
	}

	@Override
	public WireColor getWireColor() {
		if (color == null) {
			color = ((BlockWire) getBlockState().getBlock()).wire.color;
		}
		return color;
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		compound.putInt("ord", getWireType().ordinal());
		compound.putInt("color", getWireColor().ordinal());
		super.saveAdditional(compound, registries);
	}

	@Override
	protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		wire = SubtypeWire.values()[compound.getInt("ord")];
		color = WireColor.values()[compound.getInt("color")];
	}

}
