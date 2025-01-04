package electrodynamics.client.render.event.guipost;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public abstract class AbstractPostGuiOverlayHandler {

	public abstract void renderToScreen(GuiGraphics graphics, DeltaTracker tracker, Minecraft minecraft);

}
