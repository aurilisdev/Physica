package electrodynamics.compatibility.mekanism;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.common.tile.compatibility.TileRotaryUnifier;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.types.gauges.AbstractScreenComponentGauge;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.RenderingUtils;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.client.render.MekanismRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class ScreenComponentChemicalGauge extends ScreenComponentGeneric {

    private final ChemicalStackSupplier chemicalStackSupplier;

    public ScreenComponentChemicalGauge(int x, int y, ChemicalStackSupplier chemicalStackSupplier) {
        super(AbstractScreenComponentGauge.GaugeTextures.BACKGROUND_DEFAULT, x, y);

        this.chemicalStackSupplier = chemicalStackSupplier;

        onTooltip((graphics, component, xAxis, yAxis) -> {
            List<FormattedCharSequence> tooltips = new ArrayList<>();
            ChemicalStack stack = chemicalStackSupplier.getChemical();
            if (stack != null) {
                Chemical chemical = stack.getChemical();
                if (stack.getAmount() > 0) {
                    tooltips.add(Component.translatable(chemical.getTranslationKey()).getVisualOrderText());
                    tooltips.add(ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(stack.getAmount()), ChatFormatter.formatFluidMilibuckets(TileRotaryUnifier.MAX_CHEM_AMOUNT)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
                } else {
                    tooltips.add(ElectroTextUtils.ratio(Component.literal("0"), ChatFormatter.formatFluidMilibuckets(TileRotaryUnifier.MAX_CHEM_AMOUNT)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
                }
            }
            if (!tooltips.isEmpty()) {
                graphics.renderTooltip(gui.getFontRenderer(), tooltips, xAxis, yAxis);
            }
        });
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
        super.renderBackground(graphics, xAxis, yAxis, guiWidth, guiHeight);

        ChemicalStack stack = chemicalStackSupplier.getChemical();

        if (!stack.isEmpty()) {
            int scale = (int) (stack.getAmount() * (AbstractScreenComponentGauge.GaugeTextures.BACKGROUND_DEFAULT.textureHeight() - 2) / TileRotaryUnifier.MAX_CHEM_AMOUNT);

            TextureAtlasSprite sprite = MekanismRenderer.getChemicalTexture(stack.getChemical());
            RenderingUtils.bindTexture(sprite.atlasLocation());

            MekanismRenderer.color(graphics, stack.getChemical());

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


        graphics.blit(super.texture.getLocation(), guiWidth + xLocation, guiHeight + yLocation, AbstractScreenComponentGauge.GaugeTextures.LEVEL_DEFAULT.textureU(), 0, AbstractScreenComponentGauge.GaugeTextures.LEVEL_DEFAULT.textureWidth(), AbstractScreenComponentGauge.GaugeTextures.LEVEL_DEFAULT.textureHeight(), AbstractScreenComponentGauge.GaugeTextures.LEVEL_DEFAULT.imageWidth(), AbstractScreenComponentGauge.GaugeTextures.LEVEL_DEFAULT.imageHeight());
    }
}
