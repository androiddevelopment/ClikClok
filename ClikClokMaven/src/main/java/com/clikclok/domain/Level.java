package com.clikclok.domain;

/**
 * Enum to store the different levels in the game. The number to search is the number of tiles nearest to the top left for the AI to choose from for that level
 * @author David
 *
 */
public enum Level {
	ONE(1, 5),
	TWO(2, 10),
	THREE(3, 15),
	FOUR(4),
	FIVE(5);
	
	private int levelNum;
	private int numberToSearch;
	private static int gridSize;
	
	private Level(int levelNum)
	{
		this.levelNum = levelNum;
	}
	
	private Level(int levelNum, int numberToSearch)
	{
		this.levelNum = levelNum;
		this.numberToSearch = numberToSearch;
	}

	public int getLevelNum() {
		return levelNum;
	}
	
	public int getNumberToSearch() {
		numberToSearch = (numberToSearch == 0) ? gridSize : numberToSearch;
		return numberToSearch;
	}
	
	public static Level getNextLevel(Level currentLevel)
	{
		for(Level level : Level.values())
		{
			if(level.getLevelNum() == (currentLevel.getLevelNum() + 1))
			{
				return level;
			}
		}
		return null;
	}

	public static void setGridSize(int gridSize)
	{
		Level.gridSize = gridSize;
	}
}
