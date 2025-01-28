package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.tile.pipelines.gas.TileGasCollector;
import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotGas;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotUpgrade;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerGasCollector extends GenericContainerBlockEntity<TileGasCollector> {

    public static final SubtypeItemUpgrade[] VALID_UPGRADES = new SubtypeItemUpgrade[]{SubtypeItemUpgrade.advancedspeed, SubtypeItemUpgrade.basicspeed};

    public ContainerGasCollector(int id, Inventory playerinv) {
        this(id, playerinv, new SimpleContainer(5), new SimpleContainerData(3));
    }

    public ContainerGasCollector(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
        super(ElectrodynamicsMenuTypes.CONTAINER_GASCOLLECTOR.get(), id, playerinv, inventory, inventorydata);
    }

    @Override
    public void addInventorySlots(Container inv, Inventory playerinv) {
        addSlot(new SlotGeneric(inv, nextIndex(), 25, 34).setIOColor(new Color(0, 240, 255, 255)));
        addSlot(new SlotGas(inv, nextIndex(), 121, 51));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 14, VALID_UPGRADES));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 34, VALID_UPGRADES));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 54, VALID_UPGRADES));
    }
}
