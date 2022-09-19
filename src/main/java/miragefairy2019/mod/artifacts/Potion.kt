package miragefairy2019.mod.artifacts

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.fairyCentrifugeCraftHandler
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.itemVariant
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.obtain
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.ItemMultiMaterial
import miragefairy2019.libkt.ItemVariantMaterial
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.concat
import miragefairy2019.libkt.containerItem
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.setCustomModelResourceLocations
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.libkt.withColor
import miragefairy2019.mod.Main
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.init.MobEffects
import net.minecraft.item.EnumAction
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

enum class PotionCard(
    val metadata: Int,
    val registryName: String,
    val unlocalizedName: String,
    val englishName: String,
    val japaneseName: String,
    val japanesePoem: String,
    val hasEffect: Boolean,
    val potionEffects: () -> List<PotionEffect>
) {
    MANDRAKE_JUICE(
        0, "mandrake_juice", "mandrakeJuice",
        "Mandrake Juice", "アルラウンＥ  ２５０ｍｌ", "負傷の防止と恢復に！",
        true,
        {
            listOf(
                PotionEffect(MobEffects.RESISTANCE, 1200, 0),
                PotionEffect(MobEffects.ABSORPTION, 1200, 1),
                PotionEffect(MobEffects.REGENERATION, 1200, 0),
                PotionEffect(MobEffects.HASTE, 1200, 1)
            )
        }
    ),
    CACTUS_JUICE(
        1, "cactus_juice", "cactusJuice",
        "Cactus Juice", "サボテンジュース", "",
        false,
        {
            listOf(
                PotionEffect(MobEffects.REGENERATION, 1200, 0)
            )
        }
    ),
    APPLE_JUICE(
        2, "apple_juice", "appleJuice",
        "Apple Juice", "リンゴジュース", "",
        false,
        {
            listOf(
                PotionEffect(MobEffects.SPEED, 12000, 0)
            )
        }
    ),
    GREEN_JUICE(
        3, "green_juice", "greenJuice",
        "Green Juice", "青汁", "",
        false,
        {
            listOf(
                PotionEffect(MobEffects.STRENGTH, 12000, 0)
            )
        }
    ),
    POISON_JUICE(
        4, "poison_juice", "poisonJuice",
        "Poison Juice", "毒薬", "",
        false,
        {
            listOf(
                PotionEffect(MobEffects.INSTANT_DAMAGE, 0, 19),
                PotionEffect(MobEffects.WITHER, 1200, 9),
                PotionEffect(MobEffects.HUNGER, 1200, 9)
            )
        }
    ),
    COLA(
        5, "cola", "cola",
        "Cola", "コーラ", "",
        false,
        {
            listOf(
                PotionEffect(MobEffects.HEALTH_BOOST, 12000, 0)
            )
        }
    ),
    RAMUNE(
        6, "ramune", "ramune",
        "Ramune", "ラムネ", "",
        false,
        {
            listOf(
                PotionEffect(MobEffects.LUCK, 12000, 0)
            )
        }
    ),
}


lateinit var itemPotion: () -> ItemPotion

fun PotionCard.createItemStack(count: Int = 1) = itemPotion().getVariant(this.metadata)!!.createItemStack(count)

val potionModule = module {

    // アイテム
    itemPotion = item({ ItemPotion() }, "potion") {
        setUnlocalizedName("potion")
        setCreativeTab { Main.creativeTab }
        PotionCard.values().forEach { potionCard ->
            itemVariant(potionCard.registryName, { ItemVariantPotion(potionCard) }, potionCard.metadata)
        }
        onRegisterItem {
            if (Main.side.isClient) item.setCustomModelResourceLocations()
        }
    }

    // 種類別
    PotionCard.values().forEach { potionCard ->
        makeItemModel(potionCard.registryName) { generated }
        onMakeLang { enJa("item.${potionCard.unlocalizedName}.name", potionCard.englishName, potionCard.japaneseName) }
        onMakeLang { enJa("item.${potionCard.unlocalizedName}.poem", "", potionCard.japanesePoem) }
    }

    // レシピ登録
    run {

        // アルラウンE
        onAddRecipe {
            fairyCentrifugeCraftHandler(30.0) {
                process { !Mana.GAIA + !Erg.DESTROY * 2.0 }
                process { !Mana.WIND + !Erg.CHEMICAL * 2.0 }
                process { !Mana.AQUA + !Erg.LIFE * 2.0 }
                input("mirageFairySyrup".oreIngredient, 1)
                input("mirageFairyMandrake".oreIngredient, 1)
                input(Items.GLASS_BOTTLE.createItemStack().ingredient, 1)
                output(PotionCard.MANDRAKE_JUICE.createItemStack(), 1.0)
            }
        }

        // サボテンジュース
        onAddRecipe {
            fairyCentrifugeCraftHandler(30.0) {
                process { !Mana.GAIA + !Erg.DESTROY * 2.0 }
                process { !Mana.WIND + !Erg.CHEMICAL * 2.0 }
                process { !Mana.AQUA + !Erg.LIFE * 2.0 }
                input("mirageFairySyrup".oreIngredient, 1)
                input("blockCactus".oreIngredient, 1)
                input(Items.GLASS_BOTTLE.createItemStack().ingredient, 1)
                output(PotionCard.CACTUS_JUICE.createItemStack(), 1.0)
            }
        }

        // リンゴジュース
        onAddRecipe {
            fairyCentrifugeCraftHandler(30.0) {
                process { !Mana.GAIA + !Erg.DESTROY * 2.0 }
                process { !Mana.WIND + !Erg.CHEMICAL * 2.0 }
                process { !Mana.AQUA + !Erg.LIFE * 2.0 }
                input("mirageFairySyrup".oreIngredient, 1)
                input(Items.APPLE.ingredient, 1)
                input(Items.GLASS_BOTTLE.createItemStack().ingredient, 1)
                output(PotionCard.APPLE_JUICE.createItemStack(), 1.0)
            }
        }

        // 青汁
        onAddRecipe {
            fairyCentrifugeCraftHandler(30.0) {
                process { !Mana.GAIA + !Erg.DESTROY * 2.0 }
                process { !Mana.WIND + !Erg.CHEMICAL * 2.0 }
                process { !Mana.AQUA + !Erg.LIFE * 2.0 }
                input("mirageFairySyrup".oreIngredient, 1)
                input("treeLeaves".oreIngredient, 1)
                input(Items.GLASS_BOTTLE.createItemStack().ingredient, 1)
                output(PotionCard.GREEN_JUICE.createItemStack(), 1.0)
            }
        }

        // 毒薬
        onAddRecipe {
            fairyCentrifugeCraftHandler(30.0) {
                process { !Mana.GAIA + !Erg.DESTROY * 2.0 }
                process { !Mana.WIND + !Erg.CHEMICAL * 2.0 }
                process { !Mana.AQUA + !Erg.LIFE * 2.0 }
                input("mirageFairySyrup".oreIngredient, 1)
                input(Items.FISH.createItemStack(metadata = 3).ingredient, 1)
                input(Items.GLASS_BOTTLE.createItemStack().ingredient, 8)
                output(PotionCard.POISON_JUICE.createItemStack(), 8.0)
            }
        }

        // コーラ
        makeRecipe("cola") {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "mirageFairySyrup"),
                    DataOreIngredient(ore = "container250CarbonatedWater"),
                    DataOreIngredient(ore = "cropNetherWart"),
                    WandType.BREAKING.ingredientData,
                    WandType.FREEZING.ingredientData,
                    DataSimpleIngredient(item = "minecraft:glass_bottle")
                ),
                result = DataResult(item = "miragefairy2019:potion", data = PotionCard.COLA.metadata)
            )
        }

        // ラムネ
        makeRecipe("ramune") {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "mirageFairySyrup"),
                    DataOreIngredient(ore = "container250CarbonatedWater"),
                    DataSimpleIngredient(item = "minecraft:sugar"),
                    WandType.FREEZING.ingredientData,
                    DataSimpleIngredient(item = "minecraft:glass_bottle")
                ),
                result = DataResult(item = "miragefairy2019:potion", data = PotionCard.RAMUNE.metadata)
            )
        }

    }

}

class ItemVariantPotion(val potionCard: PotionCard) : ItemVariantMaterial(potionCard.registryName, potionCard.unlocalizedName)

class ItemPotion : ItemMultiMaterial<ItemVariantPotion>() {

    // 見た目

    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        if (canTranslate("${getUnlocalizedName(itemStack)}.poem")) {
            val string = translateToLocal("${getUnlocalizedName(itemStack)}.poem")
            if (string.isNotEmpty()) tooltip += string
        }

        val variant = getVariant(itemStack) ?: return

        variant.potionCard.potionEffects().forEach { potionEffect ->
            tooltip += formattedText {
                concat(
                    translate(potionEffect.effectName),
                    " Lv.${potionEffect.amplifier + 1}"(),
                    if (potionEffect.duration > 20) " (${Potion.getPotionDurationString(potionEffect, 1.0f)})"() else ""()
                ).withColor(if (potionEffect.potion.isBadEffect) TextFormatting.RED else TextFormatting.BLUE)
            }
        }

    }

    @SideOnly(Side.CLIENT)
    override fun hasEffect(itemStack: ItemStack) = getVariant(itemStack)?.potionCard?.hasEffect ?: false


    // 瓶

    override fun hasContainerItem(itemStack: ItemStack) = true
    override fun getContainerItem(itemStack: ItemStack) = Items.GLASS_BOTTLE.createItemStack()


    // アクション

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        player.activeHand = hand
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }

    override fun getItemUseAction(itemStack: ItemStack) = EnumAction.DRINK
    override fun getMaxItemUseDuration(itemStack: ItemStack) = 32

    override fun onItemUseFinish(itemStack: ItemStack, world: World, player: EntityLivingBase): ItemStack {
        if (player !is EntityPlayer) return itemStack
        val variant = getVariant(itemStack) ?: return itemStack
        val containerItem = itemStack.containerItem

        // 成立

        if (player is EntityPlayerMP) CriteriaTriggers.CONSUME_ITEM.trigger(player, itemStack) // 実績トリガー

        //player.resetActiveHand() // ゲップ対策 // 無意味だった

        // 消費
        if (!player.isCreative) {
            itemStack.shrink(1)
            if (containerItem != null) player.obtain(containerItem)
        }

        // 効果
        if (!world.isRemote) {
            variant.potionCard.potionEffects().forEach { potionEffect ->
                if (potionEffect.potion.isInstant) {
                    potionEffect.potion.affectEntity(player, player, player, potionEffect.amplifier, 1.0)
                } else {
                    player.addPotionEffect(PotionEffect(potionEffect))
                }
            }
        }

        return itemStack
    }

}
