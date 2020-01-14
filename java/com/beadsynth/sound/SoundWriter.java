package com.beadsynth.sound;

import com.beadsynth.recording.AudioFileInput;

public class SoundWriter {

//	AudioTrack audioTrack;
//	AudioManager audioManager;
	long previousPlayBackHeadPosition;
	boolean audioTrackStarted;

	long numSamplesWritten;
	
	Thread writingThread;
	
//	Thread writingThread;
//	SoundWriterThread writerRunnable;
//	Thread writerThread;
	
	WriterProcess writerProcess;
	AudioFileInput fileInput;

	public static final int buffersize = 2000;

	public SoundWriter() {

		audioTrackStarted = false;
		numSamplesWritten = 0;
		
//		writerRunnable = new SoundWriterThread();
//		writerRunnable.A(audioTrack);
//		writingThread = new Thread(new SoundWriterThread());

	}
	
	public void setAudioFileInput(AudioFileInput fileInput) {
		this.fileInput = fileInput;
	}
	
	public AudioFileInput getFileInput() {
		return writerProcess.getFileInput();
	}
	
	public void switchRecordingButton() {
		writerProcess.switchRecordingButton();
	}
	
	public void setRecording(boolean recording) {
		System.out.println("soundwriter NON PROCESS recording set to: " + recording);
		writerProcess.setRecording(recording);
	}

	public void start() {
//		audioTrack.play();
	}

	public void write(short[] data, int numSamples) {
		numSamplesWritten += numSamples;
		// System.out.println("playbackheadposition: " +
		// audioTrack.getPlaybackHeadPosition());
//		writerRunnable.setData(data);
//		writerRunnable.setNumSamples(numSamples);
		
		if (!audioTrackStarted) {
//			audioTrack.play();
			audioTrackStarted = true;
			writerProcess = new WriterProcess();
			writingThread = new Thread(writerProcess);
			writingThread.start();
			writerProcess.setAudioFileInput(fileInput);
//
//			writingThread.start();
		}

//		final short[] finalData = data;
//		final int finalNumSamples = numSamples;
		
		writerProcess.writeData(data, numSamples);
		
		
//		System.out.println("Audiotrack samplerate: " + audioTrack.getSampleRate());
//		
//		
//		writerThread = new Thread(new Runnable() {
//			public void run() {
//				audioTrack.write(finalData, 0, finalNumSamples);
//			}
//		});
//		writerThread.start();

		// int result = audioTrack.write(data, 0, numSamples);
		// System.out.println("write result: " + result);
//		previousPlayBackHeadPosition = audioTrack.getPlaybackHeadPosition();
	}

	public int getBufferSize() {
		return buffersize;
	}

//	public int getBufferSpace() {
//		long numSamplesPlayed = audioTrack.getPlaybackHeadPosition();
		// System.out.println("numSamplesWritten: " + numSamplesWritten +
		// ", numSamplesPlayed: " + numSamplesPlayed + ", buffersize: " +
		// buffersize
		// + ", bufferspace: " + (buffersize - (int) (numSamplesWritten -
		// numSamplesPlayed)));
		// System.out.println("state: " + audioTrack.getState());
		// System.out.println("playState: " + audioTrack.getPlayState());
//		int bufferspace = buffersize - (int) (numSamplesWritten - numSamplesPlayed);
//		if (bufferspace > (double) buffersize / 2.0) {
//			return (int) ((double) buffersize / 2.0);
//		} else {

//			return (int)((double)bufferspace / 2.0);

//		}
//	}
	
	public void pause() {
//		audioTrack.pause();
		writerProcess.pause();
		try {
			writingThread.join();
		} catch (InterruptedException ie) {
			System.out.println("writing thread interrupted while joining during destruction.");
		}
	}
	
	public void stop() {
//		audioTrack.stop();
		writerProcess.stop();
		try {
			writingThread.join();
		} catch (InterruptedException ie) {
			System.out.println("writing thread interrupted while joining during destruction.");
		}
	}
	
	public void resume() {
		writerProcess.resume();
//		audioTrack.play();
		writingThread = new Thread(writerProcess);
		writingThread.start();
	}

}
