package miragefairy2019.modkt.api.fairy;

public interface IAbilitySet {

    public Iterable<IAbilityEntry> getEntries();

    /**
     * 非負です。
     */
    public double getPower(IErgType type);

}

