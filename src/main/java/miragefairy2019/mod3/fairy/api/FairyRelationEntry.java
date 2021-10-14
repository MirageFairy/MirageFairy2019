package miragefairy2019.mod3.fairy.api;

public final class FairyRelationEntry<T> {

    public final T key;

    public final FairyDropEntry drop;

    public FairyRelationEntry(T key, FairyDropEntry drop) {
        this.key = key;
        this.drop = drop;
    }

}
