package electrodynamics.common.packet.types.client;

import java.util.HashMap;

import electrodynamics.common.packet.NetworkHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketSetClientThermoGenSources implements CustomPacketPayload {

    public static final ResourceLocation PACKET_SETCLIENTTHERMOGENSOURCES_PACKETID = NetworkHandler.id("packetsetclientthermogensources");
    public static final Type<PacketSetClientThermoGenSources> TYPE = new Type<>(PACKET_SETCLIENTTHERMOGENSOURCES_PACKETID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSetClientThermoGenSources> CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketSetClientThermoGenSources packet) {
            buf.writeInt(packet.heatSources.size());
            packet.heatSources.forEach((fluid, value) -> {
                FluidStack.STREAM_CODEC.encode(buf, new FluidStack(fluid, 1));
                buf.writeDouble(value);
            });
        }

        @Override
        public PacketSetClientThermoGenSources decode(RegistryFriendlyByteBuf buf) {
            int count = buf.readInt();
            HashMap<Fluid, Double> values = new HashMap<>();
            for (int i = 0; i < count; i++) {
                values.put(FluidStack.STREAM_CODEC.decode(buf).getFluid(), buf.readDouble());
            }
            return new PacketSetClientThermoGenSources(values);
        }
    };

    private final HashMap<Fluid, Double> heatSources;

    public PacketSetClientThermoGenSources(HashMap<Fluid, Double> heatSources) {
        this.heatSources = heatSources;
    }

    public static void handle(PacketSetClientThermoGenSources message, IPayloadContext context) {
        ClientBarrierMethods.handlerClientThermoGenHeatSources(message.heatSources);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
