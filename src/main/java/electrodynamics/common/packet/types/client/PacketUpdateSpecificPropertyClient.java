package electrodynamics.common.packet.types.client;

import electrodynamics.common.packet.NetworkHandler;
import electrodynamics.prefab.properties.IPropertyType;
import electrodynamics.prefab.properties.PropertyManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketUpdateSpecificPropertyClient implements CustomPacketPayload {

    public static final ResourceLocation PACKET_UPDATESPECIFICPROPERTYCLIENT_PACKETID = NetworkHandler.id("packetupdatespecificpropertyclient");
    public static final Type<PacketUpdateSpecificPropertyClient> TYPE = new Type<>(PACKET_UPDATESPECIFICPROPERTYCLIENT_PACKETID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketUpdateSpecificPropertyClient> CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketUpdateSpecificPropertyClient packet) {
            /*
            buf.writeBlockPos(packet.pos);
            buf.writeInt(packet.value.index());
            buf.writeResourceLocation(packet.value.type().getId());
            packet.value.type().getPacketCodec().encode(buf, packet.value.property().get());

             */
        }

        @Override
        public PacketUpdateSpecificPropertyClient decode(RegistryFriendlyByteBuf buf) {
            /*
            BlockPos pos = buf.readBlockPos();
            int index = buf.readInt();
            IPropertyType type = PropertyManager.REGISTERED_PROPERTIES.get(buf.readResourceLocation());
            return new PacketUpdateSpecificPropertyClient(new PropertyManager.PropertyWrapper(index, type, type.getPacketCodec().decode(buf), null), pos);

             */
            return null;
        }
    };

    private final BlockPos pos;
    //private final PropertyManager.PropertyWrapper value;

    public PacketUpdateSpecificPropertyClient(/*PropertyManager.PropertyWrapper wrapper, */BlockPos pos) {
        //value = wrapper;
        this.pos = pos;
    }

    public static void handle(PacketUpdateSpecificPropertyClient packet, IPayloadContext context){
        //ClientBarrierMethods.handleUpdateSpecificPropertyClient(packet.value, packet.pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
