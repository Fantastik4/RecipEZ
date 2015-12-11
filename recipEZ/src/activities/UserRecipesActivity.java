package activities;

import objects.Model;
import objects.Recipe;
import android.os.Bundle;
import android.view.View;
import objects.Ingredient;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;
import com.fantastik4.recipez.R;
import objects.SimpleListAdapter;
import android.widget.AdapterView;
import resources.IngredientProvider;
import resources.RecipeResourceProvider;
import android.widget.AdapterView.OnItemClickListener;

public class UserRecipesActivity extends Activity {
	
	private String currentUsername;
	private ArrayList<Recipe> recipeResults;
	private ArrayList<Model> ingredientNames;
	private IngredientProvider ingredientProvider;
	private ArrayList<Ingredient> userIngredients;
	private ListView listView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_recipes);

		ingredientProvider = new IngredientProvider();
		currentUsername = (String) getIntent().getSerializableExtra("CurrentUsername");

		recipeResults = GetRecipeResults();
		populateRecipeList();
	}

	@SuppressWarnings("unchecked")
	private void populateRecipeList() 
	{
		InitializeListView();
	}

	private void InitializeListView() {
		listView = (ListView)findViewById(R.id.lv_userRecipeList);		

		ArrayList<String> recipeNames = GetNamesOfRecipes();
		ArrayList<String> displayedRecipes = (ArrayList<String>) recipeNames.clone();
		SimpleListAdapter recipeListAdapter = new SimpleListAdapter(this, android.R.layout.simple_list_item_1, displayedRecipes);

		listView.setAdapter(recipeListAdapter);
		listView.setClickable(true);

		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				Recipe selectedRecipe = recipeResults.get(position);

				Intent i = new Intent(UserRecipesActivity.this, DisplayRecipeActivity.class);
				i.putExtra("SelectedRecipe", selectedRecipe);
				i.putExtra("username", currentUsername);

				startActivity(i);
				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
		});
	}

	private ArrayList<String> GetNamesOfRecipes() 
	{
		ArrayList<String> recipeNames = new ArrayList<String>();

		for(Recipe r: recipeResults) {
			recipeNames.add(r.getName());
		}
		return recipeNames;
	}

	private ArrayList<Recipe> GetRecipeResults() {
		RecipeResourceProvider recipeProvider = new RecipeResourceProvider();
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		ArrayList<Recipe> recipeResults = new ArrayList<Recipe>();

		userIngredients = ingredientProvider.FetchIngredientsByUserID(currentUsername);
		ingredientNames = ConvertIngredientsToModelArray(userIngredients);

		for(int i = 0; i < ingredientNames.size(); i++) {
			recipes = recipeProvider.FetchRecipesByIngredientID(userIngredients.get(i).getIngredientID());
			recipeResults = AddAllNoDuplicates(recipeResults, recipes);
		}
		return recipeResults;
	}
	
	private ArrayList<Recipe> AddAllNoDuplicates(ArrayList<Recipe> destination, ArrayList<Recipe> source)
	{
		for(Recipe r: source)
		{
			if(!ListContainsRecipe(destination, r)) destination.add(r);
		}
		return destination;
	}

	private boolean ListContainsRecipe(ArrayList<Recipe> list2, Recipe recipe) {
		for (Recipe r: list2) {
			if (r.getRecipeID().equals(recipe.getRecipeID())) return true;
		}
		return false;
	}

	private ArrayList<Model> ConvertIngredientsToModelArray(ArrayList<Ingredient> ingredients) {
		ArrayList<Model> ingredientNames = new ArrayList<Model>();
		Model temp;
		
		for (Ingredient i: ingredients) {
			temp = new Model(i.getName());
			temp.setSelected(false);
			ingredientNames.add(temp);
		}

		return ingredientNames;
	}
}
