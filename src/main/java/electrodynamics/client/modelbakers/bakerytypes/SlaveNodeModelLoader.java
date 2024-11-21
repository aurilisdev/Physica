package electrodynamics.client.modelbakers.bakerytypes;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import electrodynamics.api.References;
import electrodynamics.api.multiblock.assemblybased.TileMultiblockSlave;
import electrodynamics.client.ClientRegister;
import electrodynamics.client.modelbakers.modelproperties.ModelPropertySlaveNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

/**
 * @author skip999
 */
public class SlaveNodeModelLoader implements IGeometryLoader<SlaveNodeModelLoader.SlaveNodeGeometry> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(References.ID, "electrodynamicsslavenodeloader");

    public static final SlaveNodeModelLoader INSTANCE = new SlaveNodeModelLoader();

    @Override
    public SlaveNodeGeometry read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {

        return new SlaveNodeGeometry();
    }

    public static class SlaveNodeGeometry implements IUnbakedGeometry<SlaveNodeGeometry> {

        public SlaveNodeGeometry() {}

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
            return new SlaveNodeModelLoader.SlaveNodeModel();
        }

    }

    public static class SlaveNodeModel implements IDynamicBakedModel {

        private static final List<BakedQuad> NO_QUADS = ImmutableList.of();
        @Nullable private BakedModel model;
        @Nullable private BlockState disguisedState;

        @Override
        public boolean useAmbientOcclusion() {
            return model == null ? false : model.useAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return model == null ? false : model.isGui3d();
        }

        @Override
        public boolean usesBlockLight() {
            return model == null ? false : model.usesBlockLight();
        }

        @Override
        public boolean isCustomRenderer() {
            return model == null ? false : model.isCustomRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return model == null ? ClientRegister.getSprite(ClientRegister.TEXTURE_WHITE) : model.getParticleIcon();
        }

        @Override
        public ItemOverrides getOverrides() {
            return model == null ? ItemOverrides.EMPTY : model.getOverrides();
        }

        @Override
        public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {

            return model == null ? ChunkRenderTypeSet.none() : model.getRenderTypes(disguisedState, rand, data);

        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
            if(model instanceof MultiblockModelLoader.MultiblockModel slave) {
                return slave.getQuads(disguisedState, side, rand, extraData, renderType);
            }

            return NO_QUADS;
        }

        @Override
        public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
            if (level.getBlockEntity(pos) instanceof TileMultiblockSlave slave) {
                ModelData data = slave.getModelData();
                model = Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(data.get(ModelPropertySlaveNode.INSTANCE).id()));
                disguisedState = slave.getDisguise();
                return data;
            }
            return modelData;
        }

    }

}
