package electrodynamics.prefab.tile.components.type;

import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponent;
import electrodynamics.prefab.tile.components.IComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public class ComponentUpgradeHandler implements IComponent {

    public static final double BASIC_SPEED_BOOST = 1.5;
    public static final double BASIC_SPEED_POWER = 1.5;

    public static final double ADVANCED_SPEED_BOOST = 2.25;
    public static final double ADVANCED_SPEED_POWER = 2.25;

    public static final double SOLAR_CELL_MULT = 2.25;

    public static final double STATOR_MULT = 2.25;

    private GenericTile holder;

    private Property<Double> powerUsageMultiplier;

    private Property<Boolean> hasEjectorUpgrade;

    private Property<Boolean> hasInjectorUpgrade;

    private Property<Double> powerGenerationMultiplier;

    private Property<Integer> unbreakingLevel;

    private Property<Integer> fortuneLevel;

    private Property<Integer> silkTouchLevel;

    private Property<Boolean> hasExperienceUpgrade;

    private Property<Integer> rangeLevel;

    public ComponentUpgradeHandler(GenericTile holder) {
        this.holder = holder;

        powerUsageMultiplier = holder.property(new Property<>(PropertyTypes.DOUBLE, "powerusageupgradecomponent", 1.0));
        hasEjectorUpgrade = holder.property(new Property<>(PropertyTypes.BOOLEAN, "hasejectorupgradecomponent", false));
        hasInjectorUpgrade = holder.property(new Property<>(PropertyTypes.BOOLEAN, "hasinjectorupgradecomponent", false));
        powerGenerationMultiplier = holder.property(new Property<>(PropertyTypes.DOUBLE, "powergenupgradecomponent", 1.0));

        unbreakingLevel = holder.property(new Property<>(PropertyTypes.INTEGER, "unbreakinglevelupgradecomponent", 0));
        silkTouchLevel = holder.property(new Property<>(PropertyTypes.INTEGER, "silktouchlevelupgradecomponent", 0));
        fortuneLevel = holder.property(new Property<>(PropertyTypes.INTEGER, "fortunelevelupgradecomponent", 0));

        hasExperienceUpgrade = holder.property(new Property<>(PropertyTypes.BOOLEAN, "experienceupgradecomponent", false));

        rangeLevel = holder.property(new Property<>(PropertyTypes.INTEGER, "rangelevelupgradecomponent", 1));

    }

    @Override
    public void holder(GenericTile holder) {
        this.holder = holder;
    }

    @Override
    public IComponentType getType() {
        return IComponentType.UpgradeHandler;
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {

    }

    @Override
    public void saveToNBT(CompoundTag nbt) {

    }

    public void serverTick(ComponentTickable tick) {

        if (!hasEjectorUpgrade.get() && !hasInjectorUpgrade.get()) {
            return;
        }

        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);

        for (ItemStack stack : inv.getUpgradeContents()) {

            ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();

            if (upgrade.subtype == SubtypeItemUpgrade.itemoutput && hasEjectorUpgrade.get()) {

            }

        }

    }

    public void onInventoryChange(int slot, ComponentInventory inv) {

    }

}
