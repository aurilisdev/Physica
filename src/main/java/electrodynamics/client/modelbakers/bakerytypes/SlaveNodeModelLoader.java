package electrodynamics.client.modelbakers.bakerytypes;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import electrodynamics.Electrodynamics;
import electrodynamics.api.multiblock.assemblybased.MultiblockSlaveNode;
import electrodynamics.api.multiblock.assemblybased.TileMultiblockSlave;
import electrodynamics.client.ClientRegister;
import electrodynamics.client.modelbakers.modelproperties.ModelPropertySlaveNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

/**
 * @author skip999
 */
public class SlaveNodeModelLoader implements IGeometryLoader<SlaveNodeModelLoader.SlaveNodeGeometry> {

    public static final ResourceLocation ID = Electrodynamics.rl("electrodynamicsslavenodeloader");

    public static final SlaveNodeModelLoader INSTANCE = new SlaveNodeModelLoader();

    @Override
    public SlaveNodeGeometry read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {

        return new SlaveNodeGeometry();
    }

    public static class SlaveNodeGeometry implements IUnbakedGeometry<SlaveNodeGeometry> {

        public SlaveNodeGeometry() {
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
            return new SlaveNodeModelLoader.SlaveNodeModel(context.useAmbientOcclusion(), context.isGui3d(), context.useBlockLight());
        }

    }

    public static class SlaveNodeModel implements IDynamicBakedModel {

        private static final List<BakedQuad> NO_QUADS = ImmutableList.of();

        private final HashMap<ResourceLocation, BakedModel> modelMap = new HashMap<>();

        private final boolean useAmbientOcclusion;
        private final boolean isGui3d;
        private final boolean usesBlockLight;

        public SlaveNodeModel(boolean useAmbientOcclusion, boolean isGui3d, boolean usesBlockLight) {
            this.useAmbientOcclusion = useAmbientOcclusion;
            this.isGui3d = isGui3d;
            this.usesBlockLight = usesBlockLight;
        }

        @Override
        public boolean useAmbientOcclusion() {
            return useAmbientOcclusion;
        }

        @Override
        public boolean isGui3d() {
            return isGui3d;
        }

        @Override
        public boolean usesBlockLight() {
            return usesBlockLight;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return ClientRegister.getSprite(ClientRegister.TEXTURE_MULTISUBNODE);
        }

        @Override
        public ItemOverrides getOverrides() {
            return ItemOverrides.EMPTY;
        }

        @Override
        public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
            if (data.has(ModelPropertySlaveNode.INSTANCE)) {
                BakedModel model = modelMap.get(data.get(ModelPropertySlaveNode.INSTANCE).id());
                return model == null ? ChunkRenderTypeSet.none() : model.getRenderTypes(state, rand, data);
            }
            return ChunkRenderTypeSet.none();
        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
            ModelPropertySlaveNode.SlaveNodeWrapper data = extraData.get(ModelPropertySlaveNode.INSTANCE);
            if (data == null || !MultiblockSlaveNode.hasModel(data.id())) {
                return NO_QUADS;
            }
            BakedModel model = modelMap.get(data.id());
            if (model instanceof MultiblockModelLoader.MultiblockModel slave) {
                return slave.getQuads(state, side, rand, extraData, renderType);
            }

            return NO_QUADS;
        }

        @Override
        public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
            if (level.getBlockEntity(pos) instanceof TileMultiblockSlave slave && MultiblockSlaveNode.hasModel(slave.renderModel.get())) {
                if (modelMap.get(slave.renderModel.get()) == null) {
                    modelMap.put(slave.renderModel.get(), Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(slave.renderModel.get())));
                }
                return slave.getModelData();
            }
            return modelData;
        }

    }

}
