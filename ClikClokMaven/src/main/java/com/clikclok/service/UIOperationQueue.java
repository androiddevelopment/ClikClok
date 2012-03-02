package com.clikclok.service;

import java.util.LinkedList;
import java.util.Queue;

import android.os.Handler;
import android.os.Looper;

import com.clikclok.service.domain.GridUpdateTask;
import com.google.inject.Singleton;

@Singleton
public class UIOperationQueue extends Thread implements GamePauseAndResumeService {
	private Handler handler;
	private Queue<GridUpdateTask> gridUpdateQueue;

	@Override
	public void run() {
		Looper.prepare();
		
		handler = new Handler();
		gridUpdateQueue = new LinkedList<GridUpdateTask>();
		
		Looper.loop();	
		
	}
	
	public void addGridUpdateTaskToQueue(final GridUpdateTask task)
	{
		gridUpdateQueue.add(task);
	}
	
	public void addTaskToQueue(final Runnable task)
	{
		handler.post(task);		
	}
	
	public void clearQueue(){
		handler.removeCallbacksAndMessages(null);
	}
	
	@Override
	public void resumeQueuedTasks() {
		addTaskToQueue(gridUpdateQueue.poll());
	}
}
