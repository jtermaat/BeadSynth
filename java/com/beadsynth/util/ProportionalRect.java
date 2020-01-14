package com.beadsynth.util;

import android.graphics.Rect;

public class ProportionalRect {
	int screenWidth;
	int screenHeight;
	private double pTop; // proportionalTop;
	private double pLeft;
	private double pBottom;
	private double pRight;
	
	public ProportionalRect(int screenWidth, int screenHeight, double pTop, double pLeft, double pBottom, double pRight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.setpTop(pTop);
		this.setpLeft(pLeft);
		this.setpBottom(pBottom);
		this.setpRight(pRight);
	}
	
	public ProportionalRect(int screenWidth, int screenHeight) {
		this(screenWidth, screenHeight, 0.0, 0.0, 0.0, 0.0);
	}
	
	public Rect getRect() {
		return new Rect((int)getLeft(), (int)getTop(), (int)getRight(), (int)getBottom());
	}
	
	public double getTop() {
		return pTop * (double)screenHeight;
	}
	
	public double getBottom() {
		return pBottom * (double)screenHeight;
	}
	
	public double getLeft() {
		return pLeft * (double)screenWidth;
	} 
	
	public double getRight() {
		return pRight * (double)screenWidth;
	}

	public double getpTop() {
		return pTop;
	}

	public void setpTop(double pTop) {
		this.pTop = pTop;
	}

	public double getpLeft() {
		return pLeft;
	}

	public void setpLeft(double pLeft) {
		this.pLeft = pLeft;
	}

	double getpBottom() {
		return pBottom;
	}

	void setpBottom(double pBottom) {
		this.pBottom = pBottom;
	}

	double getpRight() {
		return pRight;
	}

	void setpRight(double pRight) {
		this.pRight = pRight;
	}

}
