package com.clikclok.view;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.domain.TileDirection;
import com.clikclok.domain.TilePosition;
import com.clikclok.event.TileClickListener;
import com.clikclok.service.GameLogicService;
import com.clikclok.util.UIUtilities;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Adapter used to populate each individual cell in the grid with the relevant tile information
 * @author David
 */
@Singleton
public class TileAdapter extends BaseAdapter{ 
	@Inject
	private static Application application;
	@Inject
	private static Injector injector;
	@Inject
	private GameLogicService gameLogicService;
	private static Map<TileColour, Map<TileDirection, Bitmap>> bitmaps;
	
	private TileAdapter() {
		super();
	}

	@Override
	public int getCount() {
		// This will return the width and height of the grid
		return gameLogicService.getGameState().getTileGridSize();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		
		TilePosition tilePosition = new TilePosition(position);
		Tile thisTile = gameLogicService.getGameState().getTileInformation(tilePosition);
 		imageView = new ImageView(application);            
        
 		TileClickListener tileClickListener = new TileClickListener(thisTile);
 		injector.injectMembers(tileClickListener);
 		
        // Important that we create a new listener each time as we need to pass in a new position
        imageView.setOnClickListener(tileClickListener);
        
        // These parameters determine the size of each image
        imageView.setLayoutParams(new GridView.LayoutParams(UIUtilities.getTileWidth(), UIUtilities.getTileWidth()));
        imageView.setPadding(0, 0, 0, 0);

        imageView.setImageBitmap(loadBitmapForImage(thisTile));
        
        return imageView;

	}
	
	/**
	 * Loads the image for the specified {@link Tile}
	 * @param tile
	 * @return
	 */
	private Bitmap loadBitmapForImage(Tile tile)
	{
		Map<TileDirection, Bitmap> bitmapsForDirections = bitmaps.get(tile.getColour());
		return bitmapsForDirections.get(tile.getDirection());
	}
	
	/**
	 * This ensures that proper initialization is performed before the TileAdapter is returned
	 * @return
	 */
	public static TileAdapter initializeAdapter()
	{
		TileAdapter tileAdapter = new TileAdapter();
		injector.injectMembers(tileAdapter);
		bitmaps = new HashMap<TileColour, Map<TileDirection,Bitmap>>();
		for(TileColour tileColour : TileColour.values()){
			Bitmap bitmap = BitmapFactory.decodeResource(application.getResources(), tileColour.getIconColour());
			Map<TileDirection, Bitmap> bitmapsForDirections = new HashMap<TileDirection, Bitmap>();
			for(TileDirection tileDirection : TileDirection.values())
			{
				Matrix matrix = new Matrix();
				matrix.setRotate(tileDirection.getDegrees());
				Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				rotatedBitmap = Bitmap.createScaledBitmap(rotatedBitmap, 60, 60, true);
				bitmapsForDirections.put(tileDirection, rotatedBitmap);
			}
			bitmaps.put(tileColour, bitmapsForDirections);
		}
		return tileAdapter;
	}
}
