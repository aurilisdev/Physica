package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.tile.machines.charger.GenericTileCharger;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotCharging;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotRestricted;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerChargerGeneric extends GenericContainerBlockEntity<GenericTileCharger> {

	public ContainerChargerGeneric(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(5), new SimpleContainerData(3));
	}

	public ContainerChargerGeneric(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(ElectrodynamicsMenuTypes.CONTAINER_CHARGER.get(), id, playerinv, inventory, inventorydata);
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		addSlot(new SlotCharging(inv, nextIndex(), 95, 34).setIOColor(new Color(0, 240, 255, 255)));
		addSlot(new SlotCharging(inv, nextIndex(), 95, 54));
		addSlot(new SlotCharging(inv, nextIndex(), 115, 54));
		addSlot(new SlotCharging(inv, nextIndex(), 135, 54));
		addSlot(new SlotRestricted(inv, nextIndex(), 145, 34).setIOColor(new Color(255, 0, 0, 255)));
	}

}
