package electrodynamics.common.network;

import java.util.*;

import electrodynamics.api.References;
import electrodynamics.api.network.ITickableNetwork;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.GAME)
public class NetworkRegistry {
	private static final HashMap<UUID, ITickableNetwork> NETWORKS = new HashMap<>();
	private static final HashSet<UUID> TO_REMOVE = new HashSet<>();

	public static void register(ITickableNetwork network) {
		NETWORKS.put(network.getId(), network);
	}

	public static void deregister(ITickableNetwork network) {
		if (NETWORKS.containsKey(network.getId())) {
			TO_REMOVE.add(network.getId());
		}
	}

	@SubscribeEvent
	public static void update(ServerTickEvent.Post event) {

		try {
			for(UUID id : TO_REMOVE) {
				NETWORKS.remove(id);
			}
			TO_REMOVE.clear();
            for (ITickableNetwork net : NETWORKS.values()) {
                if (net.getSize() == 0) {
                    deregister(net);
                } else {
                    net.tick();
                }
            }
		} catch (ConcurrentModificationException exception) {
			exception.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void unloadServer(ServerStoppedEvent event) {
		try {
			NETWORKS.clear();
			TO_REMOVE.clear();
		} catch (ConcurrentModificationException exception) {
			exception.printStackTrace();
		}
	}

}