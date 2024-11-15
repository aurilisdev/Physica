package electrodynamics.common.block.subtype;

import electrodynamics.api.ISubtype;
import electrodynamics.common.tile.electricitygrid.TileCircuitBreaker;
import electrodynamics.common.tile.electricitygrid.TileCircuitMonitor;
import electrodynamics.common.tile.electricitygrid.TileCurrentRegulator;
import electrodynamics.common.tile.electricitygrid.TileMultimeterBlock;
import electrodynamics.common.tile.electricitygrid.TilePotentiometer;
import electrodynamics.common.tile.electricitygrid.TileRelay;
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
import electrodynamics.common.tile.machines.*;
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
import electrodynamics.common.tile.machines.quarry.TileMotorComplex;
import electrodynamics.common.tile.machines.quarry.TileQuarry;
import electrodynamics.common.tile.machines.quarry.TileSeismicRelay;
import electrodynamics.common.tile.machines.wiremill.TileWireMill;
import electrodynamics.common.tile.machines.wiremill.TileWireMillDouble;
import electrodynamics.common.tile.machines.wiremill.TileWireMillTriple;
import electrodynamics.common.tile.pipelines.fluid.TileCreativeFluidSource;
import electrodynamics.common.tile.pipelines.fluid.TileElectricPump;
import electrodynamics.common.tile.pipelines.fluid.TileFluidVoid;
import electrodynamics.common.tile.pipelines.gas.TileCreativeGasSource;
import electrodynamics.common.tile.pipelines.gas.TileGasCollector;
import electrodynamics.common.tile.pipelines.gas.TileGasVent;
import electrodynamics.common.tile.pipelines.fluid.tank.TileFluidTankHSLA;
import electrodynamics.common.tile.pipelines.fluid.tank.TileFluidTankReinforced;
import electrodynamics.common.tile.pipelines.fluid.tank.TileFluidTankSteel;
import electrodynamics.common.tile.pipelines.gas.tank.TileGasTankHSLA;
import electrodynamics.common.tile.pipelines.gas.tank.TileGasTankReinforced;
import electrodynamics.common.tile.pipelines.gas.tank.TileGasTankSteel;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public enum SubtypeMachine implements ISubtype {

    electricfurnace(true, TileElectricFurnace::new, RenderShape.MODEL, 8, false),
    electricfurnacedouble(true, TileElectricFurnaceDouble::new, RenderShape.MODEL, 8, false),
    electricfurnacetriple(true, TileElectricFurnaceTriple::new, RenderShape.MODEL, 8, false),
    electricarcfurnace(true, TileElectricArcFurnace::new, RenderShape.MODEL, 9, false),
    electricarcfurnacedouble(true, TileElectricArcFurnaceDouble::new, RenderShape.MODEL, 9, false),
    electricarcfurnacetriple(true, TileElectricArcFurnaceTriple::new, RenderShape.MODEL, 9, false),
    coalgenerator(true, TileCoalGenerator::new, RenderShape.MODEL, 12, false),
    wiremill(true, TileWireMill::new),
    wiremilldouble(true, TileWireMillDouble::new),
    wiremilltriple(true, TileWireMillTriple::new),
    mineralcrusher(true, TileMineralCrusher::new),
    mineralcrusherdouble(true, TileMineralCrusherDouble::new),
    mineralcrushertriple(true, TileMineralCrusherTriple::new),
    mineralgrinder(true, TileMineralGrinder::new),
    mineralgrinderdouble(true, TileMineralGrinderDouble::new),
    mineralgrindertriple(true, TileMineralGrinderTriple::new),
    batterybox(true, TileBatteryBox::new, RenderShape.ENTITYBLOCK_ANIMATED, 0, false),
    lithiumbatterybox(true, TileLithiumBatteryBox::new, RenderShape.ENTITYBLOCK_ANIMATED, 0, false),
    carbynebatterybox(true, TileCarbyneBatteryBox::new, RenderShape.ENTITYBLOCK_ANIMATED, 0, false),
    oxidationfurnace(true, TileOxidationFurnace::new, RenderShape.MODEL, 6, false),
    downgradetransformer(true, TileDowngradeTransformer::new),
    upgradetransformer(true, TileUpgradeTransformer::new),
    solarpanel(true, TileSolarPanel::new),
    advancedsolarpanel(true, TileAdvancedSolarPanel::new),
    electricpump(true, TileElectricPump::new),
    thermoelectricgenerator(true, TileThermoelectricGenerator::new),
    fermentationplant(true, TileFermentationPlant::new),
    combustionchamber(true, TileCombustionChamber::new),
    hydroelectricgenerator(true, TileHydroelectricGenerator::new),
    windmill(true, TileWindmill::new),
    mineralwasher(true, TileMineralWasher::new),
    chemicalmixer(true, TileChemicalMixer::new, RenderShape.ENTITYBLOCK_ANIMATED, 0, false),
    chemicalcrystallizer(true, TileChemicalCrystallizer::new),
    circuitbreaker(true, TileCircuitBreaker::new),
    multimeterblock(true, TileMultimeterBlock::new),
    energizedalloyer(true, TileEnergizedAlloyer::new, RenderShape.MODEL, 10, false),
    lathe(true, TileLathe::new),
    reinforcedalloyer(true, TileReinforcedAlloyer::new),
    chargerlv(true, TileChargerLV::new),
    chargermv(true, TileChargerMV::new),
    chargerhv(true, TileChargerHV::new),
    tanksteel(true, TileFluidTankSteel::new),
    tankreinforced(true, TileFluidTankReinforced::new, RenderShape.MODEL, 15, false),
    tankhsla(true, TileFluidTankHSLA::new),
    creativepowersource(true, TileCreativePowerSource::new),
    creativefluidsource(true, TileCreativeFluidSource::new),
    fluidvoid(true, TileFluidVoid::new),
    electrolyticseparator(true, TileElectrolyticSeparator::new),
    seismicrelay(true, TileSeismicRelay::new),
    quarry(true, TileQuarry::new),
    coolantresavoir(true, TileCoolantResavoir::new),
    motorcomplex(true, TileMotorComplex::new),
    gastanksteel(true, TileGasTankSteel::new),
    gastankreinforced(true, TileGasTankReinforced::new),
    gastankhsla(true, TileGasTankHSLA::new),
    gasvent(true, TileGasVent::new),
    relay(true, TileRelay::new),
    potentiometer(true, TilePotentiometer::new),
    advancedupgradetransformer(true, TileAdvancedUpgradeTransformer::new),
    advanceddowngradetransformer(true, TileAdvancedDowngradeTransformer::new),
    circuitmonitor(true, TileCircuitMonitor::new),
    currentregulator(true, TileCurrentRegulator::new),
    gascollector(true, TileGasCollector::new),
    creativegassource(true, TileCreativeGasSource::new),
    ;

    private final BlockEntityType.BlockEntitySupplier<BlockEntity> blockEntitySupplier;
    public final boolean showInItemGroup;
    private final RenderShape type;
    public final int litBrightness;
    public final boolean propogateLightDown;

    private SubtypeMachine(boolean showInItemGroup, BlockEntityType.BlockEntitySupplier<BlockEntity> blockEntitySupplier) {
        this(showInItemGroup, blockEntitySupplier, RenderShape.MODEL, 0, false);
    }

    private SubtypeMachine(boolean showInItemGroup, BlockEntityType.BlockEntitySupplier<BlockEntity> blockEntitySupplier, RenderShape renderShape, int litBrightness, boolean propogateLightDown) {
        this.showInItemGroup = showInItemGroup;
        this.blockEntitySupplier = blockEntitySupplier;
        this.litBrightness = litBrightness;
        this.type = renderShape;
        this.propogateLightDown = propogateLightDown;
    }

    public RenderShape getRenderType() {
        return type;
    }

    public BlockEntityType.BlockEntitySupplier<BlockEntity> getBlockEntitySupplier() {
        return blockEntitySupplier;
    }

    @Override
    public String tag() {
        return name();
    }

    @Override
    public String forgeTag() {
        return tag();
    }

    @Override
    public boolean isItem() {
        return false;
    }

    public boolean isPlayerStorable() {
        return this == quarry;
    }
}
