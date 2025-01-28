package electrodynamics.common.tile.machines;

import electrodynamics.Electrodynamics;
import electrodynamics.client.particle.lavawithphysics.ParticleOptionLavaWithPhysics;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerDO2OProcessor;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.sound.utils.ITickableSound;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsSounds;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;

public class TileEnergizedAlloyer extends GenericTile implements ITickableSound {

	private boolean isSoundPlaying = false;

	public TileEnergizedAlloyer(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_ENERGIZEDALLOYER.get(), worldPosition, blockState);
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickClient(this::tickClient));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BACK).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 4));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().processors(1, 2, 1, 1).upgrades(3)).setSlotsByDirection(BlockEntityUtils.MachineDirection.TOP, 0).setSlotsByDirection(BlockEntityUtils.MachineDirection.RIGHT, 1)
				//
				.setDirectionsBySlot(2, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT).setDirectionsBySlot(3, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.LEFT).validUpgrades(ContainerDO2OProcessor.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentContainerProvider(SubtypeMachine.energizedalloyer, this).createMenu((id, player) -> new ContainerDO2OProcessor(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));

		addComponent(new ComponentProcessor(this).canProcess(this::canProcessEnergAlloy).process(component -> component.processItem2ItemRecipe(component)));
	}

	protected boolean canProcessEnergAlloy(ComponentProcessor component) {
		boolean canProcess = component.canProcessItem2ItemRecipe(component, ElectrodynamicsRecipeInit.ENERGIZED_ALLOYER_TYPE.get());
		if (BlockEntityUtils.isLit(this) ^ canProcess) {
			BlockEntityUtils.updateLit(this, canProcess);
		}
		return canProcess;
	}

	protected void tickClient(ComponentTickable tickable) {
		if (!shouldPlaySound()) {
			return;
		}
		if (level.random.nextDouble() < 0.2) {
			Direction direction = getFacing();

			double axisShift = Electrodynamics.RANDOM.nextDouble(0.63) + 0.185;

			float scale = 0.1F * (level.random.nextFloat() * 0.5F + 0.5F) * 2.0F * (level.random.nextFloat() * 2.0F + 0.2F);
			int lifetime = (int)(4.0F / (level.random.nextFloat() * 0.9F + 0.1F));

			double xShift = direction.getAxis() == Direction.Axis.X ? direction.getStepX() * (direction.getStepX() == -0.5 ? 0 : 0.5) : axisShift;
			double yShift = 0.95;
			double zShift = direction.getAxis() == Direction.Axis.Z ? direction.getStepZ() * (direction.getStepZ() == -0.5 ? 0 : 0.5) : axisShift;

			double xVel = (Math.random() * 2.0 - 1.0) * 0.4F;
			double yVel = (Math.random()) * 0.4F;
			double zVel = (Math.random() * 2.0 - 1.0) * 0.4F;
			double d0 = (Math.random() + Math.random() + 1.0) * 0.15F;
			double d1 = Math.sqrt(xVel * xVel + yVel * yVel + zVel * zVel);
			xVel = xVel / d1 * d0 * 0.4F;
			yVel = yVel / d1 * d0 * 0.4F + 0.1F;
			zVel = zVel / d1 * d0 * 0.4F;

			level.addParticle(new ParticleOptionLavaWithPhysics().setParameters(scale, lifetime, 1), worldPosition.getX() + xShift, worldPosition.getY() + yShift, worldPosition.getZ() + zShift, xVel, yVel, zVel);

			int randInt = level.random.nextIntBetweenInclusive(0,2);

			axisShift = Electrodynamics.RANDOM.nextDouble(0.64) + 0.18;
			yShift = Electrodynamics.RANDOM.nextDouble(0.38) + 0.37;

			xShift = direction.getAxis() == Direction.Axis.X ? direction.getStepX() * (direction.getStepX() == -1 ? 0 : 1) : axisShift;
			zShift = direction.getAxis() == Direction.Axis.Z ? direction.getStepZ() * (direction.getStepZ() == -1 ? 0 : 1) : axisShift;

			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + xShift, worldPosition.getY() + yShift, worldPosition.getZ() + zShift, 0.0D, 0.0D, 0.0D);
			//level.addParticle(ParticleTypes.FLAME, worldPosition.getX() + xShift, worldPosition.getY() + yShift, worldPosition.getZ() + zShift, 0.0D, 0.0D, 0.0D);

			if(randInt == 1) {
				direction = direction.getClockWise();
			} else if (randInt == 2) {
				direction = direction.getCounterClockWise();
			}

			axisShift = Electrodynamics.RANDOM.nextDouble(0.64) + 0.18;
			yShift = Electrodynamics.RANDOM.nextDouble(0.38) + 0.37;

			xShift = direction.getAxis() == Direction.Axis.X ? direction.getStepX() * (direction.getStepX() == -1 ? 0 : 1) : axisShift;
			zShift = direction.getAxis() == Direction.Axis.Z ? direction.getStepZ() * (direction.getStepZ() == -1 ? 0 : 1) : axisShift;

			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + xShift, worldPosition.getY() + yShift, worldPosition.getZ() + zShift, 0.0D, 0.0D, 0.0D);
		}
		if (!isSoundPlaying) {
			isSoundPlaying = true;
			SoundBarrierMethods.playTileSound(ElectrodynamicsSounds.SOUND_HUM.get(), this, true);
		}
	}

	@Override
	public void setNotPlaying() {
		isSoundPlaying = false;
	}

	@Override
	public boolean shouldPlaySound() {
		return this.<ComponentProcessor>getComponent(IComponentType.Processor).isActive();
	}

	@Override
	public int getComparatorSignal() {
		return this.<ComponentProcessor>getComponent(IComponentType.Processor).isActive() ? 15 : 0;
	}

}
