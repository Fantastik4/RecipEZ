package com.fantastik4.recipez;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SearchActivity extends Activity {
	RecipeProvider recipeProvider = new RecipeProvider();
	ArrayList<Recipe> recipes;
	Button search;
	ListView displayRecipes;
	ArrayList<String> recipeNames;
	ArrayAdapter<String> itemsAdapter;
	SearchActivity sa = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		recipeProvider = new RecipeProvider();
		displayRecipes = (ListView) findViewById(R.id.recipeList);

		search = (Button)findViewById(R.id.findFood);
		search.setClickable(false);
		
		itemsAdapter = new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_list_item_1,
				recipeNames );
		
		
		search.setOnClickListener(new OnClickListener(){
			public void onClick(View v) 
			{
				recipes = recipeProvider.FetchAllRecipes();
				ListView lv = (ListView)findViewById(R.id.recipeList);
				
                ArrayList<String> recipeNames = new ArrayList<String>();
                for(Recipe r: recipes)
                {
                    recipeNames.add(r.getName());
                }
                
                 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                         sa, 
                         android.R.layout.simple_list_item_1,
                         recipeNames );

                 lv.setAdapter(arrayAdapter);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	public void onBackPressed() {
		//Do nothing
	}
}
