package com.clikclok.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.TilePosition;
import com.clikclok.domain.TileStatus;
import com.clikclok.util.Constants;

import android.util.Log;

public class TileUpdater {
	
	public void updateColours(Tile tile, TileStatus tileStatus, TileColour colourToUpdate, TileColour otherColour)
	{
		Log.i(this.getClass().toString(), "Entering updateColours for tile " + tile);
		Log.i(this.getClass().toString(), tileStatus.getTilesWithColours().get(TileColour.GREEN).size() +  " green tiles exist at present");
		Log.i(this.getClass().toString(), tileStatus.getTilesWithColours().get(TileColour.RED).size() +  " red tiles exist at present");
		
		tile.setColour(colourToUpdate);
		
		Log.i(this.getClass().toString(), "After updating colour, tile is now " + tile);
		
		// Add this to the list of tiles occupied by this colour
		tileStatus.getTilesWithColours().get(colourToUpdate).add(tile.getTilePosition());
		// Remove it from the other list of tiles, regardless of whether it exists there or not
		tileStatus.getTilesWithColours().get(otherColour).remove(tile.getTilePosition());
		
		Log.i(this.getClass().toString(), tileStatus.getTilesWithColours().get(TileColour.GREEN).size() +  " green tiles now exist");
		Log.i(this.getClass().toString(), tileStatus.getTilesWithColours().get(TileColour.RED).size() +  " red tiles now exist");
		
		// A new tile position will be calculated based on the position of the existing tile and the direction its facing
		TilePosition adjacentTilePosition = new TilePosition(tile);
		Log.d(this.getClass().toString(), "Adjacent tile position is " + adjacentTilePosition);
		if(isInvalidTilePosition(adjacentTilePosition))
		{
			Log.i(this.getClass().toString(), "No such adjacent tile exists");
			return;
		}
		Tile adjacentTile = tileStatus.getTiles()[adjacentTilePosition.getPositionAcrossGrid()][adjacentTilePosition.getPositionDownGrid()];
				
		Log.i(this.getClass().toString(), "Adjacent tile is " + adjacentTile);
		
		// If the adjacent file is the same colour then no need to update it
		// Otherwise update the adjacent tile
		if(adjacentTile.getColour().equals(colourToUpdate))
		{
			Log.i(this.getClass().toString(), "Adjacent tile is same colour so no more to update");
			return;
		}
		else
		{
			Log.i(this.getClass().toString(), "Adjacent tile is different colour so will update it");
			updateColours(adjacentTile, tileStatus, colourToUpdate, otherColour);
		}
		
	}
	
	public Tile calculateOptimumAITile(TileStatus tileStatus)
	{
		TileStatus copyOfTileStatus = createDeepCopyOfTileStatus(tileStatus);
		
		int highestNumberOfAITilesAttained = 0;
		int highestNumberOfUserTilesAttained = 0;
		
		Tile bestAITile = null;
		
		for(TilePosition aiPosition : tileStatus.getTilesWithColours().get(TileColour.RED))
		{
			Tile aiTile = copyOfTileStatus.getTiles()[aiPosition.getPositionAcrossGrid()][aiPosition.getPositionDownGrid()];
			
			if(bestAITile == null)
			{
				bestAITile = aiTile;
			}
			
			updateColours(aiTile, copyOfTileStatus, TileColour.RED, TileColour.GREEN);
			
			int numberOfAITilesAttained = copyOfTileStatus.getTilesWithColours().get(TileColour.RED).size();
			
			int numberOfUserTilesAttained = copyOfTileStatus.getTilesWithColours().get(TileColour.GREEN).size();
			
			if(numberOfAITilesAttained > highestNumberOfAITilesAttained)
			{
				bestAITile = aiTile;
			}
			else if(numberOfAITilesAttained == highestNumberOfAITilesAttained)
			{
				if(numberOfUserTilesAttained > highestNumberOfUserTilesAttained)
				{
					bestAITile = aiTile;
				}
			}
		}
		
		return bestAITile;		
		
	}
	
	private TileStatus createDeepCopyOfTileStatus(TileStatus tileStatus)
	{
		Tile[][] copyOfAllTiles = createDeepCopyOfAllTiles(tileStatus.getTiles());
		Map<TileColour, Set<TilePosition>> copyOfTilesWithColours = createDeepCopyOfTilesWithColours(tileStatus.getTilesWithColours());
		TileStatus copyOfTileStatus = new TileStatus();
		copyOfTileStatus.setTiles(copyOfAllTiles);
		copyOfTileStatus.setTilesWithColours(copyOfTilesWithColours);
		return copyOfTileStatus;
	}
	
	private Map<TileColour, Set<TilePosition>> createDeepCopyOfTilesWithColours(Map<TileColour, Set<TilePosition>> tilesWithColours) {

		Map<TileColour, Set<TilePosition>> deepCopy = new HashMap<TileColour, Set<TilePosition>>(2);
		
		for(TileColour tileColour : tilesWithColours.keySet())
		{
			Set<TilePosition> tilePositions = new HashSet<TilePosition>(tilesWithColours.get(tileColour).size());
											
			for(TilePosition tilePosition : tilesWithColours.get(tileColour))
			{
				tilePositions.add(new TilePosition(tilePosition.getPositionAcrossGrid(), tilePosition.getPositionDownGrid()));
			}
			
			deepCopy.put(tileColour, tilePositions);
		}
		
		return deepCopy;
		
	}

	private Tile[][] createDeepCopyOfAllTiles(Tile allTiles[][]) {
		
		Tile[][] deepCopy = new Tile[Constants.GRID_WIDTH][Constants.GRID_HEIGHT];
		
		for(int i = 0; i < Constants.GRID_WIDTH; i++)
		{
			for(int j = 0; j < Constants.GRID_HEIGHT; j++)
			{
				Tile beingCopied = allTiles[i][j];
				Tile newTile = new Tile(beingCopied.getDirection(), beingCopied.getColour(), beingCopied.getTilePosition());
				deepCopy[i][j] = newTile;				
			}
		}
		
		return deepCopy;
	}
	
	private boolean isInvalidTilePosition(TilePosition tilePosition)
	{
		return(tilePosition.getPositionAcrossGrid() < 0) || (tilePosition.getPositionDownGrid() < 0)
		|| (tilePosition.getPositionAcrossGrid() >= Constants.GRID_WIDTH) || (tilePosition.getPositionDownGrid() >= Constants.GRID_HEIGHT);
	}
	
	private void printAllTiles(TileStatus tileStatus)
	{
		Log.d(this.getClass().toString(), "Printing all tiles");
		
		Tile[][] allTiles = tileStatus.getTiles();
		for(int i = 0; i < allTiles.length; i++)
		{
			for(int j = 0; j < allTiles[0].length; j++)
			{
				Log.d(this.getClass().toString(), allTiles[i][j].toString());
			}
		}
	}
}
