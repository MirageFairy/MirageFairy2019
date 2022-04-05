package miragefairy2019.mod

import miragefairy2019.jei.Jei
import miragefairy2019.libkt.module
import miragefairy2019.mod.modules.fairyweapon.FairyWeapon
import miragefairy2019.mod3.artifacts.Artifacts
import miragefairy2019.mod3.fairy.Fairy
import miragefairy2019.mod3.fairy.relation.FairyRelation
import miragefairy2019.mod3.fairybox.FairyBox
import miragefairy2019.mod3.placeditem.PlacedItem
import miragefairy2019.mod3.playeraura.PlayerAura
import miragefairy2019.mod3.recipes.Recipes
import miragefairy2019.mod3.skill.Skill
import miragefairy2019.mod3.systems.Systems

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
