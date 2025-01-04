package electrodynamics.common.recipe.recipeutils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

public class CountableIngredient implements ICustomIngredient {

    public static final MapCodec<CountableIngredient> CODEC = RecordCodecBuilder.mapCodec(
            //
            instance -> instance.group(
                            //
                            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(instance0 -> instance0.ingredient),
                            //
                            Codec.INT.fieldOf("count").forGetter(instance0 -> instance0.stackSize)

                    )
                    //
                    .apply(instance, (ing, count) -> new CountableIngredient(ing, count))

            //
    );

    public static final Codec<List<CountableIngredient>> LIST_CODEC = CODEC.codec().listOf();

    public static final StreamCodec<RegistryFriendlyByteBuf, CountableIngredient> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, instance -> instance.ingredient,
            ByteBufCodecs.INT, instance -> instance.stackSize,
            CountableIngredient::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<CountableIngredient>> LIST_STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, List<CountableIngredient> ings) {
            buf.writeInt(ings.size());
            for (CountableIngredient ing : ings) {
                STREAM_CODEC.encode(buf, ing);
            }
        }

        @Override
        public List<CountableIngredient> decode(RegistryFriendlyByteBuf buf) {
            int length = buf.readInt();
            List<CountableIngredient> ings = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                ings.add(STREAM_CODEC.decode(buf));
            }
            return ings;
        }
    };

    private final int stackSize;

    private final Ingredient ingredient;

    @Nullable
    private ItemStack[] countedItems;

    public CountableIngredient(ItemStack stack) {
        this(Ingredient.of(stack), stack.getCount());
    }

    public CountableIngredient(Ingredient ingredient, int stackSize) {
        this.ingredient = ingredient;
        this.stackSize = stackSize;

    }

    @Override
    public boolean test(ItemStack stack) {
        return ingredient.test(stack) && stackSize <= stack.getCount();
    }

    @Override
    public Stream<ItemStack> getItems() {
        if (countedItems == null) {
            ItemStack[] items = ingredient.getItems();
            for (ItemStack item : items) {
                item.setCount(stackSize);
            }
            countedItems = items;
        }
        return Stream.of(countedItems);
    }

    public ItemStack[] getItemsArray() {
        if (countedItems == null) {
            ItemStack[] items = ingredient.getItems();
            for (ItemStack item : items) {
                item.setCount(stackSize);
            }
            countedItems = items;
        }
        return countedItems;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return ElectrodynamicsRecipeInit.COUNTABLE_INGREDIENT_TYPE.get();
    }

    public int getStackSize() {
        return stackSize;
    }

    @Override
    public String toString() {
        return getItemsArray().length == 0 ? "empty" : getItemsArray()[0].toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CountableIngredient otherIng) {

            return otherIng.stackSize == stackSize && ingredient.equals(otherIng.ingredient);

        }
        return false;
    }

}
