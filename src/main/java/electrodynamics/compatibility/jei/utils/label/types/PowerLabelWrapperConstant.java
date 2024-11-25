package electrodynamics.compatibility.jei.utils.label.types;

import electrodynamics.compatibility.jei.recipecategories.utils.AbstractRecipeCategory;
import electrodynamics.compatibility.jei.utils.label.AbstractLabelWrapper;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.network.chat.Component;

public class PowerLabelWrapperConstant extends AbstractLabelWrapper {

	private final int voltage;
	private final double wattage;

	public PowerLabelWrapperConstant(int xPos, int yPos, double joulesPerTick, int voltage) {
		super(Color.JEI_TEXT_GRAY, yPos, xPos, false);
		this.voltage = voltage;
		wattage = joulesPerTick * 20.0 / 1000.0;
	}

	@Override
	public Component getComponent(AbstractRecipeCategory<?> category, Object recipe) {

		return ElectroTextUtils.jeiTranslated("guilabel.power", voltage, wattage);

	}

}
