package com.clikclok.service.domain;

/**
 * Class that provides a better name than simply Runnable
 * @author David
 */
public abstract class Task implements Runnable {

	@Override
	public abstract void run();

}
