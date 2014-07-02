package com.marche.recipely.recipes;

import java.io.IOException;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.Handler;

public class BBCFoodRecipe extends Recipe{

	public BBCFoodRecipe(Context con, Handler h, String url, String id) {
		super(con, h, url, id);
		checkOffline(id);
		sourceWebsite = "BBC Food";
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
					Element ele = doc.select("[class$=article-title]").first();
					addName(ele);	

					ele = doc.select("img[class$=photo][id$=food-image]").first();
					addImageUrl(ele);

					ele = doc.select("h3[class$=yield]").first();
					addServes(ele);

					ele = doc.select("span[class$=author]").first();	
					addAuthor(ele);
					
					ele = doc.select("div[id$=description]").first();	
					addNotes(ele);
					
					Elements eles = doc.select("div[id$=ingredients]");
					eles = doc.select("p[class$=ingredient]");
					for (Element link : eles) {
						addIngredient(link);
					}

					eles = doc.select("li[class$=instruction]");
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
