package electrodynamics.datagen.server;

import java.util.List;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeFluidPipe;
import electrodynamics.common.block.subtype.SubtypeGasPipe;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.block.subtype.SubtypeRawOreBlock;
import electrodynamics.common.block.subtype.SubtypeResourceBlock;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.datagen.utils.AbstractLootTableProvider;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ElectrodynamicsLootTablesProvider extends AbstractLootTableProvider {

    public ElectrodynamicsLootTablesProvider(String modID, HolderLookup.Provider provider) {
        super(provider, modID);
    }

    public ElectrodynamicsLootTablesProvider(HolderLookup.Provider provider) {
        this(References.ID, provider);
    }

    @Override
    protected void generate() {

        for (SubtypeFluidPipe pipe : SubtypeFluidPipe.values()) {
            addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_FLUIDPIPE.getValue(pipe));
        }

        for (SubtypeWire wire : SubtypeWire.values()) {
            addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_WIRE.getValue(wire));
        }

        for (SubtypeGasPipe pipe : SubtypeGasPipe.values()) {
            addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_GASPIPE.getValue(pipe));
        }

        for (SubtypeGlass glass : SubtypeGlass.values()) {
            addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(glass));
        }

        for (SubtypeOre ore : SubtypeOre.values()) {
            Block block = ElectrodynamicsBlocks.BLOCKS_ORE.getValue(ore);

            if (ore.nonSilkLootItem == null) {
                addSimpleBlock(block);
            } else {
                addFortuneAndSilkTouchTable(block, ore.nonSilkLootItem.get(), ore.minDrop, ore.maxDrop);
            }

        }

        for (SubtypeOreDeepslate ore : SubtypeOreDeepslate.values()) {
            Block block = ElectrodynamicsBlocks.BLOCKS_DEEPSLATEORE.getValue(ore);

            if (ore.nonSilkLootItem == null) {
                addSimpleBlock(block);
            } else {
                addFortuneAndSilkTouchTable(block, ore.nonSilkLootItem.get(), ore.minDrop, ore.maxDrop);
            }
        }

        for (SubtypeResourceBlock storage : SubtypeResourceBlock.values()) {
            addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_RESOURCE.getValue(storage));
        }

        for (SubtypeRawOreBlock raw : SubtypeRawOreBlock.values()) {
            addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_RAWORE.getValue(raw));
        }

        addSimpleBlock(ElectrodynamicsBlocks.BLOCK_LOGISTICALMANAGER.get());
        addSimpleBlock(ElectrodynamicsBlocks.BLOCK_SEISMICMARKER.get());

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnace), ElectrodynamicsTiles.TILE_ELECTRICFURNACE, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnacedouble), ElectrodynamicsTiles.TILE_ELECTRICFURNACEDOUBLE, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricfurnacetriple), ElectrodynamicsTiles.TILE_ELECTRICFURNACETRIPLE, true, false, false, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnace), ElectrodynamicsTiles.TILE_ELECTRICARCFURNACE, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnacedouble), ElectrodynamicsTiles.TILE_ELECTRICARCFURNACEDOUBLE, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricarcfurnacetriple), ElectrodynamicsTiles.TILE_ELECTRICARCFURNACETRIPLE, true, false, false, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremill), ElectrodynamicsTiles.TILE_WIREMILL, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremilldouble), ElectrodynamicsTiles.TILE_WIREMILLDOUBLE, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.wiremilltriple), ElectrodynamicsTiles.TILE_WIREMILLTRIPLE, true, false, false, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrusher), ElectrodynamicsTiles.TILE_MINERALCRUSHER, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrusherdouble), ElectrodynamicsTiles.TILE_MINERALCRUSHERDOUBLE, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrushertriple), ElectrodynamicsTiles.TILE_MINERALCRUSHERTRIPLE, true, false, false, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrinder), ElectrodynamicsTiles.TILE_MINERALGRINDER, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrinderdouble), ElectrodynamicsTiles.TILE_MINERALGRINDERDOUBLE, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrindertriple), ElectrodynamicsTiles.TILE_MINERALGRINDERTRIPLE, true, false, false, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.batterybox), ElectrodynamicsTiles.TILE_BATTERYBOX, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.lithiumbatterybox), ElectrodynamicsTiles.TILE_LITHIUMBATTERYBOX, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.carbynebatterybox), ElectrodynamicsTiles.TILE_CARBYNEBATTERYBOX, true, false, false, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.oxidationfurnace), ElectrodynamicsTiles.TILE_OXIDATIONFURNACE, true, false, false, true, false);
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.downgradetransformer));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.upgradetransformer));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.advancedupgradetransformer));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.advanceddowngradetransformer));
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.coalgenerator), ElectrodynamicsTiles.TILE_COALGENERATOR, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.solarpanel), ElectrodynamicsTiles.TILE_SOLARPANEL, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.advancedsolarpanel), ElectrodynamicsTiles.TILE_ADVANCEDSOLARPANEL, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electricpump), ElectrodynamicsTiles.TILE_ELECTRICPUMP, false, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.thermoelectricgenerator), ElectrodynamicsTiles.TILE_THERMOELECTRICGENERATOR, false, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fermentationplant), ElectrodynamicsTiles.TILE_FERMENTATIONPLANT, true, true, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.combustionchamber), ElectrodynamicsTiles.TILE_COMBUSTIONCHAMBER, true, true, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.hydroelectricgenerator), ElectrodynamicsTiles.TILE_HYDROELECTRICGENERATOR, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.windmill), ElectrodynamicsTiles.TILE_WINDMILL, true, false, false, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralwasher), ElectrodynamicsTiles.TILE_MINERALWASHER, true, true, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chemicalmixer), ElectrodynamicsTiles.TILE_CHEMICALMIXER, true, true, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chemicalcrystallizer), ElectrodynamicsTiles.TILE_CHEMICALCRYSTALLIZER, true, true, false, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.circuitbreaker), ElectrodynamicsTiles.TILE_CIRCUITBREAKER, false, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.multimeterblock), ElectrodynamicsTiles.TILE_MULTIMETERBLOCK, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.energizedalloyer), ElectrodynamicsTiles.TILE_ENERGIZEDALLOYER, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.lathe), ElectrodynamicsTiles.TILE_LATHE, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.reinforcedalloyer), ElectrodynamicsTiles.TILE_REINFORCEDALLOYER, true, false, false, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chargerlv), ElectrodynamicsTiles.TILE_CHARGERLV, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chargermv), ElectrodynamicsTiles.TILE_CHARGERMV, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.chargerhv), ElectrodynamicsTiles.TILE_CHARGERHV, true, false, false, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel), ElectrodynamicsTiles.TILE_TANKSTEEL, true, true, false, false, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tankreinforced), ElectrodynamicsTiles.TILE_TANKREINFORCED, true, true, false, false, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tankhsla), ElectrodynamicsTiles.TILE_TANKHSLA, true, true, false, false, false);

        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.creativepowersource));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.creativefluidsource));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.creativegassource));
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidvoid), ElectrodynamicsTiles.TILE_FLUIDVOID, true, false, false, false, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolyticseparator), ElectrodynamicsTiles.TILE_ELECTROLYTICSEPARATOR, true, true, false, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.seismicrelay), ElectrodynamicsTiles.TILE_SEISMICRELAY, true, false, false, false, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.quarry), ElectrodynamicsTiles.TILE_QUARRY, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.coolantresavoir), ElectrodynamicsTiles.TILE_COOLANTRESAVOIR, true, false, false, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.motorcomplex), ElectrodynamicsTiles.TILE_MOTORCOMPLEX, true, false, false, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCK_COMPRESSOR.get(), ElectrodynamicsTiles.TILE_COMPRESSOR, true, false, true, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCK_DECOMPRESSOR.get(), ElectrodynamicsTiles.TILE_DECOMPRESSOR, true, false, true, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR.get(), ElectrodynamicsTiles.TILE_THERMOELECTRIC_MANIPULATOR, true, true, true, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCK_ADVANCEDCOMPRESSOR.get(), ElectrodynamicsTiles.TILE_ADVANCEDCOMPRESSOR, true, false, true, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCK_ADVANCEDDECOMPRESSOR.get(), ElectrodynamicsTiles.TILE_ADVANCEDDECOMPRESSOR, true, false, true, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCK_ADVANCED_THERMOELECTRICMANIPULATOR.get(), ElectrodynamicsTiles.TILE_ADVANCED_THERMOELECTRIC_MANIPULATOR, true, true, true, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gastanksteel), ElectrodynamicsTiles.TILE_GASTANK_STEEL, true, false, true, false, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gastankreinforced), ElectrodynamicsTiles.TILE_GASTANK_REINFORCED, true, false, true, false, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gastankhsla), ElectrodynamicsTiles.TILE_GASTANK_HSLA, true, false, true, false, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolyticseparator), ElectrodynamicsTiles.TILE_ELECTROLYTICSEPARATOR, true, true, true, true, false);

        addMachineTable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gascollector), ElectrodynamicsTiles.TILE_GASCOLLECTOR, true, false, true, true, false);
        addMachineTable(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOR.get(), ElectrodynamicsTiles.TILE_CHEMICALREACTOR, true, true, true, true, true);


        addSimpleBlock(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK.get());

        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gasvalve));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidvalve));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gaspipepump));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidpipepump));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gaspipefilter));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidpipefilter));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gasvent));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.potentiometer));

        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.relay));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.circuitmonitor));
        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.currentregulator));

        addSimpleBlock(ElectrodynamicsBlocks.BLOCK_STEELSCAFFOLDING.get());

        addSimpleBlock(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolosischamber));

        addSimpleBlock(ElectrodynamicsBlocks.BLOCK_ROTARYUNIFIER);

    }

    public <T extends GenericTile> void addMachineTable(Block block, DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> tilereg, boolean items, boolean fluids, boolean gases, boolean energy, boolean additional) {
        add(block, machineTable(name(block), block, tilereg.get(), items, fluids, gases, energy, additional));
    }

    /**
     * Adds the block to the loottables silk touch only
     *
     * @param reg The block that will be added
     * @author SeaRobber69
     */
    public void addSilkTouchOnlyTable(DeferredHolder<Block, ? extends Block> reg) {
        Block block = reg.get();
        add(block, createSilkTouchOnlyTable(name(block), block));
    }

    public void addFortuneAndSilkTouchTable(DeferredHolder<Block, ? extends Block> reg, Item nonSilk, int minDrop, int maxDrop) {
        addFortuneAndSilkTouchTable(reg.get(), nonSilk, minDrop, maxDrop);
    }

    public void addFortuneAndSilkTouchTable(Block block, Item nonSilk, int minDrop, int maxDrop) {
        add(block, createSilkTouchAndFortuneTable(name(block), block, nonSilk, minDrop, maxDrop));
    }

    public void addSimpleBlock(DeferredHolder<Block, ? extends Block> reg) {
        addSimpleBlock(reg.get());
    }

    public void addSimpleBlock(Block block) {

        add(block, createSimpleBlockTable(name(block), block));
    }

    public String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    @Override
    public List<Block> getExcludedBlocks() {
        return List.of(ElectrodynamicsBlocks.BLOCK_MULTISUBNODE.get(), ElectrodynamicsBlocks.BLOCK_FRAME.get(), ElectrodynamicsBlocks.BLOCK_FRAME_CORNER.get(), ElectrodynamicsBlocks.BLOCK_COMPRESSOR_SIDE.get(), ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOREXTRA_MIDDLE.get(), ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOREXTRA_TOP.get(),
                ElectrodynamicsBlocks.BLOCK_MULTIBLOCK_SLAVE.get());
    }

}
