package com.clikclok.domain;

public enum Winner {
	USER("You are"),
	AI("AI is");
	
	private String winnerText;
	
	private Winner(String text)
	{
		winnerText = text;
	}
	
	public String getWinnerText(){
		return winnerText;
	}
}
