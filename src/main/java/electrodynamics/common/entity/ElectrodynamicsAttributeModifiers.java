package electrodynamics.common.entity;

import electrodynamics.Electrodynamics;
import electrodynamics.common.item.gear.armor.types.ItemServoLeggings;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public class ElectrodynamicsAttributeModifiers {

	public static final AttributeModifier JETPACK_SPEED = new AttributeModifier(Electrodynamics.rl("jetpack_speed"), 1, Operation.ADD_MULTIPLIED_TOTAL);

	public static final AttributeModifier SERVO_LEGGINGS_STEP = new AttributeModifier(Electrodynamics.rl("servo_leggings_step"), 1.1F - ItemServoLeggings.DEFAULT_VANILLA_STEPUP, Operation.ADD_VALUE);

	public static void init() {
	}

}
