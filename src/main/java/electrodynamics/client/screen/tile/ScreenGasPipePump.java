package electrodynamics.client.screen.tile;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.common.inventory.container.tile.ContainerGasPipePump;
import electrodynamics.common.tile.network.gas.TileGasPipePump;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.editbox.ScreenComponentEditBox;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenGasPipePump extends GenericScreen<ContainerGasPipePump> {

	private ScreenComponentEditBox priority;

	private boolean needsUpdate = true;

	public ScreenGasPipePump(ContainerGasPipePump screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);

		addComponent(priority = new ScreenComponentEditBox(94, 35, 59, 16, getFontRenderer()).setTextColor(-1).setTextColorUneditable(-1).setMaxLength(1).setResponder(this::setPriority).setFilter(ScreenComponentEditBox.POSITIVE_INTEGER));
		addComponent(new ScreenComponentSimpleLabel(20, 39, 10, 4210752, ElectroTextUtils.gui("prioritypump.priority")));
	}

	private void setPriority(String prior) {

		TileGasPipePump pump = menu.getHostFromIntArray();

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

		pump.priority.updateServer();

	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		if (needsUpdate) {
			needsUpdate = false;
			TileGasPipePump pump = menu.getHostFromIntArray();
			if (pump != null) {
				priority.setValue("" + pump.priority.get());
			}
		}
	}

}
