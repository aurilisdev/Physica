package electrodynamics.api.fluid;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

/**
 * Modification of the FluidTank class incorporating the property system. The protected constructor should remain protected
 * 
 * @author skip999
 *
 */
public class PropertyFluidTank extends FluidTank {

	protected Property<FluidStack> fluidStackProperty;
	protected Property<Integer> capacityProperty;
	protected GenericTile holder;

	public PropertyFluidTank(int capacity, GenericTile holder, String key) {
		super(capacity);
		this.holder = holder;
		fluidStackProperty = holder.property(new Property<>(PropertyTypes.FLUID_STACK, "propertyfluidtankstack" + key, FluidStack.EMPTY));
		capacityProperty = holder.property(new Property<>(PropertyTypes.INTEGER, "propertyfluidtankcapacity" + key, capacity));
	}

	public PropertyFluidTank(int capacity, Predicate<FluidStack> validator, GenericTile holder, String key) {
		super(capacity, validator);
		this.holder = holder;
		fluidStackProperty = holder.property(new Property<>(PropertyTypes.FLUID_STACK, "propertyfluidtankstack" + key, FluidStack.EMPTY));
		capacityProperty = holder.property(new Property<>(PropertyTypes.INTEGER, "propertyfluidtankcapacity" + key, capacity));
	}

	protected PropertyFluidTank(PropertyFluidTank other) {
		super(other.capacity, other.validator);
		holder = other.holder;
		fluidStackProperty = other.fluidStackProperty;
		capacityProperty = other.capacityProperty;
	}

	public PropertyFluidTank[] asArray() {
		return new PropertyFluidTank[] { this };
	}

	@Override
	public String toString() {
		return "Fluid: " + getFluid().getFluidType().getDescriptionId() + "\nAmount: " + getFluidAmount() + "\nCapacity: " + getCapacity();
	}

	@Override
	public CompoundTag writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
		CompoundTag tag = new CompoundTag();
		tag.put("fluid", getFluid().save(lookupProvider));
		tag.putInt("capacity", getCapacity());
		nbt.put(fluidStackProperty.getName() + "tank", tag);
		return nbt;
	}

	@Override
	public FluidTank readFromNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
		CompoundTag tag = nbt.getCompound(fluidStackProperty.getName() + "name");
		setFluid(FluidStack.parseOptional(lookupProvider, tag.getCompound("fluid")));
		setCapacity(tag.getInt("capacity"));
		return this;
	}

	@Override
	public PropertyFluidTank setCapacity(int capacity) {
		capacityProperty.set(capacity);
		onContentsChanged();
		return this;
	}

	@Override
	public int getCapacity() {
		return capacityProperty.get();
	}

	@Override
	@NotNull
	public FluidStack getFluid() {
		return fluidStackProperty.get();
	}

	@Override
	public int getFluidAmount() {
		return getFluid().getAmount();
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || !isFluidValid(resource)) {
			return 0;
		}
		if (action.simulate()) {
			if (getFluid().isEmpty()) {
				return Math.min(getCapacity(), resource.getAmount());
			}
			if (!FluidStack.isSameFluidSameComponents(getFluid(), resource)) {
				return 0;
			}
			return Math.min(getCapacity() - getFluidAmount(), resource.getAmount());
		}
		if (isEmpty()) {
			setFluid(new FluidStack(resource.getFluid(), Math.min(getCapacity(), resource.getAmount())));
			onContentsChanged();
			return getFluidAmount();
		}
		if (!FluidStack.isSameFluidSameComponents(getFluid(), resource)) {
			return 0;
		}
		int filled = getCapacity() - getFluidAmount();

		if (resource.getAmount() < filled) {
			FluidStack stack = new FluidStack(getFluid().getFluid(), resource.getAmount() + getFluidAmount());
			setFluid(stack);
			filled = resource.getAmount();
		} else {
			setFluid(new FluidStack(getFluid().getFluid(), getCapacity()));
		}
		if (filled > 0) {
			onContentsChanged();
		}
		return filled;
	}

	@NotNull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || !FluidStack.isSameFluidSameComponents(getFluid(), resource)) {
			return FluidStack.EMPTY;
		}
		return drain(resource.getAmount(), action);
	}

	@NotNull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		int drained = maxDrain;
		if (getFluidAmount() < drained) {
			drained = getFluidAmount();
		}
		FluidStack stack = new FluidStack(getFluid().getFluid(), drained);
		if (action.execute() && drained > 0) {
			setFluid(new FluidStack(getFluid().getFluid(), getFluidAmount() - drained));
			onContentsChanged();
		}
		return stack;
	}

	@Override
	public void setFluid(FluidStack stack) {
		onContentsChanged();
		fluidStackProperty.set(stack);
	}

	@Override
	public boolean isEmpty() {
		return getFluid().isEmpty();
	}

	@Override
	public int getSpace() {
		return Math.max(0, getCapacity() - getFluidAmount());
	}

	@Override
	protected void onContentsChanged() {
		if (holder != null) {
			holder.onFluidTankChange(this);
		}
	}

}
