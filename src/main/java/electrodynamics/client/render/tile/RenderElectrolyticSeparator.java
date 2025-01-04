package electrodynamics.client.render.tile;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import electrodynamics.common.tile.machines.TileElectrolyticSeparator;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class RenderElectrolyticSeparator extends AbstractTileRenderer<TileElectrolyticSeparator> {

	private static final float MIN_X = 4.0F / 16.0F;
	private static final float MAX_X = 12.0F / 16.0F;
	private static final float MIN_Y = 6.00F / 16.0F;
	private static final float MAX_Y = 11.0F / 16.0F;
	private static final float MIN_Z = 4.0F / 16.0F;
	private static final float MAX_Z = 12.0F / 16.0F;

	public RenderElectrolyticSeparator(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TileElectrolyticSeparator entity, float ticks, @NotNull PoseStack stack, @NotNull MultiBufferSource source, int light, int overlay) {
		FluidTank tank = entity.<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getInputTanks()[0];
		if (!tank.isEmpty() && tank.getFluidAmount() > 0) {
			FluidStack fluid = tank.getFluid();
			float yHeight = Math.max(Math.min((float) tank.getFluidAmount() / (float) tank.getCapacity(), MAX_Y), MIN_Y);
			AABB aabb = new AABB(MIN_X, MIN_Y, MIN_Z, MAX_X, yHeight, MAX_Z);
			VertexConsumer builder = source.getBuffer(Sheets.translucentCullBlockSheet());
			RenderingUtils.renderFluidBox(stack, Minecraft.getInstance(), builder, aabb, fluid, light, overlay, RenderingUtils.ALL_FACES);
		}
	}

}
