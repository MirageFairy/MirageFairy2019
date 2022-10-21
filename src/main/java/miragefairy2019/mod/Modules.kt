package miragefairy2019.mod

import miragefairy2019.jei.jeiModule
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.artifacts.artifactsModule
import miragefairy2019.mod.aura.auraModule
import miragefairy2019.mod.beanstalk.beanstalkModule
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

    auraModule()
    playerAuraModule()
    placedItemModule(this)
    skillModule()
    fairyModule()
    fairyWeaponModule()
    fairyRelationModule()
    fairyBoxModule()
    magicPlantModule()
    beanstalkModule()

    // TODO move
    onMakeLang {

        enJa("miragefairy2019.gui.duration.days", "days", "日")
        enJa("miragefairy2019.gui.duration.hours", "hours", "時間")
        enJa("miragefairy2019.gui.duration.minutes", "minutes", "分")
        enJa("miragefairy2019.gui.duration.seconds", "seconds", "秒")
        enJa("miragefairy2019.gui.duration.milliSeconds", "milli seconds", "ミリ秒")

    }

}
