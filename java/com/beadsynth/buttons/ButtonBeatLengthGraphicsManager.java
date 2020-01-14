package com.beadsynth.buttons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.beadsynth.util.CommonFunctions;
import com.beadsynth.util.ProportionalRect;

public class ButtonBeatLengthGraphicsManager {
	
	private static int NUM_TOP_BUTTONS = 8;
	private static Rect DEFAULT_TOP_BUTTONS_RECT = new Rect(100, 15, 100 + 751, 15 + 48);
	private static double LENGTH_OF_SELECTION = 93.875;
	private static int[] SELECTION_START_POSITIONS_X = {DEFAULT_TOP_BUTTONS_RECT.left + (int)(LENGTH_OF_SELECTION * 0.0),
		DEFAULT_TOP_BUTTONS_RECT.left + (int)(LENGTH_OF_SELECTION * 1.0),
		DEFAULT_TOP_BUTTONS_RECT.left + (int)(LENGTH_OF_SELECTION * 2.0),
		DEFAULT_TOP_BUTTONS_RECT.left + (int)(LENGTH_OF_SELECTION * 3.0),
		DEFAULT_TOP_BUTTONS_RECT.left + (int)(LENGTH_OF_SELECTION * 4.0),
		DEFAULT_TOP_BUTTONS_RECT.left + (int)(LENGTH_OF_SELECTION * 5.0),
		DEFAULT_TOP_BUTTONS_RECT.left + (int)(LENGTH_OF_SELECTION * 6.0),
		DEFAULT_TOP_BUTTONS_RECT.left + (int)(LENGTH_OF_SELECTION * 7.0)};
	
	private Rect[] buttonLocations;
	
	Rect buttonsRect;
	Bitmap background;
	Bitmap selectedButtonImage;
	Bitmap noteSymbols;
	int selectedButton;
	
	public ButtonBeatLengthGraphicsManager(Context context, String bgImageName, String selectionImageName, String noteSymbolsImageName) {
		buttonsRect = DEFAULT_TOP_BUTTONS_RECT;
		background = CommonFunctions.loadBitmap(bgImageName, context);
		selectedButtonImage = CommonFunctions.loadBitmap(selectionImageName, context);
		noteSymbols = CommonFunctions.loadBitmap(noteSymbolsImageName, context);
		selectedButton = 0;
		buttonLocations = new Rect[NUM_TOP_BUTTONS];
		for (int i = 0; i<NUM_TOP_BUTTONS;i++) {
//			System.out.println("length of array: " + SELECTION_START_POSITIONS_X.length);
//			System.out.println("SELECTION START POSITION X FOR " + i + ": " + SELECTION_START_POSITIONS_X[i]);
			
			buttonLocations[i] = new Rect(SELECTION_START_POSITIONS_X[i], 
					DEFAULT_TOP_BUTTONS_RECT.top, SELECTION_START_POSITIONS_X[i] + (int)LENGTH_OF_SELECTION, DEFAULT_TOP_BUTTONS_RECT.bottom);
			System.out.println("selectionRect: " + buttonLocations[i]);
		}
	}
	
	public void positionButtons(ProportionalRect propButtonsPosition) {
		buttonsRect = propButtonsPosition.getRect();
		buttonLocations = new Rect[NUM_TOP_BUTTONS];
		double lengthOfSelection = (double)(buttonsRect.right - buttonsRect.left) / (double)NUM_TOP_BUTTONS;
		
		int[] startPositionsX = {buttonsRect.left + (int)(lengthOfSelection * 0.0),
				buttonsRect.left + (int)(lengthOfSelection * 1.0),
				buttonsRect.left + (int)(lengthOfSelection * 2.0),
				buttonsRect.left + (int)(lengthOfSelection * 3.0),
				buttonsRect.left + (int)(lengthOfSelection * 4.0),
				buttonsRect.left + (int)(lengthOfSelection * 5.0),
				buttonsRect.left + (int)(lengthOfSelection * 6.0),
				buttonsRect.left + (int)(lengthOfSelection * 7.0)};
		
		
		for (int i = 0; i<NUM_TOP_BUTTONS;i++) {
			buttonLocations[i] = new Rect(startPositionsX[i], 
					buttonsRect.top, startPositionsX[i] + (int)lengthOfSelection, buttonsRect.bottom);
			System.out.println("selectionRect: " + buttonLocations[i]);
		}		
	}
	
	public void toggleButton(int buttonNum) {
		System.out.println("Selected button: " + selectedButton);
		System.out.println("Currently toggling button: " + buttonNum);
		if (selectedButton == buttonNum) {
			selectedButton = 0;
		} else {
			selectedButton = buttonNum;
		}
		System.out.println("Now selected button: " + selectedButton);
	}
	
	public void draw(Canvas c) {
		Paint p = new Paint();
		System.out.println("buttonsRect: " + buttonsRect);
		c.drawBitmap(background, null, buttonsRect, p);
		c.drawBitmap(selectedButtonImage, null,  buttonLocations[selectedButton], p);
		c.drawBitmap(noteSymbols, null, buttonsRect, p);
		System.out.println("selectedButton: " + selectedButton);
		System.out.println("selected button location: " + buttonLocations[selectedButton]);
	}
	
	public void setToConfiguration(int currentSelection) {
		selectedButton = currentSelection;
	}

}
