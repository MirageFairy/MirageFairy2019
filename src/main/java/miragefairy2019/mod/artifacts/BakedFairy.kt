package miragefairy2019.mod.artifacts

import miragefairy2019.api.ErgSet
import miragefairy2019.api.IAuraItem
import miragefairy2019.api.IFoodAuraItem
import miragefairy2019.api.ManaSet
import miragefairy2019.lib.IColoredItem
import miragefairy2019.lib.RecipeBase
import miragefairy2019.lib.RecipeInput
import miragefairy2019.lib.RecipeMatcher
import miragefairy2019.lib.compound
import miragefairy2019.lib.div
import miragefairy2019.lib.fairySpec
import miragefairy2019.lib.get
import miragefairy2019.lib.int
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.registerItemColorHandler
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.setCompound
import miragefairy2019.lib.setInt
import miragefairy2019.lib.times
import miragefairy2019.lib.toItemStack
import miragefairy2019.lib.toNbt
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.libkt.translateToLocalFormatted
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.fairy.FairyCard
import miragefairy2019.mod.fairy.ItemFairy
import miragefairy2019.mod.fairy.colorSet
import miragefairy2019.mod.fairy.createItemStack
import mirrg.kotlin.hydrogen.castOrNull
import mirrg.kotlin.hydrogen.formatAs
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Items
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class Dressing(val ingredient: Ingredient, val quality: Int)

val dressingRegistry = mutableListOf<Dressing>()


object BakedFairy {
    lateinit var creativeTabBakedFairy: () -> CreativeTabs
    lateinit var itemBakedFairy: () -> ItemBakedFairy
    val bakedFairyModule = module {

        // クリエイティブタブ
        onInitCreativeTab {
            val creativeTab = object : CreativeTabs("mirageFairy2019.bakedFairy") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = ItemStack(itemBakedFairy()).also {
                    ItemBakedFairy.setFairy(it, FairyCard.MAGENTA_GLAZED_TERRACOTTA.createItemStack())
                }
            }
            creativeTabBakedFairy = { creativeTab }
        }
        lang("itemGroup.mirageFairy2019.bakedFairy", "MirageFairy2019: Baked Fairy", "MirageFairy2019：焼き妖精")

        // 焼き妖精
        itemBakedFairy = item({ ItemBakedFairy() }, "baked_fairy") {
            setUnlocalizedName("bakedFairy")
            setCreativeTab { creativeTabBakedFairy() }
            setCustomModelResourceLocation()
            registerItemColorHandler()
        }
        makeItemModel("baked_fairy") {
            DataModel(
                parent = "item/generated",
                textures = mapOf(
                    "layer0" to "miragefairy2019:items/baked_fairy_front",
                    "layer1" to "miragefairy2019:items/fairy_layer0",
                    "layer2" to "miragefairy2019:items/fairy_layer1",
                    "layer3" to "miragefairy2019:items/fairy_layer2",
                    "layer4" to "miragefairy2019:items/fairy_layer3",
                    "layer5" to "miragefairy2019:items/fairy_layer4",
                    "layer6" to "miragefairy2019:items/baked_fairy_back"
                )
            )
        }
        lang("item.bakedFairy.name", "Baked Fairy", "焼き妖精")
        lang("item.bakedFairy.format", "Baked %s", "焼き%s")

        // 焼き妖精レシピ
        onAddRecipe {
            ForgeRegistries.RECIPES.register(RecipeFairyBaking(ResourceLocation(ModMirageFairy2019.MODID, "fairy_baking")))
        }


        // ドレッシング

        // 登録
        onAddRecipe {
            dressingRegistry += Dressing(Items.SUGAR.ingredient, 5)
            dressingRegistry += Dressing("egg".oreIngredient, 7)
            dressingRegistry += Dressing(Items.MILK_BUCKET.ingredient, 8)
            dressingRegistry += Dressing(Items.DYE.createItemStack(metadata = 3).ingredient, 10)
            dressingRegistry += Dressing("mirageFairySyrup".oreIngredient, 20)
        }

        // ツールチップ
        onInit {
            MinecraftForge.EVENT_BUS.register(object {
                @SideOnly(Side.CLIENT)
                @SubscribeEvent
                fun handle(event: ItemTooltipEvent) {
                    val dressing = dressingRegistry.firstOrNull { it.ingredient.test(event.itemStack) } ?: return
                    event.toolTip += formattedText { "品質: ${dressing.quality formatAs "%+d"}"().blue } // TRANSLATE
                }
            })
        }

    }
}

class ItemBakedFairy : ItemFood(0, 0.0f, false), IColoredItem, IFoodAuraItem, IAuraItem {
    companion object {
        fun getFairy(itemStack: ItemStack) = itemStack["Fairy"]["CombinedFairy"].compound?.toItemStack()
        fun setFairy(itemStack: ItemStack, fairyItemStack: ItemStack) = itemStack["Fairy"]["CombinedFairy"].setCompound(fairyItemStack.toNbt())
        fun getQuality(itemStack: ItemStack) = itemStack["Fairy"]["Quality"].int ?: 0
        fun setQuality(itemStack: ItemStack, quality: Int) = itemStack["Fairy"]["Quality"].setInt(quality)
    }


    init {
        setAlwaysEdible()
    }

    override fun getItemStackDisplayName(itemStack: ItemStack): String {
        val fairyItemStack = getFairy(itemStack) ?: return translateToLocal("$unlocalizedName.name")
        return translateToLocalFormatted("$unlocalizedName.format", fairyItemStack.displayName)
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip += formattedText { "品質: ${getQuality(itemStack)}"() }  // TRANSLATE
        tooltip += formattedText { "オーラブースト: ${(1 + 0.01 * getQuality(itemStack)) * 100.0 formatAs "%.0f%%"}"() }  // TRANSLATE
    }

    @SideOnly(Side.CLIENT)
    override fun colorMultiplier(itemStack: ItemStack, tintIndex: Int): Int {
        val fairyItemStack = getFairy(itemStack) ?: return 0xFFFFFF
        val fairyItem = fairyItemStack.item.castOrNull<ItemFairy>() ?: return 0xFFFFFF
        val fairyVariant = fairyItem.getVariant(fairyItemStack) ?: return 0xFFFFFF
        return when (tintIndex) {
            0 -> 0xFFFFFF
            1 -> fairyVariant.colorSet.skin
            2 -> fairyItem.dressColor
            3 -> fairyVariant.colorSet.dark
            4 -> fairyVariant.colorSet.bright
            5 -> fairyVariant.colorSet.hair
            6 -> 0xFFFFFF
            else -> 0xFFFFFF
        }
    }

    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (!isInCreativeTab(tab)) return

        FairyCard.values().forEach { fairyCard ->
            items += ItemStack(this).also {
                setFairy(it, fairyCard.createItemStack())
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


    override fun getFoodAura(itemStack: ItemStack) = getFairy(itemStack)?.fairySpec?.let { it.manaSet * (1 + 0.01 * getQuality(itemStack)) }

    override fun getAuraManaSet(itemStack: ItemStack): ManaSet? {
        val fairySpec = getFairy(itemStack)?.fairySpec ?: return null
        return fairySpec.manaSet / 10.0
    }

    override fun getAuraErgSet(itemStack: ItemStack): ErgSet? {
        val fairySpec = getFairy(itemStack)?.fairySpec ?: return null
        return fairySpec.ergSet / 10.0
    }

}

class RecipeFairyBaking(registryName: ResourceLocation) : RecipeBase<RecipeFairyBaking.Result>(registryName) {
    class Result(val fairy: RecipeInput<Unit>, val quality: Int)

    override fun match(matcher: RecipeMatcher): Result? {

        matcher.pullMatched { "torch".oreIngredient.test(it) } ?: return null
        val fairy = matcher.pullMatched { it.item is ItemFairy } ?: return null
        matcher.pullMatched { Items.BOWL.ingredient.test(it) } ?: return null

        val dressings = mutableListOf<Dressing>()
        run finish@{
            dressingRegistry.forEach { dressing ->
                if (matcher.pullMatched { dressing.ingredient.test(it) } != null) {
                    dressings += dressing
                    if (dressings.size >= 3) return@finish
                }
            }
        }

        if (matcher.hasRemaining()) return null

        return Result(fairy, dressings.sumBy { it.quality })
    }

    override fun canFit(width: Int, height: Int) = width * height >= 2
    override fun getCraftingResult(result: Result): ItemStack {
        val itemStack = BakedFairy.itemBakedFairy().createItemStack()
        ItemBakedFairy.setFairy(itemStack, result.fairy.itemStack.copy(1))
        ItemBakedFairy.setQuality(itemStack, result.quality)
        return itemStack
    }
}
