package com.beadsynth.keyboard;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.beadsynth.buttons.ButtonArrangementManager;
import com.beadsynth.util.ProportionalRect;
import com.beadsynth.view.RangeSeekBar;
import com.beadsynth.view.RangeSeekBar.OnRangeSeekBarChangeListener;

public class KeyboardRangeBar {
	
	Context context;
	int currentMin;
	int currentMax;
	RangeSeekBar<Integer> seekBar;
	private static int MIN_KEYS_VISIBLE = 14;
	
	public KeyboardRangeBar(Context context, Activity activity) {
		this.context = context;
		currentMin = DynamicKeyboard.DEFAULT_START_KEY;
		currentMax = DynamicKeyboard.DEFAULT_END_KEY;
		
		// create RangeSeekBar as Integer range between 0 and 87 (the keyboard keys)
		seekBar = new RangeSeekBar<Integer>(0, DynamicKeyboard.NUM_KEYS-1, context);
		seekBar.setSelectedMinValue(currentMin);
		seekBar.setSelectedMaxValue(currentMax);
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
		        @Override
		        public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
		                // handle changed range values
		        	
		        	// Prevent less than specified static MIN_KEYS_VISIBLE.
		        		if (maxValue != currentMax) {
		        			// max changed.
		        			if (maxValue - currentMin <= MIN_KEYS_VISIBLE) {
		        				currentMax = currentMin + MIN_KEYS_VISIBLE;
		        				seekBar.setSelectedMaxValue(currentMax);
		        			} else {
		        				currentMax = maxValue;
		        			}
		        		} else if (minValue != currentMin) {
		        			if (currentMax - minValue <= MIN_KEYS_VISIBLE) {
		        				currentMin = currentMax - MIN_KEYS_VISIBLE;
		        				seekBar.setSelectedMinValue(currentMin);
		        			} else {
		        				currentMin = minValue;
		        			}
		        		}
		             // Prevent pushing out of bounds
		        		if (currentMax > DynamicKeyboard.NUM_KEYS - 1) {
		        			System.out.println("PUSHING CURRENT MAX");
		        			currentMax = DynamicKeyboard.NUM_KEYS - 1;
		        		}
		        		if (currentMin < 0) {
		        			System.out.println("PUSHING CURRENT MIN");
		        			currentMin = 0;
		        		}
		        }
		});

		// add RangeSeekBar to layout
		ViewGroup layout = (ViewGroup)activity.findViewById(com.beadsynth.view.R.id.synth_screen_layout);
			
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
		params.addRule(RelativeLayout.ALIGN_LEFT, com.beadsynth.view.R.id.beat_0);
		params.addRule(RelativeLayout.ALIGN_RIGHT, com.beadsynth.view.R.id.beat_7);
//		params.bottomMargin = 10;
		seekBar.setLayoutParams(params);

		layout.addView(seekBar, params);
	}
	
	public int getCurrentMin() {
		return this.currentMin;
	}
	
	public int getCurrentMax() {
		return this.currentMax;
	}
	
	public void setMin(int min) {
		seekBar.setSelectedMinValue(min);
		this.currentMin = min;
	}

	public void setMax(int max) {
		seekBar.setSelectedMaxValue(max);
		this.currentMax = max;
	}

	public void setProportionalPosition(ProportionalRect pRangeBarSpace) {
		ButtonArrangementManager.setViewToRect(seekBar, pRangeBarSpace.getRect());
	}

}
