package electrodynamics.client.screen.tile;

import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.screen.ITexture.Textures;
import electrodynamics.common.inventory.container.tile.ContainerThermoelectricManipulator;
import electrodynamics.common.tile.pipelines.gas.gastransformer.GenericTileGasTransformer;
import electrodynamics.common.tile.pipelines.gas.gastransformer.thermoelectricmanipulator.TileThermoelectricManipulator;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.editbox.ScreenComponentEditBox;
import electrodynamics.prefab.screen.component.types.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentFluidGauge;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentGasGauge;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGasPressure;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGasTemperature;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerMulti;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenThermoelectricManipulator extends GenericScreen<ContainerThermoelectricManipulator> {

	private ScreenComponentEditBox temperature;

	private boolean needsUpdate = true;

	public ScreenThermoelectricManipulator(ContainerThermoelectricManipulator container, Inventory inv, Component titleIn) {
		super(container, inv, titleIn);
		imageHeight += 30;
		inventoryLabelY += 30;
		addComponent(new ScreenComponentFluidGauge(() -> {
			TileThermoelectricManipulator boiler = container.getHostFromIntArray();
			if (boiler != null) {
				return boiler.<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getInputTanks()[0];
			}
			return null;
		}, 10, 18));
		addComponent(new ScreenComponentFluidGauge(() -> {
			TileThermoelectricManipulator boiler = container.getHostFromIntArray();
			if (boiler != null) {
				return boiler.<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getOutputTanks()[0];
			}
			return null;
		}, 96, 18));
		addComponent(new ScreenComponentGasGauge(() -> {
			TileThermoelectricManipulator boiler = container.getHostFromIntArray();
			if (boiler != null) {
				return boiler.<ComponentGasHandlerMulti>getComponent(IComponentType.GasHandler).getInputTanks()[0];
			}
			return null;
		}, 46, 18));
		addComponent(new ScreenComponentGasGauge(() -> {
			TileThermoelectricManipulator boiler = container.getHostFromIntArray();
			if (boiler != null) {
				return boiler.<ComponentGasHandlerMulti>getComponent(IComponentType.GasHandler).getOutputTanks()[0];
			}
			return null;
		}, 132, 18));
		addComponent(new ScreenComponentGasTemperature(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE * 2));
		addComponent(new ScreenComponentGasPressure(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE));
		addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2));
		addComponent(new ScreenComponentGeneric(Textures.CONDENSER_COLUMN, 62, 19));

		addEditBox(temperature = new ScreenComponentEditBox(94, 75, 59, 16, getFontRenderer()).setTextColor(-1).setTextColorUneditable(-1).setMaxLength(20).setResponder(this::setTemperature).setFilter(ScreenComponentEditBox.POSITIVE_DECIMAL));

		addComponent(new ScreenComponentSimpleLabel(10, 80, 10, 4210752, ElectroTextUtils.gui("thermoelectricmanipulator.temp")));
		addComponent(new ScreenComponentSimpleLabel(155, 80, 10, 4210752, DisplayUnit.TEMPERATURE_KELVIN.getSymbol()));
	}

	private void setTemperature(String temp) {

		TileThermoelectricManipulator manipulator = menu.getHostFromIntArray();

		if (manipulator == null) {
			return;
		}

		if (temp.isEmpty()) {
			return;
		}

		double temperature = Gas.ROOM_TEMPERATURE;

		try {
			temperature = Double.parseDouble(temp);
		} catch (Exception e) {

		}

		if (temperature < GasStack.ABSOLUTE_ZERO) {
			temperature = Gas.ROOM_TEMPERATURE;
		} else if (temperature > GenericTileGasTransformer.OUTPUT_TEMPERATURE) {
			temperature = GenericTileGasTransformer.OUTPUT_TEMPERATURE;
			this.temperature.setValue("" + temperature);
		}

		manipulator.targetTemperature.set(temperature);

	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.render(graphics, mouseX, mouseY, partialTicks);
		if (needsUpdate) {
			needsUpdate = false;
			TileThermoelectricManipulator manipulator = menu.getHostFromIntArray();
			if (manipulator != null) {
				temperature.setValue("" + manipulator.targetTemperature.get());
			}
		}
	}

}
