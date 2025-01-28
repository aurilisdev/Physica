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

public class PacketSeismicScanner implements CustomPacketPayload {

    public static final ResourceLocation PACKET_SEISMICSCANNER_PACKETID = NetworkHandler.id("packetseismicscanner");
    public static final CustomPacketPayload.Type<PacketSeismicScanner> TYPE = new CustomPacketPayload.Type<>(PACKET_SEISMICSCANNER_PACKETID);
    public static final StreamCodec<ByteBuf, PacketSeismicScanner> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, instance -> instance.playerId,
            ByteBufCodecs.INT, instance -> instance.mode.ordinal(),
            ByteBufCodecs.INT, instance -> instance.scannerMode,
            ByteBufCodecs.INT, instance -> instance.hand,
            (id, mode, scannermode, hand) -> new PacketSeismicScanner(id, PacketSeismicScanner.Type.values()[mode], scannermode, hand)
    );

    private final UUID playerId;
    private final PacketSeismicScanner.Type mode;
    private final int scannerMode;

    private final int hand;

    public PacketSeismicScanner(UUID uuid, PacketSeismicScanner.Type mode, int scannerMode, int hand) {
        playerId = uuid;
        this.mode = mode;
        this.scannerMode = scannerMode;
        this.hand = hand;
    }

    public static void handle(PacketSeismicScanner message, IPayloadContext context) {
        ServerBarrierMethods.handleSeismicScanner(context.player().level(), message.playerId, message.mode, message.scannerMode, message.hand);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    public static enum Type {

        manualping, switchsonarmode;

    }
}
