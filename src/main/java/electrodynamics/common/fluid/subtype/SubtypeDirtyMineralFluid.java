package electrodynamics.common.fluid.subtype;

import java.util.function.Supplier;

import electrodynamics.api.ISubtype;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsFluids;
import net.minecraft.world.level.material.Fluid;

public enum SubtypeDirtyMineralFluid implements ISubtype {
    copper(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.copper), Color.WHITE),
    tin(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.tin), Color.WHITE),
    silver(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.silver), Color.WHITE),
    lead(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.lead), Color.WHITE),
    vanadium(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.vanadium), Color.WHITE),
    iron(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.iron), Color.WHITE),
    gold(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.gold), Color.WHITE),
    lithium(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.lithium), Color.WHITE),
    molybdenum(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.molybdenum), Color.WHITE),
    netherite(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.netherite), Color.WHITE),
    aluminum(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.aluminum), Color.WHITE),
    titanium(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.titanium), Color.WHITE),
    chromium(() -> ElectrodynamicsFluids.FLUIDS_IMPUREMINERAL.getValue(SubtypeImpureMineralFluid.chromium), Color.WHITE);

    public final Supplier<Fluid> result;
    public final Color color;

    SubtypeDirtyMineralFluid(Supplier<Fluid> result, Color color) {
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
