package miragefairy2019.mod.fairy

import miragefairy2019.api.Erg
import miragefairy2019.api.ErgSet
import miragefairy2019.api.ManaSet
import miragefairy2019.lib.div
import miragefairy2019.lib.entries
import miragefairy2019.lib.max
import miragefairy2019.lib.sum
import miragefairy2019.lib.times
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.util.ResourceLocation
import kotlin.math.pow

@Suppress("unused")
class FairyCard(val variants: List<VariantFairy>) {
    companion object {
        fun values() = FairyTypes.instance.variants.map { it.bundle }

        val AIR get() = FairyTypes.instance.AIR
        val WATER get() = FairyTypes.instance.WATER
        val FIRE get() = FairyTypes.instance.FIRE
        val SUN get() = FairyTypes.instance.SUN
        val STONE get() = FairyTypes.instance.STONE
        val DIRT get() = FairyTypes.instance.DIRT
        val IRON get() = FairyTypes.instance.IRON
        val DIAMOND get() = FairyTypes.instance.DIAMOND
        val REDSTONE get() = FairyTypes.instance.REDSTONE
        val ENDERMAN get() = FairyTypes.instance.ENDERMAN
        val MOON get() = FairyTypes.instance.MOON
        val SAND get() = FairyTypes.instance.SAND
        val GOLD get() = FairyTypes.instance.GOLD
        val SPIDER get() = FairyTypes.instance.SPIDER
        val SKELETON get() = FairyTypes.instance.SKELETON
        val ZOMBIE get() = FairyTypes.instance.ZOMBIE
        val CREEPER get() = FairyTypes.instance.CREEPER
        val WHEAT get() = FairyTypes.instance.WHEAT
        val LILAC get() = FairyTypes.instance.LILAC
        val TORCH get() = FairyTypes.instance.TORCH
        val LAVA get() = FairyTypes.instance.LAVA
        val STAR get() = FairyTypes.instance.STAR
        val GRAVEL get() = FairyTypes.instance.GRAVEL
        val EMERALD get() = FairyTypes.instance.EMERALD
        val LAPISLAZULI get() = FairyTypes.instance.LAPISLAZULI
        val ENDER_DRAGON get() = FairyTypes.instance.ENDER_DRAGON
        val WITHER_SKELETON get() = FairyTypes.instance.WITHER_SKELETON
        val WITHER get() = FairyTypes.instance.WITHER
        val THUNDER get() = FairyTypes.instance.THUNDER
        val CHICKEN get() = FairyTypes.instance.CHICKEN
        val FURNACE get() = FairyTypes.instance.FURNACE
        val MAGENTA_GLAZED_TERRACOTTA get() = FairyTypes.instance.MAGENTA_GLAZED_TERRACOTTA
        val BREAD get() = FairyTypes.instance.BREAD
        val DAYTIME get() = FairyTypes.instance.DAYTIME
        val NIGHT get() = FairyTypes.instance.NIGHT
        val MORNING get() = FairyTypes.instance.MORNING
        val FINE get() = FairyTypes.instance.FINE
        val RAIN get() = FairyTypes.instance.RAIN
        val PLAINS get() = FairyTypes.instance.PLAINS
        val FOREST get() = FairyTypes.instance.FOREST
        val APPLE get() = FairyTypes.instance.APPLE
        val CARROT get() = FairyTypes.instance.CARROT
        val CACTUS get() = FairyTypes.instance.CACTUS
        val AXE get() = FairyTypes.instance.AXE
        val CHEST get() = FairyTypes.instance.CHEST
        val CRAFTING_TABLE get() = FairyTypes.instance.CRAFTING_TABLE
        val POTION get() = FairyTypes.instance.POTION
        val SWORD get() = FairyTypes.instance.SWORD
        val DISPENSER get() = FairyTypes.instance.DISPENSER
        val OCEAN get() = FairyTypes.instance.OCEAN
        val FISH get() = FairyTypes.instance.FISH
        val COD get() = FairyTypes.instance.COD
        val SALMON get() = FairyTypes.instance.SALMON
        val PUFFERFISH get() = FairyTypes.instance.PUFFERFISH
        val CLOWNFISH get() = FairyTypes.instance.CLOWNFISH
        val SPRUCE get() = FairyTypes.instance.SPRUCE
        val ANVIL get() = FairyTypes.instance.ANVIL
        val OBSIDIAN get() = FairyTypes.instance.OBSIDIAN
        val SEED get() = FairyTypes.instance.SEED
        val ENCHANT get() = FairyTypes.instance.ENCHANT
        val GLOWSTONE get() = FairyTypes.instance.GLOWSTONE
        val COAL get() = FairyTypes.instance.COAL
        val VILLAGER get() = FairyTypes.instance.VILLAGER
        val LIBRARIAN get() = FairyTypes.instance.LIBRARIAN
        val NETHER_STAR get() = FairyTypes.instance.NETHER_STAR
        val BREWING_STAND get() = FairyTypes.instance.BREWING_STAND
        val HOE get() = FairyTypes.instance.HOE
        val SHIELD get() = FairyTypes.instance.SHIELD
        val HOPPER get() = FairyTypes.instance.HOPPER
        val MINA get() = FairyTypes.instance.MINA
        val MAGNETITE get() = FairyTypes.instance.MAGNETITE
        val SULFUR get() = FairyTypes.instance.SULFUR
        val APATITE get() = FairyTypes.instance.APATITE
        val CINNABAR get() = FairyTypes.instance.CINNABAR
        val FLUORITE get() = FairyTypes.instance.FLUORITE
        val MOONSTONE get() = FairyTypes.instance.MOONSTONE
        val PYROPE get() = FairyTypes.instance.PYROPE
        val SMITHSONITE get() = FairyTypes.instance.SMITHSONITE
        val CHRISTMAS get() = FairyTypes.instance.CHRISTMAS
        val SANTA_CLAUS get() = FairyTypes.instance.SANTA_CLAUS
        val ICE get() = FairyTypes.instance.ICE
        val PACKED_ICE get() = FairyTypes.instance.PACKED_ICE
        val GOLEM get() = FairyTypes.instance.GOLEM
        val GLASS get() = FairyTypes.instance.GLASS
        val ACTIVATOR_RAIL get() = FairyTypes.instance.ACTIVATOR_RAIL
        val IRON_BARS get() = FairyTypes.instance.IRON_BARS
        val TAIGA get() = FairyTypes.instance.TAIGA
        val DESERT get() = FairyTypes.instance.DESERT
        val MOUNTAIN get() = FairyTypes.instance.MOUNTAIN
        val TIME get() = FairyTypes.instance.TIME
        val NEPHRITE get() = FairyTypes.instance.NEPHRITE
        val TOURMALINE get() = FairyTypes.instance.TOURMALINE
        val TOPAZ get() = FairyTypes.instance.TOPAZ
        val COW get() = FairyTypes.instance.COW
        val PIG get() = FairyTypes.instance.PIG
        val SUGAR get() = FairyTypes.instance.SUGAR
        val CAKE get() = FairyTypes.instance.CAKE
        val COOKIE get() = FairyTypes.instance.COOKIE
        val DARK_CHOCOLATE get() = FairyTypes.instance.DARK_CHOCOLATE
        val ENCHANTED_GOLDEN_APPLE get() = FairyTypes.instance.ENCHANTED_GOLDEN_APPLE
        val FORTUNE get() = FairyTypes.instance.FORTUNE
        val ROTTEN_FLESH get() = FairyTypes.instance.ROTTEN_FLESH
        val POISONOUS_POTATO get() = FairyTypes.instance.POISONOUS_POTATO
        val MELON get() = FairyTypes.instance.MELON
        val BAKED_POTATO get() = FairyTypes.instance.BAKED_POTATO
        val COOKED_CHICKEN get() = FairyTypes.instance.COOKED_CHICKEN
        val COOKED_SALMON get() = FairyTypes.instance.COOKED_SALMON
        val STEAK get() = FairyTypes.instance.STEAK
        val GOLDEN_APPLE get() = FairyTypes.instance.GOLDEN_APPLE
        val CUPID get() = FairyTypes.instance.CUPID
        val SPIDER_EYE get() = FairyTypes.instance.SPIDER_EYE
        val SHULKER get() = FairyTypes.instance.SHULKER
        val SLIME get() = FairyTypes.instance.SLIME
        val MAGMA_CUBE get() = FairyTypes.instance.MAGMA_CUBE
        val BLAZE get() = FairyTypes.instance.BLAZE
        val BEETROOT get() = FairyTypes.instance.BEETROOT
        val PUMPKIN_PIE get() = FairyTypes.instance.PUMPKIN_PIE
        val BEETROOT_SOUP get() = FairyTypes.instance.BEETROOT_SOUP
        val ELEVEN get() = FairyTypes.instance.ELEVEN
        val IMPERIAL_TOPAZ get() = FairyTypes.instance.IMPERIAL_TOPAZ
        val DOOR get() = FairyTypes.instance.DOOR
        val IRON_DOOR get() = FairyTypes.instance.IRON_DOOR
        val REDSTONE_REPEATER get() = FairyTypes.instance.REDSTONE_REPEATER
        val REDSTONE_COMPARATOR get() = FairyTypes.instance.REDSTONE_COMPARATOR
        val CHORUS_FRUIT get() = FairyTypes.instance.CHORUS_FRUIT
        val MUSHROOM_STEW get() = FairyTypes.instance.MUSHROOM_STEW
        val RAW_RABBIT get() = FairyTypes.instance.RAW_RABBIT
        val MIRAGE_FLOWER get() = FairyTypes.instance.MIRAGE_FLOWER
        val SUNRISE get() = FairyTypes.instance.SUNRISE
        val HATSUYUME get() = FairyTypes.instance.HATSUYUME
        val PLAYER get() = FairyTypes.instance.PLAYER
        val CHARGED_CREEPER get() = FairyTypes.instance.CHARGED_CREEPER
        val SUGAR_CANE get() = FairyTypes.instance.SUGAR_CANE
        val POTATO get() = FairyTypes.instance.POTATO
        val NOTE get() = FairyTypes.instance.NOTE
        val JUKEBOX get() = FairyTypes.instance.JUKEBOX
        val NETHER_PORTAL get() = FairyTypes.instance.NETHER_PORTAL
        val MIRAGE get() = FairyTypes.instance.MIRAGE
        val COAL_DUST get() = FairyTypes.instance.COAL_DUST
        val DIAMOND_DUST get() = FairyTypes.instance.DIAMOND_DUST
        val BUTTON get() = FairyTypes.instance.BUTTON
        val COOKED_COD get() = FairyTypes.instance.COOKED_COD
        val PEONY get() = FairyTypes.instance.PEONY
        val BOOK get() = FairyTypes.instance.BOOK
        val BEDROCK get() = FairyTypes.instance.BEDROCK
        val BAT get() = FairyTypes.instance.BAT
        val CURSE_OF_VANISHING get() = FairyTypes.instance.CURSE_OF_VANISHING
        val GRAVITY get() = FairyTypes.instance.GRAVITY
        val PYRITE get() = FairyTypes.instance.PYRITE
        val RED_SPINEL get() = FairyTypes.instance.RED_SPINEL
    }

    val parentFairy get() = getVariant().type.parentFairy
    val rare get() = getVariant().rare
    val id get() = getVariant().id
}

fun FairyCard.getVariant(rank: Int = 1) = variants[rank - 1]
fun FairyCard.createItemStack(rank: Int = 1, count: Int = 1) = getVariant(rank).createItemStack(count)

data class FairyEntry(val id: Int, val bundle: FairyCard)

@Suppress("unused")
class FairyTypes(private val count: Int) {
    companion object {
        lateinit var instance: FairyTypes
    }

    private val variantsImpl = mutableListOf<FairyEntry>()
    val variants: List<FairyEntry> get() = variantsImpl

    private fun m(shine: Double, fire: Double, wind: Double, gaia: Double, aqua: Double, dark: Double) = ManaSet(shine, fire, wind, gaia, aqua, dark)

    private fun e(vararg powers: Double): ErgSet {
        val types = Erg.values().toList()
        if (powers.size != types.size) throw IllegalArgumentException("Illegal erg count: ${powers.size} != ${types.size}")
        return ErgSet(types.withIndex().associate { (i, erg) -> erg to powers[i] })
    }

    private fun c(skin: Int, bright: Int, dark: Int, hair: Int) = ColorSet(skin, bright, dark, hair)

    private operator fun Int.invoke(name: String, parentFairy: () -> FairyCard?, rare: Int, cost: Int, rateSpecial: Double, manaSet: ManaSet, ergSet: ErgSet, colorSet: ColorSet): FairyCard {
        val id = this

        fun getType(rank: Int): FairyType {
            val rateRare = 2.0.pow((rare + rank - 2) / 4.0)
            val rateVariance = 0.5.pow(((manaSet / manaSet.max).sum - 1) / 5.0)
            val manaSetReal = manaSet / manaSet.sum * (cost * rateRare * rateVariance * rateSpecial)
            val ergSetReal = ErgSet(ergSet.entries.associate { (erg, value) -> erg to value * rateRare })
            return FairyType(
                ResourceLocation(ModMirageFairy2019.MODID, name),
                parentFairy,
                textComponent { translate("mirageFairy2019.fairy.$name.name") },
                colorSet.hair,
                cost.toDouble(),
                manaSetReal,
                ergSetReal
            )
        }

        // Create Variants
        val variants = (1..count).map { VariantFairy(id, colorSet, getType(it), rare, it) }
        val bundle = FairyCard(variants)

        // Register
        variantsImpl += FairyEntry(id, bundle)

        return bundle
    }

    val AIR = 0("air", { MIRAGE }, 1, 15, 1.00, m(0.0, 0.0, 2.0, 0.0, 1.0, 10.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 2.0, 1.0, 10.0, 3.0), c(0xFFBE80, 0xDEFFFF, 0xDEFFFF, 0xB0FFFF))
    val WATER = 1("water", { MIRAGE }, 1, 50, 1.00, m(0.0, 0.0, 1.0, 4.0, 8.0, 10.0), e(1.0, 0.0, 0.0, 0.0, 0.0, 10.0, 0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 7.0, 0.0, 2.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 2.0), c(0x5469F2, 0x5985FF, 0x172AD3, 0x2D40F4))
    val FIRE = 2("fire", { MIRAGE }, 2, 20, 1.00, m(1.0, 17.0, 1.0, 0.0, 0.0, 10.0), e(7.0, 2.0, 1.0, 10.0, 15.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 4.0, 8.0, 0.0, 0.0, 0.0, 10.0, 2.0, 0.0, 0.0, 0.0, 6.0, 0.0), c(0xFF6C01, 0xF9DFA4, 0xFF7324, 0xFF4000))
    val SUN = 3("sun", { STAR }, 5, 99, 1.00, m(10.0, 40.0, 40.0, 20.0, 20.0, 60.0), e(2.0, 0.0, 0.0, 21.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 5.0, 1.0, 0.0, 0.0, 0.0, 16.0, 0.0), c(0xff2f00, 0xff972b, 0xff7500, 0xffe7b2))
    val STONE = 4("stone", { MIRAGE }, 1, 83, 1.00, m(0.0, 0.0, 0.0, 8.0, 0.0, 10.0), e(2.0, 0.0, 2.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 1.0, 0.0, 1.0, 0.0), c(0x333333, 0x8F8F8F, 0x686868, 0x747474))
    val DIRT = 5("dirt", { MIRAGE }, 1, 70, 1.00, m(0.0, 0.0, 0.0, 0.0, 5.0, 10.0), e(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0), c(0xB87440, 0xB9855C, 0x593D29, 0x914A18))
    val IRON = 6("iron", { MIRAGE }, 2, 86, 1.00, m(0.0, 1.0, 0.0, 10.0, 1.0, 4.0), e(2.0, 0.0, 2.0, 1.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 5.0, 0.0, 0.0, 0.0, 10.0, 0.0, 1.0, 5.0, 0.0, 0.0), c(0xA0A0A0, 0xD8D8D8, 0x727272, 0xD8AF93))
    val DIAMOND = 7("diamond", { MIRAGE }, 4, 76, 1.00, m(10.0, 13.0, 19.0, 49.0, 23.0, 0.0), e(4.0, 0.0, 2.0, 5.0, 0.0, 0.0, 17.0, 4.0, 0.0, 0.0, 0.0, 10.0, 0.0, 7.0, 0.0, 4.0, 0.0, 2.0, 0.0, 2.0, 0.0, 0.0, 0.0), c(0x97FFE3, 0xD1FAF3, 0x70FFD9, 0x30DBBD))
    val REDSTONE = 8("redstone", { MIRAGE }, 3, 54, 1.00, m(1.0, 35.0, 11.0, 10.0, 0.0, 6.0), e(0.0, 0.0, 0.0, 7.0, 0.0, 0.0, 6.0, 0.0, 0.0, 1.0, 0.0, 0.0, 4.0, 0.0, 0.0, 4.0, 6.0, 3.0, 0.0, 0.0, 8.0, 2.0, 8.0), c(0xFF5959, 0xFF0000, 0xCD0000, 0xBA0000))
    val ENDERMAN = 9("enderman", { MIRAGE }, 4, 48, 1.00, m(1.0, 12.0, 12.0, 16.0, 10.0, 1.0), e(11.0, 3.0, 5.0, 2.0, 0.0, 1.0, 2.0, 7.0, 1.0, 12.0, 0.0, 4.0, 2.0, 0.0, 14.0, 8.0, 4.0, 8.0, 0.0, 0.0, 0.0, 1.0, 16.0), c(0x000000, 0x161616, 0x161616, 0xEF84FA))
    val MOON = 10("moon", { STAR }, 5, 95, 1.00, m(10.0, 25.0, 25.0, 20.0, 20.0, 90.0), e(0.0, 0.0, 0.0, 6.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 0.0, 0.0, 5.0, 0.0, 16.0, 0.0), c(0xD9E4FF, 0x747D93, 0x0C121F, 0x2D4272))
    val SAND = 11("sand", { MIRAGE }, 1, 64, 1.00, m(0.1, 0.0, 0.0, 4.0, 1.0, 10.0), e(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 7.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0), c(0xB87440, 0xEEE4B6, 0xC2BC84, 0xD8D09B))
    val GOLD = 12("gold", { MIRAGE }, 3, 93, 1.00, m(1.0, 0.0, 12.0, 10.0, 3.0, 15.0), e(1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0, 0.0, 0.0, 0.0, 3.0, 0.0, 1.0, 6.0, 0.0, 0.0), c(0xA0A0A0, 0xFFFF0B, 0xDC7613, 0xDEDE00))
    val SPIDER = 13("spider", { MIRAGE }, 2, 43, 1.00, m(0.0, 0.0, 0.0, 0.0, 10.0, 4.0), e(10.0, 0.0, 0.0, 2.0, 0.0, 2.0, 0.0, 4.0, 0.0, 0.0, 0.0, 2.0, 2.0, 1.0, 10.0, 4.0, 2.0, 3.0, 0.0, 0.0, 0.0, 3.0, 12.0), c(0x494422, 0x61554A, 0x52483F, 0xA80E0E))
    val SKELETON = 14("skeleton", { MIRAGE }, 1, 49, 1.00, m(0.0, 8.0, 10.0, 5.0, 0.0, 8.0), e(12.0, 1.0, 3.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 1.0, 12.0, 5.0, 2.0, 0.0, 2.0, 4.0, 3.0, 11.0, 0.0, 1.0, 0.0, 1.0, 4.0), c(0xCACACA, 0xCFCFCF, 0xCFCFCF, 0x494949))
    val ZOMBIE = 15("zombie", { MIRAGE }, 1, 55, 1.00, m(0.0, 9.0, 10.0, 0.0, 2.0, 9.0), e(13.0, 0.0, 1.0, 0.0, 0.0, 4.0, 0.0, 3.0, 2.0, 0.0, 0.0, 2.0, 3.0, 2.0, 4.0, 3.0, 3.0, 9.0, 0.0, 0.0, 0.0, 1.0, 2.0), c(0x2B4219, 0x00AAAA, 0x322976, 0x2B4219))
    val CREEPER = 16("creeper", { MIRAGE }, 2, 35, 1.00, m(0.0, 0.0, 10.0, 0.0, 12.0, 4.0), e(10.0, 0.0, 3.0, 0.0, 3.0, 2.0, 0.0, 0.0, 0.0, 0.0, 3.0, 17.0, 6.0, 0.0, 5.0, 3.0, 7.0, 2.0, 0.0, 0.0, 2.0, 1.0, 4.0), c(0x5BAA53, 0xD6FFCF, 0x5EE74C, 0x000000))
    val WHEAT = 17("wheat", { MIRAGE }, 2, 31, 1.00, m(0.0, 0.0, 0.0, 0.0, 10.0, 5.0), e(0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 7.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0), c(0x168700, 0xD5DA45, 0x716125, 0x9E8714))
    val LILAC = 18("lilac", { MIRAGE }, 3, 28, 1.00, m(0.0, 0.0, 1.0, 1.0, 10.0, 0.0), e(0.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 9.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 3.0), c(0x63D700, 0xF0C9FF, 0xDC8CE6, 0xA22CFF))
    val TORCH = 19("torch", { MIRAGE }, 1, 19, 1.00, m(0.1, 1.0, 1.0, 10.0, 4.0, 0.0), e(1.0, 1.0, 2.0, 12.0, 8.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 4.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0), c(0xFFFFFF, 0xFFC52C, 0xFF5800, 0xFFE6A5))
    val LAVA = 20("lava", { MIRAGE }, 2, 58, 1.00, m(0.0, 18.0, 0.0, 4.0, 0.0, 10.0), e(8.0, 1.0, 1.0, 13.0, 18.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 3.0, 3.0, 0.0, 0.0, 0.0, 7.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xCD4208, 0xEDB54A, 0xCC4108, 0x4C1500))
    val STAR = 21("star", { MIRAGE }, 4, 98, 1.00, m(10.0, 30.0, 50.0, 10.0, 30.0, 90.0), e(0.0, 0.0, 0.0, 4.0, 2.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 16.0, 0.0), c(0xffffff, 0x2C2C2E, 0x0E0E10, 0x191919))
    val GRAVEL = 22("gravel", { MIRAGE }, 2, 77, 1.00, m(0.0, 0.0, 0.0, 12.0, 0.0, 10.0), e(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 3.0, 0.0, 0.0, 0.0, 2.0, 0.0, 1.0, 0.0, 0.0, 1.0), c(0x333333, 0xC0B5B6, 0x968B8E, 0x63565C))
    val EMERALD = 23("emerald", { MIRAGE }, 4, 73, 1.00, m(10.0, 0.0, 6.0, 0.0, 42.0, 71.0), e(0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 8.0, 0.0, 4.0, 0.0, 5.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 4.0), c(0x9FF9B5, 0x81F99E, 0x17DD62, 0x008A25))
    val LAPISLAZULI = 24("lapislazuli", { MIRAGE }, 4, 62, 1.00, m(1.0, 0.0, 8.0, 10.0, 18.0, 0.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 9.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 1.0, 0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0), c(0xA2B7E8, 0x4064EC, 0x224BD5, 0x0A33C2))
    val ENDER_DRAGON = 25("ender_dragon", { MIRAGE }, 5, 61, 1.00, m(3.0, 0.0, 51.0, 0.0, 10.0, 0.0), e(20.0, 0.0, 0.0, 2.0, 0.0, 2.0, 0.0, 4.0, 0.0, 2.0, 7.0, 9.0, 1.0, 0.0, 15.0, 5.0, 6.0, 4.0, 0.0, 0.0, 0.0, 14.0, 11.0), c(0x000000, 0x181818, 0x181818, 0xA500E2))
    val WITHER_SKELETON = 26("wither_skeleton", { SKELETON }, 4, 69, 1.00, m(0.0, 11.0, 10.0, 4.0, 1.0, 1.0), e(17.0, 1.0, 3.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 7.0, 2.0, 4.0, 2.0, 5.0, 4.0, 13.0, 0.0, 0.0, 0.0, 1.0, 5.0), c(0x505252, 0x1C1C1C, 0x1C1C1C, 0x060606))
    val WITHER = 27("wither", { MIRAGE }, 5, 52, 1.00, m(0.0, 8.0, 10.0, 3.0, 1.0, 0.0), e(25.0, 0.0, 3.0, 2.0, 1.0, 1.0, 0.0, 2.0, 0.0, 0.0, 14.0, 15.0, 2.0, 0.0, 1.0, 4.0, 7.0, 9.0, 0.0, 0.0, 3.0, 13.0, 6.0), c(0x181818, 0x3C3C3C, 0x141414, 0x557272))
    val THUNDER = 28("thunder", { MIRAGE }, 3, 18, 1.00, m(2.0, 9.0, 3.0, 2.0, 0.0, 10.0), e(9.0, 0.0, 1.0, 8.0, 11.0, 0.0, 0.0, 9.0, 0.0, 4.0, 5.0, 11.0, 11.0, 0.0, 0.0, 0.0, 12.0, 1.0, 0.0, 0.0, 18.0, 5.0, 0.0), c(0xB4FFFF, 0x4D5670, 0x4D5670, 0xFFEB00))
    val CHICKEN = 29("chicken", { MIRAGE }, 1, 39, 1.00, m(0.0, 0.0, 1.0, 0.0, 10.0, 7.0), e(1.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 9.0, 0.0, 0.0, 2.0, 0.0, 1.0, 0.0, 10.0, 3.0, 2.0, 2.0, 0.0, 0.0, 0.0, 7.0, 8.0), c(0xFFDFA3, 0xFFFFFF, 0xFFFFFF, 0xD93117))
    val FURNACE = 30("furnace", { MIRAGE }, 2, 72, 1.00, m(0.0, 2.0, 0.0, 10.0, 2.0, 0.0), e(1.0, 10.0, 0.0, 6.0, 10.0, 0.0, 0.0, 2.0, 8.0, 0.0, 0.0, 1.0, 4.0, 0.0, 0.0, 3.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0), c(0xFFFFFF, 0xFF7F19, 0x8E8E8E, 0x383838))
    val MAGENTA_GLAZED_TERRACOTTA = 31("magenta_glazed_terracotta", { MIRAGE }, 3, 60, 1.00, m(0.0, 1.0, 0.0, 10.0, 11.0, 0.0), e(0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xF4B5CB, 0xCB58C2, 0x9D2D95))
    val BREAD = 32("bread", { MIRAGE }, 2, 35, 1.00, m(0.0, 0.0, 0.0, 5.0, 10.0, 0.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 14.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xCC850C, 0x9E7325, 0x654B17, 0x3F2E0E))
    val DAYTIME = 33("daytime", { TIME }, 1, 88, 1.00, m(1.0, 0.0, 10.0, 3.0, 7.0, 24.0), e(0.0, 0.0, 0.0, 11.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0), c(0xFFE260, 0xAACAEF, 0x84B5EF, 0xFFE7B2))
    val NIGHT = 34("night", { TIME }, 1, 83, 1.00, m(0.0, 7.0, 10.0, 0.0, 7.0, 24.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 3.0, 0.0, 4.0, 1.0, 3.0, 0.0), c(0xFFE260, 0x2C2C2E, 0x0E0E10, 0x2D4272))
    val MORNING = 35("morning", { TIME }, 2, 85, 1.00, m(1.0, 5.0, 10.0, 1.0, 7.0, 18.0), e(0.0, 0.0, 0.0, 9.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 3.0, 0.0, 3.0, 0.0), c(0xFFE260, 0x91C4D9, 0x4570A6, 0xFF7017))
    val FINE = 36("fine", { MIRAGE }, 1, 22, 1.00, m(1.0, 0.0, 10.0, 4.0, 12.0, 28.0), e(0.0, 0.0, 0.0, 17.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0), c(0xB4FFFF, 0xAACAEF, 0x84B5EF, 0xffe7b2))
    val RAIN = 37("rain", { MIRAGE }, 2, 25, 1.00, m(0.0, 2.0, 10.0, 0.0, 19.0, 17.0), e(1.0, 0.0, 0.0, 0.0, 0.0, 13.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 1.0, 4.0, 0.0, 1.0, 4.0, 4.0, 0.0), c(0xB4FFFF, 0x4D5670, 0x4D5670, 0x2D40F4))
    val PLAINS = 38("plains", { MIRAGE }, 1, 79, 1.00, m(0.0, 0.0, 0.0, 3.0, 18.0, 10.0), e(0.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 1.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0), c(0x80FF00, 0xD4FF82, 0x86C91C, 0xBB5400))
    val FOREST = 39("forest", { MIRAGE }, 2, 83, 1.00, m(0.0, 0.0, 2.0, 12.0, 32.0, 10.0), e(1.0, 0.0, 9.0, 0.0, 0.0, 3.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 8.0, 2.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0), c(0x80FF00, 0x7B9C62, 0x89591D, 0x2E6E14))
    val APPLE = 40("apple", { MIRAGE }, 2, 43, 1.00, m(0.1, 0.0, 3.0, 0.0, 10.0, 2.0), e(0.0, 0.0, 4.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 12.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0), c(0xFF755D, 0xFF564E, 0xFF0000, 0x01A900))
    val CARROT = 41("carrot", { MIRAGE }, 3, 38, 1.00, m(0.0, 0.0, 1.0, 0.0, 10.0, 0.0), e(0.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 11.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0), c(0xFF8F00, 0xFFAD66, 0xFF9600, 0x01A900))
    val CACTUS = 42("cactus", { MIRAGE }, 2, 41, 1.00, m(0.0, 7.0, 0.0, 0.0, 10.0, 2.0), e(9.0, 0.0, 1.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 3.0, 7.0, 8.0, 0.0, 1.0, 4.0, 0.0, 0.0, 0.0, 1.0, 2.0), c(0x008200, 0xB0FFAC, 0x00E100, 0x010000))
    val AXE = 43("axe", { MIRAGE }, 2, 83, 1.00, m(0.0, 7.0, 0.0, 10.0, 2.0, 5.0), e(12.0, 2.0, 12.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 6.0, 0.0, 10.0, 0.0, 2.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xCD9A6A, 0x529B3A, 0xC9D0C6))
    val CHEST = 44("chest", { MIRAGE }, 1, 31, 1.00, m(0.0, 0.0, 0.0, 10.0, 0.0, 7.0), e(0.0, 1.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xFFA431, 0xFFA900, 0xFFC2A5))
    val CRAFTING_TABLE = 45("crafting_table", { MIRAGE }, 2, 40, 1.00, m(0.0, 6.0, 0.0, 10.0, 0.0, 0.0), e(0.0, 12.0, 2.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xFFBB9A, 0xFFC980, 0x000000))
    val POTION = 46("potion", { MIRAGE }, 3, 29, 1.00, m(1.0, 2.0, 18.0, 10.0, 3.0, 0.0), e(3.0, 2.0, 0.0, 1.0, 1.0, 6.0, 0.0, 0.0, 2.0, 0.0, 2.0, 0.0, 10.0, 0.0, 8.0, 8.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0), c(0xFFFFFF, 0x52CAFF, 0x00AEFF, 0xFFFFFF))
    val SWORD = 47("sword", { MIRAGE }, 2, 62, 1.00, m(0.1, 8.0, 1.0, 10.0, 0.0, 3.0), e(13.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 12.0, 0.0, 2.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xFFC48E, 0xFF0300, 0xFFFFFF))
    val DISPENSER = 48("dispenser", { MIRAGE }, 2, 86, 1.00, m(0.0, 10.0, 0.0, 3.0, 0.0, 0.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 10.0, 3.0, 10.0, 0.0, 0.0, 0.0, 0.0, 2.0, 3.0, 0.0, 0.0, 0.0, 2.0, 0.0, 7.0), c(0xFFFFFF, 0xD7D7D7, 0x727272, 0x95623C))
    val OCEAN = 49("ocean", { MIRAGE }, 1, 73, 1.00, m(0.0, 0.0, 0.0, 0.0, 22.0, 10.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 22.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 4.0, 0.0, 2.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0), c(0x80FF00, 0x86B5FF, 0x1D7EFF, 0x004DA5))
    val FISH = 50("fish", { MIRAGE }, 2, 29, 1.00, m(0.0, 0.0, 0.0, 0.0, 10.0, 3.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 6.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 5.0), c(0x6B9F93, 0x5A867C, 0x43655D, 0xADBEDB))
    val COD = 51("cod", { FISH }, 2, 27, 1.00, m(0.0, 0.0, 0.0, 0.0, 10.0, 3.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 7.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 5.0), c(0xC6A271, 0xD6C5AD, 0x986D4E, 0xBEA989))
    val SALMON = 52("salmon", { FISH }, 3, 31, 1.00, m(0.0, 0.0, 0.0, 4.0, 10.0, 2.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 8.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 5.0), c(0xAB3533, 0xFF6763, 0x6B8073, 0xBD928B))
    val PUFFERFISH = 53("pufferfish", { FISH }, 4, 36, 1.00, m(0.0, 12.0, 0.0, 0.0, 10.0, 0.0), e(11.0, 0.0, 0.0, 0.0, 0.0, 8.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 7.0, 3.0, 5.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 5.0), c(0xEBDE39, 0xEBC500, 0xBF9B00, 0x429BBA))
    val CLOWNFISH = 54("clownfish", { FISH }, 4, 26, 1.00, m(1.0, 0.0, 12.0, 0.0, 10.0, 0.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 6.0, 2.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 6.0), c(0xE46A22, 0xF46F20, 0xA94B1D, 0xFFDBC5))
    val SPRUCE = 55("spruce", { MIRAGE }, 2, 95, 1.00, m(0.0, 0.0, 0.0, 8.0, 10.0, 6.0), e(0.0, 0.0, 8.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 11.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 2.0, 3.0), c(0x795C36, 0x583E1F, 0x23160A, 0x4C784C))
    val ANVIL = 56("anvil", { MIRAGE }, 3, 82, 1.00, m(0.0, 2.0, 0.0, 10.0, 0.0, 2.0), e(2.0, 8.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 9.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xA9A9A9, 0x909090, 0xA86F18))
    val OBSIDIAN = 57("obsidian", { MIRAGE }, 3, 78, 1.00, m(1.0, 2.0, 3.0, 10.0, 0.0, 0.0), e(3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10.0, 0.0, 0.0, 0.0, 0.0, 10.0, 0.0, 4.0, 0.0, 0.0, 0.0, 7.0, 0.0, 1.0, 0.0, 0.0, 0.0), c(0x775599, 0x6029B3, 0x2E095E, 0x0F0033))
    val SEED = 58("seed", { WHEAT }, 1, 17, 1.00, m(0.0, 0.0, 0.0, 0.0, 10.0, 3.0), e(0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 13.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0), c(0x03B50A, 0x03FF14, 0x037B0A, 0xAAAE36))
    val ENCHANT = 59("enchant", { MIRAGE }, 3, 13, 1.00, m(2.0, 0.0, 10.0, 0.0, 2.0, 0.0), e(0.0, 6.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 12.0, 2.0, 0.0, 0.0, 0.0, 3.0, 2.0, 9.0), c(0xD0C2FF, 0xF055FF, 0xC381E3, 0xBE00FF))
    val GLOWSTONE = 60("glowstone", { MIRAGE }, 3, 39, 1.00, m(2.0, 0.0, 7.0, 10.0, 0.0, 0.0), e(0.0, 0.0, 0.0, 14.0, 0.0, 0.0, 7.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 4.0, 1.0, 0.0, 0.0, 2.0, 6.0, 4.0), c(0xFFFFB8, 0xFFD15E, 0xFFD244, 0xFFFF00))
    val COAL = 61("coal", { MIRAGE }, 1, 61, 1.00, m(0.0, 9.0, 0.0, 10.0, 0.0, 7.0), e(0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 1.0, 2.0, 10.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0), c(0x4C2510, 0x52504C, 0x39352E, 0x150B00))
    val VILLAGER = 62("villager", { MIRAGE }, 3, 50, 1.00, m(1.0, 5.0, 5.0, 10.0, 10.0, 15.0), e(4.0, 8.0, 5.0, 0.0, 0.0, 2.0, 0.0, 10.0, 4.0, 0.0, 2.0, 5.0, 3.0, 4.0, 12.0, 7.0, 3.0, 4.0, 0.0, 0.0, 0.0, 1.0, 10.0), c(0xB58D63, 0x608C57, 0x608C57, 0x009800))
    val LIBRARIAN = 63("librarian", { VILLAGER }, 4, 42, 1.00, m(3.0, 15.0, 15.0, 1.0, 10.0, 1.0), e(2.0, 4.0, 3.0, 0.0, 0.0, 2.0, 0.0, 13.0, 4.0, 0.0, 1.0, 2.0, 6.0, 2.0, 12.0, 15.0, 3.0, 5.0, 0.0, 0.0, 0.0, 1.0, 11.0), c(0xB58D63, 0xEBEBEB, 0xEBEBEB, 0x009800))
    val NETHER_STAR = 64("nether_star", { WITHER }, 5, 41, 1.00, m(2.0, 8.0, 10.0, 0.0, 0.0, 0.0), e(0.0, 0.0, 0.0, 5.0, 0.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 10.0, 9.0, 8.0, 0.0, 2.0, 3.0, 7.0, 9.0), c(0xD8D8FF, 0xF2E3FF, 0xD9E7FF, 0xFFFF68))
    val BREWING_STAND = 65("brewing_stand", { MIRAGE }, 3, 26, 1.00, m(1.0, 10.0, 5.0, 7.0, 2.0, 0.0), e(0.0, 10.0, 0.0, 4.0, 9.0, 4.0, 0.0, 1.0, 4.0, 0.0, 0.0, 4.0, 15.0, 0.0, 2.0, 11.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0), c(0xFFFFFF, 0xAE5B5B, 0x7E7E7E, 0xFFDF55))
    val HOE = 66("hoe", { MIRAGE }, 2, 74, 1.00, m(0.0, 0.0, 0.0, 10.0, 8.0, 6.0), e(5.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 8.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xFFC48E, 0x47FF00, 0xFFFFFF))
    val SHIELD = 67("shield", { MIRAGE }, 2, 81, 1.00, m(0.1, 0.0, 0.0, 10.0, 4.0, 2.0), e(1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xFFC48E, 0x5A5A8E, 0xFFFFFF))
    val HOPPER = 68("hopper", { MIRAGE }, 3, 63, 1.00, m(0.0, 3.0, 0.0, 10.0, 0.0, 2.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 9.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0), c(0xFFFFFF, 0x797979, 0x646464, 0x5A5A5A))
    val MINA = 69("mina", { null }, 1, 50, 0.01, m(0.0, 0.0, 0.0, 0.0, 0.0, 10.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFF84, 0xFFFF00, 0xFFFF00, 0xFFC800))
    val MAGNETITE = 70("magnetite", { MIRAGE }, 2, 62, 1.00, m(10.0, 18.0, 0.0, 18.0, 0.0, 95.0), e(0.0, 2.0, 4.0, 0.0, 0.0, 0.0, 8.0, 0.0, 0.0, 0.0, 2.0, 3.0, 3.0, 0.0, 0.0, 1.0, 0.0, 3.0, 0.0, 1.0, 0.0, 0.0, 0.0), c(0x72736D, 0x2A2A26, 0x1F201C, 0x1F201C))
    val SULFUR = 71("sulfur", { MIRAGE }, 3, 44, 1.00, m(10.0, 14.0, 0.0, 67.0, 0.0, 83.0), e(4.0, 3.0, 0.0, 3.0, 8.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 5.0, 11.0, 0.0, 1.0, 3.0, 6.0, 0.0, 0.0, 1.0, 0.0, 2.0, 0.0), c(0xFFF4AA, 0xFFE82C, 0xD6C00E, 0xEAD20F))
    val APATITE = 72("apatite", { MIRAGE }, 3, 60, 1.00, m(10.0, 0.0, 0.0, 11.0, 41.0, 63.0), e(0.0, 2.0, 6.0, 2.0, 3.0, 7.0, 12.0, 0.0, 0.0, 0.0, 0.0, 5.0, 4.0, 0.0, 4.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 6.0), c(0xCAE6FF, 0x76C1FF, 0x2993EA, 0x80C2FF))
    val CINNABAR = 73("cinnabar", { MIRAGE }, 3, 61, 1.00, m(10.0, 61.0, 26.0, 0.0, 0.0, 70.0), e(8.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10.0, 0.0, 0.0, 0.0, 0.0, 3.0, 6.0, 0.0, 5.0, 5.0, 0.0, 2.0, 0.0, 1.0, 0.0, 0.0, 2.0), c(0xFEC0C0, 0xD41818, 0xBC1C1C, 0xDD2E2E))
    val FLUORITE = 74("fluorite", { MIRAGE }, 3, 59, 1.00, m(10.0, 26.0, 53.0, 5.0, 0.0, 64.0), e(0.0, 4.0, 0.0, 7.0, 4.0, 0.0, 14.0, 0.0, 0.0, 0.0, 0.0, 4.0, 3.0, 0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0, 0.0), c(0xC0FEF3, 0x28E1C7, 0xAE91E9, 0x66CDEB))
    val MOONSTONE = 75("moonstone", { MIRAGE }, 4, 68, 1.00, m(10.0, 3.0, 38.0, 0.0, 15.0, 43.0), e(0.0, 0.0, 0.0, 9.0, 2.0, 2.0, 13.0, 0.0, 0.0, 4.0, 0.0, 6.0, 0.0, 0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 3.0, 0.0, 6.0, 0.0), c(0xEDEFFF, 0xDBF7FE, 0xCCE7FE, 0xE0EEFE))
    val PYROPE = 76("pyrope", { MIRAGE }, 4, 71, 1.00, m(10.0, 0.0, 18.0, 0.0, 31.0, 75.0), e(2.0, 0.0, 0.0, 2.0, 4.0, 0.0, 14.0, 0.0, 0.0, 0.0, 0.0, 6.0, 0.0, 5.0, 0.0, 3.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0), c(0xF4CDDE, 0xEC97BC, 0xCE1860, 0xD41E6E))
    val SMITHSONITE = 77("smithsonite", { MIRAGE }, 3, 63, 1.00, m(10.0, 13.0, 0.0, 36.0, 0.0, 85.0), e(0.0, 4.0, 2.0, 0.0, 0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 1.0, 4.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0), c(0xC1F0E6, 0x95DFD0, 0x33917E, 0x52C6B5))
    val CHRISTMAS = 78("christmas", { TIME }, 4, 25, 1.20, m(2.0, 3.0, 10.0, 0.0, 7.0, 7.0), e(0.0, 0.0, 0.0, 4.0, 3.0, 4.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10.0, 14.0, 0.0, 3.0, 0.0), c(0xD2E6F6, 0xF1150F, 0xB20000, 0x248541))
    val SANTA_CLAUS = 79("santa_claus", { MIRAGE }, 5, 12, 1.20, m(4.0, 10.0, 20.0, 5.0, 10.0, 5.0), e(3.0, 6.0, 5.0, 3.0, 0.0, 2.0, 0.0, 12.0, 14.0, 7.0, 4.0, 1.0, 1.0, 1.0, 7.0, 9.0, 5.0, 1.0, 10.0, 6.0, 0.0, 11.0, 12.0), c(0xCDBBAD, 0xD61728, 0xDA0117, 0xDAD8D4))
    val ICE = 80("ice", { MIRAGE }, 1, 45, 1.00, m(10.0, 22.0, 0.0, 63.0, 95.0, 98.0), e(3.0, 0.0, 0.0, 0.0, 0.0, 7.0, 8.0, 1.0, 0.0, 0.0, 0.0, 1.0, 3.0, 0.0, 3.0, 1.0, 0.0, 0.0, 0.0, 10.0, 0.0, 0.0, 0.0), c(0xCBDBF8, 0xB6CBE3, 0xA1B6DA, 0xA1C0F5))
    val PACKED_ICE = 81("packed_ice", { MIRAGE }, 4, 75, 1.00, m(10.0, 22.0, 0.0, 85.0, 63.0, 0.0), e(4.0, 0.0, 0.0, 0.0, 0.0, 8.0, 12.0, 2.0, 0.0, 0.0, 0.0, 2.0, 3.0, 0.0, 2.0, 1.0, 0.0, 0.0, 0.0, 16.0, 0.0, 0.0, 0.0), c(0xACC8F0, 0x9EB2D6, 0x809DCC, 0x5D86CC))
    val GOLEM = 82("golem", { MIRAGE }, 3, 92, 1.00, m(0.0, 12.0, 10.0, 10.0, 0.0, 1.0), e(21.0, 2.0, 7.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 12.0, 0.0, 1.0, 0.0, 4.0, 0.0, 15.0, 0.0, 0.0, 0.0, 1.0, 7.0), c(0xC1AB9E, 0xB5ADA8, 0xABA39D, 0x557725))
    val GLASS = 83("glass", { MIRAGE }, 2, 53, 1.00, m(1.0, 2.0, 0.0, 10.0, 5.0, 3.0), e(2.0, 2.0, 1.0, 0.0, 1.0, 0.0, 8.0, 0.0, 0.0, 0.0, 0.0, 1.0, 8.0, 6.0, 0.0, 1.0, 0.0, 3.0, 0.0, 2.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xEFF5FF, 0xE8EDF5, 0xADE0E9))
    val ACTIVATOR_RAIL = 84("activator_rail", { MIRAGE }, 3, 77, 1.00, m(0.0, 10.0, 0.0, 7.0, 0.0, 0.0), e(0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 9.0), c(0xFFFFFF, 0xAC8852, 0x686868, 0xD40102))
    val IRON_BARS = 85("iron_bars", { MIRAGE }, 2, 64, 1.00, m(0.1, 4.0, 0.0, 10.0, 0.0, 12.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 16.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xA1A1A3, 0x404040, 0x404040))
    val TAIGA = 86("taiga", { MIRAGE }, 2, 88, 1.00, m(0.0, 2.0, 0.0, 19.0, 28.0, 10.0), e(3.0, 0.0, 12.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 3.0, 7.0, 2.0, 4.0, 0.0, 0.0, 7.0, 1.0, 2.0, 2.0), c(0x80FF00, 0x476545, 0x223325, 0x5A3711))
    val DESERT = 87("desert", { MIRAGE }, 2, 76, 1.00, m(1.0, 15.0, 0.0, 15.0, 3.0, 10.0), e(5.0, 0.0, 0.0, 8.0, 4.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 2.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0), c(0x80FF00, 0xDDD6A5, 0xD6CE9D, 0x0F6C1C))
    val MOUNTAIN = 88("mountain", { MIRAGE }, 2, 92, 1.00, m(0.0, 3.0, 0.0, 35.0, 12.0, 10.0), e(4.0, 1.0, 4.0, 0.0, 0.0, 1.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 1.0, 4.0, 2.0, 0.0, 5.0, 1.0, 3.0, 1.0), c(0x80FF00, 0xB1B0B1, 0x717173, 0x4B794A))
    val TIME = 89("time", { MIRAGE }, 5, 26, 1.00, m(10.0, 5.0, 5.0, 5.0, 5.0, 10.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 11.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 1.0), c(0x89D585, 0xD5DEBC, 0xD8DEA7, 0x8DD586))
    val NEPHRITE = 90("nephrite", { MIRAGE }, 3, 59, 1.00, m(1.0, 0.0, 12.0, 10.0, 3.0, 17.0), e(0.0, 13.0, 5.0, 0.0, 0.0, 0.0, 7.0, 0.0, 0.0, 1.0, 0.0, 2.0, 0.0, 0.0, 0.0, 7.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0), c(0x94D084, 0xA7D398, 0x88C698, 0x5EA15D))
    val TOURMALINE = 91("tourmaline", { MIRAGE }, 4, 66, 1.00, m(10.0, 51.0, 0.0, 19.0, 0.0, 42.0), e(0.0, 6.0, 0.0, 1.0, 0.0, 0.0, 11.0, 0.0, 0.0, 4.0, 0.0, 5.0, 5.0, 5.0, 0.0, 6.0, 5.0, 5.0, 0.0, 1.0, 14.0, 5.0, 5.0), c(0xF3F3F2, 0x3CCB5D, 0xB15498, 0xD7F5FF))
    val TOPAZ = 92("topaz", { MIRAGE }, 4, 72, 1.00, m(10.0, 38.0, 40.0, 0.0, 0.0, 35.0), e(2.0, 0.0, 0.0, 16.0, 11.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 6.0, 0.0, 6.0, 0.0, 3.0, 8.0, 0.0, 0.0, 1.0, 0.0, 2.0, 3.0), c(0xEED891, 0xEEAF2D, 0xCD7102, 0xFF9800))
    val COW = 93("cow", { MIRAGE }, 1, 54, 1.00, m(0.0, 0.0, 0.0, 2.0, 10.0, 5.0), e(2.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 5.0, 1.0, 0.0, 0.0, 3.0, 2.0, 1.0, 13.0, 3.0, 4.0, 4.0, 0.0, 0.0, 0.0, 1.0, 9.0), c(0x433626, 0x644B37, 0x4A3828, 0xADADAD))
    val PIG = 94("pig", { MIRAGE }, 1, 53, 1.00, m(0.0, 0.0, 0.0, 0.0, 10.0, 6.0), e(1.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 13.0, 3.0, 4.0, 2.0, 0.0, 0.0, 0.0, 1.0, 8.0), c(0xDB98A2, 0xF68C87, 0xC76B73, 0xDC94A1))
    val SUGAR = 95("sugar", { MIRAGE }, 1, 21, 1.00, m(0.1, 2.0, 0.0, 2.0, 10.0, 0.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 1.0, 13.0, 7.0, 7.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xE3E3E3, 0xE3E3E3, 0xCECED8, 0xF7F7F7))
    val CAKE = 96("cake", { MIRAGE }, 3, 47, 1.00, m(0.0, 0.0, 5.0, 1.0, 10.0, 0.0), e(0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 16.0, 5.0, 8.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0), c(0xCC850C, 0xF5F0DC, 0xD3D0BF, 0xDE3334))
    val COOKIE = 97("cookie", { MIRAGE }, 3, 28, 1.00, m(0.0, 0.0, 0.0, 3.0, 10.0, 0.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 12.0, 3.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xCC850C, 0xE9994F, 0xDA843C, 0x882500))
    val DARK_CHOCOLATE = 98("dark_chocolate", { MIRAGE }, 4, 26, 1.50, m(0.1, 0.0, 2.0, 2.0, 10.0, 8.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 12.0, 9.0, 8.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0), c(0x882500, 0x882500, 0x882500, 0x882500))
    val ENCHANTED_GOLDEN_APPLE = 99("enchanted_golden_apple", { GOLDEN_APPLE }, 5, 90, 1.00, m(3.0, 0.0, 8.0, 1.0, 10.0, 21.0), e(0.0, 3.0, 0.0, 5.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 13.0, 10.0, 6.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0), c(0xFF755D, 0xDEDE00, 0xDEDE00, 0xDE4FD7))
    val FORTUNE = 100("fortune", { ENCHANT }, 4, 28, 1.00, m(3.0, 0.0, 10.0, 0.0, 0.0, 0.0), e(0.0, 4.0, 11.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 9.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0), c(0xD0C2FF, 0xD0F5FF, 0xDBF7FF, 0xE2BBFF))
    val ROTTEN_FLESH = 101("rotten_flesh", { MIRAGE }, 2, 41, 1.00, m(0.0, 11.0, 8.0, 0.0, 10.0, 12.0), e(3.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 6.0, 0.0, 3.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0x846129, 0xBD5B2D, 0xBD5B2D, 0xBD422D))
    val POISONOUS_POTATO = 102("poisonous_potato", { POTATO }, 4, 40, 1.00, m(0.0, 9.0, 0.0, 3.0, 10.0, 0.0), e(4.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 6.0, 1.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0), c(0xFFE861, 0xC1D245, 0xB7C035, 0x01A900))
    val MELON = 103("melon", { MIRAGE }, 3, 56, 1.00, m(0.1, 0.0, 1.0, 2.0, 10.0, 1.0), e(0.0, 0.0, 1.0, 0.0, 0.0, 6.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 10.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0), c(0xFF5440, 0xA6EE63, 0x195612, 0x01A900))
    val BAKED_POTATO = 104("baked_potato", { POTATO }, 3, 36, 1.00, m(0.0, 0.0, 0.0, 2.0, 10.0, 7.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 13.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xC87847, 0xFFFF9A, 0xFFFF9A, 0xDEA73A))
    val COOKED_CHICKEN = 105("cooked_chicken", { CHICKEN }, 2, 31, 1.00, m(0.0, 0.0, 0.0, 2.0, 10.0, 9.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 14.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xC87847, 0xDA9F7A, 0xDA9F7A, 0x9C5321))
    val COOKED_SALMON = 106("cooked_salmon", { SALMON }, 3, 25, 1.00, m(0.0, 0.0, 0.0, 2.0, 10.0, 3.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 13.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xC87847, 0xFF7E3B, 0xFF7D41, 0xBA4F23))
    val STEAK = 107("steak", { COW }, 2, 43, 1.00, m(0.0, 0.0, 0.0, 2.0, 10.0, 5.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 15.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xC87847, 0xAA573F, 0xA4573F, 0x7A3A2A))
    val GOLDEN_APPLE = 108("golden_apple", { APPLE }, 3, 72, 1.00, m(1.0, 0.0, 18.0, 4.0, 10.0, 0.0), e(0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 12.0, 5.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0), c(0xFF755D, 0xDEDE00, 0xDEDE00, 0x01A900))
    val CUPID = 109("cupid", { MIRAGE }, 5, 33, 1.20, m(10.0, 0.0, 24.0, 11.0, 41.0, 0.0), e(6.0, 3.0, 2.0, 6.0, 0.0, 1.0, 0.0, 10.0, 3.0, 8.0, 14.0, 7.0, 2.0, 0.0, 16.0, 11.0, 2.0, 10.0, 0.0, 0.0, 0.0, 0.0, 14.0), c(0xCDBBAD, 0xE4F4FF, 0xDDEEFF, 0x893F1E))
    val SPIDER_EYE = 110("spider_eye", { SPIDER }, 3, 34, 1.00, m(0.1, 9.0, 0.0, 0.0, 10.0, 3.0), e(6.0, 0.0, 0.0, 4.0, 0.0, 1.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 2.0, 4.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 14.0), c(0x494422, 0xC45F6B, 0x65062B, 0x7B0D27))
    val SHULKER = 111("shulker", { MIRAGE }, 4, 48, 1.00, m(2.0, 0.0, 8.0, 6.0, 10.0, 0.0), e(16.0, 0.0, 0.0, 1.0, 0.0, 2.0, 2.0, 2.0, 12.0, 6.0, 13.0, 2.0, 1.0, 1.0, 5.0, 5.0, 1.0, 4.0, 0.0, 0.0, 0.0, 0.0, 7.0), c(0xDDE8A7, 0xB27AAA, 0x4B324B, 0x7B4873))
    val SLIME = 112("slime", { MIRAGE }, 3, 52, 1.00, m(0.0, 6.0, 10.0, 0.0, 4.0, 3.0), e(8.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 1.0, 2.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 1.0, 1.0, 2.0, 0.0, 0.0, 0.0, 4.0, 3.0), c(0x74BE5F, 0x71BC5E, 0x569746, 0x488A36))
    val MAGMA_CUBE = 113("magma_cube", { SLIME }, 4, 68, 1.00, m(0.0, 9.0, 10.0, 0.0, 0.0, 0.0), e(14.0, 0.0, 0.0, 4.0, 14.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 1.0, 8.0, 2.0, 0.0, 0.0, 0.0, 4.0, 2.0), c(0x3A0000, 0x592301, 0x300000, 0xE35C05))
    val BLAZE = 114("blaze", { MIRAGE }, 3, 26, 1.00, m(0.1, 15.0, 10.0, 2.0, 0.0, 0.0), e(15.0, 0.0, 0.0, 9.0, 16.0, 0.0, 0.0, 3.0, 0.0, 0.0, 13.0, 1.0, 2.0, 0.0, 1.0, 3.0, 13.0, 3.0, 0.0, 0.0, 0.0, 9.0, 6.0), c(0xE7DA21, 0xCB6E06, 0xB44500, 0xFF8025))
    val BEETROOT = 115("beetroot", { MIRAGE }, 3, 35, 1.00, m(0.0, 8.0, 0.0, 4.0, 10.0, 0.0), e(1.0, 0.0, 1.0, 0.0, 11.0, 3.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 14.0, 0.0, 2.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0), c(0xC1727C, 0xA74D55, 0x96383D, 0x01A900))
    val PUMPKIN_PIE = 116("pumpkin_pie", { MIRAGE }, 3, 46, 1.00, m(0.0, 2.0, 4.0, 7.0, 10.0, 1.0), e(0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 15.0, 2.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xE48A40, 0xF0B96A, 0xB68538, 0xED783F))
    val BEETROOT_SOUP = 117("beetroot_soup", { BEETROOT }, 3, 28, 1.00, m(0.0, 7.0, 2.0, 3.0, 10.0, 0.0), e(1.0, 1.0, 0.0, 0.0, 13.0, 7.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 13.0, 0.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xC70623, 0xDF0F27, 0xAC0616, 0x900000))
    val ELEVEN = 118("eleven", { MIRAGE }, 5, 11, 1.00, m(0.0, 99.0, 0.0, 0.0, 10.0, 0.0), e(11.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 11.0, 0.0, 0.0, 0.0, 8.0, 0.0, 1.0, 0.0, 4.0, 2.0, 4.0, 0.0, 3.0, 1.0, 0.0, 5.0), c(0x515151, 0x000000, 0x000000, 0x60F3D4))
    val IMPERIAL_TOPAZ = 119("imperial_topaz", { TOPAZ }, 5, 82, 1.20, m(10.0, 15.0, 23.0, 0.0, 26.0, 0.0), e(0.0, 1.0, 0.0, 18.0, 6.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 6.0, 0.0, 5.0, 11.0, 12.0, 0.0, 1.0, 0.0, 3.0, 6.0), c(0xFFD6AD, 0xFFCD94, 0xE89F4E, 0xFFB37E))
    val DOOR = 120("door", { MIRAGE }, 1, 75, 1.00, m(0.0, 5.0, 0.0, 10.0, 0.0, 5.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 14.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xD0A24C, 0xD1A440, 0x393B37))
    val IRON_DOOR = 121("iron_door", { DOOR }, 3, 84, 1.00, m(0.1, 7.0, 0.0, 10.0, 0.0, 1.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 17.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 1.0, 0.0, 13.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xCDCDCD, 0xCDCDCD, 0x959595))
    val REDSTONE_REPEATER = 122("redstone_repeater", { MIRAGE }, 3, 30, 1.00, m(0.0, 10.0, 0.0, 2.0, 0.0, 1.0), e(0.0, 1.0, 0.0, 3.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 1.0, 0.0, 0.0, 0.0, 9.0, 0.0, 8.0), c(0xFFFFFF, 0xFCFC95, 0xCBCBCB, 0xD40102))
    val REDSTONE_COMPARATOR = 123("redstone_comparator", { MIRAGE }, 3, 34, 1.00, m(0.0, 10.0, 1.0, 2.0, 0.0, 0.0), e(0.0, 2.0, 0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 9.0, 1.0, 0.0, 0.0, 0.0, 6.0, 0.0, 11.0), c(0xFFFFFF, 0xEAA199, 0xCBCBCB, 0xD40102))
    val CHORUS_FRUIT = 124("chorus_fruit", { MIRAGE }, 4, 32, 1.00, m(1.0, 0.0, 13.0, 0.0, 10.0, 5.0), e(0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 2.0, 0.0, 8.0, 0.0, 0.0, 1.0, 0.0, 8.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0), c(0xE1D7E1, 0x816481, 0x785978, 0x672D7A))
    val MUSHROOM_STEW = 125("mushroom_stew", { MIRAGE }, 3, 22, 1.00, m(0.0, 3.0, 11.0, 5.0, 10.0, 3.0), e(0.0, 2.0, 0.0, 0.0, 4.0, 6.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0, 0.0, 14.0, 1.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFB17E, 0xFF9666, 0xFF8A4A, 0xE7936A))
    val RAW_RABBIT = 126("raw_rabbit", { MIRAGE }, 4, 29, 1.00, m(0.0, 7.0, 7.0, 0.0, 10.0, 0.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 8.0, 0.0, 2.0, 0.0, 0.0, 2.0, 0.0, 3.0, 0.0), c(0xFFDBCE, 0xEFBCAC, 0xEFBCAC, 0x9E937F))
    val MIRAGE_FLOWER = 127("mirage_flower", { MIRAGE }, 3, 40, 1.00, m(3.0, 15.0, 4.0, 0.0, 10.0, 0.0), e(0.0, 0.0, 6.0, 0.0, 0.0, 2.0, 6.0, 0.0, 2.0, 0.0, 0.0, 1.0, 4.0, 3.0, 5.0, 13.0, 1.0, 0.0, 0.0, 0.0, 0.0, 3.0, 9.0), c(0xA3F5FF, 0x7CF3F5, 0x7CF3F5, 0x00E88A))
    val SUNRISE = 128("sunrise", { TIME }, 3, 84, 1.00, m(1.0, 6.0, 10.0, 1.0, 7.0, 21.0), e(0.0, 0.0, 0.0, 8.0, 4.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 4.0, 0.0, 4.0, 0.0, 17.0, 0.0), c(0xFFE260, 0xFF873A, 0xFF873A, 0xFF1D00))
    val HATSUYUME = 129("hatsuyume", { MIRAGE }, 5, 11, 1.20, m(10.0, 5.0, 22.0, 0.0, 13.0, 0.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 6.0, 13.0, 0.0, 0.0, 0.0, 0.0, 4.0, 14.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 18.0), c(0xFFBE80, 0xB8FFE4, 0xA5FFD5, 0x2FFFA7))
    val PLAYER = 130("player", { MIRAGE }, 5, 50, 1.00, m(10.0, 10.0, 10.0, 10.0, 10.0, 10.0), e(10.0, 10.0, 10.0, 0.0, 0.0, 2.0, 0.0, 10.0, 5.0, 0.0, 3.0, 10.0, 3.0, 3.0, 10.0, 10.0, 3.0, 1.0, 0.0, 0.0, 0.0, 3.0, 10.0), c(0xB58D63, 0x00AAAA, 0x322976, 0x4B3422))
    val CHARGED_CREEPER = 131("charged_creeper", { CREEPER }, 5, 41, 1.00, m(0.0, 8.0, 10.0, 13.0, 9.0, 0.0), e(18.0, 1.0, 0.0, 0.0, 5.0, 2.0, 0.0, 0.0, 0.0, 0.0, 5.0, 23.0, 8.0, 0.0, 5.0, 3.0, 9.0, 1.0, 0.0, 0.0, 8.0, 1.0, 4.0), c(0x5BAA53, 0xD6FFCF, 0x5EE74C, 0x7AD5FF))
    val SUGAR_CANE = 132("sugar_cane", { MIRAGE }, 2, 39, 1.00, m(0.1, 0.0, 0.0, 4.0, 10.0, 3.0), e(1.0, 1.0, 2.0, 0.0, 0.0, 3.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0, 2.0, 7.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0), c(0xAADB74, 0xAADB74, 0x96C166, 0x01A900))
    val POTATO = 133("potato", { MIRAGE }, 3, 38, 1.00, m(0.0, 0.0, 0.0, 2.0, 10.0, 0.0), e(0.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 12.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0), c(0xF4EDAD, 0xF2B362, 0xF1AE5B, 0x01A900))
    val NOTE = 134("note", { MIRAGE }, 3, 18, 1.00, m(0.0, 10.0, 2.0, 4.0, 9.0, 4.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 12.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 7.0), c(0xFFFFFF, 0x955A3E, 0x915840, 0xD40102))
    val JUKEBOX = 135("jukebox", { MIRAGE }, 4, 58, 1.00, m(0.1, 6.0, 5.0, 10.0, 13.0, 0.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 16.0, 2.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 8.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 6.0), c(0xFFFFFF, 0x955A3E, 0x915840, 0x30DBBD))
    val NETHER_PORTAL = 136("nether_portal", { MIRAGE }, 3, 37, 1.00, m(2.0, 0.0, 10.0, 10.0, 0.0, 0.0), e(1.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 1.0, 0.0, 14.0, 0.0, 2.0, 0.0, 0.0, 4.0, 1.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xA873DB, 0x491B81, 0x0F0033))
    val MIRAGE = 137("mirage", { null }, 5, 50, 1.00, m(10.0, 5.0, 5.0, 2.0, 2.0, 1.0), e(0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 8.0, 0.0, 0.0, 16.0, 0.0, 0.0, 7.0, 3.0, 2.0, 18.0, 0.0, 6.0, 0.0, 0.0, 0.0, 10.0, 13.0), c(0xFFBE80, 0x84FFFF, 0x66FFFF, 0xFFAFE0))
    val COAL_DUST = 138("coal_dust", { COAL }, 1, 56, 1.00, m(0.0, 11.0, 0.0, 10.0, 0.0, 14.0), e(0.0, 1.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 1.0, 0.0, 10.0, 1.0, 0.0, 0.0, 0.0, 6.0, 0.0), c(0x2B2724, 0x464646, 0x2D2D2D, 0x060606))
    val DIAMOND_DUST = 139("diamond_dust", { DIAMOND }, 4, 61, 1.00, m(10.0, 17.0, 16.0, 56.0, 19.0, 5.0), e(0.0, 1.0, 0.0, 8.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 11.0, 0.0, 8.0, 0.0, 2.0, 0.0, 0.0, 0.0, 13.0, 0.0, 7.0, 0.0), c(0xE9FAFF, 0xE5FFFF, 0xDBE3F4, 0xE6FFF9))
    val BUTTON = 140("button", { MIRAGE }, 1, 22, 1.00, m(0.0, 10.0, 0.0, 8.0, 3.0, 5.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 1.0, 0.0, 0.0, 8.0, 0.0, 10.0), c(0xFFFFFF, 0xBC9862, 0xBC9862, 0xD40102))
    val COOKED_COD = 141("cooked_cod", { COD }, 2, 22, 1.00, m(0.0, 0.0, 0.0, 0.0, 10.0, 2.0), e(0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 12.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xC4B1B4, 0xC4B1B4, 0xC4B1B4, 0x6B9F93))
    val PEONY = 142("peony", { MIRAGE }, 3, 35, 1.00, m(1.0, 0.0, 3.0, 0.0, 10.0, 0.0), e(0.0, 0.0, 1.0, 4.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 8.0, 8.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0), c(0x63D700, 0xEDD6F7, 0xEDD6F7, 0xDEA5F7))
    val BOOK = 143("book", { MIRAGE }, 3, 30, 1.00, m(1.0, 5.0, 5.0, 10.0, 3.0, 2.0), e(1.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 14.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xD6D6D6, 0x654B17, 0x654B17, 0x000000))
    val BEDROCK = 144("bedrock", { MIRAGE }, 4, 96, 1.00, m(5.0, 0.0, 0.0, 0.0, 0.0, 10.0), e(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 7.0, 0.0, 0.0, 0.0, 14.0, 0.0, 0.0, 0.0, 1.0, 0.0), c(0x7C7C7C, 0xB4B4B4, 0x484848, 0x040404))
    val BAT = 145("bat", { MIRAGE }, 4, 28, 1.00, m(0.0, 1.0, 8.0, 0.0, 10.0, 1.0), e(2.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 8.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 6.0, 4.0, 1.0, 4.0, 0.0, 0.0, 0.0, 10.0, 10.0), c(0x4C3E30, 0x5B482B, 0x2C2014, 0x221F1D))
    val CURSE_OF_VANISHING = 146("curse_of_vanishing", { ENCHANT }, 5, 44, 1.00, m(1.0, 22.0, 10.0, 0.0, 0.0, 0.0), e(6.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 13.0, 0.0, 1.0, 0.0, 5.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xD0C2FF, 0xFF003B, 0x370000, 0xE2BBFF))
    val GRAVITY = 147("gravity", { MIRAGE }, 5, 80, 1.00, m(10.0, 20.0, 0.0, 10.0, 0.0, 0.0), e(5.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 23.0, 0.0, 0.0, 3.0, 1.0, 0.0, 0.0, 9.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0), c(0xC2A7F2, 0x3600FF, 0x2A00B1, 0x110047))
    val PYRITE = 148("pyrite", { MIRAGE }, 3, 64, 1.00, m(10.0, 48.0, 0.0, 30.0, 0.0, 82.0), e(0.0, 2.0, 0.0, 6.0, 12.0, 0.0, 9.0, 0.0, 0.0, 5.0, 1.0, 3.0, 4.0, 0.0, 2.0, 8.0, 2.0, 6.0, 0.0, 0.0, 0.0, 0.0, 2.0), c(0xFFECB8, 0xFFFF0B, 0xDC7613, 0xFFEE97))
    val RED_SPINEL = 149("red_spinel", { MIRAGE }, 5, 68, 1.20, m(10.0, 52.0, 16.0, 0.0, 0.0, 41.0), e(2.0, 0.0, 0.0, 3.0, 0.0, 0.0, 12.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 5.0, 6.0, 4.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 7.0), c(0xFF91AA, 0xF62B4B, 0xC8001C, 0xFF3C58))

}
