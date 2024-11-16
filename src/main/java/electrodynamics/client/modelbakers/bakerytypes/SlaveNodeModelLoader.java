package electrodynamics.client.modelbakers.bakerytypes;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import electrodynamics.api.References;
import electrodynamics.api.multiblock.assemblybased.MultiblockSlaveNode;
import electrodynamics.client.modelbakers.ModelStateRotation;
import electrodynamics.client.modelbakers.modelproperties.ModelPropertyConnections;
import electrodynamics.client.modelbakers.modelproperties.ModelPropertySlaveNode;
import electrodynamics.common.block.connect.util.EnumConnectType;
import electrodynamics.prefab.tile.types.IConnectTile;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author skip999
 */
public class SlaveNodeModelLoader implements IGeometryLoader<SlaveNodeModelLoader.WirePartGeometry> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(References.ID, "electrodynamicsslavenodeloader");

    public static final SlaveNodeModelLoader INSTANCE = new SlaveNodeModelLoader();

    @Override
    public SlaveNodeModelLoader.WirePartGeometry read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {

        return new SlaveNodeModelLoader.WirePartGeometry(context.deserialize(GsonHelper.getAsJsonObject(json, "model"), BlockModel.class));
    }

    public static class WirePartGeometry implements IUnbakedGeometry<SlaveNodeModelLoader.WirePartGeometry> {

        private final BlockModel model;

        public WirePartGeometry(BlockModel model) {
            this.model = model;

        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
            boolean useBlockLight = context.useBlockLight();

            BakedModel[] models = new BakedModel[6];

            for (Direction dir : Direction.values()) {

                ModelState transform = ModelStateRotation.ROTATIONS.get(dir);

                models[dir.ordinal()] = this.model.bake(baker, this.model, spriteGetter, transform, useBlockLight);

            }

            return new SlaveNodeModelLoader.SlaveNodeModel(context.useAmbientOcclusion(), context.isGui3d(), useBlockLight, spriteGetter.apply(this.model.getMaterial("particle")), models);
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
            this.model.resolveParents(modelGetter);
        }

        @Override
        public Set<String> getConfigurableComponentNames() {
            return IUnbakedGeometry.super.getConfigurableComponentNames();
        }

    }

    public static class SlaveNodeModel implements IDynamicBakedModel {

        private static final List<BakedQuad> NO_QUADS = ImmutableList.of();

        private final boolean isAmbientOcclusion;
        private final boolean isGui3d;
        private final boolean isSideLit;
        private final TextureAtlasSprite particle;
        // render type for general model defined by this one
        private final BakedModel[] models;

        public SlaveNodeModel(boolean isAmbientOcclusion, boolean isGui3d, boolean isSideLit, TextureAtlasSprite particle, BakedModel[] models) {
            this.isAmbientOcclusion = isAmbientOcclusion;
            this.isGui3d = isGui3d;
            this.isSideLit = isSideLit;
            this.particle = particle;
            this.models = models;
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
        public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {

            return models[0].getRenderTypes(state, rand, data);

        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
            ModelPropertySlaveNode.SlaveNodeWrapper data = extraData.get(ModelPropertySlaveNode.INSTANCE);

            if (data == null || !MultiblockSlaveNode.hasModel(data.id())) {
                return NO_QUADS;
            }

            /**
             * TODO implement model switch?
             */

            return models[data.facing().ordinal()].getQuads(state, side, rand, extraData, renderType);
        }

        @Override
        public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
            if (level.getBlockEntity(pos) instanceof IConnectTile tile) {
                return ModelData.builder().with(ModelPropertyConnections.INSTANCE, () -> tile.readConnections()).build();
            }
            return modelData;
        }

    }

}
