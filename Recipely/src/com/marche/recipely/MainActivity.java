
package com.marche.recipely;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.pedrovgs.DraggablePanel;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.marche.recipely.recipes.Food52Recipe;
import com.marche.recipely.recipes.Recipe;
import com.marche.recipely.recipes.Step;

public class MainActivity extends ActionBarActivity{

	ActionBar bar;
	GlobalVar appState;
	DraggablePanel draggablePanel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		appState = ((GlobalVar)getApplicationContext());
		appState.settings = getApplicationContext().getSharedPreferences("login", 0);
		
		//Set up action bar for older android os
		bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF8A6D")));
		bar.setTitle("Welcome to Recipely");
		
		int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		if (actionBarTitleId > 0) {
			TextView title = (TextView) findViewById(actionBarTitleId);
			if (title != null) {
				title.setTextColor(Color.WHITE);
			}
		}

		//Import and set background
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.back);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
		bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutMain);
		layout.setBackgroundDrawable(bitmapDrawable);

		//Add sliding menu to be populated later
		SlidingMenu menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidth(50);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffset(1000);
		menu.setBehindWidth(500);
		menu.setFadeDegree(0.75f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

		//Inflate view in to sliding menu
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.side_list, null);
		appState.list = (ListView) view.findViewById(R.id.listView1);
		
		//For use later
		appState.list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
			}
		});

		menu.setMenu(view);
		
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		//Accept intents and convert them to the currect recipe
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
				if(URLUtil.isValidUrl(sharedText)){
					if(sharedText.contains("food52.com/recipes")){
						new Food52Recipe(this, appState.h, sharedText, "-1");
					}
				}
			} 
		} else {
			//If the user is logged in
			int loggedIn = appState.settings.getInt("id", -1);

			if(loggedIn != -1){
				appState.userID = loggedIn;
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.setCustomAnimations(R.anim.fade_in_back, R.anim.fade_out_back);
				Fragment newFragment = RecipesFragment.newInstance(1);
				fragmentTransaction.add(R.id.layoutMain, newFragment);
				fragmentTransaction.commit();
			} else {
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.setCustomAnimations(R.anim.fade_in_back, R.anim.fade_out_back);
				Fragment newFragment = LogInFragment.newInstance(1);
				fragmentTransaction.add(R.id.layoutMain, newFragment);
				fragmentTransaction.commit();
			}

		}
		
		draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);	
		
		//Handler to pass bring recipe cards on and off screen.
		appState.h = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.arg1 == 1){
					appState.incoming = (Recipe) msg.obj;
					bar.setTitle(appState.incoming.name);

					FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
					fragmentTransaction.setCustomAnimations(R.anim.rise, R.anim.fall,R.anim.rise, R.anim.fall);
					Fragment newFragment = RecipeCardFragment.newInstance(1);

					Bundle bundle=new Bundle();
					bundle.putString("id", Integer.toString(-1));
					newFragment.setArguments(bundle);

					fragmentTransaction.add(R.id.layoutMain, newFragment);
					fragmentTransaction.commit();
				} else {
					initializeDraggablePanel((Step) msg.obj);
				}
			}
		};
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		switch (item.getItemId()) {
		case android.R.id.home: 
			onBackPressed();
			bar.setDisplayHomeAsUpEnabled(false);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
    public void initializeDraggablePanel(Step step) throws Resources.NotFoundException {
    	draggablePanel.setFragmentManager(getSupportFragmentManager());
    	draggablePanel.setTopFragment(TimerFragment.newInstance(step));
    	draggablePanel.setBottomFragment(TimerBackgroundFragment.newInstance(1));
        float xScaleFactor = 1.5f;
        float yScaleFactor = 1.5f;
        draggablePanel.setXScaleFactor(xScaleFactor);
        draggablePanel.setYScaleFactor(yScaleFactor);
        draggablePanel.setTopViewHeight(500);
        draggablePanel.setTopFragmentMarginRight(10);
        draggablePanel.setTopFragmentMarginBottom(10);
        draggablePanel.initializeView();
        draggablePanel.bringToFront();
    }
}
