package electrodynamics.client.render.event.levelstage;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage;

/**
 * A note about using this event: It is IMPERATIVE that the event be as efficient as possible. The more work the event must do in a single tick, the more tiny inefficient solutions start to add up. This really doens't play out much in the marker lines renderer for example, but it definitely has an impact in the quarry renderer
 * 
 * @author skip999
 *
 */
public abstract class AbstractLevelStageHandler {

	public abstract boolean shouldRender(Stage stage);

	public abstract void render(Camera camera, Frustum frustum, LevelRenderer renderer, PoseStack stack, Matrix4f projectionMatrix, Minecraft minecraft, int renderTick, DeltaTracker deltaTracker);

	public void clear() {

	}

}
