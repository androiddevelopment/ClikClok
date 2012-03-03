package com.clikclok.service;

import java.util.LinkedList;
import java.util.Queue;

import android.os.Handler;
import android.os.Looper;

import com.clikclok.domain.TaskStatus;
import com.clikclok.service.domain.GridUpdateTask;
import com.google.inject.Singleton;

@Singleton
public class UIOperationQueue extends Thread implements GamePauseAndResumeService {
	private Handler handler;
	private Queue<GridUpdateTask> gridUpdateQueue;
	private TaskStatus aiCalculationStatus;
	private TaskStatus usersGridViewUpdateStatus;
	
	@Override
	public void run() {
		Looper.prepare();
		
		handler = new Handler();
		gridUpdateQueue = new LinkedList<GridUpdateTask>();
		aiCalculationStatus = TaskStatus.NOT_STARTED;
		usersGridViewUpdateStatus = TaskStatus.NOT_STARTED;
						
		Looper.loop();	
	}
	
	public void addAIGridUpdateTaskToQueue(final GridUpdateTask task)
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
	public void startNextTask() {
		addTaskToQueue(gridUpdateQueue.poll());
	}

	@Override
	public void aiCalculationComplete() {
		aiCalculationStatus = TaskStatus.COMPLETE;	
		checkIfReadyToPerformAIGridViewUpdate();
	}

	@Override
	public void usersGridViewUpdateComplete() {
		usersGridViewUpdateStatus = TaskStatus.COMPLETE;	
		checkIfReadyToPerformAIGridViewUpdate();
	}
	
	private void checkIfReadyToPerformAIGridViewUpdate() {
		if(aiCalculationStatus.equals(TaskStatus.COMPLETE))
		{
			if(usersGridViewUpdateStatus.equals(TaskStatus.COMPLETE)) 
			{
				startNextTask();
				aiCalculationStatus = TaskStatus.NOT_STARTED;
				usersGridViewUpdateStatus = TaskStatus.NOT_STARTED;
			}
		}
	}
}
