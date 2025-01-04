package electrodynamics.common.event.types.living.livingdamage;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public abstract class AbstractLivingDamageHandler {

	public abstract void handle(LivingDamageEvent.Pre event);

}
