package electrodynamics.prefab.screen.component.types;

import java.util.function.DoubleSupplier;

import electrodynamics.Electrodynamics;
import electrodynamics.api.screen.ITexture;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenComponentProgress extends ScreenComponentGeneric {

	public static final ResourceLocation TEXTURE = Electrodynamics.rl("textures/screen/component/progress.png");

	private final DoubleSupplier progressInfoHandler;
	private final ProgressBars bar;

	public ScreenComponentProgress(ProgressBars progressBar, DoubleSupplier progressInfoHandler, int x, int y) {
		super(progressBar.off, x, y);
		this.progressInfoHandler = progressInfoHandler;
		bar = progressBar;
	}

	@Override
	public void renderBackground(GuiGraphics graphics, final int xAxis, final int yAxis, final int guiWidth, final int guiHeight) {
		super.renderBackground(graphics, xAxis, yAxis, guiWidth, guiHeight);
		ProgressTextures on = bar.on;
		int progress;
		switch (bar) {
		case PROGRESS_ARROW_LEFT:
			progress = (int) (progressInfoHandler.getAsDouble() * on.textureWidth());
			int xStart = on.textureU() + on.textureWidth() - progress;
			graphics.blit(on.getLocation(), guiWidth + xLocation + on.textureWidth() - progress, guiHeight + yLocation, xStart, on.textureV(), progress, on.textureHeight(), on.imageWidth(), on.imageHeight());
			break;
		case COUNTDOWN_FLAME, FAN:
			progress = (int) (progressInfoHandler.getAsDouble() * on.textureHeight());
			graphics.blit(on.getLocation(), guiWidth + xLocation, guiHeight + yLocation + on.textureHeight() - progress, on.textureU(), on.textureV() + on.textureHeight() - progress, on.textureWidth(), progress, on.imageWidth(), on.imageHeight());
			break;
		case PROGRESS_ARROW_RIGHT, BATTERY_CHARGE_RIGHT, PROGRESS_ARROW_RIGHT_BIG:
			graphics.blit(on.getLocation(), guiWidth + xLocation, guiHeight + yLocation, on.textureU(), on.textureV(), (int) (progressInfoHandler.getAsDouble() * on.textureWidth()), on.textureHeight(), on.imageWidth(), on.imageHeight());
			break;
		default:
			break;
		}

	}

	public enum ProgressBars {

		PROGRESS_ARROW_RIGHT(ProgressTextures.ARROW_RIGHT_OFF, ProgressTextures.ARROW_RIGHT_ON),
		PROGRESS_ARROW_LEFT(ProgressTextures.ARROW_LEFT_OFF, ProgressTextures.ARROW_LEFT_ON),
		COUNTDOWN_FLAME(ProgressTextures.FLAME_OFF, ProgressTextures.FLAME_ON),
		BATTERY_CHARGE_RIGHT(ProgressTextures.BATTER_CHARGE_RIGHT_OFF, ProgressTextures.BATTER_CHARGE_RIGHT_ON),
		PROGRESS_ARROW_RIGHT_BIG(ProgressTextures.ARROW_RIGHT_BIG_OFF, ProgressTextures.ARROW_RIGHT_BIG_ON),
		FAN(ProgressTextures.FAN_OFF, ProgressTextures.FAN_ON);

		public final ProgressTextures off;
		public final ProgressTextures on;

		ProgressBars(ProgressTextures off, ProgressTextures on) {
			this.off = off;
			this.on = on;
		}

	}

	public enum ProgressTextures implements ITexture {
		ARROW_RIGHT_OFF(22, 16, 0, 0, 44, 16, "arrow_right"),
		ARROW_RIGHT_ON(22, 16, 22, 0, 44, 16, "arrow_right"),
		ARROW_RIGHT_BIG_OFF(64, 15, 0, 0, 64, 30, "arrow_right_big"),
		ARROW_RIGHT_BIG_ON(64, 15, 0, 15, 64, 30, "arrow_right_big"),
		ARROW_LEFT_ON(22, 16, 0, 0, 44, 16, "arrow_left"),
		ARROW_LEFT_OFF(22, 16, 22, 0, 44, 16, "arrow_left"),
		FLAME_ON(14, 14, 0, 0, 14, 28, "flame"),
		FLAME_OFF(14, 14, 0, 14, 14, 28, "flame"),
		BATTER_CHARGE_RIGHT_OFF(19, 10, 0, 0, 38, 10, "battery_charge"),
		BATTER_CHARGE_RIGHT_ON(19, 10, 19, 0, 38, 10, "battery_charge"),
		COMPRESS_ARROW_OFF(15, 9, 0, 0, 15, 9, "compressarrow"),
		DECOMPRESS_ARROW_OFF(15, 9, 0, 0, 15, 9, "decompressarrow"),
		FEYNMAN_DIAGRAM_OFF(65, 46, 0, 0, 65, 92, "feynman_diagram"),
		FEYNMAN_DIAGRAM_ON(65, 46, 0, 46, 65, 92, "feynman_diagram"),
		FAN_OFF(17, 17, 0, 0, 34, 17, "fan"),
		FAN_ON(17, 17, 17, 0, 34, 17, "fan"),
		;


		private final int textureWidth;
		private final int textureHeight;
		private final int textureU;
		private final int textureV;
		private final int imageWidth;
		private final int imageHeight;
		private final ResourceLocation loc;

		ProgressTextures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, String name) {
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
			this.textureU = textureU;
			this.textureV = textureV;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			loc = Electrodynamics.rl("textures/screen/component/progress/" + name + ".png");
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