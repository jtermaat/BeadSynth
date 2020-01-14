package com.beadsynth.keyboard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.widget.ToggleButton;

import com.beadsynth.buttons.ButtonBeatLengthGraphicsManager;
import com.beadsynth.util.CommonFunctions;
import com.beadsynth.view.R;

public class NoteSnapControl {
	
	Activity activity;
	int takeSnapshotButtonId;
	int clearSnapshotButtonId;
	int[] snapshotSelectionIds;
	int currentlySelectedSnapshot;
	int lastSnapshotTaken;
	int numSnapshotsTaken;
	
	ButtonBeatLengthGraphicsManager graphicsManager;
	
	public static int NUM_SNAPSHOTS = 8;
	public static int NO_SELECTION = -1;	
	public static int RESPONSE_WARN_NO_SNAPSHOT_SLOT_SELECTED = -2;
	public static int RESPONSE_TAKE_SNAPSHOT = -3;
	public static int RESPONSE_DO_NOTHING = -4;
	public static int RESPONSE_WARN_NO_SNAPSHOT_SLOT_TO_CLEAR = -5;
	public static int RESPONSE_CLEAR_SNAPSHOT = -6;
	
	

	public NoteSnapControl(Activity activity) {
		this.activity = activity;
		graphicsManager = new ButtonBeatLengthGraphicsManager(activity.getApplicationContext(), "buttons_top_bg", "buttons_top_bg", "buttons_top_bg");
		initData();
		initButtons();
	}
	
	public void initData() {
		snapshotSelectionIds = new int[NUM_SNAPSHOTS];
		snapshotSelectionIds[0] = R.id.note_snap_1;
		snapshotSelectionIds[1] = R.id.note_snap_2;
		snapshotSelectionIds[2] = R.id.note_snap_3;
		snapshotSelectionIds[3] = R.id.note_snap_4;
		snapshotSelectionIds[4] = R.id.note_snap_5;
		snapshotSelectionIds[5] = R.id.note_snap_6;
		snapshotSelectionIds[6] = R.id.note_snap_7;
		snapshotSelectionIds[7] = R.id.note_snap_8;
		takeSnapshotButtonId = R.id.keyboard_snapshot_button;
		clearSnapshotButtonId = R.id.keyboard_clear_button;
		
		currentlySelectedSnapshot = 0;
		lastSnapshotTaken = 0;
	}
	
	public void initButtons() {
		ToggleButton snapshotZero = (ToggleButton) activity.findViewById(snapshotSelectionIds[0]);
		snapshotZero.setChecked(true);
		for (int i = 1; i < NUM_SNAPSHOTS; i++) {
			ToggleButton snapButton = (ToggleButton) activity.findViewById(snapshotSelectionIds[i]);
			snapButton.setEnabled(false);
		}
	}
	
	public int respondToButtonClick(View v) {
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
//					graphicsManager.toggleButton(i);
					if (currentlySelectedSnapshot != i && currentlySelectedSnapshot != -1) {
						graphicsManager.toggleButton(currentlySelectedSnapshot);
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
							return currentlySelectedSnapshot;
						} else {
							snapButton.setChecked(true);
							currentlySelectedSnapshot = i;
							return currentlySelectedSnapshot;
						}
					} 
				} else {
					return RESPONSE_DO_NOTHING;
				}
			}
		}
		return RESPONSE_DO_NOTHING;
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
//				graphicsManager.toggleButton(currentlySelectedSnapshot);
//				graphicsManager.toggleButton(currentlySelectedSnapshot + 1);
				currentlySelectedSnapshot ++;
			}
		}
	}
	
	public void drawGraphics(Canvas c) {
		graphicsManager.draw(c);
	}
	
	public List<Rect> getButtonRects() {
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
		return CommonFunctions.getCombinedRect(getButtonRects());
	}
	

	public int getCurrentSnapshotSelected() {
		return currentlySelectedSnapshot;
	}
	
	public void restoreProperSettings() {
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
	}
}
