package electrodynamics.common.item.gear.tools.electric;

import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import electrodynamics.api.fluid.FluidStackComponent;
import electrodynamics.api.item.IItemTemperate;
import electrodynamics.common.entity.projectile.EntityCustomProjectile;
import electrodynamics.common.entity.projectile.types.EntityMetalRod;
import electrodynamics.common.item.gear.tools.electric.utils.ItemRailgun;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;

public class ItemRailgunKinetic extends ItemRailgun {

    private static final List<Ingredient> RAILGUN_AMMO = List.of(Ingredient.of(ElectrodynamicsTags.Items.ROD_STEEL), Ingredient.of(ElectrodynamicsTags.Items.ROD_STAINLESSSTEEL), Ingredient.of(ElectrodynamicsTags.Items.ROD_HSLASTEEL));

    public static final double JOULES_PER_SHOT = 100000.0;
    private static final int OVERHEAT_TEMPERATURE = 400;
    public static final int TEMPERATURE_PER_SHOT = 300;
    private static final double TEMPERATURE_REDUCED_PER_TICK = 2.0;
    private static final double OVERHEAT_WARNING_THRESHOLD = 0.75;

    public static final int COOLANT_PER_SHOT = 200;

    public ItemRailgunKinetic(ElectricItemProperties properties, Holder<CreativeModeTab> creativeTab) {
        super(properties, creativeTab, OVERHEAT_TEMPERATURE, OVERHEAT_WARNING_THRESHOLD, TEMPERATURE_REDUCED_PER_TICK, item -> ElectrodynamicsItems.ITEM_LITHIUMBATTERY.get());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack gunStack;
        ItemStack ammoStack;

        if (hand == InteractionHand.MAIN_HAND) {
            gunStack = player.getMainHandItem();
            ammoStack = player.getOffhandItem();
        } else {
            gunStack = player.getOffhandItem();
            ammoStack = player.getMainHandItem();
        }

        if (world.isClientSide) {
            return InteractionResultHolder.pass(gunStack);
        }

        ItemRailgunKinetic railgun = (ItemRailgunKinetic) gunStack.getItem();

        if (railgun.getJoulesStored(gunStack) < JOULES_PER_SHOT || ammoStack.isEmpty() || IItemTemperate.getTemperature(gunStack) > OVERHEAT_TEMPERATURE - TEMPERATURE_PER_SHOT) {

            world.playSound(null, player.blockPosition(), ElectrodynamicsSounds.SOUND_RAILGUNKINETIC_NOAMMO.get(), SoundSource.PLAYERS, 1, 1);
            return InteractionResultHolder.pass(gunStack);

        }

        EntityCustomProjectile projectile = null;
        int i = 0;

        for (Ingredient ammo : RAILGUN_AMMO) {
            if (ammo.test(ammoStack)) {
                projectile = new EntityMetalRod(player, world, i);
                break;
            }
            i++;
        }

        if (projectile == null) {
            world.playSound(null, player.blockPosition(), ElectrodynamicsSounds.SOUND_RAILGUNKINETIC_NOAMMO.get(), SoundSource.PLAYERS, 1, 1);
            return InteractionResultHolder.pass(gunStack);
        }

        railgun.extractPower(gunStack, JOULES_PER_SHOT, false);
        world.playSound(null, player.blockPosition(), ElectrodynamicsSounds.SOUND_RAILGUNKINETIC.get(), SoundSource.PLAYERS, 1, 1);
        projectile.setItem(ammoStack);
        projectile.setNoGravity(true);
        projectile.setOwner(player);

        Vec3 vec31 = player.getUpVector(1.0F);

        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0, vec31.x, vec31.y, vec31.z);

        Vec3 playerViewVector = player.getViewVector(1.0F);

        Vector3f viewVector = playerViewVector.toVector3f().rotate(quaternionf);

        projectile.shoot(viewVector.x(), viewVector.y(), viewVector.z(), 10, 0.0F);

        world.addFreshEntity(projectile);

        FluidStack fluidStack = gunStack.getOrDefault(ElectrodynamicsDataComponentTypes.FLUID_STACK.get(), FluidStackComponent.EMPTY).fluid;

        if (fluidStack.isEmpty()) {
            railgun.recieveHeat(gunStack, TEMPERATURE_PER_SHOT, false);
        } else {
            if (fluidStack.getAmount() < COOLANT_PER_SHOT) {
                railgun.recieveHeat(gunStack, TEMPERATURE_PER_SHOT, false);
            } else {
                fluidStack.shrink(COOLANT_PER_SHOT);
                world.playSound(null, player.blockPosition(), ElectrodynamicsSounds.SOUND_PRESSURERELEASE.get(), SoundSource.PLAYERS, 0.25F, 1);
                gunStack.set(ElectrodynamicsDataComponentTypes.FLUID_STACK, new FluidStackComponent(fluidStack.copy()));
            }
        }


        if (!player.isCreative()) {
            ammoStack.shrink(1);
        }

        return InteractionResultHolder.pass(gunStack);
    }

}
