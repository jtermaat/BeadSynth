package com.beadsynth.filestorage;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.beadsynth.recording.PrepareRecordingFragment;
import com.beadsynth.view.R;

public class SaveFileFragment extends DialogFragment {

	public static interface Listener {
		void onOkaySave(String filenameToWrite);

		void onDelete(String filenameToDelete);
	}

	View view;
	boolean overwriteFile;
	boolean deleteFile;

	public SaveFileFragment() {
		overwriteFile = false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("onCreate occurred");
		if (getActivity().getResources().getBoolean(R.bool.isLarge)) {
			setStyle(STYLE_NORMAL, android.R.style.Theme_Translucent);
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		if (getActivity().getResources().getBoolean(R.bool.isLarge)) {
			dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		}
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("onCreateView occurred");
		view = inflater.inflate(R.layout.save_to_file_layout, container, false);
		if (getActivity().getResources().getBoolean(R.bool.isLarge)) {
			getDialog().getWindow().setLayout(417, 515);
		} else {
			getDialog().getWindow().setTitle(getResources().getString(R.string.save_file_dialog_title_text));
		}

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		populateExistingFilesList();
		((Button) view.findViewById(R.id.save_button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (getActivity() instanceof Listener) {
							final String fileName = getFileNameText();
							if (isValidFileName(fileName)) {
								if (filenameExists(fileName)) {
									askUserForConfirmationAndOverwrite(fileName);
								} else {
									((Listener) getActivity())
											.onOkaySave(fileName);
									dismiss();
								}
							} else {
								warnInvalidFileName();
							}
						}
					}
				});

		((Button) view.findViewById(R.id.delete_file_button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (getActivity() instanceof Listener) {
							final String fileName = getSelectedFileText();
							if (fileName != null) {
								askUserForConfirmationToDelete(fileName);
							} else {
								alertNoFileSelectedToDelete();
							}
						}
					}
				});

		((Button) view.findViewById(R.id.cancel_button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dismiss();
					}
				});
		
		((Spinner) view.findViewById(R.id.existing_state_files_spinner))
		.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				View fragmentView = SaveFileFragment.this.view;
				EditText filenameField = (EditText)fragmentView.findViewById(R.id.filename_textfield);
				Spinner spinner = (Spinner)fragmentView.findViewById(R.id.existing_state_files_spinner);
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

		return true;
	}

	private boolean filenameExists(String filename) {
		Context context = getActivity().getApplicationContext();
		File folder = context.getFilesDir();
		String[] existingFiles = folder.list();
		boolean exists = false;
		for (String file : existingFiles) {
			if (file.equals(filename)) {
				exists = true;
			}
		}
		return exists;
	}
	
	private void alertNoFileSelectedToDelete() {
		AlertDialog dialog = new AlertDialog.Builder(getActivity())
		.setTitle("No File Selected")
		.setMessage(
				"There is no file selected to delete at this time.")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int whichButton) {
						dismiss();
					}
				}).show();
	}

	private void askUserForConfirmationToDelete(final String filename) {
//		Context context = getActivity().getApplicationContext();
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
					((Listener) act).onDelete(filename);
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
								((Listener) getActivity()).onOkaySave(filename);
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
		File folder = context.getFilesDir();
		String[] existingFiles = folder.list();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_dropdown_item_1line, existingFiles);
		Spinner spinner = (Spinner) view
				.findViewById(R.id.existing_state_files_spinner);
		spinner.setAdapter(adapter);
		
//		spinner.setLayerPaint(paint)
	}

	private String getSelectedFileText() {
		Spinner spinner = (Spinner) view
				.findViewById(R.id.existing_state_files_spinner);
		return (String) spinner.getSelectedItem();
	}

}
