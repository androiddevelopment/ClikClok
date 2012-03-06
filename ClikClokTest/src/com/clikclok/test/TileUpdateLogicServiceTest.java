package com.clikclok.test;

import java.util.Set;

import junit.framework.TestCase;

import com.clikclok.domain.Level;
import com.clikclok.domain.TestUtilities;
import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.TileDirection;
import com.clikclok.domain.TilePosition;
import com.clikclok.domain.GameState;
import com.clikclok.service.TilePositionComparator;
import com.clikclok.service.TileUpdateLogicService;

public class TileUpdateLogicServiceTest extends TestCase {
	private TileUpdateLogicService tileUpdater;	
	private GameState gameState;
	
	@Override
	protected void setUp() throws Exception {
		tileUpdater = new TileUpdateLogicService();
		gameState = new GameState(TestUtilities.initializeSmallTestTileGrid());
		Level.setGridSize(gameState.getTileGridSize());
		super.setUp();
	}

	/**
	 * Verifies that the correct adjacent tiles colours are updated when a nearby tile is updated
	 */
	public void testUpdateColours()
	{
		Tile tileUpdated = gameState.getTileInformation(new TilePosition(0, 0));
		
		tileUpdater.updateColoursAndDirection(tileUpdated, gameState, TileColour.GREEN, TileColour.RED, 0, false);	
				
		assertEquals(6, gameState.getNumberOfTilesForColour(TileColour.GREEN));
		assertEquals(TileColour.GREEN, gameState.getTileInformation(new TilePosition(1, 0)).getColour());
		assertEquals(TileColour.GREEN, gameState.getTileInformation(new TilePosition(2, 0)).getColour());
		assertEquals(TileColour.GREEN, gameState.getTileInformation(new TilePosition(2, 1)).getColour());
		assertEquals(TileColour.GREEN, gameState.getTileInformation(new TilePosition(1, 1)).getColour());
		assertEquals(TileColour.GREEN, gameState.getTileInformation(new TilePosition(3, 1)).getColour());
		
	}
	
	/**
	 * Verifies that the opposing colours will be updated if they are touching a file that has been updated 
	 */
	public void testOpposingColoursUpdated()
	{
		Tile tileOne = gameState.getTileInformation(new TilePosition(2,0));
		gameState.updateTileColour(tileOne, TileColour.RED);
		Tile tileTwo = gameState.getTileInformation(new TilePosition(1,1));
		gameState.updateTileColour(tileTwo, TileColour.RED);
		Tile tileUpdated = gameState.getTileInformation(new TilePosition(0,0));
		
		int enemyTilesGained = tileUpdater.updateColoursAndDirection(tileUpdated, gameState, TileColour.GREEN, TileColour.RED, 0, false);	
		
		Set<TilePosition> greenPositions = gameState.getTilePositionsForColour(TileColour.GREEN);
		
		assertEquals(6, greenPositions.size());
		assertEquals(TileColour.GREEN, gameState.getTileInformation(new TilePosition(1,0)).getColour());
		assertEquals(TileColour.GREEN, gameState.getTileInformation(new TilePosition(2,0)).getColour());
		assertEquals(TileColour.GREEN, gameState.getTileInformation(new TilePosition(2,1)).getColour());
		assertEquals(TileColour.GREEN, gameState.getTileInformation(new TilePosition(1,1)).getColour());
		assertEquals(TileColour.GREEN, gameState.getTileInformation(new TilePosition(3,1)).getColour());
		assertEquals(2, enemyTilesGained);
		
		
	}
	
	/**
	 * Verifies that no error is thrown when a tile's direction is pointing out-of-bounds
	 */
	public void testNonExistingTilesNotUpdated()
	{
		Tile tile = gameState.getTileInformation(new TilePosition(2,0));
		gameState.updateTileColourAndDirection(tile, tile.getColour(), TileDirection.NORTH);
		Tile tileUpdated = gameState.getTileInformation(new TilePosition(0,0));
		
		tileUpdater.updateColoursAndDirection(tileUpdated, gameState, TileColour.GREEN, TileColour.RED, 0, false);	
		
		Set<TilePosition> greenPositions = gameState.getTilePositionsForColour(TileColour.GREEN);
		
		assertEquals(3, greenPositions.size());
		assertEquals(TileColour.GREEN, gameState.getTileInformation(new TilePosition(1,0)).getColour());
		assertEquals(TileColour.GREEN, gameState.getTileInformation(new TilePosition(2,0)).getColour());
	}
	
	/**
	 * Verifies that the AI calculates the correct optimum tile to select based on the number of adjacent tiles
	 * that will be updated
	 */
	public void testCalculateOptimumAITile()
	{
		Tile tileOne = gameState.getTileInformation(new TilePosition(3,0));
		gameState.updateTileColour(tileOne, TileColour.RED);
		Tile tileTwo = gameState.getTileInformation(new TilePosition(1,1));
		gameState.updateTileColour(tileTwo, TileColour.RED);

		Tile optimumAITile = tileUpdater.calculateOptimumAITile(gameState, Level.FIVE, new TilePositionComparator(), false);
		
		// tileTwo should update 10 adjacent tiles compared to tileOne's 7
		assertEquals(tileTwo, optimumAITile);
	}
	
	public void testCalculateOptimumAITileWithOppositionColours()
	{
		Tile tileOne = gameState.getTileInformation(new TilePosition(1,0));
		gameState.updateTileColour(tileOne, TileColour.RED);
		Tile tileTwo = gameState.getTileInformation(new TilePosition(2,3));
		gameState.updateTileColour(tileTwo, TileColour.RED);
		Tile tileThree = gameState.getTileInformation(new TilePosition(2,1));
		gameState.updateTileColour(tileThree, TileColour.GREEN);
		// Change this tile's direction to ensure that (1,0) and (2,3) both result in 7 red tiles
		Tile tileFour = gameState.getTileInformation(new TilePosition(0, 1));
		gameState.updateTileColourAndDirection(tileFour, tileFour.getColour(), TileDirection.WEST);
		
		Tile optimumAITile = tileUpdater.calculateOptimumAITile(gameState, Level.FIVE, new TilePositionComparator(), false);
		
		// Both tiles being updated result in a total of 7 tiles. However, (1,0) updated a green tile
		assertEquals(tileOne, optimumAITile);
	}
	
	public void testCalculateOptimumAITileWithLessNumberOfOppositionColoursThanGreyColours() {
		gameState = new GameState(TestUtilities.initializeSmallTestTileGridPredominantlyRed());
		Tile tileZeroOne = gameState.getTileInformation(new TilePosition(0, 1));
		Tile optimumAITile = tileUpdater.calculateOptimumAITile(gameState, Level.FIVE, new TilePositionComparator(), false);
		assertEquals(tileZeroOne, optimumAITile);
	}
	
	public void testCalculateOptimumAITileWithTopLeftOccupied() {
		gameState = new GameState(TestUtilities.initializeSmallTestTileGridWithTopLeftOccupied());
		Tile tileTwoThree = gameState.getTileInformation(new TilePosition(2, 3));
		Tile tileThreeTwo = gameState.getTileInformation(new TilePosition(3, 2));
		Tile optimumAITile = tileUpdater.calculateOptimumAITile(gameState, Level.ONE, new TilePositionComparator(), false);
		assertTrue(optimumAITile.equals(tileTwoThree) || optimumAITile.equals(tileThreeTwo));
	}
	
	public void testCalculateOptimumAITileWithGreenTileInTopLeft() {
		gameState = new GameState(TestUtilities.initializeSmallTestTileGridWithGreenTileInTopLeft());
		Tile tileTwoTwo = gameState.getTileInformation(new TilePosition(2, 2));
		Tile optimumAITile = tileUpdater.calculateOptimumAITile(gameState, Level.ONE, new TilePositionComparator(), false);
		assertEquals(optimumAITile, tileTwoTwo);
	}
	
}
