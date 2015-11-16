package com.fantastik4.recipez.test;

import java.util.ArrayList;

import android.test.ActivityUnitTestCase;
import objects.Recipe;
import resources.RecipeProvider;

public class RecipeProviderTests extends ActivityUnitTestCase{

	private RecipeProvider recipeProvider;
	
	public RecipeProviderTests() {
		super(activities.RecipeResultsActivity.class);
		// TODO Auto-generated constructor stub
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		recipeProvider = new RecipeProvider();

	}
	
	public void testFetchFavoriteRecipes()
	{
		ArrayList<Recipe> recipes = recipeProvider.FetchFavoriteRecipes("brianchan");
		assertFalse("No recipes returned",recipes.isEmpty());
	}

}
