package electrodynamics.prefab.tile.components.type;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.GasTank;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.network.utils.FluidUtilities;
import electrodynamics.common.network.utils.GasUtilities;
import electrodynamics.common.recipe.ElectrodynamicsRecipe;
import electrodynamics.common.recipe.categories.fluid2fluid.Fluid2FluidRecipe;
import electrodynamics.common.recipe.categories.fluid2gas.Fluid2GasRecipe;
import electrodynamics.common.recipe.categories.fluid2item.Fluid2ItemRecipe;
import electrodynamics.common.recipe.categories.fluiditem2fluid.FluidItem2FluidRecipe;
import electrodynamics.common.recipe.categories.fluiditem2gas.FluidItem2GasRecipe;
import electrodynamics.common.recipe.categories.fluiditem2item.FluidItem2ItemRecipe;
import electrodynamics.common.recipe.categories.item2fluid.Item2FluidRecipe;
import electrodynamics.common.recipe.categories.item2item.Item2ItemRecipe;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponent;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class ComponentProcessor implements IComponent {

    private GenericTile holder;

    public Property<Double> operatingSpeed;
    public Property<Double> operatingTicks;
    public Property<Double> usage;
    public Property<Double> requiredTicks;
    private Predicate<ComponentProcessor> canProcess = component -> false;
    private Consumer<ComponentProcessor> process;
    private Consumer<ComponentProcessor> failed;
    private int processorNumber = 0;
    public int totalProcessors = 1;

    private List<RecipeHolder<ElectrodynamicsRecipe>> cachedRecipes = new ArrayList<>();
    private ElectrodynamicsRecipe recipe;
    private double storedXp = 0.0;

    private Property<Boolean> isActive;
    private Property<Boolean> shouldKeepProgress;

    public ComponentProcessor(GenericTile source) {
        this(source, 0, 1);
    }

    public ComponentProcessor(GenericTile source, int processorNumber, int totalProcessors) {
        holder(source);
        this.processorNumber = processorNumber;
        this.totalProcessors = totalProcessors;
        operatingSpeed = holder.property(new Property<>(PropertyTypes.DOUBLE, "operatingSpeed" + processorNumber, 1.0));
        operatingTicks = holder.property(new Property<>(PropertyTypes.DOUBLE, "operatingTicks" + processorNumber, 0.0));
        usage = holder.property(new Property<>(PropertyTypes.DOUBLE, "recipeUsage" + processorNumber, 0.0));
        requiredTicks = holder.property(new Property<>(PropertyTypes.DOUBLE, "requiredTicks" + processorNumber, 0.0));
        isActive = holder.property(new Property<>(PropertyTypes.BOOLEAN, "isprocactive" + processorNumber, false));
        shouldKeepProgress = holder.property(new Property<>(PropertyTypes.BOOLEAN, "shouldprockeepprogress" + processorNumber, false));
        if (!holder.hasComponent(IComponentType.Inventory)) {
            throw new UnsupportedOperationException("You need to implement an inventory component to use the processor component!");
        }
        if (!holder.hasComponent(IComponentType.Tickable)) {
            throw new UnsupportedOperationException("You need to implement a tickable component to use the processor component!");
        }
        holder.<ComponentTickable>getComponent(IComponentType.Tickable).tickServer(this::tickServer);
    }

    @Override
    public void holder(GenericTile holder) {
        this.holder = holder;
    }

    @Override
    public GenericTile getHolder() {
        return holder;
    }

    private void tickServer(ComponentTickable tickable) {
        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
        for (ItemStack stack : inv.getUpgradeContents()) {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemUpgrade upgrade && !upgrade.subtype.isEmpty) {
                for (int i = 0; i < stack.getCount(); i++) {
                    upgrade.subtype.applyUpgrade.accept(holder, this, stack);
                }
            }
        }
        if (canProcess.test(this)) {
            isActive.set(true);
            operatingTicks.set(operatingTicks.get() + operatingSpeed.get());
            if (operatingTicks.get() >= requiredTicks.get()) {
                if (process != null) {
                    process.accept(this);
                }
                operatingTicks.set(0.0);
            }
            if (holder.hasComponent(IComponentType.Electrodynamic)) {
                ComponentElectrodynamic electro = holder.getComponent(IComponentType.Electrodynamic);
                electro.joules(electro.getJoulesStored() - usage.get() * operatingSpeed.get());
            }
        } else if (isActive()) {
            isActive.set(false);
            if (!shouldKeepProgress.get()) {
                operatingTicks.set(0.0);
            }

            if (failed != null) {
                failed.accept(this);
            }
        } else {
            operatingTicks.set(0.0);
        }

    }

    public ComponentProcessor process(Consumer<ComponentProcessor> process) {
        this.process = process;
        return this;
    }

    public ComponentProcessor failed(Consumer<ComponentProcessor> failed) {
        this.failed = failed;
        return this;
    }

    public ComponentProcessor canProcess(Predicate<ComponentProcessor> canProcess) {
        this.canProcess = canProcess;
        return this;
    }

    public ComponentProcessor usage(double usage) {
        this.usage.set(usage);
        return this;
    }

    public double getUsage() {
        return usage.get() * operatingSpeed.get();
    }

    public ComponentProcessor requiredTicks(long requiredTicks) {
        this.requiredTicks.set((double) requiredTicks);
        return this;
    }

    public int getProcessorNumber() {
        return processorNumber;
    }

    @Override
    public IComponentType getType() {
        return IComponentType.Processor;
    }

    public ComponentProcessor consumeBucket() {
        FluidUtilities.drainItem(holder, holder.<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getInputTanks());
        return this;
    }

    public ComponentProcessor dispenseBucket() {
        FluidUtilities.fillItem(holder, holder.<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getOutputTanks());
        return this;
    }

    public ComponentProcessor outputToFluidPipe() {
        ComponentFluidHandlerMulti handler = holder.getComponent(IComponentType.FluidHandler);
        FluidUtilities.outputToPipe(holder, handler.getOutputTanks(), handler.outputDirections);
        return this;
    }

    public ComponentProcessor consumeGasCylinder() {
        GasUtilities.drainItem(holder, holder.<ComponentGasHandlerMulti>getComponent(IComponentType.GasHandler).getInputTanks());
        return this;
    }

    public ComponentProcessor dispenseGasCylinder() {
        GasUtilities.fillItem(holder, holder.<ComponentGasHandlerMulti>getComponent(IComponentType.GasHandler).getOutputTanks());
        return this;
    }

    public ComponentProcessor outputToGasPipe() {
        ComponentGasHandlerMulti handler = holder.getComponent(IComponentType.GasHandler);
        GasUtilities.outputToPipe(holder, handler.getOutputTanks(), handler.outputDirections);
        return this;
    }

    public ElectrodynamicsRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(ElectrodynamicsRecipe recipe) {
        this.recipe = recipe;
    }

    public void setStoredXp(double val) {
        storedXp = val;
    }

    public double getStoredXp() {
        return storedXp;
    }

    public boolean isActive() {
        return isActive.get();
    }

    public void setShouldKeepProgress(boolean should) {
        shouldKeepProgress.set(should);
    }

    public boolean canProcessItem2ItemRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
        Item2ItemRecipe locRecipe;
        if (!checkExistingRecipe(pr)) {
            setShouldKeepProgress(false);
            pr.operatingTicks.set(0.0);
            locRecipe = (Item2ItemRecipe) getRecipe(pr, typeIn);
            if (locRecipe == null) {
                return false;
            }
        } else {
            setShouldKeepProgress(true);
            locRecipe = (Item2ItemRecipe) recipe;
        }

        setRecipe(locRecipe);

        requiredTicks.set((double) locRecipe.getTicks());
        usage.set(locRecipe.getUsagePerTick());

        ComponentElectrodynamic electro = holder.getComponent(IComponentType.Electrodynamic);
        electro.maxJoules(usage.get() * operatingSpeed.get() * 10 * totalProcessors);

        if (electro.getJoulesStored() < pr.getUsage()) {
            return false;
        }

        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
        ItemStack output = inv.getOutputContents().get(processorNumber);
        ItemStack result = locRecipe.getItemRecipeOutput();
        boolean isEmpty = output.isEmpty();
        if (!isEmpty && !ItemUtils.testItems(output.getItem(), result.getItem())) {
            return false;
        }

        int locCap = isEmpty ? 64 : output.getMaxStackSize();
        if (locCap < output.getCount() + result.getCount()) {
            return false;
        }

        if (locRecipe.hasItemBiproducts()) {
            boolean itemBiRoom = roomInItemBiSlots(inv.getBiprodsForProcessor(pr.getProcessorNumber()), locRecipe.getFullItemBiStacks());
            if (!itemBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasFluidBiproducts()) {
            ComponentFluidHandlerMulti handler = holder.getComponent(IComponentType.FluidHandler);
            boolean fluidBiRoom = roomInBiproductFluidTanks(handler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
            if (!fluidBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasGasBiproducts()) {
            ComponentGasHandlerMulti handler = holder.getComponent(IComponentType.GasHandler);
            boolean gasBiRoom = roomInBiproductGasTanks(handler.getOutputTanks(), locRecipe.getFullGasBiStacks());
            if (!gasBiRoom) {
                return false;
            }
        }
        return true;
    }

    public boolean canProcessFluid2ItemRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
        Fluid2ItemRecipe locRecipe;
        if (!checkExistingRecipe(pr)) {
            setShouldKeepProgress(false);
            pr.operatingTicks.set(0.0);
            locRecipe = (Fluid2ItemRecipe) getRecipe(pr, typeIn);
            if (locRecipe == null) {
                return false;
            }
        } else {
            setShouldKeepProgress(true);
            locRecipe = (Fluid2ItemRecipe) recipe;
        }
        setRecipe(locRecipe);

        requiredTicks.set((double) locRecipe.getTicks());
        usage.set(locRecipe.getUsagePerTick());

        ComponentElectrodynamic electro = holder.getComponent(IComponentType.Electrodynamic);
        electro.maxJoules(usage.get() * operatingSpeed.get() * 10 * totalProcessors);

        if (electro.getJoulesStored() < pr.getUsage()) {
            return false;
        }

        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
        ItemStack output = inv.getOutputContents().get(processorNumber);
        ItemStack result = locRecipe.getItemRecipeOutput();
        boolean isEmpty = output.isEmpty();

        if (!isEmpty && !ItemUtils.testItems(output.getItem(), result.getItem())) {
            return false;
        }

        int locCap = isEmpty ? 64 : output.getMaxStackSize();
        if (locCap < output.getCount() + result.getCount()) {
            return false;
        }
        if (locRecipe.hasItemBiproducts()) {
            boolean itemBiRoom = roomInItemBiSlots(inv.getBiprodsForProcessor(pr.getProcessorNumber()), locRecipe.getFullItemBiStacks());
            if (!itemBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasFluidBiproducts()) {
            ComponentFluidHandlerMulti handler = holder.getComponent(IComponentType.FluidHandler);
            boolean fluidBiRoom = roomInBiproductFluidTanks(handler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
            if (!fluidBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasGasBiproducts()) {
            ComponentGasHandlerMulti handler = holder.getComponent(IComponentType.GasHandler);
            boolean gasBiRoom = roomInBiproductGasTanks(handler.getOutputTanks(), locRecipe.getFullGasBiStacks());
            if (!gasBiRoom) {
                return false;
            }
        }
        return true;
    }

    public boolean canProcessFluid2FluidRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
        Fluid2FluidRecipe locRecipe;
        if (!checkExistingRecipe(pr)) {
            setShouldKeepProgress(false);
            pr.operatingTicks.set(0.0);
            locRecipe = (Fluid2FluidRecipe) getRecipe(pr, typeIn);
            if (locRecipe == null) {
                return false;
            }
        } else {
            setShouldKeepProgress(true);
            locRecipe = (Fluid2FluidRecipe) recipe;
        }
        setRecipe(locRecipe);

        requiredTicks.set((double) locRecipe.getTicks());
        usage.set(locRecipe.getUsagePerTick());

        ComponentElectrodynamic electro = holder.getComponent(IComponentType.Electrodynamic);
        electro.maxJoules(usage.get() * operatingSpeed.get() * 10 * totalProcessors);

        if (electro.getJoulesStored() < pr.getUsage()) {
            return false;
        }

        ComponentFluidHandlerMulti handler = holder.getComponent(IComponentType.FluidHandler);
        int amtAccepted = handler.getOutputTanks()[0].fill(locRecipe.getFluidRecipeOutput(), FluidAction.SIMULATE);
        if (amtAccepted < locRecipe.getFluidRecipeOutput().getAmount()) {
            return false;
        }
        if (locRecipe.hasItemBiproducts()) {
            ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
            boolean itemBiRoom = roomInItemBiSlots(inv.getBiprodsForProcessor(pr.getProcessorNumber()), locRecipe.getFullItemBiStacks());
            if (!itemBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasFluidBiproducts()) {
            boolean fluidBiRoom = roomInBiproductFluidTanks(handler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
            if (!fluidBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasGasBiproducts()) {
            ComponentGasHandlerMulti gasHandler = holder.getComponent(IComponentType.GasHandler);
            boolean gasBiRoom = roomInBiproductGasTanks(gasHandler.getOutputTanks(), locRecipe.getFullGasBiStacks());
            if (!gasBiRoom) {
                return false;
            }
        }
        return true;
    }

    public boolean canProcessItem2FluidRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
        Item2FluidRecipe locRecipe;
        if (!checkExistingRecipe(pr)) {
            setShouldKeepProgress(false);
            pr.operatingTicks.set(0.0);
            locRecipe = (Item2FluidRecipe) getRecipe(pr, typeIn);
            if (locRecipe == null) {
                return false;
            }
        } else {
            setShouldKeepProgress(true);
            locRecipe = (Item2FluidRecipe) recipe;
        }
        setRecipe(locRecipe);

        requiredTicks.set((double) locRecipe.getTicks());
        usage.set(locRecipe.getUsagePerTick());

        ComponentElectrodynamic electro = holder.getComponent(IComponentType.Electrodynamic);
        electro.maxJoules(usage.get() * operatingSpeed.get() * 10 * totalProcessors);

        if (electro.getJoulesStored() < pr.getUsage()) {
            return false;
        }

        ComponentFluidHandlerMulti handler = holder.getComponent(IComponentType.FluidHandler);
        int amtAccepted = handler.getOutputTanks()[0].fill(locRecipe.getFluidRecipeOutput(), FluidAction.SIMULATE);
        if (amtAccepted < locRecipe.getFluidRecipeOutput().getAmount()) {
            return false;
        }
        if (locRecipe.hasItemBiproducts()) {
            ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
            boolean itemBiRoom = roomInItemBiSlots(inv.getBiprodsForProcessor(pr.getProcessorNumber()), locRecipe.getFullItemBiStacks());
            if (!itemBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasFluidBiproducts()) {
            boolean fluidBiRoom = roomInBiproductFluidTanks(handler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
            if (!fluidBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasGasBiproducts()) {
            ComponentGasHandlerMulti gasHandler = holder.getComponent(IComponentType.GasHandler);
            boolean gasBiRoom = roomInBiproductGasTanks(gasHandler.getOutputTanks(), locRecipe.getFullGasBiStacks());
            if (!gasBiRoom) {
                return false;
            }
        }
        return true;
    }

    public boolean canProcessFluidItem2FluidRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
        FluidItem2FluidRecipe locRecipe;
        if (!checkExistingRecipe(pr)) {
            setShouldKeepProgress(false);
            pr.operatingTicks.set(0.0);
            locRecipe = (FluidItem2FluidRecipe) getRecipe(pr, typeIn);
            if (locRecipe == null) {
                return false;
            }
        } else {
            setShouldKeepProgress(true);
            locRecipe = (FluidItem2FluidRecipe) recipe;
        }
        setRecipe(locRecipe);

        requiredTicks.set((double) locRecipe.getTicks());
        usage.set(locRecipe.getUsagePerTick());

        ComponentElectrodynamic electro = holder.getComponent(IComponentType.Electrodynamic);
        electro.maxJoules(usage.get() * operatingSpeed.get() * 10 * totalProcessors);

        if (electro.getJoulesStored() < pr.getUsage()) {
            return false;
        }

        ComponentFluidHandlerMulti handler = holder.getComponent(IComponentType.FluidHandler);
        int amtAccepted = handler.getOutputTanks()[0].fill(locRecipe.getFluidRecipeOutput(), FluidAction.SIMULATE);
        if (amtAccepted < locRecipe.getFluidRecipeOutput().getAmount()) {
            return false;
        }
        if (locRecipe.hasItemBiproducts()) {
            ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
            boolean itemBiRoom = roomInItemBiSlots(inv.getBiprodsForProcessor(pr.getProcessorNumber()), locRecipe.getFullItemBiStacks());
            if (!itemBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasFluidBiproducts()) {
            boolean fluidBiRoom = roomInBiproductFluidTanks(handler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
            if (!fluidBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasGasBiproducts()) {
            ComponentGasHandlerMulti gasHandler = holder.getComponent(IComponentType.GasHandler);
            boolean gasBiRoom = roomInBiproductGasTanks(gasHandler.getOutputTanks(), locRecipe.getFullGasBiStacks());
            if (!gasBiRoom) {
                return false;
            }
        }
        return true;
    }

    public boolean canProcessFluidItem2ItemRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
        FluidItem2ItemRecipe locRecipe;
        if (!checkExistingRecipe(pr)) {
            setShouldKeepProgress(false);
            pr.operatingTicks.set(0.0);
            locRecipe = (FluidItem2ItemRecipe) getRecipe(pr, typeIn);
            if (locRecipe == null) {
                return false;
            }
        } else {
            setShouldKeepProgress(true);
            locRecipe = (FluidItem2ItemRecipe) recipe;
        }
        setRecipe(locRecipe);

        requiredTicks.set((double) locRecipe.getTicks());
        usage.set(locRecipe.getUsagePerTick());

        ComponentElectrodynamic electro = holder.getComponent(IComponentType.Electrodynamic);
        electro.maxJoules(usage.get() * operatingSpeed.get() * 10 * totalProcessors);

        if (electro.getJoulesStored() < pr.getUsage()) {
            return false;
        }

        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
        ItemStack output = inv.getOutputContents().get(processorNumber);
        ItemStack result = locRecipe.getItemRecipeOutput();
        boolean isEmpty = output.isEmpty();

        if (!isEmpty && !ItemUtils.testItems(output.getItem(), result.getItem())) {
            return false;
        }

        int locCap = isEmpty ? 64 : output.getMaxStackSize();
        if (locCap < output.getCount() + result.getCount()) {
            return false;
        }
        if (locRecipe.hasItemBiproducts()) {
            boolean itemBiRoom = roomInItemBiSlots(inv.getBiprodsForProcessor(pr.getProcessorNumber()), locRecipe.getFullItemBiStacks());
            if (!itemBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasFluidBiproducts()) {
            ComponentFluidHandlerMulti handler = holder.getComponent(IComponentType.FluidHandler);
            boolean fluidBiRoom = roomInBiproductFluidTanks(handler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
            if (!fluidBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasGasBiproducts()) {
            ComponentGasHandlerMulti gasHandler = holder.getComponent(IComponentType.GasHandler);
            boolean gasBiRoom = roomInBiproductGasTanks(gasHandler.getOutputTanks(), locRecipe.getFullGasBiStacks());
            if (!gasBiRoom) {
                return false;
            }
        }
        return true;
    }

    public boolean canProcessFluid2GasRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
        Fluid2GasRecipe locRecipe;
        if (!checkExistingRecipe(pr)) {
            setShouldKeepProgress(false);
            pr.operatingTicks.set(0.0);
            locRecipe = (Fluid2GasRecipe) getRecipe(pr, typeIn);
            if (locRecipe == null) {
                return false;
            }
        } else {
            setShouldKeepProgress(true);
            locRecipe = (Fluid2GasRecipe) recipe;
        }
        setRecipe(locRecipe);

        requiredTicks.set((double) locRecipe.getTicks());
        usage.set(locRecipe.getUsagePerTick());

        ComponentElectrodynamic electro = holder.getComponent(IComponentType.Electrodynamic);
        electro.maxJoules(usage.get() * operatingSpeed.get() * 10 * totalProcessors);

        if (electro.getJoulesStored() < pr.getUsage()) {
            return false;
        }

        ComponentGasHandlerMulti gasHandler = holder.getComponent(IComponentType.GasHandler);
        ComponentFluidHandlerMulti fluidHandler = holder.getComponent(IComponentType.FluidHandler);
        double amtAccepted = gasHandler.getOutputTanks()[0].fill(locRecipe.getGasRecipeOutput(), GasAction.SIMULATE);
        if (amtAccepted < locRecipe.getGasRecipeOutput().getAmount()) {
            return false;
        }
        if (locRecipe.hasItemBiproducts()) {
            ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
            boolean itemBiRoom = roomInItemBiSlots(inv.getBiprodsForProcessor(pr.getProcessorNumber()), locRecipe.getFullItemBiStacks());
            if (!itemBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasFluidBiproducts()) {
            boolean fluidBiRoom = roomInBiproductFluidTanks(fluidHandler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
            if (!fluidBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasGasBiproducts()) {
            boolean gasBiRoom = roomInBiproductGasTanks(gasHandler.getOutputTanks(), locRecipe.getFullGasBiStacks());
            if (!gasBiRoom) {
                return false;
            }
        }

        return true;
    }

    public boolean canProcessFluidItem2GasRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
        FluidItem2GasRecipe locRecipe;
        if (!checkExistingRecipe(pr)) {
            setShouldKeepProgress(false);
            pr.operatingTicks.set(0.0);
            locRecipe = (FluidItem2GasRecipe) getRecipe(pr, typeIn);
            if (locRecipe == null) {
                return false;
            }
        } else {
            setShouldKeepProgress(true);
            locRecipe = (FluidItem2GasRecipe) recipe;
        }
        setRecipe(locRecipe);

        requiredTicks.set((double) locRecipe.getTicks());
        usage.set(locRecipe.getUsagePerTick());

        ComponentElectrodynamic electro = holder.getComponent(IComponentType.Electrodynamic);
        electro.maxJoules(usage.get() * operatingSpeed.get() * 10 * totalProcessors);

        if (electro.getJoulesStored() < pr.getUsage()) {
            return false;
        }

        ComponentGasHandlerMulti gasHandler = holder.getComponent(IComponentType.GasHandler);
        double amtAccepted = gasHandler.getOutputTanks()[0].fill(locRecipe.getGasRecipeOutput(), GasAction.SIMULATE);
        if (amtAccepted < locRecipe.getGasRecipeOutput().getAmount()) {
            return false;
        }
        if (locRecipe.hasItemBiproducts()) {
            ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
            boolean itemBiRoom = roomInItemBiSlots(inv.getBiprodsForProcessor(pr.getProcessorNumber()), locRecipe.getFullItemBiStacks());
            if (!itemBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasFluidBiproducts()) {
            ComponentFluidHandlerMulti fluidHandler = holder.getComponent(IComponentType.FluidHandler);
            boolean fluidBiRoom = roomInBiproductFluidTanks(fluidHandler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
            if (!fluidBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasGasBiproducts()) {
            boolean gasBiRoom = roomInBiproductGasTanks(gasHandler.getOutputTanks(), locRecipe.getFullGasBiStacks());
            if (!gasBiRoom) {
                return false;
            }
        }
        return true;
    }

    /*
     * CONVENTIONS TO NOTE:
     *
     * Biproducts will be output in the order they appear in the recipe JSON
     *
     * The output FluidTanks will contain both the recipe output tank and the biproduct tanks The first tank is ALWAYS the main output tank, and the following tanks will be filled in the order of the fluid biproducts
     *
     *
     *
     *
     * Also, no checks outside of the null recipe check will be performed in these methods All validity checks will take place in the recipe validator methods
     *
     */

    public void processItem2ItemRecipe(ComponentProcessor pr) {
        if (getRecipe() == null) {
            return;
        }
        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
        Item2ItemRecipe locRecipe = (Item2ItemRecipe) getRecipe();
        int procNumber = pr.getProcessorNumber();
        List<Integer> slotOrientation = locRecipe.getItemArrangment(procNumber);

        if (locRecipe.hasItemBiproducts()) {

            List<ProbableItem> itemBi = locRecipe.getItemBiproducts();
            int index = 0;

            for (int slot : inv.getBiprodSlotsForProcessor(procNumber)) {

                ItemStack stack = inv.getItem(slot);
                if (stack.isEmpty()) {
                    inv.setItem(slot, itemBi.get(index).roll().copy());
                } else {
                    stack.grow(itemBi.get(index).roll().getCount());
                    inv.setItem(slot, stack);
                }
            }

        }

        if (locRecipe.hasFluidBiproducts()) {
            ComponentFluidHandlerMulti handler = holder.getComponent(IComponentType.FluidHandler);
            List<ProbableFluid> fluidBi = locRecipe.getFluidBiproducts();
            FluidTank[] outTanks = handler.getOutputTanks();
            for (int i = 0; i < fluidBi.size(); i++) {

                outTanks[i].fill(fluidBi.get(i).roll(), FluidAction.EXECUTE);
            }
        }

        if (locRecipe.hasGasBiproducts()) {
            ComponentGasHandlerMulti handler = holder.getComponent(IComponentType.GasHandler);
            List<ProbableGas> gasBi = locRecipe.getGasBiproducts();
            GasTank[] outTanks = handler.getOutputTanks();
            for (int i = 0; i < gasBi.size(); i++) {
                outTanks[i].fill(gasBi.get(i).roll(), GasAction.EXECUTE);
            }
        }

        int outputSlot = inv.getOutputSlots().get(procNumber);

        if (inv.getOutputContents().get(procNumber).isEmpty()) {
            inv.setItem(outputSlot, locRecipe.getItemRecipeOutput().copy());
        } else {
            ItemStack stack = inv.getOutputContents().get(procNumber);
            stack.grow(locRecipe.getItemRecipeOutput().getCount());
            inv.setItem(outputSlot, stack);

        }
        List<Integer> inputs = inv.getInputSlotsForProcessor(procNumber);
        for (int i = 0; i < inputs.size(); i++) {
            int index = inputs.get(slotOrientation.get(i));
            ItemStack stack = inv.getItem(index);
            stack.shrink(locRecipe.getCountedIngredients().get(i).getStackSize());
            inv.setItem(index, stack);
        }
        dispenseExperience(inv, locRecipe.getXp());
        setChanged();
    }

    public void processFluidItem2FluidRecipe(ComponentProcessor pr) {
        if (getRecipe() == null) {
            return;
        }
        FluidItem2FluidRecipe locRecipe = (FluidItem2FluidRecipe) getRecipe();
        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
        ComponentFluidHandlerMulti handler = holder.getComponent(IComponentType.FluidHandler);
        List<Integer> slotOrientation = locRecipe.getItemArrangment(pr.getProcessorNumber());
        int procNumber = pr.getProcessorNumber();
        if (locRecipe.hasItemBiproducts()) {

            List<ProbableItem> itemBi = locRecipe.getItemBiproducts();
            int index = 0;

            for (int slot : inv.getBiprodSlotsForProcessor(procNumber)) {

                ItemStack stack = inv.getItem(slot);
                if (stack.isEmpty()) {
                    inv.setItem(slot, itemBi.get(index).roll().copy());
                } else {
                    stack.grow(itemBi.get(index).roll().getCount());
                    inv.setItem(slot, stack);
                }
            }

        }

        if (locRecipe.hasFluidBiproducts()) {
            List<ProbableFluid> fluidBi = locRecipe.getFluidBiproducts();
            FluidTank[] outTanks = handler.getOutputTanks();
            for (int i = 0; i < fluidBi.size(); i++) {
                outTanks[i + 1].fill(fluidBi.get(i).roll(), FluidAction.EXECUTE);
            }
        }

        if (locRecipe.hasGasBiproducts()) {
            ComponentGasHandlerMulti gasHandler = holder.getComponent(IComponentType.GasHandler);
            List<ProbableGas> gasBi = locRecipe.getGasBiproducts();
            GasTank[] outTanks = gasHandler.getOutputTanks();
            for (int i = 0; i < gasBi.size(); i++) {
                outTanks[i].fill(gasBi.get(i).roll(), GasAction.EXECUTE);
            }
        }

        handler.getOutputTanks()[0].fill(locRecipe.getFluidRecipeOutput(), FluidAction.EXECUTE);

        List<Integer> inputs = inv.getInputSlotsForProcessor(procNumber);
        for (int i = 0; i < inputs.size(); i++) {
            int index = inputs.get(slotOrientation.get(i));
            ItemStack stack = inv.getItem(index);
            stack.shrink(locRecipe.getCountedIngredients().get(i).getStackSize());
            inv.setItem(index, stack);
        }

        FluidTank[] tanks = handler.getInputTanks();
        List<FluidIngredient> fluidIngs = locRecipe.getFluidIngredients();
        List<Integer> tankOrientation = locRecipe.getFluidArrangement();
        for (int i = 0; i < handler.tankCount(true); i++) {
            tanks[tankOrientation.get(i)].drain(fluidIngs.get(i).getFluidStack().getAmount(), FluidAction.EXECUTE);
        }
        dispenseExperience(inv, locRecipe.getXp());
        setChanged();
    }

    public void processFluidItem2ItemRecipe(ComponentProcessor pr) {
        if (getRecipe() == null) {
            return;
        }
        FluidItem2ItemRecipe locRecipe = (FluidItem2ItemRecipe) getRecipe();
        int procNumber = pr.getProcessorNumber();
        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
        ComponentFluidHandlerMulti handler = holder.getComponent(IComponentType.FluidHandler);
        List<Integer> slotOrientation = locRecipe.getItemArrangment(procNumber);

        if (locRecipe.hasItemBiproducts()) {

            List<ProbableItem> itemBi = locRecipe.getItemBiproducts();
            int index = 0;

            for (int slot : inv.getBiprodSlotsForProcessor(procNumber)) {

                ItemStack stack = inv.getItem(slot);
                if (stack.isEmpty()) {
                    inv.setItem(slot, itemBi.get(index).roll().copy());
                } else {
                    stack.grow(itemBi.get(index).roll().getCount());
                    inv.setItem(slot, stack);
                }
            }

        }

        if (locRecipe.hasFluidBiproducts()) {
            List<ProbableFluid> fluidBi = locRecipe.getFluidBiproducts();
            FluidTank[] outTanks = handler.getOutputTanks();
            for (int i = 0; i < fluidBi.size(); i++) {
                outTanks[i].fill(fluidBi.get(i).roll(), FluidAction.EXECUTE);
            }
        }

        if (locRecipe.hasGasBiproducts()) {
            ComponentGasHandlerMulti gasHandler = holder.getComponent(IComponentType.GasHandler);
            List<ProbableGas> gasBi = locRecipe.getGasBiproducts();
            GasTank[] outTanks = gasHandler.getOutputTanks();
            for (int i = 0; i < gasBi.size(); i++) {
                outTanks[i].fill(gasBi.get(i).roll(), GasAction.EXECUTE);
            }
        }

        if (inv.getOutputContents().get(procNumber).isEmpty()) {
            inv.setItem(inv.getOutputSlots().get(procNumber), locRecipe.getItemRecipeOutput().copy());
        } else {
            inv.getOutputContents().get(procNumber).grow(locRecipe.getItemRecipeOutput().getCount());
        }

        List<Integer> inputs = inv.getInputSlotsForProcessor(procNumber);
        for (int i = 0; i < inputs.size(); i++) {
            int index = inputs.get(slotOrientation.get(i));
            ItemStack stack = inv.getItem(index);
            stack.shrink(locRecipe.getCountedIngredients().get(i).getStackSize());
            inv.setItem(index, stack);
        }

        FluidTank[] tanks = handler.getInputTanks();
        List<FluidIngredient> fluidIngs = locRecipe.getFluidIngredients();
        List<Integer> tankOrientation = locRecipe.getFluidArrangement();
        for (int i = 0; i < handler.tankCount(true); i++) {
            tanks[tankOrientation.get(i)].drain(fluidIngs.get(i).getFluidStack().getAmount(), FluidAction.EXECUTE);
        }
        dispenseExperience(inv, locRecipe.getXp());
        setChanged();
    }

    public void processFluid2ItemRecipe(ComponentProcessor pr) {
        if (getRecipe() == null) {
            return;
        }
        Fluid2ItemRecipe locRecipe = (Fluid2ItemRecipe) getRecipe();
        int procNumber = pr.getProcessorNumber();
        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
        ComponentFluidHandlerMulti handler = holder.getComponent(IComponentType.FluidHandler);

        if (locRecipe.hasItemBiproducts()) {

            List<ProbableItem> itemBi = locRecipe.getItemBiproducts();
            int index = 0;

            for (int slot : inv.getBiprodSlotsForProcessor(procNumber)) {

                ItemStack stack = inv.getItem(slot);
                if (stack.isEmpty()) {
                    inv.setItem(slot, itemBi.get(index).roll().copy());
                } else {
                    stack.grow(itemBi.get(index).roll().getCount());
                    inv.setItem(slot, stack);
                }
            }

        }

        if (locRecipe.hasFluidBiproducts()) {
            List<ProbableFluid> fluidBi = locRecipe.getFluidBiproducts();
            FluidTank[] outTanks = handler.getOutputTanks();
            for (int i = 0; i < fluidBi.size(); i++) {
                outTanks[i].fill(fluidBi.get(i).roll(), FluidAction.EXECUTE);
            }
        }

        if (locRecipe.hasGasBiproducts()) {
            ComponentGasHandlerMulti gasHandler = holder.getComponent(IComponentType.GasHandler);
            List<ProbableGas> gasBi = locRecipe.getGasBiproducts();
            GasTank[] outTanks = gasHandler.getOutputTanks();
            for (int i = 0; i < gasBi.size(); i++) {
                outTanks[i].fill(gasBi.get(i).roll(), GasAction.EXECUTE);
            }
        }

        if (inv.getOutputContents().get(procNumber).isEmpty()) {
            inv.setItem(inv.getOutputSlots().get(procNumber), locRecipe.getItemRecipeOutput().copy());
        } else {
            inv.getOutputContents().get(procNumber).grow(locRecipe.getItemRecipeOutput().getCount());
        }

        FluidTank[] tanks = handler.getInputTanks();
        List<FluidIngredient> fluidIngs = locRecipe.getFluidIngredients();
        List<Integer> tankOrientation = locRecipe.getFluidArrangement();
        for (int i = 0; i < handler.tankCount(true); i++) {
            tanks[tankOrientation.get(i)].drain(fluidIngs.get(i).getFluidStack().getAmount(), FluidAction.EXECUTE);
        }
        dispenseExperience(inv, locRecipe.getXp());
        setChanged();
    }

    public void processFluid2FluidRecipe(ComponentProcessor pr) {
        if (getRecipe() != null) {
            Fluid2FluidRecipe locRecipe = (Fluid2FluidRecipe) getRecipe();
            ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
            ComponentFluidHandlerMulti handler = holder.getComponent(IComponentType.FluidHandler);

            if (locRecipe.hasItemBiproducts()) {

                List<ProbableItem> itemBi = locRecipe.getItemBiproducts();
                int index = 0;

                for (int slot : inv.getBiprodSlotsForProcessor(pr.getProcessorNumber())) {

                    ItemStack stack = inv.getItem(slot);
                    if (stack.isEmpty()) {
                        inv.setItem(slot, itemBi.get(index).roll().copy());
                    } else {
                        stack.grow(itemBi.get(index).roll().getCount());
                        inv.setItem(slot, stack);
                    }
                }

            }

            if (locRecipe.hasFluidBiproducts()) {
                List<ProbableFluid> fluidBi = locRecipe.getFluidBiproducts();
                FluidTank[] outTanks = handler.getOutputTanks();
                for (int i = 0; i < fluidBi.size(); i++) {
                    outTanks[i + 1].fill(fluidBi.get(i).roll(), FluidAction.EXECUTE);
                }
            }

            if (locRecipe.hasGasBiproducts()) {
                ComponentGasHandlerMulti gasHandler = holder.getComponent(IComponentType.GasHandler);
                List<ProbableGas> gasBi = locRecipe.getGasBiproducts();
                GasTank[] outTanks = gasHandler.getOutputTanks();
                for (int i = 0; i < gasBi.size(); i++) {
                    outTanks[i].fill(gasBi.get(i).roll(), GasAction.EXECUTE);
                }
            }

            handler.getOutputTanks()[0].fill(locRecipe.getFluidRecipeOutput(), FluidAction.EXECUTE);

            FluidTank[] tanks = handler.getInputTanks();
            List<FluidIngredient> fluidIngs = locRecipe.getFluidIngredients();
            List<Integer> tankOrientation = locRecipe.getFluidArrangement();
            for (int i = 0; i < handler.tankCount(true); i++) {
                tanks[tankOrientation.get(i)].drain(fluidIngs.get(i).getFluidStack().getAmount(), FluidAction.EXECUTE);
            }
            dispenseExperience(inv, locRecipe.getXp());
            setChanged();
        }
    }

    public void processFluid2GasRecipe(ComponentProcessor pr) {
        if (getRecipe() == null) {
            return;
        }
        Fluid2GasRecipe locRecipe = (Fluid2GasRecipe) getRecipe();
        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
        ComponentGasHandlerMulti gasHandler = holder.getComponent(IComponentType.GasHandler);
        ComponentFluidHandlerMulti fluidHandler = holder.getComponent(IComponentType.FluidHandler);

        if (locRecipe.hasItemBiproducts()) {

            List<ProbableItem> itemBi = locRecipe.getItemBiproducts();
            int index = 0;

            for (int slot : inv.getBiprodSlotsForProcessor(pr.getProcessorNumber())) {

                ItemStack stack = inv.getItem(slot);
                if (stack.isEmpty()) {
                    inv.setItem(slot, itemBi.get(index).roll().copy());
                } else {
                    stack.grow(itemBi.get(index).roll().getCount());
                    inv.setItem(slot, stack);
                }
            }

        }

        if (locRecipe.hasFluidBiproducts()) {
            List<ProbableFluid> fluidBi = locRecipe.getFluidBiproducts();
            FluidTank[] outTanks = fluidHandler.getOutputTanks();
            for (int i = 0; i < fluidBi.size(); i++) {
                outTanks[i + 1].fill(fluidBi.get(i).roll(), FluidAction.EXECUTE);
            }
        }

        if (locRecipe.hasGasBiproducts()) {
            List<ProbableGas> gasBi = locRecipe.getGasBiproducts();
            GasTank[] outTanks = gasHandler.getOutputTanks();
            for (int i = 0; i < gasBi.size(); i++) {
                outTanks[i + 1].fill(gasBi.get(i).roll(), GasAction.EXECUTE);
            }
        }

        gasHandler.getOutputTanks()[0].fill(locRecipe.getGasRecipeOutput(), GasAction.EXECUTE);

        FluidTank[] tanks = fluidHandler.getInputTanks();
        List<FluidIngredient> fluidIngs = locRecipe.getFluidIngredients();
        List<Integer> tankOrientation = locRecipe.getFluidArrangement();
        for (int i = 0; i < fluidHandler.tankCount(true); i++) {
            tanks[tankOrientation.get(i)].drain(fluidIngs.get(i).getFluidStack().getAmount(), FluidAction.EXECUTE);
        }
        dispenseExperience(inv, locRecipe.getXp());
        setChanged();
    }

    public void processFluidItem2GasRecipe(ComponentProcessor pr) {
        if (getRecipe() == null) {
            return;
        }
        FluidItem2GasRecipe locRecipe = (FluidItem2GasRecipe) getRecipe();
        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
        ComponentGasHandlerMulti gasHandler = holder.getComponent(IComponentType.GasHandler);
        ComponentFluidHandlerMulti fluidHandler = holder.getComponent(IComponentType.FluidHandler);
        List<Integer> slotOrientation = locRecipe.getItemArrangment(pr.getProcessorNumber());
        int procNumber = pr.getProcessorNumber();
        if (locRecipe.hasItemBiproducts()) {

            List<ProbableItem> itemBi = locRecipe.getItemBiproducts();
            int index = 0;

            for (int slot : inv.getBiprodSlotsForProcessor(procNumber)) {

                ItemStack stack = inv.getItem(slot);
                if (stack.isEmpty()) {
                    inv.setItem(slot, itemBi.get(index).roll().copy());
                } else {
                    stack.grow(itemBi.get(index).roll().getCount());
                    inv.setItem(slot, stack);
                }
            }

        }

        if (locRecipe.hasFluidBiproducts()) {
            List<ProbableFluid> fluidBi = locRecipe.getFluidBiproducts();
            FluidTank[] outTanks = fluidHandler.getOutputTanks();
            for (int i = 0; i < fluidBi.size(); i++) {
                outTanks[i + 1].fill(fluidBi.get(i).roll(), FluidAction.EXECUTE);
            }
        }

        if (locRecipe.hasGasBiproducts()) {
            List<ProbableGas> gasBi = locRecipe.getGasBiproducts();
            GasTank[] outTanks = gasHandler.getOutputTanks();
            for (int i = 0; i < gasBi.size(); i++) {
                outTanks[i].fill(gasBi.get(i).roll(), GasAction.EXECUTE);
            }
        }

        gasHandler.getOutputTanks()[0].fill(locRecipe.getGasRecipeOutput(), GasAction.EXECUTE);

        List<Integer> inputs = inv.getInputSlotsForProcessor(procNumber);
        for (int i = 0; i < inputs.size(); i++) {
            int index = inputs.get(slotOrientation.get(i));
            ItemStack stack = inv.getItem(index);
            stack.shrink(locRecipe.getCountedIngredients().get(i).getStackSize());
            inv.setItem(index, stack);
        }

        FluidTank[] tanks = fluidHandler.getInputTanks();
        List<FluidIngredient> fluidIngs = locRecipe.getFluidIngredients();
        List<Integer> tankOrientation = locRecipe.getFluidArrangement();
        for (int i = 0; i < fluidHandler.tankCount(true); i++) {
            tanks[tankOrientation.get(i)].drain(fluidIngs.get(i).getFluidStack().getAmount(), FluidAction.EXECUTE);
        }
        dispenseExperience(inv, locRecipe.getXp());
        setChanged();
    }

    public void dispenseExperience(ComponentInventory inv, double experience) {
        storedXp += experience;
        for (ItemStack stack : inv.getUpgradeContents()) {

            if (!stack.isEmpty()) {
                ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();
                if (upgrade.subtype == SubtypeItemUpgrade.experience) {
                    stack.set(ElectrodynamicsDataComponentTypes.XP, stack.getOrDefault(ElectrodynamicsDataComponentTypes.XP, 0.0) + getStoredXp());
                    setStoredXp(0);
                    break;
                }
            }

        }
    }

    public static boolean roomInItemBiSlots(List<ItemStack> slots, ItemStack[] biproducts) {
        for (int i = 0; i < slots.size(); i++) {
            ItemStack slotStack = slots.get(i);
            ItemStack biStack = biproducts[Math.min(i, biproducts.length - 1)];
            if (!slotStack.isEmpty()) {
                if ((slotStack.getCount() + biStack.getCount() > slotStack.getMaxStackSize())) {
                    return false;
                }
                if (!ItemUtils.testItems(slotStack.getItem(), biStack.getItem())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean roomInBiproductFluidTanks(FluidTank[] tanks, FluidStack[] stacks) {
        for (int i = 1; i < tanks.length; i++) {
            FluidTank tank = tanks[i];
            FluidStack stack = stacks[Math.min(i, stacks.length - 1)];
            int amtTaken = tank.fill(stack, FluidAction.SIMULATE);
            if (amtTaken < stack.getAmount()) {
                return false;
            }
        }
        return true;
    }

    public static boolean roomInBiproductGasTanks(GasTank[] tanks, GasStack[] stacks) {
        for (int i = 1; i < tanks.length; i++) {
            GasTank tank = tanks[i];
            GasStack stack = stacks[Math.min(i, stacks.length - 1)];
            double amtTaken = tank.fill(stack, GasAction.SIMULATE);
            if (amtTaken < stack.getAmount()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkExistingRecipe(ComponentProcessor pr) {
        if (recipe != null) {
            return recipe.matchesRecipe(pr);
        }
        return false;
    }

    @Nullable
    public ElectrodynamicsRecipe getRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
        if (cachedRecipes.isEmpty()) {
            cachedRecipes = ElectrodynamicsRecipe.findRecipesbyType((RecipeType<ElectrodynamicsRecipe>) typeIn, pr.getHolder().getLevel());
        }
        return ElectrodynamicsRecipe.getRecipe(pr, cachedRecipes);
    }

    public void setChanged() {
        // hook method; empty for now
    }

    // now it only calculates it when the upgrades in the inventory change
    public void onInventoryChange(ComponentInventory inv, int slot) {
        if (inv.getUpgradeContents().size() > 0 && (slot >= inv.getUpgradeSlotStartIndex() || slot == -1)) {
            operatingSpeed.set(1.0);
            for (ItemStack stack : inv.getUpgradeContents()) {
                if (!stack.isEmpty() && stack.getItem() instanceof ItemUpgrade upgrade && upgrade.subtype.isEmpty) {
                    for (int i = 0; i < stack.getCount(); i++) {
                        if (upgrade.subtype == SubtypeItemUpgrade.basicspeed) {
                            operatingSpeed.set(Math.min(operatingSpeed.get() * 1.5, Math.pow(1.5, 3)));
                        } else if (upgrade.subtype == SubtypeItemUpgrade.advancedspeed) {
                            operatingSpeed.set(Math.min(operatingSpeed.get() * 2.25, Math.pow(2.25, 3)));
                        }
                    }
                }
            }

            if (holder.hasComponent(IComponentType.Electrodynamic)) {
                ComponentElectrodynamic electro = holder.getComponent(IComponentType.Electrodynamic);
                electro.maxJoules(usage.get() * operatingSpeed.get() * 10);
            }
        }
    }

}
