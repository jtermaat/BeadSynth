package com.beadsynth.keyboard;

import com.beadsynth.configuration.ScaleNote;
import com.beadsynth.configuration.ScaleType;

public class KeyboardSnapshot {
	private KeyboardKey key;
	private ScaleNote tonic;
	private ScaleType scale;
	private boolean cleared;
	
	public KeyboardKey getKey() {
		return key;
	}
	
	public void setKey(KeyboardKey key) {
		this.key = key;
	}
	
	public ScaleNote getTonic() {
		return tonic;
	}
	
	public void setTonic(ScaleNote tonic) {
		this.tonic = tonic;
	}
	
	public ScaleType getScale() {
		return scale;
	}
	
	public void setScale(ScaleType scale) {
		this.scale = scale;
	}

	public boolean isCleared() {
		return cleared;
	}

	public void setCleared(boolean isCleared) {
		this.cleared = isCleared;
	}
}
