package com.beadsynth.keyboard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.beadsynth.configuration.ScaleNote;

public class KeyboardKey {

	Rect position;
	Rect drawingPosition;
	Bitmap standardImage;
	Bitmap selectedImage;
	double noteFrequency;
	boolean selected;
	boolean isBlackKey;
	ScaleNote note;
	private int keyNumber;

	public static double DRAWING_POSITION_RATIO_WHITE = 70.0 / 65.0; // Account for
																// image shadow
	public static double DRAWING_POSITION_RATIO_BLACK = 45.0 / 40.0;

	public KeyboardKey(Rect position, Bitmap standardImage,
			Bitmap selectedImage, double noteFrequency, boolean isBlackKey,
			ScaleNote note, int keyNumber) {
		this.position = position;
		this.drawingPosition = calculateDrawingPosition();
		this.standardImage = standardImage;
		this.selectedImage = selectedImage;
		this.noteFrequency = noteFrequency;
		this.isBlackKey = isBlackKey;
		this.note = note;
		this.keyNumber = keyNumber;
	}

	public void draw(Canvas c) {
		if (position != null) {
			Paint p = new Paint();
			if (selected) {
				c.drawBitmap(selectedImage, null, drawingPosition, p);
			} else {
				c.drawBitmap(standardImage, null, drawingPosition, p);
			}
		}
	}

	public void reposition(Rect newPosition) {
		position = newPosition;
		drawingPosition = calculateDrawingPosition();
	}

	public boolean coversSelectedArea(int x, int y) {
		if (position != null) {
			return position.contains(x, y);
		} else {
			return false;
		}
	}

	public boolean isHigherThan(KeyboardKey other) {
		return (note.chromaticNumber() >= other.getNote().chromaticNumber());
	}

	public double getFrequency() {
		return noteFrequency;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void reverseSelectionState() {
		selected = !selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public boolean isBlack() {
		return isBlackKey;
	}

	public ScaleNote getNote() {
		return note;
	}

	private Rect calculateDrawingPosition() {
		double drawingPositionRatio = DRAWING_POSITION_RATIO_WHITE;
		if (this.isBlackKey) {
			drawingPositionRatio = DRAWING_POSITION_RATIO_BLACK;
		}
		if (position != null) {
			double positionWidth = (double) position.width();
			double newPositionWidth = positionWidth * drawingPositionRatio;
			double widthDifference = newPositionWidth - positionWidth;
			double positionHeight = (double) position.height();
			double newPositionHeight = positionHeight * drawingPositionRatio;
			double heightDifference = newPositionHeight - positionHeight;
			drawingPosition = new Rect(position);
			drawingPosition
					.inset((int) (widthDifference * -1.0 / 2.0), (int) (heightDifference * -1.0 / 2.0));
			return drawingPosition;
		} else {
			return null;
		}
	}

	public int getKeyNumber() {
		return keyNumber;
	}

	public void setKeyNumber(int keyNumber) {
		this.keyNumber = keyNumber;
	}

}
