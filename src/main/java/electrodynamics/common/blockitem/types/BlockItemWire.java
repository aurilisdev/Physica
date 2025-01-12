package electrodynamics.common.blockitem.types;

import java.util.HashSet;
import java.util.List;

import electrodynamics.api.References;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.block.connect.BlockWire;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class BlockItemWire extends BlockItemDescriptable {

	private static HashSet<BlockItemWire> WIRES = new HashSet<>();

	private final BlockWire wire;

	public BlockItemWire(BlockWire wire, Properties builder, Holder<CreativeModeTab> creativeTab) {
		super(wire, builder, creativeTab);
		this.wire = wire;
		WIRES.add(this);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, context, tooltip, flagIn);
		tooltip.add(ElectroTextUtils.tooltip("itemwire.resistance", ChatFormatter.getChatDisplayShort(wire.wire.getResistance(), DisplayUnit.RESISTANCE)).withStyle(ChatFormatting.GRAY));
		tooltip.add(ElectroTextUtils.tooltip("itemwire.maxamps", ChatFormatter.getChatDisplayShort(wire.wire.getAmpacity(), DisplayUnit.AMPERE)).withStyle(ChatFormatting.GRAY));
		if (wire.wire.getInsulation().shockVoltage() == 0) {
			tooltip.add(ElectroTextUtils.tooltip("itemwire.info.uninsulated"));
		} else {
			tooltip.add(ElectroTextUtils.tooltip("itemwire.info.insulationrating", ChatFormatter.getChatDisplayShort(wire.wire.getInsulation().shockVoltage(), DisplayUnit.VOLTAGE)));
		}
		if (wire.wire.getInsulation().fireproof()) {
			ElectroTextUtils.tooltip("itemwire.info.fireproof");
		}
		if (wire.wire.getWireClass().conductsRedstone()) {
			ElectroTextUtils.tooltip("itemwire.info.redstone");
		}
	}

	@EventBusSubscriber(value = Dist.CLIENT, modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
	private static class ColorHandler {

		@SubscribeEvent
		public static void registerColoredBlocks(RegisterColorHandlersEvent.Item event) {
			WIRES.forEach(item -> event.register((stack, index) -> {
				if (index == 1) {
					return item.wire.wire.getWireColor().getColor().color();
				}
				return Color.WHITE.color();
			}, item));
		}

	}

}
