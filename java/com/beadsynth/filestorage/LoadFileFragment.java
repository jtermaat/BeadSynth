package com.beadsynth.filestorage;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.beadsynth.view.R;

public class LoadFileFragment extends DialogFragment {

	public static interface Listener {
		void onOkayLoad(String filenameToWrite);
	}

	View view;

	public LoadFileFragment() {
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
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.load_from_file_layout, container, false);
		if (getActivity().getResources().getBoolean(R.bool.isLarge)) {
			getDialog().getWindow().setLayout(417, 515);
		} else {
			// TODO: Convert string to resource.
			getDialog().getWindow().setTitle("Load State Settings File");
		}

		return view;
	}
	
	private void alertNoFileSelectedToLoad() {
		AlertDialog dialog = new AlertDialog.Builder(getActivity())
		.setTitle("No File Selected")
		.setMessage(
				"There is no file selected to load at this time.")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int whichButton) {
						dismiss();
					}
				}).show();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		populateFilesList();
		
		((Button) view.findViewById(R.id.load_button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (getActivity() instanceof Listener) {
							final String filename = getSelectedFile();
							if (filename != null) {
								Toast.makeText(LoadFileFragment.this.getActivity().getApplicationContext(), "filename: " + filename, Toast.LENGTH_SHORT).show();
								((Listener) getActivity()).onOkayLoad(filename);
							} else {
								Toast.makeText(LoadFileFragment.this.getActivity().getApplicationContext(), "filename: " + filename + ", Attempting to notify user.", Toast.LENGTH_SHORT).show();
								
								alertNoFileSelectedToLoad();
							}
							dismiss();
						}
					}
				});

		((Button) view.findViewById(R.id.cancel_button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dismiss();
					}
				});
	}

	private String getSelectedFile() {
		Spinner spinner = (Spinner)view.findViewById(R.id.state_files_spinner);
		return (String)spinner.getSelectedItem();
	}
	
	private void populateFilesList() {
		Context context = getActivity().getApplicationContext();
		File folder = context.getFilesDir();
		String[] existingFiles = folder.list();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, existingFiles);
		Spinner spinner = (Spinner)view.findViewById(R.id.state_files_spinner);
		spinner.setAdapter(adapter);
	}


}