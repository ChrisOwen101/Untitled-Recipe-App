package com.marche.recipely.cards;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marche.recipely.R;
import com.marche.recipely.recipes.Ingredient;

public class IngredientChip extends LinearLayout {

	Ingredient ingre;
	Context context;

	public IngredientChip(Context context, Ingredient ingre) {
		super(context);
		this.context = context;
		this.ingre = ingre;

		View view = LayoutInflater.from(context).inflate(R.layout.ingredient_chip, null);
		TextView quantity = (TextView) view.findViewById(R.id.quantity);
		TextView unit = (TextView) view.findViewById(R.id.unit);
		TextView name = (TextView) view.findViewById(R.id.name);

		quantity.setText(ingre.quantity);
		
		if(ingre.quantity != null && (ingre.quantity.isEmpty() || ingre.quantity.equals(" "))){
			unit.setText(capitalizeString(ingre.unit));
		}else {
			unit.setText(" " + capitalizeString(ingre.unit));
		}
		
		if(ingre.name.isEmpty() || ingre.name.equals(" ")){
			name.setText(capitalizeString(ingre.name));
		}else {
			name.setText(" " + capitalizeString(ingre.name));
		}
		
		//quantity.setTextColor(Color.parseColor("#FFFFFF"));
		//unit.setTextColor(Color.parseColor("#FFFFFF"));
		//name.setTextColor(Color.parseColor("#FFFFFF"));
		
		addView(view);
	}

	public static String capitalizeString(String string) {
		char[] chars = string.toLowerCase().toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			} else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
				found = false;
			}
		}
		return String.valueOf(chars);
	}
}
