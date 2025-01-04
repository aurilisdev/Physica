package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.tile.pipelines.gas.TileGasPipeFilter;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerGasPipeFilter extends GenericContainerBlockEntity<TileGasPipeFilter> {

	public ContainerGasPipeFilter(int id, Inventory playerinv, ContainerData inventorydata) {
		super(ElectrodynamicsMenuTypes.CONTAINER_GASPIPEFILTER.get(), id, playerinv, EMPTY, inventorydata);
	}

	public ContainerGasPipeFilter(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainerData(3));
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		setPlayerInvOffset(20);
	}

}
