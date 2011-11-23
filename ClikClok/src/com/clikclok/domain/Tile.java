package com.clikclok.domain;

import android.util.Log;

public class Tile {
	
	private TileDirection direction;
	private TileColour colour;
	private TilePosition tilePosition;
	
	public Tile(TileDirection direction, TileColour colour, TilePosition tilePosition)
	{
		super();
		this.direction = direction;
		this.colour = colour;
		this.tilePosition = tilePosition;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result
				+ ((tilePosition == null) ? 0 : tilePosition.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (colour != other.colour)
			return false;
		if (direction != other.direction)
			return false;
		if (tilePosition == null) {
			if (other.tilePosition != null)
				return false;
		} else if (!tilePosition.equals(other.tilePosition))
			return false;
		return true;
	}
	public TilePosition getTilePosition() {
		return tilePosition;
	}
	public void setTilePosition(TilePosition tilePosition) {
		this.tilePosition = tilePosition;
	}
	public TileDirection getDirection() {
		return direction;
	}
	public void setDirection(TileDirection direction) {
		this.direction = direction;
	}
	public TileColour getColour() {
		return colour;
	}
	public void setColour(TileColour colour) {
		this.colour = colour;
	}
	
	@Override
	public String toString() {
		return "Tile [direction=" + direction + ", colour=" + colour
				+ ", tilePosition=" + tilePosition + "]";
	}
	
	/**
	 * @param adjacentTile
	 * @return a boolean indicating whether the current tile points in the direction of the specified adjacent tile 
	 */
	public boolean isPointingTo(Tile targetTile)
	{	
		TilePosition targetTilePosition = targetTile.getTilePosition();
		
		Log.v(this.getClass().toString(), "Target tile position is " + targetTilePosition);
		
		TilePosition adjacentTilePosition = tilePosition.getAdjacentTilePosition(direction);
		
		Log.v(this.getClass().toString(), "Actual tile this is facing is " + adjacentTilePosition);
		
		boolean isPointingTo = adjacentTilePosition.equals(targetTilePosition);
		
		Log.v(this.getClass().toString(), "Tiles touch? " + isPointingTo);
		
		return isPointingTo;
	}
	
	
	
}
