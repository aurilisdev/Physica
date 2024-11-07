package electrodynamics.compatibility.jei.utils.label;

import electrodynamics.compatibility.jei.recipecategories.utils.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;

public abstract class AbstractLabelWrapper {

	private int color;
	private int yPos;
	private int xPos;
	private boolean xIsEnd;

	public AbstractLabelWrapper(int color, int yPos, int xPos, boolean xIsEnd) {
		this.color = color;
		this.yPos = yPos;
		this.xPos = xPos;
		this.xIsEnd = xIsEnd;
	}

	public int getColor() {
		return color;
	}

	public int getYPos() {
		return yPos;
	}

	public int getXPos() {
		return xPos;
	}

	public boolean xIsEnd() {
		return xIsEnd;
	}

	public abstract Component getComponent(AbstractRecipeCategory<?> category, Object recipe);
}
