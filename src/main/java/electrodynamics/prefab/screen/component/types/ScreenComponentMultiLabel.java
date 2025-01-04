package electrodynamics.prefab.screen.component.types;

import java.util.function.Consumer;

import electrodynamics.prefab.screen.component.utils.AbstractScreenComponent;
import net.minecraft.client.gui.GuiGraphics;

public class ScreenComponentMultiLabel extends AbstractScreenComponent {

	private final Consumer<GuiGraphics> fontConsumer;

	public ScreenComponentMultiLabel(int x, int y, Consumer<GuiGraphics> fontConsumer) {
		super(x, y, 0, 0);
		this.fontConsumer = fontConsumer;
	}

	@Override
	public void renderForeground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
		if(!isVisible()){
			return;
		}
		fontConsumer.accept(graphics);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return false;
	}

}
