package miragefairy2019.mod3.fairy

import miragefairy2019.libkt.Module
import miragefairy2019.mod3.fairy.api.ApiFairyRelation
import miragefairy2019.mod3.fairy.api.FairyRelationEntry
import miragefairy2019.mod3.fairy.api.IFairyRelationHandler
import miragefairy2019.mod3.fairy.api.IFairyRelationManager
import net.minecraft.entity.Entity

val moduleFairy: Module = {

    // マネージャー初期化
    onInstantiation {
        ApiFairyRelation.fairyRelationManager = object : IFairyRelationManager {
            private val entityClassRelationRegistry = mutableListOf<FairyRelationEntry<Class<out Entity>>>()
            private val entityRelationRegistry = mutableListOf<IFairyRelationHandler<Entity>>()
            override fun getEntityClassRelationRegistry() = entityClassRelationRegistry
            override fun getEntityRelationRegistry() = entityRelationRegistry
        }
    }

}
