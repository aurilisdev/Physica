package electrodynamics.common.block.subtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import electrodynamics.api.ISubtype;
import electrodynamics.api.network.cable.type.IWire;
import electrodynamics.common.block.connect.BlockWire;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

//Storing the color on the enum was the cleanest solution...
public enum SubtypeWire implements ISubtype, IWire {

    /* UNINSULATED */

    tin(WireMaterial.TIN, InsulationMaterial.BARE, WireClass.BARE, WireColor.NONE, WireColor.NONE),
    iron(WireMaterial.IRON, InsulationMaterial.BARE, WireClass.BARE, WireColor.NONE, WireColor.NONE),
    copper(WireMaterial.COPPER, InsulationMaterial.BARE, WireClass.BARE, WireColor.NONE, WireColor.NONE),
    silver(WireMaterial.SILVER, InsulationMaterial.BARE, WireClass.BARE, WireColor.NONE, WireColor.NONE),
    gold(WireMaterial.GOLD, InsulationMaterial.BARE, WireClass.BARE, WireColor.NONE, WireColor.NONE),
    superconductive(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.BARE, WireClass.BARE, WireColor.NONE, WireColor.NONE),

    /* INSULATED */

    // black
    insulatedtinblack(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLACK, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_TIN_WIRES),
    insulatedironblack(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLACK, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_IRON_WIRES),
    insulatedcopperblack(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLACK, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES),
    insulatedsilverblack(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLACK, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SILVER_WIRES),
    insulatedgoldblack(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLACK, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_GOLD_WIRES),
    insulatedsuperconductiveblack(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLACK, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SUPERCONDUCTIVE_WIRES),
    // red
    insulatedtinred(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.RED, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_TIN_WIRES),
    insulatedironred(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.RED, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_IRON_WIRES),
    insulatedcopperred(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.RED, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES),
    insulatedsilverred(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.RED, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SILVER_WIRES),
    insulatedgoldred(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.RED, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_GOLD_WIRES),
    insulatedsuperconductivered(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.RED, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SUPERCONDUCTIVE_WIRES),
    // white
    insulatedtinwhite(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.WHITE, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_TIN_WIRES),
    insulatedironwhite(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.WHITE, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_IRON_WIRES),
    insulatedcopperwhite(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.WHITE, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES),
    insulatedsilverwhite(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.WHITE, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SILVER_WIRES),
    insulatedgoldwhite(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.WHITE, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_GOLD_WIRES),
    insulatedsuperconductivewhite(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.WHITE, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SUPERCONDUCTIVE_WIRES),
    // green
    insulatedtingreen(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.GREEN, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_TIN_WIRES),
    insulatedirongreen(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.GREEN, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_IRON_WIRES),
    insulatedcoppergreen(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.GREEN, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES),
    insulatedsilvergreen(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.GREEN, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SILVER_WIRES),
    insulatedgoldgreen(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.GREEN, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_GOLD_WIRES),
    insulatedsuperconductivegreen(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.GREEN, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SUPERCONDUCTIVE_WIRES),
    // blue
    insulatedtinblue(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLUE, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_TIN_WIRES),
    insulatedironblue(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLUE, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_IRON_WIRES),
    insulatedcopperblue(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLUE, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES),
    insulatedsilverblue(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLUE, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SILVER_WIRES),
    insulatedgoldblue(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLUE, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_GOLD_WIRES),
    insulatedsuperconductiveblue(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLUE, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SUPERCONDUCTIVE_WIRES),
    // yellow
    insulatedtinyellow(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.YELLOW, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_TIN_WIRES),
    insulatedironyellow(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.YELLOW, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_IRON_WIRES),
    insulatedcopperyellow(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.YELLOW, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES),
    insulatedsilveryellow(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.YELLOW, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SILVER_WIRES),
    insulatedgoldyellow(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.YELLOW, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_GOLD_WIRES),
    insulatedsuperconductiveyellow(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.YELLOW, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SUPERCONDUCTIVE_WIRES),
    // brown
    insulatedtinbrown(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BROWN, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_TIN_WIRES),
    insulatedironbrown(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BROWN, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_IRON_WIRES),
    insulatedcopperbrown(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BROWN, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_COPPER_WIRES),
    insulatedsilverbrown(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BROWN, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SILVER_WIRES),
    insulatedgoldbrown(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BROWN, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_GOLD_WIRES),
    insulatedsuperconductivebrown(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BROWN, WireColor.BLACK, ElectrodynamicsTags.Items.INSULATED_SUPERCONDUCTIVE_WIRES),

    /* HIGHLY INSULATED */

    // black
    highlyinsulatedtinblack(WireMaterial.TIN, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLACK, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_TIN_WIRES),
    highlyinsulatedironblack(WireMaterial.IRON, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLACK, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_IRON_WIRES),
    highlyinsulatedcopperblack(WireMaterial.COPPER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLACK, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_COPPER_WIRES),
    highlyinsulatedsilverblack(WireMaterial.SILVER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLACK, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SILVER_WIRES),
    highlyinsulatedgoldblack(WireMaterial.GOLD, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLACK, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_GOLD_WIRES),
    highlyinsulatedsuperconductiveblack(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLACK, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SUPERCONDUCTIVE_WIRES),
    // red
    highlyinsulatedtinred(WireMaterial.TIN, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.RED, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_TIN_WIRES),
    highlyinsulatedironred(WireMaterial.IRON, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.RED, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_IRON_WIRES),
    highlyinsulatedcopperred(WireMaterial.COPPER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.RED, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_COPPER_WIRES),
    highlyinsulatedsilverred(WireMaterial.SILVER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.RED, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SILVER_WIRES),
    highlyinsulatedgoldred(WireMaterial.GOLD, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.RED, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_GOLD_WIRES),
    highlyinsulatedsuperconductivered(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.RED, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SUPERCONDUCTIVE_WIRES),
    // white
    highlyinsulatedtinwhite(WireMaterial.TIN, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.WHITE, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_TIN_WIRES),
    highlyinsulatedironwhite(WireMaterial.IRON, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.WHITE, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_IRON_WIRES),
    highlyinsulatedcopperwhite(WireMaterial.COPPER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.WHITE, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_COPPER_WIRES),
    highlyinsulatedsilverwhite(WireMaterial.SILVER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.WHITE, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SILVER_WIRES),
    highlyinsulatedgoldwhite(WireMaterial.GOLD, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.WHITE, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_GOLD_WIRES),
    highlyinsulatedsuperconductivewhite(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.WHITE, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SUPERCONDUCTIVE_WIRES),
    // green
    highlyinsulatedtingreen(WireMaterial.TIN, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.GREEN, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_TIN_WIRES),
    highlyinsulatedirongreen(WireMaterial.IRON, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.GREEN, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_IRON_WIRES),
    highlyinsulatedcoppergreen(WireMaterial.COPPER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.GREEN, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_COPPER_WIRES),
    highlyinsulatedsilvergreen(WireMaterial.SILVER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.GREEN, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SILVER_WIRES),
    highlyinsulatedgoldgreen(WireMaterial.GOLD, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.GREEN, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_GOLD_WIRES),
    highlyinsulatedsuperconductivegreen(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.GREEN, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SUPERCONDUCTIVE_WIRES),
    // blue
    highlyinsulatedtinblue(WireMaterial.TIN, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLUE, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_TIN_WIRES),
    highlyinsulatedironblue(WireMaterial.IRON, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLUE, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_IRON_WIRES),
    highlyinsulatedcopperblue(WireMaterial.COPPER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLUE, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_COPPER_WIRES),
    highlyinsulatedsilverblue(WireMaterial.SILVER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLUE, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SILVER_WIRES),
    highlyinsulatedgoldblue(WireMaterial.GOLD, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLUE, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_GOLD_WIRES),
    highlyinsulatedsuperconductiveblue(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BLUE, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SUPERCONDUCTIVE_WIRES),
    // yellow
    highlyinsulatedtinyellow(WireMaterial.TIN, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.YELLOW, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_TIN_WIRES),
    highlyinsulatedironyellow(WireMaterial.IRON, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.YELLOW, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_IRON_WIRES),
    highlyinsulatedcopperyellow(WireMaterial.COPPER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.YELLOW, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_COPPER_WIRES),
    highlyinsulatedsilveryellow(WireMaterial.SILVER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.YELLOW, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SILVER_WIRES),
    highlyinsulatedgoldyellow(WireMaterial.GOLD, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.YELLOW, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_GOLD_WIRES),
    highlyinsulatedsuperconductiveyellow(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.YELLOW, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SUPERCONDUCTIVE_WIRES),
    // brown
    highlyinsulatedtinbrown(WireMaterial.TIN, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BROWN, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_TIN_WIRES),
    highlyinsulatedironbrown(WireMaterial.IRON, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BROWN, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_IRON_WIRES),
    highlyinsulatedcopperbrown(WireMaterial.COPPER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BROWN, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_COPPER_WIRES),
    highlyinsulatedsilverbrown(WireMaterial.SILVER, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BROWN, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SILVER_WIRES),
    highlyinsulatedgoldbrown(WireMaterial.GOLD, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BROWN, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_GOLD_WIRES),
    highlyinsulatedsuperconductivebrown(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.THICK_WOOL, WireClass.THICK, WireColor.BROWN, WireColor.BLACK, 3.0, ElectrodynamicsTags.Items.THICK_SUPERCONDUCTIVE_WIRES),

    /* CERAMIC INSULATED */

    // black
    ceramicinsulatedtinblack(WireMaterial.TIN, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLACK, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_TIN_WIRES),
    ceramicinsulatedironblack(WireMaterial.IRON, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLACK, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_IRON_WIRES),
    ceramicinsulatedcopperblack(WireMaterial.COPPER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLACK, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_COPPER_WIRES),
    ceramicinsulatedsilverblack(WireMaterial.SILVER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLACK, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SILVER_WIRES),
    ceramicinsulatedgoldblack(WireMaterial.GOLD, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLACK, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_GOLD_WIRES),
    ceramicinsulatedsuperconductiveblack(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLACK, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SUPERCONDUCTIVE_WIRES),
    // red
    ceramicinsulatedtinred(WireMaterial.TIN, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.RED, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_TIN_WIRES),
    ceramicinsulatedironred(WireMaterial.IRON, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.RED, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_IRON_WIRES),
    ceramicinsulatedcopperred(WireMaterial.COPPER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.RED, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_COPPER_WIRES),
    ceramicinsulatedsilverred(WireMaterial.SILVER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.RED, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SILVER_WIRES),
    ceramicinsulatedgoldred(WireMaterial.GOLD, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.RED, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_GOLD_WIRES),
    ceramicinsulatedsuperconductivered(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.RED, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SUPERCONDUCTIVE_WIRES),
    // white
    ceramicinsulatedtinwhite(WireMaterial.TIN, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.WHITE, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_TIN_WIRES),
    ceramicinsulatedironwhite(WireMaterial.IRON, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.WHITE, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_IRON_WIRES),
    ceramicinsulatedcopperwhite(WireMaterial.COPPER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.WHITE, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_COPPER_WIRES),
    ceramicinsulatedsilverwhite(WireMaterial.SILVER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.WHITE, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SILVER_WIRES),
    ceramicinsulatedgoldwhite(WireMaterial.GOLD, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.WHITE, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_GOLD_WIRES),
    ceramicinsulatedsuperconductivewhite(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.WHITE, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SUPERCONDUCTIVE_WIRES),
    // green
    ceramicinsulatedtingreen(WireMaterial.TIN, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.GREEN, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_TIN_WIRES),
    ceramicinsulatedirongreen(WireMaterial.IRON, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.GREEN, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_IRON_WIRES),
    ceramicinsulatedcoppergreen(WireMaterial.COPPER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.GREEN, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_COPPER_WIRES),
    ceramicinsulatedsilvergreen(WireMaterial.SILVER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.GREEN, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SILVER_WIRES),
    ceramicinsulatedgoldgreen(WireMaterial.GOLD, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.GREEN, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_GOLD_WIRES),
    ceramicinsulatedsuperconductivegreen(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.GREEN, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SUPERCONDUCTIVE_WIRES),
    // blue
    ceramicinsulatedtinblue(WireMaterial.TIN, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLUE, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_TIN_WIRES),
    ceramicinsulatedironblue(WireMaterial.IRON, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLUE, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_IRON_WIRES),
    ceramicinsulatedcopperblue(WireMaterial.COPPER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLUE, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_COPPER_WIRES),
    ceramicinsulatedsilverblue(WireMaterial.SILVER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLUE, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SILVER_WIRES),
    ceramicinsulatedgoldblue(WireMaterial.GOLD, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLUE, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_GOLD_WIRES),
    ceramicinsulatedsuperconductiveblue(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLUE, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SUPERCONDUCTIVE_WIRES),
    // yellow
    ceramicinsulatedtinyellow(WireMaterial.TIN, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.YELLOW, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_TIN_WIRES),
    ceramicinsulatedironyellow(WireMaterial.IRON, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.YELLOW, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_IRON_WIRES),
    ceramicinsulatedcopperyellow(WireMaterial.COPPER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.YELLOW, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_COPPER_WIRES),
    ceramicinsulatedsilveryellow(WireMaterial.SILVER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.YELLOW, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SILVER_WIRES),
    ceramicinsulatedgoldyellow(WireMaterial.GOLD, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.YELLOW, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_GOLD_WIRES),
    ceramicinsulatedsuperconductiveyellow(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.YELLOW, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SUPERCONDUCTIVE_WIRES),
    // brown
    ceramicinsulatedtinbrown(WireMaterial.TIN, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BROWN, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_TIN_WIRES),
    ceramicinsulatedironbrown(WireMaterial.IRON, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BROWN, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_IRON_WIRES),
    ceramicinsulatedcopperbrown(WireMaterial.COPPER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BROWN, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_COPPER_WIRES),
    ceramicinsulatedsilverbrown(WireMaterial.SILVER, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BROWN, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SILVER_WIRES),
    ceramicinsulatedgoldbrown(WireMaterial.GOLD, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BROWN, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_GOLD_WIRES),
    ceramicinsulatedsuperconductivebrown(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BROWN, WireColor.BROWN, ElectrodynamicsTags.Items.CERAMIC_SUPERCONDUCTIVE_WIRES),

    /* LOGISTICAL */

    // black
    logisticstinblack(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLACK, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_TIN_WIRES),
    logisticsironblack(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLACK, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_IRON_WIRES),
    logisticscopperblack(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLACK, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_COPPER_WIRES),
    logisticssilverblack(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLACK, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SILVER_WIRES),
    logisticsgoldblack(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLACK, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_GOLD_WIRES),
    logisticssuperconductiveblack(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLACK, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SUPERCONDUCTIVE_WIRES),
    // red
    logisticstinred(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.RED, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_TIN_WIRES),
    logisticsironred(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.RED, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_IRON_WIRES),
    logisticscopperred(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.RED, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_COPPER_WIRES),
    logisticssilverred(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.RED, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SILVER_WIRES),
    logisticsgoldred(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.RED, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_GOLD_WIRES),
    logisticssuperconductivered(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.RED, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SUPERCONDUCTIVE_WIRES),
    // white
    logisticstinwhite(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.WHITE, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_TIN_WIRES),
    logisticsironwhite(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.WHITE, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_IRON_WIRES),
    logisticscopperwhite(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.WHITE, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_COPPER_WIRES),
    logisticssilverwhite(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.WHITE, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SILVER_WIRES),
    logisticsgoldwhite(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.WHITE, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_GOLD_WIRES),
    logisticssuperconductivewhite(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.WHITE, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SUPERCONDUCTIVE_WIRES),
    // green
    logisticstingreen(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.GREEN, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_TIN_WIRES),
    logisticsirongreen(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.GREEN, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_IRON_WIRES),
    logisticscoppergreen(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.GREEN, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_COPPER_WIRES),
    logisticssilvergreen(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.GREEN, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SILVER_WIRES),
    logisticsgoldgreen(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.GREEN, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_GOLD_WIRES),
    logisticssuperconductivegreen(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.GREEN, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SUPERCONDUCTIVE_WIRES),
    // blue
    logisticstinblue(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLUE, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_TIN_WIRES),
    logisticsironblue(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLUE, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_IRON_WIRES),
    logisticscopperblue(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLUE, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_COPPER_WIRES),
    logisticssilverblue(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLUE, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SILVER_WIRES),
    logisticsgoldblue(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLUE, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_GOLD_WIRES),
    logisticssuperconductiveblue(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLUE, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SUPERCONDUCTIVE_WIRES),
    // yellow
    logisticstinyellow(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.YELLOW, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_TIN_WIRES),
    logisticsironyellow(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.YELLOW, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_IRON_WIRES),
    logisticscopperyellow(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.YELLOW, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_COPPER_WIRES),
    logisticssilveryellow(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.YELLOW, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SILVER_WIRES),
    logisticsgoldyellow(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.YELLOW, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_GOLD_WIRES),
    logisticssuperconductiveyellow(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.YELLOW, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SUPERCONDUCTIVE_WIRES),
    // brown
    logisticstinbrown(WireMaterial.TIN, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BROWN, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_TIN_WIRES),
    logisticsironbrown(WireMaterial.IRON, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BROWN, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_IRON_WIRES),
    logisticscopperbrown(WireMaterial.COPPER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BROWN, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_COPPER_WIRES),
    logisticssilverbrown(WireMaterial.SILVER, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BROWN, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SILVER_WIRES),
    logisticsgoldbrown(WireMaterial.GOLD, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BROWN, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_GOLD_WIRES),
    logisticssuperconductivebrown(WireMaterial.SUPERCONDUCTIVE, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BROWN, WireColor.BLACK, ElectrodynamicsTags.Items.LOGISTICAL_SUPERCONDUCTIVE_WIRES);

    public static final HashMap<IWire.IWireMaterial, HashMap<IWire.IInsulationMaterial, HashMap<IWire.IWireClass, HashMap<IWire.IWireColor, BlockWire>>>> WIRES = new HashMap<>();
    public static final HashSet<IWireMaterial> WIRE_MATERIALS = new HashSet<>();

    private final double resistance;
    private final long ampacity;
    private final WireClass wireClass;
    private final InsulationMaterial insulation;
    private final WireMaterial conductor;
    private final WireColor color;
    private final WireColor defaultColor;
    private final boolean isDefaultColor;
    @Nullable
    private final TagKey<Item> itemTag;

    private SubtypeWire(WireMaterial conductor, InsulationMaterial insulation, WireClass wireClass, WireColor color, WireColor defaultColor, double dividend, TagKey<Item> tag) {
        resistance = conductor.resistance / dividend;
        this.ampacity = (long) (conductor.ampacity * dividend);
        this.conductor = conductor;
        this.insulation = insulation;
        this.wireClass = wireClass;
        this.color = color;
        this.defaultColor = defaultColor;
        isDefaultColor = color == defaultColor;
        itemTag = tag;
    }

    private SubtypeWire(WireMaterial conductor, InsulationMaterial insulation, WireClass wireClass, WireColor color, WireColor defaultColor, TagKey<Item> tag) {
        this(conductor, insulation, wireClass, color, defaultColor, 1, tag);
    }

    private SubtypeWire(WireMaterial conductor, InsulationMaterial insulation, WireClass wireClass, WireColor color, WireColor defaultColor, double dividend) {
        this(conductor, insulation, wireClass, color, defaultColor, dividend, null);
    }

    private SubtypeWire(WireMaterial conductor, InsulationMaterial insulation, WireClass wireClass, WireColor color, WireColor defaultColor) {
        this(conductor, insulation, wireClass, color, defaultColor, 1, null);
    }

    @Override
    public double getResistance() {
        return resistance;
    }

    @Override
    public long getAmpacity() {
        return ampacity;
    }

    @Override
    public IWireClass getWireClass() {
        return wireClass;
    }

    @Override
    public IInsulationMaterial getInsulation() {
        return insulation;
    }

    @Override
    public IWireMaterial getWireMaterial() {
        return conductor;
    }

    @Override
    public IWireColor getWireColor() {
        return color;
    }

    @Override
    public IWireColor getDefaultColor() {
        return defaultColor;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public TagKey<Item> getItemTag() {
        return itemTag;
    }

    @Override
    public String tag() {
        return "wire" + name();
    }

    @Override
    public String forgeTag() {
        return tag();
    }

    @Override
    public boolean isItem() {
        return false;
    }

    @Override
    public boolean isDefaultColor() {
        return isDefaultColor;
    }

    @Nullable
    public static BlockWire getWire(IWireMaterial wireMaterial, IInsulationMaterial insulation, IWireClass wireClass, IWireColor color) {

        return WIRES.getOrDefault(wireMaterial, new HashMap<>()).getOrDefault(insulation, new HashMap<>()).getOrDefault(wireClass, new HashMap<>()).getOrDefault(color, null);
    }

    public static BlockWire[] getWires(WireMaterial[] conductors, InsulationMaterial insulation, WireClass wireClass, WireColor... colors) {

        List<BlockWire> list = new ArrayList<>();

        BlockWire wire;

        for (WireMaterial conductor : conductors) {
            for (WireColor color : colors) {
                wire = SubtypeWire.getWire(conductor, insulation, wireClass, color);
                if (wire != null) {
                    list.add(wire);
                }
            }
        }

        return list.toArray(new BlockWire[0]);
    }

    /**
     * A distinction is made between this and WireClass, as there can be multiple different wires with the same insulation
     * but different properties like the Logistical Wire. It shares the same insulation value as the standard Insulated wire
     *
     * @author skip999
     */
    public static enum InsulationMaterial implements IInsulationMaterial {

        BARE(false, true, 0, 1, Blocks.IRON_BLOCK.properties(), SoundType.METAL),
        WOOL(true, false, 240, 2, Blocks.WHITE_WOOL.properties(), SoundType.WOOL),
        THICK_WOOL(true, false, 960, 3, Blocks.WHITE_WOOL.properties(), SoundType.WOOL),
        CERAMIC(true, true, 480, 3, Blocks.STONE.properties(), SoundType.TUFF);

        private final boolean insulated;
        private final boolean fireProof;
        private final int shockVoltage;
        private final double radius;
        private final Properties material;
        private final SoundType soundType;

        InsulationMaterial(boolean insulated, boolean fireProof, int shockVoltage, double radius, Properties material, SoundType sounndType) {
            this.insulated = insulated;
            this.fireProof = fireProof;
            this.shockVoltage = shockVoltage;
            this.radius = radius;
            this.material = material;
            this.soundType = sounndType;
        }

        @Override
        public boolean insulated() {
            return insulated;
        }

        @Override
        public boolean fireproof() {
            return fireProof;
        }

        @Override
        public int shockVoltage() {
            return shockVoltage;
        }

        @Override
        public double wireRadius() {
            return radius;
        }

        @Override
        public Properties getProperties() {
            return material;
        }

        @Override
        public SoundType getSoundType() {
            return soundType;
        }
    }

    /**
     * This is a category enum to make distinctions between the different types of wires
     *
     * @author skip999
     */
    public static enum WireClass implements IWireClass {

        BARE(false),
        INSULATED(false),
        THICK(false),
        CERAMIC(false),
        LOGISTICAL(true);

        private final boolean conductsRedstone;

        WireClass(boolean conductsRedstone) {
            this.conductsRedstone = conductsRedstone;
        }

        @Override
        public boolean conductsRedstone() {
            return conductsRedstone;
        }
    }

    // Calculated using https://www.omnicalculator.com/physics/wire-resistance
    // Area is actually 0.125 = 15625mm^2
    // Length is 1 meter + 2 meters of wire - 2 * center length -> 3 - 2 * 0.125 = 2.75 meters per wire block. this is static and isnt calculated dynamically even though
    // wires are rarely connected in all directions.
    // Also manipulated uniformally
    public static enum WireMaterial implements IWireMaterial {

        COPPER(0.0030096, 360, 1.68E-8D), // annealed copper
        GOLD(0.004294, 1000, 2.44E-8D),
        IRON(0.01709, 100, 1.0E-7D),
        SILVER(0.0027984, 600, 1.59E-8D),
        SUPERCONDUCTIVE(0, Long.MAX_VALUE, 0D),
        TIN(0.020064, 60, 1.09E-7D); // Tin has 15% the conductivity of copper. Tin resistance = copper / 0.15

        public static final double BASELINE_TEMP_K = 293.15D;

        private final double resistance;
        private final long ampacity;
        private final double resistivity;

        WireMaterial(double resistance, long ampacity, double resistivity) {
            this.resistance = resistance;
            this.ampacity = ampacity;
            this.resistivity = resistivity;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.ROOT);
        }

        @Override
        public double resistance() {
            return resistance;
        }

        @Override
        public long ampacity() {
            return ampacity;
        }

        @Override
        public double materialResistivity() {
            return resistivity;
        }


        /**
         * All units assumed to be in units of meters and its equivalent powers
         *
         * @param resistivity unitless
         * @param area        meter^2
         * @param length      meter
         * @return the resistance per meter
         */
        public static double calculateResistance(double resistivity, double area, double length) {
            return (resistivity * length) / area;
        }

        /**
         * Returns the maximum permissible ampacity of a conductor based on its resistance
         *
         * @param maxOperatingTemp   units of Kelvin
         * @param resistance         units of ohm / meter
         * @param dialectricLoss     units of watts / meter
         * @param thermalResistance1 units of kelvin * meter / watts
         * @param thermalResistance2 units of kelvin * meter / watts
         * @param thermalResistance3 units of kelvin * meter / watts
         * @param thermalResistance4 units of kelvin * meter / watts
         * @param numConductors      unitless
         * @param ratioOfLosses1     unitless
         * @param ratioOfLosses2     unitless
         * @return the ampacity of this conductor as a long
         */
        public static long calculateAmpacity(double maxOperatingTemp, double resistance, double dialectricLoss, double thermalResistance1, double thermalResistance2, double thermalResistance3, double thermalResistance4, double numConductors, double ratioOfLosses1, double ratioOfLosses2) {

            double deltaT = maxOperatingTemp - BASELINE_TEMP_K;

            double top = deltaT - dialectricLoss * (0.5 * thermalResistance1 + numConductors * (thermalResistance2 + thermalResistance3 + thermalResistance4));

            double bottom = resistance * thermalResistance1 + numConductors * resistance * (1 + ratioOfLosses1) * thermalResistance2 + numConductors * resistance * (1 + ratioOfLosses1 + ratioOfLosses2) * (thermalResistance3 + thermalResistance4);

            double ampacity = Math.sqrt(top / bottom);

            return (long) ampacity;

        }
    }

    // based on NEC wire colors
    public static enum WireColor implements IWireColor {

        NONE(255, 255, 255, 255, null),
        BLACK(40, 40, 40, 255, Tags.Items.DYES_BLACK),
        RED(200, 0, 0, 255, Tags.Items.DYES_RED),
        WHITE(255, 255, 255, 255, Tags.Items.DYES_WHITE),
        GREEN(24, 147, 50, 255, Tags.Items.DYES_GREEN),
        BLUE(68, 140, 203, 255, Tags.Items.DYES_BLUE),
        YELLOW(250, 240, 104, 255, Tags.Items.DYES_YELLOW),
        BROWN(102, 78, 55, 255, Tags.Items.DYES_BROWN);

        public final Color color;
        @Nullable
        public final TagKey<Item> dyeTag;

        public static final HashSet<IWireColor> WIRE_COLORS = new HashSet<>();

        private WireColor(int r, int g, int b, int a, TagKey<Item> dyeTag) {
            color = new Color(r, g, b, a);
            this.dyeTag = dyeTag;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.ROOT);
        }

        @Nullable
        public static IWireColor getColorFromDye(ItemStack item) {
            for (IWireColor color : WIRE_COLORS) {
                if (color.getDyeTag() != null && item.is(color.getDyeTag())) {
                    return color;
                }
            }
            return null;
        }

        @NotNull
        @Override
        public Color getColor() {
            return color;
        }

        @NotNull
        @Override
        public TagKey<Item> getDyeTag() {
            return dyeTag;
        }
    }

}
