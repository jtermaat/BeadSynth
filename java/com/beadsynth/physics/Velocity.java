/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beadsynth.physics;
//import java.awt.*;

/**
 *
 * @author John
 */
public class Velocity {
    double xVelocity;
    double yVelocity;

    public Velocity(double XVelocity, double YVelocity) {
        xVelocity = XVelocity;
        yVelocity = YVelocity;
    }

    public Velocity() {
        xVelocity = 0.0;
        yVelocity = 0.0;
    }

    public Velocity(Velocity Other) {
        xVelocity = Other.getX();
        yVelocity = Other.getY();
    }
  /*
    public void setByAngle(double angle, double distance) {

    }
*/
    public double getX() {
        return xVelocity;
    }

    public double getY() {
        return yVelocity;
    }

    public void resize(double fraction) {
        xVelocity = xVelocity * fraction;
        yVelocity = yVelocity * fraction;
    }

    // in radians
    public double getAngle() {
        double a = Math.abs(xVelocity);
        double b = Math.abs(yVelocity);

        double alpha = Math.atan(b/a);
        double angle = 0.0;

        double degrees90 = Math.PI * 0.5;
        double degrees180 = Math.PI;
        double degrees270 = Math.PI * 1.5;

        if (xVelocity == 0.0 && yVelocity == 0.0)
            angle = 0.0;
        else if (xVelocity == 0.0 && yVelocity > 0.0)
            angle = 0.0;
        else if (xVelocity > 0.0 && yVelocity > 0.0)
            angle = degrees90 - alpha;
        else if (xVelocity > 0.0 && yVelocity == 0.0)
            angle = degrees90;
        else if (xVelocity > 0.0 && yVelocity < 0.0)
            angle = degrees90 + alpha;
        else if (xVelocity == 0.0 && yVelocity < 0.0)
            angle = degrees180;
        else if (xVelocity < 0.0 && yVelocity < 0.0)
            angle = degrees270 - alpha;
        else if (xVelocity < 0.0 && yVelocity == 0.0)
            angle = degrees270;
        else if (xVelocity < 0.0 && yVelocity > 0.0)
            angle = degrees270 + alpha;

        return angle;
    }

    public void square() {
        if (isPositive(xVelocity))
            xVelocity = xVelocity * xVelocity;
        else
            xVelocity = xVelocity * xVelocity * -1;
        if (isPositive(yVelocity))
            yVelocity = yVelocity * yVelocity;
        else
            yVelocity = yVelocity * yVelocity * -1;
    }

    public void squareRoot() {
        double xV = Math.sqrt(Math.abs(xVelocity));
        double yV = Math.sqrt(Math.abs(yVelocity));
        xV *= (xVelocity / Math.abs(xVelocity));
        yV *= (yVelocity / Math.abs(yVelocity));
        xVelocity = xV;
        yVelocity = yV;
    }

    public double distanceTraveled() {
        return Math.sqrt(xVelocity*xVelocity + yVelocity*yVelocity);
    }

    public void add(Velocity other) {
        xVelocity += other.getX();
        yVelocity += other.getY();
    }
    
    public void setY(double Y) {
        yVelocity = Y;
    }
    
    public void setX(double X) {
        xVelocity = X;
    }

    public void subtract(Velocity other) {
        xVelocity -= other.getX();
        yVelocity -= other.getY();
    }

    public Velocity plus(Velocity other) {
        return new Velocity(xVelocity + other.getX(), yVelocity + other.getY());
    }

    public void addX(double X) {
        xVelocity += X;
    }

    public void addY(double Y) {
        yVelocity += Y;
    }

    // returns true if x's and y's are of the same sign in both velocities.
    public boolean sameDirection(Velocity other) {
        return sameSign(other.getX(), xVelocity) && sameSign(other.getY(), yVelocity);
    }

    private static boolean sameSign(double a, double b) {
        return (a * b >= 0.0);
    }

    public String toString() {
        return "x: " + xVelocity + ", y: " + yVelocity;
    }

    public Velocity doubled() {
        return new Velocity(xVelocity * 10, yVelocity * 10);
    }

    public Velocity getReverse() {
        return new Velocity((xVelocity * (-1)), (yVelocity * (-1)));
    }

    public static Velocity max(Velocity v1, Velocity v2) {
        Velocity max = v1;
        if (v2.distanceTraveled() > v1.distanceTraveled())
            max = v2;
        return max;
    }

    private static boolean isPositive(double n) {
        return (n > 0.0);
    }

    private static boolean isNegative(double n) {
        return (n < 0.0);
    }
        



}
