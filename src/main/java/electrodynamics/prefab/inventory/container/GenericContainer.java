package electrodynamics.prefab.inventory.container;

import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import electrodynamics.prefab.utilities.ContainerUtils;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class GenericContainer<CONTAINERTYPE> extends AbstractContainerMenu {

	public static final SimpleContainer EMPTY = new SimpleContainer(0);

	private final CONTAINERTYPE inventory;
	private final Level world;
	private final Inventory playerinv;
	private final Player player;
	private final int slotCount;
	private int playerInvOffset = 0;
	private int nextIndex = 0;

	public int nextIndex() {
		return nextIndex++;
	}

	public int nextIndex(int offset){
		nextIndex += offset;
		return nextIndex ++;
	}

	public GenericContainer(MenuType<?> type, int id, Inventory playerinv, CONTAINERTYPE inventory) {
		super(type, id);
		validateContainer(inventory);
		this.inventory = inventory;
		this.playerinv = playerinv;
		this.player = playerinv.player;
		this.world = playerinv.player.level();
		addInventorySlots(inventory, playerinv);
		this.slotCount = slots.size();
		addPlayerInventory(playerinv);
	}

	public void addPlayerInventory(Inventory playerinv) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlot(new SlotGeneric(playerinv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + getPlayerInvOffset()));
			}
		}

		for (int k = 0; k < 9; ++k) {
			addSlot(new SlotGeneric(playerinv, k, 8 + k * 18, 142 + getPlayerInvOffset()));
		}
	}


	public abstract void validateContainer(CONTAINERTYPE inventory);

	public abstract void addInventorySlots(CONTAINERTYPE inv, Inventory playerinv);

	public void setPlayerInvOffset(int offset) {
		playerInvOffset = offset;
	}

	public CONTAINERTYPE getContainer() {
		return inventory;
	}

	public Level getLevel() {
		return world;
	}

	public Inventory getPlayerInventory() {
		return playerinv;
	}

	public Player getPlayer() {
		return player;
	}

	public int getAdditionalSlotCount() {
		return slotCount;
	}

	public int getPlayerInvOffset() {
		return playerInvOffset;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ContainerUtils.handleShiftClick(slots, player, index);
	}

}
