package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerGasPipeFilter;
import electrodynamics.common.tile.pipelines.gas.TileGasPipeFilter;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.button.ScreenComponentButton;
import electrodynamics.prefab.screen.component.types.ScreenComponentGasFilter;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenGasPipeFilter extends GenericScreen<ContainerGasPipeFilter> {

	public ScreenGasPipeFilter(ContainerGasPipeFilter screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);

		imageHeight += 20;
		inventoryLabelY += 20;

		addComponent(new ScreenComponentGasFilter(30, 18, 0));
		addComponent(new ScreenComponentGasFilter(64, 18, 1));
		addComponent(new ScreenComponentGasFilter(99, 18, 2));
		addComponent(new ScreenComponentGasFilter(132, 18, 3));

		addComponent(new ScreenComponentButton<>(38, 70, 100, 20).setLabel(() -> {
			TileGasPipeFilter filter = menu.getSafeHost();
			if (filter == null) {
				return Component.empty();
			}
			return filter.isWhitelist.get() ? ElectroTextUtils.gui("filter.whitelist") : ElectroTextUtils.gui("filter.blacklist");
		}).setOnPress(button -> {
			TileGasPipeFilter filter = menu.getSafeHost();
			if (filter == null) {
				return;
			}
			filter.isWhitelist.set(!filter.isWhitelist.get());

		}));

	}

}
