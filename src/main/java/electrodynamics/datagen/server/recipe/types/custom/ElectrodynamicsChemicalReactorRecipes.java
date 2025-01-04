package electrodynamics.datagen.server.recipe.types.custom;

import electrodynamics.api.References;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.fluid.subtype.SubtypeCrudeMineralFluid;
import electrodynamics.common.fluid.subtype.SubtypeDirtyMineralFluid;
import electrodynamics.common.fluid.subtype.SubtypeRoyalMineralFluid;
import electrodynamics.common.fluid.subtype.SubtypeSulfateFluid;
import electrodynamics.common.item.subtype.SubtypeOxide;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.builders.ChemicalReactorBuilder;
import electrodynamics.datagen.utils.recipe.builders.ElectrodynamicsRecipeBuilder;
import electrodynamics.registers.ElectrodynamicsFluids;
import electrodynamics.registers.ElectrodynamicsGases;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public class ElectrodynamicsChemicalReactorRecipes extends AbstractRecipeGenerator {

    public final String modID;

    public ElectrodynamicsChemicalReactorRecipes(String modID) {
        this.modID = modID;
    }

    public ElectrodynamicsChemicalReactorRecipes() {
        this(References.ID);
    }

    @Override
    public void addRecipes(RecipeOutput output) {

        for (SubtypeSulfateFluid fluid : SubtypeSulfateFluid.values()) {
            newRecipe(0, 200, 800.0, "pure_" + fluid.name() + "_from_" + fluid.name() + "_sulfate", modID)
                    //
                    .setFluidOutput(new FluidStack(fluid.result.get(), 200))
                    //
                    .addFluidTagInput(fluid.tag, 200)
                    //
                    .addFluidTagInput(FluidTags.WATER, 1000)
                    //
                    .addFluidBiproduct(new ProbableFluid(new FluidStack(ElectrodynamicsFluids.FLUID_SULFURICACID.get(), 150), 1))
                    //
                    .save(output);
        }

        newRecipe(0, 200, 700, "hydrochloric_acid", modID)
                //
                .setFluidOutput(new FluidStack(ElectrodynamicsFluids.FLUID_HYDROCHLORICACID.get(), 500))
                //
                .addFluidTagInput(FluidTags.WATER, 1000)
                //
                .addItemTagInput(ElectrodynamicsTags.Items.DUST_SALT, 5)
                //
                .addGasTagInput(ElectrodynamicsTags.Gases.HYDROGEN, new ElectrodynamicsRecipeBuilder.GasIngWrapper(1000, 500, 8))
                //
                .save(output);

        newRecipe(0, 200, 1000, "ammonia", modID)
                //
                .setGasOutput(new GasStack(ElectrodynamicsGases.AMMONIA.value(), 1000, Gas.ROOM_TEMPERATURE, Gas.PRESSURE_AT_SEA_LEVEL))
                //
                .addFluidTagInput(FluidTags.WATER, 2000)
                //
                .addGasTagInput(ElectrodynamicsTags.Gases.NITROGEN, new ElectrodynamicsRecipeBuilder.GasIngWrapper(1000, 500, 4))
                //
                .addGasTagInput(ElectrodynamicsTags.Gases.HYDROGEN, new ElectrodynamicsRecipeBuilder.GasIngWrapper(1000, 500, 4))
                //
                .save(output);

        newRecipe(0, 200, 1000, "nitric_acid", modID)
                //
                .setFluidOutput(new FluidStack(ElectrodynamicsFluids.FLUID_NITRICACID.get(), 500))
                //
                .addFluidTagInput(FluidTags.WATER, 3000)
                //
                .addGasTagInput(ElectrodynamicsTags.Gases.AMMONIA, new ElectrodynamicsRecipeBuilder.GasIngWrapper(1000, 700, 4))
                //
                .save(output);
        newRecipe(0, 200, 500, "sulfur_trioxide_alternative", modID)
                //
                .setItemOutput(new ItemStack(ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.trisulfur)))
                //
                .addGasTagInput(ElectrodynamicsTags.Gases.SULFUR_DIOXIDE, new ElectrodynamicsRecipeBuilder.GasIngWrapper(1000, 373, Gas.PRESSURE_AT_SEA_LEVEL))
                //
                .addItemTagInput(ElectrodynamicsTags.Items.OXIDE_VANADIUM, 1)
                //
                .save(output);
        newRecipe(0, 200, 500, "aqua_regia", modID)
                //
                .setFluidOutput(new FluidStack(ElectrodynamicsFluids.FLUID_AQUAREGIA, 100))
                //
                .addFluidTagInput(ElectrodynamicsTags.Fluids.HYDROCHLORIC_ACID, 1000)
                //
                .addFluidTagInput(ElectrodynamicsTags.Fluids.NITRIC_ACID, 1000)
                //
                .save(output);
        newRecipe(0, 200, 200, "fertilizer", modID)
                //
                .setItemOutput(new ItemStack(ElectrodynamicsItems.ITEM_MOLYBDENUMFERTILIZER, 16))
                //
                .addItemTagInput(ElectrodynamicsTags.Items.DUST_MOLYBDENUM, 2)
                //
                .addItemStackInput(new ItemStack(Items.BONE_MEAL))
                //
                .addGasTagInput(ElectrodynamicsTags.Gases.AMMONIA, new ElectrodynamicsRecipeBuilder.GasIngWrapper(100, Gas.ROOM_TEMPERATURE, 4))
                //
                .save(output);

        for (SubtypeRoyalMineralFluid fluid : SubtypeRoyalMineralFluid.values()) {
            newRecipe(0, 100, 800.0, "crude_" + fluid.name() + "_from_royal_" + fluid.name(), modID)
                    //
                    .setFluidOutput(new FluidStack(fluid.result.get(), 200))
                    //
                    .addFluidStackInput(new FluidStack(ElectrodynamicsFluids.FLUIDS_ROYALMINERAL.getValue(fluid), 200))
                    //
                    .addFluidTagInput(FluidTags.WATER, 1000)
                    //
                    .addFluidBiproduct(new ProbableFluid(new FluidStack(ElectrodynamicsFluids.FLUID_AQUAREGIA.get(), 50), 1))
                    //
                    .save(output);
        }

        for (SubtypeCrudeMineralFluid fluid : SubtypeCrudeMineralFluid.values()) {
            newRecipe(0, 100, 600.0, "dirty_" + fluid.name() + "_from_crude_" + fluid.name(), modID)
                    //
                    .setFluidOutput(new FluidStack(fluid.result.get(), 200))
                    //
                    .addFluidStackInput(new FluidStack(ElectrodynamicsFluids.FLUIDS_CRUDEMINERAL.getValue(fluid), 200))
                    //
                    .addFluidTagInput(ElectrodynamicsTags.Fluids.SULFURIC_ACID, 500)
                    //
                    .addFluidBiproduct(new ProbableFluid(new FluidStack(Fluids.WATER, 200), 0.25))
                    //
                    .save(output);
        }

        for (SubtypeDirtyMineralFluid fluid : SubtypeDirtyMineralFluid.values()) {
            newRecipe(0, 100, 700.0, "impure_" + fluid.name() + "_from_dirty_" + fluid.name(), modID)
                    //
                    .setFluidOutput(new FluidStack(fluid.result.get(), 200))
                    //
                    .addFluidStackInput(new FluidStack(ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(fluid), 200))
                    //
                    .addFluidTagInput(FluidTags.WATER, 1000)
                    //
                    .addFluidBiproduct(new ProbableFluid(new FluidStack(ElectrodynamicsFluids.FLUID_SULFURICACID, 500), 1))
                    //
                    .save(output);
        }
        

    }

    public ChemicalReactorBuilder newRecipe(float xp, int ticks, double usagePerTick, String name, String group) {
        return new ChemicalReactorBuilder(ElectrodynamicsRecipeBuilder.RecipeCategory.CHEMICAL_REACTOR, modID, name, group, xp, ticks, usagePerTick);
    }
}
