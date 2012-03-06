package com.clikclok.util;

import android.os.CountDownTimer;

import com.clikclok.service.GameLogicService;
import com.google.inject.Inject;

public class AITimer {
	private AICountDownTimer timer;
	@Inject 
	private GameLogicService gameLogicService;
	
	public AITimer()
	{
		timer = new AICountDownTimer(6 * 1000, 1 * 1000);
	}
	
	public void startTimer()
	{
		stopTimer();
		timer.start();
	}
	
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
			gameLogicService.setTimedOut();	
			gameLogicService.updateTimerText("" + 0);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			int secondsLeft = (int)millisUntilFinished / 1000;
			gameLogicService.updateTimerText("" + secondsLeft);
			
		}
		
	}	
}

