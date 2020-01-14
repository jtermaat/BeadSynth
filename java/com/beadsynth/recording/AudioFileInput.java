package com.beadsynth.recording;

import com.beadsynth.util.CommonFunctions;

public class AudioFileInput {
	
	long dataLengthSubmitted;
	public static int buffersize = AudioFileWriter.BUFFER_SIZE;
	short[] dataHolder;
	int writingPointerInBuffer;
	int readingPointerInBuffer;
	int dataAvailableToRead;
	
	public AudioFileInput() {
		dataLengthSubmitted = 0;
		dataHolder = new short[buffersize];
		writingPointerInBuffer = 0;
		readingPointerInBuffer = 0;
		dataAvailableToRead = 0;
	}
	
	public int getDataLengthAvailable() {
		return dataAvailableToRead;
	}
	
	public void write(int length, short[] array) {
		if (writingPointerInBuffer + length >= buffersize) {
			int distanceToEndOfBuffer = buffersize - writingPointerInBuffer;
			int remainderToWrite = length - distanceToEndOfBuffer;
			System.arraycopy(array, 0, dataHolder, writingPointerInBuffer, distanceToEndOfBuffer);
			System.arraycopy(array, distanceToEndOfBuffer, dataHolder, 0, remainderToWrite);
		} else {
			System.arraycopy(array, 0, dataHolder, writingPointerInBuffer, length);
		}
		writingPointerInBuffer = (writingPointerInBuffer + length) % buffersize;
		dataAvailableToRead += length;
	}
	
	public void read(int length, short[] array) {
		System.out.println("AudioInput: " + this + ", dataLengthSubmitted: " + dataLengthSubmitted);
		if (readingPointerInBuffer + length >= buffersize) {
			int distanceToEndOfBuffer = buffersize - readingPointerInBuffer;
			int remainderToRead = length - distanceToEndOfBuffer;
			System.arraycopy(dataHolder, readingPointerInBuffer, array, 0, distanceToEndOfBuffer);
			System.arraycopy(dataHolder, 0, array, distanceToEndOfBuffer, remainderToRead);
		} else {
			System.arraycopy(dataHolder, readingPointerInBuffer, array, 0, length);
		}
		readingPointerInBuffer = (readingPointerInBuffer + length) % buffersize;
		dataAvailableToRead -= length;
		dataLengthSubmitted += length;
	}
	
	public void readAndAddOnto(int length, short[] array) {
		if (readingPointerInBuffer + length >= buffersize) {
			int distanceToEndOfBuffer = buffersize - readingPointerInBuffer;
			int remainderToRead = length - distanceToEndOfBuffer;
			arrayAddThird(dataHolder, readingPointerInBuffer, array, 0, distanceToEndOfBuffer);
			arrayAddThird(dataHolder, 0, array, distanceToEndOfBuffer, remainderToRead);
		} else {
			arrayAddThird(dataHolder, readingPointerInBuffer, array, 0, length);
		}
		readingPointerInBuffer = (readingPointerInBuffer + length) % buffersize;
		dataAvailableToRead -= length;
		dataLengthSubmitted += length;
	}
	
	public static void arrayAddThird(short[] src, int srcPos, short[] dst, int dstPos, int length) {
		for (int i = 0; i < length;i++) {
			dst[dstPos + i] = (short)(dst[dstPos + i] + (short)((float)src[srcPos + i] / 3.0));
		}
	}
	
	
	
	

}
