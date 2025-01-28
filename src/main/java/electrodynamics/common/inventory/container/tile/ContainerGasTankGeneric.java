package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.tile.pipelines.gas.tank.GenericTileGasTank;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotGas;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotRestricted;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.IconType;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.SlotType;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerGasTankGeneric extends GenericContainerBlockEntity<GenericTileGasTank> {

	public ContainerGasTankGeneric(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(ElectrodynamicsMenuTypes.CONTAINER_GASTANK.get(), id, playerinv, inventory, inventorydata);
	}

	public ContainerGasTankGeneric(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(8), new SimpleContainerData(3));
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		addSlot(new SlotRestricted(SlotType.NORMAL, IconType.FIBERGLASS_SHEET_DARK, inv, nextIndex(), 130, 14) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		}.setRestriction(ElectrodynamicsItems.ITEM_FIBERGLASSSHEET.get()));
		addSlot(new SlotRestricted(SlotType.NORMAL, IconType.FIBERGLASS_SHEET_DARK, inv, nextIndex(), 130, 34) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		}.setRestriction(ElectrodynamicsItems.ITEM_FIBERGLASSSHEET.get()));
		addSlot(new SlotRestricted(SlotType.NORMAL, IconType.FIBERGLASS_SHEET_DARK, inv, nextIndex(), 130, 54) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		}.setRestriction(ElectrodynamicsItems.ITEM_FIBERGLASSSHEET.get()));
		addSlot(new SlotRestricted(SlotType.NORMAL, IconType.FIBERGLASS_SHEET_DARK, inv, nextIndex(), 150, 14) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		}.setRestriction(ElectrodynamicsItems.ITEM_FIBERGLASSSHEET.get()));
		addSlot(new SlotRestricted(SlotType.NORMAL, IconType.FIBERGLASS_SHEET_DARK, inv, nextIndex(), 150, 34) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		}.setRestriction(ElectrodynamicsItems.ITEM_FIBERGLASSSHEET.get()));
		addSlot(new SlotRestricted(SlotType.NORMAL, IconType.FIBERGLASS_SHEET_DARK, inv, nextIndex(), 150, 54) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		}.setRestriction(ElectrodynamicsItems.ITEM_FIBERGLASSSHEET.get()));
		addSlot(new SlotGas(inv, nextIndex(), 27, 20));
		addSlot(new SlotGas(inv, nextIndex(), 27, 50));

	}

}
