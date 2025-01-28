package electrodynamics.common.tile.pipelines.fluid;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import electrodynamics.common.inventory.container.tile.ContainerFluidPipeFilter;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.CapabilityUtils;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class TileFluidPipeFilter extends GenericTile {

	public static final BlockEntityUtils.MachineDirection INPUT_DIR = BlockEntityUtils.MachineDirection.FRONT;
	public static final BlockEntityUtils.MachineDirection OUTPUT_DIR = BlockEntityUtils.MachineDirection.BACK;

	private boolean isLocked = false;

	@SuppressWarnings("rawtypes")
	public final Property[] filteredFluids = {
			//
			property(new Property<>(PropertyTypes.FLUID_STACK, "fluidone", FluidStack.EMPTY)),
			//
			property(new Property<>(PropertyTypes.FLUID_STACK, "fluidtwo", FluidStack.EMPTY)),
			//
			property(new Property<>(PropertyTypes.FLUID_STACK, "fluidthree", FluidStack.EMPTY)),
			//
			property(new Property<>(PropertyTypes.FLUID_STACK, "fluidfour", FluidStack.EMPTY)) };

	public final Property<Boolean> isWhitelist = property(new Property<>(PropertyTypes.BOOLEAN, "iswhitelist", false));

	public TileFluidPipeFilter(BlockPos worldPos, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_FLUIDPIPEFILTER.get(), worldPos, blockState);
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentContainerProvider("container.fluidpipefilter", this).createMenu((id, inv) -> new ContainerFluidPipeFilter(id, inv, getCoordsArray())));
	}
	
	@Override
	public @Nullable IFluidHandler getFluidHandlerCapability(@Nullable Direction side) {
	    if(side == null || isLocked) {
	        return null;
	    }
	    Direction facing = getFacing();

        if (side == BlockEntityUtils.getRelativeSide(facing, OUTPUT_DIR.mappedDir)) {
            return CapabilityUtils.EMPTY_FLUID;
        }

        if (side == BlockEntityUtils.getRelativeSide(facing, INPUT_DIR.mappedDir)) {

            BlockEntity output = level.getBlockEntity(getBlockPos().relative(side.getOpposite()));

            if (output == null) {
                return CapabilityUtils.EMPTY_FLUID;
            }
            
            isLocked = true;
            
            IFluidHandler fluid = output.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, output.getBlockPos(), output.getBlockState(), output, side);

            isLocked = false;
            
            return fluid == null ? CapabilityUtils.EMPTY_FLUID : new FilteredFluidCap(fluid, getFilteredFluids(), isWhitelist.get());

        }
        
        return null;
	}

	private List<Fluid> getFilteredFluids() {
		List<Fluid> fluids = new ArrayList<>();
		for (Property<FluidStack> prop : filteredFluids) {
			if (!prop.get().isEmpty()) {
				fluids.add(prop.get().getFluid());
			}
		}
		return fluids;
	}

	private class FilteredFluidCap implements IFluidHandler {

		private final IFluidHandler outputCap;
		private final List<Fluid> validFluids;
		private final boolean whitelist;

		private FilteredFluidCap(IFluidHandler outputCap, List<Fluid> validFluids, boolean whitelist) {
			this.outputCap = outputCap;
			this.validFluids = validFluids;
			this.whitelist = whitelist;
		}

		@Override
		public int getTanks() {
			if (isLocked) {
				return 0;
			}
			isLocked = true;
			int count = outputCap.getTanks();
			isLocked = false;
			return count;
		}

		@Override
		public @NotNull FluidStack getFluidInTank(int tank) {
			if (isLocked) {
				return FluidStack.EMPTY;
			}
			isLocked = true;
			FluidStack stack = outputCap.getFluidInTank(tank);
			isLocked = false;
			return stack;
		}

		@Override
		public int getTankCapacity(int tank) {
			if (isLocked) {
				return 0;
			}
			isLocked = true;
			int cap = outputCap.getTankCapacity(tank);
			isLocked = false;
			return cap;
		}

		@Override
		public boolean isFluidValid(int tank, @NotNull FluidStack stack) {

			if (isLocked) {
				return false;
			}

			if (whitelist) {

				if (validFluids.isEmpty()) {
					return false;
				}

				if (validFluids.contains(stack.getFluid())) {
					isLocked = true;
					boolean valid = outputCap.isFluidValid(tank, stack);
					isLocked = false;
					return valid;
				}

				return false;

			}

			if (validFluids.isEmpty() || !validFluids.contains(stack.getFluid())) {
				isLocked = true;
				boolean valid = outputCap.isFluidValid(tank, stack);
				isLocked = false;
				return valid;
			}

			return false;
		}

		@Override
		public int fill(FluidStack stack, FluidAction action) {

			if (isLocked) {
				return 0;
			}

			if (isFluidValid(getTanks(), stack)) {
				isLocked = true;
				int fill = outputCap.fill(stack, action);
				isLocked = false;
				return fill;
			}

			return 0;
		}

		@Override
		public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
			isLocked = true;
			FluidStack drain = outputCap.drain(resource, action);
			isLocked = false;
			return drain;
		}

		@Override
		public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
			isLocked = true;
			FluidStack drain = outputCap.drain(maxDrain, action);
			isLocked = false;
			return drain;
		}

	}

}
