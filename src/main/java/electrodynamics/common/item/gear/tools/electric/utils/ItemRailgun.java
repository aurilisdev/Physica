package electrodynamics.common.item.gear.tools.electric.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.item.IItemTemperate;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.item.ItemElectric;
import electrodynamics.prefab.item.TemperateItemProperties;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemRailgun extends ItemElectric implements IItemTemperate {

	private final TemperateItemProperties temperateProperties = new TemperateItemProperties();
	private double overheatTemperature = 0;
	private double tempThreshold = 0;
	private double tempPerTick = 0;

	public ItemRailgun(ElectricItemProperties properties, Supplier<CreativeModeTab> creativeTab, double overheatTemperature, double tempThreshold, double tempPerTick, Function<Item, Item> getBatteryItem) {
		super(properties, creativeTab, getBatteryItem);
		this.overheatTemperature = overheatTemperature;
		this.tempThreshold = tempThreshold;
		this.tempPerTick = tempPerTick;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, context, tooltip, flagIn);
		tooltip.add(ElectroTextUtils.tooltip("railguntemp", ChatFormatter.getChatDisplayShort(IItemTemperate.getTemperature(stack), DisplayUnit.TEMPERATURE_CELCIUS)).withStyle(ChatFormatting.YELLOW));
		tooltip.add(ElectroTextUtils.tooltip("railgunmaxtemp", ChatFormatter.getChatDisplayShort(overheatTemperature, DisplayUnit.TEMPERATURE_CELCIUS)).withStyle(ChatFormatting.YELLOW));
		if (IItemTemperate.getTemperature(stack) >= getOverheatTemp()) {
			tooltip.add(ElectroTextUtils.tooltip("railgunoverheat").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
		}
	}

	@Override
	public void addCreativeModeItems(CreativeModeTab group, List<ItemStack> items) {
		List<ItemStack> superItems = new ArrayList<>();
		super.addCreativeModeItems(group, superItems);
		for (ItemStack stack : superItems) {
			if (stack.getItem() instanceof ItemRailgun) {
				IItemTemperate.setTemperature(stack, 0);
			}
		}
		items.addAll(superItems);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		((ItemRailgun) stack.getItem()).loseHeat(stack, tempPerTick, 0.0, false);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}

	public double getMaxTemp() {
		return overheatTemperature;
	}

	public double getOverheatTemp() {
		return overheatTemperature * tempThreshold;
	}

	@Override
	public TemperateItemProperties getTemperteProperties() {
		return temperateProperties;
	}

}