package com.marche.recipely.recipes;

import java.io.IOException;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.Handler;

public class FoodComRecipe extends Recipe{

	String reviewNotes = "";

	public FoodComRecipe(Context con, Handler h, String url, String id) {
		super(con, h, url, id);
		checkOffline(id);
		sourceWebsite = "Food.com";
	}

	public void checkOffline(String id){
		if(currentlyStoredOffline(id)){
			parseRecipeFromJSON();
		} else {
			parseRecipeFromURL();
		}
	}

	@Override
	public void parseRecipeFromURL() {
		System.out.println("Getting from Web");
		
		Thread thread = new Thread(){
			public void run() {
				Document doc = null;

				try {
					doc = Jsoup.connect(url).userAgent("Mozilla").get();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if(doc != null){
					Element ele = doc.select("[itemprop$=name]").first();
					addName(ele);	

					ele = doc.select("img[itemprop$=image]").first();
					addImageUrl(ele);

					ele = doc.select("p[itemprop$=recipeYield]").first();
					addServes(ele);

					ele = doc.select("p[itemprop$=description]").first();	
					addNotes(ele);

					Elements eles = doc.select("span[class$=ingredient]");
					for (Element link : eles) {
						addIngredient(link.select("span[class$=name]"), link.select("span[class$=amount]"));
					}
					
					eles = doc.select("[itemProp$=recipeCatagory]");
					for (Element link : eles) {
						addTag(link);
					}

					ele = doc.select("[itemprop$=recipeInstructions]").first();
					eles = ele.select("div[class$=txt]");
					for (Element link : eles) {
						addStep(link);
					}		
					
					try {
						writeJsonRecipe();
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					doc = null;
					update();
				} else {
					System.out.println("Doc is Null");
				}
			}
		};
		thread.start();
	}

	@Override
	public void parseRecipeFromJSON() {
		System.out.println("Getting from JSON");
		
		Thread thread = new Thread(){
			public void run() {
				readJsonRecipe();
			}
		};
		thread.start();
	}
}
