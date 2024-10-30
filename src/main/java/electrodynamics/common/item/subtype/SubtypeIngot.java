package electrodynamics.common.item.subtype;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import electrodynamics.api.ISubtype;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public enum SubtypeIngot implements ISubtype {

	tin(ElectrodynamicsTags.Items.INGOT_TIN, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.tin)),
	silver(ElectrodynamicsTags.Items.INGOT_SILVER, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.silver)),
	steel(ElectrodynamicsTags.Items.INGOT_STEEL, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.steel)),
	lead(ElectrodynamicsTags.Items.INGOT_LEAD, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.lead)),
	superconductive(ElectrodynamicsTags.Items.INGOT_SUPERCONDUCTIVE, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.superconductive)),
	bronze(ElectrodynamicsTags.Items.INGOT_BRONZE, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.bronze)),
	vanadium(ElectrodynamicsTags.Items.INGOT_VANADIUM, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.vanadium)),
	lithium(ElectrodynamicsTags.Items.INGOT_LITHIUM, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.lithium)),
	aluminum(ElectrodynamicsTags.Items.INGOT_ALUMINUM),
	chromium(ElectrodynamicsTags.Items.INGOT_CHROMIUM, () -> ElectrodynamicsItems.ITEMS_OXIDE.getValue(SubtypeOxide.chromite)),
	stainlesssteel(ElectrodynamicsTags.Items.INGOT_STAINLESSSTEEL),
	vanadiumsteel(ElectrodynamicsTags.Items.INGOT_VANADIUMSTEEL),
	hslasteel(ElectrodynamicsTags.Items.INGOT_HSLASTEEL),
	titanium(ElectrodynamicsTags.Items.INGOT_TITANIUM),
	molybdenum(ElectrodynamicsTags.Items.INGOT_MOLYBDENUM, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.molybdenum)),
	titaniumcarbide(ElectrodynamicsTags.Items.INGOT_TITANIUMCARBIDE);

	public final TagKey<Item> tag;
	@Nullable
	public final Supplier<Item> grindedDust;

	SubtypeIngot(TagKey<Item> tag) {
		this(tag, null);
	}

	SubtypeIngot(TagKey<Item> tag, Supplier<Item> grindedDust) {
		this.tag = tag;
		this.grindedDust = grindedDust;
	}

	@Override
	public String tag() {
		return "ingot" + name();
	}

	@Override
	public String forgeTag() {
		return "ingots/" + name();
	}

	@Override
	public boolean isItem() {
		return true;
	}
}
