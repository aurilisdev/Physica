package electrodynamics.common.item.gear.tools.electric;

import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import electrodynamics.api.creativetab.CreativeTabSupplier;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemMechanizedCrossbow extends ProjectileWeaponItem implements IItemElectric, CreativeTabSupplier {

	private final ElectricItemProperties properties;

	private final Holder<CreativeModeTab> creativeTab;

	public static final int JOULES_PER_SHOT = 5000;
	public static final int NUMBER_OF_SHOTS = 200;

	public static final int PROJECTILE_RANGE = 20;
	public static final int PROJECTILE_SPEED = 3;

	public ItemMechanizedCrossbow(ElectricItemProperties properties, Holder<CreativeModeTab> creativeTab) {
		super(properties);
		this.properties = properties;
		this.creativeTab = creativeTab;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack crossbow = player.getItemInHand(hand);

		if (world.isClientSide) {
			return InteractionResultHolder.pass(crossbow);
		}

		ItemMechanizedCrossbow mechanized = (ItemMechanizedCrossbow) crossbow.getItem();
		if (mechanized.getJoulesStored(crossbow) < JOULES_PER_SHOT) {
			world.playSound(null, player.blockPosition(), ElectrodynamicsSounds.SOUND_RAILGUNKINETIC_NOAMMO.get(), SoundSource.PLAYERS, 1, 1);
			return InteractionResultHolder.pass(crossbow);
		}

		ItemStack arrow = getAmmo(player);
		Projectile projectile = getArrow(world, player, crossbow, arrow);

		if (arrow.isEmpty()) {
			world.playSound(null, player.blockPosition(), ElectrodynamicsSounds.SOUND_RAILGUNKINETIC_NOAMMO.get(), SoundSource.PLAYERS, 1, 1);
			return InteractionResultHolder.pass(crossbow);
		}

		mechanized.extractPower(crossbow, JOULES_PER_SHOT, false);

		if (!player.isCreative()) {
			arrow.shrink(1);
		}

		Vec3 playerUpVector = player.getUpVector(1.0F);

		Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0, playerUpVector.x, playerUpVector.y, playerUpVector.z);

		Vec3 playerViewVector = player.getViewVector(1.0F);

		Vector3f viewVector = playerViewVector.toVector3f().rotate(quaternionf);

		projectile.shoot(viewVector.x(), viewVector.y(), viewVector.z(), PROJECTILE_SPEED, 1);

		world.addFreshEntity(projectile);

		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, 1);

		return InteractionResultHolder.pass(crossbow);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	private static AbstractArrow getArrow(Level world, LivingEntity entity, ItemStack crossbow, ItemStack ammo) {
		ArrowItem arrowitem = (ArrowItem) (ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
		AbstractArrow abstractarrow = arrowitem.createArrow(world, ammo, entity, crossbow);
		if (entity instanceof Player) {
			abstractarrow.setCritArrow(true);
		}

		abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);

		return abstractarrow;
	}

	private ItemStack getAmmo(Player player) {
		Inventory playerInv = player.getInventory();
		for (ItemStack stack : playerInv.items) {
			if (getAllSupportedProjectiles().test(stack)) {
				return stack;
			}
		}
		if (player.isCreative()) {
			return new ItemStack(Items.ARROW);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void addCreativeModeItems(CreativeModeTab group, List<ItemStack> items) {

		ItemStack empty = new ItemStack(this);
		IItemElectric.setEnergyStored(empty, 0);
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
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, context, tooltip, flagIn);
		tooltip.add(ElectroTextUtils.tooltip("item.electric.info", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(getJoulesStored(stack), DisplayUnit.JOULES), ChatFormatter.getChatDisplayShort(getMaximumCapacity(stack), DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		tooltip.add(ElectroTextUtils.tooltip("item.electric.voltage", ChatFormatter.getChatDisplayShort(properties.receive.getVoltage(), DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		IItemElectric.addBatteryTooltip(stack, context, tooltip);
	}

	@Override
	public ElectricItemProperties getElectricProperties() {
		return properties;
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return ARROW_ONLY;
	}

	@Override
	public int getDefaultProjectileRange() {
		return PROJECTILE_RANGE;
	}

	@Override
	protected void shootProjectile(LivingEntity pShooter, Projectile pProjectile, int pIndex, float pVelocity, float pInaccuracy, float pAngle, @Nullable LivingEntity pTarget) {

	}

	@Override
	public Item getDefaultStorageBattery() {
		return ElectrodynamicsItems.ITEM_BATTERY.get();
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {

		if (!IItemElectric.overrideOtherStackedOnMe(stack, other, slot, action, player, access)) {
			return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
		}

		return true;

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
