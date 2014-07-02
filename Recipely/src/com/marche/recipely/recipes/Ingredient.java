package com.marche.recipely.recipes;

import android.graphics.drawable.Drawable;

public class Ingredient {

	String[] units = {"teaspoon", "teaspoons", "tsp", "tablespoon", "tablespoons", "tbsp", "fluid ounce", "floz", "fl oz", "gill", "gills", "cup", "cups", "pint", "pints", "quart", "quarts", "gallon", "gallons", "ml", "l", "litre", "pound", "pounds", "ounce", "ounces", "lb" ,"oz", "mg", "gram", "g", "kg", "kilogram", "cm", "inch", "inches"};

	public String name = null;
	public String fullName = null;
	public String quantity = null;
	public String unit = "";
	public Drawable image = null;

	public Ingredient(String nameIn, String quantity){
		this.name = nameIn;
		this.fullName = nameIn;
		this.quantity = quantity;
		this.image = getImage();

		if(quantity != "" && quantity != null){
			int i = name.indexOf(' ');	
			if(i != -1){
				String unitHold = name.substring(0, i);

				for(int j =0; j < units.length; j++){
					if(unitHold.equals(units[j])){
						unit = name.substring(0, i);
						name = name.substring(i, name.length()).trim();
					}
				}
			}
		}

		name = name.substring(0, 1).toUpperCase() + name.substring(1);

	}

	public Drawable getImage(){		
		return null;
	}

}
