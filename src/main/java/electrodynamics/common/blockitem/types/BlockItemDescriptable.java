package electrodynamics.common.blockitem.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.blockitem.BlockItemElectrodynamics;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

public class BlockItemDescriptable extends BlockItemElectrodynamics {

    private static final HashMap<Holder<Block>, HashSet<MutableComponent>> DESCRIPTION_MAPPINGS = new HashMap<>();
    private final static HashMap<Block, HashSet<MutableComponent>> PROCESSED_DESCRIPTION_MAPPINGS = new HashMap<>();

    private static boolean initialized = false;

    public BlockItemDescriptable(Block block, Properties properties, Holder<CreativeModeTab> creativeTab) {
        super(block, properties, creativeTab);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        if (!initialized) {
            BlockItemDescriptable.initialized = true;

            DESCRIPTION_MAPPINGS.forEach((supplier, set) -> {

                PROCESSED_DESCRIPTION_MAPPINGS.put(supplier.value(), set);

            });

        }
        HashSet<MutableComponent> gotten = PROCESSED_DESCRIPTION_MAPPINGS.get(getBlock());
        if (gotten != null) {
            for (MutableComponent s : gotten) {
                tooltip.add(s.withStyle(ChatFormatting.GRAY));
            }
        }

        if (stack.has(DataComponents.BLOCK_ENTITY_DATA)) {
            double joules = stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag().getDouble("joules");
            if (joules > 0) {
                tooltip.add(ElectroTextUtils.gui("machine.stored", ChatFormatter.getChatDisplayShort(joules, DisplayUnit.JOULES)));
            }
        }
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        if (stack.has(DataComponents.BLOCK_ENTITY_DATA) && stack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag().getDouble("joules") > 0) {
            return 1;
        }
        return super.getMaxStackSize(stack);
    }

    public static void addDescription(Holder<Block> block, MutableComponent description) {

        HashSet<MutableComponent> set = DESCRIPTION_MAPPINGS.getOrDefault(block, new HashSet<>());

        set.add(description);

        DESCRIPTION_MAPPINGS.put(block, set);

    }

}
