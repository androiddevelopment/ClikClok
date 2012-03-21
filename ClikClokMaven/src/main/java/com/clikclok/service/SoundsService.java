package com.clikclok.service;

import com.clikclok.domain.OperationType;

public interface SoundsService {

	/**
	 * Plays the sound for the relevant operation type. If a large number of enemy gains are made then a different sound will be plated
	 * @param operationType
	 * @param enemyGainsMade
	 */
	public abstract void playMoveSound(OperationType operationType,
			boolean enemyGainsMade);

	/**
	 * Play the sound for when the user wins
	 */
	public abstract void playUserWinsSound();

	/**
	 * Play the sound for when the AI wins
	 */
	public abstract void playAIWinsSound();

	/**
	 * Enable or disable the sound
	 * @param enableSounds
	 */
	public abstract void adjustVolume(boolean enableSounds);

}