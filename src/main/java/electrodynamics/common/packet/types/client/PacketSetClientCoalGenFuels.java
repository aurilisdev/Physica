package electrodynamics.common.packet.types.client;

import java.util.HashSet;

import electrodynamics.common.packet.NetworkHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketSetClientCoalGenFuels implements CustomPacketPayload {

    public static final ResourceLocation PACKET_SETCLIENTCOALGENFUELS_PACKETID = NetworkHandler.id("packetsetclientcoalgenfuels");
    public static final Type<PacketSetClientCoalGenFuels> TYPE = new Type<>(PACKET_SETCLIENTCOALGENFUELS_PACKETID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSetClientCoalGenFuels> CODEC = new StreamCodec<RegistryFriendlyByteBuf, PacketSetClientCoalGenFuels>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketSetClientCoalGenFuels packet) {
            buf.writeInt(packet.fuels.size());
            for (Item item : packet.fuels) {
                ItemStack.STREAM_CODEC.encode(buf, new ItemStack(item));
            }
        }

        @Override
        public PacketSetClientCoalGenFuels decode(RegistryFriendlyByteBuf buf) {
            int count = buf.readInt();
            HashSet<Item> fuels = new HashSet<>();
            for (int i = 0; i < count; i++) {
                fuels.add(ItemStack.STREAM_CODEC.decode(buf).getItem());
            }
            return new PacketSetClientCoalGenFuels(fuels);
        }
    };
    private final HashSet<Item> fuels;

    public PacketSetClientCoalGenFuels(HashSet<Item> fuels) {
        this.fuels = fuels;
    }

    public static void handle(PacketSetClientCoalGenFuels message, IPayloadContext context) {
        ClientBarrierMethods.handlerClientCoalGenFuels(message.fuels);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
