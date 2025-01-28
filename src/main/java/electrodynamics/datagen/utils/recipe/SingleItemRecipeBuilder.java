package electrodynamics.datagen.utils.recipe;

import javax.annotation.Nullable;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleItemRecipe.Factory;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.neoforged.neoforge.common.conditions.ICondition;

/**
 * Imagine mojank providing a working data generator that didn't have the recipe book hard-coded into it
 * 
 * @author skip999
 *
 */
public class SingleItemRecipeBuilder implements RecipeBuilder {

    @Nullable
    private ICondition[] conditions;

    private ResourceLocation id;

    private final Item result;
    private final Ingredient ingredient;
    private final int count;
    private String group = "";
    private final SingleItemRecipe.Factory<?> factory;

    public SingleItemRecipeBuilder(Factory<?> factory, Ingredient ing, Item result, int count) {
        this.factory = factory;
        this.ingredient = ing;
        this.result = result;
        this.count = count;
    }

    public static SingleItemRecipeBuilder stonecuttingRecipe(Ingredient input, Item output, int count) {
        return new SingleItemRecipeBuilder(StonecutterRecipe::new, input, output, count);
    }

    public SingleItemRecipeBuilder complete(String parent, String name) {
        id = ResourceLocation.fromNamespaceAndPath(parent, name);
        return this;
    }

    @Override
    public void save(RecipeOutput output) {
        this.save(output, id);
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation altName) {
        if (conditions != null) {
            output.withConditions(conditions).accept(id, factory.create(group, ingredient, new ItemStack(result, count)), null);
        } else {
            output.accept(id, factory.create(group, ingredient, new ItemStack(result, count)), null);
        }
    }

    @Override
    public void save(RecipeOutput output, String name) {
        this.save(output, id);
    }

    @Override
    public RecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        return this;
    }

    @Override
    public SingleItemRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

}
