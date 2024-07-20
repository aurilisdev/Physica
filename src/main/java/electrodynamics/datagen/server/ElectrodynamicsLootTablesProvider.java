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
import electrodynamics.registers.ElectrodynamicsBlockTypes;
import electrodynamics.registers.ElectrodynamicsBlocks;
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
			addSimpleBlock(ElectrodynamicsBlocks.getBlock(pipe));
		}

		for (SubtypeWire wire : SubtypeWire.values()) {
			addSimpleBlock(ElectrodynamicsBlocks.getBlock(wire));
		}

		for (SubtypeGasPipe pipe : SubtypeGasPipe.values()) {
			addSimpleBlock(ElectrodynamicsBlocks.getBlock(pipe));
		}

		for (SubtypeGlass glass : SubtypeGlass.values()) {
			addSimpleBlock(ElectrodynamicsBlocks.getBlock(glass));
		}

		for (SubtypeOre ore : SubtypeOre.values()) {
			Block block = ElectrodynamicsBlocks.getBlock(ore);

			if (ore.nonSilkLootItem == null) {
				addSimpleBlock(block);
			} else {
				addFortuneAndSilkTouchTable(block, ore.nonSilkLootItem.get(), ore.minDrop, ore.maxDrop);
			}

		}

		for (SubtypeOreDeepslate ore : SubtypeOreDeepslate.values()) {
			Block block = ElectrodynamicsBlocks.getBlock(ore);

			if (ore.nonSilkLootItem == null) {
				addSimpleBlock(block);
			} else {
				addFortuneAndSilkTouchTable(block, ore.nonSilkLootItem.get(), ore.minDrop, ore.maxDrop);
			}
		}

		for (SubtypeResourceBlock storage : SubtypeResourceBlock.values()) {
			addSimpleBlock(ElectrodynamicsBlocks.getBlock(storage));
		}

		for (SubtypeRawOreBlock raw : SubtypeRawOreBlock.values()) {
			addSimpleBlock(ElectrodynamicsBlocks.getBlock(raw));
		}

		addSimpleBlock(ElectrodynamicsBlocks.blockLogisticalManager);
		addSimpleBlock(ElectrodynamicsBlocks.blockSeismicMarker);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnace), ElectrodynamicsBlockTypes.TILE_ELECTRICFURNACE, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnacedouble), ElectrodynamicsBlockTypes.TILE_ELECTRICFURNACEDOUBLE, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricfurnacetriple), ElectrodynamicsBlockTypes.TILE_ELECTRICFURNACETRIPLE, true, false, false, true, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnace), ElectrodynamicsBlockTypes.TILE_ELECTRICARCFURNACE, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnacedouble), ElectrodynamicsBlockTypes.TILE_ELECTRICARCFURNACEDOUBLE, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricarcfurnacetriple), ElectrodynamicsBlockTypes.TILE_ELECTRICARCFURNACETRIPLE, true, false, false, true, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremill), ElectrodynamicsBlockTypes.TILE_WIREMILL, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremilldouble), ElectrodynamicsBlockTypes.TILE_WIREMILLDOUBLE, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.wiremilltriple), ElectrodynamicsBlockTypes.TILE_WIREMILLTRIPLE, true, false, false, true, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrusher), ElectrodynamicsBlockTypes.TILE_MINERALCRUSHER, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrusherdouble), ElectrodynamicsBlockTypes.TILE_MINERALCRUSHERDOUBLE, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrushertriple), ElectrodynamicsBlockTypes.TILE_MINERALCRUSHERTRIPLE, true, false, false, true, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrinder), ElectrodynamicsBlockTypes.TILE_MINERALGRINDER, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrinderdouble), ElectrodynamicsBlockTypes.TILE_MINERALGRINDERDOUBLE, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrindertriple), ElectrodynamicsBlockTypes.TILE_MINERALGRINDERTRIPLE, true, false, false, true, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.batterybox), ElectrodynamicsBlockTypes.TILE_BATTERYBOX, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.lithiumbatterybox), ElectrodynamicsBlockTypes.TILE_LITHIUMBATTERYBOX, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.carbynebatterybox), ElectrodynamicsBlockTypes.TILE_CARBYNEBATTERYBOX, true, false, false, true, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.oxidationfurnace), ElectrodynamicsBlockTypes.TILE_OXIDATIONFURNACE, true, false, false, true, false);
		addSimpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.downgradetransformer));
		addSimpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.upgradetransformer));
		addSimpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.advancedupgradetransformer));
		addSimpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.advanceddowngradetransformer));
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.coalgenerator), ElectrodynamicsBlockTypes.TILE_COALGENERATOR, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.solarpanel), ElectrodynamicsBlockTypes.TILE_SOLARPANEL, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.advancedsolarpanel), ElectrodynamicsBlockTypes.TILE_ADVANCEDSOLARPANEL, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electricpump), ElectrodynamicsBlockTypes.TILE_ELECTRICPUMP, false, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.thermoelectricgenerator), ElectrodynamicsBlockTypes.TILE_THERMOELECTRICGENERATOR, false, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.fermentationplant), ElectrodynamicsBlockTypes.TILE_FERMENTATIONPLANT, true, true, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.combustionchamber), ElectrodynamicsBlockTypes.TILE_COMBUSTIONCHAMBER, true, true, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.hydroelectricgenerator), ElectrodynamicsBlockTypes.TILE_HYDROELECTRICGENERATOR, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.windmill), ElectrodynamicsBlockTypes.TILE_WINDMILL, true, false, false, true, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralwasher), ElectrodynamicsBlockTypes.TILE_MINERALWASHER, true, true, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chemicalmixer), ElectrodynamicsBlockTypes.TILE_CHEMICALMIXER, true, true, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chemicalcrystallizer), ElectrodynamicsBlockTypes.TILE_CHEMICALCRYSTALLIZER, true, true, false, true, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.circuitbreaker), ElectrodynamicsBlockTypes.TILE_CIRCUITBREAKER, false, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.multimeterblock), ElectrodynamicsBlockTypes.TILE_MULTIMETERBLOCK, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.energizedalloyer), ElectrodynamicsBlockTypes.TILE_ENERGIZEDALLOYER, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.lathe), ElectrodynamicsBlockTypes.TILE_LATHE, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.reinforcedalloyer), ElectrodynamicsBlockTypes.TILE_REINFORCEDALLOYER, true, false, false, true, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chargerlv), ElectrodynamicsBlockTypes.TILE_CHARGERLV, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chargermv), ElectrodynamicsBlockTypes.TILE_CHARGERMV, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.chargerhv), ElectrodynamicsBlockTypes.TILE_CHARGERHV, true, false, false, true, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.tanksteel), ElectrodynamicsBlockTypes.TILE_TANKSTEEL, true, true, false, false, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.tankreinforced), ElectrodynamicsBlockTypes.TILE_TANKREINFORCED, true, true, false, false, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.tankhsla), ElectrodynamicsBlockTypes.TILE_TANKHSLA, true, true, false, false, false);

		addSimpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.creativepowersource));
		addSimpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.creativefluidsource));
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.fluidvoid), ElectrodynamicsBlockTypes.TILE_FLUIDVOID, true, false, false, false, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electrolyticseparator), ElectrodynamicsBlockTypes.TILE_ELECTROLYTICSEPARATOR, true, true, false, true, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.seismicrelay), ElectrodynamicsBlockTypes.TILE_SEISMICRELAY, true, false, false, false, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.quarry), ElectrodynamicsBlockTypes.TILE_QUARRY, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.coolantresavoir), ElectrodynamicsBlockTypes.TILE_COOLANTRESAVOIR, true, false, false, true, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.motorcomplex), ElectrodynamicsBlockTypes.TILE_MOTORCOMPLEX, true, false, false, true, false);

		addMachineTable(ElectrodynamicsBlocks.blockCompressor, ElectrodynamicsBlockTypes.TILE_COMPRESSOR, true, false, true, true, false);
		addMachineTable(ElectrodynamicsBlocks.blockDecompressor, ElectrodynamicsBlockTypes.TILE_DECOMPRESSOR, true, false, true, true, false);
		addMachineTable(ElectrodynamicsBlocks.blockThermoelectricManipulator, ElectrodynamicsBlockTypes.TILE_THERMOELECTRIC_MANIPULATOR, true, true, true, true, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gastanksteel), ElectrodynamicsBlockTypes.TILE_GASTANK_STEEL, true, false, true, false, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gastankreinforced), ElectrodynamicsBlockTypes.TILE_GASTANK_REINFORCED, true, false, true, false, false);
		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gastankhsla), ElectrodynamicsBlockTypes.TILE_GASTANK_HSLA, true, false, true, false, false);

		addMachineTable(ElectrodynamicsBlocks.getBlock(SubtypeMachine.electrolyticseparator), ElectrodynamicsBlockTypes.TILE_ELECTROLYTICSEPARATOR, true, true, true, true, false);

		addSimpleBlock(ElectrodynamicsBlocks.blockGasTransformerAddonTank);

		addSimpleBlock(ElectrodynamicsBlocks.blockGasValve);
		addSimpleBlock(ElectrodynamicsBlocks.blockFluidValve);
		addSimpleBlock(ElectrodynamicsBlocks.blockGasPipePump);
		addSimpleBlock(ElectrodynamicsBlocks.blockFluidPipePump);
		addSimpleBlock(ElectrodynamicsBlocks.blockGasPipeFilter);
		addSimpleBlock(ElectrodynamicsBlocks.blockFluidPipeFilter);
		addSimpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.gasvent));
		addSimpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.potentiometer));

		addSimpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.relay));
		addSimpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.circuitmonitor));
		addSimpleBlock(ElectrodynamicsBlocks.getBlock(SubtypeMachine.currentregulator));

		addSimpleBlock(ElectrodynamicsBlocks.BLOCK_STEELSCAFFOLDING.get());

	}

	public <T extends GenericTile> void addMachineTable(Block block, DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> tilereg, boolean items, boolean fluids, boolean gases, boolean energy, boolean additional) {
		add(block, machineTable(name(block), block, tilereg.get(), items, fluids, gases, energy, additional));
	}

	/**
	 * Adds the block to the loottables silk touch only
	 *
	 * @author SeaRobber69
	 * @param reg The block that will be added
	 */
	public void addSilkTouchOnlyTable(DeferredHolder<Block, Block> reg) {
		Block block = reg.get();
		add(block, createSilkTouchOnlyTable(name(block), block));
	}

	public void addFortuneAndSilkTouchTable(DeferredHolder<Block, Block> reg, Item nonSilk, int minDrop, int maxDrop) {
		addFortuneAndSilkTouchTable(reg.get(), nonSilk, minDrop, maxDrop);
	}

	public void addFortuneAndSilkTouchTable(Block block, Item nonSilk, int minDrop, int maxDrop) {
		add(block, createSilkTouchAndFortuneTable(name(block), block, nonSilk, minDrop, maxDrop));
	}

	public void addSimpleBlock(DeferredHolder<Block, Block> reg) {
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
		return List.of(ElectrodynamicsBlocks.BLOCK_MULTISUBNODE.get(), ElectrodynamicsBlocks.BLOCK_FRAME.get(), ElectrodynamicsBlocks.BLOCK_FRAME_CORNER.get(), ElectrodynamicsBlocks.BLOCK_COMPRESSOR_SIDE.get());
	}

}
