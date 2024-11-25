package electrodynamics.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import electrodynamics.Electrodynamics;
import electrodynamics.common.tile.machines.TileElectrolosisChamber;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class RenderElectrolosisChamber extends AbstractTileRenderer<TileElectrolosisChamber> {

    private final static AABB RENDER_NORTH = new AABB(0, -1, 3, -5, 2, -2).inflate(2);
    private final static AABB RENDER_EAST = new AABB(1, -1, 3, -4, 2, -2).inflate(2);
    private final static AABB RENDER_SOUTH = new AABB(1, -1, 2, -4, 2, -3).inflate(2);
    private final static AABB RENDER_WEST = new AABB(0, -1, 2, -5, 2, -3).inflate(2);

    private static final BlockPos[] POSITION_NORTH = {
            new BlockPos(1, 0, 3),
            new BlockPos(1, 0, 2),
            new BlockPos(1, 0, 1),
            new BlockPos(0, 0, 3),
            new BlockPos(0, 0, 2),
            new BlockPos(0, 0, 1),
            new BlockPos(-1, 0, 3),
            new BlockPos(-1, 0, 2),
            new BlockPos(-1, 0, 1)
    };

    private static final BlockPos[] POSITION_EAST = {
            new BlockPos(-1, 0, 1),
            new BlockPos(-1, 0, 0),
            new BlockPos(-1, 0, -1),
            new BlockPos(-2, 0, 1),
            new BlockPos(-2, 0, 0),
            new BlockPos(-2, 0, -1),
            new BlockPos(-3, 0, 1),
            new BlockPos(-3, 0, 0),
            new BlockPos(-3, 0, -1),
    };

    private static final BlockPos[] POSITION_SOUTH = {
            new BlockPos(1, 0, -1),
            new BlockPos(1, 0, -2),
            new BlockPos(1, 0, -3),
            new BlockPos(0, 0, -1),
            new BlockPos(0, 0, -2),
            new BlockPos(0, 0, -3),
            new BlockPos(-1, 0, -1),
            new BlockPos(-1, 0, -2),
            new BlockPos(-1, 0, -3),
    };

    private static final BlockPos[] POSITION_WEST = {
            new BlockPos(3, 0, 1),
            new BlockPos(3, 0, 0),
            new BlockPos(3, 0, -1),
            new BlockPos(2, 0, 1),
            new BlockPos(2, 0, 0),
            new BlockPos(2, 0, -1),
            new BlockPos(1, 0, 1),
            new BlockPos(1, 0, 0),
            new BlockPos(1, 0, -1),
    };

    private static final Pair[] FLUID_NORTH = {
            //
            Pair.of(new AABB(POSITION_NORTH[0]), new boolean[]{false, true, false, true, false, true}),
            //
            Pair.of(new AABB(POSITION_NORTH[1]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_NORTH[2]), new boolean[]{false, true, true, false, false, true}),
            //
            Pair.of(new AABB(POSITION_NORTH[3]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_NORTH[4]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_NORTH[5]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_NORTH[6]), new boolean[]{false, true, false, true, true, false}),
            //
            Pair.of(new AABB(POSITION_NORTH[7]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_NORTH[8]), new boolean[]{false, true, true, false, true, false}),}; //new AABB(-1, 0, 2, -4, 1, -1)
    private static final Pair[] FLUID_EAST = {
            //
            Pair.of(new AABB(POSITION_EAST[0]), new boolean[]{false, true, false, true, false, true}),
            //
            Pair.of(new AABB(POSITION_EAST[1]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_EAST[2]), new boolean[]{false, true, true, false, false, true}),
            //
            Pair.of(new AABB(POSITION_EAST[3]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_EAST[4]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_EAST[5]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_EAST[6]), new boolean[]{false, true, false, true, true, false}),
            //
            Pair.of(new AABB(POSITION_EAST[7]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_EAST[8]), new boolean[]{false, true, true, false, true, false})};// new AABB(0, 0, 2, -3, 1, -1)
    private static final Pair[] FLUID_SOUTH = {
            //
            Pair.of(new AABB(POSITION_SOUTH[0]), new boolean[]{false, true, false, true, false, true}),
            //
            Pair.of(new AABB(POSITION_SOUTH[1]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_SOUTH[2]), new boolean[]{false, true, true, false, false, true}),
            //
            Pair.of(new AABB(POSITION_SOUTH[3]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_SOUTH[4]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_SOUTH[5]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_SOUTH[6]), new boolean[]{false, true, false, true, true, false}),
            //
            Pair.of(new AABB(POSITION_SOUTH[7]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_SOUTH[8]), new boolean[]{false, true, true, false, true, false}),}; //new AABB(0, 0, 1, -3, 1, -2)
    private static final Pair[] FLUID_WEST = {
            //
            Pair.of(new AABB(POSITION_WEST[0]), new boolean[]{false, true, false, true, false, true}),
            //
            Pair.of(new AABB(POSITION_WEST[1]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_WEST[2]), new boolean[]{false, true, true, false, false, true}),
            //
            Pair.of(new AABB(POSITION_WEST[3]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_WEST[4]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_WEST[5]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_WEST[6]), new boolean[]{false, true, false, true, true, false}),
            //
            Pair.of(new AABB(POSITION_WEST[7]), new boolean[]{false, true, false, false, false, false}),
            //
            Pair.of(new AABB(POSITION_WEST[8]), new boolean[]{false, true, true, false, true, false}),}; //new AABB(-1, 0, 1, -4, 1, -2)


    public RenderElectrolosisChamber(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull TileElectrolosisChamber tile, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        if (!tile.isFormed.get()) {
            return;
        }

        ComponentFluidHandlerMulti fluidHandler = tile.getComponent(IComponentType.FluidHandler);

        FluidStack input = fluidHandler.getInputTanks()[0].getFluid();

        if (input.isEmpty()) {
            //return;
        }

        poseStack.pushPose();

        Direction facing = tile.getFacing();

        for (Pair<AABB, boolean[]> pair : getFluidAABBs(facing)) {
            RenderingUtils.renderFluidBox(poseStack, minecraft(), bufferSource.getBuffer(RenderType.TRANSLUCENT), pair.getFirst(), new FluidStack(Fluids.WATER, 100), packedLight, packedOverlay, pair.getSecond());
        }

        /*
        if (!tile.isActive.get()) {
            poseStack.popPose();
            return;
        }

         */

        BlockPos[] positions = getBlockPositions(facing);
        BlockPos pos;

        for(int i = 0; i < 9; i++) {
            if(i == 4) {
                continue;
            }
            pos = tile.getBlockPos().offset(positions[i]);
            minecraft().particleEngine.createParticle(ParticleTypes.BUBBLE, pos.getX() + Electrodynamics.RANDOM.nextDouble(1.0), pos.getY() + Electrodynamics.RANDOM.nextDouble(1.0), pos.getZ() + Electrodynamics.RANDOM.nextDouble(1.0), 0, 0 ,0);
        }


        poseStack.popPose();

    }

    @Override
    public AABB getRenderBoundingBox(TileElectrolosisChamber tile) {

        switch (tile.getFacing()) {
            case WEST:

                return RENDER_WEST.move(tile.getBlockPos());
            case EAST:
                return RENDER_EAST.move(tile.getBlockPos());
            case NORTH:
                return RENDER_NORTH.move(tile.getBlockPos());
            case SOUTH:
                return RENDER_SOUTH.move(tile.getBlockPos());

        }

        return super.getRenderBoundingBox(tile);
    }

    private Pair[] getFluidAABBs(Direction facing) {
        switch (facing) {
            case WEST:
                return FLUID_WEST;
            case EAST:
                return FLUID_EAST;
            case NORTH:
                return FLUID_NORTH;
            case SOUTH:
                return FLUID_SOUTH;

        }
        return new Pair[]{};
    }

    private BlockPos[] getBlockPositions(Direction facing) {
        switch (facing) {
            case WEST:
                return POSITION_WEST;
            case EAST:
                return POSITION_EAST;
            case NORTH:
                return POSITION_NORTH;
            case SOUTH:
                return POSITION_SOUTH;

        }
        return new BlockPos[]{};
    }

    @Override
    public boolean shouldRenderOffScreen(TileElectrolosisChamber blockEntity) {
        return true;
    }
}
