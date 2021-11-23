package miragefairy2019.mod3.erg.api;

public interface IErgEntry {

    public IErgType getType();

    /**
     * 非負です。
     */
    public double getPower();

}
