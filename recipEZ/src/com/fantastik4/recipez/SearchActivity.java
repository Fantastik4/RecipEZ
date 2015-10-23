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
	
	SearchView searchField;
	String currentUsername;
	ArrayList<Boolean> checkedStates;
	ArrayList<Ingredient> ingredients;
	IngredientProvider ingredientProvider;
	IngredientListAdapter ingredientListAdapter;
	ArrayList<String> ingredientNames, displayedIngredients;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		/**
		 * LABLES:	Search Text Field - sv_searchIngredients
		 * 			Ingredients List - lv_ingredientsList
		 */

		checkedStates = new ArrayList<Boolean>();
		ingredientProvider = new IngredientProvider();
		currentUsername = (String) getIntent().getSerializableExtra("CurrentUserName");

		searchField = (SearchView) findViewById(R.id.sv_searchIngredients);
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

	private void UpdateDisplayedIngredients(String searchArg) {

		displayedIngredients.clear();
		for(String name: ingredientNames) {
			if(name.toLowerCase().contains(searchArg.toLowerCase())) displayedIngredients.add(name);
		}
	}

	private void PopulateIngredientList() {
		ListView listView = (ListView)findViewById(R.id.lv_ingredientsList);		

		ingredientNames = ConvertIngredientsToStringArray(ingredientProvider.FetchAllIngredients());
		displayedIngredients = (ArrayList<String>) ingredientNames.clone();
		ingredientListAdapter = new IngredientListAdapter(this, android.R.layout.simple_list_item_multiple_choice, displayedIngredients);

		listView.setAdapter(ingredientListAdapter);

		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
		for(Ingredient i: ingredients) {
			ingredientNames.add(i.getName());
			checkedStates.add(false);
		}

		return ingredientNames;
	}
}