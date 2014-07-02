package com.marche.recipely;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marche.recipely.libs.AutofitTextView;
import com.marche.recipely.recipes.Step;

public class TimerFragment extends Fragment {

	GlobalVar appState;
	AutofitTextView desc;
	static Step step;
	
	int secondsRemaining = -1;

	public static TimerFragment newInstance(Step step) {
		TimerFragment fragment = new TimerFragment();
		TimerFragment.step = step;
		return fragment;
	}
	
	public TimerFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.timer_fragment, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		appState = ((GlobalVar)getActivity().getApplicationContext());
		
		secondsRemaining = step.timer.get(0);
		desc = (AutofitTextView) getActivity().findViewById(R.id.textView1);
		desc.setMaxLines(1);
		desc.setMaxEms(80);
		desc.setMinEms(8);
		desc.setText(formatTime(secondsRemaining));

		ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

		/*This schedules a runnable task every second*/
		scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				secondsRemaining--;
				final String time = formatTime(secondsRemaining);
				
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						desc.setText(time);
					}
				});
			}
		}, 1, 1, TimeUnit.SECONDS);

	}
	
	public String formatTime(int seconds){
		  int hr = (int)(seconds / 3600);
		  int rem = (int)(seconds % 3600);
		  int mn = rem/60;
		  int sec = rem%60;
		  String hrStr = (hr<10 ? "0" : "")+hr;
		  String mnStr = (mn<10 ? "0" : "")+mn;
		  String secStr = (sec<10 ? "0" : "")+sec; 
		  
		  String time = "";
		  
		  if(hrStr.equals("00") == false){
			  time = hrStr + ":";
		  }
		  
		  return time + mnStr + ":" + secStr;
	}
	
}
