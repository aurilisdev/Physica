package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerElectricArcFurnaceTriple;
import electrodynamics.common.tile.machines.arcfurnace.TileElectricArcFurnace;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress.ProgressBars;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.types.wrapper.WrapperInventoryIO;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenElectricArcFurnaceTriple extends GenericScreen<ContainerElectricArcFurnaceTriple> {

	public ScreenElectricArcFurnaceTriple(ContainerElectricArcFurnaceTriple container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		addComponent(new ScreenComponentProgress(ProgressBars.PROGRESS_ARROW_RIGHT, () -> {
			TileElectricArcFurnace furnace = container.getSafeHost();
			if (furnace != null) {
				ComponentProcessor processor = furnace.getProcessor(0);
				if (processor.isActive()) {
					return processor.operatingTicks.get() / processor.requiredTicks.get();
				}
			}
			return 0;
		}, 84, 24));
		addComponent(new ScreenComponentProgress(ProgressBars.COUNTDOWN_FLAME, () -> {
			TileElectricArcFurnace furnace = container.getSafeHost();
			if (furnace != null) {
				ComponentProcessor processor = furnace.getProcessor(0);
				if (processor.isActive()) {
					return 1;
				}
			}
			return 0;
		}, 39, 26));
		addComponent(new ScreenComponentProgress(ProgressBars.PROGRESS_ARROW_RIGHT, () -> {
			TileElectricArcFurnace furnace = container.getSafeHost();
			if (furnace != null) {
				ComponentProcessor processor = furnace.getProcessor(1);
				if (processor.isActive()) {
					return processor.operatingTicks.get() / processor.requiredTicks.get();
				}
			}
			return 0;
		}, 84, 44));
		addComponent(new ScreenComponentProgress(ProgressBars.COUNTDOWN_FLAME, () -> {
			TileElectricArcFurnace furnace = container.getSafeHost();
			if (furnace != null) {
				ComponentProcessor processor = furnace.getProcessor(1);
				if (processor.isActive()) {
					return 1;
				}
			}
			return 0;
		}, 39, 46));
		addComponent(new ScreenComponentProgress(ProgressBars.PROGRESS_ARROW_RIGHT, () -> {
			TileElectricArcFurnace furnace = container.getSafeHost();
			if (furnace != null) {
				ComponentProcessor processor = furnace.getProcessor(2);
				if (processor.isActive()) {
					return processor.operatingTicks.get() / processor.requiredTicks.get();
				}
			}
			return 0;
		}, 84, 64));
		addComponent(new ScreenComponentProgress(ProgressBars.COUNTDOWN_FLAME, () -> {
			TileElectricArcFurnace furnace = container.getSafeHost();
			if (furnace != null) {
				ComponentProcessor processor = furnace.getProcessor(2);
				if (processor.isActive()) {
					return 1;
				}
			}
			return 0;
		}, 39, 66));
		imageHeight += 20;
		inventoryLabelY += 20;
		addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2));

		new WrapperInventoryIO(this, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE + 2, 75, 82 + 20, 8, 72 + 20);
	}
}