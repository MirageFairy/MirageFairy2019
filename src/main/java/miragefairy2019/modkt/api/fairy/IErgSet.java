package miragefairy2019.modkt.api.fairy;

public interface IErgSet {

    public Iterable<IErgEntry> getEntries();

    /**
     * 非負です。
     */
    public double getPower(IErgType type);

}

