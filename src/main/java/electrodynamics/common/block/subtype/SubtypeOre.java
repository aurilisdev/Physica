package electrodynamics.common.block.subtype;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import electrodynamics.api.ISubtype;
import electrodynamics.common.item.subtype.SubtypeCrystal;
import electrodynamics.common.item.subtype.SubtypeDust;
import electrodynamics.common.item.subtype.SubtypeIngot;
import electrodynamics.common.item.subtype.SubtypeRawOre;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public enum SubtypeOre implements ISubtype {

    // default mc values in OrePlacements.class

    aluminum(2, 5, 5, 16, 48, 4f, 4.4f, 1, 1, null, null, 0, 0, ElectrodynamicsTags.Items.ORE_ALUMINUM, ElectrodynamicsTags.Blocks.ORE_ALUMINUM),
    chromium(3, 4, 8, -10, 32, 10f, 6.5f, 1, 1, () -> ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.chromium), null, 0, 0, ElectrodynamicsTags.Items.ORE_CHROMIUM, ElectrodynamicsTags.Blocks.ORE_CHROMIUM),
    fluorite(1, 4, 4, -10, 30, 2f, 2f, 1, 1, () -> ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.fluorite), null, 0, 0, ElectrodynamicsTags.Items.ORE_FLUORITE, ElectrodynamicsTags.Blocks.ORE_FLUORITE),
    lead(2, 10, 6, -64, 32, 4f, 8f, 1, 1, () -> ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.lead), () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.lead), 0.7, 200, ElectrodynamicsTags.Items.ORE_LEAD, ElectrodynamicsTags.Blocks.ORE_LEAD),
    lithium(2, 3, 9, -32, 32, 4f, 8f, 1, 1, () -> ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.lepidolite), null, 0, 0, ElectrodynamicsTags.Items.ORE_LITHIUM, ElectrodynamicsTags.Blocks.ORE_LITHIUM),
    molybdenum(1, 9, 4, 50, 60, 3f, 3f, 1, 1, null,() -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.molybdenum), 0.1, 200, ElectrodynamicsTags.Items.ORE_MOLYBDENUM, ElectrodynamicsTags.Blocks.ORE_MOLYBDENUM),
    monazite(2, 5, 8, 0, 32, 6f, 4.5f, 1, 1, null, null, 0, 0, ElectrodynamicsTags.Items.ORE_MONAZITE, ElectrodynamicsTags.Blocks.ORE_MONAZITE),
    niter(1, 5, 13, 0, 28, 6f, 4.5f, 1, 2, 1, 3, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.niter), null, 0, 0, ElectrodynamicsTags.Items.ORE_SALTPETER, ElectrodynamicsTags.Blocks.ORE_SALTPETER),
    salt(1, 8, 9, 32, 64, 3f, 5f, 0, 2, 1, 3, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.halite), null, 0, 0, ElectrodynamicsTags.Items.ORE_SALT, ElectrodynamicsTags.Blocks.ORE_SALT),
    silver(2, 6, 9, -64, 48, 4f, 5.5f, 1, 1, () -> ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.silver), () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.silver), 1.0, 200, ElectrodynamicsTags.Items.ORE_SILVER, ElectrodynamicsTags.Blocks.ORE_SILVER),
    sulfur(1, 5, 13, -28, 28, 6f, 4.5f, 1, 2, 3, 5, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.sulfur), null, 0, 0, ElectrodynamicsTags.Items.ORE_SULFUR, ElectrodynamicsTags.Blocks.ORE_SULFUR),
    sylvite(1, 5, 4, -20, 50, 2f, 2f, 3, 7, 1, 3, () -> ElectrodynamicsItems.ITEMS_CRYSTAL.getValue(SubtypeCrystal.potassiumchloride), null, 0, 0, ElectrodynamicsTags.Items.ORE_POTASSIUMCHLORIDE, ElectrodynamicsTags.Blocks.ORE_POTASSIUMCHLORIDE),
    tin(1, 12, 9, -16, 112, 3f, 5f, 1, 1, () -> ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.tin), () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.tin), 0.7, 200, ElectrodynamicsTags.Items.ORE_TIN, ElectrodynamicsTags.Blocks.ORE_TIN),
    titanium(3, 5, 8, -48, -8, 10f, 6.5f, 1, 1, () -> ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.titanium), null, 0, 0, ElectrodynamicsTags.Items.ORE_TITANIUM, ElectrodynamicsTags.Blocks.ORE_TITANIUM),
    thorium(3, 10, 8, -32, 32, 10f, 6.5f, 1, 1, () -> ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.thorium), null, 0, 0, ElectrodynamicsTags.Items.ORE_THORIUM, ElectrodynamicsTags.Blocks.ORE_THORIUM),
    uranium(3, 2, 8, -64, -32, 10f, 6.5f, 1, 1, () -> ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.uranium), null, 0, 0, ElectrodynamicsTags.Items.ORE_URANIUM, ElectrodynamicsTags.Blocks.ORE_URANIUM),
    vanadium(2, 8, 8, -32, 32, 6f, 4.5f, 1, 1, () -> ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.vanadinite), () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.vanadium), 0.7, 200, ElectrodynamicsTags.Items.ORE_VANADIUM, ElectrodynamicsTags.Blocks.ORE_VANADIUM);

    public final int harvestLevel;
    public final int veinsPerChunk;
    public final int veinSize;
    public final int minY;
    public final int maxY;
    public final float hardness;
    public final float resistance;
    public final int minXP;
    public final int maxXP;
    public final int minDrop;
    public final int maxDrop;
    // Leaving this null will result in the ore dropping itself when broken
    @Nullable
    public final Supplier<Item> nonSilkLootItem;
    // Leaving this null will result in the ore not smelting into anything
    @Nullable
    public final Supplier<Item> smeltingItem;
    public final double smeltingXp;
    public final int smeltingTime;
    public final TagKey<Item> itemTag;
    public final TagKey<Block> blockTag;

    SubtypeOre(int harvestLevel, int veinsPerChunk, int veinSize, int minY, int maxY, float hardness, float resistance, int minDrop, int maxDrop, Supplier<Item> lootItem, Supplier<Item> smeltingItem, double smeltingXp, int smeltingTime, TagKey<Item> itemTag, TagKey<Block> blockTag) {
        this(harvestLevel, veinsPerChunk, veinSize, minY, maxY, hardness, resistance, 0, 0, minDrop, maxDrop, lootItem, smeltingItem, smeltingXp, smeltingTime, itemTag, blockTag);
    }

    SubtypeOre(int harvestLevel, int veinsPerChunk, int veinSize, int minY, int maxY, float hardness, float resistance, int minXP, int maxXP, int minDrop, int maxDrop, Supplier<Item> lootItem, Supplier<Item> smeltingItem, double smeltingXp, int smeltingTime, TagKey<Item> itemTag, TagKey<Block> blockTag) {

        this.harvestLevel = harvestLevel;
        this.veinsPerChunk = veinsPerChunk;
        this.veinSize = veinSize;
        this.resistance = resistance;
        this.minY = minY;
        this.maxY = maxY;
        this.hardness = hardness;
        this.minXP = minXP;
        this.maxXP = maxXP;
        this.minDrop = minDrop;
        this.maxDrop = maxDrop;
        nonSilkLootItem = lootItem;
        this.smeltingItem = smeltingItem;
        this.smeltingXp = smeltingXp;
        this.smeltingTime = smeltingTime;
        this.itemTag = itemTag;
        this.blockTag = blockTag;
    }

    @Override
    public String tag() {
        return "ore" + name();
    }

    @Override
    public String forgeTag() {
        return "ores/" + name();
    }

    @Override
    public boolean isItem() {
        return false;
    }

    public static SubtypeOre[] getOreForMiningLevel(int level) {
        List<SubtypeOre> values = new ArrayList<>();
        for (SubtypeOre value : values()) {
            if (value.harvestLevel == level) {
                values.add(value);
            }
        }
        return values.toArray(new SubtypeOre[] {});
    }

}
