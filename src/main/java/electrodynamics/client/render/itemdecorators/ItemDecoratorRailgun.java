package electrodynamics.client.render.itemdecorators;

import electrodynamics.api.fluid.FluidStackComponent;
import electrodynamics.api.screen.ITexture;
import electrodynamics.common.item.gear.tools.electric.utils.ItemRailgun;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import net.neoforged.neoforge.fluids.FluidStack;

public class ItemDecoratorRailgun implements IItemDecorator {
    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int x, int y) {

        FluidStack fluid = stack.getOrDefault(ElectrodynamicsDataComponentTypes.FLUID_STACK.get(), FluidStackComponent.EMPTY).fluid;

        if(fluid.isEmpty() || fluid.getAmount() == ItemRailgun.CAPACITY) {
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

        int width = (int) (13 * ((double) fluid.getAmount() / (double) ItemRailgun.CAPACITY));
        guiGraphics.blit(ITexture.Textures.WHITE.getLocation(), x + 2, y + 12, 199, 0, 0, width, 1, 16, 16);
        RenderingUtils.resetShaderColor();


        return false;
    }
}
