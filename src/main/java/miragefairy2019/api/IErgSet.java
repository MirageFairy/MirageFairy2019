package miragefairy2019.api;

public interface IErgSet {

    public Iterable<IErgEntry> getEntries();

    /**
     * 非負です。
     */
    public double getPower(Erg type);

}

