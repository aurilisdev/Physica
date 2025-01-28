package electrodynamics.prefab.screen.component.types;

import java.util.function.Consumer;

import electrodynamics.Electrodynamics;
import electrodynamics.api.screen.ITexture;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ScreenComponentVerticalSlider extends ScreenComponentGeneric {
    private int sliderYOffset = 0;
    private boolean active = false;

    private boolean isHeld = false;

    private Consumer<Integer> sliderDragConsumer;
    private Consumer<Integer> sliderClickConsumer;

    public ScreenComponentVerticalSlider(int x, int y, int height) {
        super(x, y, 14, Math.max(height, 30));
    }

    public ScreenComponentVerticalSlider setDragConsumer(Consumer<Integer> responder) {
        sliderDragConsumer = responder;
        return this;
    }

    public ScreenComponentVerticalSlider setClickConsumer(Consumer<Integer> responder) {
        sliderClickConsumer = responder;
        return this;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isValidClick(button)) {
            this.onMouseDrag(mouseX, mouseY, dragX, dragY);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onMouseDrag(double mouseX, double mouseY, double dragX, double dragY) {
        isHeld = true;
        if (sliderDragConsumer != null) {
            sliderDragConsumer.accept((int) (mouseY - this.gui.getGuiHeight()));
        }
        super.onMouseDrag(mouseX, mouseY, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isHeld && isPointInSlider(this.xLocation, this.yLocation, mouseX - this.gui.getGuiWidth(), mouseY - this.gui.getGuiHeight(), this.width, this.height) && sliderClickConsumer != null) {
            sliderClickConsumer.accept((int) (mouseY - this.gui.getGuiHeight()));
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isHeld = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected boolean isPointInRegion(int x, int y, double xAxis, double yAxis, int width, int height) {
        return xAxis >= x && xAxis <= (x + width - 1) && yAxis >= y + sliderYOffset + 2 && yAxis <= (y + 2 + sliderYOffset + 15);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {

        ITexture bg = VerticalSliderTextures.SLIDER_BACKGROUND;

        graphics.blit(bg.getLocation(), guiWidth + xLocation - 1, guiHeight + yLocation, width, 1, bg.textureU(), bg.textureV(), bg.textureWidth(), 1, bg.imageWidth(), bg.imageHeight());

        int permutations = (int) ((height - 2) / 28.0D);

        int remainder = height - permutations * 28 - 2;

        for (int i = 0; i < permutations; i++) {

            graphics.blit(bg.getLocation(), guiWidth + xLocation - 1, guiHeight + yLocation + 1 + i * 28, width, 28, bg.textureU(), bg.textureV() + 1, bg.textureWidth(), 28, bg.imageWidth(), bg.imageHeight());

        }

        graphics.blit(bg.getLocation(), guiWidth + xLocation - 1, guiHeight + yLocation + 1 + 28 * permutations, width, remainder, bg.textureU(), bg.textureV() + 1, bg.textureWidth(), remainder, bg.imageWidth(), bg.imageHeight());

        graphics.blit(bg.getLocation(), guiWidth + xLocation - 1, guiHeight + yLocation + height - 1, width, 1, bg.textureU(), bg.textureV() + 29, bg.textureWidth(), 1, bg.imageWidth(), bg.imageHeight());

        ITexture slider;

        if (active) {

            slider = VerticalSliderTextures.SLIDER_ACTIVE;

        } else {

            slider = VerticalSliderTextures.SLIDER_INACTIVE;

        }

        graphics.blit(slider.getLocation(), guiWidth + xLocation , guiHeight + yLocation + 1 + sliderYOffset, slider.textureWidth(), slider.textureHeight(), slider.textureU(), slider.textureV(), slider.textureWidth(), slider.textureHeight(), slider.imageWidth(), slider.imageHeight());

    }

    public void updateActive(boolean active) {
        this.active = active;
    }

    public void setSliderYOffset(int offset) {
        sliderYOffset = Math.min(offset, height - 2 - 15);
    }

    protected boolean isPointInSlider(int x, int y, double xAxis, double yAxis, int width, int height) {
        return xAxis >= x && xAxis <= (x + width - 1) && yAxis >= y && yAxis <= (y + height - 1);
    }

    public boolean isSliderActive() {
        return active;
    }

    public boolean isSliderHeld() {
        return isHeld;
    }

    public static enum VerticalSliderTextures implements ITexture {
        SLIDER_BACKGROUND(14, 30, 0, 0, 14, 30, Electrodynamics.rl("textures/screen/component/verticalslider/vertical_slider_bg.png")),
        SLIDER_ACTIVE(12, 15, 0, 0, 12, 15, Electrodynamics.rl("textures/screen/component/verticalslider/vertical_slider_active.png")),
        SLIDER_INACTIVE(12, 15, 0, 0, 12, 15, Electrodynamics.rl("textures/screen/component/verticalslider/vertical_slider_inactive.png"));

        private final int textureWidth;
        private final int textureHeight;
        private final int textureU;
        private final int textureV;
        private final int imageWidth;
        private final int imageHeight;
        private final ResourceLocation loc;

        private VerticalSliderTextures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, ResourceLocation loc) {
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
            return this.loc;
        }

        @Override
	public int imageHeight() {
            return this.imageHeight;
        }

        @Override
	public int imageWidth() {
            return this.imageWidth;
        }

        @Override
	public int textureHeight() {
            return this.textureHeight;
        }

        @Override
	public int textureU() {
            return this.textureU;
        }

        @Override
	public int textureV() {
            return this.textureV;
        }

        @Override
	public int textureWidth() {
            return this.textureWidth;
        }

    }
}
