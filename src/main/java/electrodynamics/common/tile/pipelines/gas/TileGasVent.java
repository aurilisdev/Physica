package electrodynamics.common.tile.pipelines.gas;

import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.gas.GasAction;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerGasVent;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerSimple;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.types.GenericMaterialTile;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileGasVent extends GenericMaterialTile {

    public TileGasVent(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_GASVENT.get(), worldPos, blockState);
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentGasHandlerSimple(this, "", 128000, 1000000, 1000000).universalInput());
        addComponent(new ComponentInventory(this, InventoryBuilder.newInv().gasInputs(1)).valid((slot, stack, i) -> stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM) != null));
        addComponent(new ComponentContainerProvider(SubtypeMachine.gasvent, this).createMenu((id, player) -> new ContainerGasVent(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
    }

    public void tickServer(ComponentTickable tickable) {

        ComponentInventory inv = getComponent(IComponentType.Inventory);

        ComponentGasHandlerSimple simple = getComponent(IComponentType.GasHandler);

        simple.drain(simple.getGasAmount(), GasAction.EXECUTE);

        ItemStack input = inv.getItem(0);

        if (input.isEmpty()) {

            return;

        }

        IGasHandlerItem handler = input.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (handler == null) {
            return;
        }

        for (int i = 0; i < handler.getTanks(); i++) {
            handler.drain(Integer.MAX_VALUE, GasAction.EXECUTE);
        }

    }

}
