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

public class SearchActivity extends Activity {
	IngredientProvider ingredientProvider;
	ArrayList<Ingredient> ingredients;
	ArrayList<Boolean> checkedStates;
	Button search;
	ListView displayRecipes;
	ArrayList<String> ingredientNames;
	SearchActivity sa = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		ingredientProvider = new IngredientProvider();
		checkedStates = new ArrayList<Boolean>();
		
		search = (Button)findViewById(R.id.findFood);
		search.setClickable(false);
		
		PopulateIngredientList();
		
		search.setOnClickListener(new OnClickListener(){
			public void onClick(View v) 
			{
				
			}
		});
		
		
	}

	private void PopulateIngredientList()
	{
		ingredients = ingredientProvider.FetchAllIngredients();
		ListView lv = (ListView)findViewById(R.id.recipeList);
		
        ArrayList<String> ingredientNames = new ArrayList<String>();
        for(Ingredient i: ingredients)
        {
           ingredientNames.add(i.getName());
           checkedStates.add(false);
        }
        
         ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                 sa, 
                 android.R.layout.simple_list_item_multiple_choice,
                 ingredientNames );

         lv.setAdapter(arrayAdapter);
         
         lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
         lv.setOnItemClickListener(new OnItemClickListener(){
        	 public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        	 {
        		 boolean currentlyChecked = checkedStates.get(position);
                 checkedStates.set(position, !currentlyChecked);
        		 
                 CheckedTextView markedItem = (CheckedTextView) view;
                 markedItem.setChecked(checkedStates.get(position));
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
