package electrodynamics.client.screen.tile;

import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.inventory.container.tile.ContainerPotentiometer;
import electrodynamics.common.tile.electricitygrid.TilePotentiometer;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.editbox.ScreenComponentEditBox;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenPotentiometer extends GenericScreen<ContainerPotentiometer> {

	private ScreenComponentEditBox consumption;

	private boolean needsUpdate = true;

	public ScreenPotentiometer(ContainerPotentiometer container, Inventory inv, Component title) {
		super(container, inv, title);
		addEditBox(consumption = new ScreenComponentEditBox(72, 35, 80, 16, getFontRenderer()).setTextColor(Color.WHITE).setTextColorUneditable(Color.WHITE).setMaxLength(30).setFilter(ScreenComponentEditBox.DECIMAL).setResponder(this::setConsumption));
		addComponent(new ScreenComponentSimpleLabel(10, 39, 10, Color.TEXT_GRAY, ElectroTextUtils.gui("potentiometer.usage")));
		addComponent(new ScreenComponentSimpleLabel(155, 39, 10, Color.TEXT_GRAY, DisplayUnit.WATT.getSymbol()));
	}

	private void setConsumption(String value) {

		if (value.isEmpty()) {
			return;
		}

		TilePotentiometer potentiometer = menu.getSafeHost();

		if (potentiometer == null) {
			return;
		}

		double consumption = 0;

		try {
			consumption = Double.parseDouble(value);
		} catch (Exception e) {

		}

		potentiometer.powerConsumption.set(consumption);

	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.render(graphics, mouseX, mouseY, partialTicks);
		if (needsUpdate) {
			needsUpdate = false;
			TilePotentiometer source = menu.getSafeHost();
			if (source != null) {
				consumption.setValue("" + source.powerConsumption.get());
			}
		}
	}

}
