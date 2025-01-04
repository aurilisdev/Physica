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

public class PacketToggleOnServer implements CustomPacketPayload {

    public static final ResourceLocation PACKET_TOGGLEONSERVER_PACKETID = NetworkHandler.id("packettoggleonserver");

    public static final CustomPacketPayload.Type<PacketToggleOnServer> TYPE = new CustomPacketPayload.Type<>(PACKET_TOGGLEONSERVER_PACKETID);
    public static final StreamCodec<ByteBuf, PacketToggleOnServer> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, instance -> instance.playerId,
            ByteBufCodecs.INT, instance -> instance.type.ordinal(),
            (id, ord) -> new PacketToggleOnServer(id, Type.values()[ord])
    );

    private final UUID playerId;
    private final Type type;

    public PacketToggleOnServer(UUID uuid, Type type) {
        playerId = uuid;
        this.type = type;
    }

    public static void handle(PacketToggleOnServer message, IPayloadContext context) {
        ServerBarrierMethods.handleToogleOnServer(context.player().level(), message.playerId, message.type);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Type {
        NVGS,
        SERVOLEGGINGS;
    }

}
