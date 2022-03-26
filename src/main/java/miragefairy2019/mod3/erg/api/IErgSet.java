package miragefairy2019.mod3.erg.api;

import miragefairy2019.api.Erg;

public interface IErgSet {

    public Iterable<IErgEntry> getEntries();

    /**
     * 非負です。
     */
    public double getPower(Erg type);

}

