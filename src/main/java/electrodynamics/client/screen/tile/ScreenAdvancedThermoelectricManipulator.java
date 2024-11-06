package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerAdvancedThermoelectricManipulator;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenAdvancedThermoelectricManipulator extends GenericMaterialScreen<ContainerAdvancedThermoelectricManipulator> {
    public ScreenAdvancedThermoelectricManipulator(ContainerAdvancedThermoelectricManipulator screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }
}
