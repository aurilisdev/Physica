package electrodynamics.common.inventory.container.item;

import electrodynamics.api.capability.types.itemhandler.CapabilityItemStackHandler;
import electrodynamics.common.item.gear.tools.electric.ItemElectricDrill;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.prefab.inventory.container.slot.itemhandler.type.SlotItemHandlerUpgrade;
import electrodynamics.prefab.inventory.container.types.GenericContainerItem;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ContainerElectricDrill extends GenericContainerItem {

	public static final SubtypeItemUpgrade[] VALID_UPGRADES = new SubtypeItemUpgrade[] { SubtypeItemUpgrade.advancedspeed, SubtypeItemUpgrade.basicspeed, SubtypeItemUpgrade.fortune, SubtypeItemUpgrade.silktouch };

	public ContainerElectricDrill(int id, Inventory playerinv, CapabilityItemStackHandler handler, ContainerData data) {
		super(ElectrodynamicsMenuTypes.CONTAINER_ELECTRICDRILL.get(), id, playerinv, handler, data);
	}

	public ContainerElectricDrill(int id, Inventory playerInv) {
		this(id, playerInv, new CapabilityItemStackHandler(ItemElectricDrill.SLOT_COUNT, new ItemStack(Items.COBBLESTONE)), makeDefaultData(1));
	}

	@Override
	public void addInventorySlots(CapabilityItemStackHandler inv, Inventory playerinv) {
		addSlot(new SlotItemHandlerUpgrade(inv, nextIndex(), 30, 35, VALID_UPGRADES));
		addSlot(new SlotItemHandlerUpgrade(inv, nextIndex(), 80, 35, VALID_UPGRADES));
		addSlot(new SlotItemHandlerUpgrade(inv, nextIndex(), 130, 35, VALID_UPGRADES));
	}

}
