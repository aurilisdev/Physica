package electrodynamics.prefab.screen.component.types;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import electrodynamics.common.tile.pipelines.fluid.TileFluidPipeFilter;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.types.gauges.AbstractScreenComponentGauge.GaugeTextures;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class ScreenComponentFluidFilter extends ScreenComponentGeneric {

    private final int index;

    public ScreenComponentFluidFilter(int x, int y, int index) {
        super(GaugeTextures.BACKGROUND_DEFAULT, x, y);
        this.index = index;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
        super.renderBackground(graphics, xAxis, yAxis, guiWidth, guiHeight);

        TileFluidPipeFilter filter = (TileFluidPipeFilter) ((GenericContainerBlockEntity<?>) ((GenericScreen<?>) gui).getMenu()).getSafeHost();

        if (filter == null) {
            return;
        }

        Property<FluidStack> property = filter.filteredFluids[index];

        FluidStack fluid = property.get();

        if (!fluid.isEmpty()) {

            ResourceLocation fluidText = IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture();

            if (fluidText != null) {

                ResourceLocation blocks = InventoryMenu.BLOCK_ATLAS;
                TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(blocks).apply(fluidText);
                RenderingUtils.bindTexture(sprite.atlasLocation());

                int scale = GaugeTextures.BACKGROUND_DEFAULT.textureHeight() - 2;

                RenderingUtils.setShaderColor(new Color(IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid)));

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

        }

        graphics.blit(GaugeTextures.LEVEL_DEFAULT.getLocation(), guiWidth + xLocation, guiHeight + yLocation, GaugeTextures.LEVEL_DEFAULT.textureU(), 0, GaugeTextures.LEVEL_DEFAULT.textureWidth(), GaugeTextures.LEVEL_DEFAULT.textureHeight(), GaugeTextures.LEVEL_DEFAULT.imageWidth(), GaugeTextures.LEVEL_DEFAULT.imageHeight());

    }

    @Override
    public void renderForeground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {

        if (!isPointInRegion(xLocation, yLocation, xAxis, yAxis, super.texture.textureWidth(), super.texture.textureHeight())) {
            return;
        }

        TileFluidPipeFilter filter = (TileFluidPipeFilter) ((GenericContainerBlockEntity<?>) ((GenericScreen<?>) gui).getMenu()).getSafeHost();

        if (filter == null) {
            return;
        }

        Property<FluidStack> property = filter.filteredFluids[index];

        List<FormattedCharSequence> tooltips = new ArrayList<>();

        tooltips.add(Component.translatable(property.get().getFluidType().getDescriptionId()).getVisualOrderText());

        graphics.renderTooltip(gui.getFontRenderer(), tooltips, xAxis, yAxis);
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

        GenericScreen<?> screen = (GenericScreen<?>) gui;

        TileFluidPipeFilter filter = (TileFluidPipeFilter) ((GenericContainerBlockEntity<?>) screen.getMenu()).getSafeHost();

        if (filter == null) {
            return;
        }

        Property<FluidStack> property = filter.filteredFluids[index];

        ItemStack holding = screen.getMenu().getCarried();

        if (holding.isEmpty()) {

            if (!Screen.hasShiftDown()) {
                return;
            }
            property.set(FluidStack.EMPTY);

            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BUCKET_EMPTY, 1.0F));

            return;

        }

        IFluidHandlerItem handler = holding.getCapability(Capabilities.FluidHandler.ITEM);

        if (handler == null) {
            return;
        }

        FluidStack taken = handler.drain(Integer.MAX_VALUE, FluidAction.SIMULATE);

        if (taken.isEmpty()) {
            return;
        }

        property.set(taken);

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BUCKET_FILL, 1.0F));

    }

}
