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
import miragefairy2019.lib.proxy
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.ItemMultiMaterial
import miragefairy2019.libkt.ItemVariantMaterial
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.concat
import miragefairy2019.libkt.containerItem
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.red
import miragefairy2019.libkt.setCustomModelResourceLocations
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.libkt.withColor
import miragefairy2019.mod.Main
import miragefairy2019.mod.skill.canResetMastery
import miragefairy2019.mod.skill.skillContainer
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
import java.time.Instant

interface DrinkHandler {
    @SideOnly(Side.CLIENT)
    fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag)
    fun check(itemStack: ItemStack, world: World, player: EntityPlayer): Boolean
    fun affect(itemStack: ItemStack, world: World, player: EntityPlayer)
}

class PotionEffectDrinkHandler(private val potionEffectGetter: () -> PotionEffect) : DrinkHandler {
    private val potionEffect by lazy { potionEffectGetter() }

    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip += formattedText {
            concat(
                translate(potionEffect.effectName),
                " Lv.${potionEffect.amplifier + 1}"(),
                if (potionEffect.duration > 20) " (${Potion.getPotionDurationString(potionEffect, 1.0f)})"() else ""()
            ).withColor(if (potionEffect.potion.isBadEffect) TextFormatting.RED else TextFormatting.BLUE)
        }
    }

    override fun check(itemStack: ItemStack, world: World, player: EntityPlayer) = true
    override fun affect(itemStack: ItemStack, world: World, player: EntityPlayer) {
        if (potionEffect.potion.isInstant) {
            potionEffect.potion.affectEntity(player, player, player, potionEffect.amplifier, 1.0)
        } else {
            player.addPotionEffect(PotionEffect(potionEffect))
        }
    }
}

class SkillPointResetDrinkHandler : DrinkHandler {
    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip += formattedText { "マスタリレベル初期化を可能にする"().red }
    }

    override fun check(itemStack: ItemStack, world: World, player: EntityPlayer): Boolean {
        if (!player.proxy.skillContainer.canResetMastery(Instant.now())) {
            return true
        } else {
            player.sendStatusMessage(textComponent { "マスタリレベル初期化は既に可能です"() }, true) // TRANSLATE
            return false
        }
    }

    override fun affect(itemStack: ItemStack, world: World, player: EntityPlayer) {
        if (world.isRemote) return
        if (player !is EntityPlayerMP) return
        player.proxy.skillContainer.variables.lastMasteryResetTime = null
        player.proxy.skillContainer.send(player)
        player.sendStatusMessage(textComponent { "マスタリレベル初期化が可能になりました"() }, true) // TRANSLATE
    }
}


enum class PotionCard(
    val metadata: Int,
    val registryName: String,
    val unlocalizedName: String,
    val englishName: String,
    val japaneseName: String,
    val japanesePoem: String,
    val hasEffect: Boolean,
    vararg val drinkHandlers: DrinkHandler
) {
    MANDRAKE_JUICE(
        0, "mandrake_juice", "mandrakeJuice",
        "Mandrake Juice", "アルラウンＥ  ２５０ｍｌ", "負傷の防止と恢復に！",
        true,
        PotionEffectDrinkHandler { PotionEffect(MobEffects.RESISTANCE, 1200, 0) },
        PotionEffectDrinkHandler { PotionEffect(MobEffects.ABSORPTION, 1200, 1) },
        PotionEffectDrinkHandler { PotionEffect(MobEffects.REGENERATION, 1200, 0) },
        PotionEffectDrinkHandler { PotionEffect(MobEffects.HASTE, 1200, 1) }
    ),
    CACTUS_JUICE(
        1, "cactus_juice", "cactusJuice",
        "Cactus Juice", "サボテンジュース", "",
        false,
        PotionEffectDrinkHandler { PotionEffect(MobEffects.REGENERATION, 1200, 0) }
    ),
    APPLE_JUICE(
        2, "apple_juice", "appleJuice",
        "Apple Juice", "リンゴジュース", "",
        false,
        PotionEffectDrinkHandler { PotionEffect(MobEffects.SPEED, 12000, 0) }
    ),
    GREEN_JUICE(
        3, "green_juice", "greenJuice",
        "Green Juice", "青汁", "",
        false,
        PotionEffectDrinkHandler { PotionEffect(MobEffects.STRENGTH, 12000, 0) }
    ),
    POISON_JUICE(
        4, "poison_juice", "poisonJuice",
        "Poison Juice", "毒薬", "",
        false,
        PotionEffectDrinkHandler { PotionEffect(MobEffects.INSTANT_DAMAGE, 0, 19) },
        PotionEffectDrinkHandler { PotionEffect(MobEffects.WITHER, 1200, 9) },
        PotionEffectDrinkHandler { PotionEffect(MobEffects.HUNGER, 1200, 9) }
    ),
    COLA(
        5, "cola", "cola",
        "Cola", "コーラ", "",
        false,
        PotionEffectDrinkHandler { PotionEffect(MobEffects.HEALTH_BOOST, 12000, 0) }
    ),
    RAMUNE(
        6, "ramune", "ramune",
        "Ramune", "ラムネ", "",
        false,
        PotionEffectDrinkHandler { PotionEffect(MobEffects.LUCK, 12000, 0) }
    ),
    SKILL_POINT_RESET_POTION(
        7, "skill_point_reset_potion", "skillPointResetPotion",
        "Skill Point Reset Potion", "SP還元ポーション", "",
        false,
        SkillPointResetDrinkHandler()
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
        lang("item.${potionCard.unlocalizedName}.name", potionCard.englishName, potionCard.japaneseName)
        lang("item.${potionCard.unlocalizedName}.poem", "", potionCard.japanesePoem)
    }

    // レシピ登録
    run {

        // アルラウンE
        makeRecipe("mandrake_juice") {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "mirageFairySyrup"),
                    DataOreIngredient(ore = "container1000Water"),
                    DataOreIngredient(ore = "mirageFairyMandrake"),
                    WandType.MELTING.ingredientData,
                    WandType.FREEZING.ingredientData,
                    DataSimpleIngredient(item = "minecraft:glass_bottle")
                ),
                result = DataResult(item = "miragefairy2019:potion", data = PotionCard.MANDRAKE_JUICE.metadata)
            )
        }

        // サボテンジュース
        makeRecipe("cactus_juice") {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "mirageFairySyrup"),
                    DataOreIngredient(ore = "container1000Water"),
                    DataOreIngredient(ore = "blockCactus"),
                    WandType.DISTORTION.ingredientData,
                    WandType.FREEZING.ingredientData,
                    DataSimpleIngredient(item = "minecraft:glass_bottle")
                ),
                result = DataResult(item = "miragefairy2019:potion", data = PotionCard.CACTUS_JUICE.metadata)
            )
        }

        // リンゴジュース
        makeRecipe("apple_juice") {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "mirageFairySyrup"),
                    DataOreIngredient(ore = "container1000Water"),
                    DataSimpleIngredient(item = "minecraft:apple"),
                    WandType.DISTORTION.ingredientData,
                    WandType.FREEZING.ingredientData,
                    DataSimpleIngredient(item = "minecraft:glass_bottle")
                ),
                result = DataResult(item = "miragefairy2019:potion", data = PotionCard.APPLE_JUICE.metadata)
            )
        }

        // 青汁
        makeRecipe("green_juice") {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "mirageFairySyrup"),
                    DataOreIngredient(ore = "container1000Water"),
                    DataOreIngredient(ore = "treeLeaves"),
                    WandType.BREAKING.ingredientData,
                    WandType.FREEZING.ingredientData,
                    DataSimpleIngredient(item = "minecraft:glass_bottle")
                ),
                result = DataResult(item = "miragefairy2019:potion", data = PotionCard.GREEN_JUICE.metadata)
            )
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

        variant.potionCard.drinkHandlers.forEach { drinkHandler ->
            drinkHandler.addInformation(itemStack, world, tooltip, flag)
        }

    }

    @SideOnly(Side.CLIENT)
    override fun hasEffect(itemStack: ItemStack) = getVariant(itemStack)?.potionCard?.hasEffect ?: false


    // 瓶

    override fun hasContainerItem(itemStack: ItemStack) = true
    override fun getContainerItem(itemStack: ItemStack) = Items.GLASS_BOTTLE.createItemStack()


    // アクション

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStack = player.getHeldItem(hand)
        val variant = getVariant(itemStack) ?: return ActionResult(EnumActionResult.FAIL, player.getHeldItem(hand))

        // 前提検査
        variant.potionCard.drinkHandlers.forEach { drinkHandler ->
            if (!drinkHandler.check(itemStack, world, player)) return ActionResult(EnumActionResult.FAIL, player.getHeldItem(hand))
        }

        // 成立

        player.activeHand = hand
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }

    override fun getItemUseAction(itemStack: ItemStack) = EnumAction.DRINK
    override fun getMaxItemUseDuration(itemStack: ItemStack) = 32

    override fun onItemUseFinish(itemStack: ItemStack, world: World, player: EntityLivingBase): ItemStack {
        if (player !is EntityPlayer) return itemStack
        val variant = getVariant(itemStack) ?: return itemStack
        val containerItem = itemStack.containerItem

        // 前提検査
        variant.potionCard.drinkHandlers.forEach { drinkHandler ->
            if (!drinkHandler.check(itemStack, world, player)) return itemStack
        }

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
            variant.potionCard.drinkHandlers.forEach { drinkHandler ->
                drinkHandler.affect(itemStack, world, player)
            }
        }

        return itemStack
    }

}
