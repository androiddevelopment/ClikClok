package com.clikclok.domain;

import com.clikclok.R;

public enum TileColour {
	GREEN (R.drawable.green_icon),
	RED (R.drawable.red_icon),
	GREY (R.drawable.grey_icon);
	
	
	private int iconColour;
	
	private TileColour(int colour)
	{
		this.iconColour = colour;
	}

	public int getIconColour() {
		return iconColour;
	}
	
	
}
