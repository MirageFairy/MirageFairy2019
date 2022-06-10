package miragefairy2019.mod.artifacts

import miragefairy2019.libkt.darkGray
import miragefairy2019.libkt.darkPurple
import miragefairy2019.libkt.displayText
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.green
import miragefairy2019.libkt.item
import miragefairy2019.libkt.module
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.Main
import miragefairy2019.mod.fairyweapon.MagicSelector
import miragefairy2019.mod.fairyweapon.rayTraceBlock
import miragefairy2019.mod.skill.ApiSkill
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

object AstronomicalObservationBook {
    lateinit var itemAstronomicalObservationBook: () -> ItemAstronomicalObservationBook
    val module = module {
        itemAstronomicalObservationBook = item({ ItemAstronomicalObservationBook() }, "astronomical_observation_book") {
            setUnlocalizedName("astronomicalObservationBook")
            setCreativeTab { Main.creativeTab }
            setCustomModelResourceLocation()
        }
    }
}

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
            skillContainer.variables.exp = (skillContainer.variables.exp + exp).coerceAtLeast(0)
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
    override fun getMaxItemUseDuration(itemStack: ItemStack) = 100
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {

        // 現在地平線より上にある天体を見なければならない
        if (player.lookVec.y < 0) {
            player.sendStatusMessage(textComponent { "空がよく見えない"().darkPurple }, true) // TODO translate
            return ActionResult(EnumActionResult.FAIL, player.getHeldItem(hand))
        }

        // 天井が塞がれている場合は失敗
        val selector = MagicSelector.rayTraceBlock(world, player, 64.0)
        if (selector.item.isHit) {
            player.sendStatusMessage(textComponent { "空がよく見えない"().darkPurple }, true) // TODO translate
            return ActionResult(EnumActionResult.FAIL, player.getHeldItem(hand))
        }

        // 太陽か月を見なければならない
        val vectorLook = player.lookVec
        val angle = player.world.getCelestialAngleRadians(0.0f)
        val vectorSun = Vec3d(-sin(angle).toDouble(), cos(angle).toDouble(), 0.0)
        val vectorMoon = Vec3d(-sin(angle + PI), cos(angle + PI), 0.0)
        val angleSunDiff = abs(acos(vectorLook.dotProduct(vectorSun))) * 180 / PI
        val angleMoonDiff = abs(acos(vectorLook.dotProduct(vectorMoon))) * 180 / PI
        if (angleSunDiff >= 15 && angleMoonDiff >= 15) {
            player.sendStatusMessage(textComponent { "観測可能な天体が見当たらない"().darkPurple }, true) // TODO translate
            return ActionResult(EnumActionResult.FAIL, player.getHeldItem(hand))
        }

        player.activeHand = hand
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }

    override fun onUsingTick(itemStack: ItemStack, player: EntityLivingBase, count: Int) {
        if (!player.world.isRemote) {
            if (player is EntityPlayer) {
                when (count) {
                    100 -> player.sendStatusMessage(textComponent { ">>> 5 <<<"().darkPurple }, true)
                    80 -> player.sendStatusMessage(textComponent { ">>> 4 <<<"().darkPurple }, true)
                    60 -> player.sendStatusMessage(textComponent { ">>> 3 <<<"().darkPurple }, true)
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

                val canDaily = getDailyStatus(oldLast, now) == EnumQuestStatus.INCOMPLETE
                val canWeekly = getWeeklyStatus(oldLast, now) == EnumQuestStatus.INCOMPLETE
                val canMonthly = getMonthlyStatus(oldLast, now) == EnumQuestStatus.INCOMPLETE

                // 報酬獲得
                if (canDaily) gainExp(player, 15)
                if (canWeekly) gainExp(player, 75)
                if (canMonthly) gainExp(player, 250)

                skillContainer.send(player) // 同期

                player.cooldownTracker.setCooldown(itemStack.item, 40) // クールタイム

                // 完了メッセージ
                player.sendStatusMessage(textComponent { translate("$prefix.message.completed").darkPurple }, true)
                if (!canDaily && !canWeekly && !canMonthly) player.sendStatusMessage(textComponent { translate("$prefix.message.doNothing") }, false)

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
        val last = ApiSkill.skillManager.clientSkillContainer.variables.lastAstronomicalObservationTime
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
