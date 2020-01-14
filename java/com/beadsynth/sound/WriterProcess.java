package com.beadsynth.sound;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.beadsynth.keyboard.DynamicKeyboard;
import com.beadsynth.recording.AudioFileInput;

public class WriterProcess implements Runnable {
	AudioTrack track;
	short[] dataToWrite;
	int dataToWriteNumSamples;
	short[] queuedData;
	int queuedDataNumSamples;
	public static int buffersize;
	
	AudioFileInput fileWriter;
	boolean recording;

	int numSamplesWritten;
	
	int dataToRecordNumSamples;

	boolean paused;
	boolean stopped;

//	boolean waitingForUpdate;

	boolean newData;

	boolean trackStarted;
	
	boolean testedStartedRecord;

	public WriterProcess() {
		super();
		track = new AudioTrack(AudioManager.STREAM_MUSIC,
				DynamicKeyboard.SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, SoundWriter.buffersize,
				AudioTrack.MODE_STREAM);
		newData = false;
		numSamplesWritten = 0;
		paused = false;
		stopped = false;
		buffersize = com.beadsynth.sound.SoundWriter.buffersize;
		recording = false;
		dataToRecordNumSamples = 0;
//		waitingForUpdate = false;
		
		testedStartedRecord = false;
	}

	public void run() {
//		Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
		track.play();
		while (!stopped && !paused) {
			if (newData) {
				// System.out.println("Reassigning DATA!!!");
				dataToWrite = queuedData;
				dataToWriteNumSamples = queuedDataNumSamples;
				dataToRecordNumSamples += queuedDataNumSamples;
				newData = false;
			}
			double timeOfSamples = samplesToMilliseconds(dataToWriteNumSamples);
			// System.out.println("timeOfSamples in millis: " + timeOfSamples);
			if (dataToWrite != null) {
				// if (numSamplesWritten + dataToWriteNumSamples <= track
				// .getPlaybackHeadPosition() + buffersize) {
//				if (!waitingForUpdate || waitingForUpdate) {
					// System.out.println("Not waiting for update");
					// System.out.println("preWrite:  " +
					// System.currentTimeMillis());
					// track.setPositionNotificationPeriod(10);
					// track.setPlaybackPositionUpdateListener(this);
					track.write(dataToWrite, 0, dataToWriteNumSamples);
					
					if (recording) {
						fileWriter.write(dataToWriteNumSamples, dataToWrite);
						if (!testedStartedRecord) {
//							for (int i = 0;i<dataToWriteNumSamples;i++) {
//								System.out.println("sample written: " + dataToWrite[i]);
//							}
						}
					}
					numSamplesWritten += dataToWriteNumSamples;
					int samplesAheadOfPlaybackHead = numSamplesWritten - track.getPlaybackHeadPosition();
//					double timeAheadOfPlaybackHead = samplesToMilliseconds(samplesAheadOfPlaybackHead);
//					System.out.println("time of soundwave: " + timeOfSamples + ", time ahead of playback head: " + timeAheadOfPlaybackHead + ", samples of soundwave: " + dataToWriteNumSamples + ", samplesAhead " + samplesAheadOfPlaybackHead);
					
					if (samplesAheadOfPlaybackHead > ((double)SoundWriter.buffersize / 2.0)) {
//					if (samplesAheadOfPlaybackHead > ((double)SoundWriter.buffersize - dataToWriteNumSamples)) {
//						System.out.println("sleeping");
						try {
							Thread.sleep((int) timeOfSamples);
						} catch (InterruptedException ie) {
							// do nothing
						}
					}
//					} else {
//						System.out.println("not sleeping");
//					}
//					waitingForUpdate = true;
					// System.out.println("postWrite: " +
					// System.currentTimeMillis());

//				} else {
//					// System.out.println("Yielding");
//					Thread.yield();
//				}
			}
			// if (newData) {
			// // System.out.println("Reassigning DATA!!!");
			// dataToWrite = queuedData;
			// dataToWriteNumSamples = queuedDataNumSamples;
			// newData = false;
			// }
		}
	}

	// public void run() {
	// while (!stopped && !paused) {
	// if (dataToWrite != null) {
	// // NEED TO CHECK FOR SPACE
	// // System.out.println("dataToWritenumSamples: "
	// // + dataToWriteNumSamples);
	// int bufferSpace = getBufferSpace();
	// // int realBufferSpace = getFullBufferSpace();
	// if (bufferSpace > dataToWriteNumSamples) {
	// track.write(dataToWrite, 0, dataToWriteNumSamples);
	// numSamplesWritten += dataToWriteNumSamples;
	// } else {
	// System.out.println("not enough buffer space!");
	// if (newData) {
	// // System.out.println("Reassigning DATA!!!");
	// dataToWrite = queuedData;
	// dataToWriteNumSamples = queuedDataNumSamples;
	// newData = false;
	// }
	// try {
	// Thread.sleep(Math
	// .max(0,
	// getWaitTime(bufferSpace,
	// dataToWriteNumSamples)));
	// } catch (InterruptedException ie) {
	// System.out.println("sound writing thread interrupted");
	// }
	// }
	// }
	// if (newData) {
	// // System.out.println("Reassigning DATA!!!");
	// dataToWrite = queuedData;
	// dataToWriteNumSamples = queuedDataNumSamples;
	// newData = false;
	// }
	// }
	//
	// }

	public void writeData(short[] data, int numSamples) {
		this.queuedData = data;
		this.queuedDataNumSamples = numSamples;
		if (dataToWrite == null) {
			dataToWrite = data;
			dataToWriteNumSamples = numSamples;
		}
		newData = true;
	}

//	private int getFullBufferSpace() {
//		long numSamplesPlayed = track.getPlaybackHeadPosition();
//		int bufferspace = buffersize
//				- (int) (numSamplesWritten - numSamplesPlayed);
//		return bufferspace - 100;
//	}
//
//	private int getBufferSpace() {
//		long numSamplesPlayed = track.getPlaybackHeadPosition();
//		int bufferspace = buffersize
//				- (int) (numSamplesWritten - numSamplesPlayed);
//		// System.out.println("numSamplesPlayed: " + numSamplesPlayed);
//		// System.out.println("bufferspace: " + bufferspace + ", return value: "
//		// + (int) ((double) bufferspace / 2.0));
//		// System.out.println("bufferspace: " + (int) ((double) bufferspace /
//		// 2.0));
//
//		return (int) ((double) bufferspace / 2.0);
//	}

//	private int getWaitTime(int bufferSpace, int samplesToWrite) {
//		int spaceNeeded = samplesToWrite - bufferSpace;
//		System.out.println("wait time: "
//				+ (int) ((double) spaceNeeded
//						/ (double) DynamicKeyboard.SAMPLE_RATE * 1000.0));
//		return (int) ((double) spaceNeeded
//				/ (double) DynamicKeyboard.SAMPLE_RATE * 1000.0);
//	}

	public void pauseWriting() {
		track.pause();
		paused = true;
	}

	public void resumeWriting() {
		track.play();
		paused = false;
	}

	public void pause() {
		track.pause();
		paused = true;
	}

	public void stop() {
		track.stop();
		stopped = true;
	}

	public void resume() {
		track.play();
		paused = false;
	}
	
	public double samplesToMilliseconds(int numSamples) {
		return (double) numSamples
		/ (double) DynamicKeyboard.SAMPLE_RATE * 1000.0;
	}
	
	public void setAudioFileInput(AudioFileInput fileInput) {
		fileWriter = fileInput;
	}
	
	public AudioFileInput getFileInput() {
		return fileWriter;
	}
	
	public void switchRecordingButton() {
		recording = !recording;
	}
	
	public void setRecording(boolean recording) {
		System.out.println("WRITER PROCESS RECORDING SET TO: " + recording);
		this.recording = recording;
	}

//	@Override
//	public void onMarkerReached(AudioTrack track) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onPeriodicNotification(AudioTrack track) {
//		System.out.println("received periodic notification");
//		waitingForUpdate = false;
//	}
}
