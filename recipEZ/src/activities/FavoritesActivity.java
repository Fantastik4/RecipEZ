package activities;

import objects.Recipe;
import objects.SimpleListAdapter;
import resources.RecipeResourceProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;

import com.fantastik4.recipez.R;
import com.fantastik4.recipez.R.layout;

public class FavoritesActivity extends Activity {
	private String currentUsername;
	private ArrayList<Recipe> recipeResults;
	private RecipeResourceProvider recipeProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);

		recipeProvider = new RecipeResourceProvider();
		currentUsername = (String) getIntent().getSerializableExtra("CurrentUsername");

		recipeResults = recipeProvider.FetchFavoriteRecipes(currentUsername);
		populateRecipeList();
	}

	@SuppressWarnings("unchecked")
	private void populateRecipeList() {
		ListView listView = (ListView)findViewById(R.id.lv_favoriteRecipeList);		

		ArrayList<String> recipeNames = GetNamesOfRecipes();
		ArrayList<String> displayedRecipes = (ArrayList<String>) recipeNames.clone();
		SimpleListAdapter recipeListAdapter = new SimpleListAdapter(this, android.R.layout.simple_list_item_1, displayedRecipes);

		listView.setAdapter(recipeListAdapter);
		listView.setClickable(true);

		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				Recipe selectedRecipe = recipeResults.get(position);

				Intent i = new Intent(FavoritesActivity.this, DisplayRecipeActivity.class);
				i.putExtra("SelectedRecipe", selectedRecipe);
				i.putExtra("username", currentUsername);

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
