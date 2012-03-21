package com.clikclok.domain;

import com.clikclok.R;

/**
 * Enum to store all colours and their relevant icons
 * @author David
 *
 */
public enum TileColour {
	GREEN (R.drawable.green_icon),
	RED (R.drawable.red_icon),
	GREY (R.drawable.grey_icon),
	GREEN_TURNING(R.drawable.green_icon_turning),
	RED_TURNING(R.drawable.red_icon_turning);	
	
	private int iconColour;
	
	private TileColour(int colour)
	{
		this.iconColour = colour;
	}

	public int getIconColour() {
		return iconColour;
	}
	
	
}
