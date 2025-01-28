package electrodynamics.client.render.tile;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.common.tile.machines.TileMineralWasher;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public class RenderMineralWasher extends AbstractTileRenderer<TileMineralWasher> {

    public RenderMineralWasher(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TileMineralWasher tile, float partialTicks, @NotNull PoseStack matrix, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

        ItemStack stack = tile.<ComponentInventory>getComponent(IComponentType.Inventory).getInputsForProcessor(0).get(0);

        if (stack.isEmpty()) {

            return;

        }

        Direction dir = tile.getFacing();

        matrix.pushPose();

        double scale = 12;

        matrix.translate(0.5 + dir.getStepX() / scale, stack.getItem() instanceof BlockItem ? 0.48 : 0.39, 0.5 + dir.getStepZ() / scale);

        matrix.scale(0.35f, 0.35f, 0.35f);

        matrix.scale(0.3f, 0.3f, 0.3f);

        matrix.translate(0, -0.2, 0);

        renderItem(stack, ItemDisplayContext.NONE, combinedLightIn, combinedOverlayIn, matrix, bufferIn, tile.getLevel(), 0);

        matrix.popPose();

        ComponentFluidHandlerMulti multi = tile.getComponent(IComponentType.FluidHandler);

        FluidStack fluid = multi.getFluidInTank(0, true);

        if (!tile.<ComponentProcessor>getComponent(IComponentType.Processor).isActive() || fluid.isEmpty() || level().getRandom().nextDouble() > 0.15) {
            return;
        }

        matrix.pushPose();

        IClientFluidTypeExtensions attributes = IClientFluidTypeExtensions.of(fluid.getFluid());

        TextureAtlasSprite sp = minecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(attributes.getStillTexture());

        Vector3f color = new Color(attributes.getTintColor(fluid)).multiply(Color.fromABGR(sp.getPixelRGBA(1, level().getRandom().nextIntBetweenInclusive(0, 15), level().getRandom().nextIntBetweenInclusive(0, 15)))).getFloatVector();

        for (int i = 0; i < 2; i++) {
            double x = 0.5 + level().random.nextDouble() * 0.4 - 0.2;
            double y = 0.5 + level().random.nextDouble() * 0.3 - 0.15;
            double z = 0.5 + level().random.nextDouble() * 0.4 - 0.2;
            level().addParticle(new DustParticleOptions(color, 1), tile.getBlockPos().getX() + x, tile.getBlockPos().getY() + y, tile.getBlockPos().getZ() + z, level().random.nextDouble() * 0.2 - 0.1, level().random.nextDouble() * 0.2 - 0.1, level().random.nextDouble() * 0.2 - 0.1);
        }

        matrix.popPose();


    }

    @Override
    public AABB getRenderBoundingBox(TileMineralWasher blockEntity) {
        return super.getRenderBoundingBox(blockEntity).inflate(1);
    }
}
