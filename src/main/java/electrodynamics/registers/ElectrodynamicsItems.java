package electrodynamics.registers;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.References;
import electrodynamics.api.creativetab.CreativeTabSupplier;
import electrodynamics.api.registration.BulkDeferredHolder;
import electrodynamics.common.block.subtype.SubtypeFluidPipe;
import electrodynamics.common.block.subtype.SubtypeGasPipe;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.block.subtype.SubtypeRawOreBlock;
import electrodynamics.common.block.subtype.SubtypeResourceBlock;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.blockitem.types.BlockItemDescriptable;
import electrodynamics.common.blockitem.types.BlockItemFluidPipe;
import electrodynamics.common.blockitem.types.BlockItemGasPipe;
import electrodynamics.common.blockitem.types.BlockItemWire;
import electrodynamics.common.item.ItemBoneMeal;
import electrodynamics.common.item.ItemCeramic;
import electrodynamics.common.item.ItemDrillHead;
import electrodynamics.common.item.ItemElectrodynamics;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.item.gear.armor.types.ItemCombatArmor;
import electrodynamics.common.item.gear.armor.types.ItemCompositeArmor;
import electrodynamics.common.item.gear.armor.types.ItemHydraulicBoots;
import electrodynamics.common.item.gear.armor.types.ItemJetpack;
import electrodynamics.common.item.gear.armor.types.ItemNightVisionGoggles;
import electrodynamics.common.item.gear.armor.types.ItemRubberArmor;
import electrodynamics.common.item.gear.armor.types.ItemServoLeggings;
import electrodynamics.common.item.gear.tools.ItemCanister;
import electrodynamics.common.item.gear.tools.ItemGuidebook;
import electrodynamics.common.item.gear.tools.ItemMultimeter;
import electrodynamics.common.item.gear.tools.ItemPortableCylinder;
import electrodynamics.common.item.gear.tools.ItemWrench;
import electrodynamics.common.item.gear.tools.electric.ItemElectricBaton;
import electrodynamics.common.item.gear.tools.electric.ItemElectricChainsaw;
import electrodynamics.common.item.gear.tools.electric.ItemElectricDrill;
import electrodynamics.common.item.gear.tools.electric.ItemMechanizedCrossbow;
import electrodynamics.common.item.gear.tools.electric.ItemRailgunKinetic;
import electrodynamics.common.item.gear.tools.electric.ItemRailgunPlasma;
import electrodynamics.common.item.gear.tools.electric.ItemSeismicScanner;
import electrodynamics.common.item.subtype.SubtypeCeramic;
import electrodynamics.common.item.subtype.SubtypeChromotographyCard;
import electrodynamics.common.item.subtype.SubtypeCircuit;
import electrodynamics.common.item.subtype.SubtypeCrystal;
import electrodynamics.common.item.subtype.SubtypeDrillHead;
import electrodynamics.common.item.subtype.SubtypeDust;
import electrodynamics.common.item.subtype.SubtypeGear;
import electrodynamics.common.item.subtype.SubtypeImpureDust;
import electrodynamics.common.item.subtype.SubtypeIngot;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.item.subtype.SubtypeNugget;
import electrodynamics.common.item.subtype.SubtypeOxide;
import electrodynamics.common.item.subtype.SubtypePlate;
import electrodynamics.common.item.subtype.SubtypeRawOre;
import electrodynamics.common.item.subtype.SubtypeRod;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.item.ItemElectric;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, References.ID);

	/* BLOCKS */
	public static final BulkDeferredHolder<Item, BlockItemDescriptable, SubtypeOre> ITEMS_ORE = new BulkDeferredHolder<>(SubtypeOre.values(), subtype -> ITEMS.register(subtype.tag(), () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCKS_ORE.getValue(subtype), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, BlockItemDescriptable, SubtypeOreDeepslate> ITEMS_DEEPSLATEORE = new BulkDeferredHolder<>(SubtypeOreDeepslate.values(), subtype -> ITEMS.register(subtype.tag(), () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCKS_DEEPSLATEORE.getValue(subtype), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, BlockItemDescriptable, SubtypeRawOreBlock> ITEMS_RAWOREBLOCK = new BulkDeferredHolder<>(SubtypeRawOreBlock.values(), subtype -> ITEMS.register(subtype.tag(), () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCKS_RAWORE.getValue(subtype), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, BlockItemDescriptable, SubtypeResourceBlock> ITEMS_RESOURCEBLOCK = new BulkDeferredHolder<>(SubtypeResourceBlock.values(), subtype -> ITEMS.register(subtype.tag(), () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCKS_RESOURCE.getValue(subtype), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, BlockItemDescriptable, SubtypeGlass> ITEMS_CUSTOMGLASS = new BulkDeferredHolder<>(SubtypeGlass.values(), subtype -> ITEMS.register(subtype.tag(), () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(subtype), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, BlockItemDescriptable, SubtypeMachine> ITEMS_MACHINE = new BulkDeferredHolder<>(SubtypeMachine.values(), subtype -> {
		if(subtype == SubtypeMachine.downgradetransformer || subtype == SubtypeMachine.upgradetransformer || subtype == SubtypeMachine.multimeterblock || subtype == SubtypeMachine.circuitbreaker || subtype == SubtypeMachine.relay || subtype == SubtypeMachine.potentiometer || subtype == SubtypeMachine.advanceddowngradetransformer || subtype == SubtypeMachine.advancedupgradetransformer || subtype == SubtypeMachine.circuitmonitor || subtype == SubtypeMachine.currentregulator) {
			return ITEMS.register(subtype.tag(), () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(subtype), new Item.Properties(), subtype.showInItemGroup() ? ElectrodynamicsCreativeTabs.GRID : null));
		} else {
			return ITEMS.register(subtype.tag(), () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(subtype), new Item.Properties(), subtype.showInItemGroup() ? ElectrodynamicsCreativeTabs.MAIN : null));
		}

	});

	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_CHEMICALREACTOR = ITEMS.register("chemicalreactor", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOR.get(), new Item.Properties().stacksTo(64), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_ROTARYUNIFIER = ITEMS.register("rotaryunifier", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_ROTARYUNIFIER.get(), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN) {
		@Override
		public boolean isAllowedInCreativeTab(CreativeModeTab tab) {
			if(ModList.get().isLoaded(References.MEKANISM_ID)) {
				return super.isAllowedInCreativeTab(tab);
			}
			return false;
		}
	});
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_STEELSCAFFOLD = ITEMS.register("steelscaffold", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_STEELSCAFFOLDING.get(), new Item.Properties().stacksTo(64), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_FRAME = ITEMS.register("frame", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_FRAME.get(), new Item.Properties().stacksTo(64), null));
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_FRAMECORNER = ITEMS.register("framecorner", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_FRAME_CORNER.get(), new Item.Properties().stacksTo(64), null));
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_LOGISTICALMANAGER = ITEMS.register("logisticalmanager", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_LOGISTICALMANAGER.get(), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));;
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_SEISMICMARKER = ITEMS.register("seismicmarker", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_SEISMICMARKER.get(), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_COMPRESSOR = ITEMS.register("compressor", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_COMPRESSOR.get(), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_DECOMPRESSOR = ITEMS.register("decompressor", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_DECOMPRESSOR.get(), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_ADVANCEDCOMPRESSOR = ITEMS.register("advancedcompressor", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_ADVANCEDCOMPRESSOR.get(), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_ADVANCEDDECOMPRESSOR = ITEMS.register("advanceddecompressor", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_ADVANCEDDECOMPRESSOR.get(), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_THERMOELECTRIC_MANIPULATOR = ITEMS.register("thermoelectricmanipulator", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR.get(), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_ADVANCED_THERMOELECTRIC_MANIPULATOR = ITEMS.register("advancedthermoelectricmanipulator", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_ADVANCED_THERMOELECTRICMANIPULATOR.get(), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, BlockItemDescriptable> ITEM_COMPRESSOR_ADDONTANK = ITEMS.register("compressoraddontank", () -> new BlockItemDescriptable(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK.get(), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final BulkDeferredHolder<Item, BlockItemWire, SubtypeWire> ITEMS_WIRE = new BulkDeferredHolder<>(SubtypeWire.values(), subtype -> ITEMS.register(subtype.tag(), () -> new BlockItemWire(ElectrodynamicsBlocks.BLOCKS_WIRE.getValue(subtype), new Item.Properties(), ElectrodynamicsCreativeTabs.GRID)));
	public static final BulkDeferredHolder<Item, BlockItemFluidPipe, SubtypeFluidPipe> ITEMS_PIPE = new BulkDeferredHolder<>(SubtypeFluidPipe.values(), subtype -> ITEMS.register(subtype.tag(), () -> new BlockItemFluidPipe(ElectrodynamicsBlocks.BLOCKS_FLUIDPIPE.getValue(subtype), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, BlockItemGasPipe, SubtypeGasPipe> ITEMS_GASPIPE = new BulkDeferredHolder<>(SubtypeGasPipe.values(), subtype -> ITEMS.register(subtype.tag(), () -> new BlockItemGasPipe(ElectrodynamicsBlocks.BLOCKS_GASPIPE.getValue(subtype), new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));


	/* ITEMS */

	public static final BulkDeferredHolder<Item, ItemElectrodynamics, SubtypeRawOre> ITEMS_RAWORE = new BulkDeferredHolder<>(SubtypeRawOre.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, ItemElectrodynamics, SubtypeIngot> ITEMS_INGOT = new BulkDeferredHolder<>(SubtypeIngot.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, ItemElectrodynamics, SubtypeNugget> ITEMS_NUGGET = new BulkDeferredHolder<>(SubtypeNugget.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, ItemElectrodynamics, SubtypeDust> ITEMS_DUST = new BulkDeferredHolder<>(SubtypeDust.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, ItemElectrodynamics, SubtypeImpureDust> ITEMS_IMPUREDUST = new BulkDeferredHolder<>(SubtypeImpureDust.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, ItemElectrodynamics, SubtypeCrystal> ITEMS_CRYSTAL = new BulkDeferredHolder<>(SubtypeCrystal.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, ItemElectrodynamics, SubtypeOxide> ITEMS_OXIDE = new BulkDeferredHolder<>(SubtypeOxide.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, ItemElectrodynamics, SubtypeGear> ITEMS_GEAR = new BulkDeferredHolder<>(SubtypeGear.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, ItemElectrodynamics, SubtypePlate> ITEMS_PLATE = new BulkDeferredHolder<>(SubtypePlate.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, ItemElectrodynamics, SubtypeCircuit> ITEMS_CIRCUIT = new BulkDeferredHolder<>(SubtypeCircuit.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, ItemElectrodynamics, SubtypeRod> ITEMS_ROD = new BulkDeferredHolder<>(SubtypeRod.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, ItemElectrodynamics, SubtypeChromotographyCard> ITEMS_CHROMOTOGRAPHYCARD = new BulkDeferredHolder<>(SubtypeChromotographyCard.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN)));
	public static final BulkDeferredHolder<Item, ItemUpgrade, SubtypeItemUpgrade> ITEMS_UPGRADE = new BulkDeferredHolder<>(SubtypeItemUpgrade.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemUpgrade(new Item.Properties(), subtype)));
	public static final BulkDeferredHolder<Item, ItemCeramic, SubtypeCeramic> ITEMS_CERAMIC = new BulkDeferredHolder<>(SubtypeCeramic.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemCeramic(subtype)));
	public static final BulkDeferredHolder<Item, ItemDrillHead, SubtypeDrillHead> ITEMS_DRILLHEAD = new BulkDeferredHolder<>(SubtypeDrillHead.values(), subtype -> ITEMS.register(subtype.tag(), () -> new ItemDrillHead(subtype)));

	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_COAL_COKE = ITEMS.register("coalcoke", () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_SLAG = ITEMS.register("slag", () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_SHEETPLASTIC = ITEMS.register("sheetplastic", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_RAWCOMPOSITEPLATING = ITEMS.register("compositeplatingraw", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_COMPOSITEPLATING = ITEMS.register("compositeplating", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemBoneMeal> ITEM_MOLYBDENUMFERTILIZER = ITEMS.register("molybdenumfertilizer", () -> new ItemBoneMeal(new Item.Properties().stacksTo(64), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_INSULATION = ITEMS.register("insulation", () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_CERAMICINSULATION = ITEMS.register("insulationceramic", () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_COIL = ITEMS.register("coil", () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_LAMINATEDCOIL = ITEMS.register("laminatedcoil", () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_TITANIUM_COIL = ITEMS.register("titaniumheatcoil", () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_MOTOR = ITEMS.register("motor", () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_SOLARPANELPLATE = ITEMS.register("solarpanelplate", () -> new ItemElectrodynamics(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_PLASTIC_FIBERS = ITEMS.register("plasticfibers", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_MECHANICALVALVE = ITEMS.register("mechanicalvalve", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_PRESSUREGAGE = ITEMS.register("pressuregauge", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_FIBERGLASSSHEET = ITEMS.register("fiberglasssheet", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), ElectrodynamicsCreativeTabs.MAIN));

	public static final DeferredHolder<Item, ItemElectrodynamics> ITEM_BATTERY = ITEMS.register("battery", () -> new ItemElectric((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).setIsEnergyStorageOnly().stacksTo(1), ElectrodynamicsCreativeTabs.MAIN, item -> Items.AIR));
	public static final DeferredHolder<Item, ItemElectric> ITEM_LITHIUMBATTERY = ITEMS.register("lithiumbattery", () -> new ItemElectric((ElectricItemProperties) new ElectricItemProperties().capacity(4 * 1666666.66667).extract(TransferPack.joulesVoltage(4 * 1666666.66667 / (120.0 * 20.0), 240)).receive(TransferPack.joulesVoltage(4 * 1666666.66667 / (120.0 * 20.0), 240)).setIsEnergyStorageOnly().stacksTo(1), ElectrodynamicsCreativeTabs.MAIN, item -> Items.AIR));
	public static final DeferredHolder<Item, ItemElectric> ITEM_CARBYNEBATTERY = ITEMS.register("carbynebattery", () -> new ItemElectric((ElectricItemProperties) new ElectricItemProperties().capacity(8 * 1666666.66667).extract(TransferPack.joulesVoltage(8 * 1666666.66667 / (120.0 * 20.0), 480)).receive(TransferPack.joulesVoltage(8 * 1666666.66667 / (120.0 * 20.0), 480)).setIsEnergyStorageOnly().stacksTo(1), ElectrodynamicsCreativeTabs.MAIN, item -> Items.AIR));

	public static final DeferredHolder<Item, ItemWrench> ITEM_WRENCH = ITEMS.register("wrench", () -> new ItemWrench(new Item.Properties().stacksTo(1), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemMultimeter> ITEM_MULTIMETER = ITEMS.register("multimeter", () -> new ItemMultimeter(new Item.Properties().stacksTo(1), ElectrodynamicsCreativeTabs.MAIN));

	public static final DeferredHolder<Item, ItemGuidebook> GUIDEBOOK = ITEMS.register("guidebook", () -> new ItemGuidebook(new Item.Properties(), ElectrodynamicsCreativeTabs.MAIN));

	public static final DeferredHolder<Item, ItemElectricDrill> ITEM_ELECTRICDRILL = ITEMS.register("electricdrill", () -> new ItemElectricDrill((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectricChainsaw> ITEM_ELECTRICCHAINSAW = ITEMS.register("electricchainsaw", () -> new ItemElectricChainsaw((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemElectricBaton> ITEM_ELECTRICBATON = ITEMS.register("electricbaton", () -> new ItemElectricBaton((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667 * 4).extract(TransferPack.joulesVoltage(4 * 1666666.66667 / (120.0 * 20.0), 240)).receive(TransferPack.joulesVoltage(4 * 1666666.66667 / (120.0 * 20.0), 240)).stacksTo(1), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemRailgunKinetic> ITEM_KINETICRAILGUN = ITEMS.register("railgunkinetic", () -> new ItemRailgunKinetic((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667 * 4).extract(TransferPack.joulesVoltage(4 * 1666666.66667, 240)).receive(TransferPack.joulesVoltage(4 * 1666666.66667 / (120.0 * 20.0), 240)).stacksTo(1), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemRailgunPlasma> ITEM_PLASMARAILGUN = ITEMS.register("railgunplasma", () -> new ItemRailgunPlasma((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667 * 8).extract(TransferPack.joulesVoltage(8 * 1666666.66667, 480)).receive(TransferPack.joulesVoltage(8 * 1666666.66667 / (120.0 * 20.0), 480)).stacksTo(1), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemSeismicScanner> ITEM_SEISMICSCANNER = ITEMS.register("seismicscanner", () -> new ItemSeismicScanner((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemMechanizedCrossbow> ITEM_MECHANIZEDCROSSBOW = ITEMS.register("mechanizedcrossbow", () -> new ItemMechanizedCrossbow((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), ElectrodynamicsCreativeTabs.MAIN));

	public static final DeferredHolder<Item, ItemCompositeArmor> ITEM_COMPOSITEHELMET = ITEMS.register("compositearmorhelmet", () -> new ItemCompositeArmor(Type.HELMET));
	public static final DeferredHolder<Item, ItemCompositeArmor> ITEM_COMPOSITECHESTPLATE = ITEMS.register("compositearmorchestplate", () -> new ItemCompositeArmor(Type.CHESTPLATE));
	public static final DeferredHolder<Item, ItemCompositeArmor> ITEM_COMPOSITELEGGINGS = ITEMS.register("compositearmorleggings", () -> new ItemCompositeArmor(Type.LEGGINGS));
	public static final DeferredHolder<Item, ItemCompositeArmor> ITEM_COMPOSITEBOOTS = ITEMS.register("compositearmorboots", () -> new ItemCompositeArmor(Type.BOOTS));

	public static final DeferredHolder<Item, ItemRubberArmor> ITEM_RUBBERBOOTS = ITEMS.register("rubberboots", () -> new ItemRubberArmor(Type.BOOTS, new Item.Properties().stacksTo(1).durability(100000), ElectrodynamicsCreativeTabs.MAIN));

	public static final DeferredHolder<Item, ItemNightVisionGoggles> ITEM_NIGHTVISIONGOGGLES = ITEMS.register("nightvisiongoggles", () -> new ItemNightVisionGoggles((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemHydraulicBoots> ITEM_HYDRAULICBOOTS = ITEMS.register("hydraulicboots", ItemHydraulicBoots::new);
	public static final DeferredHolder<Item, ItemJetpack> ITEM_JETPACK = ITEMS.register("jetpack", ItemJetpack::new);
	public static final DeferredHolder<Item, ItemServoLeggings> ITEM_SERVOLEGGINGS = ITEMS.register("servoleggings", () -> new ItemServoLeggings((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1).fireResistant(), ElectrodynamicsCreativeTabs.MAIN));

	public static final DeferredHolder<Item, ItemCombatArmor> ITEM_COMBATHELMET = ITEMS.register("combatarmorhelmet", () -> new ItemCombatArmor(new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1).fireResistant(), Type.HELMET, ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemCombatArmor> ITEM_COMBATCHESTPLATE = ITEMS.register("combatarmorchestplate", () -> new ItemCombatArmor(new Item.Properties().stacksTo(1).fireResistant(), Type.CHESTPLATE, ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemCombatArmor> ITEM_COMBATLEGGINGS = ITEMS.register("combatarmorleggings", () -> new ItemCombatArmor(new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1).fireResistant(), Type.LEGGINGS, ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemCombatArmor> ITEM_COMBATBOOTS = ITEMS.register("combatarmorboots", () -> new ItemCombatArmor(new Item.Properties().stacksTo(1), Type.BOOTS, ElectrodynamicsCreativeTabs.MAIN));

	public static final DeferredHolder<Item, ItemCanister> ITEM_CANISTERREINFORCED = ITEMS.register("canisterreinforced", () -> new ItemCanister(new Item.Properties().stacksTo(1), ElectrodynamicsCreativeTabs.MAIN));
	public static final DeferredHolder<Item, ItemPortableCylinder> ITEM_PORTABLECYLINDER = ITEMS.register("portablecylinder", () -> new ItemPortableCylinder(new Item.Properties().stacksTo(1), ElectrodynamicsCreativeTabs.MAIN));


	@EventBusSubscriber(value = Dist.CLIENT, modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
	private static class ElectroCreativeRegistry {

		@SubscribeEvent
		public static void registerItems(BuildCreativeModeTabContentsEvent event) {

			ITEMS.getEntries().forEach(reg -> {

				CreativeTabSupplier supplier = (CreativeTabSupplier) reg.get();

				if (supplier.hasCreativeTab() && supplier.isAllowedInCreativeTab(event.getTab())) {
					List<ItemStack> toAdd = new ArrayList<>();
					supplier.addCreativeModeItems(event.getTab(), toAdd);
					event.acceptAll(toAdd);
				}

			});

		}

	}

}
