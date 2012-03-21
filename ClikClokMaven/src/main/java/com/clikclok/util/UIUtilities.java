package com.clikclok.util;

import android.graphics.Typeface;

import com.clikclok.ClikClokActivity;
import com.google.inject.Singleton;
/**
 * Helper class to create customized fonts and determine the window size
 * @author David
 */
@Singleton
public class UIUtilities {
	private static int windowWidth;
	public static Typeface setFont(String fontName) {
		Typeface tf = Typeface.createFromAsset(ClikClokActivity.getAppContext().getAssets(), "fonts/" + fontName);		
		return tf;
	}
	
	/**
	 * Creates a customized font for the specified ID
	 * @param fontID
	 * @return
	 */
	public static Typeface setFont(int fontID) {
		Typeface tf = Typeface.createFromAsset(ClikClokActivity.getAppContext().getAssets(), "fonts/" + ClikClokActivity.getAppContext().getString(fontID));		
		return tf;
	}
	
	/**
	 * @return tile width. The multiplier is a percentage of the overall window size
	 */
	public static int getTileWidth() {
		return (int)(windowWidth * Constants.TILE_WIDTH_MULTIPLIER);
	}
	
	/**
	 * @param width sets the window's width
	 */
	public static void setWindowWidth(int width) {
		windowWidth = width;
	}
}
