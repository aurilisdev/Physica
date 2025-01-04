package electrodynamics.client.screen.tile;

import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.electricity.formatting.MeasurementUnit;
import electrodynamics.common.inventory.container.tile.ContainerCreativePowerSource;
import electrodynamics.common.tile.electricitygrid.generators.TileCreativePowerSource;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.editbox.ScreenComponentEditBox;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenCreativePowerSource extends GenericScreen<ContainerCreativePowerSource> {

	private ScreenComponentEditBox voltage;
	private ScreenComponentEditBox power;

	private boolean needsUpdate = true;

	public ScreenCreativePowerSource(ContainerCreativePowerSource container, Inventory inv, Component titleIn) {
		super(container, inv, titleIn);
		addComponent(voltage = new ScreenComponentEditBox(80, 27, 49, 16, getFontRenderer()).setTextColor(Color.WHITE).setTextColorUneditable(Color.WHITE).setMaxLength(6).setFilter(ScreenComponentEditBox.POSITIVE_INTEGER).setResponder(this::setVoltage));
		addComponent(power = new ScreenComponentEditBox(80, 45, 49, 16, getFontRenderer()).setTextColor(Color.WHITE).setTextColorUneditable(Color.WHITE).setFilter(ScreenComponentEditBox.POSITIVE_DECIMAL).setResponder(this::setPower));
		addComponent(new ScreenComponentSimpleLabel(40, 31, 10, Color.TEXT_GRAY, ElectroTextUtils.gui("creativepowersource.voltage")));
		addComponent(new ScreenComponentSimpleLabel(40, 49, 10, Color.TEXT_GRAY, ElectroTextUtils.gui("creativepowersource.power")));
		addComponent(new ScreenComponentSimpleLabel(131, 31, 10, Color.TEXT_GRAY, DisplayUnit.VOLTAGE.getSymbol()));
		addComponent(new ScreenComponentSimpleLabel(131, 49, 10, Color.TEXT_GRAY, MeasurementUnit.MEGA.getSymbol().copy().append(DisplayUnit.WATT.getSymbol())));
	}

	private void setVoltage(String val) {
		voltage.setFocus(true);
		power.setFocus(false);
		handleVoltage(val);
	}

	private void handleVoltage(String val) {
		if (val.isEmpty()) {
			return;
		}

		Integer voltage = 0;

		try {
			voltage = Integer.parseInt(val);
		} catch (Exception e) {

		}

		TileCreativePowerSource tile = menu.getSafeHost();

		if (tile == null) {
			return;
		}

		tile.voltage.set(voltage);
	}

	private void setPower(String val) {
		voltage.setFocus(false);
		power.setFocus(true);
		handlePower(val);
	}

	private void handlePower(String val) {

		if (val.isEmpty()) {
			return;
		}

		Double power = 0.0;

		try {
			power = Double.parseDouble(val);
		} catch (Exception e) {

		}

		TileCreativePowerSource tile = menu.getSafeHost();

		if (tile == null) {
			return;
		}

		tile.power.set(power);

	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.render(graphics, mouseX, mouseY, partialTicks);
		if (needsUpdate) {
			needsUpdate = false;
			TileCreativePowerSource source = menu.getSafeHost();
			if (source != null) {
				voltage.setValue("" + source.voltage.get());
				power.setValue("" + source.power.get());
			}
		}
	}

}
