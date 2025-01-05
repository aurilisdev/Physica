package electrodynamics.common.recipe.recipeutils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Extension of Ingredient that adds Fluid compatibility
 * 
 * @author skip999
 *
 */
public class FluidIngredient implements Predicate<FluidStack>, ICustomIngredient {

    // Mojank...

    public static final MapCodec<FluidIngredient> CODEC_DIRECT_FLUID = RecordCodecBuilder.mapCodec(instance ->
    //
    instance.group(
            //
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid").forGetter(instance0 -> instance0.fluid),
            //
            Codec.INT.fieldOf("amount").forGetter(instance0 -> instance0.amount)

    )
            //
            .apply(instance, (fluid, amount) -> new FluidIngredient(fluid, amount))

    );

    public static final MapCodec<FluidIngredient> CODEC_TAGGED_FLUID = RecordCodecBuilder.mapCodec(instance ->
    //
    instance.group(
            //
            TagKey.codec(Registries.FLUID).fieldOf("tag").forGetter(instance0 -> instance0.tag),
            //
            Codec.INT.fieldOf("amount").forGetter(instance0 -> instance0.amount)

    )
            //
            .apply(instance, (tag, amount) -> new FluidIngredient(tag, amount))
    //

    );

    public static final MapCodec<FluidIngredient> CODEC = NeoForgeExtraCodecs.xor(CODEC_TAGGED_FLUID, CODEC_DIRECT_FLUID).xmap(either -> either.map(tag -> tag, fluid -> fluid), value -> {
        //

        if (value.tag != null) {
            return Either.left(value);
        } else if (value.fluid != null) {
            return Either.right(value);
        } else {
            throw new UnsupportedOperationException("The Fluid Ingredient neither has a tag nor a direct fluid value defined!");
        }

    });

    public static final Codec<List<FluidIngredient>> LIST_CODEC = CODEC.codec().listOf();

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidIngredient> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, FluidIngredient ing) {
            List<FluidStack> fluidStacks = ing.getMatchingFluids();
            buf.writeInt(fluidStacks.size());
            for (FluidStack stack : fluidStacks) {
                FluidStack.STREAM_CODEC.encode(buf, stack);
            }
        }

        @Override
        public FluidIngredient decode(RegistryFriendlyByteBuf buf) {
            List<FluidStack> stacks = new ArrayList<>();
            int count = buf.readInt();
            for (int i = 0; i < count; i++) {
                stacks.add(FluidStack.STREAM_CODEC.decode(buf));
            }
            return new FluidIngredient(stacks);
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, List<FluidIngredient>> LIST_STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, List<FluidIngredient> ings) {
            buf.writeInt(ings.size());
            for (FluidIngredient ing : ings) {
                STREAM_CODEC.encode(buf, ing);
            }
        }

        @Override
        public List<FluidIngredient> decode(RegistryFriendlyByteBuf buf) {
            int length = buf.readInt();
            List<FluidIngredient> ings = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                ings.add(STREAM_CODEC.decode(buf));
            }
            return ings;
        }
    };


    @Nonnull
    private List<FluidStack> fluidStacks;

    @Nullable
    public TagKey<Fluid> tag;
    @Nullable
    private Fluid fluid;
    private int amount;

    public FluidIngredient(FluidStack fluidStack) {
        this.fluid = fluidStack.getFluid();
        this.amount = fluidStack.getAmount();
    }

    public FluidIngredient(Fluid fluid, int amount) {
        this(new FluidStack(fluid, amount));
    }

    public FluidIngredient(List<FluidStack> fluidStack) {
        fluidStacks = fluidStack;
        FluidStack fluid = getFluidStack();
        this.fluid = fluid.getFluid();
        this.amount = fluid.getAmount();
    }

    public FluidIngredient(TagKey<Fluid> tag, int amount) {
        this.tag = tag;
        this.amount = amount;

    }

    @Override
    public boolean test(ItemStack stack) {
        return false;
    }

    @Override
    public Stream<ItemStack> getItems() {
        return Stream.empty();
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return ElectrodynamicsRecipeInit.FLUID_INGREDIENT_TYPE.get();
    }

    @Override
    public boolean test(@Nullable FluidStack t) {
        if(t == null || t.isEmpty()){
            return false;
        }

        for (FluidStack stack : getMatchingFluids()) {
            if (t.getAmount() >= stack.getAmount()) {
                if (t.getFluid().isSame(stack.getFluid())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<FluidStack> getMatchingFluids() {

        if (fluidStacks == null) {

            fluidStacks = new ArrayList<>();

            if (tag != null) {

                BuiltInRegistries.FLUID.getTag(tag).get().forEach(h -> {
                    fluidStacks.add(new FluidStack(h, amount));
                });

            } else if (fluid != null) {

                fluidStacks.add(new FluidStack(fluid, amount));

            } else {
                throw new UnsupportedOperationException("Fluid Ingredient has neither a fluid nor a fluid tag defined");
            }

        }

        return fluidStacks;
    }

    public FluidStack getFluidStack() {
        return getMatchingFluids().size() < 1 ? FluidStack.EMPTY : getMatchingFluids().get(0);
    }

    @Override
    public String toString() {
        return "Fluid : " + getFluidStack().getFluid().toString() + ", Amt : " + amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FluidIngredient ing) {
            return ing.getMatchingFluids().equals(getMatchingFluids()) && ing.amount == amount;
        }
        return false;
    }

}
