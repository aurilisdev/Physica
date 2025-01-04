package electrodynamics.common.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;

public abstract class ElectrodynamicsRecipeSerializer<T extends ElectrodynamicsRecipe> implements RecipeSerializer<T> {

    public static final String COUNT = "count";
    public static final String ITEM_INPUTS = "iteminputs";
    public static final String FLUID_INPUTS = "fluidinputs";
    public static final String GAS_INPUTS = "gasinputs";
    public static final String ITEM_BIPRODUCTS = "itembi";
    public static final String FLUID_BIPRODUCTS = "fluidbi";
    public static final String GAS_BIPRODUCTS = "gasbi";
    public static final String OUTPUT = "output";
    public static final String EXPERIENCE = "experience";
    public static final String TICKS = "ticks";
    public static final String USAGE_PER_TICK = "usagepertick";
    public static final String GROUP = "group";

}
