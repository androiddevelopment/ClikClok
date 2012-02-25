package com.clikclok.domain;

import java.util.Collection;
import java.util.HashSet;

import android.util.Log;

import com.clikclok.util.Constants;

public class TilePosition implements Comparable<TilePosition>{
	private int positionAcrossGrid;
	private int positionDownGrid;
		
	public TilePosition(int overallPosition) {
		positionAcrossGrid = overallPosition % Constants.GRID_WIDTH;
		positionDownGrid = (int) overallPosition / Constants.GRID_WIDTH; 
		Log.v(this.getClass().toString(), "positionAcrossGrid is " + positionAcrossGrid + ". positionDownGrid is " + positionDownGrid);
	}
	
	public TilePosition(int positionAcrossGrid, int positionDownGrid)
	{
		this.positionAcrossGrid = positionAcrossGrid;
		this.positionDownGrid = positionDownGrid;
	}
	
	public TilePosition getAdjacentTilePosition(TileDirection tileDirection)
	{
		TilePosition adjacentTilePosition = new TilePosition(positionAcrossGrid, positionDownGrid);
				
		switch(tileDirection)
		{
			case NORTH: adjacentTilePosition.positionDownGrid -= 1; break;
			case SOUTH: adjacentTilePosition.positionDownGrid += 1; break;
			case EAST: adjacentTilePosition.positionAcrossGrid += 1; break;
			case WEST: adjacentTilePosition.positionAcrossGrid -= 1; break;
		}
		
		return adjacentTilePosition;
	}
	
	public Collection<TilePosition> getAdjacentTilePositions()
	{
		// There will be at most 4 adjacent tiles that can touch a tile
		Collection<TilePosition> adjacentTiles = new HashSet<TilePosition>(4);
		
		// Loop through each direction possible and create a new TilePosition for each adjacent tile
		// We will use the existing constructor created before
		for(TileDirection direction : TileDirection.values())
		{
			adjacentTiles.add(getAdjacentTilePosition(direction));
		}
		
		return adjacentTiles;
		
	}
	
	public int getPositionAcrossGrid() {
		return positionAcrossGrid;
	}
	
	public int getPositionDownGrid() {
		return positionDownGrid;
	}

	@Override
	public String toString() {
		return "TilePosition [positionAcrossGrid=" + positionAcrossGrid
				+ ", positionDownGrid=" + positionDownGrid + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + positionAcrossGrid;
		result = prime * result + positionDownGrid;
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
		TilePosition other = (TilePosition) obj;
		if (positionAcrossGrid != other.positionAcrossGrid)
			return false;
		if (positionDownGrid != other.positionDownGrid)
			return false;
		return true;
	}

	@Override
	public int compareTo(TilePosition other) {
		
		if(this.equals(other))
		{
			return 0;
		}
		else if(this.equals(new TilePosition(other.positionDownGrid, other.positionAcrossGrid)))
		{
			// If this position's x coordinate coordinate matches the other's y coordinate
			// and the y coordinate matches the other's x coordinate
			// then the formula below will return the same result even though they are different positions
			// Therefore we will simply add this beside it 
			return 1;
		}
		else
		{
			double comparison = calculateDistanceFromTopLeft(this) - calculateDistanceFromTopLeft(other);		
			Log.v(this.getClass().toString(), "Comparison between this TilePosition " + this + " and " + other + " is " + comparison);
			// Round off these values as we do not want them to be cast to zero
			comparison = (0 > comparison && comparison > -1) ? -1 : comparison;
			comparison = (0 < comparison && comparison < 1) ? 1 : comparison;
			return (int)comparison;
		}
	}
	
	private double calculateDistanceFromTopLeft(TilePosition tilePosition)
	{
		int xCoordinate = tilePosition.getPositionAcrossGrid();
		int yCoordinate = tilePosition.getPositionDownGrid();
		
		double distanceFromTopLeft = Math.sqrt((xCoordinate * xCoordinate) + (yCoordinate * yCoordinate));
		
		return distanceFromTopLeft;
	}
	
	
}
