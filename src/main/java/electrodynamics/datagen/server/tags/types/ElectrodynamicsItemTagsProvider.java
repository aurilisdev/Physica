package electrodynamics.datagen.server.tags.types;

import java.util.concurrent.CompletableFuture;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.block.subtype.SubtypeRawOreBlock;
import electrodynamics.common.block.subtype.SubtypeResourceBlock;
import electrodynamics.common.block.subtype.SubtypeWire.InsulationMaterial;
import electrodynamics.common.block.subtype.SubtypeWire.WireClass;
import electrodynamics.common.block.subtype.SubtypeWire.WireColor;
import electrodynamics.common.block.subtype.SubtypeWire.WireMaterial;
import electrodynamics.common.blockitem.types.BlockItemWire;
import electrodynamics.common.item.subtype.SubtypeCircuit;
import electrodynamics.common.item.subtype.SubtypeDust;
import electrodynamics.common.item.subtype.SubtypeGear;
import electrodynamics.common.item.subtype.SubtypeImpureDust;
import electrodynamics.common.item.subtype.SubtypeIngot;
import electrodynamics.common.item.subtype.SubtypeNugget;
import electrodynamics.common.item.subtype.SubtypeOxide;
import electrodynamics.common.item.subtype.SubtypePlate;
import electrodynamics.common.item.subtype.SubtypeRawOre;
import electrodynamics.common.item.subtype.SubtypeRod;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.DataGenerators;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ElectrodynamicsItemTagsProvider extends ItemTagsProvider {

	public ElectrodynamicsItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider provider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, provider.contentsGetter(), References.ID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider pProvider) {

		for (SubtypeCircuit circuit : SubtypeCircuit.values()) {
			tag(circuit.tag).add(ElectrodynamicsItems.ITEMS_CIRCUIT.getValue(circuit));
		}

		for (SubtypeDust dust : SubtypeDust.values()) {
			tag(dust.tag).add(ElectrodynamicsItems.ITEMS_DUST.getValue(dust));
		}

		for (SubtypeGear gear : SubtypeGear.values()) {
			tag(gear.tag).add(ElectrodynamicsItems.ITEMS_GEAR.getValue(gear));
		}

		for (SubtypeImpureDust dust : SubtypeImpureDust.values()) {
			tag(dust.tag).add(ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(dust));
		}

		for (SubtypeIngot ingot : SubtypeIngot.values()) {
			tag(ingot.tag).add(ElectrodynamicsItems.ITEMS_INGOT.getValue(ingot));
		}

		for (SubtypeNugget nugget : SubtypeNugget.values()) {
			tag(nugget.tag).add(ElectrodynamicsItems.ITEMS_NUGGET.getValue(nugget));
		}

		for (SubtypeOre ore : SubtypeOre.values()) {
			tag(ore.itemTag).add(ElectrodynamicsItems.ITEMS_ORE.getValue(ore));
		}

		for (SubtypeOreDeepslate ore : SubtypeOreDeepslate.values()) {
			tag(ore.itemTag).add(ElectrodynamicsItems.ITEMS_DEEPSLATEORE.getValue(ore));
		}

		for (SubtypeOxide oxide : SubtypeOxide.values()) {
			tag(oxide.tag).add(ElectrodynamicsItems.ITEMS_OXIDE.getValue(oxide));
		}

		for (SubtypeRod rod : SubtypeRod.values()) {
			tag(rod.tag).add(ElectrodynamicsItems.ITEMS_ROD.getValue(rod));
		}

		for (SubtypeRawOre raw : SubtypeRawOre.values()) {
			tag(raw.tag).add(ElectrodynamicsItems.ITEMS_RAWORE.getValue(raw));
		}

		for (SubtypePlate plate : SubtypePlate.values()) {
			tag(plate.tag).add(ElectrodynamicsItems.ITEMS_PLATE.getValue(plate));
		}

		for (SubtypeResourceBlock storage : SubtypeResourceBlock.values()) {
			tag(storage.itemTag).add(ElectrodynamicsItems.ITEMS_RESOURCEBLOCK.getValue(storage));
		}

		for (SubtypeRawOreBlock block : SubtypeRawOreBlock.values()) {
			tag(block.itemTag).add(ElectrodynamicsItems.ITEMS_RAWOREBLOCK.getValue(block));
		}

		TagAppender<Item> gears = tag(ElectrodynamicsTags.Items.GEARS);

		for (SubtypeGear gear : SubtypeGear.values()) {
			gears.addTag(gear.tag);
		}

		TagAppender<Item> ingots = tag(ElectrodynamicsTags.Items.INGOTS);

		for (SubtypeIngot ingot : SubtypeIngot.values()) {
			ingots.addTag(ingot.tag);
		}

		TagAppender<Item> ores = tag(ElectrodynamicsTags.Items.ORES);

		for (SubtypeOre ore : SubtypeOre.values()) {
			ores.addTag(ore.itemTag);
		}

		tag(ElectrodynamicsTags.Items.COAL_COKE).add(ElectrodynamicsItems.ITEM_COAL_COKE.get());

		tag(ElectrodynamicsTags.Items.PLASTIC).add(ElectrodynamicsItems.ITEM_SHEETPLASTIC.get());

		tag(ElectrodynamicsTags.Items.SLAG).add(ElectrodynamicsItems.ITEM_SLAG.get());

		tag(ElectrodynamicsTags.Items.INSULATES_PLAYER_FEET).add(ElectrodynamicsItems.ITEM_RUBBERBOOTS.get(), ElectrodynamicsItems.ITEM_COMPOSITEBOOTS.get(), ElectrodynamicsItems.ITEM_COMBATBOOTS.get());

		tag(ElectrodynamicsTags.Items.INSULATED_TIN_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.TIN }, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.values())));

		tag(ElectrodynamicsTags.Items.INSULATED_SILVER_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.SILVER }, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.values())));

		tag(ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.COPPER }, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.values())));

		tag(ElectrodynamicsTags.Items.INSULATED_GOLD_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.GOLD }, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.values())));

		tag(ElectrodynamicsTags.Items.INSULATED_IRON_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.IRON }, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.values())));

		tag(ElectrodynamicsTags.Items.INSULATED_SUPERCONDUCTIVE_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.SUPERCONDUCTIVE }, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.values())));

		tag(ElectrodynamicsTags.Items.THICK_TIN_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.TIN }, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.values())));

		tag(ElectrodynamicsTags.Items.THICK_SILVER_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.SILVER }, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.values())));

		tag(ElectrodynamicsTags.Items.THICK_COPPER_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.COPPER }, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.values())));

		tag(ElectrodynamicsTags.Items.THICK_GOLD_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.GOLD }, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.values())));

		tag(ElectrodynamicsTags.Items.THICK_IRON_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.IRON }, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.values())));

		tag(ElectrodynamicsTags.Items.THICK_SUPERCONDUCTIVE_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.SUPERCONDUCTIVE }, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.values())));

		tag(ElectrodynamicsTags.Items.CERAMIC_TIN_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.TIN }, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.values())));

		tag(ElectrodynamicsTags.Items.CERAMIC_SILVER_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.SILVER }, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.values())));

		tag(ElectrodynamicsTags.Items.CERAMIC_COPPER_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.COPPER }, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.values())));

		tag(ElectrodynamicsTags.Items.CERAMIC_GOLD_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.GOLD }, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.values())));

		tag(ElectrodynamicsTags.Items.CERAMIC_IRON_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.IRON }, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.values())));

		tag(ElectrodynamicsTags.Items.CERAMIC_SUPERCONDUCTIVE_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.SUPERCONDUCTIVE }, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.values())));

		tag(ElectrodynamicsTags.Items.LOGISTICAL_TIN_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.TIN }, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.values())));

		tag(ElectrodynamicsTags.Items.LOGISTICAL_SILVER_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.SILVER }, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.values())));

		tag(ElectrodynamicsTags.Items.LOGISTICAL_COPPER_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.COPPER }, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.values())));

		tag(ElectrodynamicsTags.Items.LOGISTICAL_GOLD_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.GOLD }, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.values())));

		tag(ElectrodynamicsTags.Items.LOGISTICAL_IRON_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.IRON }, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.values())));

		tag(ElectrodynamicsTags.Items.LOGISTICAL_SUPERCONDUCTIVE_WIRES).add(ElectrodynamicsItems.ITEMS_WIRE.getSpecificValuesArray(new BlockItemWire[0], DataGenerators.getWires(new WireMaterial[] { WireMaterial.SUPERCONDUCTIVE }, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.values())));

	}

}
