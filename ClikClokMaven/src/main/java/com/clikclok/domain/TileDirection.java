package com.clikclok.domain;


/**
 * Enum to store all directions and their relevant degrees
 * @author David
 *
 */
public enum TileDirection {
	NORTH(270),
	SOUTH(90),
	EAST(0),
	WEST(180);
	
	private float degrees; // Is it recommended to use floats for performance reasons?
	
	private TileDirection(float degrees)
	{
		this.degrees = degrees;
	}

	public float getDegrees() {
		return degrees;
	}
	
	/**
	 * Return the appropriate direction based on the number of degrees
	 * @param degrees
	 * @return
	 */
	public static TileDirection getTileDirection(int degrees)
	{
		for(TileDirection tileDirection : TileDirection.values())
		{
			if(tileDirection.getDegrees() == degrees)
			{
				return tileDirection;
			}
		}
		return null;
	}	
}

