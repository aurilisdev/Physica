package electrodynamics.common.item.gear.tools;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasHandlerItemStack;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.inventory.InventoryTickConsumer;
import electrodynamics.common.item.ItemElectrodynamics;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemPortableCylinder extends ItemElectrodynamics {

    public static final int MAX_GAS_CAPCITY = 5000;

    public static final int MAX_TEMPERATURE = 1000;
    public static final int MAX_PRESSURE = 1000;

    public static final List<InventoryTickConsumer> INVENTORY_TICK_CONSUMERS = new ArrayList<>();

    public ItemPortableCylinder(Properties properties, Holder<CreativeModeTab> creativeTab) {
        super(properties, creativeTab);
    }

    @Override
    public void addCreativeModeItems(CreativeModeTab group, List<ItemStack> items) {

        items.add(new ItemStack(this));

        if (ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM == null) {
            return;
        }

        ElectrodynamicsGases.GAS_REGISTRY.stream().forEach(gas -> {
            if (gas.isEmpty()) {
                return;
            }
            ItemStack temp = new ItemStack(this);

            IGasHandlerItem handler = temp.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

            if (handler == null) {
                return;
            }

            GasHandlerItemStack cap = (GasHandlerItemStack) handler;

            cap.setGas(new GasStack(gas, MAX_GAS_CAPCITY, gas.getCondensationTemp() >= Gas.ROOM_TEMPERATURE ? gas.getCondensationTemp() + 1 : Gas.ROOM_TEMPERATURE, Gas.PRESSURE_AT_SEA_LEVEL));

            items.add(temp);
        });

    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slot, isSelected);
        INVENTORY_TICK_CONSUMERS.forEach(consumer -> consumer.apply(stack, level, entity, slot, isSelected));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag isAdvanced) {

        IGasHandlerItem handler = stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (handler == null) {

            super.appendHoverText(stack, context, tooltips, isAdvanced);

            return;

        }

        GasStack gas = handler.getGasInTank(0);
        if (gas.isEmpty()) {
            tooltips.add(ElectroTextUtils.ratio(Component.literal("0"), ChatFormatter.formatFluidMilibuckets(MAX_GAS_CAPCITY)).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltips.add(gas.getGas().getDescription().copy().withStyle(ChatFormatting.GRAY));
            tooltips.add(ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(gas.getAmount()), ChatFormatter.formatFluidMilibuckets(MAX_GAS_CAPCITY)).withStyle(ChatFormatting.DARK_GRAY));
            tooltips.add(ChatFormatter.getChatDisplayShort(gas.getTemperature(), DisplayUnit.TEMPERATURE_KELVIN).withStyle(ChatFormatting.DARK_GRAY));
            tooltips.add(ChatFormatter.getChatDisplayShort(gas.getPressure(), DisplayUnit.PRESSURE_ATM).withStyle(ChatFormatting.DARK_GRAY));
        }
        if (Screen.hasShiftDown()) {
            tooltips.add(ElectroTextUtils.tooltip("maxpressure", ChatFormatter.getChatDisplayShort(MAX_PRESSURE, DisplayUnit.PRESSURE_ATM)).withStyle(ChatFormatting.GRAY));
            tooltips.add(ElectroTextUtils.tooltip("maxtemperature", ChatFormatter.getChatDisplayShort(MAX_TEMPERATURE, DisplayUnit.TEMPERATURE_KELVIN)).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltips, isAdvanced);
    }

    @Override
    public int getBarWidth(ItemStack stack) {

        IGasHandlerItem handler = stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (handler == null) {
            return 13;
        }

        return (int) (13.0 * handler.getGasInTank(0).getAmount() / handler.getTankCapacity(0));

    }

    @Override
    public boolean isBarVisible(ItemStack stack) {

        IGasHandlerItem handler = stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (handler == null) {
            return false;
        }

        return !handler.getGasInTank(0).isEmpty();

    }

}
