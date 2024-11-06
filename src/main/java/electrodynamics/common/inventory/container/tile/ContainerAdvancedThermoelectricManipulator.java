package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.tile.pipelines.gas.gastransformer.thermoelectricmanipulator.TileAdvancedThermoelectricManipulator;
import electrodynamics.prefab.inventory.container.GenericContainerBlockEntity;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotFluid;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotGas;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotUpgrade;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerAdvancedThermoelectricManipulator extends GenericContainerBlockEntity<TileAdvancedThermoelectricManipulator> {

    public static final SubtypeItemUpgrade[] VALID_UPGRADES = new SubtypeItemUpgrade[] { SubtypeItemUpgrade.advancedspeed, SubtypeItemUpgrade.basicspeed };

    public ContainerAdvancedThermoelectricManipulator(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
        super(ElectrodynamicsMenuTypes.CONTAINER_ADVANCED_THERMOELECTRICMANIPULATOR.get(), id, playerinv, inventory, inventorydata);
    }

    public ContainerAdvancedThermoelectricManipulator(int id, Inventory playerinv) {
        this(id, playerinv, new SimpleContainer(7), new SimpleContainerData(3));
    }

    @Override
    public void addInventorySlots(Container inv, Inventory playerinv) {
        playerInvOffset = 30;
        addSlot(new SlotFluid(inv, nextIndex(), 27, 19));
        addSlot(new SlotFluid(inv, nextIndex(), 113, 19));
        addSlot(new SlotGas(inv, nextIndex(), 27, 50));
        addSlot(new SlotGas(inv, nextIndex(), 113, 50));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 14, VALID_UPGRADES));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 34, VALID_UPGRADES));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 54, VALID_UPGRADES));
    }

}
