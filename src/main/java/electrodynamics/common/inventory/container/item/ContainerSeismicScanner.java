package electrodynamics.common.inventory.container.item;

import electrodynamics.api.capability.types.itemhandler.CapabilityItemStackHandler;
import electrodynamics.common.item.gear.tools.electric.ItemSeismicScanner;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.prefab.inventory.container.slot.itemhandler.type.SlotItemHandlerRestricted;
import electrodynamics.prefab.inventory.container.slot.itemhandler.type.SlotItemHandlerUpgrade;
import electrodynamics.prefab.inventory.container.types.GenericContainerItem;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.IconType;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.SlotType;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ContainerSeismicScanner extends GenericContainerItem {

	public static final SubtypeItemUpgrade[] VALID_UPGRADES = {SubtypeItemUpgrade.range};

	public ContainerSeismicScanner(int id, Inventory playerinv) {
		this(id, playerinv, new CapabilityItemStackHandler(ItemSeismicScanner.SLOT_COUNT, new ItemStack(Items.COBBLESTONE)), makeDefaultData(1));
	}

	public ContainerSeismicScanner(int id, Inventory playerinv, CapabilityItemStackHandler handler, ContainerData data) {
		super(ElectrodynamicsMenuTypes.CONTAINER_SEISMICSCANNER.get(), id, playerinv, handler, data);
	}

	@Override
	public void addInventorySlots(CapabilityItemStackHandler inv, Inventory playerinv) {
		setPlayerInvOffset(60);
		addSlot(new SlotItemHandlerRestricted(SlotType.NORMAL, IconType.NONE, inv, nextIndex(), 21, 36).setRestriction(stack -> (stack != null && stack.getItem() instanceof BlockItem)));
		addSlot(new SlotItemHandlerUpgrade(inv, nextIndex(), 153, 17, VALID_UPGRADES));
		addSlot(new SlotItemHandlerUpgrade(inv, nextIndex(), 153, 37, VALID_UPGRADES));
		addSlot(new SlotItemHandlerUpgrade(inv, nextIndex(), 153, 57, VALID_UPGRADES));
	}

}
