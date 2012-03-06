package com.clikclok.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.clikclok.domain.GameState;
import com.clikclok.domain.Tile;
import com.clikclok.domain.TilePosition;
import com.clikclok.event.TileClickListener;
import com.clikclok.service.TileOperationService;
import com.clikclok.util.UIUtilities;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TileAdapter extends BaseAdapter{ 
	@Inject
	private Context context;
	// Not injecting this as this object may be re-created multiple
	// times during an activity's lifecycle
	private GameState gameState;
	@Inject
	private TileOperationService tileOperationService;
				
	@Override
	public int getCount() {
		// This will return the width and height of the grid
		return gameState.getTileGridSize();
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
		Tile thisTile = gameState.getTileInformation(tilePosition);
 		imageView = new ImageView(context);            
           
        // Important that we create a new listener each time as we need to pass in a new position
        imageView.setOnClickListener(new TileClickListener(thisTile, tileOperationService));
        
        // These parameters determine the size of each image
        imageView.setLayoutParams(new GridView.LayoutParams(UIUtilities.getTileWidth(), UIUtilities.getTileWidth()));
        imageView.setPadding(0, 0, 0, 0);

        imageView.setImageBitmap(loadBitmapForImage(thisTile));
        
        return imageView;

	}
	
	private Bitmap loadBitmapForImage(Tile tile)
	{
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), tile.getColour().getIconColour());
		
		Matrix matrix = new Matrix();
		matrix.setRotate(tile.getDirection().getDegrees());
		
		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		rotatedBitmap = Bitmap.createScaledBitmap(rotatedBitmap, 60, 60, true);
		
		return rotatedBitmap;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}			
}
