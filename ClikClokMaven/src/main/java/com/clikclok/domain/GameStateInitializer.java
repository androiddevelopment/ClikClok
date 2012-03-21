package com.clikclok.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.clikclok.service.TileUpdateLogicService;
import com.clikclok.util.Constants;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Performs the logic to initialize a {@link GameState}, such as populating it with random tiles and initializing
 * the user and AI's starting tiles
 * @author David
 */
@Singleton
public class GameStateInitializer {
	@Inject
	private TileUpdateLogicService tileUpdateLogicService;
	@Inject 
	private Random random;
	public GameState createNewGameState() {
		return createNewGameState(null);
	}
	
	public GameState createNewGameState(Tile[][] tiles) {
		// Only time tiles will be spcified here will be during unit tests, otherwise we will create a random grid 
		tiles = tiles == null ? initializeTileGrid() : tiles;
		GameState gameState = new GameState(tiles);
		// Initialize the top left and bottom right tiles so that the user and AI have 3 start off tiles to select from
		Tile initialUserTile = gameState.getTileInformation(new TilePosition(0, 0));
		tileUpdateLogicService.updateColoursAndDirection(initialUserTile, gameState, TileColour.GREEN, TileColour.RED, 0, false);
		Tile initialAITile = gameState.getTileInformation(new TilePosition(gameState.getGridWidth() - 1, gameState.getGridHeight() - 1));
		tileUpdateLogicService.updateColoursAndDirection(initialAITile, gameState, TileColour.RED, TileColour.GREEN, 0, false);
		return gameState;		
	}
	
	private Tile[][] initializeTileGrid()
	{
		Tile[][] tiles = null;
		tiles = createTileDirections(tiles, new TilePosition(0, 0), new TilePosition(Constants.GRID_WIDTH, Constants.GRID_HEIGHT));    	
    	// Initialize the colours and directions of the user's start off tiles		
		tiles[0][0].setColour(TileColour.GREEN);
		tiles[0][0].setDirection(TileDirection.EAST);
		tiles[0][1].setDirection(TileDirection.NORTH);
		tiles[1][0].setDirection(TileDirection.NORTH);
		tiles[0][2].setDirection(createRandomDirection(TileDirection.NORTH));
		tiles[1][1].setDirection(createRandomDirection(TileDirection.WEST, TileDirection.NORTH));		
		tiles[2][0].setDirection(createRandomDirection(TileDirection.WEST));
		// Initialize the colours and directions of the AI's start off tiles	
		tiles[Constants.GRID_WIDTH - 1][Constants.GRID_HEIGHT - 1].setColour(TileColour.RED);
		tiles[Constants.GRID_WIDTH - 1][Constants.GRID_HEIGHT - 1].setDirection(TileDirection.WEST);
		tiles[Constants.GRID_WIDTH - 1][Constants.GRID_HEIGHT - 2].setDirection(TileDirection.SOUTH);
		tiles[Constants.GRID_WIDTH - 2][Constants.GRID_HEIGHT - 1].setDirection(TileDirection.SOUTH);
		tiles[Constants.GRID_WIDTH - 1][Constants.GRID_HEIGHT - 3].setDirection(createRandomDirection(TileDirection.SOUTH));
		tiles[Constants.GRID_WIDTH - 2][Constants.GRID_HEIGHT - 2].setDirection(createRandomDirection(TileDirection.EAST, TileDirection.SOUTH));
		tiles[Constants.GRID_WIDTH - 3][Constants.GRID_HEIGHT - 1].setDirection(createRandomDirection(TileDirection.EAST));
		return tiles;
	}
		
	/**
	 * Creates a random grid of tile directions
	 * @param tileGrid
	 * @param startPosition
	 * @param endPosition
	 * @return
	 */
	private Tile[][] createTileDirections(Tile[][] tileGrid, TilePosition startPosition, TilePosition endPosition) {		
		tileGrid = new Tile[endPosition.getPositionAcrossGrid()][endPosition.getPositionDownGrid()];    	
    	for(int i = startPosition.getPositionAcrossGrid(); i < endPosition.getPositionAcrossGrid(); i++)
    	{
    		for(int j = startPosition.getPositionDownGrid(); j < endPosition.getPositionDownGrid(); j++)
    		{
    			tileGrid[i][j] = new Tile(createRandomDirection(), TileColour.GREY, new TilePosition(i, j));
    		}
    	}    	
    	return tileGrid;
	}
	
	private TileDirection createRandomDirection() {
		return createRandomDirection(new TileDirection[]{});
	}
	
	/**
	 * Creates a random direction, that is not one of the specified directions that we do not want
	 * @param directions
	 * @return
	 */
	private TileDirection createRandomDirection(TileDirection...directions) {
		List<TileDirection> directionsToExclude = Arrays.asList(directions);
		TileDirection tileDirection;
		do {
			// Generate a number between 0 and 3, and multiply this by 90 to get the correct number of degrees
			int degrees = ((random.nextInt(4)) * 90);
			tileDirection =  TileDirection.getTileDirection(degrees);	
		} while (directionsToExclude.contains(tileDirection));
		return tileDirection;
	}	
}
