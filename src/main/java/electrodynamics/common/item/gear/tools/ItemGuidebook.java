package electrodynamics.common.item.gear.tools;

import java.util.List;
import java.util.function.Supplier;

import electrodynamics.common.inventory.container.item.ContainerGuidebook;
import electrodynamics.common.item.ItemElectrodynamics;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemGuidebook extends ItemElectrodynamics {

	private static final String LINK = "https://wiki.aurilis.dev";
	private static final Component CONTAINER_TITLE = Component.translatable("container.guidebook");

	public ItemGuidebook(Properties properties, Holder<CreativeModeTab> creativeTab) {
		super(properties, creativeTab);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag flag) {
		tooltips.add(ElectroTextUtils.tooltip("info.guidebookuse").withStyle(ChatFormatting.LIGHT_PURPLE));
		tooltips.add(ElectroTextUtils.tooltip("guidebookname").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltips, flag);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
		if (world.isClientSide) {
			if (player.isShiftKeyDown()) {
				player.sendSystemMessage(ElectroTextUtils.chatMessage("guidebookclick").withStyle(ChatFormatting.BOLD, ChatFormatting.RED).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, LINK))));
				return InteractionResultHolder.pass(player.getItemInHand(handIn));
			}
		} else {
			player.openMenu(getMenuProvider(world, player));
		}
		return super.use(world, player, handIn);
	}

	public MenuProvider getMenuProvider(Level world, Player player) {
		return new SimpleMenuProvider((id, inv, play) -> new ContainerGuidebook(id, player.getInventory()), CONTAINER_TITLE);
	}

}
