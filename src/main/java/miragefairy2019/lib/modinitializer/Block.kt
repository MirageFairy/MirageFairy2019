package miragefairy2019.lib.modinitializer

import miragefairy2019.common.ResourceName
import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraftforge.fml.common.registry.ForgeRegistries

class BlockInitializer<out B : Block>(override val modInitializer: ModInitializer, override val resourceName: ResourceName, getter: () -> B) : Initializer<B>(getter), NamedInitializer {
    val block get() = initializingObject
}

fun <B : Block> ModInitializer.block(creator: () -> B, registryName: String, block: (BlockInitializer<B>.() -> Unit)? = null): BlockInitializer<B> {
    lateinit var block2: B
    onRegisterBlock {
        block2 = creator()
        block2.setRegistryName(ModMirageFairy2019.MODID, registryName)
        ForgeRegistries.BLOCKS.register(block2)
    }
    return BlockInitializer(this, ResourceName(ModMirageFairy2019.MODID, registryName)) { block2 }.also {
        if (block != null) it.block()
    }
}

fun <B : Block> BlockInitializer<B>.setUnlocalizedName(unlocalizedName: String) = modInitializer.onRegisterItem { block.unlocalizedName = unlocalizedName }
fun <B : Block> BlockInitializer<B>.setCreativeTab(creativeTab: () -> CreativeTabs) = modInitializer.onRegisterItem { block.setCreativeTab(creativeTab()) }
