package electrodynamics.prefab.screen.component.types;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.tile.pipelines.gas.TileGasPipeFilter;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentGasGauge;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentGasGauge.GasGaugeTextures;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

public class ScreenComponentGasFilter extends ScreenComponentGeneric {

    private final int index;

    public ScreenComponentGasFilter(int x, int y, int index) {
        super(GasGaugeTextures.BACKGROUND_DEFAULT, x, y);
        this.index = index;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
        super.renderBackground(graphics, xAxis, yAxis, guiWidth, guiHeight);

        TileGasPipeFilter filter = (TileGasPipeFilter) ((GenericContainerBlockEntity<?>) ((GenericScreen<?>) gui).getMenu()).getSafeHost();

        if (filter == null) {
            return;
        }

        Property<GasStack> property = filter.filteredGases[index];

        if (!property.get().isEmpty()) {

            ScreenComponentGasGauge.renderMercuryTexture(graphics, guiWidth + xLocation + 1, guiHeight + yLocation + 1, 1);

        }

        GasGaugeTextures texture = GasGaugeTextures.LEVEL_DEFAULT;

        graphics.blit(texture.getLocation(), guiWidth + xLocation, guiHeight + yLocation, texture.textureU(), texture.textureV(), texture.textureWidth(), texture.textureHeight(), texture.imageWidth(), texture.imageHeight());

    }

    @Override
    public void renderForeground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
        if (!isPointInRegion(xLocation, yLocation, xAxis, yAxis, super.texture.textureWidth(), super.texture.textureHeight())) {
            return;
        }

        TileGasPipeFilter filter = (TileGasPipeFilter) ((GenericContainerBlockEntity<?>) ((GenericScreen<?>) gui).getMenu()).getSafeHost();

        if (filter == null) {
            return;
        }

        List<FormattedCharSequence> tooltips = new ArrayList<>();

        Property<GasStack> property = filter.filteredGases[index];

        tooltips.add(property.get().getGas().getDescription().getVisualOrderText());

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

        TileGasPipeFilter filter = (TileGasPipeFilter) ((GenericContainerBlockEntity<?>) screen.getMenu()).getSafeHost();

        if (filter == null) {
            return;
        }

        Property<GasStack> property = filter.filteredGases[index];

        ItemStack holding = screen.getMenu().getCarried();

        if (holding.isEmpty()) {

            if (!Screen.hasShiftDown()) {
                return;
            }
            property.set(GasStack.EMPTY);

            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ElectrodynamicsSounds.SOUND_PRESSURERELEASE.get(), 1.0F));

            return;

        }

        IGasHandlerItem handler = holding.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (handler == null) {
            return;
        }

        GasStack taken = handler.drain(Integer.MAX_VALUE, GasAction.SIMULATE);

        if (taken.isEmpty()) {
            return;
        }

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ElectrodynamicsSounds.SOUND_PRESSURERELEASE.get(), 1.0F));

        property.set(taken);

    }

}
