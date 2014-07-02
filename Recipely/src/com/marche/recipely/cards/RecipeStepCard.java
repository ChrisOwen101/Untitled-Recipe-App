/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package com.marche.recipely.cards;

import it.gmariotti.cardslib.library.internal.Card;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marche.recipely.GlobalVar;
import com.marche.recipely.R;
import com.marche.recipely.recipes.Step;

public class RecipeStepCard extends Card {

	protected TextView desc;
	Boolean descOpen = false;
	Step step;
	GlobalVar appState;

	

	/**
	 * Constructor with a custom inner layout
	 *
	 * @param context
	 */
	public RecipeStepCard(Context context, Step step) {
		this(context, R.layout.step_content);
		this.step = step;
		
		appState = ((GlobalVar)context.getApplicationContext());

	}

	/**
	 * @param context
	 * @param innerLayout
	 */
	public RecipeStepCard(Context context, int innerLayout) {
		super(context, innerLayout);
		init();
	}

	/**
	 * Init
	 */
	private void init() {

		//No Header

		/*
        //Set a OnClickListener listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "Click Listener card=", Toast.LENGTH_LONG).show();
            }
        });*/
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		//Retrieve elements
		desc = (TextView) parent.findViewById(R.id.carddemo_myapps_main_inner_title);
		if (desc != null){
			desc.setText(step.step);
		}

		
		LinearLayout lay = (LinearLayout) parent.findViewById(R.id.timerLayout);
		if(lay != null && step.timer.size() > 0){
			lay.setVisibility(View.VISIBLE);
			
			ImageView timer = (ImageView) parent.findViewById(R.id.timer);
			timer.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					final int interval = 1000; // 1 Second
					Handler handler = new Handler();
					Runnable runnable = new Runnable(){
					    public void run() {
					    	Message msg = new Message();
					    	msg.arg1 = 2;
					    	msg.obj = step;
					    	appState.h.sendMessage(msg);
					    }
					};
					
					handler.postAtTime(runnable, System.currentTimeMillis()+interval);
					handler.postDelayed(runnable, interval);
				}
			});
		}
	}
}
