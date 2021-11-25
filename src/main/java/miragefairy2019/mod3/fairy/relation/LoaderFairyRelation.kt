package miragefairy2019.mod3.fairy

import miragefairy2019.libkt.Module
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.fairy.registry.ApiFairyRegistry
import miragefairy2019.mod3.fairy.relation.api.ApiFairyRelation
import miragefairy2019.mod3.fairy.relation.api.FairyDropEntry
import miragefairy2019.mod3.fairy.relation.api.FairyRelationEntry
import miragefairy2019.mod3.fairy.relation.api.IFairyRelationHandler
import net.minecraft.block.Block
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
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraftforge.oredict.OreIngredient

val loaderFairyRelation: Module = {
    onCreateItemStack {
        fun i(item: Item, meta: Int = 32767) = Ingredient.fromStacks(ItemStack(item, 1, meta))
        fun i(block: Block, meta: Int = 32767) = Ingredient.fromStacks(ItemStack(Item.getItemFromBlock(block), 1, meta))
        fun i(itemStack: ItemStack) = Ingredient.fromStacks(itemStack)
        fun i(ore: String) = OreIngredient(ore)

        // 妖精関係レジストリー
        // TODO ほとんどの妖精とアイテムの関連付けは妖精レジストリーを使う
        FairyTypes.instance.run {
            operator fun RankedFairyTypeBundle.invoke(ingredient: Ingredient) {
                ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(ingredient, this.main.type.breed)
            }

            diamond(i("blockDiamond"))
            emerald(i("blockEmerald"))
            pyrope(i("blockPyrope"))
            moonstone(i("blockMoonstone"))
            apatite(i("blockApatite"))
            obsidian(i("obsidian"))
            fluorite(i("blockFluorite"))
            cinnabar(i("blockCinnabar"))
            magnetite(i("blockMagnetite"))
            glowstone(i("glowstone"))
            smithsonite(i("blockSmithsonite"))
            lapislazuli(i("blockLapis"))
            sulfur(i("blockSulfur"))
            gold(i("blockGold"))
            redstone(i("blockRedstone"))
            sand(i(ItemStack(Blocks.SAND)))
            nephrite(i("blockNephrite"))
            tourmaline(i("blockTourmaline"))
            topaz(i("blockTopaz"))
        }

        // 妖精の関係を登録
        FairyTypes.instance.run {
            operator fun RankedFairyTypeBundle.invoke(relevance: Double, vararg ingredients: Ingredient) = ingredients.forEach {
                ApiFairy.fairyRelationRegistry.registerIngredientFairyRelation(relevance, main.createItemStack(), it)
            }

            stone(1.0, i(Blocks.STONE, 0))
            dirt(1.0, i(Blocks.DIRT, 0))
            iron(1.0, i("ingotIron"))
            diamond(1.0, i("gemDiamond"))
            magnetite(1.0, i("gemMagnetite"))
            apatite(1.0, i("gemApatite"))
            fluorite(1.0, i("gemFluorite"))
            sulfur(1.0, i("gemSulfur"))
            cinnabar(1.0, i("gemCinnabar"))
            moonstone(1.0, i("gemMoonstone"))
            pyrope(1.0, i("gemPyrope"))
            smithsonite(1.0, i("gemSmithsonite"))
            redstone(1.0, i("dustRedstone"))
            sand(1.0, i(Blocks.SAND))
            gold(1.0, i("ingotGold"))
            wheat(1.0, i(Items.WHEAT))
            lilac(1.0, i(Blocks.DOUBLE_PLANT, 1))
            torch(1.0, i(Blocks.TORCH))
            gravel(1.0, i(Blocks.GRAVEL))
            emerald(1.0, i("gemEmerald"))
            lapislazuli(1.0, i("gemLapislazuli"))
            furnace(1.0, i(Blocks.FURNACE))
            magentaglazedterracotta(1.0, i(Blocks.MAGENTA_GLAZED_TERRACOTTA))
            bread(1.0, i(Items.BREAD))
            apple(1.0, i(Items.APPLE))
            carrot(1.0, i(Items.CARROT))
            cactus(1.0, i(Blocks.CACTUS))
            axe(1.0, i(Items.IRON_AXE))
            chest(1.0, i(Blocks.CHEST))
            craftingtable(1.0, i(Blocks.CRAFTING_TABLE))
            potion(1.0, i(Items.POTIONITEM))
            sword(1.0, i(Items.IRON_SWORD))
            dispenser(1.0, i(Blocks.DISPENSER))
            cod(1.0, i(Items.FISH, 0))
            salmon(1.0, i(Items.FISH, 1))
            pufferfish(1.0, i(Items.FISH, 3))
            clownfish(1.0, i(Items.FISH, 2))
            spruce(1.0, i(Blocks.LOG, 1), i(Blocks.SAPLING, 1))
            anvil(1.0, i(Blocks.ANVIL))
            obsidian(1.0, i(Blocks.OBSIDIAN))
            seed(1.0, i(Items.WHEAT_SEEDS))
            glowstone(1.0, i(Items.GLOWSTONE_DUST), i(Blocks.GLOWSTONE))
            coal(1.0, i(Items.COAL, 0))
            netherstar(1.0, i(Items.NETHER_STAR))
            brewingstand(1.0, i(Items.BREWING_STAND))
            hoe(1.0, i(Items.IRON_HOE))
            shield(1.0, i(Items.SHIELD))
            hopper(1.0, i(Blocks.HOPPER))
            nephrite(1.0, i("gemNephrite"))
            tourmaline(1.0, i("gemTourmaline"))
            topaz(1.0, i("gemTopaz"))
            cookie(1.0, i(Items.COOKIE))
            cake(1.0, i(Items.CAKE))
            enchantedgoldenapple(1.0, i(Items.GOLDEN_APPLE, 1))
            sugar(1.0, i(Items.SUGAR))
            rottenflesh(1.0, i(Items.ROTTEN_FLESH))
            poisonouspotato(1.0, i(Items.POISONOUS_POTATO))
            melon(1.0, i(Items.MELON))
            bakedpotato(1.0, i(Items.BAKED_POTATO))
            cookedchicken(1.0, i(Items.COOKED_CHICKEN))
            cookedsalmon(1.0, i(Items.COOKED_FISH, 1))
            steak(1.0, i(Items.COOKED_BEEF))
            goldenapple(1.0, i(Items.GOLDEN_APPLE, 0))
            spidereye(1.0, i(Items.SPIDER_EYE))
            beetroot(1.0, i(Items.BEETROOT))
            pumpkinpie(1.0, i(Items.PUMPKIN_PIE))
            beetrootsoup(1.0, i(Items.BEETROOT_SOUP))
            eleven(1.0, i(Items.RECORD_11))
        }

        // エンティティ関係を登録
        FairyTypes.instance.run {
            operator fun <E : Entity> RankedFairyTypeBundle.invoke(relevance: Double, clazz: Class<E>) {
                ApiFairyRelation.fairyRelationManager.entityClassRelationRegistry.add(FairyRelationEntry(clazz, FairyDropEntry(relevance, main.createItemStack())))
            }

            operator fun <E : Entity> RankedFairyTypeBundle.invoke(relevance: Double, clazz: Class<E>, predicate: E.() -> Boolean) {
                ApiFairyRelation.fairyRelationManager.entityRelationRegistry += IFairyRelationHandler {
                    @Suppress("UNCHECKED_CAST")
                    if (clazz.isInstance(it) && predicate(it as E)) FairyDropEntry(relevance, main.createItemStack()) else null
                }
            }

            enderman(1.0, EntityEnderman::class.java)
            spider(1.0, EntitySpider::class.java)
            enderdragon(1.0, EntityDragon::class.java)
            chicken(1.0, EntityChicken::class.java)
            skeleton(1.0, EntitySkeleton::class.java)
            zombie(1.0, EntityZombie::class.java)
            witherskeleton(1.0, EntityWitherSkeleton::class.java)
            wither(1.0, EntityWither::class.java)
            creeper(1.0, EntityCreeper::class.java)
            villager(1.0, EntityVillager::class.java)
            librarian(2.0, EntityVillager::class.java) { professionForge.registryName == ResourceLocation("minecraft:librarian") }
            golem(1.0, EntityIronGolem::class.java)
            cow(1.0, EntityCow::class.java)
            pig(1.0, EntityPig::class.java)
            shulker(1.0, EntityShulker::class.java)
            slime(1.0, EntitySlime::class.java)
            magmacube(2.0, EntityMagmaCube::class.java) // スライムのサブクラスのため
            blaze(1.0, EntityBlaze::class.java)
        }

    }
}