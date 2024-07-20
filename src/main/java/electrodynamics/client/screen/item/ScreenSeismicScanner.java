package electrodynamics.client.screen.item;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.inventory.container.item.ContainerSeismicScanner;
import electrodynamics.common.item.gear.tools.electric.ItemSeismicScanner;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.ScreenComponentMultiLabel;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.object.Location;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ScreenSeismicScanner extends GenericScreen<ContainerSeismicScanner> {

	public ScreenSeismicScanner(ContainerSeismicScanner screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
		addComponent(new ScreenComponentElectricInfo(this::getElectricInformation, -AbstractScreenComponentInfo.SIZE + 1, 2));
		addComponent(new ScreenComponentSimpleLabel(15, 32, 10, 4210752, ElectroTextUtils.gui("seismicscanner.material")));
		addComponent(new ScreenComponentSimpleLabel(85, 25, 10, 4210752, ElectroTextUtils.gui("seismicscanner.dataheader")));
		addComponent(new ScreenComponentMultiLabel(0, 0, graphics -> {
			ItemStack ownerItem = menu.getOwnerItem();

			Location playerLoc = ownerItem.getOrDefault(ElectrodynamicsDataComponentTypes.LOCATION_1, new Location(0, 0, 0));
			Location blockLoc = ownerItem.getOrDefault(ElectrodynamicsDataComponentTypes.LOCATION_2, new Location(0, 0, 0));

			if (blockLoc.equals(playerLoc)) {
				graphics.drawString(font, ElectroTextUtils.gui("seismicscanner.xcoordna"), 95, 35, 4210752, false);
				graphics.drawString(font, ElectroTextUtils.gui("seismicscanner.ycoordna"), 95, 45, 4210752, false);
				graphics.drawString(font, ElectroTextUtils.gui("seismicscanner.zcoordna"), 95, 55, 4210752, false);
			} else {
				graphics.drawString(font, ElectroTextUtils.gui("seismicscanner.xcoord", blockLoc.intX()), 95, 35, 4210752, false);
				graphics.drawString(font, ElectroTextUtils.gui("seismicscanner.ycoord", blockLoc.intY()), 95, 45, 4210752, false);
				graphics.drawString(font, ElectroTextUtils.gui("seismicscanner.zcoord", blockLoc.intZ()), 95, 55, 4210752, false);
			}
		}));

	}

	private List<? extends FormattedCharSequence> getElectricInformation() {
		ArrayList<FormattedCharSequence> list = new ArrayList<>();
		ItemStack ownerItem = menu.getOwnerItem();
		if (ownerItem.getItem() instanceof ItemSeismicScanner scanner) {
			list.add(ElectroTextUtils.gui("machine.usage", ChatFormatter.getChatDisplayShort(ItemSeismicScanner.JOULES_PER_SCAN / 20.0, DisplayUnit.WATT).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
			list.add(ElectroTextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(120, DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
			list.add(ElectroTextUtils.gui("machine.stored", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(scanner.getJoulesStored(ownerItem), DisplayUnit.JOULES), ChatFormatter.getChatDisplayShort(ItemSeismicScanner.JOULES_PER_SCAN * 30, DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		}
		return list;
	}

}
