package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerFluidPipePump;
import electrodynamics.common.tile.pipelines.fluid.TileFluidPipePump;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.editbox.ScreenComponentEditBox;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenFluidPipePump extends GenericScreen<ContainerFluidPipePump> {

	private ScreenComponentEditBox priority;

	private boolean needsUpdate = true;

	public ScreenFluidPipePump(ContainerFluidPipePump screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);

		addComponent(priority = new ScreenComponentEditBox(94, 35, 59, 16, getFontRenderer()).setTextColor(Color.WHITE).setTextColorUneditable(Color.WHITE).setMaxLength(1).setResponder(this::setPriority).setFilter(ScreenComponentEditBox.POSITIVE_INTEGER));
		addComponent(new ScreenComponentSimpleLabel(20, 39, 10, Color.TEXT_GRAY, ElectroTextUtils.gui("prioritypump.priority")));

	}

	private void setPriority(String prior) {

		TileFluidPipePump pump = menu.getSafeHost();

		if (pump == null) {
			return;
		}

		if (prior.isEmpty()) {
			return;
		}

		int priority = 0;

		try {
			priority = Integer.parseInt(prior);
		} catch (Exception e) {

		}

		if (priority > 9) {
			priority = 9;
			this.priority.setValue(priority + "");
		} else if (priority < 0) {
			priority = 0;
			this.priority.setValue(priority + "");
		}

		pump.priority.set(priority);

	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.render(graphics, mouseX, mouseY, partialTicks);
		if (needsUpdate) {
			needsUpdate = false;
			TileFluidPipePump pump = menu.getSafeHost();
			if (pump != null) {
				priority.setValue("" + pump.priority.get());
			}
		}
	}

}
