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
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marche.recipely.R;
import com.marche.recipely.recipes.Recipe;

public class TitleCard extends Card {

	protected TextView desc;
	Boolean descOpen = false;
	Recipe recipe;

	/**
	 * Constructor with a custom inner layout
	 *
	 * @param context
	 */
	public TitleCard(Context context, Recipe recipe) {
		this(context, R.layout.title_content);
		this.recipe = recipe;
	}

	/**
	 * @param context
	 * @param innerLayout
	 */
	public TitleCard(Context context, int innerLayout) {
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
			desc.setText(recipe.notes);
			desc.setMaxLines(2);
			desc.setEllipsize(TruncateAt.END);
			
			desc.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(descOpen == false){
						desc.setMaxLines(100);
						desc.setEllipsize(null);
						desc.setText(recipe.notes);
						descOpen = true;
					} else{
						desc.setMaxLines(2);
						desc.setEllipsize(TruncateAt.END);
						desc.setText(recipe.notes);
						descOpen = false;
					}
				}
			});
		}
	}
}
