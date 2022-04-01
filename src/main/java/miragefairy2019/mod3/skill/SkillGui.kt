package miragefairy2019.mod3.skill

import miragefairy2019.libkt.Component
import miragefairy2019.libkt.GuiHandlerContext
import miragefairy2019.libkt.ISimpleGuiHandler
import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.TextAlignment
import miragefairy2019.libkt.argb
import miragefairy2019.libkt.button
import miragefairy2019.libkt.component
import miragefairy2019.libkt.displayText
import miragefairy2019.libkt.drawGuiBackground
import miragefairy2019.libkt.guiHandler
import miragefairy2019.libkt.label
import miragefairy2019.libkt.minus
import miragefairy2019.libkt.module
import miragefairy2019.libkt.position
import miragefairy2019.libkt.rectangle
import miragefairy2019.libkt.toArgb
import miragefairy2019.libkt.tooltip
import miragefairy2019.mod3.main.Main
import miragefairy2019.mod3.skill.api.ApiSkill
import mirrg.kotlin.formatAs
import mirrg.kotlin.minus
import mirrg.kotlin.startOfMonth
import mirrg.kotlin.toInstantAsUtc
import mirrg.kotlin.utcLocalDateTime
import net.minecraft.client.gui.GuiYesNo
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import java.time.Instant

object SkillGui {
    const val guiIdSkillGui = 2
    val module = module {
        onInit {
            Main.registerGuiHandler(guiIdSkillGui, object : ISimpleGuiHandler {
                override fun GuiHandlerContext.onServer() = ContainerSkill()
                override fun GuiHandlerContext.onClient() = GuiSkill()
            }.guiHandler)
        }
    }
}

class ContainerSkill : Container() {
    override fun canInteractWith(playerIn: EntityPlayer) = true
}

class GuiSkill : GuiContainer(ContainerSkill()) {

    private val components = mutableListOf<Component>()

    private val skillManager get() = ApiSkill.skillManager
    private val skillContainer get() = skillManager.clientSkillContainer

    init {

        components += component(RectangleInt(4 + 0, 4, 20, 10)) {
            label(::fontRenderer, color = 0xFF808080.toArgb()) { "FMLv:" }
        }
        components += component(RectangleInt(4 + 20, 4, 20 - 4, 10)) {
            label(::fontRenderer, align = TextAlignment.RIGHT) { "${skillManager.getFairyMasterLevel(skillContainer.variables.exp)}" }
            tooltip(
                "フェアリーマスターレベル: ${skillManager.getFairyMasterLevel(skillContainer.variables.exp)}", // TODO translate
                "累積経験値: ${skillContainer.variables.exp formatAs "%8d"}", // TODO translate
                "必要経験値: ${skillManager.getRequiredFairyMasterExpForNextLevel(skillContainer.variables.exp) formatAs "%8d"}" // TODO translate
            )
        }
        components += component(RectangleInt(4 + 40, 4, 20, 10)) {
            label(::fontRenderer, color = 0xFF808080.toArgb()) { "SP:" }
        }
        components += component(RectangleInt(4 + 60, 4, 20 - 4, 10)) {
            label(::fontRenderer, align = TextAlignment.RIGHT) { "${skillContainer.remainingSkillPoints}" }
            tooltip("スキルポイントはフェアリーマスターレベル上昇時に入手します。") // TODO translate
        }
        components += component(RectangleInt(xSize - 34, 4, 30, 10)) {
            button {
                if (skillContainer.canResetMastery(Instant.now()) && skillContainer.usedSkillPoints > 0) {
                    mc.displayGuiScreen(GuiYesNo({ result, _ ->
                        mc.displayGuiScreen(this@GuiSkill)
                        if (result) Main.simpleNetworkWrapper.sendToServer(MessageResetMastery())
                    }, "マスタリレベル初期化", "すべてのマスタリのレベルをリセットし、スキルポイントに戻しますか？\nこの操作は毎月1度だけ実行できます。", 0)) // TODO translate
                }
            }
            // TODO クリックできないときは灰色にする
            label(::fontRenderer, color = 0xFF0000FF.toArgb(), align = TextAlignment.CENTER) { "初期化" } // TODO translate
            tooltip {
                when {
                    !skillContainer.canResetMastery(Instant.now()) -> listOf(
                        "今月は初期化できません。",
                        "残り: ${(skillContainer.variables.lastMasteryResetTime!!.utcLocalDateTime.toLocalDate().startOfMonth.plusMonths(1).toInstantAsUtc - Instant.now()).displayText.unformattedText}"
                    ) // TODO translate
                    skillContainer.usedSkillPoints == 0 -> listOf("初期化の必要はありません。") // TODO translate
                    else -> listOf("1か月に1度だけ、全マスタリのレベルをリセットしSPに戻せます。") // TODO translate
                }
            }
        }

        components += component(RectangleInt(4, 14, xSize - 4 - 4 - 10 - 20 - 20, 10)) {
            label(::fontRenderer, color = 0xFF808080.toArgb()) { "マスタリ名" }// TODO translate
        }
        components += component(RectangleInt(xSize - 54, 14, 20, 10)) {
            label(::fontRenderer, color = argb(0xFF808080.toInt()), align = TextAlignment.RIGHT) { "MLv" } // TODO translate
            tooltip("マスタリレベルはある領域に関する理解の深さです。") // TODO translate
        }
        components += component(RectangleInt(xSize - 34, 14, 20, 10)) {
            label(::fontRenderer, color = argb(0xFF808080.toInt()), align = TextAlignment.RIGHT) { "SLv" } // TODO translate
            tooltip("スキルレベルは個々のアクションの強さです。") // TODO translate
        }

        EnumMastery.values().forEachIndexed { i, mastery ->
            components += component(RectangleInt(4 + 8 * mastery.layer, 24 + 10 * i, xSize - 4 - 4 - 10 - 20 - 20 - 8 * mastery.layer, 10)) {
                label(::fontRenderer, color = 0xFF000000.toArgb()) { mastery.displayName.unformattedText }
                mastery.displayPoem.unformattedText.takeIf { it.isNotBlank() }?.let { tooltip(it) }
            }
            // TODO SP→SLv効率表示
            components += component(RectangleInt(xSize - 54, 24 + 10 * i, 20, 10)) {
                label(::fontRenderer, color = 0xFF000000.toArgb(), align = TextAlignment.RIGHT) { "${skillContainer.getMasteryLevel(mastery.name)}" }
            }
            components += component(RectangleInt(xSize - 34, 24 + 10 * i, 20, 10)) {
                label(::fontRenderer, color = 0xFF000000.toArgb(), align = TextAlignment.RIGHT) { "${skillContainer.getSkillLevel(mastery)}" }
            }
            components += component(RectangleInt(xSize - 14, 24 + 10 * i, 10, 10)) {
                button {
                    if (skillContainer.remainingSkillPoints > 0) {
                        Main.simpleNetworkWrapper.sendToServer(MessageTrainMastery(mastery.name))
                    }
                }
                // TODO ホバーで影響するマスタリのレベルを緑色に光らせつつ実行後の値を表示
                label(::fontRenderer, color = 0xFF0000FF.toArgb(), align = TextAlignment.CENTER) { "*" } // TODO icon
                tooltip { listOf(if (skillContainer.remainingSkillPoints > 0) "このマスタリにスキルポイントを割り振ります。" else "スキルポイントが足りません。") } // TODO translate
            }
        }

    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        components.forEach { it.onScreenDraw.fire { it(PointInt(mouseX, mouseY) - position, partialTicks) } }
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        rectangle.drawGuiBackground()
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        components.forEach { it.onForegroundDraw.fire { it(PointInt(mouseX, mouseY) - position) } }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        components.forEach { it.onMouseClicked.fire { it(PointInt(mouseX, mouseY) - position, mouseButton) } }
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }
}
