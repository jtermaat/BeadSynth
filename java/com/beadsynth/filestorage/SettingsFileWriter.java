package com.beadsynth.filestorage;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import android.content.Context;
import android.graphics.Rect;
import android.widget.Toast;

import com.beadsynth.configuration.ScaleNote;
import com.beadsynth.configuration.ScaleType;
import com.beadsynth.keyboard.DynamicKeyboard;
import com.beadsynth.keyboard.KeyboardSnapshot;
import com.beadsynth.physics.Bead;
import com.beadsynth.physics.BeadSnapshot;
import com.beadsynth.physics.RecordableBead;
import com.beadsynth.physics.RecordableWaveString;
import com.beadsynth.physics.WaveStringSnapshot;
import com.beadsynth.view.ImageScheme;

public class SettingsFileWriter {
	double bpm;
	ScaleType scaleType;
	ScaleNote scaleNote;
	int numStrings;
	List<RecordableWaveString> strings;
	int numBeads;
	List<RecordableBead> beads;
	int numSnapshots;
	List<DynamicKeyboard> keyboards;
	String bgFileName;
	
	public SettingsFileWriter(double bpm, ScaleType scaleType, ScaleNote scaleNote, int numStrings, List<RecordableWaveString> strings, int numBeads, List<RecordableBead> beads, List<DynamicKeyboard> keyboards, String bgFileName) {
		this.bpm = bpm;
		this.scaleType = scaleType;
		this.scaleNote = scaleNote;
		this.numStrings = numStrings;
		this.strings = strings;
		this.numBeads = numBeads;
		this.beads = beads;
		BeadSnapshot[] snaps = beads.get(0).getBeadSnapshots();
		numSnapshots = 0;
		for (int i = 0;i<snaps.length;i++) {
			if (snaps[i] != null) {
				numSnapshots = i + 1;
			}
		}
		this.keyboards = keyboards;
		this.bgFileName = bgFileName;
	}
	
	public void writeToFile(String filename, Context context) {
//		System.out.println("file writing output: \n");
//		System.out.println("File: " + filename + "\n");
//		System.out.println(fileOutputString());
		writeStringToFile(filename, fileOutputString(), context);
	}
	
	private String fileOutputString() {
		String firstLine = "SavedSettings:" + bpm + ";" + scaleType.scaleName() + ";" + scaleNote.noteName() + ";" + numStrings + ";" + numBeads + ";" + bgFileName + ";;\n";
		StringBuilder output = new StringBuilder();
		output.append(firstLine);
		for (int i = 0;i<numStrings;i++) {
			output.append(getOutputForString(strings.get(i)));
		}
		for (int i = 0;i<numBeads;i++) {
			output.append(getOutputForBead(beads.get(i)));
		}
		System.out.println("numKeyboardsToWrite: " + keyboards.size());
		for (DynamicKeyboard keyboard : keyboards) {
			System.out.println("outputforKeyboard: " + getOutputForKeyboard(keyboard));
			output.append(getOutputForKeyboard(keyboard));
		}
		return output.toString();
	}
	
	private String getOutputForKeyboard(DynamicKeyboard keyboard) {
		Rect keyboardPosition = keyboard.getPosition();
		String outputLine = "Keyboard:" + keyboard.getSelectedKey().getKeyNumber() + ";"
				+ keyboard.getStartPianoKeyNumber() + ";"
				+ keyboard.getEndPianoKeyNumber() + ";"
				+ keyboardPosition.bottom + ";"
				+ keyboardPosition.left + ";" 
				+ keyboardPosition.right + ";" 
				+ keyboardPosition.top + ";"
				+ keyboard.getNumSnapshots() + ";;\n";
		KeyboardSnapshot[] snapshots = keyboard.getSnapshots();
		System.out.println("KEYBOARD NUM SNAPSHOTS: " + keyboard.getNumSnapshots());
		for (int i = 0;i<keyboard.getNumSnapshots();i++) {
			System.out.println("Snapshot " + i + ": " + snapshots[i]);
			outputLine = outputLine + getOutputForKeyboardSnapshot(snapshots[i]);
		}
		return outputLine;
	}
	
	private String getOutputForKeyboardSnapshot(KeyboardSnapshot snapshot) {
		String outputLine = "KeyboardSnap:" + snapshot.getKey().getKeyNumber() + ";"
				+ snapshot.getScale().scaleName() + ";"
				+ snapshot.isCleared() + ";"
				+ snapshot.getTonic().chromaticNumber() + ";;\n";
		return outputLine;		
	}
	
	private String getOutputForImageScheme(ImageScheme scheme) {
		String outputLine = "ImageScheme:" 
				+ scheme.getBeadRegularImageFilename() + ";"
				+ scheme.getBeadSelectedImageFilename() + ";" 
				+ scheme.getBeadLockedImageFilename() + ";"
				+ scheme.getBeadConnectingImageFilename() + ";"
				+ scheme.getBeadGlowImageFilename() + ";"
				+ scheme.getKeyboardBlackKeyImageFilename() + ";" 
				+ scheme.getKeyboardBlackKeySelectedImageFilename() + ";"
				+ scheme.getKeyboardWhiteKeyImageFilename() + ";" 
				+ scheme.getKeyboardWhiteKeySelectedImageFilename() + ";" 
				+ scheme.getWaveColor() + ";;\n";
		return outputLine;
	}
	
	private String getOutputForString(RecordableWaveString string) {
		String firstLine = "WaveString:" + numSnapshots + ";" + string.getNumBeads() + ";"
				+ string.getyLevel() + ";"
				+ string.getxStart() + ";"
				+ string.getxEnd() + ";" 
				+ string.getMaxAmp() + ";" 
				+ string.getNumBeads() + ";";
		Bead[] beads = string.getBeads();
		System.out.println("NUMBEADS: " + string.getNumBeads());
		for (int i = 0;i<string.getNumBeads();i++) {
			System.out.println("BEAD: " + beads[i]);
			System.out.println("BEADINDEX: " + beads[i].getUniqueIndex());
			firstLine = firstLine + (beads[i].getUniqueIndex() + ",");
		}
		StringBuilder output = new StringBuilder();
		
		output.append(firstLine + ";;\n");
		output.append(getOutputForImageScheme(string.getImageScheme()));

		WaveStringSnapshot[] stringSnapshots = string.getSnapshots();
		for (int i = 0;i<numSnapshots;i++) {
			output.append(getOutputForStringSnapshot(stringSnapshots[i]));
		}
		return output.toString();
	}
	
	private String getOutputForStringSnapshot(WaveStringSnapshot snapshot) {
		String outputLine = "WaveStringSnap:" + snapshot.getyLevel() + ";"
				+ snapshot.getxStart() + ";"
				+ snapshot.getxEnd() + ";" 
				+ snapshot.getMaxAmp() + ";" 
				+ snapshot.isCleared() + ";"
				+ snapshot.getNumBeads() + ";;\n";
		return outputLine;
	}
	
	private String getOutputForBead(RecordableBead bead) {
		String firstLine = "Bead:" + bead.getUniqueIndex() + ";" 
				+ numSnapshots + ";"
				+ bead.getX() + ";" 
				+ bead.getY() + ";"
				+ bead.getWeight() + ";" 
				+ bead.getFriction() + ";" 
				+ bead.getRadius() + ";"
				+ bead.isLocked() + ";"  
				+ bead.isXLocked() + ";" 
				+ bead.isDestroyed() + ";"
				+ bead.getVelocity().getX() + ";"
				+ bead.getVelocity().getY() + ";"
				+ bead.getConnections().size() + ";";
		for (Bead connection : bead.getConnections()) {
			firstLine = firstLine + connection.getUniqueIndex() + ",";
		}
		firstLine = firstLine + ";;\n";
		StringBuilder output = new StringBuilder();
		output.append(firstLine);
		BeadSnapshot[] snapshots = bead.getBeadSnapshots();
		System.out.println("NUM SNAPSHOTS: " + numSnapshots);
		for (int i = 0;i<numSnapshots;i++) {
			System.out.println("i: " + i + ", snapshot: " + snapshots[i]);
			output.append(getOutputForBeadSnapshot(snapshots[i]));
		}
		return output.toString();
	}
	
	private String getOutputForBeadSnapshot(BeadSnapshot snapshot) {
		if (snapshot == null) {
			return "BeadSnap:null;;\n";
		}
		String outputLine = "BeadSnap:" + snapshot.getX() + ";" 
				+ snapshot.getY() + ";"
				+ snapshot.getWeight() + ";" 
				+ snapshot.getFriction() + ";" 
				+ snapshot.getRadius() + ";"
				+ snapshot.isLocked() + ";"  
				+ snapshot.isXLocked() + ";" 
				+ snapshot.isDestroyed() + ";"
				+ snapshot.isCleared() + ";"
				+ snapshot.getVelocity().getX() + ";"
				+ snapshot.getVelocity().getY() + ";"
				+ snapshot.getConnections().size() + ";";
		for (Bead connection : snapshot.getConnections()) {
			outputLine = outputLine + connection.getUniqueIndex() + ",";
		}
		outputLine = outputLine + ";;\n";
		return outputLine;
	}
	
	private void writeStringToFile(String filename, String data, Context context) {

	        try {
	            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
	            outputStreamWriter.write(data);
	            outputStreamWriter.close();
	        }
	        catch (IOException e) {
	        	Toast.makeText(context, 
	        			"An unknown error occurred while writing to file.", Toast.LENGTH_SHORT).show();
	        
	        }
	}


}
