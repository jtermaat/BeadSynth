package com.beadsynth.filestorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.beadsynth.physics.Bead;
import com.beadsynth.physics.RecordableBead;

public class BeadIndexer extends AtomicInteger implements Serializable {
	
	private static final long serialVersionUID = 1384925720938623592L;
	
	List<RecordableBead> allBeadsList;
	
	public BeadIndexer() {
		super();
		allBeadsList = new ArrayList<RecordableBead>();
	}
	
	public void addBeadAndSetIndex(Bead newBead) {
		allBeadsList.add((RecordableBead)newBead);
		newBead.setUniqueIndex(getAndIncrement());
	}
	
	public List<RecordableBead> getAllBeads() {
		return allBeadsList;
	}

}
