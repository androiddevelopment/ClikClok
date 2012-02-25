package com.clikclok;

import roboguice.activity.RoboActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.GridView;

import com.clikclok.service.GameLogicService;
import com.clikclok.service.GridOperationQueue;
import com.clikclok.util.Constants;
import com.clikclok.view.TileAdapter;
import com.google.inject.Inject;

public class ClikClokActivity extends RoboActivity{
	private TileAdapter tileAdapter;
	private GameLogicService gameLogicService;	
	private GridOperationQueue gridOperationQueue;
	private GridView gridView;
	private Handler handler;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	Log.v(this.getClass().toString(), "Entering onCreate");
    	super.onCreate(savedInstanceState);
        Log.v(this.getClass().toString(), "Never logs this with RoboGuice");
        setContentView(R.layout.main);
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setNumColumns(Constants.GRID_WIDTH);
        gridView.setAdapter(tileAdapter);
        
        Log.v(this.getClass().toString(), "GridView initialized"); 
        
        gridOperationQueue.start();
        
        handler = new Handler();
        
        gameLogicService.setClikClokActivity(this);
        
        Log.v(this.getClass().toString(), "Completed onCreate");
    }

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id)
		{
			case Constants.LEVEL_COMPLETE_DIALOG:				
				builder.setMessage(getString(R.string.level_complete, gameLogicService.getCurrentLevel().getLevelNum()))
				       .setCancelable(false)
				       .setNeutralButton(R.string.proceed_button, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {
							updateGrid();
						}				    	   
				       });
				dialog = builder.create();			
			break;
			case Constants.GAME_COMPLETE_DIALOG:
				builder.setMessage(getString(R.string.game_complete, gameLogicService.getWinner().getWinnerText()))
				       .setCancelable(false)
				       .setNeutralButton(R.string.proceed_button, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {

						
						}				    	   
				       });
				dialog = builder.create();			
			break;
		}
		
		return dialog;
	}   
    
	public void updateGrid() {
		// This is added to the Handler's queue to ensure that refreshes are performed
		// in the order that they are invoked.
		handler.post(new Runnable()
		{
			@Override
			public void run() {
				tileAdapter.notifyDataSetChanged();				
			}			
		});
	}
	
}