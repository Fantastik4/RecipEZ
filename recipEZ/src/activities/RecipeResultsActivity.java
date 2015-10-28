package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import objects.Recipe;
import objects.SimpleListAdapter;

import java.util.ArrayList;

import com.fantastik4.recipez.R;
import com.fantastik4.recipez.R.id;
import com.fantastik4.recipez.R.layout;

import android.app.Activity;
import android.content.Intent;

public class RecipeResultsActivity extends Activity {
	private ArrayList<Recipe> recipeResults;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		recipeResults = (ArrayList<Recipe>) getIntent().getSerializableExtra("RecipeResults");
		setContentView(R.layout.activity_search_recipes);

		PopulateRecipeList();

	}

	private void PopulateRecipeList() {
		ListView listView = (ListView)findViewById(R.id.lv_recipeList);		


		ArrayList<String> recipeNames = GetNamesOfRecipes();
		ArrayList<String> displayedRecipes = (ArrayList<String>) recipeNames.clone();
		SimpleListAdapter recipeListAdapter = new SimpleListAdapter(this, android.R.layout.simple_list_item_1, displayedRecipes);

		listView.setAdapter(recipeListAdapter);
		listView.setClickable(true);

		listView.setOnItemClickListener(new OnItemClickListener(){


			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				// TODO Auto-generated method stub
				Recipe selectedRecipe = recipeResults.get(position);
				Intent i = new Intent(RecipeResultsActivity.this, DisplayRecipeActivity.class);
				i.putExtra("SelectedRecipe", selectedRecipe);
				startActivity(i);

				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}

		});
	}

	private ArrayList<String> GetNamesOfRecipes() {

		ArrayList<String> recipeNames = new ArrayList<String>();
		for(Recipe r: recipeResults) {
			recipeNames.add(r.getName());
		}
		return recipeNames;
	}
}
