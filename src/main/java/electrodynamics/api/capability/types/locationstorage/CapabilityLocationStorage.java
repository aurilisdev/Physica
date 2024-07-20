package electrodynamics.api.capability.types.locationstorage;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.prefab.utilities.object.Location;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class CapabilityLocationStorage implements ILocationStorage, INBTSerializable<CompoundTag> {

	public CapabilityLocationStorage(int size) {
		// avoids null errors
		for (int i = 0; i < size; i++) {
			locations.add(new Location(0, 0, 0));
		}
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider) {
		if (ElectrodynamicsCapabilities.CAPABILITY_LOCATIONSTORAGE_ITEM != null) {
			CompoundTag nbt = new CompoundTag();
			nbt.putInt("size", locations.size());
			for (int i = 0; i < locations.size(); i++) {
				locations.get(i).writeToNBT(nbt, ElectrodynamicsCapabilities.LOCATION_KEY + i);
			}
			return nbt;
		}
		return new CompoundTag();
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
		if (ElectrodynamicsCapabilities.CAPABILITY_LOCATIONSTORAGE_ITEM != null) {
			locations.clear();
			for (int i = 0; i < nbt.getInt("size"); i++) {
				locations.add(Location.readFromNBT(nbt, ElectrodynamicsCapabilities.LOCATION_KEY + i));
			}
		}
	}

	private final List<Location> locations = new ArrayList<>();

	@Override
	public void setLocation(int index, double x, double y, double z) {
		locations.set(index, new Location(x, y, z));
	}

	@Override
	public Location getLocation(int index) {
		return locations.get(index);
	}

	@Override
	public void addLocation(double x, double y, double z) {
		locations.add(new Location(x, y, z));
	}

	@Override
	public void removeLocation(Location location) {
		locations.remove(location);
	}

	@Override
	public void clearLocations() {
		locations.clear();
	}

	@Override
	public List<Location> getLocations() {
		return locations;
	}

}