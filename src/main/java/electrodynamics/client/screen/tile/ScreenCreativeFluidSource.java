package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerCreativeFluidSource;
import electrodynamics.common.tile.pipelines.fluid.TileCreativeFluidSource;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress.ProgressTextures;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentFluidGauge;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerSimple;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenCreativeFluidSource extends GenericMaterialScreen<ContainerCreativeFluidSource> {

	public ScreenCreativeFluidSource(ContainerCreativeFluidSource container, Inventory inv, Component titleIn) {
		super(container, inv, titleIn);
		addComponent(new ScreenComponentGeneric(ProgressTextures.ARROW_RIGHT_OFF, 102, 33));
		addComponent(new ScreenComponentFluidGauge(() -> {
			TileCreativeFluidSource boiler = menu.getSafeHost();
			if (boiler != null) {
				return boiler.<ComponentFluidHandlerSimple>getComponent(IComponentType.FluidHandler);
			}
			return null;
		}, 81, 18));
		addComponent(new ScreenComponentSimpleLabel(13, 38, 10, Color.TEXT_GRAY, ElectroTextUtils.gui("creativefluidsource.setfluid")));
	}

}
