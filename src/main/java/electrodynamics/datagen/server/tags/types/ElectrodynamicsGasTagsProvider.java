package electrodynamics.datagen.server.tags.types;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import electrodynamics.api.References;
import electrodynamics.api.gas.Gas;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ElectrodynamicsGasTagsProvider extends IntrinsicHolderTagsProvider<Gas> {

	public ElectrodynamicsGasTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, ElectrodynamicsGases.GAS_REGISTRY_KEY, lookupProvider, gas -> ResourceKey.create(ElectrodynamicsGases.GAS_REGISTRY_KEY, ElectrodynamicsGases.GAS_REGISTRY.getKey(gas)), modId, existingFileHelper);
	}

	public ElectrodynamicsGasTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		this(output, lookupProvider, References.ID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider pProvider) {
		tag(ElectrodynamicsTags.Gases.HYDROGEN).add(ElectrodynamicsGases.HYDROGEN.value());
		tag(ElectrodynamicsTags.Gases.OXYGEN).add(ElectrodynamicsGases.OXYGEN.value());
		tag(ElectrodynamicsTags.Gases.STEAM).add(ElectrodynamicsGases.STEAM.value());
		tag(ElectrodynamicsTags.Gases.NITROGEN).add(ElectrodynamicsGases.NITROGEN.value());
		tag(ElectrodynamicsTags.Gases.ARGON).add(ElectrodynamicsGases.ARGON.value());
		tag(ElectrodynamicsTags.Gases.CARBON_DIOXIDE).add(ElectrodynamicsGases.CARBON_DIOXIDE.value());
		tag(ElectrodynamicsTags.Gases.SULFUR_DIOXIDE).add(ElectrodynamicsGases.SULFUR_DIOXIDE.value());
		tag(ElectrodynamicsTags.Gases.AMMONIA).add(ElectrodynamicsGases.AMMONIA.value());

		tag(ElectrodynamicsTags.Gases.IS_CORROSIVE).add(ElectrodynamicsGases.SULFUR_DIOXIDE.value());
	}

}
