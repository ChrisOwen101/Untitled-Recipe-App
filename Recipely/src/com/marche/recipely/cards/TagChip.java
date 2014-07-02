package com.marche.recipely.cards;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marche.recipely.R;
import com.marche.recipely.recipes.Ingredient;

public class TagChip extends LinearLayout {

	String tag;
	Context context;

	public TagChip(Context context, String tag) {
		super(context);
		this.context = context;
		this.tag = tag;

		View view = LayoutInflater.from(context).inflate(R.layout.tag_chip, null);
		TextView quantity = (TextView) view.findViewById(R.id.tag);
		quantity.setText(tag);
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
