package miragefairy2019.mod.lib

import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.block.model.ItemOverrideList
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.EnumFacing
import org.apache.commons.lang3.tuple.Pair
import javax.vecmath.Matrix4f

/** [isBuiltInRenderer]が常に真を返すだけのラッパーです。 */
class BakedModelBuiltinWrapper(val bakedModel: IBakedModel) : IBakedModel {
    override fun getQuads(state: IBlockState?, side: EnumFacing?, rand: Long): List<BakedQuad> = bakedModel.getQuads(state, side, rand)
    override fun isAmbientOcclusion(): Boolean = bakedModel.isAmbientOcclusion
    override fun isGui3d(): Boolean = bakedModel.isGui3d

    /** 常に真を返します。 */
    override fun isBuiltInRenderer(): Boolean = true
    override fun getParticleTexture(): TextureAtlasSprite = bakedModel.particleTexture
    override fun getItemCameraTransforms(): ItemCameraTransforms = bakedModel.itemCameraTransforms
    override fun getOverrides(): ItemOverrideList = bakedModel.overrides
    override fun isAmbientOcclusion(state: IBlockState): Boolean = bakedModel.isAmbientOcclusion(state)
    override fun handlePerspective(cameraTransformType: TransformType): Pair<out IBakedModel, Matrix4f> {
        val pair = bakedModel.handlePerspective(cameraTransformType)
        return Pair.of(BakedModelBuiltinWrapper(pair.left), pair.right)
    }
}
