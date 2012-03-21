package com.clikclok.service.impl;

import android.os.Handler;
import android.os.Looper;

import com.clikclok.service.AICalculationQueue;
import com.clikclok.service.impl.TileOperationServiceImpl.AICalculationTask;
import com.google.inject.Singleton;

/**
 * A separate thread to perform the calculation to determine the best tile for the AI to select.
 * This thread will run in parallel to the main application thread and the {@link UIOperationQueueImpl} thread
 * @author David
 */
@Singleton
public class AICalculationQueueImpl extends Thread implements AICalculationQueue {
	private Handler handler;
	
	@Override
	public void run() {
		Looper.prepare();
		handler = new Handler();
		Looper.loop();
	}
	
	@Override
	public void addAICalculationTaskToQueue(AICalculationTask task) {
		handler.post(task);
	}
	
	@Override
	public void startQueueThread() {
		if(!isAlive())
		{
			start();
		}
	}
}
