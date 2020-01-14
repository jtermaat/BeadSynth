package com.beadsynth.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.beadsynth.configuration.ScaleNote;
import com.beadsynth.configuration.ScaleType;
import com.beadsynth.filestorage.BeadIndexer;
import com.beadsynth.keyboard.DynamicKeyboard;
import com.beadsynth.keyboard.KeyboardRangeBar;
import com.beadsynth.physics.Bead;
import com.beadsynth.physics.RecordableWaveString;
import com.beadsynth.physics.WaveString;
import com.beadsynth.recording.AudioFileInput;
import com.beadsynth.sound.WaveStringWriter;
import com.beadsynth.util.ProportionalRect;

public class BeadKeyboardSynth {

	RecordableWaveString string;
	WaveStringWriter writer;
	Rect waveDisplayPosition;
	private DynamicKeyboard keyboard;
	ImageScheme imageScheme;
	BeadIndexer beadIndexer;
	
	private Paint blackPaint;
	private Paint coloredPaint;
	
	// stored globally to avoid allocating space
	short[] currentArray;
	short[] invertedBackwardArray;
	short[] totalArray;

	public BeadKeyboardSynth(Context context, int stringXStart, int stringXEnd, int stringYLevel, int stringNumBeads, double stringMaxAmplitude,
			Rect waveDisplayPosition, ProportionalRect keyboardPosition, ImageScheme imageScheme, BeadIndexer beadIndexer) {
		
		this.imageScheme = imageScheme;
		blackPaint = new Paint();
		blackPaint.setColor(Color.BLACK);
		blackPaint.setStyle(Style.STROKE);
		coloredPaint = new Paint();
		coloredPaint.setColor(imageScheme.getWaveColor());
		coloredPaint.setStyle(Style.FILL_AND_STROKE);
		setKeyboard(new DynamicKeyboard(keyboardPosition, imageScheme));
		string = new RecordableWaveString(stringXStart, stringXEnd, stringYLevel, stringNumBeads, stringMaxAmplitude, imageScheme, beadIndexer);
		writer = new WaveStringWriter(string);
		this.waveDisplayPosition = waveDisplayPosition;
		this.beadIndexer = beadIndexer;
		
		currentArray = new short[1];
		invertedBackwardArray = new short[1];
		totalArray = new short[1];
		
		writer.setWaveLength((int)getKeyboard().getWaveLength());
		writer.start();
	}
	
	public void update() {
		writer.loadData();
		string.nextFrame();
	}
	
	public void revertToSnapshot(int snapshotNum) {
		string.revertToSnapshot(snapshotNum);
	}
	
	public void repositionStringFinal(int xStart, int xEnd, int yLevel) {
		string.repositionFinal(xStart, yLevel, xEnd);
	}
	
	public void setStringMaxAmp(double maxAmp) {
		string.setMaxAmp(maxAmp);
	}
	
	public void paintLayer2(Canvas c) {
		string.draw(c);
	}
	
	public void paintLayer1(Canvas c, Context context) {
		getKeyboard().draw(c);
		string.drawConnectionLines(c);
		currentArray = string.getRecentForwardArray();
		invertedBackwardArray = string.getRecentInvertedBackwardArray();
		if (totalArray.length != currentArray.length * 2) {
			System.out.println("resizing array");
			totalArray = new short[currentArray.length * 2];
		}
		WaveString.concatArrays(currentArray, invertedBackwardArray, totalArray);
		WaveString.drawWaveArray(totalArray, waveDisplayPosition, c, blackPaint, coloredPaint, context);
	}
	
	public void takeIndexedSnapshot(int index) {
		string.setIndexedSnapshot(index);
	}
	
	public void takeKeyboardSnapshot(int index) {
		getKeyboard().setIndexedSnapshot(index);
	}
	
	public void clearIndexedSnapshot(int index) {
		string.clearIndexedSnapshot(index);
	}
	
	public void clearKeyboardSnapshot(int index) {
		getKeyboard().clearIndexedSnapshot(index);
	}
	
	public void revertToKeyboardSnapshot(int index) {
		getKeyboard().revertToSnapshot(index);
		writer.setWaveLength((int)getKeyboard().getWaveLength());
	}
	
	// Returns a bead if touch location grabs a bead from the string.
	public Bead handleTouchDown(int x, int y) {
		boolean keyboardStateChanged = getKeyboard().trySelectingLocation(x, y);
		if (keyboardStateChanged) {
			writer.setWaveLength((int)getKeyboard().getWaveLength());
		}
		return string.getGrabbedBead(x, y);
	}
	
	// checks if user is dragging across the keyboard.
	public void handleDrag(int x, int y) {
		boolean keyboardStateChanged = getKeyboard().tryDragging(x, y);
		if (keyboardStateChanged) {
			writer.setWaveLength((int)getKeyboard().getWaveLength());
		}
	}
	
	public void setWaveLengthToKeyboard() {
		writer.setWaveLength((int)getKeyboard().getWaveLength());
	}
	
	public void handleRelease(int x, int y) {
		getKeyboard().releaseDrag();
	}
	
	public void setWaveDisplayPosition(Rect waveDisplayPosition) {
		this.waveDisplayPosition = waveDisplayPosition;
	}
	
	public void setKeyboardRangeBar(KeyboardRangeBar keyboardRangeBar) {
		getKeyboard().setKeyboardRangeBar(keyboardRangeBar);
	}
	
	public void setScaleInfo(ScaleNote tonicNote, ScaleType scaleType) {
		getKeyboard().setScaleTonic(tonicNote);
		getKeyboard().setScaleType(scaleType);
	}
	
	public RecordableWaveString getString() {
		return string;
	}
	
	public void setString(RecordableWaveString string) {
		this.string = string;
		writer.setString(string);
	}
	
	public void addBead() {
		this.string.addBead();
	}
	
	public void removeBead() {
		this.string.removeBead();
	}
	
	public void pause() {
		writer.pause();
	}
	
	public void stop() {
		writer.stop();
	}
	
	public void resume() {
		writer.resume();
	}
	
	public void setAudioFileInput(AudioFileInput fileInput) {
		writer.setAudioFileInput(fileInput);
	}
	
	public AudioFileInput getFileInput() {
		return writer.getFileInput();
	}
	
	public void switchRecordingButton() {
		writer.switchRecordingButton();
	}
	
	public void setRecording(boolean recording) {
		writer.setRecording(recording);
	}

	public DynamicKeyboard getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(DynamicKeyboard keyboard) {
		this.keyboard = keyboard;
	}
	
	public void setCorrectWaveLength() {
		writer.setWaveLength((int)getKeyboard().getWaveLength());
	}
	
	public Bead getTutorialBead() {
		return string.getTutorialBead();
	}

}
