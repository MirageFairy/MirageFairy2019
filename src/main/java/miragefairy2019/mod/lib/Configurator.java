package miragefairy2019.mod.lib;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import miragefairy2019.mod.lib.multi.ItemMulti;
import miragefairy2019.mod.lib.multi.ItemVariant;
import miragefairy2019.mod.lib.multi.ItemVariantMaterial;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public final class Configurator<T> implements Supplier<T> {

    public final EventRegistryMod erMod;

    public Configurator(EventRegistryMod erMod) {
        this.erMod = erMod;
    }

    //

    private T t;

    public void set(T t) {
        this.t = t;
    }

    @Override
    public T get() {
        return t;
    }

    //

    public static <I> Function<Configurator<I>, Monad<Configurator<I>>> onRegisterItem(Consumer<I> consumer) {
        return c -> {
            c.erMod.registerItem.register(ic -> consumer.accept(c.get()));
            return Monad.of(c);
        };
    }

    public static <I> Function<Configurator<I>, Monad<Configurator<I>>> onCreateItemStack(Consumer<I> consumer) {
        return c -> {
            c.erMod.createItemStack.register(ic -> consumer.accept(c.get()));
            return Monad.of(c);
        };
    }

    public static <I> Function<Configurator<I>, Monad<Configurator<I>>> onAddRecipe(Consumer<I> consumer) {
        return c -> {
            c.erMod.addRecipe.register(() -> consumer.accept(c.get()));
            return Monad.of(c);
        };
    }

    //

    public static <I extends Item> Monad<Configurator<I>> item(EventRegistryMod erMod, Supplier<I> sItem, ResourceLocation registryName, String unlocalizedName) {
        Configurator<I> c = new Configurator<>(erMod);

        c.erMod.registerItem.register(ic -> {
            c.set(sItem.get());
            c.get().setRegistryName(registryName);
            c.get().setUnlocalizedName(unlocalizedName);
            ForgeRegistries.ITEMS.register(c.get());
        });

        return Monad.of(c);
    }

    public static <I extends Item> Function<Configurator<I>, Monad<Configurator<I>>> setCreativeTab(Supplier<CreativeTabs> sCreativeTab) {
        return c -> Monad.of(c)
                .bind(onRegisterItem(i -> i.setCreativeTab(sCreativeTab.get())));
    }

    //

    public static <I extends ItemMulti<V>, V extends ItemVariant> Monad<Configurator<V>> itemVariant(EventRegistryMod erMod, Configurator<I> cItem, int metadata, Supplier<V> sItemVariant) {
        Configurator<V> c = new Configurator<>(erMod);

        c.erMod.registerItem.register(ic -> {
            c.set(sItemVariant.get());
            cItem.get().registerVariant(metadata, c.get());
        });

        return Monad.of(c);
    }

    public static <V extends ItemVariantMaterial> Function<Configurator<V>, Monad<Configurator<V>>> addOreName(String oreName) {
        return c -> Monad.of(c)
                .bind(onCreateItemStack(v -> OreDictionary.registerOre(oreName, v.createItemStack())));
    }

}
