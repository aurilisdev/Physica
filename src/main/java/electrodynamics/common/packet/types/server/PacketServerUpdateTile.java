package electrodynamics.common.packet.types.server;

import electrodynamics.common.packet.NetworkHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketServerUpdateTile implements CustomPacketPayload {

    public static final ResourceLocation PACKET_SERVERUPDATETILE_PACKETID = NetworkHandler.id("packetserverupdatetile");
    public static final Type<PacketServerUpdateTile> TYPE = new Type<>(PACKET_SERVERUPDATETILE_PACKETID);
    public static final StreamCodec<ByteBuf, PacketServerUpdateTile> CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(CompoundTag.CODEC), instance -> instance.nbt,
            BlockPos.STREAM_CODEC, instance -> instance.target,
            PacketServerUpdateTile::new
    );
    private final BlockPos target;
    private final CompoundTag nbt;

    public PacketServerUpdateTile(CompoundTag nbt, BlockPos target) {
        this.nbt = nbt;
        this.target = target;
    }

    public static void handle(PacketServerUpdateTile message, IPayloadContext context) {
        ServerBarrierMethods.handleServerUpdateTile(context.player().level(), message.target, message.nbt);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}