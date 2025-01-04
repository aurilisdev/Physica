package electrodynamics.client.render.event.guipost;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.item.IItemTemperate;
import electrodynamics.common.item.gear.tools.electric.utils.ItemRailgun;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;

public class HandlerRailgunTemperature extends AbstractPostGuiOverlayHandler {

	@Override
	public void renderToScreen(GuiGraphics graphics, DeltaTracker tracker, Minecraft minecraft) {
		Player player = minecraft.player;
		ItemStack gunStackMainHand = player.getItemBySlot(EquipmentSlot.MAINHAND);
		ItemStack gunStackOffHand = player.getItemBySlot(EquipmentSlot.OFFHAND);

		if (gunStackMainHand.getItem() instanceof ItemRailgun) {
			renderHeatToolTip(graphics, minecraft, gunStackMainHand);
		} else if (gunStackOffHand.getItem() instanceof ItemRailgun) {
			renderHeatToolTip(graphics, minecraft, gunStackOffHand);
		}

	}

	private void renderHeatToolTip(GuiGraphics graphics, Minecraft minecraft, ItemStack item) {

		ItemRailgun railgun = (ItemRailgun) item.getItem();
		double temperature = IItemTemperate.getTemperature(item);

		PoseStack stack = graphics.pose();

		stack.pushPose();

		// ElectroTextUtils.tooltip("railguntemp", Component.literal(temperature + correction + " C"));

		Component currTempText = ElectroTextUtils.tooltip("railguntemp", ChatFormatter.getChatDisplayShort(temperature, DisplayUnit.TEMPERATURE_CELCIUS)).withStyle(ChatFormatting.YELLOW);
		Component maxTempText = ElectroTextUtils.tooltip("railgunmaxtemp", ChatFormatter.getChatDisplayShort(railgun.getMaxTemp(), DisplayUnit.TEMPERATURE_CELCIUS)).withStyle(ChatFormatting.YELLOW);

		graphics.drawString(minecraft.font, currTempText, 2, 2, 0);
		graphics.drawString(minecraft.font, maxTempText, 2, 12, 0);

		if(item.getCapability(Capabilities.FluidHandler.ITEM) != null){
			int amount = item.getCapability(Capabilities.FluidHandler.ITEM).getFluidInTank(0).getAmount();
			Component fluid = ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(amount), ChatFormatter.formatFluidMilibuckets(ItemRailgun.CAPACITY)).withStyle(ChatFormatting.GRAY);
			graphics.drawString(minecraft.font, fluid, 2, 22, 0);
		}

		if (temperature >= railgun.getOverheatTemp()) {
			Component overheatWarn = ElectroTextUtils.tooltip("railgunoverheat").withStyle(ChatFormatting.RED, ChatFormatting.BOLD);
			graphics.drawString(minecraft.font, overheatWarn, 2, 32, 0);
		}

		stack.popPose();
	}

}
