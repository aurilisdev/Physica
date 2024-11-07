package electrodynamics.compatibility.jei.recipecategories.utils.psuedorecipes.types;

import electrodynamics.common.reloadlistener.GasCollectorChromoCardsRegister;
import net.minecraft.world.item.ItemStack;

public class PsuedoGasCollectorRecipe {

    public final ItemStack input;
    public final GasCollectorChromoCardsRegister.AtmosphericResult output;

    public PsuedoGasCollectorRecipe(ItemStack input, GasCollectorChromoCardsRegister.AtmosphericResult output){
        this.input = input;
        this.output = output;
    }
}
