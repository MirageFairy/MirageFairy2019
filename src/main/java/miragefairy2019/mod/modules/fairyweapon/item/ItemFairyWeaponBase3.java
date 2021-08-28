package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.fairy.ApiFairy;
import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.magic.IMagicFactorProvider;
import miragefairy2019.mod.api.magic.IMagicHandler;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.modkt.api.erg.IErgType;
import miragefairy2019.modkt.api.magicstatus.IMagicStatus;
import miragefairy2019.modkt.api.mana.IManaType;
import miragefairy2019.modkt.api.playeraura.ApiPlayerAura;
import miragefairy2019.modkt.impl.fairy.ErgKt;
import miragefairy2019.modkt.impl.magicstatus.ImplMagicStatusKt;
import miragefairy2019.modkt.impl.mana.ManaTypeKt;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import java.util.Optional;

import static net.minecraft.util.text.TextFormatting.*;

public abstract class ItemFairyWeaponBase3 extends ItemFairyWeaponBase {

    public abstract IMagicHandler getMagicHandler(IFairyType fairyType);

    //

    @Override
    @SideOnly(Side.CLIENT)
    protected void addInformationFunctions(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(RED + "Right click to use magic");
        super.addInformationFunctions(itemStack, world, tooltip, flag);
    }

    @SideOnly(Side.CLIENT)
    private <T> ITextComponent getStatusText(IMagicStatus<T> magicStatus, IFairyType fairyType, boolean isAdvanced) {
        ITextComponent textComponent = ImplMagicStatusKt.getDisplayName(magicStatus);
        textComponent.appendText(": ");
        textComponent.appendSibling(magicStatus.getFormatter().getDisplayValue(magicStatus.getFunction(), fairyType));
        if (isAdvanced) {
            Optional<ITextComponent> c = ISuppliterator.ofIterable(ImplMagicStatusKt.getFactors(magicStatus.getFunction())).stream()
                    .reduce((a, b) -> a.appendText(",").appendSibling(b));
            if (c.isPresent()) {
                textComponent.appendText(" (");
                textComponent.appendSibling(c.get());
                textComponent.appendText(")");
            }
        }
        textComponent.setStyle(new Style().setColor(BLUE));
        return textComponent;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, IFairyType fairyType, World world, List<String> tooltip, ITooltipFlag flag) {
        IFairyType actualFairyType = ImplMagicStatusKt.getActualFairyType(fairyType, ApiPlayerAura.playerAuraManager.getClientPlayerAuraHandler().getPlayerAura());
        if (flag.isAdvanced()) {
            ISuppliterator.ofIterable(getMagicHandler(actualFairyType).getMagicStatuses())
                    .forEach(magicStatus -> tooltip.add(getStatusText(magicStatus, actualFairyType, true).getFormattedText()));
            getMagicHandler(actualFairyType).getMagicStatusList()
                    .forEach(magicStatus -> tooltip.add(magicStatus.getLocalizedName()
                            .appendText(": ")
                            .appendSibling(magicStatus.getDisplayValue())
                            .appendText(" (")
                            .appendSibling(magicStatus.getFormula(new IMagicFactorProvider() {
                                @Override
                                public ITextComponent mana(IManaType manaType) {
                                    return ManaTypeKt.getDisplayName(manaType);
                                }

                                @Override
                                public ITextComponent ability(IErgType abilityType) {
                                    return ErgKt.getDisplayName(abilityType);
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
            ISuppliterator.ofIterable(getMagicHandler(actualFairyType).getMagicStatuses())
                    .forEach(magicStatus -> tooltip.add(getStatusText(magicStatus, actualFairyType, false).getFormattedText()));
            getMagicHandler(actualFairyType).getMagicStatusList()
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

        if (world.isRemote) {
            IFairyType actualFairyType = ImplMagicStatusKt.getActualFairyType(fairyType, ApiPlayerAura.playerAuraManager.getClientPlayerAuraHandler().getPlayerAura());
            return new ActionResult<>(getMagicHandler(actualFairyType).getMagicExecutor(world, player, itemStack).onItemRightClick(hand), itemStack);
        } else {
            IFairyType actualFairyType = ImplMagicStatusKt.getActualFairyType(fairyType, ApiPlayerAura.playerAuraManager.getServerPlayerAuraHandler((EntityPlayerMP) player).getPlayerAura());
            return new ActionResult<>(getMagicHandler(actualFairyType).getMagicExecutor(world, player, itemStack).onItemRightClick(hand), itemStack);
        }
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

        if (world.isRemote) {
            IFairyType actualFairyType = ImplMagicStatusKt.getActualFairyType(fairyType, ApiPlayerAura.playerAuraManager.getClientPlayerAuraHandler().getPlayerAura());
            getMagicHandler(actualFairyType).getMagicExecutor(world, player, itemStack).onUpdate(itemSlot, isSelected);
        }
    }

}
