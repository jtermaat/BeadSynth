package com.beadsynth.physics;

import com.beadsynth.filestorage.BeadIndexer;
import com.beadsynth.view.ImageScheme;

public class RecordableWaveString extends WaveString {

	WaveStringSnapshot[] snapshots;
	private ImageScheme imageScheme;

	static int MAX_SNAPSHOTS = 10;

	public RecordableWaveString(int XStart, int XEnd, int YLevel,
			int NumBeads, double MaxAmp, ImageScheme imageScheme, WaveStringSnapshot[] snapshots,
			RecordableBead[] beads, BeadIndexer beadIndexer) {
		super(XStart, XEnd, YLevel, NumBeads, MaxAmp, imageScheme, beadIndexer);
		this.setImageScheme(imageScheme);
		this.beads = beads;
		this.snapshots = snapshots;
	}
	
	public RecordableWaveString(int XStart, int XEnd, int YLevel, int NumBeads, double MaxAmp, ImageScheme imageScheme, BeadIndexer beadIndexer) {
		super(XStart, XEnd, YLevel, NumBeads, MaxAmp, imageScheme, beadIndexer);
		this.setImageScheme(imageScheme);
		// Must create beads as recordable rather than relying on superclass.

		double xStep = (XEnd - XStart) / (double) (numBeads - 1);
		double yStep = (YLevel - YLevel) / (double) (numBeads - 1);

		beads = new RecordableBead[MAX_ALLOWED_BEADS];
		if (numBeads > 0)
			beads[0] = new RecordableBead(XStart, YLevel, false, beadIndexer);
		beads[0].setImageScheme(imageScheme);
		for (int i = 1; i < numBeads; i++) {
			beads[i] = new RecordableBead((int)(XStart + (xStep * i)), (int)(YLevel + (yStep * i)), false, beadIndexer);
			beads[i].setImageScheme(imageScheme);
			beads[i].connectTo(beads[i - 1]);
			beads[i - 1].connectTo(beads[i]);
		}
		if (numBeads > 0) {
			beads[0].lock();
			beads[numBeads - 1].lock();
		}

		snapshots = new WaveStringSnapshot[MAX_SNAPSHOTS];
	}

	// returns the number of the snapshot so it can be reverted to by number.
	private WaveStringSnapshot getSnapshot() {
		WaveStringSnapshot snap = new WaveStringSnapshot();
		snap.setyLevel(getyLevel());
		snap.setxStart(getxStart());
		snap.setxEnd(getxEnd());
		snap.setMaxAmp(getMaxAmp());
		snap.setNumBeads(numBeads);
		snap.setBeads(beads);
		snap.setGravityOn(gravityOn);
		return snap;
	}

	private void recordBeads() {
		for (int i = 0; i < beads.length; i++) {
			if (beads[i] != null) {
				RecordableBead recordableCast = (RecordableBead) beads[i];
				recordableCast.takeSnapshot();
			}
		}
	}

	private void recordBeads(int index) {
		for (int i = 0; i < beads.length; i++) {
			if (beads[i] != null) {
				RecordableBead recordableCast = (RecordableBead) beads[i];
				recordableCast.setIndexedSnapshot(index);
			}
		}
	}

	// // returns the number of the snapshot so it can be reverted to by number.
	// public int takeSnapshot() {
	// recordBeads();
	// WaveStringSnapshot snap = getSnapshot();
	// snapshots[numSnapshots] = snap;
	// numSnapshots++;
	// return numSnapshots - 1;
	// }

	public void setIndexedSnapshot(int index) {
		glow();
		recordBeads(index);
		WaveStringSnapshot snap = getSnapshot();
		snapshots[index] = snap;
	}
	
	public void clearIndexedSnapshot(int index) {
		glow();
		snapshots[index].setCleared(true);
	}

	public void revertToSnapshot(int SnapshotNum) {
		System.out.println("SnapshotNum: " + SnapshotNum);
		System.out.println("SNAP CLEARED? " + snapshots[SnapshotNum].isCleared());
		if (snapshots[SnapshotNum] != null && !snapshots[SnapshotNum].isCleared()) {
			setyLevel(snapshots[SnapshotNum].getyLevel());
			setxStart(snapshots[SnapshotNum].getxStart());
			setxEnd(snapshots[SnapshotNum].getxEnd());
			setMaxAmp(snapshots[SnapshotNum].getMaxAmp());
			numBeads = snapshots[SnapshotNum].getNumBeads();
			beads = new Bead[MAX_ALLOWED_BEADS];
			for (int i = 0; i < beads.length; i++) {
				beads[i] = snapshots[SnapshotNum].getBeads()[i];
			}
			gravityOn = snapshots[SnapshotNum].isGravityOn();

			revertBeads(SnapshotNum);
		}
	}

	private void revertBeads(int SnapshotNum) {
		for (int i = 0; i < numBeads; i++) {
			RecordableBead recordableCast = (RecordableBead) beads[i];
			recordableCast.revertToSnapshot(SnapshotNum);
		}
	}
	
	// Returns true if successfully added bead
	public boolean addBead() {
		if (numBeads < MAX_ALLOWED_BEADS) {
			if (beads[numBeads] == null) {
				beads[numBeads] = new RecordableBead(getEndBead().getX(), getyLevel(), beadIndexer);
				beads[numBeads].setImageScheme(getImageScheme());
			} else {
				beads[numBeads].revive();
				beads[numBeads].severConnections();
			}
			beads[numBeads].lock();
//			beads[numBeads].setXLocked(true);
			beads[numBeads].connectTo(beads[numBeads - 1]);
			beads[numBeads - 1].connectTo(beads[numBeads]);
			beads[numBeads - 1].unlock();
//			beads[numBeads - 1].setXLocked(false);
			numBeads++;
			return true;
		} else {
			return false;
		}
	}

	public void removeBead() {
		if (numBeads > 2) {
			beads[numBeads - 2].setX(getxEnd());
			beads[numBeads - 2].setY(getyLevel());
			beads[numBeads - 2].lock();
//			beads[numBeads - 2].setXLocked(true);
//			beads[numBeads - 1].setXLocked(false);
			beads[numBeads - 1].destroy();
			beads[numBeads - 1].severConnections();
			numBeads--;
		}
	}
	
	public WaveStringSnapshot[] getSnapshots() {
		return snapshots;
	}

	public ImageScheme getImageScheme() {
		return imageScheme;
	}

	public void setImageScheme(ImageScheme imageScheme) {
		this.imageScheme = imageScheme;
	}

}
