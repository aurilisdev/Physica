package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerCreativeGasSource;
import electrodynamics.common.tile.pipelines.gas.TileCreativeGasSource;
import electrodynamics.prefab.screen.component.types.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentGasGauge;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerSimple;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenCreativeGasSource extends GenericMaterialScreen<ContainerCreativeGasSource> {
    public ScreenCreativeGasSource(ContainerCreativeGasSource container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        addComponent(new ScreenComponentGeneric(ScreenComponentProgress.ProgressTextures.ARROW_RIGHT_OFF, 102, 33));
        addComponent(new ScreenComponentGasGauge(() -> {
            TileCreativeGasSource boiler = container.getSafeHost();
            if (boiler != null) {
                return boiler.<ComponentGasHandlerSimple>getComponent(IComponentType.GasHandler);
            }
            return null;
        }, 81, 18));
        addComponent(new ScreenComponentSimpleLabel(13, 38, 10, 4210752, ElectroTextUtils.gui("creativegassource.setgas")));
    }
}
