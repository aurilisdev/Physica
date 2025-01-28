package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.tile.machines.quarry.TileSeismicRelay;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotRestricted;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerSeismicRelay extends GenericContainerBlockEntity<TileSeismicRelay> {

	public ContainerSeismicRelay(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(ElectrodynamicsMenuTypes.CONTAINER_SEISMICRELAY.get(), id, playerinv, inventory, inventorydata);
	}

	public ContainerSeismicRelay(int id, Inventory playerinv) {
		super(ElectrodynamicsMenuTypes.CONTAINER_SEISMICRELAY.get(), id, playerinv, new SimpleContainer(1), new SimpleContainerData(3));
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		addSlot(new SlotRestricted(inv, nextIndex(), 20, 34));
	}

}
