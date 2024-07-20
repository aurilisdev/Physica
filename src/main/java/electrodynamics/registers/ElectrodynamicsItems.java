package electrodynamics.registers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import electrodynamics.api.ISubtype;
import electrodynamics.api.References;
import electrodynamics.api.creativetab.CreativeTabSupplier;
import electrodynamics.common.block.connect.BlockGasPipe;
import electrodynamics.common.block.connect.BlockWire;
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
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, References.ID);
	public static final HashMap<ISubtype, DeferredHolder<Item, Item>> SUBTYPEITEMREGISTER_MAPPINGS = new HashMap<>();

	// yes this is dirty but it works
	public static DeferredHolder<Item, Item> ITEM_COMPRESSOR;
	public static DeferredHolder<Item, Item> ITEM_DECOMPRESSOR;
	public static DeferredHolder<Item, Item> ITEM_THERMOELECTRIC_MANIPULATOR;

	public static DeferredHolder<Item, Item> ITEM_COMPRESSOR_ADDONTANK;

	public static DeferredHolder<Item, Item> ITEM_FRAME;
	public static DeferredHolder<Item, Item> ITEM_FRAMECORNER;
	public static DeferredHolder<Item, Item> ITEM_LOGISTICALMANAGER;
	public static DeferredHolder<Item, Item> ITEM_SEISMICMARKER;

	public static DeferredHolder<Item, Item> ITEM_FLUIDVALVE;
	public static DeferredHolder<Item, Item> ITEM_FLUIDPIPEPUMP;
	public static DeferredHolder<Item, Item> ITEM_FLUIDPIPEFILTER;

	public static DeferredHolder<Item, Item> ITEM_GASVALVE;
	public static DeferredHolder<Item, Item> ITEM_GASPIPEPUMP;
	public static DeferredHolder<Item, Item> ITEM_GASPIPEFILTER;

	public static DeferredHolder<Item, Item> ITEM_STEELSCAFFOLD;

	static {
		// Blocks
		registerSubtypeBlockItem(SubtypeOre.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeBlockItem(SubtypeOreDeepslate.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeBlockItem(SubtypeRawOreBlock.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeBlockItem(SubtypeResourceBlock.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeBlockItem(SubtypeGlass.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		for (SubtypeMachine machine : SubtypeMachine.values()) {
			if (machine == SubtypeMachine.downgradetransformer || machine == SubtypeMachine.upgradetransformer || machine == SubtypeMachine.multimeterblock || machine == SubtypeMachine.circuitbreaker || machine == SubtypeMachine.relay || machine == SubtypeMachine.potentiometer || machine == SubtypeMachine.advanceddowngradetransformer || machine == SubtypeMachine.advancedupgradetransformer || machine == SubtypeMachine.circuitmonitor || machine == SubtypeMachine.currentregulator) {
				SUBTYPEITEMREGISTER_MAPPINGS.put(machine, ITEMS.register(machine.tag(), () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.SUBTYPEBLOCKREGISTER_MAPPINGS.get(machine).get(), new Item.Properties(), machine.showInItemGroup ? () -> ElectrodynamicsCreativeTabs.GRID.get() : null)));
			} else {
				SUBTYPEITEMREGISTER_MAPPINGS.put(machine, ITEMS.register(machine.tag(), () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.SUBTYPEBLOCKREGISTER_MAPPINGS.get(machine).get(), new Item.Properties(), machine.showInItemGroup ? () -> ElectrodynamicsCreativeTabs.MAIN.get() : null)));
			}

		}
		ITEM_STEELSCAFFOLD = ITEMS.register("steelscaffold", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockSteelScaffold, new Item.Properties().stacksTo(64), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
		ITEM_FRAME = ITEMS.register("frame", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockFrame, new Item.Properties().stacksTo(64), null));
		ITEM_FRAMECORNER = ITEMS.register("framecorner", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockFrameCorner, new Item.Properties().stacksTo(64), null));
		ITEM_LOGISTICALMANAGER = ITEMS.register("logisticalmanager", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockLogisticalManager, new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
		ITEM_SEISMICMARKER = ITEMS.register("seismicmarker", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockSeismicMarker, new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));

		ITEM_COMPRESSOR = ITEMS.register("compressor", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockCompressor, new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
		ITEM_DECOMPRESSOR = ITEMS.register("decompressor", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockDecompressor, new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
		ITEM_THERMOELECTRIC_MANIPULATOR = ITEMS.register("thermoelectricmanipulator", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockThermoelectricManipulator, new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
		ITEM_COMPRESSOR_ADDONTANK = ITEMS.register("compressoraddontank", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockGasTransformerAddonTank, new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));

		for (SubtypeWire subtype : SubtypeWire.values()) {
			SUBTYPEITEMREGISTER_MAPPINGS.put(subtype, ITEMS.register(subtype.tag(), () -> new BlockItemWire((BlockWire) ElectrodynamicsBlocks.SUBTYPEBLOCKREGISTER_MAPPINGS.get(subtype).get(), new Item.Properties(), () -> ElectrodynamicsCreativeTabs.GRID.get())));
		}

		for (SubtypeFluidPipe subtype : SubtypeFluidPipe.values()) {
			SUBTYPEITEMREGISTER_MAPPINGS.put(subtype, ITEMS.register(subtype.tag(), () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.SUBTYPEBLOCKREGISTER_MAPPINGS.get(subtype).get(), new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get())));
		}
		ITEM_FLUIDVALVE = ITEMS.register("fluidvalve", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockFluidValve, new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
		ITEM_FLUIDPIPEPUMP = ITEMS.register("fluidpipepump", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockFluidPipePump, new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
		ITEM_FLUIDPIPEFILTER = ITEMS.register("fluidpipefilter", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockFluidPipeFilter, new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));

		for (SubtypeGasPipe subtype : SubtypeGasPipe.values()) {
			SUBTYPEITEMREGISTER_MAPPINGS.put(subtype, ITEMS.register(subtype.tag(), () -> new BlockItemGasPipe((BlockGasPipe) ElectrodynamicsBlocks.SUBTYPEBLOCKREGISTER_MAPPINGS.get(subtype).get(), new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get())));
		}
		ITEM_GASVALVE = ITEMS.register("gasvalve", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockGasValve, new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
		ITEM_GASPIPEPUMP = ITEMS.register("gaspipepump", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockGasPipePump, new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
		ITEM_GASPIPEFILTER = ITEMS.register("gaspipefilter", () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.blockGasPipeFilter, new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));

		// Items
		registerSubtypeItem(SubtypeRawOre.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeItem(SubtypeIngot.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeItem(SubtypeNugget.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeItem(SubtypeDust.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeItem(SubtypeImpureDust.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeItem(SubtypeCrystal.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeItem(SubtypeOxide.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeItem(SubtypeGear.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeItem(SubtypePlate.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeItem(SubtypeCircuit.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		registerSubtypeItem(SubtypeRod.values(), () -> ElectrodynamicsCreativeTabs.MAIN.get());
		for (SubtypeItemUpgrade subtype : SubtypeItemUpgrade.values()) {
			SUBTYPEITEMREGISTER_MAPPINGS.put(subtype, ITEMS.register(subtype.tag(), () -> new ItemUpgrade(new Item.Properties(), subtype)));
		}
		for (SubtypeCeramic subtype : SubtypeCeramic.values()) {
			SUBTYPEITEMREGISTER_MAPPINGS.put(subtype, ITEMS.register(subtype.tag(), () -> new ItemCeramic(subtype)));
		}
		for (SubtypeDrillHead drill : SubtypeDrillHead.values()) {
			SUBTYPEITEMREGISTER_MAPPINGS.put(drill, ITEMS.register(drill.tag(), () -> new ItemDrillHead(drill)));
		}
	}

	public static final DeferredHolder<Item, Item> ITEM_COAL_COKE = ITEMS.register("coalcoke", () -> new ItemElectrodynamics(new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_SLAG = ITEMS.register("slag", () -> new ItemElectrodynamics(new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_SHEETPLASTIC = ITEMS.register("sheetplastic", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_RAWCOMPOSITEPLATING = ITEMS.register("compositeplatingraw", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_COMPOSITEPLATING = ITEMS.register("compositeplating", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_MOLYBDENUMFERTILIZER = ITEMS.register("molybdenumfertilizer", () -> new ItemBoneMeal(new Item.Properties().stacksTo(64), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_INSULATION = ITEMS.register("insulation", () -> new ItemElectrodynamics(new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_CERAMICINSULATION = ITEMS.register("insulationceramic", () -> new ItemElectrodynamics(new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_COIL = ITEMS.register("coil", () -> new ItemElectrodynamics(new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_LAMINATEDCOIL = ITEMS.register("laminatedcoil", () -> new ItemElectrodynamics(new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_TITANIUM_COIL = ITEMS.register("titaniumheatcoil", () -> new ItemElectrodynamics(new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_MOTOR = ITEMS.register("motor", () -> new ItemElectrodynamics(new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_SOLARPANELPLATE = ITEMS.register("solarpanelplate", () -> new ItemElectrodynamics(new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_PLASTIC_FIBERS = ITEMS.register("plasticfibers", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_MECHANICALVALVE = ITEMS.register("mechanicalvalve", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_PRESSUREGAGE = ITEMS.register("pressuregauge", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_FIBERGLASSSHEET = ITEMS.register("fiberglasssheet", () -> new ItemElectrodynamics(new Item.Properties().stacksTo(64), () -> ElectrodynamicsCreativeTabs.MAIN.get()));

	public static final DeferredHolder<Item, Item> ITEM_BATTERY = ITEMS.register("battery", () -> new ItemElectric((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).setIsEnergyStorageOnly().stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get(), item -> Items.AIR));
	public static final DeferredHolder<Item, Item> ITEM_LITHIUMBATTERY = ITEMS.register("lithiumbattery", () -> new ItemElectric((ElectricItemProperties) new ElectricItemProperties().capacity(4 * 1666666.66667).extract(TransferPack.joulesVoltage(4 * 1666666.66667 / (120.0 * 20.0), 240)).receive(TransferPack.joulesVoltage(4 * 1666666.66667 / (120.0 * 20.0), 240)).setIsEnergyStorageOnly().stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get(), item -> Items.AIR));
	public static final DeferredHolder<Item, Item> ITEM_CARBYNEBATTERY = ITEMS.register("carbynebattery", () -> new ItemElectric((ElectricItemProperties) new ElectricItemProperties().capacity(8 * 1666666.66667).extract(TransferPack.joulesVoltage(8 * 1666666.66667 / (120.0 * 20.0), 480)).receive(TransferPack.joulesVoltage(8 * 1666666.66667 / (120.0 * 20.0), 480)).setIsEnergyStorageOnly().stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get(), item -> Items.AIR));

	public static final DeferredHolder<Item, Item> ITEM_WRENCH = ITEMS.register("wrench", () -> new ItemWrench(new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_MULTIMETER = ITEMS.register("multimeter", () -> new ItemMultimeter(new Item.Properties().stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_CANISTERREINFORCED = ITEMS.register("canisterreinforced", () -> new ItemCanister(new Item.Properties().stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_PORTABLECYLINDER = ITEMS.register("portablecylinder", () -> new ItemPortableCylinder(new Item.Properties().stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get()));

	public static final DeferredHolder<Item, Item> GUIDEBOOK = ITEMS.register("guidebook", () -> new ItemGuidebook(new Item.Properties(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));

	public static final DeferredHolder<Item, Item> ITEM_ELECTRICDRILL = ITEMS.register("electricdrill", () -> new ItemElectricDrill((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_ELECTRICCHAINSAW = ITEMS.register("electricchainsaw", () -> new ItemElectricChainsaw((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_ELECTRICBATON = ITEMS.register("electricbaton", () -> new ItemElectricBaton((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667 * 4).extract(TransferPack.joulesVoltage(4 * 1666666.66667 / (120.0 * 20.0), 240)).receive(TransferPack.joulesVoltage(4 * 1666666.66667 / (120.0 * 20.0), 240)).stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_KINETICRAILGUN = ITEMS.register("railgunkinetic", () -> new ItemRailgunKinetic((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667 * 4).extract(TransferPack.joulesVoltage(4 * 1666666.66667, 240)).receive(TransferPack.joulesVoltage(4 * 1666666.66667 / (120.0 * 20.0), 240)).stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_PLASMARAILGUN = ITEMS.register("railgunplasma", () -> new ItemRailgunPlasma((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667 * 8).extract(TransferPack.joulesVoltage(8 * 1666666.66667, 480)).receive(TransferPack.joulesVoltage(8 * 1666666.66667 / (120.0 * 20.0), 480)).stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_SEISMICSCANNER = ITEMS.register("seismicscanner", () -> new ItemSeismicScanner((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_MECHANIZEDCROSSBOW = ITEMS.register("mechanizedcrossbow", () -> new ItemMechanizedCrossbow((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get()));

	public static final DeferredHolder<Item, Item> ITEM_COMPOSITEHELMET = ITEMS.register("compositearmorhelmet", () -> new ItemCompositeArmor(Type.HELMET));
	public static final DeferredHolder<Item, Item> ITEM_COMPOSITECHESTPLATE = ITEMS.register("compositearmorchestplate", () -> new ItemCompositeArmor(Type.CHESTPLATE));
	public static final DeferredHolder<Item, Item> ITEM_COMPOSITELEGGINGS = ITEMS.register("compositearmorleggings", () -> new ItemCompositeArmor(Type.LEGGINGS));
	public static final DeferredHolder<Item, Item> ITEM_COMPOSITEBOOTS = ITEMS.register("compositearmorboots", () -> new ItemCompositeArmor(Type.BOOTS));

	public static final DeferredHolder<Item, Item> ITEM_RUBBERBOOTS = ITEMS.register("rubberboots", () -> new ItemRubberArmor(Type.BOOTS, new Item.Properties().stacksTo(1).durability(100000), () -> ElectrodynamicsCreativeTabs.MAIN.get()));

	public static final DeferredHolder<Item, Item> ITEM_NIGHTVISIONGOGGLES = ITEMS.register("nightvisiongoggles", () -> new ItemNightVisionGoggles((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1), () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_HYDRAULICBOOTS = ITEMS.register("hydraulicboots", ItemHydraulicBoots::new);
	public static final DeferredHolder<Item, Item> ITEM_JETPACK = ITEMS.register("jetpack", ItemJetpack::new);
	public static final DeferredHolder<Item, Item> ITEM_SERVOLEGGINGS = ITEMS.register("servoleggings", () -> new ItemServoLeggings((ElectricItemProperties) new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1).fireResistant(), () -> ElectrodynamicsCreativeTabs.MAIN.get()));

	public static final DeferredHolder<Item, Item> ITEM_COMBATHELMET = ITEMS.register("combatarmorhelmet", () -> new ItemCombatArmor(new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1).fireResistant(), Type.HELMET, () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_COMBATCHESTPLATE = ITEMS.register("combatarmorchestplate", () -> new ItemCombatArmor(new Item.Properties().stacksTo(1).fireResistant(), Type.CHESTPLATE, () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_COMBATLEGGINGS = ITEMS.register("combatarmorleggings", () -> new ItemCombatArmor(new ElectricItemProperties().capacity(1666666.66667).extract(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).receive(TransferPack.joulesVoltage(1666666.66667 / (120.0 * 20.0), 120)).stacksTo(1).fireResistant(), Type.LEGGINGS, () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	public static final DeferredHolder<Item, Item> ITEM_COMBATBOOTS = ITEMS.register("combatarmorboots", () -> new ItemCombatArmor(new Item.Properties().stacksTo(1), Type.BOOTS, () -> ElectrodynamicsCreativeTabs.MAIN.get()));
	
	
	private static void registerSubtypeItem(ISubtype[] array, Supplier<CreativeModeTab> tab) {
		for (ISubtype subtype : array) {
			SUBTYPEITEMREGISTER_MAPPINGS.put(subtype, ITEMS.register(subtype.tag(), () -> new ItemElectrodynamics(new Item.Properties(), tab)));
		}
	}

	private static void registerSubtypeBlockItem(ISubtype[] array, Supplier<CreativeModeTab> tab) {
		for (ISubtype subtype : array) {
			SUBTYPEITEMREGISTER_MAPPINGS.put(subtype, ITEMS.register(subtype.tag(), () -> new BlockItemDescriptable(() -> ElectrodynamicsBlocks.SUBTYPEBLOCKREGISTER_MAPPINGS.get(subtype).get(), new Item.Properties(), tab)));
		}
	}

	public static Item[] getAllItemForSubtype(ISubtype[] values) {
		List<Item> list = new ArrayList<>();
		for (ISubtype value : values) {
			list.add(SUBTYPEITEMREGISTER_MAPPINGS.get(value).get());
		}
		return list.toArray(new Item[] {});
	}

	public static Item getItem(ISubtype value) {
		return SUBTYPEITEMREGISTER_MAPPINGS.get(value).get();
	}

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
