package com.beadsynth.buttons;

import com.beadsynth.util.CommonFunctions;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.Button;

public class CustomButton {
	Bitmap background;
	Bitmap clickingBackground;
	Button button;
	Rect buttonLocation;
	Paint paint;
	
	public CustomButton(Button button, Bitmap background, Bitmap clickingBackground) {
		this.button = button;
		this.background = background;
		this.clickingBackground = clickingBackground;
		this.paint = new Paint();
		fitToButtonLocation();
	}
	
	
	
	private void fitToButtonLocation() {
		buttonLocation = CommonFunctions.getRectFromView(button);
	}
	
	public void draw(Canvas c) {
		fitToButtonLocation();
		if (button.isPressed()) {
			c.drawBitmap(clickingBackground, buttonLocation.left, buttonLocation.top, paint);
		} else {
			c.drawBitmap(background, buttonLocation.left, buttonLocation.top, paint);
		};
		
	}

}
