package electrodynamics.compatibility.jei.utils.ingredients;

import org.jetbrains.annotations.Nullable;

import electrodynamics.api.gas.GasStack;
import electrodynamics.registers.ElectrodynamicsGases;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;

public class IngredientHelperGasStack implements IIngredientHelper<GasStack> {

	@Override
	public IIngredientType<GasStack> getIngredientType() {
		return ElectrodynamicsJeiTypes.GAS_STACK;
	}

	@Override
	public String getDisplayName(GasStack ingredient) {
		return ingredient.getGas().getDescription().getString();
	}

	@Override
	public String getUniqueId(GasStack ingredient, UidContext context) {
		return ingredient.getGas().getDescription().getString();
	}

	@Override
	public ResourceLocation getResourceLocation(GasStack ingredient) {
		return ElectrodynamicsGases.GAS_REGISTRY.getKey(ingredient.getGas());
	}

	@Override
	public GasStack copyIngredient(GasStack ingredient) {
		return ingredient.copy();
	}

	@Override
	public String getErrorInfo(@Nullable GasStack ingredient) {
		return ingredient == null ? "null" : ingredient.toString();
	}

}
