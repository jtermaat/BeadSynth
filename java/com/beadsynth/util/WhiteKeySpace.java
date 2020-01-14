package com.beadsynth.util;

import java.util.List;

import android.graphics.Rect;

public class WhiteKeySpace {
	Rect whiteKey;
	List<Rect> blackKeys;
	
	public WhiteKeySpace(Rect whiteKey, List<Rect> blackKeys) {
		this.whiteKey = whiteKey;
		this.blackKeys = blackKeys;
	}
	
	public boolean contains(int x, int y) {
		boolean contained = whiteKey.contains(x,y);
		for (Rect blackKey : blackKeys) {
			contained = contained && !blackKey.contains(x,y);
		}
		return contained;
	}
}
