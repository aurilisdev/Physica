package electrodynamics.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import electrodynamics.common.tile.machines.TileElectrolosisChamber;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class RenderElectrolosisChamber extends AbstractTileRenderer<TileElectrolosisChamber> {
    public RenderElectrolosisChamber(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull TileElectrolosisChamber tile, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

    }
}
