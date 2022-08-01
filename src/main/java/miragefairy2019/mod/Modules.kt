package miragefairy2019.mod

import miragefairy2019.export.IngredientFactoryOreIngredientComplex
import miragefairy2019.jei.jeiModule
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.artifacts.artifactsModule
import miragefairy2019.mod.fairy.fairyModule
import miragefairy2019.mod.fairybox.fairyBoxModule
import miragefairy2019.mod.fairyrelation.fairyRelationModule
import miragefairy2019.mod.fairyweapon.fairyWeaponModule
import miragefairy2019.mod.magicplant.magicPlantModule
import miragefairy2019.mod.material.materialModule
import miragefairy2019.mod.pedestal.pedestalModule
import miragefairy2019.mod.placeditem.placedItemModule
import miragefairy2019.mod.playeraura.playerAuraModule
import miragefairy2019.mod.recipes.recipesModule
import miragefairy2019.mod.skill.skillModule
import miragefairy2019.mod.systems.systemsModule

val modules = module {

    Main.mainModule(this)
    jeiModule()

    artifactsModule()
    pedestalModule()
    materialModule()
    systemsModule()
    recipesModule()

    playerAuraModule()
    placedItemModule(this)
    skillModule()
    fairyModule()
    fairyWeaponModule()
    fairyRelationModule()
    fairyBoxModule()
    magicPlantModule()

    // TODO move
    onMakeIngredientFactory {
        this["ore_dict_complex"] = IngredientFactoryOreIngredientComplex::class.java
    }

    // TODO move
    onMakeLang {

        enJa("itemGroup.mirageFairy2019", "MirageFairy2019", "MirageFairy2019")

        enJa("mirageFairy2019.formula.source.cost.name", "Cost", "コスト")

        enJa("miragefairy2019.gui.duration.days", "days", "日")
        enJa("miragefairy2019.gui.duration.hours", "hours", "時間")
        enJa("miragefairy2019.gui.duration.minutes", "minutes", "分")
        enJa("miragefairy2019.gui.duration.seconds", "seconds", "秒")
        enJa("miragefairy2019.gui.duration.milliSeconds", "milli seconds", "ミリ秒")
        enJa("miragefairy2019.gui.playerAura.title", "Player Aura", "プレイヤーオーラ")
        enJa("miragefairy2019.gui.playerAura.poem.step1", "It's an appetizing scent.", "食欲をそそられる香りだ")
        enJa("miragefairy2019.gui.playerAura.poem.step2", "It's a familiar taste.", "食べ慣れた味だ")
        enJa("miragefairy2019.gui.playerAura.poem.step3", "I'm getting tired of this taste...", "この味にも飽きてきたな…")
        enJa("miragefairy2019.gui.playerAura.poem.step4", "The nutrition is biased...", "栄養が偏り気味だ…")
        enJa("miragefairy2019.gui.playerAura.poem.step5", "I want to eat something else...", "そろそろ他のものが食べたい…")

    }

}
