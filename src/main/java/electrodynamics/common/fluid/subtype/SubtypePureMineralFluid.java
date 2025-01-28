package electrodynamics.common.fluid.subtype;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import electrodynamics.api.ISubtype;
import electrodynamics.common.item.subtype.SubtypeCrystal;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public enum SubtypePureMineralFluid implements ISubtype {
    copper(ElectrodynamicsTags.Fluids.PURE_COPPER, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.copper), Color.WHITE),
    tin(ElectrodynamicsTags.Fluids.PURE_TIN, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.tin), Color.WHITE),
    silver(ElectrodynamicsTags.Fluids.PURE_SILVER, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.silver), Color.WHITE),
    lead(ElectrodynamicsTags.Fluids.PURE_LEAD, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.lead), Color.WHITE),
    vanadium(ElectrodynamicsTags.Fluids.PURE_VANADIUM, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.vanadium), Color.WHITE),
    iron(ElectrodynamicsTags.Fluids.PURE_IRON, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.iron), Color.WHITE),
    gold(ElectrodynamicsTags.Fluids.PURE_GOLD, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.gold), Color.WHITE),
    lithium(ElectrodynamicsTags.Fluids.PURE_LITHIUM, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.lithium), Color.WHITE),
    molybdenum(ElectrodynamicsTags.Fluids.PURE_MOLYBDENUM, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.molybdenum), Color.WHITE),
    netherite(ElectrodynamicsTags.Fluids.PURE_NETHERITE, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.netherite), Color.WHITE),
    aluminum(ElectrodynamicsTags.Fluids.PURE_ALUMINUM, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.aluminum), Color.WHITE),
    titanium(ElectrodynamicsTags.Fluids.PURE_TITANIUM, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.titanium), Color.WHITE),
    chromium(ElectrodynamicsTags.Fluids.PURE_CHROMIUM, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.chromium), Color.WHITE);

    public final TagKey<Fluid> tag;
    @Nullable
    public final Supplier<Item> result;
    public final Color color;

    SubtypePureMineralFluid(TagKey<Fluid> tag, Color color) {
        this(tag, null, color);
    }

    SubtypePureMineralFluid(TagKey<Fluid> tag, Supplier<Item> result, Color color) {
        this.tag = tag;
        this.result = result;
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
