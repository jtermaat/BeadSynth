/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beadsynth.physics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.beadsynth.filestorage.BeadIndexer;

/**
 *
 * @author John
 */
public class RecordableBead extends Bead {
    
    BeadSnapshot[] snapshots;
    int numSnapshots;
    
    public static int MAX_SNAPSHOTS = 10;

    public RecordableBead(double X, double Y, boolean GravityOn, BeadIndexer beadIndexer) {
        super(X,Y,GravityOn);
        snapshots = new BeadSnapshot[MAX_SNAPSHOTS];
        numSnapshots = 0;
        beadIndexer.addBeadAndSetIndex(this);
    }
    
    public RecordableBead(double X, double Y, BeadIndexer beadIndexer) {
        super(X,Y);
        snapshots = new BeadSnapshot[MAX_SNAPSHOTS];
        numSnapshots = 0;
        beadIndexer.addBeadAndSetIndex(this);
    }
    
    public RecordableBead(Bead other) {
    	super(other);
        snapshots = new BeadSnapshot[MAX_SNAPSHOTS];
        numSnapshots = 0;
    }
    
    // returns the number of the snapshot so it can be reverted to by number.
    public int takeSnapshot() {
    	BeadSnapshot snap = getSnapshot();
        snapshots[numSnapshots] = snap;
        numSnapshots++;
        return numSnapshots - 1;
    }
    
    public void setIndexedSnapshot(int index) {
        BeadSnapshot snap = getSnapshot();
        snapshots[index] = snap;
    }
    
    private BeadSnapshot getSnapshot() {
        BeadSnapshot snap = new BeadSnapshot();
        snap.setConnections(getConnections());
        snap.setFriction(getFriction());
        snap.setRadius(radius);
        snap.setVelocity(getVelocity());
        snap.setWeight(weight);
        snap.setDestroyed(isDestroyed());
        snap.setX(x);
        snap.setY(y);
        snap.setLocked(isLocked());
        snap.setXLocked(xLocked);
        return snap;
    }
    
    public void revertToSnapshot(int SnapshotNum) {
        setFriction(snapshots[SnapshotNum].getFriction());
        radius = snapshots[SnapshotNum].getRadius();
        setVelocity(new Velocity(snapshots[SnapshotNum].getVelocity()));
        weight = snapshots[SnapshotNum].getWeight();
        x = snapshots[SnapshotNum].getX();
        y = snapshots[SnapshotNum].getY();
        setLocked(snapshots[SnapshotNum].isLocked());
        xLocked = snapshots[SnapshotNum].isXLocked();
        setDestroyed(snapshots[SnapshotNum].isDestroyed());
        this.setConnections(snapshots[SnapshotNum].getConnections());
    }
    
    public void setConnections(List<Bead> Connections) {
        connections = new ArrayList<Bead>(Connections);
    }
    
    public BeadSnapshot[] getBeadSnapshots() {
    	return snapshots;
    }
    
    public int getNumSnapshots() {
    	return numSnapshots;
    }
    
    public void setSnapshots(BeadSnapshot[] snapshots) {
    	this.snapshots = snapshots;
    }
    
}
