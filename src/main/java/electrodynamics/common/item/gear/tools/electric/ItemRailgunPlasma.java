package electrodynamics.common.item.gear.tools.electric;

import electrodynamics.api.fluid.FluidStackComponent;
import electrodynamics.api.item.IItemTemperate;
import electrodynamics.common.entity.projectile.EntityCustomProjectile;
import electrodynamics.common.entity.projectile.types.EntityEnergyBlast;
import electrodynamics.common.item.gear.tools.electric.utils.ItemRailgun;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;

public class ItemRailgunPlasma extends ItemRailgun {

	public static final double JOULES_PER_SHOT = 250000.0;
	private static final int OVERHEAT_TEMPERATURE = 1250;
	public static final int TEMPERATURE_PER_SHOT = 300;
	private static final double TEMPERATURE_REDUCED_PER_TICK = 2.5;
	private static final double OVERHEAT_WARNING_THRESHOLD = 0.5;

	public static final int COOLANT_PER_SHOT = 1000;

	public ItemRailgunPlasma(ElectricItemProperties properties, Holder<CreativeModeTab> creativeTab) {
		super(properties, creativeTab, OVERHEAT_TEMPERATURE, OVERHEAT_WARNING_THRESHOLD, TEMPERATURE_REDUCED_PER_TICK, item -> ElectrodynamicsItems.ITEM_CARBYNEBATTERY.get());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack gunStack;

		if (hand == InteractionHand.MAIN_HAND) {
			gunStack = player.getMainHandItem();
		} else {
			gunStack = player.getOffhandItem();
		}

		if (world.isClientSide) {
			return InteractionResultHolder.pass(gunStack);
		}

		ItemRailgunPlasma railgun = (ItemRailgunPlasma) gunStack.getItem();

		if (railgun.getJoulesStored(gunStack) < JOULES_PER_SHOT || IItemTemperate.getTemperature(gunStack) > OVERHEAT_TEMPERATURE - TEMPERATURE_PER_SHOT) {
			world.playSound(null, player.blockPosition(), ElectrodynamicsSounds.SOUND_RAILGUNKINETIC_NOAMMO.get(), SoundSource.PLAYERS, 1, 1);
			return InteractionResultHolder.pass(gunStack);
		}

		EntityCustomProjectile projectile = new EntityEnergyBlast(player, world);
		projectile.setNoGravity(true);
		projectile.setOwner(player);
		shootNoY(projectile, player, player.getXRot(), player.getYRot(), 0F, 5f, 1.0F);
		world.addFreshEntity(projectile);

		railgun.extractPower(gunStack, JOULES_PER_SHOT, false);
		world.playSound(null, player.blockPosition(), ElectrodynamicsSounds.SOUND_RAILGUNPLASMA_FIRE.get(), SoundSource.PLAYERS, 1, 1);
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

		return InteractionResultHolder.pass(gunStack);
	}

	private void shootNoY(Projectile projectile, Entity shooter, float xRot, float yRot, float zRot, float velocity, float inaccuracy) {
		float velX = -Mth.sin(yRot * ((float) Math.PI / 180F)) * Mth.cos(xRot * ((float) Math.PI / 180F));
		float velY = -Mth.sin((xRot + zRot) * ((float) Math.PI / 180F));
		float velZ = Mth.cos(yRot * ((float) Math.PI / 180F)) * Mth.cos(xRot * ((float) Math.PI / 180F));
		projectile.shoot(velX, velY, velZ, velocity, inaccuracy);
		Vec3 deltaMove = shooter.getDeltaMovement();
		projectile.setDeltaMovement(projectile.getDeltaMovement().add(deltaMove.x, 0.0D, deltaMove.z));
	}

}
