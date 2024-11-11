package electrodynamics.common.event.types.living.incomingdamage;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public abstract class AbstractIncomingDamageHandler {

    public abstract void handle(LivingIncomingDamageEvent event);
}
