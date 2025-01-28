package electrodynamics.common.packet.types.client;

import java.util.HashMap;
import java.util.Map;

import electrodynamics.api.gas.GasStack;
import electrodynamics.common.packet.NetworkHandler;
import electrodynamics.common.reloadlistener.GasCollectorChromoCardsRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketSetClientGasCollectorCards implements CustomPacketPayload {

    public static final ResourceLocation PACKET_SETCLIENTGASCOLLECTORCARDS_PACKETID = NetworkHandler.id("packetsetclientgascollectorcards");
    public static final Type<PacketSetClientGasCollectorCards> TYPE = new Type<>(PACKET_SETCLIENTGASCOLLECTORCARDS_PACKETID);

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSetClientGasCollectorCards> CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketSetClientGasCollectorCards packet) {
            buf.writeInt(packet.results.size());
            for (Map.Entry<Item, GasCollectorChromoCardsRegister.AtmosphericResult> entry : packet.results.entrySet()) {
                ItemStack.STREAM_CODEC.encode(buf, new ItemStack(entry.getKey()));
                GasStack.STREAM_CODEC.encode(buf, entry.getValue().stack());
                if(entry.getValue().biome() != null || entry.getValue().biomeTag() != null){
                    buf.writeBoolean(true);
                    if(entry.getValue().biome() != null) {
                        buf.writeBoolean(true);
                        buf.writeResourceLocation(entry.getValue().biome().location());
                    } else {
                        buf.writeBoolean(false);
                        buf.writeResourceLocation(entry.getValue().biomeTag().location());
                    }
                } else {
                    buf.writeBoolean(false);
                }
            }
        }

        @Override
        public PacketSetClientGasCollectorCards decode(RegistryFriendlyByteBuf buf) {
            int count = buf.readInt();
            HashMap<Item, GasCollectorChromoCardsRegister.AtmosphericResult> results = new HashMap<>();
            for (int i = 0; i < count; i++) {
                Item item = ItemStack.STREAM_CODEC.decode(buf).getItem();
                GasStack stack = GasStack.STREAM_CODEC.decode(buf);
                ResourceKey<Biome> biome = null;
                TagKey<Biome> biomeTag = null;
                if(!buf.readBoolean()) {
                    biome = null;
                    biomeTag = null;
                } else {
                    if(buf.readBoolean()) {
                        biome = ResourceKey.create(Registries.BIOME, buf.readResourceLocation());
                    } else {
                        biomeTag = TagKey.create(Registries.BIOME, buf.readResourceLocation());
                    }
                }
                results.put(item, new GasCollectorChromoCardsRegister.AtmosphericResult(stack, biome, biomeTag));
            }
            return new PacketSetClientGasCollectorCards(results);
        }
    };

    private final HashMap<Item, GasCollectorChromoCardsRegister.AtmosphericResult> results;

    public PacketSetClientGasCollectorCards(HashMap<Item, GasCollectorChromoCardsRegister.AtmosphericResult> results) {
        this.results = results;
    }

    public static void handle(PacketSetClientGasCollectorCards message, IPayloadContext context) {
        ClientBarrierMethods.handlerClientGasCollectorCards(message.results);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
