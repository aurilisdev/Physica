package electrodynamics.registers;

import electrodynamics.api.References;
import electrodynamics.api.registration.BulkDeferredHolder;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.SimpleWaterBasedFluidType;
import electrodynamics.common.fluid.subtype.SubtypeCrudeMineralFluid;
import electrodynamics.common.fluid.subtype.SubtypeDirtyMineralFluid;
import electrodynamics.common.fluid.subtype.SubtypeImpureMineralFluid;
import electrodynamics.common.fluid.subtype.SubtypePureMineralFluid;
import electrodynamics.common.fluid.subtype.SubtypeRoyalMineralFluid;
import electrodynamics.common.fluid.subtype.SubtypeSulfateFluid;
import electrodynamics.common.fluid.types.FluidCrudeMineral;
import electrodynamics.common.fluid.types.FluidDirtyMineral;
import electrodynamics.common.fluid.types.FluidImpureMineral;
import electrodynamics.common.fluid.types.FluidPureMineral;
import electrodynamics.common.fluid.types.FluidRoyalMineral;
import electrodynamics.common.fluid.types.FluidSulfate;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsFluids {
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, References.ID);


	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_AMMONIA = FLUIDS.register("fluidammonia", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidammonia", "ammonia", Color.WHITE)));//new Color(255, 242, 252, 255)
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_AQUAREGIA = FLUIDS.register("fluidaquaregia", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidaquaregia", "aquaregia", Color.WHITE))); //new Color(104, 112, 255 ,255)
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_CLAY = FLUIDS.register("fluidclay", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidclay", "clay", Color.WHITE))); //new Color(105, 110, 121, 255)
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_ETHANOL = FLUIDS.register("fluidethanol", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidethanol", "ethanol", new Color(116, 121, 45, 230))));
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_HYDRAULIC = FLUIDS.register("fluidhydraulic", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidhydraulic", "hydraulic", Color.WHITE))); //new Color(202, 191, 11, 255)
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_HYDROFLUORICACID = FLUIDS.register("fluidhydrofluoricacid", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidhydrofluoricacid", "hydrofluoricacid", new Color(152, 135, 0, 233))));
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_HYDROGEN = FLUIDS.register("fluidhydrogen", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidhydrogen", "hydrogen", Color.WHITE)));
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_OXYGEN = FLUIDS.register("fluidoxygen", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidoxygen", "oxygen", new Color(139, 203, 239, 255))));
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_POLYETHYLENE = FLUIDS.register("fluidpolyethylene", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidpolyethylene", "polyethylene", new Color(140, 140, 140, 233))));
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_SULFURICACID = FLUIDS.register("fluidsulfuricacid", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidsulfuricacid", "sulfuricacid", new Color(152, 135, 0, 233))));
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_NITRICACID = FLUIDS.register("fluidnitricacid", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidnitricacid", "nitricacid", Color.WHITE))); //new Color(152, 70, 0, 255)
	public static final DeferredHolder<Fluid, FluidNonPlaceable> FLUID_HYDROCHLORICACID = FLUIDS.register("fluidhydrochloricacid", () -> new FluidNonPlaceable(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, new SimpleWaterBasedFluidType(References.ID, "fluidhydrochloricacid", "hydrochloricacid", Color.WHITE))); //new Color(152, 241, 0, 255)

	public static final BulkDeferredHolder<Fluid, FluidSulfate, SubtypeSulfateFluid> FLUIDS_SULFATE = new BulkDeferredHolder<>(SubtypeSulfateFluid.values(), subtype -> FLUIDS.register("fluidsulfate" + subtype.name(), () -> new FluidSulfate(subtype)));
	public static final BulkDeferredHolder<Fluid, FluidPureMineral, SubtypePureMineralFluid> FLUIDS_PUREMINERAL = new BulkDeferredHolder<>(SubtypePureMineralFluid.values(), subtype -> FLUIDS.register("fluidpuremineral" + subtype.name(), () -> new FluidPureMineral(subtype)));
	public static final BulkDeferredHolder<Fluid, FluidImpureMineral, SubtypeImpureMineralFluid> FLUIDS_IMPUREMINERAL = new BulkDeferredHolder<>(SubtypeImpureMineralFluid.values(), subtype -> FLUIDS.register("fluidimpuremineral" + subtype.name(), () -> new FluidImpureMineral(subtype)));
	public static final BulkDeferredHolder<Fluid, FluidDirtyMineral, SubtypeDirtyMineralFluid> FLUIDS_DIRTYMINERAL = new BulkDeferredHolder<>(SubtypeDirtyMineralFluid.values(), subtype -> FLUIDS.register("fluiddirtymineral" + subtype.name(), () -> new FluidDirtyMineral(subtype)));
	public static final BulkDeferredHolder<Fluid, FluidCrudeMineral, SubtypeCrudeMineralFluid> FLUIDS_CRUDEMINERAL = new BulkDeferredHolder<>(SubtypeCrudeMineralFluid.values(), subtype -> FLUIDS.register("fluidcrudemineral" + subtype.name(), () -> new FluidCrudeMineral(subtype)));
	public static final BulkDeferredHolder<Fluid, FluidRoyalMineral, SubtypeRoyalMineralFluid> FLUIDS_ROYALMINERAL = new BulkDeferredHolder<>(SubtypeRoyalMineralFluid.values(), subtype -> FLUIDS.register("fluidroyalmineral" + subtype.name(), () -> new FluidRoyalMineral(subtype)));

}
