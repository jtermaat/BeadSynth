package com.beadsynth.sound;

import com.beadsynth.physics.RecordableWaveString;
import com.beadsynth.physics.WaveString;
import com.beadsynth.recording.AudioFileInput;

public class WaveStringWriter {

	int waveLength;
	SoundWriter writer;
	WaveString string;
	boolean inverted; // Indicates if this wave should be written in its
						// inverted form or directly.
	
	short[] waveData;
	short[] reversedData;
	short[] waveDataToWrite;
	short[] completedData;
	boolean active;

	public WaveStringWriter(WaveString string) {
		this.string = string;
		writer = new SoundWriter();
		inverted = false;
		active = true;
		reversedData = new short[1];
		completedData = new short[1];
		waveDataToWrite = new short[SoundWriter.buffersize];
	}

	public void setWaveLength(int waveLength) {
		if (waveLength != 0) {
			this.waveLength = waveLength;
			active = true;
		} else {
			active = false;
		}
	}

	public void start() {
		writer.start();
	}

	public void loadData() {
		// NEED TO MAKE THIS MORE EFFICIENT!!
		string.getShortArrayTwo(waveLength);
		completedData = string.getCompletedArray();
		writer.write(completedData, completedData.length);
		
//		waveData = string.getShortArrayTwo(waveLength);
//		reversedData = string.getRecentInvertedBackwardArray();

//		int bufferspace = writer.getBufferSpace();
////		System.out.println("Bufferspace: " + bufferspace);
//		if (bufferspace > 0) {
//			int numWavesToWrite = (int) ((double) bufferspace / (double) completedData.length);
//
//			// If this writer is not active, simply write the array of 0's to the writer.
//			if (active) {
//
//				for (int i = 0; i < numWavesToWrite; i++) {
//					System.arraycopy(completedData, 0, waveDataToWrite, completedData.length * i, completedData.length);
////					if (inverted) {
////						System.arraycopy(reversedData, 0, waveDataToWrite, waveData.length * i, waveData.length);
////					} else {
////						System.arraycopy(waveData, 0, waveDataToWrite, waveData.length * i, waveData.length);
////					}
////					inverted = !inverted;
//				}
//
//			}
//			writer.write(waveDataToWrite, numWavesToWrite * completedData.length);
//
//		}

	}
	
	public void setString(RecordableWaveString string) {
		this.string = string;
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

}
