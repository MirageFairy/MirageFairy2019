package miragefairy2019.mod.modules.fairy.registry;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairy.registry.IFairyRecord;
import miragefairy2019.mod.api.fairy.registry.IFairyRegistry;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class FairyRegistry implements IFairyRegistry {

    public static final IFairyRegistry instance = new FairyRegistry();

    private Map<ResourceLocation, IFairyRecord> map = new HashMap<>();
    private List<IFairyRecord> list = new ArrayList<>();

    @Override
    public void registerFairy(ResourceLocation registryName, IFairyType fairyType, ItemStack itemStack) {
        if (map.containsKey(registryName)) throw new RuntimeException("Multiple definition: " + registryName);
        IFairyRecord fairyRecord = new FairyRecord(registryName, fairyType, itemStack);
        map.put(registryName, fairyRecord);
        list.add(fairyRecord);
    }

    @Override
    public Optional<IFairyRecord> getFairy(ResourceLocation registryName) {
        return Optional.ofNullable(map.get(registryName));
    }

    @Override
    public ISuppliterator<IFairyRecord> getFairies() {
        return ISuppliterator.ofIterable(list);
    }

}
