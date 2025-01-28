package electrodynamics.prefab.screen.component.types;

import java.util.function.Consumer;

import electrodynamics.prefab.screen.component.utils.AbstractScreenComponent;
import net.minecraft.client.gui.GuiGraphics;

public class ScreenComponentCustomRender extends AbstractScreenComponent {

    private final Consumer<GuiGraphics> graphicsConsumer;

    public ScreenComponentCustomRender(int x, int y, Consumer<GuiGraphics> graphicsConsumer) {
        super(x, y, 0, 0);
        this.graphicsConsumer = graphicsConsumer;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {
        if(!isVisible()){
            return;
        }
        graphicsConsumer.accept(graphics);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }
}
