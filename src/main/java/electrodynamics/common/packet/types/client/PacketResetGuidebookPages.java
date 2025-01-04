package electrodynamics.common.packet.types.client;

import electrodynamics.common.packet.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketResetGuidebookPages implements CustomPacketPayload {

    public static final PacketResetGuidebookPages PACKET = new PacketResetGuidebookPages();
    public static final ResourceLocation PACKET_RESETGUIDEBOOKPAGES_PACKETID = NetworkHandler.id("packetresetguidebookpages");
    public static final Type<PacketResetGuidebookPages> TYPE = new Type<>(PACKET_RESETGUIDEBOOKPAGES_PACKETID);
    public static final StreamCodec<FriendlyByteBuf, PacketResetGuidebookPages> CODEC = StreamCodec.unit(PACKET);

    public static void handle(PacketResetGuidebookPages message, IPayloadContext context) {
        ClientBarrierMethods.handlerSetGuidebookInitFlag();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
