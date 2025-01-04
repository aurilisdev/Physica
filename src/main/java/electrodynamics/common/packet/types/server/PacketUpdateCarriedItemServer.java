package electrodynamics.common.packet.types.server;

import java.util.UUID;

import electrodynamics.common.packet.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketUpdateCarriedItemServer implements CustomPacketPayload {

    public static final ResourceLocation PACKET_UPDATECARRIEDITEMSERVER_PACKETID = NetworkHandler.id("packetupdatecarrieditemserver");
    public static final Type<PacketUpdateCarriedItemServer> TYPE = new Type<>(PACKET_UPDATECARRIEDITEMSERVER_PACKETID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketUpdateCarriedItemServer> CODEC = StreamCodec.composite(
       ItemStack.STREAM_CODEC, instance -> instance.carriedItem,
            BlockPos.STREAM_CODEC, instance -> instance.tilePos,
            UUIDUtil.STREAM_CODEC, instance -> instance.playerId,
            PacketUpdateCarriedItemServer::new
    );

    private final ItemStack carriedItem;
    private final BlockPos tilePos;
    private final UUID playerId;

    public PacketUpdateCarriedItemServer(ItemStack carriedItem, BlockPos tilePos, UUID playerId) {
        this.carriedItem = carriedItem;
        this.tilePos = tilePos;
        this.playerId = playerId;
    }

    public static void handle(PacketUpdateCarriedItemServer message, IPayloadContext context) {
        ServerBarrierMethods.handleUpdateCarriedItemServer(context.player().level(), message.carriedItem, message.tilePos, message.playerId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
