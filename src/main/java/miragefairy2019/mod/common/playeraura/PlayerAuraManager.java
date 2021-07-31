package miragefairy2019.mod.common.playeraura;

import miragefairy2019.mod.api.fairy.ApiFairy;
import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairy.IItemFairy;
import miragefairy2019.mod.api.fairy.IManaSet;
import miragefairy2019.mod.api.fairy.relation.IIngredientFairyRelation;
import miragefairy2019.mod.api.playeraura.IPlayerAura;
import miragefairy2019.mod.api.playeraura.IPlayerAuraManager;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.modules.fairy.ManaSet;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayerAuraManager implements IPlayerAuraManager {

    private PlayerAura playerAuraClient = new PlayerAura(this);
    private Map<String, PlayerAura> mapServer = new HashMap<>();

    @Override
    public IPlayerAura getClientPlayerAura() {
        return playerAuraClient;
    }

    @Override
    public IPlayerAura getServerPlayerAura(EntityPlayer player) {
        return mapServer.computeIfAbsent(player.getCachedUniqueIdString(), p -> Monad.of(new PlayerAura(this))
                .peek(pa -> pa.load(player))
                .get());
    }

    @Override
    public Optional<IManaSet> getFoodAura(ItemStack itemStack) {

        // 該当する全妖精のリスト
        List<IIngredientFairyRelation> list = ApiFairy.fairyRelationRegistry.getIngredientFairyRelations()
                .filter(r -> r.getIngredient().test(itemStack))
                .filter(r -> r.getRelevance() >= 1)
                .toList();

        // 誰も該当しなければ中止
        if (list.isEmpty()) return Optional.empty();

        // 最も関連の深い妖精のリスト
        double relevanceMax = ISuppliterator.ofIterable(list)
                .map(r -> r.getRelevance())
                .max(r -> r)
                .get();
        List<IFairyType> list2 = ISuppliterator.ofIterable(list)
                .filter(r -> r.getRelevance() == relevanceMax)
                .mapIfPresent(r -> getFairyType(r.getItemStackFairy()))
                .toList();

        //

        return Optional.of(new ManaSet(
                list2.stream()
                        .mapToDouble(r -> r.getManas().getShine() / r.getCost() * 50 * 0.5)
                        .average()
                        .getAsDouble(),
                list2.stream()
                        .mapToDouble(r -> r.getManas().getFire() / r.getCost() * 50 * 0.5)
                        .average()
                        .getAsDouble(),
                list2.stream()
                        .mapToDouble(r -> r.getManas().getWind() / r.getCost() * 50 * 0.5)
                        .average()
                        .getAsDouble(),
                list2.stream()
                        .mapToDouble(r -> r.getManas().getGaia() / r.getCost() * 50 * 0.5)
                        .average()
                        .getAsDouble(),
                list2.stream()
                        .mapToDouble(r -> r.getManas().getAqua() / r.getCost() * 50 * 0.5)
                        .average()
                        .getAsDouble(),
                list2.stream()
                        .mapToDouble(r -> r.getManas().getDark() / r.getCost() * 50 * 0.5)
                        .average()
                        .getAsDouble()));
    }

    private static Optional<IFairyType> getFairyType(ItemStack itemStackFairy) {
        if (itemStackFairy.getItem() instanceof IItemFairy) {
            return ((IItemFairy) itemStackFairy.getItem()).getMirageFairy2019Fairy(itemStackFairy);
        } else {
            return Optional.empty();
        }
    }

}
