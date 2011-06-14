package com.clikclok.domain;

import com.clikclok.util.Constants;

import android.util.Log;

public class TilePosition {
	private int positionAcrossGrid;
	private int positionDownGrid;
		
	public TilePosition(int overallPosition) {
		positionAcrossGrid = overallPosition % Constants.GRID_WIDTH;
		positionDownGrid = (int) overallPosition / Constants.GRID_WIDTH;
		Log.d(this.getClass().toString(), "positionAcrossGrid is " + positionAcrossGrid + ". positionDownGrid is " + positionDownGrid);
	}
	
	public TilePosition(int positionAcrossGrid, int positionDownGrid)
	{
		this.positionAcrossGrid = positionAcrossGrid;
		this.positionDownGrid = positionDownGrid;
	}
	
	public TilePosition (Tile tile)
	{
		TileDirection tileDirection = tile.getDirection();
		positionAcrossGrid = tile.getTilePosition().positionAcrossGrid;
		positionDownGrid = tile.getTilePosition().positionDownGrid;
		
		switch(tileDirection)
		{
			case NORTH: this.positionDownGrid -= 1; break;
			case SOUTH: this.positionDownGrid += 1; break;
			case EAST: this.positionAcrossGrid += 1; break;
			case WEST: this.positionAcrossGrid -= 1; break;
		}
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
	
	
	
}
