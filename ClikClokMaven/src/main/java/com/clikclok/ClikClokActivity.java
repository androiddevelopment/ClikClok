package com.clikclok;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.clikclok.domain.Level;
import com.clikclok.domain.OperationType;
import com.clikclok.event.EnableSoundsListener;
import com.clikclok.event.UpdateUIListener;
import com.clikclok.service.GameLogicService;
import com.clikclok.service.TileOperationService;
import com.clikclok.service.UIOperationQueue;
import com.clikclok.service.domain.GameResumeTask;
import com.clikclok.util.Constants;
import com.clikclok.util.UIUtilities;
import com.clikclok.view.CustomDialog;
import com.clikclok.view.TileAdapter;
import com.google.inject.Inject;

/**
 * @author David
 * 
 */
public class ClikClokActivity extends RoboActivity implements UpdateUIListener {
	@Inject
	private TileAdapter tileAdapter;
	@Inject
	private GameLogicService gameLogicService;
	@Inject
	private UIOperationQueue uiOperationQueue;
	@Inject
	private TileOperationService tileOperationService;
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
	private Level currentLevel;
	private static Context context;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.d(this.getClass().toString(), "Entering onCreate");
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Translucent);
		setContentView(R.layout.main);
		initializeGridWidthAndHeight();
		gridView.setNumColumns(Constants.GRID_WIDTH);

		context = getApplicationContext();

		gameLogicService.setUpdateUIListener(this);
		enableSoundsView.setOnClickListener(enableSoundsListener);

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

		showDialog(Constants.INTRODUCTION_DIALOG);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		CustomDialog dialog = null;
		CustomDialog.Builder builder = new CustomDialog.Builder((Context) this,
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
							// Need to find a better way to inject this
							tileAdapter.setGameState(gameLogicService
									.getGameState());
							updateGrid(0, 0, false, OperationType.AI_OPERATION);
							dismissDialog(Constants.LEVEL_COMPLETE_DIALOG);
						}
					});
			dialog = builder.create();
			dialog.setCancelable(false);
			break;
		case Constants.USER_WINNER_DIALOG:
			builder.setMessage(getString(R.string.userIsWinner))
					.createLeftButton(getString(R.string.prizeButton),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
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
									finish();
								}
							})
					.createRightButton(getString(R.string.yesButton),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
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
	public void updateGrid(final int userScore, final int aiScore, final boolean userHasWon, final OperationType operationType) {
		if(operationType.equals(OperationType.USER_OPERATION)) {
			uiOperationQueue.startNextTask();
		}
		// This is added to the Handler's queue to ensure that refreshes are
		// performed in the order that they are invoked.
		handler.post(new Runnable() {
			@Override
			public void run() {
				tileAdapter.notifyDataSetChanged();
				// Prefix these with empty String to ensure autoboxing occurs
				gridView.post(new GameResumeTask(uiOperationQueue, userHasWon, operationType));
				setScores(userScore, aiScore);
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
				levelNumberView.setText(""
						+ Level.getNextLevel(currentLevel).getLevelNum());
			}
		});

	}

	/**
	 * Width is size of tile times ten, minus one eight Height is size of tile
	 * times thirteen. Minus an eight does not have any impact here Vertical
	 * spacing attempts to pull cells closer to each other by one eight
	 */
	private void initializeGridWidthAndHeight() {
		int layoutWidth = Constants.TILE_WIDTH * Constants.GRID_WIDTH * 7 / 8;
		int layoutHeight = Constants.TILE_WIDTH * Constants.GRID_HEIGHT * 15
				/ 16;
		int verticalSpacing = 0 - Constants.TILE_WIDTH / 8;
		Log.d(this.getClass().toString(), "Layout parameters are "
				+ layoutWidth + ", " + layoutHeight + "," + verticalSpacing);
		gridView.setLayoutParams(new FrameLayout.LayoutParams(layoutWidth,
				layoutHeight));
		countDownTimerView.setLayoutParams(new FrameLayout.LayoutParams(
				layoutWidth, layoutHeight));
		gridView.setVerticalSpacing(verticalSpacing);
	}

	@Override
	protected void onDestroy() {
		Log.d(this.getClass().toString(), "onDestroy() called");
		tileOperationService.clearOperationsFromQueue();
		super.onDestroy();
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		CustomDialog customDialog = (CustomDialog) dialog;
		switch (id) {
		case Constants.LEVEL_COMPLETE_DIALOG:
			customDialog.setMessage((getString(R.string.levelComplete,
					currentLevel.getLevelNum())));
			break;
		}

	}

	@Override
	public void updateCountdownTimer(final String secondsLeft) {
		Log.d(this.getClass().toString(), "Entering updateCountdownTimer with "
				+ secondsLeft + " seconds left");
		handler.post(new Runnable() {
			@Override
			public void run() {
				countDownTimerView.setText(secondsLeft);
			}
		});

	}

	@Override
	protected void onStart() {
		Log.d(this.getClass().toString(), "onStart() called");

		initialize();

		super.onStart();
	}

	private void initialize() {
		updateVolumeIcon(gameLogicService.isSoundEnabled());

		if (!uiOperationQueue.isAlive()) {
			uiOperationQueue.start();
		}

		gameLogicService.initialize();
		tileAdapter.setGameState(gameLogicService.getGameState());
		gridView.setAdapter(tileAdapter);
		setScores(0, 0); // This is required if the user wishes to restart the
							// game
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

	public static Context getContext() {
		return context;
	}

	@Override
	protected void onPause() {
		stopGame();
		super.onPause();
	}

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

	private void setScores(final int userScore, final int aiScore) {
		userScoreView.setText(String.format("%03d", userScore));
		aiScoreView.setText(String.format("%03d", aiScore));
	}
}