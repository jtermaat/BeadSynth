package com.beadsynth.tutorial;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class TutorialView extends View {
	
	Context context;
	TutorialSubjectLocationData subjectLocationData;
	boolean active;
	int subjectNum;
	Activity parentActivity;
	ViewGroup vg;
	
	public TutorialView(Context context, AttributeSet attr) {
		super(context, attr);
		
		this.context = context.getApplicationContext();
		active = false;
		subjectNum = 0;
	}
	
	public void setTutorialSubectLocationData(TutorialSubjectLocationData subjectLocationData) {
		this.subjectLocationData = subjectLocationData;
	}
	
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//    	
//    }
	
	@Override
	public void onDraw(Canvas c) {
		System.out.println("TUTORIAL WOULD DRAW BUT IT'S " + active + " SO IT EITHER ISN'T BASED ON THAT");
		if (active) {
			subjectLocationData.drawArrowToSubject(c, subjectNum);
//			subjectLocationData.testInlateTutorialTextView(parentActivity, vg, this, c);
			subjectLocationData.drawTextView(c, subjectNum);
		}
	}
	
	public void startTutorial(ViewGroup vg, Activity activity) {
		this.vg = vg;
		this.parentActivity = activity;
		subjectNum = 0;
		active = true;
		System.out.println("TUTORIAL STARTING");
		invalidate();
	}
	
	public void nextStep() {
		System.out.println("TUTORIAL DOING ANOTHER THING");
		subjectNum += 1;
		System.out.println("subjectNum: " + subjectNum);
		invalidate();
	}
	
	public void endTutorial() {
		active = false;
		invalidate();
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActivity(Activity activity) {
		this.parentActivity = activity;
	}
}
