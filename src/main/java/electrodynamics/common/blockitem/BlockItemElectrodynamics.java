package electrodynamics.common.blockitem;

import java.util.List;

import electrodynamics.api.creativetab.CreativeTabSupplier;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class BlockItemElectrodynamics extends BlockItem implements CreativeTabSupplier {

	private final Holder<CreativeModeTab> creativeTab;

	public BlockItemElectrodynamics(Block block, Properties properties, Holder<CreativeModeTab> creativeTab) {
		super(block, properties);
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
