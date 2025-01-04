package electrodynamics.common.network;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;

import electrodynamics.api.References;
import electrodynamics.api.network.ITickableNetwork;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.GAME)
public class NetworkRegistry {
	private static final HashSet<ITickableNetwork> NETWORKS = new HashSet<>();
	private static final HashSet<ITickableNetwork> TO_REMOVE = new HashSet<>();

	public static void register(ITickableNetwork network) {
		NETWORKS.add(network);
	}

	public static void deregister(ITickableNetwork network) {
		if (NETWORKS.contains(network)) {
			TO_REMOVE.add(network);
		}
	}

	@SubscribeEvent
	public static void update(ServerTickEvent.Post event) {

		try {
			NETWORKS.removeAll(TO_REMOVE);
			TO_REMOVE.clear();
			Iterator<ITickableNetwork> it = NETWORKS.iterator();
			while (it.hasNext()) {
				ITickableNetwork net = it.next();
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