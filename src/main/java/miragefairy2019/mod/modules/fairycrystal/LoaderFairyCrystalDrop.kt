package miragefairy2019.mod.modules.fairycrystal

import miragefairy2019.libkt.Module
import miragefairy2019.mod.api.ApiFairyCrystal
import miragefairy2019.mod.api.fairycrystal.DropFixed
import miragefairy2019.mod.api.fairycrystal.RightClickDrops
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.modules.fairy.FairyTypes
import miragefairy2019.mod.modules.fairy.VariantFairy
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
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.function.Predicate

val loaderFairyCrystalDrop: Module = {
    onCreateItemStack {
        fun m(material: String) = listOf("ingot", "nugget", "gem", "dust", "dustTiny", "block", "rod", "plate", "ore").map { "$it$material" }.toTypedArray()
        fun r(variantFairy: VariantFairy) = DropFixed(variantFairy.createItemStack(), 0.1 * Math.pow(0.1, (variantFairy.rare - 1).toDouble()))
        fun d(variantFairy: VariantFairy, weight: Double) = DropFixed(variantFairy.createItemStack(), weight)
        fun time(world: World, min: Int, max: Int) = world.provider.isSurfaceWorld && min <= (world.worldTime + 6000) % 24000 && (world.worldTime + 6000) % 24000 <= max

        val d = ApiFairyCrystal.dropsFairyCrystal

        // コモン
        run {
            d.add(RightClickDrops.world(r(FairyTypes.instance.water.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.stone.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.dirt.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.sand.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.gravel.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.iron.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.gold.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.diamond.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.emerald.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.magnetite.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.apatite.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.fluorite.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.sulfur.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.cinnabar.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.moonstone.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.pyrope.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.smithsonite.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.redstone.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.lapislazuli.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.obsidian.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.coal.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.ice.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.nephrite.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.tourmaline.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.topaz.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })

            d.add(RightClickDrops.world(r(FairyTypes.instance.spider.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.chicken.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.skeleton.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.zombie.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.creeper.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.fish.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.cod.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.salmon.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.pufferfish.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.clownfish.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.villager.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.cow.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.pig.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })

            d.add(RightClickDrops.world(r(FairyTypes.instance.wheat.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.lilac.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.apple.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.carrot.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.cactus.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.spruce.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.seed.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.poisonouspotato.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
            d.add(RightClickDrops.world(r(FairyTypes.instance.melon.main)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld })
        }
        run {
            d.add(RightClickDrops.biomeTypes(r(FairyTypes.instance.lava.main), BiomeDictionary.Type.NETHER))
            d.add(RightClickDrops.biomeTypes(r(FairyTypes.instance.fire.main), BiomeDictionary.Type.NETHER))

            d.add(RightClickDrops.biomeTypes(r(FairyTypes.instance.glowstone.main), BiomeDictionary.Type.NETHER))
        }
        run {
            d.add(RightClickDrops.biomeTypes(r(FairyTypes.instance.enderman.main), BiomeDictionary.Type.END))
            d.add(RightClickDrops.biomeTypes(r(FairyTypes.instance.enderdragon.main), BiomeDictionary.Type.END))
        }

        // 限定高確率ドロップ
        run {
            d.add(RightClickDrops.fixed(d(FairyTypes.instance.air.main, 1.0)))

            d.add(RightClickDrops.blocks(d(FairyTypes.instance.water.main, 0.3), Blocks.WATER, Blocks.FLOWING_WATER))
            d.add(RightClickDrops.items(d(FairyTypes.instance.water.main, 0.3), Items.WATER_BUCKET))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.lava.main, 0.1), Blocks.LAVA, Blocks.FLOWING_LAVA))
            d.add(RightClickDrops.items(d(FairyTypes.instance.lava.main, 0.3), Items.LAVA_BUCKET))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.fire.main, 0.1), Blocks.FIRE))

            d.add(RightClickDrops.world(d(FairyTypes.instance.thunder.main, 0.01)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld && w.canSeeSky(p) && w.isRainingAt(p) && w.isThundering })
            d.add(RightClickDrops.world(d(FairyTypes.instance.sun.main, 0.0001)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld && w.canSeeSky(p) && time(w, 6000, 18000) && !w.isRainingAt(p) })
            d.add(RightClickDrops.world(d(FairyTypes.instance.moon.main, 0.0001)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld && w.canSeeSky(p) && (time(w, 19000, 24000) || time(w, 0, 5000)) && !w.isRainingAt(p) })
            d.add(RightClickDrops.world(d(FairyTypes.instance.star.main, 0.0003)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld && w.canSeeSky(p) && (time(w, 19000, 24000) || time(w, 0, 5000)) && !w.isRainingAt(p) })

            d.add(RightClickDrops.blocks(d(FairyTypes.instance.stone.main, 0.3), Blocks.STONE, Blocks.COBBLESTONE))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.dirt.main, 0.3), Blocks.DIRT, Blocks.GRASS))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.sand.main, 0.3), Blocks.SAND, Blocks.SANDSTONE, Blocks.RED_SANDSTONE))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.gravel.main, 0.1), Blocks.GRAVEL))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.iron.main, 0.1), *m("Iron")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.gold.main, 0.03), *m("Gold")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.diamond.main, 0.01), *m("Diamond")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.emerald.main, 0.03), *m("Emerald")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.magnetite.main, 0.1), *m("Magnetite")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.apatite.main, 0.03), *m("Apatite")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.fluorite.main, 0.01), *m("Fluorite")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.sulfur.main, 0.03), *m("Sulfur")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.cinnabar.main, 0.01), *m("Cinnabar")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.moonstone.main, 0.003), *m("Moonstone")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.pyrope.main, 0.01), *m("Pyrope")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.smithsonite.main, 0.1), *m("Smithsonite")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.redstone.main, 0.1), *m("Redstone")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.lapislazuli.main, 0.1), *m("Lapis")))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.obsidian.main, 0.1), Blocks.OBSIDIAN))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.glowstone.main, 0.1), *m("Glowstone")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.glowstone.main, 0.1), "glowstone"))
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.coal.main, 0.1), Ingredient.fromStacks(ItemStack(Items.COAL))))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.coal.main, 0.1), *m("Coal")))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.ice.main, 0.3), Blocks.ICE))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.packedice.main, 0.01), Blocks.PACKED_ICE))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.nephrite.main, 0.03), *m("Nephrite")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.tourmaline.main, 0.01), *m("Tourmaline")))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.topaz.main, 0.01), *m("Topaz")))

            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.enderman.main, 0.03), EntityEnderman::class.java))
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.spider.main, 0.1), EntitySpider::class.java))
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.enderdragon.main, 0.1), EntityDragon::class.java))
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.chicken.main, 0.1), EntityChicken::class.java))
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.skeleton.main, 0.3), EntitySkeleton::class.java))
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.zombie.main, 0.3), EntityZombie::class.java))
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.witherskeleton.main, 0.03), EntityWitherSkeleton::class.java))
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.wither.main, 0.01), EntityWither::class.java))
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.creeper.main, 0.1), EntityCreeper::class.java))
            d.add(RightClickDrops.items(d(FairyTypes.instance.fish.main, 0.3), Items.FISH))
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.cod.main, 0.1), Ingredient.fromStacks(ItemStack(Items.FISH, 1, 0))))
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.salmon.main, 0.1), Ingredient.fromStacks(ItemStack(Items.FISH, 1, 1))))
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.pufferfish.main, 0.03), Ingredient.fromStacks(ItemStack(Items.FISH, 1, 3))))
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.clownfish.main, 0.03), Ingredient.fromStacks(ItemStack(Items.FISH, 1, 2))))
            d.add(RightClickDrops.entity(d(FairyTypes.instance.villager.main, 0.3), EntityVillager::class.java) { e: EntityVillager? -> true })
            d.add(RightClickDrops.entity(d(FairyTypes.instance.librarian.main, 0.1), EntityVillager::class.java) { e: EntityVillager -> e.professionForge.registryName == ResourceLocation("minecraft:librarian") })
            d.add(RightClickDrops.items(d(FairyTypes.instance.netherstar.main, 0.01), Items.NETHER_STAR))
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.golem.main, 0.1), EntityIronGolem::class.java))
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.cow.main, 0.1), EntityCow::class.java))
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.pig.main, 0.1), EntityPig::class.java))

            d.add(RightClickDrops.blocks(d(FairyTypes.instance.wheat.main, 0.1), Blocks.WHEAT, Blocks.HAY_BLOCK))
            d.add(RightClickDrops.blockStates(d(FairyTypes.instance.lilac.main, 0.03), Blocks.DOUBLE_PLANT.defaultState.withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SYRINGA)))
            d.add(RightClickDrops.items(d(FairyTypes.instance.apple.main, 0.1), Items.APPLE, Items.GOLDEN_APPLE))
            d.add(RightClickDrops.items(d(FairyTypes.instance.carrot.main, 0.03), Items.CARROT, Items.CARROT_ON_A_STICK, Items.GOLDEN_CARROT))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.cactus.main, 0.1), Blocks.CACTUS))
            d.add(RightClickDrops.blockStates(d(FairyTypes.instance.spruce.main, 0.1),
                    Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE),
                    Blocks.PLANKS.defaultState.withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE),
                    Blocks.LEAVES.defaultState.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)))
            d.add(RightClickDrops.items(d(FairyTypes.instance.seed.main, 0.1), Items.WHEAT_SEEDS))
            d.add(RightClickDrops.items(d(FairyTypes.instance.poisonouspotato.main, 0.01), Items.POISONOUS_POTATO))
            d.add(RightClickDrops.items(d(FairyTypes.instance.melon.main, 0.03), Items.MELON))

            d.add(RightClickDrops.blocks(d(FairyTypes.instance.torch.main, 0.3), Blocks.TORCH))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.furnace.main, 0.1), Blocks.FURNACE))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.magentaglazedterracotta.main, 0.03), Blocks.MAGENTA_GLAZED_TERRACOTTA))
            d.add(RightClickDrops.items(d(FairyTypes.instance.axe.main, 0.1), Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.chest.main, 0.1), Blocks.CHEST))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.craftingtable.main, 0.1), Blocks.CRAFTING_TABLE))
            d.add(RightClickDrops.items(d(FairyTypes.instance.potion.main, 0.1), Items.POTIONITEM, Items.LINGERING_POTION, Items.SPLASH_POTION))
            d.add(RightClickDrops.items(d(FairyTypes.instance.sword.main, 0.1), Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.dispenser.main, 0.1), Blocks.DISPENSER))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.anvil.main, 0.1), Blocks.ANVIL))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.enchant.main, 0.03), Blocks.ENCHANTING_TABLE))
            d.add(RightClickDrops.items(d(FairyTypes.instance.enchant.main, 0.03), Items.ENCHANTED_BOOK))
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.enchant.main, 0.03), Predicate { itemStack: ItemStack -> itemStack.isItemEnchanted }))
            d.add(RightClickDrops.items(d(FairyTypes.instance.brewingstand.main, 0.03), Items.BREWING_STAND))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.brewingstand.main, 0.03), Blocks.BREWING_STAND))
            d.add(RightClickDrops.items(d(FairyTypes.instance.hoe.main, 0.3), Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE))
            d.add(RightClickDrops.items(d(FairyTypes.instance.shield.main, 0.1), Items.SHIELD))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.hopper.main, 0.1), Blocks.HOPPER))
            d.add(RightClickDrops.ores(d(FairyTypes.instance.glass.main, 0.1), "blockGlass"))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.activatorrail.main, 0.03), Blocks.ACTIVATOR_RAIL))
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.ironbars.main, 0.1), Blocks.IRON_BARS))

            d.add(RightClickDrops.items(d(FairyTypes.instance.bread.main, 0.1), Items.BREAD))
            d.add(RightClickDrops.items(d(FairyTypes.instance.cookie.main, 0.1), Items.COOKIE))
            d.add(RightClickDrops.items(d(FairyTypes.instance.cake.main, 0.03), Items.CAKE))
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.enchantedgoldenapple.main, 0.003), Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 1))))
            d.add(RightClickDrops.items(d(FairyTypes.instance.sugar.main, 0.3), Items.SUGAR))
            if (false) {
                d.add(RightClickDrops.items(d(FairyTypes.instance.darkchocolate.main, 0.001), Items.COOKIE))
                d.add(RightClickDrops.ingredients(d(FairyTypes.instance.darkchocolate.main, 0.001), Ingredient.fromStacks(ItemStack(Items.DYE, 1, 3))))
            }
            d.add(RightClickDrops.items(d(FairyTypes.instance.rottenflesh.main, 0.1), Items.ROTTEN_FLESH))
            d.add(RightClickDrops.items(d(FairyTypes.instance.bakedpotato.main, 0.03), Items.BAKED_POTATO))
            d.add(RightClickDrops.items(d(FairyTypes.instance.cookedchicken.main, 0.1), Items.COOKED_CHICKEN))
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.cookedsalmon.main, 0.03), Ingredient.fromStacks(ItemStack(Items.COOKED_FISH, 1, 1))))
            d.add(RightClickDrops.items(d(FairyTypes.instance.steak.main, 0.1), Items.COOKED_BEEF))
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.goldenapple.main, 0.03), Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 0))))

            d.add(RightClickDrops.world(d(FairyTypes.instance.daytime.main, 0.001)) { w: World, p: BlockPos? -> time(w, 6000, 18000) })
            d.add(RightClickDrops.world(d(FairyTypes.instance.night.main, 0.001)) { w: World, p: BlockPos? -> time(w, 19000, 24000) || time(w, 0, 5000) })
            d.add(RightClickDrops.world(d(FairyTypes.instance.morning.main, 0.001)) { w: World, p: BlockPos? -> time(w, 5000, 9000) })
            d.add(RightClickDrops.world(d(FairyTypes.instance.fine.main, 0.01)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld && w.canSeeSky(p) && !w.isRainingAt(p) })
            d.add(RightClickDrops.world(d(FairyTypes.instance.rain.main, 0.01)) { w: World, p: BlockPos? -> w.provider.isSurfaceWorld && w.canSeeSky(p) && w.isRainingAt(p) })

            d.add(RightClickDrops.biomeTypes(d(FairyTypes.instance.plains.main, 0.01), BiomeDictionary.Type.PLAINS))
            d.add(RightClickDrops.biomeTypes(d(FairyTypes.instance.forest.main, 0.01), BiomeDictionary.Type.FOREST))
            d.add(RightClickDrops.biomeTypes(d(FairyTypes.instance.ocean.main, 0.01), BiomeDictionary.Type.OCEAN))
            d.add(RightClickDrops.biomeTypes(d(FairyTypes.instance.taiga.main, 0.01), BiomeDictionary.Type.CONIFEROUS))
            d.add(RightClickDrops.biomeTypes(d(FairyTypes.instance.desert.main, 0.01), BiomeDictionary.Type.SANDY))
            d.add(RightClickDrops.biomeTypes(d(FairyTypes.instance.mountain.main, 0.01), BiomeDictionary.Type.MOUNTAIN))

            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.fortune.main, 0.01), Predicate { itemStack: ItemStack? -> EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack) > 0 }))

            d.add(RightClickDrops.fixed(d(FairyTypes.instance.time.main, 0.0001)))
            if (true) {
                ApiMain.logger().info("Limited Fairy: cupid")
                val epochSecondNow = Instant.now().epochSecond
                val epochSecondLimit = LocalDateTime.of(2021, 7, 1, 0, 0, 0).toInstant(ZoneOffset.ofHours(9)).epochSecond
                ApiMain.logger().info("Now  : $epochSecondNow")
                ApiMain.logger().info("Limit: $epochSecondLimit")
                if (epochSecondNow < epochSecondLimit) {
                    d.add(RightClickDrops.fixed(d(FairyTypes.instance.cupid.main, 0.001)))
                }
            }

        }

    }
}
