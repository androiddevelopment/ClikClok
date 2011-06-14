package com.clikclok.test;

import junit.framework.TestCase;

import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.TileDirection;
import com.clikclok.domain.TilePosition;
import com.clikclok.domain.TileStatus;
import com.clikclok.service.TileUpdater;

public class TileUpdaterTest extends TestCase {
	private TileUpdater tileUpdater;
	
	public void testUpdateColours()
	{
		TileStatus tileStatus = new TileStatus(initializeTestTileGrid());
		tileStatus.initializeTilesWithColoursSet(new TilePosition(0, 0), new TilePosition(3, 3));
		Tile tileUpdated = tileStatus.getTiles()[0][0];
		
		tileUpdater.updateColours(tileUpdated, tileStatus, TileColour.GREEN, TileColour.RED);		
	}
	
	private Tile[][] initializeTestTileGrid()
	{
		Tile[][] tiles = new Tile[4][4];
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				tiles[i][j] = new Tile();
				tiles[i][j].setTilePosition(new TilePosition(i,j));
				tiles[i][j].setColour(TileColour.GREY);
			}
		}
		
		tiles[0][0].setDirection(TileDirection.EAST);
		tiles[0][1].setDirection(TileDirection.SOUTH);
		tiles[0][2].setDirection(TileDirection.EAST);
		tiles[0][3].setDirection(TileDirection.SOUTH);
		
		tiles[1][0].setDirection(TileDirection.EAST);
		tiles[1][1].setDirection(TileDirection.EAST);
		tiles[1][2].setDirection(TileDirection.WEST);
		tiles[1][3].setDirection(TileDirection.EAST);
		
		tiles[2][0].setDirection(TileDirection.SOUTH);
		tiles[2][1].setDirection(TileDirection.WEST);
		tiles[2][2].setDirection(TileDirection.WEST);
		tiles[2][3].setDirection(TileDirection.SOUTH);
		
		tiles[3][0].setDirection(TileDirection.SOUTH);
		tiles[3][1].setDirection(TileDirection.WEST);
		tiles[3][2].setDirection(TileDirection.EAST);
		tiles[3][3].setDirection(TileDirection.SOUTH);
		
		return tiles;
	}
	
}
