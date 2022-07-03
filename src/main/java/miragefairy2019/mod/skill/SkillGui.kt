package miragefairy2019.mod.skill

import miragefairy2019.lib.gui.Alignment
import miragefairy2019.lib.gui.ContainerComponent
import miragefairy2019.lib.gui.GuiComponent
import miragefairy2019.lib.gui.button
import miragefairy2019.lib.gui.label
import miragefairy2019.lib.gui.rectangle
import miragefairy2019.lib.gui.tooltip
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.proxy
import miragefairy2019.lib.skillContainer
import miragefairy2019.libkt.GuiHandlerContext
import miragefairy2019.libkt.ISimpleGuiHandler
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.displayText
import miragefairy2019.libkt.guiHandler
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.toArgb
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.Main
import mirrg.kotlin.hydrogen.formatAs
import mirrg.kotlin.minus
import mirrg.kotlin.startOfMonth
import mirrg.kotlin.toInstantAsUtc
import mirrg.kotlin.utcLocalDateTime
import net.minecraft.client.gui.GuiYesNo
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.time.Instant

val skillGuiModule = module {
    onInit {
        Main.registerGuiHandler(GuiId.skillGui, object : ISimpleGuiHandler {
            override fun GuiHandlerContext.onServer() = ContainerSkill(player).also { it.init() }
            override fun GuiHandlerContext.onClient() = GuiSkill(ContainerSkill(player).also { it.init() })
        }.guiHandler)
    }
}

class ContainerSkill(val player: EntityPlayer) : ContainerComponent() {
    override fun canInteractWith(playerIn: EntityPlayer) = true
    private val skillContainer get() = player.proxy.skillContainer

    init {
        val xSize = 176

        rectangle(RectangleInt(4 + 0, 4, 20, 10)) {
            label(Alignment.LEFT, color = 0xFF808080.toArgb()) { textComponent { "FMLv:"() } }
        }
        rectangle(RectangleInt(4 + 20, 4, 20 - 4, 10)) {
            label(Alignment.RIGHT) { textComponent { "${ApiSkill.skillManager.getFairyMasterLevel(skillContainer.variables.getExp())}"() } }
        }
        rectangle(RectangleInt(4 + 20, 4, 20 - 4, 10)) {
            tooltip {
                listOf(
                    "フェアリーマスターレベル: ${ApiSkill.skillManager.getFairyMasterLevel(skillContainer.variables.getExp())}", // TODO translate
                    "累積経験値: ${skillContainer.variables.getExp() formatAs "%8d"}", // TODO translate
                    "必要経験値: ${ApiSkill.skillManager.getRequiredFairyMasterExpForNextLevel(skillContainer.variables.getExp()) formatAs "%8d"}" // TODO translate
                )
            }
        }
        rectangle(RectangleInt(4 + 40, 4, 20, 10)) {
            label(Alignment.LEFT, color = 0xFF808080.toArgb()) { textComponent { "SP:"() } }
        }
        rectangle(RectangleInt(4 + 60, 4, 20 - 4, 10)) {
            label(Alignment.RIGHT) { textComponent { "${skillContainer.remainingSkillPoints}"() } }
        }
        rectangle(RectangleInt(4 + 60, 4, 20 - 4, 10)) {
            tooltip { listOf("スキルポイントはフェアリーマスターレベル上昇時に入手します。") } // TODO translate
        }
        rectangle(RectangleInt(xSize - 34, 4, 30, 10)) {
            button { gui, _, _ ->
                if (skillContainer.canResetMastery(Instant.now()) && skillContainer.usedSkillPoints > 0) {
                    gui.mc.displayGuiScreen(GuiYesNo({ result, _ ->
                        gui.mc.displayGuiScreen(gui)
                        if (result) Main.simpleNetworkWrapper.sendToServer(MessageResetMastery())
                    }, "マスタリレベル初期化", "すべてのマスタリのレベルをリセットし、スキルポイントに戻しますか？\nこの操作は毎月1度だけ実行できます。", 0)) // TODO translate
                }
            }
        }
        rectangle(RectangleInt(xSize - 34, 4, 30, 10)) {
            // TODO クリックできないときは灰色にする
            label(Alignment.CENTER, color = 0xFF0000FF.toArgb()) { textComponent { "初期化"() } } // TODO translate
        }
        rectangle(RectangleInt(xSize - 34, 4, 30, 10)) {
            tooltip {
                when {
                    !skillContainer.canResetMastery(Instant.now()) -> listOf(
                        "今月は初期化できません。",
                        "残り: ${(skillContainer.variables.getLastMasteryResetTime()!!.utcLocalDateTime.toLocalDate().startOfMonth.plusMonths(1).toInstantAsUtc - Instant.now()).displayText.unformattedText}"
                    ) // TODO translate
                    skillContainer.usedSkillPoints == 0 -> listOf("初期化の必要はありません。") // TODO translate
                    else -> listOf("1か月に1度だけ、全マスタリのレベルをリセットしSPに戻せます。") // TODO translate
                }
            }
        }

        rectangle(RectangleInt(4, 14, xSize - 4 - 4 - 10 - 20 - 20, 10)) {
            label(Alignment.LEFT, color = 0xFF808080.toArgb()) { textComponent { "マスタリ名"() } }// TODO translate
        }
        rectangle(RectangleInt(xSize - 54, 14, 20, 10)) {
            label(Alignment.RIGHT, color = 0xFF808080.toArgb()) { textComponent { "MLv"() } } // TODO translate
        }
        rectangle(RectangleInt(xSize - 54, 14, 20, 10)) {
            tooltip { listOf("マスタリレベルはある領域に関する理解の深さです。") } // TODO translate
        }
        rectangle(RectangleInt(xSize - 34, 14, 20, 10)) {
            label(Alignment.RIGHT, color = 0xFF808080.toArgb()) { textComponent { "SLv"() } } // TODO translate
        }
        rectangle(RectangleInt(xSize - 34, 14, 20, 10)) {
            tooltip { listOf("スキルレベルは個々のアクションの強さです。") } // TODO translate
        }

        Mastery.values().forEachIndexed { i, mastery ->
            rectangle(RectangleInt(4 + 8 * mastery.layer, 24 + 10 * i, xSize - 4 - 4 - 10 - 20 - 20 - 8 * mastery.layer, 10)) {
                label(Alignment.LEFT) { mastery.displayName }
            }
            rectangle(RectangleInt(4 + 8 * mastery.layer, 24 + 10 * i, xSize - 4 - 4 - 10 - 20 - 20 - 8 * mastery.layer, 10)) {
                mastery.displayPoem.unformattedText.takeIf { it.isNotBlank() }?.let { tooltip { listOf(it) } }
            }
            // TODO SP→SLv効率表示
            rectangle(RectangleInt(xSize - 54, 24 + 10 * i, 20, 10)) {
                label(Alignment.RIGHT) { textComponent { "${skillContainer.getMasteryLevel(mastery.name)}"() } }
            }
            rectangle(RectangleInt(xSize - 34, 24 + 10 * i, 20, 10)) {
                label(Alignment.RIGHT) { textComponent { "${skillContainer.getSkillLevel(mastery)}"() } }
            }
            rectangle(RectangleInt(xSize - 14, 24 + 10 * i, 10, 10)) {
                button { _, _, _ ->
                    if (skillContainer.remainingSkillPoints > 0) {
                        Main.simpleNetworkWrapper.sendToServer(MessageTrainMastery(mastery.name))
                    }
                }
            }
            rectangle(RectangleInt(xSize - 14, 24 + 10 * i, 10, 10)) {
                // TODO ホバーで影響するマスタリのレベルを緑色に光らせつつ実行後の値を表示
                label(Alignment.CENTER, color = 0xFF0000FF.toArgb()) { textComponent { "*"() } } // TODO icon
            }
            rectangle(RectangleInt(xSize - 14, 24 + 10 * i, 10, 10)) {
                tooltip { listOf(if (skillContainer.remainingSkillPoints > 0) "このマスタリにスキルポイントを割り振ります。" else "スキルポイントが足りません。") } // TODO translate
            }
        }

    }
}

@SideOnly(Side.CLIENT)
class GuiSkill(container: ContainerComponent) : GuiComponent(container)
