package electrodynamics.datagen.server.tags;

import java.util.concurrent.CompletableFuture;

import electrodynamics.datagen.server.tags.types.ElectrodynamicsBlockTagsProvider;
import electrodynamics.datagen.server.tags.types.ElectrodynamicsDamageTagsProvider;
import electrodynamics.datagen.server.tags.types.ElectrodynamicsEnchantmentTagsProvider;
import electrodynamics.datagen.server.tags.types.ElectrodynamicsFluidTagsProvider;
import electrodynamics.datagen.server.tags.types.ElectrodynamicsGasTagsProvider;
import electrodynamics.datagen.server.tags.types.ElectrodynamicsItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ElectrodynamicsTagsProvider {

	public static void addTagProviders(DataGenerator generator, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
		ElectrodynamicsBlockTagsProvider blockProvider = new ElectrodynamicsBlockTagsProvider(output, lookupProvider, helper);
		generator.addProvider(true, blockProvider);
		generator.addProvider(true, new ElectrodynamicsItemTagsProvider(output, lookupProvider, blockProvider, helper));
		generator.addProvider(true, new ElectrodynamicsFluidTagsProvider(output, lookupProvider, helper));
		generator.addProvider(true, new ElectrodynamicsGasTagsProvider(output, lookupProvider, helper));
		generator.addProvider(true, new ElectrodynamicsDamageTagsProvider(output, lookupProvider, helper));
		generator.addProvider(true, new ElectrodynamicsEnchantmentTagsProvider(output, lookupProvider, helper));
	}

}
