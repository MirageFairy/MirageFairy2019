package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.item
import miragefairy2019.libkt.module
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.lib.BakedModelBuiltinWrapper
import miragefairy2019.mod3.artifacts.oreName
import miragefairy2019.mod3.artifacts.sphereType
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.erg.api.EnumErgType.ATTACK
import miragefairy2019.mod3.erg.api.EnumErgType.CHRISTMAS
import miragefairy2019.mod3.erg.api.EnumErgType.CRYSTAL
import miragefairy2019.mod3.erg.api.EnumErgType.DESTROY
import miragefairy2019.mod3.erg.api.EnumErgType.ENERGY
import miragefairy2019.mod3.erg.api.EnumErgType.HARVEST
import miragefairy2019.mod3.erg.api.EnumErgType.KNOWLEDGE
import miragefairy2019.mod3.erg.api.EnumErgType.LIFE
import miragefairy2019.mod3.erg.api.EnumErgType.LIGHT
import miragefairy2019.mod3.erg.api.EnumErgType.SLASH
import miragefairy2019.mod3.erg.api.EnumErgType.SOUND
import miragefairy2019.mod3.erg.api.EnumErgType.THUNDER
import miragefairy2019.mod3.erg.api.EnumErgType.WARP
import miragefairy2019.mod3.erg.api.EnumErgType.WATER
import miragefairy2019.mod3.main.api.ApiMain.creativeTab
import miragefairy2019.mod3.main.api.ApiMain.side
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.oredict.OreDictionary

private fun <T : ItemFairyWeapon> ModInitializer.fw(
    tier: Int,
    creator: () -> T,
    registryName: String,
    unlocalizedName: String,
    oreNameList: List<String>,
    parent: (() -> () -> ItemFairyWeapon)?,
    vararg manualRepairIngredientSuppliers: () -> Ingredient
) = item(creator, registryName) {
    setUnlocalizedName(unlocalizedName)
    setCreativeTab { creativeTab }
    onRegisterItem {
        if (side.isClient) {
            val modelResourceLocation = ModelResourceLocation(item.registryName!!, "normal")
            MinecraftForge.EVENT_BUS.register(object : Any() {
                @SubscribeEvent
                fun accept(event: ModelBakeEvent) {
                    event.modelRegistry.putObject(modelResourceLocation, BakedModelBuiltinWrapper(event.modelRegistry.getObject(modelResourceLocation)!!))
                }
            })
            ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation)
        }
    }
    onInit {
        val durability = (1..tier).fold(16) { a, b -> a * 2 }
        item.maxDamage = durability - 1
        item.tier = tier
    }
    onCreateItemStack {
        if (parent != null) item.manualRepairIngredients += parent()().manualRepairIngredients
        manualRepairIngredientSuppliers.forEach { item.manualRepairIngredients += it() }
        oreNameList.forEach { OreDictionary.registerOre(it, item.createItemStack(metadata = OreDictionary.WILDCARD_VALUE)) }
    }
}


lateinit var fairyWeaponLoader: FairyWeaponLoader

@Suppress("MemberVisibilityCanBePrivate", "unused")
class FairyWeaponLoader(m: ModInitializer) {
    operator fun EnumErgType.not(): () -> Ingredient = { sphereType.oreName.oreIngredient }
    operator fun String.not(): () -> Ingredient = { oreIngredient }

    val miragiumSword = m.fw(2, ::ItemFairyWeapon, "miragium_sword", "miragiumSword", listOf(), null, !ATTACK, !SLASH)
    val crystalSword = m.fw(3, ::ItemCrystalSword, "crystal_sword", "crystalSword", listOf(), { miragiumSword }, !CRYSTAL)
    val fairySword = m.fw(3, ::ItemFairySword, "fairy_sword", "fairySword", listOf(), { miragiumSword }, !ATTACK)

    val miragiumAxe = m.fw(2, ::ItemMiragiumAxe, "miragium_axe", "miragiumAxe", listOf(), null, !SLASH, !HARVEST, !"plateMiragium")

    val magicWandBase = m.fw(3, ::ItemFairyWeapon, "magic_wand_base", "magicWandBase", listOf(), null, !KNOWLEDGE)
    val magicWandLight = m.fw(3, ::ItemMagicWandLight, "light_magic_wand", "magicWandLight", listOf(), { magicWandBase }, !LIGHT)
    val magicWandCollecting = m.fw(3, ::ItemMagicWandCollecting, "collecting_magic_wand", "magicWandCollecting", listOf(), { magicWandBase }, !WARP)
    val magicWandLightning = m.fw(3, ::ItemMagicWandLightning, "lightning_magic_wand", "magicWandLightning", listOf(), { magicWandBase }, !THUNDER, !ENERGY)

    val ocarinaBase = m.fw(3, ::ItemFairyWeapon, "ocarina_base", "ocarinaBase", listOf(), null, !SOUND)
    val ocarinaTemptation = m.fw(3, ::ItemOcarinaTemptation, "temptation_ocarina", "ocarinaTemptation", listOf(), { ocarinaBase }, !LIFE)
    val bellBase = m.fw(2, ::ItemBellBase, "bell_base", "bellBase", listOf(), null, !SOUND)
    val bellFlowerPicking = m.fw(2, { ItemBellFlowerPicking(0.0, 0.001, 0.2) }, "flower_picking_bell", "bellFlowerPicking", listOf(), { bellBase }, !HARVEST)
    val bellFlowerPicking2 = m.fw(4, { ItemBellFlowerPicking(10.0, 0.01, 10000.0) }, "flower_picking_bell_2", "bellFlowerPicking2", listOf(), { bellFlowerPicking }, !HARVEST)
    val bellChristmas = m.fw(3, ::ItemBellChristmas, "christmas_bell", "bellChristmas", listOf(), { bellBase }, !CHRISTMAS, !ATTACK)
    val miragiumScythe = m.fw(2, { ItemMiragiumScythe(0.0, 2.0f) }, "miragium_scythe", "miragiumScythe", listOf(), null, !SLASH, !HARVEST)
    val lilagiumScythe = m.fw(3, { ItemMiragiumScythe(10.0, 4.0f) }, "lilagium_scythe", "lilagiumScythe", listOf(), { miragiumScythe }, !HARVEST)
    val ryugyoDrill = m.fw(4, { ItemRyugyoDrill(0.0) }, "ryugyo_drill", "ryugyoDrill", listOf(), null, !DESTROY, !THUNDER, !WATER)
}

object FairyWeapon {
    val module = module {
        fairyWeaponLoader = FairyWeaponLoader(this)
        onMakeLang {
            enJa("miragefairy2019.magic.${MagicMessage.NO_FAIRY.unlocalizedName}.text", "You don't have a fairy", "妖精を所持していません")
            enJa("miragefairy2019.magic.${MagicMessage.INSUFFICIENT_DURABILITY.unlocalizedName}.text", "Insufficient durability", "耐久値が不足しています")
            enJa("miragefairy2019.magic.${MagicMessage.NO_TARGET.unlocalizedName}.text", "There is no target", "発動対象がありません")
            enJa("miragefairy2019.magic.${MagicMessage.COOL_TIME.unlocalizedName}.text", "Cool time remains", "クールタイムが残っています")
        }
    }
}

enum class MagicMessage(val unlocalizedName: String) {
    NO_FAIRY("noFairy"),
    INSUFFICIENT_DURABILITY("insufficientDurability"),
    NO_TARGET("noTarget"),
    COOL_TIME("coolTime"),
    ;

    val displayText get() = textComponent { translate("miragefairy2019.magic.$unlocalizedName.text") }
}
