package electrodynamics.registers;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import electrodynamics.Electrodynamics;
import electrodynamics.api.References;
import electrodynamics.common.item.gear.armor.types.ItemCombatArmor;
import electrodynamics.common.item.gear.armor.types.ItemCompositeArmor;
import electrodynamics.common.item.gear.armor.types.ItemHydraulicBoots;
import electrodynamics.common.item.gear.armor.types.ItemJetpack;
import electrodynamics.common.item.gear.armor.types.ItemNightVisionGoggles;
import electrodynamics.common.item.gear.armor.types.ItemRubberArmor;
import electrodynamics.common.item.gear.armor.types.ItemServoLeggings;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, References.ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COMPOSITE_ARMOR = register("composite_armor", ItemCompositeArmor.DEFENSE_MAP, 0, 2.0F, 4.0F, ElectrodynamicsSounds.SOUND_EQUIPHEAVYARMOR, () -> Ingredient.EMPTY, Electrodynamics.rl("textures/model/armor/compositearmor.png"));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COMBAT_ARMOR = register("combat_armor", ItemCombatArmor.DEFENSE_MAP, 0, 2.0F, 4.0F, ElectrodynamicsSounds.SOUND_EQUIPHEAVYARMOR, () -> Ingredient.EMPTY, Electrodynamics.rl("textures/model/armor/combatarmor.png"));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> NVGS = register("nvgs", ItemNightVisionGoggles.DEFENSE_MAP, 0, 0, 0, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.EMPTY, Electrodynamics.rl("textures/model/armor/nightvisiongogglesoff.png"));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> JETPACK = register("jetpack", ItemJetpack.DEFENSE_MAP, 0, 0, 0, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.EMPTY, Electrodynamics.rl("textures/model/armor/jetpack.png"));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SERVO_LEGGINGS = register("server_leggings", ItemServoLeggings.DEFENSE_MAP, 0, 0, 0, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.EMPTY, Electrodynamics.rl("textures/model/armor/servoleggings.png"));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HYDRAULIC_BOOTS = register("hydraulic_boots", ItemHydraulicBoots.DEFENSE_MAP, 0, 0, 0, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.EMPTY, Electrodynamics.rl("textures/model/armor/hydraulicboots.png"));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> RUBBER_BOOTS = register("rubber_boots", ItemRubberArmor.DEFENSE_MAP, 0, 0, 0, SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of(ElectrodynamicsItems.ITEM_INSULATION.get()), Electrodynamics.rl("rubberarmor"));

    private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(String name, Map<ArmorItem.Type, Integer> slotMap, int enchantValue, float toughness, float knockbackResistance, Holder<SoundEvent> sound, Supplier<Ingredient> repairIngredient, ResourceLocation texture) {
        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(slotMap, enchantValue, sound, repairIngredient, List.of(new ArmorMaterial.Layer(texture)), toughness, knockbackResistance));
    }

}
