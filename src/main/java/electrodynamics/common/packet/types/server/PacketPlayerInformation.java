package electrodynamics.common.packet.types.server;

import java.util.List;

import electrodynamics.common.packet.NetworkHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketPlayerInformation implements CustomPacketPayload {

    public static final ResourceLocation PACKET_PLAYERINFORMATION_PACKETID = NetworkHandler.id("packetplayerinformation");
    public static final Type<PacketPlayerInformation> TYPE = new Type<>(PACKET_PLAYERINFORMATION_PACKETID);
    public static final StreamCodec<ByteBuf, PacketPlayerInformation> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, instance -> instance.information,
            PacketPlayerInformation::new
    );

    private String information;

    public PacketPlayerInformation() {
        String actual = "";
        List<ModInfo> total = FMLLoader.getLoadingModList().getMods();
        for (ModInfo info : total) {
            actual += info.getModId() + ":";
        }
        for (Pack pack : Minecraft.getInstance().getResourcePackRepository().getAvailablePacks()) {
            actual += pack.getId() + "," + pack.getTitle().getString() + "," + pack.getDescription() + ":";
        }
        information = actual;
    }

    public PacketPlayerInformation(String info) {
        information = info;
    }

    public static void handle(PacketPlayerInformation message, IPayloadContext context) {
        ServerBarrierMethods.handlePlayerInformation(context.channelHandlerContext().name(), message.information);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}