/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beadsynth.physics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.beadsynth.filestorage.BeadIndexer;
import com.beadsynth.view.ColorScheme;
import com.beadsynth.view.ImageScheme;

/**
 * 
 * @author John
 */
public class Bead {

	protected List<Bead> connections;
	private List<Integer> connectionIds;

	double x;
	double y;
	double weight;
	double radius;
	double glowRadius;
	private double friction;
	
	private boolean glowing;
	private long glowingStartTime;
	private static int DEFAULT_GLOW_TIME = 175;
	
	private int uniqueIndex;

	ColorScheme colorScheme;
	
	ImageScheme imageScheme;

	static double RADIUS_RATIO = 150.0;

	static double FRICTION_RATIO = 1.0;

	private Velocity v;

	private boolean locked;
	boolean xLocked; // Can only be moved along the y-axis.
	boolean grabbed; // Indicates if the user is currently dragging this bead.
	private boolean awaitingConnection; // Indicates if the bead is in
										// preparation to be locked to another
										// bead.
	private boolean gravityOn;
	private boolean destroyed;

	final static double DEFAULT_RADIUS = 22;
	final static double DEFAULT_GLOW_RADIUS = 30;
	final static double DEFAULT_WEIGHT = .1;
	// final static double FRICTION = 0.04;
	static double RING_SPACE = 2.0;

	static double GRAVITY_FORCE = 2.0;

	public Bead(double X, double Y) {
		x = X;
		y = Y;
		// connections = new Bead[15];
		setConnections(new ArrayList<Bead>());
		setVelocity(new Velocity(0.0, 0.0));
		weight = DEFAULT_WEIGHT;
		radius = DEFAULT_RADIUS;
		glowRadius = DEFAULT_GLOW_RADIUS;
//		radius = weight * RADIUS_RATIO;
		setFriction(weight * FRICTION_RATIO);
		setGravityOn(false);
		setDestroyed(false);
		xLocked = false;
		glowing = false;
		glowingStartTime = 0;
	}

	public Bead(double X, double Y, boolean GravityOn) {
		x = X;
		y = Y;
		// connections = new Bead[15];
		setConnections(new ArrayList<Bead>());
		setVelocity(new Velocity(0.0, 0.0));
		weight = DEFAULT_WEIGHT;
		radius = DEFAULT_RADIUS;
		glowRadius = DEFAULT_GLOW_RADIUS;
//		radius = weight * RADIUS_RATIO;
		setFriction(weight * FRICTION_RATIO);
		setGravityOn(GravityOn);
		setDestroyed(false);
		xLocked = false;
		glowing = false;
		glowingStartTime = 0;
	}

	public Bead(Bead other) {
		setConnections(other.getConnections());
		x = other.getX();
		y = other.getY();
		weight = other.getWeight();
		radius = other.getRadius();
		setFriction(other.getFriction());
		setVelocity(other.getVelocity());
		setLocked(other.isLocked());
		xLocked = other.isXLocked();
		grabbed = other.isGrabbed();
		setGravityOn(other.isGravityOn());
		setDestroyed(other.isDestroyed());
	}

	public void connectTo(Bead other) {
		// connections[numConnections] = other;
		getConnections().add(other);
	}

	public void disconnectFrom(Bead other) {
		getConnections().remove(other);
	}

	public void addWeight(double newWeight) {
		weight += newWeight;
		radius = weight * RADIUS_RATIO;
		setFriction(weight * FRICTION_RATIO);
	}

	public void switchGravity() {
		setGravityOn(!isGravityOn());
	}
	
	public void glow() {
		glowing = true;
		glowingStartTime = System.currentTimeMillis();
	}

	public void nextFrame() {
		if (!isLocked() && !isDestroyed() && !xLocked) {
			for (int i = 0; i < getConnections().size(); i++) {
				Velocity influence = getConnections().get(i).getInfluenceOn(x, y, weight);
				// influence.square();
				// influence.squareRoot();
				getVelocity().add(influence);
			}
			getVelocity().resize(1.0 - getFriction());
			x += getVelocity().getX();
			y += getVelocity().getY();
			if (isGravityOn()) {
				y += GRAVITY_FORCE;
			}
		}
		if (glowing) {
			if (System.currentTimeMillis() - glowingStartTime > DEFAULT_GLOW_TIME) {
				glowing = false;
			}
		}

	}

	public Velocity getInfluenceOn(double X, double Y, double otherWeight) {
		double xInfluence = ((x - X) * weight) / (otherWeight / weight);
		double yInfluence = ((y - Y) * weight) / (otherWeight / weight);
		if (isDestroyed())
			return new Velocity();
		else
			return new Velocity(xInfluence, yInfluence);
	}

	public void destroy() {
		setDestroyed(true);
//		for (Bead connection : getConnections()) {
//			connection.disconnectFrom(this);
//			this.disconnectFrom(connection);
//		}
	}

	public double getXDistanceFromBead(Bead Next) {
		return Next.getX() - x;
	}

	public double getX() {
		return x;
	}

	public void setWeight(double W) {
		weight = W;
	}

	public double getWeight() {
		return weight;
	}

	public double getY() {
		return y;
	}

	public void setX(double X) {
		if (!xLocked) {
			x = X;
		}
	}

	public void setY(double Y) {
		y = Y;
	}

	public void addX(int X) {
		if (!xLocked) {
			x += X;
		}
	}

	public void addY(int Y) {
		y += Y;
	}

	public void lock() {
		setLocked(true);
	}

	public void unlock() {
		setLocked(false);
	}

	public void setXLocked(boolean xLocked) {
		this.xLocked = xLocked;
	}

	public boolean isXLocked() {
		return xLocked;
	}

	public void grab() {
		grabbed = true;
	}

	public boolean isGrabbed() {
		return grabbed;
	}

	public void release() {
		grabbed = false;
	}

	public double getRadius() {
		return radius;
	}
	
	private Rect getBeadDimensions() {
		Rect dimensions = new Rect((int)(x-radius), (int)(y-radius), (int)(x+radius), (int)(y+radius));
		return dimensions;
	}
	
	private Rect getBeadGlowDimensions() {
		Rect dimensions = new Rect((int)(x-radius), (int)(y-radius), (int)(x+radius), (int)(y+radius));
		return dimensions;		
	}

	public void drawStandard(Canvas c) {
    	try {
    		c.drawBitmap(imageScheme.getBeadRegularImage(), null, getBeadDimensions(), new Paint());
    	} catch (NullPointerException e) {
    	}
	}

	public void drawGrabbed(Canvas c) {
//    	try {
//    		c.drawBitmap(imageScheme.getBeadSelectedImage(), null, getBeadDimensions(), new Paint());
//    	} catch (NullPointerException e) {
//    	}
		drawGlowing(c);
		this.drawStandard(c);
	}

	public void drawLocked(Canvas c) {
    	try {
    		c.drawBitmap(imageScheme.getBeadLockedImage(), null, getBeadDimensions(), new Paint());
    	} catch (NullPointerException e) {
    	}
	}
	
	public void drawConnecting(Canvas c) {
//    	try {
//    		c.drawBitmap(imageScheme.getBeadConnectingImage(), null, getBeadDimensions(), new Paint());
//    	} catch (NullPointerException e) {
//    	}
		drawGlowing(c);
		this.drawStandard(c);
	}
	
	public void drawGlowing(Canvas c) {
    	try {
    		c.drawBitmap(imageScheme.getBeadGlowImage(), null, getBeadGlowDimensions(), new Paint());
    	} catch (NullPointerException e) {
    	}		
	}

	public void draw(Canvas c) {
		Paint p = new Paint();
		
		if (glowing){
			drawGlowing(c);
		}

		if (this.isAwaitingConnection()) {
			drawConnecting(c);
		}
		else if (this.isGrabbed()) {
			drawGrabbed(c);
		}
		else if (this.isLocked()) {
			drawLocked(c);
		} else {
			this.drawStandard(c);
		}
	}
	
	public void drawConnectionLines(Canvas c) {
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		for (int i = 0; i < getConnections().size(); i++) {
			c.drawLine((float) x, (float) y, (float) getConnections().get(i).getX(), (float) getConnections().get(i).getY(), p);
		}
	}

//	public void drawRing(Canvas c) {
//		Paint p = new Paint();
//		p.setStyle(Paint.Style.STROKE);
//		c.drawCircle((float) x, (float) y, (float) (radius + RING_SPACE), p);
//	}

	public double getFriction() {
		return friction;
	}

	public void setFriction(double friction) {
		this.friction = friction;
	}

	public Velocity getVelocity() {
		return v;
	}

	public void setVelocity(Velocity v) {
		this.v = v;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isGravityOn() {
		return gravityOn;
	}

	public void setGravityOn(boolean gravityOn) {
		this.gravityOn = gravityOn;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	public List<Bead> getConnections() {
		return connections;
	}

	public void setConnections(List<Bead> connections) {
		this.connections = connections;
	}

	public boolean isAwaitingConnection() {
		return awaitingConnection;
	}

	public void setAwaitingConnection(boolean awaitingConnection) {
		this.awaitingConnection = awaitingConnection;
	}
	
	public void setImageScheme(ImageScheme imageScheme) {
		this.imageScheme = imageScheme;
	}

	public int getUniqueIndex() {
		return uniqueIndex;
	}
	
	public void setUniqueIndex(int uniqueIndex) {
		this.uniqueIndex = uniqueIndex;
	}

	public List<Integer> getConnectionIds() {
		return connectionIds;
	}

	public void setConnectionIds(List<Integer> connectionIds) {
		this.connectionIds = connectionIds;
	}
	
    public void revive() {
    	this.destroyed = false;
    }
    
    public void severConnections() {
    	for (Bead connection : connections) {
    		connection.disconnectFrom(this);
    	}
    	connections = new ArrayList<Bead>();
    }
    
    public boolean equals(Bead other) {
    	return this.uniqueIndex == other.getUniqueIndex();
    }
}
