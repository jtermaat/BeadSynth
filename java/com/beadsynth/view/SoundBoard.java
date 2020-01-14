package com.beadsynth.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import com.beadsynth.configuration.ScaleNote;
import com.beadsynth.configuration.ScaleType;
import com.beadsynth.filestorage.BeadIndexer;
import com.beadsynth.filestorage.SavedSettings;
import com.beadsynth.filestorage.SettingsFileWriter;
import com.beadsynth.keyboard.DynamicKeyboard;
import com.beadsynth.keyboard.KeyboardRangeBar;
import com.beadsynth.keyboard.KeyboardReversionStatus;
import com.beadsynth.keyboard.KeyboardSnapshot;
import com.beadsynth.physics.Bead;
import com.beadsynth.physics.BeatManager;
import com.beadsynth.physics.RecordableBead;
import com.beadsynth.physics.RecordableWaveString;
import com.beadsynth.recording.AudioFileInput;
import com.beadsynth.recording.AudioFileWriter;
import com.beadsynth.util.CommonFunctions;
import com.beadsynth.util.ProportionalRect;

public class SoundBoard implements Runnable {

	static int DEFAULT_XSTART = 35;
	static int DEFAULT_XEND = 300;
	static int DEFAULT_YLEVEL1 = 250;
	static int DEFAULT_YLEVEL2 = 300;
	static int DEFAULT_YLEVEL3 = 350;
	static double DEFAULT_MAX_AMP = 400;
	static Rect WAVE_SPACE_1 = new Rect(20, 300, 300, 500);
	static Rect WAVE_SPACE_2 = new Rect(20, 300, 300, 500);
	static Rect WAVE_SPACE_3 = new Rect(20, 300, 300, 500);
	static Rect KEYBOARD_SPACE_1 = new Rect(100, 450, 900, 515);
	static Rect KEYBOARD_SPACE_2 = new Rect(100, 515, 900, 580);
	static Rect KEYBOARD_SPACE_3 = new Rect(100, 580, 900, 645);	
	
	// Using proportional spaces
//	static ProportionalRect P_KEYBOARD_SPACE_1 = 
//			new ProportionalRect(100, 100, 0.6465517241, 0.078125, 0.7399425287, 0.703125);
//	
	
	static ProportionalRect P_KEYBOARD_SPACE_1  = 
			new ProportionalRect(100, 100, 0.6465517241, 0.078125, 0.7399425287, 0.703125);
	static ProportionalRect P_KEYBOARD_SPACE_2  = 
			new ProportionalRect(100, 100, 0.7399425287, 0.078125, 0.8333333333, 0.703125);
	static ProportionalRect P_KEYBOARD_SPACE_3  = 
			new ProportionalRect(100, 100, 0.8333333333, 0.078125, 0.9267241379, 0.703125);


	int xStart;
	int xEnd;
	int[] yLevels;
	int numStrings;

	public boolean paused;
	public boolean stopped;

	double maxAmp;
	
	private int yStringMax;
	private int yStringMin;

	BeadIndexer beadIndexer;
	
	KeyboardRangeBar keyboardRangeBar;

	KeyboardReversionStatus keyboardRevesionStatus;

	static int DEFAULT_NUM_BEADS = 25;

	List<BeadKeyboardSynth> synths;

	long frameEndTime;

	Bead bound;
	Bead beadToConnect;

	private boolean lockOnRelease;
	private boolean connectOnRelease;


	private BeatManager beatManager;
	boolean needsToRestoreButtons;

	
	KeyboardSnapshot[] keyboardSnapshots;
	int numSnapshots;
	
	private AudioFileWriter recordingWriter;

	int screenWidth;
	int screenHeight;

	double bpm;
	ScaleNote tonicNote;
	ScaleType scaleType;
	
	private String bgImageFilename;

	public static int SAMPLE_RATE = 22050;

	static double frequency1 = 110.000;
	static double waveLength1 = (int) ((double) SAMPLE_RATE / frequency1);

	static double DEFAULT_BPM = 100.0;
	static ScaleNote DEFAULT_TONIC_NOTE = ScaleNote.A;
	static ScaleType DEFAULT_SCALE_TYPE = ScaleType.CHROMATIC;
	
	Paint paint;
	Bitmap pipeEnd;
	
	ButtonControl buttonControl;

	Context context;

	public SoundBoard(Context context) {
		xStart = DEFAULT_XSTART;
		xEnd = DEFAULT_XEND;
		numStrings = 3;
		yLevels = new int[numStrings];
		yLevels[0] = DEFAULT_YLEVEL1;
		yLevels[1] = DEFAULT_YLEVEL2;
		yLevels[2] = DEFAULT_YLEVEL3;
		maxAmp = DEFAULT_MAX_AMP;

		tonicNote = DEFAULT_TONIC_NOTE;
		scaleType = DEFAULT_SCALE_TYPE;

		bpm = 100.0;
		beadIndexer = new BeadIndexer();
		
		keyboardSnapshots = new KeyboardSnapshot[DynamicKeyboard.MAX_SNAPSHOTS];
		for (int i = 0;i<DynamicKeyboard.MAX_SNAPSHOTS;i++) {
			keyboardSnapshots[i] = new KeyboardSnapshot();
		}
		numSnapshots = 0;

		keyboardRevesionStatus = new KeyboardReversionStatus();

		// Prepare the three synths

		Rect waveSpace = new Rect(WAVE_SPACE_1);
		Rect keyboardSpace = new Rect(KEYBOARD_SPACE_1);
		ProportionalRect pKeyboardSpace = P_KEYBOARD_SPACE_1;

		BeadKeyboardSynth synth1 = new BeadKeyboardSynth(context, xStart, xEnd,
				yLevels[0], DEFAULT_NUM_BEADS, maxAmp, waveSpace,
				pKeyboardSpace, ImageScheme.getDefaultImageScheme(0, context),
				beadIndexer);

		Rect waveSpace2 = new Rect(20, 300, 300, 500);
		Rect keyboardSpace2 = new Rect(KEYBOARD_SPACE_2);
		ProportionalRect pKeyboardSpace2 = P_KEYBOARD_SPACE_2;

		BeadKeyboardSynth synth2 = new BeadKeyboardSynth(context, xStart, xEnd,
				yLevels[1], DEFAULT_NUM_BEADS, maxAmp, waveSpace2,
				pKeyboardSpace2, ImageScheme.getDefaultImageScheme(1, context),
				beadIndexer);

		Rect waveSpace3 = new Rect(20, 300, 300, 500);
		Rect keyboardSpace3 = new Rect(KEYBOARD_SPACE_3);
		ProportionalRect pKeyboardSpace3 = P_KEYBOARD_SPACE_3;

		BeadKeyboardSynth synth3 = new BeadKeyboardSynth(context, xStart, xEnd,
				yLevels[2], DEFAULT_NUM_BEADS, maxAmp, waveSpace3,
				pKeyboardSpace3, ImageScheme.getDefaultImageScheme(2, context),
				beadIndexer);

		synths = new ArrayList<BeadKeyboardSynth>();

		AudioFileInput recordingInput1 = new AudioFileInput();
		AudioFileInput recordingInput2 = new AudioFileInput();
		AudioFileInput recordingInput3 = new AudioFileInput();
		
		synth1.setAudioFileInput(recordingInput1);
		synth2.setAudioFileInput(recordingInput2);
		synth3.setAudioFileInput(recordingInput3);
		
		synth1.getKeyboard().setSelectedKeyByNumber(27);
		synth2.getKeyboard().setSelectedKeyByNumber(34);
		synth3.getKeyboard().setSelectedKeyByNumber(43);
		
		synth1.setWaveLengthToKeyboard();
		synth2.setWaveLengthToKeyboard();
		synth3.setWaveLengthToKeyboard();
		
		AudioFileInput[] recordingInputs = new AudioFileInput[3];
		recordingInputs[0] = recordingInput1;
		recordingInputs[1] = recordingInput2;
		recordingInputs[2] = recordingInput3;
		
		setRecordingWriter(new AudioFileWriter(recordingInputs, 3, context));
		
		synths.add(synth1);
		synths.add(synth2);
		synths.add(synth3);
		
		paint = new Paint();
		pipeEnd = CommonFunctions.loadBitmap("pipe_end", context);

		paused = false;
		needsToRestoreButtons = false;

		bound = null;
		setBeatManager(new BeatManager());
		getBeatManager().setBpm(DEFAULT_BPM);
		getBeatManager().setBeatFraction(.25);
		getBeatManager().setActive(false);
		getBeatManager().start();
		this.context = context;
	}

	public void run() {
		final int fps = 20;
		int delay;
		long frame;
		delay = (int) (1000.0 / (double) fps);

		while (!paused && !stopped) {
			try {
				frameEndTime = System.currentTimeMillis() + delay;
//				if (needsToRestoreButtons) {
//					buttonControl.restoreProperSettings();
//					noteSnapControl.restoreProperSettings();
//					needsToRestoreButtons = false;
//				}
				for (BeadKeyboardSynth synth : synths) {
					synth.update();
				}
				if (getBeatManager().checkForBeat()) {
					if (keyboardRevesionStatus.readyForReversion()) {
						immediatelyRevertToKeyboardIndex(keyboardRevesionStatus
								.revertTo());
					}
					if (buttonControl.hasSelectedSnapshot()) {
						for (BeadKeyboardSynth synth : synths) {
							synth.revertToSnapshot(buttonControl
									.getCurrentSnapshotSelected());
						}
					}
				}
				try {

					Thread.sleep(Math.max(0,
							frameEndTime - System.currentTimeMillis()));
				} catch (InterruptedException e) {
					System.out.println("My sleep was interrupted.");
				}
			} catch (NullPointerException ne) {
				System.out
						.println("Main functional thread incurred conflict.  Attempting to repeat frame.");
			}
		}

	}

	public void setDimensions(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		calculateComponentDimensions();
		setyStringMin((int)((double)screenHeight * 0.15));
		setyStringMax((int)((double)screenHeight * 0.6));
		ProportionalRect pKeyboardSpace1  = 
				new ProportionalRect(screenWidth, screenHeight, 0.6465517241, 0.078125, 0.7399425287, 0.703125);
		ProportionalRect pKeyboardSpace2  = 
				new ProportionalRect(screenWidth, screenHeight, 0.7399425287, 0.078125, 0.8333333333, 0.703125);
		ProportionalRect pKeyboardSpace3  = 
				new ProportionalRect(screenWidth, screenHeight, 0.8333333333, 0.078125, 0.9267241379, 0.703125);
		ProportionalRect pRangeBarSpace =
				new ProportionalRect(screenWidth, screenHeight, 0.945, 0.078125, 0.99, 0.703125);
		
		keyboardRangeBar.setProportionalPosition(pRangeBarSpace);
		
//		1: 0.078125,	0.6465517241,	0.703125,	0.7399425287
//		2: 0.078125,	0.7399425287,	0.703125,	0.8333333333
//		3: 0.078125,	0.8333333333,	0.703125,	0.7399425287
//		0.8333333333
		
		
		synths.get(0).getKeyboard().setProportionalPosition(pKeyboardSpace1);
		synths.get(1).getKeyboard().setProportionalPosition(pKeyboardSpace2);
		synths.get(2).getKeyboard().setProportionalPosition(pKeyboardSpace3);
		for (int i = 0; i < numStrings; i++) {
			synths.get(i).repositionStringFinal(xStart, xEnd, yLevels[i]);
			synths.get(i).setStringMaxAmp(maxAmp);
			synths.get(i).getString().setMaxY(getyStringMax());
			synths.get(i).getString().setMinY(getyStringMin());
		}
		System.out.println("screenWidth: " + screenWidth + ", screenHeight: "
				+ screenHeight);
	}

	private void calculateComponentDimensions() {
		xStart = (int)((double)screenWidth * 0.055);
		xEnd = (int)((double)screenWidth * 0.75);
		yLevels[0] = (int)((double) screenHeight * .3);
		for (int i = 1; i < numStrings; i++) {
			yLevels[i] = yLevels[i - 1] + 40;
		}
		for (int i = 0; i < numStrings; i++) {
			double spaceHeight = (double) screenHeight * (.125 / 2.0);
			int spaceTop = (int) ((double) screenHeight * .40)
					+ (int) (spaceHeight * i);
			int spaceBottom = (int) ((double) screenHeight * .40)
					+ (int) (spaceHeight * (i + 1));
			Rect waveDisplayPosition = new Rect(
					(int) ((double) screenWidth * .75), spaceTop,
					(int) ((double) screenWidth * .85), spaceBottom);
			System.out.println("setting wave display position i: " + i
					+ ", synths(i): " + synths.get(i));
			synths.get(i).setWaveDisplayPosition(waveDisplayPosition);
		}

		maxAmp = (double) screenHeight * .9;
	}

	public void paintFrame(Canvas c) {
//		sidePanelGraphics.drawButtons(c);
//		sidePanelGraphicsBottom.drawButtons(c);
//		for (CustomButton button : customButtons) {
//			button.draw(c);
//		}
		
		c.drawLine(xStart,  yStringMin, xStart, yStringMax, paint);
		c.drawLine(xEnd, yStringMin, xEnd, yStringMax, paint);
/*		c.drawCircle(xStart, yStringMin, 8, paint);
		c.drawCircle(xStart, yStringMax, 8, paint);
		c.drawCircle(xEnd, yStringMin, 8, paint);
		c.drawCircle(xEnd, yStringMax, 8, paint);*/
		
		int pipeheadRadius = CommonFunctions.getPipeheadRadius(context);
		
//		pipeheadRadius = (int)(screenWidth / 200.0);
		
		Rect sourceRect = new Rect(0, 0, pipeEnd.getWidth(), pipeEnd.getHeight());
		RectF drawSpot1 = new RectF(xStart-pipeheadRadius, yStringMin-pipeheadRadius, xStart + pipeheadRadius, yStringMin + pipeheadRadius);
		RectF drawSpot2 = new RectF(xStart-pipeheadRadius, yStringMax-pipeheadRadius, xStart + pipeheadRadius, yStringMax + pipeheadRadius);
		RectF drawSpot3 = new RectF(xEnd-pipeheadRadius, yStringMin-pipeheadRadius, xEnd + pipeheadRadius, yStringMin + pipeheadRadius);
		RectF drawSpot4 = new RectF(xEnd-pipeheadRadius, yStringMax-pipeheadRadius, xEnd + pipeheadRadius, yStringMax + pipeheadRadius);
		
		c.drawBitmap(pipeEnd, sourceRect, drawSpot1, paint);
		c.drawBitmap(pipeEnd, sourceRect, drawSpot2, paint);
		c.drawBitmap(pipeEnd, sourceRect, drawSpot3, paint);
		c.drawBitmap(pipeEnd, sourceRect, drawSpot4, paint);
		
	
		for (BeadKeyboardSynth synth : synths) {
			synth.paintLayer1(c, context);
		}
		for (BeadKeyboardSynth synth : synths) {
			synth.paintLayer2(c);
		}
		/*
		 * update();
		 */
	}

	public void touchedDown(float x, float y) {
		System.out.println("Touched Down");
		for (BeadKeyboardSynth synth : synths) {
			if (bound == null) {
				bound = synth.handleTouchDown((int) x, (int) y);
			}
		}
		if (bound != null) {
			bound.lock();
			bound.grab();
			if (beadToConnect != null) {
				beadToConnect.connectTo(bound);
				bound.connectTo(beadToConnect);
				beadToConnect.setAwaitingConnection(false);
				beadToConnect = null;
				connectOnRelease = false;
				buttonControl.toggleConnectButton();
			}
		}
	}

	public void dragMoved(float x, float y) {
		if (bound != null) {
			bound.setX((int)x);
			bound.setY((int)y);
			bound.lock();
		} else {
			for (BeadKeyboardSynth synth : synths) {
				synth.handleDrag((int) x, (int) y);
			}
		}
	}

	public void dragDrop(float x, float y) {
		if (bound != null) {
			if (!isLockOnRelease() && !isConnectOnRelease()) {
				bound.unlock();
			}
			bound.release();
			if (isConnectOnRelease()) {
				beadToConnect = bound;
				bound.setAwaitingConnection(true);
			}
		}
		bound = null;
		for (BeadKeyboardSynth synth : synths) {
			synth.handleRelease((int) x, (int) y);
		}
	}
	
	public void takeIndexedSnapshot() {
		for (BeadKeyboardSynth synth : synths) {
			synth.takeIndexedSnapshot(buttonControl.getLastSnapshotTaken());
		}
	}
	
	public void clearIndexedSnapshot() {
		for (BeadKeyboardSynth synth : synths) {
			synth.clearIndexedSnapshot(buttonControl.getCurrentSnapshotSelected());
		}
	}

//	public void respondToButtonClick(View v) {
//		int response = buttonControl.respondToButtonClick(v);
//		if (response == ButtonControl.RESPONSE_LOCK_ON_RELEASE_ALTERED) {
//			setLockOnRelease(buttonControl.isLockOnReleaseSet());
//		} else if (response == ButtonControl.RESPONSE_CONNECT_ON_RELEASE_ALTERED) {
//			setConnectOnRelease(buttonControl.isConnectOnReleaseSet());
//		} else if (response == ButtonControl.RESPONSE_TAKE_SNAPSHOT) {
//			for (BeadKeyboardSynth synth : synths) {
//				synth.takeIndexedSnapshot(buttonControl.getLastSnapshotTaken());
//			}
//		} else if (response == ButtonControl.RESPONSE_SET_BEAT_LENGTH_NONE) {
//			getBeatManager().setActive(false);
//		} else if (response == ButtonControl.RESPONSE_WARN_NO_SNAPSHOT_SLOT_SELECTED) {
//			Toast.makeText(
//					context,
//					"No snapshot slot has been selected\n"
//							+ "Select a slot to store snapshot",
//					Toast.LENGTH_SHORT).show();
//		} else if (response > 0) {
//			getBeatManager().setBeatFraction(buttonControl.getFraction(response));
//			getBeatManager().setActive(true);
//		}
//	}

//	public void respondToKeyboardRelatedButtonClick(View v) {
//		int currentlySelectedSnapshot = noteSnapControl
//				.getCurrentSnapshotSelected();
//		int response = noteSnapControl.respondToButtonClick(v);
//		if (response == NoteSnapControl.RESPONSE_TAKE_SNAPSHOT) {
//			takeKeyboardSnapshot(currentlySelectedSnapshot);
//		} else if (response == NoteSnapControl.RESPONSE_WARN_NO_SNAPSHOT_SLOT_SELECTED) {
//			Toast.makeText(
//					context,
//					"No keyboard snapshot slot has been selected\n"
//							+ "Select a slot to store snapshot",
//					Toast.LENGTH_SHORT).show();
//		} else if (response == NoteSnapControl.RESPONSE_DO_NOTHING) {
//			// Seriously, do nothing.
//		} else {
//			setKeyboardReversionSnapshot(response);
//		}
//	}

	public void takeKeyboardSnapshot(int snapIndex) {
		System.out.println("TAKING NOTE SNAPSHOTS");
		for (BeadKeyboardSynth synth : synths) {
			synth.takeKeyboardSnapshot(snapIndex);
		}
		keyboardSnapshots[snapIndex] = new KeyboardSnapshot();
		keyboardSnapshots[snapIndex].setScale(scaleType);
		keyboardSnapshots[snapIndex].setTonic(tonicNote);
	}
	
	public void clearKeyboardSnapshot(int snapIndex) {
		for (BeadKeyboardSynth synth : synths) {
			synth.clearKeyboardSnapshot(snapIndex);
		}
	}

	public void setKeyboardReversionSnapshot(int snapIndex) {
		if (buttonControl.getCurrentBeatSelected() == ButtonControl.RESPONSE_SET_BEAT_LENGTH_NONE) {
			immediatelyRevertToKeyboardIndex(snapIndex);
		} else {
			keyboardRevesionStatus.prepForReversionAtNextBeat(snapIndex);
		}
	}

	private void immediatelyRevertToKeyboardIndex(int snapIndex) {
		System.out.println("reverting to keyboard index: " + snapIndex);
		for (BeadKeyboardSynth synth : synths) {
			synth.revertToKeyboardSnapshot(snapIndex);
		}
		if (keyboardSnapshots[snapIndex].getTonic() != null) {
			this.tonicNote = keyboardSnapshots[snapIndex].getTonic();
			this.scaleType = keyboardSnapshots[snapIndex].getScale();
		}
	}

	public void setKeyboardRangeBar(KeyboardRangeBar keyboardRangeBar) {
		for (BeadKeyboardSynth synth : synths) {
			synth.setKeyboardRangeBar(keyboardRangeBar);
		}
		this.keyboardRangeBar = keyboardRangeBar;
	}

	public void saveSettingsToFile(String filename) {
		List<RecordableWaveString> allStrings = new ArrayList<RecordableWaveString>();
		List<DynamicKeyboard> keyboards = new ArrayList<DynamicKeyboard>();
		for (BeadKeyboardSynth synth : synths) {
			allStrings.add(synth.getString());
			keyboards.add(synth.getKeyboard());
		}
		List<RecordableBead> allBeads = beadIndexer.getAllBeads();
		SettingsFileWriter fileWriter = new SettingsFileWriter(this.bpm,
				scaleType, tonicNote, numStrings, allStrings, allBeads.size(),
				allBeads, keyboards, bgImageFilename);
		fileWriter.writeToFile(filename, context);
	}

	public void revertToSavedSettings(SavedSettings settings) {
		beadIndexer = settings.getBeadIndexer();
		this.bpm = settings.getBpm();
		this.setScaleInfo(settings.getScaleNote(), settings.getScaleType());
		List<RecordableWaveString> strings = settings.getStrings();
		List<DynamicKeyboard> keyboards = settings.getKeyboards();
		keyboardRangeBar.setMin(keyboards.get(0).getStartPianoKeyNumber());
		keyboardRangeBar.setMax(keyboards.get(0).getEndPianoKeyNumber());
		
		setyStringMin((int)((double)screenHeight * 0.15));
		setyStringMax((int)((double)screenHeight * 0.6));
		for (int i = 0; i < synths.size(); i++) {
			synths.get(i).setString(strings.get(i));
			keyboards.get(i).setKeyboardRangeBar(keyboardRangeBar);
			synths.get(i).setKeyboard(keyboards.get(i));
			synths.get(i).getString().setMaxY(getyStringMax());
			synths.get(i).getString().setMinY(getyStringMin());
			synths.get(i).setCorrectWaveLength();
			keyboards.get(i).setReadyToCheckKeyAlignment();
		}
/*		buttonControl.initPreviouslySetButtons(settings.getNumSnapshots());
		noteSnapControl.initPreviouslySetButtons(settings.getNumNoteSnapshots());*/
	}

	public void setBpm(double bpm) {
		this.bpm = bpm;
		getBeatManager().setBpm(bpm);
	}

	public void addBead() {
		for (BeadKeyboardSynth synth : synths) {
			synth.addBead();
		}
	}

	public void removeBead() {
		for (BeadKeyboardSynth synth : synths) {
			synth.removeBead();
		}
	}

	public double getBpm() {
		return bpm;
	}

	public void setScaleInfo(ScaleNote tonicNote, ScaleType scaleType) {
		this.tonicNote = tonicNote;
		this.scaleType = scaleType;
		for (BeadKeyboardSynth synth : synths) {
			synth.setScaleInfo(tonicNote, scaleType);
		}
	}

	public ScaleType getScaleType() {
		return scaleType;
	}

	public ScaleNote getTonicNote() {
		return tonicNote;
	}

	public void pause() {
//		paused = true;
		for (BeadKeyboardSynth synth : synths) {
			synth.pause();
		}
	}

	public void stop() {
		stopped = true;
		for (BeadKeyboardSynth synth : synths) {
			synth.stop();
		}
	}
	
/*	public ButtonControl getButtonControl() {
		return buttonControl;
	}
	
	public NoteSnapControl getNoteSnapControl() {
		return noteSnapControl;
	}*/
	
	public void setButtonControl(ButtonControl buttonControl) {
		this.buttonControl = buttonControl;
	}

	public void resume() {
		paused = false;
		for (BeadKeyboardSynth synth : synths) {
			synth.resume();
		}
		needsToRestoreButtons = true;
	}

	public AudioFileWriter getRecordingWriter() {
		return recordingWriter;
	}

	public void setRecordingWriter(AudioFileWriter recordingWriter) {
		this.recordingWriter = recordingWriter;
	}
	
	public void switchRecordingButton() {
		for (BeadKeyboardSynth synth : synths) {
			synth.switchRecordingButton();
		}
	}
	
	public void setRecording(boolean recording) {
		for (BeadKeyboardSynth synth : synths) {
			synth.setRecording(recording);
		}
	}

	public String getBgImageFilename() {
		return bgImageFilename;
	}

	public void setBgImageFilename(String bgImageFilename) {
		this.bgImageFilename = bgImageFilename;
	}
	
//	public void setSidePanelGraphics(SidePanelButtonGraphicsManager sidePanelGraphics) {
//		this.sidePanelGraphics = sidePanelGraphics;
//	}
//	
//	public void setSidePanelGraphicsBottom(SidePanelButtonGraphicsManager sidePanelGraphicsBottom) {
//		this.sidePanelGraphicsBottom = sidePanelGraphicsBottom;
//	}

	public int getyStringMax() {
		return yStringMax;
	}

	public void setyStringMax(int yStringMax) {
		this.yStringMax = yStringMax;
	}

	public int getyStringMin() {
		return yStringMin;
	}

	public void setyStringMin(int yStringMin) {
		this.yStringMin = yStringMin;
	}
	
//	public void setCustomButtons(List<CustomButton> customButtons) {
//		this.customButtons = customButtons;
//	}

	public boolean isLockOnRelease() {
		return lockOnRelease;
	}

	public void setLockOnRelease(boolean lockOnRelease) {
		this.lockOnRelease = lockOnRelease;
	}

	public boolean isConnectOnRelease() {
		return connectOnRelease;
	}

	public void setConnectOnRelease(boolean connectOnRelease) {
		this.connectOnRelease = connectOnRelease;
	}

	public BeatManager getBeatManager() {
		return beatManager;
	}

	public void setBeatManager(BeatManager beatManager) {
		this.beatManager = beatManager;
	}
	
	public Bead getTutorialStartBead() {
		return synths.get(0).getTutorialBead();
	}
	
	public Bead getTutorialSecondBead() {
		return synths.get(1).getTutorialBead();
	}
	
	public Point getTutorialKeyboardPoint() {
		return synths.get(0).getKeyboard().getTutorialSubjectPoint();
	}

	public void setRangeBarPosition(ProportionalRect pRangeBarSpace) {
		keyboardRangeBar.setProportionalPosition(pRangeBarSpace);
	}
}
