package electrodynamics.datagen.client;

import java.util.Locale;

import javax.annotation.Nullable;

import electrodynamics.api.References;
import electrodynamics.client.ClientRegister;
import electrodynamics.common.block.subtype.SubtypeFluidPipe;
import electrodynamics.common.block.subtype.SubtypeGasPipe;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.block.subtype.SubtypeWire.InsulationMaterial;
import electrodynamics.common.block.subtype.SubtypeWire.WireClass;
import electrodynamics.common.block.subtype.SubtypeWire.WireColor;
import electrodynamics.common.block.subtype.SubtypeWire.WireMaterial;
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
import electrodynamics.datagen.DataGenerators;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelFile.ExistingModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ElectrodynamicsItemModelsProvider extends ItemModelProvider {

	public final String modID;

	public ElectrodynamicsItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modID) {
		super(output, modID, existingFileHelper);
		this.modID = modID;
	}

	public ElectrodynamicsItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		this(output, existingFileHelper, References.ID);
	}

	@Override
	protected void registerModels() {

		layeredItem(ElectrodynamicsItems.ITEM_COAL_COKE, Parent.GENERATED, itemLoc("coalcoke"));
		layeredItem(ElectrodynamicsItems.ITEM_CERAMICINSULATION, Parent.GENERATED, itemLoc("insulationceramic"));
		layeredBuilder(name(ElectrodynamicsItems.ITEM_COIL), Parent.GENERATED, itemLoc("coil")).transforms().transform(ItemDisplayContext.GUI).scale(0.8F).end();
		layeredBuilder(name(ElectrodynamicsItems.ITEM_LAMINATEDCOIL), Parent.GENERATED, itemLoc("laminatedcoil")).transforms().transform(ItemDisplayContext.GUI).scale(0.8F).end();
		layeredItem(ElectrodynamicsItems.ITEM_INSULATION, Parent.GENERATED, itemLoc("insulation"));
		layeredItem(ElectrodynamicsItems.ITEM_MOLYBDENUMFERTILIZER, Parent.GENERATED, itemLoc("molybdenumfertilizer"));
		layeredItem(ElectrodynamicsItems.ITEM_MOTOR, Parent.GENERATED, itemLoc("motor"));
		layeredItem(ElectrodynamicsItems.ITEM_RAWCOMPOSITEPLATING, Parent.GENERATED, itemLoc("compositeplatingraw"));
		layeredItem(ElectrodynamicsItems.ITEM_SHEETPLASTIC, Parent.GENERATED, itemLoc("sheetplastic"));
		layeredItem(ElectrodynamicsItems.ITEM_SLAG, Parent.GENERATED, itemLoc("slag"));
		layeredBuilder(name(ElectrodynamicsItems.ITEM_SOLARPANELPLATE), Parent.GENERATED, itemLoc("solarpanelplate")).transforms().transform(ItemDisplayContext.GUI).scale(0.8F).end();
		layeredItem(ElectrodynamicsItems.ITEM_TITANIUM_COIL, Parent.GENERATED, itemLoc("titaniumheatcoil"));
		layeredItem(ElectrodynamicsItems.ITEM_PLASTIC_FIBERS, Parent.GENERATED, itemLoc("plasticfibers"));
		layeredItem(ElectrodynamicsItems.ITEM_MECHANICALVALVE, Parent.GENERATED, itemLoc("mechanicalvalve"));
		layeredItem(ElectrodynamicsItems.ITEM_PRESSUREGAGE, Parent.GENERATED, itemLoc("pressuregauge"));
		layeredItem(ElectrodynamicsItems.ITEM_FIBERGLASSSHEET, Parent.GENERATED, itemLoc("fiberglasssheet"));

		layeredItem(ElectrodynamicsItems.ITEM_COMBATHELMET, Parent.GENERATED, itemLoc("armor/combathelmet"));
		layeredItem(ElectrodynamicsItems.ITEM_COMBATCHESTPLATE, Parent.GENERATED, itemLoc("armor/combatchestplate"));
		layeredItem(ElectrodynamicsItems.ITEM_COMBATLEGGINGS, Parent.GENERATED, itemLoc("armor/combatleggings"));
		layeredItem(ElectrodynamicsItems.ITEM_COMBATBOOTS, Parent.GENERATED, itemLoc("armor/combatboots"));
		layeredItem(ElectrodynamicsItems.ITEM_COMPOSITEHELMET, Parent.GENERATED, itemLoc("armor/compositehelmet"));
		layeredItem(ElectrodynamicsItems.ITEM_COMPOSITECHESTPLATE, Parent.GENERATED, itemLoc("armor/compositechestplate"));
		layeredItem(ElectrodynamicsItems.ITEM_COMPOSITELEGGINGS, Parent.GENERATED, itemLoc("armor/compositeleggings"));
		layeredItem(ElectrodynamicsItems.ITEM_COMPOSITEBOOTS, Parent.GENERATED, itemLoc("armor/compositeboots"));
		layeredItem(ElectrodynamicsItems.ITEM_COMPOSITEPLATING, Parent.GENERATED, itemLoc("compositeplating"));
		layeredItem(ElectrodynamicsItems.ITEM_NIGHTVISIONGOGGLES, Parent.GENERATED, itemLoc("armor/nightvisiongoggles"));
		layeredItem(ElectrodynamicsItems.ITEM_JETPACK, Parent.GENERATED, itemLoc("armor/jetpack"));
		layeredItem(ElectrodynamicsItems.ITEM_SERVOLEGGINGS, Parent.GENERATED, itemLoc("armor/servoleggings"));
		layeredItem(ElectrodynamicsItems.ITEM_HYDRAULICBOOTS, Parent.GENERATED, itemLoc("armor/hydraulicboots"));
		layeredItem(ElectrodynamicsItems.ITEM_RUBBERBOOTS, Parent.GENERATED, itemLoc("armor/rubberboots"));

		getBucketModel(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, Parent.FORGE_DEFAULT).fluid(Fluids.WATER).applyFluidLuminosity(true).applyTint(true).end().texture("base", itemLoc("canisterreinforced/base")).texture("fluid", itemLoc("canisterreinforced/fluid"));
		layeredItem(ElectrodynamicsItems.GUIDEBOOK, Parent.GENERATED, itemLoc("guidebook"));
		layeredBuilder(name(ElectrodynamicsItems.ITEM_MULTIMETER), Parent.GENERATED, itemLoc("multimeter")).transforms().transform(ItemDisplayContext.GUI).scale(0.9F).end();
		layeredBuilder(name(ElectrodynamicsItems.ITEM_SEISMICSCANNER), Parent.GENERATED, itemLoc("seismicscanner")).transforms().transform(ItemDisplayContext.GUI).scale(0.75F).end();
		layeredItem(ElectrodynamicsItems.ITEM_WRENCH, Parent.GENERATED, itemLoc("wrench"));
		layeredItem(ElectrodynamicsItems.ITEM_BATTERY, Parent.GENERATED, itemLoc("battery"));
		layeredItem(ElectrodynamicsItems.ITEM_LITHIUMBATTERY, Parent.GENERATED, itemLoc("lithiumbattery"));
		layeredItem(ElectrodynamicsItems.ITEM_CARBYNEBATTERY, Parent.GENERATED, itemLoc("carbynebattery"));
		layeredItem(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, Parent.GENERATED, itemLoc("portablecylinder"));
		// TODO make this toggleable?
		toggleableItem(ElectrodynamicsItems.ITEM_ELECTRICBATON, "on", Parent.HANDHELD, Parent.HANDHELD, new ResourceLocation[] { itemLoc("tools/electricbaton") }, new ResourceLocation[] { itemLoc("tools/electricbatonon") });
		toggleableItem(ElectrodynamicsItems.ITEM_ELECTRICCHAINSAW, "on", Parent.HANDHELD, Parent.HANDHELD, new ResourceLocation[] { itemLoc("tools/electricchainsaw") }, new ResourceLocation[] { itemLoc("tools/electricchainsawon") });

		toggleableItem(ElectrodynamicsItems.ITEM_ELECTRICDRILL, "on", Parent.HANDHELD, Parent.HANDHELD, new ResourceLocation[] { itemLoc("tools/electricdrilloffbase"), itemLoc("tools/electricdrilloffhead") }, new ResourceLocation[] { itemLoc("tools/electricdrillonbase"), itemLoc("tools/electricdrillonhead") });

		layeredBuilder(name(ElectrodynamicsItems.ITEM_MECHANIZEDCROSSBOW), Parent.GENERATED, itemLoc("tools/mechanizedcrossbow")).transforms().transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(-90, 0, -60).translation(2F, 0.1F, -3F).scale(0.9F).end().transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(-90, 0, 30).translation(2, 0.1F, -3).scale(0.9F).end().transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(-90, 0, -55).translation(1.13F, 3.2F, 1.13F).scale(0.68F).end().transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(-90, 0, 35).translation(1.13F, 3.2F, 1.13F).scale(0.68F).end();

		for (SubtypeCeramic ceramic : SubtypeCeramic.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_CERAMIC.getValue(ceramic), Parent.GENERATED, itemLoc("ceramic/" + ceramic.tag()));
		}

		for (SubtypeCircuit circuit : SubtypeCircuit.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_CIRCUIT.getValue(circuit), Parent.GENERATED, itemLoc("circuit/" + circuit.tag()));
		}

		for (SubtypeCrystal crystal : SubtypeCrystal.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(crystal), Parent.GENERATED, itemLoc("crystal/" + crystal.tag()));
		}

		for (SubtypeDrillHead drill : SubtypeDrillHead.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_DRILLHEAD.getValue(drill), Parent.GENERATED, itemLoc("drillhead/drillhead"));
		}

		for (SubtypeDust dust : SubtypeDust.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_DUST.getValue(dust), Parent.GENERATED, itemLoc("dust/" + dust.tag()));
		}

		for (SubtypeGear gear : SubtypeGear.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_GEAR.getValue(gear), Parent.GENERATED, itemLoc("gear/" + gear.tag()));
		}

		for (SubtypeImpureDust impure : SubtypeImpureDust.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(impure), Parent.GENERATED, itemLoc("impuredust/" + impure.tag()));
		}

		for (SubtypeIngot ingot : SubtypeIngot.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_INGOT.getValue(ingot), Parent.GENERATED, itemLoc("ingot/" + ingot.tag()));
		}

		for (SubtypeItemUpgrade upgrade : SubtypeItemUpgrade.values()) {
			layeredBuilder(name(ElectrodynamicsItems.ITEMS_UPGRADE.getValue(upgrade)), Parent.GENERATED, itemLoc("upgrade/" + upgrade.tag())).transforms().transform(ItemDisplayContext.GUI).scale(0.8F).end();
		}

		for (SubtypeNugget nugget : SubtypeNugget.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_NUGGET.getValue(nugget), Parent.GENERATED, itemLoc("nugget/" + nugget.tag()));
		}

		for (SubtypeOxide oxide : SubtypeOxide.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_OXIDE.getValue(oxide), Parent.GENERATED, itemLoc("oxide/" + oxide.tag()));
		}

		for (SubtypePlate plate : SubtypePlate.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_PLATE.getValue(plate), Parent.GENERATED, itemLoc("plate/" + plate.tag()));
		}

		for (SubtypeRawOre raw : SubtypeRawOre.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_RAWORE.getValue(raw), Parent.GENERATED, itemLoc("rawore/" + raw.tag()));
		}

		for (SubtypeRod rod : SubtypeRod.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_ROD.getValue(rod), Parent.GENERATED, itemLoc("rod/" + rod.tag()));
		}

		// bare
		for (SubtypeWire wire : DataGenerators.getWires(WireMaterial.values(), InsulationMaterial.BARE, WireClass.BARE, WireColor.NONE)) {
			layeredBuilder(name(ElectrodynamicsItems.ITEMS_WIRE.getValue(wire)), Parent.GENERATED, itemLoc("wire/" + wire.tag())).transforms().transform(ItemDisplayContext.GUI).scale(0.7F).end();
		}

		// insulated
		for (SubtypeWire wire : DataGenerators.getWires(WireMaterial.values(), InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.values())) {
			layeredItem(ElectrodynamicsItems.ITEMS_WIRE.getValue(wire), Parent.GENERATED, itemLoc("wire/wireinsulated" + wire.getWireMaterial().toString()), itemLoc("wire/wireinsulatedcoil"));
		}

		// logistical
		for (SubtypeWire wire : DataGenerators.getWires(WireMaterial.values(), InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.values())) {
			layeredItem(ElectrodynamicsItems.ITEMS_WIRE.getValue(wire), Parent.GENERATED, itemLoc("wire/wirelogistics" + wire.getWireMaterial().toString()), itemLoc("wire/wirelogisticscoil"), itemLoc("wire/wirelogisticsredstone"));
		}

		// ceramic
		for (SubtypeWire wire : DataGenerators.getWires(WireMaterial.values(), InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.values())) {
			layeredItem(ElectrodynamicsItems.ITEMS_WIRE.getValue(wire), Parent.GENERATED, itemLoc("wire/wireceramicinsulated" + wire.getWireMaterial().toString()), itemLoc("wire/wireceramicinsulatedcolortips"), itemLoc("wire/wireceramicinsulatedcoil"));
		}

		// highly insulated
		for (SubtypeWire wire : DataGenerators.getWires(WireMaterial.values(), InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.values())) {
			layeredItem(ElectrodynamicsItems.ITEMS_WIRE.getValue(wire), Parent.GENERATED, itemLoc("wire/wirehighlyinsulated" + wire.getWireMaterial().toString()), itemLoc("wire/wirehighlyinsulatedcoil"));
		}

		for (SubtypeFluidPipe pipe : SubtypeFluidPipe.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_PIPE.getValue(pipe), Parent.GENERATED, itemLoc("pipe/" + pipe.tag()));
		}

		for (SubtypeGasPipe pipe : SubtypeGasPipe.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_GASPIPE.getValue(pipe), Parent.GENERATED, itemLoc("gaspipe/" + pipe.tag()));
		}

		for (SubtypeChromotographyCard card : SubtypeChromotographyCard.values()) {
			layeredItem(ElectrodynamicsItems.ITEMS_CHROMOTOGRAPHYCARD.getValue(card), Parent.GENERATED, itemLoc("chromotographycard/" + card.tag()));
		}

		simpleBlockItem(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.advancedsolarpanel), existingBlock(blockLoc("advancedsolarpanelitem"))).transforms().transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(35, 45, 0).translation(0, 2.5F, 0).scale(0.375F).end().transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(35, 45, 0).translation(0, 2.5F, 0).scale(0.375F).end().transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, 45, 0).scale(0.4F).end().transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 225, 0).scale(0.4F).end().transform(ItemDisplayContext.GUI).rotation(30, 225, 0).translation(0, -3F, 0).scale(0.265F).end();
		simpleBlockItem(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.hydroelectricgenerator), existingBlock(blockLoc("hydroelectricgeneratoritem"))).transforms().transform(ItemDisplayContext.GUI).rotation(30, 225, 0).translation(1.85F, 1.0F, 0).scale(0.55F).end();
		simpleBlockItem(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrusher), existingBlock(blockLoc("mineralcrusheritem")));
		simpleBlockItem(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrusherdouble), existingBlock(blockLoc("mineralcrusherdoubleitem")));
		simpleBlockItem(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralcrushertriple), existingBlock(blockLoc("mineralcrushertripleitem")));
		simpleBlockItem(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrinder), existingBlock(blockLoc("mineralgrinderitem")));
		simpleBlockItem(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrinderdouble), existingBlock(blockLoc("mineralgrinderdoubleitem")));
		simpleBlockItem(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.mineralgrindertriple), existingBlock(blockLoc("mineralgrindertripleitem")));
		simpleBlockItem(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.motorcomplex), existingBlock(blockLoc("motorcomplexitem")));
		simpleBlockItem(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.windmill), existingBlock(blockLoc("windmillitem")));
		simpleBlockItem(ElectrodynamicsBlocks.BLOCK_ADVANCEDCOMPRESSOR.get(), existingBlock(blockLoc("advancedcompressoritem"))).transforms().transform(ItemDisplayContext.GUI).scale(0.3333F).rotation(30.0F, 225.0F, 0.0F).end();
		simpleBlockItem(ElectrodynamicsBlocks.BLOCK_ADVANCEDDECOMPRESSOR.get(), existingBlock(blockLoc("advanceddecompressoritem"))).transforms().transform(ItemDisplayContext.GUI).rotation(30.0F, 225.0F, 0.0F).scale(0.3333F).end();
		simpleBlockItem(ElectrodynamicsBlocks.BLOCK_ADVANCED_THERMOELECTRICMANIPULATOR.get(), existingBlock(blockLoc("advancedthermoelectricmanipulatoritem"))).transforms().transform(ItemDisplayContext.GUI).rotation(30.0F, 225.0F, 0.0F).scale(0.3333F).end();
		simpleBlockItem(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOR.get(), existingBlock(blockLoc("chemicalreactoritem"))).transforms().transform(ItemDisplayContext.GUI).scale(0.3F).rotation(30.0F, 225.0F, 0.0F).end();

		simpleBlockItem(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.gaspipepump), existingBlock(blockLoc("gaspipepumpitem")));
		simpleBlockItem(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.fluidpipepump), existingBlock(blockLoc("fluidpipepumpitem")));

	}

	public void layeredItem(DeferredHolder<Item, ? extends Item> item, Parent parent, ResourceLocation... textures) {
		layeredItem(name(item), parent, textures);
	}

	public void layeredItem(Item item, Parent parent, ResourceLocation... textures) {
		layeredItem(name(item), parent, textures);
	}

	public void layeredItem(String name, Parent parent, ResourceLocation... textures) {
		layeredBuilder(name, parent, textures);
	}

	public void toggleableItem(DeferredHolder<Item, ? extends Item> item, String toggle, Parent parentOff, Parent parentOn, ResourceLocation[] offText, ResourceLocation[] onText) {
		toggleableItem(name(item), toggle, parentOff, parentOn, offText, onText);
	}

	public void toggleableItem(String name, String toggle, Parent parentOff, Parent parentOn, ResourceLocation[] offText, ResourceLocation[] onText) {
		ItemModelBuilder off = layeredBuilder(name, parentOff, offText);
		ItemModelBuilder on = layeredBuilder(name + toggle, parentOn, onText);
		off.override().predicate(ClientRegister.ON, 1.0F).model(on).end();
	}

	public ItemModelBuilder layeredBuilder(String name, Parent parent, ResourceLocation... textures) {
		if (textures == null || textures.length == 0) {
			throw new UnsupportedOperationException("You need to provide at least one texture");
		}
		ItemModelBuilder builder = withExistingParent(name, parent.loc());
		int counter = 0;
		for (ResourceLocation location : textures) {
			builder.texture("layer" + counter, location);
			counter++;
		}
		return builder;
	}

	public DynamicFluidContainerModelBuilder<ItemModelBuilder> getBucketModel(DeferredHolder<Item, ? extends Item> item, Parent parent) {
		return getBucketModel(name(item), parent);
	}

	public DynamicFluidContainerModelBuilder<ItemModelBuilder> getBucketModel(String name, Parent parent) {
		return withExistingParent(name, parent.loc).customLoader(DynamicFluidContainerModelBuilder::begin);
	}

	public ItemModelBuilder simpleBlockItem(Block block, ModelFile model) {
		return getBuilder(key(block).getPath()).parent(model);
	}

	public ResourceLocation key(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	public ResourceLocation itemLoc(String texture) {
		return modLoc("item/" + texture);
	}

	public ResourceLocation blockLoc(String texture) {
		return modLoc("block/" + texture);
	}

	public String name(DeferredHolder<Item, ? extends Item> item) {
		return name(item.get());
	}

	public String name(Item item) {
		return BuiltInRegistries.ITEM.getKey(item).getPath();
	}

	public ExistingModelFile existingBlock(DeferredHolder<Block, ? extends Block> block) {
		return existingBlock(block.getId());
	}

	public ExistingModelFile existingBlock(Block block) {
		return existingBlock(BuiltInRegistries.BLOCK.getKey(block));
	}

	public ExistingModelFile existingBlock(ResourceLocation loc) {
		return getExistingFile(loc);
	}

	public enum Parent {

		GENERATED(),
		HANDHELD(),
		FORGE_DEFAULT("neoforge", "item/default");

		@Nullable
		private final ResourceLocation loc;

		Parent() {
			loc = null;
		}

		Parent(String id, String loc) {
			this.loc = ResourceLocation.fromNamespaceAndPath(id, loc);
		}

		public ResourceLocation loc() {
			return loc == null ? ResourceLocation.parse(toString().toLowerCase(Locale.ROOT)) : loc;
		}
	}

}
