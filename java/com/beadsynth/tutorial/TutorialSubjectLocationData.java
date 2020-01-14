package com.beadsynth.tutorial;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.beadsynth.physics.Bead;
import com.beadsynth.view.R;

public class TutorialSubjectLocationData {
	// Need to know total height and width for determining arrow alignment.
	private int screenWidth;
	private int screenHeight;
	
	private Bead tutorialBeadStart;
	private Bead tutorialBeadSecond;
	private Button takeSnapshotButton;
	private Button rhythmSelectButton;
	private Button lockBeadsButton;
	private Button connectBeadsButton;
	private Button addBeadsButton;
	private Button subtractBeadsButton;
	private Point keyboardPointerSpot;
	private Button keyboardSnapshotButton;
	private Button settingsScreenButton;
	private Button recordButton;
	
	private List<Object> subjects;
	
	public static final int ARROW_LENGTH = 250;
	public static final int ARROW_DIST_FROM_SUBJECT = 40;
	
	public TutorialSubjectLocationData(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	public void populateListWithSubjects() {
		subjects = new ArrayList<Object>();
		subjects.add(tutorialBeadStart);
		subjects.add(tutorialBeadSecond);
		subjects.add(takeSnapshotButton);
		subjects.add(rhythmSelectButton);
		subjects.add(lockBeadsButton);
		subjects.add(connectBeadsButton);
		subjects.add(addBeadsButton);
		subjects.add(subtractBeadsButton);
		subjects.add(keyboardPointerSpot);
		subjects.add(settingsScreenButton);
		subjects.add(recordButton);
	}
	
	private double getAngleForSubject(Point subject) {
		double screenCenterX = (double)screenWidth / 2.0;
		double screenCenterY = (double)screenHeight / 2.0;
		double subjectX = (double)subject.x;
		double subjectY = (double)subject.y;
		double angle = Math.atan2((subjectY - screenCenterY),(subjectX - screenCenterX));
		System.out.println("ANGLE: " + angle);
		return angle;
	}
	
	private Point getArrowEndPointForSubject(Point subject) {
		System.out.println("Point SUBJECT: " + subject.x + ", " + subject.y);
		double screenCenterX = (double)screenWidth / 2.0;
		double screenCenterY = (double)screenHeight / 2.0;
		double angle = getAngleForSubject(subject);
		double distFromSubjectX = Math.cos(angle) * ARROW_DIST_FROM_SUBJECT;
		double distFromSubjectY = Math.sin(angle) * ARROW_DIST_FROM_SUBJECT;
		
		System.out.println("arrowEndForSubject: " + distFromSubjectX + ", " + distFromSubjectY);
		return new Point(subject.x - (int)distFromSubjectX, subject.y - (int)distFromSubjectY);
	}
	
	private Point getArrowStartPointForSubject(Point subject) {
		double screenCenterX = (double)screenWidth / 2.0;
		double screenCenterY = (double)screenHeight / 2.0;
		double angle = getAngleForSubject(subject);
		double totalDistanceFromSubject = ARROW_DIST_FROM_SUBJECT + ARROW_LENGTH;
		double distFromSubjectX = Math.cos(angle) * totalDistanceFromSubject;
		double distFromSubjectY = Math.sin(angle) * totalDistanceFromSubject;
		return new Point(subject.x - (int)distFromSubjectX, subject.y - (int)distFromSubjectY);
	}
	
	private Point getPointForSubject(Object subject) {
		int x = 0;
		int y = 0;
		System.out.println("determining if assignable.");
		if (Bead.class.isAssignableFrom(subject.getClass())) {
			System.out.println("is assignable from Bead");
			Bead bead = (Bead)subject;
			x = (int)bead.getX();
			y = (int)bead.getY();
		} else if (Button.class.isAssignableFrom(subject.getClass())) {
//			System.out.println("is assignable from Button");
//			Button button = (Button)subject;
//			Rect buttonRect = new Rect();
//			button.getDrawingRect(buttonRect);
//			x = buttonRect.centerX();
//			y = buttonRect.centerY();
//			x = button.getLeft();
//			y = button.getTop();
			Button button = (Button)subject;
			Point buttonPoint = getButtonCenterPoint(button);
			x = buttonPoint.x;
			y = buttonPoint.y;
		} else if (Point.class.isAssignableFrom(subject.getClass())) {
			System.out.println("is assignable from Point");
			Point p = (Point)subject;
			x = p.x;
			y = p.y;
		}
		Point output = new Point(x,y);
		return output;
	}
	
	private Point getButtonCenterPoint(Button b) {
		int top = b.getTop();
		int bottom = b.getBottom();
		int left = b.getLeft();
		int right = b.getRight();
		int middleY = (int)((double)(bottom - top)/2.0 + top);
		int middleX = (int)((double)(right - left)/2.0 + left);
		return new Point(middleX, middleY);
	}
	
	private void drawArrowToSubject(Canvas c, Object subject) {
		System.out.println("About to draw Arrow to subject. STEP 2");
		Point subjectPoint = getPointForSubject(subject);
		System.out.println("SubjectPoint: " + subjectPoint);
		Point arrowEnd = getArrowEndPointForSubject(subjectPoint);
		Point arrowStart = getArrowStartPointForSubject(subjectPoint);
		fillArrow(c, (float)arrowStart.x, (float)arrowStart.y, (float)arrowEnd.x, (float)arrowEnd.y);
	}
	
	public void drawArrowToSubject(Canvas c, int subjectNum) {
		System.out.println("About to draw Arrow to subject. STEP 1");
		Object subject = subjects.get(subjectNum);
		System.out.println("DRAWING: Subject Num: " + subjectNum + ", subject: " + subject);
		drawArrowToSubject(c, subject);
	}
	
	private void inflateTutorialTextView(Activity activity, ViewGroup mainView, TutorialView tutorialView, Canvas c, String header, String content, int top, int left, int width, int height) {
//		View instructionsView = activity.getLayoutInflater().inflate(R.layout.tutorial_text_view, mainView);
//		((TextView)instructionsView.findViewById(R.id.tutorial_content_text)).setText(content);
//		((TextView)instructionsView.findViewById(R.id.tutorial_header_text)).setText(header);
//		LayoutParams params = new RelativeLayout.LayoutParams(width, height);
//		params.leftMargin = left;
//		params.topMargin = top;
//		mainView.removeView(mainView);
//		mainView.addView(instructionsView, params);
//		tutorialView.findViewById(R.id.po)
		
	}
	
//	public void testInlateTutorialTextView(Activity activity, ViewGroup mainView, View tutorialView, Canvas c) {
//		inflateTutorialTextView(activity, mainView, tutorialView, c, "Test Header", 
//				"Cookie cheesecake jelly cake. Powder unerdwear.com candy canes. Cotton candy sweet roll muffin dessert dessert liquorice halvah candy canes toffee. Dragée sesame snaps jujubes chocolate bar powder bear claw dessert donut apple pie. Tart chocolate sweet roll candy canes pudding lollipop tiramisu cupcake soufflé. Bonbon cookie jujubes marshmallow ice cream chocolate. Liquorice jelly soufflé. Dessert jelly beans bear claw.",
//				100, 100, 500, 500);
//	}
	
	// TEMP METHOD FOR DRAWING ARROW
	private void fillArrow(Canvas canvas, float x0, float y0, float x1, float y1) {
		System.out.println("about to draw dimensions: (" + x0 + ", " + y0 + "); (" + x1 + ", " + y1 + ")");
		
		Paint paint = new Paint();
		paint.setColor(Color.RED);
	    paint.setStyle(Paint.Style.FILL);
	    
		double screenCenterX = (double)screenWidth / 2.0;
		double screenCenterY = (double)screenHeight / 2.0;
		canvas.drawCircle((float)screenCenterX, (float)screenCenterY, (float)5.0, paint);

	    float deltaX = x1 - x0;
	    float deltaY = y1 - y0;
	    float frac = (float) 0.1;

	    float point_x_1 = x0 + (float) ((1 - frac) * deltaX + frac * deltaY);
	    float point_y_1 = y0 + (float) ((1 - frac) * deltaY - frac * deltaX);

	    float point_x_2 = x1;
	    float point_y_2 = y1;

	    float point_x_3 = x0 + (float) ((1 - frac) * deltaX - frac * deltaY);
	    float point_y_3 = y0 + (float) ((1 - frac) * deltaY + frac * deltaX);

	    Path path = new Path();
	    path.setFillType(Path.FillType.EVEN_ODD);

	    path.moveTo(point_x_1, point_y_1);
	    path.lineTo(point_x_2, point_y_2);
	    path.lineTo(point_x_3, point_y_3);
	    path.lineTo(point_x_1, point_y_1);
	    path.lineTo(point_x_1, point_y_1);
	    path.close();

	    canvas.drawPath(path, paint);
	    canvas.drawLine(x0, y0, x1, y1, paint);
	}
	
	public void drawTextView(Canvas c, int subjectNum) {
		
		
	}
	
	
/*	public static Rect getArrowLocationForSubjectPoint(Point subject) {
		
	}*/
	

	public Bead getTutorialBeadStart() {
		return tutorialBeadStart;
	}
	public void setTutorialBeadStart(Bead tutorialBeadStart) {
		this.tutorialBeadStart = tutorialBeadStart;
	}
	public Bead getTutorialBeadSecond() {
		return tutorialBeadSecond;
	}
	public void setTutorialBeadSecond(Bead tutorialBeadSecond) {
		this.tutorialBeadSecond = tutorialBeadSecond;
	}
	public Button getTakeSnapshotButton() {
		return takeSnapshotButton;
	}
	public void setTakeSnapshotButton(Button takeSnapshotButton) {
		this.takeSnapshotButton = takeSnapshotButton;
	}
	public Button getRhythmSelectButton() {
		return rhythmSelectButton;
	}
	public void setRhythmSelectButton(Button rhythmSelectButton) {
		this.rhythmSelectButton = rhythmSelectButton;
	}
	public Button getLockBeadsButton() {
		return lockBeadsButton;
	}
	public void setLockBeadsButton(Button lockBeadsButton) {
		this.lockBeadsButton = lockBeadsButton;
	}
	public Button getConnectBeadsButton() {
		return connectBeadsButton;
	}
	public void setConnectBeadsButton(Button connectBeadsButton) {
		this.connectBeadsButton = connectBeadsButton;
	}
	public Button getAddBeadsButton() {
		return addBeadsButton;
	}
	public void setAddBeadsButton(Button addBeadsButton) {
		this.addBeadsButton = addBeadsButton;
	}
	public Button getSubtractBeadsButton() {
		return subtractBeadsButton;
	}
	public void setSubtractBeadsButton(Button subtractBeadsButton) {
		this.subtractBeadsButton = subtractBeadsButton;
	}
	public Point getKeyboardPointerSpot() {
		return keyboardPointerSpot;
	}
	public void setKeyboardPointerSpot(Point keyboardPointerSpot) {
		this.keyboardPointerSpot = keyboardPointerSpot;
	}
	public Button getKeyboardSnapshotButton() {
		return keyboardSnapshotButton;
	}
	public void setKeyboardSnapshotButton(Button keyboardSnapshotButton) {
		this.keyboardSnapshotButton = keyboardSnapshotButton;
	}
	public Button getSettingsScreenButton() {
		return settingsScreenButton;
	}
	public void setSettingsScreenButton(Button settingsScreenButton) {
		this.settingsScreenButton = settingsScreenButton;
	}
	public Button getRecordButton() {
		return recordButton;
	}
	public void setRecordButton(Button recordButton) {
		this.recordButton = recordButton;
	}

}
