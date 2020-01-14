package com.beadsynth.configuration;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.beadsynth.view.R;

public class ConfigurationFragment extends DialogFragment {

	public static interface Listener {
		void onOkay(ConfigurationData result);

		void onCancel();
		
		void onSaveFile();
		
		void onLoadFile();
	}

	ConfigurationData configData;
	View view;

	public ConfigurationFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("onCreate occurred");
		// Set transluscent on non-phones.
		if (getActivity().getResources().getBoolean(R.bool.isLarge)) {
			setStyle(STYLE_NORMAL, android.R.style.Theme_Translucent);
		} else {
//			setStyle(R.style.BeadSynthDialogTheme, R.style.BeadSynthTheme);
//			setStyle(STYLE_NORMAL, R.style.BeadSynthTheme);
		}
	}
	
//	@Override
//	public void onStart()
//	{
//		// TODO: this should only be done for phone size screens!
//	    super.onStart();
//	    Dialog dialog = getDialog();
//	    if (dialog != null)
//	    {
//	        int width = ViewGroup.LayoutParams.MATCH_PARENT;
//	        int height = ViewGroup.LayoutParams.MATCH_PARENT;
////	        Toast.makeText(getActivity(), "width: " + width + ", " + "height: " + height, Toast.LENGTH_LONG).show();
////	        dialog.getWindow().setLayout(width, height);
////	        dialog.getWindow().setLayout(getActivity().getParent())
//	    }
//	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		// Set to feature no title if for tablet
		if (getActivity().getResources().getBoolean(R.bool.isLarge)) {
			dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		}
		return dialog;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("onCreateView occurred");
		view = inflater
				.inflate(R.layout.configuration_layout, container, false);
		//********* Set layout space if tablet, set title if Phone
		if(getActivity().getResources().getBoolean(R.bool.isLarge)) {
			getDialog().getWindow().setLayout(417, 515);
		} else {
			getDialog().setTitle("Settings");
		}
//		getDialog().getWindow().setLayout(417, 515);
//		Rect displayRectangle = new Rect();
//		Window window = getActivity().getWindow();
//		window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//		
//		getDialog().getWindow().setLayout(displayRectangle.width(), displayRectangle.height());
//		getDialog().setTitle("Settings");
//		getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		
		//*********
		
		
//		getDialog().set4
		// gonna have to use getDialog().setContentView to custom VIEW TODO
		
//		getDialog().setContentView(view);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		implementConfigurationData();
		
		// TODO: Only on phones
//        int width = ViewGroup.LayoutParams.MATCH_PARENT;
//        int height = ViewGroup.LayoutParams.MATCH_PARENT;
//        Toast.makeText(getActivity(), "width: " + width + ", " + "height: " + height, Toast.LENGTH_LONG).show();
//        getDialog().getWindow().setLayout(width, height);

		((Button) view.findViewById(R.id.save_button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (getActivity() instanceof Listener) {
							((Listener) getActivity()).onOkay(gatherData());
							dismiss();
						}
					}
				});

		((Button) view.findViewById(R.id.cancel_button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (getActivity() instanceof Listener) {
//							((Listener) getActivity()).onCancel();
							dismiss();
						}

					}
				});
		((Button) view.findViewById(R.id.save_to_file_button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (getActivity() instanceof Listener) {
							((Listener) getActivity()).onSaveFile();
						}
					}
				});
		((Button) view.findViewById(R.id.load_from_file_button))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (getActivity() instanceof Listener) {
							((Listener) getActivity()).onLoadFile();
						}
					}
				});
	}

	public void setConfigurationData(ConfigurationData data) {
		configData = data;
	}

	private void implementConfigurationData() {
		ConfigurationData data = configData;
		System.out.println("view: " + view);
		System.out.println("id: " + R.id.scale_tonic_spinner);
		System.out.println("subview: "
				+ view.findViewById(R.id.scale_tonic_spinner));
		System.out.println("spinner: "
				+ (Spinner) view.findViewById(R.id.scale_type_spinner));
		Spinner scaleTypeSpinner = (Spinner) view
				.findViewById(R.id.scale_type_spinner);
		Spinner scaleTonicSpinner = (Spinner) view
				.findViewById(R.id.scale_tonic_spinner);
		EditText bpmText = (EditText) view.findViewById(R.id.bpm_text);
		RadioGroup noteTypeSelection = (RadioGroup) view
				.findViewById(R.id.note_type_radiogroup);

		double bpm = data.getBpm();
		String bpmString = Double.toString(bpm);
		bpmText.setText(bpmString);

		System.out.println("data: " + data);
		System.out.println("data.getScaleType(): " + data.getScaleType());

		String scaleName = data.getScaleType().scaleName();
		for (int i = 0; i < scaleTypeSpinner.getCount(); i++) {
			if (scaleName.equals(scaleTypeSpinner.getItemAtPosition(i)
					.toString())) {
				scaleTypeSpinner.setSelection(i);
			}
		}

		String scaleLetter = data.getTonicNote().basicNote();
		for (int i = 0; i < scaleTonicSpinner.getCount(); i++) {
			if (scaleLetter.equals(scaleTonicSpinner.getItemAtPosition(i)
					.toString())) {
				scaleTonicSpinner.setSelection(i);
			}
		}

		int modifier = data.getTonicNote().modifier();

		if (modifier == ScaleNote.FLAT) {
			RadioButton flatButton = (RadioButton) view
					.findViewById(R.id.radio_button_flat);
			flatButton.setChecked(true);
		} else if (modifier == ScaleNote.SHARP) {
			RadioButton sharpButton = (RadioButton) view
					.findViewById(R.id.radio_button_sharp);
			sharpButton.setChecked(true);
		} else {
			RadioButton naturalButton = (RadioButton) view
					.findViewById(R.id.radio_button_natural);
			naturalButton.setChecked(true);
		}
	}

	private ConfigurationData gatherData() {
		Spinner scaleTypeSpinner = (Spinner) getView().findViewById(
				R.id.scale_type_spinner);
		Spinner scaleTonicSpinner = (Spinner) getView().findViewById(
				R.id.scale_tonic_spinner);
		EditText bpmText = (EditText) getView().findViewById(R.id.bpm_text);
		RadioGroup noteTypeSelection = (RadioGroup) getView().findViewById(
				R.id.note_type_radiogroup);

		String scaleTypeString = scaleTypeSpinner.getSelectedItem().toString();
		String scaleTonicString = scaleTonicSpinner.getSelectedItem()
				.toString();
		String bpmString = bpmText.getText().toString();
		int noteTypeSelectionId = noteTypeSelection.getCheckedRadioButtonId();
		int modifier = ScaleNote.NATURAL;
		if (noteTypeSelectionId == R.id.radio_button_natural) {
			modifier = ScaleNote.NATURAL;
		} else if (noteTypeSelectionId == R.id.radio_button_flat) {
			modifier = ScaleNote.FLAT;
		} else if (noteTypeSelectionId == R.id.radio_button_sharp) {
			modifier = ScaleNote.SHARP;
		}

		ScaleType scaleType = ScaleType.getFromString(scaleTypeString);
		ScaleNote tonicNote = ScaleNote.getByNoteAndModifier(scaleTonicString,
				modifier);
		double bpm = Double.parseDouble(bpmString);

		ConfigurationData data = new ConfigurationData(bpm, tonicNote,
				scaleType);
		return data;
	}
	
	public void onDismiss (DialogInterface dialog) {
		((Listener) getActivity()).onCancel();
	}
}
