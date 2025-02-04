package electrodynamics.client.render.event.guipost;

import java.util.List;

import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.common.item.gear.armor.types.ItemJetpack;
import electrodynamics.common.item.gear.armor.types.ItemServoLeggings;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class HandlerArmorData extends AbstractPostGuiOverlayHandler {

    private final Component statusGogglesOn = ElectroTextUtils.tooltip("nightvisiongoggles.status").withStyle(ChatFormatting.GRAY).append(ElectroTextUtils.tooltip("nightvisiongoggles.on").withStyle(ChatFormatting.GREEN));
    private final Component statusGogglesOff = ElectroTextUtils.tooltip("nightvisiongoggles.status").withStyle(ChatFormatting.GRAY).append(ElectroTextUtils.tooltip("nightvisiongoggles.off").withStyle(ChatFormatting.RED));

    @Override
    public void renderToScreen(GuiGraphics graphics, DeltaTracker tracker, Minecraft minecraft) {

        if (!Constants.RENDER_COMBAT_ARMOR_STATUS) {
            return;
        }

        List<ItemStack> armor = minecraft.player.getInventory().armor;

        graphics.pose().pushPose();

        int heightOffset = graphics.guiHeight();

        if (!armor.get(0).isEmpty() && handleBoots(armor.get(0), graphics, minecraft, heightOffset)) {
            heightOffset -= 30;
        }

        if (!armor.get(1).isEmpty() && handleLeggings(armor.get(1), graphics, minecraft, heightOffset)) {
            heightOffset -= 30;
        }

        if (!armor.get(2).isEmpty() && handleChestplate(armor.get(2), graphics, minecraft, heightOffset)) {
            heightOffset -= 30;
        }

        if (!armor.get(3).isEmpty() && handleHelmet(armor.get(3), graphics, minecraft, heightOffset)) {
            heightOffset -= 30;
        }

        graphics.pose().popPose();

    }

    private boolean handleHelmet(ItemStack helmet, GuiGraphics graphics, Minecraft minecraft, int heightOffset) {

        boolean renderItem = false;

        if (ItemUtils.testItems(helmet.getItem(), ElectrodynamicsItems.ITEM_NIGHTVISIONGOGGLES.get(), ElectrodynamicsItems.ITEM_COMBATHELMET.get())) {
            renderItem = true;
            Component mode;
            if (helmet.getOrDefault(ElectrodynamicsDataComponentTypes.ON, false)) {
                mode = statusGogglesOn;
            } else {
                mode = statusGogglesOff;
            }
            graphics.drawString(minecraft.font, mode, 35, heightOffset - 30, Color.BLACK.color());
            graphics.drawString(minecraft.font, ChatFormatter.getChatDisplayShort(((IItemElectric)helmet.getItem()).getJoulesStored(helmet), DisplayUnit.JOULES), 35, heightOffset - 20, Color.WHITE.color(), false);
        }

        if (renderItem) {
            RenderingUtils.renderItemScaled(graphics, helmet.getItem(), 10, heightOffset - 30, 1.5F);
        }

        return renderItem;

    }

    private boolean handleChestplate(ItemStack chestplate, GuiGraphics graphics, Minecraft minecraft, int heightOffset) {

        boolean renderItem = false;

        if (ItemUtils.testItems(chestplate.getItem(), ElectrodynamicsItems.ITEM_JETPACK.get())) {
            renderItem = true;
            Component mode = ItemJetpack.getModeText(chestplate.getOrDefault(ElectrodynamicsDataComponentTypes.MODE, 0));

            IGasHandlerItem handler = chestplate.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

            graphics.pose().pushPose();

            if (handler != null) {
                GasStack gas = handler.getGasInTank(0);
                if (gas.isEmpty()) {
                    graphics.drawString(minecraft.font, mode, 35, heightOffset - 30, 0);
                    graphics.drawString(minecraft.font, ElectroTextUtils.ratio(Component.literal("0"), ChatFormatter.formatFluidMilibuckets(ItemJetpack.MAX_CAPACITY)), 35, heightOffset - 20, Color.WHITE.color());
                } else {
                    graphics.drawString(minecraft.font, mode, 35, heightOffset - 30, 0);
                    graphics.drawString(minecraft.font, ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(gas.getAmount()), ChatFormatter.formatFluidMilibuckets(ItemJetpack.MAX_CAPACITY)), 35, heightOffset - 20, Color.WHITE.color(), false);
                }
            }

            graphics.pose().popPose();
        }

        if (ItemUtils.testItems(chestplate.getItem(), ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get())) {

            graphics.pose().pushPose();

            graphics.pose().scale(0.8F, 0.8F, 0.8F);
            renderItem = true;
            Component mode = ItemJetpack.getModeText(chestplate.getOrDefault(ElectrodynamicsDataComponentTypes.MODE, 0));

            IGasHandlerItem handler = chestplate.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

            if (handler != null) {
                GasStack gas = handler.getGasInTank(0);
                int x = (int) (35 / 0.8F);
                if (gas.isEmpty()) {
                    graphics.drawString(minecraft.font, mode, x, (int) ((heightOffset - 34) / 0.8F), 0);
                    graphics.drawString(minecraft.font, ElectroTextUtils.ratio(Component.literal("0"), ChatFormatter.formatFluidMilibuckets(ItemJetpack.MAX_CAPACITY)), x, (int) ((heightOffset - 25) / 0.8F), -1);
                    graphics.drawString(minecraft.font, ElectroTextUtils.tooltip("ceramicplatecount", Component.literal(chestplate.getOrDefault(ElectrodynamicsDataComponentTypes.PLATES, 0) + "")).withStyle(ChatFormatting.AQUA), x, (int) ((heightOffset - 16) / 0.8F), Color.WHITE.color());
                } else {
                    graphics.drawString(minecraft.font, mode, x, (int) ((heightOffset - 34) / 0.8F), 0);
                    graphics.drawString(minecraft.font, ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(gas.getAmount()), ChatFormatter.formatFluidMilibuckets(ItemJetpack.MAX_CAPACITY)), x, (int) ((heightOffset - 25) / 0.8F), -1);
                    graphics.drawString(minecraft.font, ElectroTextUtils.tooltip("ceramicplatecount", Component.literal(chestplate.getOrDefault(ElectrodynamicsDataComponentTypes.PLATES, 0) + "")).withStyle(ChatFormatting.AQUA), x, (int) ((heightOffset - 16) / 0.8F), Color.WHITE.color());
                }
            }

            graphics.pose().popPose();
        }

        if (ItemUtils.testItems(chestplate.getItem(), ElectrodynamicsItems.ITEM_COMPOSITECHESTPLATE.get())) {
            renderItem = true;
            graphics.drawString(minecraft.font, ElectroTextUtils.tooltip("ceramicplatecount", Component.literal(chestplate.getOrDefault(ElectrodynamicsDataComponentTypes.PLATES, 0) + "")).withStyle(ChatFormatting.AQUA), 35, heightOffset - 25, Color.WHITE.color(), false);
        }

        if (renderItem) {
            RenderingUtils.renderItemScaled(graphics, chestplate.getItem(), 10, heightOffset - 30, 1.5F);
        }

        return renderItem;
    }

    private boolean handleLeggings(ItemStack leggings, GuiGraphics graphics, Minecraft minecraft, int heightOffset) {

        boolean renderItem = false;

        if (ItemUtils.testItems(leggings.getItem(), ElectrodynamicsItems.ITEM_SERVOLEGGINGS.get(), ElectrodynamicsItems.ITEM_COMBATLEGGINGS.get())) {
            renderItem = true;
            Component on;
            if (leggings.getOrDefault(ElectrodynamicsDataComponentTypes.ON, false)) {
                on = ElectroTextUtils.tooltip("nightvisiongoggles.status").withStyle(ChatFormatting.GRAY).append(ElectroTextUtils.tooltip("nightvisiongoggles.on").withStyle(ChatFormatting.GREEN));
            } else {
                on = ElectroTextUtils.tooltip("nightvisiongoggles.status").withStyle(ChatFormatting.GRAY).append(ElectroTextUtils.tooltip("nightvisiongoggles.off").withStyle(ChatFormatting.RED));
            }
            int x = (int) (35 / 0.8F);
            graphics.pose().pushPose();
            graphics.pose().scale(0.8F, 0.8F, 0.8F);
            graphics.drawString(minecraft.font, on, x, (int) ((heightOffset - 34) / 0.8F), 0);
            graphics.drawString(minecraft.font, ItemServoLeggings.getModeText(leggings.getOrDefault(ElectrodynamicsDataComponentTypes.MODE, -1)), x, (int) ((heightOffset - 25) / 0.8F), Color.BLACK.color(), false);
            graphics.drawString(minecraft.font, ChatFormatter.getChatDisplayShort(((IItemElectric)leggings.getItem()).getJoulesStored(leggings), DisplayUnit.JOULES), x, (int) ((heightOffset - 16) / 0.8F), Color.WHITE.color(), false);
            graphics.pose().popPose();
        }

        if (renderItem) {
            RenderingUtils.renderItemScaled(graphics, leggings.getItem(), 10, heightOffset - 30, 1.5F);
        }

        return renderItem;
    }

    private boolean handleBoots(ItemStack boots, GuiGraphics graphics, Minecraft minecraft, int heightOffset) {

        boolean renderItem = false;

        if (ItemUtils.testItems(boots.getItem(), ElectrodynamicsItems.ITEM_HYDRAULICBOOTS.get(), ElectrodynamicsItems.ITEM_COMBATBOOTS.get())) {
            renderItem = true;
            IFluidHandlerItem handler = boots.getCapability(Capabilities.FluidHandler.ITEM);
            if (handler != null) {
                graphics.drawString(minecraft.font, ChatFormatter.formatFluidMilibuckets(handler.getFluidInTank(0).getAmount()), 35, heightOffset - 25, Color.WHITE.color());
            }
        }

        if (renderItem) {
            RenderingUtils.renderItemScaled(graphics, boots.getItem(), 10, heightOffset - 30, 1.5F);
        }

        return renderItem;
    }

}
