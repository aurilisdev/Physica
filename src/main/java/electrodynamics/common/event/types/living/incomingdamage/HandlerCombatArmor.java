package electrodynamics.common.event.types.living.incomingdamage;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.gas.GasStack;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class HandlerCombatArmor extends AbstractIncomingDamageHandler {

    private static final ItemStack[] COMBAT_ARMOR = new ItemStack[] { new ItemStack(ElectrodynamicsItems.ITEM_COMBATHELMET.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMBATLEGGINGS.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMBATBOOTS.get()) };

    @Override
    public void handle(LivingIncomingDamageEvent event) {

        if(!event.getSource().is(DamageTypeTags.IS_FIRE)){
            return;
        }

        List<ItemStack> armorPieces = new ArrayList<>();
        event.getEntity().getArmorSlots().forEach(piece -> armorPieces.add(piece));

        if (!compareArmor(armorPieces, COMBAT_ARMOR)) {
            return;
        }

        GasStack gas = armorPieces.get(1).getOrDefault(ElectrodynamicsDataComponentTypes.GAS_STACK, GasStack.EMPTY);

        if(gas.isEmpty()){
            return;
        }

        gas.shrink(1);

        armorPieces.get(1).set(ElectrodynamicsDataComponentTypes.GAS_STACK, gas.copy());

        event.setCanceled(true);

    }

    private boolean compareArmor(List<ItemStack> set1, ItemStack[] set2) {
        if (set1.size() >= 3) {
            return set1.get(0).getItem() == set2[3].getItem() && set1.get(1).getItem() == set2[2].getItem() && set1.get(2).getItem() == set2[1].getItem() && set1.get(3).getItem() == set2[0].getItem();
        }
        return false;
    }
}
