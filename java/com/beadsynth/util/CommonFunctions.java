package com.beadsynth.util;

import java.util.List;

import com.beadsynth.view.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.View;

public class CommonFunctions {

	public static Bitmap loadBitmap(String filename, Context context) {
		Resources resources = context.getResources();
		int drawableId = resources.getIdentifier(filename, "drawable", "com.beadsynth.view");
		Bitmap image = BitmapFactory.decodeResource(resources, drawableId);
		return image;
	}
	
	public static int positiveModulus(int x, int n) {
		int r = x % n;
		if (r < 0) {
			r += n;
		}
		return r;
	}
	
	public static boolean arrayContainsInt(int[] array, int target) {
		boolean found = false;
		for (int i = 0;i<array.length;i++) {
			if (target == array[i]) {
				found = true;
			}
		}
		return found;
	}
	
	public static void divideArrayContents(short[] array, int divideBy) {
		for (int i = 0;i<array.length;i++) {
			array[i] = (short)((float)array[i] / (float)divideBy);
		}
	}
	
	public static Rect getCombinedRect(List<Rect> rects) {
		if (rects.size() > 0) {
			Rect startRect = rects.get(0);
			for (Rect rect : rects) {
				startRect.union(rect);
			}
			return startRect;
		} else {
			return null;
		}
	}
	
	public static Rect getRectFromView(View v) {
		System.out.println("coordinates: " + v.getX() + ", " + v.getY() + ", " + v.getWidth() + ", " + v.getHeight());
		Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		return rect;
	}

	public static int getPipeheadRadius(Context context) {
		if (context.getResources().getBoolean(R.bool.isLarge)) {
			return 7;
		} else {
			return 10;
		}
	}

}
