package electrodynamics.common.packet.types.server;

import java.util.UUID;

import electrodynamics.common.packet.NetworkHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketSwapBattery implements CustomPacketPayload {

    public static final ResourceLocation PACKET_SWAPBATTER_PACKETID = NetworkHandler.id("packetswapbattery");
    public static final Type<PacketSwapBattery> TYPE = new Type<>(PACKET_SWAPBATTER_PACKETID);
    public static final StreamCodec<ByteBuf, PacketSwapBattery> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, instance -> instance.playerId,
            PacketSwapBattery::new
    );

    private final UUID playerId;

    public PacketSwapBattery(UUID uuid) {
        playerId = uuid;
    }

    public static void handle(PacketSwapBattery message, IPayloadContext context) {
        ServerBarrierMethods.handleSwapBattery(context.player().level(), message.playerId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
