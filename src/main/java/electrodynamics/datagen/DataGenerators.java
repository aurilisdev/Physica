package electrodynamics.datagen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;

import electrodynamics.api.References;
import electrodynamics.api.network.cable.type.IWire;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.datagen.client.ElectrodynamicsBlockModelsProvider;
import electrodynamics.datagen.client.ElectrodynamicsBlockStateProvider;
import electrodynamics.datagen.client.ElectrodynamicsItemModelsProvider;
import electrodynamics.datagen.client.ElectrodynamicsLangKeyProvider;
import electrodynamics.datagen.client.ElectrodynamicsLangKeyProvider.Locale;
import electrodynamics.datagen.client.ElectrodynamicsSoundProvider;
import electrodynamics.datagen.server.CoalGeneratorFuelSourceProvider;
import electrodynamics.datagen.server.CombustionChamberFuelSourceProvider;
import electrodynamics.datagen.server.ElectrodynamicsLootTablesProvider;
import electrodynamics.datagen.server.GasCollectorChromoCardsProvider;
import electrodynamics.datagen.server.ThermoelectricGenHeatSourceProvider;
import electrodynamics.datagen.server.advancement.ElectrodynamicsAdvancementProvider;
import electrodynamics.datagen.server.multiblock.ElectrodynamicsMultiblockProvider;
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

    public static final HashMap<IWire.IWireClass, HashSet<SubtypeWire>> WIRES = new HashMap<>();

    static {
        for (SubtypeWire wire : SubtypeWire.values()) {
            HashSet<SubtypeWire> wireSet = WIRES.getOrDefault(wire.getWireClass(), new HashSet<>());
            wireSet.add(wire);
            WIRES.put(wire.getWireClass(), wireSet);
        }
    }

    @Nullable
    public static SubtypeWire getWire(IWire.IWireMaterial conductor, SubtypeWire.InsulationMaterial insulation, SubtypeWire.WireClass wireClass, SubtypeWire.WireColor color) {

        for (SubtypeWire wire : WIRES.getOrDefault(wireClass, new HashSet<>())) {
            if (wire.getWireMaterial() == conductor && wire.getInsulation() == insulation && wire.getWireClass() == wireClass && wire.getWireColor() == color) {
                return wire;
            }
        }
        return null;
    }

    public static SubtypeWire[] getWires(IWire.IWireMaterial[] conductors, SubtypeWire.InsulationMaterial insulation, SubtypeWire.WireClass wireClass, SubtypeWire.WireColor... colors) {

        List<SubtypeWire> list = new ArrayList<>();

        SubtypeWire wire;
        for (IWire.IWireMaterial conductor : conductors) {
            for (SubtypeWire.WireColor color : colors) {
                wire = getWire(conductor, insulation, wireClass, color);
                if (wire != null) {
                    list.add(wire);
                }
            }
        }

        return list.toArray(new SubtypeWire[0]);
    }


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
            generator.addProvider(true, new GasCollectorChromoCardsProvider(output));

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
            generator.addProvider(true, new ElectrodynamicsAdvancementProvider(output, datapacks.getRegistryProvider()));
            generator.addProvider(true, new ElectrodynamicsMultiblockProvider(output, datapacks.getRegistryProvider(), helper));
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
