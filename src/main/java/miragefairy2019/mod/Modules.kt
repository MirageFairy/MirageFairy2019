package miragefairy2019.mod

import miragefairy2019.jei.Jei
import miragefairy2019.mod.modules.fairyweapon.FairyWeapon
import miragefairy2019.mod3.artifacts.Artifacts
import miragefairy2019.mod3.artifacts.fairybox.FairyBox
import miragefairy2019.mod3.damagesource.DamageSource
import miragefairy2019.mod3.fairy.Fairy
import miragefairy2019.mod3.fairy.relation.FairyRelation
import miragefairy2019.mod3.main.Main
import miragefairy2019.mod3.manualrepair.ManualRepair
import miragefairy2019.mod3.pick.Pick
import miragefairy2019.mod3.placeditem.PlacedItem
import miragefairy2019.mod3.playeraura.PlayerAura
import miragefairy2019.mod3.skill.Skill

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
    ManualRepair.module,
    DamageSource.module,
    Pick.module,
    FairyBox.module
)
