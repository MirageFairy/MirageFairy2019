package miragefairy2019.mod.api.fairystick;

public interface IFairyStickCraftExecutor
{

	public void onUpdate();

	public void hookOnUpdate(Runnable listener);

	public void onCraft();

	public void hookOnCraft(Runnable listener);

}
