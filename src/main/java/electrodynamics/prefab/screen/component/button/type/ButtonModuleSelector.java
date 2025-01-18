package electrodynamics.prefab.screen.component.button.type;

import electrodynamics.Electrodynamics;
import electrodynamics.api.screen.ITexture;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ButtonModuleSelector extends ButtonSpecificPage {

	private boolean selected = false;

	public ButtonModuleSelector(int x, int y, int page, boolean selected) {
		super(GuidebookButtonTextures.CHECKBOX_OFF, x, y, page);
		this.selected = selected;
		onPress = button -> {
			ButtonModuleSelector selector = (ButtonModuleSelector) button;
			selector.selected = !selector.selected;
		};
	}

	@Override
	public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {

		if (selected && isVisible()) {
			ITexture texture = GuidebookButtonTextures.CHECKBOX_ON;
			graphics.blit(texture.getLocation(), this.xLocation + guiWidth, this.yLocation + guiHeight, texture.textureU(), texture.textureV(), texture.textureWidth(), texture.textureHeight(), texture.imageWidth(), texture.imageHeight());
		} else {
			super.renderBackground(graphics, xAxis, yAxis, guiWidth, guiHeight);
		}

	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public static enum GuidebookButtonTextures implements ITexture {
		CHECKBOX_OFF(9, 9, 0, 0, 9, 9, "checkboxoff"),
		CHECKBOX_ON(9, 9, 0, 0, 9, 9, "checkboxon");

		private final int textureWidth;
		private final int textureHeight;
		private final int textureU;
		private final int textureV;
		private final int imageWidth;
		private final int imageHeight;
		private final ResourceLocation loc;

		private GuidebookButtonTextures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, String name) {
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
			this.textureU = textureU;
			this.textureV = textureV;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			loc = Electrodynamics.rl("textures/screen/guidebook/buttons/" + name + ".png");
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
