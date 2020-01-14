package com.beadsynth.tutorial;

import android.graphics.Canvas;

public interface TutorialStep {
	
	public void draw(Canvas c);
	
	public TutorialStep getNext();

}
