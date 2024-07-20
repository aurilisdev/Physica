package electrodynamics.prefab.tile.components.type;

import java.util.List;

import electrodynamics.common.packet.types.client.PacketSendUpdatePropertiesClient;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponent;
import electrodynamics.prefab.tile.components.IComponentType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class ComponentPacketHandler implements IComponent {

    private GenericTile holder;

    public ComponentPacketHandler(GenericTile holder) {
        this.holder = holder;
    }

    @Override
    public void holder(GenericTile holder) {
        this.holder = holder;
    }

    @Override
    public GenericTile getHolder() {
        return holder;
    }

    public void sendProperties() {
        Level world = holder.getLevel();

        if (world == null || world.isClientSide || !holder.getPropertyManager().isDirty() || holder.getPropertyManager().getClientUpdateProperties().isEmpty()) {
            return;
        }

        BlockPos pos = holder.getBlockPos();

        if (world instanceof ServerLevel level) {
            List<ServerPlayer> players = level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false);

            if (players.isEmpty()) {
                return;
            }

            PacketSendUpdatePropertiesClient packet = new PacketSendUpdatePropertiesClient(holder);

            players.forEach(p -> PacketDistributor.sendToPlayer(p, packet));

            holder.getPropertyManager().clean();
        }
    }

    @Override
    public IComponentType getType() {
        return IComponentType.PacketHandler;
    }
}
