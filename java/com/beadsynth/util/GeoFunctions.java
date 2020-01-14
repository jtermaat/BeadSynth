/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beadsynth.util;
//import java.awt.*;
/**
 *
 * @author John
 */
public class GeoFunctions {

//    public static void drawCircle(Canvas g, int centerX, int centerY, int radius) {
//        g.drawOval(centerX-radius, centerY-radius, centerX+radius, centerY+radius);
//    }
//
//    public static void fillCircle(Graphics g, int centerX, int centerY, int radius) {
//        g.fillOval(centerX-radius, centerY-radius, centerX+radius, centerY+radius);
//    }

    public static boolean isInCircle(int pointX, int pointY, int centerX, int centerY, int radius) {
        int distFromCenter = (int)Math.sqrt(((double)pointX-(double)centerX)*((double)pointX-(double)centerX) + ((double)pointY-(double)centerY)*((double)pointY-(double)centerY));
        return (distFromCenter <= radius);
    }
    
    public static double pointLineDistance(int pX, int pY, int startX, int startY, int endX, int endY) {
        return pointLineDistance((double)pX, (double)pY, (double)startX, (double)startY, (double)endX, (double)endY);
    }

    public static double pointLineDistance(double pX, double pY, double startX, double startY, double endX, double endY) {
        return Math.abs((endX-startX)*(startY-pY) - (startX-pX)*(endY-startY)) / Math.sqrt((endX-startX)*(endX-startX) + (endY-startY)*(endY-startY));
    }

    public static boolean lineCrossesCircle(int lineStartX, int lineStartY, int lineEndX, int lineEndY, int circleCenterX, int circleCenterY, int circleRadius) {
        double ptLineDist = pointLineDistance(circleCenterX, circleCenterY, lineStartX, lineStartY, lineEndX, lineEndY);
        return (circleRadius < ptLineDist);
    }

}
