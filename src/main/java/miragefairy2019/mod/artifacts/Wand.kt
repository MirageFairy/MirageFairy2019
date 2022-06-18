package miragefairy2019.mod.artifacts

import miragefairy2019.lib.proxy
import miragefairy2019.lib.skillContainer
import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapedRecipe
import miragefairy2019.libkt.DataSimpleIngredient
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.OreIngredientComplex
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.green
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeItemModel
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.module
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
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
import miragefairy2019.mod.skill.EnumMastery
import miragefairy2019.mod.skill.displayName
import miragefairy2019.mod.skill.getSkillLevel
import miragefairy2019.mod.systems.IFairyStickCraftItem
import miragefairy2019.mod.systems.addFairyStickCraftCoolTime
import mirrg.kotlin.formatAs
import mirrg.kotlin.gson.jsonElement
import mirrg.kotlin.gson.jsonElementNotNull
import mirrg.kotlin.toLowerCamelCase
import mirrg.kotlin.toUpperCamelCase
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

private val Int.roman get() = listOf("I", "II", "III", "IV", "V").getOrNull(this - 1) ?: throw IllegalArgumentException()

enum class WandType(val registryName: String) {
    CRAFTING("crafting"),
    HYDRATING("hydrating"),
    MELTING("melting"),
    BREAKING("breaking"),
    FREEZING("freezing"),
    POLISHING("polishing"),
    SUMMONING("summoning"),
    DISTORTION("distortion"),
    FUSION("fusion"),
    ;

    val oreName get() = "mirageFairy2019CraftingToolFairyWand${registryName.toUpperCamelCase()}"
    val ingredient get() = OreIngredientComplex(oreName)
    val ingredientData get() = DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = oreName)
}

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

val WandKind.registryName get() = "${type.registryName}_fairy_wand${if (rank == 1) "" else "_$rank"}"
val WandKind.unlocalizedName get() = "fairy_wand_${type.registryName}${if (rank == 1) "" else "_$rank"}".toLowerCamelCase()

object Wand {
    val module = module {

        fun <T : ItemFairyWand> fairyWand(tier: Int, type: WandType, englishType: String, japaneseType: String, number: Int, creator: () -> T, vararg additionalOreNames: String) {
            val registryName = "${type.registryName}_fairy_wand${if (number == 1) "" else "_$number"}"
            item(creator, registryName) {
                setUnlocalizedName("fairyWand${type.registryName.toUpperCamelCase()}${if (number == 1) "" else "$number"}")
                setCreativeTab { Main.creativeTab }
                setCustomModelResourceLocation()
                onInit {
                    val durability = (1..tier).fold(16) { a, _ -> a * 2 }
                    item.maxDamage = durability - 1
                    item.tier = tier
                }
                onCreateItemStack {
                    OreDictionary.registerOre(type.oreName, item.createItemStack(metadata = OreDictionary.WILDCARD_VALUE))
                    additionalOreNames.forEach { OreDictionary.registerOre(it, item.createItemStack(metadata = OreDictionary.WILDCARD_VALUE)) }
                }
            }
            makeItemModel(ResourceName(ModMirageFairy2019.MODID, registryName)) {
                jsonElement(
                    "parent" to "item/handheld".jsonElement,
                    "textures" to jsonElement(
                        "layer0" to "miragefairy2019:items/fairy_wand_rod_$tier".jsonElement,
                        "layer1" to "miragefairy2019:items/${type.registryName}_fairy_wand".jsonElement
                    )
                )
            }
            onMakeLang {
                enJa(
                    "item.fairyWand${type.registryName.toUpperCamelCase()}${if (number == 1) "" else "$number"}.name",
                    "$englishType Wand${if (number == 1) "" else " ${number.roman}"}",
                    "${japaneseType}のワンド${if (number == 1) "" else " ${number.roman}"}"
                )
            }
        }
        (1..5).forEach { fairyWand(it, WandType.CRAFTING, "Crafting", "技巧", it, { ItemFairyWand() }) }
        (1..5).forEach { fairyWand(it, WandType.HYDRATING, "Hydrating", "加水", it, { ItemFairyWand() }, "container1000Water") }
        (1..4).forEach { fairyWand(it + 1, WandType.MELTING, "Melting", "紅蓮", it, { ItemFairyWand() }) }
        (1..4).forEach { fairyWand(it + 1, WandType.BREAKING, "Breaking", "破砕", it, { ItemFairyWand() }) }
        (1..4).forEach { fairyWand(it + 1, WandType.FREEZING, "Freezing", "氷晶", it, { ItemFairyWand() }) }
        (1..3).forEach { fairyWand(it + 2, WandType.POLISHING, "Polishing", "珠玉", it, { ItemFairyWand() }) }
        (1..3).forEach { fairyWand(it + 2, WandType.SUMMONING, "Wizard's", "冥王", it, { ItemFairyWand() }) }
        (1..2).forEach { fairyWand(it + 3, WandType.DISTORTION, "Distortion", "歪曲", it, { ItemFairyWand() }) }
        (1..2).forEach { fairyWand(it + 3, WandType.FUSION, "Fusion", "融合", it, { ItemFairyWand() }) }

        onMakeLang {
            WandKind.values().forEach { wandKind ->
                enJa("item.${wandKind.unlocalizedName}.poem", wandKind.englishPoem, wandKind.japanesePoem)
            }
        }

        onMakeLang {
            enJa("advancements.miragefairy2019.wand.root.title", "Wand", "ワンド")
            enJa("advancements.miragefairy2019.wand.root.description", "Wand", "ワンド")
            enJa("advancements.miragefairy2019.wand.all.description", "Get a specific item", "所定のアイテムを入手する")
        }
        onMakeResource {

            // ルート
            dirBase.resolve("assets/miragefairy2019/advancements/wand/root.json").place(
                jsonElement(
                    "display" to jsonElementNotNull(
                        "icon" to jsonElement(
                            "item" to "miragefairy2019:crafting_fairy_wand_4".jsonElement
                        ),
                        "title" to jsonElement(
                            "translate" to "advancements.miragefairy2019.wand.root.title".jsonElement
                        ),
                        "description" to jsonElement(
                            "translate" to "advancements.miragefairy2019.wand.root.description".jsonElement
                        ),
                        "background" to "miragefairy2019:textures/blocks/nephrite_block.png".jsonElement
                    ),
                    "criteria" to jsonElement(
                        "main" to jsonElement(
                            "trigger" to "minecraft:inventory_changed".jsonElement,
                            "conditions" to jsonElement(
                                "items" to jsonElement(
                                    jsonElement(
                                        "type" to "forge:ore_dict".jsonElement,
                                        "ore" to "mirageFairy2019SphereAny".jsonElement
                                    )
                                )
                            )
                        )
                    )
                )
            )

            // 各種
            WandKind.values().forEach { wandKind ->
                dirBase.resolve("assets/miragefairy2019/advancements/wand/${wandKind.registryName}.json").place(
                    jsonElement(
                        "display" to jsonElement(
                            "icon" to jsonElement(
                                "item" to "miragefairy2019:${wandKind.registryName}".jsonElement
                            ),
                            "title" to jsonElement(
                                "translate" to "item.${wandKind.unlocalizedName}.name".jsonElement
                            ),
                            "description" to jsonElement(
                                "translate" to "advancements.miragefairy2019.wand.all.description".jsonElement
                            )
                        ),
                        "parent" to "miragefairy2019:wand/${wandKind.parent?.registryName ?: "root"}".jsonElement,
                        "criteria" to jsonElement(
                            "main" to jsonElement(
                                "trigger" to "minecraft:inventory_changed".jsonElement,
                                "conditions" to jsonElement(
                                    "items" to jsonElement(
                                        jsonElement(
                                            "item" to "miragefairy2019:${wandKind.registryName}".jsonElement
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            }

        }

        // 普通のワンド
        fun fairyWand(registerName: String, rod: String, erg: String) = makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, registerName),
            DataShapedRecipe(
                pattern = listOf(
                    " cS",
                    " R ",
                    "R  "
                ),
                key = mapOf(
                    "c" to DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandCrafting"),
                    "R" to DataOreIngredient(ore = rod),
                    "S" to DataOreIngredient(ore = "mirageFairy2019Sphere$erg")
                ),
                result = DataResult(item = "miragefairy2019:$registerName")
            )
        )

        val rs = listOf("stickMirageFlower", "rodMiragium", "mirageFairy2019ManaRodQuartz", "stickMirageFairyWood", "rodMirageFairyPlastic")
        fairyWand("crafting_fairy_wand", rs[0], "Craft")
        fairyWand("crafting_fairy_wand_2", rs[1], "Craft")
        fairyWand("crafting_fairy_wand_3", rs[2], "Craft")
        fairyWand("crafting_fairy_wand_4", rs[3], "Craft")
        fairyWand("crafting_fairy_wand_5", rs[4], "Craft")
        fairyWand("hydrating_fairy_wand", rs[0], "Water")
        fairyWand("hydrating_fairy_wand_2", rs[1], "Water")
        fairyWand("hydrating_fairy_wand_3", rs[2], "Water")
        fairyWand("hydrating_fairy_wand_4", rs[3], "Water")
        fairyWand("hydrating_fairy_wand_5", rs[4], "Water")
        fairyWand("melting_fairy_wand", rs[1], "Flame")
        fairyWand("melting_fairy_wand_2", rs[2], "Flame")
        fairyWand("melting_fairy_wand_3", rs[3], "Flame")
        fairyWand("melting_fairy_wand_4", rs[4], "Flame")
        fairyWand("breaking_fairy_wand", rs[1], "Destroy")
        fairyWand("breaking_fairy_wand_2", rs[2], "Destroy")
        fairyWand("breaking_fairy_wand_3", rs[3], "Destroy")
        fairyWand("breaking_fairy_wand_4", rs[4], "Destroy")
        fairyWand("freezing_fairy_wand", rs[1], "Freeze")
        fairyWand("freezing_fairy_wand_2", rs[2], "Freeze")
        fairyWand("freezing_fairy_wand_3", rs[3], "Freeze")
        fairyWand("freezing_fairy_wand_4", rs[4], "Freeze")
        fairyWand("polishing_fairy_wand", rs[2], "Crystal")
        fairyWand("polishing_fairy_wand_2", rs[3], "Crystal")
        fairyWand("polishing_fairy_wand_3", rs[4], "Crystal")
        fairyWand("summoning_fairy_wand", rs[2], "Submission")
        fairyWand("summoning_fairy_wand_2", rs[3], "Submission")
        fairyWand("summoning_fairy_wand_3", rs[4], "Submission")
        fairyWand("distortion_fairy_wand", rs[3], "Space")
        fairyWand("distortion_fairy_wand_2", rs[4], "Space")
        fairyWand("fusion_fairy_wand", rs[3], "Warp")
        fairyWand("fusion_fairy_wand_2", rs[4], "Warp")

        // 糸から技巧杖
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "crafting_fairy_wand_from_string"),
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
        )

        onAddRecipe {

            // 丸石＞紅蓮→焼き石
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(WandType.MELTING.ingredient)
                it.conditions += FairyStickCraftConditionReplaceBlock({ Blocks.COBBLESTONE.defaultState }, { Blocks.STONE.defaultState })
            })

        }

    }
}

open class ItemFairyWand : Item(), IFairyStickCraftItem {
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
        getMagicDescription(itemStack)?.let { tooltip += formattedText { it().red } } // 魔法

        tooltip += formattedText { "使用可能回数: ${(getMaxDamage(itemStack) - getDamage(itemStack) + 1).coerceAtLeast(0)}"().green } // 耐久値 TODO translate

        tooltip += formattedText { ("スキル: "() + EnumMastery.processing.displayName() + " (${ApiSkill.skillManager.getClientSkillContainer().getSkillLevel(EnumMastery.processing)})"()).gold } // TODO translate
        tooltip += formattedText { "クールタイム: ${getCoolTime(player) / 20.0 formatAs "%.2f"} 秒"().blue } // TODO translate

    }


    // ユーティリティの利用
    override fun isEnchantable(stack: ItemStack) = false // エンチャント不可
    override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment) = false // すべてのエンチャントが不適正
    override fun isBookEnchantable(stack: ItemStack, book: ItemStack) = false // 本を使用したエンチャント不可
    override fun isRepairable() = false // 金床での修理不可


    // フェアリーステッキクラフト関係

    @SideOnly(Side.CLIENT)
    open fun getMagicDescription(itemStack: ItemStack): String? = "右クリックでフェアリーステッキクラフト" // TODO translate

    fun getCoolTime(player: EntityPlayer) = ceil(40.0 / (1.0 + 0.01 * player.proxy.skillContainer.getSkillLevel(EnumMastery.processing))).toInt()

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
