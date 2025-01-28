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

public enum SubtypeRoyalMineralFluid implements ISubtype {
    copper(() -> ElectrodynamicsFluids.FLUIDS_CRUDEMINERAL.getValue(SubtypeCrudeMineralFluid.copper), ItemTags.COPPER_ORES, Color.WHITE),
    tin(() -> ElectrodynamicsFluids.FLUIDS_CRUDEMINERAL.getValue(SubtypeCrudeMineralFluid.tin), ElectrodynamicsTags.Items.ORE_TIN, Color.WHITE),
    silver(() -> ElectrodynamicsFluids.FLUIDS_CRUDEMINERAL.getValue(SubtypeCrudeMineralFluid.silver), ElectrodynamicsTags.Items.ORE_SILVER, Color.WHITE),
    lead(() -> ElectrodynamicsFluids.FLUIDS_CRUDEMINERAL.getValue(SubtypeCrudeMineralFluid.lead), ElectrodynamicsTags.Items.ORE_LEAD, Color.WHITE),
    vanadium(() -> ElectrodynamicsFluids.FLUIDS_CRUDEMINERAL.getValue(SubtypeCrudeMineralFluid.vanadium), ElectrodynamicsTags.Items.ORE_VANADIUM, Color.WHITE),
    iron(() -> ElectrodynamicsFluids.FLUIDS_CRUDEMINERAL.getValue(SubtypeCrudeMineralFluid.iron), ItemTags.IRON_ORES, Color.WHITE),
    gold(() -> ElectrodynamicsFluids.FLUIDS_CRUDEMINERAL.getValue(SubtypeCrudeMineralFluid.gold), ItemTags.GOLD_ORES, Color.WHITE),
    lithium(() -> ElectrodynamicsFluids.FLUIDS_CRUDEMINERAL.getValue(SubtypeCrudeMineralFluid.lithium), ElectrodynamicsTags.Items.ORE_LITHIUM, Color.WHITE),
    molybdenum(() -> ElectrodynamicsFluids.FLUIDS_CRUDEMINERAL.getValue(SubtypeCrudeMineralFluid.molybdenum), ElectrodynamicsTags.Items.ORE_MOLYBDENUM, Color.WHITE),
    netherite(() -> ElectrodynamicsFluids.FLUIDS_CRUDEMINERAL.getValue(SubtypeCrudeMineralFluid.netherite), Tags.Items.ORES_NETHERITE_SCRAP, Color.WHITE),
    aluminum(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.aluminum), ElectrodynamicsTags.Items.ORE_ALUMINUM, Color.WHITE),
    titanium(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.titanium), ElectrodynamicsTags.Items.ORE_TITANIUM, Color.WHITE),
    chromium(() -> ElectrodynamicsFluids.FLUIDS_DIRTYMINERAL.getValue(SubtypeDirtyMineralFluid.chromium), ElectrodynamicsTags.Items.ORE_CHROMIUM, Color.WHITE);

    @Nullable
    public final TagKey<Item> source;
    @Nullable
    public final Supplier<Fluid> result;
    public final Color color;

    SubtypeRoyalMineralFluid(TagKey<Item> source, Color color) {
        this(null, source, color);
    }

    SubtypeRoyalMineralFluid(Supplier<Fluid> result, TagKey<Item> source, Color color) {
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
