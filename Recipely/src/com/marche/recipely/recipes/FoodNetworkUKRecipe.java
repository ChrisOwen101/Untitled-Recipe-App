package com.marche.recipely.recipes;

import java.io.IOException;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.Handler;

public class FoodNetworkUKRecipe extends Recipe{


	public FoodNetworkUKRecipe(Context con, Handler h, String url, String id) {
		super(con, h, url, id);
		checkOffline(id);
		sourceWebsite = "Food Network UK";
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
					Element ele = doc.select("span[itemprop$=name]").first();
					addName(ele);	

					Elements eles = doc.select("[itemprop$=photo][alt$= ]");
					addImageUrl(eles);

					ele = doc.select("[itemprop$=servingSize]").first();
					addServes(ele);

					eles = doc.select("[itemprop$=ingredients]");
					for (Element link : eles) {
						addIngredient(link);
					}
					
					eles = doc.select("span[class$=topic-button]");
					for (Element link : eles) {
						addTag(link);
					}

					ele = doc.select("[id$=method-box]").first();
					eles = ele.select("p");
					System.out.println(eles.size());
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
