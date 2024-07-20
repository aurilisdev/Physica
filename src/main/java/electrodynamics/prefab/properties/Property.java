package electrodynamics.prefab.properties;

import java.util.function.BiConsumer;

import electrodynamics.common.packet.types.server.PacketSendUpdatePropertiesServer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * A wrapper class designed to monitor a value and take action when it changes
 * 
 * @author skip999
 * @author AurilisDev
 *
 * @param <T> The type of the property
 */
public class Property<T> {
    private PropertyManager manager;
    private final IPropertyType type;
    private boolean isDirty = true;
    private boolean shouldSave = true;
    private boolean shouldUpdateClient = true;
    private String name;
    private T value;

    private int index = 0;

    // property has new value and value is old value
    private BiConsumer<Property<T>, T> onChange = (prop, val) -> {
    };
    private BiConsumer<Property<T>, T> onLoad = (prop, val) -> {
    };

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

    public Property<T> onLoad(BiConsumer<Property<T>, T> event) {
        onLoad = onLoad.andThen(event);
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
        checkForChange((T) updated);
        T old = value;
        value = (T) updated;
        if (isDirty() && manager.getOwner().getLevel() != null) {
            if (!manager.getOwner().getLevel().isClientSide()) {
                manager.setDirty(this);
            }
            onChange.accept(this, old);
        }

        return this;
    }

    /**
     * This method should be used when working with more complex data types like arrays (InventoryItems for example)
     * 
     * If it is a single object (FluidStack for example), then do NOT used this method
     */
    @Deprecated(since = "This should be used when working with arrays")
    public void forceDirty() {
        if (!manager.getOwner().getLevel().isClientSide()) {
            manager.setDirty(this);
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

    public void load(T val) {
        if (val == null) {
            val = value;
        }
        value = val;
        onLoad.accept(this, value);
    }

    public boolean shouldSave() {
        return shouldSave;
    }

    public Property<T> setNoSave() {
        shouldSave = false;
        return this;
    }

    public boolean shouldUpdateClient() {
        return shouldUpdateClient;
    }

    public Property<T> setNoUpdateClient() {
        shouldUpdateClient = false;
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

    public void loadFromTag(CompoundTag tag, Level world) {
        load((T) getType().readFromTag(new IPropertyType.TagReader(this, tag, world)));
    }

    public void saveToTag(CompoundTag tag, Level world) {
        getType().writeToTag(new IPropertyType.TagWriter<>(this, tag, world));
    }

    public void updateServer() {

        if (manager.getOwner() != null) {
            PacketDistributor.sendToServer(new PacketSendUpdatePropertiesServer(this, manager.getOwner().getBlockPos()));
        }

    }

}
