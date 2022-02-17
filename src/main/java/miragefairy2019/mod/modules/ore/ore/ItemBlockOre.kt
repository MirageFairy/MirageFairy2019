package miragefairy2019.mod.modules.ore.ore

import miragefairy2019.mod.lib.ItemBlockMulti

class ItemBlockOre<V : IBlockVariantOre>(block: BlockOre<V>) : ItemBlockMulti<BlockOre<V>, V>(block)
