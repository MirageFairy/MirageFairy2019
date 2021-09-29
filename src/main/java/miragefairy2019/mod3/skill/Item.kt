package miragefairy2019.mod3.skill

import miragefairy2019.libkt.atDayStart
import miragefairy2019.libkt.atMonthStart
import miragefairy2019.libkt.atWeekStart
import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.darkGray
import miragefairy2019.libkt.darkPurple
import miragefairy2019.libkt.displayText
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.green
import miragefairy2019.libkt.minus
import miragefairy2019.libkt.red
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.toInstant
import miragefairy2019.libkt.toLocalDateTime
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.common.magic.MagicSelectorRayTrace
import miragefairy2019.mod3.skill.api.ApiSkill
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
import java.util.function.Supplier
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

lateinit var itemSkillBook: Supplier<ItemSkillBook>
lateinit var itemAstronomicalObservationBook: Supplier<ItemAstronomicalObservationBook>

class ItemSkillBook : Item() {
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        if (!world.isRemote) player.openGui(ModMirageFairy2019.instance, guiIdSkill, player.world, player.position.x, player.position.y, player.position.z)
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        // TODO 現在スキルレベルとか表示
    }
}

private enum class EnumQuestStatus { IMPOSSIBLE, INCOMPLETE, COMPLETED }

private val EnumQuestStatus.displayText
    get() = when (this) {
        EnumQuestStatus.IMPOSSIBLE -> buildText { translate("miragefairy2019.gui.astronomicalObservation.impossible").darkGray }
        EnumQuestStatus.INCOMPLETE -> buildText { translate("miragefairy2019.gui.astronomicalObservation.incomplete").red }
        EnumQuestStatus.COMPLETED -> buildText { translate("miragefairy2019.gui.astronomicalObservation.completed").green }
    }

class ItemAstronomicalObservationBook : Item() {
    companion object {
        private const val prefix = "miragefairy2019.gui.astronomicalObservation"
    }

    override fun getItemUseAction(stack: ItemStack) = EnumAction.BOW
    override fun getMaxItemUseDuration(itemStack: ItemStack) = 100
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {

        // 現在地平線より上にある天体を見なければならない
        if (player.lookVec.y < 0) {
            player.sendStatusMessage(textComponent { (!"空がよく見えない").darkPurple }, true) // TODO translate
            return ActionResult(EnumActionResult.FAIL, player.getHeldItem(hand))
        }

        // 天井が塞がれている場合は失敗
        val selector = MagicSelectorRayTrace(world, player, 64.0)
        if (selector.isHit) {
            player.sendStatusMessage(textComponent { (!"空がよく見えない").darkPurple }, true) // TODO translate
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
            player.sendStatusMessage(textComponent { (!"観測可能な天体が見当たらない").darkPurple }, true) // TODO translate
            return ActionResult(EnumActionResult.FAIL, player.getHeldItem(hand))
        }

        player.activeHand = hand
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }

    override fun onUsingTick(itemStack: ItemStack, player: EntityLivingBase, count: Int) {
        if (!player.world.isRemote) {
            if (player is EntityPlayer) {
                when (count) {
                    100 -> player.sendStatusMessage(textComponent { (!">>> 5 <<<").darkPurple }, true)
                    80 -> player.sendStatusMessage(textComponent { (!">>> 4 <<<").darkPurple }, true)
                    60 -> player.sendStatusMessage(textComponent { (!">>> 3 <<<").darkPurple }, true)
                    40 -> player.sendStatusMessage(textComponent { (!">>> 2 <<<").darkPurple }, true)
                    20 -> player.sendStatusMessage(textComponent { (!">>> 1 <<<").darkPurple }, true)
                }
            }
        }
        super.onUsingTick(itemStack, player, count)
    }

    /**
     * 同期を行いません。
     */
    private fun gainExp(player: EntityPlayer, exp: Int) {
        val skillContainer = ApiSkill.skillManager.getServerSkillContainer(player)

        // 獲得処理
        val lvOld = skillContainer.skillManager.getFairyMasterLevel(skillContainer.variables.exp)
        skillContainer.variables.exp += exp
        val lv = skillContainer.skillManager.getFairyMasterLevel(skillContainer.variables.exp)
        val lvDiff = lv - lvOld

        // エフェクト
        player.sendStatusMessage(textComponent { translate("$prefix.message.gainExp", exp) }, false)
        player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.75f, 1.2f)
        if (lvDiff > 0) {
            player.sendStatusMessage(textComponent { translate("$prefix.message.gainLevel", lvDiff) }, false)
            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.75f, 1.2f)
        }

    }

    override fun onItemUseFinish(itemStack: ItemStack, world: World, player: EntityLivingBase): ItemStack {
        if (!world.isRemote) {
            if (player is EntityPlayer) {
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

                skillContainer.send(player as EntityPlayerMP) // 同期

                player.cooldownTracker.setCooldown(itemStack.item, 40) // クールタイム

                // 完了メッセージ
                player.sendStatusMessage(textComponent { translate("$prefix.message.completed").darkPurple }, true)
                if (!canDaily && !canWeekly && !canMonthly) player.sendStatusMessage(textComponent { translate("$prefix.message.doNothing") }, false)

            }
        }
        return super.onItemUseFinish(itemStack, world, player)
    }


    private fun getLastLimitDaily(now: Instant): Instant = now.toLocalDateTime().toLocalDate().atDayStart().toInstant()
    private fun getLastLimitWeekly(now: Instant): Instant = now.toLocalDateTime().toLocalDate().atWeekStart().toInstant()
    private fun getLastLimitMonthly(now: Instant): Instant = now.toLocalDateTime().toLocalDate().atMonthStart().toInstant()
    private fun getLimitDaily(now: Instant): Instant = now.toLocalDateTime().toLocalDate().atDayStart().plusDays(1).toInstant()
    private fun getLimitWeekly(now: Instant): Instant = now.toLocalDateTime().toLocalDate().atWeekStart().plusDays(7).toInstant()
    private fun getLimitMonthly(now: Instant): Instant = now.toLocalDateTime().toLocalDate().atMonthStart().plusMonths(1).toInstant()

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
        tooltip += textComponent { translate("$prefix.title") + !":" }.formattedText
        val last = ApiSkill.skillManager.clientSkillContainer.variables.lastAstronomicalObservationTime
        val now: Instant = Instant.now()
        tooltip += formattedText { !"  " + translate("$prefix.daily") + !": " + !getDailyStatus(last, now).displayText + !" (" + translate("$prefix.message.remaining", (getLimitDaily(now) - now).displayText) + !")" }
        tooltip += formattedText { !"  " + translate("$prefix.weekly") + !": " + !getWeeklyStatus(last, now).displayText + !" (" + translate("$prefix.message.remaining", (getLimitWeekly(now) - now).displayText) + !")" }
        tooltip += formattedText { !"  " + translate("$prefix.monthly") + !": " + !getMonthlyStatus(last, now).displayText + !" (" + translate("$prefix.message.remaining", (getLimitMonthly(now) - now).displayText) + !")" }
        tooltip += formattedText()
        tooltip += formattedText { translate("$prefix.usage").red }
    }
}
