package miragefairy2019.mod.modules.sphere

import miragefairy2019.mod.lib.UtilsMinecraft
import miragefairy2019.mod.lib.multi.ItemMulti
import miragefairy2019.mod.lib.multi.ItemVariant
import miragefairy2019.modkt.impl.fairy.displayName
import net.minecraft.item.ItemStack

class ItemSpheres : ItemMulti<VariantSphere>() {
    override fun getItemStackDisplayName(itemStack: ItemStack): String {
        val variant = getVariant(itemStack).orElse(null) ?: return UtilsMinecraft.translateToLocal("$unlocalizedName.name")
        return UtilsMinecraft.translateToLocalFormatted("$unlocalizedName.format", variant.sphere.abilityType.displayName.formattedText)
    }
}

class VariantSphere(val sphere: EnumSphere) : ItemVariant()
