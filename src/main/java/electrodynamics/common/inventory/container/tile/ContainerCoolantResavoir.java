package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.tile.machines.quarry.TileCoolantResavoir;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotFluid;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerCoolantResavoir extends GenericContainerBlockEntity<TileCoolantResavoir> {

	public ContainerCoolantResavoir(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(ElectrodynamicsMenuTypes.CONTAINER_COOLANTRESAVOIR.get(), id, playerinv, inventory, inventorydata);
	}

	public ContainerCoolantResavoir(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(1), new SimpleContainerData(3));
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		addSlot(new SlotFluid(inv, nextIndex(), 47, 34));
	}

}
