package electrodynamics.common.tile.machines;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import electrodynamics.Electrodynamics;
import electrodynamics.api.IWrenchItem;
import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.multiblock.assemblybased.Multiblock;
import electrodynamics.api.multiblock.assemblybased.TileMultiblockController;
import electrodynamics.api.multiblock.assemblybased.TileMultiblockSlave;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerElectrolosisChamber;
import electrodynamics.common.network.utils.FluidUtilities;
import electrodynamics.common.recipe.ElectrodynamicsRecipe;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.recipe.categories.fluid2fluid.specificmachines.ElectrolosisChamberRecipe;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.components.CapabilityInputType;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;

public class TileElectrolosisChamber extends TileMultiblockController {

    public static final ResourceLocation ID = Electrodynamics.rl("electrolosischamber");
    public static final ResourceKey<Multiblock> RESOURCE_KEY = Multiblock.makeKey(ID);

    public static final int MAX_INPUT_TANK_CAPACITY = 5000;
    public static final int MAX_OUTPUT_TANK_CAPACITY = 5000;


    public final Property<Integer> processAmount = property(new Property<>(PropertyTypes.INTEGER, "processamount", 0));
    public final Property<Double> operatingTicks = property(new Property<>(PropertyTypes.DOUBLE, "operatingticks", 0.0));
    public final Property<Double> neededTicks = property(new Property<>(PropertyTypes.DOUBLE, "neededticks", 0.0));
    public final Property<Boolean> isActive = property(new Property<>(PropertyTypes.BOOLEAN, "isactive", false));

    private @Nullable ElectrolosisChamberRecipe currRecipe = null;

    public TileElectrolosisChamber(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_ELECTROLOSISCHAMBER.get(), worldPos, blockState);

        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BACK).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 16).maxJoules(Constants.ELECTROLOSIS_CHAMBER_TARGET_JOULES * 20 * 100));
        addComponent(new ComponentFluidHandlerMulti(this).setInputDirections(BlockEntityUtils.MachineDirection.RIGHT).setInputTanks(1, arr(MAX_INPUT_TANK_CAPACITY)).setOutputDirections(BlockEntityUtils.MachineDirection.LEFT).setOutputTanks(1, MAX_OUTPUT_TANK_CAPACITY).setRecipeType(ElectrodynamicsRecipeInit.ELECTROLOSIS_CHAMBER_TYPE.get()));
        addComponent(new ComponentContainerProvider(SubtypeMachine.electrolosischamber, this).createMenu((id, player) -> new ContainerElectrolosisChamber(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
        addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().bucketInputs(1).bucketOutputs(1)).valid(machineValidator()));


    }

    @Override
    public void tickServer(ComponentTickable tickable) {
        super.tickServer(tickable);

        ComponentFluidHandlerMulti fluidHandler = getComponent(IComponentType.FluidHandler);
        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

        FluidUtilities.drainItem(this, fluidHandler.getInputTanks());
        FluidUtilities.fillItem(this, fluidHandler.getOutputTanks());

        outputToPipe();

        if (currRecipe == null) {
            for (RecipeHolder<ElectrolosisChamberRecipe> recipe : getLevel().getRecipeManager().getAllRecipesFor(ElectrodynamicsRecipeInit.ELECTROLOSIS_CHAMBER_TYPE.get())) {
                if (testRecipe(recipe.value(), fluidHandler.getInputTanks())) {
                    currRecipe = recipe.value();
                    break;
                }
            }
        } else if (!testRecipe(currRecipe, fluidHandler.getInputTanks())) {
            currRecipe = null;
        }

        if (currRecipe == null || electro.getJoulesStored() <= 0 || (!fluidHandler.getOutputTanks()[0].isEmpty() && !fluidHandler.getOutputTanks()[0].getFluid().is(currRecipe.getFluidRecipeOutput().getFluid()))) {
            operatingTicks.set(0.0);
            isActive.set(false);
            processAmount.set(0);
            neededTicks.set(0.0);
            return;
        }

        double energySatisfaction = electro.getJoulesStored() / Constants.ELECTROLOSIS_CHAMBER_TARGET_JOULES;

        if (energySatisfaction < 1) {
            neededTicks.set(1.0 / energySatisfaction);
            processAmount.set(1);
        } else {
            neededTicks.set(0.0);
            operatingTicks.set(0.0);
            processAmount.set((int) energySatisfaction);
        }

        int room = fluidHandler.getOutputTanks()[0].getCapacity() - fluidHandler.getOutputTanks()[0].getFluidAmount();

        if (room <= 0) {
            isActive.set(false);
            return;
        }

        int amtToProcess = Math.min(room, processAmount.get());

        electro.setJoulesStored(0);

        isActive.set(true);

        if (neededTicks.get() > 0 && operatingTicks.get() < neededTicks.get()) {
            operatingTicks.set(operatingTicks.get() + 1.0);
            return;
        }

        operatingTicks.set(0.0);

        fluidHandler.getInputTanks()[0].drain(amtToProcess, IFluidHandler.FluidAction.EXECUTE);
        fluidHandler.getOutputTanks()[0].fill(new FluidStack(currRecipe.getFluidRecipeOutput().getFluidHolder(), amtToProcess), IFluidHandler.FluidAction.EXECUTE);


    }

    private boolean testRecipe(ElectrolosisChamberRecipe recipe, FluidTank[] inputTanks) {
        Pair<List<Integer>, Boolean> pair = ElectrodynamicsRecipe.areFluidsValid(recipe.getFluidIngredients(), inputTanks);
        if (pair.getSecond()) {
            recipe.setFluidArrangement(pair.getFirst());
            return true;
        }
        return false;
    }

    private void outputToPipe() {

        ComponentFluidHandlerMulti component = getComponent(IComponentType.FluidHandler);
        Direction[] outputDirections = component.outputDirections;

        Direction facing = getFacing();

        for (Direction relative : outputDirections) {

            Direction direction = BlockEntityUtils.getRelativeSide(facing, relative);

            BlockEntity faceTile = getLevel().getBlockEntity(getBlockPos().relative(direction).offset(2, 0, 2));

            if (faceTile == null) {
                continue;
            }

            IFluidHandler handler = getLevel().getCapability(Capabilities.FluidHandler.BLOCK, faceTile.getBlockPos(), faceTile.getBlockState(), faceTile, direction.getOpposite());

            if (handler == null) {
                continue;
            }

            for (FluidTank fluidTank : component.getOutputTanks()) {

                FluidStack tankFluid = fluidTank.getFluid();

                int amtAccepted = handler.fill(tankFluid, IFluidHandler.FluidAction.EXECUTE);

                FluidStack taken = new FluidStack(tankFluid.getFluid(), amtAccepted);

                fluidTank.drain(taken, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    @Override
    public @Nullable IFluidHandler getFluidHandlerCapability(@Nullable Direction side) {
        return null;
    }

    @Nullable
    @Override
    public IFluidHandler getSlaveFluidHandlerCapability(TileMultiblockSlave slave, @Nullable Direction side) {
        if (slave.index.get() != 35 && slave.index.get() != 39) {
            return null;
        }
        return this.<IComponentFluidHandler>getComponent(IComponentType.FluidHandler).getCapability(side, CapabilityInputType.NONE);
    }

    @Override
    public @Nullable ICapabilityElectrodynamic getElectrodynamicCapability(@Nullable Direction side) {
        return null;
    }

    @Nullable
    @Override
    public ICapabilityElectrodynamic getSlaveCapabilityElectrodynamic(TileMultiblockSlave slave, @Nullable Direction side) {
        if (slave.index.get() != 7) {
            return null;
        }
        return this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getCapability(side, CapabilityInputType.NONE);
    }

    @Override
    public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        return null;
    }

    @Override
    public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide() && hit.getBlockPos().equals(getBlockPos()) && used.getItem() instanceof IWrenchItem) {
            checkFormed();
            if (isFormed.get()) {
                formMultiblock();
            } else {
                destroyMultiblock();
            }
            return ItemInteractionResult.CONSUME;


        }
        return super.useWithItem(used, player, hand, hit);
    }

    @Override
    public InteractionResult useWithoutItem(Player player, BlockHitResult hit) {
        if (!isFormed.get()) {
            return InteractionResult.FAIL;
        }
        return super.useWithoutItem(player, hit);
    }

    @Override
    public ResourceLocation getMultiblockId() {
        return ID;
    }

    @Override
    public ResourceKey<Multiblock> getResourceKey() {
        return RESOURCE_KEY;
    }
}
