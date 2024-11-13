package electrodynamics.prefab.inventory.container.slot.itemhandler;

import electrodynamics.api.screen.ITexture;
import electrodynamics.api.screen.component.ISlotTexture;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.IconType;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.SlotType;
import electrodynamics.prefab.screen.component.utils.SlotTextureProvider;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerCopySlot;

public class SlotItemHandlerGeneric extends ItemHandlerCopySlot implements SlotTextureProvider {

	private final ISlotTexture slotType;
	private final ITexture iconType;

	private boolean active = true;

	public SlotItemHandlerGeneric(ISlotTexture slotType, ITexture iconType, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.slotType = slotType;
		this.iconType = iconType;
	}

	public SlotItemHandlerGeneric(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		this(SlotType.NORMAL, IconType.NONE, itemHandler, index, xPosition, yPosition);
	}

	@Override
	public ISlotTexture getSlotType() {
		return slotType;
	}

	@Override
	public ITexture getIconType() {
		return iconType;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
