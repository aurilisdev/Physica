package electrodynamics.prefab.properties;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import electrodynamics.Electrodynamics;
import electrodynamics.common.packet.types.server.PacketSendUpdatePropertiesServer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * A wrapper class designed to monitor a value and take action when it changes
 *
 * @param <T> The type of the property
 * @author skip999
 * @author AurilisDev
 */
public class Property<T> {
    private PropertyManager manager;
    private final IPropertyType type;
    private boolean isDirty = true;
    private boolean shouldSave = true;
    private boolean shouldUpdateClient = true;
    //set this if you want to update a property without having a tile tick
    //otherwise the property will be synced to the client upon change at the end of the tile's tick
    private boolean shouldUpdateOnChange = false;
    private boolean shouldUpdateServer = true;
    private String name;
    private T value;

    private int index = 0;

    //This fires when the property has had a value set and that value is different from the value the property currently has
    //The property contains the new value and val represents the old value. Level may or may not be present.
    private BiConsumer<Property<T>, T> onChange = (prop, val) -> {
    };
    //this fires when the owning tile has been loaded. This fires on both the client and server-side, and Level is present
    private Consumer<Property<T>> onTileLoaded = prop -> {
    };

    private boolean alreadySynced = false;

    public Property(IPropertyType type, String name, T defaultValue) {
        this.type = type;
        if (name == null || name.length() == 0) {
            throw new RuntimeException("The property's name cannot be null or empty");
        }
        this.name = name;
        value = defaultValue;
    }

    public T get() {
        return value;
    }

    public IPropertyType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Property<T> onChange(BiConsumer<Property<T>, T> event) {
        onChange = onChange.andThen(event);
        return this;
    }

    public Property<T> onTileLoaded(Consumer<Property<T>> event) {
        onTileLoaded = onTileLoaded.andThen(event);
        return this;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void clean() {
        isDirty = false;
    }

    public void setManager(PropertyManager manager) {
        this.manager = manager;
    }

    public Property<T> set(Object updated) {

        if (alreadySynced) {
            return this;
        }
        /*
        if (!updated.getClass().equals(value.getClass())) {
            throw new RuntimeException("Value " + updated + " being set for " + getName() + " on tile " + getPropertyManager().getOwner() + " is an invalid data type!");
        }

         */
        checkForChange((T) updated);
        T old = value;
        value = (T) updated;
        if (isDirty() && manager.getOwner().getLevel() != null) {
            if (!manager.getOwner().getLevel().isClientSide()) {
                if (shouldUpdateOnChange) {
                    alreadySynced = true;
                    manager.getOwner().getLevel().sendBlockUpdated(manager.getOwner().getBlockPos(), manager.getOwner().getBlockState(), manager.getOwner().getBlockState(), Block.UPDATE_CLIENTS);
                    manager.getOwner().setChanged();
                    alreadySynced = false;
                }
                manager.setDirty(this);
            } else if(shouldUpdateServer) {
                CompoundTag data = new CompoundTag();
                saveToTag(data, manager.getOwner().getLevel().registryAccess());
                PacketDistributor.sendToServer(new PacketSendUpdatePropertiesServer(data, getIndex(), manager.getOwner().getBlockPos()));
            }
            onChange.accept(this, old);
        }

        return this;
    }

    /**
     * This method should be used when working with more complex data types like arrays (InventoryItems for example)
     * and is a hack to handle issues with Java arrays
     * <p>
     * If it is a single object (FluidStack for example), then do NOT used this method
     */
    @Deprecated(since = "This should be used when working with arrays")
    public void forceDirty() {
        if (!manager.getOwner().getLevel().isClientSide()) {
            manager.setDirty(this);
        } else {
            CompoundTag data = new CompoundTag();
            saveToTag(data, manager.getOwner().getLevel().registryAccess());
            PacketDistributor.sendToServer(new PacketSendUpdatePropertiesServer(data, getIndex(), manager.getOwner().getBlockPos()));
        }
    }

    public void copy(Property<T> other) {
        T otherVal = other.get();
        if (otherVal == null) {
            return;
        }
        set(otherVal);
    }

    private boolean checkForChange(T updated) {
        boolean shouldUpdate = value == null && updated != null;
        if (value != null && updated != null) {
            shouldUpdate = !type.hasChanged(value, updated);
        }
        if (shouldUpdate) {
            isDirty = true;
        }
        return shouldUpdate;
    }

    /**
     * This method is called by the tile on the server once Level is present
     */
    public void onTileLoaded() {
        onTileLoaded.accept(this);
    }

    public boolean shouldSave() {
        return shouldSave;
    }

    public boolean shouldUpdateClient() {
        return shouldUpdateClient;
    }

    public Property<T> setNoSave() {
        shouldSave = false;
        return this;
    }

    public Property<T> setNoUpdateClient() {
        shouldUpdateClient = false;
        return this;
    }

    public Property<T> setShouldUpdateOnChange() {
        shouldUpdateOnChange = true;
        return this;
    }

    public Property<T> setNoUpdateServer() {
        shouldUpdateServer = false;
        return this;
    }

    @Override
    public String toString() {
        return value == null ? "null" : value.toString();
    }

    public PropertyManager getPropertyManager() {
        return manager;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void loadFromTag(CompoundTag tag, HolderLookup.Provider registries) {
        try {
            T data = (T) getType().readFromTag(new IPropertyType.TagReader(this, tag, registries));
            if (data != null) {
                T old = value;
                value = data;
                onChange.accept(this, value);
            }
        } catch (Exception e) {
            Electrodynamics.LOGGER.info("Property " + getName() + " was impropertly cast");
        }


    }

    public void saveToTag(CompoundTag tag, HolderLookup.Provider registries) {
        try {
            getType().writeToTag(new IPropertyType.TagWriter<>(this, tag, registries));
        } catch (Exception e) {
            Electrodynamics.LOGGER.info("Property " + getName() + " was impropertly cast");
        }

    }

}
