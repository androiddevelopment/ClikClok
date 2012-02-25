package com.clikclok.service;

import android.os.Handler;
import android.os.Looper;

import com.google.inject.Singleton;

@Singleton
public class UIOperationQueue extends Thread {
	private Handler handler;

	@Override
	public void run() {
		Looper.prepare();
		
		handler = new Handler();
		
		Looper.loop();	
		
	}
		
	public void addTaskToQueue(final Runnable task)
	{
		handler.post(task);		
	}
	
	public void clearQueue(){
		handler.removeCallbacksAndMessages(null);
	}
}
