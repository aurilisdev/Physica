package electrodynamics.common.recipe.categories.fluid2gas;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.ElectrodynamicsRecipeSerializer;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import net.minecraft.network.FriendlyByteBuf;

public class Fluid2GasRecipeSerializer<T extends Fluid2GasRecipe> extends ElectrodynamicsRecipeSerializer<T> {

    private final Fluid2GasRecipe.Factory<T> factory;
    private Codec<T> codec;

    public Fluid2GasRecipeSerializer(Fluid2GasRecipe.Factory<T> factory) {
        this.factory = factory;
        codec = RecordCodecBuilder.create(instance -> instance.group(
                //
                Codec.STRING.fieldOf(GROUP).forGetter(instance0 -> instance0.getGroup()),
                //
                FluidIngredient.LIST_CODEC.fieldOf(FLUID_INPUTS).forGetter(instance0 -> instance0.getFluidIngredients()),
                //
                GasStack.CODEC.fieldOf(OUTPUT).forGetter(instance0 -> instance0.getGasRecipeOutput()),
                //
                Codec.DOUBLE.optionalFieldOf(EXPERIENCE, 0.0).forGetter(instance0 -> instance0.getXp()),
                //
                Codec.INT.fieldOf(TICKS).forGetter(instance0 -> instance0.getTicks()),
                //
                Codec.DOUBLE.fieldOf(USAGE_PER_TICK).forGetter(instance0 -> instance0.getUsagePerTick()),
                //
                ProbableItem.LIST_CODEC.optionalFieldOf(ITEM_BIPRODUCTS, null).forGetter(instance0 -> instance0.getItemBiproducts()),
                //
                ProbableFluid.LIST_CODEC.optionalFieldOf(FLUID_BIPRODUCTS, null).forGetter(instance0 -> instance0.getFluidBiproducts()),
                //
                ProbableGas.LIST_CODEC.optionalFieldOf(GAS_BIPRODUCTS, null).forGetter(instance0 -> instance0.getGasBiproducts())
        //

        )
                //
                .apply(instance, factory::create)

        );
    }

    @Override
    public Codec<T> codec() {
        return codec;
    }

    @Override
    public @Nullable T fromNetwork(FriendlyByteBuf buffer) {
        String group = buffer.readUtf();
        boolean hasItemBi = buffer.readBoolean();
        boolean hasFluidBi = buffer.readBoolean();
        boolean hasGasBi = buffer.readBoolean();
        List<FluidIngredient> inputs = FluidIngredient.readList(buffer);
        GasStack output = GasStack.readFromBuffer(buffer);
        double experience = buffer.readDouble();
        int ticks = buffer.readInt();
        double usagePerTick = buffer.readDouble();
        List<ProbableItem> itemBi = null;
        List<ProbableFluid> fluidBi = null;
        List<ProbableGas> gasBi = null;
        if (hasItemBi) {
            itemBi = ProbableItem.readList(buffer);
        }
        if (hasFluidBi) {
            fluidBi = ProbableFluid.readList(buffer);

        }
        if (hasGasBi) {
            gasBi = ProbableGas.readList(buffer);
        }
        return factory.create(group, inputs, output, experience, ticks, usagePerTick, itemBi, fluidBi, gasBi);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        buffer.writeUtf(recipe.getGroup());
        buffer.writeBoolean(recipe.hasItemBiproducts());
        buffer.writeBoolean(recipe.hasFluidBiproducts());
        buffer.writeBoolean(recipe.hasGasBiproducts());
        FluidIngredient.writeList(buffer, recipe.getFluidIngredients());
        recipe.getGasRecipeOutput().writeToBuffer(buffer);
        buffer.writeDouble(recipe.getXp());
        buffer.writeInt(recipe.getTicks());
        buffer.writeDouble(recipe.getUsagePerTick());
        if (recipe.hasItemBiproducts()) {
            ProbableItem.writeList(buffer, recipe.getItemBiproducts());
        }
        if (recipe.hasFluidBiproducts()) {
            ProbableFluid.writeList(buffer, recipe.getFluidBiproducts());
        }
        if (recipe.hasGasBiproducts()) {
            ProbableGas.writeList(buffer, recipe.getGasBiproducts());
        }
    }

}
