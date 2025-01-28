package electrodynamics.common.tile.machines.quarry;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerMotorComplex;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.sound.utils.ITickableSound;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsSounds;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileMotorComplex extends GenericTile implements ITickableSound {

	// 10 ticks per block
	public static final int DEFAULT_SPEED = Math.min(Constants.MIN_QUARRYBLOCKS_PER_TICK, 100);
	// 1 tick per block
	public static final int MAX_SPEED = Math.max(Constants.MAX_QUARRYBLOCKS_PER_TICK, 1);

	private boolean isSoundPlaying = false;

	public final Property<Integer> speed = property(new Property<>(PropertyTypes.INTEGER, "speed", DEFAULT_SPEED));
	public final Property<Double> powerMultiplier = property(new Property<>(PropertyTypes.DOUBLE, "powerMultiplier", 1.0));
	public final Property<Boolean> isPowered = property(new Property<>(PropertyTypes.BOOLEAN, "isPowered", false));

	public TileMotorComplex(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_MOTORCOMPLEX.get(), pos, state);
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickServer(this::tickServer).tickClient(this::tickClient));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.FRONT).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 2).maxJoules(Constants.MOTORCOMPLEX_USAGE_PER_TICK * 10000));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().upgrades(3)).validUpgrades(ContainerMotorComplex.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentContainerProvider(SubtypeMachine.motorcomplex, this).createMenu((id, player) -> new ContainerMotorComplex(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	private void tickServer(ComponentTickable tick) {
		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

		if (electro.getJoulesStored() >= Constants.MOTORCOMPLEX_USAGE_PER_TICK * powerMultiplier.get()) {
			electro.joules(electro.getJoulesStored() - Constants.MOTORCOMPLEX_USAGE_PER_TICK * powerMultiplier.get());
			isPowered.set(true);
		} else {
			isPowered.set(false);
		}
	}

	private void tickClient(ComponentTickable tick) {
		if (shouldPlaySound() && !isSoundPlaying) {
			isSoundPlaying = true;
			SoundBarrierMethods.playTileSound(ElectrodynamicsSounds.SOUND_MOTORRUNNING.get(), this, true);
		}
	}

	@Override
	public void onInventoryChange(ComponentInventory inv, int slot) {
		super.onInventoryChange(inv, slot);
		if (inv.getUpgradeContents().size() > 0 && (slot >= inv.getUpgradeSlotStartIndex() || slot == -1)) {
			speed.set(DEFAULT_SPEED);
			powerMultiplier.set(1.0);
			for (ItemStack stack : inv.getUpgradeContents()) {
				if (!stack.isEmpty()) {
					for (int i = 0; i < stack.getCount(); i++) {
						switch (((ItemUpgrade) stack.getItem()).subtype) {
						case basicspeed:
							speed.set((int) Math.max((double) speed.get() * 0.8, MAX_SPEED));
							powerMultiplier.set(powerMultiplier.get() * 3);
							break;
						case advancedspeed:
							speed.set((int) Math.max((double) speed.get() * 0.5, MAX_SPEED));
							powerMultiplier.set(powerMultiplier.get() * 2);
							break;
						default:
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void setNotPlaying() {
		isSoundPlaying = false;
	}

	@Override
	public boolean shouldPlaySound() {
		return isPowered.get();
	}

	@Override
	public int getComparatorSignal() {
		return isPowered.get() ? 15 : 0;
	}

}
