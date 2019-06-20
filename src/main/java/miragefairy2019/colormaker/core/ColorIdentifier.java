package miragefairy2019.colormaker.core;

import java.awt.Color;

public class ColorIdentifier
{

	public final String string;

	public ColorIdentifier(String string)
	{
		this.string = string;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((string == null) ? 0 : string.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ColorIdentifier other = (ColorIdentifier) obj;
		if (string == null) {
			if (other.string != null) return false;
		} else if (!string.equals(other.string)) return false;
		return true;
	}

	public Color decode()
	{
		return Color.decode(string);
	}

}
