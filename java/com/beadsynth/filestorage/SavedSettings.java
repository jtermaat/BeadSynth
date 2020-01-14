package com.beadsynth.filestorage;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.beadsynth.configuration.ScaleNote;
import com.beadsynth.configuration.ScaleType;
import com.beadsynth.keyboard.DynamicKeyboard;
import com.beadsynth.physics.RecordableWaveString;

public class SavedSettings implements Parcelable {
	
	private double bpm;
	private ScaleType scaleType;
	private ScaleNote scaleNote;
	private List<RecordableWaveString> strings;
	private List<DynamicKeyboard> keyboards;
	private int numSnapshots;
	private BeadIndexer beadIndexer;
	private int numNoteSnapshots;
	
	public SavedSettings() {
	}
	
	public SavedSettings(Parcel in) {
		bpm = in.readDouble();
		scaleType = (ScaleType)in.readSerializable();
		scaleNote = (ScaleNote)in.readSerializable();
		in.readList(strings, null);
		in.readList(keyboards, null);
		numSnapshots = in.readInt();
		beadIndexer = (BeadIndexer)in.readSerializable();
		numNoteSnapshots = in.readInt();
	}
	
	public double getBpm() {
		return bpm;
	}
	public void setBpm(double bpm) {
		this.bpm = bpm;
	}
	public ScaleType getScaleType() {
		return scaleType;
	}
	public void setScaleType(ScaleType scaleType) {
		this.scaleType = scaleType;
	}
	public ScaleNote getScaleNote() {
		return scaleNote;
	}
	public void setScaleNote(ScaleNote scaleNote) {
		this.scaleNote = scaleNote;
	}
	public List<RecordableWaveString> getStrings() {
		return strings;
	}
	public void setStrings(List<RecordableWaveString> strings) {
		this.strings = strings;
	}
	public int getNumSnapshots() {
		return numSnapshots;
	}
	public void setNumSnapshots(int numSnapshots) {
		this.numSnapshots = numSnapshots;
	}
	public BeadIndexer getBeadIndexer() {
		return beadIndexer;
	}
	public void setBeadIndexer(BeadIndexer beadIndexer) {
		this.beadIndexer = beadIndexer;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeDouble(bpm);
		out.writeSerializable(scaleType);
		out.writeSerializable(scaleNote);
		out.writeList(strings);
		out.writeList(keyboards);
		out.writeInt(numSnapshots);
		out.writeSerializable(beadIndexer);		
		out.writeInt(numNoteSnapshots);
	}

	public List<DynamicKeyboard> getKeyboards() {
		return keyboards;
	}

	public void setKeyboards(List<DynamicKeyboard> keyboards) {
		this.keyboards = keyboards;
	}
	
	public int getNumNoteSnapshots() {
		return numNoteSnapshots;
	}
	
	public void setNumNoteSnapshots(int numNoteSnapshots) {
		this.numNoteSnapshots = numNoteSnapshots;
	}

}
