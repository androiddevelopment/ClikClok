//package com.clikclok.util;
//
//import com.clikclok.event.TileClickListener;
//import com.clikclok.service.TileOperationService;
//
//import android.os.CountDownTimer;
//
//public class AITimer {
//	private AICountDownTimer timer;
//	private TileOperationService tileOperationService;
//	
//	public AITimer(TileOperationService updateTileService)
//	{
//		timer = new AICountDownTimer(5 * 1000, 1 * 1000);
//		this.tileOperationService = updateTileService;
//	}
//	
//	public void startTimer()
//	{
//		timer.cancel();
//		timer.start();
//	}
//	
//	
//	private class AICountDownTimer extends CountDownTimer
//	{
//		public AICountDownTimer(long millisInFuture, long countDownInterval) {
//			super(millisInFuture, countDownInterval);
//		}
//
//		@Override
//		public void onFinish() {
//			TileClickListener.setTimeoutExpired(true);
////			tileOperationService.performAIOperation();
//			TileClickListener.setTimeoutExpired(false);			
//		}
//
//		@Override
//		public void onTick(long millisUntilFinished) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
//	
//}

