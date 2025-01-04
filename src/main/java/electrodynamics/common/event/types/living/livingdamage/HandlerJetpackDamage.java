package electrodynamics.common.event.types.living.livingdamage;

import java.util.ArrayList;

import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class HandlerJetpackDamage extends AbstractLivingDamageHandler {

	// this way we know the impulse was because of the player being hurt
	@Override
	public void handle(LivingDamageEvent.Pre event) {

		LivingEntity entity = event.getEntity();
		ArrayList<ItemStack> armor = new ArrayList<>();
		entity.getArmorSlots().forEach(armor::add);
		if (armor.size() < 3) {
			return;
		}
		ItemStack chestplate = armor.get(3);

		if (chestplate.isEmpty() || !ItemUtils.testItems(chestplate.getItem(), ElectrodynamicsItems.ITEM_JETPACK.get(), ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get())) {
			return;
		}

		chestplate.set(ElectrodynamicsDataComponentTypes.HURT, true);

	}

}
