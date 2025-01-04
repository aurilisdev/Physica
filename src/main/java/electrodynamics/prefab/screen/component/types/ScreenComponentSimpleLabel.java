package electrodynamics.prefab.screen.component.types;

import java.util.function.Supplier;

import electrodynamics.prefab.screen.component.utils.AbstractScreenComponent;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ScreenComponentSimpleLabel extends AbstractScreenComponent {

	private Supplier<Component> text = Component::empty;
	public Color color = Color.WHITE;

	public ScreenComponentSimpleLabel(int x, int y, int height, Color color, Component text) {
		this(x, y, height, color, () -> text);
	}

	public ScreenComponentSimpleLabel(int x, int y, int height, Color color, Supplier<Component> text) {
		super(x, y, 0, height);
		this.text = text;
		this.color = color;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return isPointInRegion(xLocation, yLocation, mouseX - gui.getGuiWidth(), mouseY - gui.getGuiHeight(), gui.getFontRenderer().width(text.get()), height);
	}

	@Override
	public void renderForeground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
		if (isVisible()) {
			graphics.drawString(gui.getFontRenderer(), text.get(), xLocation, yLocation, color.color(), false);
		}
	}

}
