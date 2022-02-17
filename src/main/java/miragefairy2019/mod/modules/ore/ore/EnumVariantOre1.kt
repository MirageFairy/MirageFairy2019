package miragefairy2019.mod.modules.ore.ore

import miragefairy2019.libkt.getItemStack
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.lib.BlockVariantList
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.util.IStringSerializable
import net.minecraft.util.NonNullList
import net.minecraft.util.math.MathHelper
import java.util.Random

enum class EnumVariantOre1(
    override val metadata: Int,
    override val resourceName: String,
    override val unlocalizedName: String,
    @JvmField val hardness: Float,
    @JvmField val resistance: Float,
    @JvmField val harvestLevel: Int,
    private val gemProvider: GemProvider
) : IStringSerializable, IBlockVariantOre {
    APATITE_ORE(0, "apatite_ore", "oreApatite", 3f, 5f, 1, GemProvider({ getItemStack("gemApatite") }, 1.0, 1.5, 1, 3)),
    FLUORITE_ORE(1, "fluorite_ore", "oreFluorite", 3f, 5f, 2, GemProvider({ getItemStack("gemFluorite") }, 1.0, 1.0, 15, 30)),
    SULFUR_ORE(2, "sulfur_ore", "oreSulfur", 3f, 5f, 1, GemProvider({ getItemStack("gemSulfur") }, 1.0, 1.5, 1, 3)),
    CINNABAR_ORE(3, "cinnabar_ore", "oreCinnabar", 3f, 5f, 2, GemProvider({ getItemStack("gemCinnabar") }, 1.0, 1.0, 1, 3)),
    MOONSTONE_ORE(4, "moonstone_ore", "oreMoonstone", 3f, 5f, 2, GemProvider({ getItemStack("gemMoonstone") }, 1.0, 0.5, 20, 40)),
    MAGNETITE_ORE(5, "magnetite_ore", "oreMagnetite", 3f, 5f, 1, GemProvider({ getItemStack("gemMagnetite") }, 1.0, 2.0, 1, 2)),

    PYROPE_ORE(6, "pyrope_ore", "orePyrope", 3f, 5f, 2, GemProvider({ getItemStack("gemPyrope") }, 1.0, 0.5, 1, 5)),
    SMITHSONITE_ORE(7, "smithsonite_ore", "oreSmithsonite", 3f, 5f, 1, GemProvider({ getItemStack("gemSmithsonite") }, 1.0, 1.0, 1, 3)),

    NETHERRACK_APATITE_ORE(8, "netherrack_apatite_ore", "oreApatite", 0.4f, 0.4f, 1, GemProvider({ getItemStack("gemApatite") }, 1.0, 1.5, 1, 3)),
    NETHERRACK_FLUORITE_ORE(9, "netherrack_fluorite_ore", "oreFluorite", 0.4f, 0.4f, 2, GemProvider({ getItemStack("gemFluorite") }, 1.0, 1.0, 15, 30)),
    NETHERRACK_SULFUR_ORE(10, "netherrack_sulfur_ore", "oreSulfur", 0.4f, 0.4f, 1, GemProvider({ getItemStack("gemSulfur") }, 1.0, 1.5, 1, 3)),
    NETHERRACK_CINNABAR_ORE(11, "netherrack_cinnabar_ore", "oreCinnabar", 0.4f, 0.4f, 2, GemProvider({ getItemStack("gemCinnabar") }, 1.0, 1.0, 1, 3)),
    NETHERRACK_MOONSTONE_ORE(12, "netherrack_moonstone_ore", "oreMoonstone", 0.4f, 0.4f, 2, GemProvider({ getItemStack("gemMoonstone") }, 1.0, 0.5, 20, 40)),
    NETHERRACK_MAGNETITE_ORE(13, "netherrack_magnetite_ore", "oreMagnetite", 0.4f, 0.4f, 1, GemProvider({ getItemStack("gemMagnetite") }, 1.0, 2.0, 1, 2)),

    NEPHRITE_ORE(14, "nephrite_ore", "oreNephrite", 3f, 5f, 1, GemProvider({ getItemStack("gemNephrite") }, 1.0, 2.0, 1, 3)),
    TOPAZ_ORE(15, "topaz_ore", "oreTopaz", 3f, 5f, 2, GemProvider({ getItemStack("gemTopaz") }, 1.0, 0.5, 1, 5)),
    ;

    class GemProvider(val itemStackSupplier: () -> ItemStack, val amount: Double, val amountPerFortune: Double, val expMin: Int, val expMax: Int)

    override fun toString() = resourceName
    override fun getName() = resourceName
    override fun getHardness() = hardness
    override fun getResistance() = resistance
    override fun getHarvestLevel() = harvestLevel

    override fun getDrops(drops: NonNullList<ItemStack>, random: Random, block: Block, metadata: Int, fortune: Int) {
        repeat(random.randomInt(gemProvider.amount + random.nextDouble() * gemProvider.amountPerFortune * fortune)) {
            drops += gemProvider.itemStackSupplier().copy()
        }
    }

    override fun getExpDrop(random: Random, fortune: Int) = MathHelper.getInt(random, gemProvider.expMin, gemProvider.expMax)

    companion object {
        val variantList = BlockVariantList(values().toList())
    }
}
