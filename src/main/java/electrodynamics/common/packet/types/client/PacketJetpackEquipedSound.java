package electrodynamics.common.packet.types.client;

import java.util.UUID;

import electrodynamics.common.packet.NetworkHandler;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketJetpackEquipedSound implements CustomPacketPayload {

	public static final ResourceLocation PACKET_JETPACKEQUIPEDSOUND_PACKETID = NetworkHandler.id("packetjetpackequipedsound");
	public static final Type<PacketJetpackEquipedSound> TYPE = new Type<>(PACKET_JETPACKEQUIPEDSOUND_PACKETID);

	public static final StreamCodec<FriendlyByteBuf, PacketJetpackEquipedSound> CODEC = StreamCodec.composite(

			UUIDUtil.STREAM_CODEC, instance -> instance.player,
			PacketJetpackEquipedSound::new

	);

	private final UUID player;

	public PacketJetpackEquipedSound(UUID uuid) {
		player = uuid;
	}

	public static void handle(PacketJetpackEquipedSound message, IPayloadContext context) {
	    ClientBarrierMethods.handlePacketJetpackEquipedSound(message.player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
