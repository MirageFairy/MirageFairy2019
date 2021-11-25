package miragefairy2019.mod3.erg.api;

public interface IErgSet {

    public Iterable<IErgEntry> getEntries();

    /**
     * 非負です。
     */
    public double getPower(EnumErgType type);

}

