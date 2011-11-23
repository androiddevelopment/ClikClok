package com.clikclok.test;

import java.util.ArrayList;
import java.util.Set;

import junit.framework.TestCase;

import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.TileDirection;
import com.clikclok.domain.TilePosition;
import com.clikclok.domain.GameState;

public class TileStatusTest extends TestCase{
	private GameState tileStatus;	
	
	@Override
	protected void setUp() throws Exception {
		tileStatus = new GameState(TestUtilities.initializeSmallTestTileGrid());
		super.setUp();
	}
	
	/**
	 * Verifies that the tile information returned for a specified position is correct.
	 * Also verifies that a tile cannot be updated without using the TileStatus.updateTile() method
	 */
	public void testGetTileInformation()
	{
		Tile tile = tileStatus.getTileInformation(new TilePosition(1, 1));
		assertEquals(TileColour.GREY, tile.getColour());
		assertEquals(new TilePosition(1,1), tile.getTilePosition());
		assertEquals(TileDirection.EAST, tile.getDirection());
		
		// Update the direction and verify that when we retrieve the tile again that
		// it's direction has not been updated as we did not update it using the updateTile() method
		tile.setDirection(TileDirection.SOUTH);
		tile = tileStatus.getTileInformation(new TilePosition(1, 1));
		assertEquals(TileDirection.EAST, tile.getDirection());		
	}
	
	/**
	 * Verifies that invoking the updateTile() method successfully updates that tile
	 */
	public void testUpdateTile()
	{
		Tile greenTile = new Tile(TileDirection.SOUTH, TileColour.GREEN, new TilePosition(1,1));
		tileStatus.updateTile(greenTile);
		
		Tile updatedTile = tileStatus.getTileInformation(new TilePosition(1, 1));
		assertEquals(TileColour.GREEN, updatedTile.getColour());
		assertEquals(TileDirection.SOUTH, updatedTile.getDirection());
		assertEquals(new TilePosition(1, 1), updatedTile.getTilePosition());
		
		assertEquals(2, tileStatus.getNumberOfTilesForColour(TileColour.GREEN));		
	}
	
	/**
	 * Verifies that invoking the updateTile() method successfully updates the tile positions collection
	 */
	public void testUpdateTileUpdatesTilePositionsForColours()
	{
		Tile tileOne = new Tile(TileDirection.SOUTH, TileColour.GREEN, new TilePosition(1,1));
		tileStatus.updateTile(tileOne);
		Tile tileTwo = new Tile(TileDirection.SOUTH, TileColour.RED, new TilePosition(3,2));
		tileStatus.updateTile(tileTwo);
		
		assertEquals(2, tileStatus.getNumberOfTilesForColour(TileColour.GREEN));
		assertEquals(2, tileStatus.getNumberOfTilesForColour(TileColour.RED));
		
		tileOne.setColour(TileColour.RED);
		tileStatus.updateTile(tileOne);
		
		Set<TilePosition> greenTiles = tileStatus.getTilePositionsForColour(TileColour.GREEN);
		Set<TilePosition> redTiles = tileStatus.getTilePositionsForColour(TileColour.RED);
		
		assertEquals(1, greenTiles.size());
		assertEquals(3, redTiles.size());
		
		assertTrue(greenTiles.contains(new TilePosition(0, 0)));
		assertTrue(redTiles.contains(new TilePosition(1, 1)));
		assertTrue(redTiles.contains(new TilePosition(3, 2)));
		assertTrue(redTiles.contains(new TilePosition(3, 3)));		
	}
	
	/**
	 * Verifies that the getTilePositionsForColour() method correctly returns a set containing all TilePosition objects
	 * for the specified colour
	 */
	public void testGetTilePositionsForColour()
	{
		Set<TilePosition> greenTiles = tileStatus.getTilePositionsForColour(TileColour.GREEN);
		
		TilePosition greenTilePosition = new TilePosition(0, 0);
		
		greenTiles.remove(greenTilePosition);
		
		greenTiles = tileStatus.getTilePositionsForColour(TileColour.GREEN);
		
		assertTrue(greenTiles.contains(greenTilePosition));
	}
	
	/**
	 * Verifies that the various methods to determine grid size return the correct values
	 */
	public void testGridSizes()
	{
		assertEquals(4, tileStatus.getGridWidth());
		assertEquals(4, tileStatus.getGridHeight());
		assertEquals(16, tileStatus.getTileGridSize());
	}
	
	/**
	 * Verifies that the TilePosition compare algorithm correctly places TilePositions in their correct position
	 * 
	 */
	public void testTilePositionOrdering()
	{
		Set<TilePosition> redTilePositions = tileStatus.getTilePositionsForColour(TileColour.RED);
		ArrayList<TilePosition> positionList = new ArrayList<TilePosition>(redTilePositions);
		TilePosition tilePositionFour = new TilePosition(3, 3);
		assertEquals(tilePositionFour, positionList.get(0));
		
		TilePosition tilePositionOne = new TilePosition(1, 1);
		Tile tileOne = tileStatus.getTileInformation(tilePositionOne);
		tileOne.setColour(TileColour.RED);
		tileStatus.updateTile(tileOne);
		positionList = new ArrayList<TilePosition>(tileStatus.getTilePositionsForColour(TileColour.RED));
		assertEquals(tilePositionOne, positionList.get(0));
		assertEquals(tilePositionFour, positionList.get(1));
		
		TilePosition tilePositionTwo = new TilePosition(2, 2);
		Tile tileTwo = tileStatus.getTileInformation(tilePositionTwo);
		tileTwo.setColour(TileColour.RED);
		tileStatus.updateTile(tileTwo);
		positionList = new ArrayList<TilePosition>(tileStatus.getTilePositionsForColour(TileColour.RED));
		assertEquals(tilePositionOne, positionList.get(0));
		assertEquals(tilePositionTwo, positionList.get(1));
		assertEquals(tilePositionFour, positionList.get(2));
		
		TilePosition tilePositionThree = new TilePosition(0, 0);
		Tile tileThree = tileStatus.getTileInformation(tilePositionThree);
		tileThree.setColour(TileColour.RED);
		tileStatus.updateTile(tileThree);
		positionList = new ArrayList<TilePosition>(tileStatus.getTilePositionsForColour(TileColour.RED));
		assertEquals(tilePositionThree, positionList.get(0));
		assertEquals(tilePositionOne, positionList.get(1));
		assertEquals(tilePositionTwo, positionList.get(2));
		assertEquals(tilePositionFour, positionList.get(3));
		
		TilePosition tilePositionFive = new TilePosition(1, 2);
		Tile tileFive = tileStatus.getTileInformation(tilePositionFive);
		tileFive.setColour(TileColour.RED);
		tileStatus.updateTile(tileFive);
		positionList = new ArrayList<TilePosition>(tileStatus.getTilePositionsForColour(TileColour.RED));
		assertEquals(tilePositionThree, positionList.get(0));
		assertEquals(tilePositionOne, positionList.get(1));
		assertEquals(tilePositionFive, positionList.get(2));
		assertEquals(tilePositionTwo, positionList.get(3));
		assertEquals(tilePositionFour, positionList.get(4));
		
		TilePosition tilePositionSix = new TilePosition(2, 1);
		Tile tileSix = tileStatus.getTileInformation(tilePositionSix);
		tileSix.setColour(TileColour.RED);
		tileStatus.updateTile(tileSix);
		positionList = new ArrayList<TilePosition>(tileStatus.getTilePositionsForColour(TileColour.RED));
		assertEquals(tilePositionThree, positionList.get(0));
		assertEquals(tilePositionOne, positionList.get(1));
		assertEquals(tilePositionFive, positionList.get(2));
		assertEquals(tilePositionSix, positionList.get(3));
		assertEquals(tilePositionTwo, positionList.get(4));
		assertEquals(tilePositionFour, positionList.get(5));
		
		
	}
	
	/**
	 * Verifies that a clone of the TileStatus does not contain any state belonging to cloned object
	 */
	public void testClone()
	{
		GameState clone = tileStatus.clone();
		
		TilePosition tilePosition = new TilePosition(1, 1);
		Tile tile = tileStatus.getTileInformation(tilePosition);
		tile.setColour(TileColour.RED);
		tileStatus.updateTile(tile);
		
		Tile tileClone = clone.getTileInformation(tilePosition);
		assertEquals(TileColour.GREY, tileClone.getColour());
		
		assertEquals(tileStatus.getNumberOfTilesForColour(TileColour.RED), clone.getNumberOfTilesForColour(TileColour.RED) + 1);
	}
}
