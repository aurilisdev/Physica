package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.tile.pipelines.gas.TileGasPipePump;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerGasPipePump extends GenericContainerBlockEntity<TileGasPipePump> {

	public ContainerGasPipePump(int id, Inventory playerinv, ContainerData inventorydata) {
		super(ElectrodynamicsMenuTypes.CONTAINER_GASPIPEPUMP.get(), id, playerinv, EMPTY, inventorydata);
	}

	public ContainerGasPipePump(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainerData(3));
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {

	}
}
