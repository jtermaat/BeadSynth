/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beadsynth.util;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 *
 * @author John
 */
public class Functions {

    public static int max(int a, int b) {
        if (b > a)
            return b;
        else
            return a;
    }

    public static void fillArrayLine(short[] Array, int startIndex, short startValue, int endIndex, short endValue) {
        double percentThrough = 0.0;
        int totalDistance = endIndex - startIndex;
        int currentDistance;
        short valueDifference = (short)(endValue - startValue);
        for (int i = startIndex;i<endIndex;i++) {
            currentDistance = i - startIndex;
            percentThrough = (double)currentDistance / (double)totalDistance;
            Array[i] = (short)((short)(percentThrough * (double)valueDifference) + startValue);
            //System.out.println("Array[" + i + "]: " + Array[i] + "(start value: " + startValue + ", end value: " + endValue + ")");
        }
    }

    public static void fillLineBySine(short[] Array, int startIndex, short startValue, int endIndex, short endValue) {
        double A = ((double)startValue - (double)endValue);
        double ox = Math.PI / 2.0;
        double w = Math.PI * ((double)endIndex - (double)startIndex);
        int distance = endIndex - startIndex;
        for (int i = 0;i<distance;i++) {
            Array[startIndex + i] = (short)(A * Math.sin(w*(double)i + ox));
        }
    }

    // stopIndex is not affected
    public static void setArrayPartial(short[] startArray, short[] endArray, double percentThrough, short[] resultArray, int startIndex, int stopIndex) {
        for (int i = startIndex;i<stopIndex;i++) {
            short diff = (short)(endArray[i] - startArray[i]);
            resultArray[i] = (short)(startArray[i] + (short)(percentThrough * diff));
        }
    }

    public static void setArrayPartial(short[] beginArray, short[] endArray, double percentThrough, short[] resultArray, int beginStart, int beginStop, int endStart, int endStop, int resultStart, int resultStop) {
        for (int i = resultStart;i<resultStop;i++) {
            double percentThroughTraversal = (double)(i - resultStart)/(double)(resultStop-resultStart);
            int endDiff = endStop - endStart;
            int endIndex = endStart + (int)((double)endDiff * percentThroughTraversal);
            int beginDiff = beginStop - beginStart;
            int beginIndex = beginStart + (int)((double)beginDiff * percentThroughTraversal);
            short diff = (short)(endArray[endIndex] - beginArray[beginIndex]);
            resultArray[i] = (short)(beginArray[beginIndex] + (short)(percentThrough * diff));
        }
    }

    public static void setArrayPartial(short[] startArray, short[] endArray, double percentThrough, short[] resultArray) {
        setArrayPartial(startArray, endArray, percentThrough, resultArray, 0, startArray.length);
    }

        // returns number of maxima
    public static int calculateMaxima(short[] array, int[] maxima) {
        int numMaximum = 0;
        for (int i = 1;i<array.length;i++) {
            if (array[i] >= array[i-1] && array[i] >= array[i+1]) {
                maxima[numMaximum] = i;
                numMaximum++;
            }
            else if (array[i] <= array[i-1] && array[i] <= array[i+1]) {
                maxima[numMaximum] = i;
                numMaximum++;
            }
        }
        return numMaximum;
    }

    // first and last sample are called "maxima" TEMP: No they're not.
    public static int calculateMaxima(short[] array, int[] maxima, int minAmpDiff) {
        maxima[0] = 0;
        int numMaximum = 1; //1
        for (int i = 1;i<array.length-1;i++) {
            if (array[i] >= array[i-1] && array[i] >= array[i+1]) {
                if (Math.abs(array[i] - array[maxima[numMaximum-1]]) > minAmpDiff) {
                    maxima[numMaximum] = i;
                    numMaximum++;
                }
                else {
                    if (numMaximum > 1) {
                        numMaximum--;
                    }
                }
            }
            else if (array[i] <= array[i-1] && array[i] <= array[i+1]) {
                if (Math.abs(array[i] - array[maxima[numMaximum-1]]) > minAmpDiff) {
                    maxima[numMaximum] = i;
                    numMaximum++;
                }
                else {
                    if (numMaximum > 1) {
                        numMaximum--;
                    }
                }
            }
        }
        return numMaximum;
    }

    public static int getPeakMaximaIndex(short[] arrayData, int[] maxima, int numMaxima, int startMaximaIndex, int stopMaximaIndex) {
        int peakIndex = 0;
        for (int i = startMaximaIndex;i<stopMaximaIndex;i++) {
            if ((arrayData[maxima[i]]) > (arrayData[maxima[peakIndex]])) {
                peakIndex = i;
            }
        }
        return peakIndex;
    }

    public static short[] clipSound(short[] sound, short gate, int padLength) {
        return clipSound(sound, gate, 0, sound.length, padLength);
    }

    public static short[] clipSound(short[] sound, short gate, int soundStart, int soundStop, int padLength) {
        int clippedStart = soundStart;
        while(clippedStart < soundStop && Math.abs(sound[clippedStart])<gate) {
            clippedStart++;
        }
        int clippedStop = soundStop;
        while(clippedStop > soundStart+1 && Math.abs(sound[clippedStop-1])<gate) {
            clippedStop--;
        }
        //if (clippedStop < soundStop)
        //    clippedStop++; // to make sure it lies beyond the gate-breach

        if ((clippedStart - soundStart) > padLength)
            clippedStart -= padLength;
        else
            clippedStart = soundStart;

        if ((soundStop - clippedStop) > padLength)
            clippedStop += padLength;
        else
            clippedStop = soundStop;

        int clippedLength = clippedStop - clippedStart;
        //System.out.println("clippedStop: " + clippedStop);
        //System.out.println("clippedStart: " + clippedStart);
        short[] resultSound = new short[clippedLength];
        //System.out.println("resultSoundLength: " + resultSound.length);
        System.arraycopy(sound, clippedStart, resultSound, 0, clippedLength);
        return resultSound;
    }


    public static void drawWave(Canvas c, short[] array, short dataMax, Rect space) {
        int arrayLength = array.length;
        double xLength = space.width();
        double step = xLength / (double)arrayLength;
        double startX = space.left;
        double endX = startX + xLength;

        double topY = space.top;
        double bottomY = topY + space.height();
        double middleY = topY + (bottomY - topY)/2;
        double maxAmp = middleY - topY;


        double percentThrough;
        for (int i = 0;i<arrayLength-1;i++) {
            percentThrough = (double)i / (double)arrayLength;
            double thisX = startX + xLength * percentThrough;
            double nextX = thisX + step;
            double percentThisY = (double)array[i] / (double)dataMax;
            double percentNextY = (double)array[i+1] / (double)dataMax;
            double actualThisY = percentThisY * maxAmp + middleY;
            double actualNextY = percentNextY * maxAmp + middleY;
            c.drawLine((float)thisX, (float)actualThisY, (float)nextX, (float)actualNextY, new Paint());
        }

        c.drawRect((float)space.left, (float)space.top, (float)space.width(), (float)space.height(), new Paint());
        c.drawLine((float)startX, (float)middleY, (float)endX, (float)middleY, new Paint());
    }

    public static boolean isEven(int i) {
        return (i%2 == 0);
    }

    public static boolean isOdd(int i) {
        return (i%2 == 1);
    }

    public static short[] getLinedUpData(int numPieces, short[][] data, int[] pieceSizes) {
        int totalDataLength = sumArray(pieceSizes);
        //System.out.println("first Length: " + pieceSizes[0] + "totalLength: " + totalDataLength);
        short[] result = new short[totalDataLength];
        int currentStart = 0;
        for (int i = 0;i<numPieces;i++) {
            //System.out.println("piece: " + i + ", length: " + pieceSizes[i]);
            System.arraycopy(data[i], 0, result, currentStart, pieceSizes[i]);
            currentStart += pieceSizes[i];
        }
        return result;
    }

    public static int sumArray(int[] array) {
        int sum = 0;
        for (int i = 0;i<array.length;i++) {
            sum += array[i];
        }
        return sum;
    }
}
