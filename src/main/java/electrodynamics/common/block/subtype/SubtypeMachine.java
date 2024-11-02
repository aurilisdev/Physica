package electrodynamics.common.block.subtype;

import java.lang.reflect.InvocationTargetException;

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
import electrodynamics.common.tile.pipelines.TileCreativeFluidSource;
import electrodynamics.common.tile.pipelines.TileElectricPump;
import electrodynamics.common.tile.pipelines.fluids.TileFluidVoid;
import electrodynamics.common.tile.pipelines.gas.TileGasCollector;
import electrodynamics.common.tile.pipelines.gas.TileGasVent;
import electrodynamics.common.tile.pipelines.tanks.fluid.TileFluidTankHSLA;
import electrodynamics.common.tile.pipelines.tanks.fluid.TileFluidTankReinforced;
import electrodynamics.common.tile.pipelines.tanks.fluid.TileFluidTankSteel;
import electrodynamics.common.tile.pipelines.tanks.gas.TileGasTankHSLA;
import electrodynamics.common.tile.pipelines.tanks.gas.TileGasTankReinforced;
import electrodynamics.common.tile.pipelines.tanks.gas.TileGasTankSteel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public enum SubtypeMachine implements ISubtype {

    electricfurnace(true, TileElectricFurnace.class, RenderShape.MODEL, 8, false),
    electricfurnacedouble(true, TileElectricFurnaceDouble.class, RenderShape.MODEL, 8, false),
    electricfurnacetriple(true, TileElectricFurnaceTriple.class, RenderShape.MODEL, 8, false),
    electricarcfurnace(true, TileElectricArcFurnace.class, RenderShape.MODEL, 9, false),
    electricarcfurnacedouble(true, TileElectricArcFurnaceDouble.class, RenderShape.MODEL, 9, false),
    electricarcfurnacetriple(true, TileElectricArcFurnaceTriple.class, RenderShape.MODEL, 9, false),
    coalgenerator(true, TileCoalGenerator.class, RenderShape.MODEL, 12, false),
    wiremill(true, TileWireMill.class),
    wiremilldouble(true, TileWireMillDouble.class),
    wiremilltriple(true, TileWireMillTriple.class),
    mineralcrusher(true, TileMineralCrusher.class),
    mineralcrusherdouble(true, TileMineralCrusherDouble.class),
    mineralcrushertriple(true, TileMineralCrusherTriple.class),
    mineralgrinder(true, TileMineralGrinder.class),
    mineralgrinderdouble(true, TileMineralGrinderDouble.class),
    mineralgrindertriple(true, TileMineralGrinderTriple.class),
    batterybox(true, TileBatteryBox.class, RenderShape.ENTITYBLOCK_ANIMATED, 0, false),
    lithiumbatterybox(true, TileLithiumBatteryBox.class, RenderShape.ENTITYBLOCK_ANIMATED, 0, false),
    carbynebatterybox(true, TileCarbyneBatteryBox.class, RenderShape.ENTITYBLOCK_ANIMATED, 0, false),
    oxidationfurnace(true, TileOxidationFurnace.class, RenderShape.MODEL, 6, false),
    downgradetransformer(true, TileDowngradeTransformer.class),
    upgradetransformer(true, TileUpgradeTransformer.class),
    solarpanel(true, TileSolarPanel.class),
    advancedsolarpanel(true, TileAdvancedSolarPanel.class),
    electricpump(true, TileElectricPump.class),
    thermoelectricgenerator(true, TileThermoelectricGenerator.class),
    fermentationplant(true, TileFermentationPlant.class),
    combustionchamber(true, TileCombustionChamber.class),
    hydroelectricgenerator(true, TileHydroelectricGenerator.class),
    windmill(true, TileWindmill.class),
    mineralwasher(true, TileMineralWasher.class),
    chemicalmixer(true, TileChemicalMixer.class, RenderShape.ENTITYBLOCK_ANIMATED, 0, false),
    chemicalcrystallizer(true, TileChemicalCrystallizer.class),
    circuitbreaker(true, TileCircuitBreaker.class),
    multimeterblock(true, TileMultimeterBlock.class),
    energizedalloyer(true, TileEnergizedAlloyer.class, RenderShape.MODEL, 10, false),
    lathe(true, TileLathe.class),
    reinforcedalloyer(true, TileReinforcedAlloyer.class),
    chargerlv(true, TileChargerLV.class),
    chargermv(true, TileChargerMV.class),
    chargerhv(true, TileChargerHV.class),
    tanksteel(true, TileFluidTankSteel.class),
    tankreinforced(true, TileFluidTankReinforced.class, RenderShape.MODEL, 15, false),
    tankhsla(true, TileFluidTankHSLA.class),
    creativepowersource(true, TileCreativePowerSource.class),
    creativefluidsource(true, TileCreativeFluidSource.class),
    fluidvoid(true, TileFluidVoid.class),
    electrolyticseparator(true, TileElectrolyticSeparator.class),
    seismicrelay(true, TileSeismicRelay.class),
    quarry(true, TileQuarry.class),
    coolantresavoir(true, TileCoolantResavoir.class),
    motorcomplex(true, TileMotorComplex.class),
    gastanksteel(true, TileGasTankSteel.class),
    gastankreinforced(true, TileGasTankReinforced.class),
    gastankhsla(true, TileGasTankHSLA.class),
    gasvent(true, TileGasVent.class),
    relay(true, TileRelay.class),
    potentiometer(true, TilePotentiometer.class),
    advancedupgradetransformer(true, TileAdvancedUpgradeTransformer.class),
    advanceddowngradetransformer(true, TileAdvancedDowngradeTransformer.class),
    circuitmonitor(true, TileCircuitMonitor.class),
    currentregulator(true, TileCurrentRegulator.class),

    gascollector(true, TileGasCollector.class),
    ;

    public final Class<? extends BlockEntity> tileclass;
    public final boolean showInItemGroup;
    private RenderShape type = RenderShape.MODEL;
    public final int litBrightness;
    public final boolean propogateLightDown;

    SubtypeMachine(boolean showInItemGroup, Class<? extends BlockEntity> tileclass) {
        this(showInItemGroup, tileclass, RenderShape.MODEL, 0, false);
    }

    SubtypeMachine(boolean showInItemGroup, Class<? extends BlockEntity> tileclass, RenderShape renderShape, int litBrightness, boolean propogateLightDown) {
        this.showInItemGroup = showInItemGroup;
        this.tileclass = tileclass;
        this.litBrightness = litBrightness;
        this.type = renderShape;
        this.propogateLightDown = propogateLightDown;
    }

    public RenderShape getRenderType() {
        return type;
    }

    public BlockEntity createTileEntity(BlockPos pos, BlockState state) {
        if (tileclass != null) {
            try {
                return tileclass.getConstructor(BlockPos.class, BlockState.class).newInstance(pos, state);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }
        return null;
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
