package electrodynamics.common.item.gear.armor.types;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

import electrodynamics.api.References;
import electrodynamics.client.ClientRegister;
import electrodynamics.client.render.model.armor.types.ModelCompositeArmor;
import electrodynamics.common.item.gear.armor.ItemElectrodynamicsArmor;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.*;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class ItemCompositeArmor extends ItemElectrodynamicsArmor {

	public static final EnumMap<Type, Integer> DEFENSE_MAP = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(Type.HELMET, 6);
		map.put(Type.CHESTPLATE, 12);
		map.put(Type.LEGGINGS, 16);
		map.put(Type.BOOTS, 6);
	});

	public static final String ARMOR_TEXTURE_LOCATION = References.ID + ":textures/model/armor/compositearmor.png";

	public ItemCompositeArmor(Type slot) {
		super(ElectrodynamicsArmorMaterials.COMPOSITE_ARMOR, slot, new Item.Properties().stacksTo(1).fireResistant().setNoRepair().durability(2000), () -> ElectrodynamicsCreativeTabs.MAIN.get());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> properties) {

				ItemStack[] armorPiecesArray = new ItemStack[] { new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITEHELMET.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITECHESTPLATE.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITELEGGINGS.get()), new ItemStack(ElectrodynamicsItems.ITEM_COMPOSITEBOOTS.get()) };

				List<ItemStack> armorPieces = new ArrayList<>();
				entity.getArmorSlots().forEach(armorPieces::add);

				boolean isBoth = armorPieces.get(0).getItem() == armorPiecesArray[3].getItem() && armorPieces.get(1).getItem() == armorPiecesArray[2].getItem();

				boolean hasChest = armorPieces.get(2).getItem() == armorPiecesArray[1].getItem();

				ModelCompositeArmor<LivingEntity> model;

				if (isBoth) {
					if (hasChest) {
						model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_COMB_CHEST.bakeRoot(), getEquipmentSlot());
					} else {
						model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_COMB_NOCHEST.bakeRoot(), getEquipmentSlot());
					}
				} else if (getEquipmentSlot() == EquipmentSlot.FEET) {
					model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_BOOTS.bakeRoot(), getEquipmentSlot());
				} else if (hasChest) {
					model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_LEG_CHEST.bakeRoot(), getEquipmentSlot());
				} else {
					model = new ModelCompositeArmor<>(ClientRegister.COMPOSITE_ARMOR_LAYER_LEG_NOCHEST.bakeRoot(), getEquipmentSlot());
				}

				model.crouching = properties.crouching;
				model.riding = properties.riding;
				model.young = properties.young;

				return model;
			}
		});
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
