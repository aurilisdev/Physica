package electrodynamics.registers;

import electrodynamics.Electrodynamics;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class ElectrodynamicsDamageTypes {

	public static final ResourceKey<DamageType> ELECTRICITY = create("electricity");
	public static final ResourceKey<DamageType> RADIATION = create("radiation");
	public static final ResourceKey<DamageType> ACCELERATED_BOLT = create("accelerated_bolt");
	public static final ResourceKey<DamageType> ACCELERATED_BOLT_IGNOREARMOR = create("accelerated_bolt_ignorearmor");
	public static final ResourceKey<DamageType> PLASMA_BOLT = create("plasma_bolt");

	public static ResourceKey<DamageType> create(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, Electrodynamics.rl(name));
	}

	public static void registerTypes(BootstrapContext<DamageType> context) {
		context.register(ELECTRICITY, new DamageType("electricity", DamageScaling.NEVER, 0, DamageEffects.HURT));
		context.register(RADIATION, new DamageType("radiation", DamageScaling.NEVER, 0.1F, DamageEffects.HURT));
		context.register(ACCELERATED_BOLT, new DamageType("acceleratedbolt", DamageScaling.NEVER, 0, DamageEffects.HURT));
		context.register(ACCELERATED_BOLT_IGNOREARMOR, new DamageType("acceleratedbolt", DamageScaling.NEVER, 0, DamageEffects.HURT));
		context.register(PLASMA_BOLT, new DamageType("plasmabolt", DamageScaling.NEVER, 0, DamageEffects.BURNING));
	}

}
