package com.clikclok.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import junit.framework.TestCase;

import com.clikclok.domain.TestUtilities;
import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.TileDirection;
import com.clikclok.domain.TilePosition;
import com.clikclok.domain.GameState;
import com.clikclok.service.TilePositionComparator;

public class GameStateTest extends TestCase{
	private GameState gameState;	
	
	@Override
	protected void setUp() throws Exception {
		gameState = new GameState(TestUtilities.initializeSmallTestTileGrid());
		super.setUp();
	}
	
	/**
	 * Verifies that the tile information returned for a specified position is correct.
	 * Also verifies that a tile cannot be updated without using the TileStatus.updateTile() method
	 */
	public void testGetTileInformation()
	{
		Tile tile = gameState.getTileInformation(new TilePosition(1, 1));
		assertEquals(TileColour.GREY, tile.getColour());
		assertEquals(new TilePosition(1,1), tile.getTilePosition());
		assertEquals(TileDirection.EAST, tile.getDirection());
		
	}
	
	/**
	 * Verifies that invoking the updateTile() method successfully updates that tile
	 */
	public void testUpdateTile()
	{
		Tile greenTile = gameState.getTileInformation(new TilePosition(1, 1));
		gameState.updateTileColourAndDirection(greenTile, TileColour.GREEN);
		
		Tile updatedTile = gameState.getTileInformation(new TilePosition(1, 1));
		assertEquals(TileColour.GREEN, updatedTile.getColour());
		assertEquals(TileDirection.SOUTH, updatedTile.getDirection());
		assertEquals(new TilePosition(1, 1), updatedTile.getTilePosition());
		
		assertEquals(2, gameState.getNumberOfTilesForColour(TileColour.GREEN));		
	}
	
	/**
	 * Verifies that invoking the updateTile() method successfully updates the tile positions collection
	 */
	public void testUpdateTileUpdatesTilePositionsForColours()
	{
		Tile tileOne = gameState.getTileInformation(new TilePosition(1, 1));
		Tile tileTwo = gameState.getTileInformation(new TilePosition(3,2));
		gameState.updateTileColour(tileOne, TileColour.GREEN);
		gameState.updateTileColour(tileTwo, TileColour.RED);
		
		assertEquals(2, gameState.getNumberOfTilesForColour(TileColour.GREEN));
		assertEquals(2, gameState.getNumberOfTilesForColour(TileColour.RED));
		
		gameState.updateTileColour(tileOne, TileColour.RED);
		
		Set<TilePosition> greenTiles = gameState.getTilePositionsForColour(TileColour.GREEN);
		Set<TilePosition> redTiles = gameState.getTilePositionsForColour(TileColour.RED);
		
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
		Set<TilePosition> greenTiles = gameState.getTilePositionsForColour(TileColour.GREEN);
		
		TilePosition greenTilePosition = new TilePosition(0, 0);
		
		greenTiles = gameState.getTilePositionsForColour(TileColour.GREEN);
		
		assertTrue(greenTiles.contains(greenTilePosition));
	}
	
	/**
	 * Verifies that the various methods to determine grid size return the correct values
	 */
	public void testGridSizes()
	{
		assertEquals(4, gameState.getGridWidth());
		assertEquals(4, gameState.getGridHeight());
		assertEquals(16, gameState.getTileGridSize());
	}
	
	/**
	 * Verifies that the TilePosition compare algorithm correctly places TilePositions in their correct position
	 * 
	 */
	public void testTilePositionOrdering()
	{
		TilePositionComparator comparator = new TilePositionComparator();
		Set<TilePosition> redTilePositions = gameState.getTilePositionsForColour(TileColour.RED);
		ArrayList<TilePosition> positionList = new ArrayList<TilePosition>(redTilePositions);
		Collections.sort(positionList, comparator);
		TilePosition tilePositionThreeThree = new TilePosition(3, 3);
		assertEquals(tilePositionThreeThree, positionList.get(0));
		
		TilePosition tilePositionOneOne = new TilePosition(1, 1);
		Tile tileOne = gameState.getTileInformation(tilePositionOneOne);
		gameState.updateTileColour(tileOne, TileColour.RED);
		positionList = new ArrayList<TilePosition>(gameState.getTilePositionsForColour(TileColour.RED));
		Collections.sort(positionList, comparator);
		assertEquals(tilePositionOneOne, positionList.get(0));
		assertEquals(tilePositionThreeThree, positionList.get(1));
		
		TilePosition tilePositionTwoTwo = new TilePosition(2, 2);
		Tile tileTwo = gameState.getTileInformation(tilePositionTwoTwo);
		gameState.updateTileColour(tileTwo, TileColour.RED);
		positionList = new ArrayList<TilePosition>(gameState.getTilePositionsForColour(TileColour.RED));
		Collections.sort(positionList, comparator);
		assertEquals(tilePositionOneOne, positionList.get(0));
		assertEquals(tilePositionTwoTwo, positionList.get(1));
		assertEquals(tilePositionThreeThree, positionList.get(2));
		
		TilePosition tilePositionZeroZero = new TilePosition(0, 0);
		Tile tileThree = gameState.getTileInformation(tilePositionZeroZero);
		gameState.updateTileColour(tileThree, TileColour.RED);
		positionList = new ArrayList<TilePosition>(gameState.getTilePositionsForColour(TileColour.RED));
		Collections.sort(positionList, comparator);
		assertEquals(tilePositionZeroZero, positionList.get(0));
		assertEquals(tilePositionOneOne, positionList.get(1));
		assertEquals(tilePositionTwoTwo, positionList.get(2));
		assertEquals(tilePositionThreeThree, positionList.get(3));
		
		TilePosition tilePositionOneTwo = new TilePosition(1, 2);
		Tile tileFive = gameState.getTileInformation(tilePositionOneTwo);
		gameState.updateTileColour(tileFive, TileColour.RED);
		positionList = new ArrayList<TilePosition>(gameState.getTilePositionsForColour(TileColour.RED));
		Collections.sort(positionList, comparator);
		assertEquals(tilePositionZeroZero, positionList.get(0));
		assertEquals(tilePositionOneOne, positionList.get(1));
		assertEquals(tilePositionOneTwo, positionList.get(2));
		assertEquals(tilePositionTwoTwo, positionList.get(3));
		assertEquals(tilePositionThreeThree, positionList.get(4));
		
		TilePosition tilePositionTwoOne = new TilePosition(2, 1);
		Tile tileSix = gameState.getTileInformation(tilePositionTwoOne);
		gameState.updateTileColour(tileSix, TileColour.RED);
		positionList = new ArrayList<TilePosition>(gameState.getTilePositionsForColour(TileColour.RED));
		Collections.sort(positionList, comparator);
		assertEquals(tilePositionZeroZero, positionList.get(0));
		assertEquals(tilePositionOneOne, positionList.get(1));
		assertEquals(tilePositionTwoOne, positionList.get(2));
		assertEquals(tilePositionOneTwo, positionList.get(3));
		assertEquals(tilePositionTwoTwo, positionList.get(4));
		assertEquals(tilePositionThreeThree, positionList.get(5));
		
		
	}
	
	/**
	 * Verifies that a clone of the TileStatus does not contain any state belonging to cloned object
	 */
	public void testClone()
	{
		GameState clone = gameState.clone();
		
		TilePosition tilePosition = new TilePosition(1, 1);
		Tile tile = gameState.getTileInformation(tilePosition);
		gameState.updateTileColour(tile, TileColour.RED);
		
		Tile tileClone = clone.getTileInformation(tilePosition);
		assertEquals(TileColour.GREY, tileClone.getColour());
		
		assertEquals(gameState.getNumberOfTilesForColour(TileColour.RED), clone.getNumberOfTilesForColour(TileColour.RED) + 1);
	}
}
