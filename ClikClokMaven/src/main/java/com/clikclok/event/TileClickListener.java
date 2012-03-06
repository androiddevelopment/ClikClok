package com.clikclok.event;

import android.view.View;
import android.view.View.OnClickListener;

import com.clikclok.domain.Tile;
import com.clikclok.domain.TileColour;
import com.clikclok.service.TileOperationService;

public class TileClickListener implements OnClickListener {
	
	private Tile clickedTile;
	// Does not seem to be injected as TileClickListener is not itself injected
	private TileOperationService tileOperationService;
		
	public TileClickListener(Tile clickedTile, TileOperationService tileOperationService)
	{
		this.clickedTile = clickedTile;	
		this.tileOperationService = tileOperationService;
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
