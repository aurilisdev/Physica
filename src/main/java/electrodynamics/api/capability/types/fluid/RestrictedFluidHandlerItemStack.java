package electrodynamics.api.capability.types.fluid;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import electrodynamics.api.fluid.FluidStackComponent;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

/**
 * Almost carbon copy of Forge's FluidHandlerItemStack capability, except the way you validate fluids actually makes
 * sense and I don't have to bolt on a bunch of random crap to make it work
 * 
 * @author skip999
 */
public class RestrictedFluidHandlerItemStack implements IFluidHandlerItem {

    public static final String FLUID_NBT_KEY = "Fluid";

    private Predicate<FluidStack> isFluidValid = stack -> true;

    @NotNull
    protected ItemStack container;
    protected int capacity;

    public RestrictedFluidHandlerItemStack(ItemStack container, int capacity) {
        this.container = container;
        this.capacity = capacity;
    }

    public RestrictedFluidHandlerItemStack setValidator(Predicate<FluidStack> isFluidValid) {
        this.isFluidValid = isFluidValid;
        return this;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return capacity;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return isFluidValid.test(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (container.getCount() != 1 || resource.isEmpty() || !isFluidValid(1, resource)) {
            return 0;
        }

        FluidStack contained = getFluid();
        if (contained.isEmpty()) {
            int fillAmount = Math.min(capacity, resource.getAmount());

            if (action.execute()) {
                FluidStack filled = resource.copy();
                filled.setAmount(fillAmount);
                setFluid(filled);
            }

            return fillAmount;
        }
        if (FluidStack.isSameFluidSameComponents(resource, contained)) {
            int fillAmount = Math.min(capacity - contained.getAmount(), resource.getAmount());

            if (action.execute() && fillAmount > 0) {
                contained.grow(fillAmount);
                setFluid(contained);
            }

            return fillAmount;
        }

        return 0;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        if (container.getCount() != 1 || resource.isEmpty() || !FluidStack.isSameFluidSameComponents(getFluid(), resource)) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        if (container.getCount() != 1 || maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        FluidStack contained = getFluid();
        if (contained.isEmpty()) {
            return FluidStack.EMPTY;
        }

        final int drainAmount = Math.min(contained.getAmount(), maxDrain);

        FluidStack drained = contained.copyWithAmount(drainAmount);

        if (action.execute()) {
            contained.shrink(drainAmount);
            if (contained.isEmpty()) {
                setContainerToEmpty();
            } else {
                setFluid(contained);
            }
        }

        return drained;
    }

    @Override
    public @NotNull ItemStack getContainer() {
        return container;
    }

    @NotNull
    public FluidStack getFluid() {
        return container.getOrDefault(ElectrodynamicsDataComponentTypes.FLUID_STACK, FluidStackComponent.EMPTY).fluid;
    }

    public void setFluid(FluidStack fluid) {
        container.set(ElectrodynamicsDataComponentTypes.FLUID_STACK, new FluidStackComponent(fluid));
    }

    public void setContainerToEmpty() {
        container.remove(ElectrodynamicsDataComponentTypes.FLUID_STACK);
    }

    public static class Consumable extends RestrictedFluidHandlerItemStack {

        public Consumable(ItemStack container, int capacity) {
            super(container, capacity);
        }

        @Override
        public void setContainerToEmpty() {
            super.setContainerToEmpty();
            container.shrink(1);
        }
    }

    /**
     * Swaps the container item for a different one when it's emptied.
     */
    public static class SwapEmpty extends RestrictedFluidHandlerItemStack {

        public final ItemStack emptyContainer;

        public SwapEmpty(ItemStack container, ItemStack emptyContainer, int capacity) {
            super(container, capacity);
            this.emptyContainer = emptyContainer;
        }

        @Override
        public void setContainerToEmpty() {
            super.setContainerToEmpty();
            container = emptyContainer;
        }
    }

}
