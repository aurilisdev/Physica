package electrodynamics.prefab.inventory.container.types;

import electrodynamics.prefab.inventory.container.GenericContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;

public abstract class GenericContainerSlotData<CONTAINERTYPE> extends GenericContainer<CONTAINERTYPE> {

    private final ContainerData data;

    public GenericContainerSlotData(MenuType<?> type, int id, Inventory playerinv, CONTAINERTYPE inventory, ContainerData data) {
        super(type, id, playerinv, inventory);
        checkContainerDataCount(data, data.getCount());
        this.data = data;
        addDataSlots(this.data);
    }

    public ContainerData getData() {
        return data;
    }
}
