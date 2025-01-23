package electrodynamics.registers;

import electrodynamics.Electrodynamics;
import electrodynamics.api.References;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsSounds {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, References.ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_HUM = sound("hum");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_MINERALCRUSHER = sound("mineralcrusher");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_MINERALGRINDER = sound("mineralgrinder");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_ELECTRICPUMP = sound("electricpump");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_COMBUSTIONCHAMBER = sound("combustionchamber");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_HYDROELECTRICGENERATOR = sound("hydroelectricgenerator");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_WINDMILL = sound("windmill");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_CERAMICPLATEBREAKING = sound("ceramicplatebreaking");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_CERAMICPLATEADDED = sound("ceramicplateadded");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_RAILGUNKINETIC = sound("railgunkinetic");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_RAILGUNKINETIC_NOAMMO = sound("railgunkinetic_noammo");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_RAILGUNPLASMA_FIRE = sound("railgunplasma_fire");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_RAILGUNPLASMA_HIT = sound("railgunplasma_hit");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_RAILGUNPLASMA_NOPOWER = sound("railgunplasma_nopower");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_RODIMPACTINGGROUND = sound("rodhittingground");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_LATHEPLAYING = sound("latherunning");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_EQUIPHEAVYARMOR = sound("equipheavyarmor");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_SEISMICSCANNER = sound("seismicscanner");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_ELECTROLYTICSEPARATOR = sound("electrolyticseparator");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_NIGHTVISIONGOGGLESON = sound("nightvisiongoggleson");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_NIGHTVISIONGOGGLESOFF = sound("nightvisiongogglesoff");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_HYDRAULICBOOTS = sound("hydraulicboots");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_JETPACKSWITCHMODE = sound("jetpackswitchmode");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_JETPACK = sound("jetpack");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_MOTORRUNNING = sound("motorrunning");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_BATTERY_SWAP = sound("batteryswap");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_PRESSURERELEASE = sound("pressurerelease");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_COMPRESSORRUNNING = sound("compressorrunning");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_DECOMPRESSORRUNNING = sound("decompressorrunning");
	public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_TRANSFORMERHUM = sound("transformerhum");

	private static DeferredHolder<SoundEvent, SoundEvent> sound(String name) {
		return SOUNDS.register(name, () -> SoundEvent.createFixedRangeEvent(Electrodynamics.rl(name), 16.0F));
	}
}
