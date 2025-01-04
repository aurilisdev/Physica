package electrodynamics.api.capability.types.gas;

import javax.annotation.Nonnull;

import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;

/**
 * An implementation of a Gas Handler capability modeled after IFluidHandler
 * 
 * @author skip999
 *
 */
public interface IGasHandler {

	/**
	 * @return The number of gas storage units available in this handler.
	 */
	int getTanks();

	/**
	 * @param tank : The tank to check.
	 * @return A GasStack representing the gas stored in that tank.
	 */
	GasStack getGasInTank(int tank);

	/**
	 * @param tank : The tank to check.
	 * @return An integer representing the maximum storage capacity of the checked tank.
	 */
	int getTankCapacity(int tank);

	/**
	 * @param tank : The tank to check.
	 * @return An integer representing the maximum temperature (in degrees Kelvin) of the checked tank
	 */
	int getTankMaxTemperature(int tank);

	/**
	 * @param tank : The tank to check
	 * @return A double representing the maximum pressure (in ATM) of the checked tank
	 */
	int getTankMaxPressure(int tank);

	/**
	 * @param tank : The tank to check
	 * @param gas  : The gas being checked
	 * @return Whether or not the checked tank could EVER accept the checked gas
	 */
	boolean isGasValid(int tank, @Nonnull GasStack gas);

	/**
	 * @param gas    : The gas to fill
	 * @param action : If SIMULATE, the action will only be simulated
	 * @return The amount of gas that was accepted
	 */
	int fill(GasStack gas, GasAction action);

	/**
	 * @param gas    : The gas to be drained
	 * @param action : If SIMULATE, the action will only be simulated
	 * @return A GasStack representing how much gas was actually drained
	 */
	GasStack drain(GasStack gas, GasAction action);

	/**
	 * @param maxFill : The amount of gas to drain
	 * @param action  : If SIMULATE, the action will only be simulated
	 * @return A GasStack representing how much gas was actually drained
	 */
	GasStack drain(int maxFill, GasAction action);

	/**
	 * @param tank : the tank to heat
	 * @param deltaTemperature : The amount the temperature should change.
	 * @param action           : If SIMULATE, the heating will only be simulated.
	 * @return How much room is left in the tank after the gas is heated. A VALUE OF NEGATIVE ONE INDICATES THERE IS NOT ENOUGH ROOM.
	 */
	int heat(int tank, int deltaTemperature, GasAction action);

	/**
	 * @param tank : the tank to pressurize
	 * @param action        : If SIMULATE, the pressurizing will only be simulated.
	 * @return How much room is left in the tank after the gas is pressurized. A VALUE OF NEGATIVE ONE INDICATES THERE IS NOT ENOUGH ROOM.
	 */
	int bringPressureTo(int tank, int atm, GasAction action);

}
