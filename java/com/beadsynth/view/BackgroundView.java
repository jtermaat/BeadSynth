package com.beadsynth.view;

import java.util.Currency;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.beadsynth.buttons.SidePanelButtonGraphicsManager;
import com.beadsynth.filestorage.SavedSettings;
import com.beadsynth.keyboard.NoteSnapControl;
import com.beadsynth.util.CommonFunctions;
import com.beadsynth.util.ProportionalRect;

public class BackgroundView extends View {
	Context context;
	Bitmap bg_img;
	Paint p;
	int screenWidth;
	int screenHeight;
	Rect screenRect;
	ProportionalRect topButtonsRect;
	
	// Shared with BeadView
	SoundBoard board;
//	SidePanelButtonGraphicsManager sidePanelGraphics;
//	SidePanelButtonGraphicsManager sidePanelGraphicsBottom;
	ButtonControl buttonControl;
	NoteSnapControl noteSnapControl;
	
	// REMOVING THESE!
//	List<CustomButton> customButtons;
	
	public BackgroundView(Context context, AttributeSet attr) {
		super(context, attr);
		this.context = context.getApplicationContext();
		bg_img = CommonFunctions.loadBitmap("bg_dark_v21", context);
		topButtonsRect = new ProportionalRect(screenWidth, screenHeight);
		p = new Paint();
	}
	
	@Override
	protected void onDraw(Canvas c) {
		screenWidth = c.getWidth();
		screenHeight = c.getHeight();
		screenRect = new Rect(0, 0, screenWidth, screenHeight);
		
		c.drawBitmap(bg_img, null, screenRect, p);
		drawButtonRelatedGraphics(c);
	}
	
	// BUTTON METHODS: TO BE REFACTORED TO SEPARATE VIEW
	
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
		if (topButtonsRect != null) {
			buttonControl.setTopButtonsRect(topButtonsRect);
		}
		buttonControl.drawGraphics(c);
//		sidePanelGraphics.drawButtons(c);
//		sidePanelGraphicsBottom.drawButtons(c);
//		for (CustomButton button : customButtons) {
//			button.draw(c);
//		}
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
		} else if (response == ButtonControl.RESPONSE_CLEAR_SNAPSHOT) {
			board.clearIndexedSnapshot();
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
		// RISKY TEST
		// TRYING NOT TO INVALIDATE
		invalidate();
	}
	
	public void respondToKeyboardRelatedButtonClick(View v) {
		int currentlySelectedSnapshot = noteSnapControl
				.getCurrentSnapshotSelected();
		int response = noteSnapControl.respondToButtonClick(v);
		if (response == NoteSnapControl.RESPONSE_TAKE_SNAPSHOT) {
			board.takeKeyboardSnapshot(currentlySelectedSnapshot);
		} else if (response == NoteSnapControl.RESPONSE_CLEAR_SNAPSHOT) {
			board.clearKeyboardSnapshot(currentlySelectedSnapshot);
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
		
		// RISKY TEST
//		invalidate();
	}
	
//	public List<CustomButton> getCustomButtons() {
//		return customButtons;
//	}
//
//	public void setCustomButtons(List<CustomButton> customButtons) {
//		this.customButtons = customButtons;
////		board.setCustomButtons(customButtons);
//	}
	
//	public void setSidePanelGraphics(SidePanelButtonGraphicsManager sidePanelGraphics) {
////		board.setSidePanelGraphics(sidePanelGraphics);
//		this.sidePanelGraphics = sidePanelGraphics;
//	}
//	
//	public void setSidePanelGraphicsBottom(SidePanelButtonGraphicsManager sidePanelGraphicsBottom) {
////		board.setSidePanelGraphicsBottom(sidePanelGraphicsBottom);
//		this.sidePanelGraphicsBottom = sidePanelGraphicsBottom;
//	}
	
	public void setBoard(SoundBoard board) {
		this.board = board;
	}
	
	public void revertToSavedSettings(SavedSettings settings) {
		buttonControl.initPreviouslySetButtons(settings.getNumSnapshots());
		noteSnapControl.initPreviouslySetButtons(settings.getNumNoteSnapshots());
	}
	
	public void pause() {
		
	}
	
	public void setTopButtonsRect(ProportionalRect topButtonsRect) {
		this.topButtonsRect = topButtonsRect;
		invalidate();
	}

}
