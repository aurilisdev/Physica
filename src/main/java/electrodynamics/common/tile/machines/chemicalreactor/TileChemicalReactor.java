package electrodynamics.common.tile.machines.chemicalreactor;

import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasTank;
import electrodynamics.api.multiblock.Subnode;
import electrodynamics.api.multiblock.parent.IMultiblockParentTile;
import electrodynamics.common.block.BlockMachine;
import electrodynamics.common.block.chemicalreactor.BlockChemicalReactorExtra;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerChemicalReactor;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.recipe.categories.chemicalreactor.ChemicalReactorRecipe;
import electrodynamics.common.recipe.recipeutils.*;
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.prefab.block.GenericEntityBlockWaterloggable;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.components.CapabilityInputType;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.*;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.prefab.tile.types.GenericGasTile;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileChemicalReactor extends GenericGasTile {

    public static final int MAX_FLUID_TANK_CAPACITY = 5000;
    public static final double MAX_GAS_TANK_CAPACITY = 5000.0;

    public final Property<Boolean> hasItemInputs = property(new Property<>(PropertyTypes.BOOLEAN, "hasiteminputs", false));
    public final Property<Boolean> hasFluidInputs = property(new Property<>(PropertyTypes.BOOLEAN, "hasfluidinputs", false));
    public final Property<Boolean> hasGasInputs = property(new Property<>(PropertyTypes.BOOLEAN, "hasgasinputs", false));

    public TileChemicalReactor(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_CHEMICALREACTOR.get(), worldPos, blockState);
        addComponent(new ComponentTickable(this));
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentContainerProvider("container.chemicalreactor", this).createMenu((id, player) -> new ContainerChemicalReactor(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(Direction.UP, Direction.DOWN).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 4));
        addComponent(new ComponentFluidHandlerMulti(this).setTanks(2, 2, new int[]{MAX_FLUID_TANK_CAPACITY, MAX_FLUID_TANK_CAPACITY}, new int[]{MAX_FLUID_TANK_CAPACITY, MAX_FLUID_TANK_CAPACITY}).setInputDirections(Direction.EAST, Direction.SOUTH).setOutputDirections(Direction.WEST, Direction.NORTH).setRecipeType(ElectrodynamicsRecipeInit.CHEMICAL_REACTOR_TYPE.get()));
        addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().processors(1, 2, 1, 3).bucketInputs(2).bucketOutputs(2).gasInputs(2).gasOutputs(2).upgrades(3)).setDirectionsBySlot(0, Direction.SOUTH).setDirectionsBySlot(1, Direction.EAST).setSlotsByDirection(Direction.WEST, 2).setSlotsByDirection(Direction.NORTH, 3, 4, 5).validUpgrades(ContainerChemicalReactor.VALID_UPGRADES).valid(machineValidator()));
        addComponent(new ComponentGasHandlerMulti(this).setInputDirections(Direction.WEST, Direction.NORTH).setOutputDirections(Direction.EAST, Direction.SOUTH).setInputTanks(2, arr(MAX_GAS_TANK_CAPACITY, MAX_GAS_TANK_CAPACITY), arr(1000.0, 1000.0), arr(1024, 1024)).setOutputTanks(2, arr(MAX_GAS_TANK_CAPACITY, MAX_GAS_TANK_CAPACITY), arr(1000.0, 1000.0), arr(1024, 1024)).setCondensedHandler(getCondensedHandler()).setRecipeType(ElectrodynamicsRecipeInit.CHEMICAL_REACTOR_TYPE.get()));
        addComponent(new ComponentProcessor(this).canProcess(this::canProcess).process(this::process));
    }

    private boolean canProcess(ComponentProcessor pr) {
        pr.consumeBucket().consumeGasCylinder().dispenseGasCylinder().dispenseBucket().outputToGasPipe();
        outputToPipe();
        ChemicalReactorRecipe locRecipe;
        if (!pr.checkExistingRecipe(pr)) {
            pr.setShouldKeepProgress(false);
            pr.operatingTicks.set(0.0);
            locRecipe = (ChemicalReactorRecipe) pr.getRecipe(pr, ElectrodynamicsRecipeInit.CHEMICAL_REACTOR_TYPE.get());
            if (locRecipe == null) {
                hasItemInputs.set(false);
                hasFluidInputs.set(false);
                hasGasInputs.set(false);
                return false;
            }
        } else {
            pr.setShouldKeepProgress(true);
            locRecipe = (ChemicalReactorRecipe) pr.getRecipe();
        }
        pr.setRecipe(locRecipe);
        hasItemInputs.set(locRecipe.hasItemInputs());
        hasFluidInputs.set(locRecipe.hasFluidInputs());
        hasGasInputs.set(locRecipe.hasGasInputs());

        pr.requiredTicks.set((double) locRecipe.getTicks());
        pr.usage.set(locRecipe.getUsagePerTick());

        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
        electro.maxJoules(pr.usage.get() * pr.operatingSpeed.get() * 10 * pr.totalProcessors);

        if (electro.getJoulesStored() < pr.getUsage()) {
            return false;
        }

        if (locRecipe.hasItemOutput()) {
            ComponentInventory inv = getComponent(IComponentType.Inventory);
            ItemStack output = inv.getOutputContents().get(pr.getProcessorNumber());
            ItemStack result = locRecipe.getItemRecipeOutput();
            boolean isEmpty = output.isEmpty();
            if (!isEmpty && !ItemUtils.testItems(output.getItem(), result.getItem())) {
                return false;
            }

            int locCap = isEmpty ? 64 : output.getMaxStackSize();
            if (locCap < output.getCount() + result.getCount()) {
                return false;
            }
        }

        if (locRecipe.hasFluidOutput()) {
            ComponentFluidHandlerMulti handler = getComponent(IComponentType.FluidHandler);
            int amtAccepted = handler.getOutputTanks()[0].fill(locRecipe.getFluidRecipeOutput(), IFluidHandler.FluidAction.SIMULATE);
            if (amtAccepted < locRecipe.getFluidRecipeOutput().getAmount()) {
                return false;
            }
        }

        if (locRecipe.hasGasOutput()) {
            ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);
            double amtAccepted = gasHandler.getOutputTanks()[0].fill(locRecipe.getGasRecipeOutput(), GasAction.SIMULATE);
            if (amtAccepted < locRecipe.getGasRecipeOutput().getAmount()) {
                return false;
            }
        }

        if (locRecipe.hasItemBiproducts()) {
            ComponentInventory inv = getComponent(IComponentType.Inventory);
            boolean itemBiRoom = pr.roomInItemBiSlots(inv.getBiprodsForProcessor(pr.getProcessorNumber()), locRecipe.getFullItemBiStacks());
            if (!itemBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasFluidBiproducts()) {
            ComponentFluidHandlerMulti fluidHandler = getComponent(IComponentType.FluidHandler);
            boolean fluidBiRoom = pr.roomInBiproductFluidTanks(fluidHandler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
            if (!fluidBiRoom) {
                return false;
            }
        }
        if (locRecipe.hasGasBiproducts()) {
            ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);
            boolean gasBiRoom = pr.roomInBiproductGasTanks(gasHandler.getOutputTanks(), locRecipe.getFullGasBiStacks());
            if (!gasBiRoom) {
                return false;
            }
        }
        return true;
    }

    private void process(ComponentProcessor pr) {
        if (pr.getRecipe() == null) {
            return;
        }
        ChemicalReactorRecipe locRecipe = (ChemicalReactorRecipe) pr.getRecipe();

        ComponentInventory inv = getComponent(IComponentType.Inventory);
        ComponentGasHandlerMulti gasHandler = getComponent(IComponentType.GasHandler);
        ComponentFluidHandlerMulti fluidHandler = getComponent(IComponentType.FluidHandler);

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
                outTanks[i + 1].fill(fluidBi.get(i).roll(), IFluidHandler.FluidAction.EXECUTE);
            }
        }

        if (locRecipe.hasGasBiproducts()) {
            List<ProbableGas> gasBi = locRecipe.getGasBiproducts();
            GasTank[] outTanks = gasHandler.getOutputTanks();
            for (int i = 0; i < gasBi.size(); i++) {
                outTanks[i].fill(gasBi.get(i).roll(), GasAction.EXECUTE);
            }
        }

        if (locRecipe.hasItemOutput()) {
            if (inv.getOutputContents().get(procNumber).isEmpty()) {
                inv.setItem(inv.getOutputSlots().get(procNumber), locRecipe.getItemRecipeOutput().copy());
            } else {
                inv.getOutputContents().get(procNumber).grow(locRecipe.getItemRecipeOutput().getCount());
            }
        }

        if (locRecipe.hasFluidOutput()) {
            fluidHandler.getOutputTanks()[0].fill(locRecipe.getFluidRecipeOutput(), IFluidHandler.FluidAction.EXECUTE);
        }

        if (locRecipe.hasGasOutput()) {
            gasHandler.getOutputTanks()[0].fill(locRecipe.getGasRecipeOutput(), GasAction.EXECUTE);
        }

        if (locRecipe.hasItemInputs()) {
            List<Integer> slotOrientation = locRecipe.getItemArrangment(pr.getProcessorNumber());
            List<Integer> inputs = inv.getInputSlotsForProcessor(procNumber);
            for (int i = 0; i < inputs.size(); i++) {
                int index = inputs.get(slotOrientation.get(i));
                ItemStack stack = inv.getItem(index);
                stack.shrink(locRecipe.getCountedIngredients().get(i).getStackSize());
                inv.setItem(index, stack);
            }
        }

        if (locRecipe.hasFluidInputs()) {
            FluidTank[] tanks = fluidHandler.getInputTanks();
            List<FluidIngredient> fluidIngs = locRecipe.getFluidIngredients();
            List<Integer> tankOrientation = locRecipe.getFluidArrangement();
            for (int i = 0; i < fluidHandler.tankCount(true); i++) {
                tanks[tankOrientation.get(i)].drain(fluidIngs.get(i).getFluidStack().getAmount(), IFluidHandler.FluidAction.EXECUTE);
            }
        }

        if (locRecipe.hasGasInputs()) {
            GasTank[] tanks = gasHandler.getInputTanks();
            List<GasIngredient> gasIngs = locRecipe.getGasIngredients();
            List<Integer> tankOrientation = locRecipe.getFluidArrangement();
            for (int i = 0; i < gasHandler.tankCount(true); i++) {
                tanks[tankOrientation.get(i)].drain(gasIngs.get(i).getGasStack().getAmount(), GasAction.EXECUTE);
            }
        }


        pr.dispenseExperience(inv, locRecipe.getXp());
        pr.setChanged();
    }

    private void outputToPipe() {

        ComponentFluidHandlerMulti component = getComponent(IComponentType.FluidHandler);
        Direction[] outputDirections = component.outputDirections;

        Direction facing = getFacing();

        for (Direction relative : outputDirections) {

            Direction direction = BlockEntityUtils.getRelativeSide(facing, relative.getOpposite());

            BlockPos face = getBlockPos().relative(direction.getOpposite()).offset(0, 2, 0);

            BlockEntity faceTile = getLevel().getBlockEntity(face);

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

    @Override
    public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        return null;
    }

    @Override
    public void onBlockDestroyed() {
        getLevel().destroyBlock(getBlockPos().offset(BlockChemicalReactorExtra.Location.MIDDLE.offsetUpFromParent), false);
        getLevel().destroyBlock(getBlockPos().offset(BlockChemicalReactorExtra.Location.TOP.offsetUpFromParent), false);
        super.onBlockDestroyed();
    }
}
