package electrodynamics.common.fluid;

import java.util.function.Consumer;

import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;

public class SimpleWaterBasedFluidType extends FluidType {
	private final String modId;
	private final String id;
	private final ResourceLocation texture;
	private final int color;

	public SimpleWaterBasedFluidType(String modId, String id, String texture, int color) {
		super(FluidType.Properties.create().descriptionId("fluid." + modId + "." + id).fallDistanceModifier(0F).canExtinguish(true).canConvertToSource(true).supportsBoating(true).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH).canHydrate(true));
		this.modId = modId;
		this.id = id;
		this.texture = ResourceLocation.fromNamespaceAndPath(this.modId, "block/fluid/" + texture);
		this.color = color;
	}

	public SimpleWaterBasedFluidType(String modId, String fluidName, String texture) {
		this(modId, fluidName, texture, 0xFF3F76E4);
	}

	@Override
	public @Nullable PathType getBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, boolean canFluidLog) {
		return canFluidLog ? super.getBlockPathType(state, level, pos, mob, true) : null;
	}

	@Override
	public void initializeClient(@NotNull Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new IClientFluidTypeExtensions() {

			@Override
			public ResourceLocation getStillTexture() {
				return texture;
			}

			@Override
			public ResourceLocation getFlowingTexture() {
				return texture;
			}

			@Override
			public int getTintColor() {
				return color;
			}

			@Override
			public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
				return getTintColor();
			}
		});
	}
}
