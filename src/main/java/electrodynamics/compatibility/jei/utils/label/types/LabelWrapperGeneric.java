package electrodynamics.compatibility.jei.utils.label.types;

import electrodynamics.compatibility.jei.recipecategories.utils.AbstractRecipeCategory;
import electrodynamics.compatibility.jei.utils.label.AbstractLabelWrapper;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.network.chat.Component;

public class LabelWrapperGeneric extends AbstractLabelWrapper {

	private final Component label;

	public LabelWrapperGeneric(Color color, int yPos, int xPos, boolean xIsEnd, Component label) {
		super(color, yPos, xPos, xIsEnd);
		this.label = label;
	}

	@Override
	public Component getComponent(AbstractRecipeCategory<?> category, Object recipe) {
		return label;
	}

}
