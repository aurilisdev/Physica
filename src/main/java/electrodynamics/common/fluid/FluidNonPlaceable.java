package electrodynamics.common.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidType;

public class FluidNonPlaceable extends Fluid {

	private final Holder<Item> bucket;
	private final FluidType type;

	public FluidNonPlaceable(Holder<Item> bucket, FluidType type) {
		this.bucket = bucket;
		this.type = type;
	}

	@Override
	public Item getBucket() {
		return bucket.value();
	}

	@Override
	protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockReader, BlockPos pos, Fluid fluid, Direction direction) {
		return false;
	}

	@Override
	protected Vec3 getFlow(BlockGetter blockReader, BlockPos pos, FluidState fluidState) {
		return Vec3.ZERO;
	}

	@Override
	public int getTickDelay(LevelReader levelReader) {
		return 0;
	}

	@Override
	protected float getExplosionResistance() {
		return 0;
	}

	@Override
	public float getHeight(FluidState fluidState, BlockGetter blockGetter, BlockPos pos) {
		return 0;
	}

	@Override
	public float getOwnHeight(FluidState state) {
		return 0;
	}

	@Override
	protected BlockState createLegacyBlock(FluidState state) {
		return Blocks.AIR.defaultBlockState();
	}

	@Override
	public boolean isSource(FluidState state) {
		return false;
	}

	@Override
	public int getAmount(FluidState state) {
		return 0;
	}

	@Override
	public VoxelShape getShape(FluidState state, BlockGetter getter, BlockPos pos) {
		return Shapes.block();
	}

	@Override
	public FluidType getFluidType() {
		return type;
	}
}
