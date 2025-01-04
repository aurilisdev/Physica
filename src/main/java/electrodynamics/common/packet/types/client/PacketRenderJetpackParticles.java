package electrodynamics.common.packet.types.client;

import java.util.UUID;

import electrodynamics.common.packet.NetworkHandler;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketRenderJetpackParticles implements CustomPacketPayload {

    public static final ResourceLocation PACKET_RENDERJETPACKPARTICLES_PACKETID = NetworkHandler.id("packetrenderjetpackparticles");
    public static final Type<PacketRenderJetpackParticles> TYPE = new Type<>(PACKET_RENDERJETPACKPARTICLES_PACKETID);
    public static final StreamCodec<FriendlyByteBuf, PacketRenderJetpackParticles> CODEC = StreamCodec.composite(

            UUIDUtil.STREAM_CODEC, instance -> instance.player,
            ByteBufCodecs.BOOL, instance -> instance.isCombat,
            PacketRenderJetpackParticles::new
    );

    private final UUID player;
    private final boolean isCombat;

    public PacketRenderJetpackParticles(UUID uuid, boolean combat) {
        player = uuid;
        isCombat = combat;
    }

    public static void handle(PacketRenderJetpackParticles message, IPayloadContext context) {
        ClientBarrierMethods.handleJetpackParticleRendering(message.player, message.isCombat);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
