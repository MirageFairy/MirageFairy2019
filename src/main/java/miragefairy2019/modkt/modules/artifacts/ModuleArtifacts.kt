package miragefairy2019.modkt.modules.artifacts

import miragefairy2019.libkt.block
import miragefairy2019.libkt.invoke
import miragefairy2019.libkt.item
import miragefairy2019.libkt.tileEntity
import miragefairy2019.libkt.tileEntityRenderer
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.lib.EventRegistryMod
import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.ItemBlock
import net.minecraftforge.client.model.ModelLoader

object ModuleArtifacts {
    lateinit var blockDish: Block

    @JvmStatic
    fun init(erMod: EventRegistryMod) {
        erMod {

            // 皿
            block(ModMirageFairy2019.MODID, "dish", { BlockDish().also { blockDish = it } }) {
                unlocalizedName = "dish"
                setCreativeTab(ApiMain.creativeTab())
            }
            item(ModMirageFairy2019.MODID, "dish", { ItemBlock(blockDish) }) {
                unlocalizedName = "dish"
                if (ApiMain.side().isClient) {
                    ModelLoader.setCustomModelResourceLocation(this, 0, ModelResourceLocation(registryName!!, "normal"))
                }
            }
            tileEntity(ModMirageFairy2019.MODID, "dish", TileEntityDish::class.java)
            tileEntityRenderer(TileEntityDish::class.java, { TileEntityRendererDish() })

        }
    }
}
