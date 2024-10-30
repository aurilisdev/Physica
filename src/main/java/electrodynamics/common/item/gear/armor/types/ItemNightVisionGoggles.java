package electrodynamics.common.item.gear.armor.types;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

import electrodynamics.api.References;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.client.ClientRegister;
import electrodynamics.client.render.model.armor.types.ModelNightVisionGoggles;
import electrodynamics.common.item.gear.armor.ItemElectrodynamicsArmor;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsArmorMaterials;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class ItemNightVisionGoggles extends ItemElectrodynamicsArmor implements IItemElectric {

	public static final EnumMap<Type, Integer> DEFENSE_MAP = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(Type.HELMET, 1);
		map.put(Type.CHESTPLATE, 0);
		map.put(Type.LEGGINGS, 0);
		map.put(Type.BOOTS, 0);
	});

	private final ElectricItemProperties properties;

	public static final int JOULES_PER_TICK = 5;
	public static final int DURATION_SECONDS = 12;

	private static final ResourceLocation ARMOR_TEXTURE_OFF = ResourceLocation.parse(References.ID + ":textures/model/armor/nightvisiongogglesoff.png");
	private static final ResourceLocation ARMOR_TEXTURE_ON = ResourceLocation.parse(References.ID + ":textures/model/armor/nightvisiongoggleson.png");

	public ItemNightVisionGoggles(ElectricItemProperties properties, Holder<CreativeModeTab> creativeTab) {
		super(ElectrodynamicsArmorMaterials.NVGS, Type.HELMET, properties, creativeTab);
		this.properties = properties;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> properties) {
				ModelNightVisionGoggles<LivingEntity> model = new ModelNightVisionGoggles<>(ClientRegister.NIGHT_VISION_GOGGLES.bakeRoot());

				model.crouching = properties.crouching;
				model.riding = properties.riding;
				model.young = properties.young;

				return model;
			}
		});
	}

	@Override
	public void onWearingTick(ItemStack stack, Level level, Player player, int slotId, boolean isSelected) {
		super.onWearingTick(stack, level, player, slotId, isSelected);
		wearingTick(stack, level, player);
	}

	protected static void wearingTick(ItemStack stack, Level world, Player player) {
		if (!world.isClientSide) {
			IItemElectric nvgs = (IItemElectric) stack.getItem();
			if (stack.getOrDefault(ElectrodynamicsDataComponentTypes.ON, false) && nvgs.getJoulesStored(stack) >= JOULES_PER_TICK) {
				nvgs.extractPower(stack, JOULES_PER_TICK, false);
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, DURATION_SECONDS * 20, 0, false, false, false));
			}
		}
	}

	@Override
	public ElectricItemProperties getElectricProperties() {
		return properties;
	}

	@Override
	public boolean isEnchantable(ItemStack p_41456_) {
		return false;
	}

	@Override
	public boolean isRepairable(ItemStack stack) {
		return false;
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
		return 0;
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
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, context, tooltip, flagIn);
		tooltip.add(ElectroTextUtils.tooltip("item.electric.info", ChatFormatter.getChatDisplayShort(getJoulesStored(stack), DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY));
		tooltip.add(ElectroTextUtils.tooltip("item.electric.voltage", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(properties.receive.getVoltage(), DisplayUnit.VOLTAGE), ChatFormatter.getChatDisplayShort(properties.extract.getVoltage(), DisplayUnit.VOLTAGE))).withStyle(ChatFormatting.RED));
		if (stack.getOrDefault(ElectrodynamicsDataComponentTypes.ON, false)) {
			tooltip.add(ElectroTextUtils.tooltip("nightvisiongoggles.status").withStyle(ChatFormatting.GRAY).append(ElectroTextUtils.tooltip("nightvisiongoggles.on").withStyle(ChatFormatting.GREEN)));
		} else {
			tooltip.add(ElectroTextUtils.tooltip("nightvisiongoggles.status").withStyle(ChatFormatting.GRAY).append(ElectroTextUtils.tooltip("nightvisiongoggles.off").withStyle(ChatFormatting.RED)));
		}
		IItemElectric.addBatteryTooltip(stack, context, tooltip);
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
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}

	@Override
	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		if (stack.getOrDefault(ElectrodynamicsDataComponentTypes.ON, false)) {
			return ARMOR_TEXTURE_ON;
		}
		return ARMOR_TEXTURE_OFF;
	}

	/*
	public enum NightVisionGoggles implements ICustomArmor {
		NVGS;

		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ARMOR_EQUIP_IRON;
		}

		@Override
		public String getName() {
			return References.ID + ":nvgs";
		}

		@Override
		public float getToughness() {
			return 0.0F;
		}

		@Override
		public float getKnockbackResistance() {
			return 0.0F;
		}

		@Override
		public int getDurabilityForType(Type pType) {
			return 100;
		}

		@Override
		public int getDefenseForType(Type pType) {
			return 1;
		}

	}

	 */

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

}
