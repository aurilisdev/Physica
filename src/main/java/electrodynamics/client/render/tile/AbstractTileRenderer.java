package electrodynamics.client.render.tile;

import java.util.Random;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/**
 * A basic abstract class for BlockEntityRenderer that allows for the storage of utility methods amongst other things
 * 
 * @author skip999
 *
 * @param <T>
 */
public abstract class AbstractTileRenderer<T extends GenericTile> implements BlockEntityRenderer<T> {

	protected BlockEntityRendererProvider.Context context;
	protected final Random random;

	public AbstractTileRenderer(BlockEntityRendererProvider.Context context) {
		this.context = context;
		random = new Random();
	}

	public long getGameTime() {
		return minecraft().level.getGameTime();
	}

	public Minecraft minecraft() {
		return Minecraft.getInstance();
	}

	public ClientLevel level() {
		return minecraft().level;
	}

	public BakedModel getModel(ModelResourceLocation model) {
		return Minecraft.getInstance().getModelManager().getModel(model);
	}

	public void renderItem(ItemStack stack, ItemDisplayContext context, int light, int overlay, PoseStack poseStack, MultiBufferSource bufferSource, @Nullable Level world, int seed) {
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, context, light, overlay, poseStack, bufferSource, world, seed);
	}

	@Override
	public abstract void render(@NotNull T tile, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay);

	public AABB aabb(double x0, double y0, double z0, double x1, double y1, double z1) {
		return new AABB(x0 / 16.0F, y0 / 16.0F, z0 / 16.0F, x1 / 16.0F, y1 / 16.0F, z1 / 16.0F);
	}

}
