package electrodynamics.common.item.gear.tools.electric.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import electrodynamics.api.References;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.fluid.FluidStackComponent;
import electrodynamics.api.item.IItemTemperate;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.item.ItemElectric;
import electrodynamics.prefab.item.TemperateItemProperties;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.fluids.FluidStack;

public abstract class ItemRailgun extends ItemElectric implements IItemTemperate {

	private static final List<ItemRailgun> ITEMS = new ArrayList<>();

	public static final Color[]  HEAT_COLORS = {new Color(183, 183, 183, 255), new Color(102, 0, 0, 255), new Color(152, 1, 0, 255), new Color(204, 0 ,1, 255), new Color(253, 51, 1, 255), new Color(255, 102, 51, 255), new Color(254, 154, 100, 255), new Color(255, 203, 102, 255), new Color(254, 204, 50, 255), new Color(255, 255, 101, 255), new Color(255, 255, 153, 255)};

	public static final int CAPACITY = 5000;

	private final TemperateItemProperties temperateProperties = new TemperateItemProperties();
	private double overheatTemperature = 0;
	private double tempThreshold = 0;
	private double tempPerTick = 0;

	public ItemRailgun(ElectricItemProperties properties, Holder<CreativeModeTab> creativeTab, double overheatTemperature, double tempThreshold, double tempPerTick, Function<Item, Item> getBatteryItem) {
		super(properties, creativeTab, getBatteryItem);
		this.overheatTemperature = overheatTemperature;
		this.tempThreshold = tempThreshold;
		this.tempPerTick = tempPerTick;
		ITEMS.add(this);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, context, tooltip, flagIn);
		tooltip.add(ElectroTextUtils.tooltip("railguntemp", ChatFormatter.getChatDisplayShort(IItemTemperate.getTemperature(stack), DisplayUnit.TEMPERATURE_CELCIUS)).withStyle(ChatFormatting.YELLOW));
		tooltip.add(ElectroTextUtils.tooltip("railgunmaxtemp", ChatFormatter.getChatDisplayShort(overheatTemperature, DisplayUnit.TEMPERATURE_CELCIUS)).withStyle(ChatFormatting.YELLOW));
		if (IItemTemperate.getTemperature(stack) >= getOverheatTemp()) {
			tooltip.add(ElectroTextUtils.tooltip("railgunoverheat").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
		}
		FluidStack fluid = stack.getOrDefault(ElectrodynamicsDataComponentTypes.FLUID_STACK.get(), FluidStackComponent.EMPTY).fluid;
		tooltip.add(ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(fluid.getAmount()), ChatFormatter.formatFluidMilibuckets(CAPACITY)).withStyle(ChatFormatting.GRAY));
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

	public static Predicate<FluidStack> getPredicate() {
		return fluid -> fluid.getFluid().builtInRegistryHolder().is(ElectrodynamicsTags.Fluids.AMMONIA);
	}

	@EventBusSubscriber(value = Dist.CLIENT, modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
	private static class ColorHandler {

		@SubscribeEvent
		public static void registerColoredBlocks(RegisterColorHandlersEvent.Item event) {
			ITEMS.forEach(item -> event.register((stack, index) -> {
				if (index != 1) {
					return Color.WHITE.color();
				}

				double currHeat = IItemTemperate.getTemperature(stack);

				if(currHeat <= 0){
					return HEAT_COLORS[0].color();
				}

				double maxHeat = item.getMaxTemp();

				double amtPerTier = maxHeat / HEAT_COLORS.length;

				int threshhold = (int) (currHeat / amtPerTier);

				if(threshhold == HEAT_COLORS.length - 1){
					return HEAT_COLORS[threshhold].color();
				}

				double amtNextTier = (currHeat - amtPerTier * threshhold) / amtPerTier;

				return HEAT_COLORS[threshhold].blend(HEAT_COLORS[threshhold + 1], amtNextTier).color();


			}, item));
		}

	}

}