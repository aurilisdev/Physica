package electrodynamics.common.packet;

import java.util.HashMap;

import electrodynamics.api.References;
import electrodynamics.common.packet.types.client.PacketAddClientRenderInfo;
import electrodynamics.common.packet.types.client.PacketJetpackEquipedSound;
import electrodynamics.common.packet.types.client.PacketRenderJetpackParticles;
import electrodynamics.common.packet.types.client.PacketResetGuidebookPages;
import electrodynamics.common.packet.types.client.PacketSendUpdatePropertiesClient;
import electrodynamics.common.packet.types.client.PacketSetClientCoalGenFuels;
import electrodynamics.common.packet.types.client.PacketSetClientCombustionFuel;
import electrodynamics.common.packet.types.client.PacketSetClientThermoGenSources;
import electrodynamics.common.packet.types.client.PacketSpawnSmokeParticle;
import electrodynamics.common.packet.types.server.PacketJetpackFlightServer;
import electrodynamics.common.packet.types.server.PacketModeSwitchServer;
import electrodynamics.common.packet.types.server.PacketPlayerInformation;
import electrodynamics.common.packet.types.server.PacketPowerSetting;
import electrodynamics.common.packet.types.server.PacketSendUpdatePropertiesServer;
import electrodynamics.common.packet.types.server.PacketServerUpdateTile;
import electrodynamics.common.packet.types.server.PacketSwapBattery;
import electrodynamics.common.packet.types.server.PacketToggleOnServer;
import electrodynamics.common.packet.types.server.PacketUpdateCarriedItemServer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {

    public static HashMap<String, String> playerInformation = new HashMap<>();
    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void registerPackets(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registry = event.registrar(References.ID).versioned(PROTOCOL_VERSION).optional();

        // CLIENT

        registry.playToClient(PacketAddClientRenderInfo.TYPE, PacketAddClientRenderInfo.CODEC, (packet, context) -> PacketAddClientRenderInfo.handle(packet, context));
        registry.playToClient(PacketJetpackEquipedSound.TYPE, PacketJetpackEquipedSound.CODEC, (packet, context) -> PacketJetpackEquipedSound.handle(packet, context));
        registry.playToClient(PacketRenderJetpackParticles.TYPE, PacketRenderJetpackParticles.CODEC, (packet, context) -> PacketRenderJetpackParticles.handle(packet, context));
        registry.playToClient(PacketResetGuidebookPages.TYPE, PacketResetGuidebookPages.CODEC, (packet, context) -> PacketResetGuidebookPages.handle(packet, context));
        registry.playToClient(PacketSendUpdatePropertiesClient.TYPE, PacketSendUpdatePropertiesClient.CODEC, (packet, context) -> PacketSendUpdatePropertiesClient.handle(packet, context));
        registry.playToClient(PacketSetClientCoalGenFuels.TYPE, PacketSetClientCoalGenFuels.CODEC, (packet, context) -> PacketSetClientCoalGenFuels.handle(packet, context));
        registry.playToClient(PacketSetClientCombustionFuel.TYPE, PacketSetClientCombustionFuel.CODEC, (packet, context) -> PacketSetClientCombustionFuel.handle(packet, context));
        registry.playToClient(PacketSetClientThermoGenSources.TYPE, PacketSetClientThermoGenSources.CODEC, (packet, context) -> PacketSetClientThermoGenSources.handle(packet, context));
        registry.playToClient(PacketSpawnSmokeParticle.TYPE, PacketSpawnSmokeParticle.CODEC, (packet, context) -> PacketSpawnSmokeParticle.handle(packet, context));

        // SERVER

        registry.playToServer(PacketJetpackFlightServer.TYPE, PacketJetpackFlightServer.CODEC, (packet, context) -> PacketJetpackFlightServer.handle(packet, context));
        registry.playToServer(PacketModeSwitchServer.TYPE, PacketModeSwitchServer.CODEC, (packet, context) -> PacketModeSwitchServer.handle(packet, context));
        registry.playToServer(PacketPlayerInformation.TYPE, PacketPlayerInformation.CODEC, (packet, context) -> PacketPlayerInformation.handle(packet, context));
        registry.playToServer(PacketPowerSetting.TYPE, PacketPowerSetting.CODEC, (packet, context) -> PacketPowerSetting.handle(packet, context));
        registry.playToServer(PacketSendUpdatePropertiesServer.TYPE, PacketSendUpdatePropertiesServer.CODEC, (packet, context) -> PacketSendUpdatePropertiesServer.handle(packet, context));
        registry.playToServer(PacketServerUpdateTile.TYPE, PacketServerUpdateTile.CODEC, (packet, context) -> PacketServerUpdateTile.handle(packet, context));
        registry.playToServer(PacketSwapBattery.TYPE, PacketSwapBattery.CODEC, (packet, context) -> PacketSwapBattery.handle(packet, context));
        registry.playToServer(PacketToggleOnServer.TYPE, PacketToggleOnServer.CODEC, (packet, server) -> PacketToggleOnServer.handle(packet, server));
        registry.playToServer(PacketUpdateCarriedItemServer.TYPE, PacketUpdateCarriedItemServer.CODEC, (packet, context) -> PacketUpdateCarriedItemServer.handle(packet, context));

    }

    public static String getPlayerInformation(String username) {
        return playerInformation.getOrDefault(username, "No Information");
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(References.ID, name);
    }


}
