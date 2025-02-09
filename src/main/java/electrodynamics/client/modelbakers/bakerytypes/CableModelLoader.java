package electrodynamics.client.modelbakers.bakerytypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import electrodynamics.Electrodynamics;
import electrodynamics.client.modelbakers.ModelStateRotation;
import electrodynamics.client.modelbakers.modelproperties.ModelPropertyConnections;
import electrodynamics.common.block.connect.util.EnumConnectType;
import electrodynamics.prefab.tile.types.IConnectTile;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

public class CableModelLoader implements IGeometryLoader<CableModelLoader.WirePartGeometry> {

    public static final ResourceLocation ID = Electrodynamics.rl("electrodynamicscableloader");

    public static final CableModelLoader INSTANCE = new CableModelLoader();

    @Override
    public WirePartGeometry read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {

	BlockModel none = context.deserialize(GsonHelper.getAsJsonObject(json, EnumConnectType.NONE.toString()),
		BlockModel.class);
	BlockModel wire = context.deserialize(GsonHelper.getAsJsonObject(json, EnumConnectType.WIRE.toString()),
		BlockModel.class);
	BlockModel inventory = context
		.deserialize(GsonHelper.getAsJsonObject(json, EnumConnectType.INVENTORY.toString()), BlockModel.class);
	return new WirePartGeometry(none, wire, inventory);
    }

    public static class WirePartGeometry implements IUnbakedGeometry<WirePartGeometry> {

	private final BlockModel none;
	private final BlockModel wire;
	private final BlockModel inventory;

	public WirePartGeometry(BlockModel none, BlockModel wire, BlockModel inventory) {
	    this.none = none;
	    this.wire = wire;
	    this.inventory = inventory;

	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker,
		Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
	    boolean useBlockLight = context.useBlockLight();

	    BakedModel none = this.none.bake(baker, this.none, spriteGetter, modelState, useBlockLight);

	    BakedModel[] wires = new BakedModel[6];
	    BakedModel[] inventories = new BakedModel[6];

	    for (Direction dir : Direction.values()) {

		ModelState transform = ModelStateRotation.ROTATIONS.get(dir);

		wires[dir.ordinal()] = this.wire.bake(baker, this.wire, spriteGetter, transform, useBlockLight);
		inventories[dir.ordinal()] = this.inventory.bake(baker, this.inventory, spriteGetter, transform,
			useBlockLight);

	    }

	    return new CableModel(context.useAmbientOcclusion(), context.isGui3d(), useBlockLight,
		    spriteGetter.apply(this.none.getMaterial("particle")), none, wires, inventories);
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter,
		IGeometryBakingContext context) {
	    this.none.resolveParents(modelGetter);
	    this.wire.resolveParents(modelGetter);
	    this.inventory.resolveParents(modelGetter);
	}

	@Override
	public Set<String> getConfigurableComponentNames() {
	    return IUnbakedGeometry.super.getConfigurableComponentNames();
	}

    }

    public static class CableModel implements IDynamicBakedModel {

	private static final List<BakedQuad> NO_QUADS = ImmutableList.of();

	private final boolean isAmbientOcclusion;
	private final boolean isGui3d;
	private final boolean isSideLit;
	private final TextureAtlasSprite particle;
	// render type for general model defined by this one
	private final BakedModel none;
	private final BakedModel[] wires;
	private final BakedModel[] inventories;

	public CableModel(boolean isAmbientOcclusion, boolean isGui3d, boolean isSideLit, TextureAtlasSprite particle,
		BakedModel none, BakedModel[] wires, BakedModel[] inventories) {
	    this.isAmbientOcclusion = isAmbientOcclusion;
	    this.isGui3d = isGui3d;
	    this.isSideLit = isSideLit;
	    this.particle = particle;
	    this.none = none;
	    this.wires = wires;
	    this.inventories = inventories;
	}

	@Override
	public boolean useAmbientOcclusion() {
	    return this.isAmbientOcclusion;
	}

	@Override
	public boolean isGui3d() {
	    return this.isGui3d;
	}

	@Override
	public boolean usesBlockLight() {
	    return isSideLit;
	}

	@Override
	public boolean isCustomRenderer() {
	    return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
	    return this.particle;
	}

	@Override
	public ItemOverrides getOverrides() {
	    return ItemOverrides.EMPTY;
	}

	@Override
	public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand,
		@NotNull ModelData data) {

	    return none.getRenderTypes(state, rand, data);

	}

	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
		@NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
	    @Nullable
	    Supplier<EnumConnectType[]> m = extraData.get(ModelPropertyConnections.INSTANCE);
	    if (m == null) {
		return NO_QUADS;
	    }
	    EnumConnectType[] data = m.get();
	    if (data == null) {
		return NO_QUADS;
	    }

	    boolean none = false;

	    List<BakedQuad> quads = new ArrayList<>();

	    for (int i = 0; i < data.length; i++) {

		switch (data[i]) {
		case NONE:
		    none = true;
		    break;
		case WIRE:
		    quads.addAll(this.wires[i].getQuads(state, side, rand, extraData, renderType));
		    break;
		case INVENTORY:
		    quads.addAll(this.inventories[i].getQuads(state, side, rand, extraData, renderType));
		    break;
		default:
		    none = true;
		    break;
		}
	    }

	    if (none) {
		quads.addAll(this.none.getQuads(state, side, rand, extraData, renderType));
	    }

	    return quads;
	}

	@Override
	public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos,
		@NotNull BlockState state, @NotNull ModelData modelData) {
	    if (level.getBlockEntity(pos) instanceof IConnectTile tile) {
		return ModelData.builder().with(ModelPropertyConnections.INSTANCE, () -> tile.readConnections())
			.build();
	    }
	    return modelData;
	}

    }

}
