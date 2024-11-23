package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerElectrolosisChamber;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenElectrolosisChamber extends GenericMaterialScreen<ContainerElectrolosisChamber> {
    public ScreenElectrolosisChamber(ContainerElectrolosisChamber screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }
}
