package miragefairy2019.libkt

import miragefairy2019.common.OreName
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

fun OreName.getItemStacks(): List<ItemStack> = OreDictionary.getOres(this.string)

@Deprecated(message = "下位素材の辞書名を経由して過剰な品質の素材が返却される可能性があります。ワイルドカードにより不正なアイテムが返却される可能性があります。")
fun OreName.copyItemStack(count: Int = 1) = getItemStacks().firstOrNull()?.copy(count)
