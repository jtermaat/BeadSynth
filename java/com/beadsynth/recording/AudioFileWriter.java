package com.beadsynth.recording;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import com.beadsynth.util.CommonFunctions;

import android.content.Context;

public class AudioFileWriter {

	long dataSize;
	AudioFileInput[] inputs;
	int numInputs;
	DataOutputStream outFile;
	short[] dataHolder;
	String fullFilename;
	Context context;

	public static int MAX_AUDIO_INPUTS = 10;
	public static int BUFFER_SIZE = 1000000;

	public AudioFileWriter(Context context) {
		dataSize = 0;
		this.inputs = new AudioFileInput[MAX_AUDIO_INPUTS];
		this.numInputs = 0;
		dataHolder = new short[BUFFER_SIZE];
		this.context = context;
	}

	public AudioFileWriter(AudioFileInput[] inputs, int numInputs, Context context) {
		dataSize = 0;
		this.inputs = inputs;
		this.numInputs = numInputs;
		dataHolder = new short[BUFFER_SIZE];
		this.context = context;
	}

	public void attemptWrite() throws IOException {
		int shortestDataAvailable = 1000000;
		for (AudioFileInput input : inputs) {
			if (input.dataAvailableToRead < shortestDataAvailable) {
				shortestDataAvailable = input.dataAvailableToRead;
			}
		}
//		System.out.println("attempting write: shortestDataAvailable: " + shortestDataAvailable);
		inputs[0].read(shortestDataAvailable, dataHolder);
		CommonFunctions.divideArrayContents(dataHolder, 3);
		for (int i = 1; i < numInputs; i++) {
			inputs[i].readAndAddOnto(shortestDataAvailable, dataHolder);
		}
//		for (int i = 0;i<longestDataAvailable;i++) {
//			System.out.println("contents of dataHolder[ "+ i +" ]: " + dataHolder[i]);
//		}

		writeDataToFile(dataHolder, shortestDataAvailable);
	}

	private void writeDataToFile(short[] finalData, int length)
			throws IOException {
//		ByteBuffer byteBuffer = ByteBuffer.allocate(length * 2);
//		ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
//		shortBuffer.put(finalData, 0, length);
//		dataSize += length;
//		outFile.write(byteBuffer.array());
		
		byte[] byteData = new byte[length * 2];
		for (int i = 0;i<length;i++) {
			byte[] twoBytes = shortToByteArray(finalData[i]);
			byteData[i * 2] = twoBytes[0];
			byteData[i * 2 + 1] = twoBytes[1];
		}
		dataSize += byteData.length;
		outFile.write(byteData);
//		System.out.println("outfile write length: " + outFile.);
	}

	public void beginWritingFile(String filename) throws IOException {
//		File dir = context.getFilesDir();
		fullFilename = PrepareRecordingFragment.getMusicStorageDir() + "/" + filename + ".wav";
//		fullFilename = dir.getAbsolutePath() + "/" + filename;
		long mySubChunk1Size = 16;
		int myBitsPerSample = 16;
		int myFormat = 1;
		long myChannels = 1;
		long mySampleRate = 22100;
		long myByteRate = mySampleRate * myChannels * myBitsPerSample / 8;
		int myBlockAlign = (int) (myChannels * myBitsPerSample / 8);

		long myDataSize = 0L;
		long myChunk2Size = myDataSize * myChannels * myBitsPerSample / 8;
		long myChunkSize = 36 + myChunk2Size;

		OutputStream os;
		os = new FileOutputStream(new File(fullFilename));
		BufferedOutputStream bos = new BufferedOutputStream(os);
		outFile = new DataOutputStream(bos);

		outFile.writeBytes("RIFF"); // 00 - RIFF
		outFile.write(intToByteArray((int) myChunkSize), 0, 4); // 04 - how big
																// is the rest
																// of this file?
		outFile.writeBytes("WAVE"); // 08 - WAVE
		outFile.writeBytes("fmt "); // 12 - fmt
		outFile.write(intToByteArray((int) mySubChunk1Size), 0, 4); // 16 - size
																	// of this
																	// chunk
		outFile.write(shortToByteArray((short) myFormat), 0, 2); // 20 - what is
																	// the audio
																	// format? 1
																	// for PCM =
																	// Pulse
																	// Code
																	// Modulation
		outFile.write(shortToByteArray((short) myChannels), 0, 2); // 22 - mono
																	// or
																	// stereo? 1
																	// or 2? (or
																	// 5 or ???)
		outFile.write(intToByteArray((int) mySampleRate), 0, 4); // 24 - samples
																	// per
																	// second
																	// (numbers
																	// per
																	// second)
		outFile.write(intToByteArray((int) myByteRate), 0, 4); // 28 - bytes per
																// second
		outFile.write(shortToByteArray((short) myBlockAlign), 0, 2); // 32 - #
																		// of
																		// bytes
																		// in
																		// one
																		// sample,
																		// for
																		// all
																		// channels
		outFile.write(shortToByteArray((short) myBitsPerSample), 0, 2); // 34 -
																		// how
																		// many
																		// bits
																		// in a
																		// sample(number)?
																		// usually
																		// 16 or
																		// 24
		outFile.writeBytes("data"); // 36 - data
		outFile.write(intToByteArray((int) myDataSize), 0, 4); // 40 - how big
																// is this data
																// chunk
		// 44 - the actual data itself - just a long string of numbers
	}

	// private void properWAV(File fileToConvert, float newRecordingID){
	// try {
	// long mySubChunk1Size = 16;
	// int myBitsPerSample= 16;
	// int myFormat = 1;
	// long myChannels = 1;
	// long mySampleRate = 22100;
	// long myByteRate = mySampleRate * myChannels * myBitsPerSample/8;
	// int myBlockAlign = (int) (myChannels * myBitsPerSample/8);
	//
	// byte[] clipData = getBytesFromFile(fileToConvert);
	//
	// long myDataSize = clipData.length;
	// long myChunk2Size = myDataSize * myChannels * myBitsPerSample/8;
	// long myChunkSize = 36 + myChunk2Size;
	//
	// OutputStream os;
	// os = new FileOutputStream(new
	// File("/sdcard/onefile/assessor/OneFile_Audio_"+ newRecordingID+".wav"));
	// BufferedOutputStream bos = new BufferedOutputStream(os);
	// DataOutputStream outFile = new DataOutputStream(bos);
	//
	// outFile.writeBytes("RIFF"); // 00 - RIFF
	// outFile.write(intToByteArray((int)myChunkSize), 0, 4); // 04 - how big is
	// the rest of this file?
	// outFile.writeBytes("WAVE"); // 08 - WAVE
	// outFile.writeBytes("fmt "); // 12 - fmt
	// outFile.write(intToByteArray((int)mySubChunk1Size), 0, 4); // 16 - size
	// of this chunk
	// outFile.write(shortToByteArray((short)myFormat), 0, 2); // 20 - what is
	// the audio format? 1 for PCM = Pulse Code Modulation
	// outFile.write(shortToByteArray((short)myChannels), 0, 2); // 22 - mono or
	// stereo? 1 or 2? (or 5 or ???)
	// outFile.write(intToByteArray((int)mySampleRate), 0, 4); // 24 - samples
	// per second (numbers per second)
	// outFile.write(intToByteArray((int)myByteRate), 0, 4); // 28 - bytes per
	// second
	// outFile.write(shortToByteArray((short)myBlockAlign), 0, 2); // 32 - # of
	// bytes in one sample, for all channels
	// outFile.write(shortToByteArray((short)myBitsPerSample), 0, 2); // 34 -
	// how many bits in a sample(number)? usually 16 or 24
	// outFile.writeBytes("data"); // 36 - data
	// outFile.write(intToByteArray((int)myDataSize), 0, 4); // 40 - how big is
	// this data chunk
	// outFile.write(clipData); // 44 - the actual data itself - just a long
	// string of numbers
	//
	// outFile.flush();
	// outFile.close();
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }

	// ' Finalize the file. Write the file size to the header.
	// fileSize = LOF(1) ' Get the actual file size.
	// Put #1, 5, CLng(fileSize - 8) ' Set first file size marker.
	// Put #1, 41, CLng(fileSize - 44) ' Set data size marker.
	// Close #1 ' Close the file.

	public void finalizeFile() throws IOException {
		System.out.println("finalizing file: " + fullFilename);
		outFile.flush();
		outFile.close();
		RandomAccessFile fileInsert = new RandomAccessFile(fullFilename, "rw");
		long fileLength = dataSize + 44;
		fileInsert.seek(4); // formerly 5
		fileInsert.write(intToByteArray((int) fileLength - 8));
		fileInsert.seek(40); // formerly 41
		fileInsert.write(intToByteArray((int) fileLength - 44));
		fileInsert.close();
	}

	private static byte[] intToByteArray(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) (i & 0x00FF);
		b[1] = (byte) ((i >> 8) & 0x000000FF);
		b[2] = (byte) ((i >> 16) & 0x000000FF);
		b[3] = (byte) ((i >> 24) & 0x000000FF);
		return b;
	}

	// convert a short to a byte array
	public static byte[] shortToByteArray(short data) {
		/*
		 * NB have also tried: return new byte[]{(byte)(data &
		 * 0xff),(byte)((data >> 8) & 0xff)};
		 */

		return new byte[] { (byte) (data & 0xff), (byte) ((data >>> 8) & 0xff) };
	}
	
	public void addInput(AudioFileInput input) {
		inputs[numInputs] = input;
		numInputs++;
	}

}
