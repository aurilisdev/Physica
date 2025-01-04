package electrodynamics.common.item.subtype;

import java.util.function.Supplier;

import electrodynamics.api.ISubtype;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public enum SubtypeImpureDust implements ISubtype {
	iron(ElectrodynamicsTags.Items.IMPURE_DUST_IRON, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.iron)),
	gold(ElectrodynamicsTags.Items.IMPURE_DUST_GOLD, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.gold)),
	copper(ElectrodynamicsTags.Items.IMPURE_DUST_COPPER, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.copper)),
	tin(ElectrodynamicsTags.Items.IMPURE_DUST_TIN, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.tin)),
	silver(ElectrodynamicsTags.Items.IMPURE_DUST_SILVER, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.silver)),
	lead(ElectrodynamicsTags.Items.IMPURE_DUST_LEAD, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.lead)),
	vanadium(ElectrodynamicsTags.Items.IMPURE_DUST_VANADIUM, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.vanadium)),
	lithium(ElectrodynamicsTags.Items.IMPURE_DUST_LITHIUM, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.lithium)),
	molybdenum(ElectrodynamicsTags.Items.IMPURE_DUST_MOLYBDENUM, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.molybdenum)),
	netherite(ElectrodynamicsTags.Items.IMPURE_DUST_NETHERITE, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.netherite)),
	aluminum(ElectrodynamicsTags.Items.IMPURE_DUST_ALUMINUM, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.aluminum)),
	chromium(ElectrodynamicsTags.Items.IMPURE_DUST_CHROMIUM, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.chromium)),
	titanium(ElectrodynamicsTags.Items.IMPURE_DUST_TITANIUM, () -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.titanium));


	public final TagKey<Item> tag;
	public final Supplier<Item> grindedDust;

	SubtypeImpureDust(TagKey<Item> tag, Supplier<Item> grindedDust) {
		this.tag = tag;
		this.grindedDust = grindedDust;
	}

	@Override
	public String tag() {
		return "impuredust" + name();
	}

	@Override
	public String forgeTag() {
		return "impuredusts/" + name();
	}

	@Override
	public boolean isItem() {
		return true;
	}
}
