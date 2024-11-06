package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerAdvancedDecompressor;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenAdvancedDecompressor extends GenericMaterialScreen<ContainerAdvancedDecompressor> {
    public ScreenAdvancedDecompressor(ContainerAdvancedDecompressor screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }
}
