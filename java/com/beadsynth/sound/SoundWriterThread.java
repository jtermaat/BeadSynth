package com.beadsynth.sound;

import android.media.AudioTrack;

public class SoundWriterThread implements Runnable {

	AudioTrack track;
	short[] data;
	int numSamples;
	boolean stopped;
	
	public SoundWriterThread() {
		stopped = false;
	}

	public void setData(short[] data) {
		this.data = data;
	}

	public void setNumSamples(int numSamples) {
		this.numSamples = numSamples;
	}

	public void setTrack(AudioTrack track) {
		this.track = track;
	}

	public void run() {
		while (!stopped) {
			System.out.println("data: " + data + ", numSamples: " + numSamples + ", track: " + track);
			track.write(data, 0, numSamples);
		}
	}
	
	public void stop() {
		stopped = true;
	}

}
