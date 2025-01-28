package electrodynamics.compatibility.jei.utils.ingredients;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.GasStack;
import electrodynamics.client.ClientRegister;
import electrodynamics.compatibility.jei.utils.gui.types.gasgauge.IGasGaugeTexture;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentGasGauge;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

public class IngredientRendererGasStack implements IIngredientRenderer<GasStack> {

	public static final IIngredientRenderer<GasStack> LIST_RENDERER = new IIngredientRenderer<>() {

		@Override
		public void render(GuiGraphics graphics, GasStack ingredient) {
			graphics.blit(0, 0, 0, 16, 16, ClientRegister.getSprite(ClientRegister.TEXTURE_GAS));
		}

		@Override
		public List<Component> getTooltip(GasStack ingredient, TooltipFlag tooltipFlag) {
			return List.of(ingredient.getGas().getDescription());
		}

	};

	private final int tankAmount;
	private final int mercuryOffset;
	private final int tooltipHeight;
	private final IGasGaugeTexture bars;

	public IngredientRendererGasStack(int tankAmount, int mercuryOffset, int tooltipHeight, IGasGaugeTexture bars) {
		this.tankAmount = tankAmount;
		this.mercuryOffset = mercuryOffset;
		this.tooltipHeight = tooltipHeight;
		this.bars = bars;
	}

	@Override
	public void render(GuiGraphics graphics, GasStack ingredient) {
		if (ingredient.isEmpty()) {
			return;
		}
		PoseStack stack = graphics.pose();

		stack.pushPose();

		float amt = ingredient.getAmount();

		if (amt < tankAmount / 50) {
			double amtPowTen = Math.pow(10, Math.round(Math.log10(amt) - Math.log10(5.5) + 0.5));
			if (amtPowTen == 0) {
				amtPowTen = 1;
			}
			double gaugePowTen = Math.log10(Math.pow(10, Math.round(Math.log10(tankAmount) - Math.log10(5.5) + 0.5)));
			double logAmtPowTen = Math.log10(amtPowTen);

			double delta = gaugePowTen - logAmtPowTen;

			amt *= Math.pow(10, delta);
		}

		float ratio = amt / tankAmount;

		ScreenComponentGasGauge.renderMercuryTexture(graphics, 0, mercuryOffset, ratio);

		graphics.blit(bars.getLocation(), bars.getXOffset(), mercuryOffset + bars.getYOffset(), bars.textureU(), bars.textureV(), bars.textureWidth(), bars.textureHeight(), bars.imageWidth(), bars.imageHeight());

		stack.popPose();
	}

	@Override
	public List<Component> getTooltip(GasStack ingredient, TooltipFlag tooltipFlag) {
		List<Component> tooltips = new ArrayList<>();
		tooltips.add(ingredient.getGas().getDescription());
		if (!ingredient.isEmpty()) {
			tooltips.add(ChatFormatter.formatFluidMilibuckets(ingredient.getAmount()).withStyle(ChatFormatting.GRAY));
			tooltips.add(ChatFormatter.getChatDisplayShort(ingredient.getTemperature(), DisplayUnit.TEMPERATURE_KELVIN).withStyle(ChatFormatting.GRAY));
			tooltips.add(ChatFormatter.getChatDisplayShort(ingredient.getPressure(), DisplayUnit.PRESSURE_ATM).withStyle(ChatFormatting.GRAY));
		}

		return tooltips;
	}

	@Override
	public int getWidth() {
		return bars.textureWidth() - 2;
	}

	@Override
	public int getHeight() {
		return Math.max(1, tooltipHeight - 1);
	}

}
