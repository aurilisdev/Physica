package electrodynamics.compatibility.mekanism;

import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class MekanismClientHandler {

    public static void registerMenus(RegisterMenuScreensEvent event) {
        event.register(ElectrodynamicsMenuTypes.CONTAINER_ROTARYUNIFIER.get(), ScreenRotaryUnifier::new);
    }

}
