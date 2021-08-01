package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.modkt.api.playeraura.ApiPlayerAura;
import miragefairy2019.mod.api.fairy.ApiFairy;
import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.magic.IMagicFactorProvider;
import miragefairy2019.mod.api.magic.IMagicHandler;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.modkt.api.playeraura.IPlayerAura;
import miragefairy2019.mod.modules.fairy.EnumAbilityType;
import miragefairy2019.mod.modules.fairy.EnumManaType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.*;

public abstract class ItemFairyWeaponBase3 extends ItemFairyWeaponBase {

    public abstract IMagicHandler getMagicHandler(IPlayerAura playerAura, IFairyType fairyType);

    //

    @Override
    @SideOnly(Side.CLIENT)
    protected void addInformationFunctions(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(RED + "Right click to use magic");
        super.addInformationFunctions(itemStack, world, tooltip, flag);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, IFairyType fairyType, World world, List<String> tooltip, ITooltipFlag flag) {
        if (flag.isAdvanced()) {
            getMagicHandler(ApiPlayerAura.playerAuraManager.getClientPlayerAura(), fairyType).getMagicStatusList()
                    .forEach(magicStatus -> tooltip.add(magicStatus.getLocalizedName()
                            .appendText(": ")
                            .appendSibling(magicStatus.getDisplayValue())
                            .appendText(" (")
                            .appendSibling(magicStatus.getFormula(new IMagicFactorProvider() {
                                @Override
                                public ITextComponent mana(EnumManaType manaType) {
                                    return manaType.getDisplayName();
                                }

                                @Override
                                public ITextComponent ability(EnumAbilityType abilityType) {
                                    return abilityType.getDisplayName();
                                }

                                @Override
                                public ITextComponent cost() {
                                    return new TextComponentString("")
                                            .appendSibling(new TextComponentTranslation("mirageFairy2019.formula.source.cost.name")
                                                    .setStyle(new Style().setColor(DARK_PURPLE)));
                                }
                            }))
                            .appendText(")")
                            .setStyle(new Style().setColor(BLUE))
                            .getFormattedText()));
        } else {
            getMagicHandler(ApiPlayerAura.playerAuraManager.getClientPlayerAura(), fairyType).getMagicStatusList()
                    .forEach(magicStatus -> tooltip.add(magicStatus.getLocalizedName()
                            .appendText(": ")
                            .appendSibling(magicStatus.getDisplayValue())
                            .setStyle(new Style().setColor(BLUE))
                            .getFormattedText()));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        // アイテム取得
        ItemStack itemStack = player.getHeldItem(hand);

        // 妖精取得
        IFairyType fairyType = findFairy(itemStack, player).map(t -> t.y).orElseGet(ApiFairy::empty);

        return new ActionResult<>(getMagicHandler(ApiPlayerAura.playerAuraManager.getServerPlayerAura(player), fairyType).getMagicExecutor(world, player, itemStack).onItemRightClick(hand), itemStack);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {

        // クライアントサイドでなければ中止
        if (!ApiMain.side().isClient()) return;

        // プレイヤー取得
        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;

        // アイテム取得
        if (!isSelected && player.getHeldItemOffhand() != itemStack) return;

        // 妖精取得
        IFairyType fairyType = findFairy(itemStack, player).map(t -> t.y).orElseGet(ApiFairy::empty);

        getMagicHandler(ApiPlayerAura.playerAuraManager.getServerPlayerAura(player), fairyType).getMagicExecutor(world, player, itemStack).onUpdate(itemSlot, isSelected);

    }

}
