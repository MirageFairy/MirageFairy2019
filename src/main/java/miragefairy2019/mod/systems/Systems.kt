package miragefairy2019.mod.systems

import miragefairy2019.libkt.module

object Systems {
    val module = module {
        manaModule(this)
        ergModule(this)
        masteryModule(this)
        DamageSource.module(this)
        ManualRepair.module(this)
        Combine.module(this)
        VanillaItemBlocking.module(this)
        VanillaOreNames.module(this)
        Ae2SpatialIoBlocking.module(this)
        DaemonSystem.module(this)
    }
}
