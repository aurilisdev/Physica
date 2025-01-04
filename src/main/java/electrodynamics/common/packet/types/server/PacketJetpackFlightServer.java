package electrodynamics.common.packet.types.server;

import java.util.UUID;

import electrodynamics.common.packet.NetworkHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketJetpackFlightServer implements CustomPacketPayload {

    public static final ResourceLocation PACKET_JETPACKFLIGHTSERVER_PACKETID = NetworkHandler.id("packetjetpackflightserver");
    public static final Type<PacketJetpackFlightServer> TYPE = new Type<>(PACKET_JETPACKFLIGHTSERVER_PACKETID);
    public static final StreamCodec<ByteBuf, PacketJetpackFlightServer> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, instance -> instance.playerId,
            ByteBufCodecs.BOOL, instance -> instance.bool,
            ByteBufCodecs.DOUBLE, instance -> instance.prevDeltaY,
            PacketJetpackFlightServer::new


    );
    private final UUID playerId;
    private final boolean bool;
    private final double prevDeltaY;

    public PacketJetpackFlightServer(UUID uuid, boolean bool, double prevDeltaY) {
        playerId = uuid;
        this.bool = bool;
        this.prevDeltaY = prevDeltaY;
    }

    public static void handle(PacketJetpackFlightServer message, IPayloadContext context) {
        ServerBarrierMethods.handleJetpackFlightServer(context.player().level(), message.playerId, message.bool, message.prevDeltaY);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
