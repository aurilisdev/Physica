package electrodynamics.common.event.types.living.knockback;

import java.util.ArrayList;

import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;

/**
 * This event should deal with the jetpack user getting yeeted into the ground when flying if damaged
 * 
 * @author skip999
 *
 */
public class HandlerJetpackKnockbackImpulse extends AbstractLivingKnockbackHandler {

	@Override
	public void handle(LivingKnockBackEvent event) {

		LivingEntity entity = event.getEntity();
		ArrayList<ItemStack> armor = new ArrayList<>();
		entity.getArmorSlots().forEach(armor::add);
		if (armor.size() < 3) {
			return;
		}
		ItemStack chestplate = armor.get(3);

		if (chestplate.isEmpty() || !ItemUtils.testItems(chestplate.getItem(), ElectrodynamicsItems.ITEM_JETPACK.get(), ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get()) || chestplate.getOrDefault(ElectrodynamicsDataComponentTypes.USED, false)) {
			return;
		}

		event.setCanceled(true);

	}

}
