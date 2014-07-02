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

import it.gmariotti.cardslib.library.internal.CardExpand;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marche.recipely.R;
import com.marche.recipely.recipes.Recipe;

public class TitleExpandCard extends CardExpand {
	
	Recipe recipe;

	public TitleExpandCard(Context context, Recipe recipe) {
		super(context, R.layout.title_card_expand);
		this.recipe = recipe;
	}

	//You can set you properties here (example buttons visibility)

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		if (view == null) return;

		//Retrieve TextView elements
		TextView sourceWebsite = (TextView) view.findViewById(R.id.sourceWebsite);
		TextView author = (TextView) view.findViewById(R.id.author);
		TextView sourceUrl = (TextView) view.findViewById(R.id.sourceUrl);
		TextView tx4 = (TextView) view.findViewById(R.id.carddemo_expand_text4);
		
		sourceWebsite.setText("Source Website: " + recipe.sourceWebsite);
		sourceWebsite.setVisibility(View.VISIBLE);

		if(recipe.author != null){
			author.setText("Author: " + recipe.author);
			author.setVisibility(View.VISIBLE);
		}
		
		sourceUrl.setText("Source Link: " + recipe.url);
		sourceUrl.setMaxLines(1);
		sourceUrl.setEllipsize(TruncateAt.END);
	    sourceUrl.setMovementMethod(LinkMovementMethod.getInstance());
	    sourceUrl.setVisibility(View.VISIBLE);
	}
}
