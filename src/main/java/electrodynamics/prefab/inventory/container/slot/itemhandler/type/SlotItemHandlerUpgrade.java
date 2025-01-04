package electrodynamics.prefab.inventory.container.slot.itemhandler.type;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.prefab.inventory.container.slot.itemhandler.SlotItemHandlerGeneric;
import electrodynamics.prefab.inventory.container.slot.utils.IUpgradeSlot;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.IconType;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.SlotType;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class SlotItemHandlerUpgrade extends SlotItemHandlerGeneric implements IUpgradeSlot {

	private final List<Item> items = new ArrayList<>();

	public SlotItemHandlerUpgrade(IItemHandler itemHandler, int index, int xPosition, int yPosition, SubtypeItemUpgrade... upgrades) {
		super(SlotType.NORMAL, IconType.UPGRADE_DARK, itemHandler, index, xPosition, yPosition);

		items.clear();
		for (SubtypeItemUpgrade upg : upgrades) {
			items.add(ElectrodynamicsItems.ITEMS_UPGRADE.getValue(upg));
		}

	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return items.contains(stack.getItem());
	}

	@Override
	public List<Item> getUpgrades() {
		return items;
	}

}
