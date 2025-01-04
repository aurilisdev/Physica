package electrodynamics.common.packet.types.client;

import java.util.HashSet;

import electrodynamics.common.packet.NetworkHandler;
import electrodynamics.prefab.utilities.object.CombustionFuelSource;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketSetClientCombustionFuel implements CustomPacketPayload {

    public static final ResourceLocation PACKET_SETCLIENTCOMBUSTIONFUEL_PACKETID = NetworkHandler.id("packetsetclientcombusionfuel");
    public static final Type<PacketSetClientCombustionFuel> TYPE = new Type<>(PACKET_SETCLIENTCOMBUSTIONFUEL_PACKETID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSetClientCombustionFuel> CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketSetClientCombustionFuel packet) {
            buf.writeInt(packet.fuels.size());
            for (CombustionFuelSource source : packet.fuels) {
                source.writeToBuffer(buf);
            }
        }

        @Override
        public PacketSetClientCombustionFuel decode(RegistryFriendlyByteBuf buf) {
            int count = buf.readInt();
            HashSet<CombustionFuelSource> fuels = new HashSet<>();
            for (int i = 0; i < count; i++) {
                fuels.add(CombustionFuelSource.readFromBuffer(buf));
            }
            return new PacketSetClientCombustionFuel(fuels);
        }
    };

    private final HashSet<CombustionFuelSource> fuels;

    public PacketSetClientCombustionFuel(HashSet<CombustionFuelSource> fuels) {
        this.fuels = fuels;
    }

    public static void handle(PacketSetClientCombustionFuel message, IPayloadContext context) {
        ClientBarrierMethods.handlerClientCombustionFuels(message.fuels);
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
