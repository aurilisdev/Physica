package electrodynamics.prefab.utilities.object;

import java.util.List;

import com.google.gson.JsonObject;

import electrodynamics.Electrodynamics;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class CombustionFuelSource {

	public static final String FLUID_KEY = "fluid_tag";
	public static final String USAGE_AMOUNT = "usage_per_burn";
	public static final String POWER_MULTIPLIER = "power_multiplier";

	public static final CombustionFuelSource EMPTY = new CombustionFuelSource(FluidTags.create(Electrodynamics.vanillarl("air")), 0, 0);

	private FluidIngredient fuel;
	private double powerMultiplier;
	private final TagKey<Fluid> tag;

	private CombustionFuelSource(TagKey<Fluid> tag, int usageAmount, double powerMultiplier) {
		fuel = new FluidIngredient(tag, usageAmount);
		this.powerMultiplier = powerMultiplier;
		this.tag = tag;
	}

	public double getPowerMultiplier() {
		return powerMultiplier;
	}

	public boolean isFuelSource(FluidStack stack) {
		return fuel.test(stack);
	}

	public List<FluidStack> getFuels() {
		return fuel.getMatchingFluids();
	}

	public TagKey<Fluid> getTag() {
		return tag;
	}

	public int getFuelUsage() {
		return fuel.getFluidStack().getAmount();
	}

	public boolean isEmpty() {
		return this == EMPTY;
	}

	public static CombustionFuelSource fromJson(JsonObject json) {
		TagKey<Fluid> tag = FluidTags.create(ResourceLocation.parse(json.get(FLUID_KEY).getAsString()));
		return new CombustionFuelSource(tag, json.get(USAGE_AMOUNT).getAsInt(), json.get(POWER_MULTIPLIER).getAsDouble());
	}

	public static JsonObject toJson(TagKey<Fluid> fluid, int usageAmount, double powerMultiplier) {
		JsonObject json = new JsonObject();
		json.addProperty(FLUID_KEY, fluid.location().toString());
		json.addProperty(USAGE_AMOUNT, usageAmount);
		json.addProperty(POWER_MULTIPLIER, powerMultiplier);
		return json;
	}

	public void writeToBuffer(FriendlyByteBuf buffer) {
		buffer.writeUtf(fuel.tag.location().toString());
		buffer.writeInt(fuel.getFluidStack().getAmount());
		buffer.writeDouble(powerMultiplier);
	}

	public static CombustionFuelSource readFromBuffer(FriendlyByteBuf buffer) {
		TagKey<Fluid> tag = FluidTags.create(ResourceLocation.parse(buffer.readUtf()));
		return new CombustionFuelSource(tag, buffer.readInt(), buffer.readDouble());
	}

}
