package electrodynamics.common.item.gear.tools.electric;

import electrodynamics.api.item.IItemTemperate;
import electrodynamics.common.entity.projectile.EntityCustomProjectile;
import electrodynamics.common.entity.projectile.types.EntityEnergyBlast;
import electrodynamics.common.item.gear.tools.electric.utils.ItemRailgun;
import electrodynamics.prefab.item.ElectricItemProperties;
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

public class ItemRailgunPlasma extends ItemRailgun {

	public static final double JOULES_PER_SHOT = 250000.0;
	private static final int OVERHEAT_TEMPERATURE = 1250;
	private static final int TEMPERATURE_PER_SHOT = 300;
	private static final double TEMPERATURE_REDUCED_PER_TICK = 2.5;
	private static final double OVERHEAT_WARNING_THRESHOLD = 0.5;

	public ItemRailgunPlasma(ElectricItemProperties properties, Holder<CreativeModeTab> creativeTab) {
		super(properties, creativeTab, OVERHEAT_TEMPERATURE, OVERHEAT_WARNING_THRESHOLD, TEMPERATURE_REDUCED_PER_TICK, item -> ElectrodynamicsItems.ITEM_CARBYNEBATTERY.get());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack gunStack;

		if (handIn == InteractionHand.MAIN_HAND) {
			gunStack = playerIn.getMainHandItem();
		} else {
			gunStack = playerIn.getOffhandItem();
		}

		if (worldIn.isClientSide) {
			return InteractionResultHolder.pass(gunStack);
		}

		ItemRailgunPlasma railgun = (ItemRailgunPlasma) gunStack.getItem();

		if (railgun.getJoulesStored(gunStack) < JOULES_PER_SHOT || IItemTemperate.getTemperature(gunStack) > OVERHEAT_TEMPERATURE - TEMPERATURE_PER_SHOT) {
			worldIn.playSound(null, playerIn.blockPosition(), ElectrodynamicsSounds.SOUND_RAILGUNKINETIC_NOAMMO.get(), SoundSource.PLAYERS, 1, 1);
			return InteractionResultHolder.pass(gunStack);
		}

		EntityCustomProjectile projectile = new EntityEnergyBlast(playerIn, worldIn);
		projectile.setNoGravity(true);
		projectile.setOwner(playerIn);
		shootNoY(projectile, playerIn, playerIn.getXRot(), playerIn.getYRot(), 0F, 5f, 1.0F);
		worldIn.addFreshEntity(projectile);

		railgun.extractPower(gunStack, JOULES_PER_SHOT, false);
		worldIn.playSound(null, playerIn.blockPosition(), ElectrodynamicsSounds.SOUND_RAILGUNPLASMA_FIRE.get(), SoundSource.PLAYERS, 1, 1);
		railgun.recieveHeat(gunStack, TEMPERATURE_PER_SHOT, false);

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
