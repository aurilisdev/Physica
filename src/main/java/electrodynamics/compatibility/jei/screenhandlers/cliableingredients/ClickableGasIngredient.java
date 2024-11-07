package electrodynamics.compatibility.jei.screenhandlers.cliableingredients;

import electrodynamics.api.gas.GasStack;
import electrodynamics.compatibility.jei.utils.ingredients.ElectrodynamicsJeiTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;

public class ClickableGasIngredient extends AbstractClickableIngredient<GasStack> {

    private final GasIngredientType typeIngredient;

    public ClickableGasIngredient(Rect2i rect, GasStack gasStack) {
        super(rect);
        typeIngredient = new GasIngredientType(gasStack);
    }

    @Override
    public ITypedIngredient<GasStack> getTypedIngredient() {
        return typeIngredient;
    }

    private record GasIngredientType(GasStack gasStack) implements ITypedIngredient<GasStack> {

        @Override
        public IIngredientType<GasStack> getType() {
            return ElectrodynamicsJeiTypes.GAS_STACK;
        }

        @Override
        public GasStack getIngredient() {
            return gasStack;
        }

    }

}
