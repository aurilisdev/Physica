package electrodynamics.common.item.gear.armor.types;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import electrodynamics.Electrodynamics;
import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.client.keys.KeyBinds;
import electrodynamics.common.item.gear.armor.ItemElectrodynamicsArmor;
import electrodynamics.common.packet.types.client.PacketRenderJetpackParticles;
import electrodynamics.common.packet.types.server.PacketJetpackFlightServer;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsArmorMaterials;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsCreativeTabs;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
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
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class ItemJetpack extends ItemElectrodynamicsArmor {

    // public static final Fluid EMPTY_FLUID = Fluids.EMPTY;

    public static final EnumMap<Type, Integer> DEFENSE_MAP = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(Type.HELMET, 0);
        map.put(Type.CHESTPLATE, 1);
        map.put(Type.LEGGINGS, 0);
        map.put(Type.BOOTS, 0);
    });

    public static final int MAX_CAPACITY = 30000;

    public static final int USAGE_PER_TICK = 1;
    public static final double VERT_SPEED_INCREASE = 0.5;
    public static final double TERMINAL_VERTICAL_VELOCITY = 1;
    public static final int MAX_PRESSURE = 4;
    public static final int MAX_TEMPERATURE = Gas.ROOM_TEMPERATURE;

    private static final ResourceLocation ARMOR_TEXTURE_LOCATION = Electrodynamics.rl("textures/model/armor/jetpack.png");

    public static final float OFFSET = 0.1F;

    public static final String DELTA_Y_KEY = "prevdeltay";
    public static final String WAS_HURT_KEY = "washurt";

    public ItemJetpack() {
        super(ElectrodynamicsArmorMaterials.JETPACK, Type.CHESTPLATE, new Item.Properties().stacksTo(1), ElectrodynamicsCreativeTabs.MAIN);
    }


    @Override
    public void addCreativeModeItems(CreativeModeTab tab, List<ItemStack> items) {
        super.addCreativeModeItems(tab, items);
        if (ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM == null) {

            return;

        }
        ItemStack full = new ItemStack(this);

        IGasHandlerItem handler = full.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (handler == null) {
            return;
        }

        GasStack gas = new GasStack(ElectrodynamicsGases.HYDROGEN.value(), MAX_CAPACITY, Gas.ROOM_TEMPERATURE, Gas.PRESSURE_AT_SEA_LEVEL);

        handler.fill(gas, GasAction.EXECUTE);

        items.add(full);

    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        staticAppendHoverText(stack, context, tooltip, flagIn);
        super.appendHoverText(stack, context, tooltip, flagIn);
    }

    public static void staticAppendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag flagIn) {
        if (ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM != null) {

            IGasHandlerItem handler = stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

            if (handler != null) {

                GasStack gas = handler.getGasInTank(0);
                // tooltips.add(gas.getGas().getDescription());
                if (gas.isEmpty()) {
                    tooltips.add(ElectroTextUtils.ratio(Component.literal("0"), ChatFormatter.formatFluidMilibuckets(MAX_CAPACITY)).withStyle(ChatFormatting.GRAY));
                } else {
                    tooltips.add(ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(gas.getAmount()), ChatFormatter.formatFluidMilibuckets(MAX_CAPACITY)).withStyle(ChatFormatting.GRAY));
                    tooltips.add(ChatFormatter.getChatDisplayShort(gas.getTemperature(), DisplayUnit.TEMPERATURE_KELVIN).withStyle(ChatFormatting.GRAY));
                    tooltips.add(ChatFormatter.getChatDisplayShort(gas.getPressure(), DisplayUnit.PRESSURE_ATM).withStyle(ChatFormatting.GRAY));
                }

            }

            if (Screen.hasShiftDown()) {
                tooltips.add(ElectroTextUtils.tooltip("maxpressure", ChatFormatter.getChatDisplayShort(MAX_PRESSURE, DisplayUnit.PRESSURE_ATM)).withStyle(ChatFormatting.GRAY));
                tooltips.add(ElectroTextUtils.tooltip("maxtemperature", ChatFormatter.getChatDisplayShort(MAX_TEMPERATURE, DisplayUnit.TEMPERATURE_KELVIN)).withStyle(ChatFormatting.GRAY));
            }
        }
        // cheesing sync issues one line of code at a time
        tooltips.add(getModeText(stack.getOrDefault(ElectrodynamicsDataComponentTypes.MODE, 0)));
    }

    public static Component getModeText(int mode) {
        return switch (mode) {
        case 0 -> ElectroTextUtils.tooltip("jetpack.mode").withStyle(ChatFormatting.DARK_GRAY).append(ElectroTextUtils.tooltip("jetpack.moderegular").withStyle(ChatFormatting.GREEN));
        case 1 -> ElectroTextUtils.tooltip("jetpack.mode").withStyle(ChatFormatting.DARK_GRAY).append(ElectroTextUtils.tooltip("jetpack.modehover").withStyle(ChatFormatting.AQUA));
        case 2 -> ElectroTextUtils.tooltip("jetpack.mode").withStyle(ChatFormatting.DARK_GRAY).append(ElectroTextUtils.tooltip("jetpack.modeelytra").withStyle(ChatFormatting.YELLOW));
        case 3 -> ElectroTextUtils.tooltip("jetpack.mode").withStyle(ChatFormatting.DARK_GRAY).append(ElectroTextUtils.tooltip("jetpack.modeoff").withStyle(ChatFormatting.RED));
        default -> Component.empty();
        };
    }

    @Override
    public void onWearingTick(ItemStack stack, Level level, Player player, int slotId, boolean isSelected) {
        super.onWearingTick(stack, level, player, slotId, isSelected);
        wearingTick(stack, level, player, OFFSET, false);
    }

    public static void wearingTick(ItemStack stack, Level world, Player player, float particleZ, boolean isCombat) {
        if (world.isClientSide) {

            ArmorItem item = (ArmorItem) stack.getItem();
            if (item.getEquipmentSlot() == EquipmentSlot.CHEST) {
                boolean isDown = KeyBinds.jetpackAscend.isDown();
                int mode = stack.getOrDefault(ElectrodynamicsDataComponentTypes.MODE, 0);

                IGasHandlerItem handler = stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

                boolean enoughFuel = handler == null ? false : handler.getGasInTank(0).getAmount() >= ItemJetpack.USAGE_PER_TICK;

                if (enoughFuel) {
                    int pressure = handler.getGasInTank(0).getPressure();
                    if (mode == 0 && isDown) {
                        double deltaY = moveWithJetpack(ItemJetpack.VERT_SPEED_INCREASE, ItemJetpack.TERMINAL_VERTICAL_VELOCITY * pressure, player, stack);
                        renderClientParticles(world, player, particleZ);
                        sendPacket(player, true, deltaY);
                    } else if (mode == 1 && isDown) {
                        double deltaY = moveWithJetpack(ItemJetpack.VERT_SPEED_INCREASE / 2.0 * pressure, ItemJetpack.TERMINAL_VERTICAL_VELOCITY / 2.0 * pressure, player, stack);
                        renderClientParticles(world, player, particleZ);
                        sendPacket(player, true, deltaY);
                    } else if (mode == 1 && player.getBlockStateOn().isAir()) {
                        double deltaY = hoverWithJetpack(pressure, player, stack);
                        renderClientParticles(world, player, particleZ);
                        sendPacket(player, true, deltaY);
                    } else if (mode == 2 && isDown) {
                        double deltaY = moveWithJetpack(ItemJetpack.VERT_SPEED_INCREASE / 4.0 * pressure, ItemJetpack.TERMINAL_VERTICAL_VELOCITY / 4.0 * pressure, player, stack);
                        sendPacket(player, true, deltaY);
                        // TODO elytra fuel particles?
                    } else {
                        sendPacket(player, false, player.getDeltaMovement().y);
                    }
                } else {
                    sendPacket(player, false, player.getDeltaMovement().y);
                }
            } else {
                sendPacket(player, false, player.getDeltaMovement().y);
            }
        } else {
            boolean hasRan = stack.getOrDefault(ElectrodynamicsDataComponentTypes.USED, false);
            if (hasRan) {
                drainHydrogen(stack);

                PacketDistributor.sendToPlayer((ServerPlayer) player, new PacketRenderJetpackParticles(player.getUUID(), isCombat));

                player.resetFallDistance();
            }
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack1, ItemStack stack2) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return staticIsBarVisible(stack);
    }

    public static boolean staticIsBarVisible(ItemStack stack) {

        IGasHandlerItem handler = stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (handler == null) {
            return false;
        }

        return !handler.getGasInTank(0).isEmpty();

    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return staticGetBarWidth(stack);
    }

    public static int staticGetBarWidth(ItemStack stack) {

        IGasHandlerItem handler = stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (handler == null) {
            return 13;
        }

        return (int) (13.0 * handler.getGasInTank(0).getAmount() / handler.getTankCapacity(0));

    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public static Predicate<GasStack> getGasValidator() {
        return gas -> gas.getGas().equals(ElectrodynamicsGases.HYDROGEN.value());
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return staticCanElytraFly(stack, entity);
    }

    public static boolean staticCanElytraFly(ItemStack stack, LivingEntity entity) {
        int mode = stack.getOrDefault(ElectrodynamicsDataComponentTypes.MODE, 0);

        if (mode != 2) {
            return false;
        }

        IGasHandlerItem handler = stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (handler == null) {
            return false;
        }

        return handler.getGasInTank(0).getAmount() >= ItemJetpack.USAGE_PER_TICK;

    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return staticElytraFlightTick(stack, entity, flightTicks);
    }

    public static boolean staticElytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (entity.level().isClientSide) {
            return true;
        }
        int nextFlightTick = flightTicks + 1;
        if (nextFlightTick % 10 == 0) {
            if (nextFlightTick % 20 == 0) {

                drainHydrogen(stack);

            }
            entity.gameEvent(net.minecraft.world.level.gameevent.GameEvent.ELYTRA_GLIDE);
        }
        return true;
    }

    protected static double moveWithJetpack(double speed, double termVelocity, Player player, ItemStack jetpack) {

        Vec3 movement = player.getDeltaMovement();

        // if (player.hasImpulse && wasEntityHurt(jetpack)) {
        // movement = new Vec3(movement.x, getPrevDeltaY(jetpack), movement.z);
        // }

        double newY = Math.min(movement.y + speed, termVelocity);
        Vec3 currMovement = new Vec3(movement.x, newY, movement.z);

        player.setDeltaMovement(currMovement);
        player.resetFallDistance();
        // we keep track of the previous delta y for impulses
        return currMovement.y;
    }

    protected static double hoverWithJetpack(double multiplier, Player player, ItemStack jetpack) {

        Vec3 movement = player.getDeltaMovement();

        if (player.hasImpulse && wasEntityHurt(jetpack)) {
            movement = new Vec3(movement.x, getPrevDeltaY(jetpack), movement.z);
        }

        if (player.isShiftKeyDown()) {
            movement = new Vec3(movement.x, -0.3 * multiplier, movement.z);
        } else {
            movement = new Vec3(movement.x, 0, movement.z);
        }
        player.setDeltaMovement(movement);
        player.resetFallDistance();
        // we keep track of the previous delta y for impulses
        return movement.y;
    }

    public static void renderClientParticles(Level world, Player player, float particleZ) {
        Vec3 worldPosition = player.position();
        float rad = (float) (processDeg(player.yBodyRot) / 180.0F * Math.PI);
        double cosY = Mth.cos(rad);
        double sinY = Mth.sin(rad);
        double xOffCos = cosY * 0.2;
        double xOffSin = sinY * 0.2;
        double zOffCos = cosY * particleZ;
        double zOffSin = sinY * particleZ;
        double xRight = worldPosition.x - xOffCos + zOffSin;
        double xLeft = worldPosition.x + xOffCos + zOffSin;
        double zRight = worldPosition.z - zOffCos - xOffSin;
        double zLeft = worldPosition.z - zOffCos + xOffSin;
        double y = worldPosition.y + (player.isShiftKeyDown() ? 0.5 : 0.8);
        for (int i = 0; i < 10; i++) {
            world.addParticle(ParticleTypes.FLAME, xRight, y, zRight, 0, -2D, 0);
            // world.sendParticles(ParticleTypes.FLAME, xRight, y , zRight, 0, 0.0D, -2D,
            // 0.0D, 2D);
        }
        for (int i = 0; i < 10; i++) {
            world.addParticle(ParticleTypes.FLAME, xLeft, y, zLeft, 0, -2D, 0);
            // world.sendParticles(ParticleTypes.FLAME, xLeft, y, zLeft, 0, 0.0D, -2D, 0.0D,
            // 2D);
        }
    }

    protected static void drainHydrogen(ItemStack stack) {

        IGasHandlerItem handler = stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (handler == null) {
            return;
        }

        handler.drain(ItemJetpack.USAGE_PER_TICK, GasAction.EXECUTE);

    }

    protected static void sendPacket(Player player, boolean state, double prevDeltaMove) {
        PacketDistributor.sendToServer(new PacketJetpackFlightServer(player.getUUID(), state, prevDeltaMove));
    }

    protected static double getPrevDeltaY(ItemStack stack) {
        return stack.getOrDefault(ElectrodynamicsDataComponentTypes.DELTA_Y, 0.0);
    }

    protected static boolean wasEntityHurt(ItemStack stack) {
        return stack.getOrDefault(ElectrodynamicsDataComponentTypes.HURT, false);
    }

    // we need to do this based upon some testing I did
    private static float processDeg(float deg) {
        if (deg > 180) {
            return deg - 360;
        }
        if (deg < 180) {
            return deg + 360;
        }
        return deg;
    }

    /*

    public enum Jetpack implements ICustomArmor {
        JETPACK;

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_IRON;
        }

        @Override
        public String getName() {
            return References.ID + ":jetpack";
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
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ARMOR_TEXTURE_LOCATION;
    }
}
