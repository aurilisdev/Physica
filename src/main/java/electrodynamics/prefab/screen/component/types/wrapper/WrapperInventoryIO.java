package electrodynamics.prefab.screen.component.types.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.button.ScreenComponentButton;
import electrodynamics.prefab.screen.component.types.ScreenComponentInventoryIO;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGuiTab;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public class WrapperInventoryIO {

	private ScreenComponentInventoryIO[] ioArr = new ScreenComponentInventoryIO[6];

	private ScreenComponentSimpleLabel label;

	public ScreenComponentButton<?> button;

	private final GenericScreen<?> screen;

	private final BiFunction<SlotGeneric, Integer, Color> defaultColorSupplier;

	private Consumer<Boolean> additionalToHide = show -> {};

	public WrapperInventoryIO(GenericScreen<?> screen, int tabX, int tabY, int slotStartX, int slotStartY, int labelX, int labelY) {
		this(screen, tabX, tabY, slotStartX, slotStartY, labelX, labelY, (slot, index) -> Color.WHITE);
	}

	public WrapperInventoryIO(GenericScreen<?> screen, int tabX, int tabY, int slotStartX, int slotStartY, int labelX, int labelY, BiFunction<SlotGeneric, Integer, Color> defaultColorSupplier) {
		this.screen = screen;
		this.defaultColorSupplier = defaultColorSupplier;
		screen.addComponent(button = (ScreenComponentButton<?>) new ScreenComponentButton<>(ScreenComponentGuiTab.GuiInfoTabTextures.REGULAR, tabX, tabY).setOnPress(button -> {
			//
			button.isPressed = !button.isPressed;

			if (button.isPressed) {

				additionalToHide.accept(false);

				this.screen.playerInvLabel.setVisible(false);

				setColoredSlots();

				updateVisibility(true);

			} else {
				additionalToHide.accept(true);

				this.screen.playerInvLabel.setVisible(true);

				resetSlots();

				updateVisibility(false);
			}

		}).onTooltip((graphics, but, xAxis, yAxis) -> {
			//
			ScreenComponentButton<?> button = (ScreenComponentButton<?>) but;
			List<Component> tooltips = new ArrayList<>();
			tooltips.add(ElectroTextUtils.tooltip("inventoryio").withStyle(ChatFormatting.DARK_GRAY));
			if (!button.isPressed) {
				tooltips.add(ElectroTextUtils.tooltip("inventoryio.presstoshow").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
			} else {
				tooltips.add(ElectroTextUtils.tooltip("inventoryio.presstohide").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
			}

			graphics.renderComponentTooltip(this.screen.getFontRenderer(), tooltips, xAxis, yAxis);

		}).setIcon(ScreenComponentSlot.IconType.INVENTORY_IO));

		this.screen.addComponent(label = new ScreenComponentSimpleLabel(labelX, labelY, 10, Color.TEXT_GRAY, ElectroTextUtils.tooltip("inventoryio.slotmap")));

		label.setVisible(false);

		this.screen.addComponent(ioArr[0] = new ScreenComponentInventoryIO(slotStartX, slotStartY+1, Direction.UP));
		this.screen.addComponent(ioArr[1] = new ScreenComponentInventoryIO(slotStartX, slotStartY + 26, Direction.NORTH));
		this.screen.addComponent(ioArr[2] = new ScreenComponentInventoryIO(slotStartX, slotStartY + 26 * 2 - 1, Direction.DOWN));
		this.screen.addComponent(ioArr[3] = new ScreenComponentInventoryIO(slotStartX - 25, slotStartY + 26, Direction.EAST));
		this.screen.addComponent(ioArr[4] = new ScreenComponentInventoryIO(slotStartX + 25, slotStartY + 26, Direction.WEST));
		this.screen.addComponent(ioArr[5] = new ScreenComponentInventoryIO(slotStartX + 25, slotStartY + 26 * 2 - 1, Direction.SOUTH));

		for (ScreenComponentInventoryIO io : ioArr) {
			io.setVisible(false);
		}
	}

	public WrapperInventoryIO hideAdditional(Consumer<Boolean> additionalToHide){
		this.additionalToHide = additionalToHide;
		return this;
	}

	public void updateVisibility(boolean show) {
		for (ScreenComponentInventoryIO io : ioArr) {
			io.setVisible(show);
		}

		label.setVisible(show);
	}

	public void setColoredSlots() {
		for (int i = this.screen.getMenu().getAdditionalSlotCount(); i < this.screen.getMenu().slots.size(); i++) {

			((SlotGeneric) this.screen.getMenu().slots.get(i)).setActive(false);

		}

		for (int i = 0; i < this.screen.getMenu().getAdditionalSlotCount(); i++) {

			SlotGeneric generic = (SlotGeneric) this.screen.getMenu().slots.get(i);

			if (generic.ioColor != null) {

				this.screen.slots.get(i).setColor(generic.ioColor);

			}

		}
	}

	public void resetSlots() {
		for (int i = this.screen.getMenu().getAdditionalSlotCount(); i < this.screen.getMenu().slots.size(); i++) {

			((SlotGeneric) this.screen.getMenu().slots.get(i)).setActive(true);

		}

		for (int i = 0; i < this.screen.getMenu().getAdditionalSlotCount(); i++) {

			SlotGeneric generic = (SlotGeneric) this.screen.getMenu().slots.get(i);

			if (generic.ioColor != null) {

				this.screen.slots.get(i).setColor(this.defaultColorSupplier.apply(generic, i));

			}

		}
	}

}
