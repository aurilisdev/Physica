package electrodynamics.common.recipe.recipeutils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

public class EnchantmentIngredient implements ICustomIngredient {

    public static final MapCodec<EnchantmentIngredient> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(instance -> instance.ingredient),
                    TagKey.codec(Registries.ENCHANTMENT).listOf().fieldOf("enchantments").forGetter(instance -> instance.enchantments),
                    Codec.BOOL.fieldOf("isStrict").forGetter(instance -> instance.isStrict)


            ).apply(builder, EnchantmentIngredient::new)


    );

    private final Ingredient ingredient;
    private final List<TagKey<Enchantment>> enchantments;

    private final boolean isStrict;

    public EnchantmentIngredient(Ingredient base, List<TagKey<Enchantment>> enchantments, boolean isStrict) {
        this.ingredient = base;
        this.enchantments = enchantments;
        this.isStrict = isStrict;
    }

    @Override
    public boolean test(ItemStack stack) {

        boolean isBase = ingredient.test(stack);

        if (!isBase) {
            return false;
        }

        if (isStrict) {

            ItemEnchantments current = stack.getTagEnchantments();
            if (current.isEmpty() || current.keySet().size() != enchantments.size()) {
                return false;
            }

            boolean foundMatch = false;
            boolean hasStored = stack.has(DataComponents.STORED_ENCHANTMENTS);

            for (Holder<Enchantment> enchant : current.keySet()) {
                for (TagKey<Enchantment> tag : enchantments) {
                    if (enchant.is(tag)) {
                        foundMatch = true;
                        break;
                    }
                }
                if (!foundMatch && !hasStored) {
                    return false;
                }
                foundMatch = false;
            }

            current = stack.get(DataComponents.STORED_ENCHANTMENTS);

            if (current.isEmpty() || current.keySet().size() != enchantments.size()) {
                return false;
            }

            foundMatch = false;

            for (Holder<Enchantment> enchant : current.keySet()) {
                for (TagKey<Enchantment> tag : enchantments) {
                    if (enchant.is(tag)) {
                        foundMatch = true;
                        break;
                    }
                }
                if (!foundMatch && !hasStored) {
                    return false;
                }
                foundMatch = false;
            }

            return true;


        } else {
            for (TagKey<Enchantment> enchantment : enchantments) {
                if (EnchantmentHelper.hasTag(stack, enchantment)) {
                    return true;
                }

                if (!stack.has(DataComponents.STORED_ENCHANTMENTS)) {
                    continue;
                }

                for (var enchantmentHolder : stack.get(DataComponents.STORED_ENCHANTMENTS).keySet()) {
                    if (enchantmentHolder.is(enchantment)) {
                        return true;
                    }
                }
            }
        }


        return false;
    }

    @Override
    public Stream<ItemStack> getItems() {

        return Stream.of(ingredient.getItems());
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return ElectrodynamicsRecipeInit.ENCHANTMENT_INGREDIENT_TYPE.get();
    }

    @Override
    public String toString() {
        return "items: " + Arrays.toString(ingredient.getItems()) + ", enchants: " + StringUtils.join(enchantments.iterator(), ", ") + ", is strict: " + isStrict;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EnchantmentIngredient ing) {
            return ing.isStrict == isStrict && ing.ingredient.equals(ingredient) && ing.enchantments.equals(enchantments);
        }
        return false;
    }
}
