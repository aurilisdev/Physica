package electrodynamics.client.screen.tile;

import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.screen.ITexture.Textures;
import electrodynamics.common.inventory.container.tile.ContainerThermoelectricManipulator;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.pipelines.gas.gastransformer.thermoelectricmanipulator.GenericTileThermoelectricManipulator;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.editbox.ScreenComponentEditBox;
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
import electrodynamics.prefab.utilities.math.Color;
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
			GenericTileThermoelectricManipulator boiler = container.getSafeHost();
			if (boiler != null) {
				return boiler.<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getInputTanks()[0];
			}
			return null;
		}, 10, 18));
		addComponent(new ScreenComponentFluidGauge(() -> {
			GenericTileThermoelectricManipulator boiler = container.getSafeHost();
			if (boiler != null) {
				return boiler.<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getOutputTanks()[0];
			}
			return null;
		}, 96, 18));
		addComponent(new ScreenComponentGasGauge(() -> {
			GenericTileThermoelectricManipulator boiler = container.getSafeHost();
			if (boiler != null) {
				return boiler.<ComponentGasHandlerMulti>getComponent(IComponentType.GasHandler).getInputTanks()[0];
			}
			return null;
		}, 46, 18));
		addComponent(new ScreenComponentGasGauge(() -> {
			GenericTileThermoelectricManipulator boiler = container.getSafeHost();
			if (boiler != null) {
				return boiler.<ComponentGasHandlerMulti>getComponent(IComponentType.GasHandler).getOutputTanks()[0];
			}
			return null;
		}, 132, 18));
		addComponent(new ScreenComponentGasTemperature(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE * 2));
		addComponent(new ScreenComponentGasPressure(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE));
		addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2));
		addComponent(new ScreenComponentGeneric(Textures.CONDENSER_COLUMN, 62, 19));

		addEditBox(temperature = new ScreenComponentEditBox(94, 75, 59, 16, getFontRenderer()).setTextColor(Color.WHITE).setTextColorUneditable(Color.WHITE).setMaxLength(20).setResponder(this::setTemperature).setFilter(ScreenComponentEditBox.POSITIVE_INTEGER));

		addComponent(new ScreenComponentSimpleLabel(10, 80, 10, Color.TEXT_GRAY, ElectroTextUtils.gui("thermoelectricmanipulator.temp")));
		addComponent(new ScreenComponentSimpleLabel(155, 80, 10, Color.TEXT_GRAY, DisplayUnit.TEMPERATURE_KELVIN.getSymbol()));
	}

	private void setTemperature(String temp) {

		GenericTileThermoelectricManipulator manipulator = menu.getSafeHost();

		if (manipulator == null) {
			return;
		}

		if (temp.isEmpty()) {
			return;
		}

		int temperature = Gas.ROOM_TEMPERATURE;

		try {
			temperature = Integer.parseInt(temp);
		} catch (Exception e) {

		}

		if (temperature < GasStack.ABSOLUTE_ZERO) {
			temperature = Gas.ROOM_TEMPERATURE;
		} else if (temperature > Constants.GAS_TRANSFORMER_OUTPUT_TEMP_CAP) {
			temperature = Constants.GAS_TRANSFORMER_OUTPUT_TEMP_CAP;
			this.temperature.setValue("" + temperature);
		}

		manipulator.targetTemperature.set(temperature);

	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.render(graphics, mouseX, mouseY, partialTicks);
		if (needsUpdate) {
			needsUpdate = false;
			GenericTileThermoelectricManipulator manipulator = menu.getSafeHost();
			if (manipulator != null) {
				temperature.setValue("" + manipulator.targetTemperature.get());
			}
		}
	}

}
