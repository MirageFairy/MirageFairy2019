package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.mod.fairy.FairyTypes
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

private val FairyRelationEntry<*>.fairyCrystalBaseDropWeight get() = 0.1 * 0.1.pow((fairyCard.getVariant().rare - 1.0) / 2.0) * weight

val fairyCrystalDropLoaderModule = module {
    onCreateItemStack {
        fun World.time(min: Int, max: Int) = provider.isSurfaceWorld && min <= (worldTime + 6000) % 24000 && (worldTime + 6000) % 24000 <= max

        class WithDropCategory(val dropCategory: DropCategory) {
            operator fun FairyCard.invoke(weight: Double? = null) = DropFixed(this, dropCategory, weight ?: (0.1 * 0.1.pow((getVariant().rare - 1).toDouble())))
        }

        operator fun DropCategory.invoke(block: WithDropCategory.() -> Unit) = WithDropCategory(this).block()


        // コモン枠
        DropCategory.COMMON {

            fun IDrop.overworld() = FairyCrystalDrop.dropHandlers.add(DropHandler(this@overworld) a@{ (world ?: return@a false).provider.isSurfaceWorld })
            fun IDrop.nether() = FairyCrystalDrop.dropHandlers.add(DropHandler(this@nether) { BiomeDictionary.Type.NETHER in biomeTypes })
            fun IDrop.end() = FairyCrystalDrop.dropHandlers.add(DropHandler(this@end) { BiomeDictionary.Type.END in biomeTypes })

            // 地上
            FairyTypes.instance.water().overworld()
            FairyTypes.instance.stone().overworld()
            FairyTypes.instance.dirt().overworld()
            FairyTypes.instance.sand().overworld()
            FairyTypes.instance.gravel().overworld()
            FairyTypes.instance.iron().overworld()
            FairyTypes.instance.gold().overworld()
            FairyTypes.instance.diamond().overworld()
            FairyTypes.instance.emerald().overworld()
            FairyTypes.instance.magnetite().overworld()
            FairyTypes.instance.apatite().overworld()
            FairyTypes.instance.fluorite().overworld()
            FairyTypes.instance.sulfur().overworld()
            FairyTypes.instance.cinnabar().overworld()
            FairyTypes.instance.moonstone().overworld()
            FairyTypes.instance.pyrope().overworld()
            FairyTypes.instance.smithsonite().overworld()
            FairyTypes.instance.redstone().overworld()
            FairyTypes.instance.lapislazuli().overworld()
            FairyTypes.instance.obsidian().overworld()
            FairyTypes.instance.coal().overworld()
            FairyTypes.instance.ice().overworld()
            FairyTypes.instance.nephrite().overworld()
            FairyTypes.instance.tourmaline().overworld()
            FairyTypes.instance.topaz().overworld()
            FairyTypes.instance.pyrite().overworld()
            FairyTypes.instance.bedrock().overworld()

            FairyTypes.instance.spider().overworld()
            FairyTypes.instance.chicken().overworld()
            FairyTypes.instance.skeleton().overworld()
            FairyTypes.instance.zombie().overworld()
            FairyTypes.instance.creeper().overworld()
            FairyTypes.instance.fish().overworld()
            FairyTypes.instance.cod().overworld()
            FairyTypes.instance.salmon().overworld()
            FairyTypes.instance.pufferfish().overworld()
            FairyTypes.instance.clownfish().overworld()
            FairyTypes.instance.villager().overworld()
            FairyTypes.instance.cow().overworld()
            FairyTypes.instance.pig().overworld()
            FairyTypes.instance.spiderEye().overworld()
            FairyTypes.instance.slime().overworld()
            FairyTypes.instance.bat().overworld()

            FairyTypes.instance.wheat().overworld()
            FairyTypes.instance.lilac().overworld()
            FairyTypes.instance.apple().overworld()
            FairyTypes.instance.carrot().overworld()
            FairyTypes.instance.cactus().overworld()
            FairyTypes.instance.spruce().overworld()
            FairyTypes.instance.seed().overworld()
            FairyTypes.instance.poisonousPotato().overworld()
            FairyTypes.instance.melon().overworld()
            FairyTypes.instance.beetroot().overworld()
            FairyTypes.instance.mirageFlower().overworld()
            FairyTypes.instance.sugarCane().overworld()
            FairyTypes.instance.potato().overworld()
            FairyTypes.instance.peony().overworld()


            // ネザー
            FairyTypes.instance.lava().nether()
            FairyTypes.instance.fire().nether()

            FairyTypes.instance.glowstone().nether()
            FairyTypes.instance.bedrock().nether()

            FairyTypes.instance.magmaCube().nether()
            FairyTypes.instance.blaze().nether()


            // エンド
            FairyTypes.instance.enderman().end()
            FairyTypes.instance.enderDragon().end()
            FairyTypes.instance.shulker().end()
            FairyTypes.instance.chorusFruit().end()

        }

        // 固定枠
        DropCategory.FIXED {

            fun IDrop.fixed() = FairyCrystalDrop.dropHandlers.add(DropHandler(this@fixed) { true })

            FairyTypes.instance.air(1.0).fixed()
            FairyTypes.instance.time(0.0001).fixed()
            FairyTypes.instance.gravity(0.0001).fixed()

        }

        // レア枠
        DropCategory.RARE {

            fun IDrop.world(predicate: World.(BlockPos) -> Boolean) = FairyCrystalDrop.dropHandlers.add(DropHandler(this@world) a@{
                predicate(world ?: return@a false, (pos ?: return@a false).offset(facing ?: return@a false))
            })

            FairyTypes.instance.thunder(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && isRainingAt(it) && isThundering }
            FairyTypes.instance.sun(0.0001).world { provider.isSurfaceWorld && canSeeSky(it) && time(6000, 18000) && !isRainingAt(it) }
            FairyTypes.instance.moon(0.0001).world { provider.isSurfaceWorld && canSeeSky(it) && (time(19000, 24000) || time(0, 5000)) && !isRainingAt(it) }
            FairyTypes.instance.star(0.0003).world { provider.isSurfaceWorld && canSeeSky(it) && (time(19000, 24000) || time(0, 5000)) && !isRainingAt(it) }

            FairyTypes.instance.daytime(0.001).world { time(6000, 18000) }
            FairyTypes.instance.night(0.001).world { time(19000, 24000) || time(0, 5000) }
            FairyTypes.instance.morning(0.001).world { time(5000, 9000) }
            FairyTypes.instance.sunrise(0.001).world { time(5000, 6000) }
            FairyTypes.instance.fine(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && !isRainingAt(it) }
            FairyTypes.instance.rain(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && isRainingAt(it) }

        }

        // TODO remove
        DropCategory.RARE {

            fun IDrop.world(predicate: World.(BlockPos) -> Boolean) = FairyCrystalDrop.dropHandlers.add(DropHandler(this@world) a@{
                predicate(world ?: return@a false, (pos ?: return@a false).offset(facing ?: return@a false))
            })

            FairyTypes.instance.cupid(0.00001).world {
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
