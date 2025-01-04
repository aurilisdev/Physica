package electrodynamics.client.keys.event;

import electrodynamics.client.keys.KeyBinds;
import electrodynamics.common.packet.types.server.PacketToggleOnServer;
import electrodynamics.common.packet.types.server.PacketToggleOnServer.Type;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.InputEvent.Key;
import net.neoforged.neoforge.network.PacketDistributor;

public class HandlerToggleServoLegs extends AbstractKeyPressHandler {

    @Override
    public void handler(Key event, Minecraft minecraft) {
        Player player = minecraft.player;
        if (KeyBinds.toggleServoLeggings.matches(event.getKey(), event.getScanCode()) && KeyBinds.toggleServoLeggings.isDown()) {
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            if (ItemUtils.testItems(legs.getItem(), ElectrodynamicsItems.ITEM_SERVOLEGGINGS.get()) || ItemUtils.testItems(legs.getItem(), ElectrodynamicsItems.ITEM_COMBATLEGGINGS.get())) {
                PacketDistributor.sendToServer(new PacketToggleOnServer(player.getUUID(), Type.SERVOLEGGINGS));
            }
        }
    }

}
