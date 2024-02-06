package electrodynamics.datagen.server.recipe.types.custom.fluiditem2fluid;

import electrodynamics.api.References;
import electrodynamics.common.fluid.types.liquid.subtype.SubtypeSulfateFluid;
import electrodynamics.common.recipe.categories.fluiditem2fluid.specificmachines.MineralWasherRecipe;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder.RecipeCategory;
import electrodynamics.datagen.utils.recipe.builders.FluidItem2FluidBuilder;
import electrodynamics.registers.ElectrodynamicsFluids;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public class ElectrodynamicsMineralWasherRecipes extends AbstractRecipeGenerator {

	public static double MINERALWASHER_USAGE_PER_TICK = 400.0;
	public static int MINERALWASHER_REQUIRED_TICKS = 200;

	private final String modID;

	public ElectrodynamicsMineralWasherRecipes(String modID) {
		this.modID = modID;
	}

	public ElectrodynamicsMineralWasherRecipes() {
		this(References.ID);
	}

	@Override
	public void addRecipes(RecipeOutput output) {

		newRecipe(new FluidStack(Fluids.LAVA, 1500), 0, 200, 400.0, "lava_from_magma_block", modID)
				//
				.addFluidTagInput(FluidTags.LAVA, 1000)
				//
				.addItemStackInput(new ItemStack(Items.MAGMA_BLOCK))
				//
				.save(output);

		for (SubtypeSulfateFluid fluid : SubtypeSulfateFluid.values()) {
			if (fluid.source != null) {
				newRecipe(new FluidStack(ElectrodynamicsFluids.SUBTYPEFLUID_REGISTRY_MAP.get(fluid).get(), 1000), 0, 200, 400.0, "sulfate_" + fluid.name(), modID)
						//
						.addItemTagInput(fluid.source, 1)
						//
						.addFluidTagInput(ElectrodynamicsTags.Fluids.SULFURIC_ACID, 1000)
						//
						.save(output);
			}
		}

	}

	public FluidItem2FluidBuilder<MineralWasherRecipe> newRecipe(FluidStack stack, float xp, int ticks, double usagePerTick, String name, String group) {
		return new FluidItem2FluidBuilder<>(MineralWasherRecipe::new, stack, RecipeCategory.FLUID_ITEM_2_FLUID, modID, "mineral_washer/" + name, group, xp, ticks, usagePerTick);
	}

}
