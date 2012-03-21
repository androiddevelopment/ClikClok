package com.clikclok.service;

import com.clikclok.service.impl.TileOperationServiceImpl.AICalculationTask;

public interface AICalculationQueue {

	/**
	 * Adds the specified task to the queue
	 * @param task
	 */
	public abstract void addAICalculationTaskToQueue(AICalculationTask task);

	/**
	 * Starts the thread if it's not already started
	 */
	public abstract void startQueueThread();

}