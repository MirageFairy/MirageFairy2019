package miragefairy2019.mod3.erg.api;

public interface IErgEntry {

    public EnumErgType getType();

    /**
     * 非負です。
     */
    public double getPower();

}
