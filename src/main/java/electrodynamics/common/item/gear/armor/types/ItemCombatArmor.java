package electrodynamics.common.item.gear.armor.types;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import electrodynamics.Electrodynamics;
import electrodynamics.api.capability.types.fluid.RestrictedFluidHandlerItemStack;
import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.common.item.gear.armor.ItemElectrodynamicsArmor;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsArmorMaterials;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsFluids;
import electrodynamics.registers.ElectrodynamicsGases;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class ItemCombatArmor extends ItemElectrodynamicsArmor implements IItemElectric {

    public static final int HELMET_CAPACITY = 5000;
    public static final int HELMET_MAX_TEMP = Gas.ROOM_TEMPERATURE;
    public static final int HELMET_MAX_PRESSURE = Gas.PRESSURE_AT_SEA_LEVEL;

    public static final int LEGGINGS_CAPACITY = 5000;
    public static final int LEGGINGS_MAX_TEMP = 500;
    public static final int LEGGINGS_MAX_PRESSURE = 4;

    public static final EnumMap<Type, Integer> DEFENSE_MAP = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(Type.HELMET, 6);
        map.put(Type.CHESTPLATE, 12);
        map.put(Type.LEGGINGS, 16);
        map.put(Type.BOOTS, 6);
    });

    public static final ResourceLocation ARMOR_TEXTURE_LOCATION = Electrodynamics.rl("textures/model/armor/combatarmor.png");

    private final ElectricItemProperties properties;

    public static final float OFFSET = 0.2F;

    public ItemCombatArmor(Properties properties, Type type, Holder<CreativeModeTab> creativeTab) {
        super(ElectrodynamicsArmorMaterials.COMBAT_ARMOR, type, properties, creativeTab);
        switch (type) {
            case HELMET, LEGGINGS:
                this.properties = (ElectricItemProperties) properties;
                break;
            default:
                this.properties = new ElectricItemProperties();
                break;
        }
    }

    @Override
    public void addCreativeModeItems(CreativeModeTab tab, List<ItemStack> items) {

        switch (getEquipmentSlot()) {
            case HEAD:
                ItemStack empty = new ItemStack(this);
                IItemElectric.setEnergyStored(empty, 0);
                items.add(empty);

                ItemStack charged = new ItemStack(this);
                IItemElectric.setEnergyStored(charged, getMaximumCapacity(charged));
                charged.set(ElectrodynamicsDataComponentTypes.GAS_STACK, new GasStack(ElectrodynamicsGases.OXYGEN.value(), HELMET_CAPACITY, Gas.ROOM_TEMPERATURE, Gas.PRESSURE_AT_SEA_LEVEL));
                items.add(charged);
                break;
            case CHEST:
                items.add(new ItemStack(this));
                if (ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM == null) {
                    break;
                }
                ItemStack full = new ItemStack(this);

                GasStack gas = new GasStack(ElectrodynamicsGases.HYDROGEN.value(), ItemJetpack.MAX_CAPACITY, Gas.ROOM_TEMPERATURE, Gas.PRESSURE_AT_SEA_LEVEL);

                IGasHandlerItem handler = full.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

                if (handler == null) {
                    return;
                }

                handler.fill(gas, GasAction.EXECUTE);

                full.set(ElectrodynamicsDataComponentTypes.PLATES, 2);

                items.add(full);
                break;
            case LEGS:
                empty = new ItemStack(this);
                IItemElectric.setEnergyStored(empty, 0);
                items.add(empty);

                charged = new ItemStack(this);
                IItemElectric.setEnergyStored(charged, getMaximumCapacity(charged));
                charged.set(ElectrodynamicsDataComponentTypes.GAS_STACK, new GasStack(ElectrodynamicsGases.ARGON.value(), LEGGINGS_CAPACITY, Gas.ROOM_TEMPERATURE, Gas.PRESSURE_AT_SEA_LEVEL));
                items.add(charged);
                break;
            case FEET:
                items.add(new ItemStack(this));

                if (Capabilities.FluidHandler.ITEM == null) {
                    return;
                }

                full = new ItemStack(this);

                IFluidHandlerItem handlerFluid = full.getCapability(Capabilities.FluidHandler.ITEM);

                if (handlerFluid == null) {
                    return;
                }

                ((RestrictedFluidHandlerItemStack) handlerFluid).setFluid(new FluidStack(ElectrodynamicsFluids.FLUID_HYDRAULIC, ItemHydraulicBoots.MAX_CAPACITY));

                items.add(full);

                break;
            default:
                break;
        }

    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag flagin) {
        super.appendHoverText(stack, context, tooltips, flagin);
        switch (((ArmorItem) stack.getItem()).getEquipmentSlot()) {
            case HEAD:
                tooltips.add(ElectroTextUtils.tooltip("item.electric.info", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(getJoulesStored(stack), DisplayUnit.JOULES), ChatFormatter.getChatDisplayShort(getMaximumCapacity(stack), DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
                tooltips.add(ElectroTextUtils.tooltip("item.electric.voltage", ChatFormatter.getChatDisplayShort(properties.receive.getVoltage(), DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
                IItemElectric.addBatteryTooltip(stack, context, tooltips);
                if (stack.getOrDefault(ElectrodynamicsDataComponentTypes.ON, false)) {
                    tooltips.add(ElectroTextUtils.tooltip("nightvisiongoggles.status").withStyle(ChatFormatting.DARK_GRAY).append(ElectroTextUtils.tooltip("nightvisiongoggles.on").withStyle(ChatFormatting.GREEN)));
                } else {
                    tooltips.add(ElectroTextUtils.tooltip("nightvisiongoggles.status").withStyle(ChatFormatting.DARK_GRAY).append(ElectroTextUtils.tooltip("nightvisiongoggles.off").withStyle(ChatFormatting.RED)));
                }
                GasStack gas = stack.getOrDefault(ElectrodynamicsDataComponentTypes.GAS_STACK.get(), GasStack.EMPTY);
                tooltips.add(ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(gas.getAmount()), ChatFormatter.formatFluidMilibuckets(HELMET_CAPACITY)).withStyle(ChatFormatting.GRAY));
                if (Screen.hasShiftDown()) {
                    tooltips.add(ElectroTextUtils.tooltip("maxpressure", ChatFormatter.getChatDisplayShort(HELMET_MAX_PRESSURE, DisplayUnit.PRESSURE_ATM)).withStyle(ChatFormatting.GRAY));
                    tooltips.add(ElectroTextUtils.tooltip("maxtemperature", ChatFormatter.getChatDisplayShort(HELMET_MAX_TEMP, DisplayUnit.TEMPERATURE_KELVIN)).withStyle(ChatFormatting.GRAY));
                }
                break;
            case CHEST:
                ItemJetpack.staticAppendHoverText(stack, context, tooltips, flagin);
                ItemCompositeArmor.staticAppendHoverText(stack, context, tooltips, flagin);
                break;
            case LEGS:
                tooltips.add(ElectroTextUtils.tooltip("item.electric.info", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(getJoulesStored(stack), DisplayUnit.JOULES), ChatFormatter.getChatDisplayShort(getMaximumCapacity(stack), DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
                tooltips.add(ElectroTextUtils.tooltip("item.electric.voltage", ChatFormatter.getChatDisplayShort(properties.receive.getVoltage(), DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
                ItemServoLeggings.staticAppendTooltips(stack, context, tooltips, flagin);
                gas = stack.getOrDefault(ElectrodynamicsDataComponentTypes.GAS_STACK.get(), GasStack.EMPTY);
                tooltips.add(ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(gas.getAmount()), ChatFormatter.formatFluidMilibuckets(HELMET_CAPACITY)).withStyle(ChatFormatting.GRAY));
                if (Screen.hasShiftDown()) {
                    tooltips.add(ElectroTextUtils.tooltip("maxpressure", ChatFormatter.getChatDisplayShort(LEGGINGS_MAX_PRESSURE, DisplayUnit.PRESSURE_ATM)).withStyle(ChatFormatting.GRAY));
                    tooltips.add(ElectroTextUtils.tooltip("maxtemperature", ChatFormatter.getChatDisplayShort(LEGGINGS_MAX_TEMP, DisplayUnit.TEMPERATURE_KELVIN)).withStyle(ChatFormatting.GRAY));
                }
                break;
            case FEET:
                if (Capabilities.FluidHandler.ITEM == null) {
                    return;
                }
                IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);

                if (handler == null) {
                    return;
                }

                tooltips.add(ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(handler.getFluidInTank(0).getAmount()), ChatFormatter.formatFluidMilibuckets(ItemHydraulicBoots.MAX_CAPACITY)).withStyle(ChatFormatting.GRAY));
                break;
            default:
                break;
        }
    }

    @Override
    public void onWearingTick(ItemStack stack, Level level, Player player, int slotId, boolean isSelected) {
        super.onWearingTick(stack, level, player, slotId, isSelected);
        ItemCombatArmor combat = (ItemCombatArmor) stack.getItem();
        switch (combat.getEquipmentSlot()) {
            case HEAD:
                ItemNightVisionGoggles.wearingTick(stack, level, player);

                if(player.getAirSupply() < player.getMaxAirSupply()){
                    GasStack oxygen = stack.getOrDefault(ElectrodynamicsDataComponentTypes.GAS_STACK, GasStack.EMPTY);
                    if(!oxygen.isEmpty()){
                        oxygen.shrink(1);
                        stack.set(ElectrodynamicsDataComponentTypes.GAS_STACK.get(), oxygen);
                        player.setAirSupply(player.getMaxAirSupply());
                    }
                }
                break;
            case CHEST:
                ItemJetpack.wearingTick(stack, level, player, OFFSET, true);
                break;
            case LEGS:
                ItemServoLeggings.wearingTick(stack, level, player);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        ItemCombatArmor combat = (ItemCombatArmor) stack.getItem();
        switch (combat.getEquipmentSlot()) {
            case HEAD, LEGS:
                return getJoulesStored(stack) < getMaximumCapacity(stack);
            case CHEST:
                return ItemJetpack.staticIsBarVisible(stack);
            case FEET:
                return ItemHydraulicBoots.staticIsBarVisible(stack);
            default:
                return false;
        }
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        ItemCombatArmor combat = (ItemCombatArmor) stack.getItem();
        switch (combat.getEquipmentSlot()) {
            case HEAD, LEGS:
                return (int) Math.round(13.0f * getJoulesStored(stack) / getMaximumCapacity(stack));
            case CHEST:
                return ItemJetpack.staticGetBarWidth(stack);
            case FEET:
                return ItemHydraulicBoots.staticGetBarWidth(stack);
            default:
                return 0;
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack1, ItemStack stack2) {
        return false;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public ElectricItemProperties getElectricProperties() {
        return properties;
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return ItemJetpack.staticCanElytraFly(stack, entity);
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return ItemJetpack.staticElytraFlightTick(stack, entity, flightTicks);
    }

    @Override
    public Item getDefaultStorageBattery() {
        return switch (getEquipmentSlot()) {
            case HEAD, LEGS -> ElectrodynamicsItems.ITEM_BATTERY.get();
            default -> Items.AIR;
        };
    }

    public static Predicate<GasStack> getHelmetGasValidator() {
        return gas -> gas.is(ElectrodynamicsTags.Gases.OXYGEN);
    }
    public static Predicate<GasStack> getLeggingsGasValidator() {
        return gas -> gas.is(ElectrodynamicsTags.Gases.ARGON);
    }


    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {

        if (getEquipmentSlot() == EquipmentSlot.CHEST || getEquipmentSlot() == EquipmentSlot.FEET) {
            return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
        }

        if (!IItemElectric.overrideOtherStackedOnMe(stack, other, slot, action, player, access)) {
            return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
        }

        return true;

    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ARMOR_TEXTURE_LOCATION;
    }
}
