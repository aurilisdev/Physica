package electrodynamics.client.screen.tile;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.inventory.container.tile.ContainerBatteryBox;
import electrodynamics.common.tile.electricitygrid.batteries.TileBatteryBox;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.ScreenComponentMultiLabel;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenBatteryBox extends GenericScreen<ContainerBatteryBox> {
	public ScreenBatteryBox(ContainerBatteryBox container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		addComponent(new ScreenComponentElectricInfo(this::getElectricInformation, -AbstractScreenComponentInfo.SIZE + 1, 2));
		addComponent(new ScreenComponentMultiLabel(0, 0, graphics -> {
			TileBatteryBox box = menu.getSafeHost();
			if (box == null) {
				return;
			}
			ComponentElectrodynamic electro = box.getComponent(IComponentType.Electrodynamic);
			graphics.drawString(font, ElectroTextUtils.gui("machine.current", ChatFormatter.getChatDisplayShort(box.powerOutput.get() * 20.0 * box.currentCapacityMultiplier.get() / electro.getVoltage(), DisplayUnit.AMPERE)), inventoryLabelX, inventoryLabelY - 55, 4210752, false);
			graphics.drawString(font, ElectroTextUtils.gui("machine.transfer", ChatFormatter.getChatDisplayShort(box.powerOutput.get() * 20.0 * box.currentCapacityMultiplier.get(), DisplayUnit.WATT)), inventoryLabelX, inventoryLabelY - 42, 4210752, false);
			graphics.drawString(font, ElectroTextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnit.VOLTAGE)), inventoryLabelX, inventoryLabelY - 29, 4210752, false);
			graphics.drawString(font, ElectroTextUtils.gui("machine.stored", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(electro.getJoulesStored(), DisplayUnit.JOULES), ChatFormatter.getChatDisplayShort(box.maxJoules.get() * box.currentCapacityMultiplier.get(), DisplayUnit.JOULES))), inventoryLabelX, inventoryLabelY - 16, 4210752, false);
		}));
	}

	private List<? extends FormattedCharSequence> getElectricInformation() {
		ArrayList<FormattedCharSequence> list = new ArrayList<>();
		TileBatteryBox box = menu.getSafeHost();
		if (box == null) {
			return list;
		}

		ComponentElectrodynamic el = box.getComponent(IComponentType.Electrodynamic);
		list.add(ElectroTextUtils.gui("machine.current", ChatFormatter.getChatDisplayShort(box.powerOutput.get() * 20.0 * box.currentCapacityMultiplier.get() / el.getVoltage(), DisplayUnit.AMPERE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		list.add(ElectroTextUtils.gui("machine.transfer", ChatFormatter.getChatDisplayShort(box.powerOutput.get() * 20.0 * box.currentCapacityMultiplier.get(), DisplayUnit.WATT).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		list.add(ElectroTextUtils.gui("machine.transfer", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(box.powerOutput.get() * box.currentCapacityMultiplier.get(), DisplayUnit.FORGE_ENERGY_UNIT).withStyle(ChatFormatting.GRAY), DisplayUnit.TIME_TICKS.getSymbol())).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		list.add(ElectroTextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(el.getVoltage(), DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		list.add(ElectroTextUtils.gui("machine.stored", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(el.getJoulesStored(), DisplayUnit.JOULES), ChatFormatter.getChatDisplayShort(box.maxJoules.get() * box.currentCapacityMultiplier.get(), DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());

		return list;
	}

}