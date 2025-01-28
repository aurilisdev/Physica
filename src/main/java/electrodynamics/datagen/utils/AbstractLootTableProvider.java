package electrodynamics.datagen.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import electrodynamics.Electrodynamics;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyCustomDataFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public abstract class AbstractLootTableProvider extends VanillaBlockLoot {

    private final String modID;

    public AbstractLootTableProvider(HolderLookup.Provider provider, String modID) {
        super(provider);
        this.modID = modID;
    }

    public LootTable.Builder machineTable(String name, Block block, BlockEntityType<?> type, boolean items, boolean fluids, boolean gases, boolean energy, boolean additional) {
        CopyCustomDataFunction.Builder function = CopyCustomDataFunction.copyData(ContextNbtProvider.BLOCK_ENTITY);

        if (items) {
            function = function.copy("Items", "BlockEntityTag", CopyCustomDataFunction.MergeStrategy.REPLACE);
            function = function.copy(ComponentInventory.SAVE_KEY + "_size", "BlockEntityTag", CopyCustomDataFunction.MergeStrategy.REPLACE);
        }

        if (fluids) {
            function = function.copy("fluid", "BlockEntityTag", CopyCustomDataFunction.MergeStrategy.REPLACE);
        }

        if (gases) {
            // function = function
        }

        if (energy) {
            function = function.copy("joules", "BlockEntityTag.joules", CopyCustomDataFunction.MergeStrategy.REPLACE);
        }

        if (additional) {
            function = function.copy("additional", "BlockEntityTag.additional", CopyCustomDataFunction.MergeStrategy.REPLACE);
        }

        LootPool.Builder builder = LootPool.lootPool()
                //
                .name(name)
                //
                .setRolls(ConstantValue.exactly(1))
                //
                .add(
                        //
                        LootItem.lootTableItem(block)
                                //
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                //
                                .apply(function)
                                //
                                .apply(SetContainerContents.setContents(ContainerComponentManipulators.CONTAINER).withEntry(DynamicLoot.dynamicEntry(Electrodynamics.vanillarl("contents"))))
                        //
                );
        return LootTable.lootTable().withPool(builder);
    }

    /**
     * Creates a silk touch and fortune loottable for a block
     *
     * @param name     Name of the block
     * @param block    The block that will be added
     * @param lootItem The alternative item that is dropped when silk is not used
     * @param min      The minimum amount dropped
     * @param max      The maximum amount dropped
     * @author SeaRobber69
     */
    protected LootTable.Builder createSilkTouchAndFortuneTable(String name, Block block, Item lootItem, float min, float max) {
        LootPool.Builder builder = LootPool.lootPool()
                //
                .name(name)
                //
                .setRolls(ConstantValue.exactly(1))
                //
                .add(
                        //
                        AlternativesEntry.alternatives(
                                //
                                LootItem.lootTableItem(block).when(hasSilkTouch()),
                                //
                                LootItem.lootTableItem(lootItem)
                                        //
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                                        //
                                        .apply(ApplyBonusCount.addUniformBonusCount(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), 1))
                                        //
                                        .apply(ApplyExplosionDecay.explosionDecay())
                                //
                        )
                        //
                );
        return LootTable.lootTable().withPool(builder);
    }

    /**
     * Creates a silk touch only loottable for a block
     *
     * @param name  Name of the block
     * @param block The block that will be added
     * @author SeaRobber69
     */
    protected LootTable.Builder createSilkTouchOnlyTable(String name, Block block) {
        LootPool.Builder builder = LootPool.lootPool().name(name).setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block).when(hasSilkTouch())

        );
        return LootTable.lootTable().withPool(builder);
    }

    protected LootTable.Builder createSimpleBlockTable(String name, Block block) {
        LootPool.Builder builder = LootPool.lootPool().name(name).setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block));
        return LootTable.lootTable().withPool(builder);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {

        return BuiltInRegistries.BLOCK.entrySet().stream().filter(e -> e.getKey().location().getNamespace().equals(modID) && !getExcludedBlocks().contains(e.getValue())).map(Map.Entry::getValue).collect(Collectors.toList());
    }

    public abstract List<Block> getExcludedBlocks();

}
