package electrodynamics.common.fluid.subtype;

import java.util.function.Supplier;

import electrodynamics.api.ISubtype;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsFluids;
import net.minecraft.world.level.material.Fluid;

public enum SubtypeImpureMineralFluid implements ISubtype {
    copper(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.copper), Color.WHITE),
    tin(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.tin), Color.WHITE),
    silver(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.silver), Color.WHITE),
    lead(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.lead), Color.WHITE),
    vanadium(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.vanadium), Color.WHITE),
    iron(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.iron), Color.WHITE),
    gold(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.gold), Color.WHITE),
    lithium(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.lithium), Color.WHITE),
    molybdenum(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.molybdenum), Color.WHITE),
    netherite(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.netherite), Color.WHITE),
    aluminum(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.aluminum), Color.WHITE),
    titanium(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.titanium), Color.WHITE),
    chromium(() -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.chromium), Color.WHITE);

    public final Supplier<Fluid> result;
    public final Color color;

    SubtypeImpureMineralFluid(Supplier<Fluid> result, Color color) {
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
