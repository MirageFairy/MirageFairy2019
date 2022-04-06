package miragefairy2019.mod

import miragefairy2019.jei.Jei
import miragefairy2019.libkt.module
import miragefairy2019.mod.modules.fairyweapon.FairyWeapon
import miragefairy2019.mod.artifacts.Artifacts
import miragefairy2019.mod.fairy.Fairy
import miragefairy2019.mod.fairyrelation.FairyRelation
import miragefairy2019.mod.fairybox.FairyBox
import miragefairy2019.mod.placeditem.PlacedItem
import miragefairy2019.mod.playeraura.PlayerAura
import miragefairy2019.mod.recipes.Recipes
import miragefairy2019.mod.skill.Skill
import miragefairy2019.mod.systems.Systems

val modules = module {

    Main.module(this)
    Jei.module(this)

    Artifacts.module(this)
    Systems.module(this)
    Recipes.module(this)

    PlayerAura.module(this)
    PlacedItem.module(this)
    Skill.module(this)
    Fairy.module(this)
    FairyWeapon.module(this)
    FairyRelation.module(this)
    FairyBox.module(this)

}
