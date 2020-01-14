/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beadsynth.util;
import android.graphics.Point;

/**
 *
 * @author John
 */
public class TrigFunctions {


    public static double getAngleFromPoints(int startX, int startY, int endX, int endY) {
        double diffX = (double)endX - (double)startX;
        double diffY = (double)endY - (double)startY;


        double a = Math.abs(diffX);
        double b = Math.abs(diffY);

        double alpha = Math.atan(b/a);
        double angle = 0.0;

        double degrees90 = Math.PI * 0.5;
        double degrees180 = Math.PI;
        double degrees270 = Math.PI * 1.5;

        if (diffX == 0.0 && diffY == 0.0)
            angle = 0.0;
        else if (diffX == 0.0 && diffY > 0.0)
            angle = 0.0;
        else if (diffX > 0.0 && diffY > 0.0)
            angle = degrees90 - alpha;
        else if (diffX > 0.0 && diffY == 0.0)
            angle = degrees90;
        else if (diffX > 0.0 && diffY < 0.0)
            angle = degrees90 + alpha;
        else if (diffX == 0.0 && diffY < 0.0)
            angle = degrees180;
        else if (diffX < 0.0 && diffY < 0.0)
            angle = degrees270 - alpha;
        else if (diffX < 0.0 && diffY == 0.0)
            angle = degrees270;
        else if (diffX < 0.0 && diffY > 0.0)
            angle = degrees270 + alpha;

        return angle;
    }

    public static double getLengthFromEndPoints(int startX, int startY, int endX, int endY) {
        return getLengthFromEndPoints((double)startX, (double)startY, (double)endX, (double)endY);
    }

    public static double getLengthFromEndPoints(double startX, double startY, double endX, double endY) {
        double a = Math.abs(endX - startX);
        double b = Math.abs(endY - startY);
        return Math.sqrt(a*a + b*b);
    }

    public static Point getLineEndPoint(int startX, int startY, double angle, double length) {
        int endX;
        int endY;
        double ninetyDegrees = Math.PI / 2.0;

        int xMultiplier = 1;
        int yMultiplier = 1;

        if (angle >= ninetyDegrees && angle < ninetyDegrees * 2.0) {
            yMultiplier = -1;
            angle = Math.PI - angle;
        }
        else if (angle >= ninetyDegrees * 2.0 && angle < ninetyDegrees * 3.0) {
            xMultiplier = -1;
            yMultiplier = -1;
            angle -= Math.PI;
        }
        else if (angle >= ninetyDegrees * 3.0 && angle < ninetyDegrees * 4.0) {
            xMultiplier = -1;
            angle = (2.0 * Math.PI) - angle;
        }

        endX = (int)(length * (Math.sin(angle))) * xMultiplier;
        endY = (int)(length * (Math.cos(angle))) * yMultiplier;

        return new Point(endX, endY);

    }
    
    public static double getDistance(double X1, double Y1, double X2, double Y2) {
        return Math.sqrt((X1-X2)*(X1-X2) + (Y1-Y2)*(Y1-Y2));
    }
}
