package electrodynamics.common.fluid.subtype;

import electrodynamics.api.ISubtype;
import electrodynamics.common.item.subtype.SubtypeCrystal;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum SubtypePureMineralFluid implements ISubtype {
    copper(ElectrodynamicsTags.Fluids.PURE_COPPER, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.copper)),
    tin(ElectrodynamicsTags.Fluids.PURE_TIN, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.tin)),
    silver(ElectrodynamicsTags.Fluids.PURE_SILVER, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.silver)),
    lead(ElectrodynamicsTags.Fluids.PURE_LEAD, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.lead)),
    vanadium(ElectrodynamicsTags.Fluids.PURE_VANADIUM, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.vanadium)),
    iron(ElectrodynamicsTags.Fluids.PURE_IRON, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.iron)),
    gold(ElectrodynamicsTags.Fluids.PURE_GOLD, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.gold)),
    lithium(ElectrodynamicsTags.Fluids.PURE_LITHIUM, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.lithium)),
    molybdenum(ElectrodynamicsTags.Fluids.PURE_MOLYBDENUM, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.molybdenum)),
    netherite(ElectrodynamicsTags.Fluids.PURE_NETHERITE, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.netherite)),
    aluminum(ElectrodynamicsTags.Fluids.PURE_ALUMINUM, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.aluminum)),
    titanium(ElectrodynamicsTags.Fluids.PURE_TITANIUM, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.titanium)),
    chromium(ElectrodynamicsTags.Fluids.PURE_CHROMIUM, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.chromium));

    public final TagKey<Fluid> tag;
    @Nullable
    public final Supplier<Item> result;

    SubtypePureMineralFluid(TagKey<Fluid> tag) {
        this(tag, null);
    }

    SubtypePureMineralFluid(TagKey<Fluid> tag, Supplier<Item> result) {
        this.tag = tag;
        this.result = result;
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
