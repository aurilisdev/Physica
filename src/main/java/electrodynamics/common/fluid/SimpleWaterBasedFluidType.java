package electrodynamics.common.fluid;

import org.jetbrains.annotations.Nullable;

import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;

public class SimpleWaterBasedFluidType extends FluidType {

    public static final Color DEFAULT_COLOR_TINT = new Color(63, 118, 228, 255);

    public final ResourceLocation texture;
    public final Color color;

    public SimpleWaterBasedFluidType(String modId, String id, String texture, Color color) {
        super(FluidType.Properties.create().descriptionId("fluid." + modId + "." + id).fallDistanceModifier(0F).canExtinguish(true).canConvertToSource(true).supportsBoating(true).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH).canHydrate(true));
        this.texture = ResourceLocation.fromNamespaceAndPath(modId, "block/fluid/" + texture);
        this.color = color;
    }

    public SimpleWaterBasedFluidType(String modId, String fluidName, String texture) {
        this(modId, fluidName, texture, DEFAULT_COLOR_TINT);
    }

    @Override
    public @Nullable PathType getBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, boolean canFluidLog) {
        return canFluidLog ? super.getBlockPathType(state, level, pos, mob, true) : null;
    }

}
