package electrodynamics.datagen;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import electrodynamics.api.References;
import electrodynamics.datagen.client.ElectrodynamicsBlockModelsProvider;
import electrodynamics.datagen.client.ElectrodynamicsBlockStateProvider;
import electrodynamics.datagen.client.ElectrodynamicsItemModelsProvider;
import electrodynamics.datagen.client.ElectrodynamicsLangKeyProvider;
import electrodynamics.datagen.client.ElectrodynamicsLangKeyProvider.Locale;
import electrodynamics.datagen.client.ElectrodynamicsSoundProvider;
import electrodynamics.datagen.server.CoalGeneratorFuelSourceProvider;
import electrodynamics.datagen.server.CombustionChamberFuelSourceProvider;
import electrodynamics.datagen.server.ElectrodynamicsLootTablesProvider;
import electrodynamics.datagen.server.ThermoelectricGenHeatSourceProvider;
import electrodynamics.datagen.server.advancement.ElectrodynamicsAdvancementProvider;
import electrodynamics.datagen.server.recipe.ElectrodynamicsRecipeProvider;
import electrodynamics.datagen.server.tags.ElectrodynamicsTagsProvider;
import electrodynamics.registers.ElectrodynamicsDamageTypes;
import electrodynamics.registers.ElectrodynamicsFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {

		DataGenerator generator = event.getGenerator();

		PackOutput output = generator.getPackOutput();

		ExistingFileHelper helper = event.getExistingFileHelper();

		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();


		if (event.includeServer()) {
			generator.addProvider(true, new LootTableProvider(output, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(ElectrodynamicsLootTablesProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

			generator.addProvider(true, new CombustionChamberFuelSourceProvider(output));
			generator.addProvider(true, new CoalGeneratorFuelSourceProvider(output));
			generator.addProvider(true, new ThermoelectricGenHeatSourceProvider(output));
			generator.addProvider(true, new ElectrodynamicsAdvancementProvider(output, lookupProvider));

			DatapackBuiltinEntriesProvider datapacks = new DatapackBuiltinEntriesProvider(output, lookupProvider, new RegistrySetBuilder()
					//
					.add(Registries.DAMAGE_TYPE, ElectrodynamicsDamageTypes::registerTypes)
					//
					.add(Registries.CONFIGURED_FEATURE, context -> ElectrodynamicsFeatures.registerConfigured(context))
					//
					.add(Registries.PLACED_FEATURE, ElectrodynamicsFeatures::registerPlaced)
					//
					.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ElectrodynamicsFeatures::registerModifiers)
			//
					, Set.of(References.ID));

			generator.addProvider(true, datapacks);
			ElectrodynamicsTagsProvider.addTagProviders(generator, output, datapacks.getRegistryProvider(), helper);
			generator.addProvider(true, new ElectrodynamicsRecipeProvider(output, lookupProvider));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new ElectrodynamicsBlockStateProvider(output, helper));
			generator.addProvider(true, new ElectrodynamicsBlockModelsProvider(output, helper));
			generator.addProvider(true, new ElectrodynamicsItemModelsProvider(output, helper));
			generator.addProvider(true, new ElectrodynamicsLangKeyProvider(output, Locale.EN_US));
			generator.addProvider(true, new ElectrodynamicsSoundProvider(output, helper));
		}
	}

}
