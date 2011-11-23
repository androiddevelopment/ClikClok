package com.clikclok.service;

import android.os.Handler;
import android.os.Looper;

import com.clikclok.service.domain.GridUpdateTask;
import com.google.inject.Singleton;

@Singleton
public class GridOperationQueue extends Thread {
	private Handler handler;

	@Override
	public void run() {
		Looper.prepare();
		
		handler = new Handler();
		
		Looper.loop();	
		
	}
		
	public void addTaskToQueue(final GridUpdateTask task)
	{
		handler.post(new Runnable()
		{
			@Override
			public void run() {
				task.run();
			}
			
		});
	}
	
	public void clearQueue(){
		handler.removeCallbacksAndMessages(null);
	}
}
