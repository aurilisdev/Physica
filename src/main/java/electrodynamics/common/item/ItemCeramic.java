package electrodynamics.common.item;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.common.item.subtype.SubtypeCeramic;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.registers.ElectrodynamicsCreativeTabs;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemCeramic extends ItemElectrodynamics {
	public SubtypeCeramic subtype;

	public ItemCeramic(SubtypeCeramic subtype) {
		super(new Item.Properties().stacksTo(64), ElectrodynamicsCreativeTabs.MAIN);
		this.subtype = subtype;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {

		ItemStack handStack = player.getItemInHand(hand);

		if (world.isClientSide || !ItemUtils.testItems(handStack.getItem(), ElectrodynamicsItems.ITEMS_CERAMIC.getValue(SubtypeCeramic.plate))) {
			return InteractionResultHolder.pass(player.getItemInHand(hand));
		}

		List<ItemStack> armorPieces = new ArrayList<>();
		player.getArmorSlots().forEach(armorPieces::add);

		ItemStack chestplate = armorPieces.get(2);
		if (chestplate.getItem() == ElectrodynamicsItems.ITEM_COMPOSITECHESTPLATE.get() || chestplate.getItem() == ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get()) {
			int stored = chestplate.getOrDefault(ElectrodynamicsDataComponentTypes.PLATES, 0);
			if (stored < 2) {
				world.playSound(null, player.getOnPos(), ElectrodynamicsSounds.SOUND_CERAMICPLATEADDED.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
				chestplate.set(ElectrodynamicsDataComponentTypes.PLATES, stored + 1);
				if (!player.isCreative()) {
					handStack.shrink(1);
					player.setItemInHand(hand, handStack);
				}
			}

		}
		return InteractionResultHolder.pass(player.getItemInHand(hand));
	}

}
