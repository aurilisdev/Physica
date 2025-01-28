package electrodynamics.client.modelbakers.bakerytypes;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import electrodynamics.Electrodynamics;
import electrodynamics.api.multiblock.assemblybased.MultiblockSlaveNode;
import electrodynamics.client.modelbakers.ModelStateRotation;
import electrodynamics.client.modelbakers.modelproperties.ModelPropertySlaveNode;
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
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.NamedRenderTypeManager;
import net.neoforged.neoforge.client.model.ExtendedBlockModelDeserializer;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

public class MultiblockModelLoader implements IGeometryLoader<MultiblockModelLoader.MultiblockModelGeometry> {

    public static final ResourceLocation ID = Electrodynamics.rl("electrodynamicsmultiblockmodelloader");

    public static final MultiblockModelLoader INSTANCE = new MultiblockModelLoader();

    private static final ExtendedBlockModelDeserializer DESERIALIZER = new ExtendedBlockModelDeserializer();

    @Override
    public MultiblockModelGeometry read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {

        json.remove("loader");
        if (json.has("modelloader")) {
            json.addProperty("loader", json.get("modelloader").getAsString());
            json.remove("modelloader");
        }

        ResourceLocation loc = null;

        if(json.has("render_type")){
             loc = ResourceLocation.parse(json.get("render_type").getAsString());
        }

        return new MultiblockModelGeometry(DESERIALIZER.deserialize(json, null, context), loc);
    }

    public static class MultiblockModelGeometry implements IUnbakedGeometry<MultiblockModelGeometry> {

        private final BlockModel model;
        @Nullable
        private final ResourceLocation renderType;

        public MultiblockModelGeometry(BlockModel model, @Nullable ResourceLocation renderType) {
            this.model = model;
            this.renderType = renderType;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {

            BakedModel[] models = new BakedModel[6];

            for (Direction dir : Direction.values()) {

                ModelState transform = ModelStateRotation.ROTATIONS.get(dir);

                if(model.customData.getCustomGeometry() != null) {
                    models[dir.ordinal()] = this.model.customData.getCustomGeometry().bake(context, baker, spriteGetter, transform, overrides);
                } else {
                    models[dir.ordinal()] = this.model.bake(baker, model, spriteGetter, transform, context.isGui3d());
                }



            }


            return new MultiblockModelLoader.MultiblockModel(models, renderType == null ? null : ChunkRenderTypeSet.of(NamedRenderTypeManager.get(renderType).block()));
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

    public static class MultiblockModel implements IDynamicBakedModel {

        private static final List<BakedQuad> NO_QUADS = ImmutableList.of();

        private final BakedModel[] models;
        @Nullable
        private final ChunkRenderTypeSet renderType;

        public MultiblockModel(BakedModel[] models, @Nullable ChunkRenderTypeSet renderType) {
            this.models = models;
            this.renderType = renderType;
        }

        @Override
        public boolean useAmbientOcclusion() {
            return models[0].useAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return models[0].isGui3d();
        }

        @Override
        public boolean usesBlockLight() {
            return models[0].usesBlockLight();
        }

        @Override
        public boolean isCustomRenderer() {
            return models[0].isCustomRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return models[0].getParticleIcon();
        }

        @Override
        public ItemOverrides getOverrides() {
            return models[0].getOverrides();
        }

        @Override
        public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
            return renderType == null ? models[0].getRenderTypes(state, rand, data) : renderType;

        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
            ModelPropertySlaveNode.SlaveNodeWrapper data = extraData.get(ModelPropertySlaveNode.INSTANCE);

            if (data == null || !MultiblockSlaveNode.hasModel(data.id())) {
                return NO_QUADS;
            }

            return models[data.facing().ordinal()].getQuads(state, side, rand, extraData, renderType);
        }

    }
}

