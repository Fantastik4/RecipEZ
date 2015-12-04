package activities;

import objects.Model;
import objects.Recipe;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import objects.Ingredient;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import com.fantastik4.recipez.R;


import android.widget.SearchView;
import resources.IngredientProvider;
import resources.RecipeResourceProvider;
import objects.CheckedListViewAdapter;

public class EditListActivity extends Activity {

	
	String userID;
	ListView listView;
	SearchView searchField;
	Button findRecipes;
	ImageView trash;
	ArrayList<Ingredient> ingredients;
	IngredientProvider ingredientProvider;
	CheckedListViewAdapter ingredientListAdapter;
	ArrayList<Model> displayedIngredients;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_list);
		
		userID = (String) getIntent().getSerializableExtra("CurrentUsername");
		ingredientProvider = new IngredientProvider();
		PopulateIngredientList();
	}
	
	@SuppressWarnings("unchecked")
	private void PopulateIngredientList() {
		listView = (ListView)findViewById(R.id.lv_userList);
		findRecipes = (Button) findViewById(R.id.edit_list_get_recipes);
		trash = (ImageView) findViewById(R.id.edit_list_trash);
		
		ingredients = ingredientProvider.FetchIngredientsByUserID(userID);
		displayedIngredients = ConvertIngredientsToModelArray(ingredients);
		ingredientListAdapter = new CheckedListViewAdapter(this, displayedIngredients);

		listView.setAdapter(ingredientListAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		trash.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View trashView) 
			{
				for(int i = 0; i < displayedIngredients.size(); i++)
				{
					if(displayedIngredients.get(i).isSelected())
						{
							ingredientProvider.RemoveIngredientFromUser(userID, ingredients.get(i).getIngredientID());
						}
				}
				PopulateIngredientList();
			}			
		});
		
		findRecipes.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ArrayList<Recipe> recipeResults = GetRecipeResults();
				Intent i = new Intent(EditListActivity.this, RecipeResultsActivity.class);
				i.putExtra("RecipeResults", recipeResults);
				i.putExtra("username", userID);
				startActivity(i);

				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
			
		});
	}
	
	private ArrayList<Recipe> GetRecipeResults() {
		RecipeResourceProvider recipeProvider = new RecipeResourceProvider();
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		ArrayList<Recipe> recipeResults = null;
		for(int i = 0; i < displayedIngredients.size(); i++)
		{
			if(displayedIngredients.get(i).isSelected()) 
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
