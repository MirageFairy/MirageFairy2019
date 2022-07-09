package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.mod.fairy.FairyCard
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
            FairyCard.WATER().overworld()
            FairyCard.STONE().overworld()
            FairyCard.DIRT().overworld()
            FairyCard.SAND().overworld()
            FairyCard.GRAVEL().overworld()
            FairyCard.IRON().overworld()
            FairyCard.GOLD().overworld()
            FairyCard.DIAMOND().overworld()
            FairyCard.EMERALD().overworld()
            FairyCard.MAGNETITE().overworld()
            FairyCard.APATITE().overworld()
            FairyCard.FLUORITE().overworld()
            FairyCard.SULFUR().overworld()
            FairyCard.CINNABAR().overworld()
            FairyCard.MOONSTONE().overworld()
            FairyCard.PYROPE().overworld()
            FairyCard.SMITHSONITE().overworld()
            FairyCard.REDSTONE().overworld()
            FairyCard.LAPISLAZULI().overworld()
            FairyCard.OBSIDIAN().overworld()
            FairyCard.COAL().overworld()
            FairyCard.ICE().overworld()
            FairyCard.NEPHRITE().overworld()
            FairyCard.TOURMALINE().overworld()
            FairyCard.TOPAZ().overworld()
            FairyCard.PYRITE().overworld()
            FairyCard.BEDROCK().overworld()

            FairyCard.SPIDER().overworld()
            FairyCard.CHICKEN().overworld()
            FairyCard.SKELETON().overworld()
            FairyCard.ZOMBIE().overworld()
            FairyCard.CREEPER().overworld()
            FairyCard.FISH().overworld()
            FairyCard.COD().overworld()
            FairyCard.SALMON().overworld()
            FairyCard.PUFFERFISH().overworld()
            FairyCard.CLOWNFISH().overworld()
            FairyCard.VILLAGER().overworld()
            FairyCard.COW().overworld()
            FairyCard.PIG().overworld()
            FairyCard.SPIDER_EYE().overworld()
            FairyCard.SLIME().overworld()
            FairyCard.BAT().overworld()

            FairyCard.WHEAT().overworld()
            FairyCard.LILAC().overworld()
            FairyCard.APPLE().overworld()
            FairyCard.CARROT().overworld()
            FairyCard.CACTUS().overworld()
            FairyCard.SPRUCE().overworld()
            FairyCard.SEED().overworld()
            FairyCard.POISONOUS_POTATO().overworld()
            FairyCard.MELON().overworld()
            FairyCard.BEETROOT().overworld()
            FairyCard.MIRAGE_FLOWER().overworld()
            FairyCard.SUGAR_CANE().overworld()
            FairyCard.POTATO().overworld()
            FairyCard.PEONY().overworld()


            // ネザー
            FairyCard.LAVA().nether()
            FairyCard.FIRE().nether()

            FairyCard.GLOWSTONE().nether()
            FairyCard.BEDROCK().nether()

            FairyCard.MAGMA_CUBE().nether()
            FairyCard.BLAZE().nether()


            // エンド
            FairyCard.ENDERMAN().end()
            FairyCard.ENDER_DRAGON().end()
            FairyCard.SHULKER().end()
            FairyCard.CHORUS_FRUIT().end()

        }

        // 固定枠
        DropCategory.FIXED {

            fun IDrop.fixed() = FairyCrystalDrop.dropHandlers.add(DropHandler(this@fixed) { true })

            FairyCard.AIR(1.0).fixed()
            FairyCard.TIME(0.0001).fixed()
            FairyCard.GRAVITY(0.0001).fixed()

        }

        // レア枠
        DropCategory.RARE {

            fun IDrop.world(predicate: World.(BlockPos) -> Boolean) = FairyCrystalDrop.dropHandlers.add(DropHandler(this@world) a@{
                predicate(world ?: return@a false, (pos ?: return@a false).offset(facing ?: return@a false))
            })

            FairyCard.THUNDER(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && isRainingAt(it) && isThundering }
            FairyCard.SUN(0.0001).world { provider.isSurfaceWorld && canSeeSky(it) && time(6000, 18000) && !isRainingAt(it) }
            FairyCard.MOON(0.0001).world { provider.isSurfaceWorld && canSeeSky(it) && (time(19000, 24000) || time(0, 5000)) && !isRainingAt(it) }
            FairyCard.STAR(0.0003).world { provider.isSurfaceWorld && canSeeSky(it) && (time(19000, 24000) || time(0, 5000)) && !isRainingAt(it) }

            FairyCard.DAYTIME(0.001).world { time(6000, 18000) }
            FairyCard.NIGHT(0.001).world { time(19000, 24000) || time(0, 5000) }
            FairyCard.MORNING(0.001).world { time(5000, 9000) }
            FairyCard.SUNRISE(0.001).world { time(5000, 6000) }
            FairyCard.FINE(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && !isRainingAt(it) }
            FairyCard.RAIN(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && isRainingAt(it) }

        }

        // TODO remove
        DropCategory.RARE {

            fun IDrop.world(predicate: World.(BlockPos) -> Boolean) = FairyCrystalDrop.dropHandlers.add(DropHandler(this@world) a@{
                predicate(world ?: return@a false, (pos ?: return@a false).offset(facing ?: return@a false))
            })

            FairyCard.CUPID(0.00001).world {
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
