package activities;

import objects.Model;
import objects.Recipe;
import android.os.Bundle;
import android.view.View;
import objects.Ingredient;
import java.util.ArrayList;
import android.app.Activity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.media.Image;
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
	ImageView reset, findRecipes, addToFridge;
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
			public boolean onQueryTextChange(String searchArg) 
			{
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
		findRecipes = (ImageView) findViewById(R.id.edit_list_get_recipes);
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

		addToFridge = (ImageView) findViewById(R.id.AddToFridgeButton);
		addToFridge.setOnClickListener(new OnClickListener(){
			public void onClick(View view)
			{
				AddIngredientsToFridge();
			}
		});
		
		reset = (ImageView) findViewById(R.id.ingredientListResetButton);
		reset.setOnClickListener(new OnClickListener(){
			public void onClick(View view)
			{
				ResetIngredientList();
				UpdateDisplayedIngredients("");
				ingredientListAdapter.notifyDataSetChanged();
			}
		});
		PopulateIngredientList();
	}
	
	public void ResetIngredientList()
	{
		for(Model m: ingredientNames)
		{
			m.setSelected(false);
		}
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
	private void UpdateDisplayedIngredients(String searchArg) 
	{
		displayedIngredients.clear();
		AddSelectedIngredientsToDisplayList();
		if(searchArg.length() <= 0) return;

		for(Model m: ingredientNames)
		{
			if(m.getName().toLowerCase().contains(searchArg.toLowerCase()))
			{
				if(!ModelListContainsString(displayedIngredients, m.getName())) displayedIngredients.add(m);
			}
		}
	}
	
	private void AddSelectedIngredientsToDisplayList()
	{
		for(Model m: ingredientNames) 
		{
			if(m.isSelected()) displayedIngredients.add(m);
		}
	}
	
	private boolean ModelListContainsString(ArrayList<Model> list, String name)
	{
		for(Model m: list)
		{
			if(m.getName().equals(name)) return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void PopulateIngredientList() 
	{
		listView = (ListView)findViewById(R.id.lv_ingredientsList);		

		ingredients = ingredientProvider.FetchAllIngredients();
		ingredientNames = ConvertIngredientsToModelArray(ingredients);
		displayedIngredients = new ArrayList<Model>();
		AddSelectedIngredientsToDisplayList();
		
		ingredientListAdapter = new CheckedListViewAdapter(this, displayedIngredients);

		listView.setAdapter(ingredientListAdapter);

		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setVisibility(View.VISIBLE);

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