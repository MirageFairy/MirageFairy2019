package miragefairy2019.mod3.fairy

import miragefairy2019.libkt.Module
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.fairy.registry.ApiFairyRegistry
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
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.oredict.OreIngredient

class FairyRelationEntry<T>(
    private val fairySupplier: () -> RankedFairyTypeBundle,
    private val keySupplier: () -> T,
    /**
     * キーと妖精の概念的な近さ（関係性）を表す値です。
     * 1.0のときに標準的な関係性で、0.0のときに完全に無関係です。
     * この値は通常0.5、2.0、4.0のような2の整数乗の値を指定します。
     *
     * その中間は、例えば「『パン』と『食品という概念に対する妖精』」のような、妖精側が通常よりも抽象的である関係を表します。
     * これは、『パン』に対する『パンの妖精』の関係性が1.0であり、それよりも優先度を低くするためです。
     *
     * 逆に「『帯電したクリーパー』と『帯電したクリーパーの妖精』」のような、特定の状態を表す妖精は1よりも大きな数を設定します。
     * これは、『帯電したクリーパー』は『クリーパー』の一種であるため、『クリーパーの妖精』との関係性が1.0と計算されるためです。
     */
    val relevance: Double = 1.0,
    /**
     * キーの実現難易度に応じた妖精のドロップ率の係数です。
     * この値は、単純にキーに該当している状態を維持するのが困難な場合にバランス調整のために1よりも大きな値を設定してください。
     *
     * 例えば、『木の棒』はインベントリにただ置いておけばよいため、1.0です。
     * 一方『エンダーマン』のようなその場に長時間にわたって保持しておくのが困難な対象がキーの場合には1.0よりも大きな値を設定します。
     * 逆に『平原バイオーム』のような条件を回避する方が難しいキーの場合、低い値に設定します。
     */
    val weight: Double = 1.0
) {
    val fairy get() = fairySupplier()
    val key get() = keySupplier()
}

object FairyRelation {
    val module: Module = {

        // ローダー
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
                door(1.0, i(Items.OAK_DOOR))
                irondoor(1.0, i(Items.IRON_DOOR))
                redstonerepeater(1.0, i(Items.REPEATER))
                redstonecomparator(1.0, i(Items.COMPARATOR))
                chorusfruit(1.0, i(Items.CHORUS_FRUIT))
                mushroomstew(1.0, i(Items.MUSHROOM_STEW))
                rawrabbit(1.0, i(Items.RABBIT))
            }

        }

    }

    // TODO init関数形式にする
    val biomeType: List<FairyRelationEntry<BiomeDictionary.Type>> = listOf(
        FairyRelationEntry({ FairyTypes.instance.plains }, { BiomeDictionary.Type.PLAINS }, weight = 0.1),
        FairyRelationEntry({ FairyTypes.instance.forest }, { BiomeDictionary.Type.FOREST }, weight = 0.1),
        FairyRelationEntry({ FairyTypes.instance.ocean }, { BiomeDictionary.Type.OCEAN }, weight = 0.1),
        FairyRelationEntry({ FairyTypes.instance.taiga }, { BiomeDictionary.Type.CONIFEROUS }, weight = 0.1),
        FairyRelationEntry({ FairyTypes.instance.desert }, { BiomeDictionary.Type.SANDY }, weight = 0.1),
        FairyRelationEntry({ FairyTypes.instance.mountain }, { BiomeDictionary.Type.MOUNTAIN }, weight = 0.1)
    )

    private inline fun <reified E : Entity> entity(): (Entity) -> Boolean = { it is E }
    private inline fun <reified E : Entity> entity(crossinline predicate: E.() -> Boolean): (Entity) -> Boolean = { it is E && predicate(it) }
    val entity: List<FairyRelationEntry<(Entity) -> Boolean>> = listOf(
        // 長生きするエンティティ
        FairyRelationEntry({ FairyTypes.instance.chicken }, { entity<EntityChicken>() }, weight = 2.0),
        FairyRelationEntry({ FairyTypes.instance.cow }, { entity<EntityCow>() }, weight = 2.0),
        FairyRelationEntry({ FairyTypes.instance.pig }, { entity<EntityPig>() }, weight = 2.0),
        FairyRelationEntry({ FairyTypes.instance.villager }, { entity<EntityVillager>() }, weight = 2.0),
        FairyRelationEntry({ FairyTypes.instance.librarian }, { entity<EntityVillager> { professionForge.registryName == ResourceLocation("minecraft:librarian") } }, relevance = 2.0, weight = 2.0),
        FairyRelationEntry({ FairyTypes.instance.golem }, { entity<EntityIronGolem>() }, weight = 2.0),

        // 持続的に湧かせられるエンティティ
        FairyRelationEntry({ FairyTypes.instance.skeleton }, { entity<EntitySkeleton>() }, weight = 5.0),
        FairyRelationEntry({ FairyTypes.instance.zombie }, { entity<EntityZombie>() }, weight = 5.0),
        FairyRelationEntry({ FairyTypes.instance.spider }, { entity<EntitySpider>() }, weight = 5.0),
        FairyRelationEntry({ FairyTypes.instance.blaze }, { entity<EntityBlaze>() }, weight = 5.0),
        FairyRelationEntry({ FairyTypes.instance.enderman }, { entity<EntityEnderman>() }, weight = 5.0),

        // 滅多に会えないエンティティ
        FairyRelationEntry({ FairyTypes.instance.creeper }, { entity<EntityCreeper>() }, weight = 10.0),
        FairyRelationEntry({ FairyTypes.instance.slime }, { entity<EntitySlime>() }, weight = 10.0),
        FairyRelationEntry({ FairyTypes.instance.magmacube }, { entity<EntityMagmaCube>() }, relevance = 2.0 /* スライムのサブクラスのため */, weight = 10.0),
        FairyRelationEntry({ FairyTypes.instance.witherskeleton }, { entity<EntityWitherSkeleton>() }, weight = 10.0),
        FairyRelationEntry({ FairyTypes.instance.shulker }, { entity<EntityShulker>() }, weight = 10.0),
        FairyRelationEntry({ FairyTypes.instance.wither }, { entity<EntityWither>() }, weight = 10.0),

        // 滅多に地上に降りてこないエンティティ
        FairyRelationEntry({ FairyTypes.instance.enderdragon }, { entity<EntityDragon>() }, weight = 20.0)
    )
}
