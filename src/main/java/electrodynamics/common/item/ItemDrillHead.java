package electrodynamics.common.item;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.References;
import electrodynamics.common.item.subtype.SubtypeDrillHead;
import electrodynamics.registers.ElectrodynamicsCreativeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class ItemDrillHead extends ItemElectrodynamics {

	private static final List<ItemDrillHead> HEADS = new ArrayList<>();

	public SubtypeDrillHead head;

	public ItemDrillHead(SubtypeDrillHead head) {
		super(new Item.Properties().durability(head.durability).durability(head.durability).rarity(head.isUnbreakable ? Rarity.UNCOMMON : Rarity.COMMON), ElectrodynamicsCreativeTabs.MAIN);
		this.head = head;
		HEADS.add(this);
	}

	@EventBusSubscriber(value = Dist.CLIENT, modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
	private static class ColorHandler {

		@SubscribeEvent
		public static void registerColoredBlocks(RegisterColorHandlersEvent.Item event) {
			HEADS.forEach(item -> event.register((stack, index) -> item.head.color.color(), item));
		}

	}

}
