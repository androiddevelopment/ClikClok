package com.clikclok;

public enum Level {
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5);
	
	private int levelNum;
	
	private Level(int levelNum)
	{
		this.levelNum = levelNum;
	}

	public int getLevelNum() {
		return levelNum;
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
}
