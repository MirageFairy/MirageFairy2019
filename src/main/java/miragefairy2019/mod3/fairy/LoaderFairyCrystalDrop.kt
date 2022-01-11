package miragefairy2019.mod3.fairy

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.toInstant
import miragefairy2019.mod.api.ApiFairyCrystal
import miragefairy2019.mod.api.fairycrystal.DropCategory
import miragefairy2019.mod.api.fairycrystal.DropFixed
import miragefairy2019.mod.api.fairycrystal.IDrop
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop
import miragefairy2019.mod3.fairy.relation.FairyRelationEntry
import miragefairy2019.mod3.fairy.relation.FairyRelationRegistries
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import java.time.Instant
import java.time.LocalDateTime
import kotlin.math.pow

private val FairyRelationEntry<*>.fairyCrystalBaseDropWeight get() = 0.1 * 0.1.pow((fairy.main.rare - 1.0) / 2.0) * weight

val loaderFairyCrystalDrop: Module = {
    onCreateItemStack {
        fun World.time(min: Int, max: Int) = provider.isSurfaceWorld && min <= (worldTime + 6000) % 24000 && (worldTime + 6000) % 24000 <= max

        class WithDropCategory(val dropCategory: DropCategory) {
            operator fun RankedFairyTypeBundle.invoke(weight: Double? = null) = DropFixed(this, dropCategory, weight ?: (0.1 * 0.1.pow((main.rare - 1).toDouble())))
        }

        operator fun DropCategory.invoke(block: WithDropCategory.() -> Unit) = WithDropCategory(this).block()

        fun register(listener: IRightClickDrop) = run { ApiFairyCrystal.dropsFairyCrystal.add(listener); Unit }


        // コモン枠
        DropCategory.COMMON {

            fun IDrop.overworld() = register(object : IRightClickDrop {
                override fun getDrop() = this@overworld
                override fun testWorld(world: World, pos: BlockPos) = world.provider.isSurfaceWorld
            })

            fun IDrop.nether() = register(object : IRightClickDrop {
                override fun getDrop() = this@nether
                override fun testBiomeType(biomeType: BiomeDictionary.Type) = biomeType == BiomeDictionary.Type.NETHER
            })

            fun IDrop.end() = register(object : IRightClickDrop {
                override fun getDrop() = this@end
                override fun testBiomeType(biomeType: BiomeDictionary.Type) = biomeType == BiomeDictionary.Type.END
            })

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
            FairyTypes.instance.spidereye().overworld()
            FairyTypes.instance.slime().overworld()

            FairyTypes.instance.wheat().overworld()
            FairyTypes.instance.lilac().overworld()
            FairyTypes.instance.apple().overworld()
            FairyTypes.instance.carrot().overworld()
            FairyTypes.instance.cactus().overworld()
            FairyTypes.instance.spruce().overworld()
            FairyTypes.instance.seed().overworld()
            FairyTypes.instance.poisonouspotato().overworld()
            FairyTypes.instance.melon().overworld()
            FairyTypes.instance.beetroot().overworld()
            FairyTypes.instance.mirageflower().overworld()


            // ネザー
            FairyTypes.instance.lava().nether()
            FairyTypes.instance.fire().nether()

            FairyTypes.instance.glowstone().nether()

            FairyTypes.instance.magmacube().nether()
            FairyTypes.instance.blaze().nether()


            // エンド
            FairyTypes.instance.enderman().end()
            FairyTypes.instance.enderdragon().end()
            FairyTypes.instance.shulker().end()
            FairyTypes.instance.chorusfruit().end()

        }

        // 固定枠
        FairyTypes.instance.run {
            DropCategory.FIXED {

                fun IDrop.fixed() = register(object : IRightClickDrop {
                    override fun getDrop() = this@fixed
                    override fun testUseItem(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) = true
                })

                air(1.0).fixed()
                time(0.0001).fixed()
                if (Instant.now() < LocalDateTime.of(2022, 1, 10, 0, 0, 0).toInstant()) {
                    santaclaus(0.001).fixed()
                    christmas(0.005).fixed()
                }
                if (Instant.now() < LocalDateTime.of(2022, 2, 1, 0, 0, 0).toInstant()) {
                    hatsuyume(0.0001).fixed()
                }

            }
        }

        // レア枠
        FairyTypes.instance.run {
            DropCategory.RARE {

                FairyRelationRegistries.entity.forEach { relation ->
                    register(object : IRightClickDrop {
                        override fun getDrop() = DropFixed(relation.fairy, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)
                        override fun testEntity(entity: Entity) = relation.key(entity)
                    })
                }
                FairyRelationRegistries.biomeType.forEach { relation ->
                    register(object : IRightClickDrop {
                        override fun getDrop() = DropFixed(relation.fairy, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)
                        override fun testBiomeType(biomeType: BiomeDictionary.Type) = biomeType == relation.key
                    })
                }
                FairyRelationRegistries.block.forEach { relation ->
                    register(object : IRightClickDrop {
                        override fun getDrop() = DropFixed(relation.fairy, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)
                        override fun testBlock(block: Block) = block == relation.key
                    })
                }
                FairyRelationRegistries.blockState.forEach { relation ->
                    register(object : IRightClickDrop {
                        override fun getDrop() = DropFixed(relation.fairy, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)
                        override fun testBlockState(world: World, blockPos: BlockPos, blockState: IBlockState) = blockState == relation.key
                    })
                }
                FairyRelationRegistries.item.forEach { relation ->
                    register(object : IRightClickDrop {
                        override fun getDrop() = DropFixed(relation.fairy, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)
                        override fun testItem(item: Item) = item == relation.key
                    })
                }
                FairyRelationRegistries.itemStack.forEach { relation ->
                    register(object : IRightClickDrop {
                        override fun getDrop() = DropFixed(relation.fairy, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)
                        override fun testItemStack(itemStack: ItemStack) = relation.key(itemStack)
                    })
                }
                FairyRelationRegistries.ingredient.forEach { relation ->
                    register(object : IRightClickDrop {
                        override fun getDrop() = DropFixed(relation.fairy, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)
                        override fun testItemStack(itemStack: ItemStack) = relation.key.test(itemStack)
                    })
                }


                fun IDrop.world(predicate: World.(BlockPos) -> Boolean) = register(object : IRightClickDrop {
                    override fun getDrop(): IDrop = this@world
                    override fun testWorld(world: World, pos: BlockPos): Boolean = predicate(world, pos)
                })

                thunder(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && isRainingAt(it) && isThundering }
                sun(0.0001).world { provider.isSurfaceWorld && canSeeSky(it) && time(6000, 18000) && !isRainingAt(it) }
                moon(0.0001).world { provider.isSurfaceWorld && canSeeSky(it) && (time(19000, 24000) || time(0, 5000)) && !isRainingAt(it) }
                star(0.0003).world { provider.isSurfaceWorld && canSeeSky(it) && (time(19000, 24000) || time(0, 5000)) && !isRainingAt(it) }

                daytime(0.001).world { time(6000, 18000) }
                night(0.001).world { time(19000, 24000) || time(0, 5000) }
                morning(0.001).world { time(5000, 9000) }
                sunrise(0.001).world { time(5000, 6000) }
                fine(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && !isRainingAt(it) }
                rain(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && isRainingAt(it) }

            }
        }

    }
}
