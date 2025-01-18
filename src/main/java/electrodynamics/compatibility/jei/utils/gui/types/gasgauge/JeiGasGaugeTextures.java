package electrodynamics.compatibility.jei.utils.gui.types.gasgauge;

import electrodynamics.Electrodynamics;
import net.minecraft.resources.ResourceLocation;

public enum JeiGasGaugeTextures implements IGasGaugeTexture {

	BACKGROUND_DEFAULT(14, 49, 0, 0, 41, 49, Electrodynamics.rl("textures/screen/jei/gas_gauge_default.png")),
	LEVEL_DEFAULT(14, 49, 14, 0, 41, 49, Electrodynamics.rl("textures/screen/jei/gas_gauge_default.png"), -1, -1);

	private final int textureWidth;
	private final int textureHeight;
	private final int textureU;
	private final int textureV;
	private final int imageWidth;
	private final int imageHeight;
	private final ResourceLocation loc;

	private final int xOffset;
	private final int yOffset;

	private JeiGasGaugeTextures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, ResourceLocation loc) {
		this(textureWidth, textureHeight, textureU, textureV, imageWidth, imageHeight, loc, 0, 0);
	}

	private JeiGasGaugeTextures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, ResourceLocation loc, int xOffset, int yOffset) {
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.textureU = textureU;
		this.textureV = textureV;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.loc = loc;

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
	public int getXOffset() {
		return xOffset;
	}

	@Override
	public int getYOffset() {
		return yOffset;
	}

}
