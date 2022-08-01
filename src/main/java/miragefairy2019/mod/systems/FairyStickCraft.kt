package miragefairy2019.mod.systems

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.mod.fairystickcraft.ApiFairyStickCraft
import miragefairy2019.mod.fairystickcraft.FairyStickCraftRegistry

val fairyStickCraftModule = module {

    // TODO シンプル化
    // 初期化
    onInstantiation {
        ApiFairyStickCraft.fairyStickCraftRegistry = FairyStickCraftRegistry()
    }

}
