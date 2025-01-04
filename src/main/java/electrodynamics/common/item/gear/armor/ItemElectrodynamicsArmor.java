package electrodynamics.common.item.gear.armor;

import java.util.List;

import electrodynamics.api.creativetab.CreativeTabSupplier;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemElectrodynamicsArmor extends ArmorItem implements CreativeTabSupplier {

	private final Holder<CreativeModeTab> creativeTab;

	public ItemElectrodynamicsArmor(Holder<ArmorMaterial> material, Type type, Properties properties, Holder<CreativeModeTab> creativeTab) {
		super(material, type, properties);
		this.creativeTab = creativeTab;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);

		if(slotId > 35 && slotId < 40 && entity instanceof Player player){
			onWearingTick(stack, level, player, slotId, isSelected);
		}
	}

	public void onWearingTick(ItemStack stack, Level level, Player player, int slotId, boolean isSelected) {

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
