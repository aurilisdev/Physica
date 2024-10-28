package electrodynamics.prefab.screen.component.types.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.button.type.ButtonInventoryIOView;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.screen.component.types.io.ScreenComponentInventoryIO;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public class InventoryIOWrapper {

	private ScreenComponentInventoryIO[] ioArr = new ScreenComponentInventoryIO[6];

	private ScreenComponentSimpleLabel label;

	private final GenericScreen<?> screen;

	private final BiFunction<SlotGeneric, Integer, Color> defaultColorSupplier;

	public InventoryIOWrapper(GenericScreen<?> screen, int tabX, int tabY, int slotStartX, int slotStartY, int labelX, int labelY) {
		this(screen, tabX, tabY, slotStartX, slotStartY, labelX, labelY, (slot, index) -> Color.WHITE);
	}

	public InventoryIOWrapper(GenericScreen<?> screen, int tabX, int tabY, int slotStartX, int slotStartY, int labelX, int labelY, BiFunction<SlotGeneric, Integer, Color> defaultColorSupplier) {
		this.screen = screen;
		this.defaultColorSupplier = defaultColorSupplier;
		screen.addComponent(new ButtonInventoryIOView(tabX, tabY).setOnPress(but -> {
			//
			ButtonInventoryIOView button = (ButtonInventoryIOView) but;
			button.isPressed = !button.isPressed;

			if (button.isPressed) {

				this.screen.playerInvLabel.setVisible(false);

				for (int i = this.screen.getMenu().slotCount; i < this.screen.getMenu().slots.size(); i++) {

					((SlotGeneric) this.screen.getMenu().slots.get(i)).setActive(false);

				}

				for (int i = 0; i < this.screen.getMenu().slotCount; i++) {

					SlotGeneric generic = (SlotGeneric) this.screen.getMenu().slots.get(i);

					if (generic.ioColor != null) {

						this.screen.slots.get(i).setColor(generic.ioColor);

					}

				}

				for (ScreenComponentInventoryIO io : ioArr) {
					io.setVisible(true);
				}

				label.setVisible(true);

			} else {

				this.screen.playerInvLabel.setVisible(true);

				for (int i = this.screen.getMenu().slotCount; i < this.screen.getMenu().slots.size(); i++) {

					((SlotGeneric) this.screen.getMenu().slots.get(i)).setActive(true);

				}

				for (int i = 0; i < this.screen.getMenu().slotCount; i++) {

					SlotGeneric generic = (SlotGeneric) this.screen.getMenu().slots.get(i);

					if (generic.ioColor != null) {

						this.screen.slots.get(i).setColor(this.defaultColorSupplier.apply(generic, i));

					}

				}

				for (ScreenComponentInventoryIO io : ioArr) {
					io.setVisible(false);
				}

				label.setVisible(false);

			}

		}).onTooltip((graphics, but, xAxis, yAxis) -> {
			//
			ButtonInventoryIOView button = (ButtonInventoryIOView) but;
			List<Component> tooltips = new ArrayList<>();
			tooltips.add(ElectroTextUtils.tooltip("inventoryio").withStyle(ChatFormatting.DARK_GRAY));
			if (!button.isPressed) {
				tooltips.add(ElectroTextUtils.tooltip("inventoryio.presstoshow").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
			} else {
				tooltips.add(ElectroTextUtils.tooltip("inventoryio.presstohide").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
			}

			graphics.renderComponentTooltip(this.screen.getFontRenderer(), tooltips, xAxis, yAxis);

		}));

		this.screen.addComponent(label = new ScreenComponentSimpleLabel(labelX, labelY, 10, 4210752, ElectroTextUtils.tooltip("inventoryio.slotmap")));

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

}
