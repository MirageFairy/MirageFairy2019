package miragefairy2019.mod

import miragefairy2019.jei.Jei
import miragefairy2019.mod.modules.fairyweapon.FairyWeapon
import miragefairy2019.mod3.artifacts.Artifacts
import miragefairy2019.mod3.damagesource.DamageSource
import miragefairy2019.mod3.fairy.Fairy
import miragefairy2019.mod3.fairy.relation.FairyRelation
import miragefairy2019.mod3.fairybox.FairyBox
import miragefairy2019.mod3.main.Main
import miragefairy2019.mod3.pick.Pick
import miragefairy2019.mod3.placeditem.PlacedItem
import miragefairy2019.mod3.playeraura.PlayerAura
import miragefairy2019.mod3.recipes.Recipes
import miragefairy2019.mod3.skill.Skill
import miragefairy2019.mod3.systems.Systems

val modules = listOf(
    Jei.module,
    Main.module,
    PlayerAura.module,
    PlacedItem.module,
    Skill.module,
    Fairy.module,
    FairyWeapon.module,
    FairyRelation.module,
    Artifacts.module,
    Systems.module,
    DamageSource.module,
    Pick.module,
    FairyBox.module,
    Recipes.module
)
