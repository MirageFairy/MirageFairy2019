package miragefairy2019.mod.lib;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

/**
 * {@link #isBuiltInRenderer()}が常に真を返すだけのラッパーです。
 */
public class BakedModelBuiltinWrapper implements IBakedModel {

    public final IBakedModel bakedModel;

    public BakedModelBuiltinWrapper(IBakedModel bakedModel) {
        this.bakedModel = bakedModel;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        return bakedModel.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return bakedModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return bakedModel.isGui3d();
    }

    /**
     * 常に真を返します。
     */
    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return bakedModel.getParticleTexture();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return bakedModel.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return bakedModel.getOverrides();
    }

    @Override
    public boolean isAmbientOcclusion(IBlockState state) {
        return bakedModel.isAmbientOcclusion(state);
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        Pair<? extends IBakedModel, Matrix4f> pair = bakedModel.handlePerspective(cameraTransformType);
        return Pair.of(new BakedModelBuiltinWrapper(pair.getLeft()), pair.getRight());
    }

}
