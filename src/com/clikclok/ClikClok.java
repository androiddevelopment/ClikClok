package com.clikclok;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.GridView;

public class ClikClok extends Activity {
	private Level currentLevel;
	private TileAdapter tileAdapter;
	private TileStatus tileStatus;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        currentLevel = Level.ONE;
        
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setNumColumns(Constants.GRID_WIDTH);
        
        tileStatus = new TileStatus();
        
        tileAdapter = new TileAdapter(this, currentLevel);
        gridView.setAdapter(tileAdapter);
    }

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog = null;
		switch(id)
		{
			case Constants.LEVEL_COMPLETE_DIALOG:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getString(R.string.level_complete, currentLevel.getLevelNum()))
				       .setCancelable(false)
				       .setNeutralButton(R.string.proceed_button, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {
							setCurrentLevel(Level.getNextLevel(currentLevel));
							tileStatus = new TileStatus();
							tileAdapter.notifyDataSetChanged();							
						}				    	   
				       });
				dialog = builder.create();			
			break;
		}
		
		return dialog;
	}   
    
	public Level getCurrentLevel() {
		return currentLevel;
	}
	
	public TileStatus getTileStatus() {
		return tileStatus;
	}

	
	
	public void setCurrentLevel(Level currentLevel) {
		this.currentLevel = currentLevel;
	}
	
    
}