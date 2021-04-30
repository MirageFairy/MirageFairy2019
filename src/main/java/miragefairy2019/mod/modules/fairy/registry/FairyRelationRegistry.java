package miragefairy2019.mod.modules.fairy.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import miragefairy2019.mod.api.fairy.registry.IFairyRelationRegistry;
import miragefairy2019.mod.api.fairy.registry.IFairySelector;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class FairyRelationRegistry implements IFairyRelationRegistry
{

	public static final IFairyRelationRegistry instance = new FairyRelationRegistry();

	protected Map<ResourceLocation, FairyRelation> map = new HashMap<>();

	@Override
	public void registerFairyRelationItemStack(Predicate<ItemStack> predicate, ResourceLocation registryName)
	{
		map.computeIfAbsent(registryName, k -> new FairyRelation()).addFairyRelationItemStack(predicate);
	}

	@Override
	public ISuppliterator<ResourceLocation> getRegistryNames()
	{
		return ISuppliterator.ofIterable(map.keySet());
	}

	@Override
	public IFairySelector fairySelector()
	{
		return new FairySelector(this);
	}

}
