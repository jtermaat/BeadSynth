package com.beadsynth.view;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import com.beadsynth.filestorage.LoadFileFragment;
import com.beadsynth.view.R;

public class SplashActivity extends Activity implements LoadFileFragment.Listener{
	
	LoadFileFragment loadFileFragment;

	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Need to remove status bar in addition to action bar
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
		
        	View decorView = getWindow().getDecorView();
        	// Hide the status bar.
        	int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        	decorView.setSystemUiVisibility(uiOptions);
        	
        }
		
			// Will need a new background to deal with this
        	ActionBar actionBar = getActionBar();
        	actionBar.hide();
		
		setContentView(R.layout.splash_screen);

		findViewById(R.id.begin_new_button).setOnClickListener(beginNewButtonListener);
		findViewById(R.id.load_existing_button).setOnClickListener(loadExistingButtonListener);
		findViewById(R.id.about_button).setOnClickListener(aboutButtonListener);
	}
	
	private OnClickListener beginNewButtonListener = new OnClickListener() {
		public void onClick(View v) {
            Intent mainIntent = new Intent(SplashActivity.this,BeadActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            SplashActivity.this.finish();
		}
	};
	
	private OnClickListener aboutButtonListener = new OnClickListener() {
		public void onClick(View v) {
            Intent mainIntent = new Intent(SplashActivity.this,BeadActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            SplashActivity.this.finish();
		}
	};
	
	private OnClickListener loadExistingButtonListener = new OnClickListener() {
		public void onClick(View v) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Fragment prev = getFragmentManager().findFragmentByTag("load");
			if (prev != null) {
				ft.remove(prev);
			}
			ft.addToBackStack(null);

			// Create and show the dialog.
			loadFileFragment = new LoadFileFragment();

			loadFileFragment.show(ft, "load");
		}
	};
	
	@Override
	public void onOkayLoad(String filename) {
		Intent mainIntent = new Intent(SplashActivity.this,BeadActivity.class);
		mainIntent.putExtra("containsLoadString", true);
		mainIntent.putExtra("fileToLoad", filename);
        SplashActivity.this.startActivity(mainIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        SplashActivity.this.finish();
	}
	
}
