/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beadsynth.physics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.beadsynth.filestorage.BeadIndexer;
import com.beadsynth.util.CommonFunctions;
import com.beadsynth.view.ImageScheme;
import com.beadsynth.view.R;

/**
 * 
 * @author John
 */
public class WaveString extends BeadString {

	private int yLevel;
	private int xStart;
	private int xEnd;
	private double maxAmp;
	private Paint blackPaint;
	private Paint coloredPaint;
	BeadIndexer beadIndexer;
	private int maxY;
	private int minY;
	
	
	// Reusuable unless it's resized;
	short[] storageArray;
	short[] invertedBackwardArray;
	short[] completedArray;

	static int MAX_INT = 2147483647;
	static short MAX_SHORT = 32767;
	static byte MAX_BYTE = (byte) 255;

	// static int MIN_INT = -2147483647;

	public WaveString(int XStart, int XEnd, int YLevel, int NumBeads,
			double MaxAmp, ImageScheme imageScheme, BeadIndexer beadIndexer) {
		super(XStart, YLevel, XEnd, YLevel, NumBeads, false, imageScheme);
		setxStart(XStart);
		setyLevel(YLevel);
		setxEnd(XEnd);
		setMaxAmp(MaxAmp);
		storageArray = new short[1];
		invertedBackwardArray = new short[1];
		completedArray = new short[1];
		this.beadIndexer = beadIndexer;
	}

	public void repositionFinal(int XStart, int YLevel, int XEnd) {
		setxStart(XStart);
		setxEnd(XEnd);
		setyLevel(YLevel);

		double xStep = (XEnd - XStart) / (double) (numBeads - 1);
		for (int i = 0; i < numBeads; i++) {
			beads[i].setX((int)(XStart + (xStep * i)));
			beads[i].setY(getyLevel());
		}
//		getStartBead().setXLocked(true);
//		getEndBead().setXLocked(true);
	}

	public void checkForStringDrag() {
		if (getStartBead().isGrabbed()) {
			adjustYLevel((int)getStartBead().getY());
		} else if (getEndBead().isGrabbed()) {
			adjustYLevel((int)getEndBead().getY());
		}
		
		if (yLevel > maxY) {
			yLevel = maxY;
		} else if (yLevel < minY) {
			yLevel = minY;
		}

		// Start and end beads must always remain locked in place.
		getStartBead().lock();
		getEndBead().lock();
		getStartBead().setY(yLevel);
		getEndBead().setY(yLevel);
		if (getStartBead().getX() < xStart) {
			getStartBead().setX(xStart);
		} else if (getStartBead().getX() > xEnd) {
			getStartBead().setX(xEnd);
		}
		if (getEndBead().getX() > xEnd) {
			getEndBead().setX(xEnd);
		} else if (getEndBead().getX() < xStart) {
			getEndBead().setX(xStart);
		}
		getEndBead().setY(yLevel);
	}

	public void nextFrame() {
		super.nextFrame();
		checkForStringDrag();
	}

	public void adjustYLevel(int newY) {
		setyLevel(newY);
		getStartBead().setY(newY);
		getEndBead().setY(newY);
	}

	public short[] getShortArrayTwo(int ArrayLength) {
		if (storageArray.length != ArrayLength) {
			storageArray = new short[ArrayLength];
		}
		double actualTotalX = getTotalXLength();
		int currentArrayIndex;
		double totalPercentThrough = 0.0;
		int thisBead = 0;
		int nextBead = 1;
		double distanceToThisBead = 0.0;
		double distanceToNextBead = getXLengthToBead(nextBead);
		double thisBeadPercentThrough = 0.0;
		double nextBeadPercentThrough = distanceToNextBead / actualTotalX;
		double percentToNextBead;
		
		// DECLARE THESE IN ADVANCE TO AVOID CONSTANTLY REALLOCATING MEMORY
		double thisY;
		double nextY;
		double yChange;
		double currentY;
		short adjustedY;
		
		for (currentArrayIndex = 0; currentArrayIndex < ArrayLength; currentArrayIndex++) {
			totalPercentThrough = (double) currentArrayIndex
					/ (double) ArrayLength;
			while (totalPercentThrough >= nextBeadPercentThrough) {
				thisBead++;
				nextBead++;
				distanceToThisBead = distanceToNextBead;
				distanceToNextBead = getXLengthToBead(nextBead);
				thisBeadPercentThrough = nextBeadPercentThrough;
				nextBeadPercentThrough = distanceToNextBead / actualTotalX;
			}
			percentToNextBead = (totalPercentThrough - thisBeadPercentThrough)
					/ (nextBeadPercentThrough - thisBeadPercentThrough);
			thisY = beads[thisBead].getY() - getyLevel();
			nextY = beads[nextBead].getY() - getyLevel();
			yChange = nextY - thisY;
			currentY = (yChange * percentToNextBead) + thisY;
			if (Math.abs(currentY) > getMaxAmp())
				adjustedY = MAX_SHORT;
			else
				adjustedY = (short) ((currentY / getMaxAmp()) * (double) MAX_SHORT);
			storageArray[currentArrayIndex] = adjustedY;
		}
		if (invertedBackwardArray.length != storageArray.length) {
			invertedBackwardArray = new short[storageArray.length];
		}
		getInvertedBackward(storageArray, invertedBackwardArray);
		if (completedArray.length != storageArray.length * 2)
		{
			completedArray = new short[storageArray.length * 2];
		}
		concatArrays(storageArray, invertedBackwardArray, completedArray);
		return storageArray;
	}
	
	public short[] getRecentInvertedBackwardArray() {
		return invertedBackwardArray;
	}
	
	public short[] getRecentForwardArray() {
		return storageArray;
	}
	
	public short[] getCompletedArray() {
		return completedArray;
	}

	// Returns true if successfully added bead
	public boolean addBead() {
		if (numBeads < MAX_ALLOWED_BEADS) {
			if (beads[numBeads] == null) {
				beads[numBeads] = new Bead(getxEnd(), getyLevel());
			} else {
				beads[numBeads].revive();
			}
			beads[numBeads].lock();
//			beads[numBeads].setXLocked(true);
			beads[numBeads].connectTo(beads[numBeads - 1]);
			beads[numBeads - 1].connectTo(beads[numBeads]);
			beads[numBeads - 1].unlock();
//			beads[numBeads - 1].setXLocked(false);
			numBeads++;
			return true;
		} else {
			return false;
		}
	}

	public void removeBead() {
		if (numBeads > 2) {
			beads[numBeads - 2].setX(getxEnd());
			beads[numBeads - 2].setY(getyLevel());
			beads[numBeads - 2].lock();
//			beads[numBeads - 2].setXLocked(true);
//			beads[numBeads - 1].setXLocked(false);
			beads[numBeads - 1].destroy();
			numBeads--;
		}
	}

	// returned in absolute value
	private double getTotalXLength() {
		double totalLength = 0.0;
		for (int i = 0; i < numBeads - 1; i++) {
			totalLength += Math
					.abs(beads[i].getXDistanceFromBead(beads[i + 1]));
		}
		return totalLength;
	}

	private double getXLengthToBead(int BeadIndex) {
		double totalLength = 0.0;
		for (int i = 0; i < BeadIndex; i++) {
			totalLength += Math
					.abs(beads[i].getXDistanceFromBead(beads[i + 1]));
		}
		return totalLength;
	}

	public void draw(Canvas c) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		c.drawLine((float) getxStart(), (float) getyLevel(), (float) getxEnd(),
				(float) getyLevel(), paint);
//		c.drawLine((int) getxStart(), (int) (getyLevel() + getMaxAmp()),
//				(int) getxStart(), (int) (getyLevel() - getMaxAmp()), paint);
//		c.drawLine((int) getxEnd(), (int) (getyLevel() + getMaxAmp()),
//				(int) getxEnd(), (int) (getyLevel() - getMaxAmp()), paint);
//		c.drawLine((int) getxStart(), (int) (getyLevel() + getMaxAmp()),
//				(int) getxEnd(), (int) (getyLevel() + getMaxAmp()), paint);
//		c.drawLine((int) getxStart(), (int) (getyLevel() - getMaxAmp()),
//				(int) getxEnd(), (int) (getyLevel() - getMaxAmp()), paint);
		super.draw(c);
	}

	public static void getInvertedBackward(short[] sourceArray, short[] invertedArray) {
		int length = sourceArray.length;
		for (int i = 0; i < length; i++) {
			invertedArray[i] = (short) (sourceArray[length - i - 1] * (short) (-1));
		}
	}

//	public static int[] concatArrays(int[] arr1, int[] arr2) {
//		int[] arr3 = new int[arr1.length + arr2.length];
//		for (int i = 0; i < arr1.length; i++) {
//			arr3[i] = arr1[i];
//		}
//		for (int i = arr1.length; i < arr3.length; i++) {
//			arr3[i] = arr2[i - arr1.length];
//		}
//		return arr3;
//	}

	public static void concatArrays(short[] arr1, short[] arr2, short[] combined) {
		System.arraycopy(arr1, 0, combined, 0, arr1.length);
		System.arraycopy(arr2, 0, combined, arr1.length, arr2.length);
	}

	public static void drawWaveArray(short[] array, Rect space, Canvas c, Paint blackPaint, Paint coloredPaint, Context context) {

		int arrayLength = array.length;
		double xLength = (double) space.width();
		// System.out.println("space.width(): " + space.width());
		double step = xLength / (double) arrayLength;
		double startX = (double) space.left;
		double endX = startX + xLength;

		double topY = (double) space.top;
		double bottomY = topY + (double) space.height();
		double middleY = topY + (bottomY - topY) / 2.0;
		double maxAmp = middleY - topY;

		double percentThrough;

		Path wavePath = new Path();
		wavePath.moveTo((float) startX, (float) middleY);

		// Declare these early to avoid excess memory allocation
		double thisX;
		double nextX;
		double percentNextY;
		double actualNextY;
		
		for (int i = 0; i < arrayLength - 1; i++) {
			percentThrough = (double) i / (double) arrayLength;
			thisX = startX + xLength * percentThrough;
			nextX = thisX + step;
//			double percentThisY = (double) array[i] / (double) MAX_SHORT;
			percentNextY = (double) array[i + 1] / (double) MAX_SHORT;
//			double actualThisY = percentThisY * maxAmp + middleY;
			actualNextY = percentNextY * maxAmp + middleY;
			// System.out.println("thisX: " + thisX + ", float thisX: " +
			// (float)thisX);
			// System.out.println("y: " + (float)actualThisY);
			// System.out.println("actualNextX: " + (float)nextX);
			// c.drawLine((float)thisX, (float)actualThisY, (float)nextX,
			// (float)actualNextY, redPaint);
			wavePath.lineTo((float) nextX, (float) actualNextY);
		}
//		System.out.println("waveFormEndX: " + endX);
//		System.out.println("waveFormYLevel: " + middleY);
		c.drawPath(wavePath, coloredPaint);
		c.drawPath(wavePath, blackPaint);
//		c.drawRect(space, paint);
		c.drawLine((float) startX, (float) middleY, (float) endX,
				(float) middleY, blackPaint);
		
		int pipeheadRadius = CommonFunctions.getPipeheadRadius(context);
		
		Bitmap pipeEnd = CommonFunctions.loadBitmap("pipe_end", context);
		Rect sourceRect = new Rect(0,0,pipeEnd.getWidth(), pipeEnd.getHeight());
		RectF destRect = new RectF((float)(endX-pipeheadRadius), (float)(middleY-pipeheadRadius), (float)(endX+pipeheadRadius), (float)(middleY+pipeheadRadius));
		c.drawBitmap(pipeEnd, sourceRect, destRect, blackPaint);
	}

	// public static void drawWaveArray(int[] array, Rect space, Canvas c) {
	// int arrayLength = array.length;
	// double xLength = space.width();
	// double step = xLength / (double)arrayLength;
	// double startX = space.left;
	// double endX = startX + xLength;
	//
	// double topY = space.top;
	// double bottomY = topY + space.height();
	// double middleY = topY + (bottomY - topY)/2;
	// double maxAmp = middleY - topY;
	//
	//
	// double percentThrough;
	// for (int i = 0;i<arrayLength-1;i++) {
	// percentThrough = (double)i / (double)arrayLength;
	// double thisX = startX + xLength * percentThrough;
	// double nextX = thisX + step;
	// double percentThisY = (double)array[i] / (double)MAX_INT;
	// double percentNextY = (double)array[i+1] / (double)MAX_INT;
	// double actualThisY = percentThisY * maxAmp + middleY;
	// double actualNextY = percentNextY * maxAmp + middleY;
	// c.drawLine((float)thisX, (float)actualThisY, (float)nextX,
	// (float)actualNextY, new Paint());
	// }
	//
	// c.drawRect(space, new Paint());
	// c.drawLine((float)startX, (float)middleY, (float)endX, (float)middleY,
	// new Paint());
	// }

	public void setMaxAmp(double maxAmp) {
		this.maxAmp = maxAmp;
	}

	public int getyLevel() {
		return yLevel;
	}

	public void setyLevel(int yLevel) {
		this.yLevel = yLevel;
	}

	public int getxStart() {
		return xStart;
	}

	public void setxStart(int xStart) {
		this.xStart = xStart;
	}

	public int getxEnd() {
		return xEnd;
	}

	public void setxEnd(int xEnd) {
		this.xEnd = xEnd;
	}

	public double getMaxAmp() {
		return maxAmp;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}
	
	public Bead getTutorialBead() {
		return beads[4];
	}
}
