package electrodynamics.common.tags;

import electrodynamics.Electrodynamics;
import electrodynamics.api.gas.Gas;
import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ElectrodynamicsTags {

	public static void init() {
		Items.init();
		Blocks.init();
		Fluids.init();
		Gases.init();
		Enchantments.init();
	}

	public static class Items {

		public static final TagKey<Item> CIRCUITS_BASIC = forgeTag("circuits/basic");
		public static final TagKey<Item> CIRCUITS_ADVANCED = forgeTag("circuits/advanced");
		public static final TagKey<Item> CIRCUITS_ELITE = forgeTag("circuits/elite");
		public static final TagKey<Item> CIRCUITS_ULTIMATE = forgeTag("circuits/ultimate");

		public static final TagKey<Item> DUST_ALUMINUM = forgeTag("dusts/aluminum");
		public static final TagKey<Item> DUST_BRONZE = forgeTag("dusts/bronze");
		public static final TagKey<Item> DUST_COPPER = forgeTag("dusts/copper");
		public static final TagKey<Item> DUST_CHROMIUM = forgeTag("dusts/chromium");
		public static final TagKey<Item> DUST_ENDEREYE = forgeTag("dusts/endereye");
		public static final TagKey<Item> DUST_GOLD = forgeTag("dusts/gold");
		public static final TagKey<Item> DUST_IRON = forgeTag("dusts/iron");
		public static final TagKey<Item> DUST_LEAD = forgeTag("dusts/lead");
		public static final TagKey<Item> DUST_LITHIUM = forgeTag("dusts/lithium");
		public static final TagKey<Item> DUST_MOLYBDENUM = forgeTag("dusts/molybdenum");
		public static final TagKey<Item> DUST_NETHERITE = forgeTag("dusts/netherite");
		public static final TagKey<Item> DUST_OBSIDIAN = forgeTag("dusts/obsidian");
		public static final TagKey<Item> DUST_SALT = forgeTag("dusts/salt");
		public static final TagKey<Item> DUST_SALTPETER = forgeTag("dusts/saltpeter");
		public static final TagKey<Item> DUST_SILICA = forgeTag("dusts/silica");
		public static final TagKey<Item> DUST_SILVER = forgeTag("dusts/silver");
		public static final TagKey<Item> DUST_STEEL = forgeTag("dusts/steel");
		public static final TagKey<Item> DUST_SULFUR = forgeTag("dusts/sulfur");
		public static final TagKey<Item> DUST_SUPERCONDUCTIVE = forgeTag("dusts/superconductive");
		public static final TagKey<Item> DUST_TIN = forgeTag("dusts/tin");
		public static final TagKey<Item> DUST_TITANIUM = forgeTag("dusts/titanium");
		public static final TagKey<Item> DUST_VANADIUM = forgeTag("dusts/vanadium");

		public static final TagKey<Item> GEAR_BRONZE = forgeTag("gears/bronze");
		public static final TagKey<Item> GEAR_COPPER = forgeTag("gears/copper");
		public static final TagKey<Item> GEAR_IRON = forgeTag("gears/iron");
		public static final TagKey<Item> GEAR_STEEL = forgeTag("gears/steel");
		public static final TagKey<Item> GEAR_TIN = forgeTag("gears/tin");

		public static final TagKey<Item> IMPURE_DUST_ALUMINUM = forgeTag("impuredusts/aluminum");
		public static final TagKey<Item> IMPURE_DUST_COPPER = forgeTag("impuredusts/copper");
		public static final TagKey<Item> IMPURE_DUST_CHROMIUM = forgeTag("impuredusts/chromium");
		public static final TagKey<Item> IMPURE_DUST_GOLD = forgeTag("impuredusts/gold");
		public static final TagKey<Item> IMPURE_DUST_IRON = forgeTag("impuredusts/iron");
		public static final TagKey<Item> IMPURE_DUST_LEAD = forgeTag("impuredusts/lead");
		public static final TagKey<Item> IMPURE_DUST_LITHIUM = forgeTag("impuredusts/lithium");
		public static final TagKey<Item> IMPURE_DUST_MOLYBDENUM = forgeTag("impuredusts/molybdenum");
		public static final TagKey<Item> IMPURE_DUST_NETHERITE = forgeTag("impuredusts/netherite");
		public static final TagKey<Item> IMPURE_DUST_SILVER = forgeTag("impuredusts/silver");
		public static final TagKey<Item> IMPURE_DUST_TIN = forgeTag("impuredusts/tin");
		public static final TagKey<Item> IMPURE_DUST_TITANIUM = forgeTag("impuredusts/titanium");
		public static final TagKey<Item> IMPURE_DUST_VANADIUM = forgeTag("impuredusts/vanadium");

		public static final TagKey<Item> INGOT_ALUMINUM = forgeTag("ingots/aluminum");
		public static final TagKey<Item> INGOT_BRONZE = forgeTag("ingots/bronze");
		public static final TagKey<Item> INGOT_CHROMIUM = forgeTag("ingots/chromium");
		public static final TagKey<Item> INGOT_HSLASTEEL = forgeTag("ingots/hslasteel");
		public static final TagKey<Item> INGOT_LEAD = forgeTag("ingots/lead");
		public static final TagKey<Item> INGOT_LITHIUM = forgeTag("ingots/lithium");
		public static final TagKey<Item> INGOT_MOLYBDENUM = forgeTag("ingots/molybdenum");
		public static final TagKey<Item> INGOT_SILVER = forgeTag("ingots/silver");
		public static final TagKey<Item> INGOT_STAINLESSSTEEL = forgeTag("ingots/stainlesssteel");
		public static final TagKey<Item> INGOT_STEEL = forgeTag("ingots/steel");
		public static final TagKey<Item> INGOT_SUPERCONDUCTIVE = forgeTag("ingots/superconductive");
		public static final TagKey<Item> INGOT_TIN = forgeTag("ingots/tin");
		public static final TagKey<Item> INGOT_TITANIUM = forgeTag("ingots/titanium");
		public static final TagKey<Item> INGOT_TITANIUMCARBIDE = forgeTag("ingots/titaniumcarbide");
		public static final TagKey<Item> INGOT_VANADIUM = forgeTag("ingots/vanadium");
		public static final TagKey<Item> INGOT_VANADIUMSTEEL = forgeTag("ingots/vanadiumsteel");

		public static final TagKey<Item> NUGGET_COPPER = forgeTag("nuggets/copper");
		public static final TagKey<Item> NUGGET_HSLASTEEL = forgeTag("nuggets/hslasteel");
		public static final TagKey<Item> NUGGET_SILVER = forgeTag("nuggets/silver");
		public static final TagKey<Item> NUGGET_STAINLESSSTEEL = forgeTag("nuggets/stainlesssteel");
		public static final TagKey<Item> NUGGET_STEEL = forgeTag("nuggets/steel");
		public static final TagKey<Item> NUGGET_SUPERCONDUCTIVE = forgeTag("nuggets/superconductive");
		public static final TagKey<Item> NUGGET_TIN = forgeTag("nuggets/tin");
		public static final TagKey<Item> NUGGET_TITANIUMCARBIDE = forgeTag("nuggets/titaniumcarbide");

		public static final TagKey<Item> ORE_ALUMINUM = forgeTag("ores/aluminum");
		public static final TagKey<Item> ORE_CHROMIUM = forgeTag("ores/chromium");
		public static final TagKey<Item> ORE_FLUORITE = forgeTag("ores/fluorite");
		public static final TagKey<Item> ORE_LEAD = forgeTag("ores/lead");
		public static final TagKey<Item> ORE_LITHIUM = forgeTag("ores/lithium");
		public static final TagKey<Item> ORE_MOLYBDENUM = forgeTag("ores/molybdenum");
		public static final TagKey<Item> ORE_MONAZITE = forgeTag("ores/monazite");
		public static final TagKey<Item> ORE_POTASSIUMCHLORIDE = forgeTag("ores/potassiumchloride");
		public static final TagKey<Item> ORE_SALT = forgeTag("ores/salt");
		public static final TagKey<Item> ORE_SALTPETER = forgeTag("ores/saltpeter");
		public static final TagKey<Item> ORE_SILVER = forgeTag("ores/silver");
		public static final TagKey<Item> ORE_SULFUR = forgeTag("ores/sulfur");
		public static final TagKey<Item> ORE_THORIUM = forgeTag("ores/thorium");
		public static final TagKey<Item> ORE_TIN = forgeTag("ores/tin");
		public static final TagKey<Item> ORE_TITANIUM = forgeTag("ores/titanium");
		public static final TagKey<Item> ORE_URANIUM = forgeTag("ores/uranium");
		public static final TagKey<Item> ORE_VANADIUM = forgeTag("ores/vanadium");

		public static final TagKey<Item> OXIDE_CALCIUMCARBONATE = forgeTag("oxides/calciumcarbonate");
		public static final TagKey<Item> OXIDE_CHROMIUM = forgeTag("oxides/chromium");
		public static final TagKey<Item> OXIDE_CHROMIUMDISILICIDE = forgeTag("oxides/chromiumdisilicide");
		public static final TagKey<Item> OXIDE_DISULFUR = forgeTag("oxides/disulfur");
		public static final TagKey<Item> OXIDE_DITITANIUM = forgeTag("oxides/dititanium");
		public static final TagKey<Item> OXIDE_SODIUMCARBONATE = forgeTag("oxides/sodiumcarbonate");
		public static final TagKey<Item> OXIDE_SULFURDICHLORIDE = forgeTag("oxides/sulfurdichloride");
		public static final TagKey<Item> OXIDE_THIONYLCHLORIDE = forgeTag("oxides/thionylchloride");
		public static final TagKey<Item> OXIDE_TRISULFUR = forgeTag("oxides/trisulfur");
		public static final TagKey<Item> OXIDE_VANADIUM = forgeTag("oxides/vanadium");

		public static final TagKey<Item> PLATE_ALUMINUM = forgeTag("plates/aluminum");
		public static final TagKey<Item> PLATE_BRONZE = forgeTag("plates/bronze");
		public static final TagKey<Item> PLATE_HSLASTEEL = forgeTag("plates/hslasteel");
		public static final TagKey<Item> PLATE_IRON = forgeTag("plates/iron");
		public static final TagKey<Item> PLATE_LEAD = forgeTag("plates/lead");
		public static final TagKey<Item> PLATE_LITHIUM = forgeTag("plates/lithium");
		public static final TagKey<Item> PLATE_COPPER = forgeTag("plates/copper");
		public static final TagKey<Item> PLATE_STAINLESSSTEEL = forgeTag("plates/stainlesssteel");
		public static final TagKey<Item> PLATE_STEEL = forgeTag("plates/steel");
		public static final TagKey<Item> PLATE_TITANIUM = forgeTag("plates/titanium");
		public static final TagKey<Item> PLATE_TITANIUMCARBIDE = forgeTag("plates/titaniumcarbide");
		public static final TagKey<Item> PLATE_VANADIUMSTEEL = forgeTag("plates/vanadiumsteel");

		public static final TagKey<Item> RAW_ORE_CHROMIUM = forgeTag("raw_materials/chromium");
		public static final TagKey<Item> RAW_ORE_FLUORITE = forgeTag("raw_materials/fluorite");
		public static final TagKey<Item> RAW_ORE_LEAD = forgeTag("raw_materials/lead");
		public static final TagKey<Item> RAW_ORE_LEPIDOLITE = forgeTag("raw_materials/lepidolite");
		public static final TagKey<Item> RAW_ORE_SILVER = forgeTag("raw_materials/silver");
		public static final TagKey<Item> RAW_ORE_THORIUM = forgeTag("raw_materials/thorium");
		public static final TagKey<Item> RAW_ORE_TIN = forgeTag("raw_materials/tin");
		public static final TagKey<Item> RAW_ORE_TITANIUM = forgeTag("raw_materials/titanium");
		public static final TagKey<Item> RAW_ORE_URANIUM = forgeTag("raw_materials/uranium");
		public static final TagKey<Item> RAW_ORE_VANADIUM = forgeTag("raw_materials/vanadinite");

		public static final TagKey<Item> ROD_HSLASTEEL = forgeTag("rods/hslasteel");
		public static final TagKey<Item> ROD_STAINLESSSTEEL = forgeTag("rods/stainlesssteel");
		public static final TagKey<Item> ROD_STEEL = forgeTag("rods/steel");
		public static final TagKey<Item> ROD_TITANIUMCARBIDE = forgeTag("rods/titaniumcarbide");

		public static final TagKey<Item> STORAGE_BLOCK_ALUMINUM = forgeTag("storage_blocks/aluminum");
		public static final TagKey<Item> STORAGE_BLOCK_BRONZE = forgeTag("storage_blocks/bronze");
		public static final TagKey<Item> STORAGE_BLOCK_CHROMIUM = forgeTag("storage_blocks/chromium");
		public static final TagKey<Item> STORAGE_BLOCK_HSLASTEEL = forgeTag("storage_blocks/hslasteel");
		public static final TagKey<Item> STORAGE_BLOCK_LEAD = forgeTag("storage_blocks/lead");
		public static final TagKey<Item> STORAGE_BLOCK_SILVER = forgeTag("storage_blocks/silver");
		public static final TagKey<Item> STORAGE_BLOCK_STAINLESSSTEEL = forgeTag("storage_blocks/stainlesssteel");
		public static final TagKey<Item> STORAGE_BLOCK_STEEL = forgeTag("storage_blocks/steel");
		public static final TagKey<Item> STORAGE_BLOCK_TIN = forgeTag("storage_blocks/tin");
		public static final TagKey<Item> STORAGE_BLOCK_TITANIUM = forgeTag("storage_blocks/titanium");
		public static final TagKey<Item> STORAGE_BLOCK_TITANIUMCARBIDE = forgeTag("storage_blocks/titaniumcarbide");
		public static final TagKey<Item> STORAGE_BLOCK_VANADIUMSTEEL = forgeTag("storage_blocks/vanadiumsteel");

		public static final TagKey<Item> BLOCK_RAW_ORE_CHROMIUM = forgeTag("storage_blocks/rawchromium");
		public static final TagKey<Item> BLOCK_RAW_ORE_FLUORITE = forgeTag("storage_blocks/rawfluorite");
		public static final TagKey<Item> BLOCK_RAW_ORE_LEAD = forgeTag("storage_blocks/rawlead");
		public static final TagKey<Item> BLOCK_RAW_ORE_LEPIDOLITE = forgeTag("storage_blocks/rawlepidolite");
		public static final TagKey<Item> BLOCK_RAW_ORE_SILVER = forgeTag("storage_blocks/rawsilver");
		public static final TagKey<Item> BLOCK_RAW_ORE_THORIUM = forgeTag("storage_blocks/rawthorium");
		public static final TagKey<Item> BLOCK_RAW_ORE_TIN = forgeTag("storage_blocks/rawtin");
		public static final TagKey<Item> BLOCK_RAW_ORE_TITANIUM = forgeTag("storage_blocks/rawtitanium");
		public static final TagKey<Item> BLOCK_RAW_ORE_URANIUM = forgeTag("storage_blocks/rawuranium");
		public static final TagKey<Item> BLOCK_RAW_ORE_VANADIUM = forgeTag("storage_blocks/rawvanadinite");

		public static final TagKey<Item> COAL_COKE = forgeTag("coal_coke");
		public static final TagKey<Item> PLASTIC = forgeTag("plastic");
		public static final TagKey<Item> SLAG = forgeTag("slag");

		public static final TagKey<Item> GEARS = forgeTag("gears");
		public static final TagKey<Item> INGOTS = forgeTag("ingots");
		public static final TagKey<Item> ORES = forgeTag("ores");
		public static final TagKey<Item> INSULATES_PLAYER_FEET = forgeTag("insulates_player_feet");

		public static final TagKey<Item> INSULATED_COPPER_WIRES = forgeTag("wires/insulated_copper");
		public static final TagKey<Item> INSULATED_GOLD_WIRES = forgeTag("wires/insulated_gold");
		public static final TagKey<Item> INSULATED_IRON_WIRES = forgeTag("wires/insulated_iron");
		public static final TagKey<Item> INSULATED_SILVER_WIRES = forgeTag("wires/insulated_silver");
		public static final TagKey<Item> INSULATED_SUPERCONDUCTIVE_WIRES = forgeTag("wires/insulated_superconductive");
		public static final TagKey<Item> INSULATED_TIN_WIRES = forgeTag("wires/insulated_tin");

		public static final TagKey<Item> THICK_COPPER_WIRES = forgeTag("wires/thick_copper");
		public static final TagKey<Item> THICK_GOLD_WIRES = forgeTag("wires/thick_gold");
		public static final TagKey<Item> THICK_IRON_WIRES = forgeTag("wires/thick_iron");
		public static final TagKey<Item> THICK_SILVER_WIRES = forgeTag("wires/thick_silver");
		public static final TagKey<Item> THICK_SUPERCONDUCTIVE_WIRES = forgeTag("wires/thick_superconductive");
		public static final TagKey<Item> THICK_TIN_WIRES = forgeTag("wires/thick_tin");

		public static final TagKey<Item> CERAMIC_COPPER_WIRES = forgeTag("wires/ceramic_copper");
		public static final TagKey<Item> CERAMIC_GOLD_WIRES = forgeTag("wires/ceramic_gold");
		public static final TagKey<Item> CERAMIC_IRON_WIRES = forgeTag("wires/ceramic_iron");
		public static final TagKey<Item> CERAMIC_SILVER_WIRES = forgeTag("wires/ceramic_silver");
		public static final TagKey<Item> CERAMIC_SUPERCONDUCTIVE_WIRES = forgeTag("wires/ceramic_superconductive");
		public static final TagKey<Item> CERAMIC_TIN_WIRES = forgeTag("wires/ceramic_tin");

		public static final TagKey<Item> LOGISTICAL_COPPER_WIRES = forgeTag("wires/logistical_copper");
		public static final TagKey<Item> LOGISTICAL_GOLD_WIRES = forgeTag("wires/logistical_gold");
		public static final TagKey<Item> LOGISTICAL_IRON_WIRES = forgeTag("wires/logistical_iron");
		public static final TagKey<Item> LOGISTICAL_SILVER_WIRES = forgeTag("wires/logistical_silver");
		public static final TagKey<Item> LOGISTICAL_SUPERCONDUCTIVE_WIRES = forgeTag("wires/logistical_superconductive");
		public static final TagKey<Item> LOGISTICAL_TIN_WIRES = forgeTag("wires/logistical_tin");

		private static void init() {
		}

		private static TagKey<Item> forgeTag(String name) {
			return ItemTags.create(Electrodynamics.commonrl(name));
		}
	}

	public static class Blocks {

		public static final TagKey<Block> ORE_ALUMINUM = forgeTag("ores/aluminum");
		public static final TagKey<Block> ORE_CHROMIUM = forgeTag("ores/chromium");
		public static final TagKey<Block> ORE_FLUORITE = forgeTag("ores/fluorite");
		public static final TagKey<Block> ORE_LEAD = forgeTag("ores/lead");
		public static final TagKey<Block> ORE_LITHIUM = forgeTag("ores/lithium");
		public static final TagKey<Block> ORE_MOLYBDENUM = forgeTag("ores/molybdenum");
		public static final TagKey<Block> ORE_MONAZITE = forgeTag("ores/monazite");
		public static final TagKey<Block> ORE_POTASSIUMCHLORIDE = forgeTag("ores/potassiumchloride");
		public static final TagKey<Block> ORE_SALT = forgeTag("ores/salt");
		public static final TagKey<Block> ORE_SALTPETER = forgeTag("ores/saltpeter");
		public static final TagKey<Block> ORE_SILVER = forgeTag("ores/silver");
		public static final TagKey<Block> ORE_SULFUR = forgeTag("ores/sulfur");
		public static final TagKey<Block> ORE_THORIUM = forgeTag("ores/thorium");
		public static final TagKey<Block> ORE_TIN = forgeTag("ores/tin");
		public static final TagKey<Block> ORE_TITANIUM = forgeTag("ores/titanium");
		public static final TagKey<Block> ORE_URANIUM = forgeTag("ores/uranium");
		public static final TagKey<Block> ORE_VANADIUM = forgeTag("ores/vanadium");

		public static final TagKey<Block> STORAGE_BLOCK_ALUMINUM = forgeTag("storage_blocks/aluminum");
		public static final TagKey<Block> STORAGE_BLOCK_BRONZE = forgeTag("storage_blocks/bronze");
		public static final TagKey<Block> STORAGE_BLOCK_CHROMIUM = forgeTag("storage_blocks/chromium");
		public static final TagKey<Block> STORAGE_BLOCK_HSLASTEEL = forgeTag("storage_blocks/hslasteel");
		public static final TagKey<Block> STORAGE_BLOCK_LEAD = forgeTag("storage_blocks/lead");
		public static final TagKey<Block> STORAGE_BLOCK_SILVER = forgeTag("storage_blocks/silver");
		public static final TagKey<Block> STORAGE_BLOCK_STAINLESSSTEEL = forgeTag("storage_blocks/stainlesssteel");
		public static final TagKey<Block> STORAGE_BLOCK_STEEL = forgeTag("storage_blocks/steel");
		public static final TagKey<Block> STORAGE_BLOCK_TIN = forgeTag("storage_blocks/tin");
		public static final TagKey<Block> STORAGE_BLOCK_TITANIUM = forgeTag("storage_blocks/titanium");
		public static final TagKey<Block> STORAGE_BLOCK_TITANIUMCARBIDE = forgeTag("storage_blocks/titaniumcarbide");
		public static final TagKey<Block> STORAGE_BLOCK_VANADIUMSTEEL = forgeTag("storage_blocks/vanadiumsteel");

		public static final TagKey<Block> BLOCK_RAW_ORE_CHROMIUM = forgeTag("storage_blocks/rawchromium");
		public static final TagKey<Block> BLOCK_RAW_ORE_FLUORITE = forgeTag("storage_blocks/rawfluorite");
		public static final TagKey<Block> BLOCK_RAW_ORE_LEAD = forgeTag("storage_blocks/rawlead");
		public static final TagKey<Block> BLOCK_RAW_ORE_LEPIDOLITE = forgeTag("storage_blocks/rawlepidolite");
		public static final TagKey<Block> BLOCK_RAW_ORE_SILVER = forgeTag("storage_blocks/rawsilver");
		public static final TagKey<Block> BLOCK_RAW_ORE_THORIUM = forgeTag("storage_blocks/rawthorium");
		public static final TagKey<Block> BLOCK_RAW_ORE_TIN = forgeTag("storage_blocks/rawtin");
		public static final TagKey<Block> BLOCK_RAW_ORE_TITANIUM = forgeTag("storage_blocks/rawtitanium");
		public static final TagKey<Block> BLOCK_RAW_ORE_URANIUM = forgeTag("storage_blocks/rawuranium");
		public static final TagKey<Block> BLOCK_RAW_ORE_VANADIUM = forgeTag("storage_blocks/rawvanadinite");


		public static final TagKey<Block> ELECTRIC_DRILL_BLOCKS = forgeTag("electricdrillblocks");

		public static final TagKey<Block> ORES = forgeTag("ores");

		private static void init() {
		}

		private static TagKey<Block> forgeTag(String name) {
			return BlockTags.create(Electrodynamics.commonrl(name));
		}

	}

	// Only the Tag objects should ever be visible from this class!
	public static class Fluids {

		public static final TagKey<Fluid> AMMONIA = forgeTag("ammonia");
		public static final TagKey<Fluid> SULFURIC_ACID = forgeTag("sulfuric_acid");
		public static final TagKey<Fluid> HYDROFLUORIC_ACID = forgeTag("hydrofluoric_acid");
		public static final TagKey<Fluid> HYDROCHLORIC_ACID = forgeTag("hydrochloric_acid");
		public static final TagKey<Fluid> NITRIC_ACID = forgeTag("nitric_acid");
		public static final TagKey<Fluid> AQUA_REGIA = forgeTag("aquaregia");
		public static final TagKey<Fluid> ETHANOL = forgeTag("ethanol");
		public static final TagKey<Fluid> POLYETHLYENE = forgeTag("polyethylene");
		public static final TagKey<Fluid> CLAY = forgeTag("clay");
		public static final TagKey<Fluid> OXYGEN = forgeTag("oxygen");
		public static final TagKey<Fluid> HYDROGEN = forgeTag("hydrogen");
		public static final TagKey<Fluid> HYDRAULIC_FLUID = forgeTag("hydraulic_fluid");
		public static final TagKey<Fluid> COPPER_SULFATE = forgeTag("sulfates/copper");
		public static final TagKey<Fluid> TIN_SULFATE = forgeTag("sulfates/tin");
		public static final TagKey<Fluid> SILVER_SULFATE = forgeTag("sulfates/silver");
		public static final TagKey<Fluid> LEAD_SULFATE = forgeTag("sulfates/lead");
		public static final TagKey<Fluid> VANADIUM_SULFATE = forgeTag("sulfates/vanadium");
		public static final TagKey<Fluid> IRON_SULFATE = forgeTag("sulfates/iron");
		public static final TagKey<Fluid> GOLD_SULFATE = forgeTag("sulfates/gold");
		public static final TagKey<Fluid> LITHIUM_SULFATE = forgeTag("sulfates/lithium");
		public static final TagKey<Fluid> MOLYBDENUM_SULFATE = forgeTag("sulfates/molybdenum");
		public static final TagKey<Fluid> NETHERITE_SULFATE = forgeTag("sulfates/netherite");
		public static final TagKey<Fluid> PURE_COPPER = forgeTag("puremineral/copper");
		public static final TagKey<Fluid> PURE_TIN = forgeTag("puremineral/tin");
		public static final TagKey<Fluid> PURE_SILVER = forgeTag("puremineral/silver");
		public static final TagKey<Fluid> PURE_LEAD = forgeTag("puremineral/lead");
		public static final TagKey<Fluid> PURE_VANADIUM = forgeTag("puremineral/vanadium");
		public static final TagKey<Fluid> PURE_IRON = forgeTag("puremineral/iron");
		public static final TagKey<Fluid> PURE_GOLD = forgeTag("puremineral/gold");
		public static final TagKey<Fluid> PURE_LITHIUM = forgeTag("puremineral/lithium");
		public static final TagKey<Fluid> PURE_MOLYBDENUM = forgeTag("puremineral/molybdenum");
		public static final TagKey<Fluid> PURE_NETHERITE = forgeTag("puremineral/netherite");
		public static final TagKey<Fluid> PURE_ALUMINUM = forgeTag("puremineral/aluminum");
		public static final TagKey<Fluid> PURE_CHROMIUM = forgeTag("puremineral/chromium");
		public static final TagKey<Fluid> PURE_TITANIUM = forgeTag("puremineral/titanium");

		private static void init() {

		}

		private static TagKey<Fluid> forgeTag(String name) {
			return FluidTags.create(Electrodynamics.commonrl(name));
		}

	}

	public static class Gases {

		public static final TagKey<Gas> HYDROGEN = forgeTag("hydrogen");
		public static final TagKey<Gas> OXYGEN = forgeTag("oxygen");
		public static final TagKey<Gas> STEAM = forgeTag("steam");
		public static final TagKey<Gas> NITROGEN = forgeTag("nitrogen");
		public static final TagKey<Gas> CARBON_DIOXIDE = forgeTag("carbondioxide");
		public static final TagKey<Gas> ARGON = forgeTag("argon");
		public static final TagKey<Gas> SULFUR_DIOXIDE = forgeTag("sulfurdioxide");
		public static final TagKey<Gas> AMMONIA = forgeTag("ammonia");


		public static final TagKey<Gas> IS_CORROSIVE = forgeTag("iscorrosive");

		private static void init() {

		}

		private static TagKey<Gas> forgeTag(String name) {
			return create(Electrodynamics.commonrl(name));
		}

		public static TagKey<Gas> create(ResourceLocation loc) {
			return TagKey.create(ElectrodynamicsGases.GAS_REGISTRY_KEY, loc);
		}
	}

	public static class Enchantments {

		public static final TagKey<Enchantment> EFFICIENCY = forgeTag("efficiency");
		public static final TagKey<Enchantment> UNBREAKING = forgeTag("unbreaking");

		public static final TagKey<Enchantment> SILK_TOUCH = forgeTag("silk_touch");

		private static void init()  { }

		private static TagKey<Enchantment> forgeTag(String name) {
			return TagKey.create(Registries.ENCHANTMENT,Electrodynamics.commonrl(name));
		}
	}

}
