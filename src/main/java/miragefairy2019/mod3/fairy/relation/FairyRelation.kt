package miragefairy2019.mod3.fairy

import miragefairy2019.libkt.Module
import miragefairy2019.mod3.fairy.relation.api.ApiFairyRelation
import miragefairy2019.mod3.fairy.relation.api.FairyRelationEntry
import miragefairy2019.mod3.fairy.relation.api.IFairyRelationHandler
import miragefairy2019.mod3.fairy.relation.api.IFairyRelationManager
import net.minecraft.entity.Entity

object FairyRelation {
    val module: Module = {

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
}
