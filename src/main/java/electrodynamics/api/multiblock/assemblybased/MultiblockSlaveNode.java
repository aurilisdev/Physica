package electrodynamics.api.multiblock.assemblybased;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.Electrodynamics;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 
 * @author skip999
 *
 * @param placeState    : The blockstate that will be placed when the slave node is created by the controller. This is to allow
 *                      for extensions of the base TileMultiblockSlave class
 * @param replaceState  : The blockstate the controller will look for for valid placement. Note if using tags, it will look for
 *                      Properties after checking the tag
 * @param taggedBlocks: A list of valid blocks the controller will look for. Leave null if there is only one valid block, which
 *                      will be represented by replacedState
 * @param offset        : The relative offset of the slave from the controller
 * @param renderShape   : The render VoxelShape the slave will have
 * @param model         : The model this Slave Node will render if any
 *
 */
public record MultiblockSlaveNode(BlockState placeState, BlockState replaceState, TagKey<Block> taggedBlocks, Vec3i offset, VoxelShape renderShape, ResourceLocation model) {
	public static final TagKey<Block> NOTAG = BlockTags.create(Electrodynamics.rl("notag"));
	public static final ResourceLocation NOMODEL = Electrodynamics.rl("nomodel");

	public static final Codec<AABB> AABB_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			//
			Codec.DOUBLE.fieldOf("xMin").forGetter(aabb -> aabb.minX),
			//
			Codec.DOUBLE.fieldOf("yMin").forGetter(aabb -> aabb.minY),
			//
			Codec.DOUBLE.fieldOf("zMin").forGetter(aabb -> aabb.minZ),
			//
			Codec.DOUBLE.fieldOf("xMax").forGetter(aabb -> aabb.maxX),
			//
			Codec.DOUBLE.fieldOf("yMax").forGetter(aabb -> aabb.maxY),
			//
			Codec.DOUBLE.fieldOf("zMax").forGetter(aabb -> aabb.maxZ)
	//

	).apply(instance, AABB::new));

	public static final Codec<VoxelShape> VOXELSHAPE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			//
			AABB_CODEC.listOf().fieldOf("aabbs").forGetter(VoxelShape::toAabbs)).apply(instance, list -> {
				VoxelShape shape = Shapes.empty();
				for (AABB aabb : list) {
					shape = Shapes.or(shape, Shapes.create(aabb));
				}
				return shape;
			}));

	public static final String SLAVE_OFFSET_FIELD = "offset";

	public static final String PLACE_STATE_FIELD = "placestate";

	public static final String REPLACE_STATE_FIELD = "replacestate";

	public static final String BLOCK_TAG_FIELD = "tag";

	public static final String VOXEL_SHAPE_FIELD = "voxelshape";

	public static final String MODEL_FIELD = "model";

	public static final Codec<MultiblockSlaveNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			//
			BlockState.CODEC.fieldOf(PLACE_STATE_FIELD).forGetter(MultiblockSlaveNode::placeState),
			//
			BlockState.CODEC.fieldOf(REPLACE_STATE_FIELD).forGetter(MultiblockSlaveNode::replaceState),
			//
			TagKey.codec(Registries.BLOCK).optionalFieldOf(BLOCK_TAG_FIELD, NOTAG).forGetter(MultiblockSlaveNode::taggedBlocks),
			//
			Vec3i.CODEC.fieldOf(SLAVE_OFFSET_FIELD).forGetter(MultiblockSlaveNode::offset),
			//
			VOXELSHAPE_CODEC.fieldOf(VOXEL_SHAPE_FIELD).forGetter(MultiblockSlaveNode::renderShape),
			//
			ResourceLocation.CODEC.optionalFieldOf(MODEL_FIELD, NOMODEL).forGetter(MultiblockSlaveNode::model)

	//
	).apply(instance, MultiblockSlaveNode::new));

	public static final MultiblockSlaveNode EMPTY = new MultiblockSlaveNode(Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), NOTAG, Vec3i.ZERO, Shapes.empty(), NOMODEL);

	public static final StreamCodec<ByteBuf, MultiblockSlaveNode> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

	public boolean hasBlockTag() {
		return !taggedBlocks().toString().equals(NOTAG.toString());
	}

	public static boolean hasModel(ResourceLocation model) {
		return !model.toString().equals(NOMODEL.toString());
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}
}
