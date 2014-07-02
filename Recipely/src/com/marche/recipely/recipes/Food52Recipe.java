package com.marche.recipely.recipes;

import java.io.IOException;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.Handler;

public class Food52Recipe extends Recipe{

	String reviewNotes = "";

	public Food52Recipe(Context con, Handler h, String url, String id) {
		super(con, h, url, id);
		checkOffline(id);
		sourceWebsite = "Food52";
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
					Element ele = doc.select("h1[itemprop$=name]").first();
					addName(ele);	

					ele = doc.select("img[itemprop$=image]").first();
					addImageUrl(ele);

					ele = doc.select("p[itemprop$=recipeYield]").first();
					addServes(ele);

					ele = doc.select("span[class$=recipe-note]").first();	

					if(ele != null){
						if(ele.html().startsWith("<strong>Author Notes:")){
							addNotes(ele);

							ele = doc.select("a[class$=username]").first();	
							addAuthor(ele);
						} else if(ele.html().startsWith("<strong>Food52 Review:")){
							addNotes(ele);
						}
					}

					Elements eles = doc.select("li[itemprop$=ingredients]");
					for (Element link : eles) {
						addIngredient(link.select("span[class$=item-name]"), link.select("span[class$=quantity]"));
					}
					
					eles = doc.select("a[href^=/recipes/search?recipe_search]");
					for (Element link : eles) {
						addTag(link);
					}

					eles = doc.select("li[itemprop$=recipeInstructions]");
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
