package electrodynamics.common.item.subtype;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import electrodynamics.api.ISubtype;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.world.item.Item;

public enum SubtypeCrystal implements ISubtype {
	iron(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.iron)),
	gold(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.gold)),
	copper(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.copper)),
	tin(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.tin)),
	silver(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.silver)),
	lead(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.lead)),
	vanadium(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.vanadium)),
	lithium(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.lithium)),
	halite(() -> ElectrodynamicsItems.ITEMS_DUST.getValue(SubtypeDust.salt)),
	molybdenum(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.molybdenum)),
	potassiumchloride(null),
	netherite(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.netherite)),
	aluminum(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.aluminum)),
	titanium(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.titanium)),
	chromium(() -> ElectrodynamicsItems.ITEMS_IMPUREDUST.getValue(SubtypeImpureDust.chromium));

	@Nullable
	public final Supplier<Item> crushedItem;

	SubtypeCrystal(Supplier<Item> crushedItem) {
		this.crushedItem = crushedItem;
	}

	@Override
	public String tag() {
		return "crystal" + name();
	}

	@Override
	public String forgeTag() {
		return "crystal/" + name();
	}

	@Override
	public boolean isItem() {
		return true;
	}
}
