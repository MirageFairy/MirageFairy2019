package miragefairy2019.mod.systems

import miragefairy2019.lib.modinitializer.module

val systemsModule = module {
    manaModule()
    ergModule()
    masteryModule()
    damageSourceModule()
    manualRepairModule()
    combineModule()
    VanillaItemBlocking.vanillaItemBlockingModule(this)
    vanillaOreNamesModule()
    Ae2SpatialIoBlocking.ae2SpatialIoBlockingModule(this)
    DaemonSystem.daemonSystemModule(this)
    facedCursorModule()
    fairyStickCraftModule()
    lightningBoltBlockerModule()
    iotMessageModule()
}
