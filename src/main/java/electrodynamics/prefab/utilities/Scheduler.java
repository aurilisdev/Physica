package electrodynamics.prefab.utilities;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import electrodynamics.api.References;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.GAME)
public class Scheduler {
	private static ConcurrentHashMap<Runnable, Integer> scheduled = new ConcurrentHashMap<>();

	@SubscribeEvent
	public static void onTick(ServerTickEvent.Post event) {
		if (!scheduled.isEmpty()) {
			Iterator<Entry<Runnable, Integer>> it = scheduled.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Runnable, Integer> next = it.next();
				if (next.getValue() <= 0) {
					next.getKey().run();
					it.remove();
				} else {
					next.setValue(next.getValue() - 1);
				}
			}
		}
	}

	public static void schedule(int timeUntil, Runnable run) {
		scheduled.put(run, timeUntil);
	}
}