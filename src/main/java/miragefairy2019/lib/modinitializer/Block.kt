package miragefairy2019.lib.modinitializer

import miragefairy2019.common.ResourceName
import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraftforge.fml.common.registry.ForgeRegistries

class BlockInitializer<out B : Block>(override val modInitializer: ModInitializer, override val resourceName: ResourceName, getter: () -> B) : Initializer<B>(getter), NamedInitializer {
    val block get() = initializingObject
}

fun <B : Block> ModInitializer.block(creator: () -> B, registryName: String, initializer: (BlockInitializer<B>.() -> Unit)? = null): BlockInitializer<B> {
    lateinit var block: B
    onRegisterBlock {
        block = creator()
        block.setRegistryName(ModMirageFairy2019.MODID, registryName)
        ForgeRegistries.BLOCKS.register(block)
    }
    return BlockInitializer(this, ResourceName(modId, registryName)) { block }.also {
        if (initializer != null) it.initializer()
    }
}

fun <B : Block> BlockInitializer<B>.setUnlocalizedName(unlocalizedName: String) = modInitializer.onRegisterItem { block.unlocalizedName = unlocalizedName }
fun <B : Block> BlockInitializer<B>.setCreativeTab(creativeTab: () -> CreativeTabs) = modInitializer.onRegisterItem { block.setCreativeTab(creativeTab()) }
