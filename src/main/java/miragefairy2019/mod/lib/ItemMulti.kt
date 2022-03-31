package miragefairy2019.mod.lib

import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.translateToLocal
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

open class ItemMulti<V : ItemVariant> : Item() {
    init {
        setHasSubtypes(true)
        maxDamage = 0
    }

    //

    private val map = mutableMapOf<Int, V>()

    fun registerVariant(metadata: Int, variant: V) {
        require(metadata !in map) { "Illegal metadata: $metadata" }
        map[metadata] = variant
        variant.item = this
        variant.metadata = metadata
    }

    fun getVariant(itemStack: ItemStack) = getVariant(itemStack.metadata)

    fun getVariant(metadata: Int) = map[metadata]

    val variants get() = map.values

    //

    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (!isInCreativeTab(tab)) return
        map.forEach { (metadata, variant) ->
            if (variant.canSeeOnCreativeTab) items += createItemStack(1, metadata)
        }
    }
}

open class ItemVariant {
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    private lateinit var iMetadata: Integer
    var metadata
        get() = iMetadata.toInt()
        set(it) {
            iMetadata = Integer(it)
        }
    lateinit var item: Item

    fun createItemStack(amount: Int = 1) = item.createItemStack(amount, metadata)
    var canSeeOnCreativeTab = true
}


// Material

open class ItemMultiMaterial<V : ItemVariantMaterial> : ItemMulti<V>() {
    override fun getUnlocalizedName(itemStack: ItemStack): String {
        val variant = getVariant(itemStack) ?: return "item.null"
        return "item.${variant.unlocalizedName}"
    }

    // TODO 子クラスに移動
    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        getVariant(itemStack) ?: return

        // ポエム
        val key = "${getUnlocalizedName(itemStack)}.poem"
        if (canTranslate(key)) {
            val string = translateToLocal(key)
            if (string.isNotEmpty()) tooltip += string
        }

    }
}

@SideOnly(Side.CLIENT)
fun <V : ItemVariantMaterial> ItemMultiMaterial<V>.setCustomModelResourceLocations() = variants.forEach { variant ->
    ModelLoader.setCustomModelResourceLocation(
        this,
        variant.metadata,
        ModelResourceLocation(ResourceLocation(registryName!!.resourceDomain, variant.registryName), "normal")
    )
}

open class ItemVariantMaterial(val registryName: String, val unlocalizedName: String) : ItemVariant()
