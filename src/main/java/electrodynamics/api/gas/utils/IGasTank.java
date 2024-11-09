package electrodynamics.api.gas.utils;

import javax.annotation.Nonnull;

import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;

/**
 * Interface template for an implementation of a GasTank. Modeled after IFluidTank
 * 
 * @author skip999
 *
 */
public interface IGasTank {

	@Nonnull
	GasStack getGas();

	/**
	 * @return How much gas is currently in the tank.
	 */
	int getGasAmount();

	/**
	 * @return How much gas the tank can hold.
	 */
	int getCapacity();

	/**
	 * @return The maximum temperature in Kelvin that the tank can handle
	 */
	int getMaxTemperature();

	/**
	 * @return The maximum pressure in ATM that the tank can handle
	 */
	int getMaxPressure();

	/**
	 * @param other: The gas to be tested.
	 * @return Whether or not this tank can EVER hold the tested gas.
	 */
	boolean isGasValid(GasStack other);

	/**
	 * @param resource : The GasStack attempting to fill this tank.
	 * @param action   : If SIMULATE, the fill will only be simulated.
	 * @return How much Gas was accepted
	 */
	int fill(GasStack resource, GasAction action);

	/**
	 * @param amount : The amount of Gas desired to be removed.
	 * @param action : If SIMULATE, the drain will only be simulated.
	 * @return The Gas removed from the tank; Empty if none was removed.
	 */
	GasStack drain(int amount, GasAction action);

	/**
	 * @param resource : The Gas desired to be removed.
	 * @param action   : If SIMULATE, the drain will only be simulated.
	 * @return The Gas removed from the tank; Empty if none was removed.
	 */
	GasStack drain(GasStack resource, GasAction action);

	/**
	 * @param tank : the tank to heat
	 * @param deltaTemperature : The amount the temperature should change.
	 * @param action           : If SIMULATE, the heating will only be simulated.
	 * @return how much room is left in the tank after the gas is heated. A VALUE OF NEGATIVE ONE INDICATES THERE IS NOT ENOUGH ROOM.
	 */
	int heat(int tank, int deltaTemperature, GasAction action);

	/**
	 * @param tank : the tank to pressurize
	 * @param atm : The new pressure the GasTank should have
	 * @param action        : If SIMULATE, the pressurizing will only be simulated.
	 * @return how much room is left in the tank after the gas is pressurized. A VALUE OF NEGATIVE ONE INDICATES THERE IS NOT ENOUGH ROOM.
	 */
	int bringPressureTo(int tank, int atm, GasAction action);

}
