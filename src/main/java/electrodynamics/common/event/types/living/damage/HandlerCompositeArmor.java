package electrodynamics.common.event.types.living.damage;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class HandlerCompositeArmor extends AbstractLivingDamageHandler {

	private static final float LETHAL_DAMAGE_AMOUNT = 18.0f;

	private static final ItemStack[] COMPOSITE_ARMOR = new ItemStack[] { new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITEHELMET.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITECHESTPLATE.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITELEGGINGS.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITEBOOTS.get()) };
	private static final ItemStack[] COMBAT_ARMOR = new ItemStack[] { new ItemStack(ElectrodynamicsItems.ITEM_COMBATHELMET.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMBATLEGGINGS.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMBATBOOTS.get()) };

	@Override
	public void handle(LivingDamageEvent.Pre event) {
		LivingEntity entity = event.getEntity();
		if (event.getSource().is(DamageTypes.FALL)) {
			return;
		}
		List<ItemStack> armorPieces = new ArrayList<>();
		event.getEntity().getArmorSlots().forEach(piece -> armorPieces.add(piece));

		if (compareArmor(armorPieces, COMPOSITE_ARMOR) || compareArmor(armorPieces, COMBAT_ARMOR)) {
			ItemStack stack = armorPieces.get(2);
			int stored = stack.getOrDefault(ElectrodynamicsDataComponentTypes.PLATES, 0);
			if (event.getOriginalDamage() >= LETHAL_DAMAGE_AMOUNT && stored > 0) {
				event.setNewDamage((float) Math.sqrt(event.getOriginalDamage()));
				stack.set(ElectrodynamicsDataComponentTypes.PLATES, stored);
                event.getEntity().getCommandSenderWorld().playSound(null, event.getEntity().blockPosition(), ElectrodynamicsSounds.SOUND_CERAMICPLATEBREAKING.get(), SoundSource.PLAYERS, 1, 1);
			}
		}

	}

	private boolean compareArmor(List<ItemStack> set1, ItemStack[] set2) {
		if (set1.size() >= 3) {
			return set1.get(0).getItem() == set2[3].getItem() && set1.get(1).getItem() == set2[2].getItem() && set1.get(2).getItem() == set2[1].getItem() && set1.get(3).getItem() == set2[0].getItem();
		}
		return false;
	}

}
