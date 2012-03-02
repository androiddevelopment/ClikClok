package com.clikclok.domain;

import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.TileDirection;
import com.clikclok.domain.TilePosition;

public class TestUtilities {
	
	public static Tile[][] initializeSmallTestTileGrid()
	{
		Tile[][] tiles = new Tile[4][4];
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				tiles[i][j] = new Tile(null, TileColour.GREY, new TilePosition(i,j));
			}
		}
		
		tiles[0][0].setDirection(TileDirection.EAST);
		tiles[0][0].setColour(TileColour.GREEN);
		tiles[0][1].setDirection(TileDirection.SOUTH);
		tiles[0][2].setDirection(TileDirection.EAST);
		tiles[0][3].setDirection(TileDirection.NORTH);
		
		tiles[1][0].setDirection(TileDirection.EAST);
		tiles[1][1].setDirection(TileDirection.EAST);
		tiles[1][2].setDirection(TileDirection.WEST);
		tiles[1][3].setDirection(TileDirection.EAST);
		
		tiles[2][0].setDirection(TileDirection.SOUTH);
		tiles[2][1].setDirection(TileDirection.WEST);
		tiles[2][2].setDirection(TileDirection.EAST);
		tiles[2][3].setDirection(TileDirection.WEST);
		
		tiles[3][0].setDirection(TileDirection.EAST);
		tiles[3][1].setDirection(TileDirection.WEST);
		tiles[3][2].setDirection(TileDirection.EAST);
		tiles[3][3].setDirection(TileDirection.SOUTH);
		tiles[3][3].setColour(TileColour.RED);
		
		return tiles;
	}
	
	public static Tile[][] initializeSmallTestTileGridPredominantlyRed()
	{
		Tile[][] tiles = new Tile[3][3];
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				tiles[i][j] = new Tile(null, TileColour.RED, new TilePosition(i,j));
			}
		}
		
		tiles[0][0].setDirection(TileDirection.NORTH);
		tiles[0][0].setColour(TileColour.GREEN);
		tiles[0][1].setDirection(TileDirection.WEST);
		tiles[0][2].setDirection(TileDirection.EAST);
		
		
		tiles[1][0].setDirection(TileDirection.NORTH);
		tiles[1][1].setDirection(TileDirection.WEST);
		tiles[1][2].setDirection(TileDirection.WEST);
				
		tiles[2][0].setDirection(TileDirection.SOUTH);
		tiles[2][0].setColour(TileColour.GREY);
		tiles[2][1].setDirection(TileDirection.SOUTH);
		tiles[2][1].setColour(TileColour.GREY);
		tiles[2][2].setDirection(TileDirection.WEST);
		
		return tiles;
	}
	
	public static Tile[][] initializeSmallTestTileGridWithTopLeftOccupied()
	{
		Tile[][] tiles = new Tile[4][4];
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				tiles[i][j] = new Tile(null, TileColour.RED, new TilePosition(i,j));
			}
		}
		
		tiles[0][0].setDirection(TileDirection.EAST);
		tiles[0][1].setDirection(TileDirection.SOUTH);
		tiles[0][2].setDirection(TileDirection.EAST);
		tiles[0][3].setDirection(TileDirection.NORTH);
		
		tiles[1][0].setDirection(TileDirection.EAST);
		tiles[1][1].setDirection(TileDirection.EAST);
		tiles[1][2].setDirection(TileDirection.WEST);
		tiles[1][3].setDirection(TileDirection.EAST);
		
		tiles[2][0].setDirection(TileDirection.SOUTH);
		tiles[2][1].setDirection(TileDirection.WEST);
		tiles[2][2].setDirection(TileDirection.EAST);
		tiles[2][3].setDirection(TileDirection.WEST);
		
		tiles[3][0].setDirection(TileDirection.EAST);
		tiles[3][1].setDirection(TileDirection.WEST);
		tiles[3][2].setDirection(TileDirection.WEST);
		tiles[3][3].setDirection(TileDirection.SOUTH);
		tiles[3][3].setColour(TileColour.GREEN);
		
		return tiles;
	}
}
