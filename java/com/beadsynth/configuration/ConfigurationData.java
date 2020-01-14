package com.beadsynth.configuration;

public class ConfigurationData {
	private double bpm;
	private ScaleNote tonicNote;
	private ScaleType scaleType;
	
	public ConfigurationData(double bpm, ScaleNote tonicNote, ScaleType scaleType) {
		this.setBpm(bpm);
		this.setTonicNote(tonicNote);
		this.setScaleType(scaleType);
	}

	public double getBpm() {
		return bpm;
	}

	public void setBpm(double bpm) {
		this.bpm = bpm;
	}

	public ScaleNote getTonicNote() {
		return tonicNote;
	}

	public void setTonicNote(ScaleNote tonicNote) {
		this.tonicNote = tonicNote;
	}

	public ScaleType getScaleType() {
		return scaleType;
	}

	public void setScaleType(ScaleType scaleType) {
		this.scaleType = scaleType;
	}

}
