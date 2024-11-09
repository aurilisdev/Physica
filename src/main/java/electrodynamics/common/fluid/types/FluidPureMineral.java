package electrodynamics.common.fluid.types;

import electrodynamics.api.References;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.SimpleWaterBasedFluidType;
import electrodynamics.common.fluid.subtype.SubtypePureMineralFluid;
import electrodynamics.registers.ElectrodynamicsItems;

public class FluidPureMineral extends FluidNonPlaceable {

    public SubtypePureMineralFluid subtype;
    public FluidPureMineral(SubtypePureMineralFluid subtype) {
        super(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidpuremineral" + subtype.name(), "pure/" + subtype.name(), subtype.color));
        this.subtype = subtype;
    }

    public SubtypePureMineralFluid getSubtype() {
        return subtype;
    }
}
