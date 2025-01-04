package electrodynamics.datagen.utils.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;

public class ShapedCraftingRecipeBuilder implements RecipeBuilder {

    private ResourceLocation id;

    private Item item;
    private int count;
    private List<String> patterns = new ArrayList<>();
    private Map<Character, Ingredient> keys = new HashMap<>();
    @Nullable
    private ICondition[] recipeConditions;
    private String group = "";

    private ShapedCraftingRecipeBuilder(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public static ShapedCraftingRecipeBuilder start(Item item, int count) {
        return new ShapedCraftingRecipeBuilder(item, count);
    }

    public ShapedCraftingRecipeBuilder addPattern(String pattern) {
        if (pattern.length() > 3) {
            throw new UnsupportedOperationException("The pattern " + pattern + " is more than 3 characters long and is not valid!");
        }
        if (patterns.size() > 3) {
            throw new UnsupportedOperationException("Already 3 patterns present");
        }
        patterns.add(pattern);
        return this;
    }

    public ShapedCraftingRecipeBuilder addKey(Character key, Ingredient ing) {
        keys.put(key, ing);
        return this;
    }

    public ShapedCraftingRecipeBuilder addKey(Character key, ICustomIngredient ing) {
        keys.put(key, new Ingredient(ing));
        return this;
    }

    public ShapedCraftingRecipeBuilder addKey(Character key, TagKey<Item> ing) {
        keys.put(key, Ingredient.of(ing));
        return this;
    }

    public ShapedCraftingRecipeBuilder addKey(Character key, String parent, String tag) {
        keys.put(key, Ingredient.of(itemTag(ResourceLocation.fromNamespaceAndPath(parent, tag))));
        return this;
    }

    public ShapedCraftingRecipeBuilder addKey(Character key, Item item) {
        return addKey(key, new ItemStack(item));
    }

    public ShapedCraftingRecipeBuilder addKey(Character key, ItemStack item) {
        keys.put(key, Ingredient.of(item));
        return this;
    }

    public ShapedCraftingRecipeBuilder addConditions(ICondition... conditions) {
        recipeConditions = conditions;
        return this;
    }

    public ShapedCraftingRecipeBuilder complete(String parent, String name, RecipeOutput output) {
        for (Character character : keys.keySet()) {
            if (isKeyNotUsed(character)) {
                throw new UnsupportedOperationException("The key " + character + " is defined by never used!");
            }
        }
        id = ResourceLocation.fromNamespaceAndPath(parent, name);
        save(output);
        return this;
    }

    private boolean isKeyNotUsed(char character) {
        for (String str : patterns) {
            for (char ch : str.toCharArray()) {
                if (ch == character) {
                    return false;
                }
            }
        }
        return true;

    }

    private TagKey<Item> itemTag(ResourceLocation tag) {
        return TagKey.create(Registries.ITEM, tag);
    }

    @Override
    public RecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        return this;
    }

    @Override
    public ShapedCraftingRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return item;
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation altName) {
        if (recipeConditions != null) {
            output.withConditions(recipeConditions).accept(id, new ShapedRecipe(group, CraftingBookCategory.MISC, ShapedRecipePattern.of(keys, patterns), new ItemStack(item, count)), null);
        } else {
            output.accept(id, new ShapedRecipe(group, CraftingBookCategory.MISC, ShapedRecipePattern.of(keys, patterns), new ItemStack(item, count)), null);
        }
    }

    @Override
    public void save(RecipeOutput output) {
        this.save(output, id);
    }

    @Override
    public void save(RecipeOutput output, String group) {
        this.save(output, id);
    }

}
