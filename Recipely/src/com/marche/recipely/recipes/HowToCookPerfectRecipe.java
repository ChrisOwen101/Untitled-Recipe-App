package com.marche.recipely.recipes;

import java.io.IOException;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.Handler;

public class HowToCookPerfectRecipe extends Recipe{


	public HowToCookPerfectRecipe(Context con, Handler h, String url, String id) {
		super(con, h, url, id);
		checkOffline(id);
		sourceWebsite = "The Guardian";
	}

	public void checkOffline(String id){
		//if(currentlyStoredOffline(id)){
		//	parseRecipeFromJSON();
		//} else {
		parseRecipeFromURL();
		//}
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
					Element ele = doc.select("h1[itemprop^=name]").first();
					addName(ele);	

					ele = doc.select("span[class$=wide]").first();
					ele = ele.select("img").first();
					addImageUrl(ele);

					ele = doc.select("[itemprop$=recipeYield]").first();
					addServes(ele);

					ele = doc.select("[itemprop$=name]").first();
					addAuthor(ele);

					ele = doc.select("[itemprop$=description]").first();	
					if(ele != null){
						addNotes(ele);
					}

					Elements eles = doc.select("p > strong");
					for (Element link : eles) {
						String[] array = link.html().split("<br />");
						for(int i = 0; i< array.length; i++){
							addIngredient(array[i]);
						}
					}

					eles = doc.select("div[id$=article-body-blocks]");
					eles = eles.select("p");
					for (Element link : eles) {
						String pos = link.html();
						if(pos.length() > 0 && Character.isDigit(pos.charAt(0))){
							if(pos.charAt(1) == '.'){
								if(pos.contains("<br />")){
									String[] array = link.html().split("<br />");
									for(int i = 0; i< array.length; i++){
										if(array[i].length() > 0 && array[i].equals(" ") == false){
											addStep(array[i]);
										}
									}
								} else {
									addStep(link);
								}
							}
						} else {
							link.select("strong").remove();
							String[] array = link.text().split("[0-9]?[0-9]\\. ");
							if(array.length > 1){
								for(int i = 0; i< array.length; i++){
									if(array[i].length() > 0 && array[i].equals(" ") == false){
										addStep(array[i]);
									}
								}
							}

						}
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
