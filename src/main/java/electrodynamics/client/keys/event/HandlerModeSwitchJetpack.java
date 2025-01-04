package electrodynamics.client.keys.event;

import electrodynamics.client.keys.KeyBinds;
import electrodynamics.common.packet.types.server.PacketModeSwitchServer;
import electrodynamics.common.packet.types.server.PacketModeSwitchServer.Mode;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.InputEvent.Key;
import net.neoforged.neoforge.network.PacketDistributor;

public class HandlerModeSwitchJetpack extends AbstractKeyPressHandler {

    @Override
    public void handler(Key event, Minecraft minecraft) {
        Player player = minecraft.player;
        if (KeyBinds.switchJetpackMode.matches(event.getKey(), event.getScanCode()) && KeyBinds.switchJetpackMode.isDown()) {
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (ItemUtils.testItems(chest.getItem(), ElectrodynamicsItems.ITEM_JETPACK.get()) || ItemUtils.testItems(chest.getItem(), ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get())) {
                PacketDistributor.sendToServer(new PacketModeSwitchServer(player.getUUID(), Mode.JETPACK));
            }
        }
    }

}
