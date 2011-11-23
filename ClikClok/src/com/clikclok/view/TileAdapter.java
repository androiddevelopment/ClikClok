package com.clikclok.view;

import roboguice.inject.ContextSingleton;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
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
import com.google.inject.Inject;

@ContextSingleton
public class TileAdapter extends BaseAdapter{
	@Inject
	private Context context;
	@Inject
	private GameState tileStatus;
	@Inject
	private TileOperationService tileOperationService;
				
	public TileAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		// This will return the width and height of the grid
		return tileStatus.getTileGridSize();
	}

	@Override
	public Object getItem(int arg0) {
		Log.v(this.getClass().toString(), "in getItem for int " + arg0);
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		Log.v(this.getClass().toString(), "in getItemId for int " + arg0);
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Log.v(this.getClass().toString(), "Entering getView for position " + position);
		
		ImageView imageView;
		
		TilePosition tilePosition = new TilePosition(position);
		Tile thisTile = tileStatus.getTileInformation(tilePosition);
 		Log.v(this.getClass().toString(), "Loading image for tile " + thisTile);
		
 		Log.v(this.getClass().toString(), "Context is " + context);
 		
        imageView = new ImageView(context);            
           
        // Important that we create a new listener each time as we need to pass in a new position
        imageView.setOnClickListener(new TileClickListener(thisTile, tileOperationService));
        
        // These parameters determine the size of each image
        imageView.setLayoutParams(new GridView.LayoutParams(35, 35));
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

	
			
}
