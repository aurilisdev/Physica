package electrodynamics.api.multiblock.assemblybased;

import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.Electrodynamics;
import electrodynamics.api.References;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

/**
 * The data structure defining the multiblock structure based on directions
 *
 * @param nodes
 * @author Skip999
 */
public record Multiblock(Map<Direction, List<MultiblockSlaveNode>> nodes) {

    public static final String FOLDER = "multiblock";
    public static final ResourceKey<Registry<Multiblock>> REGISTRY_KEY = ResourceKey.createRegistryKey(Electrodynamics.rl(FOLDER));

    public static final String MEMBER_FIELD = "members";

    public static final Codec<Multiblock> CODEC = RecordCodecBuilder.create(instance ->

            instance.group(

                    Codec.unboundedMap(Direction.CODEC, MultiblockSlaveNode.CODEC.listOf()).fieldOf(MEMBER_FIELD).forGetter(Multiblock::nodes)

            ).apply(instance, Multiblock::new));

    public static List<MultiblockSlaveNode> getNodes(Level world, ResourceKey<Multiblock> id, Direction facing) {
        return world.registryAccess().lookupOrThrow(Multiblock.REGISTRY_KEY).getOrThrow(id).value().nodes().get(facing);
    }

    public static ResourceKey makeKey(ResourceLocation id) {
        return ResourceKey.create(REGISTRY_KEY, id);
    }

    @EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
    private static final class MultiblockRegistry {

        @SubscribeEvent
        public static void registerMultiblocks(DataPackRegistryEvent.NewRegistry event) {
            event.dataPackRegistry(REGISTRY_KEY, CODEC, CODEC);
        }

    }

}
