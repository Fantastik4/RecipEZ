package activities;

import objects.Model;
import objects.Recipe;
import android.os.Bundle;
import android.view.View;
import objects.Ingredient;
import java.util.ArrayList;
import android.app.Activity;
import android.widget.Button;
import android.content.Intent;
import android.widget.ListView;
import com.fantastik4.recipez.R;
import resources.RecipeResourceProvider;
import android.widget.SearchView;
import resources.IngredientProvider;
import objects.CheckedListViewAdapter;
import android.annotation.SuppressLint;
import android.view.View.OnClickListener;
import android.widget.SearchView.OnQueryTextListener;

public class IngredientSearchActivity extends Activity {

	ListView listView;
	SearchView searchField;
	String currentUsername;
	Button findRecipes, addToFridge;
	ArrayList<Ingredient> ingredients;
	IngredientProvider ingredientProvider;
	CheckedListViewAdapter ingredientListAdapter;
	ArrayList<Model> displayedIngredients, ingredientNames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		/**
		 * LABLES:	Search Text Field - sv_searchIngredients
		 * 			Ingredients List - lv_ingredientsList
		 */
		ingredientProvider = new IngredientProvider();
		currentUsername = (String) getIntent().getSerializableExtra("CurrentUsername");

		//Initializing SearchField On Query Text Listener
		searchField = (SearchView) findViewById(R.id.sv_searchIngredients);
		searchField.setSelected(true);
		searchField.setOnQueryTextListener(new OnQueryTextListener(){
			
			@Override
			public boolean onQueryTextChange(String searchArg) {

				if (searchArg.length() == 0)
					listView.setVisibility(View.INVISIBLE);
				else
					listView.setVisibility(View.VISIBLE);

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

		//Initializing Buttons
		findRecipes = (Button) findViewById(R.id.GetRecipeButton);
		findRecipes.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				ArrayList<Recipe> recipeResults = GetRecipeResults();
				Intent i = new Intent(IngredientSearchActivity.this, RecipeResultsActivity.class);
				i.putExtra("RecipeResults", recipeResults);
				i.putExtra("username", currentUsername);
				startActivity(i);

				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
		});

		addToFridge = (Button) findViewById(R.id.AddToFridgeButton);
		addToFridge.setOnClickListener(new OnClickListener(){
			public void onClick(View view)
			{
				AddIngredientsToFridge();
			}
		});

		PopulateIngredientList();
	}

	private void AddIngredientsToFridge()
	{
		for(int i = 0; i < ingredientNames.size(); i++)
		{
			if(ingredientNames.get(i).isSelected())
			{
				ingredientProvider.AddIngredientToUser(currentUsername, ingredients.get(i).getIngredientID());
			}
		}
	}

	private ArrayList<Recipe> GetRecipeResults() {
		RecipeResourceProvider recipeProvider = new RecipeResourceProvider();
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		ArrayList<Recipe> recipeResults = null;
		for(int i = 0; i < ingredientNames.size(); i++)
		{
			if(ingredientNames.get(i).isSelected()) 
			{
				recipes = recipeProvider.FetchRecipesByIngredientID(ingredients.get(i).getIngredientID());
				if(recipeResults == null) recipeResults = recipes;
				else recipeResults = IntersectionOfRecipeLists(recipeResults, recipes);
			}
		}
		return recipeResults;
	}

	private ArrayList<Recipe> IntersectionOfRecipeLists(ArrayList<Recipe> list1, ArrayList<Recipe> list2) {
		ArrayList<Recipe> list = new ArrayList<Recipe>();

		for (Recipe r : list1) {
			if(ListContainsRecipe(list2, r)) {
				list.add(r);
			}
		}

		return list;
	}

	private boolean ListContainsRecipe(ArrayList<Recipe> list2, Recipe recipe) {
		for(Recipe r: list2)
		{
			if(r.getRecipeID().equals(recipe.getRecipeID()))return true;
		}
		return false;
	}

	@SuppressLint("DefaultLocale")
	private void UpdateDisplayedIngredients(String searchArg) {
		displayedIngredients.clear();
		for(Model m: ingredientNames) {
			if(m.getName().toLowerCase().contains(searchArg.toLowerCase())){
				displayedIngredients.add(m);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void PopulateIngredientList() {
		listView = (ListView)findViewById(R.id.lv_ingredientsList);		

		ingredients = ingredientProvider.FetchAllIngredients();
		ingredientNames = ConvertIngredientsToModelArray(ingredients);
		displayedIngredients = (ArrayList<Model>) ingredientNames.clone();
		ingredientListAdapter = new CheckedListViewAdapter(this, displayedIngredients);

		listView.setAdapter(ingredientListAdapter);

		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setVisibility(View.INVISIBLE);
	}

	private ArrayList<Model> ConvertIngredientsToModelArray(ArrayList<Ingredient> ingredients) {
		ArrayList<Model> ingredientNames = new ArrayList<Model>();
		Model temp;
		for(Ingredient i: ingredients) {
			temp = new Model(i.getName());
			temp.setSelected(false);
			ingredientNames.add(temp);
		}

		return ingredientNames;
	}
}