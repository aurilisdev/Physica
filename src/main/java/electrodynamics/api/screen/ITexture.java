package electrodynamics.api.screen;

import electrodynamics.Electrodynamics;
import net.minecraft.resources.ResourceLocation;

/**
 * Simple wrapper interface allowing for basic compatibility between different textures. Textures are assumed to be 256 x 256
 * 
 * @author skip999
 *
 */
public interface ITexture {

	int textureWidth();

	int textureHeight();

	int textureU();

	int textureV();

	int imageWidth();

	int imageHeight();

	ResourceLocation getLocation();

	enum Textures implements ITexture {
		NONE(0, 0, 0, 0, 0, 0, null),
		CONDENSER_COLUMN(32, 47, 0, 0, 32, 47, Electrodynamics.rl("textures/screen/component/condensercolumn.png")),
		TRANSFORMER_SYMBOL(25, 33, 0, 0, 25, 33, Electrodynamics.rl("textures/screen/component/transformercoil.png")),
		WHITE(10, 10, 0, 0 ,10, 10, Electrodynamics.forgerl("textures/white.png"));

		private final int textureWidth;
		private final int textureHeight;
		private final int textureU;
		private final int textureV;
		private final int imageWidth;
		private final int imageHeight;
		private final ResourceLocation loc;

		Textures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, ResourceLocation loc) {
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
