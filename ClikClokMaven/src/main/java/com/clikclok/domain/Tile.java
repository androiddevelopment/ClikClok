package com.clikclok.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Class used to store all relevant information about a tile
 * @author David
 */
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
		return new HashCodeBuilder().append(direction).append(colour).append(tilePosition).hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Tile) {
			Tile other = (Tile) obj;
			return new EqualsBuilder().append(direction, other.direction).append(colour, other.colour).append(tilePosition, other.tilePosition).isEquals();
		}
		return false;
	}
	public TilePosition getTilePosition() {
		return tilePosition;
	}
	protected void setTilePosition(TilePosition tilePosition) {
		this.tilePosition = tilePosition;
	}
	public TileDirection getDirection() {
		return direction;
	}
	protected void setDirection(TileDirection direction) {
		this.direction = direction;
	}
	public TileColour getColour() {
		return colour;
	}
	protected void setColour(TileColour colour) {
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
		
		TilePosition adjacentTilePosition = tilePosition.getAdjacentTilePosition(direction);
		
		boolean isPointingTo = adjacentTilePosition.equals(targetTilePosition);
		
		return isPointingTo;
	}
	/**
	 * Turns the direction of the tile clockwise once. 
	 */
	public void updateDirection()
	{
		int degrees = (int) getDirection().getDegrees();
		
		// Add 90 to the number of degrees. If it's 270 then we are effectively adding 90 and resetting to 0 as it's 360
		degrees = (degrees == 270) ? 0 : (degrees += 90);
		
		// Set it's new direction
		setDirection(TileDirection.getTileDirection(degrees));
	}
	
	
	
}
