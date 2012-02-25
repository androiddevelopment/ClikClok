package com.clikclok.util;

import com.clikclok.service.GameLogicService;
import com.google.inject.Inject;

import android.os.CountDownTimer;
import android.util.Log;

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
		Log.d(this.getClass().toString(), "Starting AI timer");
		stopTimer();
		timer.start();
	}
	
	public void stopTimer()
	{
		Log.d(this.getClass().toString(), "Stopping AI timer");
		timer.cancel();
	}
	
	private class AICountDownTimer extends CountDownTimer
	{
		public AICountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			Log.d(this.getClass().toString(), "AI timer finished");
			gameLogicService.setTimedOut();	
			gameLogicService.updateTimerText("" + 0);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			int secondsLeft = (int)millisUntilFinished / 1000;
			Log.d(this.getClass().toString(), secondsLeft + "seconds left in AI timer");
			gameLogicService.updateTimerText("" + secondsLeft);
			
		}
		
	}	
}

