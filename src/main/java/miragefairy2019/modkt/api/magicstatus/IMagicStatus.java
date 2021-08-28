package miragefairy2019.modkt.api.magicstatus;

public interface IMagicStatus<T> {

    public String getName();

    public IMagicStatusFunction<T> getFunction();

    public IMagicStatusFormatter<T> getFormatter();

}
