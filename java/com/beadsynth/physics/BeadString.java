/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beadsynth.physics;

import android.graphics.Canvas;

import com.beadsynth.filestorage.BeadIndexer;
import com.beadsynth.util.TrigFunctions;
import com.beadsynth.view.ImageScheme;

/**
 * 
 * @author John
 */
public class BeadString {
	Bead[] beads;
	int numBeads;
	boolean gravityOn;

	public static int MAX_ALLOWED_BEADS = 35;

	public BeadString(int XStart, int YStart, int XEnd, int YEnd, int NumBeads, boolean GravityOn, ImageScheme imageScheme) {
		numBeads = NumBeads;
		double xStep = (XEnd - XStart) / (double) (numBeads - 1);
		double yStep = (YEnd - YStart) / (double) (numBeads - 1);
		gravityOn = GravityOn;

		beads = new Bead[MAX_ALLOWED_BEADS];
		if (numBeads > 0)
			beads[0] = new Bead(XStart, YStart, GravityOn);
			beads[0].setImageScheme(imageScheme);
		for (int i = 1; i < numBeads; i++) {
			beads[i] = new Bead((int)(XStart + (xStep * i)), (int)(YStart + (yStep * i)), GravityOn);
			beads[i].connectTo(beads[i - 1]);
			beads[i - 1].connectTo(beads[i]);
			beads[i].setImageScheme(imageScheme);
		}
		if (numBeads > 0) {
			beads[0].lock();
			beads[numBeads - 1].lock();
		}
		// beads[5].addX(40);
	}

	public void draw(Canvas c) {
		for (int i = 0; i < numBeads; i++) {
			beads[i].draw(c);
		}
	}
	
	public void drawConnectionLines(Canvas c) {
		for (int i = 0;i < numBeads; i++) {
			beads[i].drawConnectionLines(c);
		}
	}

	public void nextFrame() {
		for (int i = 0; i < numBeads; i++) {
			beads[i].nextFrame();
		}
	}

	public int getNumBeads() {
		return numBeads;
	}

	public void addBead(int x, int y) {
		if (numBeads < MAX_ALLOWED_BEADS) {
			beads[numBeads] = new Bead(x, y, gravityOn);
			beads[numBeads].lock();
			numBeads++;
		}
	}

	public void addBead(Bead NewBead) {
		if (numBeads < MAX_ALLOWED_BEADS) {
			beads[numBeads] = NewBead;
			numBeads++;
		}
	}

	public Bead[] getArray() {
		return beads;
	}

	public Bead getGrabbedBead(int x, int y) {
		Bead grabbed = null;
		for (int i = 0; i < numBeads; i++) {
			double beadX = beads[i].getX();
			double beadY = beads[i].getY();
			double beadRadius = beads[i].getRadius();
			// double radius to allow easier bead grabbing
			if (TrigFunctions.getDistance(beadX, beadY, (double) x, (double) y) <= beadRadius * 2.0) {
				grabbed = beads[i];
			}
		}
		return grabbed;
	}

	public Bead getStartBead() {
		return beads[0];
	}

	public Bead getEndBead() {
		return beads[numBeads - 1];
	}
	
	public Bead[] getBeads() {
		return beads;
	}
	
	public void glow() {
		for (Bead bead : beads) {
			if (bead != null) {
				bead.glow();
			}
		}
	}

}
