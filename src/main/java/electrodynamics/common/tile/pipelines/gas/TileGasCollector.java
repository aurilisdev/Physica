package electrodynamics.common.tile.pipelines.gas;

import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerDO2OProcessor;
import electrodynamics.common.inventory.container.tile.ContainerGasCollector;
import electrodynamics.common.network.utils.GasUtilities;
import electrodynamics.common.reloadlistener.GasCollectorChromoCardsRegister;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.sound.utils.ITickableSound;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerSimple;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.types.GenericGasTile;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsSounds;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

public class TileGasCollector extends GenericGasTile implements ITickableSound {

    public static final int CARD_SLOT = 0;

    private boolean isSoundPlaying = false;

    public TileGasCollector(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_GASCOLLECTOR.get(), worldPos, blockState);
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentTickable(this).tickServer(this::tickServer).tickClient(this::tickClient));
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 2.0).maxJoules(Constants.GAS_COLLECTOR_USAGE_PER_TICK * 20));
        addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().inputs(1).gasOutputs(1).upgrades(3)).validUpgrades(ContainerDO2OProcessor.VALID_UPGRADES).valid(machineValidator()));
        addComponent(new ComponentProcessor(this).canProcess(this::canProcess).process(this::process));
        addComponent(new ComponentContainerProvider(SubtypeMachine.gascollector, this).createMenu((id, player) -> new ContainerGasCollector(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
        addComponent(new ComponentGasHandlerSimple(this, "", 5000, 1000, 10).setOutputDirections(BlockEntityUtils.MachineDirection.BACK).setOnGasCondensed(getCondensedHandler()));
    }

    private void tickClient(ComponentTickable componentTickable) {
        if (!isSoundPlaying) {
            isSoundPlaying = true;
            SoundBarrierMethods.playTileSound(ElectrodynamicsSounds.SOUND_WINDMILL.get(), this, true);
        }
    }

    private void tickServer(ComponentTickable componentTickable) {

        ComponentGasHandlerSimple handler = getComponent(IComponentType.GasHandler);
        GasUtilities.fillItem(this, handler.asArray());
        GasUtilities.outputToPipe(this, handler.asArray(), handler.outputDirections);

    }

    private void process(ComponentProcessor componentProcessor) {
        ComponentInventory inv = getComponent(IComponentType.Inventory);
        ItemStack card = inv.getItem(CARD_SLOT);
        GasCollectorChromoCardsRegister.AtmosphericResult result = GasCollectorChromoCardsRegister.INSTANCE.getResult(card.getItem());
        ComponentGasHandlerSimple tank = getComponent(IComponentType.GasHandler);
        tank.fill(new GasStack(result.stack().getGas(), (int) (result.stack().getAmount() * componentProcessor.operatingSpeed.get()), result.stack().getTemperature(), result.stack().getPressure()), GasAction.EXECUTE);
    }

    private boolean canProcess(ComponentProcessor componentProcessor) {
        boolean valid = checkRecipe(componentProcessor);
        if (BlockEntityUtils.isLit(this) ^ valid) {
            BlockEntityUtils.updateLit(this, valid);
        }
        return valid;
    }

    private boolean checkRecipe(ComponentProcessor componentProcessor) {
        ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
        if(electro.getJoulesStored() < componentProcessor.getUsage()){
            return false;
        }
        ComponentInventory inv = getComponent(IComponentType.Inventory);
        ItemStack card = inv.getItem(CARD_SLOT);
        if(card.isEmpty() || !GasCollectorChromoCardsRegister.INSTANCE.hasResult(card.getItem())){
            return false;
        }
        GasCollectorChromoCardsRegister.AtmosphericResult result = GasCollectorChromoCardsRegister.INSTANCE.getResult(card.getItem());

        ComponentGasHandlerSimple tank = getComponent(IComponentType.GasHandler);
        if(!tank.isEmpty() && !tank.getGas().getGas().equals(result.stack().getGas())) {
            return false;
        }

        if(result.biome() != null || result.biomeTag() != null){
            Biome biome = getLevel().getBiome(getBlockPos()).value();
            if(result.biome() != null) {
                return getLevel().registryAccess().registry(Registries.BIOME).get().get(result.biome()) == biome;
            } else {
                return getLevel().registryAccess().registry(Registries.BIOME).get().getOrCreateTag(result.biomeTag()).contains(new Holder.Direct<>(biome));
            }
        }

        return true;
    }

    @Override
    public void setNotPlaying() {
        isSoundPlaying = false;
    }

    @Override
    public boolean shouldPlaySound() {
        return this.<ComponentProcessor>getComponent(IComponentType.Processor).isActive();
    }

    @Override
    public int getComparatorSignal() {
        return this.<ComponentProcessor>getComponent(IComponentType.Processor).isActive() ? 15 : 0;
    }

}
