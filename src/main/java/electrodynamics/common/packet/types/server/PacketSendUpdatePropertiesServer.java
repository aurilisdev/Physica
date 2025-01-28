package electrodynamics.common.packet.types.server;

import electrodynamics.common.packet.NetworkHandler;
//import electrodynamics.prefab.properties.PropertyManager.PropertyWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketSendUpdatePropertiesServer implements CustomPacketPayload {

    public static final ResourceLocation PACKET_SENDUPDATEPROPERTIESSERVER_PACKETID = NetworkHandler.id("packetsendupdatepropertiesserver");
    public static final Type<PacketSendUpdatePropertiesServer> TYPE = new Type<>(PACKET_SENDUPDATEPROPERTIESSERVER_PACKETID);
    public static final StreamCodec<FriendlyByteBuf, PacketSendUpdatePropertiesServer> CODEC = new StreamCodec<>() {

        @Override
        public void encode(FriendlyByteBuf buf, PacketSendUpdatePropertiesServer packet) {
            //buf.writeInt(packet.wrapper.index());
            //buf.writeResourceLocation(packet.wrapper.type().getId());
            //packet.wrapper.type().getPacketCodec().encode(buf, packet.wrapper.value());

            buf.writeNbt(packet.data);
            buf.writeInt(packet.index);
            buf.writeBlockPos(packet.tilePos);

        }

        @Override
        public PacketSendUpdatePropertiesServer decode(FriendlyByteBuf buf) {
            return new PacketSendUpdatePropertiesServer(buf.readNbt(), buf.readInt(), buf.readBlockPos());
            //int index = buf.readInt();
            //IPropertyType type = PropertyManager.REGISTERED_PROPERTIES.get(buf.readResourceLocation());

            //return new PacketSendUpdatePropertiesServer(new PropertyWrapper(index, type, type.getPacketCodec().decode(buf), null), buf.readBlockPos());
        }
    };

    private final BlockPos tilePos;

    private int index;

    private CompoundTag data;
    //private final PropertyWrapper wrapper;

    public PacketSendUpdatePropertiesServer(CompoundTag data, int index, BlockPos tilePos) {
        this.tilePos = tilePos;
        this.index = index;
        this.data = data;
        //wrapper = new PropertyWrapper(property.getIndex(), property.getType(), property.get(), property);
    }

    /*
    public PacketSendUpdatePropertiesServer(PropertyWrapper property, BlockPos tilePos) {
        this.tilePos = tilePos;
        wrapper = property;
    }

     */

    public static void handle(PacketSendUpdatePropertiesServer message, IPayloadContext context) {
        ServerBarrierMethods.handleSendUpdatePropertiesServer(context.player().level(), message.tilePos, message.data, message.index);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
