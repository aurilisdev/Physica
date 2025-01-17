package electrodynamics.common.blockitem.types;

import java.util.List;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.block.connect.BlockGasPipe;
import electrodynamics.common.blockitem.BlockItemElectrodynamics;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class BlockItemGasPipe extends BlockItemElectrodynamics {

	private final BlockGasPipe pipe;

	public BlockItemGasPipe(BlockGasPipe pipe, Properties properties, Holder<CreativeModeTab> creativeTab) {
		super(pipe, properties, creativeTab);
		this.pipe = pipe;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag advanced) {
		super.appendHoverText(stack, context, tooltips, advanced);
		//tooltips.add(ElectroTextUtils.tooltip("pipematerial", pipe.pipe.getPipeMaterial().getName().copy().withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		tooltips.add(ElectroTextUtils.tooltip("pipethroughput", ChatFormatter.getChatDisplayShort(pipe.pipe.getMaxTransfer() / 1000.0, DisplayUnit.BUCKETS).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		// tooltips.add(TextUtils.tooltip("pipeinsulationmaterial", pipe.pipe.insulationMaterial.getTranslatedName()).withStyle(ChatFormatting.GRAY));
		tooltips.add(ElectroTextUtils.tooltip("pipemaximumpressure", ChatFormatter.getChatDisplayShort(pipe.pipe.getPipeMaterial().getMaxPressuire(), DisplayUnit.PRESSURE_ATM).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		// tooltips.add(TextUtils.tooltip("pipeheatloss", ChatFormatter.getChatDisplayShort(pipe.pipe.effectivePipeHeatLoss, DisplayUnit.TEMPERATURE_KELVIN)).withStyle(ChatFormatting.GRAY));
	}

}
