package electrodynamics.common.fluid.types;

import electrodynamics.api.References;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.SimpleWaterBasedFluidType;
import electrodynamics.common.fluid.subtype.SubtypeImpureMineralFluid;
import electrodynamics.registers.ElectrodynamicsItems;

public class FluidImpureMineral extends FluidNonPlaceable {

    public SubtypeImpureMineralFluid subtype;
    public FluidImpureMineral(SubtypeImpureMineralFluid subtype) {
        super(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidimpuremineral" + subtype.name(), "impure/impuremineralfluid", subtype.color));
        this.subtype = subtype;
    }

    public SubtypeImpureMineralFluid getSubtype() {
        return subtype;
    }
}
