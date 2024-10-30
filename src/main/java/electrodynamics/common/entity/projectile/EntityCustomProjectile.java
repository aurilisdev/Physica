package electrodynamics.common.entity.projectile;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public abstract class EntityCustomProjectile extends ThrowableItemProjectile implements IEntityWithComplexSpawn {

	public Vec3 clientDeltaX = Vec3.ZERO;

	protected EntityCustomProjectile(EntityType<? extends ThrowableItemProjectile> type, Level world) {
		super(type, world);
	}

	protected EntityCustomProjectile(EntityType<? extends ThrowableItemProjectile> type, LivingEntity entity, Level world) {
		super(type, entity, world);
	}

	protected EntityCustomProjectile(EntityType<? extends ThrowableItemProjectile> type, double x, double y, double z, Level worldIn) {
		super(type, x, y, z, worldIn);
	}

	@Override
	protected void onHitBlock(BlockHitResult par) {
		remove(Entity.RemovalReason.DISCARDED);
	}

	@Override
	public void onHitEntity(EntityHitResult par) {
		remove(Entity.RemovalReason.DISCARDED);
	}

	@Override
	protected Item getDefaultItem() {
		return Items.COBBLESTONE;
	}

	@Override
	public void tick() {
		if (level().isClientSide) {
			setDeltaMovement(clientDeltaX);
		}
		super.tick();
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
		buffer.writeDouble(getDeltaMovement().x);
		buffer.writeDouble(getDeltaMovement().y);
		buffer.writeDouble(getDeltaMovement().z);
		buffer.writeFloat(getXRot());
		buffer.writeFloat(getYRot());
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
		clientDeltaX = new Vec3(additionalData.readDouble(), additionalData.readDouble(), additionalData.readDouble());
		setXRot(additionalData.readFloat());
		setYRot(additionalData.readFloat());
	}

}
