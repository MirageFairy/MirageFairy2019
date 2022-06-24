package miragefairy2019.mod.systems

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.skill.Mastery

val masteryModule = module {
    onMakeLang {
        Mastery.values().forEach {
            fun f(name: String, englishName: String, japaneseName: String, japanesePoem: String) {
                enJa("mirageFairy2019.mastery.$name.name", englishName, japaneseName)
                enJa("mirageFairy2019.mastery.$name.poem", "", japanesePoem)
            }
            when (it) {
                Mastery.root -> f("root", "Fairy M.", "フェアリーマスタリ", "妖精と仲良くなるための知識です。")
                Mastery.combat -> f("combat", "Combat M.", "戦闘マスタリ", "マナの流れを敵にぶつけるための技術です。")
                Mastery.closeCombat -> f("closeCombat", "Attack M.", "アタックマスタリ", "妖精の力を借りた武器攻撃のための技術です。")
                Mastery.rangedCombat -> f("rangedCombat", "Shooting M.", "シューティングマスタリ", "発射体を制御するための技術です。")
                Mastery.magicCombat -> f("magicCombat", "Magic M.", "マジックマスタリ", "エルグを直接作用させてダメージを与える技術です。")
                Mastery.production -> f("production", "Production M.", "生産マスタリ", "マナの流れを物体にぶつけるための技術です。")
                Mastery.harvest -> f("harvest", "Harvest M.", "収穫マスタリ", "資源を利用可能にするための技術です。")
                Mastery.mining -> f("mining", "Mining M.", "採掘マスタリ", "岩石の境界を見極める能力です。")
                Mastery.lumbering -> f("lumbering", "Lumbering M.", "伐採マスタリ", "木の痛みを知る能力です。")
                Mastery.flowerPicking -> f("flowerPicking", "Flower Picking M.", "花摘みマスタリ", "お花と交渉し、余ったものを譲ってもらう能力です。")
                Mastery.processing -> f("processing", "Processing M.", "加工マスタリ", "資源の質を向上させるための技術です。")
                Mastery.fairySummoning -> f("fairySummoning", "Fairy Summoning M.", "妖精召喚マスタリ", "妖精の好きなものが分かる能力です。")
                Mastery.fabrication -> f("fabrication", "Fabrication M.", "製作マスタリ", "実用的な物を作るための技術です。")
                Mastery.brewing -> f("brewing", "Brewing M.", "醸造マスタリ", "薬品の安全な使い方や製法の知識です。")
            }
        }
    }
}
