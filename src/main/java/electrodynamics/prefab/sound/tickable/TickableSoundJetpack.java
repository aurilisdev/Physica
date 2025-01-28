package electrodynamics.prefab.sound.tickable;

import java.util.UUID;

import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.prefab.utilities.WorldUtils;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TickableSoundJetpack extends AbstractTickableSoundInstance {

	private static final int MAX_DISTANCE = 10;
	private UUID originId;
	private Player originPlayer;

	public TickableSoundJetpack(UUID originPlayer) {
		super(ElectrodynamicsSounds.SOUND_JETPACK.get(), SoundSource.PLAYERS, RandomSource.create());
		originId = originPlayer;
		volume = 0.5F;
		pitch = 1.0F;
		looping = true;
	}

	@Override
	public void tick() {
		originPlayer = Minecraft.getInstance().level.getPlayerByUUID(originId);
		if (checkStop()) {
			stop();
			return;
		}
		volume = getPlayedVolume();
		pitch = 1.0F;
	}

	public float getPlayedVolume() {
		ItemStack jetpack = originPlayer.getItemBySlot(EquipmentSlot.CHEST);
		if (jetpack.getOrDefault(ElectrodynamicsDataComponentTypes.USED, false)) {
			double distance = WorldUtils.distanceBetweenPositions(originPlayer.blockPosition(), Minecraft.getInstance().player.blockPosition());
			if (distance > 0 && distance <= MAX_DISTANCE) {
				return (float) (0.5F / distance);
			}
			if (distance <= MAX_DISTANCE) {
				return 0.5F;
			}
		}
		return 0;
	}

	protected boolean checkStop() {
		if (originPlayer == null || originPlayer.isRemoved()) {
			return true;
		}
		ItemStack jetpack = originPlayer.getItemBySlot(EquipmentSlot.CHEST);
		if (jetpack.isEmpty()) {
			return true;
		}
		if (!ItemUtils.testItems(jetpack.getItem(), ElectrodynamicsItems.ITEM_JETPACK.get())) {
			if (!ItemUtils.testItems(jetpack.getItem(), ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get())) {
				return true;
			}
		}

		return false;
	}

}
