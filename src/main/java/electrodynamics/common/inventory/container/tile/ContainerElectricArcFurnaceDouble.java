package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.tile.machines.arcfurnace.TileElectricArcFurnaceDouble;
import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotRestricted;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotUpgrade;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerElectricArcFurnaceDouble extends GenericContainerBlockEntity<TileElectricArcFurnaceDouble> {

	public ContainerElectricArcFurnaceDouble(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(7) ,new SimpleContainerData(3));
	}

	public ContainerElectricArcFurnaceDouble(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(ElectrodynamicsMenuTypes.CONTAINER_ELECTRICARCFURNACEDOUBLE.get(), id, playerinv, inventory, inventorydata);
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		addSlot(new SlotGeneric(inv, nextIndex(), 56, 24).setIOColor(new Color(0, 240, 255, 255)));
		addSlot(new SlotGeneric(inv, nextIndex(), 56, 44).setIOColor(new Color(0, 240, 255, 255)));
		addSlot(new SlotRestricted(inv, nextIndex(), 116, 24).setIOColor(new Color(255, 0, 0, 255)));
		addSlot(new SlotRestricted(inv, nextIndex(), 116, 44).setIOColor(new Color(255, 0, 0, 255)));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 14, ContainerElectricArcFurnace.VALID_UPGRADES));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 34, ContainerElectricArcFurnace.VALID_UPGRADES));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 54, ContainerElectricArcFurnace.VALID_UPGRADES));
	}
}
