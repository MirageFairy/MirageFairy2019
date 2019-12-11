package miragefairy2019.mod.modules.fairyweapon.magic;

public enum EnumTargetExecutability
{
	EFFECTIVE(0xFFFFFF),
	OVERFLOWED(0xFFFF00),
	INVALID(0xFF0000),
	;

	public final int color;

	private EnumTargetExecutability(int color)
	{
		this.color = color;
	}

}
