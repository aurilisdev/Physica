package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.tile.electricitygrid.TileCircuitMonitor;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerCircuitMonitor extends GenericContainerBlockEntity<TileCircuitMonitor> {

	public ContainerCircuitMonitor(int id, Inventory playerinv, ContainerData inventorydata) {
		super(ElectrodynamicsMenuTypes.CONTAINER_CIRCUITMONITOR.get(), id, playerinv, EMPTY, inventorydata);
	}

	public ContainerCircuitMonitor(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainerData(3));
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		setPlayerInvOffset(40);
	}

	@Override
	public void addPlayerInventory(Inventory playerinv) {

	}
}
