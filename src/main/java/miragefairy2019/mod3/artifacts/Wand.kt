package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.BlockRegion
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.green
import miragefairy2019.libkt.item
import miragefairy2019.libkt.module
import miragefairy2019.libkt.orNull
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.modules.fairyweapon.FairyWeaponUtils
import miragefairy2019.mod3.fairystickcraft.ApiFairyStickCraft
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionReplaceBlock
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRecipe
import miragefairy2019.mod3.main.Main
import miragefairy2019.mod3.skill.EnumMastery
import miragefairy2019.mod3.skill.api.ApiSkill
import miragefairy2019.mod3.skill.displayName
import miragefairy2019.mod3.skill.getSkillLevel
import mirrg.kotlin.formatAs
import mirrg.kotlin.gson.jsonElement
import mirrg.kotlin.gson.jsonElementNotNull
import mirrg.kotlin.toUpperCamelCase
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.EnumAction
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreIngredient
import kotlin.math.ceil

private val Int.roman get() = listOf("I", "II", "III", "IV").getOrNull(this - 1) ?: throw IllegalArgumentException()

object Wand {
    val module = module {

        fun <T : ItemFairyWand> fairyWand(tier: Int, type: String, englishType: String, japaneseType: String, number: Int, creator: () -> T, vararg additionalOreNames: String) {
            item(creator, "${type}_fairy_wand${if (number == 1) "" else "_$number"}") {
                setUnlocalizedName("fairyWand${type.toUpperCamelCase()}${if (number == 1) "" else "$number"}")
                setCreativeTab { Main.creativeTab }
                setCustomModelResourceLocation()
                onInit {
                    val durability = (1..tier).fold(16) { a, _ -> a * 2 }
                    item.maxDamage = durability - 1
                    item.tier = tier
                }
                onCreateItemStack {
                    OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWand${type.toUpperCamelCase()}", item.createItemStack(metadata = OreDictionary.WILDCARD_VALUE))
                    additionalOreNames.forEach { OreDictionary.registerOre(it, item.createItemStack(metadata = OreDictionary.WILDCARD_VALUE)) }
                }
            }
            onMakeLang {
                enJa(
                    "item.fairyWand${type.toUpperCamelCase()}${if (number == 1) "" else "$number"}.name",
                    "$englishType Wand${if (number == 1) "" else " ${number.roman}"}",
                    "${japaneseType}のワンド${if (number == 1) "" else " ${number.roman}"}"
                )
            }
        }
        fairyWand(1, "crafting", "Crafting", "技巧", 1, { ItemFairyWand() })
        fairyWand(2, "crafting", "Crafting", "技巧", 2, { ItemFairyWand() })
        fairyWand(3, "crafting", "Crafting", "技巧", 3, { ItemFairyWand() })
        fairyWand(4, "crafting", "Crafting", "技巧", 4, { ItemFairyWand() })
        fairyWand(1, "hydrating", "Hydrating", "加水", 1, { ItemFairyWand() }, "container1000Water")
        fairyWand(2, "hydrating", "Hydrating", "加水", 2, { ItemFairyWand() }, "container1000Water")
        fairyWand(3, "hydrating", "Hydrating", "加水", 3, { ItemFairyWand() }, "container1000Water")
        fairyWand(4, "hydrating", "Hydrating", "加水", 4, { ItemFairyWand() }, "container1000Water")
        fairyWand(2, "melting", "Melting", "紅蓮", 1, { ItemFairyWand() })
        fairyWand(3, "melting", "Melting", "紅蓮", 2, { ItemFairyWand() })
        fairyWand(4, "melting", "Melting", "紅蓮", 3, { ItemFairyWand() })
        fairyWand(2, "breaking", "Breaking", "破砕", 1, { ItemFairyWand() })
        fairyWand(3, "breaking", "Breaking", "破砕", 2, { ItemFairyWand() })
        fairyWand(4, "breaking", "Breaking", "破砕", 3, { ItemFairyWand() })
        fairyWand(2, "freezing", "Freezing", "氷晶", 1, { ItemFairyWand() })
        fairyWand(3, "freezing", "Freezing", "氷晶", 2, { ItemFairyWand() })
        fairyWand(4, "freezing", "Freezing", "氷晶", 3, { ItemFairyWand() })
        fairyWand(3, "polishing", "Polishing", "珠玉", 1, { ItemFairyWand() })
        fairyWand(4, "polishing", "Polishing", "珠玉", 2, { ItemFairyWand() })
        fairyWand(3, "summoning", "Wizard's", "冥王", 1, { ItemFairyWandSummoning(2) })
        fairyWand(4, "summoning", "Wizard's", "冥王", 2, { ItemFairyWandSummoning(5) })
        fairyWand(4, "distortion", "Distortion", "歪曲", 1, { ItemFairyWand() })
        fairyWand(4, "fusion", "Fusion", "融合", 1, { ItemFairyWand() })

        onMakeLang {
            enJa("item.fairyWandCrafting.poem", "", "スフィアから聞こえる、妖精の声")
            enJa("item.fairyWandCrafting2.poem", "", "靴を作ってくれる妖精さん")
            enJa("item.fairyWandCrafting3.poem", "", "魔法の鍋掴み")
            enJa("item.fairyWandCrafting4.poem", "", "妖精の3Dプリンター")
            enJa("item.fairyWandHydrating.poem", "", "物質生成の初歩")
            enJa("item.fairyWandHydrating2.poem", "", "先端のこれはぷにぷにしている")
            enJa("item.fairyWandHydrating3.poem", "", "直射日光を避けて保管してください。")
            enJa("item.fairyWandHydrating4.poem", "", "マッサージに使うと気持ちよい")
            enJa("item.fairyWandMelting.poem", "", "金属を溶かすほどの情熱")
            enJa("item.fairyWandMelting2.poem", "", "高温注意！")
            enJa("item.fairyWandMelting3.poem", "", "かまどの火とは何かが違う")
            enJa("item.fairyWandBreaking.poem", "", "これで釘を打たないように")
            enJa("item.fairyWandBreaking2.poem", "", "振ると衝撃波が迸る")
            enJa("item.fairyWandBreaking3.poem", "", "実はガラスより脆い")
            enJa("item.fairyWandFreezing.poem", "", "料理に大活躍")
            enJa("item.fairyWandFreezing2.poem", "", "物体のフォノンを消滅させる")
            enJa("item.fairyWandFreezing3.poem", "", "お手軽反エントロピー")
            enJa("item.fairyWandPolishing.poem", "", "究極に手先の器用な妖精さん")
            enJa("item.fairyWandPolishing2.poem", "", "分子のセーター")
            enJa("item.fairyWandSummoning.poem", "The magic of the contract: thia ri me sorie ge Fairy'a zi miyukto", "契約の魔法、シア リ メ ソーリエ ゲ ファイリア ジ ミユクト")
            enJa("item.fairyWandSummoning2.poem", "The magic of feeding: me Fairy'a ri me Crystal'a zi karto", "給餌の魔法、メ ファイリア リ メ ツリスターラ ジ カルト")
            enJa("item.fairyWandDistortion.poem", "", "空間がねじれている")
            enJa("item.fairyWandFusion.poem", "", "4次元折り紙")
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
            class Achievement(val registerName: String, val unlocalizedName: String, val parent: Achievement? = null) {
                init {
                    dirBase.resolve("assets/miragefairy2019/advancements/wand/$registerName.json").place(
                        jsonElement(
                            "display" to jsonElement(
                                "icon" to jsonElement(
                                    "item" to "miragefairy2019:$registerName".jsonElement
                                ),
                                "title" to jsonElement(
                                    "translate" to "item.$unlocalizedName.name".jsonElement
                                ),
                                "description" to jsonElement(
                                    "translate" to "advancements.miragefairy2019.wand.all.description".jsonElement
                                )
                            ),
                            "parent" to "miragefairy2019:wand/${parent?.registerName ?: "root"}".jsonElement,
                            "criteria" to jsonElement(
                                "main" to jsonElement(
                                    "trigger" to "minecraft:inventory_changed".jsonElement,
                                    "conditions" to jsonElement(
                                        "items" to jsonElement(
                                            jsonElement(
                                                "item" to "miragefairy2019:$registerName".jsonElement
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                }
            }

            val crafting_fairy_wand = Achievement("crafting_fairy_wand", "fairyWandCrafting", null)
            val crafting_fairy_wand_2 = Achievement("crafting_fairy_wand_2", "fairyWandCrafting2", crafting_fairy_wand)
            val crafting_fairy_wand_3 = Achievement("crafting_fairy_wand_3", "fairyWandCrafting3", crafting_fairy_wand_2)
            val crafting_fairy_wand_4 = Achievement("crafting_fairy_wand_4", "fairyWandCrafting4", crafting_fairy_wand_3)
            val hydrating_fairy_wand = Achievement("hydrating_fairy_wand", "fairyWandHydrating", null)
            val hydrating_fairy_wand_2 = Achievement("hydrating_fairy_wand_2", "fairyWandHydrating2", hydrating_fairy_wand)
            val hydrating_fairy_wand_3 = Achievement("hydrating_fairy_wand_3", "fairyWandHydrating3", hydrating_fairy_wand_2)
            val hydrating_fairy_wand_4 = Achievement("hydrating_fairy_wand_4", "fairyWandHydrating4", hydrating_fairy_wand_3)
            val melting_fairy_wand = Achievement("melting_fairy_wand", "fairyWandMelting", null)
            val melting_fairy_wand_2 = Achievement("melting_fairy_wand_2", "fairyWandMelting2", melting_fairy_wand)
            val melting_fairy_wand_3 = Achievement("melting_fairy_wand_3", "fairyWandMelting3", melting_fairy_wand_2)
            val breaking_fairy_wand = Achievement("breaking_fairy_wand", "fairyWandBreaking", null)
            val breaking_fairy_wand_2 = Achievement("breaking_fairy_wand_2", "fairyWandBreaking2", breaking_fairy_wand)
            val breaking_fairy_wand_3 = Achievement("breaking_fairy_wand_3", "fairyWandBreaking3", breaking_fairy_wand_2)
            val freezing_fairy_wand = Achievement("freezing_fairy_wand", "fairyWandFreezing", null)
            val freezing_fairy_wand_2 = Achievement("freezing_fairy_wand_2", "fairyWandFreezing2", freezing_fairy_wand)
            val freezing_fairy_wand_3 = Achievement("freezing_fairy_wand_3", "fairyWandFreezing3", freezing_fairy_wand_2)
            val polishing_fairy_wand = Achievement("polishing_fairy_wand", "fairyWandPolishing", null)
            val polishing_fairy_wand_2 = Achievement("polishing_fairy_wand_2", "fairyWandPolishing2", polishing_fairy_wand)
            val summoning_fairy_wand = Achievement("summoning_fairy_wand", "fairyWandSummoning", null)
            val summoning_fairy_wand_2 = Achievement("summoning_fairy_wand_2", "fairyWandSummoning2", summoning_fairy_wand)
            val distortion_fairy_wand = Achievement("distortion_fairy_wand", "fairyWandDistortion", null)
            val fusion_fairy_wand = Achievement("fusion_fairy_wand", "fairyWandFusion", null)

        }

        onAddRecipe {

            // 丸石＞紅蓮→焼き石
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairy2019CraftingToolFairyWandMelting"))
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

        tooltip += formattedText { ("スキル: "() + EnumMastery.processing.displayName() + " (${ApiSkill.skillManager.clientSkillContainer.getSkillLevel(EnumMastery.processing)})"()).gold } // TODO translate
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

class ItemFairyWandSummoning(val maxTryCountPerTick: Int) : ItemFairyWand() {

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "右クリック長押しでフェアリークリスタルを高速消費" // TODO translate Hold right mouse button to use fairy crystals quickly

    //

    // TODO フェアリーステッキクラフト無効
    //override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) = EnumActionResult.PASS
    //override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) = Unit

    override fun getItemUseAction(stack: ItemStack) = EnumAction.BOW
    override fun getMaxItemUseDuration(stack: ItemStack) = 72000 // 永続

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        player.activeHand = hand
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }

    override fun onUsingTick(stack: ItemStack, entityLivingBase: EntityLivingBase, count: Int) {
        if (entityLivingBase.world.isRemote) return

        if (entityLivingBase is EntityPlayer) {
            repeat(getTryCount(count)) a@{
                if (!tryUseCrystal(entityLivingBase)) return@a
            }
        }
    }

    private fun tryUseCrystal(player: EntityPlayer): Boolean {

        // 妖晶を得る
        val itemStackFairyCrystal = FairyWeaponUtils.findItemOptional(player) { itemStack -> itemStack!!.item is ItemFairyCrystal }.orElse(null) ?: return false // クリスタルを持ってない場合は無視
        val variantFairyCrystal = (itemStackFairyCrystal.item as ItemFairyCrystal).getVariant(itemStackFairyCrystal) ?: return false // 異常なクリスタルを持っている場合は無視

        // プレイヤー視点判定
        val rayTraceResult = rayTrace(player.world, player, false) ?: return false // ブロックに当たらなかった場合は無視
        if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return false // ブロックに当たらなかった場合は無視

        // ガチャ環境計算
        val environment = FairyCrystalDropEnvironment(player, player.world, rayTraceResult.blockPos, rayTraceResult.sideHit)
        environment.insertItemStacks(player) // インベントリ
        environment.insertBlocks(player.world, BlockRegion(rayTraceResult.blockPos.add(-2, -2, -2), rayTraceResult.blockPos.add(2, 2, 2))) // ワールドブロック
        environment.insertBiome(player.world.getBiome(rayTraceResult.blockPos)) // バイオーム
        environment.insertEntities(player.world, player.positionVector, 10.0) // エンティティ

        // ガチャリスト取得
        val commonBoost = variantFairyCrystal.getRateBoost(DropCategory.COMMON, player.proxy.skillContainer)
        val rareBoost = variantFairyCrystal.getRateBoost(DropCategory.RARE, player.proxy.skillContainer)
        val dropTable = environment.getDropTable(variantFairyCrystal.dropRank, commonBoost, rareBoost)

        // ガチャを引く
        val itemStackDrop = dropTable.getRandomItem(player.world.rand)?.orNull ?: return false // ガチャが引けなかった場合は無視

        // 成立

        // ガチャアイテムを消費
        if (!player.isCreative) itemStackFairyCrystal.shrink(1)
        player.addStat(StatList.getObjectUseStats(itemStackFairyCrystal.item))

        // 妖精をドロップ
        val blockPos = rayTraceResult.blockPos.offset(rayTraceResult.sideHit)
        val entityItem = EntityItem(player.world, blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5, itemStackDrop.copy())
        entityItem.setNoPickupDelay()
        player.world.spawnEntity(entityItem)

        return true
    }

    private fun getTryCount(count: Int): Int {
        val t = 72000 - count
        return when {
            t >= 200 -> 5
            t >= 100 -> 2
            t >= 60 -> 1
            t >= 20 -> if (t % 2 == 0) 1 else 0
            t >= 5 -> if (t % 5 == 0) 1 else 0
            t == 1 -> 1
            else -> 0
        }.coerceAtMost(maxTryCountPerTick)
    }
}
