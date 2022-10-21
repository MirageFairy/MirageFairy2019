package miragefairy2019.mod.artifacts

import miragefairy2019.lib.BlockRayTraceWrapper
import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.rayTraceBlock
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.darkGray
import miragefairy2019.libkt.darkPurple
import miragefairy2019.libkt.displayText
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.green
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.Main
import miragefairy2019.mod.fairy.FairyCard
import miragefairy2019.mod.fairy.obtainFairy
import miragefairy2019.mod.skill.ApiSkill
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.minus
import mirrg.kotlin.startOfDay
import mirrg.kotlin.startOfMonth
import mirrg.kotlin.startOfWeek
import mirrg.kotlin.toInstantAsUtc
import mirrg.kotlin.utcLocalDateTime
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.SoundEvents
import net.minecraft.item.EnumAction
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.time.Instant
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

val astronomicalObservationBookModule = module {
    itemAstronomicalObservationBook = item({ ItemAstronomicalObservationBook() }, "astronomical_observation_book") {
        setUnlocalizedName("astronomicalObservationBook")
        setCreativeTab { Main.creativeTab }
        setCustomModelResourceLocation()
        makeItemModel { generated }
        makeRecipe {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataSimpleIngredient(item = "minecraft:writable_book"),
                    DataOreIngredient(ore = "mirageFairyCrystal"),
                    DataOreIngredient(ore = "obsidian"),
                    DataOreIngredient(ore = "gemMoonstone")
                ),
                result = DataResult(item = "miragefairy2019:astronomical_observation_book")
            )
        }
    }
    run {
        lang("item.astronomicalObservationBook.name", "Astronomical Observation Note", "天体観測ノート")
        val prefix = "miragefairy2019.gui.astronomicalObservation"
        lang("$prefix.title", "Astronomical Observation Quest", "天体観測クエスト")
        lang("$prefix.daily", "Daily", "日間")
        lang("$prefix.weekly", "Weekly", "週間")
        lang("$prefix.monthly", "Monthly", "月間")
        lang("$prefix.impossible", "Impossible", "不可能")
        lang("$prefix.incomplete", "Incomplete", "未完了")
        lang("$prefix.completed", "Completed", "完了")
        lang("$prefix.usage", "Right-click and hold toward the Sun, the Moon or Twinkle Stone.", "太陽・月・トゥインクルストーンに向かって右クリック長押ししてください。")
        lang("$prefix.message.remaining", "Updated with %s left", "残り %s で更新")
        lang("$prefix.message.completed", "Completed!", "完了！")
        lang("$prefix.message.gainExp", "You have earned %s Fairy Master XP!", "%s のフェアリーマスター経験値を獲得しました！")
        lang("$prefix.message.gainLevel", "You have earned %s Fairy Master Level!", "フェアリーマスターレベルが %s 上昇しました！")
        lang("$prefix.message.doNothing", "You got nothing...", "特筆すべき変化は見られなかった…")
    }
}

lateinit var itemAstronomicalObservationBook: () -> ItemAstronomicalObservationBook

class ItemAstronomicalObservationBook : Item() {
    companion object {
        private const val prefix = "miragefairy2019.gui.astronomicalObservation"

        /**
         * 同期を行いません。
         * サーバーワールドのみ
         */
        fun gainExp(player: EntityPlayerMP, exp: Int) {
            val skillContainer = ApiSkill.skillManager.getServerSkillContainer(player)

            // 獲得処理
            val expOld = skillContainer.variables.exp
            val lvOld = skillContainer.skillManager.getFairyMasterLevel(skillContainer.variables.exp)
            skillContainer.variables.exp = (skillContainer.variables.exp + exp) atLeast 0
            val expNew = skillContainer.variables.exp
            val lvNew = skillContainer.skillManager.getFairyMasterLevel(skillContainer.variables.exp)
            val lvDiff = lvNew - lvOld
            val expDiff = expNew - expOld

            // エフェクト
            if (expDiff != 0) {
                player.sendStatusMessage(textComponent { translate("$prefix.message.gainExp", expDiff) }, false)
                player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.75f, 1.2f)
            }
            if (lvDiff != 0) {
                player.sendStatusMessage(textComponent { translate("$prefix.message.gainLevel", lvDiff) }, false)
                player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.75f, 1.2f)
            }

        }

    }

    override fun getItemUseAction(stack: ItemStack) = EnumAction.BOW
    override fun getMaxItemUseDuration(itemStack: ItemStack) = 50
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {

        // 視線判定
        val selector = MagicSelector.rayTraceBlock(world, player, 64.0)

        // トゥインクルストーン判定
        run next@{

            if (selector.item.rayTraceWrapper !is BlockRayTraceWrapper) return@next // ブロックにヒットしなかった

            if (world.getBlockState(selector.item.rayTraceWrapper.blockPos).block !is BlockTwinkleStone) return@next // トゥインクルストーンではない

            player.activeHand = hand
            return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
        }

        // 天体判定
        run next@{

            // 現在地平線より上にある天体を見なければならない
            if (player.lookVec.y < 0) {
                player.sendStatusMessage(textComponent { "空がよく見えない"().darkPurple }, true) // TRANSLATE
                return@next
            }

            // 天体が見えない場合は失敗
            if (selector.item.rayTraceWrapper.isHit) {
                player.sendStatusMessage(textComponent { "空がよく見えない"().darkPurple }, true) // TRANSLATE
                return@next
            }

            // 太陽か月を見なければならない
            val vectorLook = player.lookVec
            val angle = player.world.getCelestialAngleRadians(0.0f)
            val vectorSun = Vec3d(-sin(angle).toDouble(), cos(angle).toDouble(), 0.0)
            val vectorMoon = Vec3d(-sin(angle + PI), cos(angle + PI), 0.0)
            val angleSunDiff = abs(acos(vectorLook.dotProduct(vectorSun))) * 180 / PI
            val angleMoonDiff = abs(acos(vectorLook.dotProduct(vectorMoon))) * 180 / PI
            if (angleSunDiff >= 15 && angleMoonDiff >= 15) {
                player.sendStatusMessage(textComponent { "観測可能な天体が見当たらない"().darkPurple }, true) // TRANSLATE
                return@next
            }

            player.activeHand = hand
            return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
        }

        return ActionResult(EnumActionResult.FAIL, player.getHeldItem(hand))
    }

    override fun onUsingTick(itemStack: ItemStack, player: EntityLivingBase, count: Int) {
        if (!player.world.isRemote) {
            if (player is EntityPlayer) {
                when (count) {
                    40 -> player.sendStatusMessage(textComponent { ">>> 2 <<<"().darkPurple }, true)
                    20 -> player.sendStatusMessage(textComponent { ">>> 1 <<<"().darkPurple }, true)
                }
            }
        }
        super.onUsingTick(itemStack, player, count)
    }

    override fun onItemUseFinish(itemStack: ItemStack, world: World, player: EntityLivingBase): ItemStack {
        if (!world.isRemote) {
            if (player is EntityPlayerMP) {
                val skillContainer = ApiSkill.skillManager.getServerSkillContainer(player)
                val oldLast = skillContainer.variables.lastAstronomicalObservationTime

                // クエスト完了済みマーク
                val now = Instant.now()
                skillContainer.variables.lastAstronomicalObservationTime = now

                // 獲得Exp量
                val exp = 0 +
                    (if (getDailyStatus(oldLast, now) == EnumQuestStatus.INCOMPLETE) 15 else 0) +
                    (if (getWeeklyStatus(oldLast, now) == EnumQuestStatus.INCOMPLETE) 75 else 0) +
                    (if (getMonthlyStatus(oldLast, now) == EnumQuestStatus.INCOMPLETE) 250 else 0)

                if (exp != 0) {
                    gainExp(player, exp) // Exp獲得
                    player.obtainFairy(FairyCard.MINA, 8 * 8 * 8 * exp) // ミーニャ獲得
                }

                skillContainer.send(player) // 同期

                player.cooldownTracker.setCooldown(itemStack.item, 40) // クールタイム

                // 完了メッセージ
                player.sendStatusMessage(textComponent { translate("$prefix.message.completed").darkPurple }, true)
                if (exp == 0) player.sendStatusMessage(textComponent { translate("$prefix.message.doNothing") }, false)

            }
        }
        return super.onItemUseFinish(itemStack, world, player)
    }


    private fun getLastLimitDaily(now: Instant): Instant = now.utcLocalDateTime.toLocalDate().startOfDay.toInstantAsUtc
    private fun getLastLimitWeekly(now: Instant): Instant = now.utcLocalDateTime.toLocalDate().startOfWeek.toInstantAsUtc
    private fun getLastLimitMonthly(now: Instant): Instant = now.utcLocalDateTime.toLocalDate().startOfMonth.toInstantAsUtc
    private fun getLimitDaily(now: Instant): Instant = now.utcLocalDateTime.toLocalDate().startOfDay.plusDays(1).toInstantAsUtc
    private fun getLimitWeekly(now: Instant): Instant = now.utcLocalDateTime.toLocalDate().startOfWeek.plusDays(7).toInstantAsUtc
    private fun getLimitMonthly(now: Instant): Instant = now.utcLocalDateTime.toLocalDate().startOfMonth.plusMonths(1).toInstantAsUtc

    private fun getDailyStatus(last: Instant?, now: Instant) = when {
        last == null -> EnumQuestStatus.INCOMPLETE
        last < getLastLimitDaily(now) -> EnumQuestStatus.INCOMPLETE
        else -> EnumQuestStatus.COMPLETED
    }

    private fun getWeeklyStatus(last: Instant?, now: Instant) = when {
        last == null -> EnumQuestStatus.IMPOSSIBLE
        last < getLastLimitWeekly(now) -> EnumQuestStatus.INCOMPLETE
        else -> EnumQuestStatus.COMPLETED
    }

    private fun getMonthlyStatus(last: Instant?, now: Instant) = when {
        last == null -> EnumQuestStatus.IMPOSSIBLE
        last < getLastLimitMonthly(now) -> EnumQuestStatus.INCOMPLETE
        else -> EnumQuestStatus.COMPLETED
    }


    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip += textComponent { translate("$prefix.title") + ":"() }.formattedText
        val last = ApiSkill.skillManager.getClientSkillContainer().variables.lastAstronomicalObservationTime
        val now: Instant = Instant.now()
        tooltip += formattedText { "  "() + translate("$prefix.daily") + ": "() + getDailyStatus(last, now).displayText() + " ("() + translate("$prefix.message.remaining", (getLimitDaily(now) - now).displayText) + ")"() }
        tooltip += formattedText { "  "() + translate("$prefix.weekly") + ": "() + getWeeklyStatus(last, now).displayText() + " ("() + translate("$prefix.message.remaining", (getLimitWeekly(now) - now).displayText) + ")"() }
        tooltip += formattedText { "  "() + translate("$prefix.monthly") + ": "() + getMonthlyStatus(last, now).displayText() + " ("() + translate("$prefix.message.remaining", (getLimitMonthly(now) - now).displayText) + ")"() }
        tooltip += formattedText { empty }
        tooltip += formattedText { translate("$prefix.usage").red }
    }
}

private enum class EnumQuestStatus { IMPOSSIBLE, INCOMPLETE, COMPLETED }

private val EnumQuestStatus.displayText
    get() = when (this) {
        EnumQuestStatus.IMPOSSIBLE -> textComponent { translate("miragefairy2019.gui.astronomicalObservation.impossible").darkGray }
        EnumQuestStatus.INCOMPLETE -> textComponent { translate("miragefairy2019.gui.astronomicalObservation.incomplete").red }
        EnumQuestStatus.COMPLETED -> textComponent { translate("miragefairy2019.gui.astronomicalObservation.completed").green }
    }
