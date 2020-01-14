/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beadsynth.physics;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author John
 */
public class BeadSnapshot {
    private List<Bead> connections;
    private List<Integer> connectionIds;
    
    private double x;
    private double y;
    private double weight;
    private double radius;
    private double friction;
    
    private boolean locked;
    private boolean xLocked;
    private boolean destroyed;
    
    private boolean cleared;
    
    Velocity v;
    
    public void setVelocity(Velocity V) {
        v = new Velocity(V);
    }
    
    public Velocity getVelocity() {
        return v;
    }
    
    public void setConnections(List<Bead> Connections) {
        connections = new ArrayList<Bead>(Connections);
    }
    
    public List<Bead> getConnections() {
        return connections;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * @return the friction
     */
    public double getFriction() {
        return friction;
    }

    /**
     * @param friction the friction to set
     */
    public void setFriction(double friction) {
        this.friction = friction;
    }

    /**
     * @return the locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * @param locked the locked to set
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    /**
     * @return the locked
     */
    public boolean isXLocked() {
        return xLocked;
    }

    /**
     * @param locked the locked to set
     */
    public void setXLocked(boolean locked) {
        this.xLocked = xLocked;
    }    
    
    /**
     * @return the locked
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * @param locked the locked to set
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

	public List<Integer> getConnectionIds() {
		return connectionIds;
	}

	public void setConnectionIds(List<Integer> connectionIds) {
		this.connectionIds = connectionIds;
	}

	public boolean isCleared() {
		return cleared;
	}

	public void setCleared(boolean cleared) {
		this.cleared = cleared;
	}
    
}
