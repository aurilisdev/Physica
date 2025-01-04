package electrodynamics.common.item.subtype;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import electrodynamics.api.ISubtype;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum SubtypeDust implements ISubtype {
	iron(ElectrodynamicsTags.Items.DUST_IRON, () -> Items.IRON_INGOT, 200),
	gold(ElectrodynamicsTags.Items.DUST_GOLD, () -> Items.GOLD_INGOT, 200),
	copper(ElectrodynamicsTags.Items.DUST_COPPER, () -> Items.COPPER_INGOT, 200),
	tin(ElectrodynamicsTags.Items.DUST_TIN, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.tin), 200),
	silver(ElectrodynamicsTags.Items.DUST_SILVER, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.silver), 200),
	steel(ElectrodynamicsTags.Items.DUST_STEEL, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.steel), 200),
	lead(ElectrodynamicsTags.Items.DUST_LEAD, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.lead), 200),
	bronze(ElectrodynamicsTags.Items.DUST_BRONZE, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.bronze), 200),
	superconductive(ElectrodynamicsTags.Items.DUST_SUPERCONDUCTIVE, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.superconductive), 200),
	endereye(ElectrodynamicsTags.Items.DUST_ENDEREYE),
	vanadium(ElectrodynamicsTags.Items.DUST_VANADIUM, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.vanadium), 200),
	sulfur(ElectrodynamicsTags.Items.DUST_SULFUR),
	niter(ElectrodynamicsTags.Items.DUST_SALTPETER),
	obsidian(ElectrodynamicsTags.Items.DUST_OBSIDIAN),
	lithium(ElectrodynamicsTags.Items.DUST_LITHIUM, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.lithium), 200),
	salt(ElectrodynamicsTags.Items.DUST_SALT),
	silica(ElectrodynamicsTags.Items.DUST_SILICA, () -> ElectrodynamicsItems.ITEMS_CUSTOMGLASS.getValue(SubtypeGlass.clear), 200),
	molybdenum(ElectrodynamicsTags.Items.DUST_MOLYBDENUM, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.molybdenum), 200),
	netherite(ElectrodynamicsTags.Items.DUST_NETHERITE, () -> Items.NETHERITE_SCRAP, 200),
	aluminum(ElectrodynamicsTags.Items.DUST_ALUMINUM, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.aluminum), 200),
	chromium(ElectrodynamicsTags.Items.DUST_CHROMIUM, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.chromium), 200),
	titanium(ElectrodynamicsTags.Items.DUST_TITANIUM, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.titanium), 200);

	public final TagKey<Item> tag;
	@Nullable
	public final Supplier<Item> smeltedItem;
	public final int smeltTime;

	SubtypeDust(TagKey<Item> tag) {
		this(tag, null, 0);
	}

	SubtypeDust(TagKey<Item> tag, Supplier<Item> smeltedItem, int smeltTime) {
		this.tag = tag;
		this.smeltedItem = smeltedItem;
		this.smeltTime = smeltTime;
	}

	@Override
	public String tag() {
		return "dust" + name();
	}

	@Override
	public String forgeTag() {
		return "dusts/" + name();
	}

	@Override
	public boolean isItem() {
		return true;
	}
}
