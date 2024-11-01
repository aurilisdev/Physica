package electrodynamics.common.fluid.subtype;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import electrodynamics.api.ISubtype;
import electrodynamics.common.item.subtype.SubtypeCrystal;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsFluids;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;

public enum SubtypeSulfateFluid implements ISubtype {
    copper(ElectrodynamicsTags.Fluids.COPPER_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.copper), ItemTags.COPPER_ORES),
    tin(ElectrodynamicsTags.Fluids.TIN_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.tin), ElectrodynamicsTags.Items.ORE_TIN),
    silver(ElectrodynamicsTags.Fluids.SILVER_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.silver), ElectrodynamicsTags.Items.ORE_SILVER),
    lead(ElectrodynamicsTags.Fluids.LEAD_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.lead), ElectrodynamicsTags.Items.ORE_LEAD),
    vanadium(ElectrodynamicsTags.Fluids.VANADIUM_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.vanadium), ElectrodynamicsTags.Items.ORE_VANADIUM),
    iron(ElectrodynamicsTags.Fluids.IRON_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.iron), ItemTags.IRON_ORES),
    gold(ElectrodynamicsTags.Fluids.GOLD_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.gold), ItemTags.GOLD_ORES),
    lithium(ElectrodynamicsTags.Fluids.LITHIUM_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.lithium), ElectrodynamicsTags.Items.ORE_LITHIUM),
    molybdenum(ElectrodynamicsTags.Fluids.MOLYBDENUM_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.molybdenum), ElectrodynamicsTags.Items.ORE_MOLYBDENUM),
    netherite(ElectrodynamicsTags.Fluids.NETHERITE_SULFATE, () -> ElectrodynamicsFluids.FLUIDS_PUREMINERAL.getValue(SubtypePureMineralFluid.netherite), Tags.Items.ORES_NETHERITE_SCRAP);

    public final TagKey<Fluid> tag;
    @Nullable
    public final TagKey<Item> source;
    @Nullable
    public final Supplier<Fluid> result;

    SubtypeSulfateFluid(TagKey<Fluid> tag, TagKey<Item> source) {
        this(tag, null, source);
    }

    SubtypeSulfateFluid(TagKey<Fluid> tag, Supplier<Fluid> result, TagKey<Item> source) {
        this.tag = tag;
        this.result = result;
        this.source = source;
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
