package electrodynamics.compatibility.jei.utils.gui.types.gasgauge;

import electrodynamics.compatibility.jei.utils.gui.ScreenObject;

public abstract class AbstractGasGaugeObject extends ScreenObject {

	private final IGasGaugeTexture bars;

	public AbstractGasGaugeObject(IGasGaugeTexture base, IGasGaugeTexture bars, int x, int y) {
		super(base, x, y);
		this.bars = bars;
	}
	public IGasGaugeTexture getBarsTexture() {
		return bars;
	}

}
