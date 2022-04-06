package miragefairy2019.mod.artifacts

import miragefairy2019.api.IFoodAuraItem
import miragefairy2019.lib.RecipeInput
import miragefairy2019.lib.RecipeMatcher
import miragefairy2019.lib.div
import miragefairy2019.lib.fairyType
import miragefairy2019.lib.toNonNullList
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemStacks
import miragefairy2019.libkt.module
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.libkt.translateToLocalFormatted
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.fairy.Fairy
import miragefairy2019.mod.fairy.FairyTypes
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Items
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.util.Constants
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.registries.IForgeRegistryEntry

object BakedFairy {
    lateinit var creativeTabBakedFairy: () -> CreativeTabs
    lateinit var itemBakedFairy: () -> ItemBakedFairy
    val module = module {

        // クリエイティブタブ
        onInitCreativeTab {
            val creativeTab = object : CreativeTabs("mirageFairy2019.bakedFairy") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = ItemStack(itemBakedFairy()).also {
                    ItemBakedFairy.setFairy(it, FairyTypes.instance.magentaGlazedTerracotta.main.createItemStack())
                }
            }
            creativeTabBakedFairy = { creativeTab }
        }

        // 焼き妖精
        itemBakedFairy = item({ ItemBakedFairy() }, "baked_fairy") {
            setUnlocalizedName("bakedFairy")
            setCreativeTab { creativeTabBakedFairy() }
            modInitializer.onRegisterItem {
                if (Main.side.isClient) {
                    ModelLoader.setCustomModelResourceLocation(item, 0, ModelResourceLocation(item.registryName!!, "normal"))
                }
            }
        }

        // 焼き妖精のカスタム色
        onRegisterItemColorHandler {
            object {
                @SideOnly(Side.CLIENT)
                fun run() {
                    @SideOnly(Side.CLIENT)
                    class ItemColorImpl : IItemColor {
                        override fun colorMultiplier(itemStack: ItemStack, tintIndex: Int): Int {
                            val fairyItemStack = (ItemBakedFairy.getFairy(itemStack) ?: return 0xFFFFFF)
                            val variant = Fairy.listItemFairy[0].getVariant(fairyItemStack) ?: return 0xFFFFFF
                            return when (tintIndex) {
                                0 -> 0xFFFFFF
                                1 -> variant.colorSet.skin
                                2 -> 0xFFFFFF
                                3 -> variant.colorSet.dark
                                4 -> variant.colorSet.bright
                                5 -> variant.colorSet.hair
                                6 -> 0xFFFFFF
                                else -> 0xFFFFFF
                            }
                        }
                    }
                    Minecraft.getMinecraft().itemColors.registerItemColorHandler(ItemColorImpl(), itemBakedFairy())
                }
            }.run()
        }

        // 焼き妖精レシピ
        onAddRecipe {
            ForgeRegistries.RECIPES.register(RecipeFairyBaking())
        }

    }
}

class ItemBakedFairy : ItemFood(0, 0.0f, false), IFoodAuraItem {
    companion object {
        fun getFairy(itemStack: ItemStack): ItemStack? {
            if (!itemStack.hasTagCompound()) return null
            val nbt = itemStack.tagCompound!!
            if (!nbt.hasKey("Fairy", Constants.NBT.TAG_COMPOUND)) return null
            val fairy = nbt.getCompoundTag("Fairy")
            if (!fairy.hasKey("CombinedFairy", Constants.NBT.TAG_COMPOUND)) return null
            return ItemStack(fairy.getCompoundTag("CombinedFairy"))
        }

        fun setFairy(itemStack: ItemStack, itemStackFairy: ItemStack) {
            if (!itemStack.hasTagCompound()) itemStack.tagCompound = NBTTagCompound()
            val nbt = itemStack.tagCompound!!
            if (!nbt.hasKey("Fairy", Constants.NBT.TAG_COMPOUND)) nbt.setTag("Fairy", NBTTagCompound())
            val fairy = nbt.getCompoundTag("Fairy")
            fairy.setTag("CombinedFairy", itemStackFairy.writeToNBT(NBTTagCompound()))
        }
    }


    override fun getItemStackDisplayName(itemStack: ItemStack): String = getFairy(itemStack)?.fairyType?.let { translateToLocalFormatted("$unlocalizedName.format", it.displayName.formattedText) } ?: translateToLocal("$unlocalizedName.name")

    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (!isInCreativeTab(tab)) return

        FairyTypes.instance.variants.forEach { (_, fairy) ->
            items += ItemStack(this).also {
                setFairy(it, fairy.main.createItemStack())
            }
        }

    }


    override fun getHealAmount(itemStack: ItemStack) = 2
    override fun getSaturationModifier(itemStack: ItemStack) = getHealAmount(itemStack) * 0.1f
    override fun getMaxItemUseDuration(itemStack: ItemStack) = 16
    override fun onItemUseFinish(itemStack: ItemStack, world: World, entity: EntityLivingBase): ItemStack {
        super.onItemUseFinish(itemStack, world, entity)
        return if (!itemStack.isEmpty) {
            if (!world.isRemote) ItemStack(Items.BOWL).drop(world, entity.positionVector, motionless = true, noPickupDelay = true)
            itemStack
        } else {
            ItemStack(Items.BOWL)
        }
    }


    override fun getFoodAura(itemStack: ItemStack) = getFairy(itemStack)?.fairyType?.let { it.manaSet / (it.cost / 50.0) }
}

class RecipeFairyBaking : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
    init {
        registryName = ResourceLocation(ModMirageFairy2019.MODID, "fairy_baking")
    }


    // match

    private class Result(val fairy: RecipeInput<Unit>)

    private fun match(inventoryCrafting: InventoryCrafting): Result? {
        val matcher = RecipeMatcher(inventoryCrafting)

        matcher.pullMatched { "torch".oreIngredient.test(it) } ?: return null
        val fairy = matcher.pullMatched { it.item == Fairy.listItemFairy[0] } ?: return null
        matcher.pullMatched { Items.BOWL.ingredient.test(it) } ?: return null


        if (matcher.hasRemaining()) return null

        return Result(fairy)
    }


    // overrides

    override fun isDynamic() = true
    override fun canFit(width: Int, height: Int) = width * height >= 2
    override fun matches(inventoryCrafting: InventoryCrafting, world: World) = match(inventoryCrafting) != null
    override fun getRecipeOutput(): ItemStack = ItemStack.EMPTY

    override fun getCraftingResult(inventoryCrafting: InventoryCrafting): ItemStack {
        val result = match(inventoryCrafting) ?: return ItemStack.EMPTY
        return BakedFairy.itemBakedFairy().createItemStack().also { ItemBakedFairy.setFairy(it, result.fairy.itemStack.copy(1)) }
    }

    override fun getRemainingItems(inventoryCrafting: InventoryCrafting): NonNullList<ItemStack> {
        if (match(inventoryCrafting) == null) return NonNullList.create()
        return inventoryCrafting.itemStacks.map { itemStack ->
            ForgeHooks.getContainerItem(itemStack)
        }.toNonNullList()
    }

}
