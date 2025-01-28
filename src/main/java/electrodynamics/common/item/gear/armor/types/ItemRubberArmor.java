package electrodynamics.common.item.gear.armor.types;

import java.util.EnumMap;

import org.jetbrains.annotations.Nullable;

import electrodynamics.Electrodynamics;
import electrodynamics.common.item.gear.armor.ItemElectrodynamicsArmor;
import electrodynamics.registers.ElectrodynamicsArmorMaterials;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ItemRubberArmor extends ItemElectrodynamicsArmor {

	public static final EnumMap<Type, Integer> DEFENSE_MAP = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(Type.HELMET, 0);
		map.put(Type.CHESTPLATE, 0);
		map.put(Type.LEGGINGS, 0);
		map.put(Type.BOOTS, 2);
	});

	public static final ResourceLocation ARMOR_TEXTURE_LOCATION = Electrodynamics.rl("textures/model/armor/rubberarmor.png");

	public ItemRubberArmor(ArmorItem.Type type, Properties properties, Holder<CreativeModeTab> creativeTab) {
		super(ElectrodynamicsArmorMaterials.RUBBER_BOOTS, type, properties, creativeTab);
	}

	@Override
	public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return ARMOR_TEXTURE_LOCATION;
	}

	/*
	public enum ArmorMaterialRubber implements ArmorMaterial {
		rubber;

		@Override
		public int getEnchantmentValue() {
			return 0;
		}

		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ARMOR_EQUIP_LEATHER;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(ElectrodynamicsItems.ITEM_INSULATION.get());
		}

		@Override
		public String getName() {
			return super.name();
		}

		@Override
		public float getToughness() {
			return 0;
		}

		@Override
		public float getKnockbackResistance() {
			return 0;
		}

		@Override
		public int getDurabilityForType(Type pType) {
			return 100000;
		}

		@Override
		public int getDefenseForType(Type pType) {
			return 2;
		}

	}

	 */
}
