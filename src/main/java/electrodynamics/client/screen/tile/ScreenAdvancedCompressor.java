package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerAdvancedCompressor;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenAdvancedCompressor extends GenericMaterialScreen<ContainerAdvancedCompressor> {
    public ScreenAdvancedCompressor(ContainerAdvancedCompressor screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }
}
