package electrodynamics.common.item;

import java.util.List;

import electrodynamics.api.creativetab.CreativeTabSupplier;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemBoneMeal extends BoneMealItem implements CreativeTabSupplier {

	private final Holder<CreativeModeTab> creativeTab;

	public ItemBoneMeal(Properties properties, Holder<CreativeModeTab> creativeTab) {
		super(properties);
		this.creativeTab = creativeTab;
	}

	@Override
	public void addCreativeModeItems(CreativeModeTab tab, List<ItemStack> items) {
		items.add(new ItemStack(this));

	}

	@Override
	public boolean isAllowedInCreativeTab(CreativeModeTab tab) {
		return creativeTab.value() == tab;
	}

	@Override
	public boolean hasCreativeTab() {
		return creativeTab != null;
	}

}
