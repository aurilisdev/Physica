package electrodynamics.common.item.gear.tools;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.item.ItemElectrodynamics;
import electrodynamics.common.network.type.ElectricNetwork;
import electrodynamics.common.tile.electricitygrid.TileWire;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemMultimeter extends ItemElectrodynamics {

	public ItemMultimeter(Properties properties, Holder<CreativeModeTab> creativeTab) {
		super(properties, creativeTab);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {

		if (context.getLevel().isClientSide) {
			return super.useOn(context);
		}

		BlockEntity tile = context.getLevel().getBlockEntity(context.getClickedPos());
		if (tile instanceof TileWire wire) {
			ElectricNetwork net = wire.getNetwork();

			MutableComponent display = Component.empty();

			// active current to max current ratio
			double transferAmps = net.getActiveVoltage() == 0 ? 0 : net.getActiveVoltage() == 0 ? 0 : net.getActiveTransmitted() * 20 / net.getActiveVoltage();
			display.append(ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(transferAmps, DisplayUnit.AMPERE), ChatFormatter.getChatDisplayShort(net.networkMaxTransfer, DisplayUnit.AMPERE)));
			display.append(", ");

			// active voltage
			display.append(ChatFormatter.getChatDisplayShort(net.getActiveVoltage(), DisplayUnit.VOLTAGE));
			display.append(", ");

			// active power
			display.append(ChatFormatter.getChatDisplayShort(net.getActiveTransmitted() * 20, DisplayUnit.WATT));
			display.append(", ");

			// resistance and energy loss
			display.append(ChatFormatter.getChatDisplayShort(net.getResistance(), DisplayUnit.RESISTANCE).append(" ( -").append(ChatFormatter.getChatDisplayShort(Math.round(net.getLastEnergyLoss() / net.getActiveTransmitted() * 100.0), DisplayUnit.PERCENTAGE)).append(" ").append(ChatFormatter.getChatDisplayShort(net.getLastEnergyLoss() * 20, DisplayUnit.WATT).append(" )")));
			display.append(", ");

			// minimum voltage
			double minimumVoltage = net.getMinimumVoltage();
			if (minimumVoltage < 0) {
				minimumVoltage = net.getActiveVoltage();
			}
			display.append(ChatFormatter.getChatDisplayShort(minimumVoltage, DisplayUnit.VOLTAGE));

			context.getPlayer().displayClientMessage(display, true);
		}
		return super.useOn(context);
	}
}