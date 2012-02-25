package com.clikclok.service;

import java.util.Random;

import com.clikclok.R;
import com.clikclok.domain.OperationType;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import android.content.Context;
import android.media.MediaPlayer;

@Singleton
public class SoundsService {
	@Inject 
	private Context context;
	private MediaPlayer mediaPlayer;
	private boolean enableSounds;
		
	private MediaPlayer getMediaPlayer(int resource)
	{
		mediaPlayer = MediaPlayer.create(context, resource);
		adjustVolume();
		return mediaPlayer;
	}
	
	public void playMoveSound(OperationType operationType, boolean enemyGainsMade) {
		// Stop any playing sounds first
		// Will only be ever null before the first sound is played
		if(mediaPlayer != null) 
		{
			mediaPlayer.stop();
		}
		
		if(!operationType.equals(OperationType.AI_SELECTION_OPERATION))
		{
			if(enemyGainsMade)
			{			
				getMediaPlayer(operationType.getEnemyTilesGainedSoundResource()).start();
			}
			else
			{
				getMediaPlayer(operationType.getMoveSoundResource()).start();
			}
		}
	}
	
	public void playUserWinsSound() {
		// Stop any playing sounds first
		mediaPlayer.stop();
		getMediaPlayer(R.raw.user_wins).start();
	}
	
	public void playAIWinsSound() {
		// Stop any playing sounds first
		mediaPlayer.stop();
		int soundResource = selectRandomSound(new int [] {R.raw.ai_wins_1, R.raw.ai_wins_2, R.raw.ai_wins_3});
		getMediaPlayer(soundResource).start();
	}
	
	public void adjustVolume(boolean enableSounds) {
		this.enableSounds = enableSounds;
		adjustVolume();
	}
	
	private void adjustVolume() {
		if(mediaPlayer != null) 
		{
			if(enableSounds) 
			{
				mediaPlayer.setVolume(0, 1);
			}
			else 
			{
				mediaPlayer.setVolume(0, 0);
			}
		}
	}
	
	private int selectRandomSound(int[] arraySounds) {
		Random randomNumberGenerator = new Random();
		int soundIndex = randomNumberGenerator.nextInt(arraySounds.length - 1);
		return arraySounds[soundIndex];
	}
}
