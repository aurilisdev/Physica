package electrodynamics.common.tile.pipelines.tanks.gas;

import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerGasTankGeneric;
import electrodynamics.common.network.utils.GasUtilities;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerSimple;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.types.GenericGasTile;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GenericTileGasTank extends GenericGasTile {

	public static final double INSULATION_EFFECTIVENESS = 1.05;

	public static final double HEAT_LOSS = 0.0025; // .05 / 20

	public final Property<Double> insulationBonus = property(new Property<>(PropertyTypes.DOUBLE, "insulationbonus", 1.0));

	public GenericTileGasTank(BlockEntityType<?> type, BlockPos pos, BlockState state, SubtypeMachine machine, double capacity, int maxPressure, double maxTemperature) {
		super(type, pos, state);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentGasHandlerSimple(this, "", capacity, maxTemperature, maxPressure).setInputDirections(Direction.UP).setOutputDirections(Direction.DOWN).setOnGasCondensed(getCondensedHandler()));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().inputs(6).gasInputs(1).gasOutputs(1)).valid(machineValidator()));
		addComponent(new ComponentContainerProvider(machine, this).createMenu((id, player) -> new ContainerGasTankGeneric(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	public void tickServer(ComponentTickable tick) {
		ComponentGasHandlerSimple handler = getComponent(IComponentType.GasHandler);
		GasUtilities.drainItem(this, handler.asArray());
		GasUtilities.fillItem(this, handler.asArray());
		GasUtilities.outputToPipe(this, handler.asArray(), handler.outputDirections);

		GasStack gasIn = handler.getGas();

		if (!gasIn.isEmpty() && gasIn.getTemperature() != Gas.ROOM_TEMPERATURE) {

			double deltaT = Math.signum(Gas.ROOM_TEMPERATURE - gasIn.getTemperature());

			double temperatureDecrease = HEAT_LOSS / Math.max(1.0, insulationBonus.get()) * deltaT;

			handler.heat(temperatureDecrease, GasAction.EXECUTE);

		}

		// Output to tank below
		BlockPos pos = getBlockPos();
		BlockPos below = pos.below();

		if (level.getBlockState(below).hasBlockEntity()) {
			BlockEntity tile = level.getBlockEntity(below);
			if (tile instanceof GenericTileGasTank tankBelow) {
				ComponentGasHandlerSimple belowHandler = tankBelow.getComponent(IComponentType.GasHandler);

				handler.drain(belowHandler.fill(handler.getGas(), GasAction.SIMULATE), GasAction.EXECUTE);
			}
		}
	}

	@Override
	public int getComparatorSignal() {
		ComponentGasHandlerSimple handler = getComponent(IComponentType.GasHandler);
		return (int) (handler.getGasAmount() / Math.max(1, handler.getCapacity()) * 15.0);
	}

	@Override
	public void onInventoryChange(ComponentInventory inv, int slot) {
		super.onInventoryChange(inv, slot);
		if (slot > 5) {
			return;
		}

		double insulationBonus = 1.0;

		for (ItemStack item : inv.getInputContents()) {

			if (item.getItem() == ElectrodynamicsItems.ITEM_FIBERGLASSSHEET.get()) {

				insulationBonus *= INSULATION_EFFECTIVENESS;

			}

		}

		this.insulationBonus.set(insulationBonus);

	}

}
