package electrodynamics.common.packet.types.server;

import java.util.UUID;

import electrodynamics.api.item.IItemElectric;
import electrodynamics.api.tile.IPacketServerUpdateTile;
import electrodynamics.common.item.gear.armor.types.ItemNightVisionGoggles;
import electrodynamics.common.packet.NetworkHandler;
import electrodynamics.common.tile.electricitygrid.generators.TileCreativePowerSource;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.IPropertyHolderTile;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ServerBarrierMethods {

    public static void handleJetpackFlightServer(Level level, UUID playerId, boolean bool, double prevDeltaY) {
        ServerLevel world = (ServerLevel) level;
        Player player = world.getPlayerByUUID(playerId);
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (ItemUtils.testItems(chest.getItem(), ElectrodynamicsItems.ITEM_JETPACK.get()) || ItemUtils.testItems(chest.getItem(), ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get())) {
            chest.set(ElectrodynamicsDataComponentTypes.USED, bool);
            chest.set(ElectrodynamicsDataComponentTypes.HURT, false);
            chest.set(ElectrodynamicsDataComponentTypes.DELTA_Y, prevDeltaY);
        }
    }

    public static void handleModeSwitchServer(Level world, UUID playerId, PacketModeSwitchServer.Mode mode) {
        ServerLevel serverWorld = (ServerLevel) world;
        if (serverWorld == null) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer) serverWorld.getPlayerByUUID(playerId);
        switch (mode) {
            case JETPACK:
                ItemStack chest = serverPlayer.getItemBySlot(EquipmentSlot.CHEST);
                if (ItemUtils.testItems(chest.getItem(), ElectrodynamicsItems.ITEM_JETPACK.get()) || ItemUtils.testItems(chest.getItem(), ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get())) {
                    int curMode = chest.getOrDefault(ElectrodynamicsDataComponentTypes.MODE, 0);
                    if (curMode < 3) {
                        curMode++;
                    } else {
                        curMode = 0;
                    }
                    chest.set(ElectrodynamicsDataComponentTypes.MODE, curMode);
                    serverPlayer.playNotifySound(ElectrodynamicsSounds.SOUND_JETPACKSWITCHMODE.get(), SoundSource.PLAYERS, 1, 1);
                }
                break;
            case SERVOLEGS:
                ItemStack legs = serverPlayer.getItemBySlot(EquipmentSlot.LEGS);
                if (ItemUtils.testItems(legs.getItem(), ElectrodynamicsItems.ITEM_SERVOLEGGINGS.get()) || ItemUtils.testItems(legs.getItem(), ElectrodynamicsItems.ITEM_COMBATLEGGINGS.get())) {
                    int curMode = legs.getOrDefault(ElectrodynamicsDataComponentTypes.MODE, 0);
                    if (curMode < 2) {
                        curMode++;
                    } else {
                        curMode = 0;
                    }
                    legs.set(ElectrodynamicsDataComponentTypes.MODE, curMode);
                    serverPlayer.playNotifySound(ElectrodynamicsSounds.SOUND_HYDRAULICBOOTS.get(), SoundSource.PLAYERS, 1, 1);
                }
                break;
            default:
                break;
        }
    }

    public static void handlePlayerInformation(String name, String message) {
        NetworkHandler.playerInformation.put(name, message);
    }

    public static void handlePacketPowerSetting(int voltage, int power, BlockPos pos, Level level) {
        ServerLevel world = (ServerLevel) level;
        if (world == null) {
            return;
        }
        TileCreativePowerSource tile = (TileCreativePowerSource) world.getBlockEntity(pos);
        if (tile == null) {
            return;
        }
        tile.voltage.set(voltage);
        tile.power.set(power);
    }

    public static void handleSendUpdatePropertiesServer(Level level, BlockPos tilePos, CompoundTag data, int index) {
        ServerLevel world = (ServerLevel) level;
        if (world == null) {
            return;
        }
        BlockEntity tile = world.getBlockEntity(tilePos);
        if (tile instanceof IPropertyHolderTile holder) {
            holder.getPropertyManager().loadDataFromClient(index, data);
        }
    }

    public static void handleServerUpdateTile(Level level, BlockPos target, CompoundTag nbt) {
        ServerLevel world = (ServerLevel) level;
        if (world == null) {
            return;
        }
        BlockEntity tile = world.getBlockEntity(target);
        if (tile instanceof IPacketServerUpdateTile serv) {
            serv.readCustomUpdate(nbt);
        }
    }

    public static void handleSwapBattery(Level level, UUID playerId) {
        ServerLevel world = (ServerLevel) level;
        if (world == null) {
            return;
        }
        Player player = world.getPlayerByUUID(playerId);
        ItemStack handItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!handItem.isEmpty() && handItem.getItem() instanceof IItemElectric electric) {
            electric.swapBatteryPackFirstItem(handItem, player);
        }
    }

    public static void handleToogleOnServer(Level level, UUID playerId, PacketToggleOnServer.Type type) {
        ServerLevel world = (ServerLevel) level;
        if (world == null) {
            return;
        }
        Player player = world.getPlayerByUUID(playerId);
        switch (type) {
            case NVGS:
                ItemStack playerHead = player.getItemBySlot(EquipmentSlot.HEAD);
                if (ItemUtils.testItems(playerHead.getItem(), ElectrodynamicsItems.ITEM_NIGHTVISIONGOGGLES.get()) || ItemUtils.testItems(playerHead.getItem(), ElectrodynamicsItems.ITEM_COMBATHELMET.get())) {
                    playerHead.set(ElectrodynamicsDataComponentTypes.ON, !playerHead.getOrDefault(ElectrodynamicsDataComponentTypes.ON, false));
                    if (((IItemElectric) playerHead.getItem()).getJoulesStored(playerHead) >= ItemNightVisionGoggles.JOULES_PER_TICK) {
                        if (playerHead.getOrDefault(ElectrodynamicsDataComponentTypes.ON, false)) {
                            player.playNotifySound(ElectrodynamicsSounds.SOUND_NIGHTVISIONGOGGLESON.get(), SoundSource.PLAYERS, 1, 1);
                        } else {
                            player.playNotifySound(ElectrodynamicsSounds.SOUND_NIGHTVISIONGOGGLESOFF.get(), SoundSource.PLAYERS, 1, 1);
                        }
                    }
                }
                break;
            case SERVOLEGGINGS:
                ItemStack playerLegs = player.getItemBySlot(EquipmentSlot.LEGS);
                if (ItemUtils.testItems(playerLegs.getItem(), ElectrodynamicsItems.ITEM_SERVOLEGGINGS.get()) || ItemUtils.testItems(playerLegs.getItem(), ElectrodynamicsItems.ITEM_COMBATLEGGINGS.get())) {
                    playerLegs.set(ElectrodynamicsDataComponentTypes.ON, !playerLegs.getOrDefault(ElectrodynamicsDataComponentTypes.ON, false));
                    player.playNotifySound(ElectrodynamicsSounds.SOUND_JETPACKSWITCHMODE.get(), SoundSource.PLAYERS, 1, 1);
                }
                break;
            default:
                break;
        }
    }

    public static void handleUpdateCarriedItemServer(Level level, ItemStack carriedItem, BlockPos tilePos, UUID playerId) {
        ServerLevel world = (ServerLevel) level;
        if (world == null) {
            return;
        }
        GenericTile tile = (GenericTile) world.getBlockEntity(tilePos);
        if (tile != null) {
            tile.updateCarriedItemInContainer(carriedItem, playerId);
        }
    }

    public static void handleSeismicScanner(Level level, UUID playerId, PacketSeismicScanner.Type mode, int scannerMode, int hand) {
        ServerLevel world = (ServerLevel) level;
        if (world == null) {
            return;
        }
        Player player = world.getPlayerByUUID(playerId);

        ItemStack stack = player.getItemInHand(InteractionHand.values()[hand]);

        if (stack.isEmpty()) {
            return;
        }

        switch (mode) {

            case manualping:
                stack.set(ElectrodynamicsDataComponentTypes.USED, true);

                break;
            case switchsonarmode:
                stack.set(ElectrodynamicsDataComponentTypes.ENUM, scannerMode);


                break;

        }


    }
}
