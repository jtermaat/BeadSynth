package com.beadsynth.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.beadsynth.buttons.ButtonArrangementManager;
import com.beadsynth.buttons.SidePanelButtonGraphicsManager;
import com.beadsynth.configuration.ConfigurationData;
import com.beadsynth.configuration.ConfigurationFragment;
import com.beadsynth.filestorage.LoadFileFragment;
import com.beadsynth.filestorage.SaveFileFragment;
import com.beadsynth.filestorage.SavedSettings;
import com.beadsynth.filestorage.SettingsFileReader;
import com.beadsynth.keyboard.KeyboardRangeBar;
import com.beadsynth.keyboard.NoteSnapControl;
import com.beadsynth.physics.Bead;
import com.beadsynth.proto_tutorial.WalkthroughActivity;
import com.beadsynth.recording.AudioFileWriter;
import com.beadsynth.recording.PrepareRecordingFragment;
import com.beadsynth.tutorial.TutorialSubjectLocationData;
import com.beadsynth.util.ProportionalRect;

public class BeadActivity extends Activity implements
		ConfigurationFragment.Listener, SaveFileFragment.Listener,
		LoadFileFragment.Listener, PrepareRecordingFragment.Listener,
		ViewTreeObserver.OnGlobalLayoutListener {

	SoundBoard board;
	BeadView beadView;
	BackgroundView backgroundView;
//	TutorialFragment tutFragment;
//	TutorialView tutorialView;
	
	int screenWidth;
	int screenHeight;
	ConfigurationFragment settingsDialogFragment;
	PrepareRecordingFragment recordingFragment;
	SaveFileFragment saveFileFragment;
	LoadFileFragment loadFileFragment;
	
	Button[] beadSnapButtons;
	SidePanelButtonGraphicsManager sidePanelGraphics;
	
	Button[] noteSnapButtons;
	SidePanelButtonGraphicsManager sidePanelGraphicsBottom;
	
	AudioFileWriter audioFileWriter;
	
	ViewTreeObserver viewTreeObserver;
	boolean needButtonRedraw;
	
	// TEST
	Activity thisActivity;
	
	boolean buttonControlCreated;
	View rootView;

	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		thisActivity = this;
		
		board = new SoundBoard(this.getApplicationContext());
		setContentView(R.layout.synth_screen);
		backgroundView = (BackgroundView) findViewById(R.id.synth_screen_view1);
		beadView = (BeadView) findViewById(R.id.synth_screen_view_foreground);
		
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
		
		//		tutFragment = new TutorialFragment();
		
		
		// Temporary til we figure this out.
//		getFragmentManager().beginTransaction().replace(R.id.tutorial_fragment, tutFragment).commit();

		
		//		tutorialView = (TutorialView) findViewById(R.id.popup_view);
//		tutorialView.setActivity(this);
		
//		beadView = (BeadView) findViewById(R.id.synth_screen_view1);
//		beadView = new BeadView(this.getApplicationContext(), board);
		beadView.setup(board, this);
/*		beadView.setBoard(board);*/
		backgroundView.setBoard(board);

		KeyboardRangeBar keyboardRangeBar = new KeyboardRangeBar(
				this.getApplicationContext(), this);
		
		beadSnapButtons = new Button[10];
		beadSnapButtons[0] = (Button)findViewById(R.id.snap_1);
		beadSnapButtons[1] = (Button)findViewById(R.id.snap_2);
		beadSnapButtons[2] = (Button)findViewById(R.id.snap_3);
		beadSnapButtons[3] = (Button)findViewById(R.id.snap_4);
		beadSnapButtons[4] = (Button)findViewById(R.id.snap_5);
		beadSnapButtons[5] = (Button)findViewById(R.id.snap_6);
		beadSnapButtons[6] = (Button)findViewById(R.id.snap_7);
		beadSnapButtons[7] = (Button)findViewById(R.id.snap_8);
		beadSnapButtons[8] = (Button)findViewById(R.id.take_snapshot);
		beadSnapButtons[9] = (Button)findViewById(R.id.clear_snapshot);
		
		noteSnapButtons = new Button[10];
		noteSnapButtons[0] = (Button)findViewById(R.id.note_snap_1);
		noteSnapButtons[1] = (Button)findViewById(R.id.note_snap_2);
		noteSnapButtons[2] = (Button)findViewById(R.id.note_snap_3);
		noteSnapButtons[3] = (Button)findViewById(R.id.note_snap_4);
		noteSnapButtons[4] = (Button)findViewById(R.id.note_snap_5);
		noteSnapButtons[5] = (Button)findViewById(R.id.note_snap_6);
		noteSnapButtons[6] = (Button)findViewById(R.id.note_snap_7);
		noteSnapButtons[7] = (Button)findViewById(R.id.note_snap_8);
		noteSnapButtons[8] = (Button)findViewById(R.id.keyboard_snapshot_button);
		noteSnapButtons[9] = (Button)findViewById(R.id.keyboard_clear_button);
		
		// Set onClick events
		findViewById(R.id.connect_button).setOnClickListener(buttonListener);
		findViewById(R.id.lock_button).setOnClickListener(buttonListener);
		findViewById(R.id.take_snapshot).setOnClickListener(buttonListener);
		findViewById(R.id.clear_snapshot).setOnClickListener(buttonListener);
		findViewById(R.id.snap_1).setOnClickListener(buttonListener);
		findViewById(R.id.snap_2).setOnClickListener(buttonListener);
		findViewById(R.id.snap_3).setOnClickListener(buttonListener);
		findViewById(R.id.snap_4).setOnClickListener(buttonListener);
		findViewById(R.id.snap_5).setOnClickListener(buttonListener);
		findViewById(R.id.snap_6).setOnClickListener(buttonListener);
		findViewById(R.id.snap_7).setOnClickListener(buttonListener);
		findViewById(R.id.snap_8).setOnClickListener(buttonListener);
		findViewById(R.id.beat_0).setOnClickListener(buttonListener);
		findViewById(R.id.beat_1).setOnClickListener(buttonListener);
		findViewById(R.id.beat_2).setOnClickListener(buttonListener);
		findViewById(R.id.beat_3).setOnClickListener(buttonListener);
		findViewById(R.id.beat_4).setOnClickListener(buttonListener);
		findViewById(R.id.beat_5).setOnClickListener(buttonListener);
		findViewById(R.id.beat_6).setOnClickListener(buttonListener);
		findViewById(R.id.beat_7).setOnClickListener(buttonListener);

		findViewById(R.id.keyboard_snapshot_button).setOnClickListener(
				keyboardButtonListener);
		findViewById(R.id.keyboard_clear_button).setOnClickListener(keyboardButtonListener);
		findViewById(R.id.note_snap_1).setOnClickListener(
				keyboardButtonListener);
		findViewById(R.id.note_snap_2).setOnClickListener(
				keyboardButtonListener);
		findViewById(R.id.note_snap_3).setOnClickListener(
				keyboardButtonListener);
		findViewById(R.id.note_snap_4).setOnClickListener(
				keyboardButtonListener);
		findViewById(R.id.note_snap_5).setOnClickListener(
				keyboardButtonListener);
		findViewById(R.id.note_snap_6).setOnClickListener(
				keyboardButtonListener);
		findViewById(R.id.note_snap_7).setOnClickListener(
				keyboardButtonListener);
		findViewById(R.id.note_snap_8).setOnClickListener(
				keyboardButtonListener);

		findViewById(R.id.settings_button).setOnClickListener(
				settingsButtonListener);

		System.out.println("addbead: " + R.id.add_bead + ", onClickListener: " + addBeadButtonListener);
		System.out.println("removebead: " + R.id.remove_bead + ", onClickListener: " + removeBeadButtonListener);
		findViewById(R.id.add_bead).setOnClickListener(addBeadButtonListener);
		findViewById(R.id.remove_bead).setOnClickListener(removeBeadButtonListener);
		
		findViewById(R.id.record_button).setOnClickListener(recordButtonListener);
		findViewById(R.id.help_button).setOnClickListener(helpButtonListener);

		beadView.setKeyboardRangeBar(keyboardRangeBar);

		Intent intent = this.getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			boolean hasSettings = extras.getBoolean("containsLoadString");
			if (hasSettings) {
				String loadFromFileName = extras.getString("fileToLoad");
				SettingsFileReader reader = new SettingsFileReader();
				System.out.println("TRYING TO READ SETTINGS FILE!!!!!");
				try {
					SavedSettings settings = reader.readFile(loadFromFileName,
							getApplicationContext());
					System.out.println("ABOUT TO REVERT TO SAVED SETTINGS!!!!!");
					beadView.revertToSavedSettings(settings);
					System.out.println("OSTENSIBLY LOADED SAVED SETTINGS SUCCESSFULLY!!!!!!!");
				} catch (IOException ie) {
					Toast.makeText(getApplicationContext(),
							"Error loading file!", Toast.LENGTH_SHORT).show();
				}
			}
		}
//		viewTreeObserver = backgroundView.getViewTreeObserver();
//		viewTreeObserver.addOnGlobalLayoutListener(this);
	}

	@SuppressLint("InlinedApi")
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		// ** DONE IN ONCREATE AS WELL
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
		
		if (!buttonControlCreated)  {
			ButtonControl buttonControl = new ButtonControl(this);
//			beadView.setButtonControl(buttonControl);
			backgroundView.setButtonControl(buttonControl);
			beadView.setButtonControl(buttonControl);
			NoteSnapControl snapControl = new NoteSnapControl(this);
//			beadView.setNoteSnapControl(snapControl);
			backgroundView.setNoteSnapControl(snapControl);
			
//			sidePanelGraphics = new SidePanelButtonGraphicsManager(beadSnapButtons, getApplicationContext());
//			sidePanelGraphicsBottom = new SidePanelButtonGraphicsManager(noteSnapButtons, getApplicationContext());
/*			beadView.setSidePanelGraphics(sidePanelGraphics);
			beadView.setSidePanelGraphicsBottom(sidePanelGraphicsBottom);*/
//			backgroundView.setSidePanelGraphics(sidePanelGraphics);
//			backgroundView.setSidePanelGraphicsBottom(sidePanelGraphicsBottom);
			audioFileWriter = beadView.getAudioFileWriter();
			
//			screenWidth = tutorialView.getWidth();
//			screenHeight = tutorialView.getHeight();
			
//			Toast.makeText(this, "isLarge: " + getResources().getBoolean(R.bool.isLarge), Toast.LENGTH_LONG).show();
			
			try {
				
				// Temporarily removing custom buttons!!!!
			
//			List<CustomButton> customButtons = new ArrayList<CustomButton>();
//			Bitmap plusBitmap = CommonFunctions.loadBitmap("button_plus", getApplicationContext());
//			Button plusButton = (Button)findViewById(R.id.add_bead);
//			CustomButton plusCustom = new CustomButton(plusButton, plusBitmap, plusBitmap);
//			customButtons.add(plusCustom);
//			
//			Bitmap minusBitmap = CommonFunctions.loadBitmap("button_minus", getApplicationContext());
//			Button minusButton = (Button)findViewById(R.id.remove_bead);
//			CustomButton minusCustom = new CustomButton(minusButton, minusBitmap, minusBitmap);
//			customButtons.add(minusCustom);
//			
//			Bitmap recordBitmap = CommonFunctions.loadBitmap("button_record", getApplicationContext());
//			Button recordButton = (Button)findViewById(R.id.record_button);
//			CustomButton recordCustom = new CustomButton(recordButton, recordBitmap, recordBitmap);
//			customButtons.add(recordCustom);
//			
//			Bitmap settingsBitmap = CommonFunctions.loadBitmap("button_settings", getApplicationContext());
//			Button settingsButton = (Button)findViewById(R.id.settings_button);
//			CustomButton settingsCustom = new CustomButton(settingsButton, settingsBitmap, settingsBitmap);
//			customButtons.add(settingsCustom);
//			
//			Bitmap helpBitmap = CommonFunctions.loadBitmap("button_help", getApplicationContext());
//			Button helpButton = (Button)findViewById(R.id.help_button);
//			CustomButton helpCustom = new CustomButton(helpButton, helpBitmap, helpBitmap);
//			customButtons.add(helpCustom);
//			
//			Bitmap lockBitmap = CommonFunctions.loadBitmap("button_lockbead", getApplicationContext());
//			Bitmap unlockBitmap = CommonFunctions.loadBitmap("button_unlockbead", getApplicationContext());
//			ToggleButton lockButton = (ToggleButton)findViewById(R.id.lock_button);
//			CustomToggleButton lockCustom = new CustomToggleButton(lockButton, lockBitmap, lockBitmap, unlockBitmap);
//			customButtons.add(lockCustom);
//			
//			Bitmap connectBitmap = CommonFunctions.loadBitmap("button_connect", getApplicationContext());
//			ToggleButton connectButton = (ToggleButton)findViewById(R.id.connect_button);
//			CustomToggleButton connectCustom = new CustomToggleButton(connectButton, connectBitmap, connectBitmap, connectBitmap);
//			customButtons.add(connectCustom);
//			
////			beadView.setCustomButtons(customButtons);
//			backgroundView.setCustomButtons(customButtons);
//			prepareTutorialView();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			buttonControlCreated = true;
		}
	}

	private OnClickListener buttonListener = new OnClickListener() {
		public void onClick(View v) {
//			beadView.respondToButtonClick(v);
			backgroundView.respondToButtonClick(v);
			layoutRelativeButtons();
			needButtonRedraw = true;
//			backgroundView.invalidate();
		}
	};

	private OnClickListener keyboardButtonListener = new OnClickListener() {
		public void onClick(View v) {
//			beadView.respondToKeyboardRelatedButtonClick(v);
			backgroundView.respondToKeyboardRelatedButtonClick(v);
		}
	};

	private OnClickListener settingsButtonListener = new OnClickListener() {
		public void onClick(View view) {
			beadView.pause();
			showSettingsDialog();
		}

	};

	private OnClickListener addBeadButtonListener = new OnClickListener() {
		public void onClick(View v) {
			System.out.println("calling beadview add bead");
			beadView.addBead();
		}
	};

	private OnClickListener removeBeadButtonListener = new OnClickListener() {
		public void onClick(View v) {
			beadView.removeBead();
		}
	};
	
	private OnClickListener recordButtonListener = new OnClickListener() {
		public void onClick(View v) {
			if (beadView.isRecording) {
				try {
					audioFileWriter.finalizeFile();
					Toast.makeText(BeadActivity.this, "Completed recording.", Toast.LENGTH_SHORT).show();
				} catch (IOException ie) {
					System.out.println("error finalizing file");
					Toast.makeText(BeadActivity.this, "Sorry, there was an error saving this recording.",  Toast.LENGTH_SHORT).show();
				}
				beadView.setRecording(false);
			} else {
				showRecordDialog();
			}
		}
	};
	
	private void getAllViewRelativeLocations() {
		ViewGroup fullView = (ViewGroup)getWindow().getDecorView().findViewById(android.R.id.content);
		List<View> allViews = fullView.getTouchables();
//		allViews = new ArrayList<View>();
//		int numChildren = fullView.getChildCount();
//		for (int i = 0; i < numChildren; i ++) {
//			allViews.add(fullView.getChildAt(i));
//		}
		for (View view : allViews) {
			System.out.println("view id in hex: " + Integer.toHexString(view.getId()));
//			System.out.println("view (left, top): (" + view.getLeft() + ", " + view.getTop() + ")");
			double leftAsFraction = (double)view.getLeft()/(double)backgroundView.screenWidth;
			double topAsFraction = (double)view.getTop()/(double)backgroundView.screenHeight;
			double rightAsFraction = (double)view.getRight()/(double)backgroundView.screenWidth;
			double bottomAsFraction = (double)view.getBottom()/(double)backgroundView.screenHeight;
			System.out.println("(" + leftAsFraction + ", " + topAsFraction + "); (" + rightAsFraction + ", " + bottomAsFraction + ")");
		}
	}
	
	private OnClickListener helpButtonListener = new OnClickListener() {
		public void onClick(View v) {
//			System.out.println("hit help button listener");
//			BeadActivity.this.getAllViewRelativeLocations();
//			ButtonArrangementManager arrangementManager = new ButtonArrangementManager(backgroundView.screenWidth, backgroundView.screenHeight);
//			arrangementManager.arrangeButtonsRelatively(BeadActivity.this);
//			backgroundView.setTopButtonsRect(arrangementManager.getTopProportionalRect());
//			backgroundView.invalidate();
//			if (!tutorialView.isActive()) {
//				ViewGroup vg = (ViewGroup)findViewById(R.id.synth_screen_view1).getRootView();     
////				RelativeLayout vL = (RelativeLayout)mOverflowListView.getParent();
////				vg.removeView(v);
//				
//				
//				System.out.println("tutorialView was not activated.");
//				tutorialView.startTutorial(vg, thisActivity);
//			} else {
//				System.out.println("Calling next step");
//				System.out.println("TUTORIAL VIEW WAS ACTIVATED");
//				tutorialView.nextStep();
//			}
/*			if (!tutFragment.isStarted()) {
			TutorialSubjectLocationData subjectData = getTutorialSubjectData();
			
			
			tutFragment.setTutorialSubjectData(subjectData);
			tutFragment.startTutorial();
			} else {
				tutFragment.nextStep();
			}*/
			
			Intent tutorialIntent = new Intent(BeadActivity.this, WalkthroughActivity.class);
//			mainIntent.putExtra("containsLoadString", true);
//			mainIntent.putExtra("fileToLoad", filename);
	        BeadActivity.this.startActivity(tutorialIntent);
	        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	        BeadActivity.this.finish();
			
		}
	};
	
	private void showRecordDialog() {
		beadView.pause();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("record");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		recordingFragment = new PrepareRecordingFragment();
		
		recordingFragment.show(ft, "record");	
		
	}

	private void showSettingsDialog() {
		beadView.pause();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		settingsDialogFragment = new ConfigurationFragment();
		settingsDialogFragment.setConfigurationData(beadView
				.getConfigurationData());

		settingsDialogFragment.show(ft, "dialog");
		
//		Rect displayRectangle = new Rect();
//		Window window = this.getWindow();
//		window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//		
//		settingsDialogFragment.getDialog().getWindow().setLayout(displayRectangle.width(), displayRectangle.height());
//		settingsDialogFragment.getDialog().setTitle("Settings");
	}

	@Override
	public void onOkay(ConfigurationData result) {
		beadView.setConfigurationData(result);
//		settingsDialogFragment.dismissAllowingStateLoss();
//		beadView.resume();
	}

	@Override
	public void onSaveFile() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("save");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		saveFileFragment = new SaveFileFragment();

		saveFileFragment.show(ft, "save");
	}

	@Override
	public void onLoadFile() {
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

	@Override
	public void onOkayLoad(String fileToLoad) {
		System.out.println("starting to load");
		SettingsFileReader reader = new SettingsFileReader();
		try {
			System.out.println("getting ready to read");
			SavedSettings settings = reader.readFile(fileToLoad,
					this.getApplicationContext());
			System.out.println("successfully read");
			beadView.revertToSavedSettings(settings);
			backgroundView.revertToSavedSettings(settings);
			System.out.println("supposedly reverted");
		} catch (IOException ie) {
			System.out.println("IOException");
			ie.printStackTrace();
			Toast.makeText(getApplicationContext(), "Error reading file!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onCancel() {
		settingsDialogFragment.dismissAllowingStateLoss();
		beadView.resume();
	}

	@Override
	public void onOkaySave(String filename) {
		beadView.saveSettingsToFile(filename);
		saveFileFragment.dismissAllowingStateLoss();
	}
	
	@Override 
	public void onOkayRecord(String filename) {
		try {
			audioFileWriter.beginWritingFile(filename);
		} catch (IOException ie) {
			System.out.println("Error starting recording");
			Toast.makeText(BeadActivity.this, "There was an error starting this recording!", Toast.LENGTH_SHORT).show();
		}
//		recordingFragment.dismissAllowingStateLoss();
//		beadView.resume();
		beadView.setRecording(true);
	}
	
	@Override
	public void onCancelRecord() {
		recordingFragment.dismissAllowingStateLoss();
		beadView.resume();
	}
	
	@Override
	public void onDeleteRecording(String filename) {
		String musicDirectory = Environment.DIRECTORY_MUSIC;
		Context context = getApplicationContext();
		File dir = new File(musicDirectory);
		File file = new File(dir, filename);
		if (!file.delete()) {
			Toast.makeText(context, "Error deleting file!", Toast.LENGTH_SHORT)
					.show();
		}
		beadView.resume();
	}

	@Override
	public void onDelete(String fileToDelete) {
		Context context = getApplicationContext();
		File dir = context.getFilesDir();
		File file = new File(dir, fileToDelete);
		if (!file.delete()) {
			Toast.makeText(context, "Error deleting file!", Toast.LENGTH_SHORT)
					.show();
		}

	}

	// Standard Activity Overrides
	@Override
	protected void onDestroy() {
		System.out.println("pre super ondestroy");
		super.onDestroy();
		System.out.println("onDestroy called");
		beadView.destroy();
		System.out.println("thread destruction complete");
	}

	@Override
	protected void onPause() {
		this.getAllViewRelativeLocations();
		super.onPause();
		beadView.pause();
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
        // Regardless of change, must re-hide status bar
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
        return super.dispatchKeyEvent(event);
        
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
		
        	View decorView = getWindow().getDecorView();
        	// Hide the status bar.
        	int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        	decorView.setSystemUiVisibility(uiOptions);
        }
		return super.dispatchKeyShortcutEvent(event);
	}
	
//	@SuppressLint("InlinedApi")
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//	    int action = event.getAction();
//	    int keyCode = event.getKeyCode();
//	        switch (keyCode) {
//	        case KeyEvent.KEYCODE_VOLUME_UP:
//	            if (action == KeyEvent.ACTION_DOWN) {
//	                // Regardless of change, must re-hide status bar
//	        		// Need to remove status bar in addition to action bar
//	                if (Build.VERSION.SDK_INT < 16) {
//	                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//	                } else {
//	        		
//	                	View decorView = getWindow().getDecorView();
//	                	// Hide the status bar.
//	                	int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//	                	decorView.setSystemUiVisibility(uiOptions);
//	                }
//	            }
//	            return super.dispatchKeyEvent(event);
//	        case KeyEvent.KEYCODE_VOLUME_DOWN:
//	            if (action == KeyEvent.ACTION_DOWN) {
//	        		// Need to remove status bar in addition to action bar
//	                if (Build.VERSION.SDK_INT < 16) {
//	                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//	                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//	                } else {
//	        		
//	                	View decorView = getWindow().getDecorView();
//	                	// Hide the status bar.
//	                	int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//	                	decorView.setSystemUiVisibility(uiOptions);
//	                }
//	            }
//	            return super.dispatchKeyEvent(event);
//	        default:
//        		// Need to remove status bar in addition to action bar
//                if (Build.VERSION.SDK_INT < 16) {
//                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                } else {
//        		
//                	View decorView = getWindow().getDecorView();
//                	// Hide the status bar.
//                	int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//                	decorView.setSystemUiVisibility(uiOptions);
//                }
//	            return super.dispatchKeyEvent(event);
//	        }
//	    }

	@SuppressLint("InlinedApi")
	@Override
	protected void onResume() {
		super.onResume();
		// ** DONE IN ONCREATE AS WELL
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
		
		beadView.resume();
		rootView = getWindow().getDecorView().findViewById(android.R.id.content);
		viewTreeObserver = rootView.getViewTreeObserver();
		viewTreeObserver.addOnGlobalLayoutListener(this);
	}
	
	private TutorialSubjectLocationData getTutorialSubjectData() {
		TutorialSubjectLocationData subjectData = new TutorialSubjectLocationData(screenWidth, screenHeight);
		Bead tutorialBeadStart = board.getTutorialStartBead();
		Bead tutorialBeadSecond = board.getTutorialSecondBead();
		Button takeSnapshotButton = (Button)findViewById(R.id.take_snapshot);
		Button rhythmSelectButton = (Button)findViewById(R.id.beat_0);
		Button lockBeadsButton = (Button)findViewById(R.id.lock_button);
		Button connectBeadsButton = (Button)findViewById(R.id.connect_button);
		Button addBeadsButton = (Button)findViewById(R.id.add_bead);
		Button subtractBeadsButton = (Button)findViewById(R.id.remove_bead);
		Point keyboardPoint = board.getTutorialKeyboardPoint();
		Button keyboardSnapshotButton = (Button)findViewById(R.id.keyboard_snapshot_button);
		Button recordButton = (Button)findViewById(R.id.record_button);
		
		subjectData.setTutorialBeadStart(tutorialBeadStart);
		subjectData.setTutorialBeadSecond(tutorialBeadSecond);
		subjectData.setTakeSnapshotButton(takeSnapshotButton);
		subjectData.setRhythmSelectButton(rhythmSelectButton);
		subjectData.setLockBeadsButton(lockBeadsButton);
		subjectData.setConnectBeadsButton(connectBeadsButton);
		subjectData.setAddBeadsButton(addBeadsButton);
		subjectData.setSubtractBeadsButton(subtractBeadsButton);
		subjectData.setKeyboardPointerSpot(keyboardPoint);
		subjectData.setKeyboardSnapshotButton(keyboardSnapshotButton);
		subjectData.setRecordButton(recordButton);
		
		subjectData.populateListWithSubjects();
		
//		tutorialView.setTutorialSubectLocationData(subjectData);
		return subjectData;
	}

	@Override
	public void onGlobalLayout() {
//		System.out.println("YO!!! global layout called!!!");
		layoutRelativeButtons();
		ProportionalRect pRangeBarSpace =
				new ProportionalRect(beadView.getWidth(), beadView.getHeight(), 0.945, 0.078125, 0.99, 0.703125);
		System.out.println("AHHH screenWidth: " + beadView.getWidth());
		System.out.println("AHHH screenHeight: " + beadView.getHeight());
		
		beadView.setRangeBarPosition(pRangeBarSpace);
		// Set relative layout for buttons
//		BeadActivity.this.getAllViewRelativeLocations();
//		ButtonArrangementManager arrangementManager = new ButtonArrangementManager(beadView.screenWidth, beadView.screenHeight);
//		arrangementManager.arrangeButtonsRelatively(BeadActivity.this);
//		backgroundView.setTopButtonsRect(arrangementManager.getTopProportionalRect());
//		backgroundView.invalidate();
	}
	
	public void layoutRelativeButtons() {
		BeadActivity.this.getAllViewRelativeLocations();
		ButtonArrangementManager arrangementManager = new ButtonArrangementManager(beadView.screenWidth, beadView.screenHeight);
		arrangementManager.arrangeButtonsRelatively(BeadActivity.this);
		backgroundView.setTopButtonsRect(arrangementManager.getTopProportionalRect());
//		backgroundView.invalidate();		
	}
}
