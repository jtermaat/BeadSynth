package com.beadsynth.physics;

public class WaveStringSnapshot {
	
	private int yLevel;
	private int xStart;
	private int xEnd;
	private double maxAmp;
	private Bead[] beads;
	private int numBeads;
	private boolean gravityOn;
	private boolean cleared;
	
	public int getyLevel() {
		return yLevel;
	}
	public void setyLevel(int yLevel) {
		this.yLevel = yLevel;
	}
	public int getxStart() {
		return xStart;
	}
	public void setxStart(int xStart) {
		this.xStart = xStart;
	}
	public int getxEnd() {
		return xEnd;
	}
	public void setxEnd(int xEnd) {
		this.xEnd = xEnd;
	}
	public double getMaxAmp() {
		return maxAmp;
	}
	public void setMaxAmp(double maxAmp) {
		this.maxAmp = maxAmp;
	}
	public Bead[] getBeads() {
		return beads;
	}
	public void setBeads(Bead[] beads) {
		this.beads = new Bead[BeadString.MAX_ALLOWED_BEADS];
		for (int i = 0;i<beads.length;i++) {
			this.beads[i] = beads[i];
		}
	}
	public int getNumBeads() {
		return numBeads;
	}
	public void setNumBeads(int numBeads) {
		this.numBeads = numBeads;
	}
	public boolean isGravityOn() {
		return gravityOn;
	}
	public void setGravityOn(boolean gravityOn) {
		this.gravityOn = gravityOn;
	}
	public boolean isCleared() {
		return cleared;
	}
	public void setCleared(boolean cleared) {
		this.cleared = cleared;
	}
}
