package com.clikclok;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikclok.domain.Level;
import com.clikclok.domain.OperationType;
import com.clikclok.event.EnableSoundsListener;
import com.clikclok.event.UpdateUIService;
import com.clikclok.service.GameLogicService;
import com.clikclok.service.domain.ResumeNextUITask;
import com.clikclok.service.impl.GameLogicServiceImpl;
import com.clikclok.util.Constants;
import com.clikclok.util.UIUtilities;
import com.clikclok.view.CustomDialog;
import com.clikclok.view.TileAdapter;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * This Activity is the only activity in the application. All UI updates are performed in this class so it effectively functions as the View of the application.
 * @author David
 */
public class ClikClokActivity extends RoboActivity implements UpdateUIService {
	@Inject
	private TileAdapter tileAdapter;
	@Inject
	private GameLogicService gameLogicService;
	@InjectView(R.id.gridView)
	private GridView gridView;
	@InjectView(R.id.userScore)
	private TextView userScoreView;
	@InjectView(R.id.aiScore)
	private TextView aiScoreView;
	@InjectView(R.id.levelLabel)
	private TextView levelLabelView;
	@InjectView(R.id.levelNumber)
	private TextView levelNumberView;
	@InjectView(R.id.countDownTimer)
	private TextView countDownTimerView;
	@InjectView(R.id.enableSoundsView)
	private ImageView enableSoundsView;
	@Inject
	private Handler handler;
	@Inject
	private EnableSoundsListener enableSoundsListener;
	@Inject
	private Injector injector;
	private Level currentLevel;
	private static Application application;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Initiilize the screen size
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		UIUtilities.setWindowWidth(metrics.widthPixels);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Translucent);
		setContentView(R.layout.main);
		// Initialize the grid's view and height
		initializeGridWidthAndHeight();
		gridView.setNumColumns(Constants.GRID_WIDTH);
		// This is exposed to other classes to allow them to get a hold of the context
		enableSoundsView.setOnClickListener(enableSoundsListener);
		// This needs to be manually injected as far as I can see
		((GameLogicServiceImpl) gameLogicService).setUpdateUIService(this);
		// Initialize Application context
		application = getApplication();
		// Initialize the customized fonts
		initializeFonts();
				
		showDialog(Constants.INTRODUCTION_DIALOG);
		
		
	}

	/**
	 * These are the customized fonts that will be used 
	 */
	private void initializeFonts() {
		levelLabelView.setTypeface(UIUtilities
				.setFont(getString(R.string.game_font_type)));
		levelNumberView.setTypeface(UIUtilities
				.setFont(getString(R.string.game_font_type)));
		userScoreView.setTypeface(UIUtilities
				.setFont(getString(R.string.game_font_type)));
		aiScoreView.setTypeface(UIUtilities
				.setFont(getString(R.string.game_font_type)));
		countDownTimerView.setTypeface(UIUtilities
				.setFont(getString(R.string.game_font_type)));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		CustomDialog dialog = null;
		CustomDialog.Builder builder = new CustomDialog.Builder(this,
				R.layout.dialog_layout);
		switch (id) {
		case Constants.LEVEL_COMPLETE_DIALOG:
			builder.setMessage(
					getString(R.string.levelComplete,
							currentLevel.getLevelNum())).createLeftButton(
					getString(R.string.proceedButton),
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							gameLogicService.nextLevel();
							dismissDialog(Constants.LEVEL_COMPLETE_DIALOG);
						}
					});
			dialog = builder.create();
			dialog.setCancelable(false); // Ideally this should be a part of the builder but didn't see an obvious way to achieve this
			break;
		case Constants.USER_WINNER_DIALOG:
			builder.setMessage(getString(R.string.userIsWinner))
					.createLeftButton(getString(R.string.prizeButton),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// When the user wins a browser will open to bring them to their prize
									Intent browserIntent = new Intent(
											Intent.ACTION_VIEW,
											Uri.parse(getString(R.string.prizeURL)));
									startActivity(browserIntent);
									finish();
								}
							});
			dialog = builder.create();
			dialog.setCancelable(false);
			break;
		case Constants.AI_WINNER_DIALOG:
			builder.setMessage(getString(R.string.aiIsWinner))
					.createLeftButton(getString(R.string.noButton),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// Application will close if the user does not want to play again
									finish();
								}
							})
					.createRightButton(getString(R.string.yesButton),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// Otherwise a new GameState will be initialized
									initialize();
									dismissDialog(Constants.AI_WINNER_DIALOG);
								}
							});
			dialog = builder.create();
			dialog.setCancelable(false);
			break;
		case Constants.INTRODUCTION_DIALOG:
			builder.setMessage(getString(R.string.introduction))
					.createLeftButton(getString(R.string.startGameButton),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dismissDialog(Constants.INTRODUCTION_DIALOG);
								}
							})
					.createRightButton(getString(R.string.rulesButton),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									showDialog(Constants.RULES_DIALOG);
								}
							});
			dialog = builder.create();
			dialog.setCancelable(true);
			break;
		case Constants.EXIT_DIALOG:
			builder.setMessage(getString(R.string.exitText))
					.createLeftButton(getString(R.string.noButton),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dismissDialog(Constants.EXIT_DIALOG);
								}
							})
					.createRightButton(getString(R.string.yesButton),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									stopGame();
								}
							});
			dialog = builder.create();
			dialog.setCancelable(true);
			break;
		case Constants.DEMO_DIALOG:
			builder.setMessage(getString(R.string.demoText))
					.createLeftButton(getString(R.string.exitButtonText),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									stopGame();
								}
							});
			dialog = builder.create();
			dialog.setCancelable(true);
			break;
		case Constants.RULES_DIALOG:
			builder.setMessage(getString(R.string.rules)).createLeftButton(
					getString(R.string.okButton), new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dismissDialog(Constants.RULES_DIALOG);
							showDialog(Constants.INTRODUCTION_DIALOG);
						}
					});
			dialog = builder.create();
			dialog.setCancelable(true);
			break;
		}
		return dialog;
	}

	@Override
	public void updateGridView(final int userScore, final int aiScore, final OperationType operationType) {
		final boolean haveWinner = userScore == 0 || aiScore == 0;
		// This is added to the Handler's queue to ensure that refreshes are
		// performed in the order that they are invoked.
		handler.post(new Runnable() {
			@Override
			public void run() {
				// This notifies the UI thread that we wish to refresh the grid
				tileAdapter.notifyDataSetChanged();
				// Although we have notified the UI that we wish to refresh, we have no control over when it will perform this refresh.
				// Therefore, we do not wish to perform the next UI update until we know that the previous one has completed.
				// To achieve this we add a task to the GridView's handler. This task will only run after the refresh has performed.
				// This task will then kick off the next UI update task
				ResumeNextUITask gameResumeTask = new ResumeNextUITask(haveWinner, operationType);
				injector.injectMembers(gameResumeTask);
				gridView.post(gameResumeTask);
				// After refreshing the grid we should update the scores accordingly
				setScoreViews(userScore, aiScore);
			}
		});
	}

	@Override
	public void showNextLevelDialog(final Level currentLevel) {
		this.currentLevel = currentLevel;
		handler.post(new Runnable() {
			@Override
			public void run() {
				showDialog(Constants.LEVEL_COMPLETE_DIALOG);				
			}
		});

	}
	
	@Override
	public void showDemoDialog() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				showDialog(Constants.DEMO_DIALOG);
			}
		});
	}
	
	/**
	 * Width is size of tile times width, minus one eight 
	 * Height is size of tile times the height and minus a small fraction
	 * Vertical spacing attempts to pull cells closer to each other by one eight
	 */
	private void initializeGridWidthAndHeight() {
		int layoutWidth = UIUtilities.getTileWidth() * Constants.GRID_WIDTH * 7 / 8;
		int layoutHeight = UIUtilities.getTileWidth() * Constants.GRID_HEIGHT * 15 / 16;
		int verticalSpacing = 0 - UIUtilities.getTileWidth() / 8;
		gridView.setLayoutParams(new FrameLayout.LayoutParams(layoutWidth,
				layoutHeight));
		countDownTimerView.setLayoutParams(new FrameLayout.LayoutParams(
				layoutWidth, layoutHeight));
		gridView.setVerticalSpacing(verticalSpacing);
	}

	@Override
	protected void onDestroy() {
		gameLogicService.destroyGame();
		super.onDestroy();
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		CustomDialog customDialog = (CustomDialog) dialog;
		switch (id) {
		// Because the level number changes for each level we need to update the dialog prior to it being displayed
		case Constants.LEVEL_COMPLETE_DIALOG:
			customDialog.setMessage((getString(R.string.levelComplete,
					currentLevel.getLevelNum())));
			break;
		}
	}

	@Override
	public void updateCountdownTimerView(final String secondsLeft) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				countDownTimerView.setText(secondsLeft);
			}
		});
	}

	@Override
	protected void onStart() {
		initialize();
		super.onStart();
	}

	/**
	 * This logic is common to both a new instance of the application, and resuming an existing instance
	 */
	private void initialize() {
		gameLogicService.initialize();
		// if the volume was previously disabled then the icon should reflect this
		updateVolumeIcon(gameLogicService.isSoundEnabled());
		updateLevelView(1);
		// TODO Does this need to be reinjected? 
		gridView.setAdapter(tileAdapter);
	}

	@Override
	public void showUserWinnerDialog() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				showDialog(Constants.USER_WINNER_DIALOG);
			}
		});
	}

	@Override
	public void showAIWinnerDialog() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				showDialog(Constants.AI_WINNER_DIALOG);
			}
		});
	}

	@Override
	public void onBackPressed() {
		showDialog(Constants.EXIT_DIALOG);
	}

	@Override
	protected void onPause() {
		stopGame();
		super.onPause();
	}

	/**
	 * Stop the game and close
	 */
	private void stopGame() {
		// Stop the timer as this will still be running
		gameLogicService.stopTimer();
		finish();
	}

	@Override
	public void updateVolumeIcon(final boolean volumeIsOn) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (volumeIsOn) {
					enableSoundsView.setImageResource(R.drawable.volume_icon);
				} else {
					enableSoundsView.setImageResource(R.drawable.mute_icon);
				}
			}
		});
	}
	
	/**
	 * @return Application context required by other classes
	 */
	public static Application getAppContext() {
		return application;
	}
	
	/**
	 * Set the scores
	 * @param userScore
	 * @param aiScore
	 */
	private void setScoreViews(final int userScore, final int aiScore) {
		userScoreView.setText(String.format("%03d", userScore));
		aiScoreView.setText(String.format("%03d", aiScore));
	}
	
	@Override
	public void updateLevelView(int number) {
		levelNumberView.setText("" + number);
	}
}