package com.clikclok.service.impl;

import java.util.Comparator;

import com.clikclok.domain.TilePosition;
import com.google.inject.Singleton;

/**
 * Comparator that determines the ordering of a groups of tiles based on how close they are to a target tile.
 * By default this tile position is the top left tile
 * @author David
 */
@Singleton
public class TilePositionComparator implements Comparator<TilePosition> {
	private TilePosition targetTilePosition;
	
	public TilePositionComparator() {
		targetTilePosition = new TilePosition(0, 0);
	}
	
	public TilePositionComparator(TilePosition targetTilePosition) {
		this.targetTilePosition = targetTilePosition;
	}
	
	@Override
	public int compare(TilePosition tileOne, TilePosition tileTwo) {
		if(tileOne.equals(tileTwo))
		{
			return 0;
		}
		else if(tileOne.equals(new TilePosition(tileTwo.getPositionDownGrid(), tileTwo.getPositionAcrossGrid())))
		{
			// If this position's x coordinate coordinate matches the other's y coordinate
			// and the y coordinate matches the other's x coordinate
			// then the formula below will return the same result even though they are different positions
			// Therefore we will simply add this beside it 
			return 1;
		}
		else
		{
			double comparison = calculateDistanceFromTargetTile(tileOne) - calculateDistanceFromTargetTile(tileTwo);		
			// Round off these values as we do not want them to be cast to zero
			comparison = (0 > comparison && comparison > -1) ? -1 : comparison;
			comparison = (0 < comparison && comparison < 1) ? 1 : comparison;
			return (int)comparison;
		}
	}
	
	/**
	 * Simple mathematical equation to determine the distance between this TilePosition and the target TilePosition
	 * @param tilePosition
	 * @return
	 */
	private double calculateDistanceFromTargetTile(TilePosition tilePosition)
	{
		int x1Coordinate = targetTilePosition.getPositionAcrossGrid();
		int y1Coordinate = targetTilePosition.getPositionDownGrid();
		int x2Coordinate = tilePosition.getPositionAcrossGrid();
		int y2Coordinate = tilePosition.getPositionDownGrid();
		
		double distanceFromTopLeft = Math.sqrt(((x2Coordinate - x1Coordinate) * (x2Coordinate - x1Coordinate)) + ((y2Coordinate - y1Coordinate) * (y2Coordinate - y1Coordinate)));
		return distanceFromTopLeft;
	}
}
