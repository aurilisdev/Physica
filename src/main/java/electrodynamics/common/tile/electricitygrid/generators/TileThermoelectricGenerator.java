package electrodynamics.common.tile.electricitygrid.generators;

import electrodynamics.common.reloadlistener.ThermoelectricGeneratorHeatRegister;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.ElectricityUtils;
import electrodynamics.prefab.utilities.object.CachedTileOutput;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public class TileThermoelectricGenerator extends GenericTile {

	protected CachedTileOutput output;

	public Property<Boolean> hasHeat = property(new Property<>(PropertyTypes.BOOLEAN, "hasheat", false));
	public Property<Double> heatMultipler = property(new Property<>(PropertyTypes.DOUBLE, "multiplier", 0.0));
	private Property<Boolean> hasRedstoneSignal = property(new Property<>(PropertyTypes.BOOLEAN, "redstonesignal", false));

	public TileThermoelectricGenerator(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_THERMOELECTRICGENERATOR.get(), worldPosition, blockState);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentElectrodynamic(this, true, false).setOutputDirections(BlockEntityUtils.MachineDirection.TOP));
	}

	protected void tickServer(ComponentTickable tickable) {
		if (hasRedstoneSignal.get()) {
			return;
		}
		if (output == null) {
			output = new CachedTileOutput(level, worldPosition.relative(Direction.UP));
		}
		Direction facing = getFacing();
		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
		if (tickable.getTicks() % 60 == 0) {
			Fluid fluid = level.getFluidState(worldPosition.relative(facing.getOpposite())).getType();
			hasHeat.set(ThermoelectricGeneratorHeatRegister.INSTANCE.isHeatSource(fluid));
			heatMultipler.set(ThermoelectricGeneratorHeatRegister.INSTANCE.getHeatMultiplier(fluid));
			output.update(worldPosition.relative(Direction.UP));
		}
		if (hasHeat.get() && output.valid()) {
			ElectricityUtils.receivePower(output.getSafe(), Direction.DOWN, TransferPack.ampsVoltage(Constants.THERMOELECTRICGENERATOR_AMPERAGE * level.getFluidState(worldPosition.relative(facing.getOpposite())).getAmount() / 8.0 * heatMultipler.get(), electro.getVoltage()), false);
		}
	}

	@Override
	public int getComparatorSignal() {
		return hasHeat.get() ? 15 : 0;
	}

	@Override
	public void onNeightborChanged(BlockPos neighbor, boolean blockStateTrigger) {
		if (level.isClientSide) {
			return;
		}
		hasRedstoneSignal.set(level.hasNeighborSignal(getBlockPos()));
	}

}
