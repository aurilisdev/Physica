package electrodynamics.common.block.subtype;

import electrodynamics.api.ISubtype;
import electrodynamics.api.multiblock.subnodebased.Subnode;
import electrodynamics.api.tile.IMachine;
import electrodynamics.api.tile.MachineProperties;
import electrodynamics.common.block.voxelshapes.ElectrodynamicsVoxelShapes;
import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
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
import electrodynamics.common.tile.pipelines.fluid.*;
import electrodynamics.common.tile.pipelines.gas.*;
import electrodynamics.common.tile.pipelines.fluid.tank.TileFluidTankHSLA;
import electrodynamics.common.tile.pipelines.fluid.tank.TileFluidTankReinforced;
import electrodynamics.common.tile.pipelines.fluid.tank.TileFluidTankSteel;
import electrodynamics.common.tile.pipelines.gas.tank.TileGasTankHSLA;
import electrodynamics.common.tile.pipelines.gas.tank.TileGasTankReinforced;
import electrodynamics.common.tile.pipelines.gas.tank.TileGasTankSteel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public enum SubtypeMachine implements ISubtype, IMachine {

    electricfurnace(true, TileElectricFurnace::new, MachineProperties.builder().setLitBrightness(8)),
    electricfurnacedouble(true, TileElectricFurnaceDouble::new, MachineProperties.builder().setLitBrightness(8)),
    electricfurnacetriple(true, TileElectricFurnaceTriple::new, MachineProperties.builder().setLitBrightness(8)),
    electricarcfurnace(true, TileElectricArcFurnace::new, MachineProperties.builder().setLitBrightness(9)),
    electricarcfurnacedouble(true, TileElectricArcFurnaceDouble::new, MachineProperties.builder().setLitBrightness(9)),
    electricarcfurnacetriple(true, TileElectricArcFurnaceTriple::new, MachineProperties.builder().setLitBrightness(9)),
    coalgenerator(true, TileCoalGenerator::new, MachineProperties.builder().setLitBrightness(12)),
    wiremill(true, TileWireMill::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.WIREMILL)),
    wiremilldouble(true, TileWireMillDouble::new),
    wiremilltriple(true, TileWireMillTriple::new),
    mineralcrusher(true, TileMineralCrusher::new),
    mineralcrusherdouble(true, TileMineralCrusherDouble::new),
    mineralcrushertriple(true, TileMineralCrusherTriple::new),
    mineralgrinder(true, TileMineralGrinder::new),
    mineralgrinderdouble(true, TileMineralGrinderDouble::new),
    mineralgrindertriple(true, TileMineralGrinderTriple::new),
    batterybox(true, TileBatteryBox::new, MachineProperties.builder().setRenderShape(RenderShape.ENTITYBLOCK_ANIMATED)),
    lithiumbatterybox(true, TileLithiumBatteryBox::new, MachineProperties.builder().setRenderShape(RenderShape.ENTITYBLOCK_ANIMATED)),
    carbynebatterybox(true, TileCarbyneBatteryBox::new, MachineProperties.builder().setRenderShape(RenderShape.ENTITYBLOCK_ANIMATED)),
    oxidationfurnace(true, TileOxidationFurnace::new, MachineProperties.builder().setLitBrightness(6)),
    downgradetransformer(true, TileDowngradeTransformer::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.DOWNGRADE_TRANSFORMER)),
    upgradetransformer(true, TileUpgradeTransformer::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.UPGRADE_TRANSFORMER)),
    solarpanel(true, TileSolarPanel::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.SOLAR_PANEL)),
    advancedsolarpanel(true, TileAdvancedSolarPanel::new, MachineProperties.builder().setSubnodes(Subnodes.SUBNODES_ADVANCEDSOLARPANEL).setShapeProvider(ElectrodynamicsVoxelShapes.ADVANCED_SOLAR_PANEL)),
    electricpump(true, TileElectricPump::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.ELECTRIC_PUMP)),
    thermoelectricgenerator(true, TileThermoelectricGenerator::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.THERMOELECTRIC_GENERATOR)),
    fermentationplant(true, TileFermentationPlant::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.FERMENTATION_PLANT)),
    combustionchamber(true, TileCombustionChamber::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.COMBUSTION_CHAMBER)),
    hydroelectricgenerator(true, TileHydroelectricGenerator::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.HYDROELECTRIC_GENERATOR)),
    windmill(true, TileWindmill::new, MachineProperties.builder().setSubnodes(Subnodes.SUBNODES_WINDMILL).setShapeProvider(ElectrodynamicsVoxelShapes.WINDMILL)),
    mineralwasher(true, TileMineralWasher::new),
    chemicalmixer(true, TileChemicalMixer::new, MachineProperties.builder().setRenderShape(RenderShape.ENTITYBLOCK_ANIMATED)),
    chemicalcrystallizer(true, TileChemicalCrystallizer::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.CHEMICAL_CRYSTALIZER)),
    circuitbreaker(true, TileCircuitBreaker::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.CIRCUIT_BREAKER)),
    multimeterblock(true, TileMultimeterBlock::new),
    energizedalloyer(true, TileEnergizedAlloyer::new, MachineProperties.builder().setLitBrightness(10)),
    lathe(true, TileLathe::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.LATHE)),
    reinforcedalloyer(true, TileReinforcedAlloyer::new, MachineProperties.builder().setLitBrightness(15)),
    chargerlv(true, TileChargerLV::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.LV_CHARGER)),
    chargermv(true, TileChargerMV::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.MV_CHARGER)),
    chargerhv(true, TileChargerHV::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.HV_CHARGER)),
    tanksteel(true, TileFluidTankSteel::new),
    tankreinforced(true, TileFluidTankReinforced::new),
    tankhsla(true, TileFluidTankHSLA::new),
    creativepowersource(true, TileCreativePowerSource::new),
    creativefluidsource(true, TileCreativeFluidSource::new),
    creativegassource(true, TileCreativeGasSource::new),
    fluidvoid(true, TileFluidVoid::new),
    electrolyticseparator(true, TileElectrolyticSeparator::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.ELECTROLYTIC_SEPARATOR)),
    seismicrelay(true, TileSeismicRelay::new),
    quarry(true, TileQuarry::new),
    coolantresavoir(true, TileCoolantResavoir::new),
    motorcomplex(true, TileMotorComplex::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.MOTOR_COMPLEX)),
    gastanksteel(true, TileGasTankSteel::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.STEEL_GAS_TANK)),
    gastankreinforced(true, TileGasTankReinforced::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.REINFORCED_GAS_TANK)),
    gastankhsla(true, TileGasTankHSLA::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.HSLA_GAS_TANK)),
    gasvent(true, TileGasVent::new),
    relay(true, TileRelay::new),
    potentiometer(true, TilePotentiometer::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.POTENTIOMETER)),
    advancedupgradetransformer(true, TileAdvancedUpgradeTransformer::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.UPGRADE_TRANSFORMER_MK2)),
    advanceddowngradetransformer(true, TileAdvancedDowngradeTransformer::new, MachineProperties.builder().setShapeProvider(ElectrodynamicsVoxelShapes.DOWNGRADE_TRANSFORMER_MK2)),
    circuitmonitor(true, TileCircuitMonitor::new),
    currentregulator(true, TileCurrentRegulator::new),
    gascollector(true, TileGasCollector::new),
    electrolosischamber(true, TileElectrolosisChamber::new),
    fluidvalve(true, TileFluidValve::new, MachineProperties.builder().setPropegateLightDown().setShapeProvider(ElectrodynamicsVoxelShapes.FLUID_PIPE_VALVE)),
    fluidpipepump(true, TileFluidPipePump::new, MachineProperties.builder().setPropegateLightDown().setShapeProvider(ElectrodynamicsVoxelShapes.FLUID_PIPE_PUMP)),
    fluidpipefilter(true, TileFluidPipeFilter::new, MachineProperties.builder().setPropegateLightDown().setShapeProvider(ElectrodynamicsVoxelShapes.FLUID_PIPE_FILTER)),
    gasvalve(true, TileGasValve::new, MachineProperties.builder().setPropegateLightDown().setShapeProvider(ElectrodynamicsVoxelShapes.GAS_PIPE_VALVE)),
    gaspipepump(true, TileGasPipePump::new, MachineProperties.builder().setPropegateLightDown().setShapeProvider(ElectrodynamicsVoxelShapes.GAS_PIPE_PUMP)),
    gaspipefilter(true, TileGasPipeFilter::new, MachineProperties.builder().setPropegateLightDown().setShapeProvider(ElectrodynamicsVoxelShapes.GAS_PIPE_FILTER));

    private final BlockEntityType.BlockEntitySupplier<BlockEntity> blockEntitySupplier;
    private final boolean showInItemGroup;
    private final MachineProperties properties;

    private SubtypeMachine(boolean showInItemGroup, BlockEntityType.BlockEntitySupplier<BlockEntity> blockEntitySupplier) {
        this(showInItemGroup, blockEntitySupplier, MachineProperties.DEFAULT);
    }

    private SubtypeMachine(boolean showInItemGroup, BlockEntityType.BlockEntitySupplier<BlockEntity> blockEntitySupplier, MachineProperties properties) {
        this.showInItemGroup = showInItemGroup;
        this.blockEntitySupplier = blockEntitySupplier;
        this.properties = properties;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<BlockEntity> getBlockEntitySupplier() {
        return blockEntitySupplier;
    }

    @Override
    public int getLitBrightness() {
        return properties.litBrightness;
    }

    @Override
    public RenderShape getRenderShape() {
        return properties.renderShape;
    }

    @Override
    public boolean isMultiblock() {
        return properties.isMultiblock;
    }

    @Override
    public boolean propegatesLightDown() {
        return properties.propegatesLightDown;
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

    @Override
    public Subnode[] getSubnodes() {
        return properties.subnodes;
    }

    @Override
    public VoxelShapeProvider getVoxelShapeProvider() {
        return properties.provider;
    }

    public boolean showInItemGroup() {
        return showInItemGroup;
    }


    public static class Subnodes {

        public static final Subnode[] SUBNODES_WINDMILL = {new Subnode(
                //
                new BlockPos(0, 1, 0),
                //
                new VoxelShape[]{
                        //
                        Shapes.block(),
                        //
                        Shapes.block(),
                        //
                        Shapes.or(Block.box(5, 0, 5, 11, 16, 11), Block.box(5, 10, 3, 11, 16, 13)),
                        //
                        Shapes.or(Block.box(5, 0, 5, 11, 16, 11), Block.box(5, 10, 3, 11, 16, 13)),
                        //
                        Shapes.or(Block.box(5, 0, 5, 11, 16, 11), Block.box(3, 10, 5, 13, 16, 11)),
                        //
                        Shapes.or(Block.box(5, 0, 5, 11, 16, 11), Block.box(3, 10, 5, 13, 16, 11))
                })
        };

        public static final Subnode[] SUBNODES_ADVANCEDSOLARPANEL = make(() -> {

            Subnode[] subnodes = new Subnode[9];

            int counter = 0;

            int radius = 1;

            for (int i = -radius; i <= radius; i++) {

                for (int j = -radius; j <= radius; j++) {
                    if (i == 0 && j == 0) {
                        subnodes[counter] = new Subnode(
                                //
                                new BlockPos(i, 1, j),
                                //
                                Shapes.or(Block.box(6, 0, 6, 10, 16, 10), Block.box(5, 13, 5, 11, 16, 11), Block.box(0, 14, 0, 16, 16, 16)));
                    } else {
                        subnodes[counter] = new Subnode(new BlockPos(i, 1, j), Block.box(0, 14, 0, 16, 16, 16));
                    }

                    counter++;
                }
            }

            return subnodes;

        });


        public static Subnode[] make(Supplier<Subnode[]> maker) {
            return maker.get();
        }

    }


}
