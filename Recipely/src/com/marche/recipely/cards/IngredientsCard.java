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
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marche.recipely.R;
import com.marche.recipely.libs.RowLayout;
import com.marche.recipely.recipes.Recipe;

public class IngredientsCard extends Card {

	Recipe recipe;
	Context context;


	public IngredientsCard(Context context, Recipe recipe) {
		this(context, R.layout.ingredient_content);
		this.recipe = recipe;
		this.context = context;
	}

	public IngredientsCard(Context context, int innerLayout) {
		super(context, innerLayout);
		init();
	}

	private void init() {
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		RowLayout row = (RowLayout) parent.findViewById(R.id.rowlayout);
		row.removeAllViews();
		
		for(int i = 0; i<recipe.ingredients.size(); i++){
			IngredientChip ingre = new IngredientChip(context, recipe.ingredients.get(i));
			row.addView(ingre);
		}
	}
}
