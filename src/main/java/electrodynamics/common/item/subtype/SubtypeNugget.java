package electrodynamics.common.item.subtype;

import java.util.function.Supplier;

import electrodynamics.api.ISubtype;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

public enum SubtypeNugget implements ISubtype {
	tin(ElectrodynamicsTags.Items.NUGGET_TIN, ElectrodynamicsTags.Items.INGOT_TIN, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.tin)),
	copper(ElectrodynamicsTags.Items.NUGGET_COPPER, Tags.Items.INGOTS_COPPER, () -> Items.COPPER_INGOT),
	silver(ElectrodynamicsTags.Items.NUGGET_SILVER, ElectrodynamicsTags.Items.INGOT_SILVER, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.silver)),
	superconductive(ElectrodynamicsTags.Items.NUGGET_SUPERCONDUCTIVE, ElectrodynamicsTags.Items.INGOT_SUPERCONDUCTIVE, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.superconductive)),
	steel(ElectrodynamicsTags.Items.NUGGET_STEEL, ElectrodynamicsTags.Items.INGOT_STEEL, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.steel)),
	stainlesssteel(ElectrodynamicsTags.Items.NUGGET_STAINLESSSTEEL, ElectrodynamicsTags.Items.INGOT_STAINLESSSTEEL, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.stainlesssteel)),
	hslasteel(ElectrodynamicsTags.Items.NUGGET_HSLASTEEL, ElectrodynamicsTags.Items.INGOT_HSLASTEEL, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.hslasteel)),
	titaniumcarbide(ElectrodynamicsTags.Items.NUGGET_TITANIUMCARBIDE, ElectrodynamicsTags.Items.INGOT_TITANIUMCARBIDE, () -> ElectrodynamicsItems.ITEMS_INGOT.getValue(SubtypeIngot.titaniumcarbide));

	public final TagKey<Item> tag;
	public final TagKey<Item> sourceIngot;
	public final Supplier<Item> productIngot;

	SubtypeNugget(TagKey<Item> tag, TagKey<Item> sourceIngot, Supplier<Item> productIngot) {
		this.tag = tag;
		this.sourceIngot = sourceIngot;
		this.productIngot = productIngot;
	}

	@Override
	public String tag() {
		return "nugget" + name();
	}

	@Override
	public String forgeTag() {
		return "nuggets/" + name();
	}

	@Override
	public boolean isItem() {
		return true;
	}

}
