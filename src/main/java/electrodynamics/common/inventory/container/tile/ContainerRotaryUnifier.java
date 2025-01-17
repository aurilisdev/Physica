package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.tile.compatibility.TileRotaryUnifier;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotUpgrade;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerRotaryUnifier extends GenericContainerBlockEntity<TileRotaryUnifier> {

    public static final SubtypeItemUpgrade[] VALID_UPGRADES = new SubtypeItemUpgrade[] { SubtypeItemUpgrade.advancedspeed, SubtypeItemUpgrade.basicspeed };

    public ContainerRotaryUnifier(int id, Inventory playerinva) {
        this(id, playerinva, new SimpleContainer(3), new SimpleContainerData(3));
    }

    public ContainerRotaryUnifier(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
        super(ElectrodynamicsMenuTypes.CONTAINER_ROTARYUNIFIER.get(), id, playerinv, inventory, inventorydata);
    }

    @Override
    public void addInventorySlots(Container inv, Inventory playerinv) {

        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 14, VALID_UPGRADES));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 34, VALID_UPGRADES));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 54, VALID_UPGRADES));

    }
}
