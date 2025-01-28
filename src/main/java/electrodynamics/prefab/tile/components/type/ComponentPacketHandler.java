package electrodynamics.prefab.tile.components.type;

import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponent;
import electrodynamics.prefab.tile.components.IComponentType;

//Defunct class should be removed at some point
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

    /*
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

     */

    @Override
    public IComponentType getType() {
        return IComponentType.PacketHandler;
    }
}
