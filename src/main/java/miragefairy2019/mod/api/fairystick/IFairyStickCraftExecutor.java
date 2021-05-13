package miragefairy2019.mod.api.fairystick;

public interface IFairyStickCraftExecutor
{

	public void onCraft();

	public void onUpdate();

	public void hookOnCraft(Runnable listener);

	public void hookOnUpdate(Runnable listener);

}
