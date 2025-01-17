package electrodynamics.prefab.item;

import java.util.List;
import java.util.function.Function;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.common.item.ItemElectrodynamics;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

public class ItemElectric extends ItemElectrodynamics implements IItemElectric {

	private final ElectricItemProperties properties;
	private final Function<Item, Item> getBatteryItem;

	public ItemElectric(ElectricItemProperties properties, Holder<CreativeModeTab> creativeTab, Function<Item, Item> getBatteryItem) {
		super(properties, creativeTab);
		this.properties = properties;
		this.getBatteryItem = getBatteryItem;
	}

	@Override
	public void addCreativeModeItems(CreativeModeTab group, List<ItemStack> items) {

		ItemStack empty = new ItemStack(this);
		//IItemElectric.setEnergyStored(empty, 0);
		items.add(empty);
		ItemStack charged = new ItemStack(this);
		IItemElectric.setEnergyStored(charged, getMaximumCapacity(charged));
		items.add(charged);

	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return (int) Math.round(13.0f * getJoulesStored(stack) / getMaximumCapacity(stack));
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return getJoulesStored(stack) < getMaximumCapacity(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, context, tooltip, flagIn);
		tooltip.add(ElectroTextUtils.tooltip("item.electric.info", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(getJoulesStored(stack), DisplayUnit.JOULES), ChatFormatter.getChatDisplayShort(getMaximumCapacity(stack), DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		tooltip.add(ElectroTextUtils.tooltip("item.electric.voltage", ChatFormatter.getChatDisplayShort(properties.receive.getVoltage(), DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		if (getDefaultStorageBattery() != Items.AIR) {
			IItemElectric.addBatteryTooltip(stack, context, tooltip);
		}
	}

	@Override
	public ElectricItemProperties getElectricProperties() {
		return properties;
	}

	@Override
	public Item getDefaultStorageBattery() {
		return getBatteryItem.apply(this);
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {

		if (!IItemElectric.overrideOtherStackedOnMe(stack, other, slot, action, player, access)) {
			return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
		}

		return true;

	}

}
