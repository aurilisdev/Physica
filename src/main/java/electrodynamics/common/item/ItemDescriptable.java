package electrodynamics.common.item;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ItemDescriptable extends ItemElectrodynamics {

	private Component[] tooltips;

	public ItemDescriptable(Properties properties, Supplier<CreativeModeTab> creativeTab, Component... tooltips) {
		super(properties, creativeTab);
		this.tooltips = tooltips;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltips, flag);
		if (tooltips != null) {
			for (Component tooltip : this.tooltips) {
				tooltips.add(tooltip);
			}
		}
	}

}
