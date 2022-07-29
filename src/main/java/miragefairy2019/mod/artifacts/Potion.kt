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
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.makeItemModel
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
import miragefairy2019.libkt.plus
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
    val potionEffects: () -> List<PotionEffect>
) {
    MANDRAKE_JUICE(
        0, "mandrake_juice", "mandrakeJuice",
        "Mandrake Juice", "アルラウンＥ  ２５０ｍｌ", "負傷の防止と恢復に！",
        {
            listOf(
                PotionEffect(MobEffects.RESISTANCE, 1800, 0),
                PotionEffect(MobEffects.ABSORPTION, 1800, 1)
            )
        }
    ),
}


lateinit var itemPotion: () -> ItemPotion

fun PotionCard.createItemStack(count: Int = 1) = itemPotion().getVariant(this.metadata)!!.createItemStack(count)

val potionModule = module {

    // アイテム登録
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


    // マンドレイク→全耐性のポーション
    onAddRecipe {
        fairyCentrifugeCraftHandler(30.0) {
            process { !Mana.GAIA + !Erg.FLAME * 2.0 }
            process { !Mana.WIND + !Erg.CHEMICAL * 2.0 }
            process { !Mana.AQUA + !Erg.LIFE * 2.0 }
            input("mirageFairySyrup".oreIngredient, 1)
            input("mirageFairyMandrake".oreIngredient, 1)
            input(Items.GLASS_BOTTLE.createItemStack().ingredient, 1)
            output(PotionCard.MANDRAKE_JUICE.createItemStack(), 1.0)
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
                    if (potionEffect.amplifier > 0) " "() + translate("potion.potency.${potionEffect.amplifier}") else ""(),
                    if (potionEffect.duration > 20) " (${Potion.getPotionDurationString(potionEffect, 1.0f)})"() else ""()
                ).withColor(if (potionEffect.potion.isBadEffect) TextFormatting.RED else TextFormatting.BLUE)
            }
        }

    }

    @SideOnly(Side.CLIENT)
    override fun hasEffect(itemStack: ItemStack) = true


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
                player.addPotionEffect(potionEffect)
            }
        }

        return itemStack
    }

}
