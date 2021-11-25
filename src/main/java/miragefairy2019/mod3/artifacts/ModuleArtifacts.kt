package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.block
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.tileEntity
import miragefairy2019.libkt.tileEntityRenderer
import miragefairy2019.mod.lib.multi.ItemBlockMulti
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.item.ItemBlock
import java.util.function.Supplier

lateinit var blockDish: Supplier<BlockDish>
lateinit var itemDish: Supplier<ItemBlock>
lateinit var itemFertilizer: Supplier<ItemFertilizer>
lateinit var blockTwinkleStone: Supplier<BlockTwinkleStone>
lateinit var itemBlockTwinkleStone: Supplier<ItemBlockMulti<BlockTwinkleStone, EnumVariantTwinkleStone>>

val moduleArtifacts: Module = {

    // 皿
    blockDish = block({ BlockDish() }, "dish") {
        setUnlocalizedName("dish")
        setCreativeTab { ApiMain.creativeTab }
    }
    itemDish = item({ ItemBlock(blockDish.get()) }, "dish") {
        setUnlocalizedName("dish")
        setCustomModelResourceLocation()
    }
    tileEntity("dish", TileEntityDish::class.java)
    tileEntityRenderer(TileEntityDish::class.java, { TileEntityRendererDish() })

    // 肥料
    itemFertilizer = item({ ItemFertilizer() }, "fertilizer") {
        setUnlocalizedName("fertilizer")
        setCreativeTab { ApiMain.creativeTab }
        setCustomModelResourceLocation()
    }

    // トゥインクルストーン
    blockTwinkleStone = block({ BlockTwinkleStone() }, "twinkle_stone") {
        setCreativeTab { ApiMain.creativeTab }
    }
    itemBlockTwinkleStone = item({ ItemBlockMulti(blockTwinkleStone.get()) }, "twinkle_stone") {
        setUnlocalizedName("twinkleStone")
        //setCreativeTab{ApiMain.creativeTab}
        onRegisterItem {
            blockTwinkleStone.get().variantList.forEach { variant ->
                item.setCustomModelResourceLocation(variant.resourceName, variant.metadata)
            }
        }
        onCreateItemStack {
            blockTwinkleStone.get().variantList.forEach { variant ->
                variant.oreNames.forEach { oreName ->
                    item.addOreName(oreName, variant.metadata)
                }
            }
        }
    }

}
