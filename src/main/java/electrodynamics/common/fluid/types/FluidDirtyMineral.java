package electrodynamics.common.fluid.types;

import electrodynamics.api.References;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.SimpleWaterBasedFluidType;
import electrodynamics.common.fluid.subtype.SubtypeDirtyMineralFluid;
import electrodynamics.registers.ElectrodynamicsItems;

public class FluidDirtyMineral extends FluidNonPlaceable {

    public SubtypeDirtyMineralFluid subtype;
    public FluidDirtyMineral(SubtypeDirtyMineralFluid subtype) {
        super(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluiddirtymineral" + subtype.name(), "dirty/dirtymineralfluid", subtype.color));
        this.subtype = subtype;
    }

    public SubtypeDirtyMineralFluid getSubtype() {
        return subtype;
    }
}
