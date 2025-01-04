package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerFluidTankGeneric;
import electrodynamics.common.tile.pipelines.fluid.tank.GenericTileFluidTank;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress.ProgressTextures;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentFluidGauge;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerSimple;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenFluidTankGeneric extends GenericMaterialScreen<ContainerFluidTankGeneric> {

	public ScreenFluidTankGeneric(ContainerFluidTankGeneric screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);

		addComponent(new ScreenComponentGeneric(ProgressTextures.ARROW_RIGHT_OFF, 52, 33));
		addComponent(new ScreenComponentGeneric(ProgressTextures.ARROW_RIGHT_OFF, 102, 33));
		addComponent(new ScreenComponentFluidGauge(() -> {
			GenericTileFluidTank boiler = menu.getSafeHost();
			if (boiler != null) {
				return boiler.<ComponentFluidHandlerSimple>getComponent(IComponentType.FluidHandler);
			}
			return null;
		}, 81, 18));
	}

}
