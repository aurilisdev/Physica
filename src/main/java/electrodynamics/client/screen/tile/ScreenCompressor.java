package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerCompressor;
import electrodynamics.common.tile.pipelines.gas.gastransformer.compressor.GenericTileBasicCompressor;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.types.ScreenComponentCondensedFluid;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress.ProgressTextures;
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

public class ScreenCompressor extends GenericMaterialScreen<ContainerCompressor> {

	public ScreenCompressor(ContainerCompressor container, Inventory inv, Component titleIn) {
		super(container, inv, titleIn);
		addComponent(new ScreenComponentGeneric(ProgressTextures.COMPRESS_ARROW_OFF, 65, 40));
		addComponent(new ScreenComponentGasGauge(() -> {
			GenericTileBasicCompressor.TileCompressor boiler = container.getSafeHost();
			if (boiler != null) {
				return boiler.<ComponentGasHandlerMulti>getComponent(IComponentType.GasHandler).getInputTanks()[0];
			}
			return null;
		}, 41, 18));
		addComponent(new ScreenComponentGasGauge(() -> {
			GenericTileBasicCompressor.TileCompressor boiler = container.getSafeHost();
			if (boiler != null) {
				return boiler.<ComponentGasHandlerMulti>getComponent(IComponentType.GasHandler).getOutputTanks()[0];
			}
			return null;
		}, 90, 18));
		addComponent(new ScreenComponentGasTemperature(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE * 2));
		addComponent(new ScreenComponentGasPressure(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE));
		addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2));

		addComponent(new ScreenComponentCondensedFluid(() -> {
			GenericTileBasicCompressor.TileCompressor generic = container.getSafeHost();
			if (generic == null) {
				return null;
			}

			return generic.condensedFluidFromGas;

		}, 110, 20));
	}

}
