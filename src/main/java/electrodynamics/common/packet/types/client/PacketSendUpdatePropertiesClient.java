package electrodynamics.common.packet.types.client;

import electrodynamics.common.packet.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketSendUpdatePropertiesClient implements CustomPacketPayload {

    public static final ResourceLocation PACKET_SENDUPDATEPROPERTIESCLIENT_PACKETID = NetworkHandler.id("packetsendupdatepropertiesclient");
    public static final Type<PacketSendUpdatePropertiesClient> TYPE = new Type<>(PACKET_SENDUPDATEPROPERTIESCLIENT_PACKETID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSendUpdatePropertiesClient> CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketSendUpdatePropertiesClient packet) {
            /*
            buf.writeBlockPos(packet.pos);
            buf.writeInt(packet.values.size());
            packet.values.forEach(wrapper -> {
                buf.writeInt(wrapper.index());
                buf.writeResourceLocation(wrapper.type().getId());
                wrapper.type().getPacketCodec().encode(buf, wrapper.property().get());
            });

             */
        }

        @Override
        public PacketSendUpdatePropertiesClient decode(RegistryFriendlyByteBuf buf) {
            /*
            BlockPos pos = buf.readBlockPos();

            int size = buf.readInt();
            HashSet<PropertyWrapper> properties = new HashSet<>();

            int index;
            IPropertyType propertyType;

            for (int i = 0; i < size; i++) {

                index = buf.readInt();
                propertyType = PropertyManager.REGISTERED_PROPERTIES.get(buf.readResourceLocation());
                properties.add(new PropertyWrapper(index, propertyType, propertyType.getPacketCodec().decode(buf), null));

            }
            return new PacketSendUpdatePropertiesClient(pos, properties);

             */
            return null;
        }
    };

    private final BlockPos pos;
    //private final HashSet<PropertyWrapper> values;

    /*
    public PacketSendUpdatePropertiesClient(IPropertyHolderTile tile) {
        this(tile.getTile().getBlockPos(), tile.getPropertyManager().getClientUpdateProperties());
    }

     */

    public PacketSendUpdatePropertiesClient(BlockPos pos/*, HashSet<PropertyWrapper> dirtyProperties*/) {
        this.pos = pos;
        //values = dirtyProperties;
    }

    public static void handle(PacketSendUpdatePropertiesClient message, IPayloadContext context) {
        //ClientBarrierMethods.handlePropertiesUpdateClient(message.pos/*, message.values */) ;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}