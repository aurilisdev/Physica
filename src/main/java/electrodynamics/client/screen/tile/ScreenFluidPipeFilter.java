package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerFluidPipeFilter;
import electrodynamics.common.tile.pipelines.fluid.TileFluidPipeFilter;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.button.ScreenComponentButton;
import electrodynamics.prefab.screen.component.types.ScreenComponentFluidFilter;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenFluidPipeFilter extends GenericScreen<ContainerFluidPipeFilter> {

    public ScreenFluidPipeFilter(ContainerFluidPipeFilter screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);

        imageHeight += 20;
        inventoryLabelY += 20;

        addComponent(new ScreenComponentFluidFilter(30, 18, 0));
        addComponent(new ScreenComponentFluidFilter(64, 18, 1));
        addComponent(new ScreenComponentFluidFilter(99, 18, 2));
        addComponent(new ScreenComponentFluidFilter(132, 18, 3));

        addComponent(new ScreenComponentButton<>(38, 70, 100, 20).setLabel(() -> {
            TileFluidPipeFilter filter = menu.getSafeHost();
            if (filter == null) {
                return Component.empty();
            }
            return filter.isWhitelist.get() ? ElectroTextUtils.gui("filter.whitelist") : ElectroTextUtils.gui("filter.blacklist");
        }).setOnPress(button -> {
            TileFluidPipeFilter filter = menu.getSafeHost();
            if (filter == null) {
                return;
            }
            filter.isWhitelist.set(!filter.isWhitelist.get());

        }));

    }

}
