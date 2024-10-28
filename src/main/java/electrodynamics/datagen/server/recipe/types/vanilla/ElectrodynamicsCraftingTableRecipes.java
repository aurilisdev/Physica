package electrodynamics.datagen.server.recipe.types.vanilla;

import com.google.common.collect.Lists;
import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeFluidPipe;
import electrodynamics.common.block.subtype.SubtypeGasPipe;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.block.subtype.SubtypeRawOreBlock;
import electrodynamics.common.block.subtype.SubtypeResourceBlock;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.block.subtype.SubtypeWire.Conductor;
import electrodynamics.common.block.subtype.SubtypeWire.InsulationMaterial;
import electrodynamics.common.block.subtype.SubtypeWire.WireClass;
import electrodynamics.common.block.subtype.SubtypeWire.WireColor;
import electrodynamics.common.item.subtype.*;
import electrodynamics.common.recipe.recipeutils.EnchantmentIngredient;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.ShapedCraftingRecipeBuilder;
import electrodynamics.datagen.utils.recipe.ShapelessCraftingRecipeBuilder;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

public class ElectrodynamicsCraftingTableRecipes extends AbstractRecipeGenerator {

    @Override
    public void addRecipes(RecipeOutput output) {

        addMachine(output);
        addGear(output);
        addWires(output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_BATTERY.get(), 1)
                //
                .addPattern(" C ")
                //
                .addPattern("TRT")
                //
                .addPattern("TWT")
                //
                .addKey('C', ItemTags.COALS)
                //
                .addKey('T', ElectrodynamicsTags.Items.INGOT_TIN)
                //
                .addKey('R', Tags.Items.DUSTS_REDSTONE)
                //
                .addKey('W', WIRES[SubtypeWire.copper.ordinal()])
                //
                .complete(References.ID, "battery_basic", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_LITHIUMBATTERY.get(), 1)
                //
                .addPattern(" L ")
                //
                .addPattern("SCS")
                //
                .addPattern("SWS")
                //
                .addKey('L', ElectrodynamicsTags.Items.PLATE_LITHIUM)
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', OXIDES[SubtypeOxide.thionylchloride.ordinal()])
                //
                .addKey('W', WIRES[SubtypeWire.gold.ordinal()])
                //
                .complete(References.ID, "battery_lithium", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_CARBYNEBATTERY.get(), 1)
                //
                .addPattern(" S ")
                //
                .addPattern("TNT")
                //
                .addPattern("TNT")
                //
                .addKey('S', WIRES[SubtypeWire.superconductive.ordinal()])
                //
                .addKey('T', ElectrodynamicsTags.Items.PLATE_TITANIUMCARBIDE)
                //
                .addKey('N', ElectrodynamicsTags.Items.DUST_NETHERITE)
                //
                .complete(References.ID, "battery_carbyne", output);

        ShapedCraftingRecipeBuilder.start(CERAMICS[SubtypeCeramic.fuse.ordinal()], 1)
                //
                .addPattern("#P#")
                //
                .addPattern(" W ")
                //
                .addPattern("#P#")
                //
                .addKey('#', CERAMICS[SubtypeCeramic.cooked.ordinal()])
                //
                .addKey('P', CERAMICS[SubtypeCeramic.plate.ordinal()])
                //
                .addKey('W', WIRES[SubtypeWire.copper.ordinal()])
                //
                .complete(References.ID, "ceramic_fuse", output);

        ShapedCraftingRecipeBuilder.start(CERAMICS[SubtypeCeramic.plate.ordinal()], 1)
                //
                .addPattern("###")
                //
                .addKey('#', CERAMICS[SubtypeCeramic.cooked.ordinal()])
                //
                .complete(References.ID, "ceramic_plate", output);

        ShapedCraftingRecipeBuilder.start(CERAMICS[SubtypeCeramic.wet.ordinal()], 4)
                //
                .addPattern("SCS")
                //
                .addPattern("CWC")
                //
                .addPattern("SCS")
                //
                .addKey('S', Tags.Items.SANDS)
                //
                .addKey('C', Items.CLAY_BALL)
                //
                .addKey('W', Items.WATER_BUCKET)
                //
                .complete(References.ID, "wet_ceramic", output);

        ShapedCraftingRecipeBuilder.start(CIRCUITS[SubtypeCircuit.basic.ordinal()], 1)
                //
                .addPattern("WRW")
                //
                .addPattern("RPR")
                //
                .addPattern("WRW")
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES)
                //
                .addKey('R', Tags.Items.DUSTS_REDSTONE)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .complete(References.ID, "circuit_basic", output);

        ShapedCraftingRecipeBuilder.start(CIRCUITS[SubtypeCircuit.advanced.ordinal()], 1)
                //
                .addPattern("QQQ")
                //
                .addPattern("CDC")
                //
                .addPattern("QQQ")
                //
                .addKey('Q', Tags.Items.GEMS_QUARTZ)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('D', Tags.Items.GEMS_DIAMOND)
                //
                .complete(References.ID, "circuit_advanced", output);

        ShapedCraftingRecipeBuilder.start(CIRCUITS[SubtypeCircuit.elite.ordinal()], 1)
                //
                .addPattern("GGG")
                //
                .addPattern("CBC")
                //
                .addPattern("GGG")
                //
                .addKey('G', Tags.Items.INGOTS_GOLD)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .addKey('B', Tags.Items.STORAGE_BLOCKS_LAPIS)
                //
                .complete(References.ID, "circuit_elite", output);

        ShapedCraftingRecipeBuilder.start(CIRCUITS[SubtypeCircuit.ultimate.ordinal()], 1)
                //
                .addPattern("OPO")
                //
                .addPattern("COC")
                //
                .addPattern("OPO")
                //
                .addKey('O', ElectrodynamicsTags.Items.DUST_OBSIDIAN)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .complete(References.ID, "circuit_ultimate", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_COIL.get(), 1)
                //
                .addPattern(" W ")
                //
                .addPattern("WIW")
                //
                .addPattern(" W ")
                //
                .addKey('W', WIRES[SubtypeWire.copper.ordinal()])
                //
                .addKey('I', Tags.Items.INGOTS_IRON)
                //
                .complete(References.ID, "copper_coil", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_LAMINATEDCOIL.get(), 1)
                //
                .addPattern(" P ")
                //
                .addPattern("PCP")
                //
                .addPattern(" P ")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLASTIC)
                //
                .addKey('C', ElectrodynamicsItems.ITEM_COIL.get())
                //
                .complete(References.ID, "copper_coil_laminated", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_RAWCOMPOSITEPLATING.get(), 6)
                //
                .addPattern("TTT")
                //
                .addPattern("CCC")
                //
                .addPattern("PPP")
                //
                .addKey('T', ElectrodynamicsTags.Items.PLATE_TITANIUM)
                //
                .addKey('C', CERAMICS[SubtypeCeramic.plate.ordinal()])
                //
                .addKey('P', ElectrodynamicsTags.Items.PLASTIC)
                //
                .complete(References.ID, "raw_composite_plating", output);

        for (SubtypeNugget nugget : SubtypeNugget.values()) {
            ShapedCraftingRecipeBuilder.start(nugget.productIngot.get(), 1)
                    //
                    .addPattern("NNN")
                    //
                    .addPattern("NNN")
                    //
                    .addPattern("NNN")
                    //
                    .addKey('N', nugget.tag)
                    //
                    .complete(References.ID, nugget.name() + "_nuggets_to_" + nugget.name() + "_ingot", output);
        }

        ShapedCraftingRecipeBuilder.start(DRILL_HEADS[SubtypeDrillHead.hslasteel.ordinal()], 1)
                //
                .addPattern(" I ")
                //
                .addPattern("IPI")
                //
                .addKey('I', ElectrodynamicsTags.Items.INGOT_HSLASTEEL)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_HSLASTEEL)
                //
                .complete(References.ID, "drill_head_hslasteel", output);

        ShapedCraftingRecipeBuilder.start(DRILL_HEADS[SubtypeDrillHead.stainlesssteel.ordinal()], 1)
                //
                .addPattern(" I ")
                //
                .addPattern("IPI")
                //
                .addKey('I', ElectrodynamicsTags.Items.INGOT_STAINLESSSTEEL)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .complete(References.ID, "drill_head_stainlesssteel", output);

        ShapedCraftingRecipeBuilder.start(DRILL_HEADS[SubtypeDrillHead.steel.ordinal()], 1)
                //
                .addPattern(" I ")
                //
                .addPattern("IPI")
                //
                .addKey('I', ElectrodynamicsTags.Items.INGOT_STEEL)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .complete(References.ID, "drill_head_steel", output);

        ShapedCraftingRecipeBuilder.start(DRILL_HEADS[SubtypeDrillHead.titanium.ordinal()], 1)
                //
                .addPattern(" I ")
                //
                .addPattern("IPI")
                //
                .addKey('I', ElectrodynamicsTags.Items.INGOT_TITANIUM)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_TITANIUM)
                //
                .complete(References.ID, "drill_head_titanium", output);

        ShapedCraftingRecipeBuilder.start(DRILL_HEADS[SubtypeDrillHead.titaniumcarbide.ordinal()], 1)
                //
                .addPattern(" I ")
                //
                .addPattern("IPI")
                //
                .addKey('I', ElectrodynamicsTags.Items.INGOT_TITANIUMCARBIDE)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_TITANIUMCARBIDE)
                //
                .complete(References.ID, "drill_head_titaniumcarbide", output);

        ShapedCraftingRecipeBuilder.start(DUSTS[SubtypeDust.superconductive.ordinal()], 9)
                //
                .addPattern("#S#")
                //
                .addPattern("G#G")
                //
                .addPattern("#S#")
                //
                .addKey('#', ElectrodynamicsTags.Items.DUST_ENDEREYE)
                //
                .addKey('S', ElectrodynamicsTags.Items.DUST_SILVER)
                //
                .addKey('G', ElectrodynamicsTags.Items.DUST_GOLD)
                //
                .complete(References.ID, "dust_superconductive", output);

        for (SubtypeResourceBlock block : SubtypeResourceBlock.values()) {
            ShapedCraftingRecipeBuilder.start(STORAGE_BLOCKS[block.ordinal()], 1)
                    //
                    .addPattern("III")
                    //
                    .addPattern("III")
                    //
                    .addPattern("III")
                    //
                    .addKey('I', block.sourceIngot)
                    //
                    .complete(References.ID, "resource_block_" + block.name(), output);
        }

        for (SubtypeRawOreBlock block : SubtypeRawOreBlock.values()) {
            ShapedCraftingRecipeBuilder.start(RAW_ORE_BLOCKS[block.ordinal()], 1)
                    //
                    .addPattern("RRR")
                    //
                    .addPattern("RRR")
                    //
                    .addPattern("RRR")
                    //
                    .addKey('R', block.sourceRawOre)
                    //
                    .complete(References.ID, "raw_ore_block_" + block.name(), output);
        }

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsBlocks.blockSteelScaffold.asItem(), 32)
                //
                .addPattern("SSS")
                //
                .addPattern("S S")
                //
                .addPattern("SSS")
                //
                .addKey('S', ElectrodynamicsTags.Items.ROD_STEEL)
                //
                .complete(References.ID, "steel_scaffold", output);

        ShapedCraftingRecipeBuilder.start(WIRES[SubtypeWire.copper.ordinal()], 1)
                //
                .addPattern("C")
                //
                .addPattern("C")
                //
                .addKey('C', Tags.Items.INGOTS_COPPER)
                //
                .complete(References.ID, "wire_copper", output);

        for (SubtypeGear gear : SubtypeGear.values()) {
            ShapedCraftingRecipeBuilder.start(GEARS[gear.ordinal()], 1)
                    //
                    .addPattern(" I ")
                    //
                    .addPattern("I I")
                    //
                    .addPattern(" I ")
                    //
                    .addKey('I', gear.sourceIngot)
                    //
                    .complete(References.ID, "gear_" + gear.name(), output);
        }

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.getItem(SubtypeChromotographyCard.oxygen), 1)
                //
                .addPattern(" G ")
                //
                .addPattern("WPW")
                //
                .addPattern(" C ")
                //
                .addKey('G', Tags.Items.GEMS_DIAMOND)
                //
                .addKey('W', ElectrodynamicsItems.getItem(SubtypeWire.copper))
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .complete(References.ID, "chromotographycard_" + SubtypeChromotographyCard.oxygen.name(), output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.getItem(SubtypeChromotographyCard.carbondioxide), 1)
                //
                .addPattern(" G ")
                //
                .addPattern("WPW")
                //
                .addPattern(" C ")
                //
                .addKey('G', ElectrodynamicsTags.Items.COAL_COKE)
                //
                .addKey('W', ElectrodynamicsItems.getItem(SubtypeWire.tin))
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .complete(References.ID, "chromotographycard_" + SubtypeChromotographyCard.carbondioxide.name(), output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.getItem(SubtypeChromotographyCard.nitrogen), 1)
                //
                .addPattern(" G ")
                //
                .addPattern("WPW")
                //
                .addPattern(" C ")
                //
                .addKey('G', Tags.Items.GEMS_EMERALD)
                //
                .addKey('W', ElectrodynamicsItems.getItem(SubtypeWire.iron))
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .complete(References.ID, "chromotographycard_" + SubtypeChromotographyCard.nitrogen.name(), output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.getItem(SubtypeChromotographyCard.argon), 1)
                //
                .addPattern(" G ")
                //
                .addPattern("WPW")
                //
                .addPattern(" C ")
                //
                .addKey('G', Tags.Items.GEMS_AMETHYST)
                //
                .addKey('W', ElectrodynamicsItems.getItem(SubtypeWire.gold))
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .complete(References.ID, "chromotographycard_" + SubtypeChromotographyCard.argon.name(), output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.getItem(SubtypeChromotographyCard.sulfurdioxide), 1)
                //
                .addPattern(" G ")
                //
                .addPattern("WPW")
                //
                .addPattern(" C ")
                //
                .addKey('G', ElectrodynamicsTags.Items.DUST_SULFUR)
                //
                .addKey('W', ElectrodynamicsItems.getItem(SubtypeWire.iron))
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .complete(References.ID, "chromotographycard_" + SubtypeChromotographyCard.sulfurdioxide.name(), output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_MOTOR.get(), 1)
                //
                .addPattern(" S ")
                //
                .addPattern("WCW")
                //
                .addPattern(" S ")
                //
                .addKey('S', ElectrodynamicsTags.Items.INGOT_STEEL)
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES)
                //
                .addKey('C', ElectrodynamicsItems.ITEM_COIL.get())
                //
                .complete(References.ID, "motor_steel", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_MOTOR.get(), 4)
                //
                .addPattern(" S ")
                //
                .addPattern("WCW")
                //
                .addPattern(" S ")
                //
                .addKey('S', ElectrodynamicsTags.Items.INGOT_STAINLESSSTEEL)
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES)
                //
                .addKey('C', ElectrodynamicsItems.ITEM_COIL.get())
                //
                .complete(References.ID, "motor_stainlesssteel", output);

        ShapedCraftingRecipeBuilder.start(PIPES[SubtypeFluidPipe.copper.ordinal()], 10)
                //
                .addPattern("III")
                //
                .addPattern("   ")
                //
                .addPattern("III")
                //
                .addKey('I', Tags.Items.INGOTS_COPPER)
                //
                .complete(References.ID, "fluidpipe_copper_horizontal", output);

        ShapedCraftingRecipeBuilder.start(PIPES[SubtypeFluidPipe.copper.ordinal()], 10)
                //
                .addPattern("I I")
                //
                .addPattern("I I")
                //
                .addPattern("I I")
                //
                .addKey('I', Tags.Items.INGOTS_COPPER)
                //
                .complete(References.ID, "fluidpipe_copper_vertical", output);

        ShapedCraftingRecipeBuilder.start(PIPES[SubtypeFluidPipe.steel.ordinal()], 4)
                //
                .addPattern("III")
                //
                .addPattern("   ")
                //
                .addPattern("III")
                //
                .addKey('I', ElectrodynamicsTags.Items.INGOT_STEEL)
                //
                .complete(References.ID, "fluidpipe_steel_horizontal", output);

        ShapedCraftingRecipeBuilder.start(PIPES[SubtypeFluidPipe.steel.ordinal()], 4)
                //
                .addPattern("I I")
                //
                .addPattern("I I")
                //
                .addPattern("I I")
                //
                .addKey('I', ElectrodynamicsTags.Items.INGOT_STEEL)
                //
                .complete(References.ID, "fluidpipe_steel_vertical", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.getItem(SubtypeGasPipe.UNINSULATEDCOPPER), 10)
                //
                .addPattern("PPP")
                //
                .addPattern("   ")
                //
                .addPattern("PPP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_COPPER)
                //
                .complete(References.ID, "gaspipe_copper_horizontal", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.getItem(SubtypeGasPipe.UNINSULATEDCOPPER), 10)
                //
                .addPattern("P P")
                //
                .addPattern("P P")
                //
                .addPattern("P P")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_COPPER)
                //
                .complete(References.ID, "gaspipe_copper_vertical", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.getItem(SubtypeGasPipe.UNINSULATEDSTEEL), 3)
                //
                .addPattern("PPP")
                //
                .addPattern("   ")
                //
                .addPattern("PPP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .complete(References.ID, "gaspipe_steel_horizontal", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.getItem(SubtypeGasPipe.UNINSULATEDSTEEL), 3)
                //
                .addPattern("P P")
                //
                .addPattern("P P")
                //
                .addPattern("P P")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .complete(References.ID, "gaspipe_steel_vertical", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.getItem(SubtypeGasPipe.UNINSULATEDPLASTIC), 6)
                //
                .addPattern("PPP")
                //
                .addPattern("   ")
                //
                .addPattern("PPP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLASTIC)
                //
                .complete(References.ID, "gaspipe_plastic_horizontal", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.getItem(SubtypeGasPipe.UNINSULATEDPLASTIC), 6)
                //
                .addPattern("P P")
                //
                .addPattern("P P")
                //
                .addPattern("P P")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLASTIC)
                //
                .complete(References.ID, "gaspipe_plastic_vertical", output);

        ShapedCraftingRecipeBuilder.start(PLATES[SubtypePlate.bronze.ordinal()], 1)
                //
                .addPattern("II")
                //
                .addPattern("II")
                //
                .addKey('I', ElectrodynamicsTags.Items.INGOT_BRONZE)
                //
                .complete(References.ID, "plate_bronze", output);

        ShapedCraftingRecipeBuilder.start(PLATES[SubtypePlate.copper.ordinal()], 1)
                //
                .addPattern("II")
                //
                .addPattern("II")
                //
                .addKey('I', Tags.Items.INGOTS_COPPER)
                //
                .complete(References.ID, "plate_copper", output);

        ShapedCraftingRecipeBuilder.start(PLATES[SubtypePlate.iron.ordinal()], 1)
                //
                .addPattern("II")
                //
                .addPattern("II")
                //
                .addKey('I', Tags.Items.INGOTS_IRON)
                //
                .complete(References.ID, "plate_iron", output);

        ShapedCraftingRecipeBuilder.start(PLATES[SubtypePlate.lead.ordinal()], 1)
                //
                .addPattern("II")
                //
                .addPattern("II")
                //
                .addKey('I', ElectrodynamicsTags.Items.INGOT_LEAD)
                //
                .complete(References.ID, "plate_lead", output);

        ShapedCraftingRecipeBuilder.start(PLATES[SubtypePlate.steel.ordinal()], 1)
                //
                .addPattern("II")
                //
                .addPattern("II")
                //
                .addKey('I', ElectrodynamicsTags.Items.INGOT_STEEL)
                //
                .complete(References.ID, "plate_steel", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_SEISMICMARKER.get(), 1)
                //
                .addPattern(" P ")
                //
                .addPattern("PAP")
                //
                .addPattern(" P ")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('A', Tags.Items.GEMS_AMETHYST)
                //
                .complete(References.ID, "seismic_marker", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_SOLARPANELPLATE.get(), 1)
                //
                .addPattern("BGB")
                //
                .addPattern("RWR")
                //
                .addPattern("ICI")
                //
                .addKey('B', Items.BLUE_STAINED_GLASS_PANE)
                //
                .addKey('G', Items.GRAY_STAINED_GLASS_PANE)
                //
                .addKey('R', Tags.Items.DUSTS_REDSTONE)
                //
                .addKey('W', WIRES[SubtypeWire.copper.ordinal()])
                //
                .addKey('I', Tags.Items.INGOTS_IRON)
                //
                .addKey('C', Tags.Items.INGOTS_COPPER)
                //
                .complete(References.ID, "solar_panel_plate", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.advancedcapacity.ordinal()], 1)
                //
                .addPattern("PBP")
                //
                .addPattern("BWB")
                //
                .addPattern("CBC")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('B', UPGRADES[SubtypeItemUpgrade.basiccapacity.ordinal()])
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .complete(References.ID, "upgrade_advanced_capacity", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.advancedspeed.ordinal()], 1)
                //
                .addPattern("PGP")
                //
                .addPattern("BWB")
                //
                .addPattern("CGC")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_BRONZE)
                //
                .addKey('B', UPGRADES[SubtypeItemUpgrade.basicspeed.ordinal()])
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .complete(References.ID, "upgrade_advanced_speed", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.basiccapacity.ordinal()], 1)
                //
                .addPattern("PBP")
                //
                .addPattern("BWB")
                //
                .addPattern("CBC")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('B', ElectrodynamicsItems.ITEM_BATTERY.get())
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .complete(References.ID, "upgrade_basic_capacity", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.basicspeed.ordinal()], 1)
                //
                .addPattern("PGP")
                //
                .addPattern("WWW")
                //
                .addPattern("CGC")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_BRONZE)
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_GOLD_WIRES)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .complete(References.ID, "upgrade_basic_speed", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.experience.ordinal()], 1)
                //
                .addPattern("PBP")
                //
                .addPattern("BWB")
                //
                .addPattern("PBP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_BRONZE)
                //
                .addKey('B', Items.GLASS_BOTTLE)
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES)
                //
                .complete(References.ID, "upgrade_experience", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.fortune.ordinal()], 1)
                //
                .addPattern("PCP")
                //
                .addPattern("CBC")
                //
                .addPattern("PCP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .addKey('B', new EnchantmentIngredient(Ingredient.of(Items.ENCHANTED_BOOK), Lists.newArrayList(Tags.Enchantments.INCREASE_BLOCK_DROPS), false))
                //
                .complete(References.ID, "upgrade_fortune", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.improvedsolarcell.ordinal()], 1)
                //
                .addPattern("PPP")
                //
                .addPattern("BCB")
                //
                .addPattern("BSB")
                //
                .addKey('P', ElectrodynamicsItems.ITEM_SOLARPANELPLATE.get())
                //
                .addKey('B', ElectrodynamicsTags.Items.PLATE_BRONZE)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .complete(References.ID, "upgrade_improved_solar_cell", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.iteminput.ordinal()], 1)
                //
                .addPattern("C")
                //
                .addPattern("P")
                //
                .addPattern("A")
                //
                .addKey('A', ElectrodynamicsTags.Items.PLATE_ALUMINUM)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('P', Items.STICKY_PISTON)
                //
                .complete(References.ID, "upgrade_item_input", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.itemoutput.ordinal()], 1)
                //
                .addPattern("C")
                //
                .addPattern("P")
                //
                .addPattern("A")
                //
                .addKey('A', ElectrodynamicsTags.Items.PLATE_ALUMINUM)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('P', Items.PISTON)
                //
                .complete(References.ID, "upgrade_item_output", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.itemvoid.ordinal()], 1)
                //
                .addPattern("C")
                //
                .addPattern("B")
                //
                .addPattern("P")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', Items.CACTUS)
                //
                .addKey('B', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .complete(References.ID, "upgrade_item_void", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.range.ordinal()], 1)
                //
                .addPattern("PWP")
                //
                .addPattern("WBW")
                //
                .addPattern("PWP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES)
                //
                .addKey('B', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .complete(References.ID, "upgrade_range", output);


        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.silktouch.ordinal()], 1)
                //
                .addPattern("PCP")
                //
                .addPattern("CBC")
                //
                .addPattern("PCP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ULTIMATE)
                //
                .addKey('B', new EnchantmentIngredient(Ingredient.of(Items.ENCHANTED_BOOK), Lists.newArrayList(ElectrodynamicsTags.Enchantments.SILK_TOUCH), false))
                //
                .complete(References.ID, "upgrade_silk_touch", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.stator.ordinal()], 1)
                //
                .addPattern("PCP")
                //
                .addPattern("CRC")
                //
                .addPattern("PCP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsItems.ITEM_COIL.get())
                //
                .addKey('R', Tags.Items.DUSTS_REDSTONE)
                //
                .complete(References.ID, "upgrade_stator", output);

        ShapedCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.unbreaking.ordinal()], 1)
                //
                .addPattern("PCP")
                //
                .addPattern("CBC")
                //
                .addPattern("PCP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .addKey('B', new EnchantmentIngredient(Ingredient.of(Items.ENCHANTED_BOOK), Lists.newArrayList(ElectrodynamicsTags.Enchantments.UNBREAKING), false))
                //
                .complete(References.ID, "upgrade_unbreaking", output);

        ShapelessCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.itemoutput.ordinal()], 1)
                //
                .addIngredient(UPGRADES[SubtypeItemUpgrade.itemoutput.ordinal()])
                //
                .complete(References.ID, "upgrade_item_output_reset", output);

        ShapelessCraftingRecipeBuilder.start(UPGRADES[SubtypeItemUpgrade.iteminput.ordinal()], 1)
                //
                .addIngredient(UPGRADES[SubtypeItemUpgrade.iteminput.ordinal()])
                //
                .complete(References.ID, "upgrade_item_input_reset", output);

        for (SubtypeNugget nugget : SubtypeNugget.values()) {
            ShapelessCraftingRecipeBuilder.start(NUGGETS[nugget.ordinal()], 9)
                    //
                    .addIngredient(nugget.sourceIngot)
                    //
                    .complete(References.ID, nugget.name() + "_ingot_to_" + nugget.name() + "_nuggets", output);

        }

        ShapelessCraftingRecipeBuilder.start(DUSTS[SubtypeDust.bronze.ordinal()], 3)
                //
                .addIngredient(ElectrodynamicsTags.Items.DUST_COPPER)
                //
                .addIngredient(ElectrodynamicsTags.Items.DUST_COPPER)
                //
                .addIngredient(ElectrodynamicsTags.Items.DUST_TIN)
                //
                .complete(References.ID, "dust_bronze", output);

        for (SubtypeResourceBlock block : SubtypeResourceBlock.values()) {
            ShapelessCraftingRecipeBuilder.start(block.productIngot.get(), 9)
                    //
                    .addIngredient(STORAGE_BLOCKS[block.ordinal()])
                    //
                    .complete(References.ID, block.name() + "_ingot_from_storage_block", output);
        }

        for (SubtypeRawOreBlock block : SubtypeRawOreBlock.values()) {
            ShapelessCraftingRecipeBuilder.start(block.productRawOre.get(), 9)
                    //
                    .addIngredient(RAW_ORE_BLOCKS[block.ordinal()])
                    //
                    .complete(References.ID, "raw_ore_" + block.name() + "_from_storage_block", output);
        }

        ShapelessCraftingRecipeBuilder.start(Items.GUNPOWDER, 6)
                //
                .addIngredient(ElectrodynamicsTags.Items.DUST_SULFUR)
                //
                .addIngredient(ElectrodynamicsTags.Items.DUST_SALTPETER)
                //
                .addIngredient(ItemTags.COALS)
                //
                .complete(References.ID, "gunpowder", output);

        ShapelessCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_INSULATION.get(), 20)
                //
                .addIngredient(ElectrodynamicsTags.Items.PLASTIC)
                //
                .complete(References.ID, "insulation_from_plastic", output);

        ShapelessCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_INSULATION.get(), 1)
                //
                .addIngredient(ItemTags.WOOL)
                //
                .complete(References.ID, "insulation_from_wool", output);

        ShapelessCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_INSULATION.get(), 6)
                //
                .addIngredient(Tags.Items.LEATHERS)
                //
                .complete(References.ID, "insulation_from_leather", output);

        ShapelessCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_INSULATION.get(), 3)
                //
                .addIngredient(Items.RABBIT_HIDE)
                //
                .complete(References.ID, "insulation_from_rabbit_hide", output);

        ShapelessCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_MOLYBDENUMFERTILIZER.get(), 8)
                //
                .addIngredient(Items.BONE_MEAL)
                //
                .addIngredient(ElectrodynamicsTags.Items.DUST_MOLYBDENUM)
                //
                .addIngredient(ElectrodynamicsTags.Items.DUST_MOLYBDENUM)
                //
                .complete(References.ID, "molybdenum_fertilizer", output);

        ShapelessCraftingRecipeBuilder.start(Items.OBSIDIAN, 2)
                //
                .addIngredient(Items.WATER_BUCKET)
                //
                .addIngredient(Items.LAVA_BUCKET)
                //
                .complete(References.ID, "obsidian", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_MECHANICALVALVE.get(), 1)
                //
                .addPattern("SLS")
                //
                .addPattern("BIB")
                //
                .addPattern("SSS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('L', Items.LEVER)
                //
                .addKey('B', ElectrodynamicsTags.Items.GEAR_BRONZE)
                //
                .addKey('I', ElectrodynamicsTags.Items.GEAR_IRON)
                //
                .complete(References.ID, "mechanical_valve", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_PRESSUREGAGE.get(), 1)
                //
                .addPattern("IPI")
                //
                .addPattern("CRD")
                //
                .addPattern("IGI")
                //
                .addKey('I', ElectrodynamicsTags.Items.PLATE_IRON)
                //
                .addKey('P', Tags.Items.GLASS_PANES)
                //
                .addKey('C', ElectrodynamicsItems.ITEM_COIL.get())
                //
                .addKey('R', ElectrodynamicsTags.Items.ROD_STEEL)
                //
                .addKey('D', Tags.Items.DUSTS_REDSTONE)
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_TIN)
                //
                .complete(References.ID, "pressure_gauge", output);
    }

    private void addMachine(RecipeOutput output) {

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.advanceddowngradetransformer.ordinal()], 1)
                //
                .addPattern("PPP")
                //
                .addPattern("OTO")
                //
                .addPattern("PCP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('O', ElectrodynamicsItems.ITEM_LAMINATEDCOIL.get())
                //
                .addKey('T', MACHINES[SubtypeMachine.downgradetransformer.ordinal()])
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .complete(References.ID, "machine_advanced_downgrade_transformer", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.advancedupgradetransformer.ordinal()], 1)
                //
                .addPattern("PPP")
                //
                .addPattern("OTO")
                //
                .addPattern("CPW")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('O', ElectrodynamicsItems.ITEM_LAMINATEDCOIL.get())
                //
                .addKey('T', MACHINES[SubtypeMachine.upgradetransformer.ordinal()])
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('W', WIRES[SubtypeWire.copper.ordinal()])
                //
                .complete(References.ID, "machine_advanced_upgrade_transformer", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.advancedsolarpanel.ordinal()], 1)
                //
                .addPattern(" S ")
                //
                .addPattern("SPS")
                //
                .addPattern("III")
                //
                .addKey('S', MACHINES[SubtypeMachine.solarpanel.ordinal()])
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('I', Tags.Items.INGOTS_IRON)
                //
                .complete(References.ID, "machine_advanced_solar_panel", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.batterybox.ordinal()], 1)
                //
                .addPattern("BBB")
                //
                .addPattern("SWS")
                //
                .addPattern("BBB")
                //
                .addKey('B', ElectrodynamicsItems.ITEM_BATTERY.get())
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES)
                //
                .complete(References.ID, "machine_battery_box_basic", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.carbynebatterybox.ordinal()], 1)
                //
                .addPattern("BBB")
                //
                .addPattern("PWP")
                //
                .addPattern("BBB")
                //
                .addKey('B', ElectrodynamicsItems.ITEM_CARBYNEBATTERY.get())
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_HSLASTEEL)
                //
                .addKey('W', ElectrodynamicsTags.Items.THICK_SUPERCONDUCTIVE_WIRES)
                //
                .complete(References.ID, "machine_battery_box_carbyne", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.chargerhv.ordinal()], 1)
                //
                .addPattern("W W")
                //
                .addPattern("NMN")
                //
                .addPattern("PCP")
                //
                .addKey('W', ElectrodynamicsTags.Items.THICK_SUPERCONDUCTIVE_WIRES)
                //
                .addKey('N', Items.NETHERITE_SCRAP)
                //
                .addKey('M', MACHINES[SubtypeMachine.chargermv.ordinal()])
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_HSLASTEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ULTIMATE)
                //
                .complete(References.ID, "machine_charger_hv", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.chargerlv.ordinal()], 1)
                //
                .addPattern("W W")
                //
                .addPattern("PBP")
                //
                .addPattern("PCP")
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('B', MACHINES[SubtypeMachine.batterybox.ordinal()])
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .complete(References.ID, "machine_charger_lv", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.chargermv.ordinal()], 1)
                //
                .addPattern("W W")
                //
                .addPattern("PLP")
                //
                .addPattern("PCP")
                //
                .addKey('W', ElectrodynamicsTags.Items.THICK_GOLD_WIRES)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('L', MACHINES[SubtypeMachine.chargerlv.ordinal()])
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .complete(References.ID, "machine_charger_mv", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.chemicalcrystallizer.ordinal()], 1)
                //
                .addPattern("SCS")
                //
                .addPattern("GMG")
                //
                .addPattern("SCS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_BRONZE)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .complete(References.ID, "machine_chemical_crystallizer", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.chemicalmixer.ordinal()], 1)
                //
                .addPattern("SCS")
                //
                .addPattern("MGM")
                //
                .addPattern("SCS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_BRONZE)
                //
                .complete(References.ID, "machine_chemical_mixer", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.circuitbreaker.ordinal()], 1)
                //
                .addPattern("SCS")
                //
                .addPattern("FBF")
                //
                .addPattern("SCS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('B', ElectrodynamicsItems.getItem(SubtypeMachine.relay))
                //
                .addKey('F', CERAMICS[SubtypeCeramic.fuse.ordinal()])
                //
                .complete(References.ID, "machine_circuit_breaker", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.circuitmonitor.ordinal()], 1)
                //
                .addPattern("DRD")
                //
                .addPattern("PMP")
                //
                .addPattern("DCD")
                //
                .addKey('D', Tags.Items.DUSTS_REDSTONE)
                //
                .addKey('R', Items.COMPARATOR)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('M', MACHINES[SubtypeMachine.multimeterblock.ordinal()])
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .complete(References.ID, "machine_circuit_monitor", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.relay.ordinal()], 1)
                //
                .addPattern("SLS")
                //
                .addPattern("WIW")
                //
                .addPattern("SCS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('W', ElectrodynamicsItems.getItem(SubtypeWire.copper))
                //
                .addKey('I', Tags.Items.INGOTS_COPPER)
                //
                .addKey('L', Items.LEVER)
                //
                .addKey('C', ElectrodynamicsItems.getItem(SubtypeCeramic.plate))
                //
                .complete(References.ID, "machine_relay", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.potentiometer.ordinal()], 1)
                //
                .addPattern("ACA")
                //
                .addPattern("CGC")
                //
                .addPattern("ACA")
                //
                .addKey('A', ElectrodynamicsTags.Items.PLATE_ALUMINUM)
                //
                .addKey('C', CERAMICS[SubtypeCeramic.plate.ordinal()])
                //
                .addKey('G', ItemTags.COALS)
                //
                .complete(References.ID, "machine_potentiometer", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.coalgenerator.ordinal()], 1)
                //
                .addPattern("SSS")
                //
                .addPattern("SFS")
                //
                .addPattern("SMS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('F', Items.FURNACE)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .complete(References.ID, "machine_coal_generator_steel", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.coalgenerator.ordinal()], 1)
                //
                .addPattern("BBB")
                //
                .addPattern("BFB")
                //
                .addPattern("BMB")
                //
                .addKey('B', ElectrodynamicsTags.Items.PLATE_BRONZE)
                //
                .addKey('F', Items.FURNACE)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .complete(References.ID, "machine_coal_generator_bronze", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.combustionchamber.ordinal()], 1)
                //
                .addPattern("PMP")
                //
                .addPattern("GWG")
                //
                .addPattern("PCP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_IRON)
                //
                .addKey('W', ElectrodynamicsItems.ITEM_COIL.get())
                //
                .addKey('C', Items.CAULDRON)
                //
                .complete(References.ID, "machine_combustion_chamber", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.coolantresavoir.ordinal()], 1)
                //
                .addPattern("SSS")
                //
                .addPattern("STS")
                //
                .addPattern("SCS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('T', MACHINES[SubtypeMachine.tanksteel.ordinal()])
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .complete(References.ID, "machine_coolant_resavoir", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.creativefluidsource.ordinal()], 1)
                //
                .addPattern("CCC")
                //
                .addPattern("CBC")
                //
                .addPattern("CCC")
                //
                .addKey('C', MACHINES[SubtypeMachine.creativepowersource.ordinal()])
                //
                .addKey('B', Items.BEDROCK)
                //
                .complete(References.ID, "machine_creative_fluid_source", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.creativepowersource.ordinal()], 1)
                //
                .addPattern("BBB")
                //
                .addPattern("BNB")
                //
                .addPattern("BBB")
                //
                .addKey('B', Items.BEDROCK)
                //
                .addKey('N', Items.NETHER_STAR)
                //
                .complete(References.ID, "machine_creative_power_source", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.currentregulator.ordinal()], 1)
                //
                .addPattern("SAS")
                //
                .addPattern("DCU")
                //
                .addPattern("SAS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('A', ElectrodynamicsTags.Items.PLATE_ALUMINUM)
                //
                .addKey('D', ElectrodynamicsItems.getItem(SubtypeMachine.downgradetransformer))
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('U', ElectrodynamicsItems.getItem(SubtypeMachine.upgradetransformer))
                //
                .complete(References.ID, "machine_current_regulator", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.downgradetransformer.ordinal()], 1)
                //
                .addPattern("ISI")
                //
                .addPattern("W C")
                //
                .addPattern("SIS")
                //
                .addKey('I', Tags.Items.INGOTS_IRON)
                //
                .addKey('S', ElectrodynamicsTags.Items.INGOT_STEEL)
                //
                .addKey('W', WIRES[SubtypeWire.copper.ordinal()])
                //
                .addKey('C', ElectrodynamicsItems.ITEM_COIL.get())
                //
                .complete(References.ID, "machine_downgrade_transformer", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.electricfurnace.ordinal()], 1)
                //
                .addPattern("SSS")
                //
                .addPattern("SCS")
                //
                .addPattern("SMS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .complete(References.ID, "machine_electric_furnace", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.electricfurnacedouble.ordinal()], 1)
                //
                .addPattern("CSC")
                //
                .addPattern("GFG")
                //
                .addPattern("CSC")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('G', Tags.Items.INGOTS_GOLD)
                //
                .addKey('F', MACHINES[SubtypeMachine.electricfurnace.ordinal()])
                //
                .complete(References.ID, "machine_electric_furnace_double", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.electricfurnacetriple.ordinal()], 1)
                //
                .addPattern("COC")
                //
                .addPattern("DFD")
                //
                .addPattern("COC")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .addKey('O', ElectrodynamicsTags.Items.DUST_OBSIDIAN)
                //
                .addKey('D', Tags.Items.GEMS_DIAMOND)
                //
                .addKey('F', MACHINES[SubtypeMachine.electricfurnacedouble.ordinal()])
                //
                .complete(References.ID, "machine_electric_furnace_triple", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.electricpump.ordinal()], 1)
                //
                .addPattern("SSS")
                //
                .addPattern("SMS")
                //
                .addPattern("SPS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('P', Items.PISTON)
                //
                .complete(References.ID, "machine_electric_pump", output);

        ShapelessCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.electricarcfurnace.ordinal()], 1)
                //
                .addIngredient(MACHINES[SubtypeMachine.electricfurnace.ordinal()])
                //
                .addIngredient(Items.BLAST_FURNACE)
                //
                .complete(References.ID, "machine_electric_arc_furnace", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.electricarcfurnacedouble.ordinal()], 1)
                //
                .addPattern("CSC")
                //
                .addPattern("GEG")
                //
                .addPattern("CSC")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('G', Tags.Items.INGOTS_GOLD)
                //
                .addKey('E', MACHINES[SubtypeMachine.electricarcfurnace.ordinal()])
                //
                .complete(References.ID, "machine_electric_arc_furnace_double", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.electricarcfurnacetriple.ordinal()], 1)
                //
                .addPattern("COC")
                //
                .addPattern("DED")
                //
                .addPattern("COC")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .addKey('O', ElectrodynamicsTags.Items.DUST_OBSIDIAN)
                //
                .addKey('D', Tags.Items.GEMS_DIAMOND)
                //
                .addKey('E', MACHINES[SubtypeMachine.electricarcfurnacedouble.ordinal()])
                //
                .complete(References.ID, "machine_electric_arc_furnace_triple", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.electrolyticseparator.ordinal()], 1)
                //
                .addPattern("PTP")
                //
                .addPattern("TXT")
                //
                .addPattern("PCP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('T', MACHINES[SubtypeMachine.gastanksteel.ordinal()])
                //
                .addKey('X', MACHINES[SubtypeMachine.upgradetransformer.ordinal()])
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .complete(References.ID, "machine_electrolytic_separator", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.energizedalloyer.ordinal()], 1)
                //
                .addPattern("ACA")
                //
                .addPattern("CTC")
                //
                .addPattern("AEA")
                //
                .addKey('A', ElectrodynamicsTags.Items.PLATE_ALUMINUM)
                //
                .addKey('C', CERAMICS[SubtypeCeramic.plate.ordinal()])
                //
                .addKey('T', ElectrodynamicsItems.ITEM_TITANIUM_COIL.get())
                //
                .addKey('E', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .complete(References.ID, "machine_energized_alloyer", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.fermentationplant.ordinal()], 1)
                //
                .addPattern("IMI")
                //
                .addPattern("BCB")
                //
                .addPattern("IFI")
                //
                .addKey('I', ElectrodynamicsTags.Items.PLATE_IRON)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('B', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('C', Items.CAULDRON)
                //
                .addKey('F', Items.FURNACE)
                //
                .complete(References.ID, "machine_fermentation_plant", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.fluidvoid.ordinal()], 1)
                //
                .addPattern("SBS")
                //
                .addPattern("BCB")
                //
                .addPattern("SBS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('B', Items.BUCKET)
                //
                .addKey('C', Items.CACTUS)
                //
                .complete(References.ID, "machine_fluid_void", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.getItem(SubtypeMachine.gascollector), 1)
                //
                .addPattern("SSS")
                //
                .addPattern("PMV")
                //
                .addPattern("SSS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('V', ElectrodynamicsItems.ITEM_MECHANICALVALVE.get())
                //
                .complete(References.ID, "machine_gascollector", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.gasvent.ordinal()], 1)
                //
                .addPattern("SBS")
                //
                .addPattern("BCB")
                //
                .addPattern("SBS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('B', Items.IRON_BARS)
                //
                .addKey('C', Items.CACTUS)
                //
                .complete(References.ID, "machine_gas_vent", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.lithiumbatterybox.ordinal()], 1)
                //
                .addPattern("BBB")
                //
                .addPattern("SWS")
                //
                .addPattern("BBB")
                //
                .addKey('B', ElectrodynamicsItems.ITEM_LITHIUMBATTERY.get())
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_GOLD_WIRES)
                //
                .complete(References.ID, "battery_box_lithium", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.lathe.ordinal()], 1)
                //
                .addPattern("AMA")
                //
                .addPattern("MCM")
                //
                .addPattern("AWA")
                //
                .addKey('A', ElectrodynamicsTags.Items.PLATE_ALUMINUM)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .addKey('W', MACHINES[SubtypeMachine.wiremill.ordinal()])
                //
                .complete(References.ID, "lathe", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_LOGISTICALMANAGER.get(), 1)
                //
                .addPattern("SCS")
                //
                .addPattern("CPC")
                //
                .addPattern("SCS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('P', Items.PISTON)
                //
                .complete(References.ID, "machine_logistical_manager", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.hydroelectricgenerator.ordinal()], 1)
                //
                .addPattern(" M ")
                //
                .addPattern("WSW")
                //
                .addPattern("PSP")
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('P', Items.PISTON)
                //
                .addKey('W', ItemTags.PLANKS)
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .complete(References.ID, "machine_hydroelectric_generator", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.mineralcrusher.ordinal()], 1)
                //
                .addPattern("SCS")
                //
                .addPattern("GPG")
                //
                .addPattern("SWS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_BRONZE)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_BRONZE)
                //
                .addKey('W', WIRES[SubtypeWire.copper.ordinal()])
                //
                .complete(References.ID, "machine_mineral_crusher", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.mineralcrusherdouble.ordinal()], 1)
                //
                .addPattern("EDE")
                //
                .addPattern("MCM")
                //
                .addPattern("EDE")
                //
                .addKey('E', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .addKey('D', Tags.Items.GEMS_DIAMOND)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('C', MACHINES[SubtypeMachine.mineralcrusher.ordinal()])
                //
                .complete(References.ID, "machine_mineral_crusher_double", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.mineralcrushertriple.ordinal()], 1)
                //
                .addPattern("UVU")
                //
                .addPattern("MCM")
                //
                .addPattern("UVU")
                //
                .addKey('U', ElectrodynamicsTags.Items.CIRCUITS_ULTIMATE)
                //
                .addKey('V', CRYSTALS[SubtypeCrystal.vanadium.ordinal()])
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('C', MACHINES[SubtypeMachine.mineralcrusherdouble.ordinal()])
                //
                .complete(References.ID, "machine_mineral_crusher_triple", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.mineralgrinder.ordinal()], 1)
                //
                .addPattern("PCP")
                //
                .addPattern("GMG")
                //
                .addPattern("PGP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_COPPER)
                //
                .complete(References.ID, "machine_mineral_grinder", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.mineralgrinderdouble.ordinal()], 1)
                //
                .addPattern("CGC")
                //
                .addPattern("BMB")
                //
                .addPattern("CGC")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .addKey('B', ElectrodynamicsTags.Items.GEAR_BRONZE)
                //
                .addKey('G', Tags.Items.INGOTS_GOLD)
                //
                .addKey('M', MACHINES[SubtypeMachine.mineralgrinder.ordinal()])
                //
                .complete(References.ID, "machine_mineral_grinder_double", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.mineralgrindertriple.ordinal()], 1)
                //
                .addPattern("CDC")
                //
                .addPattern("GMG")
                //
                .addPattern("CDC")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_STEEL)
                //
                .addKey('D', ElectrodynamicsTags.Items.DUST_SUPERCONDUCTIVE)
                //
                .addKey('M', MACHINES[SubtypeMachine.mineralgrinderdouble.ordinal()])
                //
                .complete(References.ID, "machine_mineral_grinder_triple", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.mineralwasher.ordinal()], 1)
                //
                .addPattern("SGS")
                //
                .addPattern("CEC")
                //
                .addPattern("PEP")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ULTIMATE)
                //
                .addKey('P', PIPES[SubtypeFluidPipe.steel.ordinal()])
                //
                .addKey('E', MACHINES[SubtypeMachine.electricpump.ordinal()])
                //
                .complete(References.ID, "machine_mineral_washer", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.motorcomplex.ordinal()], 1)
                //
                .addPattern("PGP")
                //
                .addPattern("GCM")
                //
                .addPattern("PGP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .complete(References.ID, "machine_motor_complex", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.multimeterblock.ordinal()], 1)
                //
                .addPattern(" S ")
                //
                .addPattern("SMS")
                //
                .addPattern(" S ")
                //
                .addKey('S', ElectrodynamicsTags.Items.INGOT_STEEL)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MULTIMETER.get())
                //
                .complete(References.ID, "machine_multimeter_block", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.oxidationfurnace.ordinal()], 1)
                //
                .addPattern("PGP")
                //
                .addPattern("CFC")
                //
                .addPattern("PMP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .addKey('F', MACHINES[SubtypeMachine.electricfurnace.ordinal()])
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .complete(References.ID, "machine_oxidation_furnace", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.quarry.ordinal()], 1)
                //
                .addPattern("PCP")
                //
                .addPattern("CDC")
                //
                .addPattern("PCP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .addKey('D', Items.DIAMOND_PICKAXE)
                //
                .complete(References.ID, "machine_quarry", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.reinforcedalloyer.ordinal()], 1)
                //
                .addPattern("CSC")
                //
                .addPattern("SAS")
                //
                .addPattern("CTC")
                //
                .addKey('C', ElectrodynamicsItems.ITEM_COMPOSITEPLATING.get())
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('A', MACHINES[SubtypeMachine.energizedalloyer.ordinal()])
                //
                .addKey('T', ElectrodynamicsItems.ITEM_TITANIUM_COIL.get())
                //
                .complete(References.ID, "machine_reinforced_alloyer", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.seismicrelay.ordinal()], 1)
                //
                .addPattern("PAP")
                //
                .addPattern("ACA")
                //
                .addPattern("MAM")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('A', Tags.Items.GEMS_AMETHYST)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ULTIMATE)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_SEISMICMARKER.get())
                //
                .complete(References.ID, "machine_seismic_relay", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.solarpanel.ordinal()], 1)
                //
                .addPattern("SPS")
                //
                .addPattern("CIC")
                //
                .addPattern("IWI")
                //
                .addKey('S', ElectrodynamicsItems.ITEM_SOLARPANELPLATE.get())
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('I', Tags.Items.INGOTS_IRON)
                //
                .addKey('W', WIRES[SubtypeWire.copper.ordinal()])
                //
                .complete(References.ID, "machine_solar_panel", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.tanksteel.ordinal()], 1)
                //
                .addPattern("SGS")
                //
                .addPattern("GCG")
                //
                .addPattern("SGS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('G', Tags.Items.GLASS_BLOCKS)
                //
                .addKey('C', Items.CAULDRON)
                //
                .complete(References.ID, "machine_tank_steel", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.tankreinforced.ordinal()], 1)
                //
                .addPattern("SGS")
                //
                .addPattern("GTG")
                //
                .addPattern("SGS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('G', CUSTOM_GLASS[SubtypeGlass.clear.ordinal()])
                //
                .addKey('T', MACHINES[SubtypeMachine.tanksteel.ordinal()])
                //
                .complete(References.ID, "machine_tank_reinforced", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.tankhsla.ordinal()], 1)
                //
                .addPattern("SGS")
                //
                .addPattern("GTG")
                //
                .addPattern("SGS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_HSLASTEEL)
                //
                .addKey('G', CUSTOM_GLASS[SubtypeGlass.aluminum.ordinal()])
                //
                .addKey('T', MACHINES[SubtypeMachine.tankreinforced.ordinal()])
                //
                .complete(References.ID, "machine_tank_hsla", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_COMPRESSOR_ADDONTANK.get(), 1)
                //
                .addPattern("SSS")
                //
                .addPattern("SCS")
                //
                .addPattern("SSS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', Items.CAULDRON)
                //
                .complete(References.ID, "pressurized_tank", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.gastanksteel.ordinal()], 1)
                //
                .addPattern(" V ")
                //
                .addPattern("SPS")
                //
                .addPattern(" S ")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('P', ElectrodynamicsItems.ITEM_COMPRESSOR_ADDONTANK.get())
                //
                .addKey('V', ElectrodynamicsItems.ITEM_MECHANICALVALVE.get())
                //
                .complete(References.ID, "machine_gastank_steel", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.gastankreinforced.ordinal()], 1)
                //
                .addPattern("SSS")
                //
                .addPattern("STS")
                //
                .addPattern("SSS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('T', MACHINES[SubtypeMachine.gastanksteel.ordinal()])
                //
                .complete(References.ID, "machine_gastank_reinforced", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.gastankhsla.ordinal()], 1)
                //
                .addPattern("SSS")
                //
                .addPattern("STS")
                //
                .addPattern("SSS")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_HSLASTEEL)
                //
                .addKey('T', MACHINES[SubtypeMachine.gastankreinforced.ordinal()])
                //
                .complete(References.ID, "machine_gastank_hsla", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.thermoelectricgenerator.ordinal()], 1)
                //
                .addPattern("ISI")
                //
                .addPattern("SPS")
                //
                .addPattern("CFC")
                //
                .addKey('I', Tags.Items.INGOTS_IRON)
                //
                .addKey('S', ElectrodynamicsTags.Items.INGOT_STEEL)
                //
                .addKey('C', Tags.Items.INGOTS_COPPER)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('F', Items.FURNACE)
                //
                .complete(References.ID, "machine_thermoelectric_generator", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.upgradetransformer.ordinal()], 1)
                //
                .addPattern("ISI")
                //
                .addPattern("C W")
                //
                .addPattern("SSS")
                //
                .addKey('I', Tags.Items.INGOTS_IRON)
                //
                .addKey('S', ElectrodynamicsTags.Items.INGOT_STEEL)
                //
                .addKey('W', WIRES[SubtypeWire.copper.ordinal()])
                //
                .addKey('C', ElectrodynamicsItems.ITEM_COIL.get())
                //
                .complete(References.ID, "machine_upgrade_transformer", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.windmill.ordinal()], 1)
                //
                .addPattern(" GM")
                //
                .addPattern(" S ")
                //
                .addPattern("ISI")
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_IRON)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('I', Tags.Items.INGOTS_IRON)
                //
                .complete(References.ID, "machine_windmill", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.wiremill.ordinal()], 1)
                //
                .addPattern("PBP")
                //
                .addPattern("MGM")
                //
                .addPattern("PGP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('B', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_BRONZE)
                //
                .complete(References.ID, "machine_wiremill", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.wiremilldouble.ordinal()], 1)
                //
                .addPattern("CGC")
                //
                .addPattern("WMW")
                //
                .addPattern("CGC")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_BRONZE)
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_SILVER_WIRES)
                //
                .addKey('M', MACHINES[SubtypeMachine.wiremill.ordinal()])
                //
                .complete(References.ID, "machine_wiremill_double", output);

        ShapedCraftingRecipeBuilder.start(MACHINES[SubtypeMachine.wiremilltriple.ordinal()], 1)
                //
                .addPattern("CGC")
                //
                .addPattern("WMW")
                //
                .addPattern("CGC")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .addKey('G', ElectrodynamicsTags.Items.GEAR_STEEL)
                //
                .addKey('W', ElectrodynamicsTags.Items.CERAMIC_SILVER_WIRES)
                //
                .addKey('M', MACHINES[SubtypeMachine.wiremilldouble.ordinal()])
                //
                .complete(References.ID, "machine_wiremill_triple", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsBlocks.blockCompressor.asItem(), 1)
                //
                .addPattern("PPP")
                //
                .addPattern("TVT")
                //
                .addPattern("CMG")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('T', ElectrodynamicsItems.ITEM_COMPRESSOR_ADDONTANK.get())
                //
                .addKey('V', ElectrodynamicsItems.ITEM_MECHANICALVALVE.get())
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('G', ElectrodynamicsItems.ITEM_PRESSUREGAGE.get())
                //
                .complete(References.ID, "machine_compressor", output);

        ShapelessCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_DECOMPRESSOR.get(), 1)
                //
                .addIngredient(ElectrodynamicsItems.ITEM_COMPRESSOR.get())
                //
                .addIngredient(ElectrodynamicsItems.ITEM_MECHANICALVALVE.get())
                //
                .complete(References.ID, "machine_decompressor", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsBlocks.blockThermoelectricManipulator.asItem(), 1)
                //
                .addPattern("PGP")
                //
                .addPattern("TVT")
                //
                .addPattern("MOC")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('T', ElectrodynamicsItems.ITEM_COMPRESSOR_ADDONTANK.get())
                //
                .addKey('V', ElectrodynamicsItems.ITEM_MECHANICALVALVE.get())
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('G', ElectrodynamicsItems.ITEM_PRESSUREGAGE.get())
                //
                .addKey('O', ElectrodynamicsItems.ITEM_COIL.get())
                //
                .complete(References.ID, "machine_thermoelectric_manipulator", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsBlocks.blockFluidValve.asItem(), 1)
                //
                .addPattern("VPR")
                //
                .addKey('V', ElectrodynamicsItems.ITEM_MECHANICALVALVE.get())
                //
                .addKey('P', ElectrodynamicsItems.getItem(SubtypeFluidPipe.steel))
                //
                .addKey('R', Tags.Items.DUSTS_REDSTONE)
                //
                .complete(References.ID, "pipe_fluidvalve", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsBlocks.blockGasValve.asItem(), 1)
                //
                .addPattern("VPR")
                //
                .addKey('V', ElectrodynamicsItems.ITEM_MECHANICALVALVE.get())
                //
                .addKey('P', ElectrodynamicsItems.getItem(SubtypeGasPipe.UNINSULATEDSTEEL))
                //
                .addKey('R', Tags.Items.DUSTS_REDSTONE)
                //
                .complete(References.ID, "pipe_gasvalve", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsBlocks.blockFluidPipePump.asItem(), 1)
                //
                .addPattern(" C ")
                //
                .addPattern("VPV")
                //
                .addPattern(" M ")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('V', ElectrodynamicsItems.ITEM_MECHANICALVALVE.get())
                //
                .addKey('P', ElectrodynamicsItems.getItem(SubtypeFluidPipe.steel))
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .complete(References.ID, "pipe_fluidpump", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsBlocks.blockGasPipePump.asItem(), 1)
                //
                .addPattern(" C ")
                //
                .addPattern("VPV")
                //
                .addPattern(" M ")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('V', ElectrodynamicsItems.ITEM_MECHANICALVALVE.get())
                //
                .addKey('P', ElectrodynamicsItems.getItem(SubtypeGasPipe.UNINSULATEDSTEEL))
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .complete(References.ID, "pipe_gaspump", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsBlocks.blockFluidPipeFilter.asItem(), 1)
                //
                .addPattern(" C ")
                //
                .addPattern("VPV")
                //
                .addPattern(" # ")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('V', ElectrodynamicsItems.ITEM_MECHANICALVALVE.get())
                //
                .addKey('P', ElectrodynamicsItems.getItem(SubtypeFluidPipe.steel))
                //
                .addKey('#', Items.PAPER)
                //
                .complete(References.ID, "pipe_fluidfilter", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsBlocks.blockGasPipeFilter.asItem(), 1)
                //
                .addPattern(" C ")
                //
                .addPattern("VPV")
                //
                .addPattern(" # ")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('V', ElectrodynamicsItems.ITEM_MECHANICALVALVE.get())
                //
                .addKey('P', ElectrodynamicsItems.getItem(SubtypeGasPipe.UNINSULATEDSTEEL))
                //
                .addKey('#', Items.PAPER)
                //
                .complete(References.ID, "pipe_gasfilter", output);

    }

    private void addGear(RecipeOutput output) {

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_CANISTERREINFORCED.get(), 1)
                //
                .addPattern("S")
                //
                .addPattern("A")
                //
                .addPattern("S")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('A', CUSTOM_GLASS[SubtypeGlass.aluminum.ordinal()])
                //
                .complete(References.ID, "reinforced_canister", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_PORTABLECYLINDER.get(), 1)
                //
                .addPattern("V")
                //
                .addPattern("S")
                //
                .addPattern("S")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('V', ElectrodynamicsItems.ITEM_MECHANICALVALVE.get())
                //
                .complete(References.ID, "portable_cylinder", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_COMBATBOOTS.get(), 1)
                //
                .addPattern("THT")
                //
                .addPattern("TST")
                //
                .addPattern("TBT")
                //
                .addKey('T', ElectrodynamicsTags.Items.PLATE_TITANIUMCARBIDE)
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_HSLASTEEL)
                //
                .addKey('H', ElectrodynamicsItems.ITEM_HYDRAULICBOOTS.get())
                //
                .addKey('B', ElectrodynamicsItems.ITEM_COMPOSITEBOOTS.get())
                //
                .complete(References.ID, "combat_boots", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get(), 1)
                //
                .addPattern("TJT")
                //
                .addPattern("TST")
                //
                .addPattern("TCT")
                //
                .addKey('T', ElectrodynamicsTags.Items.PLATE_TITANIUMCARBIDE)
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_HSLASTEEL)
                //
                .addKey('J', ElectrodynamicsItems.ITEM_JETPACK.get())
                //
                .addKey('C', ElectrodynamicsItems.ITEM_COMPOSITECHESTPLATE.get())
                //
                .complete(References.ID, "combat_chestplate", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_COMBATHELMET.get(), 1)
                //
                .addPattern("TNT")
                //
                .addPattern("TST")
                //
                .addPattern("THT")
                //
                .addKey('T', ElectrodynamicsTags.Items.PLATE_TITANIUMCARBIDE)
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_HSLASTEEL)
                //
                .addKey('N', ElectrodynamicsItems.ITEM_NIGHTVISIONGOGGLES.get())
                //
                .addKey('H', ElectrodynamicsItems.ITEM_COMPOSITEHELMET.get())
                //
                .complete(References.ID, "combat_helmet", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_COMBATLEGGINGS.get(), 1)
                //
                .addPattern("TMT")
                //
                .addPattern("TST")
                //
                .addPattern("TLT")
                //
                .addKey('T', ElectrodynamicsTags.Items.PLATE_TITANIUMCARBIDE)
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_HSLASTEEL)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_SERVOLEGGINGS.get())
                //
                .addKey('L', ElectrodynamicsItems.ITEM_COMPOSITELEGGINGS.get())
                //
                .complete(References.ID, "combat_leggings", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_COMPOSITEBOOTS.get(), 1)
                //
                .addPattern("P P")
                //
                .addPattern("PSP")
                //
                .addKey('P', ElectrodynamicsItems.ITEM_COMPOSITEPLATING.get())
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .complete(References.ID, "composite_boots", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_COMPOSITECHESTPLATE.get(), 1)
                //
                .addPattern("PSP")
                //
                .addPattern("PPP")
                //
                .addPattern("PPP")
                //
                .addKey('P', ElectrodynamicsItems.ITEM_COMPOSITEPLATING.get())
                //
                .addKey('S', ElectrodynamicsTags.Items.PLASTIC)
                //
                .complete(References.ID, "composite_chestplate", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_COMPOSITEHELMET.get(), 1)
                //
                .addPattern("PPP")
                //
                .addPattern("PGP")
                //
                .addKey('P', ElectrodynamicsItems.ITEM_COMPOSITEPLATING.get())
                //
                .addKey('G', CUSTOM_GLASS[SubtypeGlass.aluminum.ordinal()])
                //
                .complete(References.ID, "composite_helmet", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_COMPOSITELEGGINGS.get(), 1)
                //
                .addPattern("PPP")
                //
                .addPattern("P P")
                //
                .addPattern("P P")
                //
                .addKey('P', ElectrodynamicsItems.ITEM_COMPOSITEPLATING.get())
                //
                .complete(References.ID, "composite_leggings", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_ELECTRICBATON.get(), 1)
                //
                .addPattern("  T")
                //
                .addPattern("PT ")
                //
                .addPattern("BC ")
                //
                .addKey('T', ElectrodynamicsTags.Items.ROD_STEEL)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLASTIC)
                //
                .addKey('B', ElectrodynamicsItems.ITEM_LITHIUMBATTERY.get())
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .complete(References.ID, "electric_baton", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_ELECTRICCHAINSAW.get(), 1)
                //
                .addPattern("SP ")
                //
                .addPattern("IMB")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLASTIC)
                //
                .addKey('I', WIRES[SubtypeWire.iron.ordinal()])
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('B', ElectrodynamicsItems.ITEM_BATTERY.get())
                //
                .complete(References.ID, "electric_chainsaw", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_ELECTRICDRILL.get(), 1)
                //
                .addPattern("DMP")
                //
                .addPattern(" RB")
                //
                .addKey('D', DRILL_HEADS[SubtypeDrillHead.steel.ordinal()])
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('R', ElectrodynamicsTags.Items.ROD_STEEL)
                //
                .addKey('B', ElectrodynamicsItems.ITEM_BATTERY.get())
                //
                .complete(References.ID, "electric_drill", output);

        ShapelessCraftingRecipeBuilder.start(ElectrodynamicsItems.GUIDEBOOK.get(), 1)
                //
                .addIngredient(Items.BOOK)
                //
                .addIngredient(WIRES[SubtypeWire.copper.ordinal()])
                //
                .complete(References.ID, "guidebook", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_HYDRAULICBOOTS.get(), 1)
                //
                .addPattern("C C")
                //
                .addPattern("P P")
                //
                .addPattern("WSW")
                //
                .addKey('C', ElectrodynamicsItems.ITEM_CANISTERREINFORCED.get())
                //
                .addKey('P', Items.PISTON)
                //
                .addKey('W', ItemTags.WOOL)
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .complete(References.ID, "hydraulic_boots", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_CERAMICINSULATION.get(), 6)
                //
                .addPattern("P P")
                //
                .addPattern("PBP")
                //
                .addPattern("P P")
                //
                .addKey('P', CERAMICS[SubtypeCeramic.plate.ordinal()])
                //
                .addKey('B', Items.IRON_BARS)
                //
                .complete(References.ID, "ceramic_insulation", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_JETPACK.get(), 1)
                //
                .addPattern("CTC")
                //
                .addPattern("SPS")
                //
                .addPattern("P P")
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('T', MACHINES[SubtypeMachine.gastanksteel.ordinal()])
                //
                .addKey('P', ElectrodynamicsItems.getItem(SubtypeGasPipe.UNINSULATEDSTEEL))
                //
                .complete(References.ID, "jetpack", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_MECHANIZEDCROSSBOW.get(), 1)
                //
                .addPattern(" S ")
                //
                .addPattern("SbC")
                //
                .addPattern(" MB")
                //
                .addKey('S', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('b', Items.CROSSBOW)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('B', ElectrodynamicsItems.ITEM_BATTERY.get())
                //
                .complete(References.ID, "mechanized_crossbow", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_MULTIMETER.get(), 1)
                //
                .addPattern("SWS")
                //
                .addPattern("SCS")
                //
                .addPattern("SWS")
                //
                .addKey('S', ElectrodynamicsTags.Items.INGOT_STEEL)
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .complete(References.ID, "multimeter", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_NIGHTVISIONGOGGLES.get(), 1)
                //
                .addPattern("LLL")
                //
                .addPattern("PCP")
                //
                .addPattern("GBG")
                //
                .addKey('L', Tags.Items.LEATHERS)
                //
                .addKey('P', Items.LIME_STAINED_GLASS_PANE)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ADVANCED)
                //
                .addKey('G', Items.GLOW_INK_SAC)
                //
                .addKey('B', ElectrodynamicsItems.ITEM_BATTERY.get())
                //
                .complete(References.ID, "night_vision_goggles", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_RUBBERBOOTS.get(), 1)
                //
                .addPattern("I I")
                //
                .addPattern("I I")
                //
                .addKey('I', ElectrodynamicsItems.ITEM_INSULATION.get())
                //
                .complete(References.ID, "rubber_boots", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_KINETICRAILGUN.get(), 1)
                //
                .addPattern("CXS")
                //
                .addPattern("LPR")
                //
                .addPattern("BCS")
                //
                .addKey('L', ElectrodynamicsItems.ITEM_LITHIUMBATTERY.get())
                //
                .addKey('X', MACHINES[SubtypeMachine.upgradetransformer.ordinal()])
                //
                .addKey('S', WIRES[SubtypeWire.superconductive.ordinal()])
                //
                .addKey('B', ElectrodynamicsTags.Items.STORAGE_BLOCK_STAINLESSSTEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ELITE)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STAINLESSSTEEL)
                //
                .addKey('R', ElectrodynamicsTags.Items.ROD_STAINLESSSTEEL)
                //
                .complete(References.ID, "rail_gun_kinetic", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_PLASMARAILGUN.get(), 1)
                //
                .addPattern("CXS")
                //
                .addPattern("LPR")
                //
                .addPattern("BCS")
                //
                .addKey('L', ElectrodynamicsItems.ITEM_CARBYNEBATTERY.get())
                //
                .addKey('X', MACHINES[SubtypeMachine.upgradetransformer.ordinal()])
                //
                .addKey('S', WIRES[SubtypeWire.superconductive.ordinal()])
                //
                .addKey('B', ElectrodynamicsTags.Items.STORAGE_BLOCK_HSLASTEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_ULTIMATE)
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_HSLASTEEL)
                //
                .addKey('R', ElectrodynamicsTags.Items.ROD_TITANIUMCARBIDE)
                //
                .complete(References.ID, "rail_gun_plasma", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_SEISMICSCANNER.get(), 1)
                //
                .addPattern("PQP")
                //
                .addPattern("ABA")
                //
                .addPattern("PCP")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('Q', Tags.Items.GEMS_QUARTZ)
                //
                .addKey('A', Tags.Items.GEMS_AMETHYST)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('B', ElectrodynamicsItems.ITEM_BATTERY.get())
                //
                .complete(References.ID, "seismic_scanner", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_SERVOLEGGINGS.get(), 1)
                //
                .addPattern("PCP")
                //
                .addPattern("MWM")
                //
                .addPattern("B B")
                //
                .addKey('P', ElectrodynamicsTags.Items.PLATE_STEEL)
                //
                .addKey('C', ElectrodynamicsTags.Items.CIRCUITS_BASIC)
                //
                .addKey('M', ElectrodynamicsItems.ITEM_MOTOR.get())
                //
                .addKey('W', ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES)
                //
                .addKey('B', ElectrodynamicsItems.ITEM_BATTERY.get())
                //
                .complete(References.ID, "servo_leggings", output);

        ShapedCraftingRecipeBuilder.start(ElectrodynamicsItems.ITEM_WRENCH.get(), 1)
                //
                .addPattern(" S ")
                //
                .addPattern(" SS")
                //
                .addPattern("S  ")
                //
                .addKey('S', ElectrodynamicsTags.Items.INGOT_STEEL)
                //
                .complete(References.ID, "wrench", output);

    }

    private void addWires(RecipeOutput output) {

        // Insulated Wires
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLACK)) {

            SubtypeWire uninsulated = SubtypeWire.getWire(wire.conductor, InsulationMaterial.BARE, WireClass.BARE, WireColor.NONE);

            ShapelessCraftingRecipeBuilder.start(WIRES[wire.ordinal()], 1)
                    //
                    .addIngredient(WIRES[uninsulated.ordinal()])
                    //
                    .addIngredient(ElectrodynamicsItems.ITEM_INSULATION.get())
                    //
                    .complete(References.ID, "wire_" + wire.name(), output);

        }

        // Logistics Wires
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLACK)) {

            SubtypeWire insulated = SubtypeWire.getWire(wire.conductor, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLACK);

            ShapelessCraftingRecipeBuilder.start(WIRES[wire.ordinal()], 1)
                    //
                    .addIngredient(insulated.itemTag)
                    //
                    .addIngredient(Tags.Items.DUSTS_REDSTONE)
                    //
                    .complete(References.ID, "wire_" + wire.name(), output);

        }

        // Ceramic Insulated
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BROWN)) {

            SubtypeWire insulated = SubtypeWire.getWire(wire.conductor, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLACK);

            ShapelessCraftingRecipeBuilder.start(WIRES[wire.ordinal()], 1)
                    //
                    .addIngredient(insulated.itemTag)
                    //
                    .addIngredient(ElectrodynamicsItems.ITEM_CERAMICINSULATION.get())
                    //
                    .complete(References.ID, "wire_" + wire.name(), output);

        }

        // Highly Insulated
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLACK)) {

            SubtypeWire insulated = SubtypeWire.getWire(wire.conductor, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLACK);

            ShapelessCraftingRecipeBuilder.start(WIRES[wire.ordinal()], 2)
                    //
                    .addIngredient(insulated.itemTag)
                    //
                    .addIngredient(insulated.itemTag)
                    //
                    .addIngredient(insulated.itemTag)
                    //
                    .addIngredient(ElectrodynamicsItems.ITEM_INSULATION.get())
                    //
                    .addIngredient(ElectrodynamicsItems.ITEM_INSULATION.get())
                    //
                    .addIngredient(ElectrodynamicsItems.ITEM_INSULATION.get())
                    //
                    .complete(References.ID, "wire_" + wire.name(), output);

        }

        // Insulated Wires
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.values())) {
            if (wire.color.dyeTag == null) {
                continue;
            }
            ShapedCraftingRecipeBuilder.start(WIRES[wire.ordinal()], 8)
                    //
                    .addPattern("WWW")
                    //
                    .addPattern("WDW")
                    //
                    .addPattern("WWW")
                    //
                    .addKey('W', wire.itemTag)
                    //
                    .addKey('D', wire.color.dyeTag)
                    //
                    .complete(References.ID, "wire_" + wire.name() + "_multi", output);

            ShapelessCraftingRecipeBuilder.start(WIRES[wire.ordinal()], 1)
                    //
                    .addIngredient(wire.itemTag)
                    //
                    .addIngredient(wire.color.dyeTag)
                    //
                    .complete(References.ID, "wire_" + wire.name() + "_single", output);
        }

        // Highly Insulated Wires
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.values())) {
            if (wire.color.dyeTag == null) {
                continue;
            }
            ShapedCraftingRecipeBuilder.start(WIRES[wire.ordinal()], 8)
                    //
                    .addPattern("WWW")
                    //
                    .addPattern("WDW")
                    //
                    .addPattern("WWW")
                    //
                    .addKey('W', wire.itemTag)
                    //
                    .addKey('D', wire.color.dyeTag)
                    //
                    .complete(References.ID, "wire_" + wire.name() + "_multi", output);

            ShapelessCraftingRecipeBuilder.start(WIRES[wire.ordinal()], 1)
                    //
                    .addIngredient(wire.itemTag)
                    //
                    .addIngredient(wire.color.dyeTag)
                    //
                    .complete(References.ID, "wire_" + wire.name() + "_single", output);
        }

        // Ceramic Insulated Wires
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.values())) {
            if (wire.color.dyeTag == null) {
                continue;
            }
            ShapedCraftingRecipeBuilder.start(WIRES[wire.ordinal()], 8)
                    //
                    .addPattern("WWW")
                    //
                    .addPattern("WDW")
                    //
                    .addPattern("WWW")
                    //
                    .addKey('W', wire.itemTag)
                    //
                    .addKey('D', wire.color.dyeTag)
                    //
                    .complete(References.ID, "wire_" + wire.name() + "_multi", output);

            ShapelessCraftingRecipeBuilder.start(WIRES[wire.ordinal()], 1)
                    //
                    .addIngredient(wire.itemTag)
                    //
                    .addIngredient(wire.color.dyeTag)
                    //
                    .complete(References.ID, "wire_" + wire.name() + "_single", output);
        }

        // Ceramic Insulated Wires
        for (SubtypeWire wire : SubtypeWire.getWires(Conductor.values(), InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.values())) {
            if (wire.color.dyeTag == null) {
                continue;
            }
            ShapedCraftingRecipeBuilder.start(WIRES[wire.ordinal()], 8)
                    //
                    .addPattern("WWW")
                    //
                    .addPattern("WDW")
                    //
                    .addPattern("WWW")
                    //
                    .addKey('W', wire.itemTag)
                    //
                    .addKey('D', wire.color.dyeTag)
                    //
                    .complete(References.ID, "wire_" + wire.name() + "_multi", output);

            ShapelessCraftingRecipeBuilder.start(WIRES[wire.ordinal()], 1)
                    //
                    .addIngredient(wire.itemTag)
                    //
                    .addIngredient(wire.color.dyeTag)
                    //
                    .complete(References.ID, "wire_" + wire.name() + "_single", output);
        }

    }

}
