package miragefairy2019.mod3.fairy

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.toInstant
import miragefairy2019.mod.api.ApiFairyCrystal
import miragefairy2019.mod.api.fairycrystal.DropCategory
import miragefairy2019.mod.api.fairycrystal.DropFixed
import miragefairy2019.mod.api.fairycrystal.IDrop
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop
import miragefairy2019.mod.api.fairycrystal.RightClickDrops
import miragefairy2019.mod3.worldgen.MirageFlower
import net.minecraft.block.Block
import net.minecraft.block.BlockDoublePlant
import net.minecraft.block.BlockOldLeaf
import net.minecraft.block.BlockOldLog
import net.minecraft.block.BlockPlanks
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.EnchantmentHelper
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
import java.time.Instant
import java.time.LocalDateTime
import java.util.function.Predicate
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
        FairyTypes.instance.run {
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
                water().overworld()
                stone().overworld()
                dirt().overworld()
                sand().overworld()
                gravel().overworld()
                iron().overworld()
                gold().overworld()
                diamond().overworld()
                emerald().overworld()
                magnetite().overworld()
                apatite().overworld()
                fluorite().overworld()
                sulfur().overworld()
                cinnabar().overworld()
                moonstone().overworld()
                pyrope().overworld()
                smithsonite().overworld()
                redstone().overworld()
                lapislazuli().overworld()
                obsidian().overworld()
                coal().overworld()
                ice().overworld()
                nephrite().overworld()
                tourmaline().overworld()
                topaz().overworld()

                spider().overworld()
                chicken().overworld()
                skeleton().overworld()
                zombie().overworld()
                creeper().overworld()
                fish().overworld()
                cod().overworld()
                salmon().overworld()
                pufferfish().overworld()
                clownfish().overworld()
                villager().overworld()
                cow().overworld()
                pig().overworld()
                spidereye().overworld()
                slime().overworld()

                wheat().overworld()
                lilac().overworld()
                apple().overworld()
                carrot().overworld()
                cactus().overworld()
                spruce().overworld()
                seed().overworld()
                poisonouspotato().overworld()
                melon().overworld()
                beetroot().overworld()
                mirageflower().overworld()


                // ネザー
                lava().nether()
                fire().nether()

                glowstone().nether()

                magmacube().nether()
                blaze().nether()


                // エンド
                enderman().end()
                enderdragon().end()
                shulker().end()
                chorusfruit().end()

            }
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
                    register(RightClickDrops.entity(DropFixed(relation.fairy, DropCategory.RARE, relation.fairyCrystalBaseDropWeight), { relation.key(it) }))
                }
                FairyRelation.biomeType.forEach { relation ->
                    register(RightClickDrops.biomeTypes(DropFixed(relation.fairy, DropCategory.RARE, relation.fairyCrystalBaseDropWeight), relation.key))
                }

                fun IDrop.world(predicate: World.(BlockPos) -> Boolean) = register(RightClickDrops.world(this) { world, blockPos -> world.predicate(blockPos) })
                fun IDrop.block(vararg blocks: Block) = register(RightClickDrops.blocks(this, *blocks))
                fun IDrop.blockState(vararg blockStates: IBlockState) = register(RightClickDrops.blockStates(this, *blockStates))
                fun IDrop.item(vararg items: Item) = register(RightClickDrops.items(this, *items))
                fun IDrop.itemStack(vararg itemStacks: ItemStack) = register(RightClickDrops.ingredients(this, *itemStacks.map { Ingredient.fromStacks(it) }.toTypedArray()))
                fun IDrop.itemStack(predicate: (ItemStack) -> Boolean) = register(RightClickDrops.ingredients(this, Predicate { predicate(it) }))
                fun IDrop.material(material: String) = register(RightClickDrops.ores(this, *listOf("ingot", "nugget", "gem", "dust", "dustTiny", "block", "rod", "plate", "ore").map { "$it$material" }.toTypedArray()))
                fun IDrop.ore(vararg ore: String) = register(RightClickDrops.ores(this, *ore))

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
