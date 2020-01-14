package com.beadsynth.tutorial;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.beadsynth.view.R;

public class MoveablePopup extends RelativeLayout {

	private LayoutInflater inflater = null;
	private EditText edit_text;
	private Button btn_clear;

	public MoveablePopup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews();
	}

	public MoveablePopup(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();

	}

	public MoveablePopup(Context context) {
		super(context);
		initViews();
	}

	private void initViews() {
		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		this.hide();
	}
	
	public void movePopup(Rect newLocation, String contentText, String headerText) {
		((TextView)findViewById(R.id.tutorial_content_text)).setText(contentText);
		((TextView)findViewById(R.id.tutorial_header_text)).setText(headerText);
		
		LayoutParams params = new RelativeLayout.LayoutParams(newLocation.width(), newLocation.height());
		params.leftMargin = newLocation.left;
		params.topMargin = newLocation.top;
		
		this.setLayoutParams(params);
		this.invalidate();
	}
	
	public void show() {
		this.setVisibility(VISIBLE);
	}
	
	public void hide() {
		this.setVisibility(INVISIBLE);
	}
}
