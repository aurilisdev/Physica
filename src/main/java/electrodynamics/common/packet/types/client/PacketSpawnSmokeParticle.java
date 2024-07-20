package electrodynamics.common.packet.types.client;

import electrodynamics.common.packet.NetworkHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketSpawnSmokeParticle implements CustomPacketPayload {

    public static final ResourceLocation PACKET_SPAWNSMOKEPARTICLE_PACKETID = NetworkHandler.id("packetspawnsmokeparticle");
    public static final Type<PacketSpawnSmokeParticle> TYPE = new Type<>(PACKET_SPAWNSMOKEPARTICLE_PACKETID);
    public static final StreamCodec<ByteBuf, PacketSpawnSmokeParticle> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, instance0 -> instance0.pos,
            PacketSpawnSmokeParticle::new
    );

    private final BlockPos pos;

    public PacketSpawnSmokeParticle(BlockPos pos) {
        this.pos = pos;
    }

    public static void handle(PacketSpawnSmokeParticle message, IPayloadContext context) {
        ClientBarrierMethods.handlerSpawnSmokeParicle(message.pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}