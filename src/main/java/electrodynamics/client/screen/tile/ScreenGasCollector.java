package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerGasCollector;
import electrodynamics.common.tile.pipelines.gas.TileGasCollector;
import electrodynamics.prefab.screen.component.types.ScreenComponentCondensedFluid;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentGasGauge;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGasPressure;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGasTemperature;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerSimple;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenGasCollector extends GenericMaterialScreen<ContainerGasCollector> {
    public ScreenGasCollector(ContainerGasCollector container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        addComponent(new ScreenComponentGasGauge(() -> {
            TileGasCollector boiler = container.getHostFromIntArray();
            if (boiler != null) {
                return boiler.<ComponentGasHandlerSimple>getComponent(IComponentType.GasHandler);
            }
            return null;
        }, 90, 18));
        addComponent(new ScreenComponentProgress(ScreenComponentProgress.ProgressBars.FAN, () -> {
            GenericTile furnace = container.getHostFromIntArray();
            if (furnace != null) {
                ComponentProcessor processor = furnace.getComponent(IComponentType.Processor);
                if (processor.isActive()) {
                    return 1.0;
                }
            }
            return 0;
        }, 57, 34));
        addComponent(new ScreenComponentGasTemperature(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE * 2));
        addComponent(new ScreenComponentGasPressure(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE));
        addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2));
        addComponent(new ScreenComponentCondensedFluid(() -> {
            TileGasCollector electric = container.getHostFromIntArray();
            if (electric == null) {
                return null;
            }

            return electric.condensedFluidFromGas;

        }, 122, 20));
    }
}
