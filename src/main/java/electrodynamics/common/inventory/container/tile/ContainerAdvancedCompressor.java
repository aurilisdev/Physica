package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.tile.pipelines.gas.gastransformer.compressor.GenericTileAdvancedCompressor;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotGas;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotUpgrade;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerAdvancedCompressor extends GenericContainerBlockEntity<GenericTileAdvancedCompressor.TileAdvancedCompressor> {

    public static final SubtypeItemUpgrade[] VALID_UPGRADES = new SubtypeItemUpgrade[] { SubtypeItemUpgrade.advancedspeed, SubtypeItemUpgrade.basicspeed };

    public ContainerAdvancedCompressor(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
        super(ElectrodynamicsMenuTypes.CONTAINER_ADVANCEDCOMPRESSOR.get(), id, playerinv, inventory, inventorydata);
    }

    public ContainerAdvancedCompressor(int id, Inventory playerInv) {
        this(id, playerInv, new SimpleContainer(5), new SimpleContainerData(3));
    }

    @Override
    public void addInventorySlots(Container inv, Inventory playerinv) {
        setPlayerInvOffset(47);
        addSlot(new SlotGas(inv, nextIndex(), 20, 50));
        addSlot(new SlotGas(inv, nextIndex(), 109, 50));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 14, VALID_UPGRADES));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 34, VALID_UPGRADES));
        addSlot(new SlotUpgrade(inv, nextIndex(), 153, 54, VALID_UPGRADES));
    }

}
