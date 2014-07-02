package com.marche.recipely;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.marche.recipely.libs.JSONParser;

public class LogInFragment extends Fragment {

	JSONParser jsonParser = new JSONParser();
	ProgressDialog pDialog;

	EditText username;
	EditText password;

	final String getuser = "http://www.chrisowenportfolio.com/recipely/get_user.php";
	final String adduser = "http://www.chrisowenportfolio.com/recipely/add_user.php";

	GlobalVar appState;

	public static LogInFragment newInstance(int sectionNumber) {
		LogInFragment fragment = new LogInFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.login_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		appState = ((GlobalVar)getActivity().getApplicationContext());

		initCards();
	}


	private void initCards() {

		//Create a Card
		Card card = new Card(getActivity(),R.layout.username_card);
		CardHeader header = new CardHeader(getActivity());
		header.setTitle("Login");
		card.addCardHeader(header);
		//Set card in the cardView
		CardView cardView = (CardView) getActivity().findViewById(R.id.login);
		cardView.setCard(card);
		username = (EditText) cardView.findViewById(R.id.username);
		password = (EditText) cardView.findViewById(R.id.password);

		//Create a Card for the log in button
		card = new Card(getActivity(),R.layout.login_button);
		header = new CardHeader(getActivity());
		card.addCardHeader(header);
		card.setOnClickListener(new Card.OnCardClickListener() {
			@Override
			public void onClick(Card card, View view) {
				new GetUser().execute();
			}
		});
		cardView = (CardView) getActivity().findViewById(R.id.loginButton);
		cardView.setCard(card);

		//Create a Card for the create an account button
		card = new Card(getActivity(),R.layout.create_user_button);
		header = new CardHeader(getActivity());
		card.addCardHeader(header);
		card.setOnClickListener(new Card.OnCardClickListener() {
			@Override
			public void onClick(Card card, View view) {
				new CreateNewUser().execute();
			}
		});
		cardView = (CardView) getActivity().findViewById(R.id.createButton);
		cardView.setCard(card);

		final LinearLayout rel = (LinearLayout) getActivity().findViewById(R.id.layoutLogin);

	}

	public static float convertDpToPixel(float dp, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * 
	 * Queries a database for their log in information.
	 *
	 */
	class GetUser extends AsyncTask<String, String, String> {

		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Logging In...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... params2) {	
			int success;
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username.getText().toString()));
				params.add(new BasicNameValuePair("password", password.getText().toString()));


				// getting product details by making HTTP request
				// Note that product details url will use GET request
				JSONObject json = jsonParser.makeHttpRequest(getuser, "GET", params);

				// json success tag
				success = json.getInt("success");
				if (success == 1) {
					// successfully received product details
					JSONArray productObj = json.getJSONArray("username"); // JSON Array

					// get first product object from JSON Array
					JSONObject product = productObj.getJSONObject(0);
					String id = product.getString("id");
					appState.userID = Integer.parseInt(id);

					if(username.getText().toString().equals(product.getString("username"))){
						return "done";
					} else{
						System.out.println("Login Unsuccessful");
						return "fail";
					}
				}else{
					return "fail";
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return "fail";
			}
		}

		protected void onPostExecute(String comp) {
			if(comp.equals("done")){
				pDialog.dismiss();

				SharedPreferences.Editor editor = appState.settings.edit();
				editor.putInt("id", appState.userID);
				editor.commit();

				FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
				fragmentTransaction.setCustomAnimations(R.anim.fade_in_back, R.anim.fade_out_back);
				Fragment newFragment = RecipesFragment.newInstance(1);
				fragmentTransaction.replace(R.id.layoutMain, newFragment);
				fragmentTransaction.commit();
			}
		}
	}

	class CreateNewUser extends AsyncTask<String, String, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Creating Account...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username.getText().toString()));
			params.add(new BasicNameValuePair("password", password.getText().toString()));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(adduser,"POST", params);

			// check log cat for response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt("success");             
				String id = json.getString("id");
				System.out.println(id);

				if (success == 1) {
					//Intent i = new Intent(getApplicationContext(), MultiplayerHome.class);
					//i.putExtra("fbuser",1);
					//startActivity(i);


				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}

}
