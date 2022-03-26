package miragefairy2019.mod3.erg.api;

import miragefairy2019.api.Erg;

public interface IErgEntry {

    public Erg getType();

    /**
     * 非負です。
     */
    public double getPower();

}
