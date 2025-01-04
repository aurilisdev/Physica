package electrodynamics.client.render.itemdecorators;

import electrodynamics.api.gas.GasStack;
import electrodynamics.api.screen.ITexture;
import electrodynamics.common.item.gear.armor.types.ItemCombatArmor;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;

public class ItemDecoratorCombatHelmet implements IItemDecorator {
    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int x, int y) {

        GasStack gas = stack.getOrDefault(ElectrodynamicsDataComponentTypes.GAS_STACK.get(), GasStack.EMPTY);

        if(gas.isEmpty() || gas.getAmount() == ItemCombatArmor.HELMET_CAPACITY){
            return false;
        }

        int blackBoxHeight = 1;

        if(!stack.isBarVisible()){
            y += 1;
            blackBoxHeight = 2;
        }

        guiGraphics.setColor(0, 0, 0, 255);
        guiGraphics.blit(ITexture.Textures.WHITE.getLocation(), x + 2, y + 12, 199, 0, 0, 13, blackBoxHeight, 16, 16);
        guiGraphics.setColor(0, 255, 0, 255);

        int width = (int) (13 * ((double) gas.getAmount() / (double) ItemCombatArmor.HELMET_CAPACITY));
        guiGraphics.blit(ITexture.Textures.WHITE.getLocation(), x + 2, y + 12, 199, 0, 0, width, 1, 16, 16);
        RenderingUtils.resetShaderColor();




        return false;
    }
}
