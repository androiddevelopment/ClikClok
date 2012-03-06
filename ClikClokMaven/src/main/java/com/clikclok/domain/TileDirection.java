package com.clikclok.domain;


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
	
	public boolean isFacing(TileDirection otherTileDirection)
	{
		float calculation = (this.degrees + otherTileDirection.degrees) % 180;
		// If both have the same direction then they are not facing
		// Otherwise, the sum of opposite directions will always be a multiple of 180
		boolean isFacing = this.degrees == otherTileDirection.degrees ? false : (calculation == 0);
		
		return isFacing;
	}
}

