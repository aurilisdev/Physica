package electrodynamics.compatibility.jei.utils.label.types;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.recipe.ElectrodynamicsRecipe;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.compatibility.jei.recipecategories.utils.AbstractRecipeCategory;
import electrodynamics.compatibility.jei.utils.label.AbstractLabelWrapper;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.network.chat.Component;

public class BiproductPercentWrapperElectroRecipe extends AbstractLabelWrapper {

	private final BiproductType type;
	private final int index;

	public BiproductPercentWrapperElectroRecipe(int xPos, int yPos, BiproductType type, int index) {
		super(Color.JEI_TEXT_GRAY, yPos, xPos, false);
		this.type = type;
		this.index = index;
	}

	@Override
	public Component getComponent(AbstractRecipeCategory<?> category, Object recipe) {

		switch(type){
			case ITEM:
				ElectrodynamicsRecipe electro = (ElectrodynamicsRecipe) recipe;
				if (electro.hasItemBiproducts() && index < electro.getItemBiproducts().size()) {
					ProbableItem item = electro.getItemBiproducts().get(index);
					return ChatFormatter.getChatDisplayShort(item.getChance() * 100, DisplayUnit.PERCENTAGE);
				}
				break;
			case FLUID:
				electro = (ElectrodynamicsRecipe) recipe;
				if (electro.hasFluidBiproducts() && index < electro.getFluidBiproducts().size()) {
					ProbableFluid item = electro.getFluidBiproducts().get(index);
					return ChatFormatter.getChatDisplayShort(item.getChance() * 100, DisplayUnit.PERCENTAGE);
				}
				break;
			case GAS:
				electro = (ElectrodynamicsRecipe) recipe;
				if (electro.hasGasBiproducts() && index < electro.getFluidBiproducts().size()) {
					ProbableGas item = electro.getGasBiproducts().get(index);
					return ChatFormatter.getChatDisplayShort(item.getChance() * 100, DisplayUnit.PERCENTAGE);
				}
				break;
		}
		return Component.empty();
	}

	public static enum BiproductType {
		ITEM, FLUID, GAS;
	}
}
