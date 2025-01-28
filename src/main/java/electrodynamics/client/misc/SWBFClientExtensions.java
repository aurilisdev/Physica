package electrodynamics.client.misc;

import electrodynamics.common.fluid.SimpleWaterBasedFluidType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public class SWBFClientExtensions implements IClientFluidTypeExtensions {

    private final SimpleWaterBasedFluidType fluidType;

    public SWBFClientExtensions(SimpleWaterBasedFluidType fluidType) {
        this.fluidType = fluidType;
    }

    @Override
    public ResourceLocation getStillTexture() {
        return fluidType.texture;
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        return fluidType.texture;
    }

    @Override
    public int getTintColor() {
        return fluidType.color.color();
    }

    @Override
    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return getTintColor();
    }

}
