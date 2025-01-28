package electrodynamics.common.item.gear.armor.types;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import electrodynamics.Electrodynamics;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.common.entity.ElectrodynamicsAttributeModifiers;
import electrodynamics.common.item.gear.armor.ItemElectrodynamicsArmor;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsArmorMaterials;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemServoLeggings extends ItemElectrodynamicsArmor implements IItemElectric {

    public static final EnumMap<Type, Integer> DEFENSE_MAP = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(Type.HELMET, 0);
        map.put(Type.CHESTPLATE, 0);
        map.put(Type.LEGGINGS, 1);
        map.put(Type.BOOTS, 0);
    });
    public static final int JOULES_PER_TICK = 5;
    public static final int DURATION_SECONDS = 1;

    public static final float DEFAULT_VANILLA_STEPUP = 0.6F;

    private static final ResourceLocation ARMOR_TEXTURE = Electrodynamics.rl("textures/model/armor/servoleggings.png");

    final ElectricItemProperties properties;

    public ItemServoLeggings(ElectricItemProperties properties, Holder<CreativeModeTab> creativeTab) {
        super(ElectrodynamicsArmorMaterials.SERVO_LEGGINGS, Type.LEGGINGS, properties, creativeTab);
        this.properties = properties;
    }

    @Override
    public ElectricItemProperties getElectricProperties() {
        return properties;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
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
        tooltip.add(ElectroTextUtils.tooltip("item.electric.info", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(getJoulesStored(stack), DisplayUnit.JOULES), ChatFormatter.getChatDisplayShort(getMaximumCapacity(stack), DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(ElectroTextUtils.tooltip("item.electric.voltage", ChatFormatter.getChatDisplayShort(properties.receive.getVoltage(), DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
        staticAppendTooltips(stack, context, tooltip, flagIn);
    }

    protected static void staticAppendTooltips(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        IItemElectric.addBatteryTooltip(stack, context, tooltip);
        if (stack.getOrDefault(ElectrodynamicsDataComponentTypes.ON, false)) {
            tooltip.add(ElectroTextUtils.tooltip("nightvisiongoggles.status").withStyle(ChatFormatting.DARK_GRAY).append(ElectroTextUtils.tooltip("nightvisiongoggles.on").withStyle(ChatFormatting.GREEN)));
        } else {
            tooltip.add(ElectroTextUtils.tooltip("nightvisiongoggles.status").withStyle(ChatFormatting.DARK_GRAY).append(ElectroTextUtils.tooltip("nightvisiongoggles.off").withStyle(ChatFormatting.RED)));
        }

        tooltip.add(getModeText(stack.getOrDefault(ElectrodynamicsDataComponentTypes.MODE, 0)));
    }

    public static Component getModeText(int mode) {
        return switch (mode) {
            case 0 -> ElectroTextUtils.tooltip("jetpack.mode").withStyle(ChatFormatting.DARK_GRAY).append(ElectroTextUtils.tooltip("servolegs.step").withStyle(ChatFormatting.GREEN));
            case 1 -> ElectroTextUtils.tooltip("jetpack.mode").withStyle(ChatFormatting.DARK_GRAY).append(ElectroTextUtils.tooltip("servolegs.both").withStyle(ChatFormatting.AQUA));
            case 2 -> ElectroTextUtils.tooltip("jetpack.mode").withStyle(ChatFormatting.DARK_GRAY).append(ElectroTextUtils.tooltip("servolegs.speed").withStyle(ChatFormatting.GREEN));
            case 3 -> ElectroTextUtils.tooltip("jetpack.mode").withStyle(ChatFormatting.DARK_GRAY).append(ElectroTextUtils.tooltip("servolegs.none").withStyle(ChatFormatting.RED));
            default -> Component.literal("");
        };
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
    public void onWearingTick(ItemStack stack, Level level, Player player, int slotId, boolean isSelected) {
        super.onWearingTick(stack, level, player, slotId, isSelected);
        wearingTick(stack, level, player);
    }

    protected static void wearingTick(ItemStack stack, Level world, Player player) {
        if (!world.isClientSide) {
            IItemElectric legs = (IItemElectric) stack.getItem();
            if (stack.getOrDefault(ElectrodynamicsDataComponentTypes.ON, false) && legs.getJoulesStored(stack) >= JOULES_PER_TICK) {
                switch (stack.getOrDefault(ElectrodynamicsDataComponentTypes.MODE, 0)) {
                    case 0:
                        stack.set(ElectrodynamicsDataComponentTypes.RESET, false);
                        stack.set(ElectrodynamicsDataComponentTypes.SUCESS, true);
						player.getAttribute(Attributes.STEP_HEIGHT).addPermanentModifier(ElectrodynamicsAttributeModifiers.SERVO_LEGGINGS_STEP);
                        legs.extractPower(stack, JOULES_PER_TICK, false);
                        break;
                    case 1:
                        stack.set(ElectrodynamicsDataComponentTypes.RESET, false);
                        stack.set(ElectrodynamicsDataComponentTypes.SUCESS, true);
                        player.getAttribute(Attributes.STEP_HEIGHT).addPermanentModifier(ElectrodynamicsAttributeModifiers.SERVO_LEGGINGS_STEP);
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, DURATION_SECONDS * 20, 0, false, false, false));
                        legs.extractPower(stack, JOULES_PER_TICK, false);
                        break;
                    case 2:
                        stack.set(ElectrodynamicsDataComponentTypes.RESET, false);
                        stack.set(ElectrodynamicsDataComponentTypes.SUCESS, false);
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, DURATION_SECONDS * 20, 0, false, false, false));
                        legs.extractPower(stack, JOULES_PER_TICK, false);
                        break;
                    case 3:
                        stack.set(ElectrodynamicsDataComponentTypes.SUCESS, false);
                        if (!stack.getOrDefault(ElectrodynamicsDataComponentTypes.RESET, false)) {
                            player.getAttribute(Attributes.STEP_HEIGHT).removeModifier(ElectrodynamicsAttributeModifiers.SERVO_LEGGINGS_STEP);
                        }
                        break;
                    default:
                        break;
                }
            } else {
                stack.set(ElectrodynamicsDataComponentTypes.SUCESS, false);
                if (!stack.getOrDefault(ElectrodynamicsDataComponentTypes.RESET, false)) {
                    player.getAttribute(Attributes.STEP_HEIGHT).removeModifier(ElectrodynamicsAttributeModifiers.SERVO_LEGGINGS_STEP);

                }
            }
        } else if (stack.getOrDefault(ElectrodynamicsDataComponentTypes.RESET, false)) {
            switch (stack.getOrDefault(ElectrodynamicsDataComponentTypes.MODE, 0)) {
                case 0, 1:
                    player.getAttribute(Attributes.STEP_HEIGHT).addPermanentModifier(ElectrodynamicsAttributeModifiers.SERVO_LEGGINGS_STEP);
                    break;
                case 2, 3:
                    if (!stack.getOrDefault(ElectrodynamicsDataComponentTypes.RESET, false)) {
                        player.getAttribute(Attributes.STEP_HEIGHT).removeModifier(ElectrodynamicsAttributeModifiers.SERVO_LEGGINGS_STEP);
                    }
                    break;
                default:
                    break;
            }
        } else if (!stack.getOrDefault(ElectrodynamicsDataComponentTypes.RESET, false)) {
            player.getAttribute(Attributes.STEP_HEIGHT).removeModifier(ElectrodynamicsAttributeModifiers.SERVO_LEGGINGS_STEP);
        }

    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

	/*

	public enum ServoLeggings implements ICustomArmor {
		SERVOLEGGINGS;

		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ARMOR_EQUIP_IRON;
		}

		@Override
		public String getName() {
			return References.ID + ":servoleggings";
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

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ARMOR_TEXTURE;
    }
}
