package miragefairy2019.mod.fairybox

import miragefairy2019.api.Erg
import miragefairy2019.api.FairyCentrifugeCraftRegistry
import miragefairy2019.api.Mana
import miragefairy2019.common.toOreName
import miragefairy2019.lib.createGui
import miragefairy2019.lib.fairyCentrifugeCraftHandler
import miragefairy2019.lib.process
import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.GuiHandlerContext
import miragefairy2019.libkt.ISimpleGuiHandler
import miragefairy2019.libkt.block
import miragefairy2019.libkt.copyItemStack
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.guiHandler
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.module
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.tileEntity
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.FairyMaterials
import miragefairy2019.mod.artifacts.oreName
import miragefairy2019.mod.artifacts.sphereType
import mirrg.kotlin.castOrNull
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemBlock

object FairyBox {
    val module = module {

        // 妖精の家（空）
        run {
            val block = block({ BlockFairyBoxBase { TileEntityFairyBoxEmpty() } }, "fairy_box") {
                setUnlocalizedName("fairyBox")
                setCreativeTab { Main.creativeTab }
                makeBlockStates {
                    DataBlockStates(
                        variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                            "facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_box", y = facing.second)
                        }
                    )
                }
            }
            val item = item({ ItemBlock(block()) }, "fairy_box") {
                setCustomModelResourceLocation(variant = "facing=north")
            }
            onMakeLang { enJa("tile.fairyBox.name", "Fairy Box", "妖精の家") }
            tileEntity("fairy_box", TileEntityFairyBoxEmpty::class.java)
        }

        // 樹液の家
        run {
            val block = block({ BlockFairyBoxBase { TileEntityFairyBoxResinTapper() } }, "fairy_resin_tapper") {
                setUnlocalizedName("fairyResinTapper")
                setCreativeTab { Main.creativeTab }
                makeBlockStates {
                    DataBlockStates(
                        variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                            "facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_resin_tapper", y = facing.second)
                        }
                    )
                }
            }
            val item = item({ ItemBlock(block()) }, "fairy_resin_tapper") {
                setCustomModelResourceLocation(variant = "facing=north")
            }
            onMakeLang { enJa("tile.fairyResinTapper.name", "Fairy Resin Tapper", "樹液取り職人スプルーツァの家") }
            tileEntity("fairy_resin_tapper", TileEntityFairyBoxResinTapper::class.java)
        }

        // 分離機
        run {
            val block = block({ BlockFairyBoxBase { TileEntityFairyBoxCentrifuge() } }, "fairy_centrifuge") {
                setUnlocalizedName("fairyCentrifuge")
                setCreativeTab { Main.creativeTab }
                makeBlockStates {
                    DataBlockStates(
                        variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                            "facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_centrifuge", y = facing.second)
                        }
                    )
                }
            }
            val item = item({ ItemBlock(block()) }, "fairy_centrifuge") {
                setCustomModelResourceLocation(variant = "facing=north")
            }
            onMakeLang { enJa("tile.fairyCentrifuge.name", "Fairy Centrifuge", "錬金術師グラヴィーチャの家") }
            tileEntity("fairy_centrifuge", TileEntityFairyBoxCentrifuge::class.java)
            onInit {
                Main.registerGuiHandler(GuiId.fairyBoxCentrifuge, object : ISimpleGuiHandler {
                    override fun GuiHandlerContext.onServer() = tileEntity?.castOrNull<TileEntityFairyBoxCentrifuge>()?.createContainer(player)
                    override fun GuiHandlerContext.onClient() = tileEntity?.castOrNull<TileEntityFairyBoxCentrifuge>()?.createContainer(player)?.createGui()
                }.guiHandler)
            }

            onAddRecipe {

                // 樹液16＋ガラス瓶→シロップ
                FairyCentrifugeCraftRegistry.fairyCentrifugeCraftHandlers += fairyCentrifugeCraftHandler(
                    process("ろ過", 20.0) { !Mana.DARK + !Erg.WATER }, // TODO translate
                    process("蒸留", 70.0) { !Mana.GAIA + !Erg.FLAME }, // TODO translate
                    process("調味", 10.0) { !Mana.AQUA + !Erg.LIFE }, // TODO translate
                    { FairyMaterials.itemVariants.fairySyrup.createItemStack() }, 0.5,
                    "mirageFairyWoodResin".oreIngredient to 16,
                    Items.GLASS_BOTTLE.ingredient to 1
                )

                // 樹液＋砂糖→砂糖3
                FairyCentrifugeCraftRegistry.fairyCentrifugeCraftHandlers += fairyCentrifugeCraftHandler(
                    process("ろ過", 20.0) { !Mana.DARK + !Erg.WATER }, // TODO translate
                    process("結晶化", 60.0) { !Mana.DARK + !Erg.CRYSTAL }, // TODO translate
                    process("粉砕", 20.0) { !Mana.GAIA + !Erg.DESTROY }, // TODO translate
                    { Items.SUGAR.createItemStack(3) }, 1.0,
                    "mirageFairyWoodResin".oreIngredient to 1,
                    Items.SUGAR.ingredient to 1
                )

                // グロウストーンダスト10＋ボウル→金3
                FairyCentrifugeCraftRegistry.fairyCentrifugeCraftHandlers += fairyCentrifugeCraftHandler(
                    process("融解", 30.0) { !Mana.GAIA + !Erg.FLAME }, // TODO translate
                    process("沈殿", 40.0) { !Mana.DARK + !Erg.SPACE }, // TODO translate
                    process("凝固", 30.0) { !Mana.GAIA + !Erg.FREEZE }, // TODO translate
                    { "ingotGold".toOreName().copyItemStack(3) }, 0.0,
                    "dustGlowstone".oreIngredient to 10,
                    Items.BOWL.ingredient to 1
                )

                // 硫黄3＋赤石10→辰砂3
                FairyCentrifugeCraftRegistry.fairyCentrifugeCraftHandlers += fairyCentrifugeCraftHandler(
                    process("融解", 30.0) { !Mana.GAIA + !Erg.FLAME }, // TODO translate
                    process("析出", 60.0) { !Mana.FIRE + !Erg.CHEMICAL }, // TODO translate
                    process("洗浄", 10.0) { !Mana.DARK + !Erg.WATER }, // TODO translate
                    { "dustCinnabar".toOreName().copyItemStack(3) }, 0.0,
                    "dustSulfur".oreIngredient to 3,
                    "dustRedstone".oreIngredient to 10
                )

                // 石炭の粉64＋フリント8＋黒曜石→ダイヤモンド
                FairyCentrifugeCraftRegistry.fairyCentrifugeCraftHandlers += fairyCentrifugeCraftHandler(
                    process("圧縮", 10.0) { !Mana.GAIA + !Erg.CRAFT }, // TODO translate
                    process("歪縮", 40.0) { !Mana.FIRE + !Erg.SPACE }, // TODO translate
                    process("結晶成長", 50.0) { !Mana.SHINE + !Erg.CRYSTAL }, // TODO translate
                    { "gemDiamond".toOreName().copyItemStack() }, 0.0,
                    "dustCoal".oreIngredient to 64,
                    Items.FLINT.ingredient to 8,
                    "obsidian".oreIngredient to 1
                )

                // マグマクリーム2＋雪玉→スライムボール
                FairyCentrifugeCraftRegistry.fairyCentrifugeCraftHandlers += fairyCentrifugeCraftHandler(
                    process("混合", 20.0) { !Mana.DARK + !Erg.DESTROY }, // TODO translate
                    process("凝固", 50.0) { !Mana.FIRE + !Erg.CHEMICAL }, // TODO translate
                    process("抽出", 30.0) { !Mana.GAIA + !Erg.CRAFT }, // TODO translate
                    { Items.SLIME_BALL.createItemStack() }, 0.0,
                    Items.MAGMA_CREAM.ingredient to 2,
                    Items.SNOWBALL.ingredient to 1
                )

                // マグマクリーム2＋コンクリートパウダー→ブレイズパウダー
                FairyCentrifugeCraftRegistry.fairyCentrifugeCraftHandlers += fairyCentrifugeCraftHandler(
                    process("混合", 20.0) { !Mana.DARK + !Erg.DESTROY }, // TODO translate
                    process("凝固", 50.0) { !Mana.FIRE + !Erg.CHEMICAL }, // TODO translate
                    process("抽出", 30.0) { !Mana.GAIA + !Erg.CRAFT }, // TODO translate
                    { Items.BLAZE_POWDER.createItemStack() }, 0.0,
                    Items.MAGMA_CREAM.ingredient to 2,
                    Blocks.CONCRETE_POWDER.ingredient!! to 1
                )

                // シュルカーの殻＋エンドストーン64＋ラブラドライト5＋空間のスフィア5→シュルカーの殻2

                // 妖精変成


                // R2蜘蛛精＋ミラジウムウォーター＋土→蜘蛛の目
                FairyCentrifugeCraftRegistry.fairyCentrifugeCraftHandlers += fairyCentrifugeCraftHandler(
                    process("形状抽出", 30.0) { !Mana.FIRE + !Erg.KNOWLEDGE }, // TODO translate
                    process("材料溶解", 30.0) { !Mana.GAIA + !Erg.CHEMICAL }, // TODO translate
                    process("分子合成", 40.0) { !Mana.SHINE + !Erg.LIFE }, // TODO translate
                    { Items.SPIDER_EYE.createItemStack() }, 1.0,
                    "mirageFairy2019FairySpiderRank2".oreIngredient to 1,
                    "container1000MiragiumWater".oreIngredient to 1,
                    "dirt".oreIngredient to 1
                )

            }
        }

    }
}
