package miragefairy2019.mod.material

import miragefairy2019.libkt.ItemMultiMaterial
import miragefairy2019.libkt.ItemVariantMaterial
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.IFuelItem
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemSimpleMaterials : ItemMultiMaterial<ItemVariantSimpleMaterials>(), IFuelItem {
    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val variant = getVariant(itemStack) ?: return

        // ポエム
        if (canTranslate("${getUnlocalizedName(itemStack)}.poem")) {
            val string = translateToLocal("${getUnlocalizedName(itemStack)}.poem")
            if (string.isNotEmpty()) tooltip += string
        }

        // Tier
        tooltip += formattedText { "Tier ${variant.commonMaterialCard.tier}"().aqua }

    }

    override fun getItemBurnTime(itemStack: ItemStack) = getVariant(itemStack)?.burnTime ?: -1
}

class ItemVariantSimpleMaterials(val commonMaterialCard: CommonMaterialCard) : ItemVariantMaterial(commonMaterialCard.registryName, commonMaterialCard.unlocalizedName) {
    var burnTime: Int? = null
}
