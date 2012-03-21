package com.clikclok.event;

import android.view.View;
import android.view.View.OnClickListener;

import com.clikclok.service.GameLogicService;
import com.clikclok.service.UIOperationQueue;
import com.clikclok.service.domain.Task;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This listener is used to handle when a user clicks the sound icon to enable/disable volume
 * @author David
 */
@Singleton
public class EnableSoundsListener implements OnClickListener {
	@Inject
	private GameLogicService gameLogicService;
	@Inject
	private UIOperationQueue uiOperationQueue;

	@Override
	public void onClick(View view) {		
		uiOperationQueue.addUITaskToQueue(new Task() {			
			@Override
			public void run() {
				gameLogicService.toggleSounds();				
			}
		});		
	}
	
	
}
