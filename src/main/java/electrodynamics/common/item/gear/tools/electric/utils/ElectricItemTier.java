package electrodynamics.common.item.gear.tools.electric.utils;

import java.util.List;
import java.util.Locale;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public enum ElectricItemTier implements Tier {
    ELECTRIC_DRILL(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 0, Tiers.GOLD.getSpeed() * 3.5f, Tiers.IRON.getAttackDamageBonus() * 1.6f, 5),
    ELECTRIC_CHAINSAW(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 0, Tiers.GOLD.getSpeed() * 3.5f, Tiers.IRON.getAttackDamageBonus() * 1.6f, 5),
    ELECTRIC_BATON(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 0, Tiers.GOLD.getSpeed() * 3.5f, Tiers.IRON.getAttackDamageBonus() * 1.6f, 5);
    private final TagKey<Block> harvestLevel;
    private final float efficency;
    private final float attackDammage;
    private final int enchantability;

    ElectricItemTier(TagKey<Block> harvestLevel, int maxUses, float efficency, float attackDammage, int enchantability) {
        this.harvestLevel = harvestLevel;
        this.efficency = efficency;
        this.attackDammage = attackDammage;
        this.enchantability = enchantability;
    }

    @Override
    public int getUses() {
        return 0;
    }

    @Override
    public float getSpeed() {
        return efficency;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDammage;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }

    @Override
    public Tool createToolProperties(TagKey<Block> pBlock) {
        return new Tool(List.of(Tool.Rule.deniesDrops(this.getIncorrectBlocksForDrops()), Tool.Rule.minesAndDrops(pBlock, this.getSpeed())), 1.0F, 0);
    }

    public String tag() {
        return name().toLowerCase(Locale.ROOT);
    }

}
