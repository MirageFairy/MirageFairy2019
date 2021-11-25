package miragefairy2019.mod.api.fairy.registry;

import miragefairy2019.mod3.fairy.api.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public interface IFairyRegistry {

    /**
     * 特定の名称に関連付けられた妖精の品種を登録します。
     * 妖精アイテムが成長を行う場合、最も基本的なアイテムだけを登録します。
     */
    public void registerFairy(ResourceLocation registryName, IFairyType fairyType, ItemStack itemStack);

    public Optional<IFairyRecord> getFairy(ResourceLocation registryName);

    public ISuppliterator<IFairyRecord> getFairies();

}
