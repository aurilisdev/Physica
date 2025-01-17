package electrodynamics.compatibility.mekanism;

import mekanism.api.chemical.ChemicalStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface ChemicalStackSupplier {

    public ChemicalStack getChemical();

}
