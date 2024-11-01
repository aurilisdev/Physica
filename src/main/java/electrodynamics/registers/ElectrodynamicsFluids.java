package electrodynamics.registers;

import electrodynamics.api.References;
import electrodynamics.api.registration.BulkDeferredHolder;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.SimpleWaterBasedFluidType;
import electrodynamics.common.fluid.subtype.SubtypePureMineralFluid;
import electrodynamics.common.fluid.subtype.SubtypeSulfateFluid;
import electrodynamics.common.fluid.types.FluidPureMineral;
import electrodynamics.common.fluid.types.FluidSulfate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsFluids {
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, References.ID);

	//
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_CLAY = FLUIDS.register("fluidclay", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidclay", "clay")));
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_ETHANOL = FLUIDS.register("fluidethanol", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidethanol", "ethanol", -428574419)));
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_HYDRAULIC = FLUIDS.register("fluidhydraulic", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidhydraulic", "hydraulic")));;
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_HYDROFLUORICACID = FLUIDS.register("fluidhydrofluoricacid", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidhydrofluoricacid", "hydrofluoricacid", -375879936)));
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_HYDROGEN = FLUIDS.register("fluidhydrogen", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidhydrogen", "hydrogen")));
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_OXYGEN = FLUIDS.register("fluidoxygen", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidoxygen", "oxygen")));
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_POLYETHYLENE = FLUIDS.register("fluidpolyethylene", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidpolyethylene", "polyethylene", -376664948)));
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_SULFURICACID = FLUIDS.register("fluidsulfuricacid", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidsulfuricacid", "sulfuricacid", -375879936)));

	public static final BulkDeferredHolder<Fluid, FluidSulfate, SubtypeSulfateFluid> FLUIDS_SULFATE = new BulkDeferredHolder<>(SubtypeSulfateFluid.values(), subtype -> FLUIDS.register("fluidsulfate" + subtype.name(), () -> new FluidSulfate(subtype)));
	public static final BulkDeferredHolder<Fluid, FluidPureMineral, SubtypePureMineralFluid> FLUIDS_PUREMINERAL = new BulkDeferredHolder<>(SubtypePureMineralFluid.values(), subtype -> FLUIDS.register("fluidpuremineral" + subtype.name(), () -> new FluidPureMineral(subtype)));

}
