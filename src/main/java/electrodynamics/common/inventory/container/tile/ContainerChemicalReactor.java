package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.tile.machines.chemicalreactor.TileChemicalReactor;
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

public class ContainerChemicalReactor extends GenericContainerBlockEntity<TileChemicalReactor> {

    public static final SubtypeItemUpgrade[] VALID_UPGRADES = new SubtypeItemUpgrade[] { SubtypeItemUpgrade.advancedspeed, SubtypeItemUpgrade.basicspeed, SubtypeItemUpgrade.iteminput, SubtypeItemUpgrade.itemoutput, SubtypeItemUpgrade.experience };

    public ContainerChemicalReactor(int id, Inventory playerinv) {
        this(id, playerinv, new SimpleContainer(17), new SimpleContainerData(3));
    }

    public ContainerChemicalReactor(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
        super(ElectrodynamicsMenuTypes.CONTAINER_CHEMICALREACTOR.get(), id, playerinv, inventory, inventorydata);
    }

    @Override
    public void addInventorySlots(Container inv, Inventory playerinv) {
        setPlayerInvOffset(35);
        addSlot(new SlotGeneric(inv, nextIndex(), 46, 42).setIOColor(new Color(0, 255, 30, 255)));
        addSlot(new SlotGeneric(inv, nextIndex(), 46 , 62).setIOColor(new Color(144, 0, 255, 255)));
        addSlot(new SlotRestricted(inv, nextIndex(), 92, 22).setIOColor(new Color(255, 0, 0, 255)));
        addSlot(new SlotRestricted(inv, nextIndex(), 92, 42).setIOColor(new Color(255, 255, 0, 255)));
        addSlot(new SlotRestricted(inv, nextIndex(), 92, 62).setIOColor(new Color(255, 255, 0, 255)));
        addSlot(new SlotRestricted(inv, nextIndex(), 92, 82).setIOColor(new Color(255, 255, 0, 255)));
        //addSlot(new SlotFluid(inv, nextIndex(), 27, 19));
        //addSlot(new SlotFluid(inv, nextIndex(), 27, 50));
        //addSlot(new SlotFluid(inv, nextIndex(), 113, 19));
        //addSlot(new SlotFluid(inv, nextIndex(), 113, 50));
        //addSlot(new SlotGas(inv, nextIndex(), 27, 73));
        //addSlot(new SlotGas(inv, nextIndex(), 27, 104));
        //addSlot(new SlotGas(inv, nextIndex(), 113, 73));
        //addSlot(new SlotGas(inv, nextIndex(), 113, 104));
        addSlot(new SlotUpgrade(inv, nextIndex(8), 153, 22, VALID_UPGRADES));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 42, VALID_UPGRADES));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 62, VALID_UPGRADES));
    }
}
