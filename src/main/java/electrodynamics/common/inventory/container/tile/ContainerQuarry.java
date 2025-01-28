package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.item.ItemDrillHead;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.tile.machines.quarry.TileQuarry;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotQuarryTrashcan;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotRestricted;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotUpgrade;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.IconType;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.SlotType;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerQuarry extends GenericContainerBlockEntity<TileQuarry> {

	public static final SubtypeItemUpgrade[] VALID_UPGRADES = new SubtypeItemUpgrade[] { SubtypeItemUpgrade.fortune, SubtypeItemUpgrade.silktouch, SubtypeItemUpgrade.unbreaking, SubtypeItemUpgrade.itemvoid };

	public ContainerQuarry(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(ElectrodynamicsMenuTypes.CONTAINER_QUARRY.get(), id, playerinv, inventory, inventorydata);
	}

	public ContainerQuarry(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(19), new SimpleContainerData(3));
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		setPlayerInvOffset(58);
		addSlot(new SlotRestricted(SlotType.NORMAL, IconType.DRILL_HEAD_DARK, inv, nextIndex(), 30, 100).setRestriction(ItemDrillHead.class).setIOColor(new Color(0, 240, 255, 255)));
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 3; ++j) {
				addSlot(new SlotQuarryTrashcan(inv, nextIndex(), 85 + j * 18, 26 + i * 18));
			}
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				addSlot(new SlotRestricted(inv, nextIndex(), 85 + j * 18, 75 + i * 18).setIOColor(new Color(255, 0, 0, 255)));
			}
		}
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 71, VALID_UPGRADES));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 91, VALID_UPGRADES));
		addSlot(new SlotUpgrade(inv, nextIndex(), 153, 111, VALID_UPGRADES));
	}

}
