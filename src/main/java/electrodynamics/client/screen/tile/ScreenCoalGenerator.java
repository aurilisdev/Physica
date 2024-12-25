package electrodynamics.client.screen.tile;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.inventory.container.tile.ContainerCoalGenerator;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.electricitygrid.generators.TileCoalGenerator;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.ScreenComponentMultiLabel;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress.ProgressBars;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentTemperature;
import electrodynamics.prefab.screen.component.types.wrapper.WrapperInventoryIO;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenCoalGenerator extends GenericScreen<ContainerCoalGenerator> {
	public ScreenCoalGenerator(ContainerCoalGenerator container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		addComponent(new ScreenComponentProgress(ProgressBars.COUNTDOWN_FLAME, () -> {
			TileCoalGenerator box = container.getSafeHost();
			if (box != null) {
				return (double) box.burnTime.get() / (double) box.maxBurnTime.get();
			}
			return 0;
		}, 25, 25));
		addComponent(new ScreenComponentTemperature(this::getTemperatureInformation, -AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE));
		addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2));
		addComponent(new ScreenComponentMultiLabel(0, 0, graphics -> {
			TileCoalGenerator coal = menu.getSafeHost();
			if (coal == null) {
				return;
			}
			TransferPack output = TransferPack.ampsVoltage(Constants.COALGENERATOR_AMPERAGE * Math.min((coal.heat.getValue() - 27.0) / (3000.0 - 27.0), 1), ElectrodynamicsCapabilities.DEFAULT_VOLTAGE);
			graphics.drawString(font, ElectroTextUtils.gui("coalgenerator.timeleft", ChatFormatter.getChatDisplayShort((double) coal.burnTime.get() / 20.0, DisplayUnit.TIME_SECONDS)), inventoryLabelX + 60, inventoryLabelY - 53, 4210752, false);
			graphics.drawString(font, ElectroTextUtils.gui("machine.current", ChatFormatter.getChatDisplayShort(output.getAmps(), DisplayUnit.AMPERE)), inventoryLabelX + 60, inventoryLabelY - 40, 4210752, false);
			graphics.drawString(font, ElectroTextUtils.gui("machine.output", ChatFormatter.getChatDisplayShort(output.getWatts(), DisplayUnit.WATT)), inventoryLabelX + 60, inventoryLabelY - 27, 4210752, false);
			graphics.drawString(font, ElectroTextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(output.getVoltage(), DisplayUnit.VOLTAGE)), inventoryLabelX + 60, inventoryLabelY - 14, 4210752, false);
		}));

		new WrapperInventoryIO(this, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE * 2 + 2, 75, 82, 8, 72);
	}

	private List<FormattedCharSequence> getTemperatureInformation() {
		ArrayList<FormattedCharSequence> list = new ArrayList<>();
		TileCoalGenerator box = menu.getSafeHost();
		if (box == null) {
			return list;
		}

		list.add(ElectroTextUtils.gui("coalgenerator.timeleft", ChatFormatter.getChatDisplayShort((double) box.burnTime.get() / 20.0, DisplayUnit.TIME_SECONDS).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		list.add(ElectroTextUtils.gui("machine.temperature", ChatFormatter.getChatDisplayShort(box.heat.getValue() * (2500.0 / 3000.0), DisplayUnit.TEMPERATURE_CELCIUS).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		list.add(ElectroTextUtils.gui("machine.heat", ChatFormatter.getChatDisplayShort((box.heat.getValue() - 27.0) / (3000.0 - 27.0) * 100, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());

		return list;
	}

}