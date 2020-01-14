package com.beadsynth.recording;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.beadsynth.view.R;

public class PrepareRecordingFragment extends DialogFragment {
	
	public static interface Listener {
		void onOkayRecord(String filenameToWrite);

		void onDeleteRecording(String filenameToDelete);
		
		void onCancelRecord();
	}
	
	public static String AUDIO_DIR_NAME = "beadsynth/";

	View view;
	boolean overwriteFile;
	boolean deleteFile;

	public PrepareRecordingFragment() {
		overwriteFile = false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		System.out.println("onCreate occurred");
		if (getActivity().getResources().getBoolean(R.bool.isLarge)) {
			setStyle(STYLE_NORMAL, android.R.style.Theme_Translucent);
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		if (getActivity().getResources().getBoolean(R.bool.isLarge)) {
			dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		} else {
			dialog.getWindow().setTitle("Record");
		}
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		System.out.println("onCreateView occurred");
		view = inflater.inflate(R.layout.save_recording_layout, container, false);
		
		if (getActivity().getResources().getBoolean(R.bool.isLarge)) {
			getDialog().getWindow().setLayout(417, 515);
		} 

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		populateExistingFilesList();
		((Button) view.findViewById(R.id.begin_record_button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (getActivity() instanceof Listener) {
							final String fileName = getFileNameText();
							if (fileName.endsWith(".wav")) {
								fileName.replaceAll(".wav", "");
							}
							if (isValidFileName(fileName)) {
								if (filenameExists(fileName + ".wav")) {
									System.out.println("filename exists: " + fileName);
									askUserForConfirmationAndOverwrite(fileName);
								} else {
									System.out.println("filename doesn't exist!" + fileName);
									((Listener) getActivity())
											.onOkayRecord(fileName);
									dismiss();
								}
							} else {
								warnInvalidFileName();
							}
						}
					}
				});

		((Button) view.findViewById(R.id.cancel_button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (getActivity() instanceof Listener) {
//							((Listener) getActivity()).onCancelRecord();
							dismiss();
						}
					}
				});
		
		((Spinner) view.findViewById(R.id.existing_recordings_spinner))
		.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				View fragmentView = PrepareRecordingFragment.this.view;
				EditText filenameField = (EditText)fragmentView.findViewById(R.id.filename_textfield);
				Spinner spinner = (Spinner)fragmentView.findViewById(R.id.existing_recordings_spinner);
				filenameField.setText(spinner.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private String getFileNameText() {
		System.out.println("view: " + view);
		System.out.println("viewId: " + R.id.filename_textfield);
		EditText filenameField = (EditText) view
				.findViewById(R.id.filename_textfield);
		System.out.println("filenameField: " + filenameField);
		System.out.println("filenameField.getContentDescription(): "
				+ filenameField.getContentDescription());
		return filenameField.getEditableText().toString();
		// return filenameField.getContentDescription().toString();
	}

	private boolean isValidFileName(String filename) {
		if (!isExternalStorageWritable()) {
			Context context = getActivity().getApplicationContext();
			Toast.makeText(context, "This device cannot currently write to external storage.", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private boolean filenameExists(String filename) {
		File folder = getMusicStorageDir();
		System.out.println("files Directory: " + folder);
		String[] existingFiles = folder.list();
		boolean exists = false;
		for (String file : existingFiles) {
			if (file.equals(filename)) {
				System.out.println("File: " + file + " being compared to " + filename);
				exists = true;
			}
		}
		return exists;
	}

	private void askUserForConfirmationToDelete(final String filename) {
		Context context = getActivity().getApplicationContext();
		AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.setTitle("Delete File?")
				.setMessage(
						"Are you sure you want to delete the file '" + filename
								+ "'?  This action cannot be undone.")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								setDeleteStatus(true);
								dismiss();
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								setDeleteStatus(false);
								dismiss();
							}
						}).show();
		final Activity act = getActivity();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				System.out.println("deleteFile: " + deleteFile);
				if (deleteFile) {
					System.out.println("filename: " + filename);
					System.out.println("getActivity(): " + getActivity());
					((Listener) act).onDeleteRecording(filename);
				}
			}
		});

	}

	private void setDeleteStatus(boolean delete) {
		deleteFile = delete;
	}

	private void askUserForConfirmationAndOverwrite(final String filename) {
		new AlertDialog.Builder(getActivity())
				.setTitle("Filename Exists")
				.setMessage(
						"A file called '"
								+ filename
								+ "' already exists.  Do you wish to overwrite the file?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								((Listener) getActivity()).onOkayRecord(filename);
								dismiss();
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).show();
	}

	private void warnInvalidFileName() {
		Context context = getActivity().getApplicationContext();
		Toast.makeText(context, "Invalid filename!", Toast.LENGTH_SHORT).show();
	}

	private void populateExistingFilesList() {
		Context context = getActivity().getApplicationContext();
		// NEED TO HANDLE DIRECTORY / PERMISSION ISSUES.  MAYBE JUST USE ANOTHER DIRECTORY
		System.out.println("Environment.DIRECTORY MUSIC: " + Environment.DIRECTORY_MUSIC);
		//File folder = new File(Environment.DIRECTORY_MUSIC);
		File folder = getMusicStorageDir();
		String[] existingFiles = folder.list();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_dropdown_item_1line, existingFiles);
		Spinner spinner = (Spinner) view
				.findViewById(R.id.existing_recordings_spinner);
		spinner.setAdapter(adapter);
	}

	private String getSelectedFileText() {
		Spinner spinner = (Spinner) view
				.findViewById(R.id.existing_recordings_spinner);
		return (String) spinner.getSelectedItem();
	}
	
	/* Checks if external storage is available for writing */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public static File getMusicStorageDir() {
	    // Get the directory for music
	    File file = new File(Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_MUSIC), AUDIO_DIR_NAME);
//		File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
	    if (!file.mkdirs()) {
	        System.out.println("Error. Directory " + file + " not created");
	    }
//		Context context = getActivity().getApplicationContext();
//		File directory = context.getFilesDir();
	    return file;
//		return directory;
	}
	
	public void onDismiss (DialogInterface dialog) {
		((Listener) getActivity()).onCancelRecord();
	}

}
