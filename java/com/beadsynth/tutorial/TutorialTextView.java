package com.beadsynth.tutorial;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.widget.Button;

public class TutorialTextView {
	
	private String header;
	private String content;
	Button nextButton;
	Button youtubeButton;
	Button websiteLinkButton;
	
	Rect location;
	Rect headerLocation;
	Rect contentLocation;
	
	private Bitmap headerBackground;
	private Bitmap contentBackground;
	
	boolean lowAsPossible;
	boolean leftAsPossible;
	int xBounds;
	int yBounds;
	int screenWidth;
	int screenHeight;
	
	Paint headerPaint;
	Paint contentPaint;
	
	static int HEADER_HEIGHT = 40;
	
	static int HEADER_TEXT_SIZE = 25;
	static int CONTENT_TEXT_SIZE = 15;
	
	public TutorialTextView() {
		headerPaint = new Paint();
		Typeface headerTypeface = Typeface.create("Roboto", Typeface.BOLD);
		headerPaint.setTypeface(headerTypeface);
		headerPaint.setTextSize(HEADER_TEXT_SIZE);
		contentPaint = new Paint();
		Typeface contentTypeface = Typeface.create("Roboto", Typeface.NORMAL);
		contentPaint.setTypeface(contentTypeface);
		contentPaint.setTextSize(CONTENT_TEXT_SIZE);
		
	}
	
	public void init() {
		getRectsFromBounds();
	}
	
	public void draw(Canvas c) {
		c.drawBitmap(headerBackground, null, headerLocation, headerPaint);
		c.drawBitmap(contentBackground, null, contentLocation, contentPaint);
		
	}
	
	private void getRectsFromBounds() {
		// TODO: Calculate size based on text content, etc.
//		Rect availableSpaceRect = getAvailableSpaceRect();
		
		Rect bounds = new Rect();
		contentPaint.getTextBounds(content,0,content.length(),bounds);
		int contentBoxHeight = bounds.height();
		int contentBoxWidth = bounds.width();
		int headerBoxHeight = HEADER_HEIGHT;
		
		int left;
		int right;
		int top;
		int bottom;
		
		if (leftAsPossible) {
			left = xBounds;
			right = xBounds + contentBoxWidth;
		} else {
			right = xBounds;
			left = xBounds - contentBoxWidth;
		}
		if (lowAsPossible) {
			bottom = yBounds;
			top = yBounds - headerBoxHeight - contentBoxHeight;
		} else {
			top = yBounds;
			bottom = yBounds + headerBoxHeight + contentBoxHeight;
		}
		location = new Rect(left, top, right, bottom);
		headerLocation = new Rect(left, top, right, top + headerBoxHeight);
		contentLocation = new Rect(left, top + headerBoxHeight, right, bottom);
	}
	
	// Calculates entire space on the screen where the view can reside.
//	private Rect getAvailableSpaceRect() {
//		int boundsTop;
//		int boundsBottom;
//		int boundsLeft;
//		int boundsRight;
//		
//		if (lowAsPossible) {
//			boundsTop = 0;
//			boundsBottom = yBounds;
//		} else {
//			boundsTop = yBounds;
//			boundsBottom = screenHeight;
//		}
//		
//		if (leftAsPossible) {
//			boundsRight = screenWidth;
//			boundsLeft = xBounds;
//		} else {
//			boundsRight = xBounds;
//			boundsLeft = 0;
//		}
//		
//		return new Rect(boundsLeft, boundsTop, boundsRight, boundsBottom);
//	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Bitmap getHeaderBackground() {
		return headerBackground;
	}

	public void setHeaderBackground(Bitmap headerBackground) {
		this.headerBackground = headerBackground;
	}

	public Bitmap getContentBackground() {
		return contentBackground;
	}

	public void setContentBackground(Bitmap contentBackground) {
		this.contentBackground = contentBackground;
	}

}
