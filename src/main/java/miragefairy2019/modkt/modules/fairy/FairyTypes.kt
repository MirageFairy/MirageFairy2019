package miragefairy2019.modkt.modules.fairy

import miragefairy2019.libkt.buildText
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.api.fairy.registry.ApiFairyRegistry
import miragefairy2019.modkt.api.erg.ErgTypes
import miragefairy2019.modkt.api.erg.IErgSet
import miragefairy2019.modkt.api.mana.IManaSet
import miragefairy2019.modkt.impl.ManaSet
import miragefairy2019.modkt.impl.div
import miragefairy2019.modkt.impl.fairy.ColorSet
import miragefairy2019.modkt.impl.fairy.ErgEntry
import miragefairy2019.modkt.impl.fairy.ErgSet
import miragefairy2019.modkt.impl.fairy.FairyType
import miragefairy2019.modkt.impl.max
import miragefairy2019.modkt.impl.sum
import miragefairy2019.modkt.impl.times
import mirrg.boron.util.struct.Tuple
import net.minecraft.util.ResourceLocation
import kotlin.math.pow

class RankedFairyTypeBundle(val variants: List<VariantFairy>) {
    val main get() = variants[0]
    operator fun get(i: Int) = variants[i]
}

@Suppress("unused")
class FairyTypes(private val count: Int) {
    companion object {
        lateinit var instance: FairyTypes
    }

    private val variantsImpl = mutableListOf<Tuple<Int, RankedFairyTypeBundle>>() // TODO Tuple -> Pair
    val variants: List<Tuple<Int, RankedFairyTypeBundle>> get() = variantsImpl

    private fun m(shine: Double, fire: Double, wind: Double, gaia: Double, aqua: Double, dark: Double) = ManaSet(shine, fire, wind, gaia, aqua, dark)

    private fun a(vararg powers: Double): IErgSet {
        val types = ErgTypes.values.toList()
        if (powers.size != types.size) throw IllegalArgumentException("Illegal erg count: ${powers.size} != ${types.size}")
        return ErgSet(types.indices.map { i -> ErgEntry(types[i], powers[i]) })
    }

    private fun c(skin: Int, bright: Int, dark: Int, hair: Int) = ColorSet(skin, bright, dark, hair)

    private operator fun Int.invoke(name: String, rare: Int, cost: Int, rateSpecial: Double, manaSet: IManaSet, ergSet: IErgSet, colorSet: ColorSet): RankedFairyTypeBundle {
        val id = this

        fun getType(rank: Int): FairyType {
            val rateRare = 2.0.pow((rare + rank - 2) / 4.0)
            val rateVariance = 0.5.pow(((manaSet / manaSet.max).sum - 1) / 5.0)
            val manaSetReal = manaSet / manaSet.sum * (cost * rateRare * rateVariance * rateSpecial)
            val ergSetReal = ErgSet(ergSet.entries.map { ErgEntry(it.type, it.power * rateRare) })
            return FairyType(ResourceLocation(ModMirageFairy2019.MODID, name), buildText { translate("mirageFairy2019.fairy.$name.name") }, colorSet.hair, cost.toDouble(), manaSetReal, ergSetReal)
        }

        // Create Variants
        val variants = (1..count).map { VariantFairy(id, colorSet, getType(it), rare, it) }
        val bundle = RankedFairyTypeBundle(variants)

        // Register
        variantsImpl += Tuple.of(id, bundle)
        ApiFairyRegistry.getFairyRegistry().registerFairy(bundle.main.type.breed, bundle.main.type, bundle.main.createItemStack())

        return bundle
    }

    val air = 0("air", 1, 15, 1.0, m(0.0, 0.0, 2.0, 0.0, 1.0, 10.0), a(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 2.0, 1.0, 0.0), c(0xFFBE80, 0xDEFFFF, 0xDEFFFF, 0xB0FFFF))
    val water = 1("water", 1, 50, 1.0, m(0.0, 0.0, 1.0, 4.0, 8.0, 10.0), a(1.0, 0.0, 0.0, 0.0, 0.0, 10.0, 0.0, 5.0, 0.0, 0.0, 0.0, 1.0, 7.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0x5469F2, 0x5985FF, 0x172AD3, 0x2D40F4))
    val fire = 2("fire", 2, 20, 1.0, m(1.0, 17.0, 1.0, 0.0, 0.0, 10.0), a(7.0, 2.0, 1.0, 10.0, 15.0, 0.0, 0.0, 6.0, 0.0, 0.0, 0.0, 4.0, 8.0, 0.0, 0.0, 0.0, 10.0, 2.0, 0.0, 0.0, 0.0, 0.0), c(0xFF6C01, 0xF9DFA4, 0xFF7324, 0xFF4000))
    val sun = 3("sun", 5, 99, 1.0, m(10.0, 40.0, 40.0, 20.0, 20.0, 60.0), a(2.0, 0.0, 0.0, 21.0, 6.0, 0.0, 0.0, 6.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 5.0, 1.0, 0.0, 0.0, 0.0, 0.0), c(0xff2f00, 0xff972b, 0xff7500, 0xffe7b2))
    val stone = 4("stone", 1, 83, 1.0, m(0.0, 0.0, 0.0, 8.0, 0.0, 10.0), a(2.0, 0.0, 2.0, 0.0, 0.0, 0.0, 3.0, 2.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 1.0, 0.0, 0.0), c(0x333333, 0x8F8F8F, 0x686868, 0x747474))
    val dirt = 5("dirt", 1, 70, 1.0, m(0.0, 0.0, 0.0, 0.0, 5.0, 10.0), a(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xB87440, 0xB9855C, 0x593D29, 0x914A18))
    val iron = 6("iron", 2, 86, 1.0, m(0.0, 1.0, 0.0, 10.0, 1.0, 4.0), a(2.0, 0.0, 2.0, 1.0, 0.0, 0.0, 4.0, 2.0, 0.0, 0.0, 0.0, 3.0, 0.0, 5.0, 0.0, 0.0, 0.0, 10.0, 0.0, 1.0, 5.0, 0.0), c(0xA0A0A0, 0xD8D8D8, 0x727272, 0xD8AF93))
    val diamond = 7("diamond", 4, 76, 1.0, m(10.0, 13.0, 19.0, 49.0, 23.0, 0.0), a(4.0, 0.0, 2.0, 5.0, 0.0, 0.0, 17.0, 8.0, 0.0, 0.0, 0.0, 10.0, 0.0, 7.0, 0.0, 4.0, 0.0, 2.0, 0.0, 2.0, 0.0, 0.0), c(0x97FFE3, 0xD1FAF3, 0x70FFD9, 0x30DBBD))
    val redstone = 8("redstone", 3, 54, 1.0, m(1.0, 35.0, 11.0, 10.0, 0.0, 6.0), a(0.0, 0.0, 0.0, 7.0, 0.0, 0.0, 6.0, 4.0, 0.0, 1.0, 0.0, 0.0, 4.0, 0.0, 0.0, 4.0, 6.0, 3.0, 0.0, 0.0, 8.0, 0.0), c(0xFF5959, 0xFF0000, 0xCD0000, 0xBA0000))
    val enderman = 9("enderman", 4, 48, 1.0, m(1.0, 12.0, 12.0, 16.0, 10.0, 1.0), a(11.0, 3.0, 5.0, 2.0, 0.0, 1.0, 2.0, 6.0, 1.0, 12.0, 0.0, 4.0, 2.0, 0.0, 2.0, 8.0, 4.0, 8.0, 0.0, 0.0, 0.0, 0.0), c(0x000000, 0x161616, 0x161616, 0xEF84FA))
    val moon = 10("moon", 5, 95, 1.0, m(10.0, 25.0, 25.0, 20.0, 20.0, 90.0), a(0.0, 0.0, 0.0, 6.0, 0.0, 0.0, 2.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 0.0, 0.0, 5.0, 0.0, 0.0), c(0xD9E4FF, 0x747D93, 0x0C121F, 0x2D4272))
    val sand = 11("sand", 1, 64, 1.0, m(0.1, 0.0, 0.0, 4.0, 1.0, 10.0), a(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 7.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0), c(0xB87440, 0xEEE4B6, 0xC2BC84, 0xD8D09B))
    val gold = 12("gold", 3, 93, 1.0, m(1.0, 0.0, 12.0, 10.0, 3.0, 15.0), a(1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 3.0, 12.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0, 0.0, 0.0, 0.0, 3.0, 0.0, 1.0, 6.0, 0.0), c(0xA0A0A0, 0xFFFF0B, 0xDC7613, 0xDEDE00))
    val spider = 13("spider", 2, 43, 1.0, m(0.0, 0.0, 0.0, 0.0, 10.0, 4.0), a(10.0, 0.0, 0.0, 2.0, 0.0, 2.0, 0.0, 2.0, 0.0, 0.0, 0.0, 2.0, 2.0, 1.0, 3.0, 4.0, 2.0, 3.0, 0.0, 0.0, 0.0, 0.0), c(0x494422, 0x61554A, 0x52483F, 0xA80E0E))
    val skeleton = 14("skeleton", 1, 49, 1.0, m(0.0, 8.0, 10.0, 5.0, 0.0, 8.0), a(12.0, 1.0, 3.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 1.0, 12.0, 5.0, 2.0, 0.0, 0.0, 4.0, 3.0, 11.0, 0.0, 1.0, 0.0, 0.0), c(0xCACACA, 0xCFCFCF, 0xCFCFCF, 0x494949))
    val zombie = 15("zombie", 1, 55, 1.0, m(0.0, 9.0, 10.0, 0.0, 2.0, 9.0), a(13.0, 0.0, 1.0, 0.0, 0.0, 4.0, 0.0, 1.0, 2.0, 0.0, 0.0, 2.0, 3.0, 2.0, 4.0, 3.0, 3.0, 9.0, 0.0, 0.0, 0.0, 0.0), c(0x2B4219, 0x00AAAA, 0x322976, 0x2B4219))
    val creeper = 16("creeper", 2, 35, 1.0, m(0.0, 0.0, 10.0, 0.0, 12.0, 4.0), a(10.0, 0.0, 3.0, 0.0, 3.0, 2.0, 0.0, 4.0, 0.0, 0.0, 3.0, 17.0, 6.0, 0.0, 2.0, 3.0, 7.0, 2.0, 0.0, 0.0, 2.0, 0.0), c(0x5BAA53, 0xD6FFCF, 0x5EE74C, 0x000000))
    val wheat = 17("wheat", 2, 31, 1.0, m(0.0, 0.0, 0.0, 0.0, 10.0, 5.0), a(0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 6.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0x168700, 0xD5DA45, 0x716125, 0x9E8714))
    val lilac = 18("lilac", 3, 28, 1.0, m(0.0, 0.0, 1.0, 1.0, 10.0, 0.0), a(0.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 11.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0x63D700, 0xF0C9FF, 0xDC8CE6, 0xA22CFF))
    val torch = 19("torch", 1, 19, 1.0, m(0.1, 1.0, 1.0, 10.0, 4.0, 0.0), a(1.0, 1.0, 2.0, 12.0, 8.0, 0.0, 0.0, 2.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 4.0, 2.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xFFC52C, 0xFF5800, 0xFFE6A5))
    val lava = 20("lava", 2, 58, 1.0, m(0.0, 18.0, 0.0, 4.0, 0.0, 10.0), a(8.0, 1.0, 1.0, 13.0, 18.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 3.0, 3.0, 0.0, 0.0, 0.0, 7.0, 2.0, 0.0, 0.0, 0.0, 0.0), c(0xCD4208, 0xEDB54A, 0xCC4108, 0x4C1500))
    val star = 21("star", 4, 98, 1.0, m(10.0, 30.0, 50.0, 10.0, 30.0, 90.0), a(0.0, 0.0, 0.0, 4.0, 2.0, 0.0, 1.0, 7.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xffffff, 0x2C2C2E, 0x0E0E10, 0x191919))
    val gravel = 22("gravel", 2, 77, 1.0, m(0.0, 0.0, 0.0, 12.0, 0.0, 10.0), a(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0, 3.0, 0.0, 0.0, 0.0, 2.0, 0.0, 1.0, 0.0, 0.0), c(0x333333, 0xC0B5B6, 0x968B8E, 0x63565C))
    val emerald = 23("emerald", 4, 73, 1.0, m(10.0, 0.0, 6.0, 0.0, 42.0, 71.0), a(0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 15.0, 9.0, 0.0, 0.0, 0.0, 8.0, 0.0, 4.0, 0.0, 5.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0), c(0x9FF9B5, 0x81F99E, 0x17DD62, 0x008A25))
    val lapislazuli = 24("lapislazuli", 4, 62, 1.0, m(1.0, 0.0, 8.0, 10.0, 18.0, 0.0), a(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 9.0, 10.0, 0.0, 0.0, 0.0, 3.0, 0.0, 1.0, 0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0xA2B7E8, 0x4064EC, 0x224BD5, 0x0A33C2))
    val enderdragon = 25("enderdragon", 5, 61, 1.0, m(3.0, 0.0, 51.0, 0.0, 10.0, 0.0), a(20.0, 0.0, 0.0, 2.0, 0.0, 2.0, 0.0, 5.0, 0.0, 2.0, 7.0, 9.0, 1.0, 0.0, 2.0, 5.0, 6.0, 4.0, 0.0, 0.0, 0.0, 0.0), c(0x000000, 0x181818, 0x181818, 0xA500E2))
    val witherskeleton = 26("witherskeleton", 4, 69, 1.0, m(0.0, 11.0, 10.0, 4.0, 1.0, 1.0), a(17.0, 1.0, 3.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 7.0, 2.0, 4.0, 1.0, 5.0, 4.0, 13.0, 0.0, 0.0, 0.0, 0.0), c(0x505252, 0x1C1C1C, 0x1C1C1C, 0x060606))
    val wither = 27("wither", 5, 52, 1.0, m(0.0, 8.0, 10.0, 3.0, 1.0, 0.0), a(25.0, 0.0, 3.0, 2.0, 1.0, 1.0, 0.0, 2.0, 0.0, 0.0, 14.0, 15.0, 2.0, 0.0, 1.0, 4.0, 7.0, 9.0, 0.0, 0.0, 3.0, 0.0), c(0x181818, 0x3C3C3C, 0x141414, 0x557272))
    val thunder = 28("thunder", 3, 18, 1.0, m(2.0, 9.0, 3.0, 2.0, 0.0, 10.0), a(9.0, 0.0, 1.0, 8.0, 11.0, 0.0, 0.0, 6.0, 0.0, 4.0, 5.0, 11.0, 11.0, 0.0, 0.0, 0.0, 12.0, 1.0, 0.0, 0.0, 18.0, 0.0), c(0xB4FFFF, 0x4D5670, 0x4D5670, 0xFFEB00))
    val chicken = 29("chicken", 1, 39, 1.0, m(0.0, 0.0, 1.0, 0.0, 10.0, 7.0), a(1.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 1.0, 0.0, 14.0, 3.0, 2.0, 2.0, 0.0, 0.0, 0.0, 0.0), c(0xFFDFA3, 0xFFFFFF, 0xFFFFFF, 0xD93117))
    val furnace = 30("furnace", 2, 72, 1.0, m(0.0, 2.0, 0.0, 10.0, 2.0, 0.0), a(1.0, 10.0, 0.0, 6.0, 10.0, 0.0, 0.0, 1.0, 8.0, 0.0, 0.0, 1.0, 4.0, 0.0, 0.0, 3.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xFF7F19, 0x8E8E8E, 0x383838))
    val magentaglazedterracotta = 31("magentaglazedterracotta", 3, 60, 1.0, m(0.0, 1.0, 0.0, 10.0, 11.0, 0.0), a(0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xF4B5CB, 0xCB58C2, 0x9D2D95))
    val bread = 32("bread", 2, 35, 1.0, m(0.0, 0.0, 0.0, 5.0, 10.0, 0.0), a(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 14.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xCC850C, 0x9E7325, 0x654B17, 0x3F2E0E))
    val daytime = 33("daytime", 1, 88, 1.0, m(1.0, 0.0, 10.0, 3.0, 7.0, 24.0), a(0.0, 0.0, 0.0, 11.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFE260, 0xAACAEF, 0x84B5EF, 0xFFE7B2))
    val night = 34("night", 1, 83, 1.0, m(0.0, 7.0, 10.0, 0.0, 7.0, 24.0), a(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 3.0, 0.0, 4.0, 1.0, 0.0), c(0xFFE260, 0x2C2C2E, 0x0E0E10, 0x2D4272))
    val morning = 35("morning", 2, 85, 1.0, m(1.0, 5.0, 10.0, 1.0, 7.0, 18.0), a(0.0, 0.0, 0.0, 9.0, 1.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 3.0, 0.0, 0.0), c(0xFFE260, 0x91C4D9, 0x4570A6, 0xFF7017))
    val fine = 36("fine", 1, 22, 1.0, m(1.0, 0.0, 10.0, 4.0, 12.0, 28.0), a(0.0, 0.0, 0.0, 17.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xB4FFFF, 0xAACAEF, 0x84B5EF, 0xffe7b2))
    val rain = 37("rain", 2, 25, 1.0, m(0.0, 2.0, 10.0, 0.0, 19.0, 17.0), a(1.0, 0.0, 0.0, 0.0, 0.0, 13.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 4.0, 0.0, 1.0, 4.0, 0.0), c(0xB4FFFF, 0x4D5670, 0x4D5670, 0x2D40F4))
    val plains = 38("plains", 1, 79, 1.0, m(0.0, 0.0, 0.0, 3.0, 18.0, 10.0), a(0.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0x80FF00, 0xD4FF82, 0x86C91C, 0xBB5400))
    val forest = 39("forest", 2, 83, 1.0, m(0.0, 0.0, 2.0, 12.0, 32.0, 10.0), a(1.0, 0.0, 9.0, 0.0, 0.0, 3.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0x80FF00, 0x7B9C62, 0x89591D, 0x2E6E14))
    val apple = 40("apple", 2, 43, 1.0, m(0.1, 0.0, 3.0, 0.0, 10.0, 2.0), a(0.0, 0.0, 4.0, 0.0, 0.0, 2.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 12.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFF755D, 0xFF564E, 0xFF0000, 0x01A900))
    val carrot = 41("carrot", 3, 38, 1.0, m(0.0, 0.0, 1.0, 0.0, 10.0, 0.0), a(0.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 10.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFF8F00, 0xFFAD66, 0xFF9600, 0x01A900))
    val cactus = 42("cactus", 2, 41, 1.0, m(0.0, 7.0, 0.0, 0.0, 10.0, 2.0), a(9.0, 0.0, 1.0, 0.0, 0.0, 4.0, 0.0, 2.0, 0.0, 0.0, 1.0, 2.0, 3.0, 7.0, 4.0, 0.0, 1.0, 4.0, 0.0, 0.0, 0.0, 0.0), c(0x008200, 0xB0FFAC, 0x00E100, 0x010000))
    val axe = 43("axe", 2, 83, 1.0, m(0.0, 7.0, 0.0, 10.0, 2.0, 5.0), a(12.0, 2.0, 12.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 6.0, 0.0, 10.0, 0.0, 2.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xCD9A6A, 0x529B3A, 0xC9D0C6))
    val chest = 44("chest", 1, 31, 1.0, m(0.0, 0.0, 0.0, 10.0, 0.0, 7.0), a(0.0, 1.0, 3.0, 0.0, 0.0, 0.0, 0.0, 1.0, 15.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xFFA431, 0xFFA900, 0xFFC2A5))
    val craftingtable = 45("craftingtable", 2, 40, 1.0, m(0.0, 6.0, 0.0, 10.0, 0.0, 0.0), a(0.0, 12.0, 2.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xFFBB9A, 0xFFC980, 0x000000))
    val potion = 46("potion", 3, 29, 1.0, m(1.0, 2.0, 18.0, 10.0, 3.0, 0.0), a(3.0, 2.0, 0.0, 1.0, 1.0, 6.0, 0.0, 4.0, 2.0, 0.0, 2.0, 0.0, 10.0, 0.0, 1.0, 8.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0x52CAFF, 0x00AEFF, 0xFFFFFF))
    val sword = 47("sword", 2, 62, 1.0, m(0.1, 8.0, 1.0, 10.0, 0.0, 3.0), a(13.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 4.0, 0.0, 12.0, 0.0, 2.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xFFC48E, 0xFF0300, 0xFFFFFF))
    val dispenser = 48("dispenser", 2, 86, 1.0, m(0.0, 10.0, 0.0, 3.0, 0.0, 0.0), a(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 10.0, 3.0, 10.0, 0.0, 0.0, 0.0, 0.0, 2.0, 3.0, 0.0, 0.0, 0.0, 2.0, 0.0), c(0xFFFFFF, 0xD7D7D7, 0x727272, 0x95623C))
    val ocean = 49("ocean", 1, 73, 1.0, m(0.0, 0.0, 0.0, 0.0, 22.0, 10.0), a(0.0, 0.0, 0.0, 0.0, 0.0, 22.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0x80FF00, 0x86B5FF, 0x1D7EFF, 0x004DA5))
    val fish = 50("fish", 2, 29, 1.0, m(0.0, 0.0, 0.0, 0.0, 10.0, 3.0), a(0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 10.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0), c(0x6B9F93, 0x5A867C, 0x43655D, 0xADBEDB))
    val cod = 51("cod", 2, 27, 1.0, m(0.0, 0.0, 0.0, 0.0, 10.0, 3.0), a(0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 11.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0), c(0xC6A271, 0xD6C5AD, 0x986D4E, 0xBEA989))
    val salmon = 52("salmon", 3, 31, 1.0, m(0.0, 0.0, 0.0, 4.0, 10.0, 2.0), a(0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 12.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0), c(0xAB3533, 0xFF6763, 0x6B8073, 0xBD928B))
    val pufferfish = 53("pufferfish", 4, 36, 1.0, m(0.0, 12.0, 0.0, 0.0, 10.0, 0.0), a(11.0, 0.0, 0.0, 0.0, 0.0, 8.0, 0.0, 3.0, 1.0, 0.0, 0.0, 0.0, 7.0, 3.0, 4.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0), c(0xEBDE39, 0xEBC500, 0xBF9B00, 0x429BBA))
    val clownfish = 54("clownfish", 4, 26, 1.0, m(1.0, 0.0, 12.0, 0.0, 10.0, 0.0), a(0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 9.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 7.0, 2.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0), c(0xE46A22, 0xF46F20, 0xA94B1D, 0xFFDBC5))
    val spruce = 55("spruce", 2, 95, 1.0, m(0.0, 0.0, 0.0, 8.0, 10.0, 6.0), a(0.0, 0.0, 8.0, 0.0, 0.0, 1.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0x795C36, 0x583E1F, 0x23160A, 0x4C784C))
    val anvil = 56("anvil", 3, 82, 1.0, m(0.0, 2.0, 0.0, 10.0, 0.0, 2.0), a(2.0, 8.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 9.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xA9A9A9, 0x909090, 0xA86F18))
    val obsidian = 57("obsidian", 3, 78, 1.0, m(1.0, 2.0, 3.0, 10.0, 0.0, 0.0), a(3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10.0, 7.0, 0.0, 0.0, 0.0, 10.0, 0.0, 4.0, 0.0, 0.0, 0.0, 7.0, 0.0, 1.0, 0.0, 0.0), c(0x775599, 0x6029B3, 0x2E095E, 0x0F0033))
    val seed = 58("seed", 1, 17, 1.0, m(0.0, 0.0, 0.0, 0.0, 10.0, 3.0), a(0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 5.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0x03B50A, 0x03FF14, 0x037B0A, 0xAAAE36))
    val enchant = 59("enchant", 3, 13, 1.0, m(2.0, 0.0, 10.0, 0.0, 2.0, 0.0), a(0.0, 6.0, 0.0, 5.0, 0.0, 0.0, 0.0, 7.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 12.0, 2.0, 0.0, 0.0, 0.0, 3.0, 0.0), c(0xD0C2FF, 0xF055FF, 0xC381E3, 0xBE00FF))
    val glowstone = 60("glowstone", 3, 39, 1.0, m(2.0, 0.0, 7.0, 10.0, 0.0, 0.0), a(0.0, 0.0, 0.0, 14.0, 0.0, 0.0, 7.0, 9.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 4.0, 1.0, 0.0, 0.0, 2.0, 0.0), c(0xFFFFB8, 0xFFD15E, 0xFFD244, 0xFFFF00))
    val coal = 61("coal", 1, 61, 1.0, m(0.0, 9.0, 0.0, 10.0, 0.0, 7.0), a(0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 6.0, 2.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 2.0, 10.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0x4C2510, 0x52504C, 0x39352E, 0x150B00))
    val villager = 62("villager", 3, 50, 1.0, m(1.0, 5.0, 5.0, 10.0, 10.0, 15.0), a(4.0, 8.0, 5.0, 0.0, 0.0, 2.0, 0.0, 6.0, 4.0, 0.0, 2.0, 5.0, 3.0, 4.0, 2.0, 7.0, 3.0, 4.0, 0.0, 0.0, 0.0, 0.0), c(0xB58D63, 0x608C57, 0x608C57, 0x009800))
    val librarian = 63("librarian", 4, 42, 1.0, m(3.0, 15.0, 15.0, 1.0, 10.0, 1.0), a(2.0, 4.0, 3.0, 0.0, 0.0, 2.0, 0.0, 8.0, 4.0, 0.0, 1.0, 2.0, 6.0, 2.0, 2.0, 15.0, 3.0, 5.0, 0.0, 0.0, 0.0, 0.0), c(0xB58D63, 0xEBEBEB, 0xEBEBEB, 0x009800))
    val netherstar = 64("netherstar", 5, 41, 1.0, m(2.0, 8.0, 10.0, 0.0, 0.0, 0.0), a(0.0, 0.0, 0.0, 5.0, 0.0, 0.0, 15.0, 9.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10.0, 9.0, 8.0, 0.0, 2.0, 3.0, 0.0), c(0xD8D8FF, 0xF2E3FF, 0xD9E7FF, 0xFFFF68))
    val brewingstand = 65("brewingstand", 3, 26, 1.0, m(1.0, 10.0, 5.0, 7.0, 2.0, 0.0), a(0.0, 10.0, 0.0, 4.0, 9.0, 4.0, 0.0, 2.0, 4.0, 0.0, 0.0, 4.0, 15.0, 0.0, 1.0, 11.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xAE5B5B, 0x7E7E7E, 0xFFDF55))
    val hoe = 66("hoe", 2, 74, 1.0, m(0.0, 0.0, 0.0, 10.0, 8.0, 6.0), a(5.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 4.0, 0.0, 8.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xFFC48E, 0x47FF00, 0xFFFFFF))
    val shield = 67("shield", 2, 81, 1.0, m(0.1, 0.0, 0.0, 10.0, 4.0, 2.0), a(1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xFFC48E, 0x5A5A8E, 0xFFFFFF))
    val hopper = 68("hopper", 3, 63, 1.0, m(0.0, 3.0, 0.0, 10.0, 0.0, 2.0), a(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 9.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0), c(0xFFFFFF, 0x797979, 0x646464, 0x5A5A5A))
    val mina = 69("mina", 1, 50, 0.1, m(0.0, 0.0, 0.0, 0.0, 0.0, 10.0), a(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFF84, 0xFFFF00, 0xFFFF00, 0xFFC800))
    val magnetite = 70("magnetite", 2, 62, 1.0, m(10.0, 18.0, 0.0, 18.0, 0.0, 95.0), a(0.0, 2.0, 4.0, 0.0, 0.0, 0.0, 8.0, 1.0, 0.0, 0.0, 2.0, 3.0, 3.0, 0.0, 0.0, 1.0, 0.0, 3.0, 0.0, 1.0, 0.0, 0.0), c(0x72736D, 0x2A2A26, 0x1F201C, 0x1F201C))
    val sulfur = 71("sulfur", 3, 44, 1.0, m(10.0, 14.0, 0.0, 67.0, 0.0, 83.0), a(4.0, 3.0, 0.0, 3.0, 8.0, 0.0, 6.0, 2.0, 0.0, 0.0, 0.0, 5.0, 11.0, 0.0, 0.0, 3.0, 6.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0xFFF4AA, 0xFFE82C, 0xD6C00E, 0xEAD20F))
    val apatite = 72("apatite", 3, 60, 1.0, m(10.0, 0.0, 0.0, 11.0, 41.0, 63.0), a(0.0, 2.0, 6.0, 2.0, 3.0, 7.0, 12.0, 4.0, 0.0, 0.0, 0.0, 5.0, 4.0, 0.0, 4.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0xCAE6FF, 0x76C1FF, 0x2993EA, 0x80C2FF))
    val cinnabar = 73("cinnabar", 3, 61, 1.0, m(10.0, 61.0, 26.0, 0.0, 0.0, 70.0), a(8.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10.0, 5.0, 0.0, 0.0, 0.0, 3.0, 6.0, 0.0, 0.0, 5.0, 0.0, 2.0, 0.0, 1.0, 0.0, 0.0), c(0xFEC0C0, 0xD41818, 0xBC1C1C, 0xDD2E2E))
    val fluorite = 74("fluorite", 3, 59, 1.0, m(10.0, 26.0, 53.0, 5.0, 0.0, 64.0), a(0.0, 4.0, 0.0, 7.0, 4.0, 0.0, 14.0, 6.0, 0.0, 0.0, 0.0, 4.0, 3.0, 0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0xC0FEF3, 0x28E1C7, 0xAE91E9, 0x66CDEB))
    val moonstone = 75("moonstone", 4, 68, 1.0, m(10.0, 3.0, 38.0, 0.0, 15.0, 43.0), a(0.0, 0.0, 0.0, 9.0, 2.0, 2.0, 13.0, 7.0, 0.0, 4.0, 0.0, 6.0, 0.0, 0.0, 0.0, 5.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0), c(0xEDEFFF, 0xDBF7FE, 0xCCE7FE, 0xE0EEFE))
    val pyrope = 76("pyrope", 4, 71, 1.0, m(10.0, 0.0, 18.0, 0.0, 31.0, 75.0), a(2.0, 0.0, 0.0, 2.0, 4.0, 0.0, 14.0, 7.0, 0.0, 0.0, 0.0, 6.0, 0.0, 5.0, 0.0, 3.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0xF4CDDE, 0xEC97BC, 0xCE1860, 0xD41E6E))
    val smithsonite = 77("smithsonite", 3, 63, 1.0, m(10.0, 13.0, 0.0, 36.0, 0.0, 85.0), a(0.0, 4.0, 2.0, 0.0, 0.0, 0.0, 5.0, 2.0, 0.0, 0.0, 1.0, 4.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0xC1F0E6, 0x95DFD0, 0x33917E, 0x52C6B5))
    val christmas = 78("christmas", 4, 25, 1.2, m(2.0, 3.0, 10.0, 0.0, 7.0, 7.0), a(0.0, 0.0, 0.0, 4.0, 3.0, 4.0, 0.0, 10.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 10.0, 14.0, 0.0, 0.0), c(0xD2E6F6, 0xF1150F, 0xB20000, 0x248541))
    val santaclaus = 79("santaclaus", 5, 12, 1.2, m(4.0, 10.0, 20.0, 5.0, 10.0, 5.0), a(3.0, 6.0, 5.0, 3.0, 0.0, 2.0, 0.0, 10.0, 14.0, 7.0, 4.0, 1.0, 1.0, 1.0, 4.0, 9.0, 5.0, 1.0, 10.0, 6.0, 0.0, 0.0), c(0xCDBBAD, 0xD61728, 0xDA0117, 0xDAD8D4))
    val ice = 80("ice", 1, 45, 1.0, m(10.0, 22.0, 0.0, 63.0, 95.0, 98.0), a(3.0, 0.0, 0.0, 0.0, 0.0, 7.0, 8.0, 5.0, 0.0, 0.0, 0.0, 1.0, 3.0, 0.0, 3.0, 1.0, 0.0, 0.0, 0.0, 10.0, 0.0, 0.0), c(0xCBDBF8, 0xB6CBE3, 0xA1B6DA, 0xA1C0F5))
    val packedice = 81("packedice", 4, 75, 1.0, m(10.0, 22.0, 0.0, 85.0, 63.0, 0.0), a(4.0, 0.0, 0.0, 0.0, 0.0, 8.0, 12.0, 5.0, 0.0, 0.0, 0.0, 2.0, 3.0, 0.0, 2.0, 1.0, 0.0, 0.0, 0.0, 16.0, 0.0, 0.0), c(0xACC8F0, 0x9EB2D6, 0x809DCC, 0x5D86CC))
    val golem = 82("golem", 3, 92, 1.0, m(0.0, 12.0, 10.0, 10.0, 0.0, 1.0), a(21.0, 2.0, 7.0, 0.0, 0.0, 0.0, 0.0, 7.0, 1.0, 0.0, 0.0, 12.0, 0.0, 1.0, 0.0, 4.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0), c(0xC1AB9E, 0xB5ADA8, 0xABA39D, 0x557725))
    val glass = 83("glass", 2, 53, 1.0, m(1.0, 2.0, 0.0, 10.0, 5.0, 3.0), a(2.0, 2.0, 1.0, 0.0, 1.0, 0.0, 8.0, 7.0, 0.0, 0.0, 0.0, 1.0, 8.0, 6.0, 0.0, 1.0, 0.0, 3.0, 0.0, 2.0, 0.0, 0.0), c(0xFFFFFF, 0xEFF5FF, 0xE8EDF5, 0xADE0E9))
    val activatorrail = 84("activatorrail", 3, 77, 1.0, m(0.0, 10.0, 0.0, 7.0, 0.0, 0.0), a(0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0), c(0xFFFFFF, 0xAC8852, 0x686868, 0xD40102))
    val ironbars = 85("ironbars", 2, 64, 1.0, m(0.1, 4.0, 0.0, 10.0, 0.0, 12.0), a(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 16.0, 0.0, 0.0, 0.0, 0.0), c(0xFFFFFF, 0xA1A1A3, 0x404040, 0x404040))
    val taiga = 86("taiga", 2, 88, 1.0, m(0.0, 2.0, 0.0, 19.0, 28.0, 10.0), a(3.0, 0.0, 12.0, 0.0, 0.0, 2.0, 0.0, 3.0, 0.0, 0.0, 1.0, 0.0, 0.0, 3.0, 1.0, 2.0, 4.0, 0.0, 0.0, 7.0, 1.0, 0.0), c(0x80FF00, 0x476545, 0x223325, 0x5A3711))
    val desert = 87("desert", 2, 76, 1.0, m(1.0, 15.0, 0.0, 15.0, 3.0, 10.0), a(5.0, 0.0, 0.0, 8.0, 4.0, 0.0, 4.0, 3.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 2.0, 3.0, 0.0, 0.0, 0.0, 0.0), c(0x80FF00, 0xDDD6A5, 0xD6CE9D, 0x0F6C1C))
    val mountain = 88("mountain", 2, 92, 1.0, m(0.0, 3.0, 0.0, 35.0, 12.0, 10.0), a(4.0, 1.0, 4.0, 0.0, 0.0, 1.0, 2.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 4.0, 2.0, 0.0, 5.0, 1.0, 0.0), c(0x80FF00, 0xB1B0B1, 0x717173, 0x4B794A))
    val time = 89("time", 5, 26, 1.0, m(10.0, 5.0, 5.0, 5.0, 5.0, 10.0), a(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 11.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 11.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0x89D585, 0xD5DEBC, 0xD8DEA7, 0x8DD586))
    val nephrite = 90("nephrite", 3, 59, 1.0, m(1.0, 0.0, 12.0, 10.0, 3.0, 17.0), a(0.0, 13.0, 5.0, 0.0, 0.0, 0.0, 7.0, 9.0, 0.0, 1.0, 0.0, 2.0, 0.0, 0.0, 0.0, 7.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0x94D084, 0xA7D398, 0x88C698, 0x5EA15D))
    val tourmaline = 91("tourmaline", 4, 66, 1.0, m(10.0, 51.0, 0.0, 19.0, 0.0, 42.0), a(0.0, 6.0, 0.0, 1.0, 0.0, 0.0, 11.0, 8.0, 0.0, 4.0, 0.0, 5.0, 5.0, 5.0, 0.0, 6.0, 5.0, 5.0, 0.0, 1.0, 14.0, 0.0), c(0xF3F3F2, 0x3CCB5D, 0xB15498, 0xD7F5FF))
    val topaz = 92("topaz", 4, 72, 1.0, m(10.0, 38.0, 40.0, 0.0, 0.0, 35.0), a(2.0, 0.0, 0.0, 16.0, 11.0, 0.0, 15.0, 10.0, 0.0, 0.0, 0.0, 6.0, 0.0, 6.0, 0.0, 3.0, 8.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0xEED891, 0xEEAF2D, 0xCD7102, 0xFF9800))
    val cow = 93("cow", 1, 54, 1.0, m(0.0, 0.0, 0.0, 2.0, 10.0, 5.0), a(2.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 2.0, 1.0, 0.0, 0.0, 3.0, 2.0, 1.0, 17.0, 3.0, 4.0, 4.0, 0.0, 0.0, 0.0, 0.0), c(0x433626, 0x644B37, 0x4A3828, 0xADADAD))
    val pig = 94("pig", 1, 53, 1.0, m(0.0, 0.0, 0.0, 0.0, 10.0, 6.0), a(1.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 16.0, 3.0, 4.0, 2.0, 0.0, 0.0, 0.0, 0.0), c(0xDB98A2, 0xF68C87, 0xC76B73, 0xDC94A1))
    val sugar = 95("sugar", 1, 21, 1.0, m(0.1, 2.0, 0.0, 2.0, 10.0, 0.0), a(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 6.0, 3.0, 0.0, 0.0, 0.0, 0.0, 2.0, 1.0, 7.0, 7.0, 7.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xE3E3E3, 0xE3E3E3, 0xCECED8, 0xF7F7F7))
    val cake = 96("cake", 3, 47, 1.0, m(0.0, 0.0, 5.0, 1.0, 10.0, 0.0), a(0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 9.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 16.0, 5.0, 8.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0xCC850C, 0xF5F0DC, 0xD3D0BF, 0xDE3334))
    val cookie = 97("cookie", 3, 28, 1.0, m(0.0, 0.0, 0.0, 3.0, 10.0, 0.0), a(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 12.0, 3.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xCC850C, 0xE9994F, 0xDA843C, 0x882500))
    val darkchocolate = 98("darkchocolate", 4, 26, 1.5, m(0.1, 0.0, 2.0, 2.0, 10.0, 8.0), a(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 3.0, 0.0, 12.0, 9.0, 8.0, 0.0, 0.0, 1.0, 0.0, 0.0), c(0x882500, 0x882500, 0x882500, 0x882500))
    val enchantedgoldenapple = 99("enchantedgoldenapple", 5, 90, 1.0, m(3.0, 0.0, 8.0, 1.0, 10.0, 21.0), a(0.0, 3.0, 0.0, 5.0, 0.0, 2.0, 0.0, 15.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 13.0, 10.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFF755D, 0xDEDE00, 0xDEDE00, 0xDE4FD7))
    val fortune = 100("fortune", 4, 28, 1.0, m(3.0, 0.0, 10.0, 0.0, 0.0, 0.0), a(0.0, 4.0, 11.0, 3.0, 0.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0, 9.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xD0C2FF, 0xD0F5FF, 0xDBF7FF, 0xE2BBFF))
    val rottenflesh = 101("rottenflesh", 2, 41, 1.0, m(0.0, 11.0, 8.0, 0.0, 10.0, 12.0), a(3.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 6.0, 0.0, 3.0, 3.0, 0.0, 0.0, 0.0, 0.0), c(0x846129, 0xBD5B2D, 0xBD5B2D, 0xBD422D))
    val poisonouspotato = 102("poisonouspotato", 4, 40, 1.0, m(0.0, 9.0, 0.0, 3.0, 10.0, 0.0), a(4.0, 0.0, 1.0, 0.0, 0.0, 2.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 2.0, 1.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFFE861, 0xC1D245, 0xB7C035, 0x01A900))
    val melon = 103("melon", 3, 56, 1.0, m(0.1, 0.0, 1.0, 2.0, 10.0, 1.0), a(0.0, 0.0, 1.0, 0.0, 0.0, 6.0, 0.0, 3.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 8.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFF5440, 0xA6EE63, 0x195612, 0x01A900))
    val bakedpotato = 104("bakedpotato", 3, 36, 1.0, m(0.0, 0.0, 0.0, 2.0, 10.0, 7.0), a(0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 13.0, 0.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xC87847, 0xFFFF9A, 0xFFFF9A, 0xDEA73A))
    val cookedchicken = 105("cookedchicken", 2, 31, 1.0, m(0.0, 0.0, 0.0, 2.0, 10.0, 9.0), a(0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 14.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xC87847, 0xDA9F7A, 0xDA9F7A, 0x9C5321))
    val cookedsalmon = 106("cookedsalmon", 3, 25, 1.0, m(0.0, 0.0, 0.0, 2.0, 10.0, 3.0), a(0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 13.0, 0.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xC87847, 0xFF7E3B, 0xFF7D41, 0xBA4F23))
    val steak = 107("steak", 2, 43, 1.0, m(0.0, 0.0, 0.0, 2.0, 10.0, 5.0), a(0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 15.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xC87847, 0xAA573F, 0xA4573F, 0x7A3A2A))
    val goldenapple = 108("goldenapple", 3, 72, 1.0, m(1.0, 0.0, 18.0, 4.0, 10.0, 0.0), a(0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 8.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 12.0, 5.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xFF755D, 0xDEDE00, 0xDEDE00, 0x01A900))
    val cupid = 109("cupid", 5, 33, 1.2, m(10.0, 0.0, 24.0, 11.0, 41.0, 0.0), a(6.0, 3.0, 2.0, 6.0, 0.0, 1.0, 0.0, 16.0, 3.0, 8.0, 14.0, 7.0, 2.0, 0.0, 0.0, 11.0, 2.0, 10.0, 0.0, 0.0, 0.0, 0.0), c(0xCDBBAD, 0xE4F4FF, 0xDDEEFF, 0x893F1E))
    val spidereye = 110("spidereye", 3, 34, 1.0, m(0.1, 9.0, 0.0, 0.0, 10.0, 3.0), a(6.0, 0.0, 0.0, 4.0, 0.0, 1.0, 2.0, 3.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 5.0, 4.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0), c(0x494422, 0xC45F6B, 0x65062B, 0x7B0D27))
    val shulker = 111("shulker", 4, 48, 1.0, m(2.0, 0.0, 8.0, 6.0, 10.0, 0.0), a(16.0, 0.0, 0.0, 1.0, 0.0, 2.0, 2.0, 3.0, 12.0, 6.0, 13.0, 2.0, 1.0, 1.0, 3.0, 5.0, 1.0, 4.0, 0.0, 0.0, 0.0, 0.0), c(0xDDE8A7, 0xB27AAA, 0x4B324B, 0x7B4873))
    val slime = 112("slime", 3, 52, 1.0, m(0.0, 6.0, 10.0, 0.0, 4.0, 3.0), a(8.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0, 1.0, 2.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 1.0, 1.0, 2.0, 0.0, 0.0, 0.0, 0.0), c(0x74BE5F, 0x71BC5E, 0x569746, 0x488A36))
    val magmacube = 113("magmacube", 4, 68, 1.0, m(0.0, 9.0, 10.0, 0.0, 0.0, 0.0), a(14.0, 0.0, 0.0, 4.0, 14.0, 0.0, 0.0, 2.0, 1.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 1.0, 8.0, 2.0, 0.0, 0.0, 0.0, 0.0), c(0x3A0000, 0x592301, 0x300000, 0xE35C05))
    val blaze = 114("blaze", 3, 26, 1.0, m(0.1, 15.0, 10.0, 2.0, 0.0, 0.0), a(15.0, 0.0, 0.0, 9.0, 16.0, 0.0, 0.0, 6.0, 0.0, 0.0, 13.0, 1.0, 2.0, 0.0, 2.0, 3.0, 13.0, 3.0, 0.0, 0.0, 0.0, 0.0), c(0xE7DA21, 0xCB6E06, 0xB44500, 0xFF8025))
    val beetroot = 115("beetroot", 3, 35, 1.0, m(0.0, 8.0, 0.0, 4.0, 10.0, 0.0), a(1.0, 0.0, 1.0, 0.0, 11.0, 3.0, 2.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 9.0, 0.0, 2.0, 1.0, 0.0, 0.0, 0.0, 0.0), c(0xC1727C, 0xA74D55, 0x96383D, 0x01A900))
    val pumpkinpie = 116("pumpkinpie", 3, 46, 1.0, m(0.0, 2.0, 4.0, 7.0, 10.0, 1.0), a(0.0, 2.0, 0.0, 0.0, 0.0, 1.0, 0.0, 4.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 15.0, 2.0, 6.0, 0.0, 0.0, 0.0, 0.0, 0.0), c(0xE48A40, 0xF0B96A, 0xB68538, 0xED783F))
    val beetrootsoup = 117("beetrootsoup", 3, 28, 1.0, m(0.0, 7.0, 2.0, 3.0, 10.0, 0.0), a(1.0, 1.0, 0.0, 0.0, 13.0, 7.0, 0.0, 3.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 13.0, 0.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0), c(0xC70623, 0xDF0F27, 0xAC0616, 0x900000))
    val eleven = 118("eleven", 5, 11, 1.0, m(0.0, 99.0, 0.0, 0.0, 10.0, 0.0), a(11.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 7.0, 0.0, 0.0, 0.0, 8.0, 0.0, 1.0, 0.0, 4.0, 2.0, 4.0, 0.0, 3.0, 1.0, 0.0), c(0x515151, 0x000000, 0x000000, 0x60F3D4))
    val imperialtopaz = 119("imperialtopaz", 5, 82, 1.2, m(10.0, 15.0, 23.0, 0.0, 26.0, 0.0), a(0.0, 1.0, 0.0, 18.0, 6.0, 0.0, 15.0, 12.0, 0.0, 0.0, 0.0, 2.0, 0.0, 6.0, 0.0, 5.0, 11.0, 12.0, 0.0, 1.0, 0.0, 0.0), c(0xFFD6AD, 0xFFCD94, 0xE89F4E, 0xFFB37E))

}
