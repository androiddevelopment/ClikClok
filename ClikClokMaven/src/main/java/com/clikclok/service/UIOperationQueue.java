package com.clikclok.service;

import com.clikclok.ClikClokActivity;
import com.clikclok.domain.OperationType;
import com.clikclok.service.domain.GridUpdateTask;
import com.clikclok.service.domain.Task;
import com.clikclok.service.impl.TileOperationServiceImpl.AICalculationTask;

/**
 * All UI operations that we wish to sequence should ideally be invoked on a separate thread to the main thread. This service allows UI tasks to be added from the main thread, 
 * and are then invoked in order on this separate thread. These tasks will typically invoke the {@link ClikClokActivity} to perform a UI update on the main thread
 * @author David
 */
public interface UIOperationQueue {

	
	/**
	 * Add the task to perform calculation of tiles updated, as well as the grid refresh, to the queue
	 * @param task
	 */
	void addGridUpdateTaskToQueue(GridUpdateTask task);

	/**
	 * Add straightforward UI update tasks to the queue
	 * @param task
	 */
	void addUITaskToQueue(Task task);

	/**
	 * Clear the queue of all tasks
	 */
	void clearQueue();

	/**
	 * Start the next {@link GridUpdateTask} in the queue
	 * @param currentOperationType
	 */
	void startNextGridUpdateTask(OperationType currentOperationType);

	/**
	 * This is invoked by the {@link AICalculationTask} when the AI calculation is complete. When the AI
	 * calculation is complete we can then update the grid with the selected optimum tile
	 */
	void notifyAICalculationComplete();

	/**
	 * Indicates whether there are still tasks in the queue to be processed
	 * @return
	 */
	boolean hasUnprocessedTasks();
	
	/**
	 * Initializes the thread if it is dead
	 */
	void startQueueThread();

	GridUpdateTask getTaskForOperationType(OperationType operationType);

}
