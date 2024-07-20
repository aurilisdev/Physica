package electrodynamics.common.inventory.container.item;

import electrodynamics.api.capability.types.itemhandler.CapabilityItemStackHandler;
import electrodynamics.common.item.gear.tools.electric.ItemSeismicScanner;
import electrodynamics.prefab.inventory.container.GenericContainerItem;
import electrodynamics.prefab.inventory.container.slot.itemhandler.type.SlotItemHandlerRestricted;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.IconType;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.SlotType;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ContainerSeismicScanner extends GenericContainerItem {

	public ContainerSeismicScanner(int id, Inventory playerinv) {
		this(id, playerinv, new CapabilityItemStackHandler(ItemSeismicScanner.SLOT_COUNT, ItemStack.EMPTY));
	}

	public ContainerSeismicScanner(int id, Inventory playerinv, CapabilityItemStackHandler handler) {
		super(ElectrodynamicsMenuTypes.CONTAINER_SEISMICSCANNER.get(), id, playerinv, handler);
	}

	@Override
	public void addItemInventorySlots(Container inv, Inventory playerinv) {
		if (getHandler() == null) {
			return;
		}
		addSlot(new SlotItemHandlerRestricted(SlotType.NORMAL, IconType.NONE, getHandler(), nextIndex(), 25, 42).setRestriction(stack -> (stack != null && stack.getItem() instanceof BlockItem)));
	}

}
