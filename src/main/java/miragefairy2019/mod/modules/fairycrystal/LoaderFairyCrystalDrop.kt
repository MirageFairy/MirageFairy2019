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
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import java.util.function.Predicate

val loaderFairyCrystalDrop: Module = {
    onCreateItemStack {
        fun m(material: String) = listOf("ingot", "nugget", "gem", "dust", "dustTiny", "block", "rod", "plate", "ore").map { "$it$material" }.toTypedArray()
        fun r(fairy: RankedFairyTypeBundle) = DropFixed(fairy.main.createItemStack(), 0.1 * Math.pow(0.1, (fairy.main.rare - 1).toDouble()))
        fun d(fairy: RankedFairyTypeBundle, weight: Double) = DropFixed(fairy.main.createItemStack(), weight)
        fun time(world: World, min: Int, max: Int) = world.provider.isSurfaceWorld && min <= (world.worldTime + 6000) % 24000 && (world.worldTime + 6000) % 24000 <= max

        fun register(listener: IRightClickDrop) {
            ApiFairyCrystal.dropsFairyCrystal.add(listener)
        }

        // コモン
        FairyTypes.instance.run {
            register(RightClickDrops.world(r(water)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(stone)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(dirt)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(sand)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(gravel)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(iron)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(gold)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(diamond)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(emerald)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(magnetite)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(apatite)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(fluorite)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(sulfur)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(cinnabar)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(moonstone)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(pyrope)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(smithsonite)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(redstone)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(lapislazuli)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(obsidian)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(coal)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(ice)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(nephrite)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(tourmaline)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(topaz)) { w, p -> w.provider.isSurfaceWorld })

            register(RightClickDrops.world(r(spider)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(chicken)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(skeleton)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(zombie)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(creeper)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(fish)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(cod)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(salmon)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(pufferfish)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(clownfish)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(villager)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(cow)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(pig)) { w, p -> w.provider.isSurfaceWorld })

            register(RightClickDrops.world(r(wheat)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(lilac)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(apple)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(carrot)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(cactus)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(spruce)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(seed)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(poisonouspotato)) { w, p -> w.provider.isSurfaceWorld })
            register(RightClickDrops.world(r(melon)) { w, p -> w.provider.isSurfaceWorld })
        }
        FairyTypes.instance.run {
            register(RightClickDrops.biomeTypes(r(lava), BiomeDictionary.Type.NETHER))
            register(RightClickDrops.biomeTypes(r(fire), BiomeDictionary.Type.NETHER))

            register(RightClickDrops.biomeTypes(r(glowstone), BiomeDictionary.Type.NETHER))
        }
        FairyTypes.instance.run {
            register(RightClickDrops.biomeTypes(r(enderman), BiomeDictionary.Type.END))
            register(RightClickDrops.biomeTypes(r(enderdragon), BiomeDictionary.Type.END))
        }

        // 限定高確率ドロップ
        FairyTypes.instance.run {
            register(RightClickDrops.fixed(d(air, 1.0)))

            register(RightClickDrops.blocks(d(water, 0.3), Blocks.WATER, Blocks.FLOWING_WATER))
            register(RightClickDrops.items(d(water, 0.3), Items.WATER_BUCKET))
            register(RightClickDrops.blocks(d(lava, 0.1), Blocks.LAVA, Blocks.FLOWING_LAVA))
            register(RightClickDrops.items(d(lava, 0.3), Items.LAVA_BUCKET))
            register(RightClickDrops.blocks(d(fire, 0.1), Blocks.FIRE))

            register(RightClickDrops.world(d(thunder, 0.01)) { w, p -> w.provider.isSurfaceWorld && w.canSeeSky(p) && w.isRainingAt(p) && w.isThundering })
            register(RightClickDrops.world(d(sun, 0.0001)) { w, p -> w.provider.isSurfaceWorld && w.canSeeSky(p) && time(w, 6000, 18000) && !w.isRainingAt(p) })
            register(RightClickDrops.world(d(moon, 0.0001)) { w, p -> w.provider.isSurfaceWorld && w.canSeeSky(p) && (time(w, 19000, 24000) || time(w, 0, 5000)) && !w.isRainingAt(p) })
            register(RightClickDrops.world(d(star, 0.0003)) { w, p -> w.provider.isSurfaceWorld && w.canSeeSky(p) && (time(w, 19000, 24000) || time(w, 0, 5000)) && !w.isRainingAt(p) })

            register(RightClickDrops.blocks(d(stone, 0.3), Blocks.STONE, Blocks.COBBLESTONE))
            register(RightClickDrops.blocks(d(dirt, 0.3), Blocks.DIRT, Blocks.GRASS))
            register(RightClickDrops.blocks(d(sand, 0.3), Blocks.SAND, Blocks.SANDSTONE, Blocks.RED_SANDSTONE))
            register(RightClickDrops.blocks(d(gravel, 0.1), Blocks.GRAVEL))
            register(RightClickDrops.ores(d(iron, 0.1), *m("Iron")))
            register(RightClickDrops.ores(d(gold, 0.03), *m("Gold")))
            register(RightClickDrops.ores(d(diamond, 0.01), *m("Diamond")))
            register(RightClickDrops.ores(d(emerald, 0.03), *m("Emerald")))
            register(RightClickDrops.ores(d(magnetite, 0.1), *m("Magnetite")))
            register(RightClickDrops.ores(d(apatite, 0.03), *m("Apatite")))
            register(RightClickDrops.ores(d(fluorite, 0.01), *m("Fluorite")))
            register(RightClickDrops.ores(d(sulfur, 0.03), *m("Sulfur")))
            register(RightClickDrops.ores(d(cinnabar, 0.01), *m("Cinnabar")))
            register(RightClickDrops.ores(d(moonstone, 0.003), *m("Moonstone")))
            register(RightClickDrops.ores(d(pyrope, 0.01), *m("Pyrope")))
            register(RightClickDrops.ores(d(smithsonite, 0.1), *m("Smithsonite")))
            register(RightClickDrops.ores(d(redstone, 0.1), *m("Redstone")))
            register(RightClickDrops.ores(d(lapislazuli, 0.1), *m("Lapis")))
            register(RightClickDrops.blocks(d(obsidian, 0.1), Blocks.OBSIDIAN))
            register(RightClickDrops.ores(d(glowstone, 0.1), *m("Glowstone")))
            register(RightClickDrops.ores(d(glowstone, 0.1), "glowstone"))
            register(RightClickDrops.ingredients(d(coal, 0.1), Ingredient.fromStacks(ItemStack(Items.COAL))))
            register(RightClickDrops.ores(d(coal, 0.1), *m("Coal")))
            register(RightClickDrops.blocks(d(ice, 0.3), Blocks.ICE))
            register(RightClickDrops.blocks(d(packedice, 0.01), Blocks.PACKED_ICE))
            register(RightClickDrops.ores(d(nephrite, 0.03), *m("Nephrite")))
            register(RightClickDrops.ores(d(tourmaline, 0.01), *m("Tourmaline")))
            register(RightClickDrops.ores(d(topaz, 0.01), *m("Topaz")))

            register(RightClickDrops.classEntities(d(enderman, 0.03), EntityEnderman::class.java))
            register(RightClickDrops.classEntities(d(spider, 0.1), EntitySpider::class.java))
            register(RightClickDrops.classEntities(d(enderdragon, 0.1), EntityDragon::class.java))
            register(RightClickDrops.classEntities(d(chicken, 0.1), EntityChicken::class.java))
            register(RightClickDrops.classEntities(d(skeleton, 0.3), EntitySkeleton::class.java))
            register(RightClickDrops.classEntities(d(zombie, 0.3), EntityZombie::class.java))
            register(RightClickDrops.classEntities(d(witherskeleton, 0.03), EntityWitherSkeleton::class.java))
            register(RightClickDrops.classEntities(d(wither, 0.01), EntityWither::class.java))
            register(RightClickDrops.classEntities(d(creeper, 0.1), EntityCreeper::class.java))
            register(RightClickDrops.items(d(fish, 0.3), Items.FISH))
            register(RightClickDrops.ingredients(d(cod, 0.1), Ingredient.fromStacks(ItemStack(Items.FISH, 1, 0))))
            register(RightClickDrops.ingredients(d(salmon, 0.1), Ingredient.fromStacks(ItemStack(Items.FISH, 1, 1))))
            register(RightClickDrops.ingredients(d(pufferfish, 0.03), Ingredient.fromStacks(ItemStack(Items.FISH, 1, 3))))
            register(RightClickDrops.ingredients(d(clownfish, 0.03), Ingredient.fromStacks(ItemStack(Items.FISH, 1, 2))))
            register(RightClickDrops.entity(d(villager, 0.3), EntityVillager::class.java) { e: EntityVillager? -> true })
            register(RightClickDrops.entity(d(librarian, 0.1), EntityVillager::class.java) { e: EntityVillager -> e.professionForge.registryName == ResourceLocation("minecraft:librarian") })
            register(RightClickDrops.items(d(netherstar, 0.01), Items.NETHER_STAR))
            register(RightClickDrops.classEntities(d(golem, 0.1), EntityIronGolem::class.java))
            register(RightClickDrops.classEntities(d(cow, 0.1), EntityCow::class.java))
            register(RightClickDrops.classEntities(d(pig, 0.1), EntityPig::class.java))

            register(RightClickDrops.blocks(d(wheat, 0.1), Blocks.WHEAT, Blocks.HAY_BLOCK))
            register(RightClickDrops.blockStates(d(lilac, 0.03), Blocks.DOUBLE_PLANT.defaultState.withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SYRINGA)))
            register(RightClickDrops.items(d(apple, 0.1), Items.APPLE, Items.GOLDEN_APPLE))
            register(RightClickDrops.items(d(carrot, 0.03), Items.CARROT, Items.CARROT_ON_A_STICK, Items.GOLDEN_CARROT))
            register(RightClickDrops.blocks(d(cactus, 0.1), Blocks.CACTUS))
            register(RightClickDrops.blockStates(d(spruce, 0.1),
                    Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE),
                    Blocks.PLANKS.defaultState.withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE),
                    Blocks.LEAVES.defaultState.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)))
            register(RightClickDrops.items(d(seed, 0.1), Items.WHEAT_SEEDS))
            register(RightClickDrops.items(d(poisonouspotato, 0.01), Items.POISONOUS_POTATO))
            register(RightClickDrops.items(d(melon, 0.03), Items.MELON))

            register(RightClickDrops.blocks(d(torch, 0.3), Blocks.TORCH))
            register(RightClickDrops.blocks(d(furnace, 0.1), Blocks.FURNACE))
            register(RightClickDrops.blocks(d(magentaglazedterracotta, 0.03), Blocks.MAGENTA_GLAZED_TERRACOTTA))
            register(RightClickDrops.items(d(axe, 0.1), Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE))
            register(RightClickDrops.blocks(d(chest, 0.1), Blocks.CHEST))
            register(RightClickDrops.blocks(d(craftingtable, 0.1), Blocks.CRAFTING_TABLE))
            register(RightClickDrops.items(d(potion, 0.1), Items.POTIONITEM, Items.LINGERING_POTION, Items.SPLASH_POTION))
            register(RightClickDrops.items(d(sword, 0.1), Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD))
            register(RightClickDrops.blocks(d(dispenser, 0.1), Blocks.DISPENSER))
            register(RightClickDrops.blocks(d(anvil, 0.1), Blocks.ANVIL))
            register(RightClickDrops.blocks(d(enchant, 0.03), Blocks.ENCHANTING_TABLE))
            register(RightClickDrops.items(d(enchant, 0.03), Items.ENCHANTED_BOOK))
            register(RightClickDrops.ingredients(d(enchant, 0.03), Predicate { itemStack: ItemStack -> itemStack.isItemEnchanted }))
            register(RightClickDrops.items(d(brewingstand, 0.03), Items.BREWING_STAND))
            register(RightClickDrops.blocks(d(brewingstand, 0.03), Blocks.BREWING_STAND))
            register(RightClickDrops.items(d(hoe, 0.3), Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE))
            register(RightClickDrops.items(d(shield, 0.1), Items.SHIELD))
            register(RightClickDrops.blocks(d(hopper, 0.1), Blocks.HOPPER))
            register(RightClickDrops.ores(d(glass, 0.1), "blockGlass"))
            register(RightClickDrops.blocks(d(activatorrail, 0.03), Blocks.ACTIVATOR_RAIL))
            register(RightClickDrops.blocks(d(ironbars, 0.1), Blocks.IRON_BARS))

            register(RightClickDrops.items(d(bread, 0.1), Items.BREAD))
            register(RightClickDrops.items(d(cookie, 0.1), Items.COOKIE))
            register(RightClickDrops.items(d(cake, 0.03), Items.CAKE))
            register(RightClickDrops.ingredients(d(enchantedgoldenapple, 0.003), Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 1))))
            register(RightClickDrops.items(d(sugar, 0.3), Items.SUGAR))
            register(RightClickDrops.items(d(rottenflesh, 0.1), Items.ROTTEN_FLESH))
            register(RightClickDrops.items(d(bakedpotato, 0.03), Items.BAKED_POTATO))
            register(RightClickDrops.items(d(cookedchicken, 0.1), Items.COOKED_CHICKEN))
            register(RightClickDrops.ingredients(d(cookedsalmon, 0.03), Ingredient.fromStacks(ItemStack(Items.COOKED_FISH, 1, 1))))
            register(RightClickDrops.items(d(steak, 0.1), Items.COOKED_BEEF))
            register(RightClickDrops.ingredients(d(goldenapple, 0.03), Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 0))))

            register(RightClickDrops.world(d(daytime, 0.001)) { w, p -> time(w, 6000, 18000) })
            register(RightClickDrops.world(d(night, 0.001)) { w, p -> time(w, 19000, 24000) || time(w, 0, 5000) })
            register(RightClickDrops.world(d(morning, 0.001)) { w, p -> time(w, 5000, 9000) })
            register(RightClickDrops.world(d(fine, 0.01)) { w, p -> w.provider.isSurfaceWorld && w.canSeeSky(p) && !w.isRainingAt(p) })
            register(RightClickDrops.world(d(rain, 0.01)) { w, p -> w.provider.isSurfaceWorld && w.canSeeSky(p) && w.isRainingAt(p) })

            register(RightClickDrops.biomeTypes(d(plains, 0.01), BiomeDictionary.Type.PLAINS))
            register(RightClickDrops.biomeTypes(d(forest, 0.01), BiomeDictionary.Type.FOREST))
            register(RightClickDrops.biomeTypes(d(ocean, 0.01), BiomeDictionary.Type.OCEAN))
            register(RightClickDrops.biomeTypes(d(taiga, 0.01), BiomeDictionary.Type.CONIFEROUS))
            register(RightClickDrops.biomeTypes(d(desert, 0.01), BiomeDictionary.Type.SANDY))
            register(RightClickDrops.biomeTypes(d(mountain, 0.01), BiomeDictionary.Type.MOUNTAIN))

            register(RightClickDrops.ingredients(d(fortune, 0.01), Predicate { itemStack: ItemStack? -> EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack) > 0 }))

            register(RightClickDrops.fixed(d(time, 0.0001)))
        }

    }
}
