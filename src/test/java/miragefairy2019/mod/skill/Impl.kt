package miragefairy2019.mod.skill

import mirrg.kotlin.gson.hydrogen.toJson
import mirrg.kotlin.gson.jsonWrapper2
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class TestSkill {
    @Test
    fun io() {

        assertEquals("""{"masteryLevels":{},"variables":{"exp":0}}""", SkillModel().toJsonElement().toJson())

        val json = """
        {
          "masteryLevels": {
            "flowerPicking": 10,
            "production": 4,
            "combat": 4,
            "mining": 6
          },
          "variables": {
            "exp": 1595,
            "lastMasteryResetTime": 1644817686123,
            "lastAstronomicalObservationTime": 1648530170686
          }
        }
        """.trimIndent()
        val skillModel = json.jsonWrapper2.toSkillModel()
        assertEquals(4, skillModel.masteryLevels.size)
        assertEquals(10, skillModel.masteryLevels["flowerPicking"])
        assertEquals(1595, skillModel.variables.exp)
        assertEquals(1644817686123L, skillModel.variables.lastMasteryResetTime?.toEpochMilli())
        assertEquals(1648530170686L, skillModel.variables.lastAstronomicalObservationTime?.toEpochMilli())
        assertEquals(json, skillModel.toJsonElement().toJson { setPrettyPrinting() })

    }

    @Test
    fun test() {
        val skillManager = object : SkillManager() {
            override fun getFile(player: EntityPlayer): File = throw UnsupportedOperationException()
            override fun send(player: EntityPlayerMP, json: String): Unit = throw UnsupportedOperationException()
        }

        // 必要経験値
        assertEquals(0, skillManager.getFairyMasterExp(0))
        assertEquals(5, skillManager.getFairyMasterExp(1))
        assertEquals(5 + 10, skillManager.getFairyMasterExp(2))
        assertEquals(5 + 10 + 15 + 20 + 25, skillManager.getFairyMasterExp(5))
        assertEquals(5 * 10 * (10 + 1) / 2, skillManager.getFairyMasterExp(10))

        // 現在レベル
        assertEquals(0, skillManager.getFairyMasterLevel(0))
        assertEquals(0, skillManager.getFairyMasterLevel(4))
        assertEquals(1, skillManager.getFairyMasterLevel(5))
        assertEquals(1, skillManager.getFairyMasterLevel(14))
        assertEquals(2, skillManager.getFairyMasterLevel(15))
        assertEquals(19, skillManager.getFairyMasterLevel(1049))
        assertEquals(20, skillManager.getFairyMasterLevel(1050))

        // 現在レベル計算誤差テスト
        (1..500).forEach { lv ->

            // レベルを必要経験値にしてレベルに戻すと等しい
            assertEquals(lv, skillManager.getFairyMasterLevel(skillManager.getFairyMasterExp(lv)))

            // そのレベルの必要経験値に1を足してもレベルは変わらない
            assertEquals(lv, skillManager.getFairyMasterLevel(skillManager.getFairyMasterExp(lv) + 1))

            // そのレベルの必要経験値から1を引くとレベルが1下がる
            assertEquals(lv - 1, skillManager.getFairyMasterLevel(skillManager.getFairyMasterExp(lv) - 1))

        }

    }
}
