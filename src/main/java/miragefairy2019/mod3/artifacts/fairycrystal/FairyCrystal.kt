package miragefairy2019.mod3.artifacts.fairycrystal

import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapelessRecipe
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.module
import miragefairy2019.libkt.orNull
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.totalWeight
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.libkt.translateToLocalFormatted
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.ItemMulti
import miragefairy2019.mod.lib.ItemVariant
import miragefairy2019.mod3.main.api.ApiMain
import miragefairy2019.mod3.skill.EnumMastery
import miragefairy2019.mod3.skill.api.ApiSkill
import miragefairy2019.mod3.skill.api.ISkillContainer
import miragefairy2019.mod3.skill.displayName
import miragefairy2019.mod3.skill.getSkillLevel
import mirrg.kotlin.formatAs
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World
import net.minecraftforge.oredict.OreDictionary

object FairyCrystal {
    lateinit var itemFairyCrystal: () -> ItemFairyCrystal
    lateinit var variantFairyCrystal: () -> VariantFairyCrystal
    lateinit var variantFairyCrystalChristmas: () -> VariantFairyCrystal
    lateinit var variantFairyCrystalPure: () -> VariantFairyCrystal
    val module = module {

        // フェアリークリスタル
        itemFairyCrystal = item({ ItemFairyCrystal() }, "fairy_crystal") {
            setUnlocalizedName("fairyCrystal")
            setCreativeTab { ApiMain.creativeTab }

            class RecipeParameter(val inputOreName: String, val inputWand: String)

            fun fairyCrystal(
                metadata: Int, creator: (unlocalizedName: String) -> VariantFairyCrystal,
                registryName: String, unlocalizedName: String, oreName: String,
                english: String, japanese: String,
                recipeParameter: RecipeParameter?
            ): () -> VariantFairyCrystal {
                return itemVariant(registryName, { creator(unlocalizedName) }, metadata) {
                    setCustomModelResourceLocation(metadata, model = ResourceLocation(ModMirageFairy2019.MODID, registryName))
                    onCreateItemStack { OreDictionary.registerOre(oreName, itemVariant.createItemStack()) }
                    onCreateItemStack { OreDictionary.registerOre("mirageFairyCrystalAny", itemVariant.createItemStack()) }
                    onMakeLang { enJa("item.$unlocalizedName.name", english, japanese) }
                    if (recipeParameter != null) {
                        makeRecipe(
                            ResourceName(ModMirageFairy2019.MODID, registryName),
                            DataShapelessRecipe(
                                ingredients = listOf(
                                    DataOreIngredient(ore = recipeParameter.inputOreName),
                                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWand${recipeParameter.inputWand}")
                                ),
                                result = DataResult(
                                    item = "miragefairy2019:fairy_crystal",
                                    data = metadata
                                )
                            )
                        )
                    }
                }
            }

            variantFairyCrystal = fairyCrystal(
                0, { VariantFairyCrystalNormal(it, 0, DropCategory.RARE, 1.0) },
                "fairy_crystal", "fairyCrystal", "mirageFairyCrystal",
                "Fairy Crystal", "フェアリークリスタル",
                null
            )
            variantFairyCrystalChristmas = fairyCrystal(
                1, { VariantFairyCrystalNormal(it, 0, DropCategory.RARE, 1.0) },
                "christmas_fairy_crystal", "fairyCrystalChristmas", "mirageFairyCrystalChristmas",
                "Christmas Fairy Crystal", "聖夜のフェアリークリスタル",
                null
            )
            variantFairyCrystalPure = fairyCrystal(
                2, { VariantFairyCrystalNormal(it, 1, DropCategory.RARE, 2.0) },
                "pure_fairy_crystal", "fairyCrystalPure", "mirageFairyCrystalPure",
                "Pure Fairy Crystal", "高純度フェアリークリスタル",
                RecipeParameter("blockMirageFairyCrystal", "Polishing")
            )
            )

        }

    }
}

class ItemFairyCrystal : ItemMulti<VariantFairyCrystal>() {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val itemStackCrystal = player.getHeldItem(hand)
        if (itemStackCrystal.isEmpty) return EnumActionResult.PASS
        val variant = getVariant(itemStackCrystal) ?: return EnumActionResult.PASS
        val crystalItemStack = player.getHeldItem(hand).orNull ?: return EnumActionResult.PASS // アイテムが消えた場合は中止
        if (world.isRemote) return EnumActionResult.SUCCESS // サーバースレッドのみ

        val commonBoost = variant.getRateBoost(DropCategory.COMMON, ApiSkill.skillManager.getServerSkillContainer(player))
        val rareBoost = variant.getRateBoost(DropCategory.RARE, ApiSkill.skillManager.getServerSkillContainer(player))

        if (!player.isSneaking) {

            // ガチャを引く
            val resultItemStack = FairyCrystalDropper.drop(player, world, pos, hand, facing, hitX, hitY, hitZ, variant.dropRank, commonBoost, rareBoost)?.orNull ?: return EnumActionResult.SUCCESS

            // ガチャ成立

            // ガチャアイテムを消費
            crystalItemStack.shrink(1)
            StatList.getObjectUseStats(crystalItemStack.item)?.let { player.addStat(it) }

            // 妖精をドロップ
            val pos2 = pos.offset(facing)
            resultItemStack.drop(world, Vec3d(pos2).addVector(0.5, 0.5, 0.5), noPickupDelay = true)

            return EnumActionResult.SUCCESS
        } else {

            // ガチャリスト取得
            val dropTable = FairyCrystalDropper.getDropTable(player, world, pos, hand, facing, hitX, hitY, hitZ, variant.dropRank, commonBoost, rareBoost)


            // 表示

            fun send(textComponent: ITextComponent) = player.sendStatusMessage(textComponent, false)

            send(textComponent { !"===== " + !crystalItemStack.displayName + !" (${if (world.isRemote) "Client" else "Server"}) =====" })

            val totalWeight = dropTable.totalWeight
            dropTable.sortedBy { it.item.displayName }.sortedBy { it.weight }.forEach { weightedItem ->
                send(textComponent { !"${weightedItem.weight / totalWeight * 100.0 formatAs "%f%%"}: " + !weightedItem.item.displayName })
            }

            send(textComponent { !"====================" })


            return EnumActionResult.SUCCESS
        }
    }

    override fun getItemStackDisplayName(itemStack: ItemStack): String {
        val variant = getVariant(itemStack) ?: return translateToLocal("$unlocalizedName.name")
        return translateToLocalFormatted("item.${variant.unlocalizedName}.name")
    }

    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val variant = getVariant(itemStack) ?: return
        val mastery = EnumMastery.fairySummoning
        val skillContainer = ApiSkill.skillManager.clientSkillContainer
        tooltip += formattedText { (!"スキル: " + !mastery.displayName + !" (${skillContainer.getSkillLevel(mastery)})").gold } // TODO translate
        tooltip += formattedText { (!"ランク: ${variant.dropRank + 1}").blue } // TODO translate
        tooltip += formattedText { (!"コモン判定ブースト: ${variant.getRateBoost(DropCategory.COMMON, skillContainer) * 100.0 formatAs "%.2f%%"}").blue } // TODO translate
        tooltip += formattedText { (!"レア判定ブースト: ${variant.getRateBoost(DropCategory.RARE, skillContainer) * 100.0 formatAs "%.2f%%"}").blue } // TODO translate
    }
}

abstract class VariantFairyCrystal(val unlocalizedName: String) : ItemVariant() {
    open val dropRank get() = 0
    open fun getRateBoost(dropCategory: DropCategory, skillContainer: ISkillContainer) = 1.0
}

class VariantFairyCrystalNormal(
    unlocalizedName: String,
    override val dropRank: Int,
    private val dropCategory: DropCategory,
    private val rateBoost: Double
) : VariantFairyCrystal(unlocalizedName) {
    override fun getRateBoost(dropCategory: DropCategory, skillContainer: ISkillContainer) = when (dropCategory) {
        dropCategory -> rateBoost * (1.0 + skillContainer.getSkillLevel(EnumMastery.fairySummoning) * 0.01)
        else -> super.getRateBoost(dropCategory, skillContainer)
    }
}
