package miragefairy2019.mod.fairy

import miragefairy2019.lib.entries
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.registerItemColorHandler
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.Main.side
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.kotlin.hydrogen.toUpperCamelCase
import mirrg.kotlin.hydrogen.toUpperCaseHead
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary

object Fairy {
    lateinit var creativeTabFairyMotif: CreativeTabs
    lateinit var creativeTabFairyRank: CreativeTabs
    lateinit var listItemFairy: List<() -> ItemFairy>
    val fairyModule = module {

        // クリエイティブタブ
        onInitCreativeTab {
            creativeTabFairyMotif = object : CreativeTabs("mirageFairy2019.fairy.motif") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = FairyTypes.instance.imperialTopaz.main.createItemStack()

                @SideOnly(Side.CLIENT)
                override fun displayAllRelevantItems(itemStacks: NonNullList<ItemStack>) {
                    listItemFairy.forEachIndexed { i, _ ->
                        FairyTypes.instance.variants.forEach { variant ->
                            if (variant.bundle.main.canSeeOnCreativeTab) {
                                itemStacks.add(variant.bundle[i].createItemStack())
                            }
                        }
                    }
                }

                override fun hasSearchBar() = true
            }.setBackgroundImageName("item_search.png")
            creativeTabFairyRank = object : CreativeTabs("mirageFairy2019.fairy.rank") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = FairyTypes.instance.moonstone.main.createItemStack()

                @SideOnly(Side.CLIENT)
                override fun displayAllRelevantItems(itemStacks: NonNullList<ItemStack>) {
                    FairyTypes.instance.variants.forEach { variant ->
                        if (variant.bundle.main.canSeeOnCreativeTab) {
                            listItemFairy.forEachIndexed { i, _ ->
                                itemStacks.add(variant.bundle[i].createItemStack())
                            }
                        }
                    }
                }

                override fun hasSearchBar() = true
            }.setBackgroundImageName("item_search.png")
        }
        onMakeLang {
            enJa("itemGroup.mirageFairy2019.fairy.motif", "Fairy: Motif", "妖精：モチーフ")
            enJa("itemGroup.mirageFairy2019.fairy.rank", "Fairy: Rank", "妖精：ランク")
        }

        val rankMax = 7

        // モチーフ
        onRegisterItem {
            FairyTypes.instance = FairyTypes(rankMax)
        }

        // アイテム
        listItemFairy = (1..rankMax).map { rank ->
            val dressColor = when (rank) {
                1 -> 0xFF8888
                2 -> 0x8888FF
                3 -> 0x88FF88
                4 -> 0xFFFF88
                5 -> 0x111111
                6 -> 0xFFFFFF
                7 -> 0x88FFFF
                else -> 0xFFFFFF
            }
            item({ ItemFairy(dressColor) }, if (rank == 1) "mirage_fairy" else "mirage_fairy_r$rank") {
                setUnlocalizedName("mirageFairyR$rank")
                onRegisterItem {
                    FairyTypes.instance.variants.forEach { variant ->
                        item.registerVariant(variant.id, variant.bundle[rank - 1])
                    }
                    if (side.isClient) {
                        item.variants.forEach { variant ->
                            ModelLoader.setCustomModelResourceLocation(item, variant.metadata, ModelResourceLocation(ResourceLocation(ModMirageFairy2019.MODID, "fairy"), "normal"))
                        }
                    }
                }
                registerItemColorHandler()
            }
        }
        makeItemModel("fairy") {
            DataModel(
                parent = "item/generated",
                textures = mapOf(
                    "layer0" to "miragefairy2019:items/fairy_layer0",
                    "layer1" to "miragefairy2019:items/fairy_layer1",
                    "layer2" to "miragefairy2019:items/fairy_layer2",
                    "layer3" to "miragefairy2019:items/fairy_layer3",
                    "layer4" to "miragefairy2019:items/fairy_layer4"
                )
            )
        }
        onMakeLang {
            enJa("item.mirageFairyR1.name", "Mirage Fairy", "妖精")
            enJa("item.mirageFairyR1.format", "%s", "%s")
            enJa("item.mirageFairyR2.name", "Mirage Fairy II", "妖精 II")
            enJa("item.mirageFairyR2.format", "%s II", "%s II")
            enJa("item.mirageFairyR3.name", "Mirage Fairy III", "妖精 III")
            enJa("item.mirageFairyR3.format", "%s III", "%s III")
            enJa("item.mirageFairyR4.name", "Mirage Fairy IV", "妖精 IV")
            enJa("item.mirageFairyR4.format", "%s IV", "%s IV")
            enJa("item.mirageFairyR5.name", "Mirage Fairy V", "妖精 V")
            enJa("item.mirageFairyR5.format", "%s V", "%s V")
            enJa("item.mirageFairyR6.name", "Mirage Fairy VI", "妖精 VI")
            enJa("item.mirageFairyR6.format", "%s VI", "%s VI")
            enJa("item.mirageFairyR7.name", "Mirage Fairy VII", "妖精 VII")
            enJa("item.mirageFairyR7.format", "%s VII", "%s VII")
        }

        // 鉱石辞書
        onCreateItemStack {
            FairyTypes.instance.variants.forEach { variant ->
                listItemFairy.forEachIndexed { i, _ ->

                    // 品種別
                    OreDictionary.registerOre("mirageFairy2019Fairy${variant.bundle[i].type.motif!!.resourcePath.toUpperCamelCase()}Rank${i + 1}", variant.bundle[i].createItemStack())

                    // エルグ別
                    variant.bundle[i].type.ergSet.entries.forEach {
                        if (it.second >= 10) {
                            OreDictionary.registerOre("mirageFairy2019FairyAbility${it.first.toString().toUpperCaseHead()}", variant.bundle[i].createItemStack())
                        }
                    }

                }
            }
        }

        // 凝縮・分散レシピ
        onAddRecipe {
            FairyTypes.instance.variants.forEach { variant ->
                (0 until listItemFairy.size - 1).forEach { i ->

                    // 凝縮
                    GameRegistry.addShapelessRecipe(
                        ResourceLocation("${ModMirageFairy2019.MODID}:condense_r${i}_fairy_${variant.bundle[i].type.motif!!.resourcePath}"),
                        ResourceLocation("${ModMirageFairy2019.MODID}:condense_r${i}_fairy_${variant.bundle[i].type.motif!!.resourcePath}"),
                        variant.bundle[i + 1].createItemStack(),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack())
                    )

                    // 分解
                    GameRegistry.addShapelessRecipe(
                        ResourceLocation("${ModMirageFairy2019.MODID}:decondense_r${i}_fairy_${variant.bundle[i].type.motif!!.resourcePath}"),
                        ResourceLocation("${ModMirageFairy2019.MODID}:decondense_r${i}_fairy_${variant.bundle[i].type.motif!!.resourcePath}"),
                        variant.bundle[i].createItemStack(8),
                        Ingredient.fromStacks(variant.bundle[i + 1].createItemStack())
                    )

                }
            }
        }

        onMakeLang {
            enJa("mirageFairy2019.fairy.air.name", "Airia", "空気精アイリャ")
            enJa("mirageFairy2019.fairy.water.name", "Wateria", "水精ワテーリャ")
            enJa("mirageFairy2019.fairy.fire.name", "Firia", "火精フィーリャ")
            enJa("mirageFairy2019.fairy.sun.name", "Sunia", "太陽精スーニャ")
            enJa("mirageFairy2019.fairy.stone.name", "Stonia", "石精ストーニャ")
            enJa("mirageFairy2019.fairy.dirt.name", "Dirtia", "土精ディルチャ")
            enJa("mirageFairy2019.fairy.iron.name", "Ironia", "鉄精イローニャ")
            enJa("mirageFairy2019.fairy.diamond.name", "Diamondia", "金剛石精ディアモンジャ")
            enJa("mirageFairy2019.fairy.redstone.name", "Redstonia", "赤石精レドストーニャ")
            enJa("mirageFairy2019.fairy.enderman.name", "Endermania", "終界人精エンデルマーニャ")
            enJa("mirageFairy2019.fairy.moon.name", "Moonia", "月精モーニャ")
            enJa("mirageFairy2019.fairy.sand.name", "Sandia", "砂精サンジャ")
            enJa("mirageFairy2019.fairy.gold.name", "Goldia", "金精ゴルジャ")
            enJa("mirageFairy2019.fairy.spider.name", "Spideria", "蜘蛛精スピデーリャ")
            enJa("mirageFairy2019.fairy.skeleton.name", "Skeletonia", "骸骨精スケレトーニャ")
            enJa("mirageFairy2019.fairy.zombie.name", "Zombia", "硬屍精ゾンビャ")
            enJa("mirageFairy2019.fairy.creeper.name", "Creeperia", "匠精ツレペーリャ")
            enJa("mirageFairy2019.fairy.wheat.name", "Wheatia", "麦精ウェアーチャ")
            enJa("mirageFairy2019.fairy.lilac.name", "Lilacia", "紫丁香精リラーツァ")
            enJa("mirageFairy2019.fairy.torch.name", "Torchia", "松明精トルキャ")
            enJa("mirageFairy2019.fairy.lava.name", "Lavia", "溶岩精ラーヴャ")
            enJa("mirageFairy2019.fairy.star.name", "Staria", "星精スターリャ")
            enJa("mirageFairy2019.fairy.gravel.name", "Gravelia", "砂利精グラヴェーリャ")
            enJa("mirageFairy2019.fairy.emerald.name", "Emeraldia", "翠玉精エメラルジャ")
            enJa("mirageFairy2019.fairy.lapislazuli.name", "Lapislazulia", "瑠璃石精ラピスラズーリャ")
            enJa("mirageFairy2019.fairy.ender_dragon.name", "Endere Dragonia", "終界龍精エンデレドラゴーニャ")
            enJa("mirageFairy2019.fairy.wither_skeleton.name", "Withere Dkeletonia", "枯骸骨精ウィーテレスケレトーニャ")
            enJa("mirageFairy2019.fairy.wither.name", "Witheria", "枯精ウィテーリャ")
            enJa("mirageFairy2019.fairy.thunder.name", "Thunderia", "雷精ツンデーリャ")
            enJa("mirageFairy2019.fairy.chicken.name", "Chickenia", "鶏精キッケーニャ")
            enJa("mirageFairy2019.fairy.furnace.name", "Furnacia", "釜戸精フルナーツァ")
            enJa("mirageFairy2019.fairy.magenta_glazed_terracotta.name", "Magente Glazede Terracottia", "赤紫釉陶精マゲンテグラゼデテッラツォッチャ")
            enJa("mirageFairy2019.fairy.bread.name", "Breadia", "麺麭精ブレアージャ")
            enJa("mirageFairy2019.fairy.daytime.name", "Daytimia", "昼精ダイティーミャ")
            enJa("mirageFairy2019.fairy.night.name", "Nightia", "夜精ニグチャ")
            enJa("mirageFairy2019.fairy.morning.name", "Morningia", "朝精モルニンギャ")
            enJa("mirageFairy2019.fairy.fine.name", "Finia", "晴精フィーニャ")
            enJa("mirageFairy2019.fairy.rain.name", "Rainia", "雨精ライニャ")
            enJa("mirageFairy2019.fairy.plains.name", "Plainsia", "平原精プラインシャ")
            enJa("mirageFairy2019.fairy.forest.name", "Forestia", "森精フォレスチャ")
            enJa("mirageFairy2019.fairy.apple.name", "Applia", "林檎精アップーリャ")
            enJa("mirageFairy2019.fairy.carrot.name", "Carrotia", "人参精ツァッローチャ")
            enJa("mirageFairy2019.fairy.cactus.name", "Cactusia", "仙人掌精ツァツトゥーシャ")
            enJa("mirageFairy2019.fairy.axe.name", "Axia", "斧精アーシャ")
            enJa("mirageFairy2019.fairy.chest.name", "Chestia", "箱精ケスチャ")
            enJa("mirageFairy2019.fairy.crafting_table.name", "Craftinge Tablia", "作業台精ツラフティンゲターブリャ")
            enJa("mirageFairy2019.fairy.potion.name", "Potionia", "薬精ポティオーニャ")
            enJa("mirageFairy2019.fairy.sword.name", "Swordia", "剣精スウォルジャ")
            enJa("mirageFairy2019.fairy.dispenser.name", "Dispenseria", "発射機精ディスペンセーリャ")
            enJa("mirageFairy2019.fairy.ocean.name", "Oceania", "海精オツェアーニャ")
            enJa("mirageFairy2019.fairy.fish.name", "Fishia", "魚精フィーシャ")
            enJa("mirageFairy2019.fairy.cod.name", "Codia", "鱈精ツォージャ")
            enJa("mirageFairy2019.fairy.salmon.name", "Salmonia", "鮭精サルモーニャ")
            enJa("mirageFairy2019.fairy.pufferfish.name", "Pufferfishia", "河豚精プッフェルフィーシャ")
            enJa("mirageFairy2019.fairy.clownfish.name", "Clownfishia", "隈之実精ツロウンフィーシャ")
            enJa("mirageFairy2019.fairy.spruce.name", "Sprucia", "松精スプルーツァ")
            enJa("mirageFairy2019.fairy.anvil.name", "Anvilia", "金床精アンヴィーリャ")
            enJa("mirageFairy2019.fairy.obsidian.name", "Obsidiania", "黒耀石精オブシディアーニャ")
            enJa("mirageFairy2019.fairy.seed.name", "Seedia", "種精セージャ")
            enJa("mirageFairy2019.fairy.enchant.name", "Enchantia", "付魔精エンキャンチャ")
            enJa("mirageFairy2019.fairy.glowstone.name", "Glowstonia", "蛍光石精グロウストーニャ")
            enJa("mirageFairy2019.fairy.coal.name", "Coalia", "石炭精ツォアーリャ")
            enJa("mirageFairy2019.fairy.villager.name", "Villageria", "村人精ヴィッラゲーリャ")
            enJa("mirageFairy2019.fairy.librarian.name", "Librariania", "司書精リブラリアーニャ")
            enJa("mirageFairy2019.fairy.nether_star.name", "Nethere Staria", "地獄星精ネテーレスターリャ")
            enJa("mirageFairy2019.fairy.brewing_stand.name", "Brewinge Standia", "醸造台精ブレウィンゲスタンジャ")
            enJa("mirageFairy2019.fairy.hoe.name", "Hia", "鍬精ヒャ")
            enJa("mirageFairy2019.fairy.shield.name", "Shieldia", "盾精シエルジャ")
            enJa("mirageFairy2019.fairy.hopper.name", "Hopperia", "漏斗精ホッペーリャ")
            enJa("mirageFairy2019.fairy.mina.name", "Minia", "銀子精ミーニャ")
            enJa("mirageFairy2019.fairy.magnetite.name", "Magnetitia", "磁鉄鉱精マグネティーチャ")
            enJa("mirageFairy2019.fairy.sulfur.name", "Sulfuria", "硫黄精スルフーリャ")
            enJa("mirageFairy2019.fairy.apatite.name", "Apatitia", "燐灰石精アパティーチャ")
            enJa("mirageFairy2019.fairy.cinnabar.name", "Cinnabaria", "辰砂精ツィナバーリャ")
            enJa("mirageFairy2019.fairy.fluorite.name", "Fluoritia", "蛍石精フルオリーチャ")
            enJa("mirageFairy2019.fairy.moonstone.name", "Moonstonia", "月長石精モーンストーニャ")
            enJa("mirageFairy2019.fairy.pyrope.name", "Pyropia", "苦礬柘榴石精ピローピャ")
            enJa("mirageFairy2019.fairy.smithsonite.name", "Smithsonitia", "菱亜鉛鉱精スミッソニーチャ")
            enJa("mirageFairy2019.fairy.christmas.name", "Christmasia", "聖夜精キュリストマーシャ")
            enJa("mirageFairy2019.fairy.santa_claus.name", "Sante Clausia", "聖夜老人精サンテツラウシャ")
            enJa("mirageFairy2019.fairy.ice.name", "Icia", "氷精イーツァ")
            enJa("mirageFairy2019.fairy.packed_ice.name", "Packede Icia", "氷塊精パッケデイーツァ")
            enJa("mirageFairy2019.fairy.golem.name", "Golemia", "鉄魔像精ゴレーミャ")
            enJa("mirageFairy2019.fairy.glass.name", "Glassia", "硝子精グラッシャ")
            enJa("mirageFairy2019.fairy.activator_rail.name", "Activatore Railia", "活性軌条精アツティヴァトーレライリャ")
            enJa("mirageFairy2019.fairy.iron_bars.name", "Irone Barsia", "鉄格子精イローネバルシャ")
            enJa("mirageFairy2019.fairy.taiga.name", "Taigia", "針葉樹林精タイギャ")
            enJa("mirageFairy2019.fairy.desert.name", "Desertia", "砂漠精デセルチャ")
            enJa("mirageFairy2019.fairy.mountain.name", "Mountainia", "山精モウンタイニャ")
            enJa("mirageFairy2019.fairy.time.name", "Timia", "時精ティーミャ")
            enJa("mirageFairy2019.fairy.nephrite.name", "Nephritia", "軟玉精ネフリーチャ")
            enJa("mirageFairy2019.fairy.tourmaline.name", "Tourmalinia", "電気石精トウルマリーニャ")
            enJa("mirageFairy2019.fairy.topaz.name", "Topazia", "黄玉精トパージャ")
            enJa("mirageFairy2019.fairy.cow.name", "Cowia", "牛精ツォーウャ")
            enJa("mirageFairy2019.fairy.pig.name", "Pigia", "豚精ピーギャ")
            enJa("mirageFairy2019.fairy.sugar.name", "Sugaria", "砂糖精スガーリャ")
            enJa("mirageFairy2019.fairy.cake.name", "Cakia", "蛋麭精ツァーキャ")
            enJa("mirageFairy2019.fairy.cookie.name", "Cookia", "麭精ツォーキャ")
            enJa("mirageFairy2019.fairy.dark_chocolate.name", "Darke Chocolatia", "濃智代子齢糖精ダルケキョツォラーチャ")
            enJa("mirageFairy2019.fairy.enchanted_golden_apple.name", "Enchantede Goldene Applia", "付魔金林檎精エンキャンテーデゴルデーネアップーリャ")
            enJa("mirageFairy2019.fairy.fortune.name", "Fortunia", "幸運精フォルトゥーニャ")
            enJa("mirageFairy2019.fairy.rotten_flesh.name", "Rottene Fleshia", "腐肉精ロッテーネフレーシャ")
            enJa("mirageFairy2019.fairy.poisonous_potato.name", "Poisonouse Potatia", "悪芋精ポイソノウセポターチャ")
            enJa("mirageFairy2019.fairy.melon.name", "Melonia", "西瓜精メローニャ")
            enJa("mirageFairy2019.fairy.baked_potato.name", "Bakede Potatia", "焼芋精バケーデポターチャ")
            enJa("mirageFairy2019.fairy.cooked_chicken.name", "Cookede Chickenia", "焼鶏精ツォーケーデキッケーニャ")
            enJa("mirageFairy2019.fairy.cooked_salmon.name", "Cookede Salmonia", "焼鮭精ツォーケーデサルモーニャ")
            enJa("mirageFairy2019.fairy.steak.name", "Steakia", "焼牛精ステアキャ")
            enJa("mirageFairy2019.fairy.golden_apple.name", "Goldene Applia", "金林檎精ゴルデーネアップーリャ")
            enJa("mirageFairy2019.fairy.cupid.name", "Cupidia", "恋射手精ツピージャ")
            enJa("mirageFairy2019.fairy.spider_eye.name", "Spidere Eyia", "蜘蛛目精スピデーレエーヤ")
            enJa("mirageFairy2019.fairy.shulker.name", "Shulkeria", "潜影貝精シュルケーリャ")
            enJa("mirageFairy2019.fairy.slime.name", "Slimia", "吐泥精スリーミャ")
            enJa("mirageFairy2019.fairy.magma_cube.name", "Magme Cubia", "溶岩賽精マグメツービャ")
            enJa("mirageFairy2019.fairy.blaze.name", "Blazia", "烈炎精ブラージャ")
            enJa("mirageFairy2019.fairy.beetroot.name", "Beetrootia", "火焔菜精ベートローチャ")
            enJa("mirageFairy2019.fairy.pumpkin_pie.name", "Pumpkine Pia", "南瓜餡餅精プンプキーネピャ")
            enJa("mirageFairy2019.fairy.beetroot_soup.name", "Beetroote Soupia", "火焔菜汁精ベートローテソウピャ")
            enJa("mirageFairy2019.fairy.eleven.name", "Elevenia", "十一精エレヴェーニャ")
            enJa("mirageFairy2019.fairy.imperial_topaz.name", "Imperiale Topazia", "皇帝黄玉精インペリアーレトパージャ")
            enJa("mirageFairy2019.fairy.door.name", "Dooria", "扉精ドーリャ")
            enJa("mirageFairy2019.fairy.iron_door.name", "Irone Dooria", "鉄扉精イローネドーリャ")
            enJa("mirageFairy2019.fairy.redstone_repeater.name", "Redstone Repeateria", "赤石中継器精レドストーネレペアテーリャ")
            enJa("mirageFairy2019.fairy.redstone_comparator.name", "Redstone Comparatoria", "赤石比較器精レドストーネコンパラトーリャ")
            enJa("mirageFairy2019.fairy.chorus_fruit.name", "Choruse Fruitia", "紫頌果精キョルーセフルイチャ")
            enJa("mirageFairy2019.fairy.mushroom_stew.name", "Mushroome Stewia", "茸煮精ムシュローメステーウャ")
            enJa("mirageFairy2019.fairy.raw_rabbit.name", "Rawe Rabbitia", "生兎精ラーウェラッビーチャ")
            enJa("mirageFairy2019.fairy.mirage_flower.name", "Mirage Floweria", "妖花精ミラーゲフロウェーリャ")
            enJa("mirageFairy2019.fairy.sunrise.name", "Sunrisia", "日昇精スンリーシャ")
            enJa("mirageFairy2019.fairy.hatsuyume.name", "Hatsuyumia", "初夢精ハツユーミャ")
            enJa("mirageFairy2019.fairy.player.name", "Playeria", "人精プライェーリャ")
            enJa("mirageFairy2019.fairy.charged_creeper.name", "Chargede Creeperia", "巨匠精キャルゲーデツレペーリャ")
            enJa("mirageFairy2019.fairy.sugar_cane.name", "Sugare Cania", "砂糖黍精スガーレツァーニャ")
            enJa("mirageFairy2019.fairy.potato.name", "Potatia", "芋精ポターチャ")
            enJa("mirageFairy2019.fairy.note.name", "Notia", "音符精ノーチャ")
            enJa("mirageFairy2019.fairy.jukebox.name", "Jukeboxia", "蓄音機精ジュケボーシャ")
            enJa("mirageFairy2019.fairy.nether_portal.name", "Nethere Portalia", "地獄門精ネテーレポルターリャ")
            enJa("mirageFairy2019.fairy.mirage.name", "Miragia", "妖精ミラージャ")
            enJa("mirageFairy2019.fairy.coal_dust.name", "Coale Dustia", "石炭粉精ツォアーレドゥスチャ")
            enJa("mirageFairy2019.fairy.diamond_dust.name", "Diamonde Dustia", "金剛石粉精ディアモンデドゥスチャ")
            enJa("mirageFairy2019.fairy.button.name", "Buttonia", "釦精ブットーニャ")
            enJa("mirageFairy2019.fairy.cooked_cod.name", "Cookede Codia", "焼鱈精ツォーケーデツォージャ")
            enJa("mirageFairy2019.fairy.peony.name", "Peonia", "牡丹精ペオーニャ")
            enJa("mirageFairy2019.fairy.book.name", "Bookia", "本精ボーキャ")
            enJa("mirageFairy2019.fairy.bedrock.name", "Bedrockia", "岩盤精ベドロッキャ")
            enJa("mirageFairy2019.fairy.bat.name", "Batia", "蝙蝠精バーチャ")
            enJa("mirageFairy2019.fairy.curse_of_vanishing.name", "Curse Ofe Vanishingia", "消滅呪精ツルセオフェヴァニシンギャ")
            enJa("mirageFairy2019.fairy.gravity.name", "Gravitia", "重力精グラヴィーチャ")
            enJa("mirageFairy2019.fairy.pyrite.name", "Pyritia", "黄鉄鉱精ピリーチャ")
            enJa("mirageFairy2019.fairy.red_spinel.name", "Rede Spinelia", "赤尖晶石精レーデスピネーリャ")
        }

    }
}
