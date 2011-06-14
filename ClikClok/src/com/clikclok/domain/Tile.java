package com.clikclok.domain;

public class Tile {
	
	private TileDirection direction;
	private TileColour colour;
	private TilePosition tilePosition;
	
	public Tile()
	{
		
	}
	
	public Tile(TileDirection direction, TileColour colour, TilePosition tilePosition)
	{
		super();
		this.direction = direction;
		this.colour = colour;
		this.tilePosition = tilePosition;
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
	
	
}
