package miragefairy2019.mod.api.fairy.registry;

import java.util.function.Predicate;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IFairyRelationRegistry {

    public void registerFairyRelationItemStack(Predicate<ItemStack> predicate, ResourceLocation registryName);

    public ISuppliterator<ResourceLocation> getRegistryNames();

    public IFairySelector fairySelector();

}
