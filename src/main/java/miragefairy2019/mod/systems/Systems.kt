package miragefairy2019.mod.systems

import miragefairy2019.lib.modinitializer.module

val systemsModule = module {
    manaModule(this)
    ergModule(this)
    masteryModule(this)
    damageSourceModule(this)
    manualRepairModule(this)
    combineModule(this)
    VanillaItemBlocking.vanillaItemBlockingModule(this)
    vanillaOreNamesModule(this)
    Ae2SpatialIoBlocking.ae2SpatialIoBlockingModule(this)
    DaemonSystem.daemonSystemModule(this)
}
