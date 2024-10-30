package electrodynamics.common.world;

import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.settings.OreConfig;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class OreGeneration {

	public static void generateSulfurAround(RandomSource random, BlockPos pos, WorldGenLevel level) {

		if (OreConfig.DISABLE_ALL_ORES) {
			return;
		}

		boolean allowSulfur = !OreConfig.DISABLE_STONE_ORES && OreConfig.SPAWN_SULFUR_ORE;
		boolean allowDeepSulfur = !OreConfig.DISABLE_DEEPSLATE_ORES && OreConfig.SPAWN_DEEP_SULFUR_ORE;

		if (!allowSulfur && !allowDeepSulfur) {
			return;
		}

		for (Direction direction : Direction.values()) {

			if (random.nextFloat() > 0.3) {
				continue;
			}

			BlockPos offset = pos.offset(direction.getNormal());

			if (allowSulfur && level.getBlockState(pos).is(BlockTags.STONE_ORE_REPLACEABLES)) {

				level.setBlock(offset, ElectrodynamicsBlocks.BLOCKS_ORE.getValue(SubtypeOre.sulfur).defaultBlockState(), 3);

			} else if (allowDeepSulfur && level.getBlockState(pos).is(BlockTags.DEEPSLATE_ORE_REPLACEABLES)) {

				level.setBlock(offset, ElectrodynamicsBlocks.BLOCKS_DEEPSLATEORE.getValue(SubtypeOreDeepslate.sulfur).defaultBlockState(), 3);

			}
		}
	}
}
