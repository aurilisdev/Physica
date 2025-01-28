package electrodynamics.common.recipe.categories.chemicalreactor;

import java.util.Collections;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.ElectrodynamicsRecipeSerializer;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.GasIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.prefab.utilities.CodecUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class ChemicalReactorRecipeSerializer extends ElectrodynamicsRecipeSerializer<ChemicalReactorRecipe> {

    private static final MapCodec<ChemicalReactorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    //
                    Codec.STRING.fieldOf(GROUP).forGetter(ChemicalReactorRecipe::getGroup),
                    //
                    CountableIngredient.LIST_CODEC.optionalFieldOf(ITEM_INPUTS, Collections.emptyList()).forGetter(ChemicalReactorRecipe::getCountedIngredients),
                    //
                    FluidIngredient.LIST_CODEC.optionalFieldOf(FLUID_INPUTS, Collections.emptyList()).forGetter(ChemicalReactorRecipe::getFluidIngredients),
                    //
                    GasIngredient.LIST_CODEC.optionalFieldOf(GAS_INPUTS, Collections.emptyList()).forGetter(ChemicalReactorRecipe::getGasIngredients),
                    //
                    ItemStack.OPTIONAL_CODEC.optionalFieldOf("itemoutput", ItemStack.EMPTY).forGetter(ChemicalReactorRecipe::getItemRecipeOutput),
                    //
                    FluidStack.OPTIONAL_CODEC.optionalFieldOf("fluidoutput", FluidStack.EMPTY).forGetter(ChemicalReactorRecipe::getFluidRecipeOutput),
                    //
                    GasStack.CODEC.optionalFieldOf("gasoutput", GasStack.EMPTY).forGetter(ChemicalReactorRecipe::getGasRecipeOutput),
                    //
                    Codec.DOUBLE.optionalFieldOf(EXPERIENCE, 0.0).forGetter(ChemicalReactorRecipe::getXp),
                    //
                    Codec.INT.fieldOf(TICKS).forGetter(ChemicalReactorRecipe::getTicks),
                    //
                    Codec.DOUBLE.fieldOf(USAGE_PER_TICK).forGetter(ChemicalReactorRecipe::getUsagePerTick),
                    //
                    ProbableItem.LIST_CODEC.optionalFieldOf(ITEM_BIPRODUCTS, ProbableItem.NONE).forGetter(ChemicalReactorRecipe::getItemBiproducts),
                    //
                    ProbableFluid.LIST_CODEC.optionalFieldOf(FLUID_BIPRODUCTS, ProbableFluid.NONE).forGetter(ChemicalReactorRecipe::getFluidBiproducts),
                    //
                    ProbableGas.LIST_CODEC.optionalFieldOf(GAS_BIPRODUCTS, ProbableGas.NONE).forGetter(ChemicalReactorRecipe::getGasBiproducts)
                    //

            )
            //
            .apply(instance, ChemicalReactorRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, ChemicalReactorRecipe> STREAM_CODEC = CodecUtils.composite(
            ByteBufCodecs.STRING_UTF8, ChemicalReactorRecipe::getGroup,
            CountableIngredient.LIST_STREAM_CODEC, ChemicalReactorRecipe::getCountedIngredients,
            FluidIngredient.LIST_STREAM_CODEC, ChemicalReactorRecipe::getFluidIngredients,
            GasIngredient.LIST_STREAM_CODEC, ChemicalReactorRecipe::getGasIngredients,
            ItemStack.OPTIONAL_STREAM_CODEC, ChemicalReactorRecipe::getItemRecipeOutput,
            FluidStack.OPTIONAL_STREAM_CODEC, ChemicalReactorRecipe::getFluidRecipeOutput,
            GasStack.STREAM_CODEC, ChemicalReactorRecipe::getGasRecipeOutput,
            ByteBufCodecs.DOUBLE, ChemicalReactorRecipe::getXp,
            ByteBufCodecs.INT, ChemicalReactorRecipe::getTicks,
            ByteBufCodecs.DOUBLE, ChemicalReactorRecipe::getUsagePerTick,
            ProbableItem.LIST_STREAM_CODEC, ChemicalReactorRecipe::getItemBiproducts,
            ProbableFluid.LIST_STREAM_CODEC, ChemicalReactorRecipe::getFluidBiproducts,
            ProbableGas.LIST_STREAM_CODEC, ChemicalReactorRecipe::getGasBiproducts,
            ChemicalReactorRecipe::new
    );


    @Override
    public MapCodec<ChemicalReactorRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ChemicalReactorRecipe> streamCodec() {
        return STREAM_CODEC;
    }

}
