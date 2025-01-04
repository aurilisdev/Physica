package electrodynamics.prefab.inventory.container.slot.item.type;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import electrodynamics.prefab.inventory.container.slot.utils.IUpgradeSlot;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.IconType;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.SlotType;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SlotUpgrade extends SlotGeneric implements IUpgradeSlot {

	private final List<Item> items = new ArrayList<>();

	public SlotUpgrade(Container inventory, int index, int x, int y, SubtypeItemUpgrade... upgrades) {
		super(SlotType.NORMAL, IconType.UPGRADE_DARK, inventory, index, x, y);

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
