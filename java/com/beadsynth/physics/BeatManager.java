package com.beadsynth.physics;

public class BeatManager {
	
	private double bpm;
	private double beatFraction; // i.e. 1/2 for half-notes, 1/4 for quarter notes.
	private long first_beat_time_millis;
	private long time_at_last_check_millis;
	private long beat_duration_millis;
	private long last_beat_time_millis;
	private boolean active;
	
	public BeatManager() {
		bpm = 120;
		beatFraction = .25;
	}
	
	public void start() {
		first_beat_time_millis = System.currentTimeMillis();
		last_beat_time_millis = System.currentTimeMillis();
		time_at_last_check_millis = System.currentTimeMillis();
	}
	
	public double getBpm() {
		return bpm;
	}
	public void setBpm(double bpm) {
		this.bpm = bpm;
		setBeatDurationMillis();
	}
	public double getBeatFraction() {
		return beatFraction;
	}
	public void setBeatFraction(double beatFraction) {
		this.beatFraction = beatFraction;
		setBeatDurationMillis();
	}
	
	public void setBeatDurationMillis() {
		System.out.println((1000.0 * 60.0 * 4.0) + ", bpm: " + bpm + ", beatFraction: " + beatFraction);
		beat_duration_millis = (long)(((1000.0 * 60.0 * 4.0) / bpm) * beatFraction);
		System.out.println("beat_duration_millis: " + beat_duration_millis);
	}
	
	public boolean checkForBeat() {
		if (active) {
			long time = System.currentTimeMillis();
			int numBeatsPassed = (int)((time - first_beat_time_millis) / beat_duration_millis);
			int numBeatsPassedBefore = (int)((time_at_last_check_millis - first_beat_time_millis) / beat_duration_millis);
			time_at_last_check_millis = time;
			if (numBeatsPassed != numBeatsPassedBefore) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	


}
