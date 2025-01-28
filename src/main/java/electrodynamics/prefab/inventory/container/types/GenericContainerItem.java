package electrodynamics.prefab.inventory.container.types;

import javax.annotation.Nullable;

import electrodynamics.api.capability.types.itemhandler.CapabilityItemStackHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public abstract class GenericContainerItem extends GenericContainerSlotData<CapabilityItemStackHandler> {

    /**
     * Documentation note here: DO NOT use ItemStack.EMPTY for the dummy handler created on the client. The empty
     * ItemStack cannot store data components. Use a piece of cobblestone or something generic like that!
     */
    public GenericContainerItem(MenuType<?> type, int id, Inventory playerinv, CapabilityItemStackHandler handler, ContainerData data) {
        super(type, id, playerinv, handler, data);
        getContainer().setLevelAccess(playerinv.player.level(), playerinv.player.getOnPos());
    }

    @Override
    public void validateContainer(CapabilityItemStackHandler inventory) {

    }

    @Override
    public void clicked(int slot, int button, ClickType type, Player pl) {

        Inventory playerinv = pl.getInventory();

        ItemStack owner = getOwnerItem();

        if (owner.isEmpty() || (slot >= 0 && slot <= pl.getInventory().getContainerSize() - 1 && ItemStack.isSameItem(playerinv.getItem(slot), owner))) {
            return;
        }

        super.clicked(slot, button, type, pl);
    }

    @Override
    public boolean stillValid(Player player) {

        return !getOwnerItem().isEmpty();
    }


    /**
     * Retrieves the item that owns this container
     *
     * @return Empty if not found
     */
    public ItemStack getOwnerItem() {

        if (getData().getCount() == 0 || getData().get(0) == -1) {
            return ItemStack.EMPTY;
        }

        try {
            return getPlayer().getItemInHand(InteractionHand.values()[getData().get(0)]);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }

    }

    public @Nullable InteractionHand getHand() {
        if (getData().getCount() == 0 || getData().get(0) == -1) {
            return null;
        }

        try {
            return InteractionHand.values()[getData().get(0)];
        } catch (Exception e) {
            return null;
        }
    }

    public static SimpleContainerData makeDefaultData(int size) {
        SimpleContainerData data = new SimpleContainerData(size);
        for (int i = 0; i < size; i++) {
            data.set(i, -1);
        }
        return data;
    }

    public static SimpleContainerData makeData(InteractionHand hand) {
        SimpleContainerData data = new SimpleContainerData(1);
        data.set(0, hand.ordinal());
        return data;
    }

}
