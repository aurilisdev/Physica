package electrodynamics.common.entity;

import electrodynamics.api.References;
import electrodynamics.common.item.gear.armor.types.ItemServoLeggings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public class ElectrodynamicsAttributeModifiers {

	public static final AttributeModifier JETPACK_SPEED = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(References.ID, "jetpack_speed"), 1, Operation.ADD_MULTIPLIED_TOTAL);

	public static final AttributeModifier SERVO_LEGGINGS_STEP = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(References.ID, "servo_leggings_step"), 1.1F - ItemServoLeggings.DEFAULT_VANILLA_STEPUP, Operation.ADD_VALUE);

	public static void init() {
	}

}
