package com.beadsynth.buttons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.ToggleButton;

public class CustomToggleButton extends CustomButton {
	
	Bitmap selectedBackground;
	ToggleButton toggleButton;
	
	public CustomToggleButton(ToggleButton button, Bitmap background, Bitmap clickingBackground, Bitmap selectedBackground) {
		super(button, background, clickingBackground);
		this.selectedBackground = selectedBackground;
		this.toggleButton = button;
	}
	
	@Override
	public void draw(Canvas c) {
		if (toggleButton.isPressed()) {
			c.drawBitmap(clickingBackground, buttonLocation.left, buttonLocation.top, paint);
		} else if (toggleButton.isChecked()){
			c.drawBitmap(selectedBackground, buttonLocation.left, buttonLocation.top, paint);
		} else {
			c.drawBitmap(background, buttonLocation.left, buttonLocation.top, paint);
		};
		
	}

}
