package com.clikclok.service;

import android.util.Log;

import com.clikclok.ClikClokActivity;
import com.clikclok.domain.GameState;
import com.clikclok.domain.Level;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.Winner;
import com.clikclok.util.Constants;
import com.google.inject.Inject;

public class GameLogicService {
	private Level currentLevel;
	private Winner winner;
	@Inject
	private ClikClokActivity clikClokActivity;
	@Inject
	private GameState gameState ;
	
	public GameLogicService()
	{
		currentLevel = Level.ONE;
		Level.setGridSize(gameState.getTileGridSize());
	}
	public Level getCurrentLevel()
	{
		return currentLevel;
	}
	
	public void setWinner(Winner winner)
	{
		this.winner = winner;
		clikClokActivity.showDialog(Constants.GAME_COMPLETE_DIALOG);
	}
	
	public Winner getWinner()
	{
		return winner;
	}
	public void nextLevel()
	{
		currentLevel = Level.getNextLevel(currentLevel);
		clikClokActivity.showDialog(Constants.LEVEL_COMPLETE_DIALOG);
		gameState = new GameState();
	}

	public GameState getGameState() {
		return gameState;
	}
	public void updateGrid() 
	{
		clikClokActivity.updateGrid();
		
		// Need to check for both, as when the first red turns there will be no reds at that point
		if(gameState.getNumberOfTilesForColour(TileColour.RED) + gameState.getNumberOfTilesForColour(TileColour.RED_TURNING) == 0)
		{
			Log.d(this.getClass().toString(), "No more AI tiles. Checking to see if there are more levels");
			// If the next level is null then we there are no more levels and the game is over
			if(Level.getNextLevel(currentLevel) == null)
			{
				setWinner(Winner.USER);
			}
			else
			{
				nextLevel();
			}
		}
		else if(gameState.getNumberOfTilesForColour(TileColour.GREEN) == 0)
		{
			setWinner(Winner.AI);
		}
	}
	public void setClikClokActivity(ClikClokActivity clikClokActivity) {
		this.clikClokActivity = clikClokActivity;
	}
}
