package electrodynamics.common.recipe;

import electrodynamics.api.References;
import electrodynamics.common.recipe.categories.chemicalreactor.ChemicalReactorRecipe;
import electrodynamics.common.recipe.categories.chemicalreactor.ChemicalReactorRecipeSerializer;
import electrodynamics.common.recipe.categories.fluid2fluid.Fluid2FluidRecipeSerializer;
import electrodynamics.common.recipe.categories.fluid2fluid.specificmachines.ElectrolosisChamberRecipe;
import electrodynamics.common.recipe.categories.fluid2gas.Fluid2GasRecipeSerializer;
import electrodynamics.common.recipe.categories.fluid2gas.specificmachines.ElectrolyticSeparatorRecipe;
import electrodynamics.common.recipe.categories.fluid2item.Fluid2ItemRecipeSerializer;
import electrodynamics.common.recipe.categories.fluid2item.specificmachines.ChemicalCrystalizerRecipe;
import electrodynamics.common.recipe.categories.fluiditem2fluid.FluidItem2FluidRecipeSerializer;
import electrodynamics.common.recipe.categories.fluiditem2fluid.specificmachines.ChemicalMixerRecipe;
import electrodynamics.common.recipe.categories.fluiditem2fluid.specificmachines.FermentationPlantRecipe;
import electrodynamics.common.recipe.categories.fluiditem2fluid.specificmachines.MineralWasherRecipe;
import electrodynamics.common.recipe.categories.item2item.Item2ItemRecipeSerializer;
import electrodynamics.common.recipe.categories.item2item.specificmachines.EnergizedAlloyerRecipe;
import electrodynamics.common.recipe.categories.item2item.specificmachines.LatheRecipe;
import electrodynamics.common.recipe.categories.item2item.specificmachines.MineralCrusherRecipe;
import electrodynamics.common.recipe.categories.item2item.specificmachines.MineralGrinderRecipe;
import electrodynamics.common.recipe.categories.item2item.specificmachines.OxidationFurnaceRecipe;
import electrodynamics.common.recipe.categories.item2item.specificmachines.ReinforcedAlloyerRecipe;
import electrodynamics.common.recipe.categories.item2item.specificmachines.WireMillRecipe;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.EnchantmentIngredient;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.GasIngredient;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ElectrodynamicsRecipeInit {

    // Deferred Register
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, References.ID);
    
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, References.ID);
    
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, References.ID);
    
    /* INGREDIENT TYPES */
    
    public static final DeferredHolder<IngredientType<?>, IngredientType<CountableIngredient>> COUNTABLE_INGREDIENT_TYPE = INGREDIENT_TYPES.register("countableingredient", () -> new IngredientType<>(CountableIngredient.CODEC));
    public static final DeferredHolder<IngredientType<?>, IngredientType<FluidIngredient>> FLUID_INGREDIENT_TYPE = INGREDIENT_TYPES.register("fluidingredient", () -> new IngredientType<>(FluidIngredient.CODEC));
    public static final DeferredHolder<IngredientType<?>, IngredientType<GasIngredient>> GAS_INGREDIENT_TYPE = INGREDIENT_TYPES.register("gasingredient", () -> new IngredientType<>(GasIngredient.CODEC));
    public static final DeferredHolder<IngredientType<?>, IngredientType<EnchantmentIngredient>> ENCHANTMENT_INGREDIENT_TYPE = INGREDIENT_TYPES.register("enchantmentingredient", () -> new IngredientType<>(EnchantmentIngredient.CODEC));
    
    /* RECIPE TYPES */

    // Item2Item
    public static final DeferredHolder<RecipeType<?>, RecipeType<WireMillRecipe>> WIRE_MILL_TYPE = RECIPE_TYPES.register(WireMillRecipe.RECIPE_GROUP, CustomRecipeType::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<MineralGrinderRecipe>> MINERAL_GRINDER_TYPE = RECIPE_TYPES.register(MineralGrinderRecipe.RECIPE_GROUP, CustomRecipeType::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<MineralCrusherRecipe>> MINERAL_CRUSHER_TYPE = RECIPE_TYPES.register(MineralCrusherRecipe.RECIPE_GROUP, CustomRecipeType::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<LatheRecipe>> LATHE_TYPE = RECIPE_TYPES.register(LatheRecipe.RECIPE_GROUP, CustomRecipeType::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<OxidationFurnaceRecipe>> OXIDATION_FURNACE_TYPE = RECIPE_TYPES.register(OxidationFurnaceRecipe.RECIPE_GROUP, CustomRecipeType::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<EnergizedAlloyerRecipe>> ENERGIZED_ALLOYER_TYPE = RECIPE_TYPES.register(EnergizedAlloyerRecipe.RECIPE_GROUP, CustomRecipeType::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<ReinforcedAlloyerRecipe>> REINFORCED_ALLOYER_TYPE = RECIPE_TYPES.register(ReinforcedAlloyerRecipe.RECIPE_GROUP, CustomRecipeType::new);

    // FluidItem2Fluid
    public static final DeferredHolder<RecipeType<?>, RecipeType<ChemicalMixerRecipe>> CHEMICAL_MIXER_TYPE = RECIPE_TYPES.register(ChemicalMixerRecipe.RECIPE_GROUP, CustomRecipeType::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<FermentationPlantRecipe>> FERMENTATION_PLANT_TYPE = RECIPE_TYPES.register(FermentationPlantRecipe.RECIPE_GROUP, CustomRecipeType::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<MineralWasherRecipe>> MINERAL_WASHER_TYPE = RECIPE_TYPES.register(MineralWasherRecipe.RECIPE_GROUP, CustomRecipeType::new);

    // Fluid2Item
    public static final DeferredHolder<RecipeType<?>, RecipeType<ChemicalCrystalizerRecipe>> CHEMICAL_CRYSTALIZER_TYPE = RECIPE_TYPES.register(ChemicalCrystalizerRecipe.RECIPE_GROUP, CustomRecipeType::new);

    // Fluid2Gas
    public static final DeferredHolder<RecipeType<?>, RecipeType<ElectrolyticSeparatorRecipe>> ELECTROLYTIC_SEPERATOR_TYPE = RECIPE_TYPES.register(ElectrolyticSeparatorRecipe.RECIPE_GROUP, CustomRecipeType::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ChemicalReactorRecipe>> CHEMICAL_REACTOR_TYPE = RECIPE_TYPES.register(ChemicalReactorRecipe.RECIPE_GROUP, CustomRecipeType::new);

    // Fluid2Fluid

    public static final DeferredHolder<RecipeType<?>, RecipeType<ElectrolosisChamberRecipe>> ELECTROLOSIS_CHAMBER_TYPE = RECIPE_TYPES.register(ElectrolosisChamberRecipe.RECIPE_GROUP, CustomRecipeType::new);

    /* SERIALIZERS */

    // Item2Item
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> WIRE_MILL_SERIALIZER = RECIPE_SERIALIZER.register(WireMillRecipe.RECIPE_GROUP, () -> new Item2ItemRecipeSerializer<>(WireMillRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> MINERAL_GRINDER_SERIALIZER = RECIPE_SERIALIZER.register(MineralGrinderRecipe.RECIPE_GROUP, () -> new Item2ItemRecipeSerializer<>(MineralGrinderRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> MINERAL_CRUSHER_SERIALIZER = RECIPE_SERIALIZER.register(MineralCrusherRecipe.RECIPE_GROUP, () -> new Item2ItemRecipeSerializer<>(MineralCrusherRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> LATHE_SERIALIZER = RECIPE_SERIALIZER.register(LatheRecipe.RECIPE_GROUP, () -> new Item2ItemRecipeSerializer<>(LatheRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> OXIDATION_FURNACE_SERIALIZER = RECIPE_SERIALIZER.register(OxidationFurnaceRecipe.RECIPE_GROUP, () -> new Item2ItemRecipeSerializer<>(OxidationFurnaceRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> ENERGIZED_ALLOYER_SERIALIZER = RECIPE_SERIALIZER.register(EnergizedAlloyerRecipe.RECIPE_GROUP, () -> new Item2ItemRecipeSerializer<>(EnergizedAlloyerRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> REINFORCED_ALLOYER_SERIALIZER = RECIPE_SERIALIZER.register(ReinforcedAlloyerRecipe.RECIPE_GROUP, () -> new Item2ItemRecipeSerializer<>(ReinforcedAlloyerRecipe::new));

    // FluidItem2Fluid
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> CHEMICAL_MIXER_SERIALIZER = RECIPE_SERIALIZER.register(ChemicalMixerRecipe.RECIPE_GROUP, () -> new FluidItem2FluidRecipeSerializer<>(ChemicalMixerRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> FERMENTATION_PLANT_SERIALIZER = RECIPE_SERIALIZER.register(FermentationPlantRecipe.RECIPE_GROUP, () -> new FluidItem2FluidRecipeSerializer<>(FermentationPlantRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> MINERAL_WASHER_SERIALIZER = RECIPE_SERIALIZER.register(MineralWasherRecipe.RECIPE_GROUP, () -> new FluidItem2FluidRecipeSerializer<>(MineralWasherRecipe::new));

    // Fluid2Item
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> CHEMICAL_CRYSTALIZER_SERIALIZER = RECIPE_SERIALIZER.register(ChemicalCrystalizerRecipe.RECIPE_GROUP, () -> new Fluid2ItemRecipeSerializer<>(ChemicalCrystalizerRecipe::new));

    // Fluid2Gas
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> ELECTROLYTIC_SEPARATOR_SERIALIZER = RECIPE_SERIALIZER.register(ElectrolyticSeparatorRecipe.RECIPE_GROUP, () -> new Fluid2GasRecipeSerializer<>(ElectrolyticSeparatorRecipe::new));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> CHEMICAL_REACTOR_SERIALIZER = RECIPE_SERIALIZER.register(ChemicalReactorRecipe.RECIPE_GROUP, () -> new ChemicalReactorRecipeSerializer());

    //Fluid2Fluid

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> ELECTROLOSIS_CHAMBER_SERIALIZER = RECIPE_SERIALIZER.register(ElectrolosisChamberRecipe.RECIPE_GROUP,  () -> new Fluid2FluidRecipeSerializer<>(ElectrolosisChamberRecipe::new));

    /* Functional Methods */

    public static class CustomRecipeType<T extends Recipe<?>> implements RecipeType<T> {
        @Override
        public String toString() {
            return BuiltInRegistries.RECIPE_TYPE.getKey(this).toString();
        }
    }

}
