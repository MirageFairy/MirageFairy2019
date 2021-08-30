package miragefairy2019.mod.modules.fairycrystal

import miragefairy2019.libkt.Module
import miragefairy2019.mod.api.ApiFairyCrystal
import miragefairy2019.mod.api.fairycrystal.DropFixed
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop
import miragefairy2019.mod.api.fairycrystal.RightClickDrops
import miragefairy2019.mod.modules.fairy.FairyTypes
import miragefairy2019.mod.modules.fairy.RankedFairyTypeBundle
import net.minecraft.block.BlockDoublePlant
import net.minecraft.block.BlockOldLeaf
import net.minecraft.block.BlockOldLog
import net.minecraft.block.BlockPlanks
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.boss.EntityWither
import net.minecraft.entity.monster.*
import net.minecraft.entity.passive.EntityChicken
import net.minecraft.entity.passive.EntityCow
import net.minecraft.entity.passive.EntityPig
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.init.Blocks
import net.minecraft.init.Enchantments
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import java.util.function.Predicate

val loaderFairyCrystalDrop: Module = {
    onCreateItemStack {
        fun m(material: String) = listOf("ingot", "nugget", "gem", "dust", "dustTiny", "block", "rod", "plate", "ore").map { "$it$material" }.toTypedArray()
        fun r(fairy: RankedFairyTypeBundle, weight: Double? = null) = DropFixed(fairy.main.createItemStack(), weight ?: 0.1 * Math.pow(0.1, (fairy.main.rare - 1).toDouble()))
        fun time(world: World, min: Int, max: Int) = world.provider.isSurfaceWorld && min <= (world.worldTime + 6000) % 24000 && (world.worldTime + 6000) % 24000 <= max

        operator fun RankedFairyTypeBundle.invoke(weight: Double): Pair<RankedFairyTypeBundle, Double> = Pair(this, weight)

        fun register(listener: IRightClickDrop) {
            ApiFairyCrystal.dropsFairyCrystal.add(listener)
        }

        class BlockPosScope(val world: World, val blockPos: BlockPos)

        fun RankedFairyTypeBundle.world(predicate: BlockPosScope.() -> Boolean) {
            register(RightClickDrops.world(r(this)) { world, blockPos -> BlockPosScope(world, blockPos).predicate() })
        }

        fun Pair<RankedFairyTypeBundle, Double>.world(predicate: BlockPosScope.() -> Boolean) {
            register(RightClickDrops.world(r(this.first, this.second)) { world, blockPos -> BlockPosScope(world, blockPos).predicate() })
        }

        fun RankedFairyTypeBundle.biomeType(vararg biomes: BiomeDictionary.Type) {
            register(RightClickDrops.biomeTypes(r(this), *biomes))
        }

        fun Pair<RankedFairyTypeBundle, Double>.biomeType(vararg biomes: BiomeDictionary.Type) {
            register(RightClickDrops.biomeTypes(r(this.first, this.second), *biomes))
        }

        // コモン
        FairyTypes.instance.run {
            water.world { world.provider.isSurfaceWorld }
            stone.world { world.provider.isSurfaceWorld }
            dirt.world { world.provider.isSurfaceWorld }
            sand.world { world.provider.isSurfaceWorld }
            gravel.world { world.provider.isSurfaceWorld }
            iron.world { world.provider.isSurfaceWorld }
            gold.world { world.provider.isSurfaceWorld }
            diamond.world { world.provider.isSurfaceWorld }
            emerald.world { world.provider.isSurfaceWorld }
            magnetite.world { world.provider.isSurfaceWorld }
            apatite.world { world.provider.isSurfaceWorld }
            fluorite.world { world.provider.isSurfaceWorld }
            sulfur.world { world.provider.isSurfaceWorld }
            cinnabar.world { world.provider.isSurfaceWorld }
            moonstone.world { world.provider.isSurfaceWorld }
            pyrope.world { world.provider.isSurfaceWorld }
            smithsonite.world { world.provider.isSurfaceWorld }
            redstone.world { world.provider.isSurfaceWorld }
            lapislazuli.world { world.provider.isSurfaceWorld }
            obsidian.world { world.provider.isSurfaceWorld }
            coal.world { world.provider.isSurfaceWorld }
            ice.world { world.provider.isSurfaceWorld }
            nephrite.world { world.provider.isSurfaceWorld }
            tourmaline.world { world.provider.isSurfaceWorld }
            topaz.world { world.provider.isSurfaceWorld }

            spider.world { world.provider.isSurfaceWorld }
            chicken.world { world.provider.isSurfaceWorld }
            skeleton.world { world.provider.isSurfaceWorld }
            zombie.world { world.provider.isSurfaceWorld }
            creeper.world { world.provider.isSurfaceWorld }
            fish.world { world.provider.isSurfaceWorld }
            cod.world { world.provider.isSurfaceWorld }
            salmon.world { world.provider.isSurfaceWorld }
            pufferfish.world { world.provider.isSurfaceWorld }
            clownfish.world { world.provider.isSurfaceWorld }
            villager.world { world.provider.isSurfaceWorld }
            cow.world { world.provider.isSurfaceWorld }
            pig.world { world.provider.isSurfaceWorld }

            wheat.world { world.provider.isSurfaceWorld }
            lilac.world { world.provider.isSurfaceWorld }
            apple.world { world.provider.isSurfaceWorld }
            carrot.world { world.provider.isSurfaceWorld }
            cactus.world { world.provider.isSurfaceWorld }
            spruce.world { world.provider.isSurfaceWorld }
            seed.world { world.provider.isSurfaceWorld }
            poisonouspotato.world { world.provider.isSurfaceWorld }
            melon.world { world.provider.isSurfaceWorld }
        }
        FairyTypes.instance.run {
            lava.biomeType(BiomeDictionary.Type.NETHER)
            fire.biomeType(BiomeDictionary.Type.NETHER)

            glowstone.biomeType(BiomeDictionary.Type.NETHER)
        }
        FairyTypes.instance.run {
            enderman.biomeType(BiomeDictionary.Type.END)
            enderdragon.biomeType(BiomeDictionary.Type.END)
        }

        // 限定高確率ドロップ
        FairyTypes.instance.run {
            register(RightClickDrops.fixed(r(air, 1.0)))

            register(RightClickDrops.blocks(r(water, 0.3), Blocks.WATER, Blocks.FLOWING_WATER))
            register(RightClickDrops.items(r(water, 0.3), Items.WATER_BUCKET))
            register(RightClickDrops.blocks(r(lava, 0.1), Blocks.LAVA, Blocks.FLOWING_LAVA))
            register(RightClickDrops.items(r(lava, 0.3), Items.LAVA_BUCKET))
            register(RightClickDrops.blocks(r(fire, 0.1), Blocks.FIRE))

            thunder(0.01).world { world.provider.isSurfaceWorld && world.canSeeSky(blockPos) && world.isRainingAt(blockPos) && world.isThundering }
            sun(0.0001).world { world.provider.isSurfaceWorld && world.canSeeSky(blockPos) && time(world, 6000, 18000) && !world.isRainingAt(blockPos) }
            moon(0.0001).world { world.provider.isSurfaceWorld && world.canSeeSky(blockPos) && (time(world, 19000, 24000) || time(world, 0, 5000)) && !world.isRainingAt(blockPos) }
            star(0.0003).world { world.provider.isSurfaceWorld && world.canSeeSky(blockPos) && (time(world, 19000, 24000) || time(world, 0, 5000)) && !world.isRainingAt(blockPos) }

            register(RightClickDrops.blocks(r(stone, 0.3), Blocks.STONE, Blocks.COBBLESTONE))
            register(RightClickDrops.blocks(r(dirt, 0.3), Blocks.DIRT, Blocks.GRASS))
            register(RightClickDrops.blocks(r(sand, 0.3), Blocks.SAND, Blocks.SANDSTONE, Blocks.RED_SANDSTONE))
            register(RightClickDrops.blocks(r(gravel, 0.1), Blocks.GRAVEL))
            register(RightClickDrops.ores(r(iron, 0.1), *m("Iron")))
            register(RightClickDrops.ores(r(gold, 0.03), *m("Gold")))
            register(RightClickDrops.ores(r(diamond, 0.01), *m("Diamond")))
            register(RightClickDrops.ores(r(emerald, 0.03), *m("Emerald")))
            register(RightClickDrops.ores(r(magnetite, 0.1), *m("Magnetite")))
            register(RightClickDrops.ores(r(apatite, 0.03), *m("Apatite")))
            register(RightClickDrops.ores(r(fluorite, 0.01), *m("Fluorite")))
            register(RightClickDrops.ores(r(sulfur, 0.03), *m("Sulfur")))
            register(RightClickDrops.ores(r(cinnabar, 0.01), *m("Cinnabar")))
            register(RightClickDrops.ores(r(moonstone, 0.003), *m("Moonstone")))
            register(RightClickDrops.ores(r(pyrope, 0.01), *m("Pyrope")))
            register(RightClickDrops.ores(r(smithsonite, 0.1), *m("Smithsonite")))
            register(RightClickDrops.ores(r(redstone, 0.1), *m("Redstone")))
            register(RightClickDrops.ores(r(lapislazuli, 0.1), *m("Lapis")))
            register(RightClickDrops.blocks(r(obsidian, 0.1), Blocks.OBSIDIAN))
            register(RightClickDrops.ores(r(glowstone, 0.1), *m("Glowstone")))
            register(RightClickDrops.ores(r(glowstone, 0.1), "glowstone"))
            register(RightClickDrops.ingredients(r(coal, 0.1), Ingredient.fromStacks(ItemStack(Items.COAL))))
            register(RightClickDrops.ores(r(coal, 0.1), *m("Coal")))
            register(RightClickDrops.blocks(r(ice, 0.3), Blocks.ICE))
            register(RightClickDrops.blocks(r(packedice, 0.01), Blocks.PACKED_ICE))
            register(RightClickDrops.ores(r(nephrite, 0.03), *m("Nephrite")))
            register(RightClickDrops.ores(r(tourmaline, 0.01), *m("Tourmaline")))
            register(RightClickDrops.ores(r(topaz, 0.01), *m("Topaz")))

            register(RightClickDrops.classEntities(r(enderman, 0.03), EntityEnderman::class.java))
            register(RightClickDrops.classEntities(r(spider, 0.1), EntitySpider::class.java))
            register(RightClickDrops.classEntities(r(enderdragon, 0.1), EntityDragon::class.java))
            register(RightClickDrops.classEntities(r(chicken, 0.1), EntityChicken::class.java))
            register(RightClickDrops.classEntities(r(skeleton, 0.3), EntitySkeleton::class.java))
            register(RightClickDrops.classEntities(r(zombie, 0.3), EntityZombie::class.java))
            register(RightClickDrops.classEntities(r(witherskeleton, 0.03), EntityWitherSkeleton::class.java))
            register(RightClickDrops.classEntities(r(wither, 0.01), EntityWither::class.java))
            register(RightClickDrops.classEntities(r(creeper, 0.1), EntityCreeper::class.java))
            register(RightClickDrops.items(r(fish, 0.3), Items.FISH))
            register(RightClickDrops.ingredients(r(cod, 0.1), Ingredient.fromStacks(ItemStack(Items.FISH, 1, 0))))
            register(RightClickDrops.ingredients(r(salmon, 0.1), Ingredient.fromStacks(ItemStack(Items.FISH, 1, 1))))
            register(RightClickDrops.ingredients(r(pufferfish, 0.03), Ingredient.fromStacks(ItemStack(Items.FISH, 1, 3))))
            register(RightClickDrops.ingredients(r(clownfish, 0.03), Ingredient.fromStacks(ItemStack(Items.FISH, 1, 2))))
            register(RightClickDrops.entity(r(villager, 0.3), EntityVillager::class.java) { e: EntityVillager? -> true })
            register(RightClickDrops.entity(r(librarian, 0.1), EntityVillager::class.java) { e: EntityVillager -> e.professionForge.registryName == ResourceLocation("minecraft:librarian") })
            register(RightClickDrops.items(r(netherstar, 0.01), Items.NETHER_STAR))
            register(RightClickDrops.classEntities(r(golem, 0.1), EntityIronGolem::class.java))
            register(RightClickDrops.classEntities(r(cow, 0.1), EntityCow::class.java))
            register(RightClickDrops.classEntities(r(pig, 0.1), EntityPig::class.java))

            register(RightClickDrops.blocks(r(wheat, 0.1), Blocks.WHEAT, Blocks.HAY_BLOCK))
            register(RightClickDrops.blockStates(r(lilac, 0.03), Blocks.DOUBLE_PLANT.defaultState.withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SYRINGA)))
            register(RightClickDrops.items(r(apple, 0.1), Items.APPLE, Items.GOLDEN_APPLE))
            register(RightClickDrops.items(r(carrot, 0.03), Items.CARROT, Items.CARROT_ON_A_STICK, Items.GOLDEN_CARROT))
            register(RightClickDrops.blocks(r(cactus, 0.1), Blocks.CACTUS))
            register(RightClickDrops.blockStates(r(spruce, 0.1),
                    Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE),
                    Blocks.PLANKS.defaultState.withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE),
                    Blocks.LEAVES.defaultState.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)))
            register(RightClickDrops.items(r(seed, 0.1), Items.WHEAT_SEEDS))
            register(RightClickDrops.items(r(poisonouspotato, 0.01), Items.POISONOUS_POTATO))
            register(RightClickDrops.items(r(melon, 0.03), Items.MELON))

            register(RightClickDrops.blocks(r(torch, 0.3), Blocks.TORCH))
            register(RightClickDrops.blocks(r(furnace, 0.1), Blocks.FURNACE))
            register(RightClickDrops.blocks(r(magentaglazedterracotta, 0.03), Blocks.MAGENTA_GLAZED_TERRACOTTA))
            register(RightClickDrops.items(r(axe, 0.1), Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE))
            register(RightClickDrops.blocks(r(chest, 0.1), Blocks.CHEST))
            register(RightClickDrops.blocks(r(craftingtable, 0.1), Blocks.CRAFTING_TABLE))
            register(RightClickDrops.items(r(potion, 0.1), Items.POTIONITEM, Items.LINGERING_POTION, Items.SPLASH_POTION))
            register(RightClickDrops.items(r(sword, 0.1), Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD))
            register(RightClickDrops.blocks(r(dispenser, 0.1), Blocks.DISPENSER))
            register(RightClickDrops.blocks(r(anvil, 0.1), Blocks.ANVIL))
            register(RightClickDrops.blocks(r(enchant, 0.03), Blocks.ENCHANTING_TABLE))
            register(RightClickDrops.items(r(enchant, 0.03), Items.ENCHANTED_BOOK))
            register(RightClickDrops.ingredients(r(enchant, 0.03), Predicate { itemStack: ItemStack -> itemStack.isItemEnchanted }))
            register(RightClickDrops.items(r(brewingstand, 0.03), Items.BREWING_STAND))
            register(RightClickDrops.blocks(r(brewingstand, 0.03), Blocks.BREWING_STAND))
            register(RightClickDrops.items(r(hoe, 0.3), Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE))
            register(RightClickDrops.items(r(shield, 0.1), Items.SHIELD))
            register(RightClickDrops.blocks(r(hopper, 0.1), Blocks.HOPPER))
            register(RightClickDrops.ores(r(glass, 0.1), "blockGlass"))
            register(RightClickDrops.blocks(r(activatorrail, 0.03), Blocks.ACTIVATOR_RAIL))
            register(RightClickDrops.blocks(r(ironbars, 0.1), Blocks.IRON_BARS))

            register(RightClickDrops.items(r(bread, 0.1), Items.BREAD))
            register(RightClickDrops.items(r(cookie, 0.1), Items.COOKIE))
            register(RightClickDrops.items(r(cake, 0.03), Items.CAKE))
            register(RightClickDrops.ingredients(r(enchantedgoldenapple, 0.003), Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 1))))
            register(RightClickDrops.items(r(sugar, 0.3), Items.SUGAR))
            register(RightClickDrops.items(r(rottenflesh, 0.1), Items.ROTTEN_FLESH))
            register(RightClickDrops.items(r(bakedpotato, 0.03), Items.BAKED_POTATO))
            register(RightClickDrops.items(r(cookedchicken, 0.1), Items.COOKED_CHICKEN))
            register(RightClickDrops.ingredients(r(cookedsalmon, 0.03), Ingredient.fromStacks(ItemStack(Items.COOKED_FISH, 1, 1))))
            register(RightClickDrops.items(r(steak, 0.1), Items.COOKED_BEEF))
            register(RightClickDrops.ingredients(r(goldenapple, 0.03), Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 0))))

            daytime(0.001).world { time(world, 6000, 18000) }
            night(0.001).world { time(world, 19000, 24000) || time(world, 0, 5000) }
            morning(0.001).world { time(world, 5000, 9000) }
            fine(0.01).world { world.provider.isSurfaceWorld && world.canSeeSky(blockPos) && !world.isRainingAt(blockPos) }
            rain(0.01).world { world.provider.isSurfaceWorld && world.canSeeSky(blockPos) && world.isRainingAt(blockPos) }

            plains(0.01).biomeType(BiomeDictionary.Type.PLAINS)
            forest(0.01).biomeType(BiomeDictionary.Type.FOREST)
            ocean(0.01).biomeType(BiomeDictionary.Type.OCEAN)
            taiga(0.01).biomeType(BiomeDictionary.Type.CONIFEROUS)
            desert(0.01).biomeType(BiomeDictionary.Type.SANDY)
            mountain(0.01).biomeType(BiomeDictionary.Type.MOUNTAIN)

            register(RightClickDrops.ingredients(r(fortune, 0.01), Predicate { itemStack: ItemStack? -> EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack) > 0 }))

            register(RightClickDrops.fixed(r(time, 0.0001)))
        }

    }
}
