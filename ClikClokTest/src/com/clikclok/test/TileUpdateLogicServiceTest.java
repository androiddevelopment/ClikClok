package com.clikclok.test;

import java.util.Set;

import junit.framework.TestCase;

import com.clikclok.domain.Level;
import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.TileDirection;
import com.clikclok.domain.TilePosition;
import com.clikclok.domain.GameState;
import com.clikclok.service.TileUpdateLogicService;

public class TileUpdateLogicServiceTest extends TestCase {
	private TileUpdateLogicService tileUpdater;	
	private GameState tileStatus;
	
	@Override
	protected void setUp() throws Exception {
		tileUpdater = new TileUpdateLogicService();
		tileStatus = new GameState(TestUtilities.initializeSmallTestTileGrid());
		Level.setGridSize(tileStatus.getTileGridSize());
		super.setUp();
	}

	/**
	 * Verifies that the correct adjacent tiles colours are updated when a nearby tile is updated
	 */
	public void testUpdateColours()
	{
		Tile tileUpdated = tileStatus.getTileInformation(new TilePosition(0, 0));
		
		tileUpdater.updateColours(tileUpdated, tileStatus, TileColour.GREEN, TileColour.RED);	
				
		assertEquals(6, tileStatus.getNumberOfTilesForColour(TileColour.GREEN));
		assertEquals(TileColour.GREEN, tileStatus.getTileInformation(new TilePosition(1, 0)).getColour());
		assertEquals(TileColour.GREEN, tileStatus.getTileInformation(new TilePosition(2, 0)).getColour());
		assertEquals(TileColour.GREEN, tileStatus.getTileInformation(new TilePosition(2, 1)).getColour());
		assertEquals(TileColour.GREEN, tileStatus.getTileInformation(new TilePosition(1, 1)).getColour());
		assertEquals(TileColour.GREEN, tileStatus.getTileInformation(new TilePosition(3, 1)).getColour());
		
	}
	
	/**
	 * Verifies that the opposing colours will be updated if they are touching a file that has been updated 
	 */
	public void testOpposingColoursUpdated()
	{
		Tile tileOne = tileStatus.getTileInformation(new TilePosition(2,0));
		tileOne.setColour(TileColour.RED);
		tileStatus.updateTile(tileOne);
		Tile tileTwo = tileStatus.getTileInformation(new TilePosition(1,1));
		tileTwo.setColour(TileColour.RED);
		tileStatus.updateTile(tileTwo);
		Tile tileUpdated = tileStatus.getTileInformation(new TilePosition(0,0));
		
		tileUpdater.updateColours(tileUpdated, tileStatus, TileColour.GREEN, TileColour.RED);	
		
		Set<TilePosition> greenPositions = tileStatus.getTilePositionsForColour(TileColour.GREEN);
		
		assertEquals(6, greenPositions.size());
		assertEquals(TileColour.GREEN, tileStatus.getTileInformation(new TilePosition(1,0)).getColour());
		assertEquals(TileColour.GREEN, tileStatus.getTileInformation(new TilePosition(2,0)).getColour());
		assertEquals(TileColour.GREEN, tileStatus.getTileInformation(new TilePosition(2,1)).getColour());
		assertEquals(TileColour.GREEN, tileStatus.getTileInformation(new TilePosition(1,1)).getColour());
		assertEquals(TileColour.GREEN, tileStatus.getTileInformation(new TilePosition(3, 1)).getColour());
	}
	
	/**
	 * Verifies that no error is thrown when a tile's direction is pointing out-of-bounds
	 */
	public void testNonExistingTilesNotUpdated()
	{
		Tile tile = tileStatus.getTileInformation(new TilePosition(2,0));
		tile.setDirection(TileDirection.NORTH);
		tileStatus.updateTile(tile);
		Tile tileUpdated = tileStatus.getTileInformation(new TilePosition(0,0));
		
		tileUpdater.updateColours(tileUpdated, tileStatus, TileColour.GREEN, TileColour.RED);	
		
		Set<TilePosition> greenPositions = tileStatus.getTilePositionsForColour(TileColour.GREEN);
		
		assertEquals(3, greenPositions.size());
		assertEquals(TileColour.GREEN, tileStatus.getTileInformation(new TilePosition(1,0)).getColour());
		assertEquals(TileColour.GREEN, tileStatus.getTileInformation(new TilePosition(2,0)).getColour());
	}
	
	/**
	 * Verifies that the AI calculates the correct optimum tile to select based on the number of adjacent tiles
	 * that will be updated
	 */
	public void testCalculateOptimumAITile()
	{
		Tile tileOne = tileStatus.getTileInformation(new TilePosition(3,0));
		tileOne.setColour(TileColour.RED);
		tileStatus.updateTile(tileOne);
		Tile tileTwo = tileStatus.getTileInformation(new TilePosition(1,1));
		tileTwo.setColour(TileColour.RED);
		tileStatus.updateTile(tileTwo);

		Tile optimumAITile = tileUpdater.calculateOptimumAITile(tileStatus, Level.FIVE);
		
		// tileTwo should update 10 adjacent tiles compared to tileOne's 7
		assertEquals(tileTwo, optimumAITile);
	}
	
	public void testCalculateOptimumAITileWithOppositionColours()
	{
		Tile tileOne = tileStatus.getTileInformation(new TilePosition(1,0));
		tileOne.setColour(TileColour.RED);
		tileStatus.updateTile(tileOne);
		Tile tileTwo = tileStatus.getTileInformation(new TilePosition(2,3));
		tileTwo.setColour(TileColour.RED);
		tileStatus.updateTile(tileTwo);
		Tile tileThree = tileStatus.getTileInformation(new TilePosition(2,1));
		tileThree.setColour(TileColour.GREEN);
		tileStatus.updateTile(tileThree);
		// Change this tile's direction to ensure that (1,0) and (2,3) both result in 7 red tiles
		Tile tileFour = tileStatus.getTileInformation(new TilePosition(0, 1));
		tileFour.setDirection(TileDirection.WEST);
		tileStatus.updateTile(tileFour);
		
		Tile optimumAITile = tileUpdater.calculateOptimumAITile(tileStatus, Level.FIVE);
		
		// Both tiles being updated result in a total of 7 tiles. However, (1,0) updated a green tile
		assertEquals(tileOne, optimumAITile);
	}
		
}
