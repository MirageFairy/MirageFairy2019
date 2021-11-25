package miragefairy2019.mod.modules.fairy.registry;

import miragefairy2019.mod.api.fairy.registry.IFairyRecord;
import miragefairy2019.mod3.fairy.api.IFairyType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class FairyRecord implements IFairyRecord {

    private ResourceLocation registryName;
    private IFairyType fairyType;
    private ItemStack itemStack;

    public FairyRecord(ResourceLocation registryName, IFairyType fairyType, ItemStack itemStack) {
        this.registryName = registryName;
        this.fairyType = fairyType;
        this.itemStack = itemStack;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public IFairyType getFairyType() {
        return fairyType;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

}
