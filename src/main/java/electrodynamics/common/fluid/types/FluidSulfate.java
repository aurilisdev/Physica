package electrodynamics.common.fluid.types;

import electrodynamics.api.References;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.SimpleWaterBasedFluidType;
import electrodynamics.common.fluid.subtype.SubtypeSulfateFluid;
import electrodynamics.registers.ElectrodynamicsItems;

public class FluidSulfate extends FluidNonPlaceable {

    public SubtypeSulfateFluid subtype;
    public FluidSulfate(SubtypeSulfateFluid subtype) {
        super(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidsulfate" + subtype.name(), "sulfate/" + subtype.name(), subtype.color));
        this.subtype = subtype;
    }

    public SubtypeSulfateFluid getSubtype() {
        return subtype;
    }
}
