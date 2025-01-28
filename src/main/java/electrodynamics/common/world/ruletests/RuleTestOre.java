package electrodynamics.common.world.ruletests;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.common.block.BlockOre;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.settings.OreConfig;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsRuleTestTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class RuleTestOre extends RuleTest {

	@Nullable
	private final SubtypeOre thisOre;
	@Nullable
	private final SubtypeOreDeepslate thisDeepOre;

	private final TagKey<Block> canSpawnIn;

	public static final MapCodec<RuleTestOre> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(instance0 -> {
		if (instance0.thisOre != null) {
			return ElectrodynamicsBlocks.BLOCKS_ORE.getValue(instance0.thisOre);
		}
		if (instance0.thisDeepOre != null) {
			return ElectrodynamicsBlocks.BLOCKS_DEEPSLATEORE.getValue(instance0.thisDeepOre);
		}
		return Blocks.AIR;
	}), TagKey.codec(Registries.BLOCK).fieldOf("canspawnintag").forGetter(instance0 -> instance0.canSpawnIn)).apply(instance, (block, tag) -> {
		if (block instanceof BlockOre ore) {
			return new RuleTestOre(ore.ore, ore.deepOre, tag);
		}
		return new RuleTestOre(null, null, tag);
	}));

	public RuleTestOre(SubtypeOre thisOre, SubtypeOreDeepslate thisDeepOre, TagKey<Block> canSpawnIn) {
		this.thisOre = thisOre;
		this.thisDeepOre = thisDeepOre;
		this.canSpawnIn = canSpawnIn;
	}

	@Override
	public boolean test(BlockState state, RandomSource random) {

		return isEnabled() && state.is(this.canSpawnIn);

	}

	private boolean isEnabled() {

		if (OreConfig.DISABLE_ALL_ORES) {
			return false;
		}

		if (thisOre != null) {

			if (OreConfig.DISABLE_STONE_ORES) {
				return false;
			}

			return switch (thisOre) {
			case aluminum -> OreConfig.SPAWN_ALUMINUM_ORE;
			case chromium -> OreConfig.SPAWN_CHROMIUM_ORE;
			case fluorite -> OreConfig.SPAWN_FLUORITE_ORE;
			case lead -> OreConfig.SPAWN_LEAD_ORE;
			case lithium -> OreConfig.SPAWN_LITHIUM_ORE;
			case molybdenum -> OreConfig.SPAWN_MOLYBDENUM_ORE;
			case monazite -> OreConfig.SPAWN_MONAZITE_ORE;
			case niter -> OreConfig.SPAWN_NITER_ORE;
			case salt -> OreConfig.SPAWN_SALT_ORE;
			case silver -> OreConfig.SPAWN_SILVER_ORE;
			case sulfur -> OreConfig.SPAWN_SULFUR_ORE;
			case sylvite -> OreConfig.SPAWN_SYLVITE_ORE;
			case thorium -> OreConfig.SPAWN_THORIUM_ORE;
			case tin -> OreConfig.SPAWN_TIN_ORE;
			case titanium -> OreConfig.SPAWN_TITANIUM_ORE;
			case uranium -> OreConfig.SPAWN_URANIUM_ORE;
			case vanadium -> OreConfig.SPAWN_VANADIUM_ORE;
			default -> false;
			};
		}
		if (thisDeepOre != null) {

			if (OreConfig.DISABLE_DEEPSLATE_ORES) {
				return false;
			}

			return switch (thisDeepOre) {
			case aluminum -> OreConfig.SPAWN_DEEP_ALUMINUM_ORE;
			case chromium -> OreConfig.SPAWN_DEEP_CHROMIUM_ORE;
			case fluorite -> OreConfig.SPAWN_DEEP_FLUORITE_ORE;
			case lead -> OreConfig.SPAWN_DEEP_LEAD_ORE;
			case lithium -> OreConfig.SPAWN_DEEP_LITHIUM_ORE;
			case molybdenum -> OreConfig.SPAWN_DEEP_MOLYBDENUM_ORE;
			case monazite -> OreConfig.SPAWN_DEEP_MONAZITE_ORE;
			case niter -> OreConfig.SPAWN_DEEP_NITER_ORE;
			case salt -> OreConfig.SPAWN_DEEP_SALT_ORE;
			case silver -> OreConfig.SPAWN_DEEP_SILVER_ORE;
			case sulfur -> OreConfig.SPAWN_DEEP_SULFUR_ORE;
			case sylvite -> OreConfig.SPAWN_DEEP_SYLVITE_ORE;
			case thorium -> OreConfig.SPAWN_DEEP_THORIUM_ORE;
			case tin -> OreConfig.SPAWN_DEEP_TIN_ORE;
			case titanium -> OreConfig.SPAWN_DEEP_TITANIUM_ORE;
			case uranium -> OreConfig.SPAWN_DEEP_URANIUM_ORE;
			case vanadium -> OreConfig.SPAWN_DEEP_VANADIUM_ORE;
			default -> false;
			};
		}
		return false;
	}

	@Override
	protected RuleTestType<?> getType() {
		return ElectrodynamicsRuleTestTypes.TEST_CONFIG_ORESPAWN.get();
	}

}
