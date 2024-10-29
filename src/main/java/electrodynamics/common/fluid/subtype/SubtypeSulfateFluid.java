package electrodynamics.common.fluid.subtype;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import electrodynamics.api.ISubtype;
import electrodynamics.common.item.subtype.SubtypeCrystal;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;

public enum SubtypeSulfateFluid implements ISubtype {
    copper(ElectrodynamicsTags.Fluids.COPPER_SULF, () -> ElectrodynamicsItems.getItem(SubtypeCrystal.copper), ItemTags.COPPER_ORES),
    tin(ElectrodynamicsTags.Fluids.TIN_SULF, () -> ElectrodynamicsItems.getItem(SubtypeCrystal.tin), ElectrodynamicsTags.Items.ORE_TIN),
    silver(ElectrodynamicsTags.Fluids.SILVER_SULF, () -> ElectrodynamicsItems.getItem(SubtypeCrystal.silver), ElectrodynamicsTags.Items.ORE_SILVER),
    lead(ElectrodynamicsTags.Fluids.LEAD_SULF, () -> ElectrodynamicsItems.getItem(SubtypeCrystal.lead), ElectrodynamicsTags.Items.ORE_LEAD),
    vanadium(ElectrodynamicsTags.Fluids.VANADIUM_SULF, () -> ElectrodynamicsItems.getItem(SubtypeCrystal.vanadium), ElectrodynamicsTags.Items.ORE_VANADIUM),
    iron(ElectrodynamicsTags.Fluids.IRON_SULF, () -> ElectrodynamicsItems.getItem(SubtypeCrystal.iron), ItemTags.IRON_ORES),
    gold(ElectrodynamicsTags.Fluids.GOLD_SULF, () -> ElectrodynamicsItems.getItem(SubtypeCrystal.gold), ItemTags.GOLD_ORES),
    lithium(ElectrodynamicsTags.Fluids.LITHIUM_SULF, () -> ElectrodynamicsItems.getItem(SubtypeCrystal.lithium), ElectrodynamicsTags.Items.ORE_LITHIUM),
    molybdenum(ElectrodynamicsTags.Fluids.MOLYBDENUM_SULF, () -> ElectrodynamicsItems.getItem(SubtypeCrystal.molybdenum), ElectrodynamicsTags.Items.ORE_MOLYBDENUM),
    netherite(ElectrodynamicsTags.Fluids.NETHERITE_SULF, () -> ElectrodynamicsItems.getItem(SubtypeCrystal.netherite), Tags.Items.ORES_NETHERITE_SCRAP);

    public final TagKey<Fluid> tag;
    @Nullable
    public final TagKey<Item> source;
    @Nullable
    public final Supplier<Item> crystal;

    SubtypeSulfateFluid(TagKey<Fluid> tag, TagKey<Item> source) {
        this(tag, null, source);
    }

    SubtypeSulfateFluid(TagKey<Fluid> tag, Supplier<Item> crystal, TagKey<Item> source) {
        this.tag = tag;
        this.crystal = crystal;
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
