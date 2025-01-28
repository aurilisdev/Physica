package electrodynamics.registers;

import com.google.common.collect.Sets;

import electrodynamics.api.References;
import electrodynamics.api.multiblock.assemblybased.TileMultiblockSlave;
import electrodynamics.common.block.connect.BlockFluidPipe;
import electrodynamics.common.block.connect.BlockGasPipe;
import electrodynamics.common.block.connect.BlockLogisticalWire;
import electrodynamics.common.block.connect.BlockWire;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.common.tile.compatibility.TileRotaryUnifier;
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
import electrodynamics.common.tile.machines.TileElectrolosisChamber;
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
import electrodynamics.common.tile.machines.chemicalreactor.TileChemicalReactor;
import electrodynamics.common.tile.machines.chemicalreactor.TileChemicalReactorDummy;
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
import electrodynamics.common.tile.pipelines.fluid.TileCreativeFluidSource;
import electrodynamics.common.tile.pipelines.fluid.TileElectricPump;
import electrodynamics.common.tile.pipelines.fluid.TileFluidPipe;
import electrodynamics.common.tile.pipelines.fluid.TileFluidPipeFilter;
import electrodynamics.common.tile.pipelines.fluid.TileFluidPipePump;
import electrodynamics.common.tile.pipelines.fluid.TileFluidValve;
import electrodynamics.common.tile.pipelines.fluid.TileFluidVoid;
import electrodynamics.common.tile.pipelines.fluid.tank.TileFluidTankHSLA;
import electrodynamics.common.tile.pipelines.fluid.tank.TileFluidTankReinforced;
import electrodynamics.common.tile.pipelines.fluid.tank.TileFluidTankSteel;
import electrodynamics.common.tile.pipelines.gas.TileCreativeGasSource;
import electrodynamics.common.tile.pipelines.gas.TileGasCollector;
import electrodynamics.common.tile.pipelines.gas.TileGasPipe;
import electrodynamics.common.tile.pipelines.gas.TileGasPipeFilter;
import electrodynamics.common.tile.pipelines.gas.TileGasPipePump;
import electrodynamics.common.tile.pipelines.gas.TileGasValve;
import electrodynamics.common.tile.pipelines.gas.TileGasVent;
import electrodynamics.common.tile.pipelines.gas.gastransformer.TileGasTransformerAddonTank;
import electrodynamics.common.tile.pipelines.gas.gastransformer.TileGasTransformerSideBlock;
import electrodynamics.common.tile.pipelines.gas.gastransformer.compressor.GenericTileAdvancedCompressor;
import electrodynamics.common.tile.pipelines.gas.gastransformer.compressor.GenericTileBasicCompressor;
import electrodynamics.common.tile.pipelines.gas.gastransformer.thermoelectricmanipulator.TileAdvancedThermoelectricManipulator;
import electrodynamics.common.tile.pipelines.gas.gastransformer.thermoelectricmanipulator.TileBasicThermoelectricManipulator;
import electrodynamics.common.tile.pipelines.gas.tank.TileGasTankHSLA;
import electrodynamics.common.tile.pipelines.gas.tank.TileGasTankReinforced;
import electrodynamics.common.tile.pipelines.gas.tank.TileGasTankSteel;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsTiles {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, References.ID);

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCoalGenerator>> TILE_COALGENERATOR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.coalgenerator.tag(), () -> new BlockEntityType<>(TileCoalGenerator::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.coalgenerator)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileSolarPanel>> TILE_SOLARPANEL = BLOCK_ENTITY_TYPES.register(SubtypeMachine.solarpanel.tag(), () -> new BlockEntityType<>(TileSolarPanel::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.solarpanel)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileAdvancedSolarPanel>> TILE_ADVANCEDSOLARPANEL = BLOCK_ENTITY_TYPES.register(SubtypeMachine.advancedsolarpanel.tag(), () -> new BlockEntityType<>(TileAdvancedSolarPanel::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.advancedsolarpanel)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricFurnace>> TILE_ELECTRICFURNACE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricfurnace.tag(), () -> new BlockEntityType<>(TileElectricFurnace::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnace)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricFurnaceDouble>> TILE_ELECTRICFURNACEDOUBLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricfurnacedouble.tag(), () -> new BlockEntityType<>(TileElectricFurnaceDouble::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnacedouble)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricFurnaceTriple>> TILE_ELECTRICFURNACETRIPLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricfurnacetriple.tag(), () -> new BlockEntityType<>(TileElectricFurnaceTriple::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnacetriple)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricArcFurnace>> TILE_ELECTRICARCFURNACE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricarcfurnace.tag(), () -> new BlockEntityType<>(TileElectricArcFurnace::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnace)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricArcFurnaceDouble>> TILE_ELECTRICARCFURNACEDOUBLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricarcfurnacedouble.tag(), () -> new BlockEntityType<>(TileElectricArcFurnaceDouble::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnacedouble)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricArcFurnaceTriple>> TILE_ELECTRICARCFURNACETRIPLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricarcfurnacetriple.tag(), () -> new BlockEntityType<>(TileElectricArcFurnaceTriple::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnacetriple)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileWireMill>> TILE_WIREMILL = BLOCK_ENTITY_TYPES.register(SubtypeMachine.wiremill.tag(), () -> new BlockEntityType<>(TileWireMill::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremill)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileWireMillDouble>> TILE_WIREMILLDOUBLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.wiremilldouble.tag(), () -> new BlockEntityType<>(TileWireMillDouble::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremilldouble)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileWireMillTriple>> TILE_WIREMILLTRIPLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.wiremilltriple.tag(), () -> new BlockEntityType<>(TileWireMillTriple::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremilltriple)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralGrinder>> TILE_MINERALGRINDER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralgrinder.tag(), () -> new BlockEntityType<>(TileMineralGrinder::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrinder)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralGrinderDouble>> TILE_MINERALGRINDERDOUBLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralgrinderdouble.tag(), () -> new BlockEntityType<>(TileMineralGrinderDouble::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrinderdouble)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralGrinderTriple>> TILE_MINERALGRINDERTRIPLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralgrindertriple.tag(), () -> new BlockEntityType<>(TileMineralGrinderTriple::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrindertriple)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralCrusher>> TILE_MINERALCRUSHER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralcrusher.tag(), () -> new BlockEntityType<>(TileMineralCrusher::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrusher)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralCrusherDouble>> TILE_MINERALCRUSHERDOUBLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralcrusherdouble.tag(), () -> new BlockEntityType<>(TileMineralCrusherDouble::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrusherdouble)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralCrusherTriple>> TILE_MINERALCRUSHERTRIPLE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralcrushertriple.tag(), () -> new BlockEntityType<>(TileMineralCrusherTriple::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrushertriple)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileBatteryBox>> TILE_BATTERYBOX = BLOCK_ENTITY_TYPES.register(SubtypeMachine.batterybox.tag(), () -> new BlockEntityType<>(TileBatteryBox::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.batterybox)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileLithiumBatteryBox>> TILE_LITHIUMBATTERYBOX = BLOCK_ENTITY_TYPES.register(SubtypeMachine.lithiumbatterybox.tag(), () -> new BlockEntityType<>(TileLithiumBatteryBox::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.lithiumbatterybox)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCarbyneBatteryBox>> TILE_CARBYNEBATTERYBOX = BLOCK_ENTITY_TYPES.register(SubtypeMachine.carbynebatterybox.tag(), () -> new BlockEntityType<>(TileCarbyneBatteryBox::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.carbynebatterybox)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileChargerLV>> TILE_CHARGERLV = BLOCK_ENTITY_TYPES.register(SubtypeMachine.chargerlv.tag(), () -> new BlockEntityType<>(TileChargerLV::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chargerlv)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileChargerMV>> TILE_CHARGERMV = BLOCK_ENTITY_TYPES.register(SubtypeMachine.chargermv.tag(), () -> new BlockEntityType<>(TileChargerMV::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chargermv)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileChargerHV>> TILE_CHARGERHV = BLOCK_ENTITY_TYPES.register(SubtypeMachine.chargerhv.tag(), () -> new BlockEntityType<>(TileChargerHV::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chargerhv)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidTankSteel>> TILE_TANKSTEEL = BLOCK_ENTITY_TYPES.register(SubtypeMachine.tanksteel.tag(), () -> new BlockEntityType<>(TileFluidTankSteel::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidTankReinforced>> TILE_TANKREINFORCED = BLOCK_ENTITY_TYPES.register(SubtypeMachine.tankreinforced.tag(), () -> new BlockEntityType<>(TileFluidTankReinforced::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tankreinforced)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidTankHSLA>> TILE_TANKHSLA = BLOCK_ENTITY_TYPES.register(SubtypeMachine.tankhsla.tag(), () -> new BlockEntityType<>(TileFluidTankHSLA::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tankhsla)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileDowngradeTransformer>> TILE_DOWNGRADETRANSFORMER = BLOCK_ENTITY_TYPES.register("downgradetransformer", () -> new BlockEntityType<>(TileDowngradeTransformer::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.downgradetransformer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileUpgradeTransformer>> TILE_UPGRADETRANSFORMER = BLOCK_ENTITY_TYPES.register("upgradetransformer", () -> new BlockEntityType<>(TileUpgradeTransformer::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.upgradetransformer)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileAdvancedDowngradeTransformer>> TILE_ADVANCEDDOWNGRADETRANSFORMER = BLOCK_ENTITY_TYPES.register("advanceddowngradetransformer", () -> new BlockEntityType<>(TileAdvancedDowngradeTransformer::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.advanceddowngradetransformer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileAdvancedUpgradeTransformer>> TILE_ADVANCEDUPGRADETRANSFORMER = BLOCK_ENTITY_TYPES.register("advancedupgradetransformer", () -> new BlockEntityType<>(TileAdvancedUpgradeTransformer::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.advancedupgradetransformer)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileEnergizedAlloyer>> TILE_ENERGIZEDALLOYER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.energizedalloyer.tag(), () -> new BlockEntityType<>(TileEnergizedAlloyer::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.energizedalloyer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileLathe>> TILE_LATHE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.lathe.tag(), () -> new BlockEntityType<>(TileLathe::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.lathe)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileReinforcedAlloyer>> TILE_REINFORCEDALLOYER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.reinforcedalloyer.tag(), () -> new BlockEntityType<>(TileReinforcedAlloyer::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.reinforcedalloyer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileOxidationFurnace>> TILE_OXIDATIONFURNACE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.oxidationfurnace.tag(), () -> new BlockEntityType<>(TileOxidationFurnace::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.oxidationfurnace)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCreativePowerSource>> TILE_CREATIVEPOWERSOURCE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.creativepowersource.tag(), () -> new BlockEntityType<>(TileCreativePowerSource::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.creativepowersource)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectricPump>> TILE_ELECTRICPUMP = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electricpump.tag(), () -> new BlockEntityType<>(TileElectricPump::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricpump)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileThermoelectricGenerator>> TILE_THERMOELECTRICGENERATOR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.thermoelectricgenerator.tag(), () -> new BlockEntityType<>(TileThermoelectricGenerator::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.thermoelectricgenerator)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileHydroelectricGenerator>> TILE_HYDROELECTRICGENERATOR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.hydroelectricgenerator.tag(), () -> new BlockEntityType<>(TileHydroelectricGenerator::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.hydroelectricgenerator)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileWindmill>> TILE_WINDMILL = BLOCK_ENTITY_TYPES.register(SubtypeMachine.windmill.tag(), () -> new BlockEntityType<>(TileWindmill::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.windmill)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFermentationPlant>> TILE_FERMENTATIONPLANT = BLOCK_ENTITY_TYPES.register(SubtypeMachine.fermentationplant.tag(), () -> new BlockEntityType<>(TileFermentationPlant::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fermentationplant)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCombustionChamber>> TILE_COMBUSTIONCHAMBER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.combustionchamber.tag(), () -> new BlockEntityType<>(TileCombustionChamber::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.combustionchamber)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMineralWasher>> TILE_MINERALWASHER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.mineralwasher.tag(), () -> new BlockEntityType<>(TileMineralWasher::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralwasher)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileChemicalMixer>> TILE_CHEMICALMIXER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.chemicalmixer.tag(), () -> new BlockEntityType<>(TileChemicalMixer::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chemicalmixer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileChemicalCrystallizer>> TILE_CHEMICALCRYSTALLIZER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.chemicalcrystallizer.tag(), () -> new BlockEntityType<>(TileChemicalCrystallizer::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chemicalcrystallizer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCircuitBreaker>> TILE_CIRCUITBREAKER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.circuitbreaker.tag(), () -> new BlockEntityType<>(TileCircuitBreaker::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.circuitbreaker)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMultimeterBlock>> TILE_MULTIMETERBLOCK = BLOCK_ENTITY_TYPES.register(SubtypeMachine.multimeterblock.tag(), () -> new BlockEntityType<>(TileMultimeterBlock::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.multimeterblock)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMultiSubnode>> TILE_MULTI = BLOCK_ENTITY_TYPES.register("multisubnode", () -> new BlockEntityType<>(TileMultiSubnode::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_MULTISUBNODE.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileWire>> TILE_WIRE = BLOCK_ENTITY_TYPES.register("wiregenerictile", () -> new BlockEntityType<>(TileWire::new, BlockWire.WIRES, null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileLogisticalWire>> TILE_LOGISTICALWIRE = BLOCK_ENTITY_TYPES.register("wirelogisticaltile", () -> new BlockEntityType<>(TileLogisticalWire::new, BlockLogisticalWire.WIRES, null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidPipe>> TILE_PIPE = BLOCK_ENTITY_TYPES.register("pipegenerictile", () -> new BlockEntityType<>(TileFluidPipe::new, BlockFluidPipe.PIPESET, null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileElectrolyticSeparator>> TILE_ELECTROLYTICSEPARATOR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electrolyticseparator.tag(), () -> new BlockEntityType<>(TileElectrolyticSeparator::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolyticseparator)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCreativeFluidSource>> TILE_CREATIVEFLUIDSOURCE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.creativefluidsource.tag(), () -> new BlockEntityType<>(TileCreativeFluidSource::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.creativefluidsource)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidVoid>> TILE_FLUIDVOID = BLOCK_ENTITY_TYPES.register(SubtypeMachine.fluidvoid.tag(), () -> new BlockEntityType<>(TileFluidVoid::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidvoid)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileSeismicMarker>> TILE_SEISMICMARKER = BLOCK_ENTITY_TYPES.register("seismicmarker", () -> new BlockEntityType<>(TileSeismicMarker::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_SEISMICMARKER.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileSeismicRelay>> TILE_SEISMICRELAY = BLOCK_ENTITY_TYPES.register(SubtypeMachine.seismicrelay.tag(), () -> new BlockEntityType<>(TileSeismicRelay::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.seismicrelay)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileQuarry>> TILE_QUARRY = BLOCK_ENTITY_TYPES.register(SubtypeMachine.quarry.tag(), () -> new BlockEntityType<>(TileQuarry::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.quarry)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCoolantResavoir>> TILE_COOLANTRESAVOIR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.coolantresavoir.tag(), () -> new BlockEntityType<>(TileCoolantResavoir::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.coolantresavoir)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileMotorComplex>> TILE_MOTORCOMPLEX = BLOCK_ENTITY_TYPES.register(SubtypeMachine.motorcomplex.tag(), () -> new BlockEntityType<>(TileMotorComplex::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.motorcomplex)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileLogisticalManager>> TILE_LOGISTICALMANAGER = BLOCK_ENTITY_TYPES.register("logisticalmanager", () -> new BlockEntityType<>(TileLogisticalManager::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_LOGISTICALMANAGER.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFrame>> TILE_QUARRY_FRAME = BLOCK_ENTITY_TYPES.register("quarryframe", () -> new BlockEntityType<>(TileFrame::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_FRAME.get(), ElectrodynamicsBlocks.BLOCK_FRAME_CORNER.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasPipe>> TILE_GAS_PIPE = BLOCK_ENTITY_TYPES.register("gaspipe", () -> new BlockEntityType<>(TileGasPipe::new, Sets.newHashSet(BlockGasPipe.PIPESET), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasTankSteel>> TILE_GASTANK_STEEL = BLOCK_ENTITY_TYPES.register(SubtypeMachine.gastanksteel.tag(), () -> new BlockEntityType<>(TileGasTankSteel::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gastanksteel)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasTankReinforced>> TILE_GASTANK_REINFORCED = BLOCK_ENTITY_TYPES.register(SubtypeMachine.gastankreinforced.tag(), () -> new BlockEntityType<>(TileGasTankReinforced::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gastankreinforced)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasTankHSLA>> TILE_GASTANK_HSLA = BLOCK_ENTITY_TYPES.register(SubtypeMachine.gastankhsla.tag(), () -> new BlockEntityType<>(TileGasTankHSLA::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gastankhsla)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<GenericTileBasicCompressor.TileCompressor>> TILE_COMPRESSOR = BLOCK_ENTITY_TYPES.register("compressor", () -> new BlockEntityType<>(GenericTileBasicCompressor.TileCompressor::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_COMPRESSOR.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<GenericTileBasicCompressor.TileDecompressor>> TILE_DECOMPRESSOR = BLOCK_ENTITY_TYPES.register("decompressor", () -> new BlockEntityType<>(GenericTileBasicCompressor.TileDecompressor::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_DECOMPRESSOR.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<GenericTileAdvancedCompressor.TileAdvancedCompressor>> TILE_ADVANCEDCOMPRESSOR = BLOCK_ENTITY_TYPES.register("advancedcompressor", () -> new BlockEntityType<>(GenericTileAdvancedCompressor.TileAdvancedCompressor::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_ADVANCEDCOMPRESSOR.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<GenericTileAdvancedCompressor.TileAdvancedDecompressor>> TILE_ADVANCEDDECOMPRESSOR = BLOCK_ENTITY_TYPES.register("advanceddecompressor", () -> new BlockEntityType<>(GenericTileAdvancedCompressor.TileAdvancedDecompressor::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_ADVANCEDDECOMPRESSOR.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasTransformerSideBlock>> TILE_COMPRESSOR_SIDE = BLOCK_ENTITY_TYPES.register("compressorside", () -> new BlockEntityType<>(TileGasTransformerSideBlock::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_SIDE.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasTransformerAddonTank>> TILE_COMPRESSOR_ADDONTANK = BLOCK_ENTITY_TYPES.register("compressoraddontank", () -> new BlockEntityType<>(TileGasTransformerAddonTank::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasVent>> TILE_GASVENT = BLOCK_ENTITY_TYPES.register(SubtypeMachine.gasvent.tag(), () -> new BlockEntityType<>(TileGasVent::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gasvent)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileBasicThermoelectricManipulator>> TILE_THERMOELECTRIC_MANIPULATOR = BLOCK_ENTITY_TYPES.register("thermoelectricmanipulator", () -> new BlockEntityType<>(TileBasicThermoelectricManipulator::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR.get()), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileAdvancedThermoelectricManipulator>> TILE_ADVANCED_THERMOELECTRIC_MANIPULATOR = BLOCK_ENTITY_TYPES.register("advancedthermoelectricmanipulator", () -> new BlockEntityType<>(TileAdvancedThermoelectricManipulator::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_ADVANCED_THERMOELECTRICMANIPULATOR.get()), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasValve>> TILE_GASVALVE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.gasvalve.tag(), () -> new BlockEntityType<>(TileGasValve::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gasvalve)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidValve>> TILE_FLUIDVALVE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.fluidvalve.tag(), () -> new BlockEntityType<>(TileFluidValve::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidvalve)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasPipePump>> TILE_GASPIPEPUMP = BLOCK_ENTITY_TYPES.register(SubtypeMachine.gaspipepump.tag(), () -> new BlockEntityType<>(TileGasPipePump::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gaspipepump)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidPipePump>> TILE_FLUIDPIPEPUMP = BLOCK_ENTITY_TYPES.register(SubtypeMachine.fluidpipepump.tag(), () -> new BlockEntityType<>(TileFluidPipePump::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidpipepump)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileGasPipeFilter>> TILE_GASPIPEFILTER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.gaspipefilter.tag(), () -> new BlockEntityType<>(TileGasPipeFilter::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gaspipefilter)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileFluidPipeFilter>> TILE_FLUIDPIPEFILTER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.fluidpipefilter.tag(), () -> new BlockEntityType<>(TileFluidPipeFilter::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidpipefilter)), null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileRelay>> TILE_RELAY = BLOCK_ENTITY_TYPES.register(SubtypeMachine.relay.tag(), () -> new BlockEntityType<>(TileRelay::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.relay)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TilePotentiometer>> TILE_POTENTIOMETER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.potentiometer.tag(), () -> new BlockEntityType<>(TilePotentiometer::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.potentiometer)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCircuitMonitor>> TILE_CIRCUITMONITOR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.circuitmonitor.tag(), () -> new BlockEntityType<>(TileCircuitMonitor::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.circuitmonitor)), null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileCurrentRegulator>> TILE_CURRENTREGULATOR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.currentregulator.tag(), () -> new BlockEntityType<>(TileCurrentRegulator::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.currentregulator)), null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileGasCollector>> TILE_GASCOLLECTOR = BLOCK_ENTITY_TYPES.register(SubtypeMachine.gascollector.tag(), () -> new BlockEntityType<>(TileGasCollector::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gascollector)), null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileChemicalReactor>> TILE_CHEMICALREACTOR = BLOCK_ENTITY_TYPES.register("chemicalreactor", () -> new BlockEntityType<>(TileChemicalReactor::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOR.get()), null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileChemicalReactorDummy>> TILE_CHEMICALREACTOR_DUMMY = BLOCK_ENTITY_TYPES.register("chemicalreactordummy", () -> new BlockEntityType<>(TileChemicalReactorDummy::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOREXTRA_MIDDLE.get(), ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOREXTRA_TOP.get()), null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCreativeGasSource>> TILE_CREATIVEGASSOURCE = BLOCK_ENTITY_TYPES.register(SubtypeMachine.creativegassource.tag(), () -> new BlockEntityType<>(TileCreativeGasSource::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.creativegassource)), null));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileMultiblockSlave>> TILE_MULTIBLOCK_SLAVE = BLOCK_ENTITY_TYPES.register("multiblockslave", () -> new BlockEntityType<>(TileMultiblockSlave::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_MULTIBLOCK_SLAVE.get()), null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileElectrolosisChamber>> TILE_ELECTROLOSISCHAMBER = BLOCK_ENTITY_TYPES.register(SubtypeMachine.electrolosischamber.tag(), () -> new BlockEntityType<>(TileElectrolosisChamber::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolosischamber)), null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileRotaryUnifier>> TILE_ROTARYUNIFIER = BLOCK_ENTITY_TYPES.register("rotaryunifier", () -> new BlockEntityType<>(TileRotaryUnifier::new, Sets.newHashSet(ElectrodynamicsBlocks.BLOCK_ROTARYUNIFIER.get()), null));
}
