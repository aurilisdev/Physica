package electrodynamics.api.gas;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

/**
 * Basic implementation of a Gas mirroring certain aspects of fluids
 * 
 * Gases are not designed to be place hence how lightweight it is
 * 
 * @author skip999
 *
 */
public class Gas {

	public static final double ROOM_TEMPERATURE = 293;
	public static final int PRESSURE_AT_SEA_LEVEL = 1;
	public static final double MINIMUM_HEAT_BURN_TEMP = 327;
	public static final double MINIMUM_FREEZE_TEMP = 260;

	private final Holder<Item> container;
	private final TagKey<Gas> tag;
	private final Component description;
	private final double condensationTemp; // Degrees Kelvin; set to 0 if this gas does not condense
	@Nullable
	private final Holder<Fluid> condensedFluid; // set to empty if gas does not condense

	public Gas(Holder<Item> container, @Nullable TagKey<Gas> tag, Component description) {
		this.container = container;
		this.tag = tag;
		this.description = description;
		this.condensationTemp = 0;
		this.condensedFluid = new Holder.Direct(Fluids.EMPTY);
	}

	public Gas(Holder<Item> container, @Nullable TagKey<Gas> tag, Component description, double condensationTemp, Holder<Fluid> condensedFluid) {
		this.container = container;
		this.tag = tag;
		this.description = description;
		this.condensationTemp = condensationTemp;
		this.condensedFluid = condensedFluid;
		ElectrodynamicsGases.MAPPED_GASSES.put(condensedFluid, this);
	}

	public Component getDescription() {
		return description;
	}

	public Item getContainer() {
		return container.value();
	}

	public boolean is(@Nonnull TagKey<Gas> tag) {
		return this.tag == null ? false : this.tag.equals(tag);
	}

	public boolean isEmpty() {
		return this == ElectrodynamicsGases.EMPTY.value();
	}

	public double getCondensationTemp() {
		return condensationTemp;
	}

	@Nullable
	public Fluid getCondensedFluid() {
		return condensedFluid.value();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Gas other) {
			return other == this;
		}
		return false;
	}

	@Override
	public String toString() {
		return description.getString() + ",\tcondensation temp : " + condensationTemp + " K,\tcondensed fluid: " + getCondensedFluid().getFluidType().getDescription().getString();
	}
	
	public boolean doesCondense() {
	    return condensationTemp > 0;
	}

}
