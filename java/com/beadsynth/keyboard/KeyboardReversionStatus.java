package com.beadsynth.keyboard;

public class KeyboardReversionStatus {
	
	boolean revertAtNextBeat;
	int indexToRevertTo;
	
	public KeyboardReversionStatus() {
		revertAtNextBeat = false;
		indexToRevertTo = 0;
	}
	
	public void prepForReversionAtNextBeat(int snapIndex) {
		indexToRevertTo = snapIndex;
		revertAtNextBeat = true;
	}
	
	public int revertTo() {
		revertAtNextBeat = false;
		return indexToRevertTo;
	}
	
	public boolean readyForReversion() {
		return revertAtNextBeat;
	}

}
