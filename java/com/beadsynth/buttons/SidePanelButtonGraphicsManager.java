package com.beadsynth.buttons;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.Button;
import android.widget.ToggleButton;

import com.beadsynth.util.CommonFunctions;

public class SidePanelButtonGraphicsManager {
	Bitmap background;
	List<Rect> buttonLocations;
	Bitmap[] buttonImagesDisabled;
	Bitmap[] buttonImagesEnabled;
	Bitmap[] buttonImagesSelected;

	Bitmap snapshotImage;
	Bitmap snapshotImagePressed;
	Bitmap clearImage;
	Bitmap clearImagePressed;

	Button[] buttons;

	Bitmap testButtonImage;
	Bitmap active1;
	Bitmap normal1;
	Bitmap disabled1;

	Context context;

	public SidePanelButtonGraphicsManager(Button[] buttons, Context context) {
		this.buttons = buttons;
		testButtonImage = CommonFunctions.loadBitmap("test_button_image",
				context);
		buttonLocations = new ArrayList<Rect>();
		this.context = context;
		alignButtonLocations();
		initializeImages();
	}

	private void alignButtonLocations() {
		for (Button button : buttons) {
			Rect buttonRect = CommonFunctions.getRectFromView(button);
			buttonLocations.add(buttonRect);
		}
	}

	private void initializeImages() {
		// normal1 = CommonFunctions.loadBitmap("beadsnap_1_full", context);
		// active1 = CommonFunctions.loadBitmap("beadsnap_1_active", context);
		// disabled1 = CommonFunctions.loadBitmap("beadsnap_1_inactive",
		// context);

		buttonImagesDisabled = new Bitmap[8];
		buttonImagesEnabled = new Bitmap[8];
		buttonImagesSelected = new Bitmap[8];
		for (int i = 1; i < 9; i++) {
			buttonImagesDisabled[i - 1] = CommonFunctions.loadBitmap(
					"beadsnap_" + i + "_inactive", context);
			buttonImagesEnabled[i - 1] = CommonFunctions.loadBitmap("beadsnap_"
					+ i + "_full", context);
			buttonImagesSelected[i - 1] = CommonFunctions.loadBitmap(
					"beadsnap_" + i + "_active", context);

		}
		snapshotImage = CommonFunctions
				.loadBitmap("snapshot_inactive", context);
		snapshotImagePressed = CommonFunctions.loadBitmap("snapshot_active",
				context);
		clearImage = CommonFunctions.loadBitmap("clearsnapshot_inactive",
				context);
		clearImagePressed = CommonFunctions.loadBitmap("clearsnapshot_active",
				context);

	}

	public void drawButtons(Canvas c) {
		// Temporarily ignore
//		alignButtonLocations();
//		for (int i = 0; i < buttons.length; i++) {
//			drawButton(c, i);
//		}
	}

	public void drawButton(Canvas c, int buttonNum) {
		try {
			// System.out.println("ButtonRect: " +
			// buttonLocations.get(buttonNum));
			Button button = buttons[buttonNum];
			Bitmap image;
			if (button.getClass() == new ToggleButton(context).getClass()) {
				ToggleButton toggleButton = (ToggleButton) button;
				if (toggleButton.isChecked()) {
					image = buttonImagesEnabled[buttonNum];
				} else if (toggleButton.isEnabled()) {
					image = buttonImagesSelected[buttonNum];
				} else {
					image = buttonImagesDisabled[buttonNum];
				}
			} else {
				if (buttonNum == 8) {
					image = snapshotImage;
					if (buttons[8].isPressed()) {
						image = snapshotImagePressed;
					}
				} else {
					image = clearImage;
					if (buttons[9].isPressed()) {
						image = clearImagePressed;
					}
				}

			}

			c.drawBitmap(image, null, buttonLocations.get(buttonNum),
					new Paint());

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

}
