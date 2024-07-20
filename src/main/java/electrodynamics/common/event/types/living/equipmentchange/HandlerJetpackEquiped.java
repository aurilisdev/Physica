package electrodynamics.common.event.types.living.equipmentchange;

import electrodynamics.common.packet.types.client.PacketJetpackEquipedSound;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class HandlerJetpackEquiped extends AbstractEquipmentChangeHandler {

    @Override
    public void handler(LivingEquipmentChangeEvent event) {
        Entity entity = event.getEntity();
        if (event.getSlot() == EquipmentSlot.CHEST && entity instanceof Player player) {
            ItemStack chest = event.getTo();
            if (event.getFrom().isEmpty() && (ItemUtils.testItems(chest.getItem(), ElectrodynamicsItems.ITEM_JETPACK.get()) || ItemUtils.testItems(chest.getItem(), ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get()))) {
                PacketDistributor.sendToPlayer((ServerPlayer) player, new PacketJetpackEquipedSound(player.getUUID()));
            }
        }

    }

}
