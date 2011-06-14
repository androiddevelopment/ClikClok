package com.clikclok.view;

import com.clikclok.ClikClok;
import com.clikclok.domain.Level;
import com.clikclok.domain.Tile;
import com.clikclok.domain.TilePosition;
import com.clikclok.event.TileClickListener;
import com.clikclok.util.Constants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class TileAdapter extends BaseAdapter{
	private ClikClok clikClokGame;
	private Tile[][] tiles;
				
	public TileAdapter(Context context, Level initialLevel)
	{
		this.clikClokGame = (ClikClok) context;
		tiles = clikClokGame.getTileStatus().getTiles();
	}
	
	@Override
	public int getCount() {
		// This will return the width and height of the grid
		return Constants.GRID_WIDTH * Constants.GRID_HEIGHT;
	}

	@Override
	public Object getItem(int arg0) {
		Log.d(this.getClass().toString(), "in getItem for int " + arg0);
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		Log.d(this.getClass().toString(), "in getItemId for int " + arg0);
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Log.d(this.getClass().toString(), "Entering getView for position " + position);
		
		ImageView imageView;
		
		TilePosition tilePosition = new TilePosition(position);
		Tile thisTile = tiles[tilePosition.getPositionAcrossGrid()][tilePosition.getPositionDownGrid()]; 
 		Log.d(this.getClass().toString(), "Loading image for tile " + thisTile);
		
        imageView = new ImageView(clikClokGame);            
           
        // Important that we create a new listener each time as we need to pass in a new position
        imageView.setOnClickListener(new TileClickListener(thisTile, this, clikClokGame.getTileStatus(), position));
        // These settings will need to be modified
        //imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams., LayoutParams.WRAP_CONTENT));
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setImageDrawable(loadBitmapForImage(thisTile));
        return imageView;

	}
	
	private BitmapDrawable loadBitmapForImage(Tile tile)
	{
		Bitmap bitmap = BitmapFactory.decodeResource(clikClokGame.getResources(), tile.getColour().getIconColour());
		
		Matrix matrix = new Matrix();
		matrix.setRotate(tile.getDirection().getDegrees());
		
		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		
		return new BitmapDrawable(rotatedBitmap);
	}
	
	
	
			
}
