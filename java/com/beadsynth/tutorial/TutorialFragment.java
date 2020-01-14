package com.beadsynth.tutorial;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beadsynth.view.R;

public class TutorialFragment extends Fragment {
	
	TutorialView tutorialView;
	boolean started;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	started = false;
    
    	

        tutorialView = (TutorialView)inflater.inflate(R.layout.tutorial_view, container, false);
        return tutorialView;
    }
    
    public void setTutorialSubjectData(TutorialSubjectLocationData data) {
    	tutorialView.setTutorialSubectLocationData(data);
    }

	public void nextStep() {
		tutorialView.nextStep();
	}
	
	public void startTutorial() {
		started = true;
	}
	
	public boolean isStarted() {
		return started;
	}
}
