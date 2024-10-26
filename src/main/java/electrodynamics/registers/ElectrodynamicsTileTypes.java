package electrodynamics.registers;

import static electrodynamics.registers.ElectrodynamicsBlocks.getBlock;

import com.google.common.collect.Sets;

import electrodynamics.api.References;
import electrodynamics.common.block.connect.BlockFluidPipe;
import electrodynamics.common.block.connect.BlockGasPipe;
import electrodynamics.common.block.connect.BlockLogisticalWire;
import electrodynamics.common.block.connect.BlockWire;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.common.tile.electricitygrid.TileCircuitBreaker;
import electrodynamics.common.tile.electricitygrid.TileCircuitMonitor;
import electrodynamics.common.tile.electricitygrid.TileCurrentRegulator;
import electrodynamics.common.tile.electricitygrid.TileLogisticalWire;
import electrodynamics.common.tile.electricitygrid.TileMultimeterBlock;
import electrodynamics.common.tile.electricitygrid.TilePotentiometer;
import electrodynamics.common.tile.electricitygrid.TileRelay;
import electrodynamics.common.tile.electricitygrid.TileWire;
import electrodynamics.common.tile.electricitygrid.batteries.TileBatteryBox;
import electrodynamics.common.tile.electricitygrid.batteries.TileCarbyneBatteryBox;
import electrodynamics.common.tile.electricitygrid.batteries.TileLithiumBatteryBox;
import electrodynamics.common.tile.electricitygrid.generators.TileAdvancedSolarPanel;
import electrodynamics.common.tile.electricitygrid.generators.TileCoalGenerator;
import electrodynamics.common.tile.electricitygrid.generators.TileCombustionChamber;
import electrodynamics.common.tile.electricitygrid.generators.TileCreativePowerSource;
import electrodynamics.common.tile.electricitygrid.generators.TileHydroelectricGenerator;
import electrodynamics.common.tile.electricitygrid.generators.TileSolarPanel;
import electrodynamics.common.tile.electricitygrid.generators.TileThermoelectricGenerator;
import electrodynamics.common.tile.electricitygrid.generators.TileWindmill;
import electrodynamics.common.tile.electricitygrid.transformer.TileAdvancedTransformer.TileAdvancedDowngradeTransformer;
import electrodynamics.common.tile.electricitygrid.transformer.TileAdvancedTransformer.TileAdvancedUpgradeTransformer;
import electrodynamics.common.tile.electricitygrid.transformer.TileGenericTransformer.TileDowngradeTransformer;
import electrodynamics.common.tile.electricitygrid.transformer.TileGenericTransformer.TileUpgradeTransformer;
import electrodynamics.common.tile.machines.TileChemicalCrystallizer;
import electrodynamics.common.tile.machines.TileChemicalMixer;
import electrodynamics.common.tile.machines.TileElectrolyticSeparator;
import electrodynamics.common.tile.machines.TileEnergizedAlloyer;
import electrodynamics.common.tile.machines.TileFermentationPlant;
import electrodynamics.common.tile.machines.TileLathe;
import electrodynamics.common.tile.machines.TileMineralWasher;
import electrodynamics.common.tile.machines.TileOxidationFurnace;
import electrodynamics.common.tile.machines.TileReinforcedAlloyer;
import electrodynamics.common.tile.machines.arcfurnace.TileElectricArcFurnace;
import electrodynamics.common.tile.machines.arcfurnace.TileElectricArcFurnaceDouble;
import electrodynamics.common.tile.machines.arcfurnace.TileElectricArcFurnaceTriple;
import electrodynamics.common.tile.machines.charger.TileChargerHV;
import electrodynamics.common.tile.machines.charger.TileChargerLV;
import electrodynamics.common.tile.machines.charger.TileChargerMV;
import electrodynamics.common.tile.machines.furnace.TileElectricFurnace;
import electrodynamics.common.tile.machines.furnace.TileElectricFurnaceDouble;
import electrodynamics.common.tile.machines.furnace.TileElectricFurnaceTriple;
import electrodynamics.common.tile.machines.mineralcrusher.TileMineralCrusher;
import electrodynamics.common.tile.machines.mineralcrusher.TileMineralCrusherDouble;
import electrodynamics.common.tile.machines.mineralcrusher.TileMineralCrusherTriple;
import electrodynamics.common.tile.machines.mineralgrinder.TileMineralGrinder;
import electrodynamics.common.tile.machines.mineralgrinder.TileMineralGrinderDouble;
import electrodynamics.common.tile.machines.mineralgrinder.TileMineralGrinderTriple;
import electrodynamics.common.tile.machines.quarry.TileCoolantResavoir;
import electrodynamics.common.tile.machines.quarry.TileFrame;
import electrodynamics.common.tile.machines.quarry.TileLogisticalManager;
import electrodynamics.common.tile.machines.quarry.TileMotorComplex;
import electrodynamics.common.tile.machines.quarry.TileQuarry;
import electrodynamics.common.tile.machines.quarry.TileSeismicMarker;
import electrodynamics.common.tile.machines.quarry.TileSeismicRelay;
import electrodynamics.common.tile.machines.wiremill.TileWireMill;
import electrodynamics.common.tile.machines.wiremill.TileWireMillDouble;
import electrodynamics.common.tile.machines.wiremill.TileWireMillTriple;
import electrodynamics.common.tile.pipelines.TileCreativeFluidSource;
import electrodynamics.common.tile.pipelines.TileElectricPump;
import electrodynamics.common.tile.pipelines.fluids.TileFluidPipe;
import electrodynamics.common.tile.pipelines.fluids.TileFluidPipeFilter;
import electrodynamics.common.tile.pipelines.fluids.TileFluidPipePump;
import electrodynamics.common.tile.pipelines.fluids.TileFluidValve;
import electrodynamics.common.tile.pipelines.fluids.TileFluidVoid;
import electrodynamics.common.tile.pipelines.gas.*;
import electrodynamics.common.tile.pipelines.gas.gastransformer.TileGasTransformerAddonTank;
import electrodynamics.common.tile.pipelines.gas.gastransformer.TileGasTransformerSideBlock;
import electrodynamics.common.tile.pipelines.gas.gastransformer.compressor.TileCompressor;
import electrodynamics.common.tile.pipelines.gas.gastransformer.compressor.TileDecompressor;
import electrodynamics.common.tile.pipelines.gas.gastransformer.thermoelectricmanipulator.TileThermoelectricManipulator;
import electrodynamics.common.tile.pipelines.tanks.fluid.TileFluidTankHSLA;
import electrodynamics.common.tile.pipelines.tanks.fluid.TileFluidTankReinforced;
import electrodynamics.common.tile.pipelines.tanks.fluid.TileFluidTankSteel;
import electrodynamics.common.tile.pipelines.tanks.gas.TileGasTankHSLA;
import electrodynamics.common.tile.pipelines.tanks.gas.TileGasTankReinforced;
import electrodynamics.common.tile.pipelines.tanks.gas.TileGasTankSteel;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsTileTypes {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, References.ID);

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCoalGenerator>> TILE_COALGENERATOR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.coalgenerator.tag(), () -> new BlockEntityType<>(TileCoalGenerator::new, Sets.newHashSet(getBlock(SubtypeMachine.coalgenerator)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileSolarPanel>> TILE_SOLARPANEL = BLOCK_ENTITY_TYPES.register(SubtypeMachine.solarpanel.tag(), () -> new BlockEntityType<>(TileSolarPanel::new, Sets.newHashSet(getBlock(SubtypeMachine.solarpanel)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileAdvancedSolarPanel>> TILE_ADVANCEDSOLARPANEL = BLOCK_ENTITY_TYPES.register(SubtypeMachine.advancedsolarpanel.tag(), () -> new BlockEntityType<>(TileAdvancedSolarPanel::new, Sets.newHashSet(getBlock(SubtypeMachine.advancedsolarpanel)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricFurnace>> TILE_ELECTRICFURNACE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricfurnace.tag(), () -> new BlockEntityType<>(TileElectricFurnace::new, Sets.newHashSet(getBlock(SubtypeMachine.electricfurnace)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricFurnaceDouble>> TILE_ELECTRICFURNACEDOUBLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricfurnacedouble.tag(), () -> new BlockEntityType<>(TileElectricFurnaceDouble::new, Sets.newHashSet(getBlock(SubtypeMachine.electricfurnacedouble)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricFurnaceTriple>> TILE_ELECTRICFURNACETRIPLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricfurnacetriple.tag(), () -> new BlockEntityType<>(TileElectricFurnaceTriple::new, Sets.newHashSet(getBlock(SubtypeMachine.electricfurnacetriple)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricArcFurnace>> TILE_ELECTRICARCFURNACE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricarcfurnace.tag(), () -> new BlockEntityType<>(TileElectricArcFurnace::new, Sets.newHashSet(getBlock(SubtypeMachine.electricarcfurnace)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricArcFurnaceDouble>> TILE_ELECTRICARCFURNACEDOUBLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricarcfurnacedouble.tag(), () -> new BlockEntityType<>(TileElectricArcFurnaceDouble::new, Sets.newHashSet(getBlock(SubtypeMachine.electricarcfurnacedouble)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricArcFurnaceTriple>> TILE_ELECTRICARCFURNACETRIPLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricarcfurnacetriple.tag(), () -> new BlockEntityType<>(TileElectricArcFurnaceTriple::new, Sets.newHashSet(getBlock(SubtypeMachine.electricarcfurnacetriple)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileWireMill>> TILE_WIREMILL = BLOCK_ENTITY_TYPES.register(SubtypeMachine.wiremill.tag(), () -> new BlockEntityType<>(TileWireMill::new, Sets.newHashSet(getBlock(SubtypeMachine.wiremill)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileWireMillDouble>> TILE_WIREMILLDOUBLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.wiremilldouble.tag(), () -> new BlockEntityType<>(TileWireMillDouble::new, Sets.newHashSet(getBlock(SubtypeMachine.wiremilldouble)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileWireMillTriple>> TILE_WIREMILLTRIPLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.wiremilltriple.tag(), () -> new BlockEntityType<>(TileWireMillTriple::new, Sets.newHashSet(getBlock(SubtypeMachine.wiremilltriple)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralGrinder>> TILE_MINERALGRINDER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralgrinder.tag(), () -> new BlockEntityType<>(TileMineralGrinder::new, Sets.newHashSet(getBlock(SubtypeMachine.mineralgrinder)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralGrinderDouble>> TILE_MINERALGRINDERDOUBLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralgrinderdouble.tag(), () -> new BlockEntityType<>(TileMineralGrinderDouble::new, Sets.newHashSet(getBlock(SubtypeMachine.mineralgrinderdouble)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralGrinderTriple>> TILE_MINERALGRINDERTRIPLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralgrindertriple.tag(), () -> new BlockEntityType<>(TileMineralGrinderTriple::new, Sets.newHashSet(getBlock(SubtypeMachine.mineralgrindertriple)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralCrusher>> TILE_MINERALCRUSHER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralcrusher.tag(), () -> new BlockEntityType<>(TileMineralCrusher::new, Sets.newHashSet(getBlock(SubtypeMachine.mineralcrusher)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralCrusherDouble>> TILE_MINERALCRUSHERDOUBLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralcrusherdouble.tag(), () -> new BlockEntityType<>(TileMineralCrusherDouble::new, Sets.newHashSet(getBlock(SubtypeMachine.mineralcrusherdouble)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralCrusherTriple>> TILE_MINERALCRUSHERTRIPLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralcrushertriple.tag(), () -> new BlockEntityType<>(TileMineralCrusherTriple::new, Sets.newHashSet(getBlock(SubtypeMachine.mineralcrushertriple)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileBatteryBox>> TILE_BATTERYBOX = BLOCK_ENTITY_TYPES.register(SubtypeMachine.batterybox.tag(), () -> new BlockEntityType<>(TileBatteryBox::new, Sets.newHashSet(getBlock(SubtypeMachine.batterybox)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileLithiumBatteryBox>> TILE_LITHIUMBATTERYBOX = BLOCK_ENTITY_TYPES.register(SubtypeMachine.lithiumbatterybox.tag(), () -> new BlockEntityType<>(TileLithiumBatteryBox::new, Sets.newHashSet(getBlock(SubtypeMachine.lithiumbatterybox)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCarbyneBatteryBox>> TILE_CARBYNEBATTERYBOX = BLOCK_ENTITY_TYPES.register(SubtypeMachine.carbynebatterybox.tag(), () -> new BlockEntityType<>(TileCarbyneBatteryBox::new, Sets.newHashSet(getBlock(SubtypeMachine.carbynebatterybox)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileChargerLV>> TILE_CHARGERLV = BLOCK_ENTITY_TYPES.register(SubtypeMachine.chargerlv.tag(), () -> new BlockEntityType<>(TileChargerLV::new, Sets.newHashSet(getBlock(SubtypeMachine.chargerlv)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileChargerMV>> TILE_CHARGERMV = BLOCK_ENTITY_TYPES.register(SubtypeMachine.chargermv.tag(), () -> new BlockEntityType<>(TileChargerMV::new, Sets.newHashSet(getBlock(SubtypeMachine.chargermv)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileChargerHV>> TILE_CHARGERHV = BLOCK_ENTITY_TYPES.register(SubtypeMachine.chargerhv.tag(), () -> new BlockEntityType<>(TileChargerHV::new, Sets.newHashSet(getBlock(SubtypeMachine.chargerhv)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidTankSteel>> TILE_TANKSTEEL = BLOCK_ENTITY_TYPES.register(SubtypeMachine.tanksteel.tag(), () -> new BlockEntityType<>(TileFluidTankSteel::new, Sets.newHashSet(getBlock(SubtypeMachine.tanksteel)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidTankReinforced>> TILE_TANKREINFORCED = BLOCK_ENTITY_TYPES.register(SubtypeMachine.tankreinforced.tag(), () -> new BlockEntityType<>(TileFluidTankReinforced::new, Sets.newHashSet(getBlock(SubtypeMachine.tankreinforced)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidTankHSLA>> TILE_TANKHSLA = BLOCK_ENTITY_TYPES.register(SubtypeMachine.tankhsla.tag(), () -> new BlockEntityType<>(TileFluidTankHSLA::new, Sets.newHashSet(getBlock(SubtypeMachine.tankhsla)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileDowngradeTransformer>> TILE_DOWNGRADETRANSFORMER = BLOCK_ENTITY_TYPES.register("downgradetransformer", () -> new BlockEntityType<>(TileDowngradeTransformer::new, Sets.newHashSet(getBlock(SubtypeMachine.downgradetransformer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileUpgradeTransformer>> TILE_UPGRADETRANSFORMER = BLOCK_ENTITY_TYPES.register("upgradetransformer", () -> new BlockEntityType<>(TileUpgradeTransformer::new, Sets.newHashSet(getBlock(SubtypeMachine.upgradetransformer)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileAdvancedDowngradeTransformer>> TILE_ADVANCEDDOWNGRADETRANSFORMER = BLOCK_ENTITY_TYPES.register("advanceddowngradetransformer", () -> new BlockEntityType<>(TileAdvancedDowngradeTransformer::new, Sets.newHashSet(getBlock(SubtypeMachine.advanceddowngradetransformer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileAdvancedUpgradeTransformer>> TILE_ADVANCEDUPGRADETRANSFORMER = BLOCK_ENTITY_TYPES.register("advancedupgradetransformer", () -> new BlockEntityType<>(TileAdvancedUpgradeTransformer::new, Sets.newHashSet(getBlock(SubtypeMachine.advancedupgradetransformer)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileEnergizedAlloyer>> TILE_ENERGIZEDALLOYER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.energizedalloyer.tag(), () -> new BlockEntityType<>(TileEnergizedAlloyer::new, Sets.newHashSet(getBlock(SubtypeMachine.energizedalloyer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileLathe>> TILE_LATHE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.lathe.tag(), () -> new BlockEntityType<>(TileLathe::new, Sets.newHashSet(getBlock(SubtypeMachine.lathe)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileReinforcedAlloyer>> TILE_REINFORCEDALLOYER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.reinforcedalloyer.tag(), () -> new BlockEntityType<>(TileReinforcedAlloyer::new, Sets.newHashSet(getBlock(SubtypeMachine.reinforcedalloyer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileOxidationFurnace>> TILE_OXIDATIONFURNACE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.oxidationfurnace.tag(), () -> new BlockEntityType<>(TileOxidationFurnace::new, Sets.newHashSet(getBlock(SubtypeMachine.oxidationfurnace)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCreativePowerSource>> TILE_CREATIVEPOWERSOURCE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.creativepowersource.tag(), () -> new BlockEntityType<>(TileCreativePowerSource::new, Sets.newHashSet(getBlock(SubtypeMachine.creativepowersource)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricPump>> TILE_ELECTRICPUMP = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricpump.tag(), () -> new BlockEntityType<>(TileElectricPump::new, Sets.newHashSet(getBlock(SubtypeMachine.electricpump)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileThermoelectricGenerator>> TILE_THERMOELECTRICGENERATOR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.thermoelectricgenerator.tag(), () -> new BlockEntityType<>(TileThermoelectricGenerator::new, Sets.newHashSet(getBlock(SubtypeMachine.thermoelectricgenerator)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileHydroelectricGenerator>> TILE_HYDROELECTRICGENERATOR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.hydroelectricgenerator.tag(), () -> new BlockEntityType<>(TileHydroelectricGenerator::new, Sets.newHashSet(getBlock(SubtypeMachine.hydroelectricgenerator)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileWindmill>> TILE_WINDMILL = BLOCK_ENTITY_TYPES.register(SubtypeMachine.windmill.tag(), () -> new BlockEntityType<>(TileWindmill::new, Sets.newHashSet(getBlock(SubtypeMachine.windmill)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFermentationPlant>> TILE_FERMENTATIONPLANT = BLOCK_ENTITY_TYPES.register(SubtypeMachine.fermentationplant.tag(), () -> new BlockEntityType<>(TileFermentationPlant::new, Sets.newHashSet(getBlock(SubtypeMachine.fermentationplant)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCombustionChamber>> TILE_COMBUSTIONCHAMBER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.combustionchamber.tag(), () -> new BlockEntityType<>(TileCombustionChamber::new, Sets.newHashSet(getBlock(SubtypeMachine.combustionchamber)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralWasher>> TILE_MINERALWASHER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralwasher.tag(), () -> new BlockEntityType<>(TileMineralWasher::new, Sets.newHashSet(getBlock(SubtypeMachine.mineralwasher)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileChemicalMixer>> TILE_CHEMICALMIXER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.chemicalmixer.tag(), () -> new BlockEntityType<>(TileChemicalMixer::new, Sets.newHashSet(getBlock(SubtypeMachine.chemicalmixer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileChemicalCrystallizer>> TILE_CHEMICALCRYSTALLIZER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.chemicalcrystallizer.tag(), () -> new BlockEntityType<>(TileChemicalCrystallizer::new, Sets.newHashSet(getBlock(SubtypeMachine.chemicalcrystallizer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCircuitBreaker>> TILE_CIRCUITBREAKER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.circuitbreaker.tag(), () -> new BlockEntityType<>(TileCircuitBreaker::new, Sets.newHashSet(getBlock(SubtypeMachine.circuitbreaker)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMultimeterBlock>> TILE_MULTIMETERBLOCK = BLOCK_ENTITY_TYPES.register(SubtypeMachine.multimeterblock.tag(), () -> new BlockEntityType<>(TileMultimeterBlock::new, Sets.newHashSet(getBlock(SubtypeMachine.multimeterblock)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMultiSubnode>> TILE_MULTI = BLOCK_ENTITY_TYPES.register("multisubnode", () -> new BlockEntityType<>(TileMultiSubnode::new, Sets.newHashSet(ElectrodynamicsBlocks.multi), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileWire>> TILE_WIRE = BLOCK_ENTITY_TYPES.register("wiregenerictile", () -> new BlockEntityType<>(TileWire::new, BlockWire.WIRES, null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileLogisticalWire>> TILE_LOGISTICALWIRE = BLOCK_ENTITY_TYPES.register("wirelogisticaltile", () -> new BlockEntityType<>(TileLogisticalWire::new, BlockLogisticalWire.WIRES, null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidPipe>> TILE_PIPE = BLOCK_ENTITY_TYPES.register("pipegenerictile", () -> new BlockEntityType<>(TileFluidPipe::new, BlockFluidPipe.PIPESET, null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectrolyticSeparator>> TILE_ELECTROLYTICSEPARATOR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electrolyticseparator.tag(), () -> new BlockEntityType<>(TileElectrolyticSeparator::new, Sets.newHashSet(getBlock(SubtypeMachine.electrolyticseparator)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCreativeFluidSource>> TILE_CREATIVEFLUIDSOURCE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.creativefluidsource.tag(), () -> new BlockEntityType<>(TileCreativeFluidSource::new, Sets.newHashSet(getBlock(SubtypeMachine.creativefluidsource)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidVoid>> TILE_FLUIDVOID = BLOCK_ENTITY_TYPES.register(SubtypeMachine.fluidvoid.tag(), () -> new BlockEntityType<>(TileFluidVoid::new, Sets.newHashSet(getBlock(SubtypeMachine.fluidvoid)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileSeismicMarker>> TILE_SEISMICMARKER = BLOCK_ENTITY_TYPES.register("seismicmarker", () -> new BlockEntityType<>(TileSeismicMarker::new, Sets.newHashSet(ElectrodynamicsBlocks.blockSeismicMarker), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileSeismicRelay>> TILE_SEISMICRELAY = BLOCK_ENTITY_TYPES.register(SubtypeMachine.seismicrelay.tag(), () -> new BlockEntityType<>(TileSeismicRelay::new, Sets.newHashSet(getBlock(SubtypeMachine.seismicrelay)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileQuarry>> TILE_QUARRY = BLOCK_ENTITY_TYPES.register(SubtypeMachine.quarry.tag(), () -> new BlockEntityType<>(TileQuarry::new, Sets.newHashSet(getBlock(SubtypeMachine.quarry)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCoolantResavoir>> TILE_COOLANTRESAVOIR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.coolantresavoir.tag(), () -> new BlockEntityType<>(TileCoolantResavoir::new, Sets.newHashSet(getBlock(SubtypeMachine.coolantresavoir)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMotorComplex>> TILE_MOTORCOMPLEX = BLOCK_ENTITY_TYPES.register(SubtypeMachine.motorcomplex.tag(), () -> new BlockEntityType<>(TileMotorComplex::new, Sets.newHashSet(getBlock(SubtypeMachine.motorcomplex)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileLogisticalManager>> TILE_LOGISTICALMANAGER = BLOCK_ENTITY_TYPES.register("logisticalmanager", () -> new BlockEntityType<>(TileLogisticalManager::new, Sets.newHashSet(ElectrodynamicsBlocks.blockLogisticalManager), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFrame>> TILE_QUARRY_FRAME = BLOCK_ENTITY_TYPES.register("quarryframe", () -> new BlockEntityType<>(TileFrame::new, Sets.newHashSet(ElectrodynamicsBlocks.blockFrame), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasPipe>> TILE_GAS_PIPE = BLOCK_ENTITY_TYPES.register("gaspipe", () -> new BlockEntityType<>(TileGasPipe::new, Sets.newHashSet(BlockGasPipe.PIPESET), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasTankSteel>> TILE_GASTANK_STEEL = BLOCK_ENTITY_TYPES.register("gastanksteel", () -> new BlockEntityType<>(TileGasTankSteel::new, Sets.newHashSet(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gastanksteel)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasTankReinforced>> TILE_GASTANK_REINFORCED = BLOCK_ENTITY_TYPES.register("gastankreinforced", () -> new BlockEntityType<>(TileGasTankReinforced::new, Sets.newHashSet(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gastankreinforced)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasTankHSLA>> TILE_GASTANK_HSLA = BLOCK_ENTITY_TYPES.register("gastankhsla", () -> new BlockEntityType<>(TileGasTankHSLA::new, Sets.newHashSet(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gastankhsla)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCompressor>> TILE_COMPRESSOR = BLOCK_ENTITY_TYPES.register("compressor", () -> new BlockEntityType<>(TileCompressor::new, Sets.newHashSet(ElectrodynamicsBlocks.blockCompressor), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileDecompressor>> TILE_DECOMPRESSOR = BLOCK_ENTITY_TYPES.register("decompressor", () -> new BlockEntityType<>(TileDecompressor::new, Sets.newHashSet(ElectrodynamicsBlocks.blockDecompressor), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasTransformerSideBlock>> TILE_COMPRESSOR_SIDE = BLOCK_ENTITY_TYPES.register("compressorside", () -> new BlockEntityType<>(TileGasTransformerSideBlock::new, Sets.newHashSet(ElectrodynamicsBlocks.blockGasTransformerSide), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasTransformerAddonTank>> TILE_COMPRESSOR_ADDONTANK = BLOCK_ENTITY_TYPES.register("compressoraddontank", () -> new BlockEntityType<>(TileGasTransformerAddonTank::new, Sets.newHashSet(ElectrodynamicsBlocks.blockGasTransformerAddonTank), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasVent>> TILE_GASVENT = BLOCK_ENTITY_TYPES.register("gasvent", () -> new BlockEntityType<>(TileGasVent::new, Sets.newHashSet(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gasvent)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileThermoelectricManipulator>> TILE_THERMOELECTRIC_MANIPULATOR = BLOCK_ENTITY_TYPES.register("thermoelectricmanipulator", () -> new BlockEntityType<>(TileThermoelectricManipulator::new, Sets.newHashSet(ElectrodynamicsBlocks.blockThermoelectricManipulator), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasValve>> TILE_GASVALVE = BLOCK_ENTITY_TYPES.register("gasvalve", () -> new BlockEntityType<>(TileGasValve::new, Sets.newHashSet(ElectrodynamicsBlocks.blockGasValve), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidValve>> TILE_FLUIDVALVE = BLOCK_ENTITY_TYPES.register("fluidvalve", () -> new BlockEntityType<>(TileFluidValve::new, Sets.newHashSet(ElectrodynamicsBlocks.blockFluidValve), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasPipePump>> TILE_GASPIPEPUMP = BLOCK_ENTITY_TYPES.register("gaspipepump", () -> new BlockEntityType<>(TileGasPipePump::new, Sets.newHashSet(ElectrodynamicsBlocks.blockGasPipePump), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidPipePump>> TILE_FLUIDPIPEPUMP = BLOCK_ENTITY_TYPES.register("fluidpipepump", () -> new BlockEntityType<>(TileFluidPipePump::new, Sets.newHashSet(ElectrodynamicsBlocks.blockFluidPipePump), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasPipeFilter>> TILE_GASPIPEFILTER = BLOCK_ENTITY_TYPES.register("gaspipefilter", () -> new BlockEntityType<>(TileGasPipeFilter::new, Sets.newHashSet(ElectrodynamicsBlocks.blockGasPipeFilter), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidPipeFilter>> TILE_FLUIDPIPEFILTER = BLOCK_ENTITY_TYPES.register("fluidpipefilter", () -> new BlockEntityType<>(TileFluidPipeFilter::new, Sets.newHashSet(ElectrodynamicsBlocks.blockFluidPipeFilter), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileRelay>> TILE_RELAY = BLOCK_ENTITY_TYPES.register("relay", () -> new BlockEntityType<>(TileRelay::new, Sets.newHashSet(ElectrodynamicsBlocks.getBlock(SubtypeMachine.relay)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TilePotentiometer>> TILE_POTENTIOMETER = BLOCK_ENTITY_TYPES.register("potentiometer", () -> new BlockEntityType<>(TilePotentiometer::new, Sets.newHashSet(ElectrodynamicsBlocks.getBlock(SubtypeMachine.potentiometer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCircuitMonitor>> TILE_CIRCUITMONITOR = BLOCK_ENTITY_TYPES.register("circuitmonitor", () -> new BlockEntityType<>(TileCircuitMonitor::new, Sets.newHashSet(ElectrodynamicsBlocks.getBlock(SubtypeMachine.circuitmonitor)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCurrentRegulator>> TILE_CURRENTREGULATOR = BLOCK_ENTITY_TYPES.register("currentregulator", () -> new BlockEntityType<>(TileCurrentRegulator::new, Sets.newHashSet(ElectrodynamicsBlocks.getBlock(SubtypeMachine.currentregulator)), null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileGasCollector>> TILE_GASCOLLECTOR = BLOCK_ENTITY_TYPES.register("gascollector", () -> new BlockEntityType<>(TileGasCollector::new, Sets.newHashSet(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gascollector)), null));
}
