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
import net.minecraft.entity.Entity
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.boss.EntityWither
import net.minecraft.entity.monster.EntityBlaze
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.monster.EntityIronGolem
import net.minecraft.entity.monster.EntityMagmaCube
import net.minecraft.entity.monster.EntityShulker
import net.minecraft.entity.monster.EntitySkeleton
import net.minecraft.entity.monster.EntitySlime
import net.minecraft.entity.monster.EntitySpider
import net.minecraft.entity.monster.EntityWitherSkeleton
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.entity.passive.EntityChicken
import net.minecraft.entity.passive.EntityCow
import net.minecraft.entity.passive.EntityPig
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.init.Blocks
import net.minecraft.init.Enchantments
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import java.time.Instant
import java.time.LocalDateTime
import java.util.function.Predicate

val loaderFairyCrystalDrop: Module = {
    onCreateItemStack {
        fun World.time(min: Int, max: Int) = provider.isSurfaceWorld && min <= (worldTime + 6000) % 24000 && (worldTime + 6000) % 24000 <= max

        class WithDropCategory(val dropCategory: DropCategory) {
            operator fun RankedFairyTypeBundle.invoke(weight: Double? = null) = DropFixed(this, dropCategory, weight ?: 0.1 * Math.pow(0.1, (main.rare - 1).toDouble()))
        }

        operator fun DropCategory.invoke(block: WithDropCategory.() -> Unit) = WithDropCategory(this).block()

        fun register(listener: IRightClickDrop) = run { ApiFairyCrystal.dropsFairyCrystal.add(listener); Unit }
        fun IDrop.fixed() = register(RightClickDrops.fixed(this))
        fun IDrop.world(predicate: World.(BlockPos) -> Boolean) = register(RightClickDrops.world(this) { world, blockPos -> world.predicate(blockPos) })
        fun IDrop.biomeType(vararg biomes: BiomeDictionary.Type) = register(RightClickDrops.biomeTypes(this, *biomes))
        fun IDrop.block(vararg blocks: Block) = register(RightClickDrops.blocks(this, *blocks))
        fun IDrop.blockState(vararg blockStates: IBlockState) = register(RightClickDrops.blockStates(this, *blockStates))
        fun IDrop.item(vararg items: Item) = register(RightClickDrops.items(this, *items))
        fun IDrop.itemStack(vararg itemStacks: ItemStack) = register(RightClickDrops.ingredients(this, *itemStacks.map { Ingredient.fromStacks(it) }.toTypedArray()))
        fun IDrop.itemStack(predicate: (ItemStack) -> Boolean) = register(RightClickDrops.ingredients(this, Predicate { predicate(it) }))
        fun IDrop.material(material: String) = register(RightClickDrops.ores(this, *listOf("ingot", "nugget", "gem", "dust", "dustTiny", "block", "rod", "plate", "ore").map { "$it$material" }.toTypedArray()))
        fun IDrop.ore(vararg ore: String) = register(RightClickDrops.ores(this, *ore))
        fun IDrop.entity(vararg entityClasses: Class<out Entity>) = register(RightClickDrops.classEntities(this, *entityClasses))
        fun <E : Entity> IDrop.entity(classEntity: Class<out E>, predicate: E.() -> Boolean) = register(RightClickDrops.entity(this, classEntity, predicate))


        // コモン
        FairyTypes.instance.run {
            DropCategory.COMMON {

                // 地上
                water().world { provider.isSurfaceWorld }
                stone().world { provider.isSurfaceWorld }
                dirt().world { provider.isSurfaceWorld }
                sand().world { provider.isSurfaceWorld }
                gravel().world { provider.isSurfaceWorld }
                iron().world { provider.isSurfaceWorld }
                gold().world { provider.isSurfaceWorld }
                diamond().world { provider.isSurfaceWorld }
                emerald().world { provider.isSurfaceWorld }
                magnetite().world { provider.isSurfaceWorld }
                apatite().world { provider.isSurfaceWorld }
                fluorite().world { provider.isSurfaceWorld }
                sulfur().world { provider.isSurfaceWorld }
                cinnabar().world { provider.isSurfaceWorld }
                moonstone().world { provider.isSurfaceWorld }
                pyrope().world { provider.isSurfaceWorld }
                smithsonite().world { provider.isSurfaceWorld }
                redstone().world { provider.isSurfaceWorld }
                lapislazuli().world { provider.isSurfaceWorld }
                obsidian().world { provider.isSurfaceWorld }
                coal().world { provider.isSurfaceWorld }
                ice().world { provider.isSurfaceWorld }
                nephrite().world { provider.isSurfaceWorld }
                tourmaline().world { provider.isSurfaceWorld }
                topaz().world { provider.isSurfaceWorld }

                spider().world { provider.isSurfaceWorld }
                chicken().world { provider.isSurfaceWorld }
                skeleton().world { provider.isSurfaceWorld }
                zombie().world { provider.isSurfaceWorld }
                creeper().world { provider.isSurfaceWorld }
                fish().world { provider.isSurfaceWorld }
                cod().world { provider.isSurfaceWorld }
                salmon().world { provider.isSurfaceWorld }
                pufferfish().world { provider.isSurfaceWorld }
                clownfish().world { provider.isSurfaceWorld }
                villager().world { provider.isSurfaceWorld }
                cow().world { provider.isSurfaceWorld }
                pig().world { provider.isSurfaceWorld }
                spidereye().world { provider.isSurfaceWorld }
                slime().world { provider.isSurfaceWorld }

                wheat().world { provider.isSurfaceWorld }
                lilac().world { provider.isSurfaceWorld }
                apple().world { provider.isSurfaceWorld }
                carrot().world { provider.isSurfaceWorld }
                cactus().world { provider.isSurfaceWorld }
                spruce().world { provider.isSurfaceWorld }
                seed().world { provider.isSurfaceWorld }
                poisonouspotato().world { provider.isSurfaceWorld }
                melon().world { provider.isSurfaceWorld }
                beetroot().world { provider.isSurfaceWorld }
                mirageflower().world { provider.isSurfaceWorld }


                // ネザー
                lava().biomeType(BiomeDictionary.Type.NETHER)
                fire().biomeType(BiomeDictionary.Type.NETHER)

                glowstone().biomeType(BiomeDictionary.Type.NETHER)

                magmacube().biomeType(BiomeDictionary.Type.NETHER)
                blaze().biomeType(BiomeDictionary.Type.NETHER)


                // エンド
                enderman().biomeType(BiomeDictionary.Type.END)
                enderdragon().biomeType(BiomeDictionary.Type.END)
                shulker().biomeType(BiomeDictionary.Type.END)
                chorusfruit().biomeType(BiomeDictionary.Type.END)

            }
        }

        // 条件付きドロップ
        FairyTypes.instance.run {
            DropCategory.FIXED {

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
            DropCategory.RARE {

                water(0.3).block(Blocks.WATER, Blocks.FLOWING_WATER)
                water(0.3).item(Items.WATER_BUCKET)
                lava(0.1).block(Blocks.LAVA, Blocks.FLOWING_LAVA)
                lava(0.3).item(Items.LAVA_BUCKET)
                fire(0.1).block(Blocks.FIRE)

                thunder(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && isRainingAt(it) && isThundering }
                sun(0.0001).world { provider.isSurfaceWorld && canSeeSky(it) && time(6000, 18000) && !isRainingAt(it) }
                moon(0.0001).world { provider.isSurfaceWorld && canSeeSky(it) && (time(19000, 24000) || time(0, 5000)) && !isRainingAt(it) }
                star(0.0003).world { provider.isSurfaceWorld && canSeeSky(it) && (time(19000, 24000) || time(0, 5000)) && !isRainingAt(it) }

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
                if (Instant.now() < LocalDateTime.of(2021, 12, 1, 0, 0, 0).toInstant()) {
                    imperialtopaz(0.001).material("Topaz")
                }

                enderman(0.03).entity(EntityEnderman::class.java)
                spider(0.1).entity(EntitySpider::class.java)
                enderdragon(0.1).entity(EntityDragon::class.java)
                chicken(0.1).entity(EntityChicken::class.java)
                skeleton(0.3).entity(EntitySkeleton::class.java)
                zombie(0.3).entity(EntityZombie::class.java)
                witherskeleton(0.03).entity(EntityWitherSkeleton::class.java)
                wither(0.01).entity(EntityWither::class.java)
                creeper(0.1).entity(EntityCreeper::class.java)
                fish(0.3).item(Items.FISH)
                cod(0.1).itemStack(ItemStack(Items.FISH, 1, 0))
                salmon(0.1).itemStack(ItemStack(Items.FISH, 1, 1))
                pufferfish(0.03).itemStack(ItemStack(Items.FISH, 1, 3))
                clownfish(0.03).itemStack(ItemStack(Items.FISH, 1, 2))
                villager(0.3).entity(EntityVillager::class.java) { true }
                librarian(0.1).entity(EntityVillager::class.java) { professionForge.registryName == ResourceLocation("minecraft:librarian") }
                netherstar(0.01).item(Items.NETHER_STAR)
                golem(0.1).entity(EntityIronGolem::class.java)
                cow(0.1).entity(EntityCow::class.java)
                pig(0.1).entity(EntityPig::class.java)
                shulker(0.03).entity(EntityShulker::class.java)
                slime(0.1).entity(EntitySlime::class.java)
                magmacube(0.1).entity(EntityMagmaCube::class.java)
                blaze(0.1).entity(EntityBlaze::class.java)

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

                daytime(0.001).world { time(6000, 18000) }
                night(0.001).world { time(19000, 24000) || time(0, 5000) }
                morning(0.001).world { time(5000, 9000) }
                sunrise(0.001).world { time(5000, 6000) }
                fine(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && !isRainingAt(it) }
                rain(0.01).world { provider.isSurfaceWorld && canSeeSky(it) && isRainingAt(it) }

                plains(0.01).biomeType(BiomeDictionary.Type.PLAINS)
                forest(0.01).biomeType(BiomeDictionary.Type.FOREST)
                ocean(0.01).biomeType(BiomeDictionary.Type.OCEAN)
                taiga(0.01).biomeType(BiomeDictionary.Type.CONIFEROUS)
                desert(0.01).biomeType(BiomeDictionary.Type.SANDY)
                mountain(0.01).biomeType(BiomeDictionary.Type.MOUNTAIN)

                fortune(0.01).itemStack { EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, it) > 0 }

                eleven(0.003).item(Items.RECORD_11)

            }
        }

    }
}
