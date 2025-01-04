package electrodynamics.common.tile.electricitygrid.generators;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerCreativePowerSource;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.ElectricityUtils;
import electrodynamics.prefab.utilities.object.CachedTileOutput;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class TileCreativePowerSource extends GenericTile {

	private static final int POWER_MULTIPLIER = 1000000;

	public Property<Integer> voltage = property(new Property<>(PropertyTypes.INTEGER, "setvoltage", 0));
	public Property<Double> power = property(new Property<>(PropertyTypes.DOUBLE, "setpower", 0.0));
	private Property<Boolean> hasRedstoneSignal = property(new Property<>(PropertyTypes.BOOLEAN, "redstonesignal", false));

	protected List<CachedTileOutput> outputs;

	public TileCreativePowerSource(BlockPos worldPos, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_CREATIVEPOWERSOURCE.get(), worldPos, blockState);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentElectrodynamic(this, true, false).setOutputDirections(BlockEntityUtils.MachineDirection.values()).voltage(-1));
		addComponent(new ComponentContainerProvider(SubtypeMachine.creativepowersource, this).createMenu((id, player) -> new ContainerCreativePowerSource(id, player, getCoordsArray())));
	}

	private void tickServer(ComponentTickable tick) {
		if (hasRedstoneSignal.get()) {
			return;
		}
		// ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		if (outputs == null) {
			outputs = new ArrayList<>();
			for (Direction dir : Direction.values()) {
				outputs.add(new CachedTileOutput(level, worldPosition.relative(dir)));
			}
		}
		if (tick.getTicks() % 40 == 0) {
			for (int i = 0; i < Direction.values().length; i++) {
				CachedTileOutput cache = outputs.get(i);
				cache.update(worldPosition.relative(Direction.values()[i]));
			}
		}

		if (voltage.get() <= 0) {
			return;
		}

		// electro.voltage(power.get());
		TransferPack output = TransferPack.joulesVoltage(power.get() * POWER_MULTIPLIER / 20.0, voltage.get());
		for (int i = 0; i < outputs.size(); i++) {
			CachedTileOutput cache = outputs.get(i);
			Direction dir = Direction.values()[i];
			if (cache.valid()) {
				ElectricityUtils.receivePower(cache.getSafe(), dir.getOpposite(), output, false);
			}
		}

	}

	@Override
	public int getComparatorSignal() {
		return power.get() > 0 ? 15 : 0;
	}

	@Override
	public void onNeightborChanged(BlockPos neighbor, boolean blockStateTrigger) {
		if (level.isClientSide) {
			return;
		}
		hasRedstoneSignal.set(level.hasNeighborSignal(getBlockPos()));
	}
}
