package com.beadsynth.view;

public class ColorScheme {
	private int beadColorRegular;
	private int beadColorSelected;
	private int beadColorLocked;
	private int beadColorConnecting;
	
	private int keyboardBlackKeyColor;
	private int keyboardBlackKeyColorSelected;
	private int keyboardWhiteKeyColor;
	private int keyboardWhiteKeyColorSelected;
	
	public ColorScheme(int beadColorRegular, int beadColorSelected, int beadColorLocked, int beadColorConnecting, int keyboardBlackKeyColor, int keyboardBlackKeyColorSelected, int keyboardWhiteKeyColor, int keyboardWhiteKeyColorSelected) {
		this.setBeadColorRegular(beadColorRegular);
		this.setBeadColorSelected(beadColorSelected);
		this.setBeadColorLocked(beadColorLocked);
		this.setBeadColorConnecting(beadColorConnecting);
		this.setKeyboardBlackKeyColor(keyboardBlackKeyColor);
		this.setKeyboardBlackKeyColorSelected(keyboardBlackKeyColorSelected);
		this.setKeyboardWhiteKeyColor(keyboardWhiteKeyColor);
		this.setKeyboardWhiteKeyColorSelected(keyboardWhiteKeyColorSelected);
	}

	public int getBeadColorRegular() {
		return beadColorRegular;
	}

	public void setBeadColorRegular(int beadColorRegular) {
		this.beadColorRegular = beadColorRegular;
	}

	public int getBeadColorSelected() {
		return beadColorSelected;
	}

	public void setBeadColorSelected(int beadColorSelected) {
		this.beadColorSelected = beadColorSelected;
	}

	public int getBeadColorLocked() {
		return beadColorLocked;
	}

	public void setBeadColorLocked(int beadColorLocked) {
		this.beadColorLocked = beadColorLocked;
	}

	public int getBeadColorConnecting() {
		return beadColorConnecting;
	}

	public void setBeadColorConnecting(int beadColorConnecting) {
		this.beadColorConnecting = beadColorConnecting;
	}

	public int getKeyboardBlackKeyColor() {
		return keyboardBlackKeyColor;
	}

	public void setKeyboardBlackKeyColor(int keyboardBlackKeyColor) {
		this.keyboardBlackKeyColor = keyboardBlackKeyColor;
	}

	public int getKeyboardBlackKeyColorSelected() {
		return keyboardBlackKeyColorSelected;
	}

	public void setKeyboardBlackKeyColorSelected(int keyboardBlackKeyColorSelected) {
		this.keyboardBlackKeyColorSelected = keyboardBlackKeyColorSelected;
	}

	public int getKeyboardWhiteKeyColor() {
		return keyboardWhiteKeyColor;
	}

	public void setKeyboardWhiteKeyColor(int keyboardWhiteKeyColor) {
		this.keyboardWhiteKeyColor = keyboardWhiteKeyColor;
	}

	public int getKeyboardWhiteKeyColorSelected() {
		return keyboardWhiteKeyColorSelected;
	}

	public void setKeyboardWhiteKeyColorSelected(int keyboardWhiteKeyColorSelected) {
		this.keyboardWhiteKeyColorSelected = keyboardWhiteKeyColorSelected;
	}

}
