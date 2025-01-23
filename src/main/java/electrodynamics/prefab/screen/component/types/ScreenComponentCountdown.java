package electrodynamics.prefab.screen.component.types;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;

import electrodynamics.Electrodynamics;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.screen.ITexture;
import electrodynamics.api.screen.component.TextPropertySupplier;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public class ScreenComponentCountdown extends AbstractScreenComponentInfo {

	private final DoubleSupplier progressInfoHandler;
	private TextPropertySupplier tooltip;

	public static final ResourceLocation TEXTURE = Electrodynamics.rl("textures/screen/component/countdown.png");

	public ScreenComponentCountdown(TextPropertySupplier tooltip, DoubleSupplier progressInfoHandler, int x, int y) {
		super(CountdownTextures.BACKGROUND_DEFAULT, AbstractScreenComponentInfo.EMPTY, x, y);
		this.progressInfoHandler = progressInfoHandler;
		this.tooltip = tooltip;
	}

	public ScreenComponentCountdown(DoubleSupplier progressInfoHandler, int x, int y) {
		this(null, progressInfoHandler, x, y);
	}

	@Override
	public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
		super.renderBackground(graphics, xAxis, yAxis, guiWidth, guiHeight);
		int lengthBar = (int) (progressInfoHandler.getAsDouble() * CountdownTextures.COUNTDOWN_BAR_DEFAULT.textureWidth());
		graphics.blit(CountdownTextures.BACKGROUND_DEFAULT.getLocation(), guiWidth + xLocation + 1, guiHeight + yLocation + 1, CountdownTextures.COUNTDOWN_BAR_DEFAULT.textureU(), CountdownTextures.COUNTDOWN_BAR_DEFAULT.textureV(), lengthBar, CountdownTextures.COUNTDOWN_BAR_DEFAULT.textureHeight(), CountdownTextures.COUNTDOWN_BAR_DEFAULT.imageWidth(), CountdownTextures.COUNTDOWN_BAR_DEFAULT.imageHeight());

	}

	@Override
	protected List<? extends FormattedCharSequence> getInfo(List<? extends FormattedCharSequence> list) {
		if (tooltip != null) {
			return tooltip.getInfo();
		}
		return getTooltips();
	}

	private List<? extends FormattedCharSequence> getTooltips() {
		List<FormattedCharSequence> tips = new ArrayList<>();
		if (progressInfoHandler != null) {
			tips.add(ChatFormatter.getChatDisplayShort(100 * progressInfoHandler.getAsDouble(), DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GRAY).getVisualOrderText());
		}
		return tips;
	}

	public enum CountdownTextures implements ITexture {
		BACKGROUND_DEFAULT(60, 12, 0, 0, 256, 256, TEXTURE),
		COUNTDOWN_BAR_DEFAULT(58, 10, 0, 12, 256, 256, TEXTURE);

		private final int textureWidth;
		private final int textureHeight;
		private final int textureU;
		private final int textureV;
		private final int imageWidth;
		private final int imageHeight;
		private final ResourceLocation loc;

		CountdownTextures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, ResourceLocation loc) {
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
