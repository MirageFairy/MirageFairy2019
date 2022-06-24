package miragefairy2019.mod

import miragefairy2019.jei.jeiModule
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.mod.artifacts.artifactsModule
import miragefairy2019.mod.fairy.Fairy
import miragefairy2019.mod.fairybox.FairyBox
import miragefairy2019.mod.fairyrelation.fairyRelationModule
import miragefairy2019.mod.fairyweapon.FairyWeapon
import miragefairy2019.mod.material.materialModule
import miragefairy2019.mod.placeditem.PlacedItem
import miragefairy2019.mod.playeraura.playerAuraModule
import miragefairy2019.mod.recipes.recipesModule
import miragefairy2019.mod.skill.skillModule
import miragefairy2019.mod.systems.systemsModule

val modules = module {

    Main.mainModule(this)
    jeiModule(this)

    artifactsModule(this)
    materialModule(this)
    systemsModule(this)
    recipesModule(this)

    playerAuraModule(this)
    PlacedItem.placedItemMdule(this)
    skillModule(this)
    Fairy.fairyModule(this)
    FairyWeapon.fairyWeaponModule(this)
    fairyRelationModule(this)
    FairyBox.fairyBoxModule(this)

}
