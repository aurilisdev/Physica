package electrodynamics.registers;

import java.util.List;

import electrodynamics.Electrodynamics;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.settings.OreConfig;
import electrodynamics.common.world.ruletests.RuleTestOre;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ElectrodynamicsFeatures {

	/* CONFIGURED FEATURES */

	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ALUMINUM_CONFIGURED = configured("ore_aluminum");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CHROMIUM_CONFIGURED = configured("ore_chromium");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_FLUORITE_CONFIGURED = configured("ore_fluorite");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LEAD_CONFIGURED = configured("ore_lead");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LITHIUM_CONFIGURED = configured("ore_lithium");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MOLYBDENUM_CONFIGURED = configured("ore_molybdenum");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_MONAZITE_CONFIGURED = configured("ore_monazite");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NITER_CONFIGURED = configured("ore_niter");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SALT_CONFIGURED = configured("ore_salt");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SILVER_CONFIGURED = configured("ore_silver");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SULFUR_CONFIGURED = configured("ore_sulfur");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SYLVITE_CONFIGURED = configured("ore_sylvite");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_TIN_CONFIGURED = configured("ore_tin");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_TITANIUM_CONFIGURED = configured("ore_titanium");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_THORIUM_CONFIGURED = configured("ore_thorium");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_URANIUM_CONFIGURED = configured("ore_uranium");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_VANADIUM_CONFIGURED = configured("ore_vanadium");

	public static void registerConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context) {

		FeatureUtils.register(context, ORE_ALUMINUM_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.aluminum, SubtypeOreDeepslate.aluminum));
		FeatureUtils.register(context, ORE_CHROMIUM_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.chromium, SubtypeOreDeepslate.chromium));
		FeatureUtils.register(context, ORE_FLUORITE_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.fluorite, SubtypeOreDeepslate.fluorite));
		FeatureUtils.register(context, ORE_LEAD_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.lead, SubtypeOreDeepslate.lead));
		FeatureUtils.register(context, ORE_LITHIUM_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.lithium, SubtypeOreDeepslate.lithium));
		FeatureUtils.register(context, ORE_MOLYBDENUM_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.molybdenum, SubtypeOreDeepslate.molybdenum));
		FeatureUtils.register(context, ORE_MONAZITE_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.monazite, SubtypeOreDeepslate.monazite));
		FeatureUtils.register(context, ORE_NITER_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.niter, SubtypeOreDeepslate.niter));
		FeatureUtils.register(context, ORE_SALT_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.salt, SubtypeOreDeepslate.salt));
		FeatureUtils.register(context, ORE_SILVER_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.silver, SubtypeOreDeepslate.silver));
		FeatureUtils.register(context, ORE_SULFUR_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.sulfur, SubtypeOreDeepslate.sulfur));
		FeatureUtils.register(context, ORE_SYLVITE_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.sylvite, SubtypeOreDeepslate.sylvite));
		FeatureUtils.register(context, ORE_TIN_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.tin, SubtypeOreDeepslate.tin));
		FeatureUtils.register(context, ORE_TITANIUM_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.titanium, SubtypeOreDeepslate.titanium));
		FeatureUtils.register(context, ORE_THORIUM_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.thorium, SubtypeOreDeepslate.thorium));
		FeatureUtils.register(context, ORE_URANIUM_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.uranium, SubtypeOreDeepslate.uranium));
		FeatureUtils.register(context, ORE_VANADIUM_CONFIGURED, Feature.ORE, dualOre(SubtypeOre.vanadium, SubtypeOreDeepslate.vanadium));

	}

	private static ResourceKey<ConfiguredFeature<?, ?>> configured(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, Electrodynamics.rl(name));
	}

	private static OreConfiguration dualOre(SubtypeOre ore, SubtypeOreDeepslate deepOre) {
		return new OreConfiguration(List.of(OreConfiguration.target(new RuleTestOre(ore, null, BlockTags.STONE_ORE_REPLACEABLES), ElectrodynamicsBlocks.BLOCKS_ORE.getValue(ore).defaultBlockState()), OreConfiguration.target(new RuleTestOre(null, deepOre, BlockTags.DEEPSLATE_ORE_REPLACEABLES), ElectrodynamicsBlocks.BLOCKS_DEEPSLATEORE.getValue(deepOre).defaultBlockState())), ore.veinSize);
	}

	/* PLACED FEATURES */

	public static final ResourceKey<PlacedFeature> ORE_ALUMINUM_PLACED = placed("ore_aluminum");
	public static final ResourceKey<PlacedFeature> ORE_CHROMIUM_PLACED = placed("ore_chromium");
	public static final ResourceKey<PlacedFeature> ORE_FLUORITE_PLACED = placed("ore_fluorite");
	public static final ResourceKey<PlacedFeature> ORE_LEAD_PLACED = placed("ore_lead");
	public static final ResourceKey<PlacedFeature> ORE_LITHIUM_PLACED = placed("ore_lithium");
	public static final ResourceKey<PlacedFeature> ORE_MOLYBDENUM_PLACED = placed("ore_molybdenum");
	public static final ResourceKey<PlacedFeature> ORE_MONAZITE_PLACED = placed("ore_monazite");
	public static final ResourceKey<PlacedFeature> ORE_NITER_PLACED = placed("ore_niter");
	public static final ResourceKey<PlacedFeature> ORE_SALT_PLACED = placed("ore_salt");
	public static final ResourceKey<PlacedFeature> ORE_SILVER_PLACED = placed("ore_silver");
	public static final ResourceKey<PlacedFeature> ORE_SULFUR_PLACED = placed("ore_sulfur");
	public static final ResourceKey<PlacedFeature> ORE_SYLVITE_PLACED = placed("ore_sylvite");
	public static final ResourceKey<PlacedFeature> ORE_TIN_PLACED = placed("ore_tin");
	public static final ResourceKey<PlacedFeature> ORE_TITANIUM_PLACED = placed("ore_titanium");
	public static final ResourceKey<PlacedFeature> ORE_THORIUM_PLACED = placed("ore_thorium");
	public static final ResourceKey<PlacedFeature> ORE_URANIUM_PLACED = placed("ore_uranium");
	public static final ResourceKey<PlacedFeature> ORE_VANADIUM_PLACED = placed("ore_vanadium");

	public static void registerPlaced(BootstrapContext<PlacedFeature> context) {

		HolderGetter<ConfiguredFeature<?, ?>> holderGetter = context.lookup(Registries.CONFIGURED_FEATURE);

		PlacementUtils.register(context, ORE_ALUMINUM_PLACED, holderGetter.getOrThrow(ORE_ALUMINUM_CONFIGURED), orePlacement((int) (SubtypeOre.aluminum.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.aluminum.minY, SubtypeOre.aluminum.maxY));
		PlacementUtils.register(context, ORE_CHROMIUM_PLACED, holderGetter.getOrThrow(ORE_CHROMIUM_CONFIGURED), orePlacement((int) (SubtypeOre.chromium.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.chromium.minY, SubtypeOre.chromium.maxY));
		PlacementUtils.register(context, ORE_FLUORITE_PLACED, holderGetter.getOrThrow(ORE_FLUORITE_CONFIGURED), orePlacement((int) (SubtypeOre.fluorite.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.fluorite.minY, SubtypeOre.fluorite.maxY));
		PlacementUtils.register(context, ORE_LEAD_PLACED, holderGetter.getOrThrow(ORE_LEAD_CONFIGURED), orePlacement((int) (SubtypeOre.lead.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.lead.minY, SubtypeOre.lead.maxY));
		PlacementUtils.register(context, ORE_LITHIUM_PLACED, holderGetter.getOrThrow(ORE_LITHIUM_CONFIGURED), orePlacement((int) (SubtypeOre.lithium.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.lithium.minY, SubtypeOre.lithium.maxY));
		PlacementUtils.register(context, ORE_MOLYBDENUM_PLACED, holderGetter.getOrThrow(ORE_MOLYBDENUM_CONFIGURED), orePlacement((int) (SubtypeOre.molybdenum.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.molybdenum.minY, SubtypeOre.molybdenum.maxY));
		PlacementUtils.register(context, ORE_MONAZITE_PLACED, holderGetter.getOrThrow(ORE_MONAZITE_CONFIGURED), orePlacement((int) (SubtypeOre.monazite.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.monazite.minY, SubtypeOre.monazite.maxY));
		PlacementUtils.register(context, ORE_NITER_PLACED, holderGetter.getOrThrow(ORE_NITER_CONFIGURED), orePlacement((int) (SubtypeOre.niter.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.niter.minY, SubtypeOre.niter.maxY));
		PlacementUtils.register(context, ORE_SALT_PLACED, holderGetter.getOrThrow(ORE_SALT_CONFIGURED), orePlacement((int) (SubtypeOre.salt.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.salt.minY, SubtypeOre.salt.maxY));
		PlacementUtils.register(context, ORE_SILVER_PLACED, holderGetter.getOrThrow(ORE_SILVER_CONFIGURED), orePlacement((int) (SubtypeOre.silver.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.silver.minY, SubtypeOre.silver.maxY));
		PlacementUtils.register(context, ORE_SULFUR_PLACED, holderGetter.getOrThrow(ORE_SULFUR_CONFIGURED), orePlacement((int) (SubtypeOre.sulfur.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.sulfur.minY, SubtypeOre.sulfur.maxY));
		PlacementUtils.register(context, ORE_SYLVITE_PLACED, holderGetter.getOrThrow(ORE_SYLVITE_CONFIGURED), orePlacement((int) (SubtypeOre.sylvite.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.sylvite.minY, SubtypeOre.sylvite.maxY));
		PlacementUtils.register(context, ORE_TIN_PLACED, holderGetter.getOrThrow(ORE_TIN_CONFIGURED), orePlacement((int) (SubtypeOre.tin.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.tin.minY, SubtypeOre.tin.maxY));
		PlacementUtils.register(context, ORE_TITANIUM_PLACED, holderGetter.getOrThrow(ORE_TITANIUM_CONFIGURED), orePlacement((int) (SubtypeOre.titanium.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.titanium.minY, SubtypeOre.titanium.maxY));
		PlacementUtils.register(context, ORE_THORIUM_PLACED, holderGetter.getOrThrow(ORE_THORIUM_CONFIGURED), orePlacement((int) (SubtypeOre.thorium.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.thorium.minY, SubtypeOre.thorium.maxY));
		PlacementUtils.register(context, ORE_URANIUM_PLACED, holderGetter.getOrThrow(ORE_URANIUM_CONFIGURED), orePlacement((int) (SubtypeOre.uranium.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.uranium.minY, SubtypeOre.uranium.maxY));
		PlacementUtils.register(context, ORE_VANADIUM_PLACED, holderGetter.getOrThrow(ORE_VANADIUM_CONFIGURED), orePlacement((int) (SubtypeOre.vanadium.veinsPerChunk * OreConfig.ORE_GENERATION_MULTIPLIER), SubtypeOre.vanadium.minY, SubtypeOre.vanadium.maxY));

	}

	private static ResourceKey<PlacedFeature> placed(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, Electrodynamics.rl(name));
	}

	private static List<PlacementModifier> orePlacement(int count, int minY, int maxY) {
		return List.of(CountPlacement.of(count), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY)), BiomeFilter.biome());
	}

	/* BIOME MODIFIER */

	public static final ResourceKey<BiomeModifier> ORE_ALUMINUM_MODIFIER = modifier("ore_aluminum");
	public static final ResourceKey<BiomeModifier> ORE_CHROMIUM_MODIFIER = modifier("ore_chromium");
	public static final ResourceKey<BiomeModifier> ORE_FLUORITE_MODIFIER = modifier("ore_fluorite");
	public static final ResourceKey<BiomeModifier> ORE_LEAD_MODIFIER = modifier("ore_lead");
	public static final ResourceKey<BiomeModifier> ORE_LITHIUM_MODIFIER = modifier("ore_lithium");
	public static final ResourceKey<BiomeModifier> ORE_MOLYBDENUM_MODIFIER = modifier("ore_molybdenum");
	public static final ResourceKey<BiomeModifier> ORE_MONAZITE_MODIFIER = modifier("ore_monazite");
	public static final ResourceKey<BiomeModifier> ORE_NITER_MODIFIER = modifier("ore_niter");
	public static final ResourceKey<BiomeModifier> ORE_SALT_MODIFIER = modifier("ore_salt");
	public static final ResourceKey<BiomeModifier> ORE_SILVER_MODIFIER = modifier("ore_silver");
	public static final ResourceKey<BiomeModifier> ORE_SULFUR_MODIFIER = modifier("ore_sulfur");
	public static final ResourceKey<BiomeModifier> ORE_SYLVITE_MODIFIER = modifier("ore_sylvite");
	public static final ResourceKey<BiomeModifier> ORE_TIN_MODIFIER = modifier("ore_tin");
	public static final ResourceKey<BiomeModifier> ORE_TITANIUM_MODIFIER = modifier("ore_titanium");
	public static final ResourceKey<BiomeModifier> ORE_THORIUM_MODIFIER = modifier("ore_thorium");
	public static final ResourceKey<BiomeModifier> ORE_URANIUM_MODIFIER = modifier("ore_uranium");
	public static final ResourceKey<BiomeModifier> ORE_VANADIUM_MODIFIER = modifier("ore_vanadium");

	public static void registerModifiers(BootstrapContext<BiomeModifier> context) {

		HolderGetter<Biome> biomeLookup = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placedLookup = context.lookup(Registries.PLACED_FEATURE);

		Named<Biome> overworld = biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD);

		context.register(ORE_ALUMINUM_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_ALUMINUM_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_CHROMIUM_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_CHROMIUM_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_FLUORITE_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_FLUORITE_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_LEAD_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_LEAD_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_LITHIUM_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_LITHIUM_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_MOLYBDENUM_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_MOLYBDENUM_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_MONAZITE_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_MONAZITE_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_NITER_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_NITER_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_SALT_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_SALT_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_SILVER_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_SILVER_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_SULFUR_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_SULFUR_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_SYLVITE_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_SYLVITE_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_TIN_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_TIN_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_TITANIUM_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_TITANIUM_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_THORIUM_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_THORIUM_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_URANIUM_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_URANIUM_PLACED)), Decoration.UNDERGROUND_ORES));
		context.register(ORE_VANADIUM_MODIFIER, new AddFeaturesBiomeModifier(overworld, HolderSet.direct(placedLookup.getOrThrow(ORE_VANADIUM_PLACED)), Decoration.UNDERGROUND_ORES));
	}

	private static ResourceKey<BiomeModifier> modifier(String name) {
		return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Electrodynamics.rl(name));
	}

}
