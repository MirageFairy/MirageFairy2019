package miragefairy2019.mod.lib.multi

import miragefairy2019.libkt.createItemStack
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

open class BlockMulti<V : IBlockVariant>(material: Material, val variantList: BlockVariantList<V>) : Block(material) {
    init {
        defaultState = blockState.baseState.withProperty(VARIANT, variantList.defaultMetadata)
    }


    override fun createBlockState() = BlockStateContainer(this, VARIANT)
    fun getState(variant: V): IBlockState = defaultState.withProperty(VARIANT, variant.metadata)
    override fun getStateFromMeta(metadata: Int): IBlockState = defaultState.withProperty(VARIANT, metadata)
    override fun getMetaFromState(blockState: IBlockState): Int = blockState.getValue(VARIANT)
    fun getVariant(state: IBlockState) = getVariant(getMetaFromState(state))
    fun getVariant(metadata: Int) = variantList.byMetadata(metadata)
    override fun getSubBlocks(creativeTab: CreativeTabs, itemStacks: NonNullList<ItemStack>) {
        variantList.blockVariants.forEach { variant ->
            itemStacks += ItemStack(this, 1, variant.metadata)
        }
    }


    override fun damageDropped(blockState: IBlockState) = getMetaFromState(blockState)
    private fun getItem(blockState: IBlockState) = createItemStack(metadata = damageDropped(blockState))
    override fun getItem(world: World, blockPos: BlockPos, blockState: IBlockState) = getItem(blockState)
    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, blockPos: BlockPos, blockState: IBlockState, fortune: Int) {
        drops += getItem(blockState)
    }


    companion object {
        val VARIANT: PropertyInteger = PropertyInteger.create("variant", 0, 15)
    }
}

interface IBlockVariant {
    val metadata: Int
    val resourceName: String
    val unlocalizedName: String
}

class BlockVariantList<V : IBlockVariant>(
    val blockVariants: List<V>,
    val defaultMetadata: Int = 0
) {
    private val metaLookup = mutableMapOf<Int, V>().also { map ->
        blockVariants.forEach { variant ->
            require(!map.containsKey(variant.metadata))
            map[variant.metadata] = variant
        }
    }

    fun byMetadata(metadata: Int) = metaLookup[metadata] ?: blockVariants[defaultMetadata]
}

open class ItemBlockMulti<B : BlockMulti<V>, V : IBlockVariant>(protected var block2: B) : ItemBlock(block2) {
    init {
        maxDamage = 0
        setHasSubtypes(true)
    }

    override fun getMetadata(meta: Int) = meta
    override fun getUnlocalizedName(itemStack: ItemStack) = "tile.${block2.getVariant(itemStack.metadata).unlocalizedName}"
}
