package com.marche.recipely;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TimerBackgroundFragment extends Fragment {

	GlobalVar appState;

	public static TimerBackgroundFragment newInstance(int sectionNumber) {
		TimerBackgroundFragment fragment = new TimerBackgroundFragment();
		return fragment;
	}

	public TimerBackgroundFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.timer_background_fragment, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		appState = ((GlobalVar)getActivity().getApplicationContext());


	}
	
}
