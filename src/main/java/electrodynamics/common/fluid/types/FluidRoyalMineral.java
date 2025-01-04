package electrodynamics.common.fluid.types;

import electrodynamics.api.References;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.SimpleWaterBasedFluidType;
import electrodynamics.common.fluid.subtype.SubtypeRoyalMineralFluid;
import electrodynamics.registers.ElectrodynamicsItems;

public class FluidRoyalMineral extends FluidNonPlaceable {

    public SubtypeRoyalMineralFluid subtype;
    public FluidRoyalMineral(SubtypeRoyalMineralFluid subtype) {
        super(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidroyalmineral" + subtype.name(), "royal/royalmineralfluid", subtype.color));
        this.subtype = subtype;
    }

    public SubtypeRoyalMineralFluid getSubtype() {
        return subtype;
    }
}
