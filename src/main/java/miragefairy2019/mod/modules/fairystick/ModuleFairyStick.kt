package miragefairy2019.mod.modules.fairystick

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.api.ore.ApiOre
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionConsumeItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionNotNether
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionReplaceBlock
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionSpawnBlock
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRecipe
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRegistry
import miragefairy2019.mod3.fairystickcraft.api.ApiFairyStickCraft
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.BlockDynamicLiquid
import net.minecraft.init.Blocks
import net.minecraftforge.oredict.OreIngredient
import java.util.function.Supplier

lateinit var itemFairyStick: Supplier<ItemFairyStick>

val moduleFairyStick: Module = {

    onInstantiation {
        ApiFairyStickCraft.fairyStickCraftRegistry = FairyStickCraftRegistry()
    }

    // 妖精のステッキ
    itemFairyStick = item({ ItemFairyStick() }, "fairy_stick") {
        setUnlocalizedName("fairyStick")
        setCreativeTab { ApiMain.creativeTab }
        setCustomModelResourceLocation()
        addOreName("mirageFairy2019FairyStick")
    }

    // レシピ登録
    onAddRecipe {

        // 水精→水源
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairy2019FairyStick"))
            it.conditions += FairyStickCraftConditionNotNether()
            it.conditions += FairyStickCraftConditionSpawnBlock { Blocks.WATER.defaultState }
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairyCrystal"))
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyWaterRank1"))
        })

        // 溶岩精→溶岩流
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairy2019FairyStick"))
            it.conditions += FairyStickCraftConditionSpawnBlock { Blocks.FLOWING_LAVA.defaultState.withProperty(BlockDynamicLiquid.LEVEL, 15) }
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairyCrystal"))
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyLavaRank1"))
        })

        // 蜘蛛精→糸ブロック
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairy2019FairyStick"))
            it.conditions += FairyStickCraftConditionSpawnBlock { Blocks.WEB.defaultState }
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairyCrystal"))
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairySpiderRank1"))
        })

        // 水＋ミラジウムの粉→妖水
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairy2019FairyStick"))
            it.conditions += FairyStickCraftConditionReplaceBlock({ Blocks.WATER.defaultState }, { ApiOre.blockFluidMiragiumWater.defaultState })
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustMiragium"))
        })

    }

}
