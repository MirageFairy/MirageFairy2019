package miragefairy2019.mod.artifacts

import miragefairy2019.api.Erg
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.proxy
import miragefairy2019.lib.registryName
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.makeAdvancement
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.lib.skillContainer
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.OreIngredientComplex
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.green
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.WandType.BREAKING
import miragefairy2019.mod.artifacts.WandType.CRAFTING
import miragefairy2019.mod.artifacts.WandType.DISTORTION
import miragefairy2019.mod.artifacts.WandType.FREEZING
import miragefairy2019.mod.artifacts.WandType.FUSION
import miragefairy2019.mod.artifacts.WandType.HYDRATING
import miragefairy2019.mod.artifacts.WandType.MELTING
import miragefairy2019.mod.artifacts.WandType.POLISHING
import miragefairy2019.mod.artifacts.WandType.SUMMONING
import miragefairy2019.mod.fairystickcraft.ApiFairyStickCraft
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionReplaceBlock
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftRecipe
import miragefairy2019.mod.skill.ApiSkill
import miragefairy2019.mod.skill.Mastery
import miragefairy2019.mod.skill.displayName
import miragefairy2019.mod.skill.getSkillLevel
import miragefairy2019.mod.systems.IFairyStickCraftItem
import miragefairy2019.mod.systems.addFairyStickCraftCoolTime
import mirrg.kotlin.gson.hydrogen.jsonArray
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.jsonObjectNotNull
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.formatAs
import mirrg.kotlin.hydrogen.toLowerCamelCase
import mirrg.kotlin.hydrogen.toUpperCamelCase
import mirrg.kotlin.hydrogen.toUpperCaseHead
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import kotlin.math.ceil


enum class WandType(
    val erg: Erg,
    val tier: Int,
    val registryName: String,
    val englishName: String,
    val japaneseName: String,
    val additionalOreNames: List<String>
) {
    CRAFTING(Erg.CRAFT, 1, "crafting", "Crafting", "技巧", listOf()),
    HYDRATING(Erg.WATER, 1, "hydrating", "Hydrating", "加水", listOf("container1000Water")),
    MELTING(Erg.FLAME, 2, "melting", "Melting", "紅蓮", listOf()),
    BREAKING(Erg.DESTROY, 2, "breaking", "Breaking", "破砕", listOf()),
    FREEZING(Erg.FREEZE, 2, "freezing", "Freezing", "氷晶", listOf()),
    POLISHING(Erg.CRYSTAL, 3, "polishing", "Polishing", "珠玉", listOf()),
    SUMMONING(Erg.SUBMISSION, 3, "summoning", "Wizard's", "冥王", listOf()),
    DISTORTION(Erg.SPACE, 4, "distortion", "Distortion", "歪曲", listOf()),
    FUSION(Erg.WARP, 4, "fusion", "Fusion", "融合", listOf()),
}

val wandTierToRodOreName = mapOf(
    1 to "stickMirageFlower",
    2 to "rodMiragium",
    3 to "mirageFairy2019ManaRodQuartz",
    4 to "stickMirageFairyWood",
    5 to "rodMirageFairyPlastic"
)

enum class WandKind(
    val parent: WandKind?,
    val type: WandType,
    val rank: Int,
    val englishPoem: String,
    val japanesePoem: String
) {
    CR1(null, CRAFTING, 1, "", "スフィアから聞こえる、妖精の声"),
    CR2(CR1, CRAFTING, 2, "", "靴を作ってくれる妖精さん"),
    CR3(CR2, CRAFTING, 3, "", "魔法の鍋掴み"),
    CR4(CR3, CRAFTING, 4, "", "妖精の3Dプリンター"),
    CR5(CR4, CRAFTING, 5, "", "これで料理をすれば手が油まみれにならずに済みます"),
    HY1(null, HYDRATING, 1, "", "物質生成の初歩"),
    HY2(HY1, HYDRATING, 2, "", "先端のこれはぷにぷにしている"),
    HY3(HY2, HYDRATING, 3, "", "直射日光を避けて保管してください。"),
    HY4(HY3, HYDRATING, 4, "", "マッサージに使うと気持ちよい"),
    HY5(HY4, HYDRATING, 5, "", "なぜか周囲の空気の湿度が下がる"),
    ME1(null, MELTING, 1, "", "金属を溶かすほどの情熱"),
    ME2(ME1, MELTING, 2, "", "高温注意！"),
    ME3(ME2, MELTING, 3, "", "かまどの火とは何かが違う"),
    ME4(ME3, MELTING, 4, "", "意外とこれ、触っても熱くないんです"),
    BR1(null, BREAKING, 1, "", "これで釘を打たないように"),
    BR2(BR1, BREAKING, 2, "", "振ると衝撃波が迸る"),
    BR3(BR2, BREAKING, 3, "", "実はガラスより脆い"),
    BR4(BR3, BREAKING, 4, "", "分子間の結合を弱める作用"),
    FR1(null, FREEZING, 1, "", "料理に大活躍"),
    FR2(FR1, FREEZING, 2, "", "物体のフォノンを消滅させる"),
    FR3(FR2, FREEZING, 3, "", "お手軽反エントロピー"),
    FR4(FR3, FREEZING, 4, "", "消えた熱エネルギーはどこへ？"),
    PO1(null, POLISHING, 1, "", "究極に手先の器用な妖精さん"),
    PO2(PO1, POLISHING, 2, "", "分子のセーター"),
    PO3(PO2, POLISHING, 3, "", "2個の宝石をくっつける。いつの間にか結晶の向きが揃っている"),
    SU1(null, SUMMONING, 1, "The magic of the contract: thia ri me sorie ge Fairy'a zi miyukto", "契約の魔法、ｼｱ ﾘ ﾒ ｿｰﾘｴ ｹﾞ ﾌｧｲﾘｱ ｼﾞ ﾐﾕｸﾄ"),
    SU2(SU1, SUMMONING, 2, "The magic of feeding: me Fairy'a ri me Crystal'a zi karto", "給餌の魔法、ﾒ ﾌｧｲﾘｱ ﾘ ﾒ ﾂﾘｽﾀｰﾗ ｼﾞ ｶﾙﾄ"),
    SU3(SU2, SUMMONING, 3, "The magic of return: me Fairy'a ri haito", "帰還の魔法、ﾒ ﾌｧｲﾘｱ ﾘ ﾊｲﾄ"),
    DI1(null, DISTORTION, 1, "", "空間がねじれている"),
    DI2(DI1, DISTORTION, 2, "", "エンダーチェストにエンダーチェストを入れると…？"),
    FU1(null, FUSION, 1, "", "4次元折り紙"),
    FU2(FU1, FUSION, 2, "", "いしのなかにいる"),
}


private fun Int.toRoman() = listOf("I", "II", "III", "IV", "V").getOrNull(this - 1) ?: throw IllegalArgumentException()

val WandType.oreName get() = "mirageFairy2019CraftingToolFairyWand${registryName.toUpperCamelCase()}"
val WandType.ingredient get() = OreIngredientComplex(oreName)
val WandType.ingredientData get() = DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = oreName)

val WandKind.tier get() = type.tier + (rank - 1)
val WandKind.registryName get() = "${type.registryName}_fairy_wand${if (rank == 1) "" else "_$rank"}"
val WandKind.unlocalizedName get() = "fairy_wand_${type.registryName}${if (rank == 1) "" else "_$rank"}".toLowerCamelCase()

val wandModule = module {

    // 翻訳生成
    onMakeLang {
        enJa("advancements.miragefairy2019.wand.root.title", "Wand", "ワンド")
        enJa("advancements.miragefairy2019.wand.root.description", "Wand", "ワンド")
        enJa("advancements.miragefairy2019.wand.all.description", "Get a specific item", "所定のアイテムを入手する")
    }

    // 実績生成
    makeAdvancement("wand/root") {
        jsonObject(
            "display" to jsonObjectNotNull(
                "icon" to jsonObject(
                    "item" to "miragefairy2019:crafting_fairy_wand_4".jsonElement
                ),
                "title" to jsonObject(
                    "translate" to "advancements.miragefairy2019.wand.root.title".jsonElement
                ),
                "description" to jsonObject(
                    "translate" to "advancements.miragefairy2019.wand.root.description".jsonElement
                ),
                "background" to "miragefairy2019:textures/blocks/nephrite_block.png".jsonElement
            ),
            "criteria" to jsonObject(
                "main" to jsonObject(
                    "trigger" to "minecraft:inventory_changed".jsonElement,
                    "conditions" to jsonObject(
                        "items" to jsonArray(
                            jsonObject(
                                "type" to "forge:ore_dict".jsonElement,
                                "ore" to "mirageFairy2019SphereAny".jsonElement
                            )
                        )
                    )
                )
            )
        )
    }

    // レシピ生成
    // 糸から技巧杖
    makeRecipe("crafting_fairy_wand_from_string") {
        DataShapedRecipe(
            pattern = listOf(
                " cS",
                " R ",
                "R  "
            ),
            key = mapOf(
                "c" to DataSimpleIngredient(item = "minecraft:string"),
                "R" to DataOreIngredient(ore = "stickMirageFlower"),
                "S" to DataOreIngredient(ore = "mirageFairy2019SphereCraft")
            ),
            result = DataResult(item = "miragefairy2019:crafting_fairy_wand")
        )
    }

    // レシピ登録
    // 丸石＞紅蓮→焼き石
    onAddRecipe {
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(MELTING.ingredient)
            it.conditions += FairyStickCraftConditionReplaceBlock({ Blocks.COBBLESTONE.defaultState }, { Blocks.STONE.defaultState })
        })
    }

    // 個別
    WandKind.values().forEach { wandKind ->

        // アイテム登録
        item({ ItemFairyWand() }, wandKind.registryName) {
            setUnlocalizedName("fairyWand${wandKind.type.registryName.toUpperCamelCase()}${if (wandKind.rank == 1) "" else "${wandKind.rank}"}")
            setCreativeTab { Main.creativeTab }
            setCustomModelResourceLocation()
            onInit {
                val durability = (1..wandKind.tier).fold(16) { a, _ -> a * 2 }
                item.maxDamage = durability - 1
                item.tier = wandKind.tier
            }
            onCreateItemStack {
                OreDictionary.registerOre(wandKind.type.oreName, item.createItemStack(metadata = OreDictionary.WILDCARD_VALUE))
                wandKind.type.additionalOreNames.forEach { OreDictionary.registerOre(it, item.createItemStack(metadata = OreDictionary.WILDCARD_VALUE)) }
            }
        }

        // アイテムモデル生成
        makeItemModel(wandKind.registryName) {
            DataModel(
                parent = "item/handheld",
                textures = mapOf(
                    "layer0" to "miragefairy2019:items/fairy_wand_rod_${wandKind.tier}",
                    "layer1" to "miragefairy2019:items/${wandKind.type.registryName}_fairy_wand"
                )
            )
        }

        // 翻訳生成
        onMakeLang {
            enJa(
                "item.fairyWand${wandKind.type.registryName.toUpperCamelCase()}${if (wandKind.rank == 1) "" else "${wandKind.rank}"}.name",
                "${wandKind.type.englishName} Wand${if (wandKind.rank == 1) "" else " ${wandKind.rank.toRoman()}"}",
                "${wandKind.type.japaneseName}のワンド${if (wandKind.rank == 1) "" else " ${wandKind.rank.toRoman()}"}"
            )
            enJa("item.${wandKind.unlocalizedName}.poem", wandKind.englishPoem, wandKind.japanesePoem)
        }

        // 実績生成
        makeAdvancement("wand/${wandKind.registryName}") {
            jsonObject(
                "display" to jsonObject(
                    "icon" to jsonObject(
                        "item" to "miragefairy2019:${wandKind.registryName}".jsonElement
                    ),
                    "title" to jsonObject(
                        "translate" to "item.${wandKind.unlocalizedName}.name".jsonElement
                    ),
                    "description" to jsonObject(
                        "translate" to "advancements.miragefairy2019.wand.all.description".jsonElement
                    )
                ),
                "parent" to "miragefairy2019:wand/${wandKind.parent?.registryName ?: "root"}".jsonElement,
                "criteria" to jsonObject(
                    "main" to jsonObject(
                        "trigger" to "minecraft:inventory_changed".jsonElement,
                        "conditions" to jsonObject(
                            "items" to jsonArray(
                                jsonObject(
                                    "item" to "miragefairy2019:${wandKind.registryName}".jsonElement
                                )
                            )
                        )
                    )
                )
            )
        }

        // レシピ生成
        makeRecipe(wandKind.registryName) {
            DataShapedRecipe(
                pattern = listOf(
                    " cS",
                    " R ",
                    "R  "
                ),
                key = mapOf(
                    "c" to DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandCrafting"),
                    "R" to DataOreIngredient(ore = wandTierToRodOreName[wandKind.tier]!!),
                    "S" to DataOreIngredient(ore = "mirageFairy2019Sphere${wandKind.type.erg.registryName.toUpperCaseHead()}")
                ),
                result = DataResult(item = "miragefairy2019:${wandKind.registryName}")
            )
        }

    }

}

class ItemFairyWand : Item(), IFairyStickCraftItem {
    var tier = 0

    init {
        setMaxStackSize(1)
    }


    // グラフィック
    @SideOnly(Side.CLIENT)
    override fun isFull3D() = true


    // ツールチップ
    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val player = Minecraft.getMinecraft().player ?: return

        if (canTranslate("$unlocalizedName.poem")) { // ポエム
            val string = translateToLocal("$unlocalizedName.poem")
            if (string.isNotBlank()) tooltip += formattedText { string() }
        }

        tooltip += formattedText { "Tier $tier"().aqua } // tier // TODO translate

        // 機能
        tooltip += formattedText { "右クリックでフェアリーステッキクラフト"().red } // 魔法 // TODO translate

        tooltip += formattedText { "使用可能回数: ${(getMaxDamage(itemStack) - getDamage(itemStack) + 1) atLeast 0}"().green } // 耐久値 TODO translate

        tooltip += formattedText { ("スキル: "() + Mastery.processing.displayName() + " (${ApiSkill.skillManager.getClientSkillContainer().getSkillLevel(Mastery.processing)})"()).gold } // TODO translate
        tooltip += formattedText { "クールタイム: ${getCoolTime(player) / 20.0 formatAs "%.2f"} 秒"().blue } // TODO translate

    }


    // ユーティリティの利用
    override fun isEnchantable(stack: ItemStack) = false // エンチャント不可
    override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment) = false // すべてのエンチャントが不適正
    override fun isBookEnchantable(stack: ItemStack, book: ItemStack) = false // 本を使用したエンチャント不可
    override fun isRepairable() = false // 金床での修理不可


    // フェアリーステッキクラフト関係

    private fun getCoolTime(player: EntityPlayer) = ceil(40.0 / (1.0 + 0.01 * player.proxy.skillContainer.getSkillLevel(Mastery.processing))).toInt()

    override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {

        // フェアリーステッキクラフトのレシピを判定
        val executor = null
            ?: ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(player, worldIn, pos) { player.getHeldItem(hand) }
            ?: ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(player, worldIn, pos.offset(facing)) { player.getHeldItem(hand) }
            ?: return EnumActionResult.PASS // マッチするレシピが無かった場合は抜ける

        // クラフトを実行
        executor.onCraft { player.setHeldItem(hand, it) }
        addFairyStickCraftCoolTime(player, getCoolTime(player))

        return EnumActionResult.SUCCESS
    }

    override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!world.isRemote) return // クライアント側のみ描画処理を行う
        if (!(world.rand.nextDouble() < 0.1)) return // 10Tickに1回の確率で描画を行う
        if (entity !is EntityPlayer) return // 主体はプレイヤーでなければならない
        if (!isSelected && entity.heldItemOffhand != itemStack) return // アイテムが異常な状態なら中止

        // プレイヤー視線判定
        val rayTraceResult: RayTraceResult? = rayTrace(world, entity, false)
        if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return  // 視線がブロックに当たらなかった場合は無視

        // レシピ判定
        val executor = null
            ?: ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(entity, world, rayTraceResult.blockPos) { itemStack }
            ?: ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(entity, world, rayTraceResult.blockPos.offset(rayTraceResult.sideHit)) { itemStack }
            ?: return // マッチするレシピが無かった場合は抜ける

        // 描画を実行
        executor.onUpdate()

    }

    override val isFairyStickCraftItem get() = true


    // クラフティングツール関係
    override fun hasContainerItem(itemStack: ItemStack) = !getContainerItem(itemStack).isEmpty
    override fun getContainerItem(itemStack: ItemStack): ItemStack {
        if (itemStack.itemDamage >= itemStack.maxDamage) return EMPTY_ITEM_STACK // 耐久を使い果たした
        return itemStack.copy().also { it.itemDamage = it.itemDamage + 1 }
    }

}
