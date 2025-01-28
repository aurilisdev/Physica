package electrodynamics.api.item;

import java.util.List;
import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface IItemElectric {

    public static final String JOULES_STORED = "joules";
    public static final String JOULES_CAPACITY = "maximumcapacity";
    public static final String VOLTAGE = "voltage";
    public static final String RECEIVE_LIMIT = "receivelimit";
    public static final String EXTRACT_LIMIT = "extractlimit";
    public static final String CURRENT_BATTERY = "currentbattery";

    static ElectricItemData defaultData(IItemElectric item) {
        return new ElectricItemData(0, item.getElectricProperties().capacity, item.getElectricProperties().extract.getVoltage(), item.getElectricProperties().receive.getJoules(), item.getElectricProperties().extract.getJoules(), new ItemStack(item.getDefaultStorageBattery()));
    }

    default double getJoulesStored(ItemStack stack) {
        return stack.getOrDefault(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, defaultData(this)).joulesStored;
    }

    public static void setEnergyStored(ItemStack stack, double amount) {
        ElectricItemData data = stack.getOrDefault(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, defaultData((IItemElectric) stack.getItem()));
        data.joulesStored = amount;
        stack.set(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, data);
    }

    default double getMaximumCapacity(ItemStack item) {
        return item.getOrDefault(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, defaultData((IItemElectric) item.getItem())).maxJoules;
    }

    static void setMaximumCapacity(ItemStack item, double amt) {
        ElectricItemData data = item.getOrDefault(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, defaultData((IItemElectric) item.getItem()));
        data.maxJoules = amt;
        item.set(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, data);
    }

    default double getReceiveLimit(ItemStack item) {
        return item.getOrDefault(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, defaultData((IItemElectric) item.getItem())).receiveCap;
    }

    static void setReceiveLimit(ItemStack stack, double amount) {

        ElectricItemData data = stack.getOrDefault(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, defaultData((IItemElectric) stack.getItem()));
        data.receiveCap = amount;
        stack.set(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, data);
    }

    default double getExtractLimit(ItemStack item) {
        return item.getOrDefault(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, defaultData((IItemElectric) item.getItem())).extractCap;
    }

    static void setExtractLimit(ItemStack stack, double amount) {
        ElectricItemData data = stack.getOrDefault(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, defaultData((IItemElectric) stack.getItem()));
        data.extractCap = amount;
        stack.set(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, data);
    }

    default boolean isEnergyStorageOnly() {
        return getElectricProperties().isEnergyStorageOnly;
    }

    default boolean cannotHaveBatterySwapped() {
        return getElectricProperties().cannotHaveBatterySwapped;
    }

    default TransferPack extractPower(ItemStack stack, double amount, boolean debug) {
        if (getJoulesStored(stack) <= 0) {
            return TransferPack.EMPTY;
        }
        double current = getJoulesStored(stack);
        double extracted = Math.min(current, Math.min(getExtractLimit(stack), amount));
        if (!debug) {
            setEnergyStored(stack, current - extracted);
        }
        return TransferPack.joulesVoltage(extracted, getElectricProperties().extract.getVoltage());
    }

    default TransferPack receivePower(ItemStack stack, TransferPack amount, boolean debug) {

        double current = getJoulesStored(stack);
        double received = Math.min(amount.getJoules(), getMaximumCapacity(stack) - current);
        if (!debug) {
            if (amount.getVoltage() == getElectricProperties().receive.getVoltage() || amount.getVoltage() == -1) {
                setEnergyStored(stack, current + received);
            }
            if (amount.getVoltage() > getElectricProperties().receive.getVoltage()) {
                overVoltage(amount);
                return TransferPack.EMPTY;
            }
        }
        return TransferPack.joulesVoltage(received, amount.getVoltage());
    }

    default void overVoltage(TransferPack attempt) {
    }

    /**
     * finds the first battery of matching voltage for this item and swaps out the battery in the item with a new one
     *
     * @param tool
     * @param player
     */
    default void swapBatteryPackFirstItem(ItemStack tool, Player player) {
        IItemElectric electricItem = (IItemElectric) tool.getItem();

        if (electricItem.isEnergyStorageOnly() || electricItem.cannotHaveBatterySwapped()) {
            return;
        }

        Inventory inv = player.getInventory();

        for (int i = 0; i < inv.items.size(); i++) {
            ItemStack playerItem = inv.getItem(i).copy();

            // Only allow batteries that have the same receive and extract voltage
            if (!playerItem.isEmpty() && playerItem.getItem() instanceof IItemElectric electric && electric.isEnergyStorageOnly() && electric.getJoulesStored(playerItem) > 0 && electric.getElectricProperties().extract.getVoltage() == electricItem.getElectricProperties().extract.getVoltage() && electric.getElectricProperties().receive.getVoltage() == electricItem.getElectricProperties().receive.getVoltage()) {
                ItemStack currBattery = electricItem.getCurrentBattery(tool);
                if (currBattery.isEmpty()) {
                    return;
                }
                double joulesStored = electricItem.getJoulesStored(tool);
                electricItem.setCurrentBattery(tool, playerItem);
                IItemElectric.setEnergyStored(tool, electric.getJoulesStored(playerItem));
                IItemElectric.setEnergyStored(currBattery, joulesStored);
                inv.setItem(i, ItemStack.EMPTY);
                for (int j = 0; j < inv.items.size(); j++) {
                    ItemStack item = inv.getItem(j);
                    if (item.isEmpty()) {
                        inv.setItem(j, currBattery);
                        player.level().playSound(null, player.getOnPos(), ElectrodynamicsSounds.SOUND_BATTERY_SWAP.get(), SoundSource.PLAYERS, 0.25F, 1.0F);
                        return;
                    }
                }
                return;
            }

        }

    }

    static boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {

        if (action == null || action == ClickAction.PRIMARY || other.isEmpty()) {
            return false;
        }

        if (((IItemElectric) stack.getItem()).cannotHaveBatterySwapped()) {
            return false;
        }

        if (!(other.getItem() instanceof IItemElectric) || (other.getItem() instanceof IItemElectric electric && !electric.isEnergyStorageOnly())) {
            return false;
        }

        IItemElectric thisElectric = (IItemElectric) stack.getItem();
        IItemElectric otherElectric = (IItemElectric) other.getItem();

        if (otherElectric.getJoulesStored(other) == 0 || otherElectric.getElectricProperties().receive.getVoltage() != thisElectric.getElectricProperties().receive.getVoltage() || thisElectric.getElectricProperties().extract.getVoltage() != otherElectric.getElectricProperties().extract.getVoltage()) {
            return false;
        }

        ItemStack currBattery = thisElectric.getCurrentBattery(stack);

        double joulesStored = thisElectric.getJoulesStored(stack);

        IItemElectric.setEnergyStored(currBattery, joulesStored);

        access.set(currBattery);

        IItemElectric.setEnergyStored(stack, otherElectric.getJoulesStored(other));

        thisElectric.setCurrentBattery(stack, other);

        player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), ElectrodynamicsSounds.SOUND_BATTERY_SWAP.get(), SoundSource.PLAYERS, 0.25F, 1.0F, false);

        return true;

    }

    default ItemStack getCurrentBattery(ItemStack tool) {

        ElectricItemData data = tool.getOrDefault(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, defaultData((IItemElectric) tool.getItem()));

        if (data.battery.getItem() == Items.AIR) {
            return ItemStack.EMPTY;
        }

        return data.battery;
    }

    // It is assumed you are setting a battery with this method
    default void setCurrentBattery(ItemStack tool, ItemStack battery) {

        IItemElectric.setMaximumCapacity(tool, getMaximumCapacity(tool));

        ElectricItemData data = tool.getOrDefault(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, defaultData((IItemElectric) tool.getItem()));

        data.battery = battery;

        tool.set(ElectrodynamicsDataComponentTypes.ELECTRIC_ITEM_DATA, data);
    }

    ElectricItemProperties getElectricProperties();

    Item getDefaultStorageBattery();

    static void addBatteryTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip) {
        tooltip.add(ElectroTextUtils.tooltip("currbattery", ((IItemElectric) stack.getItem()).getCurrentBattery(stack).getDisplayName().copy().withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
    }

    public static class ElectricItemData {

        public double joulesStored;

        public double maxJoules;

        public double voltage;
        public double receiveCap;

        public double extractCap;

        public ItemStack battery;

        public ElectricItemData(double joulesStored, double maxJoules, double voltage, double receiveCap, double extractCap, ItemStack battery) {
            this.joulesStored = joulesStored;
            this.maxJoules = maxJoules;
            this.voltage = voltage;
            this.receiveCap = receiveCap;
            this.extractCap = extractCap;
            this.battery = battery;
        }

        public static final Codec<ElectricItemData> CODEC = RecordCodecBuilder.create(instance ->
                        //
                        instance.group(
                                //
                                Codec.DOUBLE.fieldOf(JOULES_STORED).forGetter(instance0 -> instance0.joulesStored),
                                //
                                Codec.DOUBLE.fieldOf(JOULES_CAPACITY).forGetter(instance0 -> instance0.maxJoules),
                                //
                                Codec.DOUBLE.fieldOf(VOLTAGE).forGetter(instance0 -> instance0.voltage),
                                //
                                Codec.DOUBLE.fieldOf(RECEIVE_LIMIT).forGetter(instance0 -> instance0.receiveCap),
                                //
                                Codec.DOUBLE.fieldOf(EXTRACT_LIMIT).forGetter(instance0 -> instance0.extractCap),
                                //
                                ItemStack.OPTIONAL_CODEC.fieldOf(CURRENT_BATTERY).forGetter(instance0 -> instance0.battery)
//

                        ).apply(instance, ElectricItemData::new)


        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ElectricItemData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.DOUBLE, instance0 -> instance0.joulesStored,
                ByteBufCodecs.DOUBLE, instance0 -> instance0.maxJoules,
                ByteBufCodecs.DOUBLE, instance0 -> instance0.voltage,
                ByteBufCodecs.DOUBLE, instance0 -> instance0.receiveCap,
                ByteBufCodecs.DOUBLE, instance0 -> instance0.extractCap,
                ItemStack.OPTIONAL_STREAM_CODEC, instance0 -> instance0.battery,
                ElectricItemData::new
        );

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ElectricItemData data) {
                return joulesStored == data.joulesStored && maxJoules == data.maxJoules && voltage == data.voltage && receiveCap == data.receiveCap && extractCap == data.extractCap && battery == data.battery;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(joulesStored, maxJoules, voltage, receiveCap, extractCap, battery);
        }
    }

}