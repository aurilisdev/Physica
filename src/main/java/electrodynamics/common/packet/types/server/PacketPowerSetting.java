package electrodynamics.common.packet.types.server;

import electrodynamics.common.packet.NetworkHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketPowerSetting implements CustomPacketPayload {

    public static final ResourceLocation PACKET_POWERSETTING_PACKETID = NetworkHandler.id("packetpowersetting");
    public static final Type<PacketPowerSetting> TYPE = new Type<>(PACKET_POWERSETTING_PACKETID);
    public static final StreamCodec<ByteBuf, PacketPowerSetting> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, instance -> instance.voltage,
            ByteBufCodecs.INT, instance -> instance.power,
            BlockPos.STREAM_CODEC, instance -> instance.pos,
            PacketPowerSetting::new
    );

    private final int voltage;
    private final int power;

    private final BlockPos pos;

    public PacketPowerSetting(int voltage, int power, BlockPos target) {
        this.voltage = voltage;
        this.power = power;
        pos = target;
    }

    public static void handle(PacketPowerSetting message, IPayloadContext context) {
        ServerBarrierMethods.handlePacketPowerSetting(message.voltage, message.power, message.pos, context.player().level());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
