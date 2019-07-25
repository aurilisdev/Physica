package cofh.api.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Reference implementation of {@link IEnergyHandler}. Use/extend this or
 * implement your own.
 *
 * @author King Lemming
 *
 */
@SuppressWarnings("deprecation")
@Deprecated
public class TileEnergyHandler extends TileEntity implements IEnergyHandler {

	protected EnergyStorage storage = new EnergyStorage(32000);

	/* IEnergyConnection */
	@Override
	public boolean canConnectEnergy(ForgeDirection from)
	{

		return true;
	}

	/* IEnergyProvider */
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{

		return storage.extractEnergy(maxExtract, simulate);
	}

	/* IEnergyReceiver and IEnergyProvider */
	@Override
	public int getEnergyStored(ForgeDirection from)
	{

		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from)
	{

		return storage.getMaxEnergyStored();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{

		super.readFromNBT(nbt);
		storage.readFromNBT(nbt);
	}

	/* IEnergyReceiver */
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{

		return storage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{

		super.writeToNBT(nbt);
		storage.writeToNBT(nbt);
	}

}
