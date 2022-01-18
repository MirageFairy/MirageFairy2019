package miragefairy2019.mod

import miragefairy2019.mod.modules.fairycrystal.FairyCrystal
import miragefairy2019.mod.modules.fairyweapon.item.FairyWeapon
import miragefairy2019.mod.modules.ore.Ore
import miragefairy2019.mod.modules.oreseed.OreSeed
import miragefairy2019.mod3.artifacts.Artifacts
import miragefairy2019.mod3.damagesource.DamageSource
import miragefairy2019.mod3.fairy.Fairy
import miragefairy2019.mod3.fairy.relation.FairyRelation
import miragefairy2019.mod3.fairymaterials.FairyMaterials
import miragefairy2019.mod3.fairystick.FairyStick
import miragefairy2019.mod3.main.Main
import miragefairy2019.mod3.manualrepair.ManualRepair
import miragefairy2019.mod3.pick.Pick
import miragefairy2019.mod3.playeraura.PlayerAura
import miragefairy2019.mod3.skill.Skill
import miragefairy2019.mod3.sphere.Sphere
import miragefairy2019.mod3.worldgen.WorldGen

val modules = listOf(
    Main.module,
    PlayerAura.module,
    Skill.module,
    Fairy.module,
    FairyWeapon.module,
    FairyRelation.module,
    Sphere.module,
    FairyStick.module,
    Artifacts.module,
    ManualRepair.module,
    DamageSource.module,
    FairyMaterials.module,
    Ore.module,
    WorldGen.module,
    Pick.module,
    OreSeed.module,
    FairyCrystal.module
)
