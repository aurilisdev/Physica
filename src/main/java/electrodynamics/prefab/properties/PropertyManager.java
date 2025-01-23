package electrodynamics.prefab.properties;

import java.util.ArrayList;
import java.util.HashSet;

import electrodynamics.Electrodynamics;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

/**
 * A wrapper class designed to manage data properties on a tile
 * 
 * @author AurilisDev
 * @author skip999
 *
 */
public class PropertyManager {

	public static final String NBT_KEY = "propertydata";

	private final GenericTile owner;

	private ArrayList<Property<?>> properties = new ArrayList<>();

	//private HashSet<PropertyWrapper> dirtyProperties = new HashSet<>();
	private HashSet<Property<?>> dirtyPropertiesDirect = new HashSet<>();

	private boolean isDirty = false;

	public PropertyManager(GenericTile owner) {
		this.owner = owner;
	}

	public <T> Property<T> addProperty(Property<T> prop) {
		properties.add(prop);
		prop.setManager(this);
		prop.setIndex(properties.size() - 1);
		return prop;
	}

	public ArrayList<Property<?>> getProperties() {
		return properties;
	}

	/*
	public HashSet<PropertyWrapper> getClientUpdateProperties() {

		return dirtyProperties;

	}


	 */
	public void saveDirtyPropsToTag(CompoundTag tag, HolderLookup.Provider registries) {
		for(Property<?> prop : dirtyPropertiesDirect){
			prop.saveToTag(tag, owner.getLevel().registryAccess());
		}
	}

	public void saveAllPropsForClientSync(CompoundTag tag, HolderLookup.Provider registries) {
		for(Property<?> prop : properties){
			if(prop.shouldUpdateClient()) {
				prop.saveToTag(tag, registries);
			}
		}
	}

	public void clean() {
		isDirty = false;
		/*
		dirtyProperties.forEach(wrapper -> {
			wrapper.property.clean();
		});
		 */
		for (Property<?> property : dirtyPropertiesDirect) {
			property.clean();
		}
		//dirtyProperties.clear();
		dirtyPropertiesDirect.clear();
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(Property<?> dirtyProp) {
		isDirty = true;
		if (dirtyProp.shouldUpdateClient()) {
			dirtyPropertiesDirect.add(dirtyProp);
		}
	}

	@Override
	public String toString() {
		String string = "";
		for (int i = 0; i < properties.size(); i++) {
			string = string + i + ": " + properties.get(i).toString() + "\n";
		}
		return string;
	}

	public GenericTile getOwner() {
		return owner;
	}

	public void saveToTag(CompoundTag tag, HolderLookup.Provider registries) {
		for (Property<?> prop : getProperties()) {
			if (prop.shouldSave()) {
				prop.saveToTag(tag, registries);
			}
		}
	}

	public void loadFromTag(CompoundTag tag, HolderLookup.Provider registries) {
		for (Property<?> prop : getProperties()) {
			if (prop.shouldSave() && tag.contains(prop.getName())) {
				prop.loadFromTag(tag, registries);

				tag.remove(prop.getName());
			}
		}
	}

	public void loadDataFromClient(int index, CompoundTag data) {
		if(index >= properties.size()) {
			Electrodynamics.LOGGER.error("The tile at " + owner.getBlockPos() + " has a differently sized property list than what was declared by the packet");
			return;
		}
		Property<?> prop = properties.get(index);
		if(owner == null) {
			Electrodynamics.LOGGER.info("The property " + prop.getName() + " is sending data to a null tile");
			return;
		}
		if(owner.getLevel() == null){
			Electrodynamics.LOGGER.info("The property " + prop.getName() + " that sent data to the tile at " + owner.getBlockPos() + " encountered a null level. The data was not loaded");
			return;
		}
		prop.set(prop.getType().readFromTag(new IPropertyType.TagReader(prop, data, owner.getLevel().registryAccess())));
	}

	public void onTileLoaded() {
		for (Property<?> property : properties) {
			property.onTileLoaded();
		}
	}

	/*
	public static record PropertyWrapper(int index, IPropertyType type, Object value, @Nullable Property<?> property) {

	}

	 */
}
