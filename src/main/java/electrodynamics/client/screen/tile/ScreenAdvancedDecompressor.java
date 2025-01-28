package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerAdvancedDecompressor;
import electrodynamics.common.tile.pipelines.gas.gastransformer.compressor.GenericTileAdvancedCompressor;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.button.ScreenComponentButton;
import electrodynamics.prefab.screen.component.types.ScreenComponentCondensedFluid;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentGasGauge;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGasPressure;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGasTemperature;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerMulti;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenAdvancedDecompressor extends GenericMaterialScreen<ContainerAdvancedDecompressor> {
    public ScreenAdvancedDecompressor(ContainerAdvancedDecompressor container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        inventoryLabelY += 47;
        imageHeight += 47;
        addComponent(new ScreenComponentGeneric(ScreenComponentProgress.ProgressTextures.DECOMPRESS_ARROW_OFF, 65, 40).onTooltip(
                (graphics, component, xAxis, yAxis) -> graphics.renderTooltip(getFontRenderer(), Component.literal("1.0 / " + 1.0 / container.getSafeHost().pressureMultiplier.get()), xAxis, yAxis)
        ));
        addComponent(new ScreenComponentGasGauge(() -> {
            GenericTileAdvancedCompressor.TileAdvancedDecompressor boiler = container.getSafeHost();
            if (boiler != null) {
                return boiler.<ComponentGasHandlerMulti>getComponent(IComponentType.GasHandler).getInputTanks()[0];
            }
            return null;
        }, 41, 18));
        addComponent(new ScreenComponentGasGauge(() -> {
            GenericTileAdvancedCompressor.TileAdvancedDecompressor boiler = container.getSafeHost();
            if (boiler != null) {
                return boiler.<ComponentGasHandlerMulti>getComponent(IComponentType.GasHandler).getOutputTanks()[0];
            }
            return null;
        }, 90, 18));
        addComponent(new ScreenComponentGasTemperature(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE * 2));
        addComponent(new ScreenComponentGasPressure(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE));
        addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2));

        addComponent(new ScreenComponentCondensedFluid(() -> {
            GenericTileAdvancedCompressor.TileAdvancedDecompressor generic = container.getSafeHost();
            if (generic == null) {
                return null;
            }

            return generic.condensedFluidFromGas;

        }, 110, 20));

        addComponent(new ScreenComponentButton<>(8, 75, 40, 20).setLabel(Component.literal("1/2")).setOnPress(button -> {
            GenericTileAdvancedCompressor.TileAdvancedDecompressor compressor = menu.getSafeHost();
            if (compressor == null) {
                return;
            }
            compressor.pressureMultiplier.set(1.0 / 2.0);
        }));
        addComponent(new ScreenComponentButton<>(48, 75, 40, 20).setLabel(Component.literal("1/4")).setOnPress(button -> {
            GenericTileAdvancedCompressor.TileAdvancedDecompressor compressor = menu.getSafeHost();
            if (compressor == null) {
                return;
            }
            compressor.pressureMultiplier.set(1.0 / 4.0);
        }));
        addComponent(new ScreenComponentButton<>(88, 75, 40, 20).setLabel(Component.literal("1/8")).setOnPress(button -> {
            GenericTileAdvancedCompressor.TileAdvancedDecompressor compressor = menu.getSafeHost();
            if (compressor == null) {
                return;
            }
            compressor.pressureMultiplier.set(1.0 / 8.0);
        }));
        addComponent(new ScreenComponentButton<>(128, 75, 40, 20).setLabel(Component.literal("1/16")).setOnPress(button -> {
            GenericTileAdvancedCompressor.TileAdvancedDecompressor compressor = menu.getSafeHost();
            if (compressor == null) {
                return;
            }
            compressor.pressureMultiplier.set(1.0 / 16.0);
        }));
        addComponent(new ScreenComponentButton<>(8, 95, 40, 20).setLabel(Component.literal("1/32")).setOnPress(button -> {
            GenericTileAdvancedCompressor.TileAdvancedDecompressor compressor = menu.getSafeHost();
            if (compressor == null) {
                return;
            }
            compressor.pressureMultiplier.set(1.0 / 32.0);
        }));
        addComponent(new ScreenComponentButton<>(48, 95, 40, 20).setLabel(Component.literal("1/64")).setOnPress(button -> {
            GenericTileAdvancedCompressor.TileAdvancedDecompressor compressor = menu.getSafeHost();
            if (compressor == null) {
                return;
            }
            compressor.pressureMultiplier.set(1.0 / 64.0);
        }));
        addComponent(new ScreenComponentButton<>(88, 95, 40, 20).setLabel(Component.literal("1/128")).setOnPress(button -> {
            GenericTileAdvancedCompressor.TileAdvancedDecompressor compressor = menu.getSafeHost();
            if (compressor == null) {
                return;
            }
            compressor.pressureMultiplier.set(1.0 / 128.0);
        }));
        addComponent(new ScreenComponentButton<>(128, 95, 40, 20).setLabel(Component.literal("1/256")).setOnPress(button -> {
            GenericTileAdvancedCompressor.TileAdvancedDecompressor compressor = menu.getSafeHost();
            if (compressor == null) {
                return;
            }
            compressor.pressureMultiplier.set(1.0 / 256.0);
        }));
    }
}
