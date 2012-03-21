package com.clikclok.domain;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.clikclok.util.Constants;

/**
 * Holds the x and y coordinates for a {@link Tile} in the grid
 * @author David
 */
public class TilePosition {
	private int positionAcrossGrid;
	private int positionDownGrid;
		
	public TilePosition(int overallPosition) {
		positionAcrossGrid = overallPosition % Constants.GRID_WIDTH;
		positionDownGrid = (int) overallPosition / Constants.GRID_WIDTH; 
	}
	
	public TilePosition(int positionAcrossGrid, int positionDownGrid)
	{
		this.positionAcrossGrid = positionAcrossGrid;
		this.positionDownGrid = positionDownGrid;
	}
	
	/**
	 * Determines the adjacent tile based on the specified direction
	 * @param tileDirection
	 * @return
	 */
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
	
	/**
	 * @return the 4 adjacent tile positions for the current tile
	 */
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
		return new HashCodeBuilder().append(positionAcrossGrid).append(positionDownGrid).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TilePosition){
			TilePosition other = (TilePosition) obj;
			return new EqualsBuilder().append(positionAcrossGrid, other.positionAcrossGrid).append(positionDownGrid, other.positionDownGrid).isEquals();
		}
		return false;
	}	
}
