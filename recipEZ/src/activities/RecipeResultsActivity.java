package activities;

import objects.Recipe;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;
import com.fantastik4.recipez.R;
import objects.SimpleListAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class RecipeResultsActivity extends Activity {
	private String username;
	private ArrayList<Recipe> recipeResults;
	ListView listView;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_recipes);

		recipeResults = (ArrayList<Recipe>) getIntent().getSerializableExtra("RecipeResults");
		username = (String) getIntent().getSerializableExtra("username");
		PopulateRecipeList();
	}

	@SuppressWarnings("unchecked")
	private void PopulateRecipeList() {
		listView = (ListView)findViewById(R.id.lv_recipeList);		

		ArrayList<String> recipeNames = GetNamesOfRecipes();
		SimpleListAdapter recipeListAdapter = new SimpleListAdapter(this, android.R.layout.simple_list_item_1, recipeNames);

		listView.setAdapter(recipeListAdapter);
		listView.setClickable(true);

		listView.setOnItemClickListener(new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				// TODO Auto-generated method stub
				Recipe selectedRecipe = recipeResults.get(position);
				Intent i = new Intent(RecipeResultsActivity.this, DisplayRecipeActivity.class);
				i.putExtra("SelectedRecipe", selectedRecipe);
				i.putExtra("username", username);
				
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