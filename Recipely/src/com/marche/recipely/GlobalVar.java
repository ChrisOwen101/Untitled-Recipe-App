package com.marche.recipely;

import java.io.File;
import java.util.ArrayList;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.marche.recipely.recipes.Recipe;

/**Class to hold global variables **/
public class GlobalVar extends Application {

	public ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	public int userID = -1;
	public Recipe incoming;
	public Handler h;
	public SharedPreferences settings;
	public ArrayAdapter<String> menuAdapter;
	public ArrayList<String> menuValues = new ArrayList<String>();
	public ListView list;

	public boolean currentlyStoredOffline(String id){
		File file = getApplicationContext().getFileStreamPath("recipes/" + "recipes_" + id + ".recipely");
		if(file.exists()){
			return true;
		} else {
			return false;
		}
	}
}