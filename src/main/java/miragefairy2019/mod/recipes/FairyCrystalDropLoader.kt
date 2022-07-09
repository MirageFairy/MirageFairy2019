package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.mod.fairy.FairyCard
import miragefairy2019.mod.fairy.FairyTypes
import miragefairy2019.mod.fairyrelation.FairyRelationEntry
import miragefairy2019.mod.fairyrelation.FairyRelationRegistries
import miragefairy2019.mod.systems.DropCategory
import miragefairy2019.mod.systems.DropFixed
import miragefairy2019.mod.systems.DropHandler
import miragefairy2019.mod.systems.FairyCrystalDrop
import miragefairy2019.mod.systems.IDrop
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.pow

private val FairyRelationEntry<*>.fairyCrystalBaseDropWeight get() = 0.1 * 0.1.pow((fairyCard.rare - 1.0) / 2.0) * weight

val fairyCrystalDropLoaderModule = module {
    onCreateItemStack {
        fun World.time(min: Int, max: Int) = provider.isSurfaceWorld && min <= (worldTime + 6000) % 24000 && (worldTime + 6000) % 24000 <= max

        class WithDropCategory(val dropCategory: DropCategory) {
            operator fun FairyCard.invoke(weight: Double? = null) = DropFixed(this, dropCategory, weight ?: (0.1 * 0.1.pow((rare - 1).toDouble())))
        }

        operator fun DropCategory.invoke(block: WithDropCategory.() -> Unit) = WithDropCategory(this).block()


        // コモン枠
        DropCategory.COMMON {

            fun IDrop.overworld() = FairyCrystalDrop.dropHandlers.add(DropHandler(this@overworld) a@{ (world ?: return@a false).provider.isSurfaceWorld })
            fun IDrop.nether() = FairyCrystalDrop.dropHandlers.add(DropHandler(this@nether) { BiomeDictionary.Type.NETHER in biomeTypes })
            fun IDrop.end() = FairyCrystalDrop.dropHandlers.add(DropHandler(this@end) { BiomeDictionary.Type.END in biomeTypes })

            // 地上
            FairyTypes.instance.WATER().overworld()
            FairyTypes.instance.STONE().overworld()
            FairyTypes.instance.DIRT().overworld()
            FairyTypes.instance.SAND().overworld()
            FairyTypes.instance.GRAVEL().overworld()
            FairyTypes.instance.IRON().overworld()
            FairyTypes.instance.GOLD().overworld()
            FairyTypes.instance.DIAMOND().overworld()
            FairyTypes.instance.EMERALD().overworld()
            FairyTypes.instance.MAGNETITE().overworld()
            FairyTypes.instance.APATITE().overworld()
            FairyTypes.instance.FLUORITE().overworld()
            FairyTypes.instance.SULFUR().overworld()
            FairyTypes.instance.CINNABAR().overworld()
            FairyTypes.instance.MOONSTONE().overworld()
            FairyTypes.instance.PYROPE().overworld()
            FairyTypes.instance.SMITHSONITE().overworld()
            FairyTypes.instance.REDSTONE().overworld()
            FairyTypes.instance.LAPISLAZULI().overworld()
            FairyTypes.instance.OBSIDIAN().overworld()
            FairyTypes.instance.COAL().overworld()
            FairyTypes.instance.ICE().overworld()
            FairyTypes.instance.NEPHRITE().overworld()
            FairyTypes.instance.TOURMALINE().overworld()
            FairyTypes.instance.TOPAZ().overworld()
            FairyTypes.instance.PYRITE().overworld()
            FairyTypes.instance.BEDROCK().overworld()

            FairyTypes.instance.SPIDER().overworld()
            FairyTypes.instance.CHICKEN().overworld()
            FairyTypes.instance.SKELETON().overworld()
            FairyTypes.instance.ZOMBIE().overworld()
            FairyTypes.instance.CREEPER().overworld()
            FairyTypes.instance.FISH().overworld()
            FairyTypes.instance.COD().overworld()
            FairyTypes.instance.SALMON().overworld()
            FairyTypes.instance.PUFFERFISH().overworld()
            FairyTypes.instance.CLOWNFISH().overworld()
            FairyTypes.instance.VILLAGER().overworld()
            FairyTypes.instance.COW().overworld()
            FairyTypes.instance.PIG().overworld()
            FairyTypes.instance.SPIDER_EYE().overworld()
            FairyTypes.instance.SLIME().overworld()
            FairyTypes.instance.BAT().overworld()

            FairyTypes.instance.WHEAT().overworld()
            FairyTypes.instance.LILAC().overworld()
            FairyTypes.instance.APPLE().overworld()
            FairyTypes.instance.CARROT().overworld()
            FairyTypes.instance.CACTUS().overworld()
            FairyTypes.instance.SPRUCE().overworld()
            FairyTypes.instance.SEED().overworld()
            FairyTypes.instance.POISONOUS_POTATO().overworld()
            FairyTypes.instance.MELON().overworld()
            FairyTypes.instance.BEETROOT().overworld()
            FairyTypes.instance.MIRAGE_FLOWER().overworld()
            FairyTypes.instance.SUGAR_CANE().overworld()
            FairyTypes.instance.POTATO().overworld()
            FairyTypes.instance.PEONY().overworld()


            // ネザー
            FairyTypes.instance.LAVA().nether()
            FairyTypes.instance.FIRE().nether()

            FairyTypes.instance.GLOWSTONE().nether()
            FairyTypes.instance.BEDROCK().nether()

            FairyTypes.instance.MAGMA_CUBE().nether()
            FairyTypes.instance.BLAZE().nether()


            // エンド
            FairyTypes.instance.ENDERMAN().end()
            FairyTypes.instance.ENDER_DRAGON().end()
            FairyTypes.instance.SHULKER().end()
            FairyTypes.instance.CHORUS_FRUIT().end()

        }

        // 固定枠
        DropCategory.FIXED {

            fun IDrop.fixed() = FairyCrystalDrop.dropHandlers.add(DropHandler(this@fixed) { true })

            FairyTypes.instance.AIR(1.0).fixed()
            FairyTypes.instance.TIME(0.0001).fixed()
            FairyTypes.instance.GRAVITY(0.0001).fixed()

        }

        // レア枠
        DropCategory.RARE {

            fun IDrop.world(predicate: World.(BlockPos) -> Boolean) = FairyCrystalDrop.dropHandlers.add(DropHandler(this@world) a@{
                predicate(world ?: return@a false, (pos ?: return@a false).offset(facing ?: return@a false))
            })

            FairyTypes.instance.THUNDER(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && isRainingAt(it) && isThundering }
            FairyTypes.instance.SUN(0.0001).world { provider.isSurfaceWorld && canSeeSky(it) && time(6000, 18000) && !isRainingAt(it) }
            FairyTypes.instance.MOON(0.0001).world { provider.isSurfaceWorld && canSeeSky(it) && (time(19000, 24000) || time(0, 5000)) && !isRainingAt(it) }
            FairyTypes.instance.STAR(0.0003).world { provider.isSurfaceWorld && canSeeSky(it) && (time(19000, 24000) || time(0, 5000)) && !isRainingAt(it) }

            FairyTypes.instance.DAYTIME(0.001).world { time(6000, 18000) }
            FairyTypes.instance.NIGHT(0.001).world { time(19000, 24000) || time(0, 5000) }
            FairyTypes.instance.MORNING(0.001).world { time(5000, 9000) }
            FairyTypes.instance.SUNRISE(0.001).world { time(5000, 6000) }
            FairyTypes.instance.FINE(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && !isRainingAt(it) }
            FairyTypes.instance.RAIN(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && isRainingAt(it) }

        }

        // TODO remove
        DropCategory.RARE {

            fun IDrop.world(predicate: World.(BlockPos) -> Boolean) = FairyCrystalDrop.dropHandlers.add(DropHandler(this@world) a@{
                predicate(world ?: return@a false, (pos ?: return@a false).offset(facing ?: return@a false))
            })

            FairyTypes.instance.CUPID(0.00001).world {
                LocalDateTime.now(ZoneOffset.UTC) < LocalDateTime.of(2022, 8, 1, 0, 0, 0)
            }

        }


        // 妖精関係レジストリー
        FairyRelationRegistries.entity.forEach { relation ->
            FairyCrystalDrop.dropHandlers.add(DropHandler(DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)) { entities.any { relation.key(it) } })
        }
        FairyRelationRegistries.biomeType.forEach { relation ->
            FairyCrystalDrop.dropHandlers.add(DropHandler(DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)) { relation.key in biomeTypes })
        }
        FairyRelationRegistries.block.forEach { relation ->
            FairyCrystalDrop.dropHandlers.add(DropHandler(DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)) { relation.key in blocks })
        }
        FairyRelationRegistries.blockState.forEach { relation ->
            FairyCrystalDrop.dropHandlers.add(DropHandler(DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)) { relation.key in blockStates })
        }
        FairyRelationRegistries.item.forEach { relation ->
            FairyCrystalDrop.dropHandlers.add(DropHandler(DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)) { relation.key in items })
        }
        FairyRelationRegistries.itemStack.forEach { relation ->
            FairyCrystalDrop.dropHandlers.add(DropHandler(DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)) { itemStacks.any { relation.key(it) } })
        }
        FairyRelationRegistries.ingredient.forEach { relation ->
            FairyCrystalDrop.dropHandlers.add(DropHandler(DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)) { itemStacks.any { relation.key.test(it) } })
        }

    }
}
