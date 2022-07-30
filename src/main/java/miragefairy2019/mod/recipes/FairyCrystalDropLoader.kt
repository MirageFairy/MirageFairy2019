package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.mod.fairy.FairyCard
import miragefairy2019.mod.fairyrelation.FairyRelationEntry
import miragefairy2019.mod.fairyrelation.FairyRelationRegistries
import miragefairy2019.mod.systems.DropCategory
import miragefairy2019.mod.systems.DropFixed
import miragefairy2019.mod.systems.DropHandler
import miragefairy2019.mod.systems.FairyCrystalDrop
import miragefairy2019.mod.systems.FairyCrystalDropEnvironment
import miragefairy2019.mod.systems.IDrop
import mirrg.kotlin.hydrogen.or
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.pow

private val FairyRelationEntry<*>.fairyCrystalBaseDropWeight get() = 0.1 * 0.1.pow((fairyCard.rare - 1.0) / 2.0) * weight

val fairyCrystalDropLoaderModule = module {
    onCreateItemStack {
        fun IDrop.register(predicate: FairyCrystalDropEnvironment.() -> Boolean) = FairyCrystalDrop.dropHandlers.add(DropHandler(this, predicate))
        fun World.time(min: Int, max: Int) = provider.isSurfaceWorld && min <= (worldTime + 6000) % 24000 && (worldTime + 6000) % 24000 <= max

        class WithDropCategory(val dropCategory: DropCategory) {
            operator fun FairyCard.invoke(weight: Double? = null) = DropFixed(this, dropCategory, weight ?: (0.1 * 0.1.pow((rare - 1).toDouble())))
        }

        operator fun DropCategory.invoke(block: WithDropCategory.() -> Unit) = WithDropCategory(this).block()


        // コモン枠
        DropCategory.COMMON {

            fun IDrop.overworld() = this.register a@{ world.or { return@a false }.provider.isSurfaceWorld }
            fun IDrop.nether() = this.register { BiomeDictionary.Type.NETHER in biomeTypes }
            fun IDrop.end() = this.register { BiomeDictionary.Type.END in biomeTypes }

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

            FairyCard.AIR(1.0).register { true }
            FairyCard.TIME(0.0001).register { true }
            FairyCard.GRAVITY(0.0001).register { true }

        }

        // レア枠
        DropCategory.RARE {

            fun IDrop.world(predicate: World.(BlockPos) -> Boolean) = this.register a@{
                predicate(world ?: return@a false, (pos ?: return@a false).offset(facing ?: return@a false))
            }

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

            FairyCard.AUTUMN(0.001).register { LocalDateTime.now(ZoneOffset.UTC).monthValue in 8..12 }

            FairyCard.VOID(0.01).register a@{ player.or { return@a false }.posY < 0 }

        }

        // TODO remove
        DropCategory.RARE {

            FairyCard.CUPID(0.00001).register {
                LocalDateTime.now(ZoneOffset.UTC) < LocalDateTime.of(2022, 8, 1, 0, 0, 0)
            }

            FairyCard.AVALON(0.00001).register {
                LocalDateTime.now(ZoneOffset.UTC) < LocalDateTime.of(2022, 10, 1, 0, 0, 0)
            }
            FairyCard.AVALON(0.00001).register {
                LocalDateTime.now(ZoneOffset.UTC) < LocalDateTime.of(2022, 10, 1, 0, 0, 0) &&
                    BiomeDictionary.Type.MUSHROOM in this.biomeTypes
            }

        }


        // 妖精関係レジストリー
        FairyRelationRegistries.entity.forEach { relation ->
            DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight).register { entities.any { relation.key(it) } }
        }
        FairyRelationRegistries.biomeType.forEach { relation ->
            DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight).register { relation.key in biomeTypes }
        }
        FairyRelationRegistries.block.forEach { relation ->
            DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight).register { relation.key in blocks }
        }
        FairyRelationRegistries.blockState.forEach { relation ->
            DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight).register { relation.key in blockStates }
        }
        FairyRelationRegistries.item.forEach { relation ->
            DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight).register { relation.key in items }
        }
        FairyRelationRegistries.itemStack.forEach { relation ->
            DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight).register { itemStacks.any { relation.key(it) } }
        }
        FairyRelationRegistries.ingredient.forEach { relation ->
            DropFixed(relation.fairyCard, DropCategory.RARE, relation.fairyCrystalBaseDropWeight).register { itemStacks.any { relation.key.test(it) } }
        }

    }
}
