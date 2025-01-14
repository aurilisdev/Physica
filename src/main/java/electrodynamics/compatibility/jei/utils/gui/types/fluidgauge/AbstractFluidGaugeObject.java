package electrodynamics.compatibility.jei.utils.gui.types.fluidgauge;

import electrodynamics.api.screen.ITexture;
import electrodynamics.compatibility.jei.utils.gui.ScreenObject;

public abstract class AbstractFluidGaugeObject extends ScreenObject {

	public AbstractFluidGaugeObject(ITexture texture, int x, int y) {
		super(texture, x, y);
	}

	public abstract int getFluidTextWidth();

	public abstract int getFluidTextHeight();

	public abstract int getFluidXPos();

	public abstract int getFluidYPos();

}
