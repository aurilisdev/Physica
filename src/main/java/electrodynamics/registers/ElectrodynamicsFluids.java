package electrodynamics.registers;

import electrodynamics.api.References;
import electrodynamics.api.registration.BulkDeferredHolder;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.SimpleWaterBasedFluidType;
import electrodynamics.common.fluid.subtype.SubtypeSulfateFluid;
import electrodynamics.common.fluid.types.FluidSulfate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsFluids {
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, References.ID);

	//
	public static final DeferredHolder<Fluid, Fluid> FLUID_CLAY = FLUIDS.register("fluidclay", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidclay", "clay")));
	public static final DeferredHolder<Fluid, Fluid> FLUID_ETHANOL = FLUIDS.register("fluidethanol", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidethanol", "ethanol", -428574419)));
	public static final DeferredHolder<Fluid,Fluid> FLUID_HYDRAULIC = FLUIDS.register("fluidhydraulic", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidhydraulic", "hydraulic")));;
	public static final DeferredHolder<Fluid, Fluid> FLUID_HYDROFLUORICACID = FLUIDS.register("fluidhydrofluoricacid", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidhydrofluoricacid", "hydrofluoricacid", -375879936)));
	public static final DeferredHolder<Fluid, Fluid> FLUID_HYDROGEN = FLUIDS.register("fluidhydrogen", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidhydrogen", "hydrogen")));
	public static final DeferredHolder<Fluid, Fluid> FLUID_OXYGEN = FLUIDS.register("fluidoxygen", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidoxygen", "oxygen")));
	public static final DeferredHolder<Fluid, Fluid> FLUID_POLYETHYLENE = FLUIDS.register("fluidpolyethylene", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidpolyethylene", "polyethylene", -376664948)));
	public static final DeferredHolder<Fluid, Fluid> FLUID_SULFURICACID = FLUIDS.register("fluidsulfuricacid", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidsulfuricacid", "sulfuricacid", -375879936)));

	public static final BulkDeferredHolder<Fluid, Fluid, SubtypeSulfateFluid> FLUIDS_SULFATE = new BulkDeferredHolder<>(SubtypeSulfateFluid.values(), subtype -> FLUIDS.register("fluidsulfate" + subtype.name(), () -> new FluidSulfate(subtype)));

}
