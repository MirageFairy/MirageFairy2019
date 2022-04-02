package miragefairy2019.api;

public interface IPickExecutor {

    /**
     * @return 収穫が成功したか否か
     */
    public boolean tryPick(int fortune);

}
