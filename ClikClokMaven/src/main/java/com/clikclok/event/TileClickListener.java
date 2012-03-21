package com.clikclok.event;

import android.view.View;
import android.view.View.OnClickListener;

import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.service.TileOperationService;
import com.clikclok.service.impl.TileOperationServiceImpl;
import com.google.inject.Inject;

/**
 * Simple listener to handle each click on a tile. Delegates to the {@link TileOperationServiceImpl}
 * @author David
 */
public class TileClickListener implements OnClickListener {
	private Tile clickedTile;
	@Inject
	private TileOperationService tileOperationService;
		
	public TileClickListener(Tile clickedTile)
	{
		this.clickedTile = clickedTile;			
	}
	
	@Override
	public void onClick(View v) {
		
		// We should not perform any operation for non-user tiles
		if(!clickedTile.getColour().equals(TileColour.GREEN))
		{
			return;
		}
		
		tileOperationService.performUserOperation(clickedTile);
		
	}
	
}
