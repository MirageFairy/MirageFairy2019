package miragefairy2019.mod.modules.ore.ore

import miragefairy2019.libkt.getItemStack
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.lib.BlockVariantList
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.util.IStringSerializable
import net.minecraft.util.math.MathHelper
import java.util.Random

enum class EnumVariantOre2(
    override val metadata: Int,
    override val resourceName: String,
    override val unlocalizedName: String,
    override val hardness: Float,
    override val resistance: Float,
    override val harvestLevel: Int,
    private val gemProvider: GemProvider
) : IStringSerializable, IBlockVariantOre {
    TOURMALINE_ORE(0, "tourmaline_ore", "oreTourmaline", 3f, 5f, 2, GemProvider({ getItemStack("gemTourmaline") }, 1.0, 0.5, 1, 5)),
    HELIOLITE_ORE(1, "heliolite_ore", "oreHeliolite", 3f, 5f, 2, GemProvider({ getItemStack("gemHeliolite") }, 1.0, 0.5, 10, 20)),
    END_STONE_LABRADORITE_ORE(2, "end_stone_labradorite_ore", "oreLabradorite", 3f, 5f, 2, GemProvider({ getItemStack("gemLabradorite") }, 1.0, 0.5, 15, 30)),
    ;

    class GemProvider(val itemStackSupplier: () -> ItemStack, val amount: Double, val amountPerFortune: Double, val expMin: Int, val expMax: Int)

    override fun toString() = resourceName
    override fun getName() = resourceName

    override fun getDrops(random: Random, block: Block, metadata: Int, fortune: Int): List<ItemStack> {
        return (0 until random.randomInt(gemProvider.amount + random.nextDouble() * gemProvider.amountPerFortune * fortune)).map {
            gemProvider.itemStackSupplier().copy()
        }
    }

    override fun getExpDrop(random: Random, fortune: Int) = MathHelper.getInt(random, gemProvider.expMin, gemProvider.expMax)

    companion object {
        val variantList = BlockVariantList(values().toList())
    }
}
