package com.clikclok.event;

import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.TileDirection;
import com.clikclok.domain.TileStatus;
import com.clikclok.service.TileUpdater;
import com.clikclok.util.Constants;
import com.clikclok.view.TileAdapter;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class TileClickListener implements OnClickListener {
	
	private Tile clickedTile;
	private TileAdapter tileAdapter;
	private TileUpdater tileUpdater;
	private TileStatus tileStatus;
	private int positionInGrid;
		
	public TileClickListener(Tile clickedTile, TileAdapter tileAdapter, TileStatus tileStatus)
	{
		this(clickedTile, tileAdapter, tileStatus, -1);
	}
	
	public TileClickListener(Tile clickedTile, TileAdapter tileAdapter, TileStatus tileStatus, int positionInGrid)
	{
		this.clickedTile = clickedTile;
		this.tileAdapter = tileAdapter;
		this.tileStatus = tileStatus;
		tileUpdater = new TileUpdater();
		this.positionInGrid = positionInGrid;
	}
	
	@Override
	public void onClick(View v) {
		
		Log.i(this.getClass().toString(), "About to update grid for user's click on tile " + clickedTile);
		
		Log.d(this.getClass().toString(), "position in grid is " + positionInGrid);
		
		if(!clickedTile.getColour().equals(TileColour.GREEN))
		{
			Log.i(this.getClass().toString(), "Tile is not a user tile so no updates to be performed");
			return;
		}
		
		updateDirection(clickedTile);		
		
		tileUpdater.updateColours(clickedTile, tileStatus, TileColour.GREEN, TileColour.RED);
		// Refresh all tiles in the grid
		tileAdapter.notifyDataSetChanged();
		// Check if all tiles are occupied
		if(tileStatus.getTilesWithColours().get(TileColour.GREEN).size() == Constants.GRID_SIZE)
		{
			// User wins or next level
		}
		else
		{
//			Tile aiTile = tileUpdater.calculateOptimumAITile(tileStatus);
//			
//			Log.i(this.getClass().toString(), "Optimum tile calculated for AI is " + aiTile);
//			
//			tileUpdater.updateColours(aiTile, tileStatus, TileColour.RED, TileColour.GREEN);
//			
//			tileAdapter.notifyDataSetChanged();
//			
//			if(tileStatus.getTilesWithColours().get(TileColour.RED).size() == Constants.GRID_SIZE)
//			{
//				// AI wins, game over.
//			}
		}
	}
	
	private void updateDirection(Tile tile)
	{
		int degrees = (int) tile.getDirection().getDegrees();
		
		// Add 90 to the number of degrees. If it's 270 then we are effectively adding 90 and resetting to 0 as it's 360
		degrees = (degrees == 270) ? 0 : (degrees += 90);
		
		// Set it's new direction
		tile.setDirection(TileDirection.getTileDirection(degrees));
	}
}
