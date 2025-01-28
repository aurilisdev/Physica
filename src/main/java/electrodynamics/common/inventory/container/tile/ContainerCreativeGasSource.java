package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.tile.pipelines.gas.TileCreativeGasSource;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotGas;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerCreativeGasSource extends GenericContainerBlockEntity<TileCreativeGasSource> {

    public ContainerCreativeGasSource(int id, Inventory playerinv) {
        this(id, playerinv, new SimpleContainer(2), new SimpleContainerData(3));
    }

    public ContainerCreativeGasSource(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
        super(ElectrodynamicsMenuTypes.CONTAINER_CREATIVEGASSOURCE.get(), id, playerinv, inventory, inventorydata);
    }

    @Override
    public void addInventorySlots(Container inv, Inventory playerinv) {
        addSlot(new SlotGas(inv, nextIndex(), 58, 34));
        addSlot(new SlotGas(inv, nextIndex(), 133, 34));
    }
}
