package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.tile.machines.TileElectrolosisChamber;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerElectrolosisChamber extends GenericContainerBlockEntity<TileElectrolosisChamber> {

    public ContainerElectrolosisChamber(int id, Inventory playerinv) {
        this(id, playerinv, new SimpleContainer(0), new SimpleContainerData(3));
    }
    public ContainerElectrolosisChamber(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
        super(ElectrodynamicsMenuTypes.CONTAINER_ELECTROLOSISCHAMBER.get(), id, playerinv, inventory, inventorydata);
    }

    @Override
    public void addInventorySlots(Container inv, Inventory playerinv) {

    }
}
