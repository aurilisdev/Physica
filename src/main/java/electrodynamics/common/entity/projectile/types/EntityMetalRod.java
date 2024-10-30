package electrodynamics.common.entity.projectile.types;

import electrodynamics.common.entity.projectile.EntityCustomProjectile;
import electrodynamics.common.item.subtype.SubtypeRod;
import electrodynamics.registers.ElectrodynamicsDamageTypes;
import electrodynamics.registers.ElectrodynamicsEntities;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntityMetalRod extends EntityCustomProjectile {

	/*
	 * 0 = Steel 1 = Stainless Steel 2 = HSLA Steel
	 */
	private int number = 0;

	public EntityMetalRod(EntityType<? extends ThrowableItemProjectile> type, Level world) {
		super(ElectrodynamicsEntities.ENTITY_METALROD.get(), world);
	}

	public EntityMetalRod(LivingEntity entity, Level world, int number) {
		super(ElectrodynamicsEntities.ENTITY_METALROD.get(), entity, world);
		this.number = number;
	}

	public EntityMetalRod(double x, double y, double z, Level worldIn, int number) {
		super(ElectrodynamicsEntities.ENTITY_METALROD.get(), x, y, z, worldIn);
		this.number = number;
	}

	@Override
	public void setPos(double x, double y, double z) {
		super.setPos(x, y, z);
	}

	@Override
	protected void onHitBlock(BlockHitResult hit) {
		BlockState state = level().getBlockState(hit.getBlockPos());
		if (!state.isAir()) {
			if (!level().isClientSide) {
				// Hardness of obsidian
				if (state.getDestroySpeed(level(), hit.getBlockPos()) < 50f && !state.is(Blocks.BEDROCK)) {
					level().removeBlock(hit.getBlockPos(), false);
				}
				level().playSound(null, hit.getBlockPos(), ElectrodynamicsSounds.SOUND_RODIMPACTINGGROUND.get(), SoundSource.BLOCKS, 1f, 1f);
			}
			remove(Entity.RemovalReason.DISCARDED);
		}
	}

	@Override
	public void onHitEntity(EntityHitResult hit) {
		Entity entity = hit.getEntity();
		switch (number) {
		case 0:
			entity.hurt(entity.damageSources().source(ElectrodynamicsDamageTypes.ACCELERATED_BOLT, this, entity), 16f);
			break;
		case 1:
			entity.hurt(entity.damageSources().source(ElectrodynamicsDamageTypes.ACCELERATED_BOLT, this, entity), 20f);
			break;
		case 2:
			entity.hurt(entity.damageSources().source(ElectrodynamicsDamageTypes.ACCELERATED_BOLT_IGNOREARMOR, this, entity), 4f);
			break;
		default:
		}
		super.onHitEntity(hit);
	}

	@Override
	protected Item getDefaultItem() {
		switch (number) {
		case 0:
			return ElectrodynamicsItems.ITEMS_ROD.getValue(SubtypeRod.steel);
		case 1:
			return ElectrodynamicsItems.ITEMS_ROD.getValue(SubtypeRod.stainlesssteel);
		case 2:
			return ElectrodynamicsItems.ITEMS_ROD.getValue(SubtypeRod.hslasteel);
		default:
			return super.getDefaultItem();
		}
	}

	public int getNumber() {
		return number;
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeInt(number);
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
		super.readSpawnData(additionalData);
		number = additionalData.readInt();
	}

}
