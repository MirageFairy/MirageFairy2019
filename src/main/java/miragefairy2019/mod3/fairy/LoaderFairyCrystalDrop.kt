package miragefairy2019.mod3.fairy

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.toInstant
import miragefairy2019.mod.api.ApiFairyCrystal
import miragefairy2019.mod.api.fairycrystal.DropCategory
import miragefairy2019.mod.api.fairycrystal.DropFixed
import miragefairy2019.mod.api.fairycrystal.IDrop
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop
import miragefairy2019.mod3.worldgen.MirageFlower
import net.minecraft.block.Block
import net.minecraft.block.BlockDoublePlant
import net.minecraft.block.BlockOldLeaf
import net.minecraft.block.BlockOldLog
import net.minecraft.block.BlockPlanks
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Enchantments
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.oredict.OreIngredient
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

                FairyRelation.entity.forEach { relation ->
                    register(object : IRightClickDrop {
                        override fun getDrop() = DropFixed(relation.fairy, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)
                        override fun testEntity(entity: Entity) = relation.key(entity)
                    })
                }
                FairyRelation.biomeType.forEach { relation ->
                    register(object : IRightClickDrop {
                        override fun getDrop() = DropFixed(relation.fairy, DropCategory.RARE, relation.fairyCrystalBaseDropWeight)
                        override fun testBiomeType(biomeType: BiomeDictionary.Type) = biomeType == relation.key
                    })
                }


                fun IDrop.block(vararg blocks: Block) = register(object : IRightClickDrop {
                    override fun getDrop() = this@block
                    override fun testBlock(block: Block) = block in blocks
                })


                fun IDrop.blockState(vararg blockStates: IBlockState) = register(object : IRightClickDrop {
                    override fun getDrop() = this@blockState
                    override fun testBlockState(world: World, blockPos: BlockPos, blockState: IBlockState) = blockState in blockStates
                })


                fun IDrop.item(vararg items: Item) = register(object : IRightClickDrop {
                    override fun getDrop() = this@item
                    override fun testItem(item: Item) = item in items
                })


                fun IDrop.itemStack(predicate: (ItemStack) -> Boolean) = register(object : IRightClickDrop {
                    override fun getDrop() = this@itemStack
                    override fun testItemStack(itemStack: ItemStack) = predicate(itemStack)
                })


                fun IDrop.ingredients(vararg ingredients: Ingredient) = register(object : IRightClickDrop {
                    override fun getDrop() = this@ingredients
                    override fun testItemStack(itemStack: ItemStack) = ingredients.any { it.test(itemStack) }
                })

                fun IDrop.itemStack(vararg itemStacks: ItemStack) = ingredients(*itemStacks.map { Ingredient.fromStacks(it) }.toTypedArray())
                fun IDrop.ore(vararg oreNames: String) = ingredients(*oreNames.map { OreIngredient(it) }.toTypedArray())
                fun IDrop.material(material: String) = ore(*listOf("ingot", "nugget", "gem", "dust", "dustTiny", "block", "rod", "plate", "ore").map { "$it$material" }.toTypedArray())


                water(0.3).block(Blocks.WATER, Blocks.FLOWING_WATER)
                water(0.3).item(Items.WATER_BUCKET)
                lava(0.1).block(Blocks.LAVA, Blocks.FLOWING_LAVA)
                lava(0.3).item(Items.LAVA_BUCKET)
                fire(0.1).block(Blocks.FIRE)

                stone(0.3).block(Blocks.STONE, Blocks.COBBLESTONE)
                dirt(0.3).block(Blocks.DIRT, Blocks.GRASS)
                sand(0.3).block(Blocks.SAND, Blocks.SANDSTONE, Blocks.RED_SANDSTONE)
                gravel(0.1).block(Blocks.GRAVEL)
                iron(0.1).material("Iron")
                gold(0.03).material("Gold")
                diamond(0.01).material("Diamond")
                emerald(0.03).material("Emerald")
                magnetite(0.1).material("Magnetite")
                apatite(0.03).material("Apatite")
                fluorite(0.01).material("Fluorite")
                sulfur(0.03).material("Sulfur")
                cinnabar(0.01).material("Cinnabar")
                moonstone(0.003).material("Moonstone")
                pyrope(0.01).material("Pyrope")
                smithsonite(0.1).material("Smithsonite")
                redstone(0.1).material("Redstone")
                lapislazuli(0.1).material("Lapis")
                obsidian(0.1).block(Blocks.OBSIDIAN)
                glowstone(0.1).material("Glowstone")
                glowstone(0.1).ore("glowstone")
                coal(0.1).itemStack(ItemStack(Items.COAL))
                coal(0.1).material("Coal")
                ice(0.3).block(Blocks.ICE)
                packedice(0.01).block(Blocks.PACKED_ICE)
                nephrite(0.03).material("Nephrite")
                tourmaline(0.01).material("Tourmaline")
                topaz(0.01).material("Topaz")

                fish(0.3).item(Items.FISH)
                cod(0.1).itemStack(ItemStack(Items.FISH, 1, 0))
                salmon(0.1).itemStack(ItemStack(Items.FISH, 1, 1))
                pufferfish(0.03).itemStack(ItemStack(Items.FISH, 1, 3))
                clownfish(0.03).itemStack(ItemStack(Items.FISH, 1, 2))
                netherstar(0.01).item(Items.NETHER_STAR)

                wheat(0.1).block(Blocks.WHEAT, Blocks.HAY_BLOCK)
                lilac(0.03).blockState(Blocks.DOUBLE_PLANT.defaultState.withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SYRINGA))
                apple(0.1).item(Items.APPLE, Items.GOLDEN_APPLE)
                carrot(0.03).item(Items.CARROT, Items.CARROT_ON_A_STICK, Items.GOLDEN_CARROT)
                cactus(0.1).block(Blocks.CACTUS)
                spruce(0.1).blockState(
                    Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE),
                    Blocks.PLANKS.defaultState.withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE),
                    Blocks.LEAVES.defaultState.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)
                )
                seed(0.1).item(Items.WHEAT_SEEDS)
                poisonouspotato(0.01).item(Items.POISONOUS_POTATO)
                melon(0.03).item(Items.MELON)
                beetroot(0.03).item(Items.BEETROOT)
                mirageflower(0.03).block(MirageFlower.blockMirageFlower())

                torch(0.3).block(Blocks.TORCH)
                furnace(0.1).block(Blocks.FURNACE)
                magentaglazedterracotta(0.03).block(Blocks.MAGENTA_GLAZED_TERRACOTTA)
                axe(0.1).item(Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE)
                chest(0.1).block(Blocks.CHEST)
                craftingtable(0.1).block(Blocks.CRAFTING_TABLE)
                potion(0.1).item(Items.POTIONITEM, Items.LINGERING_POTION, Items.SPLASH_POTION)
                sword(0.1).item(Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD)
                dispenser(0.1).block(Blocks.DISPENSER)
                anvil(0.1).block(Blocks.ANVIL)
                enchant(0.03).block(Blocks.ENCHANTING_TABLE)
                enchant(0.03).item(Items.ENCHANTED_BOOK)
                enchant(0.03).itemStack { it.isItemEnchanted }
                brewingstand(0.03).item(Items.BREWING_STAND)
                brewingstand(0.03).block(Blocks.BREWING_STAND)
                hoe(0.3).item(Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE)
                shield(0.1).item(Items.SHIELD)
                hopper(0.1).block(Blocks.HOPPER)
                glass(0.1).ore("blockGlass")
                activatorrail(0.03).block(Blocks.ACTIVATOR_RAIL)
                ironbars(0.1).block(Blocks.IRON_BARS)
                door(0.1).item(Items.OAK_DOOR)
                door(0.1).block(Blocks.OAK_DOOR)
                irondoor(0.03).item(Items.IRON_DOOR)
                irondoor(0.03).block(Blocks.IRON_DOOR)
                redstonerepeater(0.03).item(Items.REPEATER)
                redstonerepeater(0.03).block(Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER)
                redstonecomparator(0.03).item(Items.COMPARATOR)
                redstonecomparator(0.03).block(Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR)

                bread(0.1).item(Items.BREAD)
                cookie(0.1).item(Items.COOKIE)
                cake(0.03).item(Items.CAKE)
                enchantedgoldenapple(0.003).itemStack(ItemStack(Items.GOLDEN_APPLE, 1, 1))
                sugar(0.3).item(Items.SUGAR)
                rottenflesh(0.1).item(Items.ROTTEN_FLESH)
                bakedpotato(0.03).item(Items.BAKED_POTATO)
                cookedchicken(0.1).item(Items.COOKED_CHICKEN)
                cookedsalmon(0.03).itemStack(ItemStack(Items.COOKED_FISH, 1, 1))
                steak(0.1).item(Items.COOKED_BEEF)
                goldenapple(0.03).itemStack(ItemStack(Items.GOLDEN_APPLE, 1, 0))
                spidereye(0.03).item(Items.SPIDER_EYE)
                pumpkinpie(0.03).item(Items.PUMPKIN_PIE)
                beetrootsoup(0.03).item(Items.BEETROOT_SOUP)
                chorusfruit(0.01).item(Items.CHORUS_FRUIT)
                mushroomstew(0.03).item(Items.MUSHROOM_STEW)
                rawrabbit(0.01).item(Items.RABBIT)

                fortune(0.01).itemStack { EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, it) > 0 }

                eleven(0.003).item(Items.RECORD_11)


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
