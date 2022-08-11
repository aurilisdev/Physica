package physica.api.core.electricity;

import cofh.api.energy.IEnergyProvider;
import net.minecraftforge.common.util.ForgeDirection;
import physica.api.core.abstraction.Face;

public interface IElectricityProvider extends IElectricTile, IEnergyProvider {
	int getElectricityStored(Face from);

	int getElectricCapacity(Face from);

	default int extractElectricity(Face from, int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	@Deprecated
	default int getEnergyStored(ForgeDirection from) {
		return getElectricityStored(Face.Parse(from));
	}

	@Override
	@Deprecated
	default int getMaxEnergyStored(ForgeDirection from) {
		return getElectricCapacity(Face.Parse(from));
	}

	@Override
	@Deprecated
	default int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return extractElectricity(Face.Parse(from), maxExtract, simulate);
	}
}
