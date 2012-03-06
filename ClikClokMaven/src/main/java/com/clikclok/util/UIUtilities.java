package com.clikclok.util;

import android.graphics.Typeface;

import com.clikclok.ClikClokActivity;
import com.google.inject.Singleton;
@Singleton
public class UIUtilities {
	private static int windowWidth;
	public static Typeface setFont(String fontName) {
		Typeface tf = Typeface.createFromAsset(ClikClokActivity.getContext().getAssets(), "fonts/" + fontName);		
		return tf;
	}
	
	public static Typeface setFont(int fontID) {
		Typeface tf = Typeface.createFromAsset(ClikClokActivity.getContext().getAssets(), "fonts/" + ClikClokActivity.getContext().getString(fontID));		
		return tf;
	}
	
	public static int getTileWidth() {
		return (int)(windowWidth * Constants.TILE_WIDTH_MULTIPLIER);
	}
	
	public static void setWindowWidth(int width) {
		windowWidth = width;
	}
}
