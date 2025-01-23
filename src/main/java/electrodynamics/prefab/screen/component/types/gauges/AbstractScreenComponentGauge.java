package electrodynamics.prefab.screen.component.types.gauges;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import electrodynamics.Electrodynamics;
import electrodynamics.api.screen.ITexture;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractScreenComponentGauge extends ScreenComponentGeneric {

	public static final ResourceLocation TEXTURE = Electrodynamics.rl("textures/screen/component/fluid.png");

	public AbstractScreenComponentGauge(int x, int y) {
		super(GaugeTextures.BACKGROUND_DEFAULT, x, y);
		onTooltip((graphics, component, xAxis, yAxis) -> {
			List<? extends FormattedCharSequence> tooltips = getTooltips();
			if (!tooltips.isEmpty()) {
				graphics.renderTooltip(gui.getFontRenderer(), tooltips, xAxis, yAxis);
			}
		});
	}

	@Override
	public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
		super.renderBackground(graphics, xAxis, yAxis, guiWidth, guiHeight);
		ResourceLocation texture = getTexture();
		int scale = getScaledLevel();

		if (texture != null && scale > 0) {
			ResourceLocation blocks = InventoryMenu.BLOCK_ATLAS;
			TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(blocks).apply(texture);
			RenderingUtils.bindTexture(sprite.atlasLocation());
			applyColor();
			for (int i = 0; i < 16; i += 16) {
				for (int j = 0; j < scale; j += 16) {
					int drawWidth = Math.min(super.texture.textureWidth() - 2 - i, 16);
					int drawHeight = Math.min(scale - j, 16);

					int drawX = guiWidth + xLocation + 1;
					int drawY = guiHeight + yLocation - 1 + super.texture.textureHeight() - Math.min(scale - j, super.texture.textureHeight());

					graphics.blit(drawX, drawY, 0, drawWidth, drawHeight, sprite);

				}
			}
			RenderSystem.setShaderColor(1, 1, 1, 1);
		}

		graphics.blit(super.texture.getLocation(), guiWidth + xLocation, guiHeight + yLocation, GaugeTextures.LEVEL_DEFAULT.textureU(), 0, GaugeTextures.LEVEL_DEFAULT.textureWidth(), GaugeTextures.LEVEL_DEFAULT.textureHeight(), GaugeTextures.LEVEL_DEFAULT.imageWidth(), GaugeTextures.LEVEL_DEFAULT.imageHeight());
	}

	protected abstract void applyColor();

	protected abstract int getScaledLevel();

	protected abstract ResourceLocation getTexture();

	protected abstract List<? extends FormattedCharSequence> getTooltips();

	public enum GaugeTextures implements ITexture {
		BACKGROUND_DEFAULT(14, 49, 0, 0, 256, 256, TEXTURE),
		LEVEL_DEFAULT(14, 49, 14, 0, 256, 256, TEXTURE);

		private final int textureWidth;
		private final int textureHeight;
		private final int textureU;
		private final int textureV;
		private final int imageWidth;
		private final int imageHeight;
		private final ResourceLocation loc;

		GaugeTextures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, ResourceLocation loc) {
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
			this.textureU = textureU;
			this.textureV = textureV;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			this.loc = loc;
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