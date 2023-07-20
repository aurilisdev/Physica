package electrodynamics.compatibility.jei.recipecategories.utils.psuedorecipes;

import java.util.Arrays;
import java.util.HashSet;

import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.compatibility.jei.recipecategories.utils.psuedorecipes.types.PsuedoFluid2GasRecipe;
import electrodynamics.compatibility.jei.recipecategories.utils.psuedorecipes.types.PsuedoGas2FluidRecipe;
import electrodynamics.prefab.utilities.CapabilityUtils;
import electrodynamics.registers.ElectrodynamicsGases;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class ElectrodynamicsPsuedoRecipes {

	public static final HashSet<PsuedoFluid2GasRecipe> EVAPORATION_RECIPES = new HashSet<>();
	public static final HashSet<PsuedoGas2FluidRecipe> CONDENSATION_RECIPES = new HashSet<>();

	public static void initRecipes() {

		for (Gas gas : ElectrodynamicsRegistries.gasRegistry().getValues()) {

			if (gas.isEmpty()) {
				continue;
			}

			if (gas.getCondensedFluid().isSame(Fluids.EMPTY)) {
				continue;
			}

			ItemStack inputCylinder = new ItemStack(ElectrodynamicsItems.ITEM_PORTABLECYLINDER.get());

			CapabilityUtils.fillGasItem(inputCylinder, new GasStack(gas, 1000, gas.getCondensationTemp() + 0.00001, Gas.PRESSURE_AT_SEA_LEVEL), GasAction.EXECUTE);

			ItemStack outputBucket = new ItemStack(gas.getCondensedFluid().getBucket());

			CapabilityUtils.fillFluidItem(outputBucket, new FluidStack(gas.getCondensedFluid(), 1000), FluidAction.EXECUTE);

			CONDENSATION_RECIPES.add(new PsuedoGas2FluidRecipe(Arrays.asList(new GasStack(gas, 1000, gas.getCondensationTemp(), 1)), new FluidStack(gas.getCondensedFluid(), 1000), inputCylinder, outputBucket));

		}

		ElectrodynamicsGases.MAPPED_GASSES.forEach((fluid, gas) -> {

			if (fluid.isSame(Fluids.EMPTY)) {
				return;
			}

			ItemStack inputBucket = new ItemStack(fluid.getBucket());

			CapabilityUtils.fillFluidItem(inputBucket, new FluidStack(fluid, 1000), FluidAction.EXECUTE);

			ItemStack outputCylinder = new ItemStack(ElectrodynamicsItems.ITEM_PORTABLECYLINDER.get());

			CapabilityUtils.fillGasItem(outputCylinder, new GasStack(gas, 1000, gas.getCondensationTemp() + 0.00001, Gas.PRESSURE_AT_SEA_LEVEL), GasAction.EXECUTE);

			EVAPORATION_RECIPES.add(new PsuedoFluid2GasRecipe(Arrays.asList(new FluidStack(fluid, 1000)), new GasStack(gas, 1000, gas.getCondensationTemp(), 1), inputBucket, outputCylinder));

		});

	}

}
