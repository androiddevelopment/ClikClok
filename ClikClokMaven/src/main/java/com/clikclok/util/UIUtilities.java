package com.clikclok.util;

import android.graphics.Typeface;

import com.clikclok.ClikClokActivity;
import com.google.inject.Singleton;
@Singleton
public class UIUtilities {
	
	public static Typeface setFont(String fontName) {
		Typeface tf = Typeface.createFromAsset(ClikClokActivity.getContext().getAssets(), "fonts/" + fontName);		
		return tf;
	}
	
	public static Typeface setFont(int fontID) {
		Typeface tf = Typeface.createFromAsset(ClikClokActivity.getContext().getAssets(), "fonts/" + ClikClokActivity.getContext().getString(fontID));		
		return tf;
	}
}
