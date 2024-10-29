package electrodynamics.datagen.server.tags.types;

import java.util.concurrent.CompletableFuture;

import electrodynamics.api.References;
import electrodynamics.common.fluid.subtype.SubtypeSulfateFluid;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ElectrodynamicsFluidTagsProvider extends FluidTagsProvider {

	public ElectrodynamicsFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, References.ID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider pProvider) {
		tag(ElectrodynamicsTags.Fluids.CLAY).add(ElectrodynamicsFluids.FLUID_CLAY.get());
		tag(ElectrodynamicsTags.Fluids.ETHANOL).add(ElectrodynamicsFluids.FLUID_ETHANOL.get());
		tag(ElectrodynamicsTags.Fluids.HYDRAULIC_FLUID).add(ElectrodynamicsFluids.FLUID_HYDRAULIC.get());
		tag(ElectrodynamicsTags.Fluids.HYDROGEN).add(ElectrodynamicsFluids.FLUID_HYDROGEN.get());
		tag(ElectrodynamicsTags.Fluids.HYDROGEN_FLUORIDE).add(ElectrodynamicsFluids.FLUID_HYDROFLUORICACID.get());
		tag(ElectrodynamicsTags.Fluids.OXYGEN).add(ElectrodynamicsFluids.FLUID_OXYGEN.get());
		tag(ElectrodynamicsTags.Fluids.POLYETHLYENE).add(ElectrodynamicsFluids.FLUID_POLYETHYLENE.get());
		tag(ElectrodynamicsTags.Fluids.SULFURIC_ACID).add(ElectrodynamicsFluids.FLUID_SULFURICACID.get());
		for (SubtypeSulfateFluid sulfate : SubtypeSulfateFluid.values()) {
			tag(sulfate.tag).add(ElectrodynamicsFluids.SULFATE_FLUIDS.getValue(sulfate).get());
		}
	}

}
