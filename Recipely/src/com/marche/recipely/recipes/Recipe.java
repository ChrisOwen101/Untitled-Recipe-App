package com.marche.recipely.recipes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.webkit.URLUtil;


public abstract class Recipe {

	public ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	public ArrayList<Step> steps = new ArrayList<Step>();
	public ArrayList<String> tags = new ArrayList<String>();
	public String servesAvg = null;
	public String url = null;
	public Bitmap bitmap = null;
	public String name = null;
	public String notes = null;
	public String sourceWebsite = null;
	public String author = null;
	public int id = -1;
	public String imageUrl;

	Recipe rec = this;

	Handler h;
	Context con;

	public Recipe(Context con, Handler h, String url, String id) {
		this.h = h;
		this.url = url;
		this.id = Integer.parseInt(id);
		this.con = con;
	}

	public abstract void parseRecipeFromURL();
	public abstract void parseRecipeFromJSON();

	public void getSourceWebsite(String url){
		if(url.contains("food52.com/recipes")){
			sourceWebsite = "food52";
		}
	}

	public boolean currentlyStoredOffline(String id){
		File file = con.getApplicationContext().getFileStreamPath("recipes_" + id + ".recipely");
		if(file.exists()){
			return true;
		} else {
			return false;
		}
	}

	public void addTag(Element ele){
		if(ele != null){
			tags.add(Jsoup.parse(ele.html()).text().trim());	
		}
	}

	public void addTag(Elements ele){
		if(ele != null){
			tags.add(Jsoup.parse(ele.html()).text().trim());	
		}
	}

	public void addTag(String ele){
		if(ele != null){
			tags.add(Jsoup.parse(ele).text().trim());	
		}
	}

	public void addName(Element ele){
		if(ele != null){
			name = Jsoup.parse(ele.html()).text().trim();	
		}
	}


	public void addName(Elements ele){
		if(ele != null){
			name = Jsoup.parse(ele.html()).text().trim();	
		}
	}

	public void addImageUrl(Element ele){
		if(ele != null){
			imageUrl = ele.attr("src");
		}
	}

	public void addImageUrl(Elements ele){
		if(ele != null){
			imageUrl = ele.attr("src");
		}
	}

	public void addServes(Element ele){
		if(ele != null){
			servesAvg = Jsoup.parse(ele.html()).text().trim();
		}
	}

	public void addNotes(Element element){
		if(element != null){
			notes = (Jsoup.parse(element.html().trim())).text();
		}
	}

	public void addNotes(Elements elements){
		if(elements != null){
			notes = (Jsoup.parse(elements.html().trim())).text();
		}
	}

	public void addAuthor(Element ele){
		if(ele != null){
			author = Jsoup.parse(ele.html()).text().trim();
		}
	}

	public void update(){		
		Message msg = new Message();
		msg.arg1 = 1;
		msg.obj = rec;
		h.sendMessage(msg);
	}

	public void addIngredient(String name){
		if(name != null){
			ingredients.add(new Ingredient(Jsoup.parse(name.trim()).text(), null));
		}
	}

	public void addIngredient(String name, String quantity){
		if(name != null){
			ingredients.add(new Ingredient(Jsoup.parse(name.trim()).text(), null));
		}
	}

	public void addIngredient(Elements name, Elements quantity){

		if(name != null){
			if(quantity == null){

			} else {
				ingredients.add(new Ingredient(Jsoup.parse(name.html().trim()).text(),Jsoup.parse(quantity.html().trim()).text()));
			}
		}
	}

	public void addIngredient(Element name){
		if(name != null){
			ingredients.add(new Ingredient(Jsoup.parse(name.html().trim()).text(),null));
		}
	}


	public void addStep(Element step){
		if(step != null){

			String stepText = Jsoup.parse(step.html().trim()).text();

			if(stepText.equals("") == false && stepText.equals(" ") == false){				
				ArrayList<Integer> timer = new ArrayList<Integer>();
				String[] arr = stepText.split(" ");

				for (int i = 0; i < arr.length; i++) {
					arr[i] = arr[i].trim();
					if(arr[i].equals("minute")){
						timer.add(1*60);

					} else if (arr[i].contains("minutes")){
						if(isNumeric(arr[i-1])){
							timer.add(Integer.parseInt(arr[i-1])*60);
						} else if(arr[i-1].contains("-")){
							String[] split = arr[i-1].split("-");
							timer.add(Integer.parseInt(split[0])*60);
						}
					}
				}

				steps.add(new Step(stepText, timer));
			}
		}
	}

	public void addStep(Elements step){
		if(step != null){
			String stepText = Jsoup.parse(step.html().trim()).text();

			if(stepText.equals("") == false && stepText.equals(" ") == false){				
				ArrayList<Integer> timer = new ArrayList<Integer>();
				String[] arr = stepText.split(" ");

				for (int i = 0; i < arr.length; i++) {
					arr[i] = arr[i].trim();
					if(arr[i].equals("minute")){
						timer.add(1*60);

					} else if (arr[i].contains("minutes")){
						if(isNumeric(arr[i-1])){
							timer.add(Integer.parseInt(arr[i-1])*60);
						} else if(arr[i-1].contains("-")){
							String[] split = arr[i-1].split("-");
							timer.add(Integer.parseInt(split[0])*60);
						}
					}
				}

				steps.add(new Step(stepText, timer));
			}
		}
	}

	public void addStep(String stepText){
		if(stepText != null){
			ArrayList<Integer> timer = new ArrayList<Integer>();

			String[] arr = stepText.split(" ");

			for (int i = 0; i < arr.length; i++) {
				arr[i] = arr[i].trim();
				if(arr[i].equals("minute")){
					timer.add(1*60);

				} else if (arr[i].contains("minutes")){
					if(isNumeric(arr[i-1])){
						timer.add(Integer.parseInt(arr[i-1])*60);
					} else if(arr[i-1].contains("-")){
						String[] split = arr[i-1].split("-");
						timer.add(Integer.parseInt(split[0])*60);
					}
				}
			}

			steps.add(new Step(stepText, timer));
		}
	}

	public static boolean isNumeric(String str)  
	{  
		try  
		{  
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}

	public void writeJsonRecipe() throws JSONException, IOException{
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("id", id);
		jsonObject.put("imageurl", imageUrl);
		jsonObject.put("name", name);
		jsonObject.put("servesavg", servesAvg);
		jsonObject.put("url", url);
		jsonObject.put("notes", notes);
		jsonObject.put("sourcewebsite", sourceWebsite);
		jsonObject.put("author", author);

		JSONArray list = new JSONArray();
		for(int i = 0; i<steps.size(); i++){
			list.put(new JSONObject()
			.put("step", steps.get(i).step));
		}

		jsonObject.put("steps", list);

		list = new JSONArray();
		for(int i = 0; i<ingredients.size(); i++){
			list.put(new JSONObject()
			.put("name", ingredients.get(i).name)
			.put("quantity", ingredients.get(i).quantity)
			.put("unit", ingredients.get(i).unit));
		}

		jsonObject.put("ingredients", list);

		list = new JSONArray();
		for(int i = 0; i<tags.size(); i++){
			list.put(new JSONObject()
			.put("tag", tags.get(i)));
		}

		jsonObject.put("tags", list);

		FileOutputStream fos = con.openFileOutput("recipes_" + id + ".recipely", Context.MODE_PRIVATE);
		fos.write(jsonObject.toString().getBytes());
		fos.close();
	}

	public String readSavedData ( ) {
		StringBuffer datax = new StringBuffer("");
		try {
			FileInputStream fIn = con.openFileInput ("recipes_" + id + ".recipely") ;
			InputStreamReader isr = new InputStreamReader ( fIn ) ;
			BufferedReader buffreader = new BufferedReader ( isr ) ;

			String readString = buffreader.readLine ( ) ;
			while ( readString != null ) {
				datax.append(readString);
				readString = buffreader.readLine ( ) ;
			}

			isr.close ( ) ;
		} catch ( IOException ioe ) {
			ioe.printStackTrace ( ) ;
		}
		return datax.toString() ;
	}

	public void readJsonRecipe() {		
		String json = readSavedData();

		if(json != null){
			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				id = jsonObject.getInt("id");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				imageUrl = jsonObject.getString("imageurl");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				name = jsonObject.getString("name");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				servesAvg = jsonObject.getString("servesavg");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				url = jsonObject.getString("url");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			try {
				notes = jsonObject.getString("notes");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				sourceWebsite = jsonObject.getString("sourcewebsite");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				author = jsonObject.getString("author");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			JSONArray list = null;
			try {
				list = jsonObject.getJSONArray("steps");
				for(int i = 0; i<list.length(); i++){
					addStep(list.getJSONObject(i).getString("step"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				list = jsonObject.getJSONArray("ingredients");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			for(int i = 0; i<list.length(); i++){
				JSONObject jsonIngre = null;
				try {
					jsonIngre = list.getJSONObject(i);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				String name = null;
				try {
					name = jsonIngre.getString("name");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				String quantity = null;
				try {
					quantity = jsonIngre.getString("quantity");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if(quantity != null){
					addIngredient(name, quantity);
				} else {
					addIngredient(name);
				}
			}

			try {
				list = jsonObject.getJSONArray("tags");
				for(int i = 0; i<list.length(); i++){
					addTag(list.getJSONObject(i).getString("tag"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			update();
		}
	}
}
