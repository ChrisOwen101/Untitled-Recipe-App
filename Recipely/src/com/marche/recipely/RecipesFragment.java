package com.marche.recipely;


import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.marche.recipely.libs.JSONParser;
import com.marche.recipely.recipes.AllRecipesRecipe;
import com.marche.recipely.recipes.BBCFoodRecipe;
import com.marche.recipely.recipes.BBCGoodFoodRecipe;
import com.marche.recipely.recipes.ChowRecipe;
import com.marche.recipely.recipes.Food52Recipe;
import com.marche.recipely.recipes.FoodComRecipe;
import com.marche.recipely.recipes.FoodNetworkUKRecipe;
import com.marche.recipely.recipes.FoodNetworkUSARecipe;
import com.marche.recipely.recipes.Recipe;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.squareup.picasso.Picasso;

public class RecipesFragment extends Fragment {

	public CardGridArrayAdapter mCardArrayAdapter;

	GlobalVar appState;

	JSONParser jsonParser = new JSONParser();
	ProgressDialog pDialog;

	final String getrecipes = "http://www.chrisowenportfolio.com/recipely/get_recipes.php";

	RecipesFragment frag = this;

	ArrayList<Card> cards;

	CardGridView staggeredView;

	public static RecipesFragment newInstance(int sectionNumber) {
		RecipesFragment fragment = new RecipesFragment();
		return fragment;
	}

	public RecipesFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.staggered_recipes_fragment, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		appState = ((GlobalVar)getActivity().getApplicationContext());

		setRetainInstance(true);

		//Set the arrayAdapter
		cards = new ArrayList<Card>();
		mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), cards);

		staggeredView = (CardGridView) getActivity().findViewById(R.id.carddemo_grid_base1);

		if(isTablet(getActivity())){
			staggeredView.setNumColumns(3);
		} else{
			staggeredView.setNumColumns(2);
		}


		//Set the empty view
		staggeredView.setEmptyView(getActivity().findViewById(android.R.id.empty));
		if (staggeredView != null) {
			setScaleAdapter();
		}

		new GetRecipes().execute();

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_main, menu);

		MenuItem menuItem = menu.findItem(R.id.searcher);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

		final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {
				ArrayList<Card> search = new ArrayList<Card>();

				int top = appState.recipes.size();
				for(int i = 0; i<top; i++ ){
					if(cards.get(i).getTitle().toLowerCase().contains(newText.toLowerCase())){
						search.add(cards.get(i));
					}
				}

				mCardArrayAdapter = new CardGridArrayAdapter(getActivity(), search);
				setScaleAdapter();
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				System.out.println(query);
				return true;
			}
		};

		searchView.setOnQueryTextListener(queryTextListener);
		View searchplate = (View)searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
		searchplate.setBackgroundResource(R.drawable.edittext_themed_white);
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public void addCard(Recipe rec){
		GridCard card = new GridCard(getActivity(),rec);                
		card.init();
		cards.add(card);
	}

	private void setScaleAdapter() {
		AnimationAdapter animCardArrayAdapter = new ScaleInAnimationAdapter(mCardArrayAdapter);
		animCardArrayAdapter.setAbsListView(staggeredView);
		staggeredView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);
	}

	Handler h = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.arg1 == 1){
				Recipe rec = (Recipe) msg.obj;
				appState.recipes.add(rec);
				addCard(appState.recipes.get(appState.recipes.size()-1));
			}
		}
	};
	
	public void getTags(){
		appState.menuValues = new ArrayList<String>();

		for(int i = 0; i<appState.recipes.size(); i++){
			if(appState.recipes.get(i).tags.size() > 0){
				for(int y = 0; y<appState.recipes.size(); y++){
					appState.menuValues.add(appState.recipes.get(i).tags.get(y));
				}
			}
		}
		
		Collections.sort(appState.menuValues);
		appState.menuValues.add(0, "All");
		appState.menuAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, appState.menuValues); 
		appState.list.setAdapter(appState.menuAdapter);
	}

	public class GridCard extends Card {

		protected int resourceIdThumbnail = -1;
		protected int count;

		GplayGridThumb thumbnail;

		Recipe rec;

		public GridCard(Context context, Recipe rec) {
			super(context, R.layout.carddemo_gplay_inner_content);
			this.rec = rec;
			setTitle(rec.name);
		}

		public GridCard(Context context, int innerLayout) {
			super(context, innerLayout);
		}

		private void init() {
			CardHeader header = new CardHeader(getContext());
			header.setButtonOverflowVisible(true);
			header.setTitle(rec.name);
			addCardHeader(header);

			thumbnail = new GplayGridThumb(getContext());
			thumbnail.setErrorResource(R.drawable.square_logo);
			if(rec.imageUrl != null && rec.imageUrl != ""){
				thumbnail.setUrlResource(rec.imageUrl);
			} else {
				thumbnail.setDrawableResource(R.drawable.square_logo);
			}
			addCardThumbnail(thumbnail);

			setOnClickListener(new OnCardClickListener() {
				@Override
				public void onClick(Card card, View view) {
					frag.getActivity().runOnUiThread (new Thread(new Runnable() { 
						public void run() {
							FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
							fragmentTransaction.setCustomAnimations(R.anim.rise, R.anim.fall,R.anim.rise, R.anim.fall);
							Fragment newFragment = RecipeCardFragment.newInstance(1);

							Bundle bundle=new Bundle();
							bundle.putString("id", Integer.toString(rec.id));
							newFragment.setArguments(bundle);

							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.add(R.id.layoutMain, newFragment);
							fragmentTransaction.commit();

							((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(rec.name);
							((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
						}
					}));
				}
			});
		}

		@Override
		public void setupInnerViewElements(ViewGroup parent, View view) {			
		}

		class GplayGridThumb extends CardThumbnail {

			public GplayGridThumb(Context context) {
				super(context);
			}

			@Override
			public void setupInnerViewElements(ViewGroup parent, View viewImage) {
				Picasso.with(getActivity()).load(rec.imageUrl).placeholder(R.drawable.square_logo).error(R.drawable.square_logo).into((ImageView)viewImage);
			}
		}

	}

	// Retrieves recipes for the user from a web service.
	class GetRecipes extends AsyncTask<String, String, String> {

		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Getting Recipes...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... params2) {	
			int success;
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id", Integer.toString(appState.userID)));				

				JSONObject json = jsonParser.makeHttpRequest(getrecipes, "GET", params);

				success = json.getInt("success");
				if (success == 1) {
					JSONArray productObj = json.getJSONArray("recipes"); // JSON Array
					
					int count = 0;

					for(int i = 0; i < productObj.length(); i++){
						String url = ((JSONObject)productObj.get(i)).getString("url");
						String id = ((JSONObject)productObj.get(i)).getString("recipe_id");
						
						//parse out the urls from different specified websites.
						if(url.contains("food52")){
							count++;
							new Food52Recipe(getActivity().getApplicationContext(), h, url, id);
						} else if(url.contains("allrecipes")){
							count++;
							new AllRecipesRecipe(getActivity().getApplicationContext(), h, url, id);
						} else if(url.contains("foodnetwork.com")){
							count++;
							new FoodNetworkUSARecipe(getActivity().getApplicationContext(), h, url, id);
						} else if(url.contains("foodnetwork.co.uk")){
							count++;
							new FoodNetworkUKRecipe(getActivity().getApplicationContext(), h, url, id);
						} else if(url.contains("food.com/recipe/")){
							count++;
							new FoodComRecipe(getActivity().getApplicationContext(), h, url, id);
						} else if(url.contains("chow.com")){
							count++;
							new ChowRecipe(getActivity().getApplicationContext(), h, url, id);
						} else if(url.contains("bbc.co.uk/food")){
							count++;
							new BBCFoodRecipe(getActivity().getApplicationContext(), h, url, id);
						} else if(url.contains("bbcgoodfood.com/recipes/")){
							count++;
							new BBCGoodFoodRecipe(getActivity().getApplicationContext(), h, url, id);
						} 
					}

					while(cards.size() < count){
						//Waiting...
					}
				}else{
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			mCardArrayAdapter.notifyDataSetChanged();
			getTags();
			pDialog.dismiss();
		}

	}

}
