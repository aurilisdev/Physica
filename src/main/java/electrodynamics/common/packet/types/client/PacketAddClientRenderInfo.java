package electrodynamics.common.packet.types.client;

import java.util.UUID;

import electrodynamics.common.packet.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketAddClientRenderInfo implements CustomPacketPayload {

    public static final ResourceLocation PACKET_ADDCLIENTRENDERINFO_PACKETID = NetworkHandler.id("packetaddclientrenderinfo");
    public static final Type<PacketAddClientRenderInfo> TYPE = new Type<>(PACKET_ADDCLIENTRENDERINFO_PACKETID);

    public static final StreamCodec<FriendlyByteBuf, PacketAddClientRenderInfo> CODEC = StreamCodec.composite(

            UUIDUtil.STREAM_CODEC, instance0 -> instance0.playerId,
            BlockPos.STREAM_CODEC, instance0 -> instance0.pos,
            PacketAddClientRenderInfo::new

    );
    private final UUID playerId;
    private final BlockPos pos;

    public PacketAddClientRenderInfo(UUID uuid, BlockPos pos) {
        playerId = uuid;
        this.pos = pos;
    }

    public static void handle(PacketAddClientRenderInfo message, IPayloadContext context) {
        ClientBarrierMethods.handleAddClientRenderInfo(message.playerId, message.pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
