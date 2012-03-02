package com.clikclok.domain;

import java.util.Collection;
import java.util.HashSet;

import android.util.Log;

import com.clikclok.util.Constants;

public class TilePosition {
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
}
