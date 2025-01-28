package electrodynamics.client.render.shader;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import electrodynamics.Electrodynamics;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

//@EventBusSubscriber(modid = References.ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ElectrodynamicsShaders extends RenderType {

	/* LOGGER INSTANCE & RANDOM SOURCE */
	public static final Logger LOGGER = LoggerFactory.getLogger("Electrodynamics: Shaders");

	/* SHADER INSTANCES */
	// private static ShaderInstance shaderPlasmaOrb;

	/* UNIFORMS */
	private static Uniform uniformAlphaCutoff;

	/* SHADER RESOURCE LOCS */
	private static final ResourceLocation GREATER_ALPHA_LOC = Electrodynamics.rl("plasmaorb");

	/* SHADER STATE SHARDS */

	// private static final ShaderStateShard RENDERTYPE_PLASMA_ORB = new ShaderStateShard(() -> shaderPlasmaOrb);

	public static final RenderType GREATER_ALPHA = RenderType.create(GREATER_ALPHA_LOC.toString(), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder().setLightmapState(LIGHTMAP).setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(NO_TEXTURE).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).createCompositeState(false));

	//@SubscribeEvent
	public static void onRegisterShaders(final RegisterShadersEvent event) {
		try {
			event.registerShader(new ShaderInstance(event.getResourceProvider(), GREATER_ALPHA_LOC, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP), shader -> {
				// shaderPlasmaOrb = shader;
				uniformAlphaCutoff = shader.getUniform("AlphaCutoff");
			});
		} catch (IOException err) {
			LOGGER.warn(err.getMessage());
		}
	}

	public static RenderType getRenderTypeAlphaCutoff(float cutoff) {
		setRenderTypeAlphaCutoff(cutoff);
		return GREATER_ALPHA;
	}

	public static void setRenderTypeAlphaCutoff(float cutoff) {
		uniformAlphaCutoff.set(Math.min(cutoff, 1.0F));
	}

	private ElectrodynamicsShaders(String name, VertexFormat format, Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
		super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
		throw new UnsupportedOperationException("No");
	}

}
