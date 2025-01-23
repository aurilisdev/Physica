package electrodynamics.prefab.screen.component.button.type;

import electrodynamics.Electrodynamics;
import electrodynamics.api.screen.ITexture;
import electrodynamics.prefab.screen.component.button.ScreenComponentButton;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ButtonTankSlider extends ScreenComponentButton<ButtonTankSlider> {

    private final TankSliderPair pair;

    public ButtonTankSlider(TankSliderPair pair, int x, int y) {
        super(pair.off, x, y);
        this.pair = pair;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
        if (isVisible() && isHovered()) {
            RenderingUtils.setShaderColor(color);
            ITexture on = pair.on;
            graphics.blit(on.getLocation(), guiWidth + xLocation, guiHeight + yLocation, on.textureU(), on.textureV(), on.textureWidth(), on.textureHeight(), on.imageWidth(), on.imageHeight());
            RenderingUtils.resetShaderColor();
        } else {
            super.renderBackground(graphics, xAxis, yAxis, guiWidth, guiHeight);
        }
    }

    public static enum TankSliderPair {

        LEFT(TankSliderTextures.LEFT_DEFAULT, TankSliderTextures.LEFT_HOVERED),
        RIGHT(TankSliderTextures.RIGHT_DEFAULT, TankSliderTextures.RIGHT_HOVERED);
        private final TankSliderTextures off;
        private final TankSliderTextures on;

        private TankSliderPair(TankSliderTextures off, TankSliderTextures on) {
            this.off = off;
            this.on = on;
        }

    }

    public static enum TankSliderTextures implements ITexture {
        LEFT_DEFAULT(6, 9, 0, 0, 12, 18),
        LEFT_HOVERED(6, 9, 0, 9, 12, 18),
        RIGHT_DEFAULT(6, 9, 6, 0, 12, 18),
        RIGHT_HOVERED(6, 9, 6, 9, 12, 18);

        private final int textureWidth;
        private final int textureHeight;
        private final int textureU;
        private final int textureV;
        private final int imageWidth;
        private final int imageHeight;
        private final ResourceLocation loc;

        private TankSliderTextures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight) {
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            this.textureU = textureU;
            this.textureV = textureV;
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            loc = Electrodynamics.rl("textures/screen/component/button/tankarrows.png");
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
