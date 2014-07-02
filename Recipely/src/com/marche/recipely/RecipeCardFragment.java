package com.marche.recipely;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.marche.recipely.cards.IngredientsCard;
import com.marche.recipely.cards.IngredientsHeader;
import com.marche.recipely.cards.RecipeStepCard;
import com.marche.recipely.cards.RecipeStepHeader;
import com.marche.recipely.cards.TagCard;
import com.marche.recipely.cards.TagHeader;
import com.marche.recipely.cards.TitleCard;
import com.marche.recipely.cards.TitleExpandCard;
import com.marche.recipely.cards.TitleHeader;
import com.marche.recipely.recipes.Recipe;
import com.squareup.picasso.Picasso;

/**Each recipe has a card **/
public class RecipeCardFragment extends Fragment {

	int recipeID = -1;
	Recipe recipe;
	GlobalVar appState;
	Bitmap bitmap;

	public static RecipeCardFragment newInstance(int sectionNumber) {
		RecipeCardFragment fragment = new RecipeCardFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		appState = ((GlobalVar)getActivity().getApplicationContext());

		// Get ID of user
		if(getArguments().getString("id") != null){
			if(getArguments().getString("id").equals("-1")){
				recipe = appState.incoming;
			} else {
				recipeID = Integer.parseInt(getArguments().getString("id"));      
				System.out.println(recipeID);

				for(int i = 0; i<appState.recipes.size(); i++){
					if(appState.recipes.get(i).id == recipeID){
						recipe = appState.recipes.get(i);
						System.out.println(recipeID);
					}
				}
			}
		}
		
		try {
			writeJsonRecipe();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return inflater.inflate(R.layout.recipe_holder, container, false);
	}
	
	//Write a recipe to json for storing offline
	public void writeJsonRecipe() throws JSONException{
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("name", recipe.name);
		jsonObject.put("servesavg", recipe.servesAvg);
		jsonObject.put("url", recipe.url);
		jsonObject.put("notes", recipe.notes);
		jsonObject.put("sourcewebsite", recipe.sourceWebsite);
		jsonObject.put("author", recipe.author);
		
		JSONArray list = new JSONArray();
		for(int i = 0; i<recipe.steps.size(); i++){
			JSONObject jsonStep = new JSONObject();
			jsonStep.put("step", recipe.steps.get(i).step);
			list.put(jsonStep);
		}
		
		jsonObject.put("steps", list);
		
		list = new JSONArray();
		for(int i = 0; i<recipe.ingredients.size(); i++){
			JSONObject jsonIngre = new JSONObject();
			jsonIngre.put("name", recipe.ingredients.get(i).name);
			jsonIngre.put("quantity", recipe.ingredients.get(i).quantity);
			jsonIngre.put("unit", recipe.ingredients.get(i).unit);
			list.put(jsonIngre);
		}
		
		jsonObject.put("ingredients", list);

		System.out.println(jsonObject.toString());
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initCards();
	}


	private void initCards() {


		ArrayList<Card> cards = new ArrayList<Card>();

		cards.add(init_title_card());
		cards.add(init_ingredients_card());
		cards.addAll(init_recipe_card());
		
		if(recipe.tags.size() > 0){
			cards.add(init_tags_card());
		}

		CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
		CardListView listView = (CardListView) getActivity().findViewById(R.id.myList);

		if (listView!=null){
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View view = inflater.inflate(R.layout.header, null);
			ImageView image = (ImageView) view.findViewById(R.id.image);
			Picasso.with(getActivity()).load(recipe.imageUrl).placeholder(R.drawable.square_logo).error(R.drawable.square_logo).into(image);
			listView.addHeaderView(view);

			view = inflater.inflate(R.layout.footer, null);
			listView.addFooterView(view);

			listView.setAdapter(mCardArrayAdapter);
		}
	}

	private Card init_ingredients_card() {
		IngredientsCard card = new IngredientsCard(getActivity(),recipe);

		IngredientsHeader header = new IngredientsHeader(getActivity());
		card.addCardHeader(header);

		return card;
	}
	
	private Card init_tags_card() {
		TagCard card = new TagCard(getActivity(),recipe);

		TagHeader header = new TagHeader(getActivity());
		card.addCardHeader(header);

		return card;
	}

	private ArrayList<Card> init_recipe_card() {
		ArrayList<Card> cards = new ArrayList<Card>();

		for(int i = 0; i<recipe.steps.size(); i++){
			RecipeStepCard card = new RecipeStepCard(getActivity(),recipe.steps.get(i));

			RecipeStepHeader header = new RecipeStepHeader(getActivity(), i);
			card.addCardHeader(header);
			cards.add(card);
		}

		return cards;
	}

	private Card init_title_card() {
		TitleCard card = new TitleCard(getActivity(),recipe);

		TitleHeader header = new TitleHeader(getActivity(), recipe);
		header.setButtonExpandVisible(true);
		card.addCardHeader(header);

		TitleExpandCard expand = new TitleExpandCard(getActivity(), recipe);
		card.addCardExpand(expand);

		return card;
	}

	public static float convertDpToPixel(float dp, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}
}
