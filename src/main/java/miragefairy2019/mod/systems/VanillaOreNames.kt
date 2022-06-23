package miragefairy2019.mod.systems

import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.module
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraftforge.oredict.OreDictionary

val vanillaOreNamesModule = module {
    onCreateItemStack {
        OreDictionary.registerOre("container1000Water", Items.WATER_BUCKET)
        OreDictionary.registerOre("container1000Lava", Items.LAVA_BUCKET)
        OreDictionary.registerOre("wool", Blocks.WOOL.createItemStack(metadata = OreDictionary.WILDCARD_VALUE))
        OreDictionary.registerOre("ice", Blocks.ICE.createItemStack())
        OreDictionary.registerOre("gemCoal", Items.COAL.createItemStack(metadata = 0))
        OreDictionary.registerOre("gemCharcoal", Items.COAL.createItemStack(metadata = 1))
    }
}
