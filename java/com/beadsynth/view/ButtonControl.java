package com.beadsynth.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.widget.ToggleButton;

import com.beadsynth.buttons.ButtonBeatLengthGraphicsManager;
import com.beadsynth.util.CommonFunctions;
import com.beadsynth.util.ProportionalRect;

public class ButtonControl {

	public static int RESPONSE_DO_NOTHING = -5;
	public static int RESPONSE_CONNECT_ON_RELEASE_ALTERED = -4;
	public static int RESPONSE_LOCK_ON_RELEASE_ALTERED = -3;
	public static int RESPONSE_WARN_NO_SNAPSHOT_SLOT_SELECTED = -2;
	public static int RESPONSE_TAKE_SNAPSHOT = -1;
	public static int RESPONSE_WARN_NO_SNAPSHOT_SLOT_TO_CLEAR = -6;
	public static int RESPONSE_CLEAR_SNAPSHOT = -7;
	public static int RESPONSE_SET_BEAT_LENGTH_NONE = 0;
	public static int RESPONSE_SET_BEAT_LENGTH_WHOLE_NOTE = 1;
	public static int RESPONSE_SET_BEAT_LENGTH_HALF_NOTE = 2;
	public static int RESPONSE_SET_BEAT_LENGTH_QUARTER_NOTE = 3;
/*	public static int RESPONSE_SET_BEAT_LENGTH_QUARTER_TRIPLET = 4;*/
	public static int RESPONSE_SET_BEAT_LENGTH_EIGHTH_NOTE = 4;
	public static int RESPONSE_SET_BEAT_LENGTH_EIGTH_TRIPLET = 5;
	public static int RESPONSE_SET_BEAT_LENGTH_SIXTEENTH_NOTE = 6;
	public static int RESPONSE_SET_BEAT_LENGTH_SIXTEENTH_TRIPLET = 7;
//	public static int RESPONSE_SET_BEAT_LENGTH_THIRTYSECOND_NOTE = 9;
	
	public static int NUM_SNAPSHOTS = 8;
	public static int NUM_BEAT_TYPES = 8;
	
	public static double[] BEAT_FRACTIONS = new double[NUM_BEAT_TYPES];
	
	public static double WHOLE_NOTE = 1.0;
	public static double HALF_NOTE = .5;
	public static double QUARTER_NOTE = 0.25;
	public static double QUARTER_TRIPLET = 1.0 / 6.0;
	public static double EIGHTH_NOTE = 1.0 / 8.0;
	public static double EIGHTH_TRIPLET = 1.0 / 12.0;
	public static double SIXTEENTH_NOTE = 1.0 / 16.0;
//	public static double SIXTEENTH_TRIPLET = 1.0 / 24.0;
//	public static double THIRTY_SECOND_NOTE = 1.0 / 32.0;


	
	public static int NO_SELECTION = -1;
	
	ButtonBeatLengthGraphicsManager graphicsManagerBeatLength;
	
	int lockOnReleaseButtonId;
	int connectOnReleaseButtonId;
	int takeSnapshotButtonId;
	int clearSnapshotButtonId;
	int[] snapshotSelectionIds;
	int[] beatFrequencyIds;

	int currentlySelectedSnapshot;
	int currentlySelectedBeat;
	int lastSnapshotTaken;
	
	int numSnapshotsTaken = 0;

	Activity activity;

	public ButtonControl(Activity activity) {
		this.activity = activity;
		graphicsManagerBeatLength = new ButtonBeatLengthGraphicsManager(activity.getApplicationContext(), "note_menu_bg", "note_menu_selected", "note_menu_symbols");
		initData();
		initButtons();
//		graphicsManager.setScreenSpace(getTopButtonSpace());
		
	}

	private void initData() {
		BEAT_FRACTIONS[0] = WHOLE_NOTE;
		BEAT_FRACTIONS[1] = HALF_NOTE;
		BEAT_FRACTIONS[2] = QUARTER_NOTE;
		BEAT_FRACTIONS[3] = QUARTER_TRIPLET;
		BEAT_FRACTIONS[4] = EIGHTH_NOTE;
		BEAT_FRACTIONS[5] = EIGHTH_TRIPLET;
		BEAT_FRACTIONS[6] = SIXTEENTH_NOTE;
//		BEAT_FRACTIONS[6] = SIXTEENTH_TRIPLET;
/*		BEAT_FRACTIONS[7] = THIRTY_SECOND_NOTE;*/
		
		lockOnReleaseButtonId = R.id.lock_button;
		connectOnReleaseButtonId = R.id.connect_button;
		takeSnapshotButtonId = R.id.take_snapshot;
		clearSnapshotButtonId = R.id.clear_snapshot;
		snapshotSelectionIds = new int[NUM_SNAPSHOTS];
		beatFrequencyIds = new int[NUM_BEAT_TYPES];
		numSnapshotsTaken = 0;
		snapshotSelectionIds[0] = R.id.snap_1;
		snapshotSelectionIds[1] = R.id.snap_2;
		snapshotSelectionIds[2] = R.id.snap_3;
		snapshotSelectionIds[3] = R.id.snap_4;
		snapshotSelectionIds[4] = R.id.snap_5;
		snapshotSelectionIds[5] = R.id.snap_6;
		snapshotSelectionIds[6] = R.id.snap_7;
		snapshotSelectionIds[7] = R.id.snap_8;
		beatFrequencyIds[0] = R.id.beat_0;
		beatFrequencyIds[1] = R.id.beat_1;
		beatFrequencyIds[2] = R.id.beat_2;
		beatFrequencyIds[3] = R.id.beat_3;
//		beatFrequencyIds[4] = R.id.beat_4;
		beatFrequencyIds[4] = R.id.beat_4;
		beatFrequencyIds[5] = R.id.beat_5;
		beatFrequencyIds[6] = R.id.beat_6;
		beatFrequencyIds[7] = R.id.beat_7;
//		beatFrequencyIds[9] = R.id.beat_9;

		currentlySelectedSnapshot = 0;
		currentlySelectedBeat = 0;
	}
	
	public static Rect getRectFromView(View v) {
		Rect bounds = new Rect();
		v.getDrawingRect(bounds);
		return bounds;
	}

	private void initButtons() {
		ToggleButton snapshotZero = (ToggleButton) activity.findViewById(snapshotSelectionIds[0]);
		snapshotZero.setChecked(true);
		System.out.println("snapshotZero dimsneions: " + getRectFromView(snapshotZero));
		for (int i = 1; i < NUM_SNAPSHOTS; i++) {
			ToggleButton snapButton = (ToggleButton) activity.findViewById(snapshotSelectionIds[i]);
			snapButton.setEnabled(false);
			System.out.println("snapshot " + i + " dimensions: " + getRectFromView(snapButton));
		}
		ToggleButton beatZero = (ToggleButton) activity.findViewById(beatFrequencyIds[0]);
		beatZero.setChecked(true);
		System.out.println("beatZero dimensions: " + getRectFromView(beatZero));
	}
	
	public void initPreviouslySetButtons(int numSnapshotsTakenPreviously) {
		numSnapshotsTaken = numSnapshotsTakenPreviously;
		for (int i = 0; i < numSnapshotsTaken; i++) {
			ToggleButton snapButton = (ToggleButton) activity.findViewById(snapshotSelectionIds[i]);
			snapButton.setEnabled(true);
		}
		if (numSnapshotsTaken < NUM_SNAPSHOTS) {
			ToggleButton snapButton = (ToggleButton) activity.findViewById(snapshotSelectionIds[numSnapshotsTaken]);
			snapButton.setEnabled(true);			
		}
		
		
		if (currentlySelectedSnapshot != NO_SELECTION) {
			ToggleButton snapButtonSelected = (ToggleButton) activity.findViewById(snapshotSelectionIds[currentlySelectedSnapshot]);
			snapButtonSelected.setChecked(false);			
		}
		if (numSnapshotsTaken > 0) {
			ToggleButton snapButtonSelected = (ToggleButton) activity.findViewById(snapshotSelectionIds[numSnapshotsTaken - 1]);
			snapButtonSelected.setChecked(true);
			currentlySelectedSnapshot = numSnapshotsTaken - 1;
		} else {
			ToggleButton snapButtonSelected = (ToggleButton) activity.findViewById(snapshotSelectionIds[0]);
			snapButtonSelected.setChecked(true);
			currentlySelectedSnapshot = 0;
		}
		for (int i = numSnapshotsTaken + 1;i<NUM_SNAPSHOTS;i++) {
			ToggleButton snapButton = (ToggleButton) activity.findViewById(snapshotSelectionIds[i]);
			snapButton.setEnabled(false);			
		}		
//		if (numSnapshotsTaken > 0) {
//			graphicsManagerBeatLength.setToConfiguration(numSnapshotsTaken - 1);
//		} else {
//			graphicsManagerBeatLength.setToConfiguration(0);
//		}
		graphicsManagerBeatLength.setToConfiguration(currentlySelectedBeat);
	}

	public int respondToButtonClick(View v) {
		if (v.getId() == lockOnReleaseButtonId) {
			return RESPONSE_LOCK_ON_RELEASE_ALTERED;
		}
		if (v.getId() == connectOnReleaseButtonId) {
			return RESPONSE_CONNECT_ON_RELEASE_ALTERED;
		}
		
		if (v.getId() == takeSnapshotButtonId) {
			lastSnapshotTaken = currentlySelectedSnapshot;
			progressSnapshotIfPossible();
			if (currentlySelectedSnapshot == NO_SELECTION) {
				return RESPONSE_WARN_NO_SNAPSHOT_SLOT_SELECTED;
			} else {
				return RESPONSE_TAKE_SNAPSHOT;
			}
		}
		if (v.getId() == clearSnapshotButtonId) {
			if (currentlySelectedSnapshot == NO_SELECTION) {
				return RESPONSE_WARN_NO_SNAPSHOT_SLOT_TO_CLEAR;
			} else {
				return RESPONSE_CLEAR_SNAPSHOT;
			}
		}
		
		// If it's a toggle button, revert the change in state that was automatically caused by the click event.
		ToggleButton buttonClicked = (ToggleButton)v;
		buttonClicked.setChecked(!buttonClicked.isChecked());
		
		for (int i = 0;i<NUM_SNAPSHOTS;i++) {
			if (v.getId() == snapshotSelectionIds[i]) {
				ToggleButton snapButton = (ToggleButton) activity.findViewById(snapshotSelectionIds[i]);
				if (snapButton.isEnabled()) {
					// Toggle graphics
//					graphicsManagerBeatLength.toggleButton(i);
					if (currentlySelectedSnapshot != i && currentlySelectedSnapshot != -1) {
//						graphicsManagerBeatLength.toggleButton(currentlySelectedSnapshot);
					}
					
					if (snapButton.isChecked()) {
						snapButton.setChecked(false);
						currentlySelectedSnapshot = NO_SELECTION;
						return RESPONSE_DO_NOTHING;
					} else {
						if (currentlySelectedSnapshot != NO_SELECTION) {
							ToggleButton currentlySelected = (ToggleButton) activity.findViewById(snapshotSelectionIds[currentlySelectedSnapshot]);
							currentlySelected.setChecked(false);
							snapButton.setChecked(true);
							currentlySelectedSnapshot = i;
							return RESPONSE_DO_NOTHING;
						} else {
							snapButton.setChecked(true);
							currentlySelectedSnapshot = i;
							return RESPONSE_DO_NOTHING;
						}
					} 
				} else {
					return RESPONSE_DO_NOTHING;
				}
			}
		}

		// TODO: Get this to work
		System.out.println("*** Starting search for beat button");
		for (int k = 0;k<NUM_BEAT_TYPES;k++) {
			if (v.getId() == beatFrequencyIds[k]) {
				ToggleButton beatButton = (ToggleButton) activity.findViewById(beatFrequencyIds[k]);
				if (beatButton.isChecked()) {
					beatButton.setChecked(false);
					graphicsManagerBeatLength.toggleButton(k);
					ToggleButton noBeatButton = (ToggleButton) activity.findViewById(beatFrequencyIds[0]);
					noBeatButton.setChecked(true);
					graphicsManagerBeatLength.toggleButton(0);
					currentlySelectedBeat = 0;
				} else {
					// disable previously selected
					for (int j = 0;j<NUM_BEAT_TYPES;j++) {
						ToggleButton beatButtonToCheck = (ToggleButton) activity.findViewById(beatFrequencyIds[j]);
						beatButtonToCheck.setChecked(false);
						graphicsManagerBeatLength.toggleButton(j);
						System.out.println("toggling button: " + j);
					}
					// Prevent getting stuck on 7th button.
					graphicsManagerBeatLength.toggleButton(NUM_BEAT_TYPES-1);
					beatButton.setChecked(true);
					graphicsManagerBeatLength.toggleButton(k);
					System.out.println("toggling button: " + k);
					currentlySelectedBeat = k;
				}
			}

		}
		return currentlySelectedBeat;
	}

	public int getCurrentSnapshotSelected() {
		return currentlySelectedSnapshot;
	}
	
	public int getCurrentBeatSelected() {
		return currentlySelectedBeat;
	}
	
	public int getLastSnapshotTaken() {
		return lastSnapshotTaken;
	}
	
	public void setTopButtonsRect(ProportionalRect topButtonsRect) {
		graphicsManagerBeatLength.positionButtons(topButtonsRect);
	}

	private void progressSnapshotIfPossible() {
		// Can only progress if some slot is selected and it's not the last slot.
		if (currentlySelectedSnapshot != -1 && currentlySelectedSnapshot != NUM_SNAPSHOTS - 1) { 
			ToggleButton nextSnapButton = (ToggleButton) activity.findViewById(snapshotSelectionIds[currentlySelectedSnapshot + 1]);
			// Only progress if the next slot is not enabled, i.e. to move down the list of slots if the user has not repositioned.
			if (!nextSnapButton.isEnabled()) {
				ToggleButton thisSnapButton = (ToggleButton) activity.findViewById(snapshotSelectionIds[currentlySelectedSnapshot]);
				thisSnapButton.setChecked(false);
				nextSnapButton.setEnabled(true);
				nextSnapButton.setChecked(true);
//				graphicsManagerBeatLength.toggleButton(currentlySelectedSnapshot);
//				graphicsManagerBeatLength.toggleButton(currentlySelectedSnapshot + 1);
				System.out.println("increasing numSnapshotsTaken");
				numSnapshotsTaken ++;
				currentlySelectedSnapshot ++;
			}
		}
	}
	
	public double getFraction(int snapButtonNum) {
		return BEAT_FRACTIONS[snapButtonNum - 1];
	}
	
	public boolean hasSelectedSnapshot() {
		return (currentlySelectedSnapshot != NO_SELECTION);
	}
	
	public boolean isLockOnReleaseSet() {
		ToggleButton lockButton = (ToggleButton) activity.findViewById(lockOnReleaseButtonId);
		return lockButton.isChecked();
	}
	
	public boolean isConnectOnReleaseSet() {
		ToggleButton connectButton = (ToggleButton) activity.findViewById(connectOnReleaseButtonId);
		return connectButton.isChecked();
	}
	
	public void drawGraphics(Canvas c) {
		System.out.println("*** Draw graphics called in Button Control.");
		graphicsManagerBeatLength.draw(c);
	}
	
	public List<Rect> getTopButtonRects() {
		ArrayList<Rect> rects = new ArrayList<Rect>();
		View snapshotButton = activity.findViewById(takeSnapshotButtonId);
		Rect snapshotRect = CommonFunctions.getRectFromView(snapshotButton);
		rects.add(snapshotRect);
		for (int i = 0;i<NUM_SNAPSHOTS;i++) {
			View v = activity.findViewById(snapshotSelectionIds[i]);
			Rect newRect = CommonFunctions.getRectFromView(v);		
			rects.add(newRect);
		}
		return rects;
	}
	
	public Rect getTopButtonSpace() {
		return CommonFunctions.getCombinedRect(getTopButtonRects());
	}
	
	public void restoreProperSettings() {
		System.out.println("buttonControl restore proper settings");
		System.out.println("num snapshots taken: " + numSnapshotsTaken);
		// Enable correct buttons
		for (int i = 0; i < numSnapshotsTaken; i++) {
			ToggleButton snapButton = (ToggleButton) activity.findViewById(snapshotSelectionIds[i]);
			snapButton.setEnabled(true);
		}
		if (numSnapshotsTaken < NUM_SNAPSHOTS) {
			ToggleButton snapButton = (ToggleButton) activity.findViewById(snapshotSelectionIds[numSnapshotsTaken]);
			snapButton.setEnabled(true);		
		}		
		
		// Uncheck all buttons and check the correct ones.
		for (int i = 0;i<NUM_SNAPSHOTS;i++) {
			ToggleButton snapButton = (ToggleButton) activity.findViewById(snapshotSelectionIds[i]);
			snapButton.setChecked(false);			
		}				
		if (currentlySelectedSnapshot != NO_SELECTION) {
			ToggleButton snapButton = (ToggleButton) activity.findViewById(snapshotSelectionIds[currentlySelectedSnapshot]);			
			snapButton.setChecked(true);
		}
		
		for (int i = 0;i<NUM_BEAT_TYPES;i++) {
			ToggleButton beatButton = (ToggleButton) activity.findViewById(beatFrequencyIds[i]);
			beatButton.setChecked(false);			
		}				
		if (currentlySelectedBeat != NO_SELECTION) {
			ToggleButton beatButton = (ToggleButton) activity.findViewById(beatFrequencyIds[currentlySelectedBeat]);			
			beatButton.setChecked(true);
		}

	}
	
	// Always sets to false.
	public void toggleConnectButton() {
		ToggleButton connectButton = (ToggleButton) activity.findViewById(connectOnReleaseButtonId);
		connectButton.setChecked(false);
	}
}
