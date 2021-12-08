package miragefairy2019.mod.modules.oreseed

import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.block
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.mod3.main.api.ApiMain
import miragefairy2019.mod3.oreseeddrop.api.EnumOreSeedType

object OreSeed {
    lateinit var blockOreSeed: () -> BlockOreSeed
    lateinit var blockOreSeedNether: () -> BlockOreSeed
    lateinit var blockOreSeedEnd: () -> BlockOreSeed
    val module: Module = {

        // 鉱石の種
        blockOreSeed = block({ BlockOreSeed(EnumOreSeedType.STONE) }, "ore_seed") {
            setCreativeTab { ApiMain.creativeTab }
            makeBlockStates { DataBlockStates(EnumVariantOreSeed.values().associate { "variant=$it" to DataBlockState("minecraft:stone") }) }
        }

        // 鉱石の種：ネザー
        blockOreSeedNether = block({ BlockOreSeed(EnumOreSeedType.NETHERRACK) }, "ore_seed_nether") {
            setCreativeTab { ApiMain.creativeTab }
            makeBlockStates { DataBlockStates(EnumVariantOreSeed.values().associate { "variant=$it" to DataBlockState("minecraft:netherrack") }) }
        }

        // 鉱石の種：エンド
        blockOreSeedEnd = block({ BlockOreSeed(EnumOreSeedType.END_STONE) }, "ore_seed_end") {
            setCreativeTab { ApiMain.creativeTab }
            makeBlockStates { DataBlockStates(EnumVariantOreSeed.values().associate { "variant=$it" to DataBlockState("minecraft:end_stone") }) }
        }

    }
}
