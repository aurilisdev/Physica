package electrodynamics.client.screen.tile;

import java.util.List;

import electrodynamics.common.inventory.container.tile.ContainerSeismicRelay;
import electrodynamics.common.tile.machines.quarry.TileSeismicRelay;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.ScreenComponentMultiLabel;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenSeismicRelay extends GenericScreen<ContainerSeismicRelay> {

	public ScreenSeismicRelay(ContainerSeismicRelay container, Inventory inv, Component titleIn) {
		super(container, inv, titleIn);
		addComponent(new ScreenComponentSimpleLabel(70, 20, 10, Color.TEXT_GRAY, ElectroTextUtils.gui("seismicrelay.dataheader")));
		addComponent(new ScreenComponentMultiLabel(0, 0, graphics -> {

			TileSeismicRelay relay = menu.getSafeHost();

			if (relay != null) {
				List<BlockPos> markers = relay.markerLocs.get();
				if (!markers.isEmpty()) {
					renderCoordinate(graphics, markers.get(0), 0, 1);
				} else {
					renderNotFound(graphics, 0, 1);
				}
				if (markers.size() > 1) {
					renderCoordinate(graphics, markers.get(1), 10, 2);
				} else {
					renderNotFound(graphics, 10, 2);
				}
				if (markers.size() > 2) {
					renderCoordinate(graphics, markers.get(2), 20, 3);
				} else {
					renderNotFound(graphics, 20, 3);
				}
				if (markers.size() > 3) {
					renderCoordinate(graphics, markers.get(3), 30, 4);
				} else {
					renderNotFound(graphics, 30, 4);
				}
			} else {
				renderNotFound(graphics, 0, 1);
				renderNotFound(graphics, 10, 2);
				renderNotFound(graphics, 20, 3);
				renderNotFound(graphics, 30, 4);
			}
		}));
	}

	private void renderNotFound(GuiGraphics graphics, int offset, int index) {
		graphics.drawString(font, ElectroTextUtils.gui("seismicrelay.posnotfound", index), 80, 30 + offset, Color.TEXT_GRAY.color(), false);
	}

	private void renderCoordinate(GuiGraphics graphics, BlockPos pos, int offset, int index) {
		graphics.drawString(font, ElectroTextUtils.gui("seismicrelay.posfound", index, pos.toShortString()), 80, 30 + offset, Color.TEXT_GRAY.color(), false);
	}

}
