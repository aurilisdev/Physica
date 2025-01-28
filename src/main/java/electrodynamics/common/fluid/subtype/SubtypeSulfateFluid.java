package electrodynamics.common.fluid.subtype;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import electrodynamics.api.ISubtype;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsFluids;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;

public enum SubtypeSulfateFluid implements ISubtype {
    copper(ElectrodynamicsTags.Fluids.COPPER_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.copper), ItemTags.COPPER_ORES, Color.WHITE),
    tin(ElectrodynamicsTags.Fluids.TIN_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.tin), ElectrodynamicsTags.Items.ORE_TIN, Color.WHITE),
    silver(ElectrodynamicsTags.Fluids.SILVER_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.silver), ElectrodynamicsTags.Items.ORE_SILVER, Color.WHITE),
    lead(ElectrodynamicsTags.Fluids.LEAD_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.lead), ElectrodynamicsTags.Items.ORE_LEAD, Color.WHITE),
    vanadium(ElectrodynamicsTags.Fluids.VANADIUM_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.vanadium), ElectrodynamicsTags.Items.ORE_VANADIUM, Color.WHITE),
    iron(ElectrodynamicsTags.Fluids.IRON_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.iron), ItemTags.IRON_ORES, Color.WHITE),
    gold(ElectrodynamicsTags.Fluids.GOLD_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.gold), ItemTags.GOLD_ORES, Color.WHITE),
    lithium(ElectrodynamicsTags.Fluids.LITHIUM_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.lithium), ElectrodynamicsTags.Items.ORE_LITHIUM, Color.WHITE),
    molybdenum(ElectrodynamicsTags.Fluids.MOLYBDENUM_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.molybdenum), ElectrodynamicsTags.Items.ORE_MOLYBDENUM, Color.WHITE),
    netherite(ElectrodynamicsTags.Fluids.NETHERITE_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.netherite), Tags.Items.ORES_NETHERITE_SCRAP, Color.WHITE);

    public final TagKey<Fluid> tag;
    @Nullable
    public final TagKey<Item> source;
    @Nullable
    public final Supplier<Fluid> result;
    public final Color color;

    SubtypeSulfateFluid(TagKey<Fluid> tag, TagKey<Item> source, Color color) {
        this(tag, null, source, color);
    }

    SubtypeSulfateFluid(TagKey<Fluid> tag, Supplier<Fluid> result, TagKey<Item> source, Color color) {
        this.tag = tag;
        this.result = result;
        this.source = source;
        this.color = color;
    }

    @Override
    public String tag() {
        return "fluid" + name();
    }

    @Override
    public String forgeTag() {
        return "fluid/" + name();
    }

    @Override
    public boolean isItem() {
        return false;
    }
}
