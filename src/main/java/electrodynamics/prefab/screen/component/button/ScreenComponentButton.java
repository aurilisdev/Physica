package electrodynamics.prefab.screen.component.button;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import electrodynamics.api.screen.ITexture;
import electrodynamics.prefab.screen.component.types.ScreenComponentGeneric;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

/**
 * A modification of the vanilla button to integrate it with the Electrodynamics system of doing GUI components as the
 * Button class has several annoying issues
 * 
 * @author skip999
 *
 * @param <T>
 */
public class ScreenComponentButton<T extends ScreenComponentButton<?>> extends ScreenComponentGeneric {

    public static final WidgetSprites VANILLA_BUTTON_SPRITES = new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/button"), ResourceLocation.withDefaultNamespace("widget/button_disabled"), ResourceLocation.withDefaultNamespace("widget/button_highlighted"));

    public final boolean isVanillaRender;

    @Nullable
    public OnTooltip onTooltip = null;
    @Nullable
    public OnPress onPress = null;

    @Nullable
    public Component label = null;

    public SoundEvent pressSound = SoundEvents.UI_BUTTON_CLICK.value();

    public ScreenComponentButton(ITexture texture, int x, int y) {
        super(texture, x, y);
        isVanillaRender = false;
    }

    public ScreenComponentButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        isVanillaRender = true;
        this.width = width;
        this.height = height;
    }

    public T setLabel(Component label) {
        this.label = label;
        return (T) this;
    }

    public T setOnPress(OnPress onPress) {
        this.onPress = onPress;
        return (T) this;
    }

    public T setOnTooltip(OnTooltip onTooltip) {
        this.onTooltip = onTooltip;
        return (T) this;
    }

    public T setPressSound(SoundEvent sound) {
        pressSound = sound;
        return (T) this;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
        if (isVanillaRender && isVisible()) {
            Minecraft minecraft = Minecraft.getInstance();
            graphics.setColor(color.rFloat(), color.gFloat(), color.bFloat(), color.aFloat());
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            graphics.blitSprite(VANILLA_BUTTON_SPRITES.get(isActive(), this.isHoveredOrFocused()), this.xLocation + guiWidth, this.yLocation + guiHeight, this.width, this.height);
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

            Font font = minecraft.font;
            Component label = getLabel();
            if (label != null) {
                graphics.drawCenteredString(font, label, this.xLocation + guiWidth + this.width / 2, this.yLocation + guiHeight + (this.height - 8) / 2, color.color());
            }

        } else {
            super.renderBackground(graphics, xAxis, yAxis, guiWidth, guiHeight);
        }
    }

    public int getVanillaYImage(boolean isMouseOver) {
        if (!isVisible()) {
            return 0;
        }
        if (isMouseOver) {
            return 2;
        }

        return 1;
    }

    @Override
    public void renderForeground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
        super.renderForeground(graphics, xAxis, yAxis, guiWidth, guiHeight);
        if (isVisible() && isHovered() && onTooltip != null) {
            onTooltip.onTooltip(graphics, this, xAxis, yAxis);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isActiveAndVisible() && isValidClick(button) && isInClickRegion(mouseX, mouseY)) {

            onMouseClick(mouseX, mouseY);

            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isValidClick(button)) {
            onMouseRelease(mouseX, mouseY);
            return true;
        }
        return false;
    }

    @Override
    public void onMouseClick(double mouseX, double mouseY) {
        if (onPress != null) {
            onPress();
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isActiveAndVisible()) {
            return false;
        }
        if (keyCode != 257 && keyCode != 32 && keyCode != 335) {
            return false;
        }
        this.playDownSound(Minecraft.getInstance().getSoundManager());
        this.onPress();
        return true;
    }

    public void onPress() {
        onPress.onPress(this);
        playDownSound(Minecraft.getInstance().getSoundManager());
    }

    public boolean isValidMouseClick(int button) {
        return button == 0;
    }

    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(pressSound, 1.0F));
    }

    public Component getLabel() {
        return label;
    }

    public static interface OnPress {

        public void onPress(ScreenComponentButton<?> button);

    }

    public static interface OnTooltip {

        public void onTooltip(GuiGraphics graphics, ScreenComponentButton<?> button, int xAxis, int yAxis);

    }

}
