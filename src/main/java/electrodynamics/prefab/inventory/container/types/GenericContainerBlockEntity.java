package electrodynamics.prefab.inventory.container.types;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class GenericContainerBlockEntity<T extends BlockEntity> extends GenericContainerSlotData<Container> {

	public GenericContainerBlockEntity(MenuType<?> type, int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(type, id, playerinv, inventory, inventorydata);
	}

	@Nullable
	public T getSafeHost() {
		try {
			return getUnsafeHost();
		} catch (Exception e) {
			return null;
		}
	}

	@Nullable
	public T getUnsafeHost() {
		return (T) getLevel().getBlockEntity(new BlockPos(getData().get(0), getData().get(1), getData().get(2)));
	}

	@Override
	public void validateContainer(Container inventory) {
		checkContainerSize(inventory, inventory.getContainerSize());
	}

	@Override
	public boolean stillValid(Player player) {
		return getContainer().stillValid(player);
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		getContainer().stopOpen(player);
	}
}