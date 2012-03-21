package com.clikclok.util;

import android.os.CountDownTimer;

import com.clikclok.service.GameLogicService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Countdown timer that will be kicked off in level 5 to provide the user with a limited timeframe to
 * select their tile
 * @author David
 */
@Singleton
public class AITimer {
	private AICountDownTimer timer;
	@Inject 
	private GameLogicService gameLogicService;
	
	public AITimer()
	{
		timer = new AICountDownTimer(Constants.NUMBER_OF_SECONDS * 1000, 1 * 1000);
	}
	
	/**
	 * Stops any existing timer and starts a new one
	 */
	public void startTimer()
	{
		stopTimer();
		timer.start();
	}
	
	/**
	 * Stops the running timer
	 */
	public void stopTimer()
	{
		timer.cancel();
	}
	
	private class AICountDownTimer extends CountDownTimer
	{
		public AICountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			// Notifies the logic service that the timer has expired
			gameLogicService.updateTimerText(0);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			int secondsLeft = (int)millisUntilFinished / 1000;
			gameLogicService.updateTimerText(secondsLeft);
			
		}
		
	}	
}

