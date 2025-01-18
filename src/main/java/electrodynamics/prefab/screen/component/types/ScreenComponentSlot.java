package electrodynamics.prefab.screen.component.types;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.Electrodynamics;
import electrodynamics.api.screen.ITexture;
import electrodynamics.api.screen.component.ISlotTexture;
import electrodynamics.api.screen.component.TextSupplier;
import electrodynamics.prefab.inventory.container.slot.utils.IUpgradeSlot;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenComponentSlot extends ScreenComponentGeneric {

	private final ISlotTexture slotType;
	private final ITexture iconType;
	private TextSupplier tooltip;

	private final Slot slot;

	public ScreenComponentSlot(Slot slot, ISlotTexture slotType, ITexture iconType, int x, int y) {
		super(slotType, x, y);
		this.slotType = slotType;
		this.iconType = iconType;
		this.slot = slot;
		onTooltip((graphics, component, xAxis, yAxis) -> {
			if(tooltip == null){
				return;
			}
			graphics.renderTooltip(gui.getFontRenderer(), tooltip.getText(), xAxis, yAxis);
		});
	}

	public ScreenComponentSlot(Slot slot, int x, int y) {
		this(slot, SlotType.NORMAL, IconType.NONE, x, y);
	}

	public ScreenComponentSlot(Slot slot, ISlotTexture slotType, ITexture iconType, int x, int y, TextSupplier tooltip) {
		this(slot, slotType, iconType, x, y);
		this.tooltip = tooltip;
	}

	public void tooltip(TextSupplier tooltip) {
		this.tooltip = tooltip;
	}

	public ScreenComponentSlot setHoverText(Slot slot) {
		tooltip = () -> slot.getItem().isEmpty() ? Component.empty() : slot.getItem().getHoverName();
		return this;
	}

	@Override
	public void renderBackground(GuiGraphics graphics, final int xAxis, final int yAxis, final int guiWidth, final int guiHeight) {
		super.renderBackground(graphics, xAxis, yAxis, guiWidth, guiHeight);
		if (iconType == IconType.NONE) {
			return;
		}
		int slotXOffset = (slotType.imageWidth() - iconType.imageWidth()) / 2;
		int slotYOffset = (slotType.imageHeight() - iconType.imageHeight()) / 2;
		graphics.blit(iconType.getLocation(), guiWidth + xLocation + slotXOffset, guiHeight + yLocation + slotYOffset, iconType.textureU(), iconType.textureV(), iconType.textureWidth(), iconType.textureHeight(), iconType.imageWidth(), iconType.imageHeight());
	}

	@Override
	public void renderForeground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
		if (!slot.isActive()) {
			return;
		}
		super.renderForeground(graphics, xAxis, yAxis, guiWidth, guiHeight);
		if (isHoveredOrFocused()) {

			if (Screen.hasControlDown() && slot instanceof IUpgradeSlot upgrade) {

				List<FormattedCharSequence> tooltips = new ArrayList<>();
				tooltips.add(ElectroTextUtils.tooltip("validupgrades").getVisualOrderText());
				for (Item item : upgrade.getUpgrades()) {
					tooltips.add(item.getDescription().copy().withStyle(ChatFormatting.GRAY).getVisualOrderText());
				}
				graphics.renderTooltip(gui.getFontRenderer(), tooltips, xAxis, yAxis);
			}
		}
	}

	@Override
	public boolean isVisible() {
		return slot.isActive();
	}

	public enum SlotType implements ISlotTexture {

		NORMAL(18, 18, 0, 0, 18, 18, "slot_regular"), //
		BIG(26, 26, 0, 0, 26, 26, "slot_big", -5, -5);

		private final int textureWidth;
		private final int textureHeight;
		private final int textureU;
		private final int textureV;
		private final int imageWidth;
		private final int imageHeight;
		private final ResourceLocation loc;

		private final int xOffset;
		private final int yOffset;

		SlotType(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, String name) {
			this(textureWidth, textureHeight, textureU, textureV, imageWidth, imageHeight, name, -1, -1);
		}

		SlotType(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, String name, int xOffset, int yOffset) {
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
			this.textureU = textureU;
			this.textureV = textureV;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			loc = Electrodynamics.rl("textures/screen/component/slot/" + name + ".png");

			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}

		@Override
		public ResourceLocation getLocation() {
			return loc;
		}

		@Override
		public int imageHeight() {
			return imageHeight;
		}

		@Override
		public int imageWidth() {
			return imageWidth;
		}

		@Override
		public int textureHeight() {
			return textureHeight;
		}

		@Override
		public int textureU() {
			return textureU;
		}

		@Override
		public int textureV() {
			return textureV;
		}

		@Override
		public int textureWidth() {
			return textureWidth;
		}

		@Override
		public int xOffset() {
			return xOffset;
		}

		@Override
		public int yOffset() {
			return yOffset;
		}

	}

	public enum IconType implements ITexture {

		NONE(0, 0, 0, 0, 0, 0, null), //
		ENERGY_DARK(10, 10, 0, 0, 10, 10, "electricity_dark"), //
		FLUID_DARK(14, 14, 0, 0, 14, 14, "fluid_dark"), //
		GAS_DARK(16, 16, 0, 0, 16, 16, "gas_dark"), //
		UPGRADE_DARK(12, 12, 0, 0, 12, 12, "upgrade_dark"), //
		DRILL_HEAD_DARK(12, 12, 0, 0, 12, 12, "drill_head_dark"), //
		TRASH_CAN_DARK(10, 10, 0, 0, 10, 10, "trash_can_dark"), //
		FIBERGLASS_SHEET_DARK(16, 16, 0, 0, 16, 16, "fiberglasssheet_dark"), //

		ENERGY_GREEN(14, 14, 0, 0, 14, 14, "electricity_green"), //
		ENCHANTMENT(16, 16, 0, 0, 16, 16, "enchantment"), //
		FLUID_BLUE(16, 16, 0, 0, 16, 16, "fluid_blue"), //
		MINING_LOCATION(18, 18, 0, 0, 18, 18, "mining_location"), //
		QUARRY_COMPONENTS(18, 18, 0, 0, 18, 18, "quarry_components"), //
		TEMPERATURE(14, 14, 0, 0, 14, 14, "temperature"), //
		THERMOMETER(16, 16, 0, 0, 16, 16, "thermometer"), //
		PRESSURE_GAUGE(10, 10, 0, 0, 10, 10, "pressuredial"), //
		INVENTORY_IO(16, 16, 0, 0, 16, 16, "inventoryio"), //
		SONAR_PROFILE(16, 16, 0, 0, 16, 16, "sonarpattern"), //
		CUBE_OUTLINE(16,16, 0, 0, 16, 16, "cubeoutline") //
		; //

		private final int textureWidth;
		private final int textureHeight;
		private final int textureU;
		private final int textureV;
		private final int imageWidth;
		private final int imageHeight;
		private final ResourceLocation loc;

		IconType(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, String name) {
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
			this.textureU = textureU;
			this.textureV = textureV;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			loc = Electrodynamics.rl("textures/screen/component/icon/" + name + ".png");
		}

		@Override
		public ResourceLocation getLocation() {
			return loc;
		}

		@Override
		public int imageHeight() {
			return imageHeight;
		}

		@Override
		public int imageWidth() {
			return imageWidth;
		}

		@Override
		public int textureHeight() {
			return textureHeight;
		}

		@Override
		public int textureU() {
			return textureU;
		}

		@Override
		public int textureV() {
			return textureV;
		}

		@Override
		public int textureWidth() {
			return textureWidth;
		}

	}

}