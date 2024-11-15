package electrodynamics.common.fluid.types;

import electrodynamics.api.References;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.SimpleWaterBasedFluidType;
import electrodynamics.common.fluid.subtype.SubtypeCrudeMineralFluid;
import electrodynamics.registers.ElectrodynamicsItems;

public class FluidCrudeMineral extends FluidNonPlaceable {

    public SubtypeCrudeMineralFluid subtype;
    public FluidCrudeMineral(SubtypeCrudeMineralFluid subtype) {
        super(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidcrudemineral" + subtype.name(), "crude/crudemineralfluid", subtype.color));
        this.subtype = subtype;
    }

    public SubtypeCrudeMineralFluid getSubtype() {
        return subtype;
    }

}
