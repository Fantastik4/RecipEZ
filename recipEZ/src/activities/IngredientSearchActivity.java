package activities;

import java.util.ArrayList;

import com.fantastik4.recipez.R;
import com.fantastik4.recipez.R.anim;
import com.fantastik4.recipez.R.id;
import com.fantastik4.recipez.R.layout;

import android.app.Activity;
import android.content.Intent;
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
import objects.CheckedListViewAdapter;
import objects.Ingredient;
import objects.Model;
import objects.Recipe;
import objects.SimpleListAdapter;
import resources.IngredientProvider;
import resources.RecipeProvider;

public class IngredientSearchActivity extends Activity {
	
	SearchView searchField;
	Button findRecipes, addToFridge;
	String currentUsername;
	ArrayList<Ingredient> ingredients;
	IngredientProvider ingredientProvider;
	CheckedListViewAdapter ingredientListAdapter;
	ArrayList<Model> displayedIngredients, ingredientNames;
	ListView listView;

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
		
		//Initializing Buttons
		findRecipes = (Button) findViewById(R.id.GetRecipeButton);
		findRecipes.setOnClickListener(new OnClickListener(){
			 
			public void onClick(View arg0) {
				ArrayList<Recipe> recipeResults = GetRecipeResults();
				Intent i = new Intent(IngredientSearchActivity.this, RecipeResultsActivity.class);
				i.putExtra("RecipeResults", recipeResults);
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
		RecipeProvider recipeProvider = new RecipeProvider();
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

	private void UpdateDisplayedIngredients(String searchArg) {
		displayedIngredients.clear();
		for(Model m: ingredientNames) {
			if(m.getName().toLowerCase().contains(searchArg.toLowerCase())){
				displayedIngredients.add(m);
			}
		}
	}

	private void PopulateIngredientList() {
		listView = (ListView)findViewById(R.id.lv_ingredientsList);		

		ingredients = ingredientProvider.FetchAllIngredients();
		ingredientNames = ConvertIngredientsToModelArray(ingredients);
		displayedIngredients = (ArrayList<Model>) ingredientNames.clone();
		ingredientListAdapter = new CheckedListViewAdapter(this, displayedIngredients);

		listView.setAdapter(ingredientListAdapter);

		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
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