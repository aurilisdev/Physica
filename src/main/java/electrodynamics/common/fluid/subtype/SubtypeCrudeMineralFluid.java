package electrodynamics.common.fluid.subtype;

import java.util.function.Supplier;

import electrodynamics.api.ISubtype;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsFluids;
import net.minecraft.world.level.material.Fluid;

public enum SubtypeCrudeMineralFluid implements ISubtype {
    copper(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.copper), Color.WHITE),
    tin(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.tin), Color.WHITE),
    silver(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.silver), Color.WHITE),
    lead(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.lead), Color.WHITE),
    vanadium(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.vanadium), Color.WHITE),
    iron(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.iron), Color.WHITE),
    gold(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.gold), Color.WHITE),
    lithium(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.lithium), Color.WHITE),
    molybdenum(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.molybdenum), Color.WHITE),
    netherite(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.netherite), Color.WHITE),
    aluminum(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.aluminum), Color.WHITE),
    titanium(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.titanium), Color.WHITE),
    chromium(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.chromium), Color.WHITE);

    public final Supplier<Fluid> result;
    public final Color color;

    SubtypeCrudeMineralFluid(Supplier<Fluid> result, Color color) {
        this.result = result;
        this.color = color;
    }

    @Override
    public String tag() {
        return "fluid" + name();
    }

    @Override
    public String forgeTag() {
        return "fluid/" + name();
    }

    @Override
    public boolean isItem() {
        return false;
    }
}
