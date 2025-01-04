package electrodynamics.client.screen.tile;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.inventory.container.tile.ContainerMotorComplex;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.machines.quarry.TileMotorComplex;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

public class ScreenMotorComplex extends GenericScreen<ContainerMotorComplex> {

	public ScreenMotorComplex(ContainerMotorComplex container, Inventory inv, Component titleIn) {
		super(container, inv, titleIn);
		addComponent(new ScreenComponentElectricInfo(this::getElectricInformation, -AbstractScreenComponentInfo.SIZE + 1, 2));
		addComponent(new ScreenComponentSimpleLabel(30, 40, 10, Color.TEXT_GRAY, () -> {
			int blocksPerTick = 0;
			TileMotorComplex motor = menu.getSafeHost();
			if (motor != null && motor.isPowered.get()) {
				blocksPerTick = motor.speed.get();
			}
			return ElectroTextUtils.gui("motorcomplex.speed", blocksPerTick);
		}));
	}

	private List<? extends FormattedCharSequence> getElectricInformation() {
		ArrayList<FormattedCharSequence> list = new ArrayList<>();
		TileMotorComplex motor = menu.getSafeHost();
		if (motor == null) {
			return list;
		}

		ComponentElectrodynamic electro = motor.getComponent(IComponentType.Electrodynamic);
		list.add(ElectroTextUtils.gui("machine.usage", ChatFormatter.getChatDisplayShort(Constants.MOTORCOMPLEX_USAGE_PER_TICK * motor.powerMultiplier.get() * 20, DisplayUnit.WATT).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
		list.add(ElectroTextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(electro.getVoltage(), DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());

		return list;
	}

}
