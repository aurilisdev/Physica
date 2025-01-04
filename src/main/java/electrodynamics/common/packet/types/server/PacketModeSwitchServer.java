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

public class PacketModeSwitchServer implements CustomPacketPayload {

    public static final ResourceLocation PACKET_MODESWITCHSERVER_PACKETID = NetworkHandler.id("packetmodeswitchserver");
    public static final Type<PacketModeSwitchServer> TYPE = new Type<>(PACKET_MODESWITCHSERVER_PACKETID);
    public static final StreamCodec<ByteBuf, PacketModeSwitchServer> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, instance -> instance.playerId,
            ByteBufCodecs.INT, instance -> instance.mode.ordinal(),
            (id, mode) -> new PacketModeSwitchServer(id, Mode.values()[mode])
    );

    private final UUID playerId;
    private final Mode mode;

    public PacketModeSwitchServer(UUID uuid, Mode mode) {
        playerId = uuid;
        this.mode = mode;
    }

    public static void handle(PacketModeSwitchServer message, IPayloadContext context) {
        ServerBarrierMethods.handleModeSwitchServer(context.player().level(), message.playerId, message.mode);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Mekanism gave me this idea
    public enum Mode {
        JETPACK,
        SERVOLEGS;
    }

}
