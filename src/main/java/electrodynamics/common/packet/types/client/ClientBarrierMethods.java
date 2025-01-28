package electrodynamics.common.packet.types.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.render.event.levelstage.HandlerSeismicScanner;
import electrodynamics.common.item.gear.armor.types.ItemCombatArmor;
import electrodynamics.common.item.gear.armor.types.ItemJetpack;
import electrodynamics.common.reloadlistener.CoalGeneratorFuelRegister;
import electrodynamics.common.reloadlistener.CombustionFuelRegister;
import electrodynamics.common.reloadlistener.GasCollectorChromoCardsRegister;
import electrodynamics.common.reloadlistener.ThermoelectricGeneratorHeatRegister;
//import electrodynamics.prefab.properties.PropertyManager.PropertyWrapper;
import electrodynamics.prefab.sound.tickable.TickableSoundJetpack;
import electrodynamics.prefab.utilities.object.CombustionFuelSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

/**
 * Apparently with packets, certain class calls cannot be called within the packet itself because Java
 * 
 * SoundInstance for example is an exclusively client class only
 * 
 * Place methods that need to use those here
 */
public class ClientBarrierMethods {

    public static void handleAddClientRenderInfo(UUID playerId, BlockPos pos) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel world = minecraft.level;
        if (world == null || minecraft.player == null) {
            return;
        }
        if (!minecraft.player.getUUID().equals(playerId)) {
            return;
        }
        HandlerSeismicScanner.addBlock(pos);
    }

    /*
    public static void handlePropertiesUpdateClient(BlockPos pos, HashSet<PropertyWrapper> values) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) {
            return;
        }
        BlockEntity tile = world.getBlockEntity(pos);

        if (tile instanceof IPropertyHolderTile holder) {
            for (PropertyWrapper wrapper : values) {

                holder.getPropertyManager().update(wrapper.index(), wrapper.value());

            }
        }
    }
    */

    public static void handlePacketJetpackEquipedSound(UUID messageId) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel world = minecraft.level;
        if (world == null) {
            return;
        }
        minecraft.getSoundManager().play(new TickableSoundJetpack(messageId));
    }

    public static void handleJetpackParticleRendering(UUID player, boolean bool) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel world = minecraft.level;
        if (world == null) {
            return;
        }
        Player clientPlayer = minecraft.player;
        if (clientPlayer == null || clientPlayer.getUUID().equals(player)) {
            return;
        }
        Player ownerPlayer = world.getPlayerByUUID(player);
        if (ownerPlayer == null) {
            return;
        }
        ItemJetpack.renderClientParticles(world, ownerPlayer, bool ? ItemCombatArmor.OFFSET : ItemJetpack.OFFSET);
    }

    public static void handlerClientCombustionFuels(HashSet<CombustionFuelSource> fuels) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }
        CombustionFuelRegister.INSTANCE.setClientValues(fuels);
    }

    public static void handlerClientCoalGenFuels(HashSet<Item> fuels) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }
        CoalGeneratorFuelRegister.INSTANCE.setClientValues(fuels);
    }

    public static void handlerSetGuidebookInitFlag() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel world = minecraft.level;
        if (world == null || minecraft.player == null) {
            return;
        }
        ScreenGuidebook.setInitNotHappened();
    }

    public static void handlerClientThermoGenHeatSources(HashMap<Fluid, Double> heatSources) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }
        ThermoelectricGeneratorHeatRegister.INSTANCE.setClientValues(heatSources);
    }

    public static void handlerSpawnSmokeParicle(BlockPos pos) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) {
            return;
        }
        world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
    }

    public static void handlerClientGasCollectorCards(HashMap<Item, GasCollectorChromoCardsRegister.AtmosphericResult> results) {
        GasCollectorChromoCardsRegister.INSTANCE.setClientValues(results);
    }

    /*
    public static void handleUpdateSpecificPropertyClient(PropertyWrapper wrapper, BlockPos pos){
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) {
            return;
        }
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IPropertyHolderTile holder) {
            holder.getPropertyManager().update(wrapper.index(), wrapper.value());
        }
    }

     */

}
