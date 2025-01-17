package electrodynamics.common.item;

import java.text.DecimalFormat;
import java.util.List;

import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import org.apache.commons.lang3.StringUtils;

import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.NBTUtils;
import electrodynamics.registers.ElectrodynamicsCreativeTabs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemUpgrade extends ItemElectrodynamics {
	public final SubtypeItemUpgrade subtype;

	private static final DecimalFormat FORMATTER = new DecimalFormat("0.00");

	public ItemUpgrade(Properties properties, SubtypeItemUpgrade subtype) {
		super(properties.stacksTo(subtype.maxSize), ElectrodynamicsCreativeTabs.MAIN);
		this.subtype = subtype;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, context, tooltip, flagIn);
		if (subtype == SubtypeItemUpgrade.advancedcapacity || subtype == SubtypeItemUpgrade.basiccapacity) {
			double capacityMultiplier = subtype == SubtypeItemUpgrade.advancedcapacity ? 2.25 : 1.5;
			double voltageMultiplier = subtype == SubtypeItemUpgrade.advancedcapacity ? 4 : 2;
			tooltip.add(ElectroTextUtils.tooltip("info.upgradecapacity", Component.literal(capacityMultiplier + "x").withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
			tooltip.add(ElectroTextUtils.tooltip("info.upgradeenergytransfer", Component.literal(capacityMultiplier + "x").withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
			tooltip.add(ElectroTextUtils.tooltip("info.upgradevoltage", Component.literal(capacityMultiplier + "x").withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
		}
		if (subtype == SubtypeItemUpgrade.advancedspeed || subtype == SubtypeItemUpgrade.basicspeed) {
			double speedMultiplier = subtype == SubtypeItemUpgrade.advancedspeed ? 2.25 : 1.5;
			tooltip.add(ElectroTextUtils.tooltip("info.upgradespeed", Component.literal(speedMultiplier + "x").withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
			tooltip.add(ElectroTextUtils.tooltip("info.upgradeenergyusage", Component.literal(speedMultiplier + "x").withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.GRAY));
		}
		if (subtype == SubtypeItemUpgrade.itemoutput || subtype == SubtypeItemUpgrade.iteminput) {
			if (subtype == SubtypeItemUpgrade.itemoutput) {
				tooltip.add(ElectroTextUtils.tooltip("info.itemoutputupgrade").withStyle(ChatFormatting.GRAY));
			} else {
				tooltip.add(ElectroTextUtils.tooltip("info.iteminputupgrade").withStyle(ChatFormatting.GRAY));
			}
			if (stack.getOrDefault(ElectrodynamicsDataComponentTypes.SMART, false)) {
				tooltip.add(ElectroTextUtils.tooltip("info.insmartmode").withStyle(ChatFormatting.LIGHT_PURPLE));
			}
			List<Direction> dirs = NBTUtils.readDirectionList(stack);
			if (!dirs.isEmpty()) {
				tooltip.add(ElectroTextUtils.tooltip("info.dirlist").withStyle(ChatFormatting.BLUE));
				for (int i = 0; i < dirs.size(); i++) {
					Direction dir = dirs.get(i);
					tooltip.add(Component.literal(i + 1 + ". " + StringUtils.capitalize(dir.getName())).withStyle(ChatFormatting.BLUE));
				}
				tooltip.add(ElectroTextUtils.tooltip("info.cleardirs").withStyle(ChatFormatting.GRAY));
			} else {
				tooltip.add(ElectroTextUtils.tooltip("info.nodirs").withStyle(ChatFormatting.GRAY));
			}
			tooltip.add(ElectroTextUtils.tooltip("info.togglesmart").withStyle(ChatFormatting.GRAY));
		}
		if (subtype == SubtypeItemUpgrade.experience) {
			double storedXp = stack.getOrDefault(ElectrodynamicsDataComponentTypes.XP, 0.0);
			tooltip.add(ElectroTextUtils.tooltip("info.xpstored", Component.literal(FORMATTER.format(storedXp)).withStyle(ChatFormatting.LIGHT_PURPLE)).withStyle(ChatFormatting.GRAY));
			tooltip.add(ElectroTextUtils.tooltip("info.xpusage").withStyle(ChatFormatting.GRAY));

		}
		if (subtype == SubtypeItemUpgrade.range) {
			tooltip.add(ElectroTextUtils.tooltip("info.range").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (!world.isClientSide) {
			ItemStack handStack = player.getItemInHand(hand);
			SubtypeItemUpgrade localSubtype = ((ItemUpgrade) handStack.getItem()).subtype;
			if (localSubtype == SubtypeItemUpgrade.iteminput || localSubtype == SubtypeItemUpgrade.itemoutput) {
				if (player.isShiftKeyDown()) {
					Vec3 look = player.getLookAngle();
					Direction lookingDir = Direction.getNearest(look.x, look.y, look.z);
					List<Direction> dirs = NBTUtils.readDirectionList(handStack);
					dirs.add(lookingDir);
					NBTUtils.clearDirectionList(handStack);
					NBTUtils.writeDirectionList(dirs, handStack);
				} else {
					handStack.set(ElectrodynamicsDataComponentTypes.SMART, !handStack.getOrDefault(ElectrodynamicsDataComponentTypes.SMART, false));
				}
				return InteractionResultHolder.pass(player.getItemInHand(hand));
			}
			if (localSubtype == SubtypeItemUpgrade.experience) {
				double storedXp = handStack.getOrDefault(ElectrodynamicsDataComponentTypes.XP, 0.0);
				int takenXp = (int) storedXp;
				// it uses a Vec3 for some reason don't ask me why
				Vec3 playerPos = new Vec3(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
				ExperienceOrb.award((ServerLevel) world, playerPos, takenXp);
				handStack.set(ElectrodynamicsDataComponentTypes.XP, storedXp - takenXp);
				return InteractionResultHolder.pass(player.getItemInHand(hand));
			}
		}
		return super.use(world, player, hand);
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		if (!entity.level().isClientSide && entity.isShiftKeyDown()) {
			if (!entity.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() || !entity.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
				SubtypeItemUpgrade subtype = ((ItemUpgrade) stack.getItem()).subtype;
				if (subtype == SubtypeItemUpgrade.iteminput || subtype == SubtypeItemUpgrade.itemoutput) {
					NBTUtils.clearDirectionList(stack);
				}
			}
		}
		return super.onEntitySwing(stack, entity);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		SubtypeItemUpgrade subtype = ((ItemUpgrade) stack.getItem()).subtype;
		return stack.getOrDefault(ElectrodynamicsDataComponentTypes.SMART, false) || subtype == SubtypeItemUpgrade.fortune || subtype == SubtypeItemUpgrade.unbreaking || subtype == SubtypeItemUpgrade.silktouch;
	}

}
