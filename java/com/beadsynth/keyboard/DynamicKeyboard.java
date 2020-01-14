package com.beadsynth.keyboard;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.beadsynth.configuration.ScaleNote;
import com.beadsynth.configuration.ScaleType;
import com.beadsynth.util.ProportionalRect;
import com.beadsynth.view.ImageScheme;

public class DynamicKeyboard {

	private Rect position;
	List<KeyboardKey> blackKeys;
	List<KeyboardKey> whiteKeys;
	List<KeyboardKey> allPossibleKeys;
	KeyboardKey selectedKey;
	private int startPianoKeyNumber;
	private int endPianoKeyNumber;

	private KeyboardSnapshot[] snapshots;
	public static int MAX_SNAPSHOTS = 8;
	int numSnapshots;
	
	boolean initialized;
	boolean readyToCheckKeyAlignment;

	KeyboardRangeBar rangeBar;

	private ScaleNote scaleTonic;
	private ScaleType scaleType;

	boolean isSelectedForDrag;

	public static int NUM_KEYS = 64;
	public static int SAMPLE_RATE = 22050; // 22050
	public static int DEFAULT_START_KEY = 27;
	public static int DEFAULT_END_KEY = 51;

	public DynamicKeyboard(ProportionalRect pRect, ImageScheme scheme,
			int startPianoKeyNumber, int endPianoKeyNumber) {
				this(pRect.getRect(), scheme, startPianoKeyNumber, endPianoKeyNumber);
			}
	
	public DynamicKeyboard(Rect position, ImageScheme scheme) {
		this(position, scheme, 27, 39);
	}
	
	public DynamicKeyboard(ProportionalRect pRect, ImageScheme scheme) {
		this(pRect.getRect(), scheme);
	}

	public DynamicKeyboard(Rect position, ImageScheme scheme,
			int startPianoKeyNumber, int endPianoKeyNumber) {

		initialized = false;
		readyToCheckKeyAlignment = true;
		setSnapshots(new KeyboardSnapshot[MAX_SNAPSHOTS]);
		for (int i = 0; i < MAX_SNAPSHOTS; i++) {
			snapshots[i] = new KeyboardSnapshot();
		}
		numSnapshots = 0;

		this.setStartPianoKeyNumber(startPianoKeyNumber);
		this.setEndPianoKeyNumber(endPianoKeyNumber);

		this.setPosition(position);
		allPossibleKeys = new ArrayList<KeyboardKey>();
		for (int i = 0; i < NUM_KEYS; i++) {
			boolean isBlack = false;
			int remainder = i % 12;
			if (remainder == 11 || remainder == 1 || remainder == 4
					|| remainder == 6 || remainder == 9) {
				isBlack = true;
			}
			ScaleNote note = ScaleNote.getDefaultByChromaticNumber(i);
			if (isBlack) {
				allPossibleKeys.add(new KeyboardKey(null, scheme
						.getKeyboardBlackKeyImage(), scheme
						.getKeyboardBlackKeySelectedImage(), getFrequency(i),
						isBlack, note, i));
			} else {
				allPossibleKeys.add(new KeyboardKey(null, scheme
						.getKeyboardWhiteKeyImage(), scheme
						.getKeyboardWhiteKeySelectedImage(), getFrequency(i),
						isBlack, note, i));
			}
		}
		setScaleTonic(ScaleNote.A);
		setScaleType(ScaleType.CHROMATIC);
		isSelectedForDrag = false;
		reshapeKeysToCurrentNumber();
		initialized = true;
	}

	private double getFrequency(int pianoKeyNumber) {
		// increment piano key number by 1 to fit on actual piano key numbering
		System.out.println("piano key number: " + pianoKeyNumber
				+ ", frequency: "
				+ Math.pow(2.0, ((double) (pianoKeyNumber + 1) - 49.0) / 12.0)
				* 440.0);
		System.out
				.println("wavelength: "
						+ (double) SAMPLE_RATE
						/ (Math.pow(2.0,
								((double) (pianoKeyNumber + 1) - 49.0) / 12.0) * 440.0));
		return Math.pow(2.0, ((double) (pianoKeyNumber + 1) - 49.0) / 12.0) * 440.0;
	}

	private void reshapeKeysToCurrentNumber() {
		List<KeyboardKey> allVisibleKeys = new ArrayList<KeyboardKey>();
		allVisibleKeys = new ArrayList<KeyboardKey>(allPossibleKeys.subList(
				getStartPianoKeyNumber(), getEndPianoKeyNumber()));
		if (allVisibleKeys.get(0).isBlack()) {
			allVisibleKeys.remove(0);
		}
		if (allVisibleKeys.get(allVisibleKeys.size() - 1).isBlack()) {
			allVisibleKeys
					.remove(allVisibleKeys.get(allVisibleKeys.size() - 1));
		}
		blackKeys = new ArrayList<KeyboardKey>();
		whiteKeys = new ArrayList<KeyboardKey>();

		for (int i = 0; i < allVisibleKeys.size(); i++) {
			if (allVisibleKeys.get(i).isBlack()) {
				blackKeys.add(allVisibleKeys.get(i));
			} else {
				whiteKeys.add(allVisibleKeys.get(i));
			}
		}
		double whiteKeyWidth = ((double) getPosition().width() / (double) whiteKeys
				.size());
		double blackKeyWidth = ((double) whiteKeyWidth * 0.6);
		int whiteKeyHeight = getPosition().height();
		int blackKeyHeight = (int) ((double) getPosition().height() * 0.6);
		int keyTop = getPosition().top;

		// Set black key half-revealed if it's the first visible key.
		int startWhiteKeys = 0;
		if (allVisibleKeys.get(0).isBlack()) {
			// HANDLE
			startWhiteKeys = 1;
		}
		int numWhiteKeysTraversed = 0;
		for (int i = startWhiteKeys; i < allVisibleKeys.size(); i++) {
			int left = (int) (getPosition().left + (numWhiteKeysTraversed * whiteKeyWidth));
			int right = left + (int) whiteKeyWidth;
			if (allVisibleKeys.get(i).isBlack()) {
				allVisibleKeys.get(i).reposition(
						new Rect(left - (int) ((double) blackKeyWidth / 2.0),
								keyTop, left
										+ (int) ((double) blackKeyWidth / 2.0),
								keyTop + blackKeyHeight));
			} else {
				allVisibleKeys.get(i).reposition(
						new Rect(left, keyTop, right, keyTop + whiteKeyHeight));
				numWhiteKeysTraversed++;
			}
		}
		if (allVisibleKeys.get(allVisibleKeys.size() - 1).isBlack()) {
			// HANDLE
		}

		// Set all "non visible keys" to have null positions.
		for (int i = 0; i < getStartPianoKeyNumber(); i++) {
			allPossibleKeys.get(i).reposition(null);
		}
		for (int i = getEndPianoKeyNumber(); i < allPossibleKeys.size(); i++) {
			allPossibleKeys.get(i).reposition(null);
		}
	}

	public void draw(Canvas c) {
			checkKeyAlignment();
		Paint p = new Paint();
		for (KeyboardKey whiteKey : whiteKeys) {
			whiteKey.draw(c);
		}
		for (KeyboardKey blackKey : blackKeys) {
			blackKey.draw(c);
		}
		p.setStyle(Paint.Style.STROKE);
		p.setColor(Color.BLACK);
		c.drawRect(getPosition(), p);
		// System.out.println("keyboardDimensions: " + position.toString());
//		 System.out.println("keyboardLeft: " + position.left);
//		 System.out.println("keyboardRight: " + position.right);
//		 System.out.println("keyboardBottom: " + position.bottom);
//		 System.out.println("keyboardTop: " + position.top);
		 
	}

	private void checkKeyAlignment() {
		int selectedMin = rangeBar.getCurrentMin();
		int selectedMax = rangeBar.getCurrentMax();
		if (getStartPianoKeyNumber() != selectedMin
				|| getEndPianoKeyNumber() != selectedMax) {
			setStartPianoKeyNumber(selectedMin);
			setEndPianoKeyNumber(selectedMax);
			reshapeKeysToCurrentNumber();
		}
	}

	public void releaseDrag() {
		isSelectedForDrag = false;
	}

	// returns true if this drag motion changed the selected key.
	public boolean tryDragging(int x, int y) {
		if (isSelectedForDrag) {
			// Black keys lie atop white, try them first.
			for (KeyboardKey blackKey : blackKeys) {
				if (blackKey.coversSelectedArea(x, y)
						&& isValidInScale(blackKey)) {
					if (selectedKey != null) {
						selectedKey.setSelected(false);
					}
					blackKey.setSelected(true);
					selectedKey = blackKey;

					return true;
				}
			}
			for (KeyboardKey whiteKey : whiteKeys) {
				if (whiteKey.coversSelectedArea(x, y)
						&& isValidInScale(whiteKey)) {
					if (selectedKey != null) {
						selectedKey.setSelected(false);
					}
					whiteKey.setSelected(true);
					selectedKey = whiteKey;

					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	// returns true if this actually selected or unselected a key.
	public boolean trySelectingLocation(int x, int y) {
		if (getPosition().contains(x, y)) {
			isSelectedForDrag = true;
		}
		// Black keys lie atop white, try them first.
		for (KeyboardKey blackKey : blackKeys) {
			if (blackKey.coversSelectedArea(x, y) && isValidInScale(blackKey)) {
				blackKey.reverseSelectionState();
				if (blackKey.isSelected()) {
					if (selectedKey != null) {
						selectedKey.setSelected(false);
					}
					selectedKey = blackKey;
				} else {
					selectedKey = null;
					isSelectedForDrag = false; // Don't allow dragging after
												// unselecting a key
				}
				return true;
			}
		}
		for (KeyboardKey whiteKey : whiteKeys) {
			if (whiteKey.coversSelectedArea(x, y) && isValidInScale(whiteKey)) {
				whiteKey.reverseSelectionState();
				if (whiteKey.isSelected()) {
					if (selectedKey != null) {
						selectedKey.setSelected(false);
					}
					selectedKey = whiteKey;
				} else {
					selectedKey = null;
					isSelectedForDrag = false;
				}
				return true;
			}
		}
		return false;
	}

	public boolean isKeySelected() {
		return selectedKey != null;
	}

	public double getFrequency() {
		if (selectedKey != null) {
			System.out.println("selectedKey: " + selectedKey);
			System.out.println("selectedKey frequency: " + selectedKey.getFrequency());
			return selectedKey.getFrequency();
		} else {
			return 0.0;
		}
	}

	public double getWaveLength() {
		double frequency = getFrequency();
		if (frequency != 0.0) {
			double waveLength = (int) ((double) SAMPLE_RATE / frequency);
			return waveLength;
		} else {
			return 0.0;
		}
	}

	private boolean isValidInScale(KeyboardKey key) {
		boolean ascending;
		if (selectedKey == null) {
			ascending = true;
		} else {
			ascending = key.isHigherThan(selectedKey);
		}
		System.out.println("FROM ISVALIDINSCALE: scaleType: " + scaleType
				+ ", scaleTonic: " + scaleTonic + ", key: " + key);
		return scaleType.partOfScale(scaleTonic, key.getNote(), ascending);
	}

	public void setIndexedSnapshot(int index) {
		if (index < MAX_SNAPSHOTS) {
			if (index == numSnapshots) {
				numSnapshots++;
			}
			snapshots[index].setKey(selectedKey);
			snapshots[index].setScale(scaleType);
			snapshots[index].setTonic(scaleTonic);
			System.out.println("snapshot " + index + ", scale: "
					+ snapshots[index].getScale() + ", scaleTonic: "
					+ snapshots[index].getTonic());
		}
	}
	
	public void clearIndexedSnapshot(int index) {
		snapshots[index].setCleared(true);
	}

	public void revertToSnapshot(int index) {
		if (snapshots[index].getScale() != null && !snapshots[index].isCleared()) {
			System.out.println("snapshot " + index + ", scale: "
					+ snapshots[index].getScale() + ", scaleTonic: "
					+ snapshots[index].getTonic());
			if (selectedKey != null) {
				selectedKey.setSelected(false);
			}
			selectedKey = snapshots[index].getKey();
			scaleType = snapshots[index].getScale();
			scaleTonic = snapshots[index].getTonic();
			if (selectedKey != null) {
				selectedKey.setSelected(true);
			}
		}
	}

	public ScaleNote getScaleTonic() {
		return scaleTonic;
	}

	public void setScaleTonic(ScaleNote scaleTonic) {
		this.scaleTonic = scaleTonic;
	}

	public ScaleType getScaleType() {
		return scaleType;
	}

	public void setScaleType(ScaleType scaleType) {
		this.scaleType = scaleType;
	}

	public void setKeyboardRangeBar(KeyboardRangeBar rangeBar) {
		this.rangeBar = rangeBar;
	}

	public KeyboardKey getSelectedKey() {
		return selectedKey;
	}

	public KeyboardSnapshot[] getSnapshots() {
		return snapshots;
	}

	public int getNumSnapshots() {
		return numSnapshots;
	}

	public int getStartPianoKeyNumber() {
		return startPianoKeyNumber;
	}

	public void setStartPianoKeyNumber(int startPianoKeyNumber) {
		this.startPianoKeyNumber = startPianoKeyNumber;
		if (initialized) {
			System.out.println("reshaping");
			readyToCheckKeyAlignment = false;
			reshapeKeysToCurrentNumber();
		}
	}

	public int getEndPianoKeyNumber() {
		return endPianoKeyNumber;
	}

	public void setEndPianoKeyNumber(int endPianoKeyNumber) {
		this.endPianoKeyNumber = endPianoKeyNumber;
		if (initialized) {
			readyToCheckKeyAlignment = false;
			System.out.println("reshaping");
			reshapeKeysToCurrentNumber();
		}
	}
	
	public void setReadyToCheckKeyAlignment() {
		readyToCheckKeyAlignment = true;
	}

	public Rect getPosition() {
		return position;
	}
	
	public void setProportionalPosition(ProportionalRect pRect) {
		setPosition(pRect.getRect());
	}

	public void setPosition(Rect position) {
		this.position = position;
	}

	public void setSnapshots(KeyboardSnapshot[] snapshots) {
		this.snapshots = snapshots;
	}

	public KeyboardKey getKeyBasedOnChromaticNumber(int chromaticNumber) {
		KeyboardKey foundKey = null;
		for (KeyboardKey key : allPossibleKeys) {
			if (key.getNote().chromaticNumber() == chromaticNumber) {
				foundKey = key;
			}
		}
		return foundKey;
	}

	public void setSelectedKeyBasedOnChromaticNumber(int chromaticNumber) {
		this.selectedKey = null;
		for (KeyboardKey key : allPossibleKeys) {
			if (key.getNote().chromaticNumber() == chromaticNumber) {
				this.selectedKey = key;
				System.out.println("Setting key!");
			}
		}
	}

	public KeyboardKey getKeyByNumber(int number) {
		for (KeyboardKey key : allPossibleKeys) {
			System.out.println("searching for key number: " + number);
			System.out.println("key.getKeyNumber(): " + key.getKeyNumber());
			if (key.getKeyNumber() == number) {
				System.out.println("returning key!");
				return key;
			}
		}
		return null;
	}

	public void setSelectedKeyByNumber(int number) {
		if (selectedKey != null) {
			selectedKey.setSelected(false);
			selectedKey = null;
		}
		selectedKey = getKeyByNumber(number);
		selectedKey.setSelected(true);
	}
	
	public Point getTutorialSubjectPoint() {
		double xMiddle = position.left + ((double)(position.right - position.left) / 2.0);
		double yTop = position.top;
		return new Point((int)xMiddle, (int)yTop);
	}
}
