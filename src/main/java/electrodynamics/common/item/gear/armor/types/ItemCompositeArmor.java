package electrodynamics.common.item.gear.armor.types;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import electrodynamics.Electrodynamics;
import electrodynamics.common.item.gear.armor.ItemElectrodynamicsArmor;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsArmorMaterials;
import electrodynamics.registers.ElectrodynamicsCreativeTabs;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemCompositeArmor extends ItemElectrodynamicsArmor {

	public static final EnumMap<Type, Integer> DEFENSE_MAP = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(Type.HELMET, 6);
		map.put(Type.CHESTPLATE, 12);
		map.put(Type.LEGGINGS, 16);
		map.put(Type.BOOTS, 6);
	});

	public static final ResourceLocation ARMOR_TEXTURE_LOCATION = Electrodynamics.rl("textures/model/armor/compositearmor.png");

	public ItemCompositeArmor(Type slot) {
		super(ElectrodynamicsArmorMaterials.COMPOSITE_ARMOR, slot, new Item.Properties().stacksTo(1).fireResistant().setNoRepair().durability(2000), ElectrodynamicsCreativeTabs.MAIN);
	}


	@Override
	public void addCreativeModeItems(CreativeModeTab group, List<ItemStack> items) {
		super.addCreativeModeItems(group, items);

		if (getEquipmentSlot() == EquipmentSlot.CHEST) {
			ItemStack filled = new ItemStack(this);
			filled.set(ElectrodynamicsDataComponentTypes.PLATES, 2);
			items.add(filled);
		}

	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
		return 0;
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, context, tooltip, flagIn);
		if (((ArmorItem) stack.getItem()).getEquipmentSlot() == EquipmentSlot.CHEST) {
			staticAppendHoverText(stack, context, tooltip, flagIn);
		}
	}

	protected static void staticAppendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(ElectroTextUtils.tooltip("ceramicplatecount", Component.translatable(stack.getOrDefault(ElectrodynamicsDataComponentTypes.PLATES, 0) + "")).withStyle(ChatFormatting.AQUA));
	}


	@Override
	public void onWearingTick(ItemStack stack, Level world, Player player, int slotId, boolean isSelected) {
		super.onWearingTick(stack, world, player, slotId, isSelected);
		player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20));
	}

	@Override
	public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return ARMOR_TEXTURE_LOCATION;
	}

	/*
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return ARMOR_TEXTURE_LOCATION;
	}

	public enum CompositeArmor implements ICustomArmor {
		COMPOSITE_ARMOR(References.ID + ":composite", new int[] { 6, 12, 16, 6 }, 2.0f);

		private final String name;
		private final int[] damageReductionAmountArray;
		private final float toughness;

		// Constructor
		CompositeArmor(String name, int[] damageReductionAmountArray, float toughness) {
			this.name = name;
			this.damageReductionAmountArray = damageReductionAmountArray;
			this.toughness = toughness;
		}

		@Override
		public SoundEvent getEquipSound() {
			return ElectrodynamicsSounds.SOUND_EQUIPHEAVYARMOR.get();
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public float getToughness() {
			return toughness;
		}

		@Override
		public float getKnockbackResistance() {
			return 4;
		}

		@Override
		public int getDurabilityForType(Type type) {
			return 2000;
		}

		@Override
		public int getDefenseForType(Type type) {
			return damageReductionAmountArray[type.ordinal()];
		}

	}

	 */
}
