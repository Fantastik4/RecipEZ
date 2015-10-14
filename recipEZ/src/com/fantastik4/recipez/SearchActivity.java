package com.fantastik4.recipez;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class SearchActivity extends Activity {
	IngredientProvider ingredientProvider;
	ArrayList<Ingredient> ingredients;
	ArrayList<Boolean> checkedStates;
	Button search;
	SearchView searchField;
	ArrayList<String> ingredientNames, displayedIngredients;
	IngredientListAdapter ingredientListAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		ingredientProvider = new IngredientProvider();
		checkedStates = new ArrayList<Boolean>();
		
		search = (Button)findViewById(R.id.findFood);
		search.setClickable(false);
		search.setOnClickListener(new OnClickListener(){
			public void onClick(View v) 
			{
				
			}
		});		
		
		searchField = (SearchView) findViewById(R.id.searchView1);
		searchField.setOnQueryTextListener(new OnQueryTextListener(){
			
			@Override
			public boolean onQueryTextChange(String searchArg) {
				// TODO Auto-generated method stub
				UpdateDisplayedIngredients(searchArg);
				ingredientListAdapter.notifyDataSetChanged();
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		
		
		PopulateIngredientList();
		
		
	}

	private void UpdateDisplayedIngredients(String searchArg) 
	{
		displayedIngredients.clear();
		for(String name: ingredientNames)
		{
			if(name.toLowerCase().contains(searchArg.toLowerCase())) displayedIngredients.add(name);
		}
	}

	private void PopulateIngredientList()
	{
		ListView listView = (ListView)findViewById(R.id.recipeList);		
		
		ingredientNames = ConvertIngredientsToStringArray(ingredientProvider.FetchAllIngredients());
        displayedIngredients = (ArrayList<String>) ingredientNames.clone();
		ingredientListAdapter = new IngredientListAdapter(this, android.R.layout.simple_list_item_multiple_choice, displayedIngredients);

		listView.setAdapter(ingredientListAdapter);
         
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				boolean currentlyChecked = checkedStates.get(position);
				checkedStates.set(position, !currentlyChecked);
        		 
				CheckedTextView markedItem = (CheckedTextView) view;
				markedItem.setChecked(checkedStates.get(position));
			}
		});
	}
	
	private ArrayList<String> ConvertIngredientsToStringArray(ArrayList<Ingredient> ingredients) {
		checkedStates = new ArrayList<Boolean>();
		ArrayList<String> ingredientNames = new ArrayList<String>();
		for(Ingredient i: ingredients)
        {
           ingredientNames.add(i.getName());
           checkedStates.add(false);
        }
		return ingredientNames;
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
