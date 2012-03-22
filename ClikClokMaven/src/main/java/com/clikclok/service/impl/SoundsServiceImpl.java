package com.clikclok.service.impl;

import java.util.Random;

import android.app.Application;
import android.media.MediaPlayer;

import com.clikclok.R;
import com.clikclok.domain.OperationType;
import com.clikclok.service.SoundsService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Service to play the various sounds in the game. These are played when a tile is updated, when a large number of tiles are won, or when the game is won or lost
 */
@Singleton
public class SoundsServiceImpl implements SoundsService {
	@Inject 
	private Application applicationContext;
	private MediaPlayer mediaPlayer;
	private boolean enableSounds;
		
	private MediaPlayer getMediaPlayer(int resource)
	{
		mediaPlayer = MediaPlayer.create(applicationContext, resource);
		adjustVolume();
		return mediaPlayer;
	}
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.SoundsService#playMoveSound(com.clikclok.domain.OperationType, boolean)
	 */
	@Override
	public void playMoveSound(OperationType operationType, boolean enemyGainsMade) {
		// Stop any playing sounds first
		// Will only be ever null before the first sound is played
		if(mediaPlayer != null) 
		{
			mediaPlayer.stop();
		}
		// There are no sounds for these 2 operation types
		if(!operationType.equals(OperationType.AI_OPERATION) && !operationType.equals(OperationType.USER_OPERATION))
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
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.SoundsService#playUserWinsSound()
	 */
	@Override
	public void playUserWinsSound() {
		// Stop any playing sounds first
		mediaPlayer.stop();
		getMediaPlayer(R.raw.user_wins).start();
	}
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.SoundsService#playAIWinsSound()
	 */
	@Override
	public void playAIWinsSound() {
		// Stop any playing sounds first
		mediaPlayer.stop();
		int soundResource = selectRandomSound(new int [] {R.raw.ai_wins_1, R.raw.ai_wins_2, R.raw.ai_wins_3});
		getMediaPlayer(soundResource).start();
	}
	
	/* (non-Javadoc)
	 * @see com.clikclok.service.impl.SoundsService#adjustVolume(boolean)
	 */
	@Override
	public void adjustVolume(boolean enableSounds) {
		this.enableSounds = enableSounds;
		adjustVolume();
	}
	
	/**
	 * Adjusts the volume on the {@link MediaPlayer}
	 */
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
	
	/**
	 * Play a random sound for the array of sounds
	 * @param arraySounds
	 * @return
	 */
	private int selectRandomSound(int[] arraySounds) {
		Random randomNumberGenerator = new Random();
		int soundIndex = randomNumberGenerator.nextInt(arraySounds.length - 1);
		return arraySounds[soundIndex];
	}
}
