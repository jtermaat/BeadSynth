package com.beadsynth.view;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.beadsynth.buttons.ButtonArrangementManager;
import com.beadsynth.configuration.ConfigurationData;
import com.beadsynth.configuration.ScaleNote;
import com.beadsynth.configuration.ScaleType;
import com.beadsynth.filestorage.SavedSettings;
import com.beadsynth.keyboard.KeyboardRangeBar;
import com.beadsynth.keyboard.NoteSnapControl;
import com.beadsynth.recording.AudioFileWriter;
import com.beadsynth.util.ProportionalRect;

public class BeadView extends View {

	BeadActivity mainActivity;
	Context context;
	SoundBoard board;
	Thread soundboardThread;
	int screenWidth;
	int screenHeight;
	Rect screenRect;
	boolean initialized;
	boolean needsToRestoreButtons;
	long frameEndTime;
	Bitmap bg_img;
	Paint p;
	AudioFileWriter audioFileWriter;
	
//	private List<CustomButton> customButtons;
	
	boolean isRecording;

	static int fps = 50;
	int delay;
	long frame;
	
	// BUTTON RELATED OBJECTS, TO BE MOVED TO SEPARATE VIEW
	
/*	SidePanelButtonGraphicsManager sidePanelGraphics;
	SidePanelButtonGraphicsManager sidePanelGraphicsBottom;*/
	ButtonControl buttonControl;
	NoteSnapControl noteSnapControl;
/*	List<CustomButton> customButtons;*/

	public BeadView(Context context, AttributeSet attr) {
		super(context, attr);
		this.context = context.getApplicationContext();
/*		this.board = board;
		soundboardThread = new Thread(board);
		soundboardThread.start();
		initialized = false;
		needsToRestoreButtons = false;
		delay = (int) (1000.0 / (double) fps);
//		bg_img = CommonFunctions.loadBitmap("bg_dark_v21", context);
		p = new Paint();
		frameEndTime = 0L;
		isRecording = false;*/
	}
	
	public void setup(SoundBoard board, BeadActivity mainActivity) {
		this.board = board;
		soundboardThread = new Thread(board);
		soundboardThread.start();
		initialized = false;
		needsToRestoreButtons = false;
		delay = (int) (1000.0 / (double) fps);
//		bg_img = CommonFunctions.loadBitmap("bg_dark_v21", context);
		p = new Paint();
		frameEndTime = 0L;
		isRecording = false;	
		this.mainActivity = mainActivity;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		frameEndTime = System.currentTimeMillis() + delay;

		if (!initialized) {
			screenWidth = canvas.getWidth();
			screenHeight = canvas.getHeight();
			board.setDimensions(screenWidth, screenHeight);
			screenRect = new Rect(0, 0, screenWidth, screenHeight);
//			ButtonArrangementManager arrangmentManager = new ButtonArrangementManager(screenWidth, screenHeight);
//			arrangmentManager.arrangeButtonsRelatively(mainActivity);
			mainActivity.layoutRelativeButtons();
			
			initialized = true;
		}
		if (needsToRestoreButtons) {
			buttonControl.restoreProperSettings();
			noteSnapControl.restoreProperSettings();
			needsToRestoreButtons = false;
		};
		if (isRecording) {
			try {
				audioFileWriter.attemptWrite();
			} catch (IOException ie) {
				System.out.println("Error writing data to audio file");
				ie.printStackTrace();
			}	
		}

		try {
//			canvas.drawBitmap(bg_img, null, screenRect, p);
			board.paintFrame(canvas);
		} catch (Exception e) {
			System.out
					.println("Threading conflict while drawing View.  Trying one more time.");
			try {
				board.paintFrame(canvas);
			} catch (Exception e2) {
				System.out
						.println("Threading conflict repeating. Skipping frame draw.");
				e2.printStackTrace();
			}
		}
		try {
			Thread.sleep(Math.max(0, frameEndTime - System.currentTimeMillis()));
		} catch (InterruptedException e) {
			System.out.println("My sleep was interrupted.");
		}

		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			board.touchedDown(event.getX(), event.getY());
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			board.dragMoved(event.getX(), event.getY());
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			board.dragDrop(event.getX(), event.getY());
		}
		return true;
	}

//	public void respondToButtonClick(View v) {
//		board.respondToButtonClick(v);
//	}

	public void setButtonControl(ButtonControl control) {
		board.setButtonControl(control);
	}

//	public void setNoteSnapControl(NoteSnapControl control) {
//		board.setNoteSnapControl(control);
//	}

//	public void respondToKeyboardRelatedButtonClick(View v) {
//		board.respondToKeyboardRelatedButtonClick(v);
//	}

	public void setKeyboardRangeBar(KeyboardRangeBar keyboardRangeBar) {
		board.setKeyboardRangeBar(keyboardRangeBar);
	}

	public double getBpm() {
		return board.getBpm();
	}

	public ScaleType getScaleType() {
		return board.getScaleType();
	}

	public ScaleNote getTonicNote() {
		return board.getTonicNote();
	}

	public void setBpm(double bpm) {
		board.setBpm(bpm);
	}

	public void setScaleInfo(ScaleNote tonicNote, ScaleType scaleType) {
		board.setScaleInfo(tonicNote, scaleType);
	}

	public ConfigurationData getConfigurationData() {
		return new ConfigurationData(getBpm(), getTonicNote(), getScaleType());
	}

	public void setConfigurationData(ConfigurationData data) {
		setScaleInfo(data.getTonicNote(), data.getScaleType());
		setBpm(data.getBpm());
	}

	public void saveSettingsToFile(String filename) {
		board.saveSettingsToFile(filename);
	}

	public void revertToSavedSettings(SavedSettings settings) {
		board.revertToSavedSettings(settings);
	}

	public void addBead() {
		board.addBead();
	}

	public void removeBead() {
		board.removeBead();
	}

	// Methods for handling Activity changes

	public void destroy() {
		board.stop();
		try {
			soundboardThread.join();
		} catch (InterruptedException ie) {
			System.out
					.println("functionality thread interrupted while joining");
		}
	}

	public void pause() {
		board.pause();
//		try {
////			soundboardThread.join();
////			soundboardThread.
//		} catch (InterruptedException ie) {
//			System.out
//					.println("functionality thread interrupted while joining");
//		}
	}

	public void resume() {
		if (initialized) {
			board.resume();
//			System.out.println("step 1");
//			soundboardThread = new Thread(board);
//			soundboardThread.start();
//			System.out.println("step 2");
//			soundboardThread.start();
//			System.out.println("invalidate");
//			needsToRestoreButtons = true;
//			invalidate();
		}
	}
	
	public AudioFileWriter getAudioFileWriter() {
		this.audioFileWriter = board.getRecordingWriter();
		return audioFileWriter;
	}
	
	public void switchRecordingButton() {
		board.switchRecordingButton();
	}
	
	public void setRecording(boolean recording) {
		isRecording = recording;
		System.out.println("setting 'isRecording' to: " + recording);
		
		board.setRecording(recording);
	}
	
	public void setBoard(SoundBoard board) {
		this.board = board;
	}

	public void setRangeBarPosition(ProportionalRect pRangeBarSpace) {
		board.setRangeBarPosition(pRangeBarSpace);
		
	}
	
/*	public void setSidePanelGraphics(SidePanelButtonGraphicsManager sidePanelGraphics) {
//		board.setSidePanelGraphics(sidePanelGraphics);
		this.sidePanelGraphics = sidePanelGraphics;
	}
	
	public void setSidePanelGraphicsBottom(SidePanelButtonGraphicsManager sidePanelGraphicsBottom) {
//		board.setSidePanelGraphicsBottom(sidePanelGraphicsBottom);
		this.sidePanelGraphicsBottom = sidePanelGraphicsBottom;
	}*/

/*	public List<CustomButton> getCustomButtons() {
		return customButtons;
	}

	public void setCustomButtons(List<CustomButton> customButtons) {
		this.customButtons = customButtons;
//		board.setCustomButtons(customButtons);
	}*/
	
/*	// BUTTON METHODS: TO BE REFACTORED TO SEPARATE VIEW
	
	public void setButtonControl(ButtonControl buttonControl) {
		// Don't accept new buttoncontrol if an existing one exists.
		if (this.buttonControl == null) {
			System.out.println("ACTUALLY ASSIGNING Button control");
			this.buttonControl = buttonControl;
		} else {
			System.out.println("NOT ASSIGNING Button control");
		}
	}

	public void setNoteSnapControl(NoteSnapControl control) {
		if (this.noteSnapControl == null) {
			this.noteSnapControl = control;
		}
	}
	
	public void drawButtonRelatedGraphics(Canvas c) {
		buttonControl.drawGraphics(c);
		sidePanelGraphics.drawButtons(c);
		sidePanelGraphicsBottom.drawButtons(c);
		for (CustomButton button : customButtons) {
			button.draw(c);
		}
	}
	
	public void respondToButtonClick(View v) {
		int response = buttonControl.respondToButtonClick(v);
		if (response == ButtonControl.RESPONSE_LOCK_ON_RELEASE_ALTERED) {
			board.setLockOnRelease(buttonControl.isLockOnReleaseSet());
		} else if (response == ButtonControl.RESPONSE_CONNECT_ON_RELEASE_ALTERED) {
			board.setConnectOnRelease(buttonControl.isConnectOnReleaseSet());
		} else if (response == ButtonControl.RESPONSE_TAKE_SNAPSHOT) {
//			for (BeadKeyboardSynth synth : synths) {
//				synth.takeIndexedSnapshot(buttonControl.getLastSnapshotTaken());
//			}
			board.takeIndexedSnapshot();
		} else if (response == ButtonControl.RESPONSE_SET_BEAT_LENGTH_NONE) {
			board.getBeatManager().setActive(false);
		} else if (response == ButtonControl.RESPONSE_WARN_NO_SNAPSHOT_SLOT_SELECTED) {
			Toast.makeText(
					context,
					"No snapshot slot has been selected\n"
							+ "Select a slot to store snapshot",
					Toast.LENGTH_SHORT).show();
		} else if (response > 0) {
			board.getBeatManager().setBeatFraction(buttonControl.getFraction(response));
			board.getBeatManager().setActive(true);
		}
	}
	
	public void respondToKeyboardRelatedButtonClick(View v) {
		int currentlySelectedSnapshot = noteSnapControl
				.getCurrentSnapshotSelected();
		int response = noteSnapControl.respondToButtonClick(v);
		if (response == NoteSnapControl.RESPONSE_TAKE_SNAPSHOT) {
			board.takeKeyboardSnapshot(currentlySelectedSnapshot);
		} else if (response == NoteSnapControl.RESPONSE_WARN_NO_SNAPSHOT_SLOT_SELECTED) {
			Toast.makeText(
					context,
					"No keyboard snapshot slot has been selected\n"
							+ "Select a slot to store snapshot",
					Toast.LENGTH_SHORT).show();
		} else if (response == NoteSnapControl.RESPONSE_DO_NOTHING) {
			// Seriously, do nothing.
		} else {
			board.setKeyboardReversionSnapshot(response);
		}
	}
	
	public List<CustomButton> getCustomButtons() {
		return customButtons;
	}

	public void setCustomButtons(List<CustomButton> customButtons) {
		this.customButtons = customButtons;
//		board.setCustomButtons(customButtons);
	}*/

}
